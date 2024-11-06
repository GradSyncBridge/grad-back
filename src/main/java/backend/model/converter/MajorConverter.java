package backend.model.converter;

import backend.model.VO.major.MajorVO;
import backend.model.VO.major.SubMajorSubject;
import backend.model.VO.major.SubMajorVO;
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
    SubMajorVO MajorToSubMajorVO(Major major, List<SubMajorSubject> initials, List<SubMajorSubject> interviews);

    List<MajorVO> MajorListToMajorVOList(List<Major> majorList);
}
