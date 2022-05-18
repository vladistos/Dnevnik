package ru.vladik.dnevnik.DiaryAPI.DataClasses.v6;

import android.graphics.drawable.Drawable;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class FeedPost extends FeedElement {

    private String timeStamp;
    private FeedPostContent content;
    private Drawable logoDrawable;
}
