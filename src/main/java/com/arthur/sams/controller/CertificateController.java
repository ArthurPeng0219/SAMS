package com.arthur.sams.controller;

import com.arthur.sams.dto.CertificateStats;
import com.arthur.sams.entity.Certificate;
import com.arthur.sams.service.CertificateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 技能证书接口
 */
@RestController
@RequestMapping("/api/certificates")
@RequiredArgsConstructor
public class CertificateController {

    private final CertificateService certificateService;

    /** GET /api/certificates?category=计算机类 */
    @GetMapping
    public List<Certificate> list(@RequestParam(required = false) String category) {
        return certificateService.list(category);
    }

    @GetMapping("/stats")
    public CertificateStats stats() {
        return certificateService.stats();
    }

    @GetMapping("/{id}")
    public Certificate get(@PathVariable Long id) {
        return certificateService.get(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Certificate create(@RequestBody Certificate certificate) {
        return certificateService.create(certificate);
    }

    @PutMapping("/{id}")
    public Certificate update(@PathVariable Long id, @RequestBody Certificate certificate) {
        return certificateService.update(id, certificate);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        certificateService.delete(id);
    }
}
