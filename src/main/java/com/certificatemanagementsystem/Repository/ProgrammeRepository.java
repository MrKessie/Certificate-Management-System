package com.certificatemanagementsystem.Repository;

import com.certificatemanagementsystem.Model.Programme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProgrammeRepository extends JpaRepository<Programme, Integer> {
    Programme findById(int programmeId);

    Optional<Programme> getProgrammeById(int programmeId);
}
