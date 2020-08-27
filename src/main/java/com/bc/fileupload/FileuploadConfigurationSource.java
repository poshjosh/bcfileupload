package com.bc.fileupload;

import com.bc.fileupload.functions.GetContentType;
import com.bc.fileupload.functions.GetContentTypeImpl;
import com.bc.fileupload.functions.GetUniquePathForFilename;
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
                this.getUniquePathForFilename(),
                this.fileStorage(),
                this.getDownloadContextPath()
        );
    }
    
    public String getDownloadContextPath() {
        return DEFAULT_DOWNLOAD_CONTEXT_PATH;
    }
    
    @Bean public GetUniquePathForFilename getUniquePathForFilename() {
        final String prop = this.getDirectoryToSaveUploadedFiles();
        final Path path;
        if(prop == null || prop.isEmpty()) {
            path = Paths.get(System.getProperty("user.home"), ".bcfileupload");
        }else{
            path = Paths.get(prop);
        }
        final File file = path.toFile();
        if(file.exists()) {
            file.mkdirs();
        }
        return new GetUniquePathForFilenameImpl(path);
    }
    
    @Bean public FileStorage fileStorage() {
        return new LocalDiscStorage(this.saveHandler());
    }
    
    @Bean public SaveHandler saveHandler() {
        return new SaveHandlerImpl();
    }
}
