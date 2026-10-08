package com.arthur.sams.controller;

import com.arthur.sams.dto.SettingsView;
import com.arthur.sams.entity.SystemSetting;
import com.arthur.sams.service.SettingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 系统管理接口
 */
@RestController
@RequestMapping("/api/settings")
@RequiredArgsConstructor
public class SettingController {

    private final SettingService settingService;

    /** GET /api/settings —— 账户信息 + 所有开关 */
    @GetMapping
    public SettingsView load() {
        return settingService.load();
    }

    /** GET /api/settings/items —— 只要开关列表 */
    @GetMapping("/items")
    public List<SystemSetting> items() {
        return settingService.all();
    }

    /** PUT /api/settings/1  请求体 {"enabled": true}  切换开关 */
    @PutMapping("/{id}")
    public SystemSetting toggle(@PathVariable Long id, @RequestBody Map<String, Boolean> body) {
        return settingService.toggle(id, body.get("enabled"));
    }
}
