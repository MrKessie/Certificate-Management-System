package com.cms.Controller;

import com.cms.Enum.Roles;
import com.cms.Model.User;
import com.cms.Service.LoginService;
import com.cms.Service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/login")
public class LoginController {

    @Autowired
    UserService userService;

    @Autowired
    LoginService loginService;

    @GetMapping
    public String showLoginPage() {
        return "login";
    }



    @PostMapping("/loginUser")
    public String loginUser(@RequestParam int userId, @RequestParam String password, HttpSession session, Model model) {
        User user = loginService.findUser(userId, password);

        boolean isAuthenticated = authenticateUser(userId, password);

        if (user == null) {
            model.addAttribute("errorMessage", "Invalid User ID or Password");
            return "login";
        }

        else {
            session.setAttribute("loggedInUser", user);
            //Redirects to appropriate dashboard based on Role
//            if (user.getStatus() == 0) {
//                return "/password-change";
//            }
            if (user.getRole() == Roles.ROLE_ADMIN) {
                return "redirect:/admin-dashboard";
            } else if (user.getRole() == Roles.ROLE_CLIENT) {
                return "redirect:/client-dashboard";
            } else {
                return "login";
            }
        }
    }


    private boolean authenticateUser(int userId, String password) {
        User user = loginService.findUser(userId);

        if (user != null && user.getPassword().equals(password)) {
            return true; // Authentication successful
        } else {
            return false; // Authentication failed
        }
    }

}
