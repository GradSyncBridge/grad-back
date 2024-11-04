package backend.controller;

import backend.util.FileManager;
import backend.util.ResultEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping(value = "/file")
public class FileController {

    /**
     * 处理文件上传请求
     *
     * @param file 文件
     * @return 存储路径
     */
    @PostMapping()
    public ResponseEntity<ResultEntity<String>> handleFileUpload(@RequestBody MultipartFile file) {
        String path = FileManager.store(file);
        return ResultEntity.success(HttpStatus.OK.value(), "ok", path);

    }
}
