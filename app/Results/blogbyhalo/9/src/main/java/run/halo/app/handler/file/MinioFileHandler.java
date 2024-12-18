package run.halo.app.handler.file;
 import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;
import run.halo.app.exception.FileOperationException;
import run.halo.app.model.enums.AttachmentType;
import run.halo.app.model.properties.MinioProperties;
import run.halo.app.model.support.HaloConst;
import run.halo.app.model.support.UploadResult;
import run.halo.app.service.OptionService;
import run.halo.app.utils.FilenameUtils;
@Slf4j
@Component
public class MinioFileHandler implements FileHandler{

 private  OptionService optionService;

public MinioFileHandler(OptionService optionService) {
    this.optionService = optionService;
}
@NonNull
@Override
public UploadResult upload(MultipartFile file){
    Assert.notNull(file, "Multipart file must not be null");
    // Get config
    String endpoint = optionService.getByPropertyOfNonNull(MinioProperties.ENDPOINT).toString();
    String accessKey = optionService.getByPropertyOfNonNull(MinioProperties.ACCESS_KEY).toString();
    String accessSecret = optionService.getByPropertyOfNonNull(MinioProperties.ACCESS_SECRET).toString();
    String bucketName = optionService.getByPropertyOfNonNull(MinioProperties.BUCKET_NAME).toString();
    String source = optionService.getByPropertyOrDefault(MinioProperties.SOURCE, String.class, "");
    endpoint = StringUtils.appendIfMissing(endpoint, HaloConst.URL_SEPARATOR);
    MinioClient minioClient = MinioClient.builder().endpoint(endpoint).credentials(accessKey, accessSecret).build();
    try {
        String basename = FilenameUtils.getBasename(Objects.requireNonNull(file.getOriginalFilename()));
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        String timestamp = String.valueOf(System.currentTimeMillis());
        String upFilePath = StringUtils.join(StringUtils.isNotBlank(source) ? source + HaloConst.URL_SEPARATOR : "", basename, "_", timestamp, ".", extension);
        String filePath = StringUtils.join(endpoint, bucketName, HaloConst.URL_SEPARATOR, upFilePath);
        PutObjectArgs putObjectArgs = PutObjectArgs.builder().contentType(file.getContentType()).bucket(bucketName).stream(file.getInputStream(), file.getSize(), -1).object(upFilePath).build();
        minioClient.putObject(putObjectArgs);
        UploadResult uploadResult = new UploadResult();
        uploadResult.setFilename(basename);
        uploadResult.setFilePath(filePath);
        uploadResult.setKey(upFilePath);
        uploadResult.setMediaType(MediaType.valueOf(Objects.requireNonNull(file.getContentType())));
        uploadResult.setSuffix(extension);
        uploadResult.setSize(file.getSize());
        // Handle thumbnail
        handleImageMetadata(file, uploadResult, () -> filePath);
        return uploadResult;
    } catch (Exception e) {
        log.error("upload file to MINIO failed", e);
        throw new FileOperationException("上传附件 " + file.getOriginalFilename() + " 到 MinIO 失败 ", e).setErrorData(e.getMessage());
    }
}


@Override
public AttachmentType getAttachmentType(){
    return AttachmentType.MINIO;
}


@Override
public void delete(String key){
    Assert.notNull(key, "File key must not be blank");
    String endPoint = optionService.getByPropertyOfNonNull(MinioProperties.ENDPOINT).toString();
    endPoint = StringUtils.appendIfMissing(endPoint, HaloConst.URL_SEPARATOR);
    String accessKey = optionService.getByPropertyOfNonNull(MinioProperties.ACCESS_KEY).toString();
    String accessSecret = optionService.getByPropertyOfNonNull(MinioProperties.ACCESS_SECRET).toString();
    String bucketName = optionService.getByPropertyOfNonNull(MinioProperties.BUCKET_NAME).toString();
    MinioClient minioClient = MinioClient.builder().endpoint(endPoint).credentials(accessKey, accessSecret).build();
    try {
        minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucketName).object(key).build());
    } catch (Exception e) {
        log.error("delete MINIO file: [{}] failed", key, e);
        throw new FileOperationException("附件 " + key + " 从 MinIO 删除失败", e).setErrorData(e.getMessage());
    }
}


}