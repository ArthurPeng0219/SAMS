/* ============================================================
   courses.js —— 课程资料
   GET /api/materials/stats 统计，GET /api/materials?category=&keyword= 列表
   ============================================================ */

let materialCategory = '';
let materialKeyword = '';
let materialList = [];

document.addEventListener('DOMContentLoaded', function () {
    init();
});

async function init() {
    try {
        renderHeader(await API.get('/profile'));
    } catch (error) {
        /* 忽略 */
    }
    loadMaterialStats();
    loadMaterialList();
    bindMaterialFilter();
    bindMaterialSearch();
    bindMaterialAdd();
    bindMaterialActions();
}

async function loadMaterialStats() {
    const box = document.querySelector('.statistics');
    if (!box) {
        return;
    }
    try {
        const stats = await API.get('/materials/stats');
        const cards = [
            ['资料总数', stats.total],
            ['Java', stats.javaCount],
            ['Python', stats.pythonCount],
            ['数据库', stats.databaseCount]
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

async function loadMaterialList() {
    const list = document.querySelector('.course-file-list');
    try {
        const params = [];
        if (materialCategory) {
            params.push('category=' + encodeURIComponent(materialCategory));
        }
        if (materialKeyword) {
            params.push('keyword=' + encodeURIComponent(materialKeyword));
        }
        const path = '/materials' + (params.length ? '?' + params.join('&') : '');
        materialList = await API.get(path);

        if (!materialList.length) {
            list.innerHTML = '<div style="padding:20px;color:#909399;">没有匹配的资料</div>';
            return;
        }

        list.innerHTML = materialList.map(function (item) {
            const type = (item.fileType || '').toUpperCase();
            const iconClass = type === 'PDF' ? 'pdf' : (type.indexOf('DOC') === 0 ? 'doc' : (type.indexOf('PPT') === 0 ? 'ppt' : 'other'));
            const iconText = type === 'IPYNB' ? 'NB' : type.slice(0, 4);
            return '<article class="course-file-item">'
                + '<div class="course-file-icon ' + iconClass + '">' + esc(iconText) + '</div>'
                + '<div class="course-file-main">'
                + '<h3>' + esc(item.fileName) + '</h3>'
                + '<p>' + esc(item.courseName) + '</p>'
                + '</div>'
                + '<div class="course-file-meta">'
                + '<span>' + esc(type) + '</span> '
                + '<span>' + esc(item.fileSize) + '</span> '
                + '<span>' + esc(item.uploadDate) + '</span>'
                + '</div>'
                + '<div class="course-file-actions">'
                + '<button class="btn btn-secondary" data-act="view" data-id="' + item.id + '">查看</button>'
                + '<button class="btn btn-secondary" data-act="download" data-id="' + item.id + '">下载</button>'
                + '<button class="btn btn-danger" data-act="del" data-id="' + item.id + '">删除</button>'
                + '</div>'
                + '</article>';
        }).join('');
    } catch (error) {
        showError(list, error);
    }
}

function bindMaterialFilter() {
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
            materialCategory = (text === '全部') ? '' : text;
            loadMaterialList();
        });
    });
}

function bindMaterialSearch() {
    const search = document.querySelector('.search-input');
    if (!search) {
        return;
    }
    search.addEventListener('input', debounce(function () {
        materialKeyword = search.value.trim();
        loadMaterialList();
    }, 300));
}

function bindMaterialAdd() {
    const button = document.querySelector('.page-header .btn-primary');
    if (!button) {
        return;
    }
    button.addEventListener('click', async function () {
        const fileName = ask('文件名（含扩展名）：');
        if (fileName === null) {
            return;
        }
        if (!fileName) {
            toast('文件名不能为空', true);
            return;
        }
        const courseName = ask('所属课程：');
        if (courseName === null) {
            return;
        }
        const category = ask('分类（Java / Python / 数据库 / 机器学习 / 其他）：', '其他');
        if (category === null) {
            return;
        }
        const fileSize = ask('文件大小（例如 1.2 MB）：', '1.0 MB');
        if (fileSize === null) {
            return;
        }
        const extension = fileName.lastIndexOf('.') > -1 ? fileName.slice(fileName.lastIndexOf('.') + 1) : '';
        try {
            await API.post('/materials', {
                fileName: fileName,
                courseName: courseName,
                category: category || '其他',
                fileType: extension.toUpperCase(),
                fileSize: fileSize,
                uploadDate: new Date().toISOString().slice(0, 10)
            });
            toast('资料已登记');
            loadMaterialStats();
            loadMaterialList();
        } catch (error) {
            toast(error.message, true);
        }
    });
}

function bindMaterialActions() {
    const list = document.querySelector('.course-file-list');
    if (!list) {
        return;
    }
    list.addEventListener('click', async function (event) {
        const button = event.target.closest('button[data-act]');
        if (!button) {
            return;
        }
        const id = Number(button.getAttribute('data-id'));
        const item = materialList.find(function (row) {
            return row.id === id;
        });
        if (!item) {
            return;
        }
        const act = button.getAttribute('data-act');

        if (act === 'view') {
            window.alert('【' + item.fileName + '】\n所属课程：' + (item.courseName || '-')
                + '\n类型：' + (item.fileType || '-') + '　大小：' + (item.fileSize || '-')
                + '\n上传日期：' + (item.uploadDate || '-'));
            return;
        }

        if (act === 'download') {
            toast('演示系统：文件本体未接入存储，暂不提供下载');
            return;
        }

        if (!window.confirm('确定删除「' + item.fileName + '」吗？')) {
            return;
        }
        try {
            await API.del('/materials/' + id);
            toast('已删除');
            loadMaterialStats();
            loadMaterialList();
        } catch (error) {
            toast(error.message, true);
        }
    });
}
