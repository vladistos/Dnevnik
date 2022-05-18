package ru.vladik.dnevnik.DiaryAPI.DataClasses.v2;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
public @Data class LessonWithMarks {
    private Lesson lesson;
    private List<Mark> marks;
}
