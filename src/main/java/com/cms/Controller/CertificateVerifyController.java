package com.cms.Controller;

import com.cms.Model.*;
import com.cms.Service.CertificateService;
import com.cms.Service.CertificateVerifyService;
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
@RequestMapping("/certificate/verify")
public class CertificateVerifyController {
    @Autowired
    CertificateVerifyService certificateVerifyService;

    @Autowired
    CertificateService certificateService;

    @GetMapping
    public String showCertificateVerifyForm(HttpSession session, Model model) {
        model.addAttribute("certificates", certificateVerifyService.verifyList());
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        model.addAttribute("loggedInUser", loggedInUser);
        return "certificate-verify";
    }


    @GetMapping("/all-verified")
    public String showCertificateAllForm(HttpSession session, Model model) {
        model.addAttribute("certificates", certificateVerifyService.verifyList());
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        model.addAttribute("loggedInUser", loggedInUser);
        return "certificate-verify-all";
    }


    @PostMapping("/student/{studentId}")
    public ResponseEntity<Map<String, Object>> getCertificate(@PathVariable Student studentId, @RequestParam("userId") User userId,
                                                              @RequestParam("employer") String  employer, @RequestParam("organization") String  organization,
                                                              @RequestParam("signature") String signatureBase64) {

        Optional<Certificate> certificate = certificateService.findByStudentId(studentId);
        Map<String, Object> response = new HashMap<>();

        if (certificate.isPresent()) {
            // Convert base64 signature to byte array
            byte[] signatureBytes = Base64.getDecoder().decode(signatureBase64);

            CertificateVerify verificationDetails = certificateVerifyService.saveVerificationDetails(studentId, userId, employer, organization, signatureBytes);

            if (verificationDetails != null) {
                response.put("success", true);
                response.put("message", "Certificate verified successfully");
                response.put("verificationId", verificationDetails.getVerificationId());
//                response.put("studentId", certificate.get().getStudentId());
                response.put("name", certificate.get().getStudentName());
                response.put("programme", certificate.get().getProgramme());
                response.put("department", certificate.get().getDepartment());
                response.put("academicYear", certificate.get().getAcademicYear());
                response.put("classHonours", certificate.get().getGraduateClass());
                response.put("viewLink", "http://localhost/" + certificate.get().getCertificatePath());
            } else {
                response.put("success", false);
                response.put("message", "Failed to verify certificate");
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
            List<Map<String, Object>> results = certificateVerifyService.verifyBulkCertificates(file);
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            e.printStackTrace(); // Log the error
            return ResponseEntity.badRequest().body(null);
        }
    }


    @GetMapping("/all")
    @ResponseBody
    public List<CertificateVerify> CertificateVerifyList() {
        return certificateVerifyService.verifyList();
    }


    @DeleteMapping("/delete/{verificationId}")
    public ResponseEntity<String> deleteVerificationDetails(@PathVariable int verificationId) {
        if (certificateVerifyService.existsByVerificationId(verificationId)) {
            certificateVerifyService.deleteVerification(verificationId);
            return ResponseEntity.ok("Certificate Verification details deleted successfully!");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Certificate Verification details not found.");
        }
    }
}
