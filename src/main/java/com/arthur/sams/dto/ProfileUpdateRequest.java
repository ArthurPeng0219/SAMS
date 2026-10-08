package com.arthur.sams.dto;

/**
 * 个人信息页面「联系方式」表单提交内容
 */
public record ProfileUpdateRequest(
        String phone,
        String email,
        String city,
        String github
) {
}
