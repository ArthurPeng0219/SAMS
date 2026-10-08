package com.arthur.sams.dto;

/**
 * 成长分析页面上方四个核心指标
 */
public record GrowthStats(
        double avgScore,
        double avgScoreChange,
        long courseCount,
        int completionRate,
        long honorCount,
        long yearHonorCount,
        long practiceCount,
        long yearPracticeCount
) {
}
