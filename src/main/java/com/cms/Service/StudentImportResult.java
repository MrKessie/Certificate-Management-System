package com.cms.Service;

import com.cms.Model.Department;
import com.cms.Model.Student;

import java.util.List;
import java.util.Map;

public class StudentImportResult {
    private List<Student> addedStudent;
    private Map<Integer, String> notAddedStudent;


    public StudentImportResult(List<Student> addedStudent, Map<Integer, String> notAddedStudent) {
        this.addedStudent = addedStudent;
        this.notAddedStudent = notAddedStudent;
    }

    public List<Student> getAddedStudent() {
        return addedStudent;
    }

    public void setAddedStudent(List<Student> addedStudent) {
        this.addedStudent = addedStudent;
    }

    public Map<Integer, String> getNotAddedStudent() {
        return notAddedStudent;
    }

    public void setNotAddedStudent(Map<Integer, String> notAddedStudent) {
        this.notAddedStudent = notAddedStudent;
    }
}
