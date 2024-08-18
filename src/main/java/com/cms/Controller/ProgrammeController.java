package com.cms.Controller;

import com.cms.Model.Department;
import com.cms.Model.Faculty;
import com.cms.Model.Programme;
import com.cms.Model.User;
import com.cms.Service.DepartmentService;
import com.cms.Service.FacultyService;
import com.cms.Service.ProgrammeService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/programme")
public class ProgrammeController {

    @Autowired
    ProgrammeService programmeService;

    @Autowired
    FacultyService facultyService;

    @Autowired
    DepartmentService departmentService;

    @GetMapping("/programme-all")
    public String showProgrammeAllPage(HttpSession session, Model model) {
        model.addAttribute("programmes", programmeService.allProgrammes());
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        model.addAttribute("loggedInUser", loggedInUser);
        return "programme-all";
    }

    @GetMapping("/programme-add")
    public String showProgrammeAddPage(HttpSession session, Model model) {
        model.addAttribute("programmes", programmeService.allProgrammes());
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        model.addAttribute("loggedInUser", loggedInUser);
        return "programme-add";
    }

    @GetMapping("/programme-edit")
    public String showProgrammeEditPage(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        model.addAttribute("loggedInUser", loggedInUser);
        return "programme-edit";
    }


    @PostMapping("/add")
    public String addProgramme(@RequestParam int programmeId, @RequestParam String programmeName, @RequestParam Faculty faculty,
                               @RequestParam Department department, Model model) {
        Programme programme = programmeService.addProgramme(programmeId, programmeName, faculty, department);
        model.addAttribute("programme", new Programme());
        return "redirect:/programme/programme-add";
    }

    @GetMapping("/all")
    @ResponseBody
    public List<Programme> programmeList(Model model) {
        List<Programme> programmes = programmeService.allProgrammes();
        return programmes;
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteDepartment(@PathVariable int id) {
        boolean isRemoved = programmeService.deleteProgramme(id);
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

    @GetMapping("/all/programmes")
    @ResponseBody
    public List<Programme> getProgramme() {
        List<Programme> programmes = programmeService.allProgrammes();
        // Create a new list of simplified faculty objects with only id and name
        List<Programme> newProgramme = new ArrayList<>();
        for (Programme programme : programmes) {
            Programme simplifiedProgramme = new Programme();
            simplifiedProgramme.setProgrammeId(programme.getProgrammeId());
            simplifiedProgramme.setProgrammeName(programme.getProgrammeName());
            newProgramme.add(simplifiedProgramme);
        }
        return newProgramme;
    }
}
