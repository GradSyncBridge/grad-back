package backend.model.VO.log;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LogVO {
    private Integer uid;
    private String endpoint;
    private String operation;
    private String created;
}
