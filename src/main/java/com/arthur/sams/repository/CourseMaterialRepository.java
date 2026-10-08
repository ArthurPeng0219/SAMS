package com.arthur.sams.repository;

import com.arthur.sams.entity.CourseMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseMaterialRepository extends JpaRepository<CourseMaterial, Long> {

    List<CourseMaterial> findAllByOrderByUploadDateDesc();

    List<CourseMaterial> findByCategoryOrderByUploadDateDesc(String category);

    List<CourseMaterial> findByFileNameContainingOrderByUploadDateDesc(String keyword);

    List<CourseMaterial> findByCategoryAndFileNameContainingOrderByUploadDateDesc(String category, String keyword);

    long countByCategory(String category);
}
