package run.halo.app.handler.file;
 import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.IOException;
import java.util.Collections;
import java.util.Objects;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import run.halo.app.exception.FileOperationException;
import run.halo.app.exception.ServiceException;
import run.halo.app.model.enums.AttachmentType;
import run.halo.app.model.properties.SmmsProperties;
import run.halo.app.model.support.HaloConst;
import run.halo.app.model.support.UploadResult;
import run.halo.app.service.OptionService;
import run.halo.app.utils.FilenameUtils;
import run.halo.app.utils.HttpClientUtils;
@Slf4j
@Component
public class SmmsFileHandler implements FileHandler{

@Deprecated
 private  String UPLOAD_API;

 private  String UPLOAD_API_V2;

@Deprecated
 private  String DELETE_API;

 private  String DELETE_API_V2;

 private  String SUCCESS_CODE;

 private  RestTemplate httpsRestTemplate;

 private  OptionService optionService;

 private  HttpHeaders headers;

 private  Boolean success;

 private  String code;

 private  String message;

 private  SmmsResponseData data;

@JsonProperty("RequestId")
 private  String requestId;

 private  Integer width;

 private  Integer height;

 private  String filename;

 private  String storename;

 private  Integer size;

 private  String path;

 private  String hash;

 private  String url;

 private  String delete;

 private  String page;

public SmmsFileHandler(RestTemplate httpsRestTemplate, OptionService optionService) {
    this.httpsRestTemplate = httpsRestTemplate;
    this.optionService = optionService;
    MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
    mappingJackson2HttpMessageConverter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
    this.httpsRestTemplate.getMessageConverters().add(mappingJackson2HttpMessageConverter);
}
public boolean isResponseSuccessfully(SmmsResponse smmsResponse){
    return smmsResponse != null && smmsResponse.getCode().equals(SUCCESS_CODE);
}


public void setHeaders(){
    headers.set(HttpHeaders.USER_AGENT, "Halo/" + HaloConst.HALO_VERSION);
    headers.set(HttpHeaders.AUTHORIZATION, optionService.getByPropertyOfNonNull(SmmsProperties.SMMS_API_SECRET_TOKEN).toString());
}


@Override
public UploadResult upload(MultipartFile file){
    Assert.notNull(file, "Multipart file must not be null");
    String apiSecretToken = optionService.getByPropertyOfNonNull(SmmsProperties.SMMS_API_SECRET_TOKEN).toString();
    if (StringUtils.isEmpty(apiSecretToken)) {
        throw new ServiceException("请先设置 SM.MS 的 Secret Token");
    }
    if (!isImageType(file)) {
        log.error("Invalid extension: [{}]", file.getContentType());
        throw new FileOperationException("不支持的文件类型，仅支持 \"jpeg, jpg, png, gif, bmp\" 格式的图片");
    }
    setHeaders();
    // Set content type
    headers.setContentType(MediaType.MULTIPART_FORM_DATA);
    LinkedMultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
    try {
        body.add("smfile", new HttpClientUtils.MultipartFileResource(file.getBytes(), file.getOriginalFilename()));
    } catch (IOException e) {
        log.error("Failed to get file input stream", e);
        throw new FileOperationException("上传附件 " + file.getOriginalFilename() + " 到 SM.MS 失败", e);
    }
    body.add("format", "json");
    HttpEntity<LinkedMultiValueMap<String, Object>> httpEntity = new HttpEntity<>(body, headers);
    // Upload file
    ResponseEntity<SmmsResponse> mapResponseEntity = httpsRestTemplate.postForEntity(UPLOAD_API_V2, httpEntity, SmmsResponse.class);
    // Check status
    if (mapResponseEntity.getStatusCode().isError()) {
        log.error("Server response detail: [{}]", mapResponseEntity.toString());
        throw new FileOperationException("SM.MS 服务状态异常，状态码: " + mapResponseEntity.getStatusCodeValue());
    }
    // Get smms response
    SmmsResponse smmsResponse = mapResponseEntity.getBody();
    // Check error
    if (!isResponseSuccessfully(smmsResponse)) {
        log.error("Smms response detail: [{}]", smmsResponse);
        throw new FileOperationException(smmsResponse == null ? "SM.MS 服务返回内容为空" : smmsResponse.getMessage()).setErrorData(smmsResponse);
    }
    if (!smmsResponse.getSuccess()) {
        throw new FileOperationException("上传请求失败：" + smmsResponse.getMessage()).setErrorData(smmsResponse);
    }
    // Get response data
    SmmsResponseData data = smmsResponse.getData();
    // Build result
    UploadResult result = new UploadResult();
    result.setFilename(FilenameUtils.getBasename(Objects.requireNonNull(file.getOriginalFilename())));
    result.setSuffix(FilenameUtils.getExtension(file.getOriginalFilename()));
    result.setMediaType(MediaType.valueOf(Objects.requireNonNull(file.getContentType())));
    result.setFilePath(data.getUrl());
    result.setThumbPath(data.getUrl());
    result.setKey(data.getHash());
    result.setWidth(data.getWidth());
    result.setHeight(data.getHeight());
    result.setSize(data.getSize().longValue());
    log.info("File: [{}] uploaded successfully", file.getOriginalFilename());
    return result;
}


@Override
public AttachmentType getAttachmentType(){
    return AttachmentType.SMMS;
}


@Override
public void delete(String key){
    Assert.hasText(key, "Deleting key must not be blank");
    // Build delete url
    String url = String.format(DELETE_API_V2, key);
    setHeaders();
    // Delete the file
    ResponseEntity<String> responseEntity = httpsRestTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(null, headers), String.class);
    if (responseEntity.getStatusCode().isError()) {
        log.debug("Smms server response error: [{}]", responseEntity.toString());
        throw new FileOperationException("SM.MS 服务状态异常");
    }
    log.debug("Smms response detail: [{}]", responseEntity.getBody());
    // Deleted successfully or have been deleted already
    log.info("File was deleted successfully or had been deleted already");
}


}