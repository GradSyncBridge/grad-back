package backend.model.DTO;

import backend.annotation.DTO.StudentSubmitDTOValidation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@StudentSubmitDTOValidation
public class StudentSubmitDTO {
    private String birth;

    private String examID;

    private String certifyID;

    private String emergency;

    private String address;

    private String majorGrad;

    private Integer majorApply;

    private String school;

    private String type;

    private List<Integer> quality;

    private Integer enrollment;
}
