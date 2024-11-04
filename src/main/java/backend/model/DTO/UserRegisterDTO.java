package backend.model.DTO;

import backend.annotation.DTO.UserRegisterDTOValidation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@UserRegisterDTOValidation
public class UserRegisterDTO {
    private String username;

    private String password;

    private Integer gender;

    private Integer role;
}
