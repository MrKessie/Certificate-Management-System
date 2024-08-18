package com.cms.Service;

import com.cms.Model.*;
import com.cms.Repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

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
    public Student addStudent(int studentId, String surname, String otherNames, Faculty faculty, Department department, Programme programme,
                              AcademicYear academicYear) {
        Student student = new Student();
        student.setStudentId(studentId);
        student.setSurname(surname);
        student.setOtherNames(otherNames);
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
