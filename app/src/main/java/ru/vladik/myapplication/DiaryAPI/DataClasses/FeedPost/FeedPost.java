package ru.vladik.myapplication.DiaryAPI.DataClasses.FeedPost;

import android.graphics.drawable.Drawable;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class FeedPost {
    private Long id;
    private String text, eventKey, topicName, topicUrl, topicType, postUrl, topicLogoUrl,
            attachmentsContainerId, viewsCount, commentsCount,
            createdDate, createdDateUtc;
    private Boolean isViewed, isRendered;
    private Author author;
    private Permissions permissions;
    private DiscussionsSettings discussionsSettings;
    private Thread thread;
    private AttachmentsContainer attachmentsContainer;
    private Likes likes;
    private Drawable logoDrawable;

    public FeedPost(Long id, String text, String eventKey, String topicName, String topicUrl,
                    String topicType, String postUrl, String topicLogoUrl,
                    String attachmentsContainerId, String viewsCount, String commentsCount,
                    String createdDate, String createdDateUtc, Boolean isViewed, Boolean isRendered,
                    Author author, Permissions permissions, DiscussionsSettings discussionsSettings,
                    Thread thread, AttachmentsContainer attachmentsContainer, Likes likes) {
        this.id = id;
        this.text = text;
        this.eventKey = eventKey;
        this.topicName = topicName;
        this.topicUrl = topicUrl;
        this.topicType = topicType;
        this.postUrl = postUrl;
        this.topicLogoUrl = topicLogoUrl;
        this.attachmentsContainerId = attachmentsContainerId;
        this.viewsCount = viewsCount;
        this.commentsCount = commentsCount;
        this.createdDate = createdDate;
        this.createdDateUtc = createdDateUtc;
        this.isViewed = isViewed;
        this.isRendered = isRendered;
        this.author = author;
        this.permissions = permissions;
        this.discussionsSettings = discussionsSettings;
        this.thread = thread;
        this.attachmentsContainer = attachmentsContainer;
        this.likes = likes;
    }

    public FeedPost() {
        this(-1L, "", "", "", "", "", "",
                "", "", "", "",
                "", "", false, false, new Author(),
                new Permissions(), new DiscussionsSettings(), new Thread(),
                new AttachmentsContainer(), new Likes());
    }
}
