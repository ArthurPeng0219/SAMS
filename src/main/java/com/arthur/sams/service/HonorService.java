package com.arthur.sams.service;

import com.arthur.sams.common.Utils;
import com.arthur.sams.dto.HonorStats;
import com.arthur.sams.entity.Honor;
import com.arthur.sams.repository.HonorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * 荣誉档案（honors.html）
 */
@Service
@RequiredArgsConstructor
public class HonorService {

    private final HonorRepository honorRepository;

    /** 荣誉列表，支持按类型筛选：奖学金 / 比赛 / 荣誉 / 其他 */
    public List<Honor> list(String type) {
        String t = Utils.blankToNull(type);
        return t == null
                ? honorRepository.findAllByOrderByHonorDateDesc()
                : honorRepository.findByTypeOrderByHonorDateDesc(t);
    }

    /** 顶部统计 */
    public HonorStats stats() {
        return new HonorStats(
                honorRepository.count(),
                honorRepository.countByLevel("校级"),
                honorRepository.countByLevel("院级"),
                honorRepository.countByType("比赛")
        );
    }

    public Honor get(Long id) {
        return honorRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("荣誉不存在：" + id));
    }

    public Honor create(Honor honor) {
        honor.setId(null);
        return honorRepository.save(honor);
    }

    public Honor update(Long id, Honor form) {
        Honor db = get(id);
        db.setTitle(form.getTitle());
        db.setDescription(form.getDescription());
        db.setType(form.getType());
        db.setLevel(form.getLevel());
        db.setHonorDate(form.getHonorDate());
        db.setIcon(form.getIcon());
        return honorRepository.save(db);
    }

    public void delete(Long id) {
        if (!honorRepository.existsById(id)) {
            throw new NoSuchElementException("荣誉不存在：" + id);
        }
        honorRepository.deleteById(id);
    }
}
