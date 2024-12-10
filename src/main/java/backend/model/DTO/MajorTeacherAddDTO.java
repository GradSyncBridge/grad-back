package backend.model.DTO;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MajorTeacherAddDTO {
    private Integer majorID;
    private Integer teacherID;
    private Integer total;
}
