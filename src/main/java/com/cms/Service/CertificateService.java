package com.cms.Service;

import com.cms.Model.Certificate;
import com.cms.Model.Student;
import com.cms.Repository.CertificateRepository;
import com.cms.Repository.StudentRepository;
import com.cms.VerificationResults;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CertificateService {

    @Autowired
    private CertificateRepository certificateRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    StudentService studentService;

    public Certificate addCertificate(Certificate certificate) {
        certificate.setDateAdded(LocalDateTime.now());
        certificate.setDateEdited(LocalDateTime.now());
        certificateRepository.save(certificate);
        return certificate;
    }


    public List<Certificate> allCertificateLis() {
        return certificateRepository.findAll();
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





}
