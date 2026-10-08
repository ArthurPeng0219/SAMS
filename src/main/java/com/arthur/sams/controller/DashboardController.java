package com.arthur.sams.controller;

import com.arthur.sams.dto.DashboardView;
import com.arthur.sams.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 首页接口
 */
@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    /** GET /api/dashboard */
    @GetMapping
    public DashboardView dashboard() {
        return dashboardService.load();
    }
}
