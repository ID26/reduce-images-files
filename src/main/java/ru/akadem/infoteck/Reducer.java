package ru.akadem.infoteck;

import com.itextpdf.text.DocumentException;
import ru.akadem.infoteck.utils.GetFileSize;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

public abstract class Reducer {
//    private int width;
//    private int height;

    public void reduce(Path path, Map<String, Counter> counters, String key, Reducer reducer) {
        File fileSrc = new File(path.toString());
        String destFile = String.format("%s/temp.pdf", fileSrc.getParentFile().toString());
        File file = new File(destFile);
        file.getParentFile().mkdirs();
        try {
            boolean isChange = manipulate(path.toString(), file.toString());
            Double srcSizeKiloBytes = GetFileSize.getFileSizeKiloBytes(fileSrc);
            Double destSizeKiloBytes = GetFileSize.getFileSizeKiloBytes(file);
            boolean isChangeLow10Perc =  ((srcSizeKiloBytes - destSizeKiloBytes) / srcSizeKiloBytes) * 100 < 10;

            if (!isChange || isChangeLow10Perc) {
                ConsoleHelper.writeMessage(String.format("Файл %s уже имеет минимальный размер по ширине %d и высоте " +
                        "%d, объем занимаемой памяти %f", fileSrc.getName(), reducer.getWidth(), reducer.getHeight(),
                        srcSizeKiloBytes));
                file.delete();
                destSizeKiloBytes = srcSizeKiloBytes;
            } else {
                ConsoleHelper.writeMessage(String.format("Файл %s был изменен с %f KB до %f KB", fileSrc.getName(),
                        srcSizeKiloBytes, destSizeKiloBytes));
                file.renameTo(fileSrc);
            }
            insertingChangesToCounters(counters.get(key), srcSizeKiloBytes, destSizeKiloBytes);

        } catch (Exception e) {
            ConsoleHelper.writeMessage(String.format("При обработке файла %s произошла ошибка %s", file.getName(), e.getMessage()));
        }
    }

    private void insertingChangesToCounters(Counter counter, Double srcSizeKiloBytes, Double destSizeKiloBytes) {
        counter.incrementTotalFiles();
        counter.addFileSizeBeforeKb(srcSizeKiloBytes);
        counter.addFileSizeAfterKb(destSizeKiloBytes);
        if (srcSizeKiloBytes > destSizeKiloBytes) {
            counter.incrementOfProcessedFiles();
        } else if (srcSizeKiloBytes < destSizeKiloBytes) {
            counter.incrementIncreasedFile();
        } else {
            counter.incrementOfUnmodifiedFiles();
        }
    }

    public abstract boolean manipulate(String src, String dest) throws DocumentException, IOException;

    public abstract int getWidth();

    public abstract int getHeight();
}
