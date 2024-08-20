package com.cms.Service;

import com.cms.Model.Department;

import java.util.List;
import java.util.Map;

public class DepartmentImportResult {
    private List<Department> addedDepartments;
    private Map<Integer, String> notAddedDepartments;

    public DepartmentImportResult(List<Department> addedDepartments, Map<Integer, String> notAddedDepartments) {
        this.addedDepartments = addedDepartments;
        this.notAddedDepartments = notAddedDepartments;
    }

    public List<Department> getAddedDepartments() {
        return addedDepartments;
    }

    public void setAddedDepartments(List<Department> addedDepartments) {
        this.addedDepartments = addedDepartments;
    }

    public Map<Integer, String> getNotAddedDepartments() {
        return notAddedDepartments;
    }

    public void setNotAddedDepartments(Map<Integer, String> notAddedDepartments) {
        this.notAddedDepartments = notAddedDepartments;
    }
}
