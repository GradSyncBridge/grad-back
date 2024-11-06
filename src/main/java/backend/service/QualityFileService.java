package backend.service;

import backend.model.VO.file.FileUploadVO;
import org.springframework.web.multipart.MultipartFile;

public interface QualityFileService {
    FileUploadVO handleFileUpload(MultipartFile file);
}
