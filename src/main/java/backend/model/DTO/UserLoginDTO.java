package backend.model.DTO;

import backend.annotation.DTO.UserLoginDTOValidation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@UserLoginDTOValidation
public class UserLoginDTO {

    private String username;

    private String password;
}
