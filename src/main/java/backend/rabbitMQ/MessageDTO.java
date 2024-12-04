package backend.rabbitMQ;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Profile;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Profile("mq")
public class MessageDTO {

    private Integer id;

    private Integer sender;

    private Integer receiver;

    private String content;
}
