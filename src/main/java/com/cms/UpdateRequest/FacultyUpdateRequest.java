package com.cms.UpdateRequest;

public class FacultyUpdateRequest {
//    @NotBlank(message = "Faculty name is required")
    private String facultyName;

    // Getters and Setters
    public String getFacultyName() {
        return facultyName;
    }

    public void setFacultyName(String facultyName) {
        this.facultyName = facultyName;
    }
}
