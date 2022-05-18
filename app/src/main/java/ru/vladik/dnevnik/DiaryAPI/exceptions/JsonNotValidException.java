package ru.vladik.dnevnik.DiaryAPI.exceptions;

public class JsonNotValidException extends RuntimeException {
    public JsonNotValidException() {
    }

    public JsonNotValidException(String message) {
        super(message);
    }

    public JsonNotValidException(String message, Throwable cause) {
        super(message, cause);
    }

    public JsonNotValidException(Throwable cause) {
        super(cause);
    }

    public JsonNotValidException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
