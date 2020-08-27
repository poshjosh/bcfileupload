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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

/**
 * @author Chinomso Bassey Ikwuagwu on Apr 3, 2019 5:14:29 PM
 */
public interface FileStorage<K> {
    
    default Path store(K key, byte [] bytes) {
        try(final ByteArrayInputStream in = new ByteArrayInputStream(bytes)) {
            return store(key, in);
        }catch(IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    Path store(K key, InputStream in);
    
    InputStream load(K key);
    
    boolean delete(Path path);
}
