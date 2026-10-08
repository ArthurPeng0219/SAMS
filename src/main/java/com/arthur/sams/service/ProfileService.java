package com.arthur.sams.service;

import com.arthur.sams.common.Utils;
import com.arthur.sams.dto.ProfileUpdateRequest;
import com.arthur.sams.dto.ResumeView;
import com.arthur.sams.entity.Resume;
import com.arthur.sams.entity.Student;
import com.arthur.sams.repository.PracticeRepository;
import com.arthur.sams.repository.ResumeRepository;
import com.arthur.sams.repository.SkillRepository;
import com.arthur.sams.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

/**
 * 个人信息（profile.html）+ 求职档案（resume.html）
 */
@Service
@RequiredArgsConstructor
public class ProfileService {

    private final StudentRepository studentRepository;
    private final ResumeRepository resumeRepository;
    private final SkillRepository skillRepository;
    private final PracticeRepository practiceRepository;

    public Student currentStudent() {
        return studentRepository.findFirstByOrderByIdAsc()
                .orElseThrow(() -> new NoSuchElementException("学生信息不存在"));
    }

    /** 修改个人信息页面的联系方式并保存 */
    @Transactional
    public Student updateContact(ProfileUpdateRequest form) {
        Student student = currentStudent();
        student.setPhone(form.phone());
        student.setEmail(form.email());
        student.setCity(form.city());
        student.setGithub(form.github());
        return studentRepository.save(student);
    }

    /** 求职档案页面整体数据 */
    public ResumeView resume() {
        Student student = currentStudent();
        Resume resume = resumeRepository.findByStudentId(student.getId())
                .or(() -> resumeRepository.findFirstByOrderByIdAsc())
                .orElseThrow(() -> new NoSuchElementException("求职档案不存在"));
        return new ResumeView(
                student,
                resume,
                skillRepository.findAllByOrderBySortOrderAsc(),
                practiceRepository.findByTypeOrderByPracticeDateDesc("项目经历")
        );
    }

    /** 更换头像文字 */
    @Transactional
    public Student updateAvatar(String avatarText) {
        Student student = currentStudent();
        String text = Utils.blankToNull(avatarText);
        if (text != null && text.length() > 2) {
            text = text.substring(0, 2);
        }
        student.setAvatarText(text);
        return studentRepository.save(student);
    }

    /**
     * 修改求职档案（求职档案页面「编辑档案」按钮）
     * 只覆盖允许编辑的字段，其余保持数据库里的原值
     */
    @Transactional
    public ResumeView updateResume(Resume form) {
        Student student = currentStudent();
        Resume resume = resumeRepository.findByStudentId(student.getId())
                .or(() -> resumeRepository.findFirstByOrderByIdAsc())
                .orElseThrow(() -> new NoSuchElementException("求职档案不存在"));

        resume.setAdvantage(form.getAdvantage());
        resume.setEducationSchool(form.getEducationSchool());
        resume.setEducationMajor(form.getEducationMajor());
        resume.setEducationPeriod(form.getEducationPeriod());
        resume.setTargetPosition(form.getTargetPosition());
        resume.setWorkCity(form.getWorkCity());
        resume.setWorkType(form.getWorkType());
        resume.setIndustry(form.getIndustry());
        resume.setJobStatus(form.getJobStatus());
        resumeRepository.save(resume);

        return resume();
    }
}
