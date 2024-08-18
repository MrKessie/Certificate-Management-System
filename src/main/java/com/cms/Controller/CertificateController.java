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

    @GetMapping("/certificate-verify")
    public String showCertificateVerifyForm(HttpSession session, Model model) {
        model.addAttribute("certificates", certificateService.allCertificateLis());
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        model.addAttribute("loggedInUser", loggedInUser);
        return "certificate-verify";
    }

    @GetMapping("/bulk")
    public String showBulkVerifyForm() {
        return "test";
    }


    @PostMapping("/add")
    public String uploadCertificate(@RequestParam("studentId") int studentId, @RequestParam  String studentName,
                                    @RequestParam Programme programme, @RequestParam AcademicYear academicYear,
                                    @RequestParam Department department, @RequestParam String graduateClass,
                                    @RequestParam("certificateFile") MultipartFile certificateFile) throws IOException {
        Student student = studentService.findByStudentId(studentId);
        if (student == null) {
            throw new RuntimeException("Student not found");
        }

        // Save file to XAMPP server
        String uploadDir = "C:\\xampp\\htdocs\\certificates\\";
        String originalFileName = certificateFile.getOriginalFilename();
        String filePath = uploadDir + originalFileName;
        String relativePath = "certificates/" + originalFileName;
        File dest = new File(filePath);
//        certificateFile.transferTo(dest);

        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }

        certificateFile.transferTo(dest);
        // Log success
        System.out.println("File saved successfully");


        // Save certificate record to database
        Certificate certificate = new Certificate();
        certificate.setCertificatePath(relativePath);
        certificate.setStudentId(student);
        certificate.setStudentName(studentName);
        certificate.setProgramme(programme);
        certificate.setDepartment(department);
        certificate.setAcademicYear(academicYear);
        certificate.setGraduateClass(graduateClass);

        certificateService.addCertificate(certificate);

        return "redirect:/certificate/certificate-add";
    }



    @PostMapping("/upload")
    @ResponseBody
    public Map<String, Object> saveCertificate(@RequestParam("studentId") int studentId,
                                               @RequestParam String studentName,
                                               @RequestParam int programmeId,
                                               @RequestParam int academicYearId,
                                               @RequestParam int departmentId,
                                               @RequestParam String graduateClass,
                                               @RequestParam("certificateFile") MultipartFile certificateFile) throws IOException {
        Map<String, Object> response = new HashMap<>();

        try {
            Student student = studentService.findByStudentId(studentId);
            if (student == null) {
                throw new RuntimeException("Student not found");
            }

            Programme programme = programmeService.findById(programmeId);
            AcademicYear academicYear = academicYearService.findById(academicYearId);
            Department department = departmentService.findById(departmentId);

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

            // Save certificate record to database
            Certificate certificate = new Certificate();
            certificate.setCertificatePath(relativePath);
            certificate.setStudentId(student);
            certificate.setStudentName(studentName);
            certificate.setProgramme(programme);
            certificate.setDepartment(department);
            certificate.setAcademicYear(academicYear);
            certificate.setGraduateClass(graduateClass);
            certificateService.addCertificate(certificate);

            response.put("success", true);
            response.put("message", "Certificate saved successfully");
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
        }

        return response;
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

    private ExtractedDatas extractData(String pdfText) {
        String name = extractName(pdfText);
        String programme = extractProgramme(pdfText);
        String course = extractDepartment(pdfText);
        String classHonours = extractClass(pdfText);

        return new ExtractedDatas(name, programme, course, classHonours);
    }

    private String extractName(String text) {
        Pattern pattern = Pattern.compile("certify that\\s+([A-Z\\s]+)\\s+having pursued");
        Matcher matcher = pattern.matcher(text);
        return matcher.find() ? matcher.group(1).trim() : "";
    }

    private String extractProgramme(String text) {
        Pattern pattern = Pattern.compile("to the degree of\\s+(Bachelor of [A-Za-z\\s]+)\\s+with");
        Matcher matcher = pattern.matcher(text);
        return matcher.find() ? matcher.group(1).trim() : "";
    }

    private String extractDepartment(String text) {
        Pattern pattern = Pattern.compile("in\\s+([A-Za-z\\s]+Education)");
        Matcher matcher = pattern.matcher(text);
        return matcher.find() ? matcher.group(1).trim() : "";
    }

    private String extractClass(String text) {
        Pattern pattern = Pattern.compile("with\\s+(\\w+\\s+CLASS\\s+HONOURS\\s+\\([^)]+\\))");
        Matcher matcher = pattern.matcher(text);
        return matcher.find() ? matcher.group(1).trim() : "";
    }


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

            // Adjust the URL to be accessible from the frontend
            String relativePath = certificate.get().getCertificatePath(); // e.g., "certificates/nameOfFile"
            String baseUrl = "http://localhost/"; // Adjust as necessary
            String fullUrl = baseUrl + relativePath;

            System.out.println("View Link: " + fullUrl);

            response.put("viewLink", fullUrl); // Replace with actual path

        } else {
            response.put("exists", false);
        }

        return ResponseEntity.ok(response);
    }


//    @GetMapping("/student/{studentId}")
//    public ResponseEntity<?> getCertificateByStudentId(@PathVariable int studentId) {
//        Student student = studentService.findByStudentId(studentId);
//        if (student == null) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Certificate not found for student ID: " + studentId);
//        }
//
//        Certificate certificate = student.getCertificates().stream().findFirst().orElse(null);
//        if (certificate == null) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                    .body("Certificate not found for student ID: " + studentId);
//       }
//
////        certificateVerifyService.saveVerification(user, student, true);
//
//        Map<String, Object> response = new HashMap<>();
//        response.put("studentId", student.getStudentId());
//        response.put("certificateFile", certificate.getCertificatePath());
//        response.put("viewLink", "http://localhost/" + certificate.getCertificatePath()); // Assuming fileUrl is the correct URL
//
//        return ResponseEntity.ok(response);
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



}
