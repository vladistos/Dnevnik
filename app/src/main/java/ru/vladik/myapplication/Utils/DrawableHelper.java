package ru.vladik.myapplication.Utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import ru.vladik.myapplication.DiaryAPI.DataClasses.Mood;

public class DrawableHelper {

    public static final int BAD_MOOD_COLOR = 0xFFAF4C4C;
    public static final int AVERAGE_MOOD_COLOR = 0xFFe5be01;
    public static final int GOOD_MOOD_COLOR = 0xFF00CC00;

    //Copied from https://stackoverflow.com/questions/3375166/android-drawable-images-from-url
    public static Drawable drawableFromUrl(String url) throws IOException {
        Bitmap x;

        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.connect();
        InputStream input = connection.getInputStream();

        x = BitmapFactory.decodeStream(input);
        return new BitmapDrawable(Resources.getSystem(), x);
    }

    public static int getColorByMood(String mood, int defaultColor) {
        switch (mood) {
            case Mood.GOOD:
                return DrawableHelper.GOOD_MOOD_COLOR;
            case Mood.AVERAGE:
                return DrawableHelper.AVERAGE_MOOD_COLOR;
            case Mood.BAD:
                return DrawableHelper.BAD_MOOD_COLOR;
            default:
                return defaultColor;
        }
    }
}
