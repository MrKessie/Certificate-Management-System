package com.certificatemanagementsystem.Repository;

import com.certificatemanagementsystem.Model.Programme;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProgrammeRepository extends CrudRepository<Programme, Integer> {
    Programme findById(int programmeId);
}
