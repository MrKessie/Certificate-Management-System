package com.certificatemanagementsystem.Repository;

import com.certificatemanagementsystem.Model.Register;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegisterRepository extends JpaRepository<Register, Integer> {
    Register findById(int userId);


    Register findByUserIdAndPassword(int userId, String password);
}
