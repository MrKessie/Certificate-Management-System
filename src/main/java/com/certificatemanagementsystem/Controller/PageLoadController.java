//package com.certificatemanagementsystem.Controller;
//
//import com.certificatemanagementsystem.Model.AcademicYear;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//
//import java.util.List;
//
//@Controller
//public class PageLoadController {
//
//    @GetMapping
//    public String showAcademicYearPage(Model model) {
//        List<AcademicYear> academicYears = academicYearService.allAcademicYears();
//        model.addAttribute("academicYears", academicYears);
//        return "academic-year";
//    }
//}
