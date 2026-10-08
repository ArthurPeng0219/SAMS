/* ============================================================
   practice.js —— 实践档案
   GET /api/practices/stats 统计，GET /api/practices/grouped 按年份分组的时间线
   ============================================================ */

let practiceType = '';
let practiceList = [];

document.addEventListener('DOMContentLoaded', function () {
    init();
});

async function init() {
    try {
        renderHeader(await API.get('/profile'));
    } catch (error) {
        /* 忽略 */
    }
    loadPracticeStats();
    loadPracticeTimeline();
    bindPracticeFilter();
    bindPracticeAdd();
    bindPracticeActions();
}

async function loadPracticeStats() {
    const box = document.querySelector('.statistics');
    if (!box) {
        return;
    }
    try {
        const stats = await API.get('/practices/stats');
        const cards = [
            ['实践总数', stats.total],
            ['项目经历', stats.projectCount],
            ['实习经历', stats.internCount],
            ['校园实践', stats.campusCount]
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

/** 时间线主体：既负责渲染，也顺手记下当前列表供编辑/删除用 */
async function loadPracticeTimeline() {
    const timeline = document.querySelector('.practice-timeline');
    try {
        if (practiceType) {
            practiceList = await API.get('/practices?type=' + encodeURIComponent(practiceType));
            timeline.innerHTML = practiceList.length
                ? '<div class="timeline-year"><div class="year-title">' + esc(practiceType) + '</div>'
                    + practiceList.map(practiceItemHtml).join('') + '</div>'
                : '<div style="padding:20px;color:#909399;">该分类下暂无记录</div>';
        } else {
            const groups = await API.get('/practices/grouped');
            practiceList = [];
            groups.forEach(function (group) {
                group.items.forEach(function (item) {
                    practiceList.push(item);
                });
            });
            timeline.innerHTML = groups.length
                ? groups.map(function (group) {
                    return '<div class="timeline-year">'
                        + '<div class="year-title">' + esc(group.year) + '</div>'
                        + group.items.map(practiceItemHtml).join('')
                        + '</div>';
                }).join('')
                : '<div style="padding:20px;color:#909399;">暂无实践记录</div>';
        }
    } catch (error) {
        showError(timeline, error);
    }
}

/* 类型 → 徽章样式类（practice.css 里定义了 project / campus 两种） */
function practiceTypeClass(type) {
    if (type === '项目经历') {
        return 'project';
    }
    if (type === '校园实践') {
        return 'campus';
    }
    return '';
}

function practiceItemHtml(practice) {
    const badges = (practice.techList || []).map(function (tech) {
        return '<span class="tech-tag">' + esc(tech) + '</span>';
    }).join('');
    return '<article class="practice-item">'
        + '<div class="timeline-dot"></div>'
        + '<div class="card practice-card">'
        + '<div class="practice-header">'
        + '<div>'
        + '<span class="practice-type ' + practiceTypeClass(practice.type) + '">' + esc(practice.type) + '</span>'
        + '<h2>' + esc(practice.title) + '</h2>'
        + '</div>'
        + '<span class="practice-date">' + esc(practice.practiceDate) + '</span>'
        + '</div>'
        + '<p class="practice-description">' + esc(practice.description) + '</p>'
        + '<div class="technology-list">' + badges + '</div>'
        + '<div class="practice-result">'
        + '<strong>实践成果</strong>'
        + '<p>' + esc(practice.result) + '</p>'
        + '</div>'
        + '<div class="practice-actions">'
        + '<button class="btn" data-act="view" data-id="' + practice.id + '">查看</button>'
        + '<button class="btn" data-act="edit" data-id="' + practice.id + '">编辑</button>'
        + '<button class="btn" data-act="del" data-id="' + practice.id + '">删除</button>'
        + '</div>'
        + '</div>'
        + '</article>';
}

function bindPracticeFilter() {
    const panel = document.querySelector('.filter-panel');
    if (!panel) {
        return;
    }
    panel.querySelectorAll('.filter-item').forEach(function (button) {
        button.addEventListener('click', function () {
            panel.querySelectorAll('.filter-item').forEach(function (item) {
                item.classList.remove('active');
            });
            button.classList.add('active');
            const text = button.textContent.trim();
            practiceType = (text === '全部') ? '' : text;
            loadPracticeTimeline();
        });
    });
}

function bindPracticeAdd() {
    const button = document.querySelector('.page-header .btn-primary');
    if (!button) {
        return;
    }
    button.addEventListener('click', async function () {
        const title = ask('实践 / 项目名称：');
        if (title === null) {
            return;
        }
        if (!title) {
            toast('名称不能为空', true);
            return;
        }
        const type = ask('类型（项目经历 / 实习经历 / 校园实践 / 志愿活动）：', '项目经历');
        if (type === null) {
            return;
        }
        const date = ask('时间（yyyy-MM）：', new Date().toISOString().slice(0, 7));
        if (date === null) {
            return;
        }
        const description = ask('描述：');
        if (description === null) {
            return;
        }
        const technologies = ask('技术栈 / 关键词（用英文逗号分隔）：');
        if (technologies === null) {
            return;
        }
        const result = ask('实践成果：');
        if (result === null) {
            return;
        }
        try {
            await API.post('/practices', {
                title: title,
                type: type || '项目经历',
                practiceDate: date,
                description: description,
                technologies: technologies,
                result: result
            });
            toast('实践记录已添加');
            loadPracticeStats();
            loadPracticeTimeline();
        } catch (error) {
            toast(error.message, true);
        }
    });
}

function bindPracticeActions() {
    const timeline = document.querySelector('.practice-timeline');
    if (!timeline) {
        return;
    }
    timeline.addEventListener('click', async function (event) {
        const button = event.target.closest('button[data-act]');
        if (!button) {
            return;
        }
        const id = Number(button.getAttribute('data-id'));
        const item = practiceList.find(function (row) {
            return row.id === id;
        });
        if (!item) {
            return;
        }
        const act = button.getAttribute('data-act');

        if (act === 'view') {
            window.alert('【' + item.title + '】\n类型：' + (item.type || '-')
                + '\n时间：' + (item.practiceDate || '-')
                + '\n技术栈：' + (item.technologies || '-')
                + '\n\n描述：' + (item.description || '-')
                + '\n\n成果：' + (item.result || '-'));
            return;
        }

        if (act === 'del') {
            if (!window.confirm('确定删除「' + item.title + '」吗？')) {
                return;
            }
            try {
                await API.del('/practices/' + id);
                toast('已删除');
                loadPracticeStats();
                loadPracticeTimeline();
            } catch (error) {
                toast(error.message, true);
            }
            return;
        }

        const type = ask('类型：', item.type);
        if (type === null) {
            return;
        }
        const date = ask('时间（yyyy-MM）：', item.practiceDate);
        if (date === null) {
            return;
        }
        const technologies = ask('技术栈（英文逗号分隔）：', item.technologies);
        if (technologies === null) {
            return;
        }
        const result = ask('实践成果：', item.result);
        if (result === null) {
            return;
        }
        try {
            await API.put('/practices/' + id, {
                title: item.title,
                type: type,
                practiceDate: date,
                description: item.description,
                technologies: technologies,
                result: result
            });
            toast('已保存');
            loadPracticeTimeline();
        } catch (error) {
            toast(error.message, true);
        }
    });
}
