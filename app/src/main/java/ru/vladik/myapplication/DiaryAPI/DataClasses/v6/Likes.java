package ru.vladik.myapplication.DiaryAPI.DataClasses.v6;

import java.util.List;

import lombok.Data;
import ru.vladik.myapplication.DiaryAPI.DataClasses.Reactions;

@Data
public class Likes {
    private String externalId, userVote;
    private List<String> iconsVotes;
    private Integer countWithoutUser;

    public Votes getVotes() {
        Votes votes = new Votes();
        for (String vote : iconsVotes) {
            votes.setReaction(Reactions.getEmotionId(vote), true);
        }
        return votes;
    }

    public int getUserVoteId() {
        return Reactions.getEmotionId(getUserVote());
    }
}
