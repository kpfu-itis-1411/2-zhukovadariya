package ru.itis.protocol.connection;

import ru.itis.protocol.constants.MessageTypes;

import java.io.Serializable;

public interface BasicMessage extends Serializable {
    MessageTypes getType();

    int senderId();
}
