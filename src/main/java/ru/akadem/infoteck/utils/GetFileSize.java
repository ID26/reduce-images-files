package ru.akadem.infoteck.utils;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class GetFileSize {

    public static Double getFileSizeMegaBytes(File file) {
        return (double) file.length() / (1024 * 1024);
    }

    public static Double getFileSizeKiloBytes(File file) {
        return roundDouble4LastDot((double) file.length() / 1024);
//        return (double) file.length() / 1024;
    }

    public static Long getFileSizeBytes(File file) {
        return file.length();
    }

    public static Double roundDouble4LastDot(Double value) {
        double scale = Math.pow(10, 4);
        return Math.ceil((value) * scale) / scale;
    }
}
