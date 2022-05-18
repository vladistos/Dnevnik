package ru.vladik.dnevnik.Adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ru.vladik.dnevnik.DataClasses.RatingData;
import ru.vladik.dnevnik.R;

public class RatingAdapter extends RecyclerView.Adapter<RatingAdapter.ViewHolder> {

    private RatingData ratingData;

    public RatingAdapter(@NonNull RatingData ratingData) {
        ratingData.init();
        this.ratingData = ratingData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rating_element, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RatingData.RatingEntry entry = ratingData.get(position);
        String name = entry.name;
        Double val = entry.value;
        holder.nameTextView.setText(name);
        holder.placeTextView.setText(String.valueOf(entry.place));
        holder.valueTextView.setText(String.valueOf(val));
    }

    @Override
    public int getItemCount() {
        return ratingData.getSize();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView, placeTextView, valueTextView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.rating_element_name);
            valueTextView = itemView.findViewById(R.id.rating_element_value);
            placeTextView = itemView.findViewById(R.id.rating_element_place);
        }
    }
}
