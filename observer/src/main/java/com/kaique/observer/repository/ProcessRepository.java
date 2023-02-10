package com.kaique.observer.repository;

import com.kaique.observer.model.Process;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProcessRepository extends JpaRepository<Process, Integer> {
    Optional<Process> findByFilename(String filename);
}
