package com.arthur.sams.dto;

/**
 * 实践档案统计卡片数据
 */
public record PracticeStats(
        long total,
        long projectCount,
        long internCount,
        long campusCount,
        long volunteerCount
) {
}
