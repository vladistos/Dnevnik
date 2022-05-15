package ru.vladik.myapplication.DiaryAPI.DataClasses;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import ru.vladik.myapplication.DiaryAPI.DataClasses.webApi.Emoji;
import ru.vladik.myapplication.R;

public class Reactions {

    public static final int NOT_SET = 0;
    public static final int LIKE = 1;
    public static final int HEART = 2;
    public static final int OK= 3;
    public static final int LAUGHING = 4;
    public static final int SURPRISED = 5;
    public static final int SAD = 6;
    public static final int ANGRY= 7;

    public static final int REACTIONS_COUNT = 8;

    private static final List<Emoji> emotions = Arrays.asList(
            new Emoji("notSet", ""),
            new Emoji("like", "\uD83D\uDC4D"),
            new Emoji("heart", "❤"),
            new Emoji("ok", "✅"),
            new Emoji("laughing", "\uD83D\uDE03"),
            new Emoji("surprised", "\uD83D\uDE32"),
            new Emoji("sad", "\uD83D\uDE2D"),
            new Emoji("angry", "\uD83D\uDE21")
    );

    public static Emoji getEmotion(int emotion) {
        return emotions.get(emotion);
    }

    public static int getEmotionId(String emotion) {
        if (emotion != null) {
            emotion = emotion.toLowerCase(Locale.ROOT);
            for (int i = 0; i < emotions.size(); i++) {
                if (emotions.get(i).getName().toLowerCase(Locale.ROOT).equals(emotion)) {
                    return i;
                }
            }
        }
        return -1;
    }

    public static List<Emoji> getEmotions() {
        return emotions;
    }

    public static List<Emoji> getEmotionsWithoutEmpty() {
        List<Emoji> emojis = new ArrayList<>(getEmotions());
        emojis.remove(Reactions.getEmotion(Reactions.NOT_SET));
        return emojis;
    }

    public static String getReactionText(Context context, int emotionId) {
        switch (emotionId) {
            case NOT_SET:
                return context.getString(R.string.not_set);
            case LIKE:
                return context.getString(R.string.like);
            case HEART:
                return context.getString(R.string.heart);
            case OK:
                return context.getString(R.string.ok);
            case LAUGHING:
                return context.getString(R.string.laughing);
            case SURPRISED:
                return context.getString(R.string.surprised);
            case SAD:
                return context.getString(R.string.sad);
            case ANGRY:
                return context.getString(R.string.angry);
            default:
                return "";
        }
    }
}
