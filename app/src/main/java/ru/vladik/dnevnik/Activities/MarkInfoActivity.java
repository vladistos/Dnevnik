package ru.vladik.dnevnik.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.vladik.dnevnik.DataClasses.RatingData;
import ru.vladik.dnevnik.DiaryAPI.DataClasses.v2.FullMark;
import ru.vladik.dnevnik.DiaryAPI.DataClasses.v2.Mark;
import ru.vladik.dnevnik.DiaryAPI.DataClasses.v2.Person;
import ru.vladik.dnevnik.DiaryAPI.DiaryAPI;
import ru.vladik.dnevnik.Exceptions.CouldNotGetDateException;
import ru.vladik.dnevnik.R;
import ru.vladik.dnevnik.Utils.AsyncUtil;
import ru.vladik.dnevnik.Utils.DateHelper;
import ru.vladik.dnevnik.Utils.DiarySingleton;
import ru.vladik.dnevnik.Utils.DrawableHelper;
import ru.vladik.dnevnik.Utils.LayoutHelper;
import ru.vladik.dnevnik.Utils.NetworkHelper;
import ru.vladik.dnevnik.Utils.NumberUtil;
import ru.vladik.dnevnik.Utils.StaticRecourses;
import ru.vladik.dnevnik.databinding.MarkDescriptionItemBinding;
import ru.vladik.dnevnik.databinding.MarkInfoDialogBinding;

public class MarkInfoActivity extends AppCompatActivity {

    private Mark mark;
    private ru.vladik.dnevnik.DiaryAPI.DataClasses.v6.Mark markV6;
    private MarkInfoDialogBinding binding;
    private PieChart markPieChart;
    private DiaryAPI diaryAPI;
    private Map<Person, List<Mark>> personMarkHashMap;

