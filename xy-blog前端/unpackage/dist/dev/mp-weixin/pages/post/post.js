"use strict";
const common_vendor = require("../../common/vendor.js");
const api_index = require("../../api/index.js");
const _sfc_main = {
  data() {
    return {
      blogData: {
        title: "",
        content: "",
        summary: "",
        tags: []
      },
      availableTags: ["前端", "JavaScript", "Vue", "React", "Node.js", "后端", "数据库", "算法", "面试", "其他"],
      customTag: "",
      isSubmitting: false,
      titleLimit: 50,
      contentLimit: 5e3,
      summaryLimit: 200
    };
  },
  onLoad() {
    const token = common_vendor.index.getStorageSync("token");
    if (!token) {
      common_vendor.index.showToast({
        title: "请先登录",
        icon: "none"
      });
      setTimeout(() => {
        common_vendor.index.redirectTo({
          url: "/pages/login/login"
        });
      }, 1500);
    }
  },
  methods: {
    // 切换标签选择
    toggleTag(tag) {
      const index = this.blogData.tags.indexOf(tag);
      if (index === -1) {
        if (this.blogData.tags.length >= 3) {
          common_vendor.index.showToast({
            title: "最多选择3个标签",
            icon: "none"
          });
          return;
        }
        this.blogData.tags.push(tag);
      } else {
        this.blogData.tags.splice(index, 1);
      }
    },
    // 添加自定义标签
    addCustomTag() {
      if (!this.customTag.trim()) {
        common_vendor.index.showToast({
          title: "标签不能为空",
          icon: "none"
        });
        return;
      }
      if (this.blogData.tags.length >= 3) {
        common_vendor.index.showToast({
          title: "最多选择3个标签",
          icon: "none"
        });
        return;
      }
      if (this.customTag.length > 10) {
        common_vendor.index.showToast({
          title: "标签最多10个字符",
          icon: "none"
        });
        return;
      }
      if (this.blogData.tags.includes(this.customTag)) {
        common_vendor.index.showToast({
          title: "标签已存在",
          icon: "none"
        });
        return;
      }
      this.blogData.tags.push(this.customTag);
      this.customTag = "";
    },
    // 提交博客
    async submitBlog() {
      if (!this.blogData.title.trim()) {
        common_vendor.index.showToast({
          title: "请输入标题",
          icon: "none"
        });
        return;
      }
      if (!this.blogData.content.trim()) {
        common_vendor.index.showToast({
          title: "请输入内容",
          icon: "none"
        });
        return;
      }
      if (!this.blogData.summary.trim()) {
        this.blogData.summary = this.blogData.content.replace(/<[^>]+>/g, "").substring(0, 100);
      }
      this.isSubmitting = true;
      try {
        const res = await api_index.blogApi.publishBlog(this.blogData);
        common_vendor.index.showToast({
          title: "发布成功",
          icon: "success"
        });
        setTimeout(() => {
          common_vendor.index.redirectTo({
            url: `/pages/detail/detail?id=${res.data.id}`
          });
        }, 1500);
      } catch (error) {
        common_vendor.index.__f__("error", "at pages/post/post.vue:208", "发布博客失败:", error);
      } finally {
        this.isSubmitting = false;
      }
    }
  }
};
function _sfc_render(_ctx, _cache, $props, $setup, $data, $options) {
  return {
    a: $data.blogData.title,
    b: common_vendor.o(($event) => $data.blogData.title = $event.detail.value),
    c: common_vendor.t($data.blogData.title.length),
    d: $data.blogData.content,
    e: common_vendor.o(($event) => $data.blogData.content = $event.detail.value),
    f: common_vendor.t($data.blogData.content.length),
    g: common_vendor.f($data.availableTags, (tag, index, i0) => {
      return {
        a: common_vendor.t(tag),
        b: $data.blogData.tags.includes(tag) ? 1 : "",
        c: index,
        d: common_vendor.o(($event) => $options.toggleTag(tag), index)
      };
    }),
    h: $data.customTag,
    i: common_vendor.o(($event) => $data.customTag = $event.detail.value),
    j: common_vendor.o((...args) => $options.addCustomTag && $options.addCustomTag(...args)),
    k: $data.blogData.summary,
    l: common_vendor.o(($event) => $data.blogData.summary = $event.detail.value),
    m: common_vendor.t($data.blogData.summary.length),
    n: common_vendor.o((...args) => $options.submitBlog && $options.submitBlog(...args))
  };
}
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["render", _sfc_render]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../.sourcemap/mp-weixin/pages/post/post.js.map
