package com.cms.Service;

import com.cms.Model.Department;
import com.cms.Model.Programme;

import java.util.List;
import java.util.Map;

public class ProgrammeImportResult {
    private List<Programme> addedProgrammes;
    private Map<Integer, String> notAddedProgrammes;


    public ProgrammeImportResult(List<Programme> addedProgrammes, Map<Integer, String> notAddedProgrammes) {
        this.addedProgrammes = addedProgrammes;
        this.notAddedProgrammes = notAddedProgrammes;
    }

    public List<Programme> getAddedProgrammes() {
        return addedProgrammes;
    }

    public void setAddedProgrammes(List<Programme> addedProgrammes) {
        this.addedProgrammes = addedProgrammes;
    }

    public Map<Integer, String> getNotAddedProgrammes() {
        return notAddedProgrammes;
    }

    public void setNotAddedProgrammes(Map<Integer, String> notAddedProgrammes) {
        this.notAddedProgrammes = notAddedProgrammes;
    }
}
