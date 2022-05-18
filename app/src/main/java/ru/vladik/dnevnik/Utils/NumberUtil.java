package ru.vladik.dnevnik.Utils;

public class NumberUtil {
    public static boolean isInt(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static int getIntByMarkOrZero(String markVal) {
        markVal = markVal.replaceAll("(-|/+)", "");
        if (isInt(markVal)) {
            return Integer.parseInt(markVal);
        } else {
            return 0;
        }
    }
}
