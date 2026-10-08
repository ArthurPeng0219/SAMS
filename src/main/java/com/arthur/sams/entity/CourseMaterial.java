package com.arthur.sams.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * 课程资料（课程资料页面）
 */
@Entity
@Table(name = "course_material")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CourseMaterial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 文件名 */
    @Column(nullable = false, length = 160)
    private String fileName;

    /** 所属课程 */
    @Column(length = 64)
    private String courseName;

    /** 分类：Java / Python / 数据库 / 机器学习 / 其他 */
    @Column(length = 16)
    private String category;

    /** 文件类型：PDF / DOCX / PPTX ... */
    @Column(length = 16)
    private String fileType;

    /** 文件大小，例如 2.4 MB */
    @Column(length = 16)
    private String fileSize;

    /** 上传日期 yyyy-MM-dd */
    @Column(length = 16)
    private String uploadDate;
}
