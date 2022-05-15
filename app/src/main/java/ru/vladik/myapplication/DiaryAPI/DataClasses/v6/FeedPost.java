package ru.vladik.myapplication.DiaryAPI.DataClasses.v6;

import android.graphics.drawable.Drawable;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
public class FeedPost extends FeedElement {

    private String timeStamp;
    private FeedPostContent content;
    private Drawable logoDrawable;
}
