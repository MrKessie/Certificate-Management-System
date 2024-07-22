package com.certificatemanagementsystem.Service;

import com.certificatemanagementsystem.Model.*;
import com.certificatemanagementsystem.Repository.*;
import jakarta.transaction.Transactional;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.JstlUtils;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
public class StudentService implements  StudentServiceInterface{
    // TODO: Implement student service methods
    @Autowired
    StudentRepository studentRepository;

    @Autowired
    ProgrammeRepository programmeRepository;

    @Autowired
    DepartmentRepository departmentRepository;

    @Autowired
    FacultyRepository facultyRepository;

    @Autowired
    AcademicYearRepository academicYearRepository;

    public Student addStudent(int studentId, String studentName, int facultyId, int departmentId, int programmeId, int yearId) {
        Student student = new Student();
        student.setStudentId(studentId);
        student.setStudentName(studentName);

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

        Optional<Programme> programmeOpt = programmeRepository.getProgrammeById(programmeId);
        if (!programmeOpt.isPresent()) {
            throw new IllegalArgumentException("Invalid programme Id: " + programmeId);
        }
        Programme programme = programmeOpt.get();

        Optional<AcademicYear> academicYearOpt = academicYearRepository.getAcademicYearByYearId(yearId);
        if (!academicYearOpt.isPresent()) {
            throw new IllegalArgumentException("Invalid academic year Id: " + yearId);
        }
        AcademicYear academicYear = academicYearOpt.get();

        student.setFacultyId(faculty);
        student.setDepartmentId(department);
        student.setProgrammeId(programme);
        student.setAcademicYearId(academicYear);
        student.setDateAdded(LocalDateTime.now());
        student.setDateEdited(LocalDateTime.now());

        studentRepository.save(student);
        return student;
    }

    @Override
    @Transactional
    public void importStudent(InputStream is) {
        List<Student> students = parseExcelFile(is);
        studentRepository.saveAll(students);
    }

    public List<Student> allStudents() {
        List<Student> students = (List<Student>) studentRepository.findAll();
        return  students;
    }

    public  Student deleteStudent(int studentId) {
        Student student = studentRepository.findById(studentId);
        studentRepository.delete(student);
        return student;
    }


    public Optional<Student> getStudentById(int studentId) {
        return studentRepository.getStudentById(studentId);
    }


    private List<Student> parseExcelFile(InputStream is) {
        try {
            Workbook workbook = new XSSFWorkbook(is);
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();
            List<Student> students = new ArrayList<>();

            int rowNumber = 0;
            while (rows.hasNext()) {
                Row currentRow = rows.next();

                // Skip header
                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }

                Iterator<Cell> cellsInRow = currentRow.iterator();
                Student student     = new Student();

                int cellIdx = 0;
                while (cellsInRow.hasNext()) {
                    Cell currentCell = cellsInRow.next();

                    switch (cellIdx) {
                        case 0:
                            student.setStudentId((int) currentCell.getNumericCellValue());
                            break;
                        case 1:
                            student.setStudentName(currentCell.getStringCellValue());
                            break;
                        case 2:
                            int facultyId = (int) currentCell.getNumericCellValue();
                            Optional<Faculty> facultyOptional = facultyRepository.getFacultyById(facultyId);
                            if (facultyOptional.isPresent()) {
                                student.setFacultyId(facultyOptional.get());
                            }
                            else {
                                throw new RuntimeException("Faculty not found: " + facultyId);
                            }
                            break;
                        case 3:
                            int departmentId = (int) currentCell.getNumericCellValue();
                            Optional<Department> departmentOptional = departmentRepository.getDepartmentById(departmentId);
                            if (departmentOptional.isPresent()) {
                                student.setDepartmentId(departmentOptional.get());
                            }
                            else {
                                throw new RuntimeException("Could not find department: " + departmentId);
                            }
                            break;
                        case 4:
                            int programmeId = (int) currentCell.getNumericCellValue();
                            Optional<Programme> programmeOptional = programmeRepository.getProgrammeById(programmeId);
                            if (programmeOptional.isPresent()) {
                                student.setProgrammeId(programmeOptional.get());
                            }
                            else {
                                throw new RuntimeException("Could not find department: " + programmeId);
                            }
                            break;
                        case 5:
                            int academicYearId = (int) currentCell.getNumericCellValue();
                            Optional<AcademicYear> academicYearOptional = academicYearRepository.getAcademicYearByYearId(academicYearId);
                            if (academicYearOptional.isPresent()) {
                                student.setAcademicYearId(academicYearOptional.get());
                            }
                            else {
                                throw new RuntimeException("Could not find department: " + academicYearId);
                            }
                            break;
                        default:
                            break;
                    }

                    cellIdx++;
                }

                students.add(student);
            }

            workbook.close();

            return students;
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse Excel file: " + e.getMessage());
        }
    }


}


