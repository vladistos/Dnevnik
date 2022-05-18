package ru.vladik.dnevnik.Views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.GridView;

import ru.vladik.dnevnik.R;

public class SelectableGridView extends GridView {
    private Drawable selectedItemBackground;


    public SelectableGridView(Context context) {
        this(context, null);
    }

    public SelectableGridView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SelectableGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    @Override
    public void setSelection(int position) {
        super.setSelection(position);
    }

    public Drawable getSelectedItemBackground() {
        return selectedItemBackground;
    }

    public void setSelectedItemBackground(Drawable selectedItemBackground) {
        this.selectedItemBackground = selectedItemBackground;
    }

    public SelectableGridView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SelectableGridView, defStyleRes, 0);
        selectedItemBackground = a.getDrawable(R.styleable.SelectableGridView_selected_item_background);
        a.recycle();
    }

}
