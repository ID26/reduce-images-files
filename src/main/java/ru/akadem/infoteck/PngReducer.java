package ru.akadem.infoteck;


import com.itextpdf.text.DocumentException;

import javax.imageio.*;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

// Сжимает на 25% png
public class PngReducer extends Reducer {

    @Override
    public void reduce(Path path, Map<String, Counter> counters, String key, Reducer reducer) {
        super.reduce(path, counters, key, reducer);
    }

    @Override
    public boolean manipulate(String src, String dest) throws DocumentException, IOException {
        BufferedImage image = null;
        IIOMetadata metadata = null;

//        try (
                ImageInputStream in = ImageIO.createImageInputStream(Files.newInputStream(Paths.get(src)));
//        ) {
            ImageReader reader = ImageIO.getImageReadersByFormatName("png").next();
            reader.setInput(in, true, false);
            image = reader.read(0);
            metadata = reader.getImageMetadata(0);
//            System.out.print(" Метадата: " + metadata.getNativeMetadataFormatName() + "\n");
            reader.dispose();
//            image.setRGB(300, 300, 300);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

//            try (
                    ImageOutputStream out = ImageIO.createImageOutputStream(Files.newOutputStream(Paths.get(dest)));
//        ) {
                ImageTypeSpecifier type = ImageTypeSpecifier.createFromRenderedImage(image);
                ImageWriter writer = ImageIO.getImageWriters(type, "png").next();

                ImageWriteParam param = writer.getDefaultWriteParam();
                if (param.canWriteCompressed()) {
                    param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                    param.setCompressionQuality(0.0f);
                }

                writer.setOutput(out);
                writer.write(null, new IIOImage(image, null, metadata), param);
                writer.dispose();
//                System.out.println(" Высота: " + image.getHeight() + "Ширина: " + image.getWidth());
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
            return true;
    }


    @Override
    public int getWidth() {
        return 0;
    }

    @Override
    public int getHeight() {
        return 0;
    }
}
