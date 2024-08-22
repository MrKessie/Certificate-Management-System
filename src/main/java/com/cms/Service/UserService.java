package com.cms.Service;

import com.cms.Enum.*;
import com.cms.Model.User;
import com.cms.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    //Method to add user
    public User addUser(int userId, String fullName, Genders gender, Roles role, String password, String confirmPassword) {
        if (userRepository.existsByUserId(userId)) {
            return null;
        }

        if (!password.equals(confirmPassword)) {
            throw new IllegalArgumentException("Passwords do not match");
        }

        User user = new User();
        user.setUserId(userId);
        user.setFullName(fullName);
        user.setGender(gender);
        user.setRole(role);
        user.setPassword(password);
        user.setConfirmPassword(confirmPassword);
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

//    public boolean userIdExists(int userId) {
//        return userRepository.findByUserId(userId).isPresent();
//    }


    public boolean userExistByUserId(int userId) {
        return userRepository.existsById(userId);
    }
}
