package ru.vladik.myapplication.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.vladik.myapplication.DiaryAPI.DataClasses.FullMark;
import ru.vladik.myapplication.DiaryAPI.DataClasses.Mark;
import ru.vladik.myapplication.DiaryAPI.DataClasses.Mood;
import ru.vladik.myapplication.DiaryAPI.DataClasses.Person;
import ru.vladik.myapplication.DiaryAPI.DiaryAPI;
import ru.vladik.myapplication.R;
import ru.vladik.myapplication.Utils.AsyncUtil;
import ru.vladik.myapplication.Utils.DiarySingleton;
import ru.vladik.myapplication.Utils.DrawableHelper;
import ru.vladik.myapplication.Utils.LayoutHelper;
import ru.vladik.myapplication.Utils.StaticRecourses;

public class MarkInfoDialog extends Dialog {

    private final Mark mark;
    private Map<Person, List<Mark>> personMarkHashMap;
    private final DiaryAPI diaryAPI;
    private final ViewGroup mainLayout;

    public MarkInfoDialog(@NonNull Context context, FullMark mark) {
        super(context);
        diaryAPI = DiarySingleton.getInstance().getDiaryAPI();
        setContentView(R.layout.mark_info_dialog);
        this.mark = mark.getMark();
        mainLayout = findViewById(R.id.main_layout_in_work_info_dialog);
        LayoutHelper.setLoading(mainLayout, true, null);
        ViewHolder holder = new ViewHolder();
        setData(holder);
    }

    private void setData(ViewHolder holder) {
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

            PieDataSet dataSet = new PieDataSet(new ArrayList<>(), "");
            dataSet.setValueTextSize(15);
            dataSet.setValueTextColor(0x00);
            dataSet.setSliceSpace(5);
            dataSet.setValueFormatter(new DefaultValueFormatter(0));
            dataSet.setColors(new ArrayList<>());
            dataSet.clear();

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
            for (String markVal : marksValueMap.keySet()) {
                List<Mark> markList = marksValueMap.get(markVal);
                if (markList != null && markList.size() > 0) {
                    switch (markList.get(0).getMood()) {
                        case Mood.GOOD:
                            colorMap.put(marksValueMap.get(markVal), DrawableHelper.GOOD_MOOD_COLOR);
                            break;
                        case Mood.AVERAGE:
                            colorMap.put(marksValueMap.get(markVal), DrawableHelper.AVERAGE_MOOD_COLOR);
                            break;
                        case Mood.BAD:
                            colorMap.put(marksValueMap.get(markVal), DrawableHelper.BAD_MOOD_COLOR);
                            break;
                        default:
                            colorMap.put(marksValueMap.get(markVal), Color.WHITE);
                            break;
                    }
                }
            }
            for (List<Mark> markList : colorMap.keySet()) {
                if (markList.size() != 0) {
                    PieEntry entry = new PieEntry(markList.size(), markList.get(0).getTextValue());
                    Integer color = colorMap.get(markList);
                    if (color != null) {
                        dataSet.addColor(color);
                    }
                    entries.add(entry);
                }
            }

            AsyncUtil.executeInMain(() -> {
                dataSet.setValues(entries);
                holder.marksGraph.setData(new PieData(dataSet));
                LayoutHelper.setLoading(mainLayout, false, null);
            });
        });
    }

    private class ViewHolder{

         PieChart marksGraph;
         TextView markDescriptionTextView;

        public ViewHolder() {
            marksGraph =findViewById(R.id.graph_in_marks_dialog);
            markDescriptionTextView = findViewById(R.id.mark_description_in_marks_dialog);
            markDescriptionTextView.setText("Выберите оценку на диаграмме");
            marksGraph.setCenterTextSize(15);
            marksGraph.setEntryLabelTextSize(20);
            marksGraph.getLegend().setEnabled(false);
            marksGraph.getDescription().setTextSize(15);
            marksGraph.setDrawHoleEnabled(true);
            marksGraph.setDescription(null);
            marksGraph.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                @Override
                public void onValueSelected(Entry e, Highlight h) {
                    StringBuilder descriptionTextBuilder = new StringBuilder();
                    descriptionTextBuilder
                            .append("В классе ")
                            .append((int) ((PieEntry) e).getValue())
                            .append(" оценок ")
                            .append(((PieEntry) e).getLabel())
                            .append("\n");
                    for (Person person : personMarkHashMap.keySet()) {
                        List<Mark> markList = personMarkHashMap.get(person);
                        if (markList != null) {
                            Log.d("main", markList.toString());
                            int i = 0;
                            for (Mark mark : markList) {
                                if (mark.getTextValue().equals((((PieEntry) e).getLabel()))) {
                                    if (i == 0) {
                                        descriptionTextBuilder
                                                .append(person.getShortName())
                                                .append(" получил ");
                                        descriptionTextBuilder.append(mark.getTextValue());
                                    } else {
                                        descriptionTextBuilder.append("/").append(mark.getTextValue());
                                    }
                                    i++;
                                }
                            }
                            if (i > 0) {
                                descriptionTextBuilder.append("\n");
                            }
                        }

                    }

                    markDescriptionTextView.setText(descriptionTextBuilder.toString());
                }

                @Override
                public void onNothingSelected() {
                    markDescriptionTextView.setText("Выберите оценку на диаграмме");
                }
            });
        }
    }
}
