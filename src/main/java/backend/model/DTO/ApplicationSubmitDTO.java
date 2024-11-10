package backend.model.DTO;


import backend.annotation.DTO.StudentTableDTOValidation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@StudentTableDTOValidation
public class ApplicationSubmitDTO {
    private List<Integer> application;
}
