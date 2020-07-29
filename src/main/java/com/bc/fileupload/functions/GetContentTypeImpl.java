package com.bc.fileupload.functions;

import java.io.File;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

/**
 * @author chinomso ikwuagwu
 */
public class GetContentTypeImpl implements GetContentType{
    
    private static final Logger LOG = LoggerFactory.getLogger(GetContentTypeImpl.class);

    @Override
    public String getDefaultContentType() {
        return ("application/octet-stream");
    }

    @Override
    public String getContentType(HttpServletRequest request, Resource resource) {
        return this.getContentType(request, resource, this.getDefaultContentType());
    }
    
    @Override
    public String getContentType(HttpServletRequest request, Resource resource, String outputIfNone) {
        File file = null;
        try {
            file = resource.getFile();
        } catch (IOException ex) {
            final String msg = "Could not access file for resource: " + resource; 
            LOG.info(msg);
            LOG.debug(msg, ex);
        }
        final String contentType = file == null ? outputIfNone : 
                getContentTypeOrDefault(request, file, outputIfNone);
        return contentType;
    }

    @Override
    public String getContentTypeOrDefault(HttpServletRequest request, File file) {
        
        return getContentTypeOrDefault(request, file, getDefaultContentType());
    }

    @Override
    public String getContentTypeOrDefault(HttpServletRequest request, File file, String outputIfNone) {
        
        // Try to determine file's content type
        String contentType = request.getServletContext().getMimeType(file.getAbsolutePath());

        if(contentType == null) {
            contentType = outputIfNone;
        }
        
        return contentType;
    }
}
