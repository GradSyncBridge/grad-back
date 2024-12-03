package backend.util;

import backend.exception.model.file.FileStorageException;
import backend.mapper.QualityFileMapper;
import backend.mapper.UserMapper;
import backend.model.entity.User;
import com.amazonaws.AmazonClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

@Service
public class FileManager {

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @Autowired
    private AmazonS3 r2Client;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private QualityFileMapper qualityFileMapper;

    @Autowired
    private RedissonClient redissonClient;

    private static final String basePath = "src/main/resources/media";

    private static final long MAX_FILE_SIZE = 1024 * 1024 * 100; // 5MB

    private static final long MAX_IMAGE_SIZE = 1024 * 1024 * 3; // 3MB

    private final Logger logger = Logger.getLogger(FileManager.class.getName());
//
//    public static String saveBase64Image(String base64String, User authUser) {
//        // 从Base64字符串中提取文件类型
//        String[] parts = base64String.split(",");
//        String metadata = parts[0];
//
//        // 根据格式提取文件类型
//        String[] metadataParts = metadata.split(";");
//        String fileType = metadataParts[0].split(":")[1].split("/")[1];
//
//        if (!fileType.equals("png") && !fileType.equals("jpg") && !fileType.equals("jpeg")) {
//            throw new FileStorageException(HttpStatus.BAD_REQUEST.value(), "Invalid file type");
//        }
//
//        // 从Base64字符串中提取文件内容
//        byte[] data = DatatypeConverter.parseBase64Binary(parts[1]);
//
//        if (data.length > MAX_IMAGE_SIZE) {
//            throw new FileStorageException(HttpStatus.BAD_REQUEST.value(), "file size exceeds the limit");
//        }
//
//        String uuid = UUID.randomUUID().toString();
//        String userId = authUser.getId().toString();
//
//        // 创建目标目录
//        File storeDir = new File(basePath + "/" + userId);
//        if (!storeDir.exists()) {
//            storeDir.mkdirs();
//        }
//
//        File storeFile = new File(storeDir, uuid + "." + fileType);
//        try (OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(storeFile))) {
//            outputStream.write(data);
//        } catch (Exception e) {
//            throw new FileStorageException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to store file: " + e.getMessage());
//        }
//
//        // 获取文件的绝对路径
//        Path fullPath = storeFile.toPath();
//
//        // 获取相对路径，截断为以 media 开头
//        String relativePath = fullPath.toString().replace(File.separator, "/"); // 替换为统一的分隔符
//        int mediaIndex = relativePath.indexOf("media");
//
//        if (mediaIndex != -1) {
//            relativePath = relativePath.substring(mediaIndex);
//        } else {
//            throw new FileStorageException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Media directory not found in path.");
//        }
//
//        return relativePath;
//    }
//
//    public static String saveBase64Image(String base64String) {
//        // 从Base64字符串中提取文件类型
//        String[] parts = base64String.split(",");
//        String metadata = parts[0];
//
//        // 根据格式提取文件类型
//        String[] metadataParts = metadata.split(";");
//        String fileType = metadataParts[0].split(":")[1].split("/")[1];
//
//        if (!fileType.equals("png") && !fileType.equals("jpg") && !fileType.equals("jpeg")) {
//            throw new FileStorageException(HttpStatus.BAD_REQUEST.value(), "Invalid file type");
//        }
//
//        // 从Base64字符串中提取文件内容
//        byte[] data = DatatypeConverter.parseBase64Binary(parts[1]);
//
//        if (data.length > MAX_IMAGE_SIZE) {
//            throw new FileStorageException(HttpStatus.BAD_REQUEST.value(), "file size exceeds the limit");
//        }
//
//        String uuid = UUID.randomUUID().toString();
//        String userId = User.getAuth().getId().toString();
//
//        // 创建目标目录
//        File storeDir = new File(basePath + "/" + userId);
//        if (!storeDir.exists()) {
//            storeDir.mkdirs();
//        }
//
//        File storeFile = new File(storeDir, uuid + "." + fileType);
//        try (OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(storeFile))) {
//            outputStream.write(data);
//        } catch (Exception e) {
//            throw new FileStorageException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to store file: " + e.getMessage());
//        }
//
//        // 获取文件的绝对路径
//        Path fullPath = storeFile.toPath();
//
//        // 获取相对路径，截断为以 media 开头
//        String relativePath = fullPath.toString().replace(File.separator, "/"); // 替换为统一的分隔符
//        int mediaIndex = relativePath.indexOf("media");
//
//        if (mediaIndex != -1) {
//            relativePath = relativePath.substring(mediaIndex);
//        } else {
//            throw new FileStorageException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Media directory not found in path.");
//        }
//
//        return relativePath;
//    }
//
//    public static String store(MultipartFile file) {
//        try {
//            if (file.getSize() > MAX_FILE_SIZE) {
//                throw new FileStorageException(HttpStatus.BAD_REQUEST.value(), "file size exceeds the limit");
//            }
//
//            String fileName = file.getOriginalFilename();
//
//            if (fileName == null || fileName.isEmpty()) {
//                throw new FileStorageException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Invalid file");
//            }
//
//            // 获取文件扩展名
//            String fileExtension;
//
//            int extensionIndex = fileName.lastIndexOf(".");
//            if (extensionIndex > 0) {
//                fileExtension = fileName.substring(extensionIndex); // 例如: ".pdf" 或 ".png"
//            } else {
//                throw new FileStorageException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "file extension is missing");
//            }
//
//            // 存储
//            String uuid = UUID.randomUUID().toString();
//            String userId = User.getAuth().getId().toString();
//
//            // 创建目标目录
//            File storeDir = new File(basePath + "/" + userId);
//            if (!storeDir.exists()) {
//                storeDir.mkdirs(); // 创建目录
//            }
//
//            File storeFile = new File(storeDir, uuid + fileExtension);
//
//            saveFile(file, storeFile);
//
//            Path fullPath = storeFile.toPath();
//
//
//            // 获取相对路径，截断为以 media 开头
//            String relativePath = fullPath.toString().replace(File.separator, "/"); // 替换为统一的分隔符
//            int mediaIndex = relativePath.indexOf("media");
//
//            if (mediaIndex != -1) {
//                relativePath = relativePath.substring(mediaIndex);
//            } else {
//                throw new FileStorageException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Media directory not found in path.");
//            }
//
//            return relativePath;
//        } catch (Exception e) {
//            throw new FileStorageException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "file upload failed: " + e.getMessage());
//        }
//    }
//
//    public static void remove(String path) {
//        File file = new File(basePath.substring(0, basePath.lastIndexOf("/")) + "/" + path);
//        if (file.exists())
//            file.delete();
//    }
//
//    private static void saveFile(MultipartFile file, File storeFile) {
//        try (InputStream inputStream = file.getInputStream();
//             FileOutputStream outputStream = new FileOutputStream(storeFile)) {
//
//            byte[] buffer = new byte[1024];
//            int bytesRead;
//
//            while ((bytesRead = inputStream.read(buffer)) != -1) {
//                outputStream.write(buffer, 0, bytesRead);
//            }
//        } catch (Exception e) {
//            throw new FileStorageException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to store file: " + e.getMessage());
//        }
//    }

