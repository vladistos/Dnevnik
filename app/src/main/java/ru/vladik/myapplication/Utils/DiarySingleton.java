package ru.vladik.myapplication.Utils;

import ru.vladik.myapplication.DiaryAPI.DiaryAPI;

public class DiarySingleton {

    private static DiarySingleton singleton = null;
    private final DiaryAPI diaryAPI;

    private DiarySingleton(DiaryAPI diaryAPI) {
        this.diaryAPI = diaryAPI;
    }

    public static DiarySingleton getInstance() {
        if (singleton == null) {
            throw new AssertionError("You have to call init first");
        }
        return singleton;
    }

    public synchronized static DiarySingleton init(DiaryAPI diaryAPI) {
        if (singleton != null) {
            throw new AssertionError("You already initialized me");
        }
        singleton = new DiarySingleton(diaryAPI);
        return singleton;
    }

    public DiaryAPI getDiaryAPI() {
        return this.diaryAPI;
    }

}
