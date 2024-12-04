package backend.model.VO.notice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class NoticeBriefList {

    private NoticeRefUser user;

    private Integer noticeID;

    private String noticeTitle;

    private String updated;

}
