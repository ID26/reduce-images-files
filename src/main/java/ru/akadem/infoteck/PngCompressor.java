package ru.akadem.infoteck;


import javax.imageio.*;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

// Сжимает на 25% png
public class PngCompressor implements Compressor {

    @Override
    public void compress(String fileName) {
        String inputPath = fileName;
        String outputPath = fileName;

        BufferedImage image = null;
        IIOMetadata metadata = null;

        try (ImageInputStream in = ImageIO.createImageInputStream(Files.newInputStream(Paths.get(inputPath)))) {
            ImageReader reader = ImageIO.getImageReadersByFormatName("png").next();
            reader.setInput(in, true, false);
            image = reader.read(0);
            metadata = reader.getImageMetadata(0);
//            System.out.print(" Метадата: " + metadata.getNativeMetadataFormatName() + "\n");
            reader.dispose();
//            image.setRGB(300, 300, 300);
        } catch (IOException e) {
            e.printStackTrace();
        }

            try (ImageOutputStream out = ImageIO.createImageOutputStream(Files.newOutputStream(Paths.get(outputPath)))) {
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
            } catch (IOException e) {
                e.printStackTrace();
            }

    }
}
