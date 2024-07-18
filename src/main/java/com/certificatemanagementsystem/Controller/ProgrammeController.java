package com.certificatemanagementsystem.Controller;

//import ch.qos.logback.core.model.Model;
import com.certificatemanagementsystem.Model.Department;
import com.certificatemanagementsystem.Model.Faculty;
import com.certificatemanagementsystem.Model.Programme;
import com.certificatemanagementsystem.Service.DepartmentService;
import com.certificatemanagementsystem.Service.FacultyService;
import com.certificatemanagementsystem.Service.ProgrammeService;
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
@RequestMapping("/programme")
public class ProgrammeController {

    @Autowired
    ProgrammeService programmeService;

    @Autowired
    FacultyService facultyService;

    @Autowired
    DepartmentService departmentService;

    @GetMapping
    public String showProgrammePage(Model model) {
        List<Programme> programmes = programmeService.programmeList();
        model.addAttribute("programmes", programmes);
        return "programme";
    }

    @PostMapping("/add")
    public String addProgramme(@RequestParam int programmeId, @RequestParam String programmeName, @RequestParam int facultyId,
                               @RequestParam int departmentId, Model model) {
        Programme programme = programmeService.addProgramme(programmeId, programmeName, facultyId, departmentId);

        List<Programme> programmes = programmeService.programmeList();
        model.addAttribute("programmes", programmes);
        return "redirect:/programme";
    }

    @PostMapping("/import-programmes")
    public String importDepartments(@RequestParam("programmeFile") MultipartFile programmeFile     ) {
        try {
            programmeService.importProgramme(programmeFile.getInputStream());
            return "redirect:/programme";
//            return new ResponseEntity<>("Departments imported successfully", HttpStatus.OK);
        } catch (IOException e) {
            return "redirect:/programme";
//            return new ResponseEntity<>("Failed to import departments: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/all")
    public String programmeList(Model model) {
        List<Programme> programmes = programmeService.programmeList();
        model.addAttribute("programmes", programmes);
        return "redirect:/programme";
    }

    @GetMapping("/all/programmes")
    @ResponseBody
    public List<Programme> getProgrammes() {
        List<Programme> programmes = programmeService.programmeList();
        // Create a new list of simplified faculty objects with only id and name
        List<Programme> newProgrammes = new ArrayList<>();
        for (Programme programme : programmes) {
            Programme simplifiedProgramme = new Programme();
            simplifiedProgramme.setId(programme.getId());
            simplifiedProgramme.setProgrammeName(programme.getProgrammeName());
            newProgrammes.add(simplifiedProgramme);
        }
        return newProgrammes;
    }

    @PostMapping("/delete")
    public Programme deleteProgramme(@RequestParam int programmeId) {
        Programme programme = programmeService.deleteProgramme(programmeId);
        return programme;
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
}
