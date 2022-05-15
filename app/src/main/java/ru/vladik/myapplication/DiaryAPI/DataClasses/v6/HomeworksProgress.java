package ru.vladik.myapplication.DiaryAPI.DataClasses.v6;

import java.util.List;

import lombok.Data;

@Data
public class HomeworksProgress {
    private Integer totalLessonsWithHomeworksCount, completedLessonsWithHomeworksCount;
    private List<Long> lessonWithHomeworksIds;
}
