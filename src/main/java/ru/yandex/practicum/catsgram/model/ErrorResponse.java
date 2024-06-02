package ru.yandex.practicum.catsgram.model;

import lombok.Data;

@Data
public class ErrorResponse {
    String error;
    String stacktrace;


    public ErrorResponse(String error, String stacktrace) {
        this.error = error;
        this.stacktrace = stacktrace;
    }
}
