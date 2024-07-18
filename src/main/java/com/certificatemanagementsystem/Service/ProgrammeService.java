package com.certificatemanagementsystem.Service;

import com.certificatemanagementsystem.Model.Department;
import com.certificatemanagementsystem.Model.Faculty;
import com.certificatemanagementsystem.Model.Programme;
import com.certificatemanagementsystem.Repository.DepartmentRepository;
import com.certificatemanagementsystem.Repository.FacultyRepository;
import com.certificatemanagementsystem.Repository.ProgrammeRepository;
import jakarta.transaction.Transactional;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
public class ProgrammeService implements ProgrammeServiceInterface{

    @Autowired
    ProgrammeRepository programmeRepository;

    @Autowired
    FacultyRepository facultyRepository;

    @Autowired
    DepartmentRepository departmentRepository;

    public Programme addProgramme(int programmeId, String programmeName, int facultyId, int departmentId) {
        Programme programme = new Programme();
        programme.setProgrammeId(programmeId);
        programme.setProgrammeName(programmeName);

        Optional<Faculty> facultyOpt = facultyRepository.getFacultyById(facultyId);
        if (!facultyOpt.isPresent()) {
            throw new IllegalArgumentException("Invalid faculty Id: " + facultyId);
        }
        Faculty faculty = facultyOpt.get();

        Optional<Department> departmentOpt = departmentRepository.getDepartmentById(departmentId);
        if (!departmentOpt.isPresent()) {
            throw new IllegalArgumentException("Invalid department Id: " + departmentId);
        }
        Department department = departmentOpt.get();

        programme.setFacultyId(faculty);
        programme.setDepartmentId(department);
        programme.setDateAdded(LocalDateTime.now());
        programme.setDateEdited(LocalDateTime.now());

        programmeRepository.save(programme);
        return programme;
    }

    @Override
    @Transactional
    public void importProgramme(InputStream is) {
        List<Programme> programmes = parseExcelFile(is);
        programmeRepository.saveAll(programmes);
    }

    public List<Programme> programmeList() {
        List<Programme> programmes = (List<Programme>) programmeRepository.findAll();
        return  programmes;
    }

    public  Programme deleteProgramme(int programmeId) {
        Programme programme = programmeRepository.findById(programmeId);
        programmeRepository.delete(programme);
        return programme;
    }

    public Optional<Programme> getProgrammeById(int programmeId) {
        return programmeRepository.getProgrammeById(programmeId);
    }


    private List<Programme> parseExcelFile(InputStream is) {
        try {
            Workbook workbook = new XSSFWorkbook(is);
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();
            List<Programme> programmes = new ArrayList<>();

            int rowNumber = 0;
            while (rows.hasNext()) {
                Row currentRow = rows.next();

                // Skip header
                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }

                Iterator<Cell> cellsInRow = currentRow.iterator();
                Programme programme     = new Programme();

                int cellIdx = 0;
                while (cellsInRow.hasNext()) {
                    Cell currentCell = cellsInRow.next();

                    switch (cellIdx) {
                        case 0:
                            programme.setProgrammeId((int) currentCell.getNumericCellValue());
                            break;
                        case 1:
                            programme.setProgrammeName(currentCell.getStringCellValue());
                            break;
                        case 2:
                            int facultyId = (int) currentCell.getNumericCellValue();
                            Optional<Faculty> facultyOptional = facultyRepository.getFacultyById(facultyId);
                            if (facultyOptional.isPresent()) {
                                programme.setFacultyId(facultyOptional.get());
                            }
                            else {
                                throw new RuntimeException("Faculty not found: " + facultyId);
                            }
                            break;
                        case 3:
                            int departmentId = (int) currentCell.getNumericCellValue();
                            Optional<Department> departmentOptional = departmentRepository.getDepartmentById(departmentId);
                            if (departmentOptional.isPresent()) {
                                programme.setDepartmentId(departmentOptional.get());
                            }
                            else {
                                throw new RuntimeException("Could not find department: " + departmentId);
                            }
                            break;
                        default:
                            break;
                    }

                    cellIdx++;
                }

                programmes.add(programme);
            }

            workbook.close();

            return programmes;
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse Excel file: " + e.getMessage());
        }
    }
}
