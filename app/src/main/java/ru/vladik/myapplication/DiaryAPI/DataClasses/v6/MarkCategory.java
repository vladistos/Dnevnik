package ru.vladik.myapplication.DiaryAPI.DataClasses.v6;

import java.io.Serializable;

import lombok.Data;

@Data
public class MarkCategory implements Serializable {
    private String mood, value;
    private Double percent;
    private Integer studentCount, markNumber;
}
