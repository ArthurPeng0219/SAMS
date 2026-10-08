/* ============================================================
   grades.js —— 学习档案
   GET /api/grades/stats 统计，GET /api/grades?keyword= 列表
   ============================================================ */

let gradeKeyword = '';

document.addEventListener('DOMContentLoaded', function () {
    init();
});

async function init() {
    try {
        renderHeader(await API.get('/profile'));
    } catch (error) {
        /* 顶部用户信息拿不到不影响主内容 */
    }
    loadGradeStats();
    loadGradeList();

    const search = document.querySelector('.search-input');
    if (search) {
        search.addEventListener('input', debounce(function () {
            gradeKeyword = search.value.trim();
            loadGradeList();
        }, 300));
    }
}

async function loadGradeStats() {
    const box = document.querySelector('.statistics');
    try {
        const stats = await API.get('/grades/stats');
        const cards = [
            ['平均成绩', stats.avgScore],
            ['最高成绩', stats.maxScore],
            ['已修课程', stats.courseCount],
            ['总学分', stats.totalCredit]
        ];
        box.innerHTML = cards.map(function (item) {
            return '<div class="card stat-card">'
                + '<div class="stat-title">' + esc(item[0]) + '</div>'
                + '<div class="stat-number">' + esc(item[1]) + '</div>'
                + '</div>';
        }).join('');
    } catch (error) {
        showError(box, error);
    }
}

async function loadGradeList() {
    const tbody = document.querySelector('.data-table tbody');
    try {
        const path = gradeKeyword ? '/grades?keyword=' + encodeURIComponent(gradeKeyword) : '/grades';
        const courses = await API.get(path);
        if (!courses.length) {
            tbody.innerHTML = '<tr><td colspan="5" style="text-align:center;color:#909399;">没有匹配的课程</td></tr>';
            return;
        }
        tbody.innerHTML = courses.map(function (course) {
            return '<tr>'
                + '<td>' + esc(course.name) + '</td>'
                + '<td>' + esc(course.term) + '</td>'
                + '<td>' + esc(course.credit) + '</td>'
                + '<td>' + esc(course.score) + '</td>'
                + '<td><span class="status ' + statusClass(course.statusClass) + '">'
                + esc(course.status) + '</span></td>'
                + '</tr>';
        }).join('');
    } catch (error) {
        showError(document.querySelector('.table-wrapper'), error);
    }
}
