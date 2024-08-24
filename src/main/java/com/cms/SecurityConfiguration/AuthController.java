package com.cms.SecurityConfiguration;

import com.cms.Enum.Genders;
import com.cms.Enum.Roles;
import com.cms.Model.User;
import com.cms.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;

@Controller
public class AuthController {

    @Autowired
    UserService userService;

    @GetMapping("/register")
    public String showRegistrationForm() {
//        model.addAttribute("user", new User());
        return "register";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }


//    @PostMapping("/perform_login")
//    public String performLogin(@RequestParam(value = "error", required = false) String error,
//                                @RequestParam(value = "logout", required = false) String logout,
//                                Model model) {
//        if (error != null) {
//            model.addAttribute("error", "Invalid Credentials");
//        }
//        if (logout != null) {
//            model.addAttribute("message", "You have been logged out successfully");
//        }
//
//        return "login";
//    }


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
