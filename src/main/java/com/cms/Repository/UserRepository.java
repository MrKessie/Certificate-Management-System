package com.cms.Repository;

import com.cms.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findUserByUserIdAndPassword(int userId, String password);

    Optional<User> findByUsername(String userName);
    boolean existsByUserId(int userId);

//    boolean existsByUser(User user);

    User findById(int userId);

    User findByUserId(int userId);
}
