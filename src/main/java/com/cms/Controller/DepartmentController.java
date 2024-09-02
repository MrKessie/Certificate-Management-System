package com.cms.Controller;

import com.cms.Model.Department;
import com.cms.Model.Faculty;
import com.cms.Model.User;
import com.cms.Service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @Autowired
    UserService userService;

    @Autowired
    UserActivityService userActivityService;

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

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userService.findById(Integer.parseInt(username));
    }


    @PostMapping("/add")
    public ResponseEntity<String> addDepartment(@RequestParam int departmentId, @RequestParam String departmentName,
                                @RequestParam Faculty faculty, Model model) {
        User currentUser = getCurrentUser();
        if (departmentService.existsByDepartmentId(departmentId)) {
            userActivityService.logActivity(currentUser, "ADD_DEPARTMENT_FAILED",
                    "Attempted to add existing department: " + departmentName);
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Department ID already exists.");
        }

        Department department = departmentService.addDepartment(departmentId, departmentName, faculty);

        if (department == null) {
            userActivityService.logActivity(currentUser, "ADD_DEPARTMENT_ERROR",
                    "Error occurred while adding department: " + departmentName);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred.");
        }

        userActivityService.logActivity(currentUser, "ADD_DEPARTMENT_SUCCESS",
                "Added new department: " + departmentName);
        return ResponseEntity.ok("Department added successfully!");

    }


    //=============METHOD TO IMPORT  DEPARTMENT=============//
    @PostMapping("/import-departments")
    public ResponseEntity<?> importDepartments(@RequestParam("file") MultipartFile file) throws IOException {
        User currentUser = getCurrentUser();
        Map<String, Object> response = new HashMap<>();

        try{
        DepartmentImportResult result = departmentService.importDepartment(file);
            userActivityService.logActivity(currentUser, "IMPORT_DEPARTMENT_START",
                    "Started importing departments from file: " + file.getOriginalFilename());

        response.put("addedCount", result.getAddedDepartments().size());
        response.put("notAddedDepartments", result.getNotAddedDepartments());
            userActivityService.logActivity(currentUser, "IMPORT_DEPARTMENT_SUCCESS",
                    String.format("Successfully imported departments. Added: %d, Not added: %d",
                            result.getAddedDepartments().size(),
                            result.getNotAddedDepartments().size()));

        return ResponseEntity.ok(response);

        } catch (IOException e) {
            userActivityService.logActivity(currentUser, "IMPORT_FACULTY_ERROR",
                    "Error occurred while importing faculties: " + e.getMessage());

            response.put("error", "An error occurred while importing faculties: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/all")
    @ResponseBody
    public List<Department> departmentList() {
        return departmentService.allDepartments();
    }

    @DeleteMapping("/delete/{departmentId}")
    public ResponseEntity<String> deleteDepartment(@PathVariable int departmentId) {
        User currentUser = getCurrentUser();
        if (departmentService.existsByDepartmentId(departmentId)) {
            departmentService.deleteDepartment(departmentId);
            userActivityService.logActivity(currentUser, "DELETE_DEPARTMENT_SUCCESS",
                    "Deleted department with ID: " + departmentId);
            return ResponseEntity.ok("Department deleted successfully!");
        } else {
            userActivityService.logActivity(currentUser, "DELETE_DEPARTMENT_FAILED",
                    "Attempted to delete non-existent department ID: " + departmentId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Department not found.");
        }
    }

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


    @PutMapping("/update")
    public ResponseEntity<?> updateDepartment(@RequestBody Department department) {
        User currentUser = getCurrentUser();
        try {
            Department updatedDepartment = departmentService.updateDepartment(department);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Department updated successfully");
            response.put("data", updatedDepartment);
            userActivityService.logActivity(currentUser, "UPDATE_DEPARTMENT_SUCCESS",
                    "Updated department: " + updatedDepartment.getDepartmentName());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error updating Department: " + e.getMessage());
            userActivityService.logActivity(currentUser, "UPDATE_DEPARTMENT_ERROR",
                    "Error updating department: " + department.getDepartmentName() + ". Error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

}
