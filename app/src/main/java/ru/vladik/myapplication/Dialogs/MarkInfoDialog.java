package ru.vladik.myapplication.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.ViewGroup;
import android.widget.ListView;
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

import ru.vladik.myapplication.Adapters.LegendAdapter;
import ru.vladik.myapplication.DiaryAPI.DataClasses.FullMark;
import ru.vladik.myapplication.DiaryAPI.DataClasses.Lesson;
import ru.vladik.myapplication.DiaryAPI.DataClasses.Mark;
import ru.vladik.myapplication.DiaryAPI.DataClasses.Person;
import ru.vladik.myapplication.DiaryAPI.DataClasses.Subject;
import ru.vladik.myapplication.DiaryAPI.DiaryAPI;
import ru.vladik.myapplication.R;
import ru.vladik.myapplication.Utils.AsyncUtil;
import ru.vladik.myapplication.Utils.DateHelper;
import ru.vladik.myapplication.Utils.DiarySingleton;
import ru.vladik.myapplication.Utils.DrawableHelper;
import ru.vladik.myapplication.Utils.LayoutHelper;
import ru.vladik.myapplication.Utils.StaticRecourses;

public class MarkInfoDialog extends Dialog {

    private final Mark mark;
    private Map<Person, List<Mark>> personMarkHashMap;
    private final Lesson lesson;
    private final Subject subject;
    private final DiaryAPI diaryAPI;
    private final ViewGroup mainLayout;

    public MarkInfoDialog(@NonNull Context context, FullMark mark) {
        super(context);
        diaryAPI = DiarySingleton.getInstance().getDiaryAPI();
        setContentView(R.layout.mark_info_dialog);
        this.mark = mark.getMark();
        lesson = mark.getLesson();
        subject = mark.getSubject();
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
            List<List<Mark>> listOfMarkLists = new ArrayList<>();
            for (String markVal : marksValueMap.keySet()) {
                List<Mark> markList = marksValueMap.get(markVal);
                if (markList != null && markList.size() > 0) {
                    colorMap.put(marksValueMap.get(markVal), DrawableHelper.getColorByMood(getContext(),
                            markList.get(0).getMood(), Color.WHITE));
                }
                listOfMarkLists.add(markList);
            }
            ((LegendAdapter) holder.legendListView.getAdapter()).refreshList(listOfMarkLists);
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
                if (subject != null) {
                    holder.descriptionTextView.append(" по предмету ");
                    holder.descriptionTextView.append(subject.getName());
                }
                try {
                    if (lesson.getDate() != null && !lesson.getDate().isEmpty()) {
                        holder.descriptionTextView.append(" за ");
                        holder.descriptionTextView.append(
                                DateHelper.getStringDate(DateHelper.getDateFromString(lesson.getDate()))
                        );
                    }

                } catch (Exception ignored) {

                }
                LayoutHelper.setLoading(mainLayout, false, null);
            });
        });
    }

    private class ViewHolder{

         PieChart marksGraph;
         TextView markDescriptionTextView, descriptionTextView;
         ListView legendListView;

        public ViewHolder() {
            marksGraph =findViewById(R.id.graph_in_marks_dialog);
            markDescriptionTextView = findViewById(R.id.mark_description_in_marks_dialog);
            legendListView = findViewById(R.id.legend_in_marks_dialog);
            descriptionTextView = findViewById(R.id.description_in_marks_dialog);
            markDescriptionTextView.setText("Выберите оценку на диаграмме");
            marksGraph.setEntryLabelTextSize(0);
            marksGraph.getLegend().setEnabled(false);
            marksGraph.getLegend().setTextSize(15);
            marksGraph.getDescription().setTextSize(15);
            marksGraph.setDrawHoleEnabled(true);
            marksGraph.setHoleRadius(80);
            marksGraph.setCenterTextColor(DrawableHelper.getColorByMood(getContext(), mark.getMood(), Color.BLACK));
            marksGraph.setCenterText(mark.getTextValue());
            marksGraph.setCenterTextSize(30);
            marksGraph.setCenterTextTypeface(Typeface.defaultFromStyle(Typeface.BOLD));

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
                    int j = 0;
                    for (Person person : personMarkHashMap.keySet()) {
                        j++;
                        List<Mark> markList = personMarkHashMap.get(person);
                        if (markList != null) {
                            int i = 0;
                            for (Mark mark : markList) {
                                if (mark.getTextValue().equals((((PieEntry) e).getLabel()))) {
                                    if (i == 0) {
                                        descriptionTextBuilder
                                                .append(person.getShortName())
                                                .append(" - ");
                                        descriptionTextBuilder.append(mark.getTextValue());
                                    } else {
                                        descriptionTextBuilder.append("/").append(mark.getTextValue());
                                    }
                                    i++;
                                }
                            }
                            if (j > 0 && j < personMarkHashMap.keySet().size() && i > 0) {
                                descriptionTextBuilder.append("\n");
                            }
                        }
                    }
                    String s = descriptionTextBuilder.toString();
                    if (s.endsWith("\n")) {
                        s = s.substring(0, s.length()-1);
                    }
                    markDescriptionTextView.setText(s);
                }

                @Override
                public void onNothingSelected() {
                    markDescriptionTextView.setText("Выберите оценку на диаграмме");
                }
            });
            legendListView.setEnabled(false);
            legendListView.setAdapter(new LegendAdapter(getContext(), R.layout.mark_description_item));
        }
    }
}
