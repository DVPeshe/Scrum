package ru.agile.scrum.mst.market.auth.exceptions;

public class BanUserException extends RuntimeException {
    public BanUserException(String message) {
        super(message);
    }
}
