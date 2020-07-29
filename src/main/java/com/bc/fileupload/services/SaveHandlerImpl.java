package com.bc.fileupload.services;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/**
 * @author chinomso ikwuagwu
 */
public class SaveHandlerImpl implements SaveHandler{
    
    @Override
    public void save(InputStream source, Path target) throws IOException{
        Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
    }
}
