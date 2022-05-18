package ru.vladik.dnevnik.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.io.IOException;

import ru.vladik.dnevnik.DiaryAPI.DataClasses.Reactions;

public class AssetsHelper {

    public static String TOMATO_PACK_PATH = "tomato_emoji";
    public static String OBAMA_PACK_PATH = "obama_emoji";

    public static Drawable[] getDrawablesPack(String filePath, Context context) {
        Drawable[] drawableList = new Drawable[10];
        try {
            for (String fileName : context.getAssets().list(filePath)) {
                Drawable d = Drawable.createFromStream(context.getAssets().open(filePath+"/"+fileName), null);
                int i = Reactions.getEmotionId(fileName.replaceAll("(.png|.gif)", "")
                        .replace("_", ""));
                if (i >= 0 && i < drawableList.length) {
                    if (d instanceof BitmapDrawable){
                        Bitmap bitmap = ((BitmapDrawable) d).getBitmap();
                        d = new BitmapDrawable(context.getResources(), Bitmap.createScaledBitmap(bitmap, 65, 65, true));
                    }
                    drawableList[i] = d;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return drawableList;
    }
}
