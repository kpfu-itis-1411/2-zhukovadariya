package ru.itis.protocol.connection;

public interface ContentMessage<T> extends BasicMessage {
    T content();
}
