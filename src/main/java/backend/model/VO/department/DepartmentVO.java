package backend.model.VO.department;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentVO {

    private Integer departmentID;

    private String  departmentNum;

    private String name;
}
