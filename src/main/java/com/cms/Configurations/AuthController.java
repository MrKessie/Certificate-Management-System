package com.cms.Configurations;

import com.cms.Enum.Genders;
import com.cms.Enum.Roles;
import com.cms.Model.User;
import com.cms.Service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    @Autowired
    UserService userService;

    @GetMapping("/register")
    public String showRegistrationForm(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        model.addAttribute("loggedInUser", loggedInUser);
        model.addAttribute("users", userService.usersList());
        return "register";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }




    @PostMapping("/register/user")
    public ResponseEntity<String> addUser(@RequestParam int userId, @RequestParam String fullName, @RequestParam String username,
                                          @RequestParam Genders gender, @RequestParam Roles role, @RequestParam String password) {
        if (userService.userExistByUserId(userId)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User already exists.");
        }

        User user = userService.addUser(userId, fullName, username, gender, role, password);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred.");
        }

        return ResponseEntity.ok("User added successfully!");
    }
}
