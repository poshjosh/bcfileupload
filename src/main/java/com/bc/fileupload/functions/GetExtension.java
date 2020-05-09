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

import java.util.function.BiFunction;

/**
 * @author Chinomso Bassey Ikwuagwu on Apr 9, 2019 10:10:58 AM
 */
public class GetExtension implements BiFunction<String, String, String>{

    @Override
    public String apply(String path, String resultIfNone) {
        
        final int len = path.length();

        final int b = path.lastIndexOf('?', len-1);
        
        final int a = path.lastIndexOf('.', len-1); 
        
        final String ext = a == -1 ? null : path.substring(a + 1, (b == -1 ? len : b) );
        
        return ext == null ? resultIfNone : ext;
    }
}
