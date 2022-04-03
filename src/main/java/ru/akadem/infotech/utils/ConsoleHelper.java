package ru.akadem.infotech.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ConsoleHelper {
    public static void writeMessage(String message) {
        System.out.println(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME) + " " + message);
    }
}
