package ru.akadem.infoteck.utils;

import java.io.File;

public class GetFileSize {

//    private File file;
//
//    public GetFileSize(File file) {
//        this.file = file;
//    }

    public static Double getFileSizeMegaBytes(File file) {
        return (double) file.length() / (1024 * 1024);
    }

    public static Double getFileSizeKiloBytes(File file) {
        return (double) file.length() / 1024;
    }

    public static Long getFileSizeBytes(File file) {
        return file.length();
    }
}
