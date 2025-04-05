"use strict";
const common_vendor = require("../../common/vendor.js");
const api_index = require("../../api/index.js");
const common_assets = require("../../common/assets.js");
const _sfc_main = {
  data() {
    return {
      formData: {
        username: "",
        password: "",
        confirmPassword: "",
        nickname: ""
      },
      // 测试用户信息
      testInfo: {
        username: "testuser",
        password: "123456",
        nickname: "测试用户"
      }
    };
  },
  methods: {
    // 处理注册
    async handleRegister() {
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
      if (this.formData.password !== this.formData.confirmPassword) {
        common_vendor.index.showToast({
          title: "两次密码输入不一致",
          icon: "none"
        });
        return;
      }
      if (!this.formData.nickname.trim()) {
        common_vendor.index.showToast({
          title: "请输入昵称",
          icon: "none"
        });
        return;
      }
      const registerData = {
        username: this.formData.username,
        password: this.formData.password,
        nickname: this.formData.nickname
      };
      try {
        const res = await api_index.userApi.register(registerData);
        common_vendor.index.__f__("log", "at pages/register/register.vue:107", res);
        if (res.code === 500) {
          common_vendor.index.showToast({
            title: res.message,
            icon: "error"
          });
        } else {
          common_vendor.index.showToast({
            title: "注册成功",
            icon: "success"
          });
          setTimeout(() => {
            common_vendor.index.navigateTo({
              url: "/pages/login/login"
            });
          }, 1500);
        }
      } catch (error) {
        common_vendor.index.__f__("error", "at pages/register/register.vue:127", "注册失败:", error);
      }
    },
    // 填充测试信息
    // fillTestInfo() {
    // 	this.formData.username = this.testInfo.username;
    // 	this.formData.password = this.testInfo.password;
    // 	this.formData.confirmPassword = this.testInfo.password;
    // 	this.formData.nickname = this.testInfo.nickname;
    // 	uni.showToast({
    // 		title: '已填充测试信息',
    // 		icon: 'none',
    // 		duration: 1000
    // 	});
    // },
    // 跳转到登录页面
    goToLogin() {
      common_vendor.index.navigateTo({
        url: "/pages/login/login"
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
    f: $data.formData.confirmPassword,
    g: common_vendor.o(($event) => $data.formData.confirmPassword = $event.detail.value),
    h: $data.formData.nickname,
    i: common_vendor.o(($event) => $data.formData.nickname = $event.detail.value),
    j: common_vendor.o((...args) => $options.handleRegister && $options.handleRegister(...args)),
    k: common_vendor.o((...args) => $options.goToLogin && $options.goToLogin(...args))
  };
}
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["render", _sfc_render]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../.sourcemap/mp-weixin/pages/register/register.js.map
