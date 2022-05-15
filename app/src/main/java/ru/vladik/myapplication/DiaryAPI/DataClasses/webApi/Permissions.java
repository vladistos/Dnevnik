package ru.vladik.myapplication.DiaryAPI.DataClasses.webApi;

import lombok.Data;

@Data
public class Permissions {
    private boolean view, edit, delete, viewInFeed, viewStatistic;

    public Permissions(boolean view, boolean edit, boolean delete, boolean viewInFeed, boolean viewStatistic) {
        this.view = view;
        this.edit = edit;
        this.delete = delete;
        this.viewInFeed = viewInFeed;
        this.viewStatistic = viewStatistic;
    }

    public Permissions() {
        this(false, false, false, false, false);
    }
}
