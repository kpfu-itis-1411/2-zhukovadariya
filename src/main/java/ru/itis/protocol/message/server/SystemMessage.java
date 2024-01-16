package ru.itis.protocol.message.server;

import ru.itis.protocol.connection.ContentMessage;
import ru.itis.protocol.constants.MessageTypes;

public record SystemMessage(String content, int senderId) implements ContentMessage<String> {

    @Override
    public MessageTypes getType() {
        return MessageTypes.SYSTEM_MESSAGE;
    }
}
