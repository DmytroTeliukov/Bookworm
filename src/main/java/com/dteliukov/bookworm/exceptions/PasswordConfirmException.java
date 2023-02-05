package com.dteliukov.bookworm.exceptions;

public class PasswordConfirmException extends RuntimeException {

    private final String message;

    public PasswordConfirmException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
