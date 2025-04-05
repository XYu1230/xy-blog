"use strict";
const common_vendor = require("../../common/vendor.js");
const api_index = require("../../api/index.js");
const common_assets = require("../../common/assets.js");
const _sfc_main = {
  data() {
    return {
      blogList: [],
      page: 1,
      pageSize: 10,
      hasMoreData: true,
      isLoading: false,
      isRefreshing: false
    };
  },
  onLoad() {
    this.fetchBlogs();
  },
  // 下拉刷新
  onPullDownRefresh() {
    this.isRefreshing = true;
    this.page = 1;
    this.fetchBlogs(true).then(() => {
      common_vendor.index.stopPullDownRefresh();
      this.isRefreshing = false;
    }).catch(() => {
      common_vendor.index.stopPullDownRefresh();
      this.isRefreshing = false;
    });
  },
  // 上拉加载更多
  onReachBottom() {
    if (this.hasMoreData && !this.isLoading) {
      this.loadMore();
    }
  },
  methods: {
    // 获取博客列表
    async fetchBlogs(isRefresh = false) {
      this.isLoading = true;
      try {
        const res = await api_index.blogApi.getBlogs(this.page, this.pageSize);
        if (isRefresh) {
          this.blogList = res.data.data || [];
        } else {
          this.blogList = [...this.blogList, ...res.data.data || []];
        }
        this.hasMoreData = res.data.data && res.data.data.length === this.pageSize;
      } catch (error) {
        common_vendor.index.__f__("error", "at pages/index/index.vue:110", "获取博客列表失败:", error);
      } finally {
        this.isLoading = false;
      }
    },
    // 加载更多
    loadMore() {
      if (this.isLoading)
        return;
      this.page++;
      this.fetchBlogs();
    },
    // 跳转到博客详情页
    goToBlogDetail(id) {
      common_vendor.index.navigateTo({
        url: `/pages/detail/detail?id=${id}`
      });
    },
    // 处理点赞
    async handleLike(blog) {
      try {
        if (blog.isLiked) {
          await api_index.blogApi.unlikeBlog(blog.id);
          blog.isLiked = false;
          blog.likeCount = Math.max(0, (blog.likeCount || 1) - 1);
        } else {
          await api_index.blogApi.likeBlog(blog.id);
          blog.isLiked = true;
          blog.likeCount = (blog.likeCount || 0) + 1;
        }
      } catch (error) {
        common_vendor.index.__f__("error", "at pages/index/index.vue:145", "点赞操作失败:", error);
      }
    },
    // 格式化时间
    formatTime(timestamp) {
      if (!timestamp)
        return "";
      const date = new Date(timestamp);
      const now = /* @__PURE__ */ new Date();
      const diff = now - date;
      const minute = 60 * 1e3;
      const hour = 60 * minute;
      const day = 24 * hour;
      if (diff < minute) {
        return "刚刚";
      }
      if (diff < hour) {
        return Math.floor(diff / minute) + "分钟前";
      }
      if (diff < day) {
        return Math.floor(diff / hour) + "小时前";
      }
      if (diff < 30 * day) {
        return Math.floor(diff / day) + "天前";
      }
      const year = date.getFullYear();
      const month = (date.getMonth() + 1).toString().padStart(2, "0");
      const dayStr = date.getDate().toString().padStart(2, "0");
      return `${year}-${month}-${dayStr}`;
    }
  }
};
function _sfc_render(_ctx, _cache, $props, $setup, $data, $options) {
  return common_vendor.e({
    a: $data.isRefreshing
  }, $data.isRefreshing ? {} : {}, {
    b: common_vendor.f($data.blogList, (item, index, i0) => {
      return {
        a: item.author.avatar || "/static/default-avatar.png",
        b: common_vendor.t(item.author.nickname),
        c: common_vendor.t($options.formatTime(item.createTime)),
        d: common_vendor.t(item.title),
        e: common_vendor.t(item.summary),
        f: common_vendor.n(item.isLiked ? "icon-like-filled" : "icon-like"),
        g: common_vendor.t(item.likeCount || 0),
        h: common_vendor.o(($event) => $options.handleLike(item), index),
        i: common_vendor.t(item.commentCount || 0),
        j: index,
        k: common_vendor.o(($event) => $options.goToBlogDetail(item.id), index)
      };
    }),
    c: $data.hasMoreData
  }, $data.hasMoreData ? {
    d: common_vendor.o((...args) => $options.loadMore && $options.loadMore(...args))
  } : {}, {
    e: $data.blogList.length === 0 && !$data.isLoading
  }, $data.blogList.length === 0 && !$data.isLoading ? {
    f: common_assets._imports_0$1
  } : {});
}
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["render", _sfc_render]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../.sourcemap/mp-weixin/pages/index/index.js.map
