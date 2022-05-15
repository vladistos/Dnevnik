package ru.vladik.myapplication.DiaryAPI.DataClasses.v6;

import android.graphics.drawable.Drawable;

import java.util.List;

import lombok.Data;

@Data
public class ImportantRecent {
    private List<FeedLesson> todayLessons, tomorrowLessons;
    private List<Mark> recentMarks;
    private HomeworksProgress homeworksProgress;
    private List<FeedPost> feed;
}
