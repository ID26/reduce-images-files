package ru.akadem.infoteck;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.parser.PdfImageObject;
import ru.akadem.infoteck.utils.GetFileSize;

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

public class PdfReduce implements Reducer {
    public static final float FACTOR = 0.50f;
    public static final int WIDTH = 1264; //поменять
    public static final int HEIGHT = 1753; //поменять
//    время к логу
//    ошибки

    @Override
    public void reduce(Path path, Map<String, Counter> counters, String key) {
        File fileSrc = new File(path.toString());
        String destFile = String.format("%s/temp.pdf", fileSrc.getParentFile().toString());
        File file = new File(destFile);
        file.getParentFile().mkdirs();
        try {
            manipulatePdf(path.toString(), file.toString());


            Double srcSizeKiloBytes = GetFileSize.getFileSizeKiloBytes(fileSrc);
            Double destSizeKiloBytes = GetFileSize.getFileSizeKiloBytes(file);

            insertingChangesToCounters(counters.get(key), srcSizeKiloBytes, destSizeKiloBytes);

        System.out.printf("Файл %s был ужат с %f KB до %f KB\n", fileSrc.getName(), srcSizeKiloBytes,
                destSizeKiloBytes);

            file.renameTo(fileSrc);
        } catch (Exception e) {
            e.printStackTrace(); // писать ошибку в лог
        }
    }

    private void insertingChangesToCounters(Counter counter, Double srcSizeKiloBytes, Double destSizeKiloBytes) {
        counter.incrementTotalFiles();
        counter.addFileSizeBeforeKb(srcSizeKiloBytes);
        counter.addFileSizeAfterKb(destSizeKiloBytes);
        if (srcSizeKiloBytes >= destSizeKiloBytes) {
            counter.incrementOfProcessedFiles();
        } else if (srcSizeKiloBytes <= destSizeKiloBytes) {
            counter.incrementIncreasedFile();
        } else {
            counter.incrementOfUnmodifiedFiles();
        }
    }

    public void manipulatePdf(String src, String dest) throws DocumentException, IOException {
        PdfReader reader = new PdfReader(src);
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
//            изменить коэфицент
            if (bi.getWidth() <= 1264) {
                continue;
            }
            Double factor =  1264 / (double) bi.getWidth();
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

    }
}
