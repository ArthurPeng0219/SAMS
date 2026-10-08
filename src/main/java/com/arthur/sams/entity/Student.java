package com.arthur.sams.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * 学生基本信息（个人信息页面 / 账户设置 / 首页头像区）
 */
@Entity
@Table(name = "student")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 姓名 */
    @Column(nullable = false, length = 32)
    private String name;

    /** 学号 */
    @Column(length = 32)
    private String studentNo;

    /** 专业 */
    @Column(length = 64)
    private String major;

    /** 班级 */
    @Column(length = 64)
    private String className;

    /** 入学年份 */
    private Integer enrollYear;

    /** 手机号 */
    @Column(length = 32)
    private String phone;

    /** 邮箱 */
    @Column(length = 64)
    private String email;

    /** 所在城市 */
    @Column(length = 32)
    private String city;

    /** GitHub 地址 */
    @Column(length = 128)
    private String github;

    /** 头像文字（单字母） */
    @Column(length = 4)
    private String avatarText;

    /** 登录用户名 */
    @Column(length = 32)
    private String username;
}
