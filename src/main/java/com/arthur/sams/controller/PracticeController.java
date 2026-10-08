package com.arthur.sams.controller;

import com.arthur.sams.dto.PracticeStats;
import com.arthur.sams.dto.PracticeYearGroup;
import com.arthur.sams.entity.Practice;
import com.arthur.sams.service.PracticeService;
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
 * 实践档案接口
 */
@RestController
@RequestMapping("/api/practices")
@RequiredArgsConstructor
public class PracticeController {

    private final PracticeService practiceService;

    /** GET /api/practices?type=项目经历 */
    @GetMapping
    public List<Practice> list(@RequestParam(required = false) String type) {
        return practiceService.list(type);
    }

    /** GET /api/practices/grouped 按年份分组，供时间线渲染 */
    @GetMapping("/grouped")
    public List<PracticeYearGroup> grouped() {
        return practiceService.groupedByYear();
    }

    @GetMapping("/stats")
    public PracticeStats stats() {
        return practiceService.stats();
    }

    @GetMapping("/{id}")
    public Practice get(@PathVariable Long id) {
        return practiceService.get(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Practice create(@RequestBody Practice practice) {
        return practiceService.create(practice);
    }

    @PutMapping("/{id}")
    public Practice update(@PathVariable Long id, @RequestBody Practice practice) {
        return practiceService.update(id, practice);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        practiceService.delete(id);
    }
}
