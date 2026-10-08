package com.arthur.sams.repository;

import com.arthur.sams.entity.Certificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CertificateRepository extends JpaRepository<Certificate, Long> {

    List<Certificate> findAllByOrderByObtainDateDesc();

    List<Certificate> findByCategoryOrderByObtainDateDesc(String category);

    long countByCategory(String category);
}
