package ru.vladik.myapplication.DiaryAPI.DataClasses.v6;

import java.io.Serializable;

import lombok.Data;

@Data
public class ShortMark implements Serializable {
    private Long id;
    private String value, mood;
}
