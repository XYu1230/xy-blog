<template>
	<view class="container">
		<!-- 博客详情 -->
		<view class="blog-detail card" v-if="blog">
			<view class="blog-header">
				<text class="blog-title">{{ blog.title }}</text>
				<view class="blog-info flex-row space-between align-center">
					<view class="blog-author flex-row align-center">
						<image :src="blog.author.avatar || '/static/default-avatar.png'" class="author-avatar"></image>
						<text class="author-name">{{ blog.author.nickname }}</text>
					</view>
					<text class="blog-time">{{ formatTime(blog.createTime) }}</text>
				</view>
			</view>

			<view class="blog-content">
				<rich-text :nodes="blog.content"></rich-text>
			</view>

			<view class="blog-footer flex-row space-between align-center">                                                             
				<view class="blog-stats flex-row align-center">
					<view class="stat-item flex-row align-center" @click="handleLike">
						<text class="iconfont" :class="blog.isLiked ? 'icon-like-filled' : 'icon-like'">❤</text>
						<text class="stat-count">点赞:{{ blog.likeCount || 0 }}</text>
					</view>
					<view class="stat-item flex-row align-center">
						<text class="iconfont icon-comment"></text>
						<text class="stat-count">评论:{{ blog.commentCount || 0 }}</text>
					</view>
				</view>

				<view class="blog-tags" v-if="blog.tags && blog.tags.length">
					<text class="tag" v-for="(tag, tagIndex) in blog.tags" :key="tagIndex">{{
            tag
          }}</text>
				</view>
			</view>
		</view>

		<!-- 评论区 -->
		<view class="comment-section card">
			<view class="section-title">评论区 ({{ comments.length }})</view>

			<!-- 评论列表 -->
			<view class="comment-list" v-if="comments.length > 0">
				<view class="comment-item" v-for="(item, index) in comments" :key="index">
					<view class="comment-info flex-row space-between">
						<view class="comment-author flex-row align-center">
							<image :src="item.user.avatar || '/static/default-avatar.png'" class="author-avatar">
							</image>
							<text class="author-name">{{ item.user.nickname }}</text>
						</view>
						<text class="comment-time">{{ formatTime(item.createTime) }}</text>
					</view>
					<view class="comment-content">
						<text>{{ item.content }}</text>
					</view>

					<!-- 删除评论按钮 (只有评论作者可见) -->
					<view class="comment-actions" v-if="isCurrentUserComment(item)">
						<text class="delete-btn" @click="deleteComment(item)">删除</text>
					</view>
				</view>
			</view>

			<!-- 暂无评论 -->
			<view class="empty-comment" v-else>
				<text class="empty-text">暂无评论，快来发表第一条评论吧</text>
			</view>

			<!-- 评论输入框 -->
			<view class="comment-input-container">
				<textarea class="comment-input" v-model="newComment" placeholder="发表你的评论..." maxlength="200"
					auto-height></textarea>
				<view class="comment-submit" @click="submitComment">
					<text>发送</text>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	import { blogApi } from '@/api/index.js';
	
	export default {
		data() {
			return {
				id: null,
				blog: null,
				comments: [],
				newComment: '',
				isLoading: false,
				isSubmitting: false,
				user: null,
				page: 1,
				pageSize: 10,
				hasMoreComments: true
			}
		},
		onLoad(options) {
			if (options.id) {
				this.id = options.id;
				this.fetchBlogDetail();
				this.fetchComments();
				
				// 获取当前用户信息
				const userInfo = uni.getStorageSync('user');
				if (userInfo) {
					this.user = userInfo;
				}
			} else {
				uni.showToast({
					title: '参数错误',
					icon: 'none'
				});
				setTimeout(() => {
					uni.navigateBack();
				}, 1500);
			}
		},
		methods: {
			// 获取博客详情
			async fetchBlogDetail() {
				this.isLoading = true;
				
				try {
					const res = await blogApi.getBlogDetail(this.id);
					this.blog = res.data;
				} catch (error) {
					console.error('获取博客详情失败:', error);
					uni.showToast({
						title: '获取博客详情失败',
						icon: 'none'
					});
				} finally {
					this.isLoading = false;
				}
			},
			
			// 获取评论列表
			async fetchComments(isRefresh = false) {
				try {
					const res = await blogApi.getBlogComments(this.id, this.page, this.pageSize);
					
					if (isRefresh) {
						this.comments = res.data.data || [];
					} else {
						this.comments = [...this.comments, ...(res.data.data || [])];
					}
					
					// 判断是否还有更多评论
					this.hasMoreComments = (res.data.data && res.data.data.length === this.pageSize);
				} catch (error) {
					console.error('获取评论失败:', error);
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
					uni.showToast({
						title: '评论内容不能为空',
						icon: 'none'
					});
					return;
				}
				
				this.isSubmitting = true;
				
				try {
					const res = await blogApi.addComment(this.id, this.newComment);
					
					// 添加新评论到列表顶部
					this.comments.unshift(res.data);
					
					// 清空输入框
					this.newComment = '';
					
					// 更新评论数
					if (this.blog) {
						this.blog.commentCount = (this.blog.commentCount || 0) + 1;
					}
					
					uni.showToast({
						title: '评论成功',
						icon: 'success'
					});
				} catch (error) {
					console.error('提交评论失败:', error);
				} finally {
					this.isSubmitting = false;
				}
			},
			
			// 删除评论
			async deleteComment(comment) {
				uni.showModal({
					title: '提示',
					content: '确定要删除这条评论吗？',
					success: async (res) => {
						if (res.confirm) {
							try {
								await blogApi.deleteComment(comment.id);
								
								// 从列表中移除
								const index = this.comments.findIndex(item => item.id === comment.id);
								if (index !== -1) {
									this.comments.splice(index, 1);
								}
								
								// 更新评论数
								if (this.blog) {
									this.blog.commentCount = Math.max(0, (this.blog.commentCount || 1) - 1);
								}
								
								uni.showToast({
									title: '删除成功',
									icon: 'success'
								});
							} catch (error) {
								console.error('删除评论失败:', error);
							}
						}
					}
				});
			},
			
			// 处理点赞
			async handleLike() {
				if (!this.blog) return;
				
				try {
					if (this.blog.isLiked) {
						// 已点赞，取消点赞
						await blogApi.unlikeBlog(this.id);
						this.blog.isLiked = false;
						this.blog.likeCount = Math.max(0, (this.blog.likeCount || 1) - 1);
					} else {
						// 未点赞，添加点赞
						await blogApi.likeBlog(this.id);
						this.blog.isLiked = true;
						this.blog.likeCount = (this.blog.likeCount || 0) + 1;
					}
				} catch (error) {
					console.error('点赞操作失败:', error);
				}
			},
			
			// 格式化时间
			formatTime(timestamp) {
				if (!timestamp) return '';
				
				const date = new Date(timestamp);
				const now = new Date();
				const diff = now - date;
				
				// 计算时间差
				const minute = 60 * 1000;
				const hour = 60 * minute;
				const day = 24 * hour;
				
				// 小于1分钟
				if (diff < minute) {
					return '刚刚';
				}
				// 小于1小时
				if (diff < hour) {
					return Math.floor(diff / minute) + '分钟前';
				}
				// 小于1天
				if (diff < day) {
					return Math.floor(diff / hour) + '小时前';
				}
				// 小于30天
				if (diff < 30 * day) {
					return Math.floor(diff / day) + '天前';
				}
				
				// 超过30天显示具体日期
				const year = date.getFullYear();
				const month = (date.getMonth() + 1).toString().padStart(2, '0');
				const dayStr = date.getDate().toString().padStart(2, '0');
				return `${year}-${month}-${dayStr}`;
			},
			
			// 判断是否是当前用户的评论
			isCurrentUserComment(comment) {
				return this.user && comment.user && this.user.id === comment.user.id;
			}
		}
	}
