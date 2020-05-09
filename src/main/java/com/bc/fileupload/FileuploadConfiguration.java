package com.bc.fileupload;

import com.bc.fileupload.functions.GetContentType;
import com.bc.fileupload.functions.GetContentTypeImpl;
import com.bc.fileupload.functions.GetUniquePathForFilename;
import com.bc.fileupload.functions.GetUniquePathForFilenameImpl;
import com.bc.fileupload.services.FileStorage;
import com.bc.fileupload.services.FileStorageHandler;
import com.bc.fileupload.services.FileStorageHandlerImpl;
import com.bc.fileupload.services.StoreFileToLocalDisc;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * @author hp
 */
@Configuration
public class FileuploadConfiguration {
    
    public static final String DOWNLOAD_PATH_CONTEXT = "/downloadFile";
    public static final String OUTPUT_DIR_PROPERTY_NAME = "bcfileupload.outputDir";
    
    @Autowired private Environment env;
    
    @Bean public GetContentType getContentType() {
        return new GetContentTypeImpl();
    }
    
    @Bean public FileStorageHandler fileStorageHandler() {
        return new FileStorageHandlerImpl(
                this.getUniquePathForFilename(),
                this.fileStorage(),
                DOWNLOAD_PATH_CONTEXT
        );
    }
    
    @Bean public GetUniquePathForFilename getUniquePathForFilename() {
        final String prop = env.getProperty(OUTPUT_DIR_PROPERTY_NAME);
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
        return new StoreFileToLocalDisc();
    }
}
