package ru.vladik.myapplication.DiaryAPI.DataClasses.v6;

import java.io.Serializable;

import lombok.Data;

@Data
public class MarkSubject implements Serializable {
    private Long id;
    private String name, knowledgeArea, subjectMood;
}
