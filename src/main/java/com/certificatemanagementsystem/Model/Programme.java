package com.certificatemanagementsystem.Model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

@Entity
public class Programme {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Id;

    @Column(nullable = false, unique = true)
    private int programmeId;

    @Column(nullable = false)
    private String programmeName;

    @ManyToOne
//    @JoinColumn(name = "faculty_Id", nullable = false)
    //@OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "faculty_id", foreignKey = @ForeignKey(name = "fk_programme_faculty",
            foreignKeyDefinition = "FOREIGN KEY (faculty_id) REFERENCES faculty(id) ON UPDATE CASCADE ON DELETE CASCADE"))
    private Faculty faculty;

    @ManyToOne
//    @JoinColumn(name = "department_Id", nullable = false)
    //@OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "department_id", foreignKey = @ForeignKey(name = "fk_programme_department",
            foreignKeyDefinition = "FOREIGN KEY (department_id) REFERENCES department(id) ON UPDATE CASCADE ON DELETE CASCADE"))
    private Department department;

    @Column(nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private LocalDateTime dateAdded;

    @Column(nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private LocalDateTime dateEdited;


    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
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
