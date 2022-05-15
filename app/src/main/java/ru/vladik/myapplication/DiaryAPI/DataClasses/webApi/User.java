package ru.vladik.myapplication.DiaryAPI.DataClasses.webApi;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class User {
    private String id, name, avatarUrl, redirectUrl;
    private List<String> roles;
    private Boolean isParentCommitteeMember;

    public User(String id, String name, String avatarUrl, String redirectUrl,
                List<String> roles, Boolean isParentCommitteeMember) {
        this.id = id;
        this.name = name;
        this.avatarUrl = avatarUrl;
        this.redirectUrl = redirectUrl;
        this.roles = roles;
        this.isParentCommitteeMember = isParentCommitteeMember;
    }

    public User() {
        this("", "", "", "", new ArrayList<>(), false);
    }
}
