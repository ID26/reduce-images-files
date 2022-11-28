package ru.akadem.infotech;

import com.itextpdf.text.DocumentException;
import ru.akadem.infotech.utils.ConsoleHelper;
import ru.akadem.infotech.utils.Counter;
import ru.akadem.infotech.utils.GetFileSize;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Map;

public abstract class Reducer {

    public static final double MIN_PROCESSABLE_FILESIZE_KB = 200;

    public void reduce(Path path, Map<String, Counter> counters, String key, Reducer reducer) {
        File fileSrc = new File(path.toString());
        String destFile = String.format("%s_temp%s", fileSrc.getAbsolutePath().substring(0,
                fileSrc.getAbsolutePath().length() - key.length()), key);
        File fileDest = new File(destFile);
        fileDest.getParentFile().mkdirs();
        try {
            Double srcSizeKiloBytes = GetFileSize.getFileSizeKiloBytes(fileSrc);
            boolean isChange = srcSizeKiloBytes > MIN_PROCESSABLE_FILESIZE_KB && manipulate(fileSrc, fileDest);
            Double destSizeKiloBytes = GetFileSize.getFileSizeKiloBytes(fileDest);
            boolean isChangeLow10Perc =  ((srcSizeKiloBytes - destSizeKiloBytes) / srcSizeKiloBytes) * 100 < 10;

            if (!isChange || isChangeLow10Perc) {
                ConsoleHelper.writeMessage(String.format("Файл %s уже имеет минимальный размер по ширине %d, объем занимаемой памяти %f KB"
                        , fileSrc.getAbsolutePath(), reducer.getWidth(), srcSizeKiloBytes));
                if (Files.exists(fileDest.toPath())) {
                    Files.delete(fileDest.toPath());
                }
                destSizeKiloBytes = srcSizeKiloBytes;
            } else {
                ConsoleHelper.writeMessage(String.format("Файл %s был изменен с %f KB до %f KB", fileSrc.getAbsolutePath(),
                        srcSizeKiloBytes, destSizeKiloBytes));

                if(destSizeKiloBytes.equals(0) || destSizeKiloBytes < 0.000001) {
                    ConsoleHelper.writeMessage(String.format("Файл %s был изменен до %f KB. Это ошибка. Оригинальный файл не удален.", fileSrc.getAbsolutePath(),
                            srcSizeKiloBytes, destSizeKiloBytes));
                } else {
                    Files.move(fileDest.toPath(), fileSrc.toPath(), StandardCopyOption.REPLACE_EXISTING);
                }
            }
            insertingChangesToCounters(counters.get(key), srcSizeKiloBytes, destSizeKiloBytes);

        } catch (Throwable e) {
            ConsoleHelper.writeMessage(String.format("При обработке файла %s произошла ошибка %s: %s", fileSrc.getAbsolutePath(),
                    e.getClass().getName(), e.getMessage()));
            e.printStackTrace();
            insertingErrors(counters.get(key));
        } finally {
            if (Files.exists(fileDest.toPath())) {
                try {
                    Files.delete(fileDest.toPath());
                } catch (IOException e) {
                    ConsoleHelper.writeMessage(String.format("При удалении файла %s произошла ошибка %s", fileDest.getAbsolutePath(),
                            e.getMessage()));
                }
            }
        }
    }

    private void insertingChangesToCounters(Counter counter, Double srcSizeKiloBytes, Double destSizeKiloBytes) {
        counter.incrementTotalFiles();
        counter.addFileSizeBeforeKb(srcSizeKiloBytes);
        counter.addFileSizeAfterKb(destSizeKiloBytes);
        if (srcSizeKiloBytes > destSizeKiloBytes) {
            counter.incrementOfProcessedFiles();
        } else {
            counter.incrementOfUnmodifiedFiles();
        }
    }

    private void insertingErrors(Counter counter) {
        counter.incrementTotalFiles();
        counter.incrementTotalErrors();
        counter.incrementOfUnmodifiedFiles();
    }

    public abstract boolean manipulate(File src, File dest) throws DocumentException, IOException;

    public abstract int getWidth();
}
