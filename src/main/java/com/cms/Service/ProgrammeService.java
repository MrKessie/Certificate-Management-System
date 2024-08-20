package com.cms.Service;

import com.cms.Model.Department;
import com.cms.Model.Faculty;
import com.cms.Model.Programme;
import com.cms.Repository.DepartmentRepository;
import com.cms.Repository.FacultyRepository;
import com.cms.Repository.ProgrammeRepository;
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
import java.util.*;

@Service
public class ProgrammeService {
    @Autowired
    ProgrammeRepository programmeRepository;

    @Autowired
    FacultyRepository facultyRepository;

    @Autowired
    DepartmentRepository departmentRepository;

    // Add new programme, update programme details, delete programme, etc.
    public Programme addProgramme(int programmeId, String programmeName, Faculty faculty, Department department) {
        if (programmeRepository.existsByProgrammeId(programmeId)) {
            return null; // Programme ID already exists
        }

        Programme programme = new Programme();
        programme.setProgrammeId(programmeId);
        programme.setProgrammeName(programmeName);
        programme.setFaculty(faculty);
        programme.setDepartment(department);
        department.setDateAdded(LocalDateTime.now());
        department.setDateEdited(LocalDateTime.now());

        programmeRepository.save(programme);
        return programme;
    }

    public List<Programme> allProgrammes() {
        List<Programme> programmes = (List<Programme>) programmeRepository.findAll();
        return programmes;
    }

    public boolean existsByProgrammeId(int programmeId) {
        return programmeRepository.existsById(programmeId);
    }

    public boolean deleteProgramme(int id) {
        if (programmeRepository.existsById(id)) {
            programmeRepository.deleteById(id);
            return true;
        }
        return false;
    }


    public Optional<Programme> getProgrammeById(int programmeId) {
        return programmeRepository.getByProgrammeId(programmeId);
    }

    public Programme findById(int programmeId) {
        return programmeRepository.findByProgrammeId(programmeId);
    }

    public List<Faculty> getAllFaculties() {
        return facultyRepository.findAll();
    }


    //=============METHOD TO PARSE EXCEL FILE=============//
    public ProgrammeImportResult importProgramme(MultipartFile file) throws IOException {
        List<Programme> programmes = importExcelFile(file);
        List<Programme> addedProgrammes = new ArrayList<>();
        Map<Integer, String> notAddedProgrammes = new HashMap<>();

        for (Programme programme : programmes) {
            int programmeId = programme.getProgrammeId();
            String programmeName = programme.getProgrammeName();
            Faculty faculty = programme.getFaculty();
            Department department = programme.getDepartment();

            if (programmeRepository.existsById(programmeId)) {
                notAddedProgrammes.put(programmeId, "Programme with ID '" + programmeId + "' already exists");
            } else if (programmeRepository.existsByProgrammeName(programmeName)) {
                notAddedProgrammes.put(programmeId, "Programme with name '" + programmeName + "' already exists");
            } else if (faculty == null) {
                notAddedProgrammes.put(programmeId, "Faculty for programme with ID '" + programmeId + "' does not exist");
            } else if (department == null) {
                notAddedProgrammes.put(programmeId, "Department for program with ID '" + programmeId + "' does not exist");
            } else {
                programmeRepository.save(programme);
                addedProgrammes.add(programme);
            }
        }

        return new ProgrammeImportResult(addedProgrammes, notAddedProgrammes);
    }

    public List<Programme> importExcelFile(MultipartFile file) throws IOException {
        List<Programme> programmes = new ArrayList<>();
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
                Programme programme = new Programme();
                programme.setProgrammeId((int) row.getCell(0).getNumericCellValue());
                programme.setProgrammeName(row.getCell(1).getStringCellValue());
                int facultyId = (int) row.getCell(2).getNumericCellValue();
                Optional<Faculty> facultyOptional = facultyRepository.findById(facultyId);
                facultyOptional.ifPresent(programme::setFaculty);
                int departmentId = (int) row.getCell(2).getNumericCellValue();
                Optional<Department> departmentOptional = departmentRepository.findById(departmentId);
                departmentOptional.ifPresent(programme::setDepartment);
                programmes.add(programme);
            }
        }
        return programmes;
    }
}
