package ru.vladik.myapplication.DiaryAPI.DataClasses.v6;

import java.io.Serializable;

import lombok.Data;

@Data
public class MarkLesson implements Serializable {
    private Long id, date;
    private String theme;
}
