package run.halo.app.controller.admin.api;
 import run.halo.app.service.BackupService.BackupType.JSON_DATA;
import run.halo.app.service.BackupService.BackupType.MARKDOWN;
import run.halo.app.service.BackupService.BackupType.WHOLE_SITE;
import io.swagger.annotations.ApiOperation;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import run.halo.app.annotation.DisableOnCondition;
import run.halo.app.config.properties.HaloProperties;
import run.halo.app.exception.NotFoundException;
import run.halo.app.model.dto.BackupDTO;
import run.halo.app.model.dto.post.BasePostDetailDTO;
import run.halo.app.model.params.PostMarkdownParam;
import run.halo.app.service.BackupService;
@RestController
@RequestMapping("/api/admin/backups")
@Slf4j
public class BackupController {

 private  BackupService backupService;

 private  HaloProperties haloProperties;

public BackupController(BackupService backupService, HaloProperties haloProperties) {
    this.backupService = backupService;
    this.haloProperties = haloProperties;
}
@DeleteMapping("markdown/export")
@ApiOperation("Deletes a markdown backup")
@DisableOnCondition
public void deleteMarkdown(String filename){
    backupService.deleteMarkdown(filename);
}


@GetMapping("data")
@ApiOperation("Lists all exported data")
public List<BackupDTO> listExportedData(){
    return backupService.listExportedData();
}


@GetMapping("data/{fileName:.+}")
@ApiOperation("Downloads a exported data")
@DisableOnCondition
public ResponseEntity<Resource> downloadExportedData(String fileName,HttpServletRequest request){
    log.info("Try to download exported data file: [{}]", fileName);
    // Load exported data as resource
    Resource exportDataResource = backupService.loadFileAsResource(haloProperties.getDataExportDir(), fileName);
    String contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
    // Try to determine file's content type
    try {
        contentType = request.getServletContext().getMimeType(exportDataResource.getFile().getAbsolutePath());
    } catch (IOException e) {
        log.warn("Could not determine file type", e);
    }
    return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType)).header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + exportDataResource.getFilename() + "\"").body(exportDataResource);
}


@GetMapping("work-dir/fetch")
public BackupDTO getWorkDirBackup(String filename){
    return backupService.getBackup(Paths.get(haloProperties.getBackupDir(), filename), WHOLE_SITE).orElseThrow(() -> new NotFoundException("备份文件 " + filename + " 不存在或已删除！").setErrorData(filename));
}


@GetMapping("markdown/export")
@ApiOperation("Gets all markdown backups")
public List<BackupDTO> listMarkdowns(){
    return backupService.listMarkdowns();
}


@PostMapping("markdown/export")
@ApiOperation("Exports markdowns")
@DisableOnCondition
public BackupDTO exportMarkdowns(PostMarkdownParam postMarkdownParam){
    return backupService.exportMarkdowns(postMarkdownParam);
}


@GetMapping("data/fetch")
public BackupDTO getDataBackup(String filename){
    return backupService.getBackup(Paths.get(haloProperties.getDataExportDir(), filename), JSON_DATA).orElseThrow(() -> new NotFoundException("备份文件 " + filename + " 不存在或已删除！").setErrorData(filename));
}


@DeleteMapping("data")
@ApiOperation("Deletes a exported data")
@DisableOnCondition
public void deleteExportedData(String filename){
    backupService.deleteExportedData(filename);
}


@PostMapping("data")
@ApiOperation("Exports all data")
@DisableOnCondition
public BackupDTO exportData(){
    return backupService.exportData();
}


@DeleteMapping("work-dir")
@ApiOperation("Deletes a work directory backup")
@DisableOnCondition
public void deleteBackup(String filename){
    backupService.deleteWorkDirBackup(filename);
}


@PostMapping("work-dir")
@ApiOperation("Backups work directory")
@DisableOnCondition
public BackupDTO backupHalo(){
    return backupService.backupWorkDirectory();
}


@GetMapping("work-dir/{filename:.+}")
@ApiOperation("Downloads a work directory backup file")
@DisableOnCondition
public ResponseEntity<Resource> downloadBackup(String filename,HttpServletRequest request){
    log.info("Trying to download backup file: [{}]", filename);
    // Load file as resource
    Resource backupResource = backupService.loadFileAsResource(haloProperties.getBackupDir(), filename);
    String contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
    // Try to determine file's content type
    try {
        contentType = request.getServletContext().getMimeType(backupResource.getFile().getAbsolutePath());
    } catch (IOException e) {
        log.warn("Could not determine file type", e);
    // Ignore this error
    }
    return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType)).header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + backupResource.getFilename() + "\"").body(backupResource);
}


@GetMapping("work-dir")
@ApiOperation("Gets all work directory backups")
public List<BackupDTO> listBackups(){
    return backupService.listWorkDirBackups();
}


@PostMapping("markdown/import")
@ApiOperation("Imports markdown")
public BasePostDetailDTO backupMarkdowns(MultipartFile file){
    return backupService.importMarkdown(file);
}


@GetMapping("markdown/export/{fileName:.+}")
@ApiOperation("Downloads a work markdown backup file")
@DisableOnCondition
public ResponseEntity<Resource> downloadMarkdown(String fileName,HttpServletRequest request){
    log.info("Try to download markdown backup file: [{}]", fileName);
    // Load file as resource
    Resource backupResource = backupService.loadFileAsResource(haloProperties.getBackupMarkdownDir(), fileName);
    String contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
    // Try to determine file's content type
    try {
        contentType = request.getServletContext().getMimeType(backupResource.getFile().getAbsolutePath());
    } catch (IOException e) {
        log.warn("Could not determine file type", e);
    // Ignore this error
    }
    return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType)).header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + backupResource.getFilename() + "\"").body(backupResource);
}


@GetMapping("markdown/fetch")
public BackupDTO getMarkdownBackup(String filename){
    return backupService.getBackup(Paths.get(haloProperties.getBackupMarkdownDir(), filename), MARKDOWN).orElseThrow(() -> new NotFoundException("备份文件 " + filename + " 不存在或已删除！").setErrorData(filename));
}


}