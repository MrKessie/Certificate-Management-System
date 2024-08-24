package com.cms.Model;

import jakarta.persistence.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
public class CertificateIssue {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int issueId;

    private String collectorName;

    @ManyToOne
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_user_issue",
            foreignKeyDefinition = "FOREIGN KEY (user_id) REFERENCES user(user_id) ON UPDATE CASCADE ON DELETE CASCADE"))
    private User userId;

    @ManyToOne
    @JoinColumn(name = "student_id", foreignKey = @ForeignKey(name = "fk_student_issue",
            foreignKeyDefinition = "FOREIGN KEY (student_id) REFERENCES student(student_id) ON UPDATE CASCADE ON DELETE CASCADE"))
    private Student studentId;


    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] signature;

    @Column(nullable = false, updatable = false)
    @UpdateTimestamp
    private LocalDateTime dateIssued;

    @Column(nullable = false, updatable = false)
    @UpdateTimestamp
    private LocalDateTime dateEdited;


    public int getIssueId() {
        return issueId;
    }

    public void setIssueId(int issueId) {
        this.issueId = issueId;
    }

    public Student getStudentId() {
        return studentId;
    }

    public void setStudentId(Student studentId) {
        this.studentId = studentId;
    }

    public String getCollectorName() {
        return collectorName;
    }

    public void setCollectorName(String collectorName) {
        this.collectorName = collectorName;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }

    public byte[] getSignature() {
        return signature;
    }

    public void setSignature(byte[] signature) {
        this.signature = signature;
    }

    //    public String getIssueStatus() {
//        return issueStatus;
//    }
//
//    public void setIssueStatus(String issueStatus) {
//        this.issueStatus = issueStatus;
//    }

    public LocalDateTime getDateIssued() {
        return dateIssued;
    }

    public void setDateIssued(LocalDateTime dateIssued) {
        this.dateIssued = dateIssued;
    }

    public LocalDateTime getDateEdited() {
        return dateEdited;
    }

    public void setDateEdited(LocalDateTime dateEdited) {
        this.dateEdited = dateEdited;
    }
}
