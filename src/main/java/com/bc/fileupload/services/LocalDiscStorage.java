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

import com.bc.fileupload.exceptions.FileStorageException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Chinomso Bassey Ikwuagwu on Mar 27, 2019 10:43:15 PM
 */
public class LocalDiscStorage implements FileStorage<Path> {

    private static final Logger LOG = LoggerFactory.getLogger(LocalDiscStorage.class);
    
    private final SaveHandler saveHandler;

    public LocalDiscStorage() {
        this(new SaveHandlerImpl());
    }
    
    public LocalDiscStorage(SaveHandler saveHandler) {
        this.saveHandler = Objects.requireNonNull(saveHandler);
    }

    @Override
    public Path store(Path path, InputStream in) {
        
        try {
            this.saveHandler.save(in, path);

            return path;
            
        } catch (IOException ex) {
            
            throw new FileStorageException("Could not store file to: " + path + 
                    ". Please try again!", ex);
        }
    }

    @Override
    public boolean delete(Path path) {
        // @TODO
        // Walk through files to local disc and delete orphans (i.e those
        // without corresponding database entry), aged more than a certain
        // limit, say 24 hours.s
        boolean deleted = false;
        try{
            deleted = Files.deleteIfExists(path);
            if( ! deleted) {
                LOG.info("Will delete on exit: {}", path);
                path.toFile().deleteOnExit();
            }
        }catch(IOException e) {
            LOG.warn("Problem deleting: " + path, e);
            LOG.info("Will delete on exit: {}", path);
            path.toFile().deleteOnExit();
        }
        
        return deleted;
    }
}
