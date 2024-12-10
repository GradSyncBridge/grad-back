package backend.model.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentApplicationSubmitDTO {
    private List<Integer> application;

    private List<Integer> majorStudy;

    private Integer reassign;
}
