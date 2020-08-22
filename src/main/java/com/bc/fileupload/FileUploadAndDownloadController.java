package com.bc.fileupload;

import com.bc.fileupload.functions.GetContentType;
import com.bc.fileupload.services.FileStorageHandler;
import java.util.List;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Chinomso Bassey Ikwuagwu on Mar 27, 2019 7:42:52 PM
 */
public class FileUploadAndDownloadController {

    private static final Logger LOG = LoggerFactory.getLogger(FileUploadAndDownloadController.class);

    private final FileStorageHandler delegate;
    private final GetContentType getContentType;

    @Autowired
    public FileUploadAndDownloadController(
            FileStorageHandler fileStorageHandler,
            GetContentType getContentType) {
        this.delegate = Objects.requireNonNull(fileStorageHandler);
        this.getContentType = Objects.requireNonNull(getContentType);
    }
    
    @PostMapping("/uploadFile")
    public UploadFileResponse uploadFile(@RequestParam("file") MultipartFile file) {
        return delegate.save(file);
    }

    @PostMapping("/uploadFiles")
    public List<UploadFileResponse> uploadFiles(@RequestParam("files") MultipartFile[] files) {
        return delegate.save(files);
    }

    @GetMapping(FileuploadConfigurationSource.DEFAULT_DOWNLOAD_CONTEXT_PATH + "/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
        
        LOG.debug("File name: {}", fileName);
        
        final Resource resource = delegate.loadFileAsResource(fileName);
        
        final String contentType = this.getContentType.getContentType(request, resource);
        
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}