package com.cms.Model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
public class Programme {

    @Id
    @Column(unique = true)
    private int programmeId;

    @Column(nullable = false, unique = true)
    private String programmeName;

    @ManyToOne
    @JoinColumn(name = "faculty_id", foreignKey = @ForeignKey(name = "fk_programme_faculty",
            foreignKeyDefinition = "FOREIGN KEY (faculty_id) REFERENCES faculty(faculty_id) ON UPDATE CASCADE ON DELETE CASCADE"))
    private Faculty faculty;

    @ManyToOne
    @JoinColumn(name = "department_id", foreignKey = @ForeignKey(name = "fk_programme_department",
            foreignKeyDefinition = "FOREIGN KEY (department_id) REFERENCES department(department_id) ON UPDATE CASCADE ON DELETE CASCADE"))
    private Department department;

    @OneToMany(mappedBy = "programme")
    private Set<Certificate> certificates;

    @Column(nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private LocalDateTime dateAdded;

    @Column(nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private LocalDateTime dateEdited;


    public Programme() {
    }

    public Programme(int programmeId) {
        this.programmeId = programmeId;
    }

    public int getProgrammeId() {
        return programmeId;
    }

    public void setProgrammeId(int programmeId) {
        this.programmeId = programmeId;
    }

    public String getProgrammeName() {
        return programmeName;
    }

    public void setProgrammeName(String programmeName) {
        this.programmeName = programmeName;
    }

    public Faculty getFaculty() {
        return faculty;
    }

    public void setFaculty(Faculty faculty) {
        this.faculty = faculty;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
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
