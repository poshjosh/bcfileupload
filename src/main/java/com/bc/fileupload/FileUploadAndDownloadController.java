package com.bc.fileupload;

import com.bc.fileupload.functions.GetUniquePathForFilename;
import com.bc.fileupload.services.FileStorage;
import com.bc.fileupload.services.FileStorageHandler;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
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

    public static final String DOWNLOAD_PATH_CONTEXT = "/downloadFile";

    private final FileStorageHandler delegate;

    public FileUploadAndDownloadController(
            @Autowired GetUniquePathForFilename getPathForFilename, 
            @Autowired FileStorage fileStorage) {
        this.delegate = new FileStorageHandler(
                getPathForFilename, fileStorage, DOWNLOAD_PATH_CONTEXT);
    }
    
    @PostMapping("/uploadFile")
    public UploadFileResponse uploadFile(@RequestParam("file") MultipartFile file) {
        return delegate.uploadFile(file);
    }

    @PostMapping("/uploadFiles")
    public List<UploadFileResponse> uploadFiles(@RequestParam("files") MultipartFile[] files) {
        return delegate.uploadFiles(files);
    }

    @GetMapping(DOWNLOAD_PATH_CONTEXT + "/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
        LOG.info("File name: {}", fileName);
        return delegate.downloadFile(fileName, request);
    }
}