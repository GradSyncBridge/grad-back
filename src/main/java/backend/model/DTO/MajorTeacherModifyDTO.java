package backend.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MajorTeacherModifyDTO {
    private Integer majorTeacherID;
    private Integer valid;
    private Integer total;
}
