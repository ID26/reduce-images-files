package ru.akadem.infotech;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageReduce extends Reducer {
    private final int width;

    public ImageReduce(int width) {
        this.width = width;
    }

    @Override
    public boolean manipulate(File src, File dest) throws IOException {
        BufferedImage bufferedImageInput = ImageIO.read(src);

        int requiredSizeX = bufferedImageInput.getWidth();
        int requiredSizeY = bufferedImageInput.getHeight();

        int smallerSide = Math.min(requiredSizeX, requiredSizeY);

        if (smallerSide <= width) return false;
        double factor = (double) width / smallerSide;
        int widthOutput = (int)(requiredSizeX * factor);
        int heightOutput = (int)(requiredSizeY * factor);
        BufferedImage bufferedImageOutput = new BufferedImage(widthOutput,
                heightOutput, Image.SCALE_DEFAULT);

        Graphics2D g2d = bufferedImageOutput.createGraphics();
        try {
            g2d.drawImage(bufferedImageInput, 0, 0, widthOutput, heightOutput, null);
        } finally {
            g2d.dispose();
        }

        String formatName = dest.toString().substring(dest.toString().lastIndexOf(".") + 1);


        return ImageIO.write(bufferedImageOutput, formatName, dest);
    }

    @Override
    public int getWidth() {
        return width;
    }
}
