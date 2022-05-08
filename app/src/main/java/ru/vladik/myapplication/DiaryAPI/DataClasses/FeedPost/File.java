package ru.vladik.myapplication.DiaryAPI.DataClasses.FeedPost;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class File {
    private String fileId, url, downloadUrl, name, extension, contentImage;

    public File(String fileId, String url, String downloadUrl, String name, String extension,
                String contentImage) {
        this.fileId = fileId;
        this.url = url;
        this.downloadUrl = downloadUrl;
        this.name = name;
        this.extension = extension;
        this.contentImage = contentImage;
    }

    public File() {
        this("", "", "", "", "", "");
    }
}
