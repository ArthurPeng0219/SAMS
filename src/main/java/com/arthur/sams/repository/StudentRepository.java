package com.arthur.sams.repository;

import com.arthur.sams.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    /** 当前系统只有一个学生档案，取第一条 */
    Optional<Student> findFirstByOrderByIdAsc();
}
