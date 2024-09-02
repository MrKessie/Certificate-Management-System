package com.cms.Enum;

public enum ConversationStatus {
    ACTIVE("Active"),
    CLOSED("Closed");

    private final String displayName;

    ConversationStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