</script>

<style lang="scss">
	.container {
		padding: 20rpx;
		background-color: #f5f5f5;
		min-height: 100vh;
	}

	.blog-detail {
		margin-bottom: 20rpx;
	}

	.blog-header {
		padding-bottom: 20rpx;
		border-bottom: 1px solid #eee;
		margin-bottom: 20rpx;
	}

	.blog-title {
		font-size: 36rpx;
		font-weight: bold;
		margin-bottom: 20rpx;
		display: block;
	}

	.blog-info {
		margin-top: 10rpx;
	}

	.author-avatar {
		width: 60rpx;
		height: 60rpx;
		border-radius: 50%;
		margin-right: 10rpx;
	}

	.author-name {
		font-size: 28rpx;
		color: #333;
	}

	.blog-time {
		font-size: 24rpx;
		color: #999;
	}

	.blog-content {
		padding: 20rpx 0;
		font-size: 30rpx;
		line-height: 1.8;
		color: #333;
	}

	.blog-footer {
		padding-top: 20rpx;
		border-top: 1px solid #eee;
	}

	.stat-item {
	  margin-right: 30rpx;
	  padding: 10rpx 20rpx;
	  background-color: #f8f8f8;
	  border-radius: 30rpx;
	  display: flex;
	  align-items: center;
	  transition: all 0.3s ease-in-out;
	  
	  &:active {
	    background-color: #e0e0e0;
	  }
	}


	.iconfont {
	  font-size: 32rpx;
	  color: #666;
	  transition: color 0.3s ease-in-out;
	}
	
	.icon-like-filled {
	  color: #ff6b6b !important;
	}


	.stat-count {
	  font-size: 26rpx;
	  font-weight: bold;
	  color: #333;
	  margin-left: 6rpx;
	}


	.tag {
		padding: 6rpx 16rpx;
		background-color: #f0f8ff;
		color: #007aff;
		border-radius: 30rpx;
		font-size: 22rpx;
		margin-left: 10rpx;
	}

	.comment-section {
		padding-bottom: 120rpx;
	}

	.section-title {
		font-size: 32rpx;
		font-weight: bold;
		margin-bottom: 30rpx;
		padding-bottom: 15rpx;
		border-bottom: 1px solid #eee;
	}

	.comment-item {
		padding: 20rpx 0;
		border-bottom: 1px solid #f5f5f5;
	}

	.comment-info {
		margin-bottom: 10rpx;
	}

	.comment-author {
		font-size: 28rpx;
	}

	.comment-time {
		font-size: 24rpx;
		color: #999;
	}

	.comment-content {
		font-size: 28rpx;
		line-height: 1.5;
		color: #333;
		padding: 10rpx 0;
	}

	.comment-actions {
		text-align: right;
	}

	.delete-btn {
		font-size: 24rpx;
		color: #ff6b6b;
	}

	.empty-comment {
		padding: 40rpx 0;
		text-align: center;
	}

	.empty-text {
		color: #999;
		font-size: 28rpx;
	}

	.comment-input-container {
		position: fixed;
		bottom: 0;
		left: 0;
		right: 0;
		background-color: #fff;
		padding: 20rpx;
		box-shadow: 0 -2rpx 6rpx rgba(0, 0, 0, 0.1);
		display: flex;
		flex-direction: row;
		align-items: center;
	}

	.comment-input {
		flex: 1;
		height: 80rpx;
		background-color: #f5f5f5;
		border-radius: 40rpx;
		padding: 10rpx 30rpx;
		font-size: 28rpx;
	}

	.comment-submit {
		width: 120rpx;
		height: 70rpx;
		background-color: #007aff;
		color: #fff;
		border-radius: 35rpx;
		margin-left: 20rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		font-size: 28rpx;
	}
</style>