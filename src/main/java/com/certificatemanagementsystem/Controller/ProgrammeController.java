package com.certificatemanagementsystem.Controller;

import com.certificatemanagementsystem.Model.Department;
import com.certificatemanagementsystem.Model.Faculty;
import com.certificatemanagementsystem.Model.Programme;
import com.certificatemanagementsystem.Service.ProgrammeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/programme")
public class ProgrammeController {

    @Autowired
    ProgrammeService programmeService;

    @GetMapping
    public String showProgrammePage() {
        return "programme";
    }

    @PostMapping("/add")
    public Programme addProgramme(int programmeId, String programmeName, Faculty facultyId, Department departmentId) {
        Programme programme = programmeService.addProgramme(programmeId, programmeName, facultyId, departmentId);
        return programme;
    }

    @GetMapping("/all")
    public String programmeList(Model model) {
        List<Programme> programmes = programmeService.programmeList();
        model.addAttribute("programmes", programmes);
        return "programme";
    }

    @PostMapping("/delete")
    public Programme deleteProgramme(int programmeId) {
        Programme programme = programmeService.deleteProgramme(programmeId);
        return programme;
    }
}
