package backend.model.converter;

import backend.model.DTO.MajorDTO;
import backend.model.VO.major.MajorVO;
import backend.model.VO.major.SubMajorVO;
import backend.model.entity.Major;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface MajorConverter {

    MajorConverter INSTANCE = Mappers.getMapper(MajorConverter.class);

    MajorVO MajorToMajorVO(Major major);

    default MajorDTO MajorToMajorDTO(Major major){
        MajorDTO majorDTO =  MajorDTO.builder()
                                .id(major.getId())
                                .name(major.getName())
                                .mid(major.getMid())
                                .pid(major.getPid())
                                .type(major.getType())
                                .total(major.getTotal())
                                .addition(major.getAddition())
                                .description(major.getDescription())
                                .department(major.getDepartment())
                                .year(major.getYear())
                                .recommend(major.getRecommend())
                                .disabled(major.getDisabled())
                                .allowReassign(major.getAllowReassign())
                                .build();

        String[] firstArr = major.getInitial().substring(1, major.getInitial().length() - 1).split(",");
        String[] secondArr = major.getInterview().substring(1, major.getInterview().length() - 1).split(",");

        List<Integer> firstList = Arrays.stream(firstArr)
                .map(s -> Integer.parseInt(s.trim()))
                .collect(Collectors.toList());

        List<Integer> secondList = Arrays.stream(secondArr)
                .map(s -> Integer.parseInt(s.trim()))
                .collect(Collectors.toList());

        majorDTO.setInitial(firstList);
        majorDTO.setInterview(secondList);

        return majorDTO;
    }

    SubMajorVO MajorToSubMajorVO(Major major, List<Map<Integer, String>> initials, List<String> interviews);

    List<MajorVO> MajorListToMajorVOList(List<Major> majorList);
}
