package com.arthur.sams.repository;

import com.arthur.sams.entity.Honor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HonorRepository extends JpaRepository<Honor, Long> {

    List<Honor> findAllByOrderByHonorDateDesc();

    List<Honor> findByTypeOrderByHonorDateDesc(String type);

    List<Honor> findTop3ByOrderByHonorDateDesc();

    long countByLevel(String level);

    long countByType(String type);
}
