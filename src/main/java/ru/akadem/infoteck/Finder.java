package ru.akadem.infoteck;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;

import static java.nio.file.FileVisitResult.CONTINUE;

public class Finder extends SimpleFileVisitor<Path> {

    private static final Map<String, Counter> counters = new HashMap<>();
    private static final Map<String, Reducer> reducerMap = new HashMap<>();

    static {
        reducerMapInit();
        countersMapInit();
    }

    private static void countersMapInit() {
        reducerMap.forEach((key, value) -> counters.put(key, new Counter(key, 0L, 0L, 0L, 0L, BigDecimal.ZERO,
                BigDecimal.ZERO)));
    }

    private static void reducerMapInit() {
        reducerMap.put(".pdf", new PdfReduce(1264, 1753));
        reducerMap.put(".png", new PngReducer());
//        ".png", ".gif", ".jpeg", ".jpg", ".tiff"
    }

    // Compares the glob pattern against
    // the file or directory name.
    void find(Path file) {
        Path name = file.getFileName();
        for (Map.Entry<String, Reducer> entry : reducerMap.entrySet()) {
            if (name != null && name.toString().endsWith(entry.getKey())) {
                Reducer reducer = entry.getValue();
                reducer.reduce(file, counters, entry.getKey(), reducer);
            }
        }
    }

    // Prints the total number of
    // matches to standard out.
    public void done() {
        for (Map.Entry<String, Counter> counter : counters.entrySet()) {
            ConsoleHelper.writeMessage(String.format("Всего обработано %d файлов формата %s, из которых сжато - %d, " +
                            "увеличено - %d, пропущено - %d. Общий размер файлов до обработки -" + " %s KB, после обработки - %s KB",
                    counter.getValue().getTotalFiles(),
                    counter.getValue().getName(),
                    counter.getValue().getQuantityOfProcessedFiles(),
                    counter.getValue().getQuantityIncreasedFiles(),
                    counter.getValue().getQuantityOfUnmodifiedFiles(),
                    counter.getValue().getTotalSizeBeforeModifier(),
                    counter.getValue().getTotalSizeAfterModifier()
                   ));
        }
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


