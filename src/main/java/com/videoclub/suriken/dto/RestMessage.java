package com.videoclub.suriken.dto;

import java.util.List;

public class RestMessage {
    private MessageType messageType;
    private String message;
    private List<String> messages;

    public RestMessage(MessageType messageType, List<String> messages) {
        this.messageType = messageType;
        this.messages = messages;
    }

    public RestMessage(MessageType messageType, String message) {
        this.messageType = messageType;
        this.message = message;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public String getMessage() {
        return message;
    }

    public List<String> getMessages() {
        return messages;
    }

    public enum MessageType {
        MOVIE_NOT_FOUND,
        NO_MOVIE_IN_STOCK,
        RESOURCE_NOT_FOUND,
    }
}
