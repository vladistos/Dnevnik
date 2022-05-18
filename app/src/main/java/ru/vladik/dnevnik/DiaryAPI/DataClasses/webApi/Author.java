package ru.vladik.dnevnik.DiaryAPI.DataClasses.webApi;

import lombok.Data;

@Data
public class Author {
    private Long id;
    private String name, avatarUrl, redirectUrl, firstName, middleName, lastName;

    public Author(Long id, String name, String avatarUrl, String redirectUrl, String firstName,
                  String middleName, String lastName) {
        this.id = id;
        this.name = name;
        this.avatarUrl = avatarUrl;
        this.redirectUrl = redirectUrl;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
    }

    public Author() {
        this(-1L, "", "", "", "", "", "");
    }
}
