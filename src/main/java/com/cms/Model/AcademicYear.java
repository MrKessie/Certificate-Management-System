package com.cms.Model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
public class AcademicYear {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, unique = true)
    private String academicYear;

    @OneToMany(mappedBy = "academicYear")
    private Set<Certificate> certificates;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime dateAdded;

    @Column(nullable = false, updatable = false)
    @UpdateTimestamp
    private LocalDateTime dateEdited;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAcademicYear() {
        return academicYear;
    }

    public void setAcademicYear(String academicYear) {
        this.academicYear = academicYear;
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
