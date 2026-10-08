package com.arthur.sams.repository;

import com.arthur.sams.entity.Resume;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResumeRepository extends JpaRepository<Resume, Long> {

    Optional<Resume> findFirstByOrderByIdAsc();

    Optional<Resume> findByStudentId(Long studentId);
}
