package com.bc.fileupload;

import com.bc.fileupload.functions.GetContentType;
import com.bc.fileupload.functions.GetContentTypeImpl;
import com.bc.fileupload.functions.GetUniquePathForFilenameImpl;
import com.bc.fileupload.services.FileStorage;
import com.bc.fileupload.services.FileStorageHandler;
import com.bc.fileupload.services.FileStorageHandlerImpl;
import com.bc.fileupload.services.SaveHandler;
import com.bc.fileupload.services.SaveHandlerImpl;
import com.bc.fileupload.services.LocalDiscStorage;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.context.annotation.Bean;
import com.bc.fileupload.functions.FilePathProvider;

/**
 * @author chinomso ikwuagwu
 */
public abstract class FileuploadConfigurationSource {
    
    public static final String DEFAULT_DOWNLOAD_CONTEXT_PATH = "/files";
    
    public FileuploadConfigurationSource() {}

    public abstract String getDirectoryToSaveUploadedFiles();
    
    @Bean public GetContentType getContentType() {
        return new GetContentTypeImpl();
    }
    
    @Bean public FileStorageHandler fileStorageHandler() {
        return new FileStorageHandlerImpl(
                this.filePathProvider(),
                this.fileStorage(),
                this.getDownloadContextPath()
        );
    }
    
    public String getDownloadContextPath() {
        return DEFAULT_DOWNLOAD_CONTEXT_PATH;
    }
    
    @Bean public FilePathProvider filePathProvider() {
        final String prop = this.getDirectoryToSaveUploadedFiles();
        final Path dirToSaveUploads;
        if(prop == null || prop.isEmpty()) {
            dirToSaveUploads = Paths.get(System.getProperty("user.home"), ".bcfileupload");
        }else{
            dirToSaveUploads = Paths.get(prop);
        }
        return new GetUniquePathForFilenameImpl(dirToSaveUploads);
    }
    
    @Bean public FileStorage fileStorage() {
        return new LocalDiscStorage(this.saveHandler());
    }
    
    @Bean public SaveHandler saveHandler() {
        return new SaveHandlerImpl();
    }
}
