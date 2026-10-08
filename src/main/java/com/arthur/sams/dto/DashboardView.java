package com.arthur.sams.dto;

import com.arthur.sams.entity.Course;
import com.arthur.sams.entity.Honor;
import com.arthur.sams.entity.Student;

import java.util.List;

/**
 * 首页数据
 */
public record DashboardView(
        Student student,
        GradeStats gradeStats,
        long honorCount,
        long certificateCount,
        long practiceCount,
        List<Course> recentCourses,
        List<Honor> recentHonors
) {
}
