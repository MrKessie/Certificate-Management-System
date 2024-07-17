package com.certificatemanagementsystem.Service;

import com.certificatemanagementsystem.Enums.Role;
import com.certificatemanagementsystem.Model.Register;
import com.certificatemanagementsystem.Repository.RegisterRepository;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RegisterService {
    @Autowired
    RegisterRepository registerRepository;

//    @Autowired
//    private PasswordEncoder //passwordEncoder;

    public void addUser(int userId, String name, String password, Role role) {
        Register user = new Register();
        //if (user.getPassword() != null) {
//            String encodedPassword = passwordEncoder.encode(user.getPassword()); //Hashes the password
        user.setPassword(password);
        user.setUserId(userId);
        user.setName(name);
//            user.setPassword(encodedPassword);
        user.setRole(role);
        user.setDateAdded(LocalDateTime.now());
        user.setDateEdited(LocalDateTime.now());

        registerRepository.save(user);
        //}
        //return "user";
    }

    public List<Register> userList() {
        List<Register> users = (List<Register>) registerRepository.findAll();
        return users;
    }

    public Register deleteUser(int userId) {
        Register user = registerRepository.findById(userId);
        registerRepository.delete(user);
        return user;
    }
}
