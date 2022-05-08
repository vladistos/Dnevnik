package ru.vladik.myapplication.DiaryAPI.DataClasses.FeedPost;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class AttachmentsContainer {
    private String containerId, contentImage;
    private List<File> files;

    public boolean isEmpty() {
        return files.isEmpty();
    }

    public AttachmentsContainer(String containerId, String contentImage, List<File> files) {
        this.containerId = containerId;
        this.contentImage = contentImage;
        this.files = files;
    }

    public AttachmentsContainer() {
        this("", "", new ArrayList<>());
    }
}
