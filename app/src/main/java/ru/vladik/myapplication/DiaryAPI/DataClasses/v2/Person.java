package ru.vladik.myapplication.DiaryAPI.DataClasses.v2;

import lombok.Data;

public @Data
class Person {
    public Person(String shortName, String sex, Long id, Long userId) {
        this.shortName = shortName;
        this.sex = sex;
        this.id = id;
        this.userId = userId;
    }

    private String shortName, sex;
    private Long id, userId;

    public Person() {
        this("", "", -1L, -1L);
    }
}
