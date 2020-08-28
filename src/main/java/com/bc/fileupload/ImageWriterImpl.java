package com.bc.fileupload;

import java.awt.image.RenderedImage;
import java.io.IOException;
import java.nio.file.Path;
import javax.imageio.ImageIO;

/**
 * @author hp
 */
public class ImageWriterImpl implements ImageWriter{

    @Override
    public boolean write(RenderedImage image, String formatName, Path to) throws IOException {
        return ImageIO.write(image, formatName, to.toFile());
    }
}
