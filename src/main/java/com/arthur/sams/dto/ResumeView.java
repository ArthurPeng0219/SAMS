package com.arthur.sams.dto;

import com.arthur.sams.entity.Practice;
import com.arthur.sams.entity.Resume;
import com.arthur.sams.entity.Skill;
import com.arthur.sams.entity.Student;

import java.util.List;

/**
 * 求职档案页面整体数据
 */
public record ResumeView(
        Student student,
        Resume resume,
        List<Skill> skills,
        List<Practice> projects
) {
}
