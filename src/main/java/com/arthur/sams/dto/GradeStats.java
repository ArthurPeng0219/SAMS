package com.arthur.sams.dto;

/**
 * 学习档案统计卡片数据
 */
public record GradeStats(
        double avgScore,
        double maxScore,
        long courseCount,
        int totalCredit
) {
}
