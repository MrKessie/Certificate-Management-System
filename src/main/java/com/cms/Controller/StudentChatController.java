//package com.cms.Controller;
//
//import com.cms.Model.User;
//import com.cms.Service.ConversationService;
//import com.cms.Service.MessageService;
//import jakarta.servlet.http.HttpSession;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//
//@Controller
//public class StudentChatController {
//    @Autowired
//    MessageService messageService;
//
//    @Autowired
//    ConversationService conversationService;
//
//    @GetMapping
//    public String showCertificateIssueForm(HttpSession session, Model model) {
////        model.addAttribute("certificatesIssued", certificateIssueService.issueList());
//        User loggedInUser = (User) session.getAttribute("loggedInUser");
//        model.addAttribute("loggedInUser", loggedInUser);
//        return "student-dashboard";
//    }
//}
