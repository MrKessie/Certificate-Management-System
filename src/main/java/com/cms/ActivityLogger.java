package com.cms;

import com.cms.Model.User;
import com.cms.Repository.UserRepository;
import com.cms.Service.UserActivityService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Optional;

@Component
@Aspect
public class ActivityLogger implements HandlerInterceptor {

    @Autowired
    private UserActivityService userActivityService;

    @Autowired
    private UserRepository userRepository;

    @Pointcut("execution(* com.cms.*.*(..)) && !within(com.cms.config..*)")
    public void applicationMethods() {}

    @Around("applicationMethods()")
    public Object logActivity(ProceedingJoinPoint joinPoint) throws Throwable {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            Optional<User> user = userRepository.findByUsername(username);

            String action = joinPoint.getSignature().getName();
            String details = joinPoint.getSignature().toString();

            User user2 = user.get();
            userActivityService.logActivity(user2, action, details);
        }
        return joinPoint.proceed();
    }
}
