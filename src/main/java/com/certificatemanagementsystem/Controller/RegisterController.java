package com.certificatemanagementsystem.Controller;

import com.certificatemanagementsystem.Enums.Role;
import com.certificatemanagementsystem.Model.Register;
import com.certificatemanagementsystem.Service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/register")
public class RegisterController {
    @Autowired
    RegisterService registerService;

    @GetMapping
    public String showRegisterPage(Model model) {
        model.addAttribute("register", new Register());
        return "register";
    }

    @PostMapping("/add")
    //@ResponseBody
    public String addUser(int userId, String name, String password, Role role) {
        registerService.addUser(userId, name, password, role);
        return "register";
//        Register user = new Register();
//        if (user.getPassword() != null) {
//            registerService.addUser(userId, name, password, role);
//            return  "register";
//        }
//        else {
//            return "redirect:/register?error";
//        }
    }

    @GetMapping("/all")
    //@ResponseBody
    public List<Register> userList() {
        List<Register> users = registerService.userList();
        return users;
    }

    @PostMapping("/delete")
    //@ResponseBody
    public Register deleteUser(int userId) {
        Register user = registerService.deleteUser(userId);
        return user;
    }

}
