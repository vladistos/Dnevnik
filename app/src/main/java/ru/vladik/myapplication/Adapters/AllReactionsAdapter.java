package ru.vladik.myapplication.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ru.vladik.myapplication.DiaryAPI.DataClasses.FeedPost.Emoji;
import ru.vladik.myapplication.DiaryAPI.DataClasses.FeedPost.Reaction;
import ru.vladik.myapplication.DiaryAPI.DataClasses.FeedPost.Reactions;
import ru.vladik.myapplication.R;
import ru.vladik.myapplication.Utils.LayoutHelper;

public class AllReactionsAdapter extends RecyclerView.Adapter<AllReactionsAdapter.ViewHolder> {

    private final List<Emoji> reactions;
    private LayoutHelper.OnItemClickListener onItemClickListener;

    public AllReactionsAdapter() {
        reactions = Reactions.getEmotions();
    }

    public void setOnItemClickListener(LayoutHelper.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.reaction_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.emotionText.setText(reactions.get(position).getEmoji());
        holder.emotionText.setTextSize(15);
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(holder.parent, v,
                        holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return reactions.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView emotionText;
        public View view;
        public ViewGroup parent;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            parent = (ViewGroup) itemView.getParent();
            emotionText = itemView.findViewById(R.id.reaction_text_in_reaction_item);
        }
    }
}
