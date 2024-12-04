package backend.service;

import backend.model.DTO.NoticeCreateDTO;
import backend.model.VO.notice.NoticeDetailVO;
import backend.model.VO.notice.PageResult;

public interface NoticeService {
    void createNotice(NoticeCreateDTO noticeCreateDTO);

    PageResult getAllNotice(Integer pageIndex, Integer pageSize);

    void deleteNotice(Integer noticeID);

    PageResult getNotice(Integer pageIndex, Integer pageSize, Integer publish);

    NoticeDetailVO getNoticeDetail(Integer noticeID);
}
