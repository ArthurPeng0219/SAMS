package com.arthur.sams.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * 系统设置项（系统管理页面的开关）
 */
@Entity
@Table(name = "system_setting")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SystemSetting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 设置项标识，例如 autoSave */
    @Column(name = "setting_key", nullable = false, length = 32, unique = true)
    private String settingKey;

    /** 设置项名称 */
    @Column(length = 32)
    private String label;

    /** 说明 */
    @Column(length = 128)
    private String description;

    /** 是否开启 */
    private Boolean enabled;

    /** 排序号 */
    private Integer sortOrder;
}
