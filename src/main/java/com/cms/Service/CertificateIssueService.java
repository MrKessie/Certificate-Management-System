package com.cms.Service;

import com.cms.CertificateVerificationResult;
import com.cms.Model.*;
import com.cms.Repository.CertificateIssueRepository;
import com.cms.Repository.CertificateRepository;
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
public class CertificateIssueService {

    @Autowired
    CertificateIssueRepository certificateIssueRepository;

    @Autowired
    CertificateRepository certificateRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    StudentRepository studentRepository;


    public CertificateIssue saveIssueDetails(Student studentId, User userId, String collectorName, byte[] signature) {
        if (!studentRepository.existsByStudentId(studentId.getStudentId())) {
            return null;
        }

        CertificateIssue issue = new CertificateIssue();
        issue.setUserId(userId);
        issue.setStudentId(studentId);
        issue.setCollectorName(collectorName);
        issue.setSignature(signature);
        issue.setDateIssued(LocalDateTime.now());
        issue.setDateEdited(LocalDateTime.now());

        certificateIssueRepository.save(issue);

        return issue;
    }


    public List<CertificateIssue> issueListList() {
        return certificateIssueRepository.findAll();
    }


    public boolean existsByIssueId(int issueId) {
        return certificateIssueRepository.existsById(issueId);
    }


    public boolean deleteIssueDetail(int issueId) {
        if (certificateIssueRepository.existsById(issueId)) {
            certificateIssueRepository.deleteById(issueId);
            return true;
        }
        return false;
    }

    public Optional<CertificateIssue> findByStudentId(Student studentId) {
        return certificateIssueRepository.findByStudentId(studentId);
    }


    public List<Map<String, Object>> issueBulkCertificates(MultipartFile file) throws IOException {
        List<Map<String, Object>> results = new ArrayList<>();

        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
//                Cell cell = row.getCell(0);
                int studentId = (int) row.getCell(0).getNumericCellValue();
                int userId = (int) row.getCell(1).getNumericCellValue();
                String collectorName = row.getCell(2).getStringCellValue();

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

                        CertificateIssue certificateIssue = new CertificateIssue();
                        Optional<User> userOptional = Optional.ofNullable(userRepository.findById(userId));
                        if (userOptional.isPresent()) {
                            User user = userOptional.get();
                            certificateIssue.setUserId(user);
                        } else {
                            throw new RuntimeException("User not found");
                        }
//                        certificateIssue.setUserId(userRepository.findById(userId).orElseThrow());
                        certificateIssue.setStudentId(student.get());
                        certificateIssue.setCollectorName(collectorName);
                        certificateIssue.setSignature(certificateIssue.getSignature());
                        certificateIssue.setDateIssued(LocalDateTime.now());
                        certificateIssue.setDateEdited(LocalDateTime.now());
                        certificateIssueRepository.save(certificateIssue);

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
