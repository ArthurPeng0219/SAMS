/* ============================================================
   certificates.js —— 技能证书
   GET /api/certificates/stats 统计，GET /api/certificates 列表
   ============================================================ */

let certificateList = [];

document.addEventListener('DOMContentLoaded', function () {
    init();
});

async function init() {
    try {
        renderHeader(await API.get('/profile'));
    } catch (error) {
        /* 忽略 */
    }
    loadCertificateStats();
    loadCertificateList();
    bindCertificateAdd();
    bindCertificateActions();
}

async function loadCertificateStats() {
    const box = document.querySelector('.statistic') || document.querySelector('.statistics');
    if (!box) {
        return;
    }
    try {
        const stats = await API.get('/certificates/stats');
        const cards = [
            ['证书总数', stats.total],
            ['计算机类', stats.computerCount],
            ['英语类', stats.englishCount],
            ['专业类', stats.majorCount]
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

async function loadCertificateList() {
    const grid = document.querySelector('.certificate-grid');
    try {
        certificateList = await API.get('/certificates');
        if (!certificateList.length) {
            grid.innerHTML = '<div style="padding:20px;color:#909399;">暂无证书记录</div>';
            return;
        }
        grid.innerHTML = certificateList.map(function (item) {
            const rows = [
                ['颁发机构', item.issuer]
            ];
            if (item.extraLabel) {
                rows.push([item.extraLabel, item.extraValue]);
            }
            rows.push(['获得时间', item.obtainDate]);
            rows.push(['证书编号', item.certNo]);

            return '<article class="card certificate-card">'
                + '<div class="certificate-header">'
                + '<div class="certificate-icon">' + esc(item.icon || item.category) + '</div>'
                + '<span class="certificate-status">' + esc(item.status) + '</span>'
                + '</div>'
                + '<h2>' + esc(item.name) + '</h2>'
                + '<p class="certificate-type">' + esc(item.category) + '</p>'
                + '<div class="certificate-info">'
                + rows.map(function (row) {
                    return '<div><span>' + esc(row[0]) + '</span><strong>' + esc(row[1]) + '</strong></div>';
                }).join('')
                + '</div>'
                + '<div class="certificate-actions">'
                + '<button class="btn btn-secondary" data-act="view" data-id="' + item.id + '">查看</button>'
                + '<button class="btn btn-secondary" data-act="edit" data-id="' + item.id + '">编辑</button>'
                + '<button class="btn btn-danger" data-act="del" data-id="' + item.id + '">删除</button>'
                + '</div>'
                + '</article>';
        }).join('');
    } catch (error) {
        showError(grid, error);
    }
}

function bindCertificateAdd() {
    const button = document.querySelector('.page-header .btn-primary');
    if (!button) {
        return;
    }
    button.addEventListener('click', async function () {
        const name = ask('证书名称：');
        if (name === null) {
            return;
        }
        if (!name) {
            toast('证书名称不能为空', true);
            return;
        }
        const category = ask('类别（计算机类 / 英语类 / 专业类 / 其他）：', '专业类');
        if (category === null) {
            return;
        }
        const issuer = ask('颁发机构：');
        if (issuer === null) {
            return;
        }
        const obtainDate = ask('获得时间（yyyy-MM）：', new Date().toISOString().slice(0, 7));
        if (obtainDate === null) {
            return;
        }
        try {
            await API.post('/certificates', {
                name: name,
                category: category || '其他',
                issuer: issuer,
                obtainDate: obtainDate,
                certNo: '待填写',
                status: '已获得',
                icon: name.slice(0, 1)
            });
            toast('证书已添加');
            loadCertificateStats();
            loadCertificateList();
        } catch (error) {
            toast(error.message, true);
        }
    });
}

function bindCertificateActions() {
    const grid = document.querySelector('.certificate-grid');
    if (!grid) {
        return;
    }
    grid.addEventListener('click', async function (event) {
        const button = event.target.closest('button[data-act]');
        if (!button) {
            return;
        }
        const id = Number(button.getAttribute('data-id'));
        const item = certificateList.find(function (row) {
            return row.id === id;
        });
        if (!item) {
            return;
        }
        const act = button.getAttribute('data-act');

        if (act === 'view') {
            window.alert('【' + item.name + '】\n'
                + '类别：' + (item.category || '-') + '\n'
                + '颁发机构：' + (item.issuer || '-') + '\n'
                + '获得时间：' + (item.obtainDate || '-') + '\n'
                + '证书编号：' + (item.certNo || '-') + '\n'
                + (item.extraLabel ? item.extraLabel + '：' + item.extraValue : ''));
            return;
        }

        if (act === 'del') {
            if (!window.confirm('确定删除「' + item.name + '」吗？')) {
                return;
            }
            try {
                await API.del('/certificates/' + id);
                toast('已删除');
                loadCertificateStats();
                loadCertificateList();
            } catch (error) {
                toast(error.message, true);
            }
            return;
        }

        const certNo = ask('证书编号：', item.certNo);
        if (certNo === null) {
            return;
        }
        const status = ask('状态（已获得 / 备考中）：', item.status);
        if (status === null) {
            return;
        }
        const extraLabel = ask('附加信息标签（留空则不显示）：', item.extraLabel);
        if (extraLabel === null) {
            return;
        }
        const extraValue = ask('附加信息值：', item.extraValue);
        if (extraValue === null) {
            return;
        }
        try {
            await API.put('/certificates/' + id, {
                name: item.name,
                category: item.category,
                issuer: item.issuer,
                obtainDate: item.obtainDate,
                certNo: certNo,
                status: status,
                icon: item.icon,
                extraLabel: extraLabel,
                extraValue: extraValue
            });
            toast('已保存');
            loadCertificateList();
        } catch (error) {
            toast(error.message, true);
        }
    });
}
