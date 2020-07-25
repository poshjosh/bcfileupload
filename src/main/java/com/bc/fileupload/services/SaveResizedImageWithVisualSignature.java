package com.bc.fileupload.services;

import com.bc.imageutil.ImageDimensions;
import com.bc.imageutil.ImageOverlay;
import com.bc.imageutil.ImageRescaler;
import com.bc.imageutil.impl.ImageRescalerImpl;
import com.bc.imageutil.impl.OverlayImageWithText;
import com.bc.imageutil.Util;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Objects;

/**
 * @author hp
 */
public class SaveResizedImageWithVisualSignature implements SaveHandler{
    
    private final Dimension preferredSize;
    
    private final String signature;
    
    private final com.bc.imageutil.ImageReader imageReader;
    
    private final ImageOverlay imageOverlay;
    
    private final ImageRescaler imageRescaler;

    public SaveResizedImageWithVisualSignature(
            Dimension preferredSize, String signature) {
        this(preferredSize, signature, 
                new com.bc.imageutil.impl.ImageReaderImpl(),
                new OverlayImageWithText(), new ImageRescalerImpl());
    }
    
    public SaveResizedImageWithVisualSignature(
            Dimension preferredSize, String signature,
            com.bc.imageutil.ImageReader imageReader, 
            ImageOverlay imageOverlay, ImageRescaler imageRescaler) {
        this.preferredSize = Objects.requireNonNull(preferredSize);
        this.signature = Objects.requireNonNull(signature);
        this.imageReader = Objects.requireNonNull(imageReader);
        this.imageOverlay = Objects.requireNonNull(imageOverlay);
        this.imageRescaler = Objects.requireNonNull(imageRescaler);
    }

    @Override
    public void save(InputStream source, Path target) throws IOException {
        
        final Path filename = target.getFileName();
        
        final String fileExtension = Util.getExtension(filename.toString(), null);
        Objects.requireNonNull(fileExtension);
        
        final BufferedImage buffImage = imageReader.read(source, fileExtension);

        if(signature != null) {
            imageOverlay.drawString(buffImage, signature);
        }

        final Dimension suggested = ImageDimensions
                .getSuggestedDimension(buffImage, preferredSize);
        
        final int width = suggested.width;
        final int height = suggested.height;
        
        final BufferedImage scaledImg = imageRescaler
                .scaleImage(buffImage, width, height, BufferedImage.TYPE_INT_RGB);

        boolean saved = javax.imageio.ImageIO.write(scaledImg, fileExtension, target.toFile());
    }
}
