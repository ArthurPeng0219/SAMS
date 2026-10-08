/* ============================================================
   resume.js —— 求职档案
   GET /api/resume 一次性拿到：学生信息 + 求职档案 + 技能 + 项目经历
   ============================================================ */

let currentResume = null;

document.addEventListener('DOMContentLoaded', function () {
    init();
});

async function init() {
    try {
        const data = await API.get('/resume');
        currentResume = data.resume;
        renderHeader(data.student);
        renderResumeLeft(data);
        renderResumeRight(data);
        bindResumeEdit();
    } catch (error) {
        showError(document.querySelector('.resume-layout'), error);
    }
}

/* 「编辑档案」：改的是数据库里的 resume 记录 */
function bindResumeEdit() {
    const button = document.querySelector('.page-header .btn-primary');
    if (!button) {
        return;
    }
    button.addEventListener('click', async function () {
        const resume = currentResume;
        if (!resume) {
            return;
        }
        const targetPosition = ask('目标职位：', resume.targetPosition);
        if (targetPosition === null) {
            return;
        }
        const workCity = ask('工作地点：', resume.workCity);
        if (workCity === null) {
            return;
        }
        const workType = ask('工作类型：', resume.workType);
        if (workType === null) {
            return;
        }
        const industry = ask('期望行业：', resume.industry);
        if (industry === null) {
            return;
        }
        const advantage = ask('个人优势：', resume.advantage);
        if (advantage === null) {
            return;
        }
        try {
            const data = await API.put('/resume', {
                advantage: advantage,
                educationSchool: resume.educationSchool,
                educationMajor: resume.educationMajor,
                educationPeriod: resume.educationPeriod,
                targetPosition: targetPosition,
                workCity: workCity,
                workType: workType,
                industry: industry,
                jobStatus: resume.jobStatus
            });
            currentResume = data.resume;
            renderResumeLeft(data);
            renderResumeRight(data);
            toast('求职档案已保存到数据库');
        } catch (error) {
            toast(error.message, true);
        }
    });
}

function renderResumeLeft(data) {
    const student = data.student;
    const resume = data.resume || {};

    document.querySelector('.resume-left').innerHTML =
        '<article class="card resume-card profile-card">'
        + '<div class="profile-avatar">' + esc(student.avatarText || student.name.slice(0, 1)) + '</div>'
        + '<h2>' + esc(student.name) + '</h2>'
        + '<p class="profile-major">' + esc(student.major) + '</p>'
        + '<div class="profile-info">'
        + '<div><span>邮箱</span><strong>' + esc(student.email) + '</strong></div>'
        + '<div><span>电话</span><strong>' + esc(student.phone) + '</strong></div>'
        + '<div><span>所在地</span><strong>' + esc(student.city) + '</strong></div>'
        + '</div>'
        + '</article>'

        + '<article class="card resume-card">'
        + '<div class="card-title"><h2>教育经历</h2></div>'
        + '<div class="education-item">'
        + '<h3>' + esc(resume.educationSchool) + '</h3>'
        + '<p>' + esc(resume.educationMajor) + '</p>'
        + '<span>' + esc(resume.educationPeriod) + '</span>'
        + '</div>'
        + '</article>'

        + '<article class="card resume-card">'
        + '<div class="card-title"><h2>个人优势</h2></div>'
        + '<p class="advantage-text">' + esc(resume.advantage) + '</p>'
        + '</article>';
}

function renderResumeRight(data) {
    const resume = data.resume || {};
    const skills = data.skills || [];
    const projects = data.projects || [];

    const skillTags = skills.map(function (skill) {
        return '<span class="skill-tag">' + esc(skill.name) + '</span>';
    }).join('');

    const projectHtml = projects.map(function (project) {
        const tags = (project.techList || []).map(function (tech) {
            return '<span class="skill-tag">' + esc(tech) + '</span>';
        }).join('');
        return '<div class="resume-project">'
            + '<div class="project-header">'
            + '<h3>' + esc(project.title) + '</h3>'
            + '<span>' + esc(project.year) + '</span>'
            + '</div>'
            + '<p>' + esc(project.description) + '</p>'
            + '<div class="skill-list">' + tags + '</div>'
            + '</div>';
    }).join('');

    document.querySelector('.resume-right').innerHTML =
        '<article class="card resume-card">'
        + '<div class="card-title">'
        + '<h2>求职意向</h2>'
        + '<span class="status-tag">' + esc(resume.jobStatus) + '</span>'
        + '</div>'
        + '<div class="job-intention">'
        + '<div class="intention-item"><span>目标职位</span><strong>' + esc(resume.targetPosition) + '</strong></div>'
        + '<div class="intention-item"><span>工作地点</span><strong>' + esc(resume.workCity) + '</strong></div>'
        + '<div class="intention-item"><span>工作类型</span><strong>' + esc(resume.workType) + '</strong></div>'
        + '<div class="intention-item"><span>期望行业</span><strong>' + esc(resume.industry) + '</strong></div>'
        + '</div>'
        + '</article>'

        + '<article class="card resume-card">'
        + '<div class="card-title"><h2>技能优势</h2></div>'
        + '<div class="skill-list">' + skillTags + '</div>'
        + '</article>'

        + '<article class="card resume-card">'
        + '<div class="card-title">'
        + '<h2>项目经历</h2>'
        + '<a href="practice.html">查看全部</a>'
        + '</div>'
        + (projectHtml || '<p style="color:#909399;">暂无项目经历</p>')
        + '</article>';
}
