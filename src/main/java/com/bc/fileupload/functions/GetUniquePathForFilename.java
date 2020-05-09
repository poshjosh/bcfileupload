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

import java.nio.file.Path;
import java.util.function.Function;

/**
 * @author Chinomso Bassey Ikwuagwu on Apr 9, 2019 10:42:13 AM
 */
public interface GetUniquePathForFilename extends Function<String, Path> {

    @Override
    Path apply(String filename);
    
    GetUniquePathForFilename newInstance(Path baseDir);

    Path getBaseDir();
}
