package backend.controller;

import backend.model.DTO.FileDeleteDTO;
import backend.model.entity.QualityFile;
import backend.service.QualityFileService;
import backend.util.FileManager;
import backend.util.ResultEntity;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * FileController
 *
 * @function handleFileUpload 处理文件上传请求
 * @function handleFileDelete 处理文件删除请求
 */
@RestController
@RequestMapping(value = "/file")
public class FileController {

    @Autowired
    private QualityFileService qualityFileService;

    /**
     * 处理文件上传请求
     * POST /file
     * @param file 文件
     * @return 存储路径
     */
    @PostMapping
    public ResponseEntity<ResultEntity<Object>> handleFileUpload(@RequestBody MultipartFile file) {
        return ResultEntity.success(
                HttpStatus.OK.value(),
                "File uploaded successfully",
                qualityFileService.handleFileUpload(file)
        );
    }

    /**
     * 处理文件删除请求
     * DELETE /file
     * @param fileDelete 文件删除信息
     * @return 删除结果
     */
    @DeleteMapping
    public ResponseEntity<ResultEntity<Object>> handleFileDelete(@RequestBody FileDeleteDTO fileDelete) {
        qualityFileService.handleFileDelete(fileDelete);

        return ResultEntity.success(
                HttpStatus.OK.value(),
                "Target file deleted successfully",
                null
        );
    }
}
