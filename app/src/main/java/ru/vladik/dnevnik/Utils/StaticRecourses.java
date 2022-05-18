package ru.vladik.dnevnik.Utils;

import java.util.List;

import ru.vladik.dnevnik.DiaryAPI.DataClasses.v2.DiaryContext;
import ru.vladik.dnevnik.DiaryAPI.DataClasses.v2.Person;
import ru.vladik.dnevnik.DiaryAPI.DataClasses.v2.TimeTable;

public class StaticRecourses {
    public static DiaryContext UserContext = null;
    public static TimeTable timeTable = null;
    public static List<Person> classmatePersonsList = null;

    public static final String LOCALE_RU = "ru";

    public static final String SHARED_PREFERENCES_NAME = "settings";
    public static final String SHARED_PREFERENCES_LOGIN = "login";
    public static final String SHARED_PREFERENCES_PASSWORD = "password";

    public static final String INTENT_WITH_MARK_NAME = "mark_intent";
    public static final String INTENT_WITH_RATING_NAME = "rating_intent";
}
