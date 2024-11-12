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
    private String username;
    private String avatar;
    private String name;
    private String email;
    private Integer gender;
    private String phone;
    private String description;
}
