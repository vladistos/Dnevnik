package ru.vladik.myapplication.Utils;

import static ru.vladik.myapplication.Utils.StaticRecourses.LOCALE_RU;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.format.TextStyle;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import ru.vladik.myapplication.Exceptions.CouldNotGetDateException;

public class DateHelper {
    public static String getDateRU(Date date, int dayOfWeek) {
        DayOfWeek day = DayOfWeek.of(dayOfWeek);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yy", Locale.getDefault());
        String stringText = day.getDisplayName(TextStyle.FULL, new Locale(LOCALE_RU)) + " " +
                dateFormat.format(date);
        char[] chars = stringText.toCharArray();
        chars[0] = Character.toUpperCase(chars[0]);
        return new String(chars);
    }

    public static String getDateRU(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        DayOfWeek day = DayOfWeek.of(calendar.get(Calendar.DAY_OF_WEEK));
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yy", Locale.getDefault());
        String stringText = day.getDisplayName(TextStyle.FULL, new Locale(LOCALE_RU)) + " " +
                dateFormat.format(date);
        char[] chars = stringText.toCharArray();
        chars[0] = Character.toUpperCase(chars[0]);
        return new String(chars);
    }

    public static String getStringDateWithTime(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yy HH:mm", Locale.getDefault());
        return dateFormat.format(date);
    }

    public static String getStringDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yy", Locale.getDefault());
        return dateFormat.format(date);
    }

    public static Date getDateFromString(String date) throws CouldNotGetDateException {
        date = date.replace("T", " ");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss",
                new Locale(StaticRecourses.LOCALE_RU)
        );
        try {
            return simpleDateFormat.parse(date);
        } catch (ParseException e) {
            throw new CouldNotGetDateException(e.getMessage());
        }
    }
}
