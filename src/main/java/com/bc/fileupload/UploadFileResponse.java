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

package com.bc.fileupload;

import java.io.Serializable;
import java.util.Objects;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Chinomso Bassey Ikwuagwu on Mar 27, 2019 7:44:29 PM
 */
public class UploadFileResponse implements Serializable {
    
    private int responseCode;
    private String responseMessage;
    private String name;
    private String originalFileName;
    private String fileName;
    private String fileDownloadUri;
    private String fileType;
    private long size;

    public UploadFileResponse() {}

    public UploadFileResponse(int responseCode, String responseMessage,
            String savedTo, String fileDownloadUri, MultipartFile file) {
        this(
                responseCode, responseMessage, file.getName(), file.getOriginalFilename(),
                savedTo, fileDownloadUri, file.getContentType(), file.getSize());
    }
    
    public UploadFileResponse(int responseCode, String responseMessage,
            String name, String originalFileName, String savedTo, 
            String fileDownloadUri, String fileType, long size) {
        //@todo validate this
        this.responseCode = responseCode;
        this.responseMessage = Objects.requireNonNull(responseMessage);
        this.name = Objects.requireNonNull(name);
        this.originalFileName = Objects.requireNonNull(originalFileName);
        this.fileName = savedTo;
        this.fileDownloadUri = fileDownloadUri;
        this.fileType = fileType;
        this.size = size;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileDownloadUri() {
        return fileDownloadUri;
    }

    public void setFileDownloadUri(String fileDownloadUri) {
        this.fileDownloadUri = fileDownloadUri;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 13 * hash + this.responseCode;
        hash = 13 * hash + Objects.hashCode(this.responseMessage);
        hash = 13 * hash + Objects.hashCode(this.name);
        hash = 13 * hash + Objects.hashCode(this.originalFileName);
        hash = 13 * hash + Objects.hashCode(this.fileName);
        hash = 13 * hash + Objects.hashCode(this.fileDownloadUri);
        hash = 13 * hash + Objects.hashCode(this.fileType);
        hash = 13 * hash + (int) (this.size ^ (this.size >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final UploadFileResponse other = (UploadFileResponse) obj;
        if (this.responseCode != other.responseCode) {
            return false;
        }
        if (this.size != other.size) {
            return false;
        }
        if (!Objects.equals(this.responseMessage, other.responseMessage)) {
            return false;
        }
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.originalFileName, other.originalFileName)) {
            return false;
        }
        if (!Objects.equals(this.fileName, other.fileName)) {
            return false;
        }
        if (!Objects.equals(this.fileDownloadUri, other.fileDownloadUri)) {
            return false;
        }
        if (!Objects.equals(this.fileType, other.fileType)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "UploadFileResponse{" + "responseCode=" + responseCode + ", responseMessage=" + responseMessage + ", name=" + name + ", originalFileName=" + originalFileName + ", fileName=" + fileName + ", fileDownloadUri=" + fileDownloadUri + ", fileType=" + fileType + ", size=" + size + '}';
    }
}