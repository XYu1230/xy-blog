<template>
	<view class="container">
		<!-- 列表下拉刷新 -->
		<view class="refresh-tip" v-if="isRefreshing">
			<text class="refresh-text">刷新中...</text>
		</view>
		
		<!-- 博客列表 -->
		<view class="blog-list">
			<view 
				class="blog-item card" 
				v-for="(item, index) in blogList" 
				:key="index"
				@click="goToBlogDetail(item.id)"
			>
				<view class="blog-header flex-row space-between align-center">
					<view class="blog-author flex-row align-center">
						<image :src="item.author.avatar || '/static/default-avatar.png'" class="author-avatar"></image>
						<text class="author-name">{{item.author.nickname}}</text>
					</view>
					<text class="blog-time">{{formatTime(item.createTime)}}</text>
				</view>
				
				<view class="blog-content">
					<text class="blog-title">{{item.title}}</text>
					<text class="blog-summary">{{item.summary}}</text>
				</view>
				
				<view class="blog-footer flex-row space-between align-center">
					<view class="blog-stats flex-row align-center" @click.stop="handleLike(item)">
						<text class="iconfont" :class="item.isLiked ? 'icon-like-filled' : 'icon-like'"></text>
						<text class="stat-count">{{item.likeCount || 0}}</text>
					</view>
					<view class="stat-item flex-row align-center">
						<text class="iconfont icon-comment"></text>
						<text class="stat-count">{{item.commentCount || 0}}</text>
					</view>
				</view>
			</view>
			
			<!-- 加载更多 -->
			<view class="load-more" v-if="hasMoreData">
				<text @click="loadMore" class="load-more-text">加载更多</text>
			</view>
			<view class="no-more" v-else>
				<text class="no-more-text">没有更多数据了</text>
			</view>
		</view>
		
		<!-- 暂无数据提示 -->
		<view class="empty-container" v-if="blogList.length === 0 && !isLoading">
			<image src="/static/empty.png" mode="aspectFit" class="empty-icon"></image>
			<text class="empty-text">暂无博客，快来发布第一篇吧</text>
		</view>
	</view>
</template>

<script>
	import { blogApi } from '@/api/index.js';
	
	export default {
		data() {
			return {
				blogList: [],
				page: 1,
				pageSize: 10,
				hasMoreData: true,
				isLoading: false,
				isRefreshing: false
			}
		},
		onLoad() {
			this.fetchBlogs();
		},
		// 下拉刷新
		onPullDownRefresh() {
			this.isRefreshing = true;
			this.page = 1;
			this.fetchBlogs(true).then(() => {
				uni.stopPullDownRefresh();
				this.isRefreshing = false;
			}).catch(() => {
				uni.stopPullDownRefresh();
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
					const res = await blogApi.getBlogs(this.page, this.pageSize);
					
					if (isRefresh) {
						this.blogList = res.data.data || [];
					} else {
						this.blogList = [...this.blogList, ...(res.data.data || [])];
					}
					
					// 判断是否还有更多数据
					this.hasMoreData = (res.data.data && res.data.data.length === this.pageSize);
				} catch (error) {
					console.error('获取博客列表失败:', error);
				} finally {
					this.isLoading = false;
				}
			},
			
			// 加载更多
			loadMore() {
				if (this.isLoading) return;
				this.page++;
				this.fetchBlogs();
			},
			
			// 跳转到博客详情页
			goToBlogDetail(id) {
				uni.navigateTo({
					url: `/pages/detail/detail?id=${id}`
				});
			},
			
			// 处理点赞
			async handleLike(blog) {
				try {
					if (blog.isLiked) {
						// 已点赞，取消点赞
						await blogApi.unlikeBlog(blog.id);
						blog.isLiked = false;
						blog.likeCount = Math.max(0, (blog.likeCount || 1) - 1);
					} else {
						// 未点赞，添加点赞
						await blogApi.likeBlog(blog.id);
						blog.isLiked = true;
						blog.likeCount = (blog.likeCount || 0) + 1;
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
	
	.refresh-tip {
		text-align: center;
		padding: 10rpx 0;
		color: #999;
		font-size: 26rpx;
	}
	
	.blog-list {
		width: 100%;
	}
	
	.blog-item {
		margin-bottom: 20rpx;
	}
	
	.blog-header {
		margin-bottom: 20rpx;
	}
	
	.blog-author {
		font-size: 28rpx;
	}
	
	.author-avatar {
		width: 60rpx;
		height: 60rpx;
		border-radius: 50%;
		margin-right: 10rpx;
	}
	
	.author-name {
		color: #333;
		font-weight: 500;
	}
	
	.blog-time {
		font-size: 24rpx;
		color: #999;
	}
	
	.blog-content {
		margin-bottom: 20rpx;
	}
	
	.blog-title {
		font-size: 32rpx;
		font-weight: bold;
		margin-bottom: 10rpx;
		display: block;
	}
	
	.blog-summary {
		font-size: 28rpx;
		color: #666;
		display: block;
		line-height: 1.5;
		display: -webkit-box;
		-webkit-line-clamp: 3;
		-webkit-box-orient: vertical;
		overflow: hidden;
		text-overflow: ellipsis;
	}
	
	.blog-footer {
		border-top: 1px solid #eee;
		padding-top: 20rpx;
	}
	
	.stat-item {
		margin-right: 30rpx;
	}
	
	.iconfont {
		font-size: 32rpx;
		color: #999;
	}
	
	.icon-like-filled {
		color: #FF6B6B;
	}
	
	.stat-count {
		font-size: 24rpx;
		color: #999;
		margin-left: 6rpx;
	}
	
	.blog-tags {
		display: flex;
		flex-wrap: wrap;
	}
	
	.tag {
		padding: 6rpx 16rpx;
		background-color: #f0f8ff;
		color: #007AFF;
		border-radius: 30rpx;
		font-size: 22rpx;
		margin-left: 10rpx;
	}
	
	.load-more, .no-more {
		text-align: center;
		padding: 30rpx 0;
	}
	
	.load-more-text {
		color: #007AFF;
		font-size: 28rpx;
	}
	
	.no-more-text {
		color: #999;
		font-size: 28rpx;
	}
	
	.empty-container {
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
		padding-top: 200rpx;
	}
	
	.empty-icon {
		width: 200rpx;
		height: 200rpx;
		margin-bottom: 20rpx;
	}
	
	.empty-text {
		color: #999;
		font-size: 28rpx;
	}
</style>
