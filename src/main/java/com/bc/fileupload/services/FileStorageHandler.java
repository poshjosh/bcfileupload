/*
 * Copyright 2019 NUROX Ltd.
 *
 * Licensed under the NUROX Ltd Software License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.looseboxes.com/legal/licenses/software.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bc.fileupload.services;

import com.bc.fileupload.UploadFileResponse;
import com.bc.fileupload.exceptions.FileNotFoundExceptionForResponseCode404;
import com.bc.fileupload.exceptions.FileUploadExceptionForInternalServerError;
import com.bc.fileupload.functions.GetUniquePathForFilename;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.MalformedURLException;
import org.springframework.core.io.UrlResource;

/**
 * @author Chinomso Bassey Ikwuagwu on Mar 27, 2019 11:02:31 PM
 */
public class FileStorageHandler {

    private static final Logger LOG = LoggerFactory.getLogger(FileStorageHandler.class);
    
    private final GetUniquePathForFilename getPathForFilename;

    private final FileStorage fileStorage;
    
    private final String downloadPathContext;

    public FileStorageHandler(GetUniquePathForFilename getPathForFilename, 
            FileStorage fileStorage, String downloadPathContext) {
        this.getPathForFilename = Objects.requireNonNull(getPathForFilename);
        this.fileStorage = Objects.requireNonNull(fileStorage);
        this.downloadPathContext = Objects.requireNonNull(downloadPathContext);
    }
    
    public List<UploadFileResponse> uploadFiles(MultipartFile[] files) {
        
        LOG.debug("Uploading {} files", files == null ? null : files.length);
        
        return Arrays.asList(files)
                .stream()
                .map(file -> {
                    try{
                        
                        return uploadFile(file);
                        
                    }catch(RuntimeException e) {
                        
                        LOG.warn("Failed to upload: " + file, e);
                        
                        return getUploadFileFailureResponse(file);
                    }    
                })
                .collect(Collectors.toList());
    }
    
    public UploadFileResponse uploadFile(MultipartFile file) {

        LOG.debug("Uploading file: {} = {}", file.getName(), file.getOriginalFilename());
        
        final Path path = getPathForFilename.apply(file.getOriginalFilename());
        
        try{
            
            final Path targetLocation = fileStorage.store(path, file.getInputStream());

            final Path relativePath = getPathForFilename.getBaseDir().relativize(targetLocation);
            
            final String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path(downloadPathContext + "/")
                    .path(relativePath.toString().replace('\\', '/'))
                    .toUriString();

            LOG.debug("Uploaded file: {} to {}", file.getName(), relativePath);
    
            return getUploadFileSuccessResponse(
                    file, relativePath.toString(), fileDownloadUri);
            
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
                responseCode, responseMessage, file.getName(), file.getOriginalFilename(),
                savedTo, fileDownloadUri, file.getContentType(), file.getSize());
    }
    
    public ResponseEntity<Resource> downloadFile(String relativePath, HttpServletRequest request) {
        
        LOG.debug("Downloading file: {}", relativePath);
        
        // Load file as Resource
        final Resource resource = loadFileAsResource(relativePath);

        File file = null;
        try {
            file = resource.getFile();
        } catch (IOException ex) {
            final String msg = "Could not determine file type for file named: " + relativePath; 
            LOG.info(msg);
            LOG.debug(msg, ex);
        }
        
        final String contentType = file == null ? getDefaultContentType() : 
                getContentTypeOrDefault(request, file);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
    
    public String getDefaultContentType() {
        return ("application/octet-stream");
    }

    public String getContentTypeOrDefault(HttpServletRequest request, File file) {
        
        return getContentTypeOrDefault(request, file, getDefaultContentType());
    }

    public String getContentTypeOrDefault(HttpServletRequest request, File file, String outputIfNone) {
        
        // Try to determine file's content type
        String contentType = request.getServletContext().getMimeType(file.getAbsolutePath());

        if(contentType == null) {
            contentType = outputIfNone;
        }
        
        return contentType;
    }

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
