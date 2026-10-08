package com.arthur.sams.dto;

import com.arthur.sams.entity.Skill;
import com.arthur.sams.entity.TimelineEvent;

import java.util.List;

/**
 * 成长分析页面整体数据
 */
public record GrowthView(
        GrowthStats stats,
        List<TermScore> scoreTrend,
        List<Skill> skills,
        List<TimelineEvent> timeline
) {
}
