package com.cms.Repository;

import com.cms.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findUserByUserIdAndPassword(int userId, String password);
    boolean existsByUserId(int userId);

    User findById(int userId);

    User findByUserId(int userId);
}
