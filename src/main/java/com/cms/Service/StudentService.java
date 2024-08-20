package com.cms.Service;

import com.cms.Model.*;
import com.cms.Repository.*;
import jakarta.transaction.Transactional;
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
public class StudentService {

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    ProgrammeRepository programmeRepository;

    @Autowired
    FacultyRepository facultyRepository;

    @Autowired
    DepartmentRepository departmentRepository;

    @Autowired
    AcademicYearRepository academicYearRepository;

    // Add new programme, update programme details, delete programme, etc.
    public Student addStudent(int studentId, String studentName, Faculty faculty, Department department, Programme programme,
                              AcademicYear academicYear) {
        if (studentRepository.existsByStudentId(studentId)) {
            return null;
        }

        Student student = new Student();
        student.setStudentId(studentId);
        student.setStudentName(studentName);
        student.setFaculty(faculty);
        student.setDepartment(department);
        student.setProgramme(programme);
        student.setAcademicYear(academicYear);
        department.setDateAdded(LocalDateTime.now());
        department.setDateEdited(LocalDateTime.now());

        studentRepository.save(student);
        return student;
    }

    public List<Student> allStudents() {
        return (List<Student>) studentRepository.findAll();
    }


    public boolean existsByStudentId(int studentId) {
        return studentRepository.existsByStudentId(studentId);
    }

    public boolean deleteStudent(int id) {
        if (studentRepository.existsById(id)) {
            studentRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Transactional
    public Student findByStudentId(int studentId) {
        return studentRepository.findByStudentId(studentId);
    }


    //=============METHOD TO PARSE EXCEL FILE=============//
    public StudentImportResult importStudent(MultipartFile file) throws IOException {
        List<Student> students = importExcelFile(file);
        List<Student> addedStudents = new ArrayList<>();
        Map<Integer, String> notAddedStudents = new HashMap<>();

        for (Student student : students) {
            int studentId = student.getStudentId();
//            String studentName = student.getStudentName();
            AcademicYear academicYear = student.getAcademicYear();
            Faculty faculty = student.getFaculty();
            Department department = student.getDepartment();
            Programme programme = student.getProgramme();


            if (studentRepository.existsById(studentId)) {
                notAddedStudents.put(studentId, "Student ID already exists.");
            }
//            else if (studentRepository.existsByStudentName(studentName)) {
//                notAddedStudents.put(studentId, "Student Name already exists");
//            }
            else if (academicYear == null) {
                notAddedStudents.put(studentId, "Academic Year does not exist");
            } else if (faculty == null) {
                notAddedStudents.put(studentId, "Faculty does not exist");
            } else if (department == null) {
                notAddedStudents.put(studentId, "Department does not exist");
            } else if (programme == null) {
                notAddedStudents.put(studentId, "Programme does not exist");
            } else {
                studentRepository.save(student);
                addedStudents.add(student);
            }
        }

        return new StudentImportResult(addedStudents, notAddedStudents);
    }

    public List<Student> importExcelFile(MultipartFile file) throws IOException {
        List<Student> students = new ArrayList<>();
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
                Student student = new Student();
                student.setStudentId((int) row.getCell(0).getNumericCellValue());

                student.setStudentName(row.getCell(1).getStringCellValue());

                int academicYear = (int) row.getCell(2).getNumericCellValue();
                Optional<AcademicYear> academicYearOptional = academicYearRepository.findById(academicYear);
                academicYearOptional.ifPresent(student::setAcademicYear);

                int facultyId = (int) row.getCell(3).getNumericCellValue();
                Optional<Faculty> facultyOptional = facultyRepository.findById(facultyId);
                facultyOptional.ifPresent(student::setFaculty);

                int departmentId = (int) row.getCell(4).getNumericCellValue();
                Optional<Department> departmentOptional = departmentRepository.findById(departmentId);
                departmentOptional.ifPresent(student::setDepartment);

                int programmeId = (int) row.getCell(5).getNumericCellValue();
                Optional<Programme> programmeOptional = programmeRepository.findById(programmeId);
                programmeOptional.ifPresent(student::setProgramme);

                students.add(student);
            }
        }
        return students;
    }

    public List<Faculty> getAllFaculties() {
        return facultyRepository.findAll();
    }
    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }
    public List<Programme> getAllProgrammes() {
        return programmeRepository.findAll();
    }
    public List<AcademicYear> getAllAcademicYears() {
        return academicYearRepository.findAll();
    }

}
