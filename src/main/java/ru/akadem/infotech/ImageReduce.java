package ru.akadem.infotech;

import com.itextpdf.text.DocumentException;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageReduce extends Reducer {
    private final int width;
    private final int height;

    public ImageReduce(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public boolean manipulate(File src, File dest) throws DocumentException, IOException {
        BufferedImage bufferedImageInput = ImageIO.read(src);

        int widthInput = bufferedImageInput.getWidth();
        int heightInput = bufferedImageInput.getHeight();

        if (widthInput <= width) return false;
        double factor = (double) width / widthInput;
        int widthOutput = (int)(widthInput * factor);
        int heightOutput = (int)(heightInput * factor);
        BufferedImage bufferedImageOutput = new BufferedImage(widthOutput,
                heightOutput, Image.SCALE_DEFAULT);

        Graphics2D g2d = bufferedImageOutput.createGraphics();
        g2d.drawImage(bufferedImageInput, 0, 0, widthOutput, heightOutput, null);
        g2d.dispose();

        String formatName = dest.toString().substring(dest.toString().lastIndexOf(".") + 1);

        return ImageIO.write(bufferedImageOutput, formatName, dest);
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }
}
