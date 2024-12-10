package backend.model.VO.department;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DepartmentDetailVO {

    private Integer departmentID;

    private String  departmentNum;

    private String name;

    private String description;

    private String logo;

    private Integer totalMajor;

}
