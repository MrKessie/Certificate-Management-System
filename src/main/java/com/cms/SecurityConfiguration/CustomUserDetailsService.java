package com.cms.SecurityConfiguration;

import com.cms.Model.User;
import com.cms.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
//        User user = userRepository.findByUserId(Integer.parseInt(userId));
//        if (user == null) {
//            throw new UsernameNotFoundException("User not found");
//        }

        int id;
        try {
            id = Integer.parseInt(userId);
        } catch (NumberFormatException e) {
            throw new UsernameNotFoundException("Invalid userId: " + userId);
        }

        User user = userRepository.findByUserId(id);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with userId: " + userId);
        }

        return new org.springframework.security.core.userdetails.User(
                String.valueOf(user.getUserId()),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(user.getRole().name()))
        );
    }

    public User getUserByUserId(int userId) {
        return userRepository.findByUserId(userId);
    }
}
