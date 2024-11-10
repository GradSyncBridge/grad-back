package backend.service.impl;

import backend.mapper.QualityFileMapper;
import backend.model.VO.file.FileUploadVO;
import backend.model.entity.QualityFile;
import backend.model.entity.User;
import backend.service.QualityFileService;
import backend.util.FileManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Service
public class QualityFileServiceImpl implements QualityFileService {

    @Autowired
    private QualityFileMapper qualityFileMapper;

    @Override
    public FileUploadVO handleFileUpload(MultipartFile file) {
        try {
            String filePath = FileManager.store(file);

            QualityFile qualityFile = QualityFile.builder().userId(User.getAuth().getId()).file(filePath).created(LocalDateTime.now()).build();
            qualityFileMapper.insertQualityFile(qualityFile);

            return FileUploadVO.builder().fileID(qualityFile.getId()).filePath(filePath).build();
        }catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
