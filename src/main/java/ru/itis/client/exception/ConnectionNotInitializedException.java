package ru.itis.client.exception;

public class ConnectionNotInitializedException extends Exception {

    public ConnectionNotInitializedException(String message) {
        super(message);
    }

    public ConnectionNotInitializedException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConnectionNotInitializedException(Throwable cause) {
        super(cause);
    }
}
