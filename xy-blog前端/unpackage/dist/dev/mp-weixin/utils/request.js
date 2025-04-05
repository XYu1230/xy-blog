"use strict";
const common_vendor = require("../common/vendor.js");
getApp();
const BASE_URL = "http://localhost:3000/api";
const request = (options) => {
  const {
    url,
    method = "GET",
    data = {},
    header = {},
    showLoading = true
  } = options;
  return new Promise((resolve, reject) => {
    if (showLoading) {
      common_vendor.index.showLoading({
        title: "加载中..."
      });
    }
    const token = common_vendor.index.getStorageSync("token");
    if (token) {
      header.Authorization = "Bearer " + token;
    }
    common_vendor.index.request({
      url: BASE_URL + url,
      method,
      data,
      header,
      success: (res) => {
        if (res.statusCode === 200) {
          resolve(res.data);
        } else if (res.statusCode === 401) {
          common_vendor.index.removeStorageSync("token");
          common_vendor.index.removeStorageSync("user");
          common_vendor.index.showToast({
            title: "请重新登录",
            icon: "none"
          });
          setTimeout(() => {
            common_vendor.index.reroute({
              url: "/pages/login/login"
            });
          }, 1500);
          reject(new Error("登录已过期"));
        } else {
          common_vendor.index.showToast({
            title: res.data.msg || "请求失败",
            icon: "none"
          });
          reject(new Error(res.data.msg || "请求失败"));
        }
      },
      fail: (err) => {
        common_vendor.index.showToast({
          title: "网络错误，请检查网络连接",
          icon: "none"
        });
        reject(new Error("网络错误"));
      },
      complete: () => {
        if (showLoading) {
          common_vendor.index.hideLoading();
        }
      }
    });
  });
};
const request$1 = {
  // GET请求
  get: (url, data = {}, loading = false) => {
    return request({
      url,
      method: "GET",
      data,
      loading
    });
  },
  // POST请求
  post: (url, data = {}, loading = false) => {
    return request({
      url,
      method: "POST",
      data,
      loading
    });
  },
  // PUT请求
  put: (url, data = {}, loading = false) => {
    return request({
      url,
      method: "PUT",
      data,
      loading
    });
  },
  // DELETE请求
  delete: (url, data = {}, loading = false) => {
    return request({
      url,
      method: "DELETE",
      data,
      loading
    });
  }
};
exports.request = request$1;
//# sourceMappingURL=../../.sourcemap/mp-weixin/utils/request.js.map
