package com.arthur.sams.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * 求职档案（求职档案页面左侧简介 + 教育经历 + 右侧求职意向）
 */
@Entity
@Table(name = "resume")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Resume {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 关联的学生 id */
    @Column(nullable = false)
    private Long studentId;

    /** 个人优势 */
    @Column(length = 1000)
    private String advantage;

    /** 教育经历 - 学校 */
    @Column(length = 64)
    private String educationSchool;

    /** 教育经历 - 专业 */
    @Column(length = 64)
    private String educationMajor;

    /** 教育经历 - 时间 */
    @Column(length = 32)
    private String educationPeriod;

    /** 求职意向 - 目标职位 */
    @Column(length = 64)
    private String targetPosition;

    /** 求职意向 - 工作地点 */
    @Column(length = 64)
    private String workCity;

    /** 求职意向 - 工作类型 */
    @Column(length = 32)
    private String workType;

    /** 求职意向 - 期望行业 */
    @Column(length = 64)
    private String industry;

    /** 求职状态，例如「求职中」 */
    @Column(length = 16)
    private String jobStatus;
}
