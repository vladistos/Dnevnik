package ru.vladik.myapplication.DiaryAPI.DataClasses.webApi;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class Comment {
    private Long id, authorId, replyUserId, createdDateTime;
    private String text;
    private Boolean hasPrevious, isDeleted, isEdited;
    private List<Comment> comments;
    private Likes likes;

    public Comment(Long id, Long authorId, Long replyUserId, Long createdDateTime, String text,
                   Boolean hasPrevious, Boolean isDeleted, Boolean isEdited,
                   List<Comment> comments, Likes likes) {
        this.id = id;
        this.authorId = authorId;
        this.replyUserId = replyUserId;
        this.createdDateTime = createdDateTime;
        this.text = text;
        this.hasPrevious = hasPrevious;
        this.isDeleted = isDeleted;
        this.isEdited = isEdited;
        this.comments = comments;
        this.likes = likes;
    }

    public Comment() {
        this(-1L, -1L, -1L, -1L, "", false,
                false, false, new ArrayList<>(), new Likes());
    }
}
