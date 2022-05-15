package ru.vladik.myapplication.DiaryAPI.DataClasses.v2;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import ru.vladik.myapplication.DiaryAPI.DataClasses.webApi.File;

public @Data
class Work implements Serializable {
    private Long id, workType, lesson, eduGroup, subjectId, createdBy;
    private Boolean displayInJournal, isImportant;
    private Integer markCount, periodNumber;
    private String type, markType, status, text, periodType, targetDate, sentDate;
    private List<Object> tasks, oneDriveLinks; //TODO: specialize class
    private List<File> files;

    public Work(Long id, Long workType, Long lesson, Long eduGroup, Long subjectId, Long createdBy,
                Boolean displayInJournal, Boolean isImportant, Integer markCount,
                Integer periodNumber, String type, String markType, String status,
                String text, String periodType, String targetDate, String sentDate,
                List<Object> tasks, List<Object> oneDriveLinks, List<File> files) {
        this.id = id;
        this.workType = workType;
        this.lesson = lesson;
        this.eduGroup = eduGroup;
        this.subjectId = subjectId;
        this.createdBy = createdBy;
        this.displayInJournal = displayInJournal;
        this.isImportant = isImportant;
        this.markCount = markCount;
        this.periodNumber = periodNumber;
        this.type = type;
        this.markType = markType;
        this.status = status;
        this.text = text;
        this.periodType = periodType;
        this.targetDate = targetDate;
        this.sentDate = sentDate;
        this.tasks = tasks;
        this.oneDriveLinks = oneDriveLinks;
        this.files = files;
    }

    public Work() {
        this(-1L, -1L, -1L, -1L, -1L, -1L,
                false, false, 0, 0, "",
                "", "", "", "", "", "",
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
    }
}
