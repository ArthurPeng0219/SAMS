/* ============================================================
   settings.js —— 系统管理
   GET  /api/settings          账户信息 + 所有开关
   PUT  /api/settings/{id}     切换开关（即时保存到数据库）
   PUT  /api/profile           修改邮箱 / 手机号
   ============================================================ */

let settingItems = [];
let currentStudent = null;

document.addEventListener('DOMContentLoaded', function () {
    init();
});

async function init() {
    try {
        const data = await API.get('/settings');
        currentStudent = await API.get('/profile');
        renderHeader(currentStudent);
        settingItems = data.settings || [];
        renderAccountSection();
        renderSwitchSection();
        bindDataSection();
        bindFooter();
    } catch (error) {
        showError(document.querySelector('.settings-section .settings-card'), error);
    }
}

function renderAccountSection() {
    const card = document.querySelectorAll('.settings-section')[0].querySelector('.settings-card');
    card.innerHTML =
        accountRow('用户名', currentStudent.username, 'username')
        + accountRow('邮箱地址', currentStudent.email, 'email')
        + accountRow('手机号码', currentStudent.phone, 'phone')
        + '<div class="setting-row">'
        + '<div class="setting-info"><strong>登录密码</strong>'
        + '<span>为保护账户安全，请定期修改密码</span></div>'
        + '<button class="btn setting-button" data-act="password">修改密码</button>'
        + '</div>';
}

function accountRow(label, value, key) {
    return '<div class="setting-row">'
        + '<div class="setting-info"><strong>' + esc(label) + '</strong>'
        + '<span>' + esc(value) + '</span></div>'
        + '<button class="btn setting-button" data-act="edit" data-key="' + esc(key) + '">修改</button>'
        + '</div>';
}

function renderSwitchSection() {
    const card = document.querySelectorAll('.settings-section')[1].querySelector('.settings-card');
    card.innerHTML = settingItems.map(function (item) {
        return '<div class="setting-row">'
            + '<div class="setting-info"><strong>' + esc(item.label) + '</strong>'
            + '<span>' + esc(item.description) + '</span></div>'
            + '<label class="switch">'
            + '<input type="checkbox" data-id="' + item.id + '"' + (item.enabled ? ' checked' : '') + '>'
            + '<span class="switch-slider"></span>'
            + '</label>'
            + '</div>';
    }).join('');
    bindSwitchChange(card);
}

/* 开关一改就调接口存库，刷新页面状态还在 */
function bindSwitchChange(card) {
    card.addEventListener('change', async function (event) {
        const input = event.target;
        if (!input.matches('input[type="checkbox"][data-id]')) {
            return;
        }
        const id = Number(input.getAttribute('data-id'));
        try {
            await API.put('/settings/' + id, { enabled: input.checked });
            const item = settingItems.find(function (row) {
                return row.id === id;
            });
            if (item) {
                item.enabled = input.checked;
            }
            // 深色模式要立刻生效，不用刷新页面
            if (item && item.settingKey === 'darkMode') {
                applyTheme(input.checked);
            }
            toast((item ? item.label : '设置项') + '已' + (input.checked ? '开启' : '关闭'));
        } catch (error) {
            input.checked = !input.checked;
            toast(error.message, true);
        }
    });
}

function bindDataSection() {
    const section = document.querySelectorAll('.settings-section')[2];
    if (!section) {
        return;
    }
    section.addEventListener('click', function (event) {
        const button = event.target.closest('button');
        if (!button) {
            return;
        }
        const text = button.textContent.trim();
        if (text === '导出数据' || text === '立即备份') {
            exportArchive(text === '立即备份' ? '备份文件已生成' : '档案数据已导出');
        } else if (text === '恢复数据') {
            toast('演示系统：恢复功能尚未接入');
        }
    });
}

/* 把各个接口的数据打包成一个 JSON 文件下载下来 */
async function exportArchive(successMessage) {
    try {
        const names = ['dashboard', 'grades', 'honors', 'certificates', 'materials', 'practices', 'growth', 'settings'];
        const results = await Promise.all([
            API.get('/dashboard'),
            API.get('/grades'),
            API.get('/honors'),
            API.get('/certificates'),
            API.get('/materials'),
            API.get('/practices'),
            API.get('/growth'),
            API.get('/settings')
        ]);
        const archive = { exportedAt: new Date().toISOString() };
        names.forEach(function (name, index) {
            archive[name] = results[index];
        });

        const blob = new Blob([JSON.stringify(archive, null, 2)], { type: 'application/json' });
        const url = URL.createObjectURL(blob);
        const link = document.createElement('a');
        link.href = url;
        link.download = 'sams-archive-' + new Date().toISOString().slice(0, 10) + '.json';
        document.body.appendChild(link);
        link.click();
        link.remove();
        URL.revokeObjectURL(url);
        toast(successMessage);
    } catch (error) {
        toast(error.message, true);
    }
}

/* 账户区的「修改」按钮 */
document.addEventListener('click', async function (event) {
    const button = event.target.closest('button[data-act="edit"]');
    if (!button || !currentStudent) {
        return;
    }
    const key = button.getAttribute('data-key');
    if (key === 'username') {
        toast('演示系统：用户名暂不支持修改');
        return;
    }
    const label = key === 'email' ? '邮箱地址' : '手机号码';
    const value = ask('新的' + label + '：', currentStudent[key]);
    if (value === null || !value) {
        return;
    }
    try {
        await API.put('/profile', {
            phone: key === 'phone' ? value : currentStudent.phone,
            email: key === 'email' ? value : currentStudent.email,
            city: currentStudent.city,
            github: currentStudent.github
        });
        currentStudent = await API.get('/profile');
        renderAccountSection();
        toast(label + '已更新');
    } catch (error) {
        toast(error.message, true);
    }
});

document.addEventListener('click', function (event) {
    const button = event.target.closest('button[data-act="password"]');
    if (button) {
        toast('演示系统：密码修改尚未接入');
    }
});

function bindFooter() {
    const footer = document.querySelector('.settings-footer');
    if (!footer) {
        return;
    }
    footer.addEventListener('click', function () {
        toast('开关类设置已即时保存，无需重复提交');
    });
}
