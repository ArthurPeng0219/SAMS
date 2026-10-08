/* ============================================================
   growth.js —— 成长分析
   GET /api/growth 一次性拿到：核心指标 + 成绩趋势 + 技能 + 时间线
   页面上的每个数字和每根柱子都是后端从数据库算出来的
   ============================================================ */

document.addEventListener('DOMContentLoaded', function () {
    init();
});

async function init() {
    try {
        renderHeader(await API.get('/profile'));
    } catch (error) {
        /* 忽略 */
    }
    try {
        const data = await API.get('/growth');
        renderGrowthStats(data.stats);
        renderAnalysis(data);
        renderGrowthTimeline(data.timeline);
    } catch (error) {
        showError(document.querySelector('.growth-stats'), error);
    }
}

function renderGrowthStats(stats) {
    const trendText = stats.avgScoreChange >= 0
        ? '较上学期 ↑ ' + stats.avgScoreChange
        : '较上学期 ↓ ' + Math.abs(stats.avgScoreChange);

    const cards = [
        ['平均成绩', stats.avgScore, trendText],
        ['已修课程', stats.courseCount, '完成度 ' + stats.completionRate + '%'],
        ['获得荣誉', stats.honorCount, '本学年 +' + stats.yearHonorCount],
        ['实践项目', stats.practiceCount, '本学年 +' + stats.yearPracticeCount]
    ];

    document.querySelector('.growth-stats').innerHTML = cards.map(function (item) {
        return '<div class="card growth-stat-card">'
            + '<span>' + esc(item[0]) + '</span>'
            + '<strong>' + esc(item[1]) + '</strong>'
            + '<small>' + esc(item[2]) + '</small>'
            + '</div>';
    }).join('');
}

function renderAnalysis(data) {
    document.querySelector('.analysis-grid').innerHTML =
        '<article class="card analysis-card">'
        + '<div class="analysis-header">'
        + '<div><h2>学习成绩趋势</h2><p>各学期平均成绩变化</p></div>'
        + '</div>'
        + renderChart(data.scoreTrend || [])
        + '</article>'

        + '<article class="card analysis-card">'
        + '<div class="analysis-header">'
        + '<div><h2>技能掌握情况</h2><p>当前技能学习程度</p></div>'
        + '</div>'
        + '<div class="skill-progress-list">'
        + (data.skills || []).map(function (skill) {
            return '<div class="skill-progress">'
                + '<div class="skill-progress-header">'
                + '<span>' + esc(skill.name) + '</span>'
                + '<strong>' + esc(skill.level) + '%</strong>'
                + '</div>'
                + '<div class="progress">'
                + '<div class="progress-value" style="width: ' + esc(skill.level) + '%;"></div>'
                + '</div>'
                + '</div>';
        }).join('')
        + '</div>'
        + '</article>';
}

/* 柱状图：y 轴刻度 60~100，柱高按「成绩在 60-100 区间里的位置」换算 */
function renderChart(trend) {
    const yAxis = ['100', '90', '80', '70', '60']
        .map(function (value) {
            return '<span>' + value + '</span>';
        }).join('');

    const gridLines = '<div class="chart-grid-line"></div>'
        + '<div class="chart-grid-line"></div>'
        + '<div class="chart-grid-line"></div>'
        + '<div class="chart-grid-line"></div>';

    const bars = trend.map(function (item) {
        const height = Math.max(4, Math.min(95, (item.avgScore - 60) / 40 * 100));
        return '<div class="bar-group">'
            + '<div class="bar" style="height: ' + height.toFixed(1) + '%;" '
            + 'title="' + esc(item.term) + ' 平均 ' + esc(item.avgScore) + ' 分"></div>'
            + '<span>' + esc(item.term) + '</span>'
            + '</div>';
    }).join('');

    return '<div class="chart">'
        + '<div class="chart-y-axis">' + yAxis + '</div>'
        + '<div class="chart-content">' + gridLines
        + '<div class="bar-chart">' + (bars || '<span style="color:#909399;font-size:13px;">暂无成绩数据</span>') + '</div>'
        + '</div>'
        + '</div>';
}

function renderGrowthTimeline(timeline) {
    const events = timeline || [];
    document.querySelector('.growth-timeline').innerHTML = events.length
        ? events.map(function (event) {
            return '<article class="growth-event">'
                + '<div class="growth-dot"></div>'
                + '<div class="growth-event-content">'
                + '<span class="growth-year">' + esc(event.year) + '</span>'
                + '<h3>' + esc(event.title) + '</h3>'
                + '<p>' + esc(event.content) + '</p>'
                + '</div>'
                + '</article>';
        }).join('')
        : '<div style="color:#909399;">暂无成长记录</div>';
}
