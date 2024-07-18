package com.certificatemanagementsystem.Controller;

import com.certificatemanagementsystem.Model.Department;
import com.certificatemanagementsystem.Model.Faculty;
import com.certificatemanagementsystem.Service.DepartmentService;
import com.certificatemanagementsystem.Service.FacultyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/department")
public class DepartmentController {

    @Autowired
    DepartmentService departmentService;

    @Autowired
    FacultyService facultyService; // Assuming FacultyService is injected her

    @GetMapping
    public String showDepartmentPage(Model model) {
        List<Department> departments = departmentService.allDepartments();
        model.addAttribute("departments", departments);
        return "department";
    }

    @PostMapping("/add")
    public String addDepartment(@RequestParam int departmentId, @RequestParam String departmentName,
                                @RequestParam Faculty facultyId, Model model) {
        Department department = departmentService.addDepartment(departmentId, departmentName, facultyId);
        model.addAttribute("department", new Department());

        List<Department> departments = departmentService.allDepartments();
        model.addAttribute("departments", departments);
        return "redirect:/department";
    }

    @PostMapping("/import-departments")
    public String importDepartments(@RequestParam("departmentFile") MultipartFile departmentFile) {
        try {
            departmentService.importDepartment(departmentFile.getInputStream());
            return "redirect:/department";
//            return new ResponseEntity<>("Departments imported successfully", HttpStatus.OK);
        } catch (IOException e) {
            return "redirect:/department";
//            return new ResponseEntity<>("Failed to import departments: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/all")
    public String departmentList(Model model) {
        List<Department> departments = departmentService.allDepartments();
        model.addAttribute("departments", departments);
        return "redirect:/department";
    }

    @GetMapping("/all/departments")
    @ResponseBody
    public List<Department> getDepartments() {
        List<Department> departments = departmentService.allDepartments();
        // Create a new list of simplified faculty objects with only id and name
        List<Department> newDepartments = new ArrayList<>();
        for (Department department : departments) {
            Department simplifiedDepartment = new Department();
            simplifiedDepartment.setId(department.getId());
            simplifiedDepartment.setDepartmentName(department.getDepartmentName());
            newDepartments.add(simplifiedDepartment);
        }
        return newDepartments;
    }

    // Utility method to fetch faculty name
    public String getFacultyName(int facultyId) {
        Optional<Faculty> facultyOptional = facultyService.getFacultyById(facultyId);
        return facultyOptional.map(Faculty::getFacultyName).orElse("Unknown");
    }

    @PostMapping("/delete")
    public Department deleteDepartment(@RequestParam int departmentId) {
        Department department = departmentService.deleteDepartment(departmentId);
        return  department;
    }
}
