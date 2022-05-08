package ru.vladik.myapplication.DiaryAPI.DataClasses;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class PersonWithMarks {
    private Person person;
    private List<Mark> marks;

    public PersonWithMarks(Person person, List<Mark> marks) {
        this.person = person;
        this.marks = marks;
    }

    public PersonWithMarks() {
        person = new Person();
        marks = new ArrayList<>();
    }
}