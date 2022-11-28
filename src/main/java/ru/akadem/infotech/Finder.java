package ru.akadem.infotech;

import ru.akadem.infotech.utils.ConsoleHelper;
import ru.akadem.infotech.utils.Counter;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static java.nio.file.FileVisitResult.CONTINUE;

public class Finder extends SimpleFileVisitor<Path> {

    private static final Map<String, Counter> counters = new HashMap<>();
    private static final Map<String, Reducer> reducerMap = new HashMap<>();

    private static LocalDateTime lastPrintStatistic;

    static {
        reducerMapInit();
        countersMapInit();
    }

    private static void countersMapInit() {
        reducerMap.forEach((key, value) ->
                counters.put(key,
                        new Counter(key,
                                0L,
                                0L,
                                0L,
                                0L,
                                BigDecimal.ZERO,
                                BigDecimal.ZERO)));
    }

    private static void reducerMapInit() {
        reducerMap.put(".pdf", new PdfReduce(1264));
        reducerMap.put(".png", new ImageReduce(1264));
        reducerMap.put(".jpg", new ImageReduce(1264));
        reducerMap.put(".jpeg", new ImageReduce(1264));
        reducerMap.put(".tiff", new ImageReduce(1264));
        reducerMap.put(".tif", new ImageReduce(1264));
        reducerMap.put(".gif", new ImageReduce(1264));
        reducerMap.put(".bmp", new ImageReduce(1264));
    }

    // Compares the glob pattern against
    // the file or directory name.
    void find(Path file) {

        System.out.println("fileName " + file.getFileName());

        Path name = file.getFileName();
        for (Map.Entry<String, Reducer> entry : reducerMap.entrySet()) {
            if (name != null && name.toString().toLowerCase().endsWith(entry.getKey())) {
                Reducer reducer = entry.getValue();
                reducer.reduce(file, counters, entry.getKey(), reducer);

                // выводим статистику каждый час - потому что может долго очень идти - неделями. очень нужна промежуточная статистика.
                if (lastPrintStatistic == null || Duration.between(lastPrintStatistic, LocalDateTime.now()).toMinutes() >= 60) {
                    lastPrintStatistic = LocalDateTime.now();
                    printStatistic();
                }
            }
        }
    }

    public static void printStatistic() {
        ConsoleHelper.writeMessage("\n");
        for (Counter counter : counters.values()) {
            ConsoleHelper.writeMessage(String.format("Всего обработано %d файлов формата %s, из которых сжато - %d, " +
                            " пропущено - %d, ошибок - %d. Общий размер файлов до обработки -" + " %s KB, после обработки - %s KB",
                    counter.getTotalFiles(),
                    counter.getName(),
                    counter.getQuantityOfProcessedFiles(),
                    counter.getQuantityOfUnmodifiedFiles(),
                    counter.getTotalErrors(),
                    counter.getTotalSizeBeforeModifier(),
                    counter.getTotalSizeAfterModifier()
                   ));
        }
        ConsoleHelper.writeMessage("\n");
        Counter totalCounter = Counter.getTotalCounter(counters);
        ConsoleHelper.writeMessage(String.format("Всего обработано %d файлов, из которых сжато - %d, пропущено - %d, ошибок - %d. " +
                        "Общий размер файлов до обработки - %s KB, после обработки - %s KB",
                totalCounter.getTotalFiles(),
                totalCounter.getQuantityOfProcessedFiles(),
                totalCounter.getQuantityOfUnmodifiedFiles(),
                totalCounter.getTotalErrors(),
                totalCounter.getTotalSizeBeforeModifier(),
                totalCounter.getTotalSizeAfterModifier()
        ));
    }

    // Invoke the pattern matching
    // method on each file.
    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
        find(file);
        return CONTINUE;
    }

    // Invoke the pattern matching
    // method on each directory.
    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
        find(dir);
        return CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) {
        System.err.println(exc);
        return CONTINUE;
    }
}


