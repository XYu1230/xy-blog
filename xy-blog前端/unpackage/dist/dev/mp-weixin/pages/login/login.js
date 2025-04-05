"use strict";
const common_vendor = require("../../common/vendor.js");
const api_index = require("../../api/index.js");
const common_assets = require("../../common/assets.js");
const _sfc_main = {
  data() {
    return {
      formData: {
        username: "",
        password: ""
      }
    };
  },
  methods: {
    // 处理登录
    async handleLogin() {
      if (!this.formData.username.trim()) {
        common_vendor.index.showToast({
          title: "请输入用户名",
          icon: "none"
        });
        return;
      }
      if (!this.formData.password.trim()) {
        common_vendor.index.showToast({
          title: "请输入密码",
          icon: "none"
        });
        return;
      }
      try {
        const res = await api_index.userApi.login(this.formData);
        common_vendor.index.setStorageSync("token", res.data.token);
        common_vendor.index.setStorageSync("user", res.data.user);
        common_vendor.index.showToast({
          title: "登录成功",
          icon: "success"
        });
        setTimeout(() => {
          common_vendor.index.switchTab({
            url: "/pages/index/index"
          });
        }, 1500);
      } catch (error) {
        common_vendor.index.__f__("error", "at pages/login/login.vue:97", "登录失败:", error);
      }
    },
    // 使用测试账号
    // useTestAccount() {
    //   this.formData.username = this.testAccount.username;
    //   this.formData.password = this.testAccount.password;
    //   uni.showToast({
    //     title: "已填充测试账号",
    //     icon: "none",
    //     duration: 1000
    //   });
    //   // 自动登录
    //   setTimeout(() => {
    //     this.handleLogin();
    //   }, 1000);
    // },
    // 跳转到注册页面
    goToRegister() {
      common_vendor.index.navigateTo({
        url: "/pages/register/register"
      });
    }
  }
};
function _sfc_render(_ctx, _cache, $props, $setup, $data, $options) {
  return {
    a: common_assets._imports_0,
    b: $data.formData.username,
    c: common_vendor.o(($event) => $data.formData.username = $event.detail.value),
    d: $data.formData.password,
    e: common_vendor.o(($event) => $data.formData.password = $event.detail.value),
    f: common_vendor.o((...args) => $options.handleLogin && $options.handleLogin(...args)),
    g: common_vendor.o((...args) => $options.goToRegister && $options.goToRegister(...args))
  };
}
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["render", _sfc_render]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../.sourcemap/mp-weixin/pages/login/login.js.map
