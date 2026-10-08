package com.arthur.sams.repository;

import com.arthur.sams.entity.SystemSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SystemSettingRepository extends JpaRepository<SystemSetting, Long> {

    List<SystemSetting> findAllByOrderBySortOrderAsc();

    Optional<SystemSetting> findBySettingKey(String settingKey);
}
