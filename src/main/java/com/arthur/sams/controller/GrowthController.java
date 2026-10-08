package com.arthur.sams.controller;

import com.arthur.sams.dto.GrowthStats;
import com.arthur.sams.dto.GrowthView;
import com.arthur.sams.dto.TermScore;
import com.arthur.sams.service.GrowthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 成长分析接口
 */
@RestController
@RequestMapping("/api/growth")
@RequiredArgsConstructor
public class GrowthController {

    private final GrowthService growthService;

    /** GET /api/growth —— 整页数据（指标 + 成绩趋势 + 技能 + 时间线） */
    @GetMapping
    public GrowthView growth() {
        return growthService.load();
    }

    /** GET /api/growth/stats —— 只要上方四个指标 */
    @GetMapping("/stats")
    public GrowthStats stats() {
        return growthService.stats();
    }

    /** GET /api/growth/score-trend —— 只要成绩趋势图数据 */
    @GetMapping("/score-trend")
    public List<TermScore> scoreTrend() {
        return growthService.scoreTrend();
    }
}
