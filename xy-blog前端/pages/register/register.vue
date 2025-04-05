<template>
	<view class="register-container">
		<view class="logo-box">
			<image src="/static/logo.png" mode="aspectFit" class="logo"></image>
			<text class="title">注册账号</text>
		</view>
		
		<view class="form-box">
			<view class="form-item">
				<input type="text" v-model="formData.username" placeholder="请输入学号" class="input-box" confirm-type="next" />
			</view>
			
			<view class="form-item">
				<input type="password" v-model="formData.password" placeholder="请输入密码" class="input-box" confirm-type="next" />
			</view>
			
			<view class="form-item">
				<input type="password" v-model="formData.confirmPassword" placeholder="请确认密码" class="input-box" confirm-type="next" />
			</view>
			
			<view class="form-item">
				<input type="text" v-model="formData.nickname" placeholder="请输入昵称" class="input-box" confirm-type="done" />
			</view>
			
			<view class="form-item">
				<button @click="handleRegister" class="btn-primary register-btn">注 册</button>
			</view>
			
			<!-- 测试用户快速注册 -->
			<!-- <view class="form-item">
				<button @click="fillTestInfo" class="btn-test">填充测试信息</button>
			</view> -->
			
			<view class="form-item login-link">
				<text>已有账号？</text>
				<text @click="goToLogin" class="link">去登录</text>
			</view>
		</view>
	</view>
</template>

<script>
	import { userApi } from '@/api/index.js';
	
	export default {
		data() {
			return {
				formData: {
					username: '',
					password: '',
					confirmPassword: '',
					nickname: ''
				},
				// 测试用户信息
				testInfo: {
					username: 'testuser',
					password: '123456',
					nickname: '测试用户'
				}
			}
		},
		methods: {
			// 处理注册
			async handleRegister() {
				// 表单验证
				if (!this.formData.username.trim()) {
					uni.showToast({
						title: '请输入用户名',
						icon: 'none'
					});
					return;
				}
				
				if (!this.formData.password.trim()) {
					uni.showToast({
						title: '请输入密码',
						icon: 'none'
					});
					return;
				}
				
				if (this.formData.password !== this.formData.confirmPassword) {
					uni.showToast({
						title: '两次密码输入不一致',
						icon: 'none'
					});
					return;
				}
				
				if (!this.formData.nickname.trim()) {
					uni.showToast({
						title: '请输入昵称',
						icon: 'none'
					});
					return;
				}
				
				// 构建注册参数
				const registerData = {
					username: this.formData.username,
					password: this.formData.password,
					nickname: this.formData.nickname
				};
				
				try {
					const res = await userApi.register(registerData);
					console.log(res);
					if(res.code === 500){
						uni.showToast({
							title: res.message,
							icon:'error'
						});
					}else{
						uni.showToast({
							title: '注册成功',
							icon: 'success'
						});
						
						// 注册成功后跳转到登录页
						setTimeout(() => {
							uni.navigateTo({
								url: '/pages/login/login'
							})
						}, 1500);
					}	
				} catch (error) {
					console.error('注册失败:', error);
				}
			},
			
			// 填充测试信息
			// fillTestInfo() {
			// 	this.formData.username = this.testInfo.username;
			// 	this.formData.password = this.testInfo.password;
			// 	this.formData.confirmPassword = this.testInfo.password;
			// 	this.formData.nickname = this.testInfo.nickname;
				
			// 	uni.showToast({
			// 		title: '已填充测试信息',
			// 		icon: 'none',
			// 		duration: 1000
			// 	});
			// },
			
			// 跳转到登录页面
			goToLogin() {
				uni.navigateTo({
					url: '/pages/login/login'
				})
			}
		}
	}
</script>

<style lang="scss">
	.register-container {
		display: flex;
		flex-direction: column;
		padding: 40rpx;
		height: 100vh;
		background-color: #f5f5f5;
	}
	
	.logo-box {
		display: flex;
		flex-direction: column;
		align-items: center;
		margin-top: 30rpx;
		margin-bottom: 40rpx;
	}
	
	.logo {
		width: 120rpx;
		height: 120rpx;
	}
	
	.title {
		font-size: 36rpx;
		font-weight: bold;
		margin-top: 20rpx;
	}
	
	.form-box {
		background-color: #fff;
		border-radius: 20rpx;
		padding: 40rpx;
		box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.1);
	}
	
	.input-box {
		width: 100%;
		height: 90rpx;
		background-color: #ffffff;
		border: 1px solid #dddddd;
		border-radius: 10rpx;
		padding: 0 20rpx;
		box-sizing: border-box;
		font-size: 28rpx;
		color: #333;
	}
	
	.register-btn {
		width: 100%;
		height: 90rpx;
		line-height: 90rpx;
		font-size: 32rpx;
		margin-top: 30rpx;
	}
	
	.btn-test {
		width: 100%;
		height: 90rpx;
		line-height: 90rpx;
		font-size: 32rpx;
		background-color: #f0f0f0;
		color: #666;
		border: none;
		border-radius: 10rpx;
	}
	
	.login-link {
		text-align: center;
		font-size: 28rpx;
		color: #666;
	}
	
	.link {
		color: #007AFF;
		margin-left: 10rpx;
	}
</style> 