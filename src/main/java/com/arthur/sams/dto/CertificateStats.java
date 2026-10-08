package com.arthur.sams.dto;

/**
 * 技能证书统计卡片数据
 */
public record CertificateStats(
        long total,
        long computerCount,
        long englishCount,
        long majorCount
) {
}
