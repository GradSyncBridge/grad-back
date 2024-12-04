package backend.service.impl;

import backend.exception.model.notice.NoticeLockedException;
import backend.exception.model.notice.NoticeNotFoundException;
import backend.exception.model.user.UserRoleDeniedException;
import backend.mapper.NoticeMapper;
import backend.mapper.QualityFileMapper;
import backend.mapper.UserMapper;
import backend.model.DTO.NoticeCreateDTO;
import backend.model.VO.notice.*;
import backend.model.converter.NoticeConverter;
import backend.model.entity.Notice;
import backend.model.entity.QualityFile;
import backend.model.entity.User;
import backend.redis.RedisService;
import backend.service.NoticeService;
import backend.util.FieldsGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class NoticeServiceImpl implements NoticeService {

    @Autowired
    private NoticeMapper noticeMapper;

    @Autowired
    private NoticeConverter noticeConverter;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private QualityFileMapper qualityFileMapper;

    @Autowired
    private RedisService redisService;

    private static final String NOTICE_PREFIX = "notice:";

    private static final String LOCKED = "locked";

    private static final String UNLOCKED = "unlocked";

    /**
     * 创建公告
     * @param noticeCreateDTO 公告信息
     */
    @Override
    @Transactional
    public void createNotice(NoticeCreateDTO noticeCreateDTO) {
        try{
            ObjectMapper objectMapper = new ObjectMapper();

            String files = objectMapper.writeValueAsString(noticeCreateDTO.getNoticeFile());

            Notice notice = Notice.builder().uid(User.getAuth().getId())
                    .title(noticeCreateDTO.getNoticeTitle())
                    .content(noticeCreateDTO.getNoticeContent())
                    .files(files).publish(noticeCreateDTO.getPublish())
                    .draft(noticeCreateDTO.getDraft())
                    .disabled(1)
                    .created(LocalDateTime.now())
                    .updated(LocalDateTime.now())
                    .build();

            noticeMapper.insertNotice(notice);

            if(notice.getDraft() == 1 && notice.getPublish() == 0)
                redisService.saveData(NOTICE_PREFIX + notice.getId(), UNLOCKED);
        }catch (Exception e){
            throw new RuntimeException("Error creating notice");
        }
    }

    /**
     * 获取公告
     * GET /unauthorized/notice
     * @param pageIndex 当前页
     * @param pageSize 每页数量
     * @return 公告列表
     */
    @Override
    public PageResult getAllNotice(Integer pageIndex, Integer pageSize) {

        PageHelper.startPage(pageIndex, pageSize, true);

        Page<Notice> page = noticeMapper.selectNoticeByPage();

        if(page == null)  return null;

        List<NoticeBriefVO> noticeBriefVOList =
                noticeConverter.INSTANCE.NoticeListToNoticeBriefVOList(page.getResult());

        return PageResult.builder()
                .noticeList(noticeBriefVOList)
                .total((int) page.getTotal())
                .build();
    }

    /**
     * 删除公告
     * DELETE /notice
     * @param noticeID 公告ID
     */
    @Override
    @Transactional
    public void deleteNotice(Integer noticeID) {
        if(User.getAuth().getTeacher() == null)
            throw new UserRoleDeniedException(1, 403);

        noticeMapper.deleteNotice(noticeID);

        if(redisService.getData(NOTICE_PREFIX + noticeID) != null)
            redisService.deleteData(NOTICE_PREFIX + noticeID);
    }

    /**
     * 获取公告
     * GET /notice
     * @param pageIndex 当前页
     * @param pageSize 每页数量
     * @param publish 是否发布
     * @return 公告列表
     */
    @Override
    public PageNotice getNotice(Integer pageIndex, Integer pageSize, Integer publish) {
        if (User.getAuth().getTeacher() == null)
            throw new UserRoleDeniedException(1, 403);

        PageHelper.startPage(pageIndex, pageSize, true);

        Page<Notice> page = noticeMapper.selectNoticeByPageWithCondition(publish);

        if (page == null) return null;

        List<User> userList = userMapper.selectUser(
                User.builder().id(User.getAuth().getId()).build(),
                FieldsGenerator.generateFields(User.class)
        );

        List<NoticeBriefList> noticeBriefList =
                noticeConverter.INSTANCE.NoticeListToNoticeBriefList(page.getResult(), userList.getFirst());

        return PageNotice.builder()
                .noticeList(noticeBriefList)
                .total((int) page.getTotal())
                .build();
    }

    /**
     * 获取公告详情
     * GET /unauthorized/notice/detail
     * @param noticeID 公告ID
     * @return 公告详情
     */
    @Override
    public NoticeDetailVO getNoticeDetail(Integer noticeID) {
        return getNoticeDetailWithCondition(noticeID);
    }

    /**
     * 更新公告
     * PUT /notice
     * @param noticeCreateDTO 公告信息
     */
    @Override
    @Transactional
    public void updateNotice(NoticeCreateDTO noticeCreateDTO) {
        try{
            if(User.getAuth().getTeacher() == null)
                throw new UserRoleDeniedException(1, 403);

            ObjectMapper objectMapper = new ObjectMapper();

            String files = objectMapper.writeValueAsString(noticeCreateDTO.getNoticeFile());

            if(noticeCreateDTO.getPublish() == 1) noticeCreateDTO.setDraft(0);
            Notice notice = Notice.builder().id(noticeCreateDTO.getNoticeID())
                    .uid(User.getAuth().getId())
                    .title(noticeCreateDTO.getNoticeTitle())
                    .content(noticeCreateDTO.getNoticeContent())
                    .files(files).publish(noticeCreateDTO.getPublish())
                    .draft(noticeCreateDTO.getDraft())
                    .disabled(noticeCreateDTO.getPublish() == 1 ? 1 : 0)
                    .updated(LocalDateTime.now())
                    .build();

            noticeMapper.updateNotice(
                    notice,
                    Notice.builder().id(noticeCreateDTO.getNoticeID()).build()
            );

            String lock = (String) redisService.getData(NOTICE_PREFIX + notice.getId());

            if(lock == null && notice.getDraft() == 1)
                redisService.saveData(NOTICE_PREFIX + notice.getId(), UNLOCKED);
            else if(lock != null && notice.getDraft() == 0)
                redisService.deleteData(NOTICE_PREFIX + notice.getId());
            else if(lock != null && notice.getPublish() == 1)
                redisService.deleteData(NOTICE_PREFIX + notice.getId());

        }catch (UserRoleDeniedException e){
            throw new UserRoleDeniedException(1, 403);
        }catch (Exception e) {
            throw new RuntimeException("Error updating notice");
        }
    }

    /**
     * 获取公告详情
     * GET /notice/detail
     * @param noticeID 公告ID
     * @return 公告详情
     */
    @Override
    @Transactional
    public NoticeDetailVO getNoticeDetailByAdmin(Integer noticeID) {
        if (User.getAuth().getTeacher() == null)
            throw new UserRoleDeniedException(1, 403);

        String lock = (String) redisService.getData(NOTICE_PREFIX + noticeID);

        if (lock == null) {
            redisService.saveData(NOTICE_PREFIX + noticeID, UNLOCKED);
            lock = UNLOCKED;
        }

        if (!lock.equals(UNLOCKED) && !lock.equals(LOCKED + ":" + User.getAuth().getId()))
            throw new NoticeLockedException();

        redisService.setData(NOTICE_PREFIX + noticeID, LOCKED + ":" + User.getAuth().getId());

        noticeMapper.updateNotice(
                Notice.builder().draft(1).build(),
                Notice.builder().id(noticeID).build()
        );

        return getNoticeDetailWithCondition(noticeID);
    }


    public NoticeDetailVO getNoticeDetailWithCondition(Integer noticeID) {
        Notice notice;

        if(SecurityContextHolder.getContext().getAuthentication() != null)
            notice = noticeMapper.selectNoticeByIdWithAdmin(noticeID);
        else
            notice = noticeMapper.selectNoticeById(noticeID);

        if(notice == null)  throw new NoticeNotFoundException();

        List<User> userList = userMapper.selectUser(
                User.builder().id(notice.getUid()).build(),
                FieldsGenerator.generateFields(User.class)
        );

        if(userList == null || userList.isEmpty())  throw new NoticeNotFoundException();

        ObjectMapper objectMapper = new ObjectMapper();

        List<NoticeFile> noticeFileList = new ArrayList<>();
        try {
            List<Integer> files = objectMapper.readValue(notice.getFiles(), List.class);

            if(files != null && !files.isEmpty()){
                files.forEach(fileID -> {
                    List<QualityFile> qualityFiles = qualityFileMapper.selectQualityFile(
                            QualityFile.builder().id(fileID).build(),
                            FieldsGenerator.generateFields(QualityFile.class)
                    );

                    if(qualityFiles != null && !qualityFiles.isEmpty())
                        noticeFileList.add(
                                NoticeFile.builder()
                                        .fileID(qualityFiles.getFirst().getId())
                                        .filePath(qualityFiles.getFirst().getFile())
                                        .build()
                        );
                });
            }

        }catch (Exception e){
            throw new RuntimeException("Error parsing files");
        }

        return NoticeConverter.INSTANCE.NoticeToNoticeDetailVO(
                notice, userList.getFirst(), noticeFileList
        );
    }
}
