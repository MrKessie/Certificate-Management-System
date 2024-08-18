package com.cms.Enum;

public enum Genders {
    MALE("MALE"),
    FEMALE("FEMALE");

    private final String displayName;

    Genders(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static Genders fromDisplayName(String displayName) {
        for (Genders gender : values()) {
            if (gender.getDisplayName().equalsIgnoreCase(displayName)) {
                return gender;
            }
        }
        throw new IllegalArgumentException("No enum constant for display name " + displayName);
    }
}
