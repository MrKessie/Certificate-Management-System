package com.cms.Model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
public class Department {

    @Id
    @Column(unique = true)
    private int departmentId;

    @Column(unique = true)
    private String departmentName;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "faculty_id", foreignKey = @ForeignKey(name = "fk_department_faculty",
            foreignKeyDefinition = "FOREIGN KEY (faculty_id) REFERENCES faculty(faculty_id) ON UPDATE CASCADE ON DELETE CASCADE"))
    Faculty faculty;
    @OneToMany(mappedBy = "department")
    private Set<Certificate> certificates;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime dateAdded;

    @Column(nullable = false)
    @UpdateTimestamp
    private LocalDateTime dateEdited;


    public Department() {
    }

    public Department(int departmentId) {
        this.departmentId = departmentId;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public Faculty getFaculty() {
        return faculty;
    }

    public void setFaculty(Faculty facultyId) {
        this.faculty = facultyId;
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
