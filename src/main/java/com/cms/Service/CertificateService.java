package com.cms.Service;

import com.cms.Model.Certificate;
import com.cms.Model.Student;
import com.cms.Repository.*;
import com.cms.VerificationResults;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class CertificateService {

    @Autowired
    private CertificateRepository certificateRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    ProgrammeRepository programmeRepository;

    @Autowired
    DepartmentRepository departmentRepository;

    @Autowired
    AcademicYearRepository academicYearRepository;

    @Autowired
    StudentService studentService;

    public Certificate addCertificate(Certificate certificate) {
        // Check if certificate with the given ID already exists
        if (certificateRepository.existsById(certificate.getCertificateId())) {
            return null; // Certificate with this ID already exists
        }

        certificate.setDateAdded(LocalDateTime.now());
        certificate.setDateEdited(LocalDateTime.now());
        return certificateRepository.save(certificate);
    }



    public List<Certificate> allCertificateLis() {
        return certificateRepository.findAll();
    }

    public boolean existsByCertificatePath(String certificatePath) {
        return certificateRepository.existsByCertificatePath(certificatePath);
    }

    public boolean existsByCertificateId(int certificateId) {
        return certificateRepository.existsById(certificateId);
    }

    public boolean deleteCertificate(int certificateId) {
        if (certificateRepository.existsById(certificateId)) {
            certificateRepository.deleteById(certificateId);
            return true;
        }
        return false;
    }


    public Optional<Certificate> findByStudentId(Student studentId) {
        return certificateRepository.findByStudentId(studentId);
    }

    public List<Certificate> getAllCertificates() {
        return certificateRepository.findAll();
    }


    public List<VerificationResults> bulkVerify(List<Integer> studentIds) {
        List<VerificationResults> results = new ArrayList<>();

        for (Integer studentId : studentIds) {
            Student student = studentService.findByStudentId(studentId);
            VerificationResults result = new VerificationResults();
            result.setStudentId(studentId);

            if (student != null && !student.getCertificates().isEmpty()) {
                result.setCertificateFound(true);
                result.setCertificatePath(student.getCertificates().get(0).getCertificatePath());
            } else {
                result.setCertificateFound(false);
            }

            results.add(result);
        }

        return results;
    }

    public List<Integer> parseExcelFile(MultipartFile file) throws Exception {
        List<Integer> studentIds = new ArrayList<>();

        try (InputStream is = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                Cell cell = row.getCell(0);
                if (cell != null && cell.getCellType() == CellType.NUMERIC) {
                    studentIds.add((int) cell.getNumericCellValue());
                }
            }
        }

        return studentIds;
    }



    public List<Map<String, Object>> importCertificates(MultipartFile file) throws IOException {
        List<Map<String, Object>> results = new ArrayList<>();

        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // Skip header row

                Map<String, Object> result = new HashMap<>();
                int certificateId = (int) row.getCell(0).getNumericCellValue();

                if (certificateRepository.existsById(certificateId)) {
                    result.put("status", "error");
                    result.put("message", "Certificate with ID " + certificateId + " already exists.");
                } else {
                    Certificate certificate = createCertificateFromRow(row);
                    certificateRepository.save(certificate);
                    result.put("status", "success");
                    result.put("message", "Certificate with ID " + certificateId + " imported successfully.");
                }

                results.add(result);
            }
        }

        return results;
    }

    private Certificate createCertificateFromRow(Row row) {
        Certificate certificate = new Certificate();
        certificate.setCertificateId((int) row.getCell(0).getNumericCellValue());
        certificate.setStudentId(studentRepository.findById((int) row.getCell(1).getNumericCellValue()).orElse(null));
        certificate.setStudentName(row.getCell(2).getStringCellValue());
        certificate.setProgramme(programmeRepository.findById((int) row.getCell(3).getNumericCellValue()).orElse(null));
        certificate.setAcademicYear(academicYearRepository.findById((int) row.getCell(4).getNumericCellValue()).orElse(null));
        certificate.setDepartment(departmentRepository.findById((int) row.getCell(5).getNumericCellValue()).orElse(null));
        certificate.setGraduateClass(row.getCell(6).getStringCellValue());
        certificate.setCertificatePath(row.getCell(7).getStringCellValue());
        certificate.setDateAdded(LocalDateTime.now());
        certificate.setDateEdited(LocalDateTime.now());
        return certificate;
    }


    public long totalCertificates() {
        return certificateRepository.count();
    }

}
