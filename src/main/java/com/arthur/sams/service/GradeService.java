package com.arthur.sams.service;

import com.arthur.sams.common.Utils;
import com.arthur.sams.dto.GradeStats;
import com.arthur.sams.entity.Course;
import com.arthur.sams.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * 学习档案（grades.html）
 */
@Service
@RequiredArgsConstructor
public class GradeService {

    private final CourseRepository courseRepository;

    /** 成绩列表，支持按课程名搜索 */
    public List<Course> list(String keyword) {
        String kw = Utils.blankToNull(keyword);
        return kw == null
                ? courseRepository.findAllByOrderByIdAsc()
                : courseRepository.findByNameContainingOrderByIdAsc(kw);
    }

    /** 顶部统计：平均成绩 / 最高成绩 / 已修课程 / 总学分 */
    public GradeStats stats() {
        Integer credit = courseRepository.totalCredit();
        return new GradeStats(
                Utils.round1(courseRepository.avgScore()),
                Utils.round1(courseRepository.maxScore()),
                courseRepository.count(),
                credit == null ? 0 : credit
        );
    }

    public Course get(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("成绩记录不存在：" + id));
    }

    public Course create(Course course) {
        course.setId(null);
        return courseRepository.save(course);
    }

    public Course update(Long id, Course form) {
        Course db = get(id);
        db.setName(form.getName());
        db.setTerm(form.getTerm());
        db.setCredit(form.getCredit());
        db.setScore(form.getScore());
        return courseRepository.save(db);
    }

    public void delete(Long id) {
        if (!courseRepository.existsById(id)) {
            throw new NoSuchElementException("成绩记录不存在：" + id);
        }
        courseRepository.deleteById(id);
    }
}
