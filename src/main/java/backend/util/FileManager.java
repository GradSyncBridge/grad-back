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

    private static final long MAX_FILE_SIZE = 1024 * 1024 * 100; // 5MB

    private static final long MAX_IMAGE_SIZE = 1024 * 1024 * 3; // 3MB

    private final Logger logger = Logger.getLogger(FileManager.class.getName());

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
                if (!objectSummary.getKey().startsWith("index") &&
                        !objectSummary.getKey().startsWith("login") &&
                        !objectSummary.getKey().startsWith("logo") &&
                        !objectSummary.getKey().startsWith("default.png") &&
                        !files.contains(objectSummary.getKey())
                )
                    r2Client.deleteObject(bucketName, objectSummary.getKey());

            request.setContinuationToken(result.getNextContinuationToken());
        } while (result.isTruncated());
    }

}
