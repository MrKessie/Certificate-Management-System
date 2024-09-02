package com.cms.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Student {

    @Id
    @Column(unique = true)
    private int studentId;
    @Column(nullable = false)
    private String studentName;


    @ManyToOne
    @JoinColumn(name = "faculty_id", foreignKey = @ForeignKey(name = "fk_student_faculty",
            foreignKeyDefinition = "FOREIGN KEY (faculty_id) REFERENCES faculty(faculty_id) ON UPDATE CASCADE ON DELETE CASCADE"))
    private Faculty faculty;

    @ManyToOne
    @JoinColumn(name = "department_id", foreignKey = @ForeignKey(name = "fk_student_department",
            foreignKeyDefinition = "FOREIGN KEY (department_id) REFERENCES department(department_id) ON UPDATE CASCADE ON DELETE CASCADE"))
    private Department department;

    @ManyToOne
    @JoinColumn(name = "programme_id", foreignKey = @ForeignKey(name = "fk_student_programme",
            foreignKeyDefinition = "FOREIGN KEY (programme_id) REFERENCES programme(programme_id) ON UPDATE CASCADE ON DELETE CASCADE"))
    private  Programme programme;

    @ManyToOne
    @JoinColumn(name = "academic_year_id", foreignKey = @ForeignKey(name = "fk_student_academic_year",
            foreignKeyDefinition = "FOREIGN KEY (academic_year_id) REFERENCES academic_year(id) ON UPDATE CASCADE ON DELETE CASCADE"))
    private AcademicYear academicYear;

    @OneToMany(mappedBy = "studentId", cascade = CascadeType.ALL, orphanRemoval = true,  fetch = FetchType.EAGER)
    @JsonIgnore
    private List<Certificate> certificates;

    @Column(nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    LocalDateTime dateAdded;

    @Column(nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    LocalDateTime dateEdited;

    public Student(String studentName, String programmeName, String departmentName, String academicYear) {
    }

    public Student(int studentId) {
        this.studentId = studentId;
    }

    public Student() {
    }

    // getters and setters

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
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

    public Programme getProgramme() {
        return programme;
    }

    public void setProgramme(Programme programme) {
        this.programme = programme;
    }

    public AcademicYear getAcademicYear() {
        return academicYear;
    }

    public void setAcademicYear(AcademicYear academicYear) {
        this.academicYear = academicYear;
    }

    public List<Certificate> getCertificates() {
        return certificates;
    }

    public void setCertificates(List<Certificate> certificates) {
        this.certificates = certificates;
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
