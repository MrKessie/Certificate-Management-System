package com.certificatemanagementsystem.Controller;

import com.certificatemanagementsystem.Model.Register;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @GetMapping("/admin-dashboard")
    public String adminDashboard(HttpSession session, Model model) {
        Register loggedInUser = (Register) session.getAttribute("loggedInUser");
        model.addAttribute("loggedInUser", loggedInUser);
        return "admin-dashboard";
    }

    @GetMapping("/client-dashboard")
    public String clientDashboard(HttpSession session, Model model) {
        Register loggedInUser = (Register) session.getAttribute("loggedInUser");
        model.addAttribute("loggedInUser", loggedInUser);
        return "client-dashboard";
    }
}