    public String uploadBase64Image(String base64Image, User user){
        String[] parts = base64Image.split(",");
        String metadata = parts[0];

        String[] metadataParts = metadata.split(";");
        String fileType = metadataParts[0].split(":")[1].split("/")[1];

        if (!fileType.equals("png") && !fileType.equals("jpg") && !fileType.equals("jpeg")) {
            throw new FileStorageException(HttpStatus.BAD_REQUEST.value(), "Invalid file type");
        }

        byte[] data = DatatypeConverter.parseBase64Binary(parts[1]);

        if (data.length > MAX_IMAGE_SIZE) {
            throw new FileStorageException(HttpStatus.BAD_REQUEST.value(), "file size exceeds the limit");
        }

        InputStream targetStream = new ByteArrayInputStream(data);

        try {
            ObjectMetadata metadataStream = new ObjectMetadata();
            metadataStream.setContentLength(data.length);
            metadataStream.setContentType(fileType);

            final String fileName = user.getId().toString() + "/" + UUID.randomUUID().toString();

            r2Client.putObject(bucketName, fileName, targetStream, metadataStream);

            return fileName;
        } catch (AmazonClientException e) {
            throw new RuntimeException("Error uploading file");
        }
    }

    public String uploadFile(MultipartFile file, User user) {
        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            metadata.setContentType(file.getContentType());

            if(file.getBytes().length > MAX_FILE_SIZE){
                throw new FileStorageException(HttpStatus.BAD_REQUEST.value(), "file size exceeds the limit");
            }

            final String fileName = user.getId().toString() + "/" + UUID.randomUUID().toString();

            r2Client.putObject(bucketName, fileName, file.getInputStream(), metadata);

            return fileName;
        } catch (AmazonClientException | IOException e) {

            throw new RuntimeException("Error uploading file");
        }
    }

    public void deleteFile(String fileName){
        try {
            r2Client.deleteObject(bucketName, fileName);
        }catch (Exception e){
            throw new RuntimeException("Error deleting file");
        }
    }

    @Scheduled(cron = "0 0 1 * * ?")
    public void deleteFilesWithLock(){
        RLock lock = redissonClient.getLock("delete-files-lock");

        boolean lockAcquired = false;

        try {
            lockAcquired = lock.tryLock(0, 30, TimeUnit.MINUTES);

            if (lockAcquired)  deleteDeprecatedFiles();

        } catch (Exception e){
            logger.severe("Failed to delete files: ");
        } finally {
            if (lockAcquired) lock.unlock();
        }
    }


    public void deleteDeprecatedFiles(){
        List<String> files = userMapper.selectAllFiles();

        files.addAll(qualityFileMapper.selectAllFiles());

        ListObjectsV2Request request = new ListObjectsV2Request().withBucketName(bucketName);
        ListObjectsV2Result result;

        do {
            result = r2Client.listObjectsV2(request);

            for (S3ObjectSummary objectSummary : result.getObjectSummaries())
                if (!files.contains(objectSummary.getKey()))
                    r2Client.deleteObject(bucketName, objectSummary.getKey());

            request.setContinuationToken(result.getNextContinuationToken());
        } while (result.isTruncated());
    }

}
