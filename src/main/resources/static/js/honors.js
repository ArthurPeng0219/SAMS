/* ============================================================
   honors.js —— 荣誉档案
   GET /api/honors/stats 统计，GET /api/honors?type= 列表
   按钮：+ 添加荣誉（POST）、编辑（PUT）、删除（DELETE）
   ============================================================ */

let honorType = '';
let honorList = [];

document.addEventListener('DOMContentLoaded', function () {
    init();
});

async function init() {
    try {
        renderHeader(await API.get('/profile'));
    } catch (error) {
        /* 忽略：不影响主内容 */
    }
    loadHonorStats();
    loadHonorList();
    bindHonorFilter();
    bindHonorAdd();
    bindHonorActions();
}

async function loadHonorStats() {
    const box = document.querySelector('.statistics');
    if (!box) {
        return;
    }
    try {
        const stats = await API.get('/honors/stats');
        const cards = [
            ['总荣誉', stats.total],
            ['校级荣誉', stats.schoolCount],
            ['院级荣誉', stats.collegeCount],
            ['比赛奖项', stats.competitionCount]
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

async function loadHonorList() {
    const grid = document.querySelector('.honor-grid');
    try {
        const path = honorType ? '/honors?type=' + encodeURIComponent(honorType) : '/honors';
        honorList = await API.get(path);
        if (!honorList.length) {
            grid.innerHTML = '<div style="padding:20px;color:#909399;">暂无荣誉记录</div>';
            return;
        }
        grid.innerHTML = honorList.map(function (honor) {
            return '<article class="card honor-card">'
                + '<div class="honor-card-top">'
                + '<div class="honor-icon">' + esc(honor.icon || honor.type) + '</div>'
                + '<span class="honor-type">' + esc(honor.type) + '</span>'
                + '</div>'
                + '<div class="honor-content">'
                + '<h2>' + esc(honor.title) + '</h2>'
                + '<p>' + esc(honor.description) + '</p>'
                + '</div>'
                + '<div class="honor-meta">'
                + '<span>' + esc(honor.level) + '</span>'
                + '<span>' + esc(honor.honorDate) + '</span>'
                + '</div>'
                + '<div class="honor-actions">'
                + '<button class="btn btn-secondary" data-act="edit" data-id="' + honor.id + '">编辑</button>'
                + '<button class="btn btn-danger" data-act="del" data-id="' + honor.id + '">删除</button>'
                + '</div>'
                + '</article>';
        }).join('');
    } catch (error) {
        showError(grid, error);
    }
}

/* 分类筛选：全部 / 奖学金 / 比赛 / 荣誉 / 其他 */
function bindHonorFilter() {
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
            honorType = (text === '全部') ? '' : text;
            loadHonorList();
        });
    });
}

/* 添加荣誉 */
function bindHonorAdd() {
    const button = document.querySelector('.page-header .btn-primary');
    if (!button) {
        return;
    }
    button.addEventListener('click', async function () {
        const title = ask('荣誉名称：');
        if (title === null) {
            return;
        }
        if (!title) {
            toast('荣誉名称不能为空', true);
            return;
        }
        const description = ask('描述：');
        if (description === null) {
            return;
        }
        const type = ask('类型（奖学金 / 比赛 / 荣誉 / 其他）：', '荣誉');
        if (type === null) {
            return;
        }
        const level = ask('级别（校级 / 院级 / 其他）：', '院级');
        if (level === null) {
            return;
        }
        const date = ask('获得时间（yyyy-MM）：', new Date().toISOString().slice(0, 7));
        if (date === null) {
            return;
        }
        try {
            await API.post('/honors', {
                title: title,
                description: description,
                type: type || '其他',
                level: level || '其他',
                honorDate: date,
                icon: title.slice(0, 1)
            });
            toast('荣誉已添加');
            loadHonorStats();
            loadHonorList();
        } catch (error) {
            toast(error.message, true);
        }
    });
}

/* 编辑 / 删除（事件委托） */
function bindHonorActions() {
    const grid = document.querySelector('.honor-grid');
    if (!grid) {
        return;
    }
    grid.addEventListener('click', async function (event) {
        const button = event.target.closest('button[data-act]');
        if (!button) {
            return;
        }
        const id = Number(button.getAttribute('data-id'));
        const honor = honorList.find(function (item) {
            return item.id === id;
        });
        if (!honor) {
            return;
        }

        if (button.getAttribute('data-act') === 'del') {
            if (!window.confirm('确定删除「' + honor.title + '」吗？')) {
                return;
            }
            try {
                await API.del('/honors/' + id);
                toast('已删除');
                loadHonorStats();
                loadHonorList();
            } catch (error) {
                toast(error.message, true);
            }
            return;
        }

        const title = ask('荣誉名称：', honor.title);
        if (title === null) {
            return;
        }
        const description = ask('描述：', honor.description);
        if (description === null) {
            return;
        }
        const level = ask('级别：', honor.level);
        if (level === null) {
            return;
        }
        try {
            await API.put('/honors/' + id, {
                title: title,
                description: description,
                type: honor.type,
                level: level,
                honorDate: honor.honorDate,
                icon: honor.icon
            });
            toast('已保存');
            loadHonorList();
        } catch (error) {
            toast(error.message, true);
        }
    });
}
