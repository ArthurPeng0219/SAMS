package com.arthur.sams.service;

import com.arthur.sams.common.Utils;
import com.arthur.sams.dto.DashboardView;
import com.arthur.sams.dto.GradeStats;
import com.arthur.sams.repository.CertificateRepository;
import com.arthur.sams.repository.CourseRepository;
import com.arthur.sams.repository.HonorRepository;
import com.arthur.sams.repository.PracticeRepository;
import com.arthur.sams.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

/**
 * 首页（index.html）
 */
@Service
@RequiredArgsConstructor
public class DashboardService {

    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final HonorRepository honorRepository;
    private final CertificateRepository certificateRepository;
    private final PracticeRepository practiceRepository;

    /** 首页数据：顶部用户 + 4 个统计卡 + 最近成绩 + 最近荣誉 */
    public DashboardView load() {
        var student = studentRepository.findFirstByOrderByIdAsc()
                .orElseThrow(() -> new NoSuchElementException("学生信息不存在"));

        Integer credit = courseRepository.totalCredit();
        GradeStats gradeStats = new GradeStats(
                Utils.round1(courseRepository.avgScore()),
                Utils.round1(courseRepository.maxScore()),
                courseRepository.count(),
                credit == null ? 0 : credit
        );

        return new DashboardView(
                student,
                gradeStats,
                honorRepository.count(),
                certificateRepository.count(),
                practiceRepository.count(),
                courseRepository.findTop4ByOrderByIdDesc(),
                honorRepository.findTop3ByOrderByHonorDateDesc()
        );
    }
}
