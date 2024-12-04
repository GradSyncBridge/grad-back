package backend.service.impl;

import backend.exception.model.notice.NoticeNotFoundException;
import backend.exception.model.user.UserRoleDeniedException;
import backend.mapper.NoticeMapper;
import backend.mapper.QualityFileMapper;
import backend.mapper.UserMapper;
import backend.model.DTO.NoticeCreateDTO;
import backend.model.VO.notice.NoticeBriefVO;
import backend.model.VO.notice.NoticeDetailVO;
import backend.model.VO.notice.NoticeFile;
import backend.model.VO.notice.PageResult;
import backend.model.converter.NoticeConverter;
import backend.model.entity.Notice;
import backend.model.entity.QualityFile;
import backend.model.entity.User;
import backend.service.NoticeService;
import backend.util.FieldsGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Override
    @Transactional
    public void createNotice(NoticeCreateDTO noticeCreateDTO) {
        try{
            ObjectMapper objectMapper = new ObjectMapper();

            String files = objectMapper.writeValueAsString(noticeCreateDTO.getNoticeFile());

            noticeMapper.insertNotice(
                    Notice.builder().uid(User.getAuth().getId())
                        .title(noticeCreateDTO.getNoticeTitle())
                        .content(noticeCreateDTO.getNoticeContent())
                        .files(files).publish(noticeCreateDTO.getPublish())
                        .draft(noticeCreateDTO.getDraft())
                        .disabled(1)
                        .created(LocalDateTime.now())
                        .updated(LocalDateTime.now())
                        .build()
            );

        }catch (Exception e){
            throw new RuntimeException("Error creating notice");
        }
    }

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

    @Override
    @Transactional
    public void deleteNotice(Integer noticeID) {
        if(User.getAuth().getTeacher() == null || User.getAuth().getTeacher().getIdentity() != 3)
            throw new UserRoleDeniedException();

        try{
            noticeMapper.deleteNotice(noticeID);
        }catch (Exception e){
            throw new RuntimeException("Error deleting notice");
        }
    }

    @Override
    public PageResult getNotice(Integer pageIndex, Integer pageSize, Integer publish) {
        if(User.getAuth().getTeacher() == null || User.getAuth().getTeacher().getIdentity() != 3)
            throw new UserRoleDeniedException();

        PageHelper.startPage(pageIndex, pageSize, true);

        Page<Notice> page = noticeMapper.selectNoticeByPageWithCondition(publish);

        if(page == null)  return null;

        List<NoticeBriefVO> noticeBriefVOList =
                noticeConverter.INSTANCE.NoticeListToNoticeBriefVOList(page.getResult());

        return PageResult.builder()
                .noticeList(noticeBriefVOList)
                .total((int) page.getTotal())
                .build();
    }

    @Override
    public NoticeDetailVO getNoticeDetail(Integer noticeID) {

        Notice notice = noticeMapper.selectNoticeById(noticeID);

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
