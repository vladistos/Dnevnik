package ru.vladik.dnevnik.Utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.IdRes;
import androidx.annotation.Nullable;

import java.util.List;

import ru.vladik.dnevnik.R;

public class LayoutHelper {

    public static void setLoading(ViewGroup parent, boolean loading, @Nullable @IdRes List<Integer> excludeList) {
        if (loading) {
            for (int i = 0; i < parent.getChildCount(); i++) {
                View view = parent.getChildAt(i);
                if (excludeList == null || !excludeList.contains(view.getId())) {
                    view.setVisibility(View.GONE);
                }
            }
            LayoutInflater.from(parent.getContext())
                    .inflate(parent.getContext().getResources().getLayout(R.layout.loading_view),
                             parent, true);
        } else {
            ProgressBar contentLoadingProgressBar = parent.findViewById(R.id.loading_progress_bar);
            if (contentLoadingProgressBar != null) {
                parent.removeView(contentLoadingProgressBar);
            }
            for (int i = 0; i < parent.getChildCount(); i++) {
                View view = parent.getChildAt(i);
                if (excludeList == null || !excludeList.contains(view.getId())) {
                    view.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(ViewGroup parent, View view,  int position);
    }
}
