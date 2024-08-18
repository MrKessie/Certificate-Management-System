package com.cms.Controller;

import com.cms.Model.AcademicYear;
import com.cms.Model.User;
import com.cms.Repository.AcademicYearRepository;
import com.cms.Service.AcademicYearService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
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
    public String addAcademicYear(@RequestParam String academicYear, Model model) {
        academicYearService.addAcademicYear(academicYear);
        model.addAttribute("academicYear", new AcademicYear());
        return "redirect:/academic-year/academic-year-add";
    }

    //=============METHOD TO IMPORT  ACADEMIC YEAR=============//
    @PostMapping("/import-academic-years")
    public String importAcademicYears(@RequestParam("file") MultipartFile file) throws IOException {
        academicYearService.importAcademicYear(file);
        return "redirect:/academic-year/academic-year-add";
    }

    //=============METHOD TO SHOW ALL ACADEMIC YEARS=============//
    @GetMapping("/all")
    @ResponseBody
    public List<AcademicYear> academicYearList() {
        return academicYearService.academicYearList();
    }


    @PostMapping("/delete/{id}")
    public String deleteDepartment(@PathVariable int id) {
        academicYearService.deleteDepartmentById(id);
        return "redirect:/departments";
    }

    //=============METHOD TO EDIT ACADEMIC YEAR BY ID=============//
//    @GetMapping("delete/{id}")
//    public String editAcademicYear(@PathVariable int id, Model model) {
//        AcademicYear academicYear = academicYearService.getById(id);
//        model.addAttribute("academicYear", academicYear);
//        return "/academic-year-edit";
//    }

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

    @GetMapping("/delete/{id}")
    public String deleteAcademicYear(@PathVariable("id") int id) {
        academicYearService.delete(id);
        return "redirect:/academicYears";
    }


}
