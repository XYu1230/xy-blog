/**
 * API接口封装
 */
import request from '@/utils/request.js';

// 用户相关API
export const userApi = {
	// 用户注册
	register(data) {
		return request.post('/user/register', data);
	},

	// 用户登录
	login(data) {
		return request.post('/user/login', data);
	},

	// 获取用户信息
	getUserInfo() {
		return request.get('/user/info');
	},

	// 更新用户信息
	updateUserInfo(data) {
		return request.put('/user/info', data);
	},
};

// 博客相关API
export const blogApi = {
	// 获取博客列表
	getBlogs(page = 1, pageSize = 10, params = {}) {
		return request.get('/blogs', {
			page,
			pageSize,
			...params
		});
	},

	// 获取博客详情
	getBlogDetail(id) {
		return request.get(`/blogs/${id}`);
	},

	// 发布博客
	publishBlog(data) {
		return request.post('/blogs', data);
	},

	// 更新博客
	updateBlog(id, data) {
		return request.put(`/blogs/${id}`, data);
	},

	// 删除博客
	deleteBlog(id) {
		return request.delete(`/blogs/${id}`);
	},

	// 点赞博客
	likeBlog(id) {
		return request.post(`/blogs/${id}/like`);
	},

	// 取消点赞
	unlikeBlog(id) {
		return request.delete(`/blogs/${id}/like`);
	},

	// 获取博客评论
	getBlogComments(blogId, page = 1, pageSize = 10) {
		return request.get(`/blogs/${blogId}/comments`, {
			page,
			pageSize
		});
	},

	// 发表评论
	addComment(blogId, content) {
		return request.post(`/blogs/${blogId}/comments`, {
			content
		});
	},

	// 删除评论
	deleteComment(commentId) {
		return request.delete(`/comments/${commentId}`);
	},
};