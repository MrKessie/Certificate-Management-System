package com.cms.Service;

import com.cms.Enum.*;
import com.cms.Model.User;
import com.cms.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    //Method to add user
    public User addUser(int userId, String fullName, String username, Genders gender, Roles role, String password) {
        if (userRepository.existsByUserId(userId)) {
            return null;
        }

        User user = new User();
        user.setUserId(userId);
        user.setFullName(fullName);
        user.setUsername(username);
        user.setGender(gender);
        user.setRole(role);
        user.setPassword(passwordEncoder.encode(password));
        user.setStatus(0);
        user.setDateAdded(LocalDateTime.now());
        user.setDateEdited(LocalDateTime.now());
        return userRepository.save(user);
    }


    public List<User> usersList(){
        return userRepository.findAll();
    }

    public boolean deleteUser(int userId) {
        if (userRepository.existsByUserId(userId)) {
            userRepository.deleteById(userId);
            return true;
        }
        return false;
    }

    public User findById(int userId) {
        return userRepository.findById(userId);
    }


    public boolean userExistByUserId(int userId) {
        return userRepository.existsById(userId);
    }

    public long totalUsers() {
        return userRepository.count();
    }

    public List<String> getOnlineUsersByRole(String role) {
        return userSessions.entrySet().stream()
                .filter(entry -> userRepository.findById(entry.getKey())
                        .map(user -> user.getRole().equals(role))
                        .orElse(false))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    public void userConnected(String userId, String sessionId) {
        userSessions.put(userId, sessionId);
    }

    public void userDisconnected(String userId) {
        userSessions.remove(userId);
    }

    public boolean isUserOnline(String userId) {
        return userSessions.containsKey(userId);
    }
}
