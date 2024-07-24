package com.certificatemanagementsystem.Controller;



import com.certificatemanagementsystem.Model.AcademicYear;
import com.certificatemanagementsystem.Model.Department;
import com.certificatemanagementsystem.Model.Faculty;
import com.certificatemanagementsystem.Service.AcademicYearService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/academic-year")
public class AcademicYearController {

    @Autowired
    AcademicYearService academicYearService;

    @GetMapping
//    @ResponseBody
    public String showAcademicYearPage(Model model) {
        List<AcademicYear> academicYears = academicYearService.allAcademicYears();
        model.addAttribute("academicYears", academicYears);
        return "academic-year";
    }

    @PostMapping("/add")
//    @ResponseBody
    public String addAcademicYear(@RequestParam String year, Model model) {
        AcademicYear academicYear = academicYearService.addAcademicYear(year);
        model.addAttribute("academicYear", new AcademicYear());

        List<AcademicYear> academicYears = academicYearService.allAcademicYears();
        model.addAttribute("academicYears", academicYears);
        return  "redirect:/academic-year";
    }

    @PostMapping("/import-academic-years")
//    @ResponseBody
    public String importAcademicYears(@RequestParam MultipartFile academicYearFile, Model model) {
        academicYearService.importAcademicYears(academicYearFile);
        List<AcademicYear> academicYears = academicYearService.allAcademicYears();
        model.addAttribute("academicYears", academicYears);

        return "redirect:/academic-year";
    }

    @GetMapping("/all")
//    @ResponseBody
    public String allAcademicYears(Model model) {
        List<AcademicYear> academicYears = academicYearService.allAcademicYears();
        model.addAttribute("academicYears", academicYears);
        return "redirect:academic-year";
    }

    @GetMapping("/all/academic-years")
//    @ResponseBody
    public List<AcademicYear> getAcademicYears() {
        List<AcademicYear> academicYears = academicYearService.allAcademicYears();
        // Create a new list of simplified faculty objects with only id and name
        List<AcademicYear> newAcademicYears = new ArrayList<>();
        for (AcademicYear academicYear : academicYears) {
            AcademicYear simplifiedAcademicYear = new AcademicYear();
            simplifiedAcademicYear.setYearId(academicYear.getYearId());
            simplifiedAcademicYear.setYear(academicYear.getYear());
            newAcademicYears.add(simplifiedAcademicYear); // create
        }
        return newAcademicYears;
    }

    @DeleteMapping("/delete/{yearId}")
//    @ResponseBody
    public ResponseEntity<?> deleteAcademicYear(@PathVariable int yearId) {
        AcademicYear academicYear = academicYearService.deleteAcademicYear(yearId);
        if (academicYear != null) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
//        return academicYear;
    }

    @GetMapping("/sort-by")
    public List<AcademicYear> sortAcademicYear(String sortType) {
        List<AcademicYear> AcademicYears = academicYearService.sortAcademicYear(sortType);
        return AcademicYears;
    }
}
