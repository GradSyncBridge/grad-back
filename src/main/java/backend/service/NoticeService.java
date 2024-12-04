package backend.service;

import backend.model.DTO.NoticeCreateDTO;
import backend.model.VO.notice.NoticeDetailVO;
import backend.model.VO.notice.PageNotice;
import backend.model.VO.notice.PageResult;

public interface NoticeService {

    /**
     * 创建公告
     * @param noticeCreateDTO 公告信息
     * @return null
     */
    void createNotice(NoticeCreateDTO noticeCreateDTO);

    /**
     * 获取公告
     * GET /unauthorized/notice
     * @param pageIndex 当前页
     * @param pageSize 每页数量
     * @return 公告列表
     */
    PageResult getAllNotice(Integer pageIndex, Integer pageSize);

    /**
     * 删除公告
     * @param noticeID 公告ID
     * @return null
     */
    void deleteNotice(Integer noticeID);


    /**
     * 获取公告
     * @param pageIndex 当前页
     * @param pageSize 每页数量
     * @param publish 是否发布
     * @return 公告列表
     */
    PageNotice getNotice(Integer pageIndex, Integer pageSize, Integer publish);

    /**
     * 获取公告详情
     * GET /unauthorized/notice/detail
     * @param noticeID 公告ID
     * @return 公告详情
     */
    NoticeDetailVO getNoticeDetail(Integer noticeID);


    /**
     * 更新公告
     * PUT /notice
     * @param noticeCreateDTO 公告信息
     * @return null
     */
    void updateNotice(NoticeCreateDTO noticeCreateDTO);


    /**
     * 获取公告详情
     * GET /notice/detail
     * @param noticeID 公告ID
     * @return 公告详情
     */
    NoticeDetailVO getNoticeDetailByAdmin(Integer noticeID);
}
