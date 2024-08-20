package com.cms.Repository;

import com.cms.Model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Integer> {

    Optional<Department> getByDepartmentId(int departmentId);

    boolean existsByDepartmentId(int departmentId);

    boolean existsByDepartmentName(String departmentName);

    Department findByDepartmentId(int departmentId);

   Department findDepartmentByDepartmentName(String departmentName);
}
