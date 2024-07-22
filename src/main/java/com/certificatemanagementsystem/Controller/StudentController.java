package com.certificatemanagementsystem.Controller;

import com.certificatemanagementsystem.Model.*;
import com.certificatemanagementsystem.Repository.*;
import com.certificatemanagementsystem.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/student")
public class StudentController {
    // Controller for student-specific functionalities
    // e.g., view-student-profile, edit-student-profile, etc.

    @Autowired
    StudentService studentService;

    @Autowired
    ProgrammeService programmeService;

    @Autowired
    FacultyService facultyService;

    @Autowired
    DepartmentService departmentService;

    @Autowired
    AcademicYearService academicYearService;

    @GetMapping
    public String showStudentPage(Model model) {
        List<Student> students = studentService.allStudents();
        model.addAttribute("students", students);
        return "student";
    }

    @PostMapping("add")
    public String addStudent(@RequestParam int studentId, @RequestParam String studentName, @RequestParam int facultyId,
                             @RequestParam int departmentId, @RequestParam int programmeId, @RequestParam int yearId, Model model) {
        Student student = studentService.addStudent(studentId, studentName,facultyId, departmentId,programmeId, yearId);

        List<Student> students = studentService.allStudents();
        model.addAttribute("students", students);

        return "redirect:/student";
    }

    @PostMapping("/import-students")
    public String importStudents(@RequestParam("studentsFile") MultipartFile studentFile) {
        try {
            studentService.importStudent(studentFile.getInputStream());
            return "redirect:/student";
//            return new ResponseEntity<>("Departments imported successfully", HttpStatus.OK);
        } catch (IOException e) {
            return "redirect:/student";
//            return new ResponseEntity<>("Failed to import departments: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/all")
    public String studentList(Model model) {
        List<Student> students = studentService.allStudents();
        model.addAttribute("students", students);
        return "redirect:/student";
    }

    @GetMapping("/all/students")
    @ResponseBody
    public List<Student> getStudents() {
        List<Student> students = studentService.allStudents();
        // Create a new list of simplified faculty objects with only id and name
        List<Student> newStudents = new ArrayList<>();
        for (Student student : students) {
            Student simplifiedStudent = new Student();
            simplifiedStudent.setId(student.getId());
            simplifiedStudent.setStudentName(student.getStudentName());
            newStudents.add(simplifiedStudent);
        }
        return newStudents;
    }

    @PostMapping("/delete")
    public Student deleteStudent(@RequestParam int studentId) {
        Student student = studentService.deleteStudent(studentId);
        return student;
    }

    // Utility method to fetch faculty name
    public String getFacultyName(int facultyId) {
        Optional<Faculty> facultyOptional = facultyService.getFacultyById(facultyId);
        return facultyOptional.map(Faculty::getFacultyName).orElse("Unknown");
    }

    // Utility method to fetch department name
    public String getDepartmentName(int departmentId) {
        Optional<Department> departmentOptional = departmentService.getDepartmentById(departmentId);
        return departmentOptional.map(Department::getDepartmentName).orElse("Unknown");
    }

    // Utility method to fetch programme name
    public String getProgrammeName(int programmeId) {
        Optional<Programme> programmeOptional = programmeService.getProgrammeById(programmeId);
        return programmeOptional.map(Programme::getProgrammeName).orElse("Unknown");
    }

    // Utility method to fetch academic year name
    public String getAcademicYear(int academicYearId) {
        Optional<AcademicYear> academicYearOptional = academicYearService.getAcademicYearById(academicYearId);
        return academicYearOptional.map(AcademicYear::getYear).orElse("Unknown");
    }
}
