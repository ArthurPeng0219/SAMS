/* ============================================================
   api.js  —— 所有页面共用的工具
   1) fetch 封装：统一拼 /api 前缀、统一处理错误
   2) 渲染小工具：头部用户信息、提示条、防抖
   ============================================================ */

const API = {
    async request(method, path, body) {
        const options = { method, headers: {} };
        if (body !== undefined) {
            options.headers['Content-Type'] = 'application/json';
            options.body = JSON.stringify(body);
        }
        const response = await fetch('/api' + path, options);
        if (response.status === 204) {
            return null;
        }
        const text = await response.text();
        const data = text ? JSON.parse(text) : null;
        if (!response.ok) {
            throw new Error((data && data.message) ? data.message : '请求失败（HTTP ' + response.status + '）');
        }
        return data;
    },
    get(path) {
        return API.request('GET', path);
    },
    post(path, body) {
        return API.request('POST', path, body);
    },
    put(path, body) {
        return API.request('PUT', path, body);
    },
    del(path) {
        return API.request('DELETE', path);
    }
};

/** 转义 HTML，避免数据里的特殊字符破坏页面 */
function esc(value) {
    if (value === null || value === undefined) {
        return '';
    }
    return String(value)
        .replace(/&/g, '&amp;')
        .replace(/</g, '&lt;')
        .replace(/>/g, '&gt;')
        .replace(/"/g, '&quot;')
        .replace(/'/g, '&#39;');
}

/** 右上角飘一个提示条（不依赖任何 CSS 文件） */
function toast(message, isError) {
    let box = document.getElementById('sams-toast');
    if (!box) {
        box = document.createElement('div');
        box.id = 'sams-toast';
        box.style.cssText = 'position:fixed;right:24px;top:80px;z-index:9999;'
            + 'display:flex;flex-direction:column;gap:8px;align-items:flex-end;';
        document.body.appendChild(box);
    }
    const item = document.createElement('div');
    item.textContent = message;
    item.style.cssText = 'padding:10px 16px;border-radius:6px;font-size:14px;color:#fff;'
        + 'box-shadow:0 2px 12px rgba(0,0,0,.15);'
        + 'background-color:' + (isError ? '#f56c6c' : '#67c23a') + ';';
    box.appendChild(item);
    setTimeout(function () {
        item.remove();
    }, 2600);
}

/** 容器渲染失败时的兜底提示 */
function showError(container, error) {
    if (!container) {
        return;
    }
    const message = (error && error.message) ? error.message : error;
    container.innerHTML = '<div style="padding:20px;color:#f56c6c;font-size:14px;">'
        + '加载失败：' + esc(message) + '</div>';
}

/** 把顶部导航里的用户名 / 头像换成数据库里的 */
function renderHeader(student) {
    if (!student) {
        return;
    }
    document.querySelectorAll('.user-info > span:first-child').forEach(function (el) {
        el.textContent = student.name;
    });
    document.querySelectorAll('.user-info .avatar').forEach(function (el) {
        el.textContent = student.avatarText || (student.name || '').slice(0, 1);
    });
}

/** 输入框防抖：搜索框别每敲一个字就发一次请求 */
function debounce(fn, delay) {
    let timer = null;
    return function () {
        const args = arguments;
        clearTimeout(timer);
        timer = setTimeout(function () {
            fn.apply(null, args);
        }, delay || 300);
    };
}

/** 弹输入框，用户点取消返回 null */
function ask(label, current) {
    const value = window.prompt(label, (current === null || current === undefined) ? '' : current);
    return value === null ? null : value.trim();
}

/** 状态标签的样式类：后端只会返回这 4 个，其它统一按 good 显示 */
function statusClass(name) {
    return ['success', 'good', 'warning', 'danger'].indexOf(name) >= 0 ? name : 'good';
}
