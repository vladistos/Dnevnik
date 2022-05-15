package ru.vladik.myapplication.Activities;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.vladik.myapplication.DiaryAPI.DataClasses.v2.FullMark;
import ru.vladik.myapplication.DiaryAPI.DataClasses.v2.Lesson;
import ru.vladik.myapplication.DiaryAPI.DataClasses.v2.Mark;
import ru.vladik.myapplication.DiaryAPI.DataClasses.v2.Person;
import ru.vladik.myapplication.DiaryAPI.DataClasses.v2.Subject;
import ru.vladik.myapplication.DiaryAPI.DiaryAPI;
import ru.vladik.myapplication.R;
import ru.vladik.myapplication.Utils.AsyncUtil;
import ru.vladik.myapplication.Utils.DateHelper;
import ru.vladik.myapplication.Utils.DiarySingleton;
import ru.vladik.myapplication.Utils.DrawableHelper;
import ru.vladik.myapplication.Utils.LayoutHelper;
import ru.vladik.myapplication.Utils.StaticRecourses;
import ru.vladik.myapplication.databinding.MarkDescriptionItemBinding;
import ru.vladik.myapplication.databinding.MarkInfoDialogBinding;

public class MarkInfoActivity extends AppCompatActivity {

    private Mark mark;
    private MarkInfoDialogBinding binding;
    private HorizontalBarChart markStatisticBarChart;
    private Lesson lesson;
    private PieChart markPieChart;
    private Subject subject;
    private DiaryAPI diaryAPI;
    private Map<Person, List<Mark>> personMarkHashMap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        diaryAPI = DiarySingleton.getInstance(this).getDiaryAPI();
        binding = MarkInfoDialogBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Object o = getIntent().getSerializableExtra("a"); //TODO rename
        if (o != null) {
            FullMark mark = (FullMark) o;
            this.mark = mark.getMark();
            lesson = mark.getLesson();
            subject = mark.getSubject();
            LayoutHelper.setLoading(binding.mainLayoutInWorkInfoDialog, true, null);
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_ios_new_24);
            toolbar.setNavigationOnClickListener(v -> onBackPressed());
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle("Оценка");

            }

            setBind();
            setData();
        }
    }

    private void setData() {
        AsyncUtil.startAsyncTask(() -> {
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

            AsyncUtil.executeInMain(() -> {
                for (List<Mark> markList : listOfMarkLists) {
                    MarkDescriptionItemBinding bindingLegend = MarkDescriptionItemBinding.inflate(getLayoutInflater(), binding.legendInMarksDialog, false);
                    if (markList.size() > 0) {
                        TextView markTextView = bindingLegend.markInMarkDescribeItem;
                        TextView marksCountTextView = bindingLegend.markDescriptionInMarkDescribeItem;
                        Mark mark = markList.get(0);
                        String text = String.valueOf(markList.size()) ;
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
                binding.graphInMarksDialog.setData(new PieData(pieDataSet));
                if (subject != null) {
                    binding.descriptionInMarksDialog.append(" по предмету ");
                    binding.descriptionInMarksDialog.append(subject.getName());
                }
                try {
                    if (lesson.getDate() != null && !lesson.getDate().isEmpty()) {
                        binding.descriptionInMarksDialog.append(" за ");
                        binding.descriptionInMarksDialog.append(
                                DateHelper.getStringDate(DateHelper.getDateFromString(lesson.getDate()))
                        );
                    }

                } catch (Exception ignored) {

                }
                markPieChart.animateY(1000, Easing.EaseOutBack);
                LayoutHelper.setLoading(binding.mainLayoutInWorkInfoDialog, false, null);
            });
        });
    }


    public void setBind() {
        markPieChart = binding.graphInMarksDialog;
        markStatisticBarChart = binding.markStatisticChart;

        markPieChart.setEntryLabelTextSize(0);
        markPieChart.getLegend().setEnabled(false);
        markPieChart.getLegend().setTextSize(15);
        markPieChart.getDescription().setTextSize(15);
        markPieChart.setDrawHoleEnabled(true);
        markPieChart.setHoleRadius(80);
        markPieChart.setCenterTextColor(DrawableHelper.getColorByMood(this, mark.getMood(), Color.BLACK));
        markPieChart.setCenterText(mark.getTextValue());
        markPieChart.setCenterTextSize(35);
        markPieChart.setCenterTextTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        markPieChart.setDescription(null);
        markPieChart.setTouchEnabled(false);

        markStatisticBarChart.setDrawGridBackground(false);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
