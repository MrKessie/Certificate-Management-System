package com.certificatemanagementsystem.Repository;

import com.certificatemanagementsystem.Model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Integer> {
    Department findById(int departmentId);

    Optional<Department> getDepartmentById(Integer integer);
}
