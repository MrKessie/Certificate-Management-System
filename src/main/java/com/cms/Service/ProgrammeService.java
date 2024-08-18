package com.cms.Service;

import com.cms.Model.Department;
import com.cms.Model.Faculty;
import com.cms.Model.Programme;
import com.cms.Repository.DepartmentRepository;
import com.cms.Repository.FacultyRepository;
import com.cms.Repository.ProgrammeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ProgrammeService {
    @Autowired
    ProgrammeRepository programmeRepository;

    @Autowired
    FacultyRepository facultyRepository;

    @Autowired
    DepartmentRepository departmentRepository;

    // Add new programme, update programme details, delete programme, etc.
    public Programme addProgramme(int programmeId, String programmeName, Faculty faculty, Department department) {
        Programme programme = new Programme();
        programme.setProgrammeId(programmeId);
        programme.setProgrammeName(programmeName);
        programme.setFaculty(faculty);
        programme.setDepartment(department);
        department.setDateAdded(LocalDateTime.now());
        department.setDateEdited(LocalDateTime.now());

        programmeRepository.save(programme);
        return programme;
    }

    public List<Programme> allProgrammes() {
        List<Programme> programmes = (List<Programme>) programmeRepository.findAll();
        return programmes;
    }

    public boolean deleteProgramme(int id) {
        if (programmeRepository.existsById(id)) {
            programmeRepository.deleteById(id);
            return true;
        }
        return false;
    }


    public Optional<Programme> getProgrammeById(int programmeId) {
        return programmeRepository.getByProgrammeId(programmeId);
    }

    public Programme findById(int programmeId) {
        return programmeRepository.findByProgrammeId(programmeId);
    }

    public List<Faculty> getAllFaculties() {
        return facultyRepository.findAll();
    }
}
