package backend.service;

import backend.model.DTO.EnrollConfirmDTO;
import backend.model.VO.enroll.EnrollSelectVO;
import backend.model.VO.enroll.EnrollVO;
import java.util.List;

public interface EnrollService {
    /**
     * 获取学院录取学生
     * GET /unauthorized/enroll
     * @param department 学院
     * @param year 年份
     * @return 录取学生列表
     */
    List<EnrollVO> getEnrollTable(Integer department, Integer year);

    /**
     * 导师确认录取学生
     * POST /enroll
     * @param confirmDTO 录取信息
     */
    void enrollConfirm(EnrollConfirmDTO confirmDTO);

    /**
     * 导师取消录取关系
     * DELETE /enroll
     * @param enrollmentID 录取记录
     */
    void enrollCancel(Integer enrollmentID);

    /**
     * 导师查看已选学生信息
     * GET /enroll/list
     * @return
     */
    List<EnrollSelectVO> getEnrollList();
}
