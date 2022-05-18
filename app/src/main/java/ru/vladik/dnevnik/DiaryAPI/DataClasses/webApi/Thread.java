package ru.vladik.dnevnik.DiaryAPI.DataClasses.webApi;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class Thread {
    private String eventKey, communityId, pollsId, poll, captcha;
    private User currentUser;
    private List<User> participants;
    private Boolean hasPrevious, availableShortViewStatsOnly, isNew, isReaded, isFavorite;
    private List<Comment> comments;

    public Thread(String eventKey, String communityId, String pollsId, String poll, String captcha,
                  User currentUser, List<User> participants, Boolean hasPrevious, Boolean availableShortViewStatsOnly,
                  Boolean isNew, Boolean isReaded, Boolean isFavorite, List<Comment> comments) {
        this.eventKey = eventKey;
        this.communityId = communityId;
        this.pollsId = pollsId;
        this.poll = poll;
        this.captcha = captcha;
        this.currentUser = currentUser;
        this.participants = participants;
        this.hasPrevious = hasPrevious;
        this.availableShortViewStatsOnly = availableShortViewStatsOnly;
        this.isNew = isNew;
        this.isReaded = isReaded;
        this.isFavorite = isFavorite;
        this.comments = comments;
    }

    public Thread() {
        this("", "", "", "", "", new User(), new ArrayList<>(),
                false, false, false, false,
                false, new ArrayList<>());
    }
}
