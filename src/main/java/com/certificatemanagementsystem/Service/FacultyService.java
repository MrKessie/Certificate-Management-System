package com.certificatemanagementsystem.Service;

import com.certificatemanagementsystem.Model.Faculty;
import com.certificatemanagementsystem.Repository.FacultyRepository;

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
public class FacultyService {
    @Autowired
    FacultyRepository facultyRepository;

    //Method to add Faculty
    public Faculty addFaculty(int facultyId, String facultyName) {
        Faculty faculty = new Faculty();
        faculty.setFacultyId(facultyId);
        faculty.setFacultyName(facultyName);
        faculty.setDateAdded(LocalDateTime.now());
        faculty.setDateEdited(LocalDateTime.now());

        facultyRepository.save(faculty);
        return faculty;
    }

    //Method to import Faculty as excel file
    public void importFaculty(MultipartFile file) {
        try {
            List<Faculty> faculties = parseExcelFile(file.getInputStream());
            facultyRepository.saveAll(faculties);
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse Excel file: " + e.getMessage());
        }
    }

    //Method to list all Faculties
    public List<Faculty> allFaculties() {
        List<Faculty> faculties = (List<Faculty>) facultyRepository.findAll();
        return faculties;
    }

    //Method to delete Faculty
    public Faculty deleteFaculty(int facultyId) {
        Faculty faculty = facultyRepository.findById(facultyId);
        facultyRepository.delete(faculty);
        return faculty;
    }

    public Optional<Faculty> getFacultyById(int facultyId) {
        return facultyRepository.getFacultyById(facultyId);
    }

    //Method to parse Excel file
    private List<Faculty> parseExcelFile(InputStream is) {
        try {
            Workbook workbook = new XSSFWorkbook(is);
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();
            List<Faculty> faculties = new ArrayList<>();

            int rowNumber = 0;
            while (rows.hasNext()) {
                Row currentRow = rows.next();

                // Skip header
                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }

                Iterator<Cell> cellsInRow = currentRow.iterator();
                Faculty faculty = new Faculty();

                int cellIdx = 0;
                while (cellsInRow.hasNext()) {
                    Cell currentCell = cellsInRow.next();

                    switch (cellIdx) {
                        case 0:
                            faculty.setFacultyId((int) currentCell.getNumericCellValue());
                            break;
                        case 1:
                            faculty.setFacultyName(currentCell.getStringCellValue());
                            break;
                        // Add more cases here for other columns
                        default:
                            break;
                    }

                    cellIdx++;
                }

                faculties.add(faculty);
            }

            workbook.close();

            return faculties;
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse Excel file: " + e.getMessage());
        }
    }


//    public List<Faculty> searchFaculties(String searchQuery) {
//        List<Faculty> faculties;
//        try {
//            int facultyId = Integer.parseInt(searchQuery);
//            faculties = facultyRepository.findByIdOrName(facultyId, "");
//        } catch (NumberFormatException e) {
//            faculties = facultyRepository.findByIdOrName(0, searchQuery);
//        }
//        return faculties;
//    }
}

