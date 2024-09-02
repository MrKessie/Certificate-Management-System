package com.cms.Controller;

import com.cms.Model.AcademicYear;
import com.cms.Model.Faculty;
import com.cms.Model.User;
import com.cms.Repository.AcademicYearRepository;
import com.cms.Service.AcademicYearImportResult;
import com.cms.Service.AcademicYearService;
import com.cms.Service.UserActivityService;
import com.cms.Service.UserService;
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
@RequestMapping("/academic-year")
public class AcademicYearController {

    @Autowired
    AcademicYearService academicYearService;

    @Autowired
    AcademicYearRepository academicYearRepository;

    @Autowired
    UserService userService;

    @Autowired
    UserActivityService userActivityService;


    //=============METHOD TO SHOW ACADEMIC YEAR ADD PAGE=============//
    @GetMapping("/academic-year-add")
    public String showAddAcademicYearPage(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        model.addAttribute("loggedInUser", loggedInUser);
        model.addAttribute("academicYears", academicYearService.academicYearList());
        return "academic-year-add";
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userService.findById(Integer.parseInt(username));
    }

    //=============METHOD TO ADD ACADEMIC YEARS=============//
    @PostMapping("/add")
    public ResponseEntity<String> addAcademicYear(@RequestParam String academicYear, Model model) {
        User currentUser = getCurrentUser();

        if (academicYearService.existsByAcademicYear(academicYear)) {
            userActivityService.logActivity(currentUser, "ADD_ACADEMIC_YEAR_FAILED",
                    "Attempted to add existing academic year: " + academicYear);
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Academic Year already exists.");
        }

        AcademicYear addAcademicYear = academicYearService.addAcademicYear(academicYear);

        if (addAcademicYear == null) {
            userActivityService.logActivity(currentUser, "ADD_ACADEMIC_YEAR_ERROR",
                    "Error occurred while adding academic year: " + academicYear);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred.");
        }

        userActivityService.logActivity(currentUser, "ADD_ACADEMIC_YEAR_SUCCESS",
                "Added new academic year: " + academicYear);
        return ResponseEntity.ok("Academic Year added successfully!");
    }


    //=============METHOD TO IMPORT  DEPARTMENT=============//
    @PostMapping("/import-academic-years")
    public ResponseEntity<?> importAcademicYears(@RequestParam("file") MultipartFile file) throws IOException {
        User currentUser = getCurrentUser();
        Map<String, Object> response = new HashMap<>();

        userActivityService.logActivity(currentUser, "IMPORT_ACADEMIC_YEARS_START",
                "Started importing academic years from file: " + file.getOriginalFilename());
        try {
            AcademicYearImportResult result = academicYearService.importAcademicYear(file);

            response.put("addedCount", result.getAddedAcademicYears().size());
            response.put("notAddedAcademicYears", result.getNotAddedAcademicYears());

            userActivityService.logActivity(currentUser, "IMPORT_ACADEMIC_YEARS_SUCCESS",
                    String.format("Successfully imported academic years. Added: %d, Not added: %d",
                            result.getAddedAcademicYears().size(),
                            result.getNotAddedAcademicYears().size()));
            return ResponseEntity.ok(response);

        } catch (IOException e) {
            userActivityService.logActivity(currentUser, "IMPORT_ACADEMIC_YEARS_ERROR",
                    "Error occurred while importing academic years: " + e.getMessage());

            response.put("error", "An error occurred while importing academic years: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }



    //=============METHOD TO SHOW ALL ACADEMIC YEARS=============//
    @GetMapping("/all")
    @ResponseBody
    public List<AcademicYear> academicYearList() {
        return academicYearService.academicYearList();
    }


    @DeleteMapping("/delete/{academicYearId}")
    public ResponseEntity<String> deleteAcademicYear(@PathVariable int academicYearId) {
        User currentUser = getCurrentUser();
        if (academicYearService.existByAcademicYearId(academicYearId)) {
            academicYearService.deleteAcademicYear(academicYearId);
            userActivityService.logActivity(currentUser, "DELETE_ACADEMIC_YEAR_SUCCESS",
                    "Deleted academic year with ID: " + academicYearId);
            return ResponseEntity.ok("Academic Year deleted successfully!");
        } else {
            userActivityService.logActivity(currentUser, "DELETE_ACADEMIC_YEAR_FAILED",
                    "Attempted to delete non-existent academic year with ID: " + academicYearId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Academic Year not found.");
        }
    }

    //=============METHOD TO GET ACADEMIC YEAR=============//
    @GetMapping("/all/academic-years")
    @ResponseBody
    public List<AcademicYear> getAcademicYear() {
        List<AcademicYear> academicYears = academicYearService.academicYearList();
        // Create a new list of simplified faculty objects with only id and name
        List<AcademicYear> newAcademicYear = new ArrayList<>();
        for (AcademicYear academicYear : academicYears) {
            AcademicYear simplifiedAcademicyear = new AcademicYear();
            simplifiedAcademicyear.setId(academicYear.getId());
            simplifiedAcademicyear.setAcademicYear(academicYear.getAcademicYear());
            newAcademicYear.add(simplifiedAcademicyear);
        }
        return newAcademicYear;
    }

    //=============METHOD TO GET ACADEMIC YEAR BY ID=============//
    @GetMapping("/{id}")
    public ResponseEntity<AcademicYear> getAcademicYearById(@PathVariable int id, Model model) {
        Optional<AcademicYear> academicYear = academicYearService.getAcademicYearById(id);
        model.addAttribute("academicYear", academicYear);
        return academicYear.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    //=============METHOD TO UPDATE ACADEMIC YEAR BY ID=============//
    @PutMapping("/update")
    public ResponseEntity<?> updateAcademicYear(@RequestBody AcademicYear academicYear) {
        User currentUser = getCurrentUser();
        try {
            AcademicYear updatedAcademicYear = academicYearService.updateAcademicYear(academicYear);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Academic Year updated successfully");
            response.put("data", updatedAcademicYear);
            userActivityService.logActivity(currentUser, "UPDATE_ACADEMIC_YEAR_SUCCESS",
                    "Updated academic year: " + updatedAcademicYear.getAcademicYear());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Error updating Academic Year: " + e.getMessage());
            userActivityService.logActivity(currentUser, "UPDATE_ACADEMIC_YEAR_ERROR",
                    "Error updating academic year: " + academicYear.getAcademicYear() + ". Error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }




}
