package ru.vladik.dnevnik.DiaryAPI.DataClasses.v2;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

public @Data
class LessonWithoutWorks {
    private Long id, group, resultPlaceId, subjectId;
    private List<Long> teachers, works;
    private Integer number;
    private String title, date, status;
    private Subject subject;

    public LessonWithoutWorks(Long id, Long group, Long resultPlaceId,
                              Long subjectId, List<Long> teachers, List<Long> works,
                              Integer number, String title, String date, String status,
                              Subject subject) {
        this.id = id;
        this.group = group;
        this.resultPlaceId = resultPlaceId;
        this.subjectId = subjectId;
        this.teachers = teachers;
        this.works = works;
        this.number = number;
        this.title = title;
        this.date = date;
        this.status = status;
        this.subject = subject;
    }

    public LessonWithoutWorks() {
        this(-1L, -1L, -1L, -1L, new ArrayList<>(), new ArrayList<>(),
                -1, "", "", "", new Subject());
    }
}


