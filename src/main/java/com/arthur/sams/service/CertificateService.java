package com.arthur.sams.service;

import com.arthur.sams.common.Utils;
import com.arthur.sams.dto.CertificateStats;
import com.arthur.sams.entity.Certificate;
import com.arthur.sams.repository.CertificateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * 技能证书（certificates.html）
 */
@Service
@RequiredArgsConstructor
public class CertificateService {

    private final CertificateRepository certificateRepository;

    /** 证书列表，支持按类别筛选：计算机类 / 英语类 / 专业类 / 其他 */
    public List<Certificate> list(String category) {
        String c = Utils.blankToNull(category);
        return c == null
                ? certificateRepository.findAllByOrderByObtainDateDesc()
                : certificateRepository.findByCategoryOrderByObtainDateDesc(c);
    }

    /** 顶部统计 */
    public CertificateStats stats() {
        return new CertificateStats(
                certificateRepository.count(),
                certificateRepository.countByCategory("计算机类"),
                certificateRepository.countByCategory("英语类"),
                certificateRepository.countByCategory("专业类")
        );
    }

    public Certificate get(Long id) {
        return certificateRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("证书不存在：" + id));
    }

    public Certificate create(Certificate certificate) {
        certificate.setId(null);
        return certificateRepository.save(certificate);
    }

    public Certificate update(Long id, Certificate form) {
        Certificate db = get(id);
        db.setName(form.getName());
        db.setCategory(form.getCategory());
        db.setIssuer(form.getIssuer());
        db.setObtainDate(form.getObtainDate());
        db.setCertNo(form.getCertNo());
        db.setStatus(form.getStatus());
        db.setIcon(form.getIcon());
        db.setExtraLabel(form.getExtraLabel());
        db.setExtraValue(form.getExtraValue());
        return certificateRepository.save(db);
    }

    public void delete(Long id) {
        if (!certificateRepository.existsById(id)) {
            throw new NoSuchElementException("证书不存在：" + id);
        }
        certificateRepository.deleteById(id);
    }
}
