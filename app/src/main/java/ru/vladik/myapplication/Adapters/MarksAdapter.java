package ru.vladik.myapplication.Adapters;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
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

import ru.vladik.myapplication.Dialogs.MarkInfoDialog;
import ru.vladik.myapplication.DiaryAPI.DataClasses.FullMark;
import ru.vladik.myapplication.DiaryAPI.DataClasses.Mark;
import ru.vladik.myapplication.DiaryAPI.DataClasses.Subject;
import ru.vladik.myapplication.DiaryAPI.DataClasses.Mood;
import ru.vladik.myapplication.R;
import ru.vladik.myapplication.Utils.DrawableHelper;

public class MarksAdapter extends RecyclerView.Adapter<MarksAdapter.ViewHolder> {

    private final LayoutInflater mInflater;
    private List<FullMark> markList;

    @SuppressLint("NotifyDataSetChanged")
    public void refreshList(List<FullMark> markList) {
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
        Mark mark = markList.get(position).getMark();
        Subject subject = markList.get(position).getSubject();
        holder.markSubject.setText(subject.getName());
        holder.markText.setText(mark.getTextValue());
        RippleDrawable rippleDrawable = (RippleDrawable) AppCompatResources.getDrawable(context, R.drawable.selectable_rounded);
        if (rippleDrawable != null && rippleDrawable.getNumberOfLayers() == 0) {
            switch (markList.get(position).getMark().getMood()) {
                case "Good":
                    rippleDrawable.addLayer(shapeGood);
                    break;
                case "Average":
                    rippleDrawable.addLayer(shapeAverage);
                    break;
                case "Bad":
                    rippleDrawable.addLayer(shapeBad);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + markList.get(position).getMark().getMood());
            }
        } else if (rippleDrawable != null) {
            switch (markList.get(position).getMark().getMood()) {
                case Mood.GOOD:
                    rippleDrawable.setDrawable(0, shapeGood);
                    break;
                case Mood.AVERAGE:
                    rippleDrawable.setDrawable(0, shapeAverage);
                    break;
                case Mood.BAD:
                    rippleDrawable.setDrawable(0, shapeBad);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + markList.get(position).getMark().getMood());
            }
        }
        holder.markLayout.setBackground(rippleDrawable);
        holder.mainLayout.setOnClickListener((view) ->{
            Dialog dialog = new MarkInfoDialog(context, markList.get(position));
            dialog.show();
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
