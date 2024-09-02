//package com.cms;
//
//import com.cms.Model.User;
//import com.cms.Service.UserActivityService;
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.annotation.AfterReturning;
//import org.aspectj.lang.annotation.Aspect;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Component;
//
//@Aspect
//@Component
//public class ActivityLoggingAspect {
//
//    @Autowired
//    private UserActivityService userActivityService;
//
//    @AfterReturning(pointcut = "execution(* com.example.controller.*.*(..))")
//    public void logActivity(JoinPoint joinPoint) {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        if (auth != null && auth.isAuthenticated()) {
//            User user = (User) auth.getPrincipal();
//            String action = joinPoint.getSignature().toShortString();
//            userActivityService.logActivity(user, action);
//        }
//    }
//}
