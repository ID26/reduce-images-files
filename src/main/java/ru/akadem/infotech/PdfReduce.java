package ru.akadem.infotech;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.parser.PdfImageObject;
import ru.akadem.infotech.utils.Counter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

public class PdfReduce extends Reducer {

    private final int width;

    public PdfReduce(int width) {
        this.width = width;
    }

    @Override
    public void reduce(Path path, Map<String, Counter> counters, String key, Reducer reducer) {
        super.reduce(path, counters, key, reducer);
    }

    @Override
    public boolean manipulate(File src, File dest) throws DocumentException, IOException {
        PdfReader reader = new PdfReader(src.toString());
        int n = reader.getXrefSize();
        PdfObject object;
        PRStream stream;
        // Look for image and manipulate image stream
        for (int i = 0; i < n; i++) {
            object = reader.getPdfObject(i);
            if (object == null || !object.isStream())
                continue;
            stream = (PRStream)object;
            if (!PdfName.IMAGE.equals(stream.getAsName(PdfName.SUBTYPE)))
                continue;
            if (!PdfName.DCTDECODE.equals(stream.getAsName(PdfName.FILTER)))
                continue;
            PdfImageObject image = new PdfImageObject(stream);
            BufferedImage bi = image.getBufferedImage();
            if (bi == null)
                continue;

            if (bi.getWidth() <= width /*&& bi.getHeight() <= HEIGHT*/) {
                return false;
            }
            double factor =  width / (double) bi.getWidth();
            int width = (int)(bi.getWidth() * factor);
            int height = (int)(bi.getHeight() * factor);
            if (bi.getWidth() <= 0 || bi.getHeight() <= 0)
                continue;
            BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            AffineTransform at = AffineTransform.getScaleInstance(factor, factor);
            Graphics2D g = img.createGraphics();
            g.drawRenderedImage(bi, at);
            ByteArrayOutputStream imgBytes = new ByteArrayOutputStream();
            ImageIO.write(img, "JPG", imgBytes);
            stream.clear();
            stream.setData(imgBytes.toByteArray(), false, PRStream.NO_COMPRESSION);
            stream.put(PdfName.TYPE, PdfName.XOBJECT);
            stream.put(PdfName.SUBTYPE, PdfName.IMAGE);
            stream.put(PdfName.FILTER, PdfName.DCTDECODE);
            stream.put(PdfName.WIDTH, new PdfNumber(width));
            stream.put(PdfName.HEIGHT, new PdfNumber(height));
            stream.put(PdfName.BITSPERCOMPONENT, new PdfNumber(8));
            stream.put(PdfName.COLORSPACE, PdfName.DEVICERGB);
        }
        reader.removeUnusedObjects();

        // Save altered PDF
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));
        stamper.setFullCompression();
        stamper.close();
        reader.close();
        return true;
    }

    public int getWidth() {
        return width;
    }
}
