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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @Autowired
    UserService userService;

    @Autowired
    UserActivityService userActivityService;

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
        return "programme";
    }

    @GetMapping("/programme-edit")
    public String showProgrammeEditPage(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        model.addAttribute("loggedInUser", loggedInUser);
        return "programme-edit";
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userService.findById(Integer.parseInt(username));
    }


    @PostMapping("/add")
    public ResponseEntity<String> addProgramme(@RequestParam int programmeId, @RequestParam String programmeName,
                                               @RequestParam Faculty faculty, @RequestParam Department department) {
        User currentUser = getCurrentUser();
        if (programmeService.existsByProgrammeId(programmeId)) {
            userActivityService.logActivity(currentUser, "ADD_PROGRAMME_FAILED",
                    "Attempted to add existing programme: " + programmeName);
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Programme ID already exists.");
        }

        Programme programme = programmeService.addProgramme(programmeId, programmeName, faculty, department);

        if (programme == null) {
            userActivityService.logActivity(currentUser, "ADD_PROGRAMME_ERROR",
                    "Error occurred while adding programme: " + programmeName);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred.");
        }

        userActivityService.logActivity(currentUser, "ADD_PROGRAMME_SUCCESS",
                "Added new programme: " + programmeName);
        return ResponseEntity.ok("Programme added successfully!");
    }

    @GetMapping("/all")
    @ResponseBody
    public List<Programme> programmeList(Model model) {
        return programmeService.allProgrammes();
    }

    @DeleteMapping("/delete/{programmeId}")
    public ResponseEntity<String> deleteDepartment(@PathVariable int programmeId) {
        User currentUser = getCurrentUser();
        if (programmeService.existsByProgrammeId(programmeId)) {
            programmeService.deleteProgramme(programmeId);
            userActivityService.logActivity(currentUser, "DELETE_PROGRAMME_SUCCESS",
                    "Deleted programme with ID: " + programmeId);
            return ResponseEntity.ok("Programme deleted successfully!");
        } else {
            userActivityService.logActivity(currentUser, "DELETE_PROGRAMME_FAILED",
                    "Attempted to delete non-existent programme ID: " + programmeId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Programme not found.");
        }
    }


    //=============METHOD TO IMPORT  DEPARTMENT=============//
    @PostMapping("/import-programmes")
    public ResponseEntity<?> importProgrammes(@RequestParam("file") MultipartFile file) throws IOException {
        User currentUser = getCurrentUser();
        Map<String, Object> response = new HashMap<>();

        try{
        ProgrammeImportResult result = programmeService.importProgramme(file);
            userActivityService.logActivity(currentUser, "IMPORT_PROGRAMME_START",
                    "Started importing programmes from file: " + file.getOriginalFilename());

        response.put("addedCount", result.getAddedProgrammes().size());
        response.put("notAddedProgrammes", result.getNotAddedProgrammes());
            userActivityService.logActivity(currentUser, "IMPORT_PROGRAMME_SUCCESS",
                    String.format("Successfully imported programmes. Added: %d, Not added: %d",
                            result.getAddedProgrammes().size(),
                            result.getNotAddedProgrammes().size()));

        return ResponseEntity.ok(response);
        } catch (IOException e) {
            userActivityService.logActivity(currentUser, "IMPORT_PROGRAMME_ERROR",
                    "Error occurred while importing programmes: " + e.getMessage());

            response.put("error", "An error occurred while importing programmes: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
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


    @PutMapping("/update")
    public ResponseEntity<?> updateProgramme(@RequestBody Programme programme) {
        User currentUser = getCurrentUser();
        try {
            Programme updatedProgramme = programmeService.updateProgramme(programme);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Programme updated successfully");
            response.put("data", updatedProgramme);
            userActivityService.logActivity(currentUser, "UPDATE_PROGRAMME_SUCCESS",
                    "Updated programme: " + updatedProgramme.getProgrammeName());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error updating Programme: " + e.getMessage());
            userActivityService.logActivity(currentUser, "UPDATE_PROGRAMME_ERROR",
                    "Error updating programme: " + programme.getProgrammeName() + ". Error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
