package ru.vladik.dnevnik.Adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;

import java.util.List;

import ru.vladik.dnevnik.DiaryAPI.DataClasses.webApi.Reaction;
import ru.vladik.dnevnik.R;
import ru.vladik.dnevnik.Utils.LayoutHelper;

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

            String reactionTextString = reaction.getEmoji().getEmoji();
        } else {
            holder.reactionImage.setImageDrawable(AppCompatResources.getDrawable(getContext(), R.drawable.ic_baseline_add_reaction_24));
        }
        if (position == selectedPosition) {
            holder.reactionImage.setBackground(selectedItemBackground);
        } else if (position != getCount() - 1){
            holder.reactionImage.setBackground(null);
        }
        if (onClickListener != null) {
            View finalView = view;
            view.setOnClickListener(v -> onClickListener.onItemClick(parent, finalView, position));
        }

        return view;
    }

    public static class ViewHolder{
        public ImageView reactionImage;
        public ViewHolder(View itemView) {
            reactionImage = itemView.findViewById(R.id.reaction_image_in_reaction_item);
        }
    }
}
