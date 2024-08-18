package com.cms.Controller;


import com.cms.Model.Faculty;
import com.cms.Model.User;
import com.cms.Repository.FacultyRepository;
import com.cms.Service.FacultyService;
import com.cms.UpdateRequest.FacultyUpdateRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/faculty")
public class FacultyController {

    @Autowired
    FacultyService facultyService;

    @Autowired
    FacultyRepository facultyRepository;

    //=============METHOD TO SHOW FACULTY ALL PAGE=============//
    @GetMapping("/faculty-all")
    public String showAllFacultyPage(HttpSession session, Model model) {
        model.addAttribute("faculties", facultyService.allFacultyList());
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        model.addAttribute("loggedInUser", loggedInUser);
        return "faculty-all";
    }



    //=============METHOD TO SHOW FACULTY ADD PAGE=============//
    @GetMapping("/faculty-add")
    public String showAddFacultyPage(HttpSession session, Model model) {
        model.addAttribute("faculties", facultyService.allFacultyList());
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        model.addAttribute("loggedInUser", loggedInUser);
        return "faculty-add";
    }



    //=============METHOD TO SHOW FACULTY EDIT PAGE=============//
    @GetMapping("/faculty-edit")
    public String showEditFacultyPage(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        model.addAttribute("loggedInUser", loggedInUser);
        return "faculty-edit";
    }



    //=============METHOD TO ADD FACULTY=============//
    @PostMapping("/add")
    public ResponseEntity<String> addFaculty(@RequestParam int facultyId, @RequestParam String facultyName, Model model, RedirectAttributes redirectAttributes) {
        if (facultyService.existByFacultyId(facultyId)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Faculty ID already exists.");
        }

        Faculty faculty = facultyService.addFaculty(facultyId,facultyName);

        if (faculty == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred.");
        }

        return ResponseEntity.ok("Faculty added successfully!");
    }



    @PostMapping("/checkFacultyExists")
    public ResponseEntity<Map<String, Boolean>> checkFacultyExists(@RequestBody Map<String, String> request) {
        int facultyId = Integer.parseInt(request.get("facultyId"));
        String facultyName = request.get("facultyName");

        Optional<Faculty> existingFaculty = facultyRepository.findByFacultyIdOrFacultyName(facultyId, facultyName);

        // Return JSON with "exists" key set to true if the faculty exists, false otherwise
        Map<String, Boolean> response = Map.of("exists", existingFaculty.isPresent());
        return ResponseEntity.ok(response);
    }



    //=============METHOD TO IMPORT  FACULTY=============//
    @PostMapping("/import-faculties")
    public String importFaculties(@RequestParam("file") MultipartFile file) throws IOException {
        facultyService.importFaculty(file);
        return "redirect:/faculty/faculty-add";
    }



    //=============METHOD TO SHOW ALL FACULTIES=============//
    @GetMapping("/all")
    @ResponseBody
    public List<Faculty> facultyList() {
        return facultyService.allFacultyList();
    }



    //=============METHOD TO DELETE ALL FACULTY BY ID=============//
    @DeleteMapping("/delete/{facultyId}")
    public ResponseEntity<String> deleteFaculty(@PathVariable int facultyId) {
        if (facultyService.existByFacultyId(facultyId)) {
            facultyService.deleteFaculty(facultyId);
            return ResponseEntity.ok("Faculty deleted successfully!");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Faculty not found.");
        }
    }

    @GetMapping("/edit/{facultyId}")
    public ResponseEntity<Faculty> getFacultyById(@PathVariable("facultyId") int facultyId) {
        Faculty faculty = facultyService.findById(facultyId);
        if (faculty != null) {
            return ResponseEntity.ok(faculty);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }


    @PutMapping("/update/{facultyId}")
    public ResponseEntity<String> updateFaculty(@PathVariable("facultyId") int facultyId, @RequestBody FacultyUpdateRequest request) {
        if (facultyService.existByFacultyId(facultyId)) {
            facultyService.updateFaculty(facultyId, request.getFacultyName());
            return ResponseEntity.ok("Faculty updated successfully!");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Faculty not found.");
        }
    }

    //=============METHOD TO GET ALL FACULTIES=============//
    @GetMapping("/all/faculties")
    @ResponseBody
    public List<Faculty> getFaculties() {
        List<Faculty> faculties = facultyService.allFacultyList();
        // Create a new list of simplified faculty objects with only id and name
        List<Faculty> newFaculties = new ArrayList<>();
        for (Faculty faculty : faculties) {
            Faculty simplifiedFaculty = new Faculty();
            simplifiedFaculty.setFacultyId(faculty.getFacultyId());
            simplifiedFaculty.setFacultyName(faculty.getFacultyName());
            newFaculties.add(simplifiedFaculty);
        }
        return newFaculties;
    }

}
