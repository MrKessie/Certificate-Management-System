package com.cms.Repository;

import com.cms.Model.Programme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProgrammeRepository extends JpaRepository<Programme, Integer> {

    Optional<Programme> getByProgrammeId(int programmeId);

    Programme findByProgrammeId(int programId);

    boolean existsByProgrammeId(int programId);

    boolean existsByProgrammeName(String programmeName);
}
