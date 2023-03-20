package ru.agile.scrum.mst.market.auth.exceptions;

public class TheUserAlreadyExistsException extends RuntimeException {
    public TheUserAlreadyExistsException(String message) {
        super(message);
    }
}
