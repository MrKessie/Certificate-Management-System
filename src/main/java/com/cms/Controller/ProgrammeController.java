package com.cms.Controller;

import com.cms.Model.Department;
import com.cms.Model.Faculty;
import com.cms.Model.Programme;
import com.cms.Model.User;
import com.cms.Service.*;
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
    public ResponseEntity<String> addProgramme(@RequestParam int programmeId, @RequestParam String programmeName,
                                               @RequestParam Faculty faculty, @RequestParam Department department) {
        if (programmeService.existsByProgrammeId(programmeId)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Programme ID already exists.");
        }

        Programme programme = programmeService.addProgramme(programmeId, programmeName, faculty, department);

        if (programme == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred.");
        }

        return ResponseEntity.ok("Programme added successfully!");
    }

    @GetMapping("/all")
    @ResponseBody
    public List<Programme> programmeList(Model model) {
        return programmeService.allProgrammes();
    }

    @DeleteMapping("/delete/{programmeId}")
    public ResponseEntity<String> deleteDepartment(@PathVariable int programmeId) {
        if (programmeService.existsByProgrammeId(programmeId)) {
            programmeService.deleteProgramme(programmeId);
            return ResponseEntity.ok("Programme deleted successfully!");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Programme not found.");
        }
    }


    //=============METHOD TO IMPORT  DEPARTMENT=============//
    @PostMapping("/import-programmes")
    public ResponseEntity<?> importProgrammes(@RequestParam("file") MultipartFile file) throws IOException {
        ProgrammeImportResult result = programmeService.importProgramme(file);

        Map<String, Object> response = new HashMap<>();
        response.put("addedCount", result.getAddedProgrammes().size());
        response.put("notAddedProgrammes", result.getNotAddedProgrammes());

        return ResponseEntity.ok(response);
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
