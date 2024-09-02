package com.cms.Enum;

public enum MessageStatus {
    SENT("Sent"),
    DELIVERED("Delivered"),
    READ("Read");

    private final String displayName;

    MessageStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
