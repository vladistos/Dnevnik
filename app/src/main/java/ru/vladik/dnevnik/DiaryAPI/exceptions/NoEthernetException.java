package ru.vladik.dnevnik.DiaryAPI.exceptions;

public class NoEthernetException extends RuntimeException {

    public NoEthernetException() {
        super("Проблемы с соединением");
    }

    public NoEthernetException(String message) {
        super(message);
    }

    public NoEthernetException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoEthernetException(Throwable cause) {
        super(cause);
    }

    public NoEthernetException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
