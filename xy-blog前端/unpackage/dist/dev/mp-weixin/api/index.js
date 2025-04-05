"use strict";
const utils_request = require("../utils/request.js");
const userApi = {
  // 用户注册
  register(data) {
    return utils_request.request.post("/user/register", data);
  },
  // 用户登录
  login(data) {
    return utils_request.request.post("/user/login", data);
  },
  // 获取用户信息
  getUserInfo() {
    return utils_request.request.get("/user/info");
  },
  // 更新用户信息
  updateUserInfo(data) {
    return utils_request.request.put("/user/info", data);
  }
};
const blogApi = {
  // 获取博客列表
  getBlogs(page = 1, pageSize = 10, params = {}) {
    return utils_request.request.get("/blogs", {
      page,
      pageSize,
      ...params
    });
  },
  // 获取博客详情
  getBlogDetail(id) {
    return utils_request.request.get(`/blogs/${id}`);
  },
  // 发布博客
  publishBlog(data) {
    return utils_request.request.post("/blogs", data);
  },
  // 更新博客
  updateBlog(id, data) {
    return utils_request.request.put(`/blogs/${id}`, data);
  },
  // 删除博客
  deleteBlog(id) {
    return utils_request.request.delete(`/blogs/${id}`);
  },
  // 点赞博客
  likeBlog(id) {
    return utils_request.request.post(`/blogs/${id}/like`);
  },
  // 取消点赞
  unlikeBlog(id) {
    return utils_request.request.delete(`/blogs/${id}/like`);
  },
  // 获取博客评论
  getBlogComments(blogId, page = 1, pageSize = 10) {
    return utils_request.request.get(`/blogs/${blogId}/comments`, {
      page,
      pageSize
    });
  },
  // 发表评论
  addComment(blogId, content) {
    return utils_request.request.post(`/blogs/${blogId}/comments`, {
      content
    });
  },
  // 删除评论
  deleteComment(commentId) {
    return utils_request.request.delete(`/comments/${commentId}`);
  }
};
exports.blogApi = blogApi;
exports.userApi = userApi;
//# sourceMappingURL=../../.sourcemap/mp-weixin/api/index.js.map
