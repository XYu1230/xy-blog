"use strict";
Object.defineProperty(exports, Symbol.toStringTag, { value: "Module" });
const common_vendor = require("./common/vendor.js");
if (!Math) {
  "./pages/login/login.js";
  "./pages/register/register.js";
  "./pages/index/index.js";
  "./pages/post/post.js";
  "./pages/detail/detail.js";
  "./pages/profile/profile.js";
}
const _sfc_main = {
  onLaunch: function() {
    common_vendor.index.__f__("log", "at App.vue:4", "App Launch");
    const token = common_vendor.index.getStorageSync("token");
    if (!token) {
      const pages = getCurrentPages();
      const currentPage = pages.length > 0 ? pages[pages.length - 1].route : "";
      if (currentPage !== "pages/login/login" && currentPage !== "pages/register/register") {
        common_vendor.index.reroute({
          url: "/pages/login/login"
        });
      }
    }
  },
  onShow: function() {
    common_vendor.index.__f__("log", "at App.vue:21", "App Show");
  },
  onHide: function() {
    common_vendor.index.__f__("log", "at App.vue:24", "App Hide");
  },
  globalData: {
    baseUrl: "http://localhost:3000/api"
    // 后端接口基础URL，根据实际情况修改
  }
};
function createApp() {
  const app = common_vendor.createSSRApp(_sfc_main);
  return {
    app
  };
}
createApp().app.mount("#app");
exports.createApp = createApp;
//# sourceMappingURL=../.sourcemap/mp-weixin/app.js.map
