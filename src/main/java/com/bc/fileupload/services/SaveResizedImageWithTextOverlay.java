package com.bc.fileupload.services;

import com.bc.fileupload.ImageWriter;
import com.bc.fileupload.ImageWriterImpl;
import com.bc.imageutil.DrawConfig;
import com.bc.imageutil.DrawConfigs;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author chinomso ikwuagwu
 */
public class SaveResizedImageWithTextOverlay implements SaveHandler{
    
    private static final Logger LOG = LoggerFactory.getLogger(SaveResizedImageWithTextOverlay.class);
    
    private final Dimension preferredSize;
    
    private final String signature;
    
    private final com.bc.imageutil.ImageReader imageReader;
    
    private final ImageOverlay imageOverlay;
    
    private final ImageRescaler imageRescaler;
    
    private final DrawConfig drawConfig;
    
    private final ImageWriter imageWriter;

    public SaveResizedImageWithTextOverlay() {
        this(null, null);
    }
    
    public SaveResizedImageWithTextOverlay(
            Dimension preferredSize, String signature) {
        this(preferredSize, signature, 
                new com.bc.imageutil.impl.ImageReaderImpl(),
                new OverlayImageWithText(), new ImageRescalerImpl(),
                DrawConfigs.centre(), new ImageWriterImpl());
    }
    
    public SaveResizedImageWithTextOverlay(
            Dimension preferredSize, String signature,
            com.bc.imageutil.ImageReader imageReader, ImageOverlay imageOverlay, 
            ImageRescaler imageRescaler, DrawConfig drawConfig,
            ImageWriter imageWriter) {
        this.preferredSize = preferredSize;
        this.signature = Objects.requireNonNull(signature);
        this.imageReader = Objects.requireNonNull(imageReader);
        this.imageOverlay = Objects.requireNonNull(imageOverlay);
        this.imageRescaler = Objects.requireNonNull(imageRescaler);
        this.drawConfig = Objects.requireNonNull(drawConfig);
        this.imageWriter = Objects.requireNonNull(imageWriter);
    }
    
    public SaveResizedImageWithTextOverlay withImageReader(com.bc.imageutil.ImageReader imageReader) {
        return new SaveResizedImageWithTextOverlay(
                this.preferredSize, this.signature, imageReader, 
                this.imageOverlay, this.imageRescaler, this.drawConfig, this.imageWriter);
    }

    public SaveResizedImageWithTextOverlay withImageOverlay(ImageOverlay imageOverlay) {
        return new SaveResizedImageWithTextOverlay(
                this.preferredSize, this.signature, this.imageReader, 
                imageOverlay, this.imageRescaler, this.drawConfig, this.imageWriter);
    }

    public SaveResizedImageWithTextOverlay withImageRescaler(ImageRescaler imageRescaler) {
        return new SaveResizedImageWithTextOverlay(
                this.preferredSize, this.signature, this.imageReader, 
                this.imageOverlay, imageRescaler, this.drawConfig, this.imageWriter);
    }

    public SaveResizedImageWithTextOverlay withDrawConfig(DrawConfig drawConfig) {
        return new SaveResizedImageWithTextOverlay(
                this.preferredSize, this.signature, this.imageReader, 
                this.imageOverlay, this.imageRescaler, drawConfig, this.imageWriter);
    }

    public SaveResizedImageWithTextOverlay withImageWriter(ImageWriter imageWriter) {
        return new SaveResizedImageWithTextOverlay(
                this.preferredSize, this.signature, this.imageReader, 
                this.imageOverlay, this.imageRescaler, this.drawConfig, imageWriter);
    }

    @Override
    public void save(InputStream source, Path target) throws IOException {
        
        final Path filename = target.getFileName();
        
        final String fileExtension = Util.getExtension(filename.toString(), null);
        Objects.requireNonNull(fileExtension);
        
        LOG.trace("File, extension: {}, name: {}", fileExtension, filename);
        
        final BufferedImage image = imageReader.read(source, fileExtension);

        final BufferedImage scaledImage;
        if(preferredSize == null) {
            scaledImage = image;
        }else{
            final Dimension targetSize = ImageDimensions
                    .getSuggestedDimension(image, preferredSize);
            final int width = targetSize.width;
            final int height = targetSize.height;
            scaledImage = imageRescaler
                    .scaleImage(image, width, height, BufferedImage.TYPE_INT_RGB);
//            LOG.trace("Scaled image from: {}, to: {}", new Dimension(image.getWidth(), image.getHeight()), targetSize);
        }

        if(signature != null) {
            imageOverlay.drawString(scaledImage, signature, this.drawConfig);
            LOG.trace("Drawn signature: {}", signature);
        }
        
        this.imageWriter.write(scaledImage, fileExtension, target);
    }
}
