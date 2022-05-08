package ru.vladik.myapplication.DiaryAPI.DataClasses.FeedPost;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class FeedPostList {
    private List<FeedPost> posts;
    private Permissions permissions;
    private Boolean commentsRestrictionIsAvailable;

    public FeedPostList(List<FeedPost> posts, Permissions permissions, Boolean commentsRestrictionIsAvailable) {
        this.posts = posts;
        this.permissions = permissions;
        this.commentsRestrictionIsAvailable = commentsRestrictionIsAvailable;
    }

    public FeedPostList() {
        this(new ArrayList<>(), new Permissions(), false);
    }
}
