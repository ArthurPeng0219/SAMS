package com.arthur.sams.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * 技能证书（技能证书页面）
 */
@Entity
@Table(name = "certificate")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Certificate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 证书名称 */
    @Column(nullable = false, length = 128)
    private String name;

    /** 类别：计算机类 / 英语类 / 专业类 / 其他 */
    @Column(length = 16)
    private String category;

    /** 颁发 / 认证机构 */
    @Column(length = 64)
    private String issuer;

    /** 获得时间 yyyy-MM */
    @Column(length = 16)
    private String obtainDate;

    /** 证书编号 */
    @Column(length = 64)
    private String certNo;

    /** 状态：已获得 / 备考中 */
    @Column(length = 16)
    private String status;

    /** 卡片图标文字 */
    @Column(length = 4)
    private String icon;

    /** 附加信息标签，例如「考试成绩」 */
    @Column(length = 16)
    private String extraLabel;

    /** 附加信息值，例如「520」 */
    @Column(length = 32)
    private String extraValue;
}
