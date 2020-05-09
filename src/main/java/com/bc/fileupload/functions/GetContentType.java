package com.bc.fileupload.functions;

import java.io.File;
import javax.servlet.http.HttpServletRequest;
import org.springframework.core.io.Resource;

/**
 * @author hp
 */
public interface GetContentType{

    String getContentType(HttpServletRequest request, Resource resource);

    String getContentType(HttpServletRequest request, Resource resource, String outputIfNone);

    String getContentTypeOrDefault(HttpServletRequest request, File file);

    String getContentTypeOrDefault(HttpServletRequest request, File file, String outputIfNone);

    String getDefaultContentType();
    
}
