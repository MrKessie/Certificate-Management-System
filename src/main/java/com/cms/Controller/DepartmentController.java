package com.cms.Controller;

import com.cms.Model.Department;
import com.cms.Model.Faculty;
import com.cms.Model.User;
import com.cms.Service.DepartmentImportResult;
import com.cms.Service.DepartmentService;
import com.cms.Service.FacultyService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Controller
@RequestMapping("/department")
public class DepartmentController {

    @Autowired
    DepartmentService departmentService;

    @Autowired
    FacultyService facultyService;

    @GetMapping("/department-all")
    public String showDepartmentAllPage(HttpSession session, Model model) {
        model.addAttribute("departments", departmentService.allDepartments());
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        model.addAttribute("loggedInUser", loggedInUser);
        return "department-all";
    }

    @GetMapping("/department-add")
    public String showDepartmentAddPage(HttpSession session, Model model) {
        model.addAttribute("faculties", departmentService.getAllFaculties());
        model.addAttribute("departments", departmentService.allDepartments());
        model.addAttribute("department", new Department());
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        model.addAttribute("loggedInUser", loggedInUser);
        return "department-add";
    }

    @GetMapping("/department-edit")
    public String showDepartmentEditPage(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        model.addAttribute("loggedInUser", loggedInUser);
        return "department-edit";
    }


    @PostMapping("/add")
    public ResponseEntity<String> addDepartment(@RequestParam int departmentId, @RequestParam String departmentName,
                                @RequestParam Faculty faculty, Model model) {
        if (departmentService.existsByDepartmentId(departmentId)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Department ID already exists.");
        }

        Department department = departmentService.addDepartment(departmentId, departmentName, faculty);

        if (department == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred.");
        }

        return ResponseEntity.ok("Department added successfully!");

    }


    //=============METHOD TO IMPORT  DEPARTMENT=============//
    @PostMapping("/import-departments")
    public ResponseEntity<?> importDepartments(@RequestParam("file") MultipartFile file) throws IOException {
        DepartmentImportResult result = departmentService.importDepartment(file);

        Map<String, Object> response = new HashMap<>();
        response.put("addedCount", result.getAddedDepartments().size());
        response.put("notAddedDepartments", result.getNotAddedDepartments());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    @ResponseBody
    public List<Department> departmentList() {
        return departmentService.allDepartments();
    }

    @DeleteMapping("/delete/{departmentId}")
    public ResponseEntity<String> deleteDepartment(@PathVariable int departmentId) {
        if (departmentService.existsByDepartmentId(departmentId)) {
            departmentService.deleteDepartment(departmentId);
            return ResponseEntity.ok("Department deleted successfully!");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Department not found.");
        }
    }

//    public String getFacultyName(int facultyId) {
//        Optional<Faculty> facultyOptional = facultyService.getFacultyById(facultyId);
//        return facultyOptional.map(Faculty::getFacultyName).orElse("Unknown");
//    }

    @GetMapping("/facultyName/{facultyId}")
    @ResponseBody
    public String getFacultyName(@PathVariable int facultyId) {
        Optional<Faculty> faculty = facultyService.getFacultyById(facultyId);
        return faculty.get().getFacultyName();
    }



    @GetMapping("/all/departments")
    @ResponseBody
    public List<Department> getDepartment() {
        List<Department> departments = departmentService.allDepartments();
        // Create a new list of simplified faculty objects with only id and name
        List<Department> newDepartment = new ArrayList<>();
        for (Department department : departments) {
            Department simplifiedDepartment = new Department();
            simplifiedDepartment.setDepartmentId(department.getDepartmentId());
            simplifiedDepartment.setDepartmentName(department.getDepartmentName());
            newDepartment.add(simplifiedDepartment);
        }
        return newDepartment;
    }


}
