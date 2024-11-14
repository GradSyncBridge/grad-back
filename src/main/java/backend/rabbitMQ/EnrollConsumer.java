package backend.rabbitMQ;

import backend.mapper.StudentMapper;
import backend.mapper.TeacherMapper;
import backend.model.entity.Student;
import backend.model.entity.Teacher;
import backend.util.FieldsGenerator;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("mq")
public class EnrollConsumer {

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private TeacherMapper teacherMapper;

    @RabbitListener(queues = "enroll-queue")
    public String process(String message) {
        String[] split = message.split(" ");
        Integer studentID = Integer.parseInt(split[0]);
        Integer teacherID = Integer.parseInt(split[1]);

        Student student = studentMapper.selectStudent(Student.builder().userId(studentID).build(), FieldsGenerator.generateFields(Student.class)).getFirst();

        if (student.getEnrollment() != null)
            return null;

        Teacher teacher = teacherMapper.selectTeacher(Teacher.builder().userId(teacherID).build(), FieldsGenerator.generateFields(Teacher.class)).getFirst();
        if (teacher.getTotal() < 1)
            return null;

        studentMapper.updateStudent(Student.builder().userId(studentID).enrollment(teacher.getUserId()).build(), Student.builder().userId(studentID).build());
        teacherMapper.updateTeacher(Teacher.builder().userId(teacher.getUserId()).total(teacher.getTotal() - 1).build(), Teacher.builder().userId(teacher.getUserId()).build());

        return "success";
    }
}
