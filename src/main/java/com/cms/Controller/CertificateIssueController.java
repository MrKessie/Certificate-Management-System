package com.cms.Controller;

import com.cms.Model.*;
import com.cms.Repository.UserRepository;
import com.cms.Service.CertificateIssueService;
import com.cms.Service.CertificateService;
import com.cms.Service.UserActivityService;
import com.cms.Service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @Autowired
    UserService userService;

    @Autowired
    UserActivityService userActivityService;

    @GetMapping
    public String showCertificateIssueForm(HttpSession session, Model model) {
        model.addAttribute("certificatesIssued", certificateIssueService.issueList());
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

    @GetMapping("/client-certificate-issue")
    public String showClientCertificateIssueForm(HttpSession session, Model model) {
        model.addAttribute("certificatesIssued", certificateIssueService.issueList());
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        model.addAttribute("loggedInUser", loggedInUser);
        return "client-certificate-issue";
    }

    @GetMapping("/client-all-issued")
    public String showClientCertificateAllForm(HttpSession session, Model model) {
        model.addAttribute("certificatesIssued", certificateIssueService.issueList());
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        model.addAttribute("loggedInUser", loggedInUser);
        return "client-certificate-issue-all";
    }




    @PostMapping("/student/{studentId}")
    public ResponseEntity<Map<String, Object>> getCertificate(@PathVariable Student studentId, @RequestParam("collectorName") String collectorName, @RequestParam("signature") String signatureBase64) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User currentUser = userService.findById(Integer.parseInt(username));

        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of(
                    "success", false,
                    "message", "User not authenticated"
            ));
        }

        Optional<Certificate> certificate = certificateService.findByStudentId(studentId);
        Map<String, Object> response = new HashMap<>();

        if (certificate.isPresent()) {
            // Convert base64 signature to byte array
            byte[] signatureBytes = Base64.getDecoder().decode(signatureBase64);

            // Save the certificate issuance details
            CertificateIssue issueDetails = certificateIssueService.saveIssueDetails(studentId, currentUser, collectorName, signatureBytes);

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

                userActivityService.logActivity(currentUser, "CERTIFICATE_ISSUED",
                        "Certificate issued for student: " + studentId.getStudentId());
            } else {
                response.put("success", false);
                response.put("message", "Failed to issue certificate");

                userActivityService.logActivity(currentUser, "CERTIFICATE_ISSUE_FAILED",
                        "Failed to issue certificate for student: " + studentId.getStudentId());
            }
        } else {
            response.put("success", false);
            response.put("message", "Certificate not found");

            userActivityService.logActivity(currentUser, "CERTIFICATE_NOT_FOUND",
                    "Certificate not found for student: " + studentId.getStudentId());
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
