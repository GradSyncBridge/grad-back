package backend.model.DTO;

import backend.annotation.DTO.UserProfileUpdateDTOValidation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@UserProfileUpdateDTOValidation
public class UserProfileUpdateDTO {
    private Integer uid;
    private String username;
    private String avatar;
    private String name;
    private String email;
    private Integer gender;
    private String phone;
}
