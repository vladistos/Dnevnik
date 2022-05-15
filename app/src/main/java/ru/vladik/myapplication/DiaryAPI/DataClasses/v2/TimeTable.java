package ru.vladik.myapplication.DiaryAPI.DataClasses.v2;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

public @Data
class TimeTable {
    private String Name, Start, FirstLessonNumber;
    private List<DayInTimeTable> Items;

    public TimeTable() {
        this("", "", "", new ArrayList<>());
    }

    public TimeTable(String name, String start, String firstLessonNumber, List<DayInTimeTable> items) {
        Name = name;
        Start = start;
        FirstLessonNumber = firstLessonNumber;
        Items = items;
    }
}
