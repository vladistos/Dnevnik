package ru.vladik.dnevnik.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import ru.vladik.dnevnik.Activities.LoginActivity;
import ru.vladik.dnevnik.DiaryAPI.DiaryAPI;

public class DiarySingleton {

    private static DiarySingleton singleton = null;
    private final DiaryAPI diaryAPI;

    private DiarySingleton(DiaryAPI diaryAPI) {
        this.diaryAPI = diaryAPI;
    }

    public static DiarySingleton getInstance(@NonNull Context context) {
        if (singleton == null) {
            context.startActivity(new Intent(context, LoginActivity.class));
            if (context instanceof Activity) {
                ((Activity) context).finish();
            }
            return null;
        } else {
            return singleton;
        }
    }

    public synchronized static void init(DiaryAPI diaryAPI) {
        if (singleton != null) {
            throw new AssertionError("You already initialized me");
        }
        singleton = new DiarySingleton(diaryAPI);
    }

    public DiaryAPI getDiaryAPI() {
        return this.diaryAPI;
    }

}
