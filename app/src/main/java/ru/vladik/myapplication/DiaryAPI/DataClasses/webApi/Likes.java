package ru.vladik.myapplication.DiaryAPI.DataClasses.webApi;

import lombok.Data;
import ru.vladik.myapplication.DiaryAPI.DataClasses.Reactions;

@Data
public class Likes {
    private String externalId, userVote;
    private Votes votes;

    public Likes(String externalId, String userVote, Votes votes) {
        this.externalId = externalId;
        this.userVote = userVote;
        this.votes = votes;
    }

    public Likes() {
        this("", "", new Votes());
    }

    public int getUserVoteId() {
        return Reactions.getEmotionId(userVote);
    }
}
