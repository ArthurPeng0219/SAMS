package com.arthur.sams.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * 实践经历（实践档案页面 + 求职档案页面的项目经历）
 */
@Entity
@Table(name = "practice")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Practice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 标题 */
    @Column(nullable = false, length = 128)
    private String title;

    /** 类型：项目经历 / 实习经历 / 校园实践 / 志愿活动 */
    @Column(length = 16)
    private String type;

    /** 时间 yyyy-MM */
    @Column(length = 16)
    private String practiceDate;

    /** 描述 */
    @Column(length = 500)
    private String description;

    /** 技术栈，逗号分隔，例如 "YOLOv8,PyTorch,OpenCV" */
    @Column(length = 200)
    private String technologies;

    /** 实践成果 */
    @Column(length = 500)
    private String result;

    /** 年份（前端按年份分组显示时间线，由 practiceDate 派生） */
    @Transient
    public String getYear() {
        if (practiceDate == null || practiceDate.length() < 4) {
            return "";
        }
        return practiceDate.substring(0, 4);
    }

    /** 技术栈拆成数组，前端直接遍历渲染标签 */
    @Transient
    public java.util.List<String> getTechList() {
        if (technologies == null || technologies.isBlank()) {
            return java.util.List.of();
        }
        return java.util.Arrays.stream(technologies.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();
    }
}
