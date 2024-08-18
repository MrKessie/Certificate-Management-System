package com.cms.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping(value = "/homepage")
public class HomePageController {

    @GetMapping
    public String showHomePage() {
        return "dashboard-page";
    }
}
