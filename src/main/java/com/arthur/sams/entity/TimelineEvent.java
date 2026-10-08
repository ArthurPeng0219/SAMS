package com.arthur.sams.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * 成长时间线节点（成长分析页面底部）
 */
@Entity
@Table(name = "timeline_event")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TimelineEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 年份（year 是 MySQL 关键字，列名改为 event_year） */
    @Column(name = "event_year")
    private Integer year;

    /** 标题 */
    @Column(nullable = false, length = 128)
    private String title;

    /** 描述 */
    @Column(length = 500)
    private String content;

    /** 排序号 */
    private Integer sortOrder;
}
