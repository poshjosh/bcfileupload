package com.bc.fileupload;

import com.bc.fileupload.functions.GetContentType;
import com.bc.fileupload.functions.GetUniquePathForFilename;
import com.bc.fileupload.services.FileStorage;
import com.bc.fileupload.services.FileStorageHandler;
import com.bc.fileupload.services.SaveHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * @author chinomso ikwuagwu
 */
@Configuration
@ConditionalOnMissingBean(FileuploadConfigurationSource.class)
public class FileuploadConfiguration {

    public static final String OUTPUT_DIR_PROPERTY_NAME = "bcfileupload.outputDir";
    
    private final FileuploadConfigurationSource delegate;
    
    public FileuploadConfiguration(Environment environment) {
        this.delegate = new FileuploadConfigurationSource(){
            @Override
            public String getDirectoryToSaveUploadedFiles() {
                return environment.getProperty(OUTPUT_DIR_PROPERTY_NAME);
            }
        };
    }

    @Bean public GetContentType getContentType() {
        return delegate.getContentType();
    }

    @Bean public FileStorageHandler fileStorageHandler() {
        return delegate.fileStorageHandler();
    }

    @Bean public GetUniquePathForFilename getUniquePathForFilename() {
        return delegate.getUniquePathForFilename();
    }

    @Bean public FileStorage fileStorage() {
        return delegate.fileStorage();
    }

    @Bean public SaveHandler saveHandler() {
        return delegate.saveHandler();
    }
}
