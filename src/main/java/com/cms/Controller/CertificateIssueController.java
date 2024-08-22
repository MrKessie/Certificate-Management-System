package com.cms.Controller;

import com.cms.Model.Certificate;
import com.cms.Model.CertificateIssue;
import com.cms.Model.Student;
import com.cms.Model.User;
import com.cms.Repository.UserRepository;
import com.cms.Service.CertificateIssueService;
import com.cms.Service.CertificateService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/certificate/issue")
public class CertificateIssueController {

    @Autowired
    CertificateIssueService certificateIssueService;

    @Autowired
    CertificateService certificateService;

    @Autowired
    UserRepository userRepository;

    @GetMapping
    public String showCertificateAddForm(HttpSession session, Model model) {
        model.addAttribute("certificates", certificateIssueService.issueListList());
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        model.addAttribute("loggedInUser", loggedInUser);
        return "certificate-issue";
    }

//    @GetMapping("/student/{studentId}")
//    public ResponseEntity<Map<String, Object>> getCertificate(@RequestParam int userId, @PathVariable Student studentId) {
//        Optional<User> issuer = Optional.ofNullable(userRepository.findById(userId));
//
//        if (issuer.isEmpty()) {
//            Map<String, Object> response = new HashMap<>();
//            response.put("error", "User does not exist.");
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
//        }
//
//        Optional<Certificate> certificate = certificateService.findByStudentId(studentId);
//        Map<String, Object> response = new HashMap<>();
//
//        if (certificate.isPresent()) {
//            response.put("exists", true);
//            response.put("studentId", certificate.get().getStudentId());
//            response.put("name", certificate.get().getStudentName());
//            response.put("programme", certificate.get().getProgramme());
//            response.put("department", certificate.get().getDepartment());
//            response.put("academicYear", certificate.get().getAcademicYear());
//            response.put("classHonours", certificate.get().getGraduateClass());
//
//            String relativePath = certificate.get().getCertificatePath(); // e.g., "certificates/nameOfFile"
//            String baseUrl = "http://localhost/"; // Adjust as necessary
//            String fullUrl = baseUrl + relativePath;
//            response.put("viewLink", fullUrl); // Replace with actual path
//
//            CertificateIssue issueDetails = certificateIssueService.saveIssueDetails(issuer.get(), studentId);
//            if (issueDetails != null) {
//                response.put("issueId", issueDetails.getIssueId());
//            }
//
//        } else {
//            response.put("exists", false);
//        }
//        return ResponseEntity.ok(response);
//    }


    @GetMapping("/student/{studentId}")
    public ResponseEntity<Map<String, Object>> getCertificate(@PathVariable Student studentId) {
        Optional<Certificate> certificate = certificateService.findByStudentId(studentId);
        Map<String, Object> response = new HashMap<>();

        if (certificate.isPresent()) {
            response.put("exists", true);
            response.put("studentId", certificate.get().getStudentId());
            response.put("name", certificate.get().getStudentName());
            response.put("programme", certificate.get().getProgramme());
            response.put("department", certificate.get().getDepartment());
            response.put("academicYear", certificate.get().getAcademicYear());
            response.put("classHonours", certificate.get().getGraduateClass());

            String relativePath = certificate.get().getCertificatePath(); // e.g., "certificates/nameOfFile"
            String baseUrl = "http://localhost/"; // Adjust as necessary
            String fullUrl = baseUrl + relativePath;
            response.put("viewLink", fullUrl); // Replace with actual path

            // Save the certificate issuance details
            CertificateIssue issueDetails = certificateIssueService.saveIssueDetails(studentId);

        } else {
            response.put("exists", false);
        }

        return ResponseEntity.ok(response);
    }


    @PostMapping("/bulk")
    public ResponseEntity<List<Map<String, Object>>> verifyBulkCertificates(@RequestParam("file") MultipartFile file) {
        try {
            List<Map<String, Object>> results = certificateIssueService.issueBulkCertificates(file);
//            certificateIssueService.saveIssueDetails((Student) results);
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            e.printStackTrace(); // Log the error
            return ResponseEntity.badRequest().body(null);
        }
    }
}
