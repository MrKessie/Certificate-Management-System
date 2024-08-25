package com.cms.Controller;

import com.cms.Model.*;
import com.cms.Repository.UserRepository;
import com.cms.Service.CertificateIssueService;
import com.cms.Service.CertificateService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Controller
@RequestMapping("/certificate-issue")
public class CertificateIssueController {

    @Autowired
    CertificateIssueService certificateIssueService;

    @Autowired
    CertificateService certificateService;

    @Autowired
    UserRepository userRepository;

    @GetMapping
    public String showCertificateAddForm(HttpSession session, Model model) {
//        model.addAttribute("certificatesIssued", certificateIssueService.issueList());
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        model.addAttribute("loggedInUser", loggedInUser);
        return "certificate-issue";
    }

    @GetMapping("/all-issued")
    public String showCertificateAllForm(HttpSession session, Model model) {
        model.addAttribute("certificatesIssued", certificateIssueService.issueList());
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        model.addAttribute("loggedInUser", loggedInUser);
        return "certificate-issue-all";
    }


    @PostMapping("/student/{studentId}")
    public ResponseEntity<Map<String, Object>> getCertificate(@PathVariable Student studentId, @RequestParam("userId") User userId,
                                                              @RequestParam("collectorName") String collectorName, @RequestParam("signature") String signatureBase64) {

        Optional<Certificate> certificate = certificateService.findByStudentId(studentId);
        Map<String, Object> response = new HashMap<>();

        if (certificate.isPresent()) {
            // Convert base64 signature to byte array
            byte[] signatureBytes = Base64.getDecoder().decode(signatureBase64);

            // Save the certificate issuance details
            CertificateIssue issueDetails = certificateIssueService.saveIssueDetails(studentId, userId, collectorName, signatureBytes);

            if (issueDetails != null) {
                response.put("success", true);
                response.put("message", "Certificate issued successfully");
                response.put("issueId", issueDetails.getIssueId());
                response.put("name", certificate.get().getStudentName());
                response.put("programme", certificate.get().getProgramme());
                response.put("department", certificate.get().getDepartment());
                response.put("academicYear", certificate.get().getAcademicYear());
                response.put("classHonours", certificate.get().getGraduateClass());
                response.put("viewLink", "http://localhost/" + certificate.get().getCertificatePath());
            } else {
                response.put("success", false);
                response.put("message", "Failed to issue certificate");
            }
        } else {
            response.put("success", false);
            response.put("message", "Certificate not found");
        }
        return ResponseEntity.ok(response);
    }


    @PostMapping("/bulk")
    public ResponseEntity<List<Map<String, Object>>> verifyBulkCertificates(@RequestParam("file") MultipartFile file) {
        try {
            List<Map<String, Object>> results = certificateIssueService.issueBulkCertificates(file);
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            e.printStackTrace(); // Log the error
            return ResponseEntity.badRequest().body(null);
        }
    }


    @GetMapping("/all")
    @ResponseBody
    public List<CertificateIssue> certificateIssueList() {
        return certificateIssueService.issueList();
    }


    @DeleteMapping("/delete/{issueId}")
    public ResponseEntity<String> deleteIssueDetails(@PathVariable int issueId) {
        if (certificateIssueService.existsByIssueId(issueId)) {
            certificateIssueService.deleteIssueDetail(issueId);
            return ResponseEntity.ok("Certificate Issue details deleted successfully!");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Certificate Issue details not found.");
        }
    }
}
