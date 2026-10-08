package com.arthur.sams.controller;

import com.arthur.sams.dto.HonorStats;
import com.arthur.sams.entity.Honor;
import com.arthur.sams.service.HonorService;
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
 * 荣誉档案接口
 */
@RestController
@RequestMapping("/api/honors")
@RequiredArgsConstructor
public class HonorController {

    private final HonorService honorService;

    /** GET /api/honors?type=奖学金 */
    @GetMapping
    public List<Honor> list(@RequestParam(required = false) String type) {
        return honorService.list(type);
    }

    @GetMapping("/stats")
    public HonorStats stats() {
        return honorService.stats();
    }

    @GetMapping("/{id}")
    public Honor get(@PathVariable Long id) {
        return honorService.get(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Honor create(@RequestBody Honor honor) {
        return honorService.create(honor);
    }

    @PutMapping("/{id}")
    public Honor update(@PathVariable Long id, @RequestBody Honor honor) {
        return honorService.update(id, honor);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        honorService.delete(id);
    }
}
