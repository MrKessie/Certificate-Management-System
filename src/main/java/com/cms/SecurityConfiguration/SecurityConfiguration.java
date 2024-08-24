package com.cms.SecurityConfiguration;


import com.cms.SecurityConfiguration.CustomUserDetailsService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.util.Collection;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {



    @Autowired
    CustomUserDetailsService customUserDetailsService;

    @Autowired
    PasswordEncoder passwordEncoder;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/login", "/register", "/register/user", "/").permitAll() // Public URLs
                        .requestMatchers("/static/styles/**", "/static/scripts/**", "/static/images/**").permitAll() // Public resources
                        .requestMatchers("/admin/**").hasRole("ADMIN") // Admin access only
                        .requestMatchers("/client/**").hasRole("CLIENT") // Client access only
                        .anyRequest().authenticated() // All other URLs require authentication
                )
//                .csrf(csrf -> csrf.disable())
                .formLogin(form -> form
                        .loginPage("/login") // Custom login page
                        .loginProcessingUrl("/perform_login")
                        .usernameParameter("userId") // Use userId instead of username
                        .passwordParameter("password") // Password remains the same
                        .permitAll()
                        .successHandler(new AuthenticationSuccessHandler() {
                                            @Override
                                            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                                                                Authentication authentication) throws IOException, ServletException {
                                                Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
                                                for (GrantedAuthority grantedAuthority : authorities) {
                                                    if (grantedAuthority.getAuthority().equals("ROLE_ADMIN")) {
                                                        response.sendRedirect("/admin/dashboard");
                                                        return;
                                                    } else if (grantedAuthority.getAuthority().equals("ROLE_CLIENT")) {
                                                        response.sendRedirect("/client/dashboard");
                                                        return;
                                                    }
                                                }
                                            }
                        })
                )
                .logout(logout -> logout
                        .permitAll() // Allow everyone to perform logout
                        .logoutUrl("/logout") // Custom logout URL, optional
                        .logoutSuccessUrl("/login?logout") // Redirect to login page after logout, optional
                );

        return http.build();
    }


    @Autowired
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder);
    }

//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }

}
