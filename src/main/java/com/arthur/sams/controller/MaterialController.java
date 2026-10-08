package com.arthur.sams.controller;

import com.arthur.sams.dto.MaterialStats;
import com.arthur.sams.entity.CourseMaterial;
import com.arthur.sams.service.MaterialService;
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
 * 课程资料接口
 */
@RestController
@RequestMapping("/api/materials")
@RequiredArgsConstructor
public class MaterialController {

    private final MaterialService materialService;

    /** GET /api/materials?category=Java&keyword=第一章 */
    @GetMapping
    public List<CourseMaterial> list(@RequestParam(required = false) String category,
                                     @RequestParam(required = false) String keyword) {
        return materialService.list(category, keyword);
    }

    @GetMapping("/stats")
    public MaterialStats stats() {
        return materialService.stats();
    }

    @GetMapping("/{id}")
    public CourseMaterial get(@PathVariable Long id) {
        return materialService.get(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CourseMaterial create(@RequestBody CourseMaterial material) {
        return materialService.create(material);
    }

    @PutMapping("/{id}")
    public CourseMaterial update(@PathVariable Long id, @RequestBody CourseMaterial material) {
        return materialService.update(id, material);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        materialService.delete(id);
    }
}
