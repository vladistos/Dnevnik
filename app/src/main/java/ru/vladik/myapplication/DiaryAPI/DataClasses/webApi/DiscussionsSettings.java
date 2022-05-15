package ru.vladik.myapplication.DiaryAPI.DataClasses.webApi;

import lombok.Data;

@Data
public class DiscussionsSettings {
    private String eventKey, eventSign, eventUrl;

    public DiscussionsSettings(String eventKey, String eventSign, String eventUrl) {
        this.eventKey = eventKey;
        this.eventSign = eventSign;
        this.eventUrl = eventUrl;
    }

    public DiscussionsSettings() {
        this("", "", "");
    }
}
