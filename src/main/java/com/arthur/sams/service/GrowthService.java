package com.arthur.sams.service;

import com.arthur.sams.common.Utils;
import com.arthur.sams.dto.GrowthStats;
import com.arthur.sams.dto.GrowthView;
import com.arthur.sams.dto.TermScore;
import com.arthur.sams.entity.Honor;
import com.arthur.sams.entity.Practice;
import com.arthur.sams.repository.CourseRepository;
import com.arthur.sams.repository.HonorRepository;
import com.arthur.sams.repository.PracticeRepository;
import com.arthur.sams.repository.SkillRepository;
import com.arthur.sams.repository.TimelineEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

/**
 * 成长分析（growth.html）：所有指标都从数据库实时算出来，不是写死的
 */
@Service
@RequiredArgsConstructor
public class GrowthService {

    /** 学期排序：数据库里是字符串，按这个顺序排才符合时间先后 */
    private static final List<String> TERM_ORDER =
            List.of("大一上", "大一下", "大二上", "大二下", "大三上", "大三下", "大四上", "大四下");

    /** 培养方案里的计划课程总数，用来算「完成度」 */
    private static final int PLANNED_COURSE_COUNT = 44;

    private final CourseRepository courseRepository;
    private final HonorRepository honorRepository;
    private final PracticeRepository practiceRepository;
    private final SkillRepository skillRepository;
    private final TimelineEventRepository timelineEventRepository;

    public GrowthView load() {
        return new GrowthView(stats(), scoreTrend(), skillRepository.findAllByOrderBySortOrderAsc(),
                timelineEventRepository.findAllByOrderBySortOrderAsc());
    }

    /** 上方四个核心指标 */
    public GrowthStats stats() {
        long courseCount = courseRepository.count();
        int completionRate = (int) Math.min(100, Math.round(courseCount * 100.0 / PLANNED_COURSE_COUNT));

        String currentYear = String.valueOf(LocalDate.now().getYear());
        List<Honor> honors = honorRepository.findAll();
        long yearHonors = honors.stream()
                .filter(h -> h.getHonorDate() != null && h.getHonorDate().startsWith(currentYear))
                .count();

        List<Practice> practices = practiceRepository.findAll();
        long yearPractices = practices.stream()
                .filter(p -> p.getPracticeDate() != null && p.getPracticeDate().startsWith(currentYear))
                .count();

        List<TermScore> trend = scoreTrend();
        double change = 0.0;
        if (trend.size() >= 2) {
            change = Utils.round1(trend.get(trend.size() - 1).avgScore() - trend.get(trend.size() - 2).avgScore());
        }

        return new GrowthStats(
                Utils.round1(courseRepository.avgScore()),
                change,
                courseCount,
                completionRate,
                honors.size(),
                yearHonors,
                practices.size(),
                yearPractices
        );
    }

    /** 各学期平均成绩，按学期先后排序 */
    public List<TermScore> scoreTrend() {
        return courseRepository.avgScoreGroupByTerm().stream()
                .map(row -> new TermScore((String) row[0], Utils.round1((Double) row[1])))
                .sorted(Comparator.comparingInt(t -> termIndex(t.term())))
                .toList();
    }

    private int termIndex(String term) {
        int index = TERM_ORDER.indexOf(term);
        return index < 0 ? Integer.MAX_VALUE : index;
    }
}
