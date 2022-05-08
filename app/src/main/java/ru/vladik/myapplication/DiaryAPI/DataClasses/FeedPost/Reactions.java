package ru.vladik.myapplication.DiaryAPI.DataClasses.FeedPost;

import android.util.Log;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class Reactions {

    public static final int NOT_SET = 0;
    public static final int LIKE = 1;
    public static final int HEART = 2;
    public static final int OK= 3;
    public static final int LAUGHING = 4;
    public static final int SURPRISED = 5;
    public static final int SAD = 6;
    public static final int ANGRY= 7;

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
        emotion = emotion.toLowerCase(Locale.ROOT);
        for (int i = 0; i < emotions.size(); i++) {
            if (emotions.get(i).getName().toLowerCase(Locale.ROOT).equals(emotion)) {
                return i;
            }
        }
        return -1;
    }

    public static List<Emoji> getEmotions() {
        return emotions;
    }
}
