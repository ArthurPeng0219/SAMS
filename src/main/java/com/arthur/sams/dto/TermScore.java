package com.arthur.sams.dto;

/**
 * 单个学期的平均成绩（成长分析 - 成绩趋势图一个柱子）
 */
public record TermScore(
        String term,
        double avgScore
) {
}
