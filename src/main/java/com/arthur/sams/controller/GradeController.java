package com.arthur.sams.controller;

import com.arthur.sams.dto.GradeStats;
import com.arthur.sams.entity.Course;
import com.arthur.sams.service.GradeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 学习档案接口（增删改查 + 统计）
 */
@RestController
@RequestMapping("/api/grades")
@RequiredArgsConstructor
public class GradeController {

    private final GradeService gradeService;

    /** GET /api/grades?keyword=Java 查询列表（支持搜索） */
    @GetMapping
    public List<Course> list(@RequestParam(required = false) String keyword) {
        return gradeService.list(keyword);
    }

    /** GET /api/grades/stats 顶部统计卡 */
    @GetMapping("/stats")
    public GradeStats stats() {
        return gradeService.stats();
    }

    @GetMapping("/{id}")
    public Course get(@PathVariable Long id) {
        return gradeService.get(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Course create(@RequestBody Course course) {
        return gradeService.create(course);
    }

    @PutMapping("/{id}")
    public Course update(@PathVariable Long id, @RequestBody Course course) {
        return gradeService.update(id, course);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        gradeService.delete(id);
    }
}
