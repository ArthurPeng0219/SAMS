package com.arthur.sams.dto;

import com.arthur.sams.entity.Practice;

import java.util.List;

/**
 * 实践档案按年份分组，一个分组对应时间线上的一个年份块
 */
public record PracticeYearGroup(
        String year,
        List<Practice> items
) {
}
