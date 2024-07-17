package com.certificatemanagementsystem.Repository;

import com.certificatemanagementsystem.Model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Integer> {

    Department findById(int departmentId);
}
