package com.cms.Service;

import com.cms.Model.Department;
import com.cms.Model.Faculty;
import com.cms.Repository.FacultyRepository;
import jakarta.persistence.EntityNotFoundException;
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
public class FacultyService {

    @Autowired
    FacultyRepository facultyRepository;

    //=============METHOD TO ADD FACULTY=============//
    public Faculty addFaculty(int facultyId, String facultyName) {
        if (facultyRepository.existsByFacultyId(facultyId)) {
            return null;
        }

        Faculty faculty = new Faculty();
        faculty.setFacultyId(facultyId);
        faculty.setFacultyName(facultyName);
        faculty.setDateAdded(LocalDateTime.now());
        faculty.setDateEdited(LocalDateTime.now());

        facultyRepository.save(faculty);
        return faculty;
    }


    //=============METHOD TO PARSE EXCEL FILE=============//
    public List<Faculty> importExcelFile(MultipartFile file) throws IOException {
        List<Faculty> faculties = new ArrayList<>();

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

                Faculty faculty = new Faculty();
                faculty.setFacultyId((int) row.getCell(0).getNumericCellValue());
                faculty.setFacultyName(row.getCell(1).getStringCellValue());

                faculties.add(faculty);
            }
        }
        return faculties;
    }

    //=============METHOD TO IMPORT ACADEMIC YEAR FROM EXCEL FILE=============//
    public FacultyImportResult importFaculty(MultipartFile file) throws IOException {
        List<Faculty> faculties = importExcelFile(file);
        List<Faculty> addedFaculties = new ArrayList<>();
        Map<Integer, String> notAddedFaculties = new HashMap<>();

        for (Faculty faculty : faculties) {
            int facultyId = faculty.getFacultyId();
            String facultyName = faculty.getFacultyName();

            if (facultyRepository.existsById(facultyId)) {
                notAddedFaculties.put(facultyId, "Faculty ID already exists");
            } else if (facultyRepository.existsByFacultyName(facultyName)) {
                notAddedFaculties.put(facultyId, "Faculty Name '" + facultyName + "' already exists");
            } else {
                facultyRepository.save(faculty);
                addedFaculties.add(faculty);
            }
        }

        return new FacultyImportResult(addedFaculties, notAddedFaculties);
    }


    //=============METHOD TO LIST ALL FACULTIES=============//
    public List<Faculty> allFacultyList() {
        return facultyRepository.findAll();
    }


    //=============METHOD TO DELETE FACULTY=============//
    public boolean existByFacultyId(int facultyId) {
        return facultyRepository.existsByFacultyId(facultyId);
    }

    public boolean deleteFaculty(int id) {
        if (facultyRepository.existsById(id)) {
            facultyRepository.deleteById(id);
            return true;
        }
        return false;
    }

    //=============METHOD TO UPDATE FACULTY =============//
    public void updateFaculty(int facultyId, String facultyName) {
        Faculty faculty = facultyRepository.findById(facultyId).orElseThrow(() -> new EntityNotFoundException("Faculty not found"));
        faculty.setFacultyName(facultyName);
        facultyRepository.save(faculty);
    }

    //=============OPTIONAL METHOD TO ADD FACULTY=============//
    public Optional<Faculty> getFacultyById(int facultyId) {
        return facultyRepository.getByFacultyId(facultyId);
    }


    public Faculty findById(int facultyId) {
        return facultyRepository.findByFacultyId(facultyId);
    }


}
