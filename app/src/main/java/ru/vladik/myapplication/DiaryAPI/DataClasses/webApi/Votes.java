package ru.vladik.myapplication.DiaryAPI.DataClasses.webApi;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import ru.vladik.myapplication.DiaryAPI.DataClasses.Reactions;

@Data
public class Votes {
    private Integer notSet, like, heart, ok, laughing, surprised, sad, angry;

    public Votes(Integer notSet, Integer like, Integer heart, Integer ok, Integer laughing, Integer surprised, Integer sad, Integer angry) {
        this.notSet = notSet;
        this.like = like;
        this.heart = heart;
        this.ok = ok;
        this.laughing = laughing;
        this.surprised = surprised;
        this.sad = sad;
        this.angry = angry;
    }

    public Votes() {
        this(0, 0, 0, 0, 0, 0, 0, 0);
    }

    public int getVotesCount() {
        return notSet + like + heart + ok + laughing + surprised + sad + angry;
    }

    public List<Reaction> getReactionList() {
        List<Reaction> votesList = new ArrayList<>();
        votesList.add(Reactions.NOT_SET, new Reaction(Reactions.getEmotion(Reactions.NOT_SET), notSet));
        votesList.add(Reactions.LIKE, new Reaction(Reactions.getEmotion(Reactions.LIKE), like));
        votesList.add(Reactions.HEART, new Reaction(Reactions.getEmotion(Reactions.HEART), heart));
        votesList.add(Reactions.OK, new Reaction(Reactions.getEmotion(Reactions.OK), ok));
        votesList.add(Reactions.LAUGHING, new Reaction(Reactions.getEmotion(Reactions.LAUGHING), laughing));
        votesList.add(Reactions.SURPRISED, new Reaction(Reactions.getEmotion(Reactions.SURPRISED), surprised));
        votesList.add(Reactions.SAD, new Reaction(Reactions.getEmotion(Reactions.SAD), sad));
        votesList.add(Reactions.ANGRY, new Reaction(Reactions.getEmotion(Reactions.ANGRY), angry));
        return votesList;
    }

    public List<Reaction> getReactionsListSorted() {
        List<Reaction> votesList = getReactionList();
        votesList.sort((o1, o2) -> o2.getVotes() - o1.getVotes());
        return votesList;
    }

    public List<Reaction> getReactionsListWithoutEmpty() {
        List<Reaction> votesList = getReactionList();
        for (int i = votesList.size()-1; i >= 0; i--) {
            if (votesList.get(i).getVotes() <= 0) {
                votesList.remove(i);
            }
        }
        return votesList;
    }
}
