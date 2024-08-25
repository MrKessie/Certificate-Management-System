package com.cms.Service;

import com.cms.Model.*;
import com.cms.Repository.CertificateRepository;
import com.cms.Repository.CertificateVerifyRepository;
import com.cms.Repository.StudentRepository;
import com.cms.Repository.UserRepository;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class CertificateVerifyService {

    @Autowired
    CertificateVerifyRepository certificateVerifyRepository;

    @Autowired
    CertificateRepository certificateRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    StudentRepository studentRepository;

    public CertificateVerify saveVerificationDetails(Student student, User userId, String employer, String organization, byte[] signature) {
        if (!studentRepository.existsByStudentId(student.getStudentId())) {
            return null;
        }

            CertificateVerify verification = new CertificateVerify();

            verification.setStudent(student);
            verification.setUserId(userId);
            verification.setEmployer(employer);
            verification.setOrganization(organization);
            verification.setSignature(signature);
            verification.setDateVerified(LocalDateTime.now());
            verification.setDateEdited(LocalDateTime.now());

            certificateVerifyRepository.save(verification);

            return verification;
    }


    public List<CertificateVerify> verifyList() {
        return certificateVerifyRepository.findAll();
    }


    public boolean existsByVerificationId(int verificationId) {
        return certificateVerifyRepository.existsById(verificationId);
    }


    public boolean deleteVerification(int verificationId) {
        if (certificateVerifyRepository.existsById(verificationId)) {
            certificateVerifyRepository.deleteById(verificationId);
            return true;
        }
        return false;
    }

    public Optional<CertificateVerify> findByStudentId(Student studentId) {
        return certificateVerifyRepository.findByStudent(studentId);
    }


    public List<Map<String, Object>> verifyBulkCertificates(MultipartFile file) throws IOException {
        List<Map<String, Object>> results = new ArrayList<>();

        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
//                Cell cell = row.getCell(0);
                int studentId = (int) row.getCell(0).getNumericCellValue();
                int userId = (int) row.getCell(1).getNumericCellValue();
                String employerName = row.getCell(2).getStringCellValue();
                String organizationName = row.getCell(3).getStringCellValue();

                // Find the Student entity first
                Optional<Student> student = studentRepository.findById(studentId);
                Map<String, Object> response = new HashMap<>();

                if (student.isPresent()) {
                    // Now use the Student entity to find the certificate
                    Optional<Certificate> certificate = certificateRepository.findByStudentId(student.get());

                    if (certificate.isPresent()) {
                        response.put("exists", true);
                        response.put("studentId", studentId);
                        response.put("name", certificate.get().getStudentName());
                        response.put("programme", certificate.get().getProgramme().getProgrammeName());
                        response.put("department", certificate.get().getDepartment().getDepartmentName());
                        response.put("academicYear", certificate.get().getAcademicYear().getAcademicYear());
                        response.put("classHonours", certificate.get().getGraduateClass());

                        String relativePath = certificate.get().getCertificatePath();
                        String baseUrl = "http://localhost/"; // Adjust as necessary
                        String fullUrl = baseUrl + relativePath;
                        response.put("viewLink", fullUrl);


                        CertificateVerify certificateVerify = new CertificateVerify();
                        Optional<User> userOptional = Optional.ofNullable(userRepository.findById(userId));
                        if (userOptional.isPresent()) {
                            User user = userOptional.get();
                            certificateVerify.setUserId(user);
                        } else {
                            throw new RuntimeException("User not found");
                        }

                        certificateVerify.setStudent(student.get());
                        certificateVerify.setEmployer(employerName);
                        certificateVerify.setOrganization(organizationName);
                        certificateVerify.setSignature(certificateVerify.getSignature());
                        certificateVerify.setDateVerified(LocalDateTime.now());
                        certificateVerify.setDateEdited(LocalDateTime.now());
                        certificateVerifyRepository.save(certificateVerify);

                    } else {
                        response.put("exists", false);
                        response.put("studentId", studentId);
                        response.put("message", "Certificate not found for existing student");
                    }

                } else {
                    response.put("exists", false);
                    response.put("studentId", studentId);
                    response.put("message", "Student not found");
                }
                results.add(response);
            }
        }
        return results;
    }

}
