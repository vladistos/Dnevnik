package ru.vladik.dnevnik.Fragments;

import static ru.vladik.dnevnik.Utils.StaticRecourses.LOCALE_RU;
import static ru.vladik.dnevnik.Utils.StaticRecourses.UserContext;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ru.vladik.dnevnik.Adapters.ScheduleAdapter;
import ru.vladik.dnevnik.DiaryAPI.DataClasses.v2.LessonWithMarks;
import ru.vladik.dnevnik.DiaryAPI.DataClasses.v2.Schedule;
import ru.vladik.dnevnik.DiaryAPI.DiaryAPI;
import ru.vladik.dnevnik.R;
import ru.vladik.dnevnik.Utils.AsyncUtil;
import ru.vladik.dnevnik.Utils.DateHelper;
import ru.vladik.dnevnik.Utils.DiarySingleton;
import ru.vladik.dnevnik.Utils.LayoutHelper;
import ru.vladik.dnevnik.Utils.NetworkHelper;
import ru.vladik.dnevnik.Utils.SwipeListener;
import ru.vladik.dnevnik.databinding.FragmentScheduleBinding;


public class ScheduleFragment extends Fragment {

    private DiaryAPI diaryAPI;
    private Schedule schedule;
    private FragmentScheduleBinding binding;
    private TextView dateTextView;
    private ListView scheduleListView;
    private int weekOffset;

    public ScheduleFragment() {
    }

    private long getDaysDelta(Calendar time1, Calendar time2) {
        LocalDate localDate1 = LocalDate.of(
                time1.get(Calendar.YEAR),
                time1.get(Calendar.MONTH) + 1,
                time1.get(Calendar.DAY_OF_MONTH)
        );
        LocalDate localDate2 = LocalDate.of(
                time2.get(Calendar.YEAR),
                time2.get(Calendar.MONTH) + 1,
                time2.get(Calendar.DAY_OF_MONTH)
        );
        return ChronoUnit.DAYS.between(localDate1, localDate2);
    }

    private long getInt(double number) {
        double afterPoint = number%1;
        return (int) (number-afterPoint);
    }

    private int getWeekOffset(int daysDelta, int nowDay) {
        return nowDay + daysDelta <= 0 ? (nowDay + daysDelta) / 7 - 1 : 0;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentScheduleBinding.inflate(inflater, container, false);
        scheduleListView = binding.scheduleListView;
        dateTextView = binding.textAboveSchedule;
        ViewGroup view = binding.getRoot();
        weekOffset = 0;
        if (getContext() != null) {
            scheduleListView.setAdapter(new ScheduleAdapter(getContext(), R.layout.schedule_list_element));
            diaryAPI = DiarySingleton.getInstance(getContext()).getDiaryAPI();
        }

        dateTextView.setOnClickListener((textView -> {
            Calendar calendar = Calendar.getInstance(new Locale(LOCALE_RU));
            calendar.setTime(getDateOnSchedule());
            DatePickerDialog.OnDateSetListener listener = (datePickerView, year, month, dayOfMonth) -> {
                Calendar calendar1 = Calendar.getInstance(new Locale(LOCALE_RU));
                calendar1.set(year, month, dayOfMonth, 0, 0, 0);
                int days = (int) getDaysDelta(calendar, calendar1);
                changeDay(days);
            };
            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), listener,
                    calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        }));

        binding.arrowAboveScheduleBefore.setOnClickListener((v) ->
                changeDay(-1));

        binding.arrowAboveScheduleNext.setOnClickListener((v) ->
                changeDay(1));
        LayoutHelper.setLoading(view, true, null);
        return view;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        NetworkHelper.startAsyncTaskCatchingApiErrors(getContext(), () -> {
            ScheduleAdapter scheduleAdapter = (ScheduleAdapter) scheduleListView.getAdapter();
            if (schedule == null) {
                schedule = diaryAPI.getScheduleWithOffset(
                        UserContext.getGroupIds().get(0),
                        UserContext.getPersonId(),
                        UserContext.getSchoolIds().get(0),
                        weekOffset
                );
            }
            List<Long> groups = UserContext.getGroupIds();
            for (List<LessonWithMarks> lessonList : schedule) {
                lessonList.removeIf(lessonWithMarks ->
                        !groups.contains(
                                lessonWithMarks.getLesson().getGroup()) &&
                                lessonWithMarks.getLesson().getId() != -1
                );
            }
            AsyncUtil.executeInMain(() -> {
                setDayOnAdapter(scheduleAdapter, scheduleAdapter.getDay(), schedule);
                scheduleListView.setOnTouchListener(new SwipeListener(getContext()) {
                    @Override
                    public void onSwipeLeft() {
                        changeDay(1);
                    }

                    @Override
                    public void onSwipeRight() {
                        changeDay(-1);
                    }
                });
                LayoutHelper.setLoading(binding.getRoot(), false, null);
            });
        });
    }

    private Date getDateOnSchedule() {
        ScheduleAdapter adapter = (ScheduleAdapter) scheduleListView.getAdapter();
        Calendar calendar = Calendar.getInstance(new Locale(LOCALE_RU));
        calendar.add(Calendar.WEEK_OF_YEAR, weekOffset);
        int day = adapter.getDayWhereSundayIsFirst();
        calendar.set(Calendar.DAY_OF_WEEK, day);
        return calendar.getTime();
    }

    private void setDayOnAdapter(@NonNull ScheduleAdapter adapter, int day, @Nullable Schedule schedule) {
        if (schedule == null || schedule.get(day) != null) {
            if (schedule != null) {
                adapter.refreshList(schedule);
                for (List<LessonWithMarks> lessonList : schedule) {
                    List<Long> groups = UserContext.getGroupIds();
                    lessonList.removeIf(lessonWithMarks ->
                            !groups.contains(
                                    lessonWithMarks.getLesson().getGroup()) &&
                                    lessonWithMarks.getLesson().getId() != -1
                    );
                }
            }
            adapter.setDay(day);
            String stringText = DateHelper.getDateRU(getDateOnSchedule(), adapter.getDay());
            dateTextView.setText(stringText);
        }

    }



    private void changeDay(int daysDelta) {
        ScheduleAdapter adapter = (ScheduleAdapter) scheduleListView.getAdapter();
        if (adapter.getDay() + daysDelta > adapter.getLastDay() || adapter.getDay() + daysDelta < 1) {
            Log.d("main", String.valueOf(daysDelta));
            NetworkHelper.startAsyncTaskCatchingApiErrors(getContext(), () -> {
                int weekOffsetDelta =
                        daysDelta > 0 ?
                                (int) getInt((double) (daysDelta + adapter.getDay() - 1) / 7) :
                                getWeekOffset(daysDelta, adapter.getDay());
                int day = adapter.getDay() + daysDelta - weekOffsetDelta * 7;
                weekOffset += weekOffsetDelta;
                Schedule schedule = diaryAPI.getScheduleWithOffset(
                        UserContext.getGroupIds().get(0),
                        UserContext.getPersonId(),
                        UserContext.getSchoolIds().get(0),
                        weekOffset
                );

                AsyncUtil.executeInMain(() -> setDayOnAdapter(adapter, day, schedule));

            });
        } else {
            setDayOnAdapter(adapter, adapter.getDay() + daysDelta, null);
        }
    }
}