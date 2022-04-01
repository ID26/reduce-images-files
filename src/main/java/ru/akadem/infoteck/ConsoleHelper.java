package ru.akadem.infoteck;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class ConsoleHelper {
//    private static BufferedReader bis = new BufferedReader(new InputStreamReader(System.in));

//    private static ResourceBundle res = ResourceBundle.getBundle(/*CashMachine.class.getPackage().getName() + ".resources.common"*/"common");

    public static void writeMessage(String message) {
        System.out.println(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME) + " " + message);
    }

//    public static String readString() throws InterruptOperationException {
//        try {
//            String text = bis.readLine();
//            if ("exit".equals(text.toLowerCase())) {
//                throw new InterruptOperationException();
//            }
//
//            return text;
//        } catch (IOException ignored) {
//
//        }
//        return null;
//    }
//
//    public static String askCurrencyCode() throws InterruptOperationException {
//        while (true) {
//            ConsoleHelper.writeMessage(res.getString("choose.currency.code"));
//            String currencyCode = ConsoleHelper.readString();
//            if (currencyCode == null || currencyCode.trim().length() != 3) {
//                ConsoleHelper.writeMessage(res.getString("invalid.data"));
//                continue;
//            }
//            return currencyCode.trim().toUpperCase();
//        }
//    }
//
//    public static String[] getValidTwoDigits(String currencyCode) throws InterruptOperationException {
//        while (true) {
//            ConsoleHelper.writeMessage(String.format(res.getString("choose.denomination.and.count.format"), currencyCode));
//            String line = ConsoleHelper.readString();
//            String[] split = null;
//            if (line == null || (split = line.split(" ")).length != 2) {
//                ConsoleHelper.writeMessage(res.getString("invalid.data"));
//            } else {
//                try {
//                    if (Integer.parseInt(split[0]) <= 0 || Integer.parseInt(split[1]) <= 0) {
//                        ConsoleHelper.writeMessage(res.getString("invalid.data"));
//                    }
//                } catch (NumberFormatException e) {
//                    ConsoleHelper.writeMessage(res.getString("invalid.data"));
//                    continue;
//                }
//                return split;
//            }
//        }
//    }
//
//    public static Operation askOperation() throws InterruptOperationException {
//        while (true) {
//            ConsoleHelper.writeMessage(res.getString("choose.operation"));
//            ConsoleHelper.writeMessage("\t 1 - " + res.getString("operation.INFO"));
//            ConsoleHelper.writeMessage("\t 2 - " + res.getString("operation.DEPOSIT"));
//            ConsoleHelper.writeMessage("\t 3 - " + res.getString("operation.WITHDRAW"));
//            ConsoleHelper.writeMessage("\t 4 - " + res.getString("operation.EXIT"));
//            int operation = Integer.parseInt(ConsoleHelper.readString().trim());
//            try {
//                return Operation.getAllowableOperationByOrdinal(operation);
//            } catch (IllegalArgumentException e) {
//                ConsoleHelper.writeMessage(res.getString("invalid.data"));
//            }
//        }
//    }

//    public static void printExitMessage() {
//        ConsoleHelper.writeMessage(res.getString("the.end"));
//    }

}
