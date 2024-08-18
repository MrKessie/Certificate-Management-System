package com.cms.Model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
public class Certificate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Id;

    @ManyToOne
    @JoinColumn(name = "student_id", foreignKey = @ForeignKey(name = "fk_student_certificate",
            foreignKeyDefinition = "FOREIGN KEY (student_id) REFERENCES student(student_id) ON UPDATE CASCADE ON DELETE CASCADE"))
    private Student studentId;

    private String studentName;

    @ManyToOne
    @JoinColumn(name = "academic_year_id", foreignKey = @ForeignKey(name = "fk_certificate_academic_year",
            foreignKeyDefinition = "FOREIGN KEY (academic_year_id) REFERENCES academic_year(id) ON UPDATE CASCADE ON DELETE CASCADE"))
    private AcademicYear academicYear;

    @ManyToOne
    @JoinColumn(name = "programme_id", foreignKey = @ForeignKey(name = "fk_certificate_programme",
            foreignKeyDefinition = "FOREIGN KEY (programme_id) REFERENCES programme(programme_id) ON UPDATE CASCADE ON DELETE CASCADE"))
    private Programme programme;

    @ManyToOne
    @JoinColumn(name = "department_id", foreignKey = @ForeignKey(name = "fk_certificate_department",
            foreignKeyDefinition = "FOREIGN KEY (department_id) REFERENCES department(department_id) ON UPDATE CASCADE ON DELETE CASCADE"))
    private Department department;

    @Column(name = "Class")
    private String graduateClass;


    private String certificatePath;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime dateAdded;

    @Column(nullable = false, updatable = false)
    @UpdateTimestamp
    private LocalDateTime dateEdited;

    public Certificate() {
    }

    public Certificate(Student studentId, String studentName, AcademicYear academicYear, Programme programme, Department department,
                       String graduateClass, String certificatePath) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.academicYear = academicYear;
        this.programme = programme;
        this.department = department;
        this.graduateClass = graduateClass;
        this.certificatePath = certificatePath;
        this.dateAdded = LocalDateTime.now();
        this.dateEdited = LocalDateTime.now();
    }

//    public Certificate(Student student, String studentName, AcademicYear academicYear, Programme programme, Department department, String graduateClass, String certificatePath) {
//    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public Student getStudentId() {
        return studentId;
    }

    public void setStudentId(Student student) {
        this.studentId = student;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public AcademicYear getAcademicYear() {
        return academicYear;
    }

    public void setAcademicYear(AcademicYear academicYear) {
        this.academicYear = academicYear;
    }

    public Programme getProgramme() {
        return programme;
    }

    public void setProgramme(Programme programme) {
        this.programme = programme;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public String getGraduateClass() {
        return graduateClass;
    }

    public void setGraduateClass(String graduateClass) {
        this.graduateClass = graduateClass;
    }

    public String getCertificatePath() {
        return certificatePath;
    }

    public void setCertificatePath(String certificatePath) {
        this.certificatePath = certificatePath;
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


