package com.cms.Configurations;

import com.cms.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/info")
public class UserInfoController {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @GetMapping("/user-info")
    public ResponseEntity<Map<String, String>> getUserInfo(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        // Assuming you use userId as username in UserDetails
        User user = customUserDetailsService.getUserByUserId(Integer.parseInt(username));

        Map<String, String> userInfo = new HashMap<>();
        userInfo.put("fullName", user.getFullName());
        userInfo.put("role", user.getRole().name()); // Assuming Role is an enum
        userInfo.put("userId", String.valueOf(user.getUserId()));

        return ResponseEntity.ok(userInfo);
    }



}
