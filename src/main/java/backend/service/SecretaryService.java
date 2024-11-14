package backend.service;

import backend.model.DTO.SecretaryExamineDTO;
import backend.model.DTO.SecretaryGradeDTO;

public interface SecretaryService {
    void examineStudentSubmission(SecretaryExamineDTO examineDTO);

    void modifyStudentGrade(SecretaryGradeDTO gradeDTO);
}
