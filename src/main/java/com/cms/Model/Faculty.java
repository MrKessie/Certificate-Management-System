package com.cms.Model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
public class Faculty {

    @Id
    @Column(unique = true)
    private int facultyId;

    @Column(unique = true)
    private String facultyName;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime dateAdded;

    @Column(nullable = false, updatable = true)
    @UpdateTimestamp
    private LocalDateTime dateEdited;

    public Faculty() {
    }

    public Faculty(int facultyId) {
        this.facultyId = facultyId;
    }

    public int getFacultyId() {
        return facultyId;
    }

    public void setFacultyId(int facultyId) {
        this.facultyId = facultyId;
    }

    public String getFacultyName() {
        return facultyName;
    }

    public void setFacultyName(String facultyName) {
        this.facultyName = facultyName;
    }

    public LocalDateTime getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(LocalDateTime dateAdded) {
        this.dateAdded = dateAdded;
    }

    public LocalDateTime getDateEdited() {
        return dateEdited;
    }

    public void setDateEdited(LocalDateTime dateEdited) {
        this.dateEdited = dateEdited;
    }
}
