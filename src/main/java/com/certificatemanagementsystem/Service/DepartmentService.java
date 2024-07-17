package com.certificatemanagementsystem.Service;

import com.certificatemanagementsystem.Model.Department;
import com.certificatemanagementsystem.Model.Faculty;
import com.certificatemanagementsystem.Repository.DepartmentRepository;
import com.certificatemanagementsystem.Repository.FacultyRepository;
import org.apache.poi.ss.usermodel.*;
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
import java.util.stream.Collectors;
import java.util.*;

@Service
public class DepartmentService {

    @Autowired
    DepartmentRepository departmentRepository;

    @Autowired
    FacultyRepository facultyRepository;

    public List<String> getFacultyNames() {
        return facultyRepository.findAll().stream()
                .map(Faculty::getFacultyName)
                .collect(Collectors.toList());
    }

    public Department addDepartment(int departmentId, String departmentName, Faculty facultyId) {
        Department department = new Department();
        department.setDepartmentId(departmentId);
        department.setDepartmentName(departmentName);
        department.setFacultyId(facultyId);
        department.setDateAdded(LocalDateTime.now());
        department.setDateEdited(LocalDateTime.now());

        departmentRepository.save(department);
        return department;
    }

//    public void importDepartment(MultipartFile file) {
//        try {
//            List<Department> departments = parseExcelFile(file.getInputStream());
//            departmentRepository.saveAll(departments);
//        } catch (IOException e) {
//            throw new RuntimeException("Failed to parse Excel file: " + e.getMessage());
//        }
//    }

    public void importDepartments(MultipartFile file) throws IOException {
        List<Department> departments = new ArrayList<>();

        Workbook workbook = WorkbookFactory.create(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);

        for (Row row : sheet) {
            Department department = new Department();
            department.setDepartmentId((int) row.getCell(0).getNumericCellValue()); // Assuming departmentId is in the first column
            department.setDepartmentName(row.getCell(1).getStringCellValue());
            int facultyId = (int) row.getCell(1).getNumericCellValue();
            Faculty faculty = facultyRepository.findById(facultyId);
            //faculty.orElseThrow(() -> new RuntimeException("Faculty not found: " + facultyId));

            department.setFacultyId(faculty);
            department.setDateAdded(LocalDateTime.now());
            department.setDateEdited(LocalDateTime.now());

            departments.add(department);
        }

        workbook.close();

        departmentRepository.saveAll(departments);
    }

    public List<Department> allDepartments() {
        List<Department> departments = (List<Department>) departmentRepository.findAll();
        return departments;
    }

    public Department deleteDepartment(int departmentId) {
        Department department = departmentRepository.findById(departmentId);
        departmentRepository.delete(department);
        return department;
    }


    private List<Department> parseExcelFile(InputStream is) {
        try {
            Workbook workbook = new XSSFWorkbook(is);
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();
            List<Department> departments = new ArrayList<>();

            int rowNumber = 0;
            while (rows.hasNext()) {
                Row currentRow = rows.next();

                // Skip header
                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }

                Iterator<Cell> cellsInRow = currentRow.iterator();
                Department department = new Department();

                int cellIdx = 0;
                while (cellsInRow.hasNext()) {
                    Cell currentCell = cellsInRow.next();

                    switch (cellIdx) {
                        case 0:
                            department.setDepartmentId((int) currentCell.getNumericCellValue());
                            break;
                        case 1:
                            department.setDepartmentName(currentCell.getStringCellValue());
                            break;
                            case 2:
                                double facultyId = currentCell.getNumericCellValue();
                                String facultyName = String.valueOf((int) facultyId); // Convert numeric value to string
                                Faculty faculty = facultyRepository.findByFacultyName(facultyName);
                                if (faculty != null) {
                                    department.setFacultyId(faculty);
                                } else {
                                    Faculty newFaculty = new Faculty();
                                    newFaculty.setFacultyName(facultyName);
                                    facultyRepository.save(newFaculty);
                                    department.setFacultyId(newFaculty);
                                }
                                break;

                        // Add more cases here for other columns
                        default:
                            break;
                    }

                    cellIdx++;
                }

                departments.add(department);
            }

            workbook.close();

            return departments;
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse Excel file: " + e.getMessage());
        }
    }
}
