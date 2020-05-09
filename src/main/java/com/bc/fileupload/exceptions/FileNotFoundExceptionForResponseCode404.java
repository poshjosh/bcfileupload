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

package com.bc.fileupload.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Chinomso Bassey Ikwuagwu on Mar 27, 2019 7:50:11 PM
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class FileNotFoundExceptionForResponseCode404 extends FileException {

    public FileNotFoundExceptionForResponseCode404() {
    }

    public FileNotFoundExceptionForResponseCode404(String message) {
        super(message);
    }

    public FileNotFoundExceptionForResponseCode404(String message, Throwable cause) {
        super(message, cause);
    }

    public FileNotFoundExceptionForResponseCode404(Throwable cause) {
        super(cause);
    }

    public FileNotFoundExceptionForResponseCode404(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
    
}