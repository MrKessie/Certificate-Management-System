package com.cms.Controller;

import com.cms.Model.*;
import com.cms.Service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    public String addStudent(@RequestParam int studentId, @RequestParam String surname, @RequestParam String otherNames,
                             @RequestParam Faculty faculty, @RequestParam Department department, @RequestParam Programme programme,
                             @RequestParam AcademicYear academicYear, Model model) {
        Student student = studentService.addStudent(studentId, surname, otherNames, faculty, department, programme, academicYear);
        model.addAttribute("student", new Student());
        return "redirect:/student/student-add";
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable int id) {
        boolean isRemoved = studentService.deleteStudent(id);
        if (!isRemoved) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
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
