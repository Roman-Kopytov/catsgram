package ru.yandex.practicum.catsgram.exception;

import java.io.IOException;

public class ImageFileException extends RuntimeException {
    private IOException e;

    public ImageFileException(String s) {
        super(s);
    }

    public ImageFileException(String s, IOException e) {
        super(s);
        this.e = e;
    }
}
