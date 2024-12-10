package backend.service;

import backend.model.DTO.FileDeleteDTO;
import backend.model.VO.file.FileUploadVO;
import org.springframework.web.multipart.MultipartFile;

public interface QualityFileService {
    /**
     * 处理文件上传请求
     * POST /file
     * @param file 文件
     * @return 存储路径
     */
    FileUploadVO handleFileUpload(MultipartFile file);

    /**
     * 处理文件删除请求
     * DELETE /file
     * @param file 文件删除信息
     */
    void handleFileDelete(FileDeleteDTO file);
}
