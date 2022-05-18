package ru.vladik.dnevnik.DiaryAPI.DataClasses.v6;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import ru.vladik.dnevnik.DiaryAPI.DataClasses.webApi.Reaction;
import ru.vladik.dnevnik.DiaryAPI.DataClasses.Reactions;

@Data
public class Votes {
    private Boolean like, heart, ok, laughing, surprised, sad, angry;

    public Votes(Boolean like, Boolean heart, Boolean ok, Boolean laughing, Boolean surprised, Boolean sad, Boolean angry) {
        this.like = like;
        this.heart = heart;
        this.ok = ok;
        this.laughing = laughing;
        this.surprised = surprised;
        this.sad = sad;
        this.angry = angry;
    }

    public Votes() {
        this(false, false, false, false, false, false, false);
    }

    public void setReaction(int reaction, Boolean val) {
        switch (reaction) {
            case Reactions.LIKE:
                like = val;
                break;
            case Reactions.HEART:
                heart = val;
                break;

            case Reactions.OK:
                ok = val;
                break;

            case Reactions.LAUGHING:
                laughing = val;
                break;

            case Reactions.SURPRISED:
                surprised = val;
                break;

            case Reactions.SAD:
                sad = val;
                break;

            case Reactions.ANGRY:
                angry = val;
                break;

        }
    }

    public List<Reaction> getReactionList() {
        List<Reaction> votesList = new ArrayList<>();
        votesList.add(Reactions.NOT_SET, new Reaction(Reactions.getEmotion(Reactions.NOT_SET), false));
        votesList.add(Reactions.LIKE, new Reaction(Reactions.getEmotion(Reactions.LIKE), like));
        votesList.add(Reactions.HEART, new Reaction(Reactions.getEmotion(Reactions.HEART), heart));
        votesList.add(Reactions.OK, new Reaction(Reactions.getEmotion(Reactions.OK), ok));
        votesList.add(Reactions.LAUGHING, new Reaction(Reactions.getEmotion(Reactions.LAUGHING), laughing));
        votesList.add(Reactions.SURPRISED, new Reaction(Reactions.getEmotion(Reactions.SURPRISED), surprised));
        votesList.add(Reactions.SAD, new Reaction(Reactions.getEmotion(Reactions.SAD), sad));
        votesList.add(Reactions.ANGRY, new Reaction(Reactions.getEmotion(Reactions.ANGRY), angry));
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
