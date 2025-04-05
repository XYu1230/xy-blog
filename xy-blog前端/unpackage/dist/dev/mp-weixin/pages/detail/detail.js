"use strict";
const common_vendor = require("../../common/vendor.js");
const api_index = require("../../api/index.js");
const _sfc_main = {
  data() {
    return {
      id: null,
      blog: null,
      comments: [],
      newComment: "",
      isLoading: false,
      isSubmitting: false,
      user: null,
      page: 1,
      pageSize: 10,
      hasMoreComments: true
    };
  },
  onLoad(options) {
    if (options.id) {
      this.id = options.id;
      this.fetchBlogDetail();
      this.fetchComments();
      const userInfo = common_vendor.index.getStorageSync("user");
      if (userInfo) {
        this.user = userInfo;
      }
    } else {
      common_vendor.index.showToast({
        title: "参数错误",
        icon: "none"
      });
      setTimeout(() => {
        common_vendor.index.navigateBack();
      }, 1500);
    }
  },
  methods: {
    // 获取博客详情
    async fetchBlogDetail() {
      this.isLoading = true;
      try {
        const res = await api_index.blogApi.getBlogDetail(this.id);
        this.blog = res.data;
      } catch (error) {
        common_vendor.index.__f__("error", "at pages/detail/detail.vue:131", "获取博客详情失败:", error);
        common_vendor.index.showToast({
          title: "获取博客详情失败",
          icon: "none"
        });
      } finally {
        this.isLoading = false;
      }
    },
    // 获取评论列表
    async fetchComments(isRefresh = false) {
      try {
        const res = await api_index.blogApi.getBlogComments(this.id, this.page, this.pageSize);
        if (isRefresh) {
          this.comments = res.data.data || [];
        } else {
          this.comments = [...this.comments, ...res.data.data || []];
        }
        this.hasMoreComments = res.data.data && res.data.data.length === this.pageSize;
      } catch (error) {
        common_vendor.index.__f__("error", "at pages/detail/detail.vue:155", "获取评论失败:", error);
      }
    },
    // 加载更多评论
    loadMoreComments() {
      if (this.hasMoreComments) {
        this.page++;
        this.fetchComments();
      }
    },
    // 提交评论
    async submitComment() {
      if (!this.newComment.trim()) {
        common_vendor.index.showToast({
          title: "评论内容不能为空",
          icon: "none"
        });
        return;
      }
      this.isSubmitting = true;
      try {
        const res = await api_index.blogApi.addComment(this.id, this.newComment);
        this.comments.unshift(res.data);
        this.newComment = "";
        if (this.blog) {
          this.blog.commentCount = (this.blog.commentCount || 0) + 1;
        }
        common_vendor.index.showToast({
          title: "评论成功",
          icon: "success"
        });
      } catch (error) {
        common_vendor.index.__f__("error", "at pages/detail/detail.vue:198", "提交评论失败:", error);
      } finally {
        this.isSubmitting = false;
      }
    },
    // 删除评论
    async deleteComment(comment) {
      common_vendor.index.showModal({
        title: "提示",
        content: "确定要删除这条评论吗？",
        success: async (res) => {
          if (res.confirm) {
            try {
              await api_index.blogApi.deleteComment(comment.id);
              const index = this.comments.findIndex((item) => item.id === comment.id);
              if (index !== -1) {
                this.comments.splice(index, 1);
              }
              if (this.blog) {
                this.blog.commentCount = Math.max(0, (this.blog.commentCount || 1) - 1);
              }
              common_vendor.index.showToast({
                title: "删除成功",
                icon: "success"
              });
            } catch (error) {
              common_vendor.index.__f__("error", "at pages/detail/detail.vue:230", "删除评论失败:", error);
            }
          }
        }
      });
    },
    // 处理点赞
    async handleLike() {
      if (!this.blog)
        return;
      try {
        if (this.blog.isLiked) {
          await api_index.blogApi.unlikeBlog(this.id);
          this.blog.isLiked = false;
          this.blog.likeCount = Math.max(0, (this.blog.likeCount || 1) - 1);
        } else {
          await api_index.blogApi.likeBlog(this.id);
          this.blog.isLiked = true;
          this.blog.likeCount = (this.blog.likeCount || 0) + 1;
        }
      } catch (error) {
        common_vendor.index.__f__("error", "at pages/detail/detail.vue:254", "点赞操作失败:", error);
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
    },
    // 判断是否是当前用户的评论
    isCurrentUserComment(comment) {
      return this.user && comment.user && this.user.id === comment.user.id;
    }
  }
};
function _sfc_render(_ctx, _cache, $props, $setup, $data, $options) {
  return common_vendor.e({
    a: $data.blog
  }, $data.blog ? common_vendor.e({
    b: common_vendor.t($data.blog.title),
    c: $data.blog.author.avatar || "/static/default-avatar.png",
    d: common_vendor.t($data.blog.author.nickname),
    e: common_vendor.t($options.formatTime($data.blog.createTime)),
    f: $data.blog.content,
    g: common_vendor.n($data.blog.isLiked ? "icon-like-filled" : "icon-like"),
    h: common_vendor.t($data.blog.likeCount || 0),
    i: common_vendor.o((...args) => $options.handleLike && $options.handleLike(...args)),
    j: common_vendor.t($data.blog.commentCount || 0),
    k: $data.blog.tags && $data.blog.tags.length
  }, $data.blog.tags && $data.blog.tags.length ? {
    l: common_vendor.f($data.blog.tags, (tag, tagIndex, i0) => {
      return {
        a: common_vendor.t(tag),
        b: tagIndex
      };
    })
  } : {}) : {}, {
    m: common_vendor.t($data.comments.length),
    n: $data.comments.length > 0
  }, $data.comments.length > 0 ? {
    o: common_vendor.f($data.comments, (item, index, i0) => {
      return common_vendor.e({
        a: item.user.avatar || "/static/default-avatar.png",
        b: common_vendor.t(item.user.nickname),
        c: common_vendor.t($options.formatTime(item.createTime)),
        d: common_vendor.t(item.content),
        e: $options.isCurrentUserComment(item)
      }, $options.isCurrentUserComment(item) ? {
        f: common_vendor.o(($event) => $options.deleteComment(item), index)
      } : {}, {
        g: index
      });
    })
  } : {}, {
    p: $data.newComment,
    q: common_vendor.o(($event) => $data.newComment = $event.detail.value),
    r: common_vendor.o((...args) => $options.submitComment && $options.submitComment(...args))
  });
}
const MiniProgramPage = /* @__PURE__ */ common_vendor._export_sfc(_sfc_main, [["render", _sfc_render]]);
wx.createPage(MiniProgramPage);
//# sourceMappingURL=../../../.sourcemap/mp-weixin/pages/detail/detail.js.map
