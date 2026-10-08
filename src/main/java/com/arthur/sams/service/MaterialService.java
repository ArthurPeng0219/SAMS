package com.arthur.sams.service;

import com.arthur.sams.common.Utils;
import com.arthur.sams.dto.MaterialStats;
import com.arthur.sams.entity.CourseMaterial;
import com.arthur.sams.repository.CourseMaterialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * 课程资料（courses.html）
 */
@Service
@RequiredArgsConstructor
public class MaterialService {

    private final CourseMaterialRepository materialRepository;

    /** 资料列表，支持「分类 + 文件名搜索」组合查询 */
    public List<CourseMaterial> list(String category, String keyword) {
        String c = Utils.blankToNull(category);
        String kw = Utils.blankToNull(keyword);
        if (c != null && kw != null) {
            return materialRepository.findByCategoryAndFileNameContainingOrderByUploadDateDesc(c, kw);
        }
        if (c != null) {
            return materialRepository.findByCategoryOrderByUploadDateDesc(c);
        }
        if (kw != null) {
            return materialRepository.findByFileNameContainingOrderByUploadDateDesc(kw);
        }
        return materialRepository.findAllByOrderByUploadDateDesc();
    }

    /** 顶部统计 */
    public MaterialStats stats() {
        return new MaterialStats(
                materialRepository.count(),
                materialRepository.countByCategory("Java"),
                materialRepository.countByCategory("Python"),
                materialRepository.countByCategory("数据库"),
                materialRepository.countByCategory("机器学习"),
                materialRepository.countByCategory("其他")
        );
    }

    public CourseMaterial get(Long id) {
        return materialRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("资料不存在：" + id));
    }

    public CourseMaterial create(CourseMaterial material) {
        material.setId(null);
        return materialRepository.save(material);
    }

    public CourseMaterial update(Long id, CourseMaterial form) {
        CourseMaterial db = get(id);
        db.setFileName(form.getFileName());
        db.setCourseName(form.getCourseName());
        db.setCategory(form.getCategory());
        db.setFileType(form.getFileType());
        db.setFileSize(form.getFileSize());
        db.setUploadDate(form.getUploadDate());
        return materialRepository.save(db);
    }

    public void delete(Long id) {
        if (!materialRepository.existsById(id)) {
            throw new NoSuchElementException("资料不存在：" + id);
        }
        materialRepository.deleteById(id);
    }
}
