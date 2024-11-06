package backend.model.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MajorDTO {

    private Integer id;

    private String name;

    private String mid;

    // first class -> null
    private Integer pid;

    private String description;

    //学硕--->0 专硕--->1
    private Integer type;

    private Integer total;

    private Integer addition;

    private Integer year;

    private List<Integer> initial;

    private List<Integer> interview;

    private Integer recommend;

    private Integer disabled;

    private Integer department;

    private Integer allowReassign;

}
