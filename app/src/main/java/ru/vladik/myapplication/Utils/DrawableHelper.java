package ru.vladik.myapplication.Utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import ru.vladik.myapplication.DiaryAPI.DataClasses.v2.Mood;
import ru.vladik.myapplication.R;

public class DrawableHelper {

    public static final int BAD_MOOD_COLOR = 0;
    public static final int AVERAGE_MOOD_COLOR = 1;
    public static final int GOOD_MOOD_COLOR = 2;

    public static final int BAD_MOOD_COLOR_LIGHT = 3;
    public static final int AVERAGE_MOOD_COLOR_LIGHT = 4;
    public static final int GOOD_MOOD_COLOR_LIGHT = 5;

    public static int getColor(Context context, int color, int defaultColor) {
        switch (color) {
            case BAD_MOOD_COLOR:
                return context.getColor(R.color.bad_mood);
            case AVERAGE_MOOD_COLOR:
                return context.getColor(R.color.average_mood);
            case GOOD_MOOD_COLOR:
                return context.getColor(R.color.good_mood);
            case BAD_MOOD_COLOR_LIGHT:
                return context.getColor(R.color.bad_mood_light);
            case AVERAGE_MOOD_COLOR_LIGHT:
                return context.getColor(R.color.average_mood_light);
            case GOOD_MOOD_COLOR_LIGHT:
                return context.getColor(R.color.good_mood_light);
            default:
                return defaultColor;
        }
    }

    //Copied from https://stackoverflow.com/questions/3375166/android-drawable-images-from-url
    public static Drawable drawableFromUrl(String url) throws IOException {
        Bitmap x;

        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.connect();
        InputStream input = connection.getInputStream();

        x = BitmapFactory.decodeStream(input);
        return new BitmapDrawable(Resources.getSystem(), x);
    }

    public static int getColorByMood(Context context, String mood, int defaultColor) {
        switch (mood) {
            case Mood.GOOD:
                return getColor(context, DrawableHelper.GOOD_MOOD_COLOR, defaultColor);
            case Mood.AVERAGE:
                return getColor(context, DrawableHelper.AVERAGE_MOOD_COLOR, defaultColor);
            case Mood.BAD:
                return getColor(context, DrawableHelper.BAD_MOOD_COLOR, defaultColor);
            default:
                return defaultColor;
        }
    }
}
