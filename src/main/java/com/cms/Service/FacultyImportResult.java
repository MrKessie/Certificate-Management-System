package com.cms.Service;

import com.cms.Model.Department;
import com.cms.Model.Faculty;

import java.util.List;
import java.util.Map;

public class FacultyImportResult {
    private List<Faculty> addedFaculties;
    private Map<Integer, String> notAddedFaculties;


    public FacultyImportResult(List<Faculty> addedFaculties, Map<Integer, String> notAddedFaculties) {
        this.addedFaculties = addedFaculties;
        this.notAddedFaculties = notAddedFaculties;
    }

    public List<Faculty> getAddedFaculties() {
        return addedFaculties;
    }

    public void setAddedFaculties(List<Faculty> addedFaculties) {
        this.addedFaculties = addedFaculties;
    }

    public Map<Integer, String> getNotAddedFaculties() {
        return notAddedFaculties;
    }

    public void setNotAddedFaculties(Map<Integer, String> notAddedFaculties) {
        this.notAddedFaculties = notAddedFaculties;
    }
}
