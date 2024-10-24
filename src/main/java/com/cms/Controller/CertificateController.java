package com.cms.Controller;

import com.cms.Model.*;
import com.cms.Service.*;
import com.cms.VerificationResults;
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

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/certificate")
public class CertificateController {

    @Autowired
    private CertificateService certificateService;

    @Autowired
    StudentService studentService;

    @Autowired
    CertificateVerifyService certificateVerifyService;

    @Autowired
    ProgrammeService programmeService;

    @Autowired
    DepartmentService departmentService;

    @Autowired
    AcademicYearService academicYearService;

    @Autowired
    UserService userService;

    @Autowired
    UserActivityService userActivityService;



    @GetMapping("/certificate-add")
    public String showCertificateAddForm(HttpSession session, Model model) {
        model.addAttribute("certificates", certificateService.allCertificateLis());
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        model.addAttribute("loggedInUser", loggedInUser);
        return "certificate-add";
    }

    @GetMapping("/client-certificate-add")
    public String showClientCertificateAddForm(HttpSession session, Model model) {
        model.addAttribute("certificates", certificateService.allCertificateLis());
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        model.addAttribute("loggedInUser", loggedInUser);
        return "client-certificate-add";
    }


    @GetMapping("/bulk")
    public String showBulkVerifyForm() {
        return "academic-year";
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userService.findById(Integer.parseInt(username));
    }


    //========== METHOD TO UPLOAD A CERTIFICATE ==========
    @PostMapping("/add")
    public ResponseEntity<Map<String, String>> uploadCertificate(@RequestParam int certificateId, @RequestParam("studentId") int studentId,
                                                                 @RequestParam String studentName, @RequestParam Programme programme,
                                                                 @RequestParam AcademicYear academicYear, @RequestParam Department department,
                                                                 @RequestParam String graduateClass, @RequestParam("certificateFile") MultipartFile certificateFile) throws IOException {

        User currentUser = getCurrentUser();
        Map<String, String> response = new HashMap<>();

        // Check if certificate with the given ID already exists
        if (certificateService.existsByCertificateId(certificateId)) {
            response.put("status", "error");
            response.put("message", "Certificate already exists.");
            userActivityService.logActivity(currentUser, "ADD_CERTIFICATE_FAILED",
                    "Attempted to add existing certificate: " + certificateId);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }

        Student student = studentService.findByStudentId(studentId);
        if (student == null) {
            response.put("status", "error");
            response.put("message", "Student does not exist.");
            userActivityService.logActivity(currentUser, "ADD_CERTIFICATE_FAILED",
                    "Attempted to add existing certificate: " + certificateId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        // Save file to XAMPP server
        String uploadDir = "C:\\xampp\\htdocs\\certificates\\";
        String originalFileName = certificateFile.getOriginalFilename();
        String filePath = uploadDir + originalFileName;
        String relativePath = "certificates/" + originalFileName;
        File dest = new File(filePath);
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        certificateFile.transferTo(dest);

        // Log success
        System.out.println("File saved successfully");

        // Save certificate record to database
        Certificate certificate = new Certificate();
        certificate.setCertificateId(certificateId);
        certificate.setCertificatePath(relativePath);
        certificate.setStudentId(student);
        certificate.setStudentName(studentName);
        certificate.setProgramme(programme);
        certificate.setDepartment(department);
        certificate.setAcademicYear(academicYear);
        certificate.setGraduateClass(graduateClass);

        Certificate savedCertificate = certificateService.addCertificate(certificate);

        if (savedCertificate == null) {
            response.put("status", "error");
            response.put("message", "Failed to add certificate. It may already exist.");
            userActivityService.logActivity(currentUser, "ADD_CERTIFICATE_FAILED",
                    "Attempted to add existing certificate: " + certificateId);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

        response.put("status", "success");
        response.put("message", "Certificate added successfully!");
        userActivityService.logActivity(currentUser, "ADD_CERTIFICATE_SUCCESS",
                "Attempted to add existing certificate: " + certificateId);
        return ResponseEntity.ok(response);
    }



    //========== METHOD TO IMPORT CERTIFICATE FROM AN EXCEL FILE
    @PostMapping("/import")
    public ResponseEntity<List<Map<String, Object>>> importCertificates(@RequestParam("file") MultipartFile file) {
        User currentUser = getCurrentUser();
        try {
            List<Map<String, Object>> results = certificateService.importCertificates(file);
            return ResponseEntity.ok(results);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    //========== METHOD TO VERIFY A CERTIFICATE IN BULK ==========
    @GetMapping("/verify/bulk")
    public ResponseEntity<?> bulkVerify(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File is empty.");
        }

        try {
            List<Integer> studentIds = certificateService.parseExcelFile(file);
            List<VerificationResults> results = certificateService.bulkVerify(studentIds);
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing file.");
        }
    }



    @GetMapping("/all")
    @ResponseBody
    public List<Certificate> facultyList() {
        return certificateService.allCertificateLis();
    }



    //============= METHOD TO DELETE CERTIFICATE BY CERTIFICATE ID =============//
    @DeleteMapping("/delete/{certificateId}")
    public ResponseEntity<String> deleteCertificate(@PathVariable int certificateId) {
        User currentUser = getCurrentUser();
        if (certificateService.existsByCertificateId(certificateId)) {
            certificateService.deleteCertificate(certificateId);
            userActivityService.logActivity(currentUser, "DELETE_CERTIFICATE_SUCCESS",
                    "Deleted certificate with ID: " + certificateId);
            return ResponseEntity.ok("Certificate deleted successfully!");
        } else {
            userActivityService.logActivity(currentUser, "DELETE_CERTIFICATE_FAILED",
                    "Attempted to delete non-existent certificate ID: " + certificateId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Certificate not found.");
        }
    }



}
