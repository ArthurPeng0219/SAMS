/* ============================================================
   profile.js —— 个人信息
   GET /api/profile 读，PUT /api/profile 存
   ============================================================ */

document.addEventListener('DOMContentLoaded', function () {
    init();
});

async function init() {
    try {
        const student = await API.get('/profile');
        renderHeader(student);
        renderProfileCard(student);
        renderInfoList(student);
        fillForm(student);
        bindForm();
    } catch (error) {
        showError(document.querySelector('.info-list'), error);
    }
}

function renderProfileCard(student) {
    const avatar = document.querySelector('.profile-avatar');
    const name = document.querySelector('.profile-card h2');
    const major = document.querySelector('.profile-major');
    if (avatar) {
        avatar.textContent = student.avatarText || (student.name || '').slice(0, 1);
    }
    if (name) {
        name.textContent = student.name;
    }
    if (major) {
        major.textContent = student.major;
    }
}

function renderInfoList(student) {
    const rows = [
        ['姓名', student.name],
        ['学号', student.studentNo],
        ['专业', student.major],
        ['班级', student.className],
        ['入学年份', student.enrollYear]
    ];
    document.querySelector('.info-list').innerHTML = rows.map(function (row) {
        return '<div class="info-item">'
            + '<span class="info-label">' + esc(row[0]) + '</span>'
            + '<span>' + esc(row[1]) + '</span>'
            + '</div>';
    }).join('');
}

function fillForm(student) {
    document.querySelector('#phone').value = student.phone || '';
    document.querySelector('#email').value = student.email || '';
    document.querySelector('#address').value = student.city || '';
    document.querySelector('#github').value = student.github || '';
}

function bindForm() {
    const form = document.querySelector('.profile-form');

    form.addEventListener('submit', async function (event) {
        event.preventDefault();
        try {
            await API.put('/profile', {
                phone: form.querySelector('#phone').value,
                email: form.querySelector('#email').value,
                city: form.querySelector('#address').value,
                github: form.querySelector('#github').value
            });
            toast('联系方式已保存到数据库');
        } catch (error) {
            toast(error.message, true);
        }
    });

    // 「取消」按钮：重新从后端读一次，恢复成库里的值
    const cancelButton = form.querySelector('button[type="button"]');
    if (cancelButton) {
        cancelButton.addEventListener('click', function () {
            init();
            toast('已恢复为数据库中的值');
        });
    }
}
