package com.cms.Model;

import jakarta.persistence.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
public class CertificateVerify {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int verificationId;

    @ManyToOne
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_user_verify",
            foreignKeyDefinition = "FOREIGN KEY (user_id) REFERENCES user(user_id) ON UPDATE CASCADE ON DELETE CASCADE"))
    private User user;

    @ManyToOne
    @JoinColumn(name = "student_id", foreignKey = @ForeignKey(name = "fk_student_verify",
            foreignKeyDefinition = "FOREIGN KEY (student_id) REFERENCES student(student_id) ON UPDATE CASCADE ON DELETE CASCADE"))
    private Student student;

//    @Column(nullable = false)
//    private String employer;
//    private String organization;

    @Column(nullable = false)
    private boolean verified = true;

    @Column(nullable = false, updatable = false)
    @UpdateTimestamp
    private LocalDateTime dateVerified;


    public int getVerificationId() {
        return verificationId;
    }

    public void setVerificationId(int verificationId) {
        this.verificationId = verificationId;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

//    public String getEmployer() {
//        return employer;
//    }
//
//    public void setEmployer(String employer) {
//        this.employer = employer;
//    }
//
//    public String getOrganization() {
//        return organization;
//    }
//
//    public void setOrganization(String organization) {
//        this.organization = organization;
//    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public LocalDateTime getDateVerified() {
        return dateVerified;
    }

    public void setDateVerified(LocalDateTime dateVerified) {
        this.dateVerified = dateVerified;
    }
}
