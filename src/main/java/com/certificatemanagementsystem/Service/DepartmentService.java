package com.certificatemanagementsystem.Service;

import com.certificatemanagementsystem.Model.Department;
import com.certificatemanagementsystem.Model.Faculty;
import com.certificatemanagementsystem.Repository.DepartmentRepository;
import com.certificatemanagementsystem.Repository.FacultyRepository;
import jakarta.transaction.Transactional;
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
public class DepartmentService implements DepartmentServiceInterface{

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

    @Override
    @Transactional
    public void importDepartment(InputStream is) {
        List<Department> departments = parseExcelFile(is);
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

    public Optional<Department> getDepartmentById(int departmentId) {
        return departmentRepository.getDepartmentById(departmentId);
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
                Department department     = new Department();

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
                                int facultyId = (int) currentCell.getNumericCellValue();
                                Optional<Faculty> facultyOptional = facultyRepository.getFacultyById(facultyId);
                                if (facultyOptional.isPresent()) {
                                    department.setFacultyId(facultyOptional.get());
                                } else {
                                    throw new RuntimeException("Faculty not found: " + facultyId);
                                }
                                break;
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
