package ru.vladik.myapplication.Adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import ru.vladik.myapplication.DiaryAPI.DataClasses.FeedPost.Reaction;
import ru.vladik.myapplication.R;
import ru.vladik.myapplication.Utils.LayoutHelper;

public class ReactionsAdapter extends ArrayAdapter<Reaction> {

    private final int recourse;
    private final Drawable selectedItemBackground;
    private LayoutHelper.OnItemClickListener onClickListener;
    private int selectedPosition;

    public ReactionsAdapter(@NonNull Context context, int resource, @NonNull List<Reaction> objects,
                            Drawable selectedItemBackground) {
        super(context, resource, objects);
        recourse = resource;
        this.selectedItemBackground = selectedItemBackground;
    }

    public void setOnItemClickListener(LayoutHelper.OnItemClickListener listener) {
        onClickListener = listener;
    }

    @Override
    public int getCount() {
        return super.getCount() + 1;
    }

    public void selectPosition(int position) {
        selectedPosition = position;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(recourse, null);
        }
        ViewHolder holder = new ViewHolder(view);
        if (position < getCount()-1) {
            Reaction reaction = getItem(position);

            String reactionTextString = reaction.getEmoji().getEmoji() + " " + reaction.getVotes();
            holder.reactionTextView.setText(reactionTextString);
        } else {
            holder.reactionTextView.setText("  ...");
        }
        if (position == selectedPosition) {
            view.setBackground(selectedItemBackground);
        } else {
            view.setBackground(null);
        }
        if (onClickListener != null) {
            View finalView = view;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.onItemClick(parent, finalView, position);
                }
            });
        }

        return view;
    }

    public static class ViewHolder{
        public TextView reactionTextView;
        public ViewHolder(View itemView) {
            reactionTextView = itemView.findViewById(R.id.reaction_text_in_reaction_item);
        }
    }
}
