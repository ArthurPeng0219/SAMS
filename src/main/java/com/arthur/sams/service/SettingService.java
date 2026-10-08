package com.arthur.sams.service;

import com.arthur.sams.dto.SettingsView;
import com.arthur.sams.entity.Student;
import com.arthur.sams.entity.SystemSetting;
import com.arthur.sams.repository.StudentRepository;
import com.arthur.sams.repository.SystemSettingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * 系统管理（settings.html）
 */
@Service
@RequiredArgsConstructor
public class SettingService {

    private final SystemSettingRepository settingRepository;
    private final StudentRepository studentRepository;

    public SettingsView load() {
        Student student = studentRepository.findFirstByOrderByIdAsc()
                .orElseThrow(() -> new NoSuchElementException("学生信息不存在"));
        return new SettingsView(
                student.getUsername(),
                student.getEmail(),
                student.getPhone(),
                settingRepository.findAllByOrderBySortOrderAsc()
        );
    }

    /** 切换某个开关 */
    @Transactional
    public SystemSetting toggle(Long id, Boolean enabled) {
        SystemSetting setting = settingRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("设置项不存在：" + id));
        setting.setEnabled(enabled == null ? Boolean.FALSE : enabled);
        return settingRepository.save(setting);
    }

    public List<SystemSetting> all() {
        return settingRepository.findAllByOrderBySortOrderAsc();
    }
}
