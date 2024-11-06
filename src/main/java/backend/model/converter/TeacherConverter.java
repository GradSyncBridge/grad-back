package backend.model.converter;

import backend.model.VO.major.TeacherInMajorVO;
import backend.model.VO.teacher.TeacherVO;
import backend.model.entity.Teacher;
import backend.model.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface TeacherConverter {
    TeacherConverter INSTANCE = Mappers.getMapper(TeacherConverter.class);

    TeacherInMajorVO TeacherToTeacherInMajorVO(Teacher teacher);

    @Mapping(target = "uid", source = "userId")
    TeacherVO TeacherToTeacherVO(Teacher teacher);

    TeacherVO UserToTeacherVO(User user);

    @Mapping(target = "uid", source = "user.id")
    @Mapping(target = "username", source = "user.username")
    @Mapping(target = "name", source = "user.name")
    @Mapping(target = "email", source = "user.email")
    @Mapping(target = "avatar", source = "user.avatar")
    @Mapping(target = "gender", source = "user.gender")
    @Mapping(target = "phone", source = "user.phone")
    @Mapping(target = "department", source = "teacher.department")
    @Mapping(target = "title", source = "teacher.title")
    @Mapping(target = "description", source = "teacher.description")
    TeacherVO TeacherAndUserToTeacherVO(Teacher teacher, User user);

    default ArrayList<TeacherVO> TeacherListToTeacherVOList(List<Teacher> teachers, List<User> users){
        ArrayList<TeacherVO> teacherVOS = new ArrayList<>();

        for(int i = 0; i < teachers.size(); i++){
           teacherVOS.add(TeacherAndUserToTeacherVO(teachers.get(i), users.get(i)));
        }
        return teacherVOS;
    }
}
