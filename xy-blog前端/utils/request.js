const app = getApp();

// 是否启用测试模式
const USE_TEST_MODE = false;

// 根据模式选择不同的 BASE_URL
const BASE_URL = USE_TEST_MODE ? 'http://test-server.com/api' : 'http://localhost:3000/api';

/**
 * 封装 uni.request 网络请求
 * @param {Object} options - 请求选项
 * @param {String} options.url - 请求URL
 * @param {String} options.method - 请求方法，默认GET
 * @param {Object} options.data - 请求参数
 * @param {Boolean} options.showLoading - 是否显示加载中提示
 * @param {Object} options.header - 自定义请求头
 */
const request = (options) => {
    const {
        url,
        method = 'GET',
        data = {},
        header = {},
        showLoading = true
    } = options;

    return new Promise((resolve, reject) => {
        if (showLoading) {
            uni.showLoading({ title: '加载中...' });
        }

        // 读取 token 并添加到请求头
        const token = uni.getStorageSync('token');
        const finalHeader = { 
            ...header, 
            ...(token ? { Authorization: `${token}` } : {}) 
        };

        uni.request({
            url: BASE_URL + url,
            method,
            data,
            header: finalHeader,
            timeout: 10000, // 超时设置 10s
            success: (res) => {
                if (res.statusCode === 200) {
                    resolve(res.data);
                } else if (res.statusCode === 401) {
                    // 处理 token 失效
                    uni.removeStorageSync('token');
                    uni.removeStorageSync('user');
                    uni.showToast({ title: '请重新登录', icon: 'none' });

                    setTimeout(() => {
                        uni.reLaunch({ url: '/pages/login/login' }); // 改为 reLaunch
                    }, 1500);

                    reject(new Error('登录已过期'));
                } else {
                    uni.showToast({ title: res.data.msg || '请求失败', icon: 'none' });
                    reject(new Error(res.data.msg || '请求失败'));
                }
            },
            fail: (err) => {
                uni.showToast({ title: '网络错误，请检查网络连接', icon: 'none' });
                reject(new Error('网络错误'));
            },
            complete: () => {
                if (showLoading) {
                    uni.hideLoading();
                }
            }
        });
    });
};

// 封装常用 HTTP 方法
export default {
    get: (url, data = {}, showLoading = false, header = {}) => {
        return request({ url, method: 'GET', data, showLoading, header });
    },
    post: (url, data = {}, showLoading = false, header = {}) => {
        return request({ url, method: 'POST', data, showLoading, header });
    },
    put: (url, data = {}, showLoading = false, header = {}) => {
        return request({ url, method: 'PUT', data, showLoading, header });
    },
    delete: (url, data = {}, showLoading = false, header = {}) => {
        return request({ url, method: 'DELETE', data, showLoading, header });
    }
};
