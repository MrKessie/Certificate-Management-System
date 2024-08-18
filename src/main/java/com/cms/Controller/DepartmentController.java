package com.cms.Controller;

import com.cms.Model.Department;
import com.cms.Model.Faculty;
import com.cms.Model.User;
import com.cms.Service.DepartmentService;
import com.cms.Service.FacultyService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
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
    public String addDepartment(@RequestParam int departmentId, @RequestParam String departmentName,
                                @RequestParam Faculty faculty, Model model) {
//        Faculty facultyId = facultyService.getById(faculty);
        departmentService.addDepartment(departmentId, departmentName, faculty);
        model.addAttribute("department", new Department());
        return "redirect:/department/department-add";
    }


    //=============METHOD TO IMPORT  FACULTY=============//
    @PostMapping("/import-departments")
    public String importDepartments(@RequestParam("file") MultipartFile file) throws IOException {
        departmentService.importDepartment(file);
        return "redirect:/department/department-add";
    }

    @GetMapping("/all")
    @ResponseBody
    public List<Department> departmentList(Model model) {
        return departmentService.allDepartments();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteDepartment(@PathVariable int id) {
        boolean isRemoved = departmentService.deleteDepartment(id);
        if (!isRemoved) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
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
