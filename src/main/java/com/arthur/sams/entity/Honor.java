package com.arthur.sams.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * 荣誉（荣誉档案页面）
 */
@Entity
@Table(name = "honor")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Honor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 荣誉名称 */
    @Column(nullable = false, length = 128)
    private String title;

    /** 描述 */
    @Column(length = 500)
    private String description;

    /** 类型：奖学金 / 比赛 / 荣誉 / 其他 */
    @Column(length = 16)
    private String type;

    /** 级别：校级 / 院级 / 其他 */
    @Column(length = 16)
    private String level;

    /** 获得时间，格式 yyyy-MM（date 是 MySQL 关键字，列名改为 honor_date） */
    @Column(name = "honor_date", length = 16)
    private String honorDate;

    /** 卡片左上角图标文字 */
    @Column(length = 4)
    private String icon;
}
