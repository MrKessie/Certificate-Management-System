package com.cms.Service;

import com.cms.Model.AcademicYear;
import com.cms.Repository.AcademicYearRepository;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class AcademicYearService {
    private static final Logger logger = Logger.getLogger(AcademicYearService.class.getName());

    @Autowired
    AcademicYearRepository academicYearRepository;

    //=============METHOD TO ADD ACADEMIC YEAR=============//
    public AcademicYear addAcademicYear(String year) {
        AcademicYear academicYear = new AcademicYear();
        academicYear.setAcademicYear(year);
        academicYearRepository.save(academicYear);
        return academicYear;
    }

    //=============METHOD TO LIST ACADEMIC YEARS=============//
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
                academicYear.setAcademicYear(row.getCell(0).getStringCellValue());

                academicYears.add(academicYear);
            }
        }
        return academicYears;
    }


    //=============METHOD TO IMPORT ACADEMIC YEAR=============//
    public void importAcademicYear(MultipartFile file) throws IOException {
        List<AcademicYear> academicYears = importExcelFile(file);
        academicYearRepository.saveAll(academicYears);
    }

    public List<AcademicYear> academicYearList() {
        return academicYearRepository.findAll();
    }

    public void deleteDepartmentById(int id) {
        academicYearRepository.deleteById(id);
    }

    //=============METHOD TO UPDATE ACADEMIC YEAR=============//

    public void update(AcademicYear academicYear) {
        academicYearRepository.save(academicYear);
    }

    //=============OPTIONAL METHOD TO ADD ACADEMIC YEAR=============//

    public Optional<AcademicYear> getAcademicYearById(int academicYearId) {
        return Optional.of(academicYearRepository.getById(academicYearId));
    }

    //=============METHOD TO IMPORT EXCEL FILE=============//


    public AcademicYear findById(int id) {
        return academicYearRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid academic year ID: " + id));
    }

    //=============METHOD TO UPDATE ACADEMIC YEAR=============//
    public void update(int id, AcademicYear updatedAcademicYear) {
        AcademicYear existingAcademicYear = findById(id);
        existingAcademicYear.setAcademicYear(updatedAcademicYear.getAcademicYear());
        existingAcademicYear.setDateAdded(updatedAcademicYear.getDateAdded());
        existingAcademicYear.setDateEdited(LocalDateTime.now());
        academicYearRepository.save(existingAcademicYear);
    }


    public void delete(int id) {
        academicYearRepository.deleteById(id);
    }


}
