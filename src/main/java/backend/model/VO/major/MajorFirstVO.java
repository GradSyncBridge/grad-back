package backend.model.VO.major;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MajorFirstVO implements Serializable {
    private Integer majorID;

    private String name;

    private String majorNum;

    private String description;

    private Integer type;

    private Integer total;

    private Integer addition;

    private Integer recommend;

    private Integer year;

    private Integer department;
}
