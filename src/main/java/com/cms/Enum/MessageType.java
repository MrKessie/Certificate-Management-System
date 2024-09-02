package com.cms.Enum;

public enum MessageType {
    TEXT("Text"),
    IMAGE("Image"),
    FILE("File");

    private final String displayName;

    MessageType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
