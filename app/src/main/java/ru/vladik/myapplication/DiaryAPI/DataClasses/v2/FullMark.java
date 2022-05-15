package ru.vladik.myapplication.DiaryAPI.DataClasses.v2;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
public @Data class FullMark implements Serializable {
    private Mark mark;
    private Lesson lesson;
    private Subject subject;
}
