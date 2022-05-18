package ru.vladik.dnevnik.Adapters;

import android.graphics.drawable.AnimatedImageDrawable;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ru.vladik.dnevnik.DiaryAPI.DataClasses.webApi.Emoji;
import ru.vladik.dnevnik.DiaryAPI.DataClasses.Reactions;
import ru.vladik.dnevnik.R;
import ru.vladik.dnevnik.Utils.LayoutHelper;

public class AllReactionsAdapter extends RecyclerView.Adapter<AllReactionsAdapter.ViewHolder> {

    private final List<Emoji> reactions;
    private Drawable[] emojiDrawables;
    private LayoutHelper.OnItemClickListener onItemClickListener;

    public AllReactionsAdapter(Drawable[] emojiDrawables) {
        this.emojiDrawables = emojiDrawables;
        reactions = Reactions.getEmotionsWithoutEmpty();
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
        Drawable drawable = emojiDrawables[position+1];
        holder.emotionImage.setImageDrawable(drawable);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            if (drawable instanceof AnimatedImageDrawable && !((AnimatedImageDrawable) drawable).isRunning()) {
                ((AnimatedImageDrawable) drawable).start();
            }
        }
        holder.view.setOnClickListener(v -> onItemClickListener.onItemClick(holder.parent, v,
                Reactions.getEmotionId(reactions.get(position).getName())));
    }

    @Override
    public int getItemCount() {
        return reactions.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView emotionImage;
        public View view;
        public ViewGroup parent;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            parent = (ViewGroup) itemView.getParent();
            emotionImage = itemView.findViewById(R.id.reaction_image_in_reaction_item);
        }
    }
}
