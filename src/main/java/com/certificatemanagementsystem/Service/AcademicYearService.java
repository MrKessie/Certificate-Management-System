package com.certificatemanagementsystem.Service;

import com.certificatemanagementsystem.Model.AcademicYear;
import com.certificatemanagementsystem.Model.Faculty;
import com.certificatemanagementsystem.Model.Programme;
import com.certificatemanagementsystem.Repository.AcademicYearRepository;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
public class AcademicYearService {

    @Autowired
    AcademicYearRepository academicYearRepository;

    //Method to add Academic Year
    public AcademicYear addAcademicYear(String year) {
        AcademicYear academicYear = new AcademicYear();
        academicYear.setYear(year);
        academicYear.setDateAdded(LocalDateTime.now());
        academicYear.setDateEdited(LocalDateTime.now());

        academicYearRepository.save(academicYear);
        return academicYear;
    }

    //Method to Import Academic Year from a file
    public void importAcademicYears(MultipartFile file) {
        try {
            List<AcademicYear> academicYears = parseExcelFile(file.getInputStream());
            academicYearRepository.saveAll(academicYears);
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse Excel file: " + e.getMessage());
        }
    }

    //Method to list all Academic Years
    public List<AcademicYear> allAcademicYears() {
        List<AcademicYear> academicYears = (List<AcademicYear>) academicYearRepository.findAll();
        return academicYears;
    }

    //Method to delete Academic Year
    public AcademicYear deleteAcademicYear(int yearId) {
        AcademicYear academicYear = academicYearRepository.findById(yearId);
        academicYearRepository.delete(academicYear);
        return academicYear;
    }

    public Optional<AcademicYear> getAcademicYearById(int academicYearId) {
        return academicYearRepository.getAcademicYearByYearId(academicYearId);
    }


    //Method to parse Excel File
    private List<AcademicYear> parseExcelFile(InputStream is) {
        try {
            Workbook workbook = new XSSFWorkbook(is);
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();
            List<AcademicYear> academicYears = new ArrayList<>();

            int rowNumber = 0;
            while (rows.hasNext()) {
                Row currentRow = rows.next();

                // Skip header
                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }

                Iterator<Cell> cellsInRow = currentRow.iterator();
                AcademicYear academicYear = new AcademicYear();

                int cellIdx = 0;
                while (cellsInRow.hasNext()) {
                    Cell currentCell = cellsInRow.next();

                    switch (cellIdx) {
//                        case 0:
//                            academicYear.setYear((int) currentCell.getNumericCellValue());
//                            break;
                        case 0:
                            academicYear.setYear(currentCell.getStringCellValue());
                            break;
                        // Add more cases here for other columns
                        default:
                            break;
                    }

                    cellIdx++;
                }

                academicYears.add(academicYear);
            }

            workbook.close();

            return academicYears;
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse Excel file: " + e.getMessage());
        }
    }
}
