package ru.vladik.myapplication.DiaryAPI.DataClasses.v2;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

public @Data
class EduGroup {
    private String type, name, fullName, status, journaltype;
    private Integer parallel;
    private Long id, timetable, studyyear;
    private List<Subject> subjects;
    private List<Long> parentIds;

    public EduGroup(String type, String name, String fullName, String status, String journaltype,
                    Integer parallel, Long id, Long timetable, Long studyyear, List<Subject> subjects,
                    List<Long> parentIds) {
        this.type = type;
        this.name = name;
        this.fullName = fullName;
        this.status = status;
        this.journaltype = journaltype;
        this.parallel = parallel;
        this.id = id;
        this.timetable = timetable;
        this.studyyear = studyyear;
        this.subjects = subjects;
        this.parentIds = parentIds;
    }

    public EduGroup() {
        this("", "", "", "", "", -1, -1L, -1L,
                -1L, new ArrayList<>(), new ArrayList<>());
    }
}
