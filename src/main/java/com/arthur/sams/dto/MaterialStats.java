package com.arthur.sams.dto;

/**
 * 课程资料统计卡片数据
 */
public record MaterialStats(
        long total,
        long javaCount,
        long pythonCount,
        long databaseCount,
        long mlCount,
        long otherCount
) {
}
