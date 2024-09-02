package com.cms.Enum;

public enum Roles {

    ROLE_ADMIN("Admin"),
    ROLE_CLIENT("Client");

    private final String displayName;

    Roles(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
