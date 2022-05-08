package ru.vladik.myapplication.DiaryAPI.DataClasses;

import android.util.Log;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.Contract;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class FormatedDate {

    private Date date;

    public FormatedDate() {
        this.date = new Date();
    }

    public FormatedDate(int offset) {
        TimeUnit day = TimeUnit.DAYS;
        this.date = new Date();
        this.date = new Date(date.getTime() + day.toMillis(offset));
    }

    public FormatedDate(Date date) {
        this.date = date;
    }

    @NonNull
    @Override
    public String toString() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS", Locale.getDefault()).format(date);
    }

    @NonNull
    public static FormatedDate startOfWeek(int offset) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.WEEK_OF_MONTH, offset);
        while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
            calendar.add(Calendar.DAY_OF_WEEK, -1);
        }
        return new FormatedDate(calendar.getTime());
    }

    @NonNull
    public static FormatedDate endOfWeek(int offset) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.WEEK_OF_MONTH, offset);
        while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
            calendar.add(Calendar.DATE, 1);
        }
        return new FormatedDate(calendar.getTime());
    }

    public Date getDate() {
        return date;
    }
}
