package ru.vladik.myapplication.Utils;

import android.os.Handler;
import android.os.Looper;

public class AsyncUtil {
    public static void executeInMain(Runnable runnable) {
        Handler mainHandler = new Handler(Looper.getMainLooper());
        mainHandler.post(runnable);
    }
}
