package com.certificatemanagementsystem.Enums;

public enum Role {

//    ADMIN,
//    CLIENT

    ROLE_ADMIN("Admin"),
    ROLE_CLIENT("Client");

    private final String displayName;

    Role(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
