package ru.vladik.myapplication.DiaryAPI.DataClasses;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
public @Data class LessonWithMarks {
    private Lesson lesson;
    private List<Mark> marks;
}
