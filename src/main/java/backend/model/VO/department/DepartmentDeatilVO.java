package backend.model.VO.department;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DepartmentDeatilVO {

    private String description;

    private String imageUrl;

    private Integer totalMajor;

}
