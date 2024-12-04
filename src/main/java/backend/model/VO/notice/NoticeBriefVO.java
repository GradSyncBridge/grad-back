package backend.model.VO.notice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NoticeBriefVO {

    private Integer noticeID;

    private String noticeTitle;

    private String updated;
}
