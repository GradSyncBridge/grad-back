package backend.model.VO.notice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NoticeDetailVO {

    private Integer noticeID;

    private NoticeRefUser user;

    private String noticeTitle;

    private String noticeContent;

    private List<NoticeFile> noticeFile;

    private String created;

    private String updated;
}
