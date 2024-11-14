package backend.model.VO.major;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubMajorSubject implements Serializable {

    private Integer subjectID;

    private String subjectNum;

    private String name;

    private Integer type;
}
