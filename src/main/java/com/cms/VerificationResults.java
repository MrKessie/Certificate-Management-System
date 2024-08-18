package com.cms;

public class VerificationResults {
    private int studentId;
    private boolean certificateFound;
    private String certificatePath;


    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public boolean isCertificateFound() {
        return certificateFound;
    }

    public void setCertificateFound(boolean certificateFound) {
        this.certificateFound = certificateFound;
    }

    public String getCertificatePath() {
        return certificatePath;
    }

    public void setCertificatePath(String certificatePath) {
        this.certificatePath = certificatePath;
    }
}
