package com.arthur.sams.dto;

import com.arthur.sams.entity.SystemSetting;

import java.util.List;

/**
 * 系统管理页面整体数据
 */
public record SettingsView(
        String username,
        String email,
        String phone,
        List<SystemSetting> settings
) {
}
