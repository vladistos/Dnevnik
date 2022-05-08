package ru.vladik.myapplication.DiaryAPI.DataClasses;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

public @Data
class DayInTimeTable {
    private Long Id;
    private String Start, Finish, Name, Type, Lesson;
    private List<String> DaysOfWeek;

    public DayInTimeTable(Long id, String start, String finish, String name,
                          String type, String lesson, List<String> daysOfWeek) {
        Id = id;
        Start = start;
        Finish = finish;
        Name = name;
        Type = type;
        Lesson = lesson;
        DaysOfWeek = daysOfWeek;
    }

    public DayInTimeTable() {
        this(-1L, "", "", "", "", "", new ArrayList<>());
    }
}
