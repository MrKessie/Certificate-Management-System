package com.certificatemanagementsystem.Service;

import com.certificatemanagementsystem.Model.Register;
import com.certificatemanagementsystem.Repository.RegisterRepository;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    @Autowired
    RegisterRepository registerRepository;

//    @Autowired
//    PasswordEncoder passwordEncoder;

    public Register findUser(int userId, String password) {
        return registerRepository.findByUserIdAndPassword(userId, password);
//        if (user != null && passwordEncoder.matches(password, user.getPassword())){
//        if (user == null){
//            return user;
//        }
//        return null;
    }

//    public boolean authenticate(int userId, String password) {
//        Register register = registerRepository.findById(userId);
//        if (register != null && register.getPassword().equals(password) && register.getRole().equals("ADMIN")) {
//            return true;
//        }
//        else if (register != null && register.getPassword().equals(password) && register.getRole().equals("CLIENT")) {
//            return true;
//        }
//        return  false;
//    }
}
