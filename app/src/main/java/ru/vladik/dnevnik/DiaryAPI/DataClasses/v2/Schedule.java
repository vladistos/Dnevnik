package ru.vladik.dnevnik.DiaryAPI.DataClasses.v2;

import androidx.annotation.NonNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class Schedule implements Iterable<List<LessonWithMarks>> {
    public List<LessonWithMarks> monday, tuesday, wednesday, thursday, friday, saturday, sunday;

    public static Schedule getScheduleFromLessonsWithMarks(List<LessonWithMarks> lessonsInfo) {
        HashMap<String, List<LessonWithMarks>> lessons = new HashMap<>();
        Schedule schedule = new Schedule();
        for (LessonWithMarks lessonWithMarks : lessonsInfo) {
            Lesson lesson = lessonWithMarks.getLesson();
            if (!lessons.containsKey(lesson.getDate())) {
                lessons.put(lesson.getDate(), new ArrayList<>(Collections.singleton(lessonWithMarks)));
            } else {
                lessons.get(lesson.getDate()).add(lessonWithMarks);
            }
        }
        for (String key : lessons.keySet()) {
            try {
                Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                        .parse(key.replace("T", " "));
                Calendar c = Calendar.getInstance();
                c.setTime(date);
                int dayOfWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
                List<LessonWithMarks> day = lessons.get(key);
                day = day != null ? new ArrayList<>(day) : new ArrayList<>();
                if (dayOfWeek < 8 && dayOfWeek >= 0) {
                    if (dayOfWeek != 0) {
                        schedule.set(dayOfWeek, (ArrayList<LessonWithMarks>) day);
                    } else {
                        schedule.set(7, (ArrayList<LessonWithMarks>) day);
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        for (int i = 1; i<8; i++) {
            if (schedule.get(i) == null) {
                schedule.set(i, new ArrayList<>());
            }
        }
        if (lessons.keySet().size() == 0) {
            for (List<LessonWithMarks> list : schedule) {
                if (lessonsInfo.size() > 0) {
                    list.add(lessonsInfo.get(0));
                } else {
                    Lesson lesson = new Lesson();
                    lesson.getSubject().setName("Нет уроков");
                    list.add(new LessonWithMarks(lesson, new ArrayList<>()));
                }
            }
        }
        return schedule;
    }

    public Schedule() {
        for (int i = 1; i < 8; i++) {
            this.set(i, new ArrayList<>());
        }
    }

    public void set(int day, ArrayList<LessonWithMarks> lessonsWithMarks) {
        switch (day) {
            case 1:
                monday = lessonsWithMarks;
                break;
            case 2:
                tuesday = lessonsWithMarks;
                break;
            case 3:
                wednesday = lessonsWithMarks;
                break;
            case 4:
                thursday = lessonsWithMarks;
                break;
            case 5:
                friday = lessonsWithMarks;
                break;
            case 6:
                saturday = lessonsWithMarks;
                break;
            case 7:
                sunday = lessonsWithMarks;
                break;
            default:
                throw new IndexOutOfBoundsException("Day " + day + " is not in range 1, 7");
        }
    }

    public List<LessonWithMarks> get(int day) {
        switch (day) {
            case 1:
                return monday;
            case 2:
                return tuesday;
            case 3:
                return wednesday;
            case 4:
                return thursday;
            case 5:
                return friday;
            case 6:
                return saturday;
            case 7:
                return sunday;
            default:
                throw new IndexOutOfBoundsException("Day " + day + " is not a day of week");
    }
    }

    @NonNull
    @Override
    public Iterator<List<LessonWithMarks>> iterator() {
        return new Iterator<List<LessonWithMarks>>() {
            int pos = 1;
            @Override
            public boolean hasNext() {
                return pos < 8 && pos > 0;
            }

            @Override
            public List<LessonWithMarks> next() {
                return get(pos++);
            }
        };
    }

    public int size() {
        int count = 0;
        for(List<LessonWithMarks> lessonWithMarksList : this) {
            count++;
        }
        return count;
    }
}
