package com.cms.Controller;

import com.cms.Enum.*;
import com.cms.Model.User;
import com.cms.Service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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



    @PostMapping("/add")
    public ResponseEntity<String> addUser(@RequestParam int userId, @RequestParam String fullName, @RequestParam Genders gender, @RequestParam Roles role,
                          @RequestParam String password, @RequestParam String confirmPassword, RedirectAttributes redirectAttributes) {
        if (userService.userExistByUserId(userId)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User already exists.");
        }

        User user = userService.addUser(userId, fullName, gender, role, password, confirmPassword);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred.");
        }

        return ResponseEntity.ok("User added successfully!");
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


    @GetMapping("/exists")
    public ResponseEntity<Boolean> userExists(@RequestParam int userId) {
        boolean exists = userService.userExistByUserId(userId);
        return ResponseEntity.ok(exists);
    }




}
