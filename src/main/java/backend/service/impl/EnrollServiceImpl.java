package backend.service.impl;

import backend.mapper.DepartmentMapper;
import backend.mapper.EnrollMapper;
import backend.mapper.MajorMapper;
import backend.mapper.UserMapper;
import backend.model.VO.enroll.EnrollVO;
import backend.model.converter.EnrollConverter;
import backend.model.entity.Department;
import backend.model.entity.Enroll;
import backend.model.entity.Major;
import backend.model.entity.User;
import backend.service.EnrollService;
import backend.util.FieldsGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class EnrollServiceImpl implements EnrollService {
    @Autowired
    private EnrollMapper enrollMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MajorMapper majorMapper;

    @Autowired
    private DepartmentMapper departmentMapper;

    @Autowired
    private EnrollConverter enrollConverter;

    @Override
    public List<EnrollVO> getEnrollTable(Integer departmentId, Integer year) {
        List<Enroll> enrolls = enrollMapper.selectEnrollWithDept(departmentId, year);

        try {
            return enrolls
                    .stream()
                    .map(e -> {
                        List<User> students = userMapper.selectUser(User.builder().id(e.getSid()).build(),
                                FieldsGenerator.generateFields(User.class));
                        User student = students.isEmpty() ? null : students.getFirst();

                        List<User> teachers = userMapper.selectUser(User.builder().id(e.getTid()).build(),
                                Map.of("name", true));
                        User teacher = teachers.isEmpty() ? null : teachers.getFirst();

                        List<Department> departments = departmentMapper.selectDepartment(
                                Department.builder().id(departmentId).build(),
                                FieldsGenerator.generateFields(Department.class));
                        Department dept = departments.isEmpty() ? null : departments.getFirst();

                        List<Major> majors = majorMapper.selectMajor(Major.builder().id(e.getMid()).build(),
                                FieldsGenerator.generateFields(Major.class));
                        Major major = majors.isEmpty() ? null : majors.getFirst();

                        if (student == null || teacher == null || dept == null || major == null)
                            return null;
                        return enrollConverter.INSTANCE.MultiAttributesToEnrollVO(student, teacher, dept, major,
                                e.getId());
                    })
                    .filter(Objects::nonNull).toList();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }
}
