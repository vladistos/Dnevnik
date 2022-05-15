package ru.vladik.myapplication.DiaryAPI.DataClasses.webApi;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Emoji {
    private String name, emoji;
}
