package com.cms.Service;

import com.cms.Model.Department;
import com.cms.Model.Faculty;
import com.cms.Repository.DepartmentRepository;
import com.cms.Repository.FacultyRepository;
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
public class DepartmentService {

    @Autowired
    DepartmentRepository departmentRepository;

    @Autowired
    FacultyRepository facultyRepository;

    //=============METHOD TO ADD DEPARTMENT=============//
    public Department addDepartment(int departmentId, String departmentName, Faculty faculty) {
        Department department = new Department();
        department.setDepartmentId(departmentId);
        department.setDepartmentName(departmentName);
        department.setFaculty(faculty);
        department.setDateAdded(LocalDateTime.now());
        department.setDateEdited(LocalDateTime.now());

        departmentRepository.save(department);
        return department;
    }

    //=============METHOD TO LIST ALL DEPARTMENTS=============//
    public List<Department> allDepartments() {
        return departmentRepository.findAll();
    }

    //=============METHOD TO DELETE DEPARTMENT=============//
    public boolean deleteDepartment(int id) {
        if (departmentRepository.existsById(id)) {
            departmentRepository.deleteById(id);
            return true;
        }
        return false;
    }

    //=============METHOD TO GET DEPARTMENT=============//
    public Optional<Department> getDepartmentById(int departmentId) {
        return departmentRepository.getByDepartmentId(departmentId);
    }

    public Department findById(int departmentId) {
        return departmentRepository.findByDepartmentId(departmentId);
    }

    //=============METHOD TO GET ALL FACULTIES=============//
    public List<Faculty> getAllFaculties() {
        return facultyRepository.findAll();
    }


    //=============METHOD TO PARSE EXCEL FILE=============//
    public List<Department> importExcelFile(MultipartFile file) throws IOException {
        List<Department> departments = new ArrayList<>();

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

                Department department = new Department();
                department.setDepartmentId((int) row.getCell(0).getNumericCellValue());
                department.setDepartmentName(row.getCell(1).getStringCellValue());
                int facultyId = (int) row.getCell(2).getNumericCellValue();
                Optional<Faculty> facultyOptional = facultyRepository.getByFacultyId(facultyId);
                if (facultyOptional.isPresent()) {
                    department.setFaculty(facultyOptional.get());
                }
                else {
                    throw new RuntimeException("Faculty not found: " + facultyId);
                }

                departments.add(department);
            }
        }
        return departments;
    }

    //=============METHOD TO IMPORT ACADEMIC YEAR FROM EXCEL FILE=============//
    public void importDepartment(MultipartFile file) throws IOException {
        List<Department> departments = importExcelFile(file);
        departmentRepository.saveAll(departments);
    }
}
