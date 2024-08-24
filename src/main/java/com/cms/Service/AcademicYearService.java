package com.cms.Service;

import com.cms.Model.AcademicYear;
import com.cms.Model.Department;
import com.cms.Model.Faculty;
import com.cms.Repository.AcademicYearRepository;
import org.apache.poi.ss.usermodel.*;
import org.hibernate.mapping.Column;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.*;
import java.util.logging.Logger;

@Service
public class AcademicYearService {
    private static final Logger logger = Logger.getLogger(AcademicYearService.class.getName());

    @Autowired
    AcademicYearRepository academicYearRepository;

    //=============METHOD TO ADD ACADEMIC YEAR=============//
    public AcademicYear addAcademicYear(String year) {
        if  (academicYearRepository.existsByAcademicYear(year)) {
            return null;
        }

        AcademicYear academicYear = new AcademicYear();
        academicYear.setAcademicYear(year);
        academicYearRepository.save(academicYear);
        return academicYear;
    }


    //=============METHOD TO PARSE EXCEL FILE=============//
    public AcademicYearImportResult importAcademicYear(MultipartFile file) throws IOException {
        List<AcademicYear> academicYears = importExcelFile(file);
        List<AcademicYear> addedAcademicYears = new ArrayList<>();
        Map<String, String> notAddedAcademicYears = new HashMap<>();

        for (AcademicYear academicYear : academicYears) {
            int academicYearId = academicYear.getId();
            String year = academicYear.getAcademicYear();

//            if (academicYearRepository.existsById(academicYearId)) {
//                notAddedAcademicYears.put(academicYearId, "Academic ID already exists");
//            }
            if (academicYearRepository.existsByAcademicYear(year)) {
                notAddedAcademicYears.put(year, "Academic Year  already exist.");
            } else {
                academicYearRepository.save(academicYear);
                addedAcademicYears.add(academicYear);
            }
        }

        return new AcademicYearImportResult(addedAcademicYears, notAddedAcademicYears);
    }

    public List<AcademicYear> importExcelFile(MultipartFile file) throws IOException {
        List<AcademicYear> academicYears = new ArrayList<>();
        try (InputStream inputStream = file.getInputStream()) {
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                if (row.getRowNum() == 0) {
                    // Skip the header row
                    continue;
                }
                AcademicYear academicYear = new AcademicYear();
//                academicYear.setId((int) row.getCell(0).getNumericCellValue());
                academicYear.setAcademicYear(row.getCell(0).getStringCellValue());

                academicYears.add(academicYear);
            }
        }
        return academicYears;
    }

    public boolean existsByAcademicYear(String academicYear) {
        return academicYearRepository.existsByAcademicYear(academicYear);
    }


    public List<AcademicYear> academicYearList() {
        return academicYearRepository.findAll();
    }




    public boolean existByAcademicYearId(int academicYearId) {
        return academicYearRepository.existsById(academicYearId);
    }


    public boolean deleteAcademicYear(int academicYearId) {
        if (academicYearRepository.existsById(academicYearId)) {
            academicYearRepository.deleteById(academicYearId);
            return true;
        }
        return false;
    }

    //=============METHOD TO UPDATE ACADEMIC YEAR=============//

    public void update(AcademicYear academicYear) {
        academicYearRepository.save(academicYear);
    }

    //=============OPTIONAL METHOD TO ADD ACADEMIC YEAR=============//

    public Optional<AcademicYear> getAcademicYearById(int academicYearId) {
        return Optional.of(academicYearRepository.getById(academicYearId));
    }


    public AcademicYear findById(int id) {
        return academicYearRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid academic year ID: " + id));
    }

    //=============METHOD TO UPDATE ACADEMIC YEAR=============//
    public void update(int id, String updatedAcademicYear) {
        AcademicYear existingAcademicYear = findById(id);
        existingAcademicYear.setAcademicYear(updatedAcademicYear);
//        existingAcademicYear.setDateAdded(updatedAcademicYear.getDateAdded());
        existingAcademicYear.setDateEdited(LocalDateTime.now());
        academicYearRepository.save(existingAcademicYear);
    }


    public void updateAcademicYear(AcademicYear academicYear) {
        // Fetch the existing record from the database
        AcademicYear existingAcademicYear = academicYearRepository.findById(academicYear.getId())
                .orElseThrow(() -> new RuntimeException("Academic Year not found"));

        // Update the fields with the new values
        existingAcademicYear.setAcademicYear(academicYear.getAcademicYear());

        // Update the dateEdited to the current time
        existingAcademicYear.setDateEdited(LocalDateTime.now());

        // Save the updated entity back to the database
        academicYearRepository.save(existingAcademicYear);
    }

    public void delete(int id) {
        academicYearRepository.deleteById(id);
    }


}
