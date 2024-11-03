package backend.util;

import backend.exception.model.file.FileStorageException;
import backend.model.entity.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

public class FileManager {

    private static final String basePath = "src/main/resources/media";

    public static String saveBase64Image(String base64String) {
        // 从Base64字符串中提取文件类型
        String[] parts = base64String.split(",");
        String metadata = parts[0];

        // 根据格式提取文件类型
        String[] metadataParts = metadata.split(";");
        String fileType = metadataParts[0].split(":")[1].split("/")[1];

        if(!fileType.equals("png") && !fileType.equals("jpg") && !fileType.equals("jpeg")){
            throw new FileStorageException(HttpStatus.BAD_REQUEST.value(), "Invalid file type");
        }

        // 从Base64字符串中提取文件内容
        byte data[] = DatatypeConverter.parseBase64Binary(parts[1]);

        String uuid = UUID.randomUUID().toString();
        String userId = User.getAuth().getId().toString();

        // 创建目标目录
        File storeDir = new File(basePath + "/" + userId);
        if (!storeDir.exists()) {
            storeDir.mkdirs();
        }

        File storeFile = new File(storeDir, uuid + "." + fileType);

        try(OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(storeFile))){
            outputStream.write(data);
        } catch (Exception e) {
            throw new FileStorageException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to store file: " + e.getMessage());
        }

        // 获取文件的绝对路径
        Path fullPath = storeFile.toPath();

        // 获取相对路径，截断为以 media 开头
        String relativePath = fullPath.toString().replace(File.separator, "/"); // 替换为统一的分隔符
        int mediaIndex = relativePath.indexOf("media");

        if (mediaIndex != -1) {
            relativePath = relativePath.substring(mediaIndex);
        } else {
            throw new FileStorageException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Media directory not found in path.");
        }

        return relativePath;
    }

    public static String store(MultipartFile file){
        try {
            String fileName = file.getOriginalFilename();

            if (fileName == null || fileName.isEmpty()) {
                throw new FileStorageException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Invalid file");
            }

            // 获取文件扩展名
            String fileExtension = "";
            int extensionIndex = fileName.lastIndexOf(".");
            if (extensionIndex > 0) {
                fileExtension = fileName.substring(extensionIndex); // 例如: ".pdf" 或 ".png"
            } else {
                throw new FileStorageException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "File extension is missing");
            }

            // 存储
            String uuid = UUID.randomUUID().toString();
            String userId = User.getAuth().getId().toString();

            // 创建目标目录
            File storeDir = new File(basePath + "/" + userId);
            if (!storeDir.exists()) {
                storeDir.mkdirs(); // 创建目录
            }

            File storeFile = new File(storeDir, uuid + fileExtension);

            saveFile(file, storeFile);

            Path fullPath = storeFile.toPath();


            // 获取相对路径，截断为以 media 开头
            String relativePath = fullPath.toString().replace(File.separator, "/"); // 替换为统一的分隔符
            int mediaIndex = relativePath.indexOf("media");

            if (mediaIndex != -1) {
                relativePath = relativePath.substring(mediaIndex);
            } else {
                throw new FileStorageException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Media directory not found in path.");
            }

            return relativePath;
        } catch (Exception e) {
            throw new FileStorageException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "File upload failed: " + e.getMessage());
        }
    }

    private static void saveFile(MultipartFile file, File storeFile){
        try (InputStream inputStream = file.getInputStream();
             FileOutputStream outputStream = new FileOutputStream(storeFile)) {

            byte[] buffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }catch (Exception e){
            throw new FileStorageException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to store file: " + e.getMessage());
        }
    }

}
