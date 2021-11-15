package com.bc.fileupload;

import java.awt.image.RenderedImage;
import java.io.IOException;
import java.nio.file.Path;

/**
 * @author hp
 */
public interface ImageWriter {
    
    boolean write(RenderedImage image, String formatName, Path to) throws IOException;
}
