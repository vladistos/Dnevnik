package ru.vladik.myapplication.DiaryAPI.DataClasses;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public @Data
class Lesson {
    private Long id, group, resultPlaceId, subjectId;
    private List<Long> teachers;
    private Integer number;
    private String title, date, status;
    private Subject subject;
    private List<Work> works;

    public Lesson(LessonWithoutWorks lessonWithoutWorks) {
        this(lessonWithoutWorks.getId(), lessonWithoutWorks.getGroup(),
                lessonWithoutWorks.getResultPlaceId(), lessonWithoutWorks.getSubjectId(),
                lessonWithoutWorks.getTeachers(), lessonWithoutWorks.getNumber(),
                lessonWithoutWorks.getTitle(), lessonWithoutWorks.getDate(),
                lessonWithoutWorks.getStatus(), lessonWithoutWorks.getSubject(), new ArrayList<>());
    }

    public Lesson(Long id, Long group, Long resultPlaceId, Long subjectId,
                  List<Long> teachers, Integer number, String title, String date, String status,
                  Subject subject, List<Work> works) {
        this.id = id;
        this.group = group;
        this.resultPlaceId = resultPlaceId;
        this.subjectId = subjectId;
        this.teachers = teachers;
        this.number = number;
        this.title = title;
        this.date = date;
        this.status = status;
        this.subject = subject;
        this.works = works;
    }

    public Lesson() {
        this(-1L, -1L, -1L, -1L, new ArrayList<>(),
                -1, "", "", "", new Subject(), new ArrayList<>());
    }
}
