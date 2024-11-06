package backend.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentTableDTO {
    private LocalDateTime birth;

    private String examID;

    private String certifyID;

    private String emergency;

    private String address;

    private String majorGrad;

    private int majorApply;

    private List<Integer> majorStudy;

    private String school;

    private String type;

    private List<Integer> quality;

    private Integer enrollment;

    private Boolean reassign;
}
