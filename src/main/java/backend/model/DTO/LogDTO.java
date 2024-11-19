package backend.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Component
public class LogDTO {
    private Integer id;
    private Integer userId;
    private String endpoint;
    private String operation;
    private LocalDateTime created;


    public LogDTO getthis(){
        return this;
    }

}
