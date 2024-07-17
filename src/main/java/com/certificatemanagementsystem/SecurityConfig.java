//package com.certificatemanagementsystem;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.Customizer;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.builders.WebSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig {
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .authorizeRequests(authorizeRequests ->
//                        authorizeRequests
//                                .requestMatchers("/register/**", "/login/**", "/login/loginUser", "/static/styles/**",
//                                        "/static/scripts/**", "/static/images/**", "/static/favicon.ico").permitAll() // Allow public access to these endpoints
//                                .requestMatchers("/admin-dashboard").hasRole("ADMIN") // Only ADMIN can access this
//                                .requestMatchers("/client-dashboard").hasRole("CLIENT") // Only CLIENT can access this
//                                .anyRequest().authenticated() // All other endpoints require authentication
//                )
//                .formLogin(formLogin ->
//                        formLogin
//                                .loginPage("/login") // Specify the login page URL
//                                .permitAll()
//                )
//                .logout(logout ->
//                        logout
//                                .permitAll()
//                )
//                .csrf(csrf -> csrf.disable());
//        return http.build();
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//}
