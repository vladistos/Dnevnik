package ru.vladik.dnevnik.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ru.vladik.dnevnik.DiaryAPI.DataClasses.v2.Mark;
import ru.vladik.dnevnik.R;
import ru.vladik.dnevnik.Utils.DrawableHelper;

public class LegendAdapter extends ArrayAdapter<List<Mark>> {
    private final int recourse;

    public LegendAdapter(@NonNull Context context, int resource) {
        super(context, resource);
        this.recourse = resource;
    }

    public void refreshList(List<List<Mark>> list) {
        clear();
        addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder = new ViewHolder(
                convertView == null ?LayoutInflater.from(getContext()).inflate(
                        recourse, parent, false
                ) : convertView
        );
        List<Mark> markList = getItem(position);
        if (markList.size() > 0) {
            Mark mark = markList.get(0);
            String text = String.valueOf(markList.size());
            holder.markText.setText(text);
            GradientDrawable Rdrawable = (GradientDrawable) AppCompatResources
                    .getDrawable(getContext(), R.drawable.solid_rounded);

            if (Rdrawable != null) {
                GradientDrawable drawable = (GradientDrawable) Rdrawable.getConstantState().newDrawable();
                drawable = (GradientDrawable) drawable.getConstantState().newDrawable();
                drawable.setColor(DrawableHelper.getColorByMood(getContext(), mark.getMood(), Color.WHITE));
                holder.markValue.setBackground(drawable);
                holder.markValue.setText(mark.getTextValue());
            }
        }
        return holder.itemView;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView markText;
        private final TextView markValue;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            markText = itemView.findViewById(R.id.mark_description_in_mark_describe_item);
            markValue = itemView.findViewById(R.id.mark_in_mark_describe_item);
        }
    }
}
