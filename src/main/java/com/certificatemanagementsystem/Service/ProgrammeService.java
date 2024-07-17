package com.certificatemanagementsystem.Service;

import com.certificatemanagementsystem.Model.Department;
import com.certificatemanagementsystem.Model.Faculty;
import com.certificatemanagementsystem.Model.Programme;
import com.certificatemanagementsystem.Repository.ProgrammeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProgrammeService {

    @Autowired
    ProgrammeRepository programmeRepository;

    public Programme addProgramme(int programmeId, String programmeName, Faculty facultyId, Department departmentId) {
        Programme programme = new Programme();
        programme.setProgrammeId(programmeId);
        programme.setProgrammeName(programmeName);
        programme.setFacultyId(facultyId);
        programme.setDepartmentId(departmentId);
        programme.setDateAdded(LocalDateTime.now());
        programme.setDateEdited(LocalDateTime.now());

        programmeRepository.save(programme);
        return programme;
    }

    public List<Programme> programmeList() {
        List<Programme> programmes = (List<Programme>) programmeRepository.findAll();
        return  programmes;
    }

    public  Programme deleteProgramme(int programmeId) {
        Programme programme = programmeRepository.findById(programmeId);
        programmeRepository.delete(programme);
        return programme;
    }
}
