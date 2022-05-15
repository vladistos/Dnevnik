package ru.vladik.myapplication.DiaryAPI.DataClasses.v6;

import lombok.Data;

@Data
public class LessonHours {
    private String startHour, startMinute, endHour, endMinute;
}
