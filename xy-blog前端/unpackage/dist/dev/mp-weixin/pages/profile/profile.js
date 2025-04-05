"use strict";
const common_vendor = require("../../common/vendor.js");
const api_index = require("../../api/index.js");
const _sfc_main = {
  data() {
    return {
      userInfo: null,
      isLoading: false,
      userStats: {
        blogCount: 0,
        likeCount: 0,
        commentCount: 0
      }
    };
  },
  onShow() {
    this.getUserInfo();
  },
  methods: {
    // 获取用户信息
    async getUserInfo() {
      this.isLoading = true;
      const userInfo = common_vendor.index.getStorageSync("user");
      if (userInfo) {
        this.userInfo = userInfo;
      }
      try {
        const res = await api_index.userApi.getUserInfo();
        this.userInfo = res.data;
        common_vendor.index.setStorageSync("user", res.data);
      } catch (error) {
        common_vendor.index.__f__("error", "at pages/profile/profile.vue:112", "获取用户信息失败:", error);
      } finally {
        this.isLoading = false;
      }
    },
    // 跳转到我的博客
    goToMyBlogs() {
      common_vendor.index.navigateTo({
        url: "/pages/my-blogs/my-blogs"
      });
    },
    // 跳转到我的点赞
    goToMyLikes() {
      common_vendor.index.navigateTo({
        url: "/pages/my-likes/my-likes"
      });
    },
    // 跳转到我的评论
    goToMyComments() {
      common_vendor.index.navigateTo({
        url: "/pages/my-comments/my-comments"
      });
    },
    // 跳转到账号设置
    goToSettings() {
      common_vendor.index.navigateTo({
        url: "/pages/settings/settings"
      });
    },
    // 跳转到关于我们
    goToAbout() {
      common_vendor.index.navigateTo({
        url: "/pages/about/about"
      });
    },
    // 退出登录
    logout() {
      common_vendor.index.showModal({
        title: "提示",
        content: "确定要退出登录吗？",
        success: (res) => {
          if (res.confirm) {
            common_vendor.index.removeStorageSync("token");
            common_vendor.index.removeStorageSync("user");
            common_vendor.index.reroute({
              url: "/pages/login/login"
            });
          }
        }
      });
    }
  }
};
function _sfc_render(_ctx, _cache, $props, $setup, $data, $options) {
  return {
    a: $data.userInfo.avatar || "/static/default-avatar.png",
    b: common_vendor.t($data.userInfo.nickname || "未设置昵称"),
    c: common_vendor.t($data.userInfo.id || ""),
    d: common_vendor.t($data.userStats.blogCount || 0),
    e: common_vendor.t($data.userStats.likeCount || 0),
    f: common_vendor.t($data.userStats.commentCount || 0),
    g: common_vendor.o((...args) => $options.goToMyBlogs && $options.goToMyBlogs(...args)),
    h: common_vendor.o((...args) => $options.goToMyLikes && $options.goToMyLikes(...args)),
    i: common_vendor.o((...args) => $options.goToMyComments && $options.goToMyComments(...args)),
    j: common_vendor.o((...args) => $options.goToSettings && $options.goToSettings(...args)),
    k: common_vendor.o((...args) => $options.goToAbout && $options.goToAbout(...args)),
    l: common_vendor.o((...args) => $options.logout && $options.logout(...args))
  };
}
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["render", _sfc_render]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../.sourcemap/mp-weixin/pages/profile/profile.js.map
