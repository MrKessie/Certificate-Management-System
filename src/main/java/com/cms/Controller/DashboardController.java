package com.cms.Controller;

import com.cms.Model.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @GetMapping("/admin/dashboard")
    public String showDashboard(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        model.addAttribute("loggedInUser", loggedInUser);
        return "/admin-dashboard";
    }

    @GetMapping("/client/dashboard")
    public String showDashboardPage() {
        return "/dashboard-client";
    }
}
