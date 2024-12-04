package backend.model.DTO;

import backend.annotation.DTO.NoticeCreateValidation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@NoticeCreateValidation
public class NoticeCreateDTO {

    private Integer noticeID;

    private String noticeTitle;

    private String noticeContent;

    private List<Integer> noticeFile;

    private Integer draft;

    private Integer publish;
}
