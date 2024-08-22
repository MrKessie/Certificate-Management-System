package com.cms.Controller;

import com.cms.ExtractedDatas;
import com.cms.Model.*;
import com.cms.Service.*;
import com.cms.VerificationResults;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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



    @GetMapping("/certificate-add")
    public String showCertificateAddForm(HttpSession session, Model model) {
        model.addAttribute("certificates", certificateService.allCertificateLis());
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        model.addAttribute("loggedInUser", loggedInUser);
        return "certificate-add";
    }


    @GetMapping("/bulk")
    public String showBulkVerifyForm() {
        return "test";
    }


    @PostMapping("/add")
    public ResponseEntity<Map<String, String>> uploadCertificate(@RequestParam int certificateId, @RequestParam("studentId") int studentId,
                                                                 @RequestParam String studentName, @RequestParam Programme programme,
                                                                 @RequestParam AcademicYear academicYear, @RequestParam Department department,
                                                                 @RequestParam String graduateClass, @RequestParam("certificateFile") MultipartFile certificateFile) throws IOException {

        Map<String, String> response = new HashMap<>();

        // Check if certificate with the given ID already exists
        if (certificateService.existsByCertificateId(certificateId)) {
            response.put("status", "error");
            response.put("message", "Certificate already exists.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }

        Student student = studentService.findByStudentId(studentId);
        if (student == null) {
            response.put("status", "error");
            response.put("message", "Student does not exist.");
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
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

        response.put("status", "success");
        response.put("message", "Certificate added successfully!");
        return ResponseEntity.ok(response);
    }



    @PostMapping("/import")
    public ResponseEntity<List<Map<String, Object>>> importCertificates(@RequestParam("file") MultipartFile file) {
        try {
            List<Map<String, Object>> results = certificateService.importCertificates(file);
            return ResponseEntity.ok(results);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


//    @PostMapping("/upload")
//    public ResponseEntity<ExtractedData> uploadFile(@RequestParam int student, @RequestParam("file") MultipartFile file) throws IOException {
//        String pdfText = PDFExtractor.extractTextFromPDF(file.getInputStream());
//        ExtractedData extractedData = extractData(pdfText);
////        Certificate certificate = extractDataAndCreateCertificate(pdfText, student);
//
////        certificateRepository.save(certificate);
//
//        return ResponseEntity.ok(extractedData);
//    }

//    private Certificate extractDataAndCreateCertificate(String pdfText, int studentId) {
//        String studentName = extractName(pdfText);
//        String programmeName = extractProgramme(pdfText);
//        String departmentName = extractDepartment(pdfText);
//        String graduateClass = extractClass(pdfText);
//
//        // Fetch the related entities from the database
//        Student student = studentRepository.findById(studentId)
//                .orElseThrow(() -> new RuntimeException("Student not found"));
//
//        Programme programme = programmeRepository.findProgrammeByProgrammeName(programmeName);
////                .orElseThrow(() -> new RuntimeException("Programme not found"));
//
//        Department department = departmentRepository.findDepartmentByDepartmentName(departmentName);
////                .orElseThrow(() -> new RuntimeException("Department not found"));
//
//        // Assume you have a way to determine the academic year, perhaps based on the current date or extracted data
//        AcademicYear academicYear = academicYearRepository.findById(1) // replace with your logic
//                .orElseThrow(() -> new RuntimeException("Academic Year not found"));
//
//        // Construct the certificate path
//        String certificatePath = "path_to_certificate_storage"; // Adjust this as per your system
//
//        // Create and return the certificate object
//        return new Certificate(student, studentName, academicYear, programme, department, graduateClass, certificatePath);
//    }

//    private ExtractedDatas extractData(String pdfText) {
//        String name = extractName(pdfText);
//        String programme = extractProgramme(pdfText);
//        String course = extractDepartment(pdfText);
//        String classHonours = extractClass(pdfText);
//
//        return new ExtractedDatas(name, programme, course, classHonours);
//    }
//
//    private String extractName(String text) {
//        Pattern pattern = Pattern.compile("certify that\\s+([A-Z\\s]+)\\s+having pursued");
//        Matcher matcher = pattern.matcher(text);
//        return matcher.find() ? matcher.group(1).trim() : "";
//    }
//
//    private String extractProgramme(String text) {
//        Pattern pattern = Pattern.compile("to the degree of\\s+(Bachelor of [A-Za-z\\s]+)\\s+with");
//        Matcher matcher = pattern.matcher(text);
//        return matcher.find() ? matcher.group(1).trim() : "";
//    }
//
//    private String extractDepartment(String text) {
//        Pattern pattern = Pattern.compile("in\\s+([A-Za-z\\s]+Education)");
//        Matcher matcher = pattern.matcher(text);
//        return matcher.find() ? matcher.group(1).trim() : "";
//    }
//
//    private String extractClass(String text) {
//        Pattern pattern = Pattern.compile("with\\s+(\\w+\\s+CLASS\\s+HONOURS\\s+\\([^)]+\\))");
//        Matcher matcher = pattern.matcher(text);
//        return matcher.find() ? matcher.group(1).trim() : "";
//    }






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



    //=============METHOD TO DELETE ALL FACULTY BY ID=============//
    @DeleteMapping("/delete/{certificateId}")
    public ResponseEntity<String> deleteCertificate(@PathVariable int certificateId) {
        if (certificateService.existsByCertificateId(certificateId)) {
            certificateService.deleteCertificate(certificateId);
            return ResponseEntity.ok("Certificate deleted successfully!");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Certificate not found.");
        }
    }



}
