package ru.vladik.dnevnik.DiaryAPI.DataClasses.webApi;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Reaction {
    private Emoji emoji;
    private int votes;

    public Reaction(Emoji emotion, Boolean voted) {
        this.emoji = emotion;
        this.votes = voted ? 1 : 0;
    }
}
