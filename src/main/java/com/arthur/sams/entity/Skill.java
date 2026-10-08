package com.arthur.sams.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * 技能（成长分析页面的技能进度条 + 求职档案页面的技能标签，同一份数据两处使用）
 */
@Entity
@Table(name = "skill")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 技能名称 */
    @Column(nullable = false, length = 32)
    private String name;

    /** 掌握程度 0-100 */
    private Integer level;

    /** 排序号 */
    private Integer sortOrder;
}
