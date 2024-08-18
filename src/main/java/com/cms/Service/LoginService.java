package com.cms.Service;

import com.cms.Model.User;
import com.cms.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    @Autowired
    UserRepository userRepository;

    public User findUser(int userId) {
        return userRepository.findById(userId);
    }

    public void updateUser(User user) {
        userRepository.save(user);
    }

    public User findUser(int userId, String password) {
        return userRepository.findUserByUserIdAndPassword(userId, password);
    }


}
