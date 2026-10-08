package com.arthur.sams.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * 课程成绩（学习档案页面）
 * status / statusClass 不是数据库字段，是根据 score 实时算出来的，直接返回给前端。
 */
@Entity
@Table(name = "course")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 课程名 */
    @Column(nullable = false, length = 64)
    private String name;

    /** 学期，例如「大二上」 */
    @Column(length = 16)
    private String term;

    /** 学分 */
    private Integer credit;

    /** 成绩 */
    private Double score;

    /** 成绩等级文案（派生字段） */
    public String getStatus() {
        if (score == null) {
            return "-";
        }
        if (score >= 90) {
            return "优秀";
        }
        if (score >= 80) {
            return "良好";
        }
        if (score >= 70) {
            return "中等";
        }
        if (score >= 60) {
            return "及格";
        }
        return "不及格";
    }

    /** 等级对应的样式类（派生字段）：success / good / warning / danger */
    public String getStatusClass() {
        if (score == null) {
            return "";
        }
        if (score >= 90) {
            return "success";
        }
        if (score >= 80) {
            return "good";
        }
        if (score >= 60) {
            return "warning";
        }
        return "danger";
    }
}
