package ru.vladik.myapplication.DiaryAPI.DataClasses;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
public @Data class FullMark {
    private Mark mark;
    private Lesson lesson;
    private Subject subject;
}
