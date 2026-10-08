package com.arthur.sams.dto;

/**
 * 荣誉档案统计卡片数据
 */
public record HonorStats(
        long total,
        long schoolCount,
        long collegeCount,
        long competitionCount
) {
}
