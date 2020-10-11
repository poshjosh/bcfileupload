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

package com.bc.fileupload.functions;

import com.bc.fileupload.exceptions.FileException;
import com.bc.fileupload.exceptions.FileStorageException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZonedDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

/**
 * @author Chinomso Bassey Ikwuagwu on Apr 9, 2019 9:20:46 AM
 */
public class GetUniquePathForFilenameImpl implements FilePathProvider {

    private static final Logger LOG = LoggerFactory.getLogger(GetUniquePathForFilenameImpl.class);
    
    private final Path baseDir;

    public GetUniquePathForFilenameImpl(Path baseDir) {
        this.baseDir = baseDir.toAbsolutePath().normalize();
        LOG.debug("File storage dir: {}", baseDir);
        this.createDirsIfNotExisting(this.baseDir);
    }
    
    @Override
    public FilePathProvider newInstance(Path baseDir) {
        return new GetUniquePathForFilenameImpl(baseDir);
    }

    @Override
    public Path getUniquePath(String arg) {
        
        final String filename = cleanAndValidate(arg);
        
        final Path dir = buildUniqueDir(this.baseDir.toString());
                
        this.createDirsIfNotExisting(dir);
        
        Path path = dir.resolve(filename);
        
        if(Files.exists(path)) {
            
            final String updatedname = buildUniqueFilename(filename);
            
            path = dir.resolve(updatedname);
        }
        
        LOG.debug("Input: {}, output: {}", arg, path);
        
        return path;
    }

    public String cleanAndValidate(String originalFileName) {
        
        // Normalize file name
        final String fileName = StringUtils.cleanPath(originalFileName);

        // Check if the file's name contains invalid characters
        if(fileName.contains("..")) {
            throw new FileException("Filename contains invalid path sequence " + fileName);
        }

        return fileName;    
    }
    
    public final Path createDirsIfNotExisting(Path path) {
        try {

            if( ! Files.exists(path)) {

                path = Files.createDirectories(path);
                
                LOG.info("Created dir: {}", path);
            }
            
            return path;
            
        } catch (IOException ex) {
            throw new FileStorageException(
                    "Could not create the directory: " + path, ex);
        }
    }
    
//    @Override
    public Path buildUniqueDir(String dir) {
        
        final ZonedDateTime zdt = ZonedDateTime.now();
        final int year = zdt.getYear();
        final int month = zdt.getMonth().getValue();
        final int day = zdt.getDayOfMonth();

        return Paths.get(dir, 
                year < 100 ? "00" + year : String.valueOf(year),
                month < 10 ? "0" + month : String.valueOf(month),
                day < 10 ? "0" + day : String.valueOf(day));
    }

//    @Override
    public String buildUniqueFilename(String filename) {
        
        final String ext = new GetExtension().apply(filename, null);

        final String H = Long.toHexString(System.currentTimeMillis());
        
        final String output;
        
        if(ext == null) {
            output = H + '_' + filename;
        }else{
            final int n = filename.indexOf(ext);
            output = filename.substring(0, n-1) + '_' + H + '.' + ext;
        }
        
        LOG.debug("From: {}, built unique filename: {}", filename, output);
        
        return output;
    }

    @Override
    public Path getBaseDir() {
        return baseDir;
    }
}
