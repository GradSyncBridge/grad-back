package backend.model.converter;

import backend.model.VO.major.*;
import backend.model.VO.subject.SubjectVO;
import backend.model.entity.Major;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MajorConverter {

    MajorConverter INSTANCE = Mappers.getMapper(MajorConverter.class);

    @Mapping(target = "majorID", source = "major.id")
    @Mapping(target = "majorNum", source = "major.mid")
    MajorVO MajorToMajorVO(Major major);

    @Mapping(target = "majorID", source = "major.id")
    @Mapping(target = "majorNum", source = "major.mid")
    SubMajorVO MajorToSubMajorVO(Major major, List<SubjectVO> initials, List<SubjectVO> interviews);

    List<MajorVO> MajorListToMajorVOList(List<Major> majorList);

    // Newer Interfaces
    @Mapping(target = "majorID", source = "id")
    @Mapping(target = "majorNum", source = "mid")
    @Mapping(target = "reassign", source = "allowReassign")
    MajorFirstVO MajorToMajorFirstVO(Major major);

    @Mapping(target = "majorID", source = "major.id")
    @Mapping(target = "majorNum", source = "major.mid")
    MajorSecondVO MajorSubjectToMajorSecondVO(Major major, List<SubjectVO> initials, List<SubjectVO> interviews);
}
