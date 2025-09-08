package com.cms.Controller;


import com.cms.Model.Faculty;
import com.cms.Model.User;
import com.cms.Repository.FacultyRepository;
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
@RequestMapping("/certificate-management-system/faculty")
public class FacultyController {

    @Autowired
    FacultyService facultyService;

    @Autowired
    FacultyRepository facultyRepository;

    @Autowired
    UserService userService;

    @Autowired
    UserActivityService userActivityService;



    //=============METHOD TO SHOW FACULTY ADD PAGE=============//
    @GetMapping
    public String showAddFacultyPage(HttpSession session, Model model) {
        model.addAttribute("faculties", facultyService.allFacultyList());
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        model.addAttribute("loggedInUser", loggedInUser);
        return "faculty";
    }


    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userService.findById(Integer.parseInt(username));
    }



    //=============METHOD TO ADD FACULTY=============//
    @PostMapping("/add")
    public ResponseEntity<String> addFaculty(@RequestParam int facultyId, @RequestParam String facultyName) {
        User currentUser = getCurrentUser();
        try {
            if (facultyService.existByFacultyId(facultyId)) {
                userActivityService.logActivity(currentUser, "ADD_FACULTY_FAILED",
                        "Attempted to add existing faculty: " + facultyName);
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Faculty ID already exists.");
            }

            Faculty faculty = facultyService.addFaculty(facultyId, facultyName);

            if (faculty == null) {
                userActivityService.logActivity(currentUser, "ADD_FACULTY_ERROR",
                        "Error occurred while adding faculty: " + facultyName);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred.");
            }

            userActivityService.logActivity(currentUser, "ADD_FACULTY_SUCCESS",
                    "Added new faculty: " + facultyName);
            return ResponseEntity.ok("Faculty added successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
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
    public ResponseEntity<?> importFaculties(@RequestParam("file") MultipartFile file) throws IOException {
        User currentUser = getCurrentUser();
        Map<String, Object> response = new HashMap<>();

        try{
        FacultyImportResult result = facultyService.importFaculty(file);
        userActivityService.logActivity(currentUser, "IMPORT_FACULTY_START",
                "Started importing faculties from file: " + file.getOriginalFilename());

        response.put("addedCount", result.getAddedFaculties().size());
        response.put("notAddedFaculties", result.getNotAddedFaculties());
            userActivityService.logActivity(currentUser, "IMPORT_FACULTY_SUCCESS",
                    String.format("Successfully imported faculties. Added: %d, Not added: %d",
                            result.getAddedFaculties().size(),
                            result.getNotAddedFaculties().size()));

        return ResponseEntity.ok(response);
        } catch (IOException e) {
            userActivityService.logActivity(currentUser, "IMPORT_FACULTY_ERROR",
                    "Error occurred while importing faculties: " + e.getMessage());

            response.put("error", "An error occurred while importing faculties: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
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
        User currentUser = getCurrentUser();
        if (facultyService.existByFacultyId(facultyId)) {
            facultyService.deleteFaculty(facultyId);

            userActivityService.logActivity(currentUser, "DELETE_FACULTY_SUCCESS",
                    "Deleted faculty with ID: " + facultyId);
            return ResponseEntity.ok("Faculty deleted successfully!");
        } else {
            userActivityService.logActivity(currentUser, "DELETE_FACULTY_FAILED",
                    "Attempted to delete non-existent faculty ID: " + facultyId);
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


    @PutMapping("/update")
    public ResponseEntity<?> updateFaculty(@RequestBody Faculty faculty) {
        User currentUser = getCurrentUser();
        try {
            Faculty updatedFaculty = facultyService.updateFaculty(faculty);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Faculty updated successfully");
            response.put("data", updatedFaculty);
            userActivityService.logActivity(currentUser, "UPDATE_FACULTY_SUCCESS",
                    "Updated faculty: " + updatedFaculty.getFacultyName());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error updating faculty: " + e.getMessage());
            userActivityService.logActivity(currentUser, "UPDATE_FACULTY_ERROR",
                    "Error updating faculty: " + faculty.getFacultyName() + ". Error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

}
