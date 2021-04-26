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

        // Duletovi
        ARGUMENT_NOT_VALID,
        CONSTRAINT_VIOLATION,
        CONSTRAINT_VIOLATION_PARAMS,
        DATA_INTEGRITY_VIOLATION,
        MESSAGE_NOT_READABLE,
        MISSING_SERVLET_REQUEST_PARAM,
        REQUEST_REJECTED,

        FORBIDDEN,
        RESOURCE_ALREADY_EXISTS,
        RESOURCE_NOT_FOUND,
        ENTITY_NOT_FOUND,
        CONFLICT_OCCURRED,

        INTERNAL_SERVER_ERROR;
    }
}