    private String date, subjectName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        diaryAPI = DiarySingleton.getInstance(this).getDiaryAPI();
        binding = MarkInfoDialogBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Object o = getIntent().getSerializableExtra(StaticRecourses.INTENT_WITH_MARK_NAME);
        if (o != null) {
            try {
                FullMark mark = (FullMark) o;
                this.mark = mark.getMark();
                if (mark.getLesson() != null && mark.getLesson().getDate() != null) {
                    date = DateHelper.getStringDate(DateHelper.getDateFromString(mark.getLesson().getDate()));
                }
                if (mark.getSubject() != null && mark.getSubject().getName() != null) {
                    subjectName = mark.getSubject().getName();
                }
            } catch (ClassCastException e) {
                ru.vladik.dnevnik.DiaryAPI.DataClasses.v6.Mark mark = (ru.vladik.dnevnik.DiaryAPI.DataClasses.v6.Mark) o;
                this.mark = null;
                this.markV6 = mark;
                if (mark.getLesson() != null && mark.getLesson().getDate() != null) {
                    date = DateHelper.getStringDate(new Date(mark.getLesson().getDate()));
                }
                if (mark.getSubject() != null && mark.getSubject().getName() != null) {
                    subjectName = mark.getSubject().getName();
                }
            } catch (CouldNotGetDateException e) {
                date = null;
            }
            LayoutHelper.setLoading(binding.mainLayoutInWorkInfoDialog, true, null);
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_ios_new_24);
            toolbar.setNavigationOnClickListener(v -> onBackPressed());
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle("Оценка");

            }
            markPieChart = binding.graphInMarksDialog;
            setData();
        }
    }

    private void setData() {
        NetworkHelper.startAsyncTaskCatchingApiErrors(this, () -> {
            if (mark == null && markV6 != null && markV6.getMarks().size() > 0) {
                mark = diaryAPI.getMarkById(markV6.getMarks().get(0).getId());
            }
            if (mark != null) {
                List<Mark> workMarks = diaryAPI.getMarksByWork(mark.getWork());
                List<Person> personList = StaticRecourses.classmatePersonsList;
                Map<Person, List<Mark>> personMarkMap = new HashMap<>();
                for (Mark mark : workMarks) {
                    for (Person person : personList) {
                        if (mark.getPerson().equals(person.getId())) {
                            if (!personMarkMap.containsKey(person)) {
                                List<Mark> markList = new ArrayList<>();
                                markList.add(mark);
                                personMarkMap.put(person, markList);
                                break;
                            } else {
                                List<Mark> markList = personMarkMap.get(person);
                                if (markList == null) {
                                    markList = new ArrayList<>();
                                }
                                markList.add(mark);
                                personMarkMap.put(person, markList);
                            }
                        }
                    }
                }
                this.personMarkHashMap = personMarkMap;
                List<PieEntry> entries = new ArrayList<>(4);

                PieDataSet pieDataSet = new PieDataSet(new ArrayList<>(), "");
                pieDataSet.setValueTextSize(15);
                pieDataSet.setValueTextColor(0x00);
                pieDataSet.setSliceSpace(-1);
                pieDataSet.setValueFormatter(new DefaultValueFormatter(0));
                pieDataSet.setColors(new ArrayList<>());
                pieDataSet.clear();

                Map<String, List<Mark>> marksValueMap = new HashMap<>();

                for (Mark mark : workMarks) {
                    List<Mark> marks;
                    if (!marksValueMap.containsKey(mark.getTextValue())) {
                        marks = new ArrayList<>();
                    } else {
                        marks = marksValueMap.get(mark.getTextValue());
                        if (marks == null) {
                            marks = new ArrayList<>();
                        }
                    }
                    marks.add(mark);
                    marksValueMap.put(mark.getTextValue(), marks);
                }

                Map<List<Mark>, Integer> colorMap = new HashMap<>();
                List<List<Mark>> listOfMarkLists = new ArrayList<>();
                for (String markVal : marksValueMap.keySet()) {
                    List<Mark> markList = marksValueMap.get(markVal);
                    if (markList != null && markList.size() > 0) {
                        colorMap.put(marksValueMap.get(markVal), DrawableHelper.getColorByMood(this,
                                markList.get(0).getMood(), Color.WHITE));
                    }
                    listOfMarkLists.add(markList);
                }
                for (List<Mark> markList : colorMap.keySet()) {
                    if (markList.size() != 0) {
                        PieEntry entry = new PieEntry(markList.size(), markList.get(0).getTextValue());
                        Integer color = colorMap.get(markList);
                        if (color != null) {
                            pieDataSet.addColor(color);
                        }
                        entries.add(entry);
                    }
                }

                BarDataSet barDataSet = new BarDataSet(new ArrayList<>(), "Оценки класса");
                HashMap<Double, List<String>> markNameMap = new HashMap<>();
                for (Person person : personMarkMap.keySet()) {
                    List<Mark> markList = personMarkMap.get(person);
                    if (markList != null) {
                        double avg = 0;
                        for (Mark mark : markList) {
                            avg += NumberUtil.getIntByMarkOrZero(mark.getTextValue());
                        }
                        avg = avg/markList.size();
                        List<String> nameList1 = null;
                        if (markNameMap.containsKey(avg)) {
                            nameList1 = markNameMap.get(avg);
                        }
                        if (nameList1 == null) {
                            nameList1 = new ArrayList<>();
                        }
                        nameList1.add(person.getShortName());
                        markNameMap.put(avg, nameList1);
                    }
                }

                Double[] values = markNameMap.keySet().toArray(new Double[0]);
                RatingData ratingData = new RatingData();

                for (Double v : values) {
                    List<String> pupils = markNameMap.get(v);
                    if (pupils != null) {
                        for (String pupil : pupils) {
                            ratingData.add(new RatingData.RatingEntry(pupil, v));
                        }
                        if (ratingData.getMaxVal() < v) {
                            ratingData.setMaxVal(v);
                        }
                    }
                }

                AsyncUtil.executeInMain(() -> {
                    binding.markStatisticRedirect.setOnClickListener((v -> {
                        Intent intent = new Intent(MarkInfoActivity.this, RatingActivity.class);
                        intent.putExtra(StaticRecourses.INTENT_WITH_RATING_NAME, ratingData);
                        startActivity(intent);
                    }));
                    setBind();
                    for (List<Mark> markList : listOfMarkLists) {
                        MarkDescriptionItemBinding bindingLegend = MarkDescriptionItemBinding
                                .inflate(getLayoutInflater(), binding.legendInMarksDialog, false);
                        if (markList.size() > 0) {
                            TextView markTextView = bindingLegend.markInMarkDescribeItem;
                            TextView marksCountTextView = bindingLegend.markDescriptionInMarkDescribeItem;
                            Mark mark = markList.get(0);
                            String text = String.valueOf(markList.size());
                            marksCountTextView.setText(text);
                            GradientDrawable Rdrawable = (GradientDrawable) AppCompatResources
                                    .getDrawable(this, R.drawable.solid_rounded);

                            if (Rdrawable != null) {
                                GradientDrawable drawable = (GradientDrawable) Rdrawable.getConstantState().newDrawable().mutate();
                                drawable.setColor(DrawableHelper.getColorByMood(this, mark.getMood(), Color.WHITE));
                                markTextView.setBackground(drawable);
                            }
                            markTextView.setText(mark.getTextValue());

                            binding.legendInMarksDialog.addView(bindingLegend.getRoot());
                        }
                    }
                    binding.legendInMarksDialog.setEnabled(false);
                    pieDataSet.setValues(entries);
                    markPieChart.setData(new PieData(pieDataSet));
                    binding.descriptionInMarksDialog.append(" по предмету ");
                    binding.descriptionInMarksDialog.append(subjectName);
                    if (date != null) {
                        binding.descriptionInMarksDialog.append(" за ");
                        binding.descriptionInMarksDialog.append(date);
                    }

                    BarData data = new BarData(barDataSet);
                    data.setDrawValues(true);

                    markPieChart.animateY(1000, Easing.EaseOutBack);
                    LayoutHelper.setLoading(binding.mainLayoutInWorkInfoDialog, false, null);
                });
            }
        });

    }



    public void setBind() {

        markPieChart.setEntryLabelTextSize(0);
        markPieChart.getLegend().setEnabled(false);
        markPieChart.getLegend().setTextSize(15);
        markPieChart.getDescription().setTextSize(15);
        markPieChart.setDrawHoleEnabled(true);
        markPieChart.setHoleRadius(80);
        markPieChart.setCenterTextColor(DrawableHelper.getColorByMood(this, mark.getMood(), Color.BLACK));
        if (markV6 == null) {
            markPieChart.setCenterText(mark.getTextValue());
        } else {
            if (markV6.getMarks().size() == 1) {
                markPieChart.setCenterText(markV6.getMarks().get(0).getValue());
            } else {
                String centerText = markV6.getMarks().get(0).getValue() + "/" + markV6.getMarks().get(1).getValue();
                markPieChart.setCenterText(centerText);
            }
        }
        markPieChart.setCenterTextSize(35);
        markPieChart.setCenterTextTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        markPieChart.setDescription(null);
        markPieChart.setTouchEnabled(false);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
