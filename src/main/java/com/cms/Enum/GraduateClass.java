package com.cms.Enum;

public enum GraduateClass {
    FIRST_CLASS("First Class"),
    SECOND_CLASS_UPPER("Second Class Upper"),
    SECOND_CLASS_LOWER("Second Class Lower"),
    THIRD_CLASS("Third Class"),
    PASS("Pass");

    private final String displayName;

    GraduateClass(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static GraduateClass fromDisplayName(String displayName) {
        for (GraduateClass graduateClass : values()) {
            if (graduateClass.getDisplayName().equalsIgnoreCase(displayName)) {
                return graduateClass;
            }
        }
        throw new IllegalArgumentException("No enum constant for display name " + displayName);
    }
}
