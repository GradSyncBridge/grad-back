package backend.model.converter;

import backend.model.VO.notice.*;
import backend.model.entity.Notice;
import backend.model.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface NoticeConverter {

    NoticeConverter INSTANCE = Mappers.getMapper(NoticeConverter.class);

    default NoticeBriefVO NoticeToNoticeBriefVO(Notice notice) {
        return NoticeBriefVO.builder()
                .noticeID(notice.getId())
                .noticeTitle(notice.getTitle())
                .updated(notice.getUpdated().toString().split("T")[0])
                .build();
    }

    default List<NoticeBriefVO> NoticeListToNoticeBriefVOList(List<Notice> noticeList) {
        List<NoticeBriefVO> noticeBriefVOList = new ArrayList<>();

        noticeList.forEach(notice -> noticeBriefVOList.add(NoticeToNoticeBriefVO(notice)));

        return noticeBriefVOList;
    }

    default NoticeDetailVO NoticeToNoticeDetailVO(Notice notice, User user, List<NoticeFile> noticeFiles) {

        NoticeRefUser noticeRefUser = NoticeRefUser.builder()
                .uid(notice.getId())
                .email(user.getEmail())
                .phone(user.getPhone())
                .avatar(user.getAvatar())
                .username(user.getUsername())
                .name(user.getName())
                .gender(user.getGender())
                .build();

        return NoticeDetailVO.builder()
                .noticeID(notice.getId())
                .user(noticeRefUser)
                .noticeTitle(notice.getTitle())
                .noticeContent(notice.getContent())
                .noticeFile(noticeFiles)
                .created(notice.getCreated().toString().split("T")[0])
                .updated(notice.getUpdated().toString().split("T")[0])
                .build();
    }

    default NoticeBriefList NoticeToNoticeBriefList(Notice notice, User user) {
        NoticeRefUser noticeRefUser = NoticeRefUser.builder()
                .uid(notice.getId())
                .email(user.getEmail())
                .phone(user.getPhone())
                .avatar(user.getAvatar())
                .username(user.getUsername())
                .name(user.getName())
                .gender(user.getGender())
                .build();

        return NoticeBriefList.builder()
                .noticeID(notice.getId())
                .noticeTitle(notice.getTitle())
                .updated(notice.getUpdated().toString().split("T")[0])
                .draft(notice.getDraft())
                .user(noticeRefUser)
                .build();
    }



    default List<NoticeBriefList> NoticeListToNoticeBriefList(List<Notice> result, User first){
        List<NoticeBriefList> noticeBriefList = new ArrayList<>();

        result.forEach(notice -> noticeBriefList.add(NoticeToNoticeBriefList(notice, first)));

        return noticeBriefList;
    }
}
