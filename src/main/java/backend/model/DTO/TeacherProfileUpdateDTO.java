package backend.model.DTO;

import backend.annotation.DTO.TeacherProfileUpdateDTOValidation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TeacherProfileUpdateDTOValidation
public class TeacherProfileUpdateDTO {
    private Integer title;
    private String description;
    private Integer total;
    private Integer remnant;
    private Integer identity;
}
