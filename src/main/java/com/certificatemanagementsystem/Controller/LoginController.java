package com.certificatemanagementsystem.Controller;

import com.certificatemanagementsystem.Enums.Role;
import com.certificatemanagementsystem.Model.Register;
import com.certificatemanagementsystem.Service.LoginService;
import com.certificatemanagementsystem.Service.RegisterService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/login")
public class LoginController {

    @Autowired
    LoginService loginService;

    @GetMapping
    public String showLoginForm() {
        return "login";
    }

//    @PostMapping("/loginUser")
    @PostMapping("/loginUser")
    public String loginUser(@RequestParam ("userId") int userId, @RequestParam ("password") String password,
                            HttpSession session, Model model) {
        Register user = loginService.findUser(userId, password);
        if (user == null) {
            model.addAttribute("errorMessage", "Invalid User ID or Password");
            return "login";
        }
        else {
            session.setAttribute("loggedInUser", user);
            //Redirects to appropriate dashboard based on Role
            if (user.getRole() == Role.ROLE_ADMIN) {
                return "redirect:/admin-dashboard";
            } else if (user.getRole() == Role.ROLE_CLIENT) {
                return "redirect:/client-dashboard";
            } else {
                return "login";
            }
        }
    }
}
