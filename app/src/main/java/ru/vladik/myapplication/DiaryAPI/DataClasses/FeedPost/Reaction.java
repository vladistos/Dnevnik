package ru.vladik.myapplication.DiaryAPI.DataClasses.FeedPost;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Reaction {
    private Emoji emoji;
    private int votes;
}
