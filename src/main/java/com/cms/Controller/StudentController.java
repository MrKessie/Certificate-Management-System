package com.cms.Controller;

import com.cms.Model.*;
import com.cms.Repository.StudentRepository;
import com.cms.Service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/student")
public class StudentController {

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

    @Autowired
    StudentRepository studentRepository;


    @GetMapping("/student-all")
    public String showStudentAllPage(HttpSession session, Model model) {
        model.addAttribute("students", studentService.allStudents());
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        model.addAttribute("loggedInUser", loggedInUser);
        return "student-all";
    }

    @GetMapping("/student-add")
    public String showStudentAAddPage(HttpSession session, Model model) {
        model.addAttribute("students", studentService.allStudents());
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        model.addAttribute("loggedInUser", loggedInUser);
        return "student-add";
    }

    @GetMapping("/student-edit")
    public String showStudentEditPage(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        model.addAttribute("loggedInUser", loggedInUser);
        return "student-edit";
    }


    @PostMapping("/add")
    public ResponseEntity<String> addStudent(@RequestParam int studentId, @RequestParam String studentName, @RequestParam Faculty faculty,
                             @RequestParam Department department, @RequestParam Programme programme, @RequestParam AcademicYear academicYear) {
        if (studentService.existsByStudentId(studentId)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Student ID already exists.");
        }

        Student student = studentService.addStudent(studentId, studentName, faculty, department, programme, academicYear);

        if (student == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred.");
        }

        return ResponseEntity.ok("Student added successfully!");
    }


    //=============METHOD TO IMPORT  DEPARTMENT=============//
    @PostMapping("/import-students")
    public ResponseEntity<?> importStudents(@RequestParam("file") MultipartFile file) throws IOException {
        StudentImportResult result = studentService.importStudent(file);

        Map<String, Object> response = new HashMap<>();
        response.put("addedCount", result.getAddedStudent().size());
        response.put("notAddedStudents", result.getNotAddedStudent());

        return ResponseEntity.ok(response);
    }



    @GetMapping("/all")
    @ResponseBody
    public List<Student> studentListList() {
        return studentService.allStudents();
    }


    @DeleteMapping("/delete/{studentId}")
    public ResponseEntity<String> deleteStudent(@PathVariable int studentId) {
        if (studentService.existsByStudentId(studentId)) {
            studentService.deleteStudent(studentId);
            return ResponseEntity.ok("Student deleted successfully!");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Student not found.");
        }
    }



    public String getFacultyName(int facultyId) {
        Optional<Faculty> facultyOptional = facultyService.getFacultyById(facultyId);
        return facultyOptional.map(Faculty::getFacultyName).orElse("Unknown");
    }

    public String getDepartmentName(int departmentId) {
        Optional<Department> departmentOptional = departmentService.getDepartmentById(departmentId);
        return departmentOptional.map(Department::getDepartmentName).orElse("Unknown");
    }

    public String getProgrammeName(int programmeId) {
        Optional<Programme> programmeOptional = programmeService.getProgrammeById(programmeId);
        return programmeOptional.map(Programme::getProgrammeName).orElse("Unknown");
    }

    public String getAcademicYear(int academicYearId) {
        Optional<AcademicYear> academicYearIdOptional = academicYearService.getAcademicYearById(academicYearId);
        return academicYearIdOptional.map(AcademicYear::getAcademicYear).orElse("Unknown");
    }
}
