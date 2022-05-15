package ru.vladik.myapplication.Adapters;

import static ru.vladik.myapplication.Utils.DateHelper.getDateFromString;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ru.vladik.myapplication.Activities.MarkInfoActivity;
import ru.vladik.myapplication.DiaryAPI.DataClasses.v2.FullMark;
import ru.vladik.myapplication.DiaryAPI.DataClasses.v2.Lesson;
import ru.vladik.myapplication.DiaryAPI.DataClasses.v2.LessonWithMarks;
import ru.vladik.myapplication.DiaryAPI.DataClasses.v2.Mark;
import ru.vladik.myapplication.DiaryAPI.DataClasses.v2.Schedule;
import ru.vladik.myapplication.DiaryAPI.DataClasses.v2.TimeTable;
import ru.vladik.myapplication.DiaryAPI.DataClasses.v2.Work;
import ru.vladik.myapplication.Exceptions.CouldNotGetDateException;
import ru.vladik.myapplication.R;
import ru.vladik.myapplication.Utils.DrawableHelper;
import ru.vladik.myapplication.Utils.StaticRecourses;

public class ScheduleAdapter extends ArrayAdapter<LessonWithMarks> {

    private final int resourceLayout;
    private Schedule schedule;
    private int day;

    public void setDay(int day) {
        this.day = day;
        clear();
        addAll(schedule.get(day));
        notifyDataSetChanged();
    }



    public int getDay() {
        return day;
    }

    public int getDayWhereSundayIsFirst() {
        int day = this.day + 1;
        return day != 8 ? day : 1;
    }

    public int getLastDay() {
        return schedule.size();
    }

    public ScheduleAdapter(@NonNull Context context, int resource) {
        super(context, resource);
        resourceLayout = resource;
        Calendar calendar = Calendar.getInstance(new Locale(StaticRecourses.LOCALE_RU));
        day = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (day == 0) {
            day = 7;
        }
    }

    public void refreshList(Schedule lessonList) {
        this.schedule = lessonList;
        clear();
        if (schedule.size() > day) {
            addAll(schedule.get(day));
        }
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TimeTable timeTable = StaticRecourses.timeTable;

        //Getting view to inflate
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(resourceLayout, null);
        }

        //Preparing data
        Lesson lesson = getItem(position).getLesson();
        List<Mark> markList = getItem(position).getMarks();
        TextView lessonName = view.findViewById(R.id.subject_name_in_schedule_item);
        TextView lessonText= view.findViewById(R.id.subject_text_in_schedule_item);
        LinearLayout marksTextLayout = view.findViewById(R.id.marks_in_schedule_item_layout);
        TextView lessonTime = view.findViewById(R.id.subject_time_in_schedule_item);

        //Preparing data for homework
        StringBuilder lessonTextString = new StringBuilder();
        for (Work work : lesson.getWorks()) {
            if (work.getType().equals("Homework")) {
                lessonTextString.append(work.getText()).append(" ");
            }
        }

        //Prepare and set data for time of lesson
        String date;
        try {
            lessonTime.setVisibility(View.VISIBLE);
            String start = timeTable.getItems().get(lesson.getNumber()-1).getStart();
            String finish = timeTable.getItems().get(lesson.getNumber()-1).getFinish();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm", new Locale(StaticRecourses.LOCALE_RU));
            Date dateStart = getDateFromString(start);
            Date dateEnd = getDateFromString(finish);
            date = simpleDateFormat.format(dateStart) + " - " + simpleDateFormat.format(dateEnd);
            lessonTime.setText(date);
        } catch (CouldNotGetDateException|IndexOutOfBoundsException e) {
            lessonTime.setVisibility(View.GONE);
        }

        //Set data for marks
        if (!markList.isEmpty()) {
            marksTextLayout.setVisibility(View.VISIBLE);
            marksTextLayout.removeAllViews();
            for (Mark mark : markList) {
                TextView markText = new TextView(getContext());
                markText.setTextSize(20);
                markText.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                markText.setText(mark.getTextValue());
                markText.setTextColor(DrawableHelper.getColorByMood(getContext(),
                        mark.getMood(), Color.BLACK));
                markText.setOnClickListener((textView) -> {
                    Intent intent = new Intent(getContext(), MarkInfoActivity.class);
                    intent.putExtra("a", new FullMark(
                            mark,
                            lesson,
                            lesson.getSubject()
                    ));
                    getContext().startActivity(intent);
                });
                markText.setPadding(5, 5, 5, 5);
                marksTextLayout.addView(markText);
            }
        } else {
            marksTextLayout.setVisibility(View.GONE);
        }

        //Set data for homework
        if (lessonTextString.length() == 0) {
            lessonText.setVisibility(View.GONE);
        } else {
            lessonText.setVisibility(View.VISIBLE);
            lessonText.setText(lessonTextString.toString());
        }

        //Set data for time of lesson
        String lessonNameText = lesson.getNumber() > 0 ?
                lesson.getNumber() + ". " + lesson.getSubject().getName() :
                lesson.getSubject().getName();
        lessonName.setText(lessonNameText);
        return view;
    }

}
