package ru.vladik.dnevnik.Utils;
import android.content.Context;
import android.widget.Toast;

import com.google.gson.JsonIOException;
import com.google.gson.JsonParseException;

import java.time.Duration;

import ru.vladik.dnevnik.DiaryAPI.exceptions.APIException;
import ru.vladik.dnevnik.DiaryAPI.exceptions.EmptyJsonStringException;
import ru.vladik.dnevnik.DiaryAPI.exceptions.JsonNotValidException;
import ru.vladik.dnevnik.DiaryAPI.exceptions.NoEthernetException;

public class NetworkHelper {

    public static void startAsyncTaskCatchingApiErrors(Context context, Runnable runnable) {
        AsyncUtil.startAsyncTask(() -> {
            try {
                runnable.run();
            } catch (APIException | EmptyJsonStringException | JsonNotValidException | NoEthernetException e) {
                e.printStackTrace();
                AsyncUtil.executeInMain(() -> {
                    Toast toast = Toast.makeText(context, "Ошибка " + e.getMessage(), Toast.LENGTH_SHORT);
                    toast.show();
                });
            } catch (NullPointerException e) {
            }
        });
    }

    public static void startAsyncTaskCatchingApiErrors(Context context, Runnable runnable, Runnable onError) {
        AsyncUtil.startAsyncTask(() -> {
            try {
                runnable.run();
            } catch (APIException | EmptyJsonStringException | JsonNotValidException | NoEthernetException e) {
                AsyncUtil.executeInMain(() -> {
                    Toast toast = new Toast(context);
                    toast.setText("Ошибка " + e.getMessage());
                    toast.show();
                });
                onError.run();
            }
        });
    }
}
