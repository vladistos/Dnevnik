package ru.vladik.dnevnik.DiaryAPI.DataClasses.v6;

import lombok.Data;

@Data
public class FeedLesson {
    private Long id, startTime, endTime;
    private Integer number;
    private String place;
    private LessonHours hours;
    private MarkSubject subject;
}

