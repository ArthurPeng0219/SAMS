/* ============================================================
   index.js —— 首页
   数据来源：GET /api/dashboard
   ============================================================ */

document.addEventListener('DOMContentLoaded', function () {
    init();
});

async function init() {
    try {
        const data = await API.get('/dashboard');
        renderHeader(data.student);

        // 欢迎语
        document.querySelector('.welcome h1').textContent = '欢迎回来，' + data.student.name;

        renderStats(data);
        renderRecentCourses(data.recentCourses);
        renderRecentHonors(data.recentHonors);
        bindQuickLinks();
    } catch (error) {
        showError(document.querySelector('.statistics'), error);
    }
}

/* 顶部 4 个统计卡：数字全部来自数据库 */
function renderStats(data) {
    const cards = [
        ['平均成绩', data.gradeStats.avgScore],
        ['获得荣誉', data.honorCount],
        ['技能证书', data.certificateCount],
        ['个人作品', data.practiceCount]
    ];
    document.querySelector('.statistics').innerHTML = cards.map(function (item) {
        return '<div class="card stat-card">'
            + '<div class="stat-title">' + esc(item[0]) + '</div>'
            + '<div class="stat-number">' + esc(item[1]) + '</div>'
            + '</div>';
    }).join('');
}

/* 最近学习成绩 */
function renderRecentCourses(courses) {
    const box = document.querySelector('.score-list');
    if (!box) {
        return;
    }
    box.innerHTML = courses.map(function (course) {
        return '<div class="score-item">'
            + '<span>' + esc(course.name) + '</span>'
            + '<strong>' + esc(course.score) + '</strong>'
            + '</div>';
    }).join('');
}

/* 最近获得的荣誉 */
function renderRecentHonors(honors) {
    const box = document.querySelector('.honor-list');
    if (!box) {
        return;
    }
    box.innerHTML = honors.map(function (honor) {
        return '<div class="honor-item">'
            + '<div class="honor-icon">' + esc(honor.icon) + '</div>'
            + '<div>'
            + '<h3>' + esc(honor.title) + '</h3>'
            + '<p>' + esc(honor.honorDate) + '</p>'
            + '</div>'
            + '</div>';
    }).join('');
}

/* 把快速入口的 # 换成真实页面地址 */
function bindQuickLinks() {
    const links = document.querySelectorAll('.quick-links .quick-item');
    const targets = ['profile.html', 'grades.html', 'certificates.html', 'resume.html'];
    links.forEach(function (link, index) {
        if (targets[index]) {
            link.setAttribute('href', targets[index]);
        }
    });
}
