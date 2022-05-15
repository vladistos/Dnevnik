package ru.vladik.myapplication.DiaryAPI.DataClasses.v6;

import java.util.List;

import lombok.Data;

@Data
public class FeedPostContent {
    private Long id, publicClubId;
    private String eventKey, topicEventKey, topicLogoUrl, eventUrl, eventSign, title, subtitle, text,
            authorImageUrl, authorFirstName, authorMiddleName, authorLastName, authorName, type,previewUrl,
            createdDateTime, messengerEntryPoint;

    private List<Object> files, attachmentFiles;
    private Boolean isReaded, isNew, unsubscribeIsPossible;
    private Integer commentsCount;
    private Likes likes;
}
