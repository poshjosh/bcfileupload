package com.bc.fileupload.services;

import com.bc.fileupload.UploadFileResponse;
import com.bc.fileupload.exceptions.FileNotFoundExceptionForResponseCode404;
import com.bc.fileupload.exceptions.FileUploadExceptionForInternalServerError;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.bc.fileupload.functions.FilePathProvider;

/**
 * @author Chinomso Bassey Ikwuagwu on Mar 27, 2019 11:02:31 PM
 */
public class FileStorageHandlerImpl implements FileStorageHandler{

    private static final Logger LOG = LoggerFactory.getLogger(FileStorageHandlerImpl.class);
    
    private final FilePathProvider getPathForFilename;

    private final FileStorage fileStorage;
    
    private final String downloadPathContext;

    public FileStorageHandlerImpl(FilePathProvider getPathForFilename, 
            FileStorage fileStorage, String downloadPathContext) {
        this.getPathForFilename = Objects.requireNonNull(getPathForFilename);
        this.fileStorage = Objects.requireNonNull(fileStorage);
        this.downloadPathContext = Objects.requireNonNull(downloadPathContext);
    }
    
    @Override
    public FileStorageHandler newInstance(Path baseDir) {
        return new FileStorageHandlerImpl(
                this.getPathForFilename.newInstance(baseDir),
                fileStorage,
                downloadPathContext
        );
    }

    @Override
    public boolean delete(Path path) {
        return this.fileStorage.delete(path);
    }
    
    @Override
    public List<UploadFileResponse> save(MultipartFile[] files) {
        
        LOG.debug("Uploading {} files", files == null ? null : files.length);
        
        return Arrays.asList(files)
                .stream()
                .map(file -> {
                    try{
                        
                        return save(file);
                        
                    }catch(RuntimeException e) {
                        
                        LOG.warn("Failed to upload: " + file, e);
                        
                        return getUploadFileFailureResponse(file);
                    }    
                })
                .collect(Collectors.toList());
    }
    
    @Override
    public UploadFileResponse save(MultipartFile file) {

        LOG.debug("Uploading file: {} = {}", file.getName(), file.getOriginalFilename());
        
        final Path path = getPathForFilename.getUniquePath(file.getOriginalFilename());
        
        try{
            
            final Path targetLocation = fileStorage.store(path, file.getInputStream());

            final Path relativePath = getPathForFilename.getBaseDir().relativize(targetLocation);
            
            final String pathStr = relativePath.toString().replace('\\', '/');
            
            final String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path(downloadPathContext + "/")
                    .path(pathStr)
                    .toUriString();

            LOG.debug("Uploaded file: {} to {}", file.getName(), pathStr);
    
            return getUploadFileSuccessResponse(file, pathStr, fileDownloadUri);
            
        }catch(IOException ex) {
        
            throw new FileUploadExceptionForInternalServerError(
                    "Failed to upload: " + file.getOriginalFilename(), ex);
        }
    }
    
    public UploadFileResponse getUploadFileFailureResponse(MultipartFile file) {
        return this.getUploadFileResponse(
                HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error", file, "", "");
    }

    public UploadFileResponse getUploadFileSuccessResponse(
            MultipartFile file, String savedTo, String fileDownloadUri) {
        return this.getUploadFileResponse(
                HttpServletResponse.SC_CREATED, "Success", file, savedTo, fileDownloadUri);
    }
    
    public UploadFileResponse getUploadFileResponse(
            int responseCode, String responseMessage,
            MultipartFile file, String savedTo, String fileDownloadUri) {
        return new UploadFileResponse(
                responseCode, responseMessage, savedTo, fileDownloadUri, file);
    }
    
    @Override
    public Resource loadFileAsResource(String relativePath) {
        try {
            
            final Path filePath = this.resolve(relativePath);
            
            final Resource resource = new UrlResource(filePath.toUri());
            
            if(resource.exists()) {
                return resource;
            } else {
                throw new FileNotFoundExceptionForResponseCode404("File not found " + relativePath);
            }
        } catch (MalformedURLException ex) {
            throw new FileNotFoundExceptionForResponseCode404("File not found " + relativePath, ex);
        }
    }

    public Path resolve(String relativePath) {
        return this.getPathForFilename.getBaseDir().resolve(relativePath);
    }
}
