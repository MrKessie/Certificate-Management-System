package com.certificatemanagementsystem.Controller;



import com.certificatemanagementsystem.Model.AcademicYear;
import com.certificatemanagementsystem.Model.Faculty;
import com.certificatemanagementsystem.Service.AcademicYearService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping("/academic-year")
public class AcademicYearController {

    @Autowired
    AcademicYearService academicYearService;

    @GetMapping
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

    @PostMapping("/delete")
//    @ResponseBody
    public AcademicYear deleteAcademicYear(@RequestParam int yearId) {
        AcademicYear academicYear = academicYearService.deleteAcademicYear(yearId);
        return academicYear;
    }
}
