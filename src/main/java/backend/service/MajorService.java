package backend.service;

import backend.model.VO.major.MajorGrabVO;

public interface MajorService {
    MajorGrabVO grabMajors(Integer department);
}
