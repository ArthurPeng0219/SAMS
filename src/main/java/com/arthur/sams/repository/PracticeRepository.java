package com.arthur.sams.repository;

import com.arthur.sams.entity.Practice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PracticeRepository extends JpaRepository<Practice, Long> {

    List<Practice> findAllByOrderByPracticeDateDesc();

    List<Practice> findByTypeOrderByPracticeDateDesc(String type);

    long countByType(String type);

    /** 时间线年份分组用：按时间倒序，Service 里再分组 */
    List<Practice> findAllByOrderByPracticeDateDescIdAsc();
}
