package com.certificatemanagementsystem.Controller;

import com.certificatemanagementsystem.Model.Faculty;
import com.certificatemanagementsystem.Service.FacultyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/faculty")
public class FacultyController {

    @Autowired
    FacultyService facultyService;

    @GetMapping
    public String showFacultyPage(Model model) {
        List<Faculty> faculties = facultyService.allFaculties();
        model.addAttribute("faculties", faculties);
        return "/faculty";
    }

    @PostMapping("/add")
    public String addFaculty(@RequestParam int facultyId, @RequestParam String facultyName, Model model) {
            Faculty faculty = facultyService.addFaculty(facultyId, facultyName);
            model.addAttribute("faculty", new Faculty());
            List<Faculty> faculties = facultyService.allFaculties();
            model.addAttribute("faculties", faculties);

        return "redirect:/faculty";
    }

    @PostMapping("/import-faculties")
    public String importFaculty(@RequestParam MultipartFile facultyFile, Model model) {
        facultyService.importFaculty(facultyFile);
        List<Faculty> faculties = facultyService.allFaculties();
        model.addAttribute("faculties", faculties);

        return "redirect:/faculty";
    }

    @GetMapping("/all")
    public String allFaculties(Model model) {
        List<Faculty> faculties = facultyService.allFaculties();
        model.addAttribute("faculties", faculties);
        return "redirect:/faculty";
    }

    //This endpoint gets all the faculties and returns it as JSON and pass it to other controller classes
    @GetMapping("/all/faculties")
    @ResponseBody
    public List<Faculty> getFaculties() {
        List<Faculty> faculties = facultyService.allFaculties();
        // Create a new list of simplified faculty objects with only id and name
        List<Faculty> newFaculties = new ArrayList<>();
        for (Faculty faculty : faculties) {
            Faculty simplifiedFaculty = new Faculty();
            simplifiedFaculty.setId(faculty.getId());
            simplifiedFaculty.setFacultyName(faculty.getFacultyName());
            newFaculties.add(simplifiedFaculty);
        }
        return newFaculties;
    }

    @DeleteMapping("/delete/{facultyId}")
    @ResponseBody
    public ResponseEntity<?> deleteFaculty(@PathVariable int facultyId) {
        Faculty faculty = facultyService.deleteFaculty(facultyId);
        if (faculty != null) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

//    @DeleteMapping("/delete/{id}")
//    public String deleteFaculty(@PathVariable("id") int id, Model model) {
//        facultyService.deleteFaculty(id);
////        List<Faculty> faculties = facultyService.allFaculties();
////        model.addAttribute("faculties", faculties);
//        return "redirect:/faculty";
//    }


//    @GetMapping("/search")
//    public String searchFaculty(@RequestParam("searchQuery") String searchQuery, Model model) {
//        List<Faculty> faculties = facultyService.searchFaculties(searchQuery);
//        model.addAttribute("faculties", faculties);
//        return "redirect:/faculty"; // Return a view to display search results
//    }
}
