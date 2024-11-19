package backend.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LogDTO {
    private Integer id;
    private String userId;
    private String endpoint;
    private String operation;
    private LocalDateTime created;


    public LogDTO getthis(){
        return this;
    }

}
