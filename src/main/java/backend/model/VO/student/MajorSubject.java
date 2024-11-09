package backend.model.VO.student;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MajorSubject {
    private String name;
    private String majorNum;
    private List<Exam> initials;
    private List<Exam> interviews;

}