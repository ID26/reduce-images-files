package ru.akadem.infotech;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class AppMain {
    public static void main(String[] args) throws IOException {

        if (args.length < 1) {
            usage();
        }

        Path startingDir = Paths.get(args[0]);

        Finder finder = new Finder();
        Files.walkFileTree(startingDir, finder);
        Finder.printStatistic();
    }

    static void usage() {
        System.err.println("Неправильно введен path");
        System.exit(-1);
    }
}
