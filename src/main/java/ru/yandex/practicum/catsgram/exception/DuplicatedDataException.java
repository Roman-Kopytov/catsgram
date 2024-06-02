package ru.yandex.practicum.catsgram.exception;

public class DuplicatedDataException extends RuntimeException {

    public DuplicatedDataException(String massage) {
        super(massage);
    }
}
