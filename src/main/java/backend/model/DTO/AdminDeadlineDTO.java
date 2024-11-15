package backend.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminDeadlineDTO {
    private Integer deadlineID;

    private String name;

    private String deadline;

}
