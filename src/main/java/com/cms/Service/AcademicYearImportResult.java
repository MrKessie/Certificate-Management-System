package com.cms.Service;

import com.cms.Model.AcademicYear;
import com.cms.Model.Department;

import java.util.List;
import java.util.Map;

public class AcademicYearImportResult {
    private List<AcademicYear> addedAcademicYears;
    private Map<String, String> notAddedAcademicYears;


    public AcademicYearImportResult(List<AcademicYear> addedAcademicYears, Map<String, String> notAddedAcademicYears) {
        this.addedAcademicYears = addedAcademicYears;
        this.notAddedAcademicYears = notAddedAcademicYears;
    }

    public List<AcademicYear> getAddedAcademicYears() {
        return addedAcademicYears;
    }

    public void setAddedAcademicYears(List<AcademicYear> addedAcademicYears) {
        this.addedAcademicYears = addedAcademicYears;
    }

    public Map<String, String> getNotAddedAcademicYears() {
        return notAddedAcademicYears;
    }

    public void setNotAddedAcademicYears(Map<String, String> notAddedAcademicYears) {
        this.notAddedAcademicYears = notAddedAcademicYears;
    }
}
