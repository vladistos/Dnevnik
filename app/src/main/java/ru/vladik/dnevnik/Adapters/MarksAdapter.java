package ru.vladik.dnevnik.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ru.vladik.dnevnik.Activities.MarkInfoActivity;
import ru.vladik.dnevnik.DiaryAPI.DataClasses.v2.Mood;
import ru.vladik.dnevnik.DiaryAPI.DataClasses.v6.Mark;
import ru.vladik.dnevnik.DiaryAPI.DataClasses.v6.MarkSubject;
import ru.vladik.dnevnik.R;
import ru.vladik.dnevnik.Utils.StaticRecourses;

public class MarksAdapter extends RecyclerView.Adapter<MarksAdapter.ViewHolder> {

    private final LayoutInflater mInflater;
    private List<Mark> markList;

    @SuppressLint("NotifyDataSetChanged")
    public void refreshList(List<Mark> markList) {
        this.markList = markList;
        notifyDataSetChanged();
    }

    private final Drawable shapeGood, shapeAverage, shapeBad;
    private final Context context;

    public MarksAdapter(@NonNull Context context) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        shapeGood = AppCompatResources.getDrawable(context, R.drawable.good_shape_gradient_drawable);
        shapeAverage = AppCompatResources.getDrawable(context, R.drawable.average_shape_gradient_drawable);
        shapeBad = AppCompatResources.getDrawable(context, R.drawable.bad_shape_gradient_drawable);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.mark_element, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Mark mark = markList.get(position);
        MarkSubject subject = mark.getSubject();
        holder.markSubject.setText(subject.getName());
        String markText = "";
        if (mark.getMarks().size() == 2) {
            markText = mark.getMarks().get(0).getValue() + "/" + mark.getMarks().get(1).getValue();
        } else if (mark.getMarks().size() == 1){
            markText = mark.getMarks().get(0).getValue();
        }
        holder.markText.setText(markText);
        RippleDrawable rippleDrawable = (RippleDrawable) AppCompatResources.getDrawable(context, R.drawable.selectable_rounded);
        if (rippleDrawable != null && rippleDrawable.getNumberOfLayers() == 0) {
            rippleDrawable.addLayer(new ColorDrawable());
        }
        if (rippleDrawable != null) {
            switch (mark.getMarks().get(0).getMood()) {
                case Mood.GOOD:
                    rippleDrawable.setDrawable(0, shapeGood);
                    break;
                case Mood.AVERAGE:
                    rippleDrawable.setDrawable(0, shapeAverage);
                    break;
                case Mood.BAD:
                    rippleDrawable.setDrawable(0, shapeBad);
                    break;
            }
        }
        holder.markLayout.setBackground(rippleDrawable);
        holder.mainLayout.setOnClickListener((view) ->{
            Intent intent = new Intent(context, MarkInfoActivity.class);
            intent.putExtra(StaticRecourses.INTENT_WITH_MARK_NAME, mark);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return markList == null ? 0 : markList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView markText, markSubject;
        RelativeLayout mainLayout;
        RelativeLayout markLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mainLayout = itemView.findViewById(R.id.recycle_main);
            markText = itemView.findViewById(R.id.mark_text);
            markSubject = itemView.findViewById(R.id.mark_subject);
            markLayout = itemView.findViewById(R.id.mark_layout);
        }
    }
}
