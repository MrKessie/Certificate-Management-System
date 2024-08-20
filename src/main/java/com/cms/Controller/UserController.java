package com.cms.Controller;

import com.cms.Enum.*;
import com.cms.Model.User;
import com.cms.Service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping()
    public String registerPage(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        model.addAttribute("loggedInUser", loggedInUser);
        return "register";
    }
    @GetMapping("/change-password")
    public String showChangePwPage() {
        return "password-change";
    }


//    @PostMapping("/add")
//    public String addUser(@RequestParam int userId, @RequestParam String fullName, @RequestParam Gender gender, @RequestParam Role role,
//                                          @RequestParam String password, @RequestParam String confirmPassword, Model model) {
//        try {
//            if  (userService.userExists(userId)) {
//                throw new RuntimeException("User ID already exists!");
//            }
//            userService.addUser(userId, fullName, gender, role, password, confirmPassword);
//            model.addAttribute("successMessage", "User added successfully!");
//            return "redirect:/user";
//        }
//        catch (RuntimeException e) {
//            model.addAttribute("error", e.getMessage());
//            return "redirect:/user";
//        }
//        catch (Exception e) {
//            model.addAttribute("errorMessage", "An error occurred. Please try again later.");
//            e.printStackTrace(); // Log the exception for debugging
//        }
//        return "redirect:/user";
//    }


    @PostMapping("/add")
    public String addUser(@RequestParam int userId, @RequestParam String fullName, @RequestParam Genders gender, @RequestParam Roles role,
                          @RequestParam String password, @RequestParam String confirmPassword, RedirectAttributes redirectAttributes) {
        try {
            if  (userService.userExists(userId)) {
                throw new RuntimeException("User ID already exists!");
            }
            userService.addUser(userId, fullName, gender, role, password, confirmPassword);
            redirectAttributes.addFlashAttribute("successMessage", "User added successfully!");
            return "redirect:/user";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "An error occurred. Please try again later.");
            e.printStackTrace(); // Log the exception for debugging
        }
        return "redirect:/user";
    }


//    @GetMapping("/all")
//    public String usersList() {
//        List<User> users = userService.usersList();
//        return "register";
//    }

    @GetMapping("/all")
    public String usersList(Model model) {
        List<User> users = userService.usersList();
        model.addAttribute("users", users);
        return "register";
    }


    @PostMapping("/delete")
    public String deleteUser(int userId) {
        User user = userService.deleteUser(userId);
        return "register";
    }

//    @GetMapping("/checkUserId")
//    @ResponseBody
//    public ResponseEntity<Map<String, Boolean>> checkUserId(@RequestParam int userId) {
//        boolean exists = userService.userIdExists(userId);
//        Map<String, Boolean> response = new HashMap<>();
//        response.put("exists", exists);
//        return ResponseEntity.ok(response);
//    }

//    @PostMapping("/change-password")
//    public String changePassword(@RequestParam int userId, @RequestParam String newPassword, @RequestParam String confirmPassword) {
//        boolean success = userService.changePassword(userId, newPassword, confirmPassword);
//        if (success) {
//            return "redirect:/login-2";
//        } else {
//            return "redirect:/user/change-password";
//        }
////        return "redirect:/login";
//    }

    @GetMapping("/exists")
    public ResponseEntity<Boolean> userExists(@RequestParam int userId) {
        boolean exists = userService.userExists(userId);
        return ResponseEntity.ok(exists);
    }




}
