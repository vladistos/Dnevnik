package ru.vladik.myapplication.DiaryAPI.DataClasses.v6;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

public @Data
class Mark implements Serializable {
    private List<MarkCategory> categories;
    private Long date;
    private MarkSubject subject;
    private String markType, criteriaMarkType, markTypeText, shortMarkTypeText;
    private MarkLesson lesson;
    private Boolean isNew, isFinal, isImportant;
    private List<ShortMark> marks;
    private Object indicator; //TODO specialize class
}
