package com.arthur.sams.service;

import com.arthur.sams.common.Utils;
import com.arthur.sams.dto.PracticeStats;
import com.arthur.sams.dto.PracticeYearGroup;
import com.arthur.sams.entity.Practice;
import com.arthur.sams.repository.PracticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * 实践档案（practice.html）
 */
@Service
@RequiredArgsConstructor
public class PracticeService {

    private final PracticeRepository practiceRepository;

    /** 实践列表，支持按类型筛选：项目经历 / 实习经历 / 校园实践 / 志愿活动 */
    public List<Practice> list(String type) {
        String t = Utils.blankToNull(type);
        return t == null
                ? practiceRepository.findAllByOrderByPracticeDateDesc()
                : practiceRepository.findByTypeOrderByPracticeDateDesc(t);
    }

    /** 按年份分组，前端时间线直接按年份渲染 */
    public List<PracticeYearGroup> groupedByYear() {
        Map<String, List<Practice>> map = new LinkedHashMap<>();
        for (Practice p : practiceRepository.findAllByOrderByPracticeDateDescIdAsc()) {
            map.computeIfAbsent(p.getYear(), k -> new ArrayList<>()).add(p);
        }
        return map.entrySet().stream()
                .map(e -> new PracticeYearGroup(e.getKey(), e.getValue()))
                .toList();
    }

    /** 顶部统计 */
    public PracticeStats stats() {
        return new PracticeStats(
                practiceRepository.count(),
                practiceRepository.countByType("项目经历"),
                practiceRepository.countByType("实习经历"),
                practiceRepository.countByType("校园实践"),
                practiceRepository.countByType("志愿活动")
        );
    }

    public Practice get(Long id) {
        return practiceRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("实践记录不存在：" + id));
    }

    public Practice create(Practice practice) {
        practice.setId(null);
        return practiceRepository.save(practice);
    }

    public Practice update(Long id, Practice form) {
        Practice db = get(id);
        db.setTitle(form.getTitle());
        db.setType(form.getType());
        db.setPracticeDate(form.getPracticeDate());
        db.setDescription(form.getDescription());
        db.setTechnologies(form.getTechnologies());
        db.setResult(form.getResult());
        return practiceRepository.save(db);
    }

    public void delete(Long id) {
        if (!practiceRepository.existsById(id)) {
            throw new NoSuchElementException("实践记录不存在：" + id);
        }
        practiceRepository.deleteById(id);
    }
}
