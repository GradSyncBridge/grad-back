package backend.model.VO.deadline;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeadlineVO {
    private Integer DeadlineID;
    private String name;
    private String deadline;
}
