package ru.vladik.myapplication.DiaryAPI.DataClasses;

import lombok.AllArgsConstructor;
import lombok.Data;

public @Data
class Subject {
    public Subject(String name, String knowledgeArea, Long id, Long fgosSubjectId) {
        this.name = name;
        this.knowledgeArea = knowledgeArea;
        this.id = id;
        this.fgosSubjectId = fgosSubjectId;
    }

    public Subject(String name) {
        this(name, "", -1L, -1L);
    }
    private String name, knowledgeArea;
    private Long id, fgosSubjectId;

    public Subject() {
        this("");
    }
}
