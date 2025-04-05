<template>
  <view class="login-container">
    <view class="logo-box">
      <image src="/static/logo.png" mode="aspectFit" class="logo"></image>
      <text class="title">星雨博客系统</text>
    </view>

    <view class="form-box">
      <view class="form-item">
        <input
          type="text"
          v-model="formData.username"
          placeholder="请输入学号"
          class="input-box"
          confirm-type="next"
        />
      </view>

      <view class="form-item">
        <input
          type="password"
          v-model="formData.password"
          placeholder="请输入密码"
          class="input-box"
          confirm-type="done"
        />
      </view>

      <view class="form-item">
        <button @click="handleLogin" class="btn-primary login-btn">登 录</button>
      </view>
      
      <!-- 添加测试用户按钮 -->
      <!-- <view class="form-item">
        <button @click="useTestAccount" class="btn-test">使用测试账号</button>
      </view> -->

      <view class="form-item register-link">
        <text>还没有账号？</text>
        <text @click="goToRegister" class="link">立即注册</text>
      </view>
    </view>
  </view>
</template>

<script>
import { userApi } from "@/api/index.js";

export default {
  data() {
    return {
      formData: {
        username: "",
        password: "",
      },
    };
  },
  methods: {
    // 处理登录
    async handleLogin() {
      // 表单验证
      if (!this.formData.username.trim()) {
        uni.showToast({
          title: "请输入用户名",
          icon: "none",
        });
        return;
      }

      if (!this.formData.password.trim()) {
        uni.showToast({
          title: "请输入密码",
          icon: "none",
        });
        return;
      }

      try {
        const res = await userApi.login(this.formData);

        // 存储token和用户信息
        uni.setStorageSync("token", res.data.token);
        uni.setStorageSync("user", res.data.user);

        uni.showToast({
          title: "登录成功",
          icon: "success",
        });

        // 跳转到首页
        setTimeout(() => {
          uni.switchTab({
            url: "/pages/index/index",
          });
        }, 1500);
      } catch (error) {
        console.error("登录失败:", error);
      }
    },

    // 使用测试账号
    // useTestAccount() {
    //   this.formData.username = this.testAccount.username;
    //   this.formData.password = this.testAccount.password;
      
    //   uni.showToast({
    //     title: "已填充测试账号",
    //     icon: "none",
    //     duration: 1000
    //   });
      
    //   // 自动登录
    //   setTimeout(() => {
    //     this.handleLogin();
    //   }, 1000);
    // },

    // 跳转到注册页面
    goToRegister() {
      uni.navigateTo({
        url: "/pages/register/register",
      });
    },
  },
};
</script>

<style lang="scss">
.login-container {
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
  margin-top: 60rpx;
  margin-bottom: 80rpx;
}

.logo {
  width: 160rpx;
  height: 160rpx;
}

.title {
  font-size: 40rpx;
  font-weight: bold;
  margin-top: 20rpx;
}

.form-box {
  background-color: #fff;
  border-radius: 20rpx;
  padding: 40rpx;
  box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.1);
}

.form-item {
  margin-bottom: 30rpx;
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

.login-btn {
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

.register-link {
  text-align: center;
  font-size: 28rpx;
  color: #666;
}

.link {
  color: #007aff;
  margin-left: 10rpx;
}
</style>
