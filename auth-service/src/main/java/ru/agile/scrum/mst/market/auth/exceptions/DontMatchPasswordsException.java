package ru.agile.scrum.mst.market.auth.exceptions;

public class DontMatchPasswordsException extends RuntimeException {
    public DontMatchPasswordsException(String message) {
        super(message);
    }
}
