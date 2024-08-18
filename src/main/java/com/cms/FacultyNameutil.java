package com.cms;

import com.cms.Controller.DepartmentController;
import org.springframework.stereotype.Component;

@Component
public class FacultyNameutil {

    DepartmentController departmentController;
    public String getFacultyName(int facultyId) {
        return departmentController.getFacultyName(facultyId);
    }
}
