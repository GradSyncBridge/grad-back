package backend.model.converter;

import backend.model.DTO.StudentTableDTO;
import backend.model.VO.student.*;
import backend.model.entity.Major;
import backend.model.entity.QualityFile;
import backend.model.entity.Student;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StudentConverter {
    StudentConverter INSTANCE = Mappers.getMapper(StudentConverter.class);

    @Mapping(target = "examId", source = "studentTableDTO.examID")
    @Mapping(target = "certifyId", source = "studentTableDTO.certifyID")
    @Mapping(target = "majorStudy", source = "majorStudy")
    @Mapping(target = "birth", source = "studentTableDTO.birthday")
    @Mapping(target = "quality", source = "quality")
    Student StudentTableDTOToStudent(StudentTableDTO studentTableDTO, String majorStudy, String quality);

    @Mapping(target = "majorID", source = "id")
    @Mapping(target = "majorNum", source = "mid")
    MajorSubject majorToMajorSubject(Major major);

    @Mapping(target = "fileID", source = "id")
    @Mapping(target = "filePath", source = "file")
    Quality qualityFileToQuality(QualityFile qualityFile);

    @Mapping(target = "examID", source = "student.examId")
    @Mapping(target = "certifyID", source = "student.certifyId")
    @Mapping(target = "majorApply", source = "majorApply")
    @Mapping(target = "majorStudy", source = "majorStudyList")
    @Mapping(target = "quality", source = "qualityList")
    @Mapping(target = "gradeFirst", source = "gradeFirst")
    @Mapping(target = "gradeSecond", source = "gradeSecond")
    @Mapping(target = "application", source = "application")
    StudentSubmitTableVO StudentToSubmitTable(Student student, Score gradeFirst, Score gradeSecond, List<Application> application, List<Quality> qualityList, MajorSubject majorApply, List<MajorSubject> majorStudyList);
}


