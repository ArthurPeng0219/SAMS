package com.arthur.sams.repository;

import com.arthur.sams.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    List<Course> findAllByOrderByIdAsc();

    List<Course> findByNameContainingOrderByIdAsc(String keyword);

    List<Course> findTop4ByOrderByIdDesc();

    /** 平均成绩 */
    @Query("select coalesce(avg(c.score), 0) from Course c")
    Double avgScore();

    /** 最高成绩 */
    @Query("select coalesce(max(c.score), 0) from Course c")
    Double maxScore();

    /** 总学分 */
    @Query("select coalesce(sum(c.credit), 0) from Course c")
    Integer totalCredit();

    /** 按学期分组统计平均分（成长分析页面的成绩趋势图数据来源） */
    @Query("select c.term, avg(c.score) from Course c where c.term is not null group by c.term")
    List<Object[]> avgScoreGroupByTerm();

    /** 某个学期的平均分 */
    @Query("select coalesce(avg(c.score), 0) from Course c where c.term = :term")
    Double avgScoreByTerm(@Param("term") String term);
}
