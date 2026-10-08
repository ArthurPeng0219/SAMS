package com.arthur.sams.controller;

import com.arthur.sams.dto.AvatarRequest;
import com.arthur.sams.dto.ProfileUpdateRequest;
import com.arthur.sams.dto.ResumeView;
import com.arthur.sams.entity.Resume;
import com.arthur.sams.entity.Student;
import com.arthur.sams.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 个人信息 / 求职档案接口
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    /** GET /api/profile —— 个人信息页面 */
    @GetMapping("/profile")
    public Student profile() {
        return profileService.currentStudent();
    }

    /** PUT /api/profile —— 保存联系方式 */
    @PutMapping("/profile")
    public Student updateProfile(@RequestBody ProfileUpdateRequest form) {
        return profileService.updateContact(form);
    }

    /** PUT /api/profile/avatar —— 更换头像文字，请求体 {"avatarText":"A"} */
    @PutMapping("/profile/avatar")
    public Student updateAvatar(@RequestBody AvatarRequest request) {
        return profileService.updateAvatar(request.avatarText());
    }

    /** GET /api/resume —— 求职档案页面 */
    @GetMapping("/resume")
    public ResumeView resume() {
        return profileService.resume();
    }

    /** PUT /api/resume —— 保存求职档案 */
    @PutMapping("/resume")
    public ResumeView updateResume(@RequestBody Resume resume) {
        return profileService.updateResume(resume);
    }
}
