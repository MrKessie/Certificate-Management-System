package com.cms.Controller;

import com.cms.Model.*;
import com.cms.Service.CertificateService;
import com.cms.Service.CertificateVerifyService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@Controller
@RequestMapping("certificate/verify")
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


    @GetMapping("/student/{studentId}")
    public ResponseEntity<Map<String, Object>> getCertificate(@PathVariable Student studentId, @RequestParam String employer,
                                                              @RequestParam String organization) {
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
            CertificateVerify verificationDetails = certificateVerifyService.saveVerificationDetails(studentId, employer, organization);

        } else {
            response.put("exists", false);
        }

        return ResponseEntity.ok(response);
    }
}
