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
public class PageNotice {

    private Integer total;

    private List<NoticeBriefList> noticeList;

}
