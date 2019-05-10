package ru.rmamedov.app.model;

public enum Status {

    PLANNED(1),
    IN_PROGRESS(2),
    DONE(3),
    PAUSED(4);

    private int code;

    Status(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
