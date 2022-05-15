package ru.vladik.myapplication.DiaryAPI.DataClasses.v2;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;

public @Data
class Mark implements Serializable {
    private Long id, person, work, lesson, workType;
    private Boolean use_avg_calc;
    private String textValue, number;
    private String type, mood;

    public Mark(Long id, Long person, Long work, Long lesson, Long workType, Boolean use_avg_calc,
                String textValue, String number, String type, String mood) {
        this.id = id;
        this.person = person;
        this.work = work;
        this.lesson = lesson;
        this.workType = workType;
        this.use_avg_calc = use_avg_calc;
        this.textValue = textValue;
        this.number = number;
        this.type = type;
        this.mood = mood;
    }

    public Mark() {
        this(-1L, -1L, -1L, -1L, -1L,
                false, "", "", "", "");
    }
}
