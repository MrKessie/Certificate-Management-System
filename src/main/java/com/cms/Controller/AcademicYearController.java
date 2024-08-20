package com.cms.Controller;

import com.cms.Model.AcademicYear;
import com.cms.Model.User;
import com.cms.Repository.AcademicYearRepository;
import com.cms.Service.AcademicYearImportResult;
import com.cms.Service.AcademicYearService;
import com.cms.Service.DepartmentImportResult;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Controller
@RequestMapping("/academic-year")
public class AcademicYearController {

    @Autowired
    AcademicYearService academicYearService;

    @Autowired
    AcademicYearRepository academicYearRepository;

    //=============METHOD TO SHOW ACADEMIC YEAR ALL PAGE=============//
    @GetMapping("/academic-year-all")
    public String showAllAcademicYearPage(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        model.addAttribute("loggedInUser", loggedInUser);
        model.addAttribute("academicYears", academicYearService.academicYearList());
        return "/academic-year-all";
    }

    //=============METHOD TO SHOW ACADEMIC YEAR ADD PAGE=============//
    @GetMapping("/academic-year-add")
    public String showAddAcademicYearPage(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        model.addAttribute("loggedInUser", loggedInUser);
        model.addAttribute("academicYears", academicYearService.academicYearList());
        return "academic-year-add";
    }

    //=============METHOD TO ADD ACADEMIC YEARS=============//
    @PostMapping("/add")
    public ResponseEntity<String> addAcademicYear(@RequestParam String academicYear, Model model) {
        if (academicYearService.existsByAcademicYear(academicYear)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Academic Year already exists.");
        }

        AcademicYear addAcademicYear = academicYearService.addAcademicYear(academicYear);

        if (addAcademicYear == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred.");
        }

        return ResponseEntity.ok("Academic Year added successfully!");
    }


    //=============METHOD TO IMPORT  DEPARTMENT=============//
    @PostMapping("/import-academic-years")
    public ResponseEntity<?> importAcademicYears(@RequestParam("file") MultipartFile file) throws IOException {
        AcademicYearImportResult result = academicYearService.importAcademicYear(file);

        Map<String, Object> response = new HashMap<>();
        response.put("addedCount", result.getAddedAcademicYears().size());
        response.put("notAddedAcademicYears", result.getNotAddedAcademicYears());

        return ResponseEntity.ok(response);
    }



    //=============METHOD TO SHOW ALL ACADEMIC YEARS=============//
    @GetMapping("/all")
    @ResponseBody
    public List<AcademicYear> academicYearList() {
        return academicYearService.academicYearList();
    }


    @DeleteMapping("/delete/{academicYearId}")
    public ResponseEntity<String> deleteAcademicYear(@PathVariable int academicYearId) {
        if (academicYearService.existByAcademicYearId(academicYearId)) {
            academicYearService.deleteAcademicYear(academicYearId);
            return ResponseEntity.ok("Academic Year deleted successfully!");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Academic Year not found.");
        }
    }

    //=============METHOD TO UPDATE ACADEMIC YEAR=============//
    @PostMapping("/update")
    public String updateItem(@RequestParam int academicYearId, Model model) {
        try {
            AcademicYear academicYear = academicYearRepository.findById(academicYearId).get();
            model.addAttribute("academicYear",academicYear);

            AcademicYear newAcademicyear = new AcademicYear();
            newAcademicyear.setAcademicYear(academicYear.getAcademicYear());
            newAcademicyear.setDateEdited(LocalDateTime.now());
            model.addAttribute("newAcademicYear",newAcademicyear);
        }
        catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
            return "redirect:/academic-year/academic-year-add";
        }
        return "redirect:/academic-year/academic-year-edit";
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
    @PostMapping("/update/{id}")
    public String updateAcademicYear(@PathVariable("id") int id, @ModelAttribute("academicYear") AcademicYear academicYear) {
        academicYearService.update(id, academicYear);
        return "redirect:/academicYears";
    }


}
