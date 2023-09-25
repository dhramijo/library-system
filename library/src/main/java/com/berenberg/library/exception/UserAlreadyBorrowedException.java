package com.berenberg.library.exception;

public class UserAlreadyBorrowedException extends LibraryException {
    public UserAlreadyBorrowedException(String message) {
        super(message);
    }
}
