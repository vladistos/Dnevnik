package ru.vladik.myapplication.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PaintDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ru.vladik.myapplication.DiaryAPI.DataClasses.Mark;
import ru.vladik.myapplication.R;
import ru.vladik.myapplication.Utils.DrawableHelper;

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
            String text = mark.getTextValue() + " - " + markList.size();
            holder.markText.setText(text);
            holder.markImage.setImageDrawable(new ColorDrawable(DrawableHelper.getColorByMood(mark.getMood(), Color.WHITE)));
        }
        return holder.itemView;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView markText;
        private final ImageView markImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            markText = itemView.findViewById(R.id.mark_text_in_mark_item);
            markImage = itemView.findViewById(R.id.mark_color_in_mark_item);
        }
    }
}
