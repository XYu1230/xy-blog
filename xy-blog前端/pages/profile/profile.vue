<template>
	<view class="container">
		<view class="user-header card">
			<view class="user-info flex-row">
				<image :src="userInfo.avatar || '/static/default-avatar.png'" class="user-avatar"></image>
				<view class="user-meta">
					<text class="user-nickname">{{userInfo.nickname || '未设置昵称'}}</text>
					<text class="user-id">ID: {{userInfo.id || ''}}</text>
				</view>
			</view>
			
			<view class="user-stats flex-row">
				<view class="stat-item">
					<text class="stat-value">{{userStats.blogCount || 0}}</text>
					<text class="stat-label">博客</text>
				</view>
				<view class="stat-item">
					<text class="stat-value">{{userStats.likeCount || 0}}</text>
					<text class="stat-label">获赞</text>
				</view>
				<view class="stat-item">
					<text class="stat-value">{{userStats.commentCount || 0}}</text>
					<text class="stat-label">评论</text>
				</view>
			</view>
		</view>
		
		<view class="menu-list card">
			<view class="menu-item flex-row space-between align-center" @click="goToMyBlogs">
				<view class="menu-left flex-row align-center">
					<text class="iconfont icon-blog"></text>
					<text class="menu-text">我的博客</text>
				</view>
				<text class="iconfont icon-right"></text>
			</view>
			<view class="menu-item flex-row space-between align-center" @click="goToMyLikes">
				<view class="menu-left flex-row align-center">
					<text class="iconfont icon-like"></text>
					<text class="menu-text">我的点赞</text>
				</view>
				<text class="iconfont icon-right"></text>
			</view>
			<view class="menu-item flex-row space-between align-center" @click="goToMyComments">
				<view class="menu-left flex-row align-center">
					<text class="iconfont icon-comment"></text>
					<text class="menu-text">我的评论</text>
				</view>
				<text class="iconfont icon-right"></text>
			</view>
		</view>
		
		<view class="menu-list card">
			<view class="menu-item flex-row space-between align-center" @click="goToSettings">
				<view class="menu-left flex-row align-center">
					<text class="iconfont icon-settings"></text>
					<text class="menu-text">账号设置</text>
				</view>
				<text class="iconfont icon-right"></text>
			</view>
			<view class="menu-item flex-row space-between align-center" @click="goToAbout">
				<view class="menu-left flex-row align-center">
					<text class="iconfont icon-about"></text>
					<text class="menu-text">关于我们</text>
				</view>
				<text class="iconfont icon-right"></text>
			</view>
		</view>
		
		<view class="logout-btn-wrapper">
			<button class="logout-btn" @click="logout">退出登录</button>
		</view>
	</view>
</template>

<script>
	import { userApi } from '@/api/index.js';
	
	export default {
		data() {
			return {
				userInfo: null,
				isLoading: false,
				userStats: {
					blogCount: 0,
					likeCount: 0,
					commentCount: 0
				}
			}
		},
		onShow() {
			this.getUserInfo();
		},
		methods: {
			// 获取用户信息
			async getUserInfo() {
				this.isLoading = true;
				
				// 先从本地存储获取
				const userInfo = uni.getStorageSync('user');
				if (userInfo) {
					this.userInfo = userInfo;
				}
				
				// 再从服务器获取最新数据
				try {
					const res = await userApi.getUserInfo();
					this.userInfo = res.data.userInfo;
					this.userStats = res.data.stats;
					console.log(res.data);
					
					// 更新本地存储
					uni.setStorageSync('user', res.data);
				} catch (error) {
					console.error('获取用户信息失败:', error);
				} finally {
					this.isLoading = false;
				}
			},
			
			// 跳转到我的博客
			goToMyBlogs() {
				uni.navigateTo({
					url: '/pages/my-blogs/my-blogs'
				});
			},
			
			// 跳转到我的点赞
			goToMyLikes() {
				uni.navigateTo({
					url: '/pages/my-likes/my-likes'
				});
			},
			
			// 跳转到我的评论
			goToMyComments() {
				uni.navigateTo({
					url: '/pages/my-comments/my-comments'
				});
			},
			
			// 跳转到账号设置
			goToSettings() {
				uni.navigateTo({
					url: '/pages/settings/settings'
				});
			},
			
			// 跳转到关于我们
			goToAbout() {
				uni.navigateTo({
					url: '/pages/about/about'
				});
			},
			
			// 退出登录
			logout() {
				uni.showModal({
					title: '提示',
					content: '确定要退出登录吗？',
					success: (res) => {
						if (res.confirm) {
							// 清除本地存储的用户信息和token
							uni.removeStorageSync('token');
							uni.removeStorageSync('user');
							
							// 跳转到登录页
							uni.navigateTo({
								url: '/pages/login/login'
							});
						}
					}
				});
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
	
	.user-header {
		padding: 40rpx 30rpx;
		margin-bottom: 20rpx;
	}
	
	.user-info {
		margin-bottom: 40rpx;
	}
	
	.user-avatar {
		width: 120rpx;
		height: 120rpx;
		border-radius: 50%;
		margin-right: 30rpx;
	}
	
	.user-meta {
		display: flex;
		flex-direction: column;
		justify-content: center;
	}
	
	.user-nickname {
		font-size: 36rpx;
		font-weight: bold;
		margin-bottom: 10rpx;
	}
	
	.user-id {
		font-size: 24rpx;
		color: #999;
	}
	
	.user-stats {
		justify-content: space-around;
		border-top: 1px solid #eee;
		padding-top: 30rpx;
	}
	
	.stat-item {
		display: flex;
		flex-direction: column;
		align-items: center;
	}
	
	.stat-value {
		font-size: 32rpx;
		font-weight: bold;
		color: #333;
		margin-bottom: 8rpx;
	}
	
	.stat-label {
		font-size: 24rpx;
		color: #999;
	}
	
	.menu-list {
		margin-bottom: 20rpx;
	}
	
	.menu-item {
		height: 100rpx;
		padding: 0 30rpx;
		border-bottom: 1px solid #f5f5f5;
	}
	
	.menu-item:last-child {
		border-bottom: none;
	}
	
	.menu-left {
		font-size: 30rpx;
	}
	
	.iconfont {
		font-size: 40rpx;
		color: #007AFF;
		margin-right: 20rpx;
	}
	
	.icon-right {
		font-size: 30rpx;
		color: #ccc;
		margin-right: 0;
	}
	
	.menu-text {
		color: #333;
	}
	
	.logout-btn-wrapper {
		padding: 40rpx 30rpx;
	}
	
	.logout-btn {
		width: 100%;
		height: 90rpx;
		line-height: 90rpx;
		background-color: #FF6B6B;
		color: #fff;
		border-radius: 10rpx;
		font-size: 32rpx;
	}
</style> 