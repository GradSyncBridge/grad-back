package backend.service.impl;

import backend.util.GlobalConfig;
import backend.mapper.*;

import backend.model.DTO.StudentApplicationSubmitDTO;
import backend.model.DTO.StudentGradeModifyDTO;
import backend.model.DTO.StudentGradeSubmitDTO;
import backend.model.DTO.StudentSubmitDTO;

import backend.model.VO.student.*;
import backend.model.VO.teacher.TeacherVO;
import backend.model.VO.user.UserProfileVO;

import backend.model.converter.StudentConverter;
import backend.model.converter.UserConverter;

import backend.model.entity.*;

import backend.service.StudentService;

import backend.enums.DeadlineEnum;
import backend.exception.model.deadline.DeadlineExceedException;
import backend.exception.model.deadline.DeadlineNotFoundException;

import backend.util.FieldsGenerator;
import backend.util.StringToList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private StudentConverter studentConverter;

    @Autowired
    private StudentGradeMapper studentGradeMapper;

    @Autowired
    private QualityFileMapper qualityFileMapper;

    @Autowired
    private MajorMapper majorMapper;

    @Autowired
    private StudentApplyMapper studentApplyMapper;

    @Autowired
    private DeadlineMapper deadlineMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserConverter userConverter;

    @Autowired
    private SubjectMapper subjectMapper;

    private void verifyDeadline(DeadlineEnum type) {
        List<Deadline> deadlines = deadlineMapper.selectDeadline(Deadline.builder().type(type.getValue()).build(),
                FieldsGenerator.generateFields(Deadline.class));
        Deadline targetDeadline = deadlines.isEmpty() ? null : deadlines.getFirst();

        if (targetDeadline == null)
            throw new DeadlineNotFoundException();

        if (LocalDateTime.now().isAfter(targetDeadline.getTime()))
            throw new DeadlineExceedException();

    }

    /**
     * 获取学生提交信息
     * GET /student
     * @param targetUid 目标用户ID
     * @return 学生提交表
     */
    @Override
    public StudentSubmitTableVO getStudentSubmitTable(Integer targetUid) {
        User user = User.getAuth();
        Student student;

        if (user.getRole() == 1)
            student = User.getAuth().getStudent();
        else {
            List<Student> students = studentMapper.selectStudent(
                    Student.builder().userId(targetUid).build(),
                    FieldsGenerator.generateFields(Student.class)
            );
            student = students.isEmpty() ? null : students.getFirst();
        }

        if (student == null || student.getQuality() == null)
            return null;

        try {
            List<Integer> majorStudy;
            if (student.getMajorStudy() == null)
                majorStudy = new ArrayList<>();
            else majorStudy = StringToList.convert(student.getMajorStudy());

            List<Quality> fileList = StringToList.convert(student.getQuality())
                    .stream()
                    .map(f -> {
                        List<QualityFile> files = qualityFileMapper.selectQualityFile(
                                QualityFile.builder().id(f).build(), FieldsGenerator.generateFields(QualityFile.class));
                        return files.isEmpty() ? null : files.getFirst();
                    })
                    .filter(Objects::nonNull)
                    .map(f -> studentConverter.INSTANCE.qualityFileToQuality(f))
                    .toList();

            // Major apply
            CompletableFuture<MajorSubject> majorListFuture = CompletableFuture.supplyAsync(() -> {
                List<Major> majorList = majorMapper.selectMajor(Major.builder().id(student.getMajorApply()).build(),
                        FieldsGenerator.generateFields(Major.class));
                return majorList.isEmpty() ? null
                        : studentConverter.INSTANCE.majorToMajorSubject(majorList.getFirst());
            });

            // Major study
            ArrayList<CompletableFuture<CompletableFuture<MajorSubject>>> majorStudyListFuture = new ArrayList<>();
            for(Integer id: majorStudy){
                CompletableFuture<CompletableFuture<MajorSubject>> majorStudyFuture =
                        CompletableFuture.supplyAsync(()-> asyncGetMajorSubject(id)).thenApply(v->v);
                majorStudyListFuture.add(majorStudyFuture);
            }

            // application
            CompletableFuture<List<TeacherVO>> applicationsFuture = CompletableFuture.supplyAsync(() -> studentApplyMapper
                    .selectApplicationWithTeacher(StudentApply.builder().userId(student.getUserId()).build()));

            // grade
            CompletableFuture<Score> gradeFirstFuture = CompletableFuture.supplyAsync(() -> {
                List<GradeList> gradeListFirst = studentGradeMapper
                        .selectGradeWithSubject(StudentGrade.builder().userId(student.getUserId()).disabled(1).build(), 0);
                return Score.builder().gradeTotal(student.getGradeFirst()).gradeList(gradeListFirst).build();
            });

            // grade
            CompletableFuture<Score> gradeSecondFuture = CompletableFuture.supplyAsync(() -> {
                List<GradeList> gradeListSecond = studentGradeMapper
                        .selectGradeWithSubject(StudentGrade.builder().userId(student.getUserId()).disabled(1).build(), 1);
                return Score.builder().gradeTotal(student.getGradeSecond()).gradeList(gradeListSecond).build();
            });

            // majorStudy groupBy
            CompletableFuture.allOf(majorStudyListFuture.toArray(new CompletableFuture[0])).join();
            List<MajorSubject> majorSubjectList = majorStudyListFuture.stream()
                    .map(CompletableFuture::join)
                    .map(CompletableFuture::join)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            return studentConverter.INSTANCE.StudentToSubmitTable(student,
                    gradeFirstFuture.join(), gradeSecondFuture.join(),
                    applicationsFuture.join(), fileList,
                    majorListFuture.join(),
                    majorSubjectList);

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 搜索学生
     * GET /student/search
     * @param key 关键词
     * @param valid 用户是否有效
     * @return 学生列表
     */
    @Override
    public List<UserProfileVO> searchStudent(String key, Integer valid) {
        try {
            User query = User.builder().build();

            if (key != null && !key.isEmpty()) {
                if (key.matches(GlobalConfig.uidPattern))
                    query.setId(Integer.parseInt(key));

                else if (key.matches(GlobalConfig.emailPattern))
                    query.setEmail(key);
                else
                    query.setName(key);
            }

            ArrayList<CompletableFuture<UserProfileVO>> userListFuture = new ArrayList<>();
            List<User> userList = userMapper.searchStudent(query, valid);

            for(User u: userList){
                CompletableFuture<UserProfileVO> userCompletableFuture = CompletableFuture.supplyAsync(()->
                        userConverter.INSTANCE.UserToUserProfileVO(u)
                ).thenApply(user->user);

                userListFuture.add(userCompletableFuture);
            }

            CompletableFuture.allOf(userListFuture.toArray(new CompletableFuture[0])).join();

            return userListFuture.stream()
                    .map(CompletableFuture::join)
                    .toList();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 学生提交/修改报名表单
     * POST /student
     * PUT /student
     * @param submitDTO 提交信息
     */
    @Override
    public void studentSubmit(StudentSubmitDTO submitDTO) {
        DeadlineEnum type = DeadlineEnum.INITIAL_SUBMISSION;

        try {
            verifyDeadline(type);

            // Accommodate for LocalDateTime
            submitDTO.setBirth(String.format("%sT00:00:00", submitDTO.getBirth()));
            studentMapper.updateStudent(
                    studentConverter.INSTANCE.StudentSubmitDTOToStudent(
                            submitDTO,
                            submitDTO.getQuality().toString()
                    ),
                    Student.builder().userId(User.getAuth().getId()).build()
            );

        } catch (DeadlineNotFoundException deadlineNotFoundException) {
            throw new DeadlineNotFoundException(type.getValue());
        } catch (DeadlineExceedException deadlineExceedException) {
            throw new DeadlineExceedException(type, 403);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 学生/教师提交成绩信息
     * /student/grade
     * @param studentGrade 学生成绩
     */
    @Override
    public void studentGradeSubmit(StudentGradeSubmitDTO studentGrade) {
        Integer role = User.getAuth().getRole();

        DeadlineEnum type = role == 1 ? DeadlineEnum.INITIAL_SUBMISSION : DeadlineEnum.SECOND_SUBMISSION;
        Integer studentID = role == 1 ? User.getAuth().getId() : studentGrade.getStudentID();

        Student student = role == 1 ?
                User.getAuth().getStudent() :
                studentMapper.selectStudent(
                        Student.builder().userId(studentID).build(),
                        FieldsGenerator.generateFields(Student.class)
                ).getFirst();

        try {
            verifyDeadline(type);

            studentGrade
                    .getGrades()
                    .forEach(g ->
                            CompletableFuture.runAsync(()-> {
                                        if(!subjectMapper.selectSubject(Subject.builder().id(g.getSubjectID()).build(),
                                                FieldsGenerator.generateFields(Subject.class)).isEmpty()) {
                                            studentGradeMapper.insertStudentGrade(
                                                    StudentGrade
                                                            .builder().grade(g.getGrade()).sid(g.getSubjectID())
                                                            .disabled(1).userId(studentID).build()
                                            );
                                        }
                                    }
                            )
                    );

            Double totalGrade = studentGrade
                    .getGrades()
                    .stream()
                    .mapToDouble(StudentGradeSubmitDTO.GradeSingleDTO::getGrade)
                    .sum();
            if (role == 1)
                student.setGradeFirst(totalGrade);
            else
                student.setGradeSecond(totalGrade);

            studentMapper.updateStudent(student, Student.builder().userId(studentID).build());

        } catch (DeadlineNotFoundException deadlineNotFoundException) {
            throw new DeadlineNotFoundException(type.getValue());
        } catch (DeadlineExceedException deadlineExceedException) {
            throw new DeadlineExceedException(type, role == 1 ? 4031 : 4032);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 学生/教师修改成绩信息
     * PUT /student/grade
     * @param studentGrade 学生成绩
     */
    @Override
    public void studentGradeModify(StudentGradeModifyDTO studentGrade) {
        Integer role = User.getAuth().getRole();

        DeadlineEnum type = role == 1 ? DeadlineEnum.INITIAL_SUBMISSION : DeadlineEnum.SECOND_SUBMISSION;
        Integer studentID = role == 1 ? User.getAuth().getId() : studentGrade.getStudentID();

        Student student = role == 1 ?
                User.getAuth().getStudent() :
                studentMapper.selectStudent(
                        Student.builder().userId(studentID).build(),
                        FieldsGenerator.generateFields(Student.class)
                ).getFirst();

        try {
            verifyDeadline(type);

            studentGrade
                    .getGrades()
                    .forEach(g ->
                            CompletableFuture.runAsync(()->
                                studentGradeMapper.updateStudentGrade(
                                        StudentGrade.builder().grade(g.getGrade()).build(),
                                        StudentGrade.builder().id(g.getGradeID()).build()
                                )
                            )
                    );

            Double totalGrade = studentGrade
                    .getGrades()
                    .stream()
                    .mapToDouble(StudentGradeModifyDTO.GradeSingleDTO::getGrade)
                    .sum();
            if (role == 1)
                student.setGradeFirst(totalGrade);
            else
                student.setGradeSecond(totalGrade);
            studentMapper.updateStudent(student, Student.builder().userId(studentID).build());

        } catch (DeadlineNotFoundException deadlineNotFoundException) {
            throw new DeadlineNotFoundException(type.getValue());
        } catch (DeadlineExceedException deadlineExceedException) {
            throw new DeadlineExceedException(type, role == 1 ? 4031 : 4032);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 学生提交志愿信息
     * POST /student/apply
     * @param studentApplication 学生志愿信息
     */
    @Override
    public void studentApplicationSubmit(StudentApplicationSubmitDTO studentApplication) {
        DeadlineEnum type = DeadlineEnum.SECOND_SUBMISSION;

        Student student = User.getAuth().getStudent();
        Integer uid = student.getUserId();

        try {
            verifyDeadline(type);

            if (!studentApplyMapper.selectStudentApply(
                    StudentApply.builder().userId(uid).disabled(1).build(),
                    Map.of("id", true)).isEmpty()
            )
                return;

            List<Integer> applications = studentApplication.getApplication();
            for (int i = 0; i < applications.size(); i++) {
                StudentApply studentApply = StudentApply.builder().tid(applications.get(i)).level(i + 1).userId(uid).disabled(1).build();
                CompletableFuture.runAsync(()-> studentApplyMapper.insertStudentApply(studentApply));
            }

            student.setMajorStudy(studentApplication.getApplication().toString());
            student.setReassign(studentApplication.getReassign());

            studentMapper.updateStudent(student, Student.builder().userId(uid).build());

        } catch (DeadlineNotFoundException deadlineNotFoundException) {
            throw new DeadlineNotFoundException(type.getValue());
        } catch (DeadlineExceedException deadlineExceedException) {
            throw new DeadlineExceedException(type, 403);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Async
    public CompletableFuture<MajorSubject> asyncGetMajorSubject(Integer id){
        List<Major> majors = majorMapper.selectMajor(Major.builder().id(id).build(),
                FieldsGenerator.generateFields(Major.class));
        return (Objects.requireNonNull(majors.isEmpty() ? null
                : CompletableFuture.completedFuture(studentConverter.INSTANCE.majorToMajorSubject(majors.getFirst()))))
                .thenApply(v->v);
    }
}
