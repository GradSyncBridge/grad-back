package backend.model.VO.major;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubMajorSubject {

    private Integer subjectID;

    private String subjectNum;

    private String name;

    private Integer type;
}
