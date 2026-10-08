package com.arthur.sams.init;

import com.arthur.sams.entity.*;
import com.arthur.sams.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 首次启动时把原本写死在 HTML 里的静态数据灌进数据库。
 * 之后所有页面数据都从数据库读，改数据用页面上的按钮或直接改库即可。
 * <p>
 * 幂等：只要 student 表里有数据就不重复插入。
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final StudentRepository studentRepository;
    private final ResumeRepository resumeRepository;
    private final CourseRepository courseRepository;
    private final HonorRepository honorRepository;
    private final CertificateRepository certificateRepository;
    private final CourseMaterialRepository materialRepository;
    private final PracticeRepository practiceRepository;
    private final SkillRepository skillRepository;
    private final TimelineEventRepository timelineEventRepository;
    private final SystemSettingRepository systemSettingRepository;

    @Override
    @Transactional
    public void run(String... args) {
        if (studentRepository.count() > 0) {
            log.info("数据库中已有数据，跳过初始化");
            return;
        }
        log.info("开始初始化 SAMS 演示数据 ...");

        Student student = new Student(null, "Arthur", "2024031001", "数据科学与大数据技术",
                "数据科学2班", 2024, "138****8888", "example@email.com", "重庆",
                "https://github.com/ArthurPeng0219", "A", "Arthur");
        studentRepository.save(student);

        resumeRepository.save(new Resume(null, student.getId(),
                "具备 Python、SQL、Java 等编程基础，对数据分析、机器学习和后端开发具有较强兴趣。"
                        + "正在通过实际项目不断提升自己的工程实践能力。",
                "重庆科技大学", "数据科学与大数据技术", "2024 - 2028",
                "数据分析师", "重庆 / 成都", "实习 / 全职", "互联网 / 科技", "求职中"));

        // ===== 学习档案：16 门课，覆盖 大一上 ~ 大三上，共 52 学分 =====
        courseRepository.saveAll(List.of(
                course("高等数学A", "大一上", 5, 85.0),
                course("程序设计基础（Python）", "大一上", 4, 91.0),
                course("大学英语（一）", "大一上", 3, 82.0),
                course("计算机导论", "大一上", 3, 88.0),

                course("线性代数", "大一下", 3, 84.0),
                course("数据结构", "大一下", 4, 87.0),
                course("大学英语（二）", "大一下", 3, 86.0),
                course("概率论与数理统计", "大一下", 3, 89.0),

                course("Java程序设计", "大二上", 3, 92.0),
                course("Python数据分析", "大二上", 3, 88.0),
                course("数据库原理", "大二上", 3, 90.0),
                course("计算机网络", "大二上", 3, 81.0),

                course("机器学习", "大二下", 3, 85.0),
                course("大数据技术基础", "大二下", 3, 90.0),

                course("数据挖掘", "大三上", 3, 93.0),
                course("Web应用开发", "大三上", 3, 89.0)
        ));

        // ===== 荣誉档案 =====
        honorRepository.saveAll(List.of(
                honor("优秀学生奖学金", "因学习成绩优秀，获得学校优秀学生奖学金。", "奖学金", "校级", "2026-06", "奖"),
                honor("大学生创新创业项目", "参加基于 YOLOv8 的传统工业缺陷识别项目并顺利结题。", "比赛", "校级", "2026-05", "奖"),
                honor("优秀志愿者", "参与校园志愿服务活动并获得优秀志愿者称号。", "荣誉", "院级", "2026-04", "荣"),
                honor("校级程序设计竞赛二等奖", "参加校级程序设计竞赛，获二等奖。", "比赛", "校级", "2025-11", "赛"),
                honor("优秀学生干部", "担任班级学习委员，被评为优秀学生干部。", "荣誉", "院级", "2025-10", "荣"),
                honor("三好学生", "学年综合表现优秀，被评为三好学生。", "荣誉", "校级", "2025-06", "荣")
        ));

        // ===== 技能证书：计算机类 3 / 英语类 2 / 专业类 3 =====
        certificateRepository.saveAll(List.of(
                certificate("全国计算机等级考试二级", "计算机类", "教育部考试中心", "2026-03",
                        "NCRE-2026-0031", "已获得", "C", "考试成绩", "92"),
                certificate("全国计算机等级考试三级（数据库技术）", "计算机类", "教育部考试中心", "2026-09",
                        "NCRE-2026-0917", "已获得", "C", "考试成绩", "88"),
                certificate("计算机技术与软件专业技术资格（初级）", "计算机类", "工信部教育与考试中心", "2026-05",
                        "RJ-2026-2210", "已获得", "C", "考试成绩", "76"),
                certificate("大学英语六级", "英语类", "全国大学英语四六级考试委员会", "2026-06",
                        "CET6-2026-1188", "已获得", "E", "考试成绩", "520"),
                certificate("大学英语四级", "英语类", "全国大学英语四六级考试委员会", "2025-12",
                        "CET4-2025-2266", "已获得", "E", "考试成绩", "486"),
                certificate("Python相关技能认证", "专业类", "专业认证机构", "2026-05",
                        "PY-2026-0088", "已获得", "P", "认证等级", "中级"),
                certificate("数据分析师（CDA Level I）", "专业类", "中国商业统计学会", "2026-08",
                        "CDA-2026-0451", "已获得", "D", "考试成绩", "82"),
                certificate("华为 HCIA-Big Data 认证", "专业类", "华为技术有限公司", "2026-09",
                        "HCIA-BD-2026-0127", "已获得", "H", "考试成绩", "890")
        ));

        // ===== 课程资料：Java 4 / Python 3 / 数据库 2 / 机器学习 2 / 其他 1 =====
        materialRepository.saveAll(List.of(
                material("Java程序设计 - 第一章.pdf", "Java程序设计", "Java", "PDF", "2.4 MB", "2026-09-01"),
                material("Java实验报告 - 实验一.docx", "Java程序设计", "Java", "DOCX", "1.2 MB", "2026-09-03"),
                material("Java程序设计 - 面向对象课件.pptx", "Java程序设计", "Java", "PPTX", "4.6 MB", "2026-09-08"),
                material("Java程序设计 - 集合与泛型笔记.pdf", "Java程序设计", "Java", "PDF", "1.8 MB", "2026-09-15"),
                material("Python数据分析 - 第三章.pptx", "Python数据分析", "Python", "PPTX", "3.8 MB", "2026-09-04"),
                material("Python数据分析 - Pandas分组聚合.ipynb", "Python数据分析", "Python", "IPYNB", "0.6 MB", "2026-09-11"),
                material("Python数据分析 - 实验二报告.docx", "Python数据分析", "Python", "DOCX", "1.4 MB", "2026-09-18"),
                material("数据库原理 - 实验报告.pdf", "数据库原理", "数据库", "PDF", "1.6 MB", "2026-09-06"),
                material("数据库原理 - SQL窗口函数笔记.pdf", "数据库原理", "数据库", "PDF", "0.9 MB", "2026-09-20"),
                material("机器学习 - 第四章 决策树.pptx", "机器学习", "机器学习", "PPTX", "5.2 MB", "2026-09-10"),
                material("机器学习 - 模型评估实验.docx", "机器学习", "机器学习", "DOCX", "2.1 MB", "2026-09-22"),
                material("数据可视化 - ECharts使用手册.pdf", "数据可视化", "其他", "PDF", "3.3 MB", "2026-09-25")
        ));

        // ===== 实践档案 =====
        practiceRepository.saveAll(List.of(
                practice("传统工业缺陷识别项目", "项目经历", "2026-06",
                        "基于 YOLOv8 构建传统工业缺陷识别系统，负责数据处理、模型训练以及目标检测实验。",
                        "YOLOv8,PyTorch,OpenCV,Python",
                        "完成 PCB 缺陷检测模型训练，实现基本的缺陷识别功能。"),
                practice("学生个人档案管理系统", "项目经历", "2026-09",
                        "设计并开发学生个人资料归档管理系统，用于统一管理学生大学期间的学习、荣誉、证书、课程资料和实践经历。",
                        "Java,Spring Boot,MySQL,HTML,CSS,JavaScript",
                        "完成系统需求分析、功能模块设计、静态页面原型以及后端 REST 接口开发。"),
                practice("数据分析实习生", "实习经历", "2026-07",
                        "在重庆某科技有限公司数据部门实习，参与销售数据的清洗、统计与可视化工作。",
                        "Python,SQL,Pandas,Matplotlib",
                        "独立完成销售数据清洗与可视化，输出周度分析报告 6 份。"),
                practice("校园实践活动", "校园实践", "2025-10",
                        "参与校园组织的实践活动，负责活动资料整理以及相关工作。",
                        "Office,团队协作",
                        "完成活动资料整理和团队协作任务。"),
                practice("程序设计集训", "校园实践", "2025-11",
                        "参加计算机学院组织的程序设计集训，系统训练算法与数据结构。",
                        "C++,算法,数据结构",
                        "完成 60 道算法题训练，通过校内选拔。"),
                practice("迎新志愿活动", "志愿活动", "2026-09",
                        "参与数据科学学院迎新志愿活动，负责新生报到引导与材料分发。",
                        "沟通协作",
                        "累计志愿服务时长 24 小时，获评优秀志愿者。")
        ));

        // ===== 技能 =====
        skillRepository.saveAll(List.of(
                skill("Python", 85, 1),
                skill("SQL", 75, 2),
                skill("Java", 65, 3),
                skill("Pandas", 70, 4),
                skill("MySQL", 72, 5),
                skill("NumPy", 60, 6),
                skill("Matplotlib", 62, 7),
                skill("Git", 68, 8)
        ));

        // ===== 成长时间线 =====
        timelineEventRepository.saveAll(List.of(
                timeline(null, 2024, "进入大学",
                        "开始学习数据科学与大数据技术，建立计算机基础知识体系。", 1),
                timeline(null, 2025, "开始系统学习 Python 与 SQL",
                        "开始进行数据分析、数据库以及数据可视化相关学习。", 2),
                timeline(null, 2026, "进入机器学习与项目实践阶段",
                        "学习机器学习、深度学习以及 Java Web，并开始进行实际项目开发。", 3),
                timeline(null, 2027, "实习与求职",
                        "通过实习和项目进一步积累工程经验，为毕业求职做好准备。", 4)
        ));

        // ===== 系统设置开关 =====
        systemSettingRepository.saveAll(List.of(
                setting("autoSave", "自动保存", "编辑档案时自动保存修改内容", true, 1),
                setting("notify", "消息通知", "接收系统的重要通知", true, 2),
                setting("dataReminder", "数据提醒", "提醒你及时更新个人档案", false, 3),
                setting("darkMode", "深色模式", "使用深色主题显示系统界面", false, 4)
        ));

        log.info("SAMS 演示数据初始化完成：课程 {} 条、荣誉 {} 条、证书 {} 条、资料 {} 条、实践 {} 条",
                courseRepository.count(), honorRepository.count(), certificateRepository.count(),
                materialRepository.count(), practiceRepository.count());
    }

    // ---------- 下面是小工厂方法，让上面的数据一目了然 ----------

    private Course course(String name, String term, Integer credit, Double score) {
        return new Course(null, name, term, credit, score);
    }

    private Honor honor(String title, String description, String type, String level, String date, String icon) {
        return new Honor(null, title, description, type, level, date, icon);
    }

    private Certificate certificate(String name, String category, String issuer, String date,
                                    String certNo, String status, String icon,
                                    String extraLabel, String extraValue) {
        return new Certificate(null, name, category, issuer, date, certNo, status, icon, extraLabel, extraValue);
    }

    private CourseMaterial material(String fileName, String courseName, String category,
                                    String fileType, String fileSize, String uploadDate) {
        return new CourseMaterial(null, fileName, courseName, category, fileType, fileSize, uploadDate);
    }

    private Practice practice(String title, String type, String date, String description,
                              String technologies, String result) {
        return new Practice(null, title, type, date, description, technologies, result);
    }

    private Skill skill(String name, Integer level, Integer sortOrder) {
        return new Skill(null, name, level, sortOrder);
    }

    private TimelineEvent timeline(Long id, Integer year, String title, String content, Integer sortOrder) {
        return new TimelineEvent(id, year, title, content, sortOrder);
    }

    private SystemSetting setting(String key, String label, String description, Boolean enabled, Integer sortOrder) {
        return new SystemSetting(null, key, label, description, enabled, sortOrder);
    }
}
