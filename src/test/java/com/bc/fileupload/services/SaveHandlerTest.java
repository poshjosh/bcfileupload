package com.bc.fileupload.services;

import com.bc.imageutil.DrawConfigs;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.Test;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * @author hp
 */
public class SaveHandlerTest {

    @Test
    public void testSave() {
        System.out.println("save");
        final String imageName = "/banner.jpg";
        try{
            InputStream source = this.getInputStream(imageName);
            Path target = Paths.get(System.getProperty("java.io.tmpdir"), imageName);
            System.out.println("Saving image to: " + target);
            SaveHandler instance = this.getInstance();
            instance.save(source, target);
        }catch(IOException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }
    
    private InputStream getInputStream(String imageName) 
            throws IOException{
        return SaveHandlerTest.class.getResourceAsStream(imageName);
    }
    
    private SaveHandler getInstance() {
        final Font font = Font.decode(Font.MONOSPACED+"-PLAIN-16");
        return new SaveResizedImageWithVisualSignature(
                new Dimension(512, 512), "MyAwesomeSite.com")
                .withDrawConfig(DrawConfigs.fromBottomRight(0f, 0.05f, font, Color.BLACK));
    }
}
