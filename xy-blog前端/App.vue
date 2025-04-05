<script>
export default {
  onLaunch: function () {
    console.log("App Launch");
    // 检查token
    const token = uni.getStorageSync("token");
    if (!token) {
      // 如果没有token，检查当前页面是否为登录或注册页面
      const pages = getCurrentPages();
      const currentPage = pages.length > 0 ? pages[pages.length - 1].route : '';
      
      // 如果不是登录页或注册页，跳转到登录页
      if (currentPage !== 'pages/login/login' && currentPage !== 'pages/register/register') {
        uni.reroute({
          url: "/pages/login/login",
        });
      }
    }
  },
  onShow: function () {
    console.log("App Show");
  },
  onHide: function () {
    console.log("App Hide");
  },
  globalData: {
    baseUrl: "http://localhost:3000/api", // 后端接口基础URL，根据实际情况修改
  },
};
</script>

<style>
/*每个页面公共css */
page {
  font-family: -apple-system, BlinkMacSystemFont, "Helvetica Neue", Helvetica, Segoe UI,
    Arial, Roboto, "PingFang SC", "miui", "Hiragino Sans GB", "Microsoft Yahei",
    sans-serif;
  font-size: 14px;
  color: #333;
  background-color: #f5f5f5;
}

.container {
  padding: 20rpx;
}

.btn-primary {
  background-color: #007aff;
  color: #ffffff;
  border: none;
  border-radius: 10rpx;
}

.btn-primary:active {
  background-color: #0062cc;
}

.form-item {
  margin-bottom: 30rpx;
}

.input-box {
  width: 100%;
  background-color: #ffffff;
  border: 1px solid #dddddd;
  border-radius: 10rpx;
  padding: 20rpx;
  box-sizing: border-box;
}

.card {
  background-color: #ffffff;
  border-radius: 10rpx;
  padding: 20rpx;
  margin-bottom: 20rpx;
  box-shadow: 0 2rpx 6rpx rgba(0, 0, 0, 0.1);
}

.flex-row {
  display: flex;
  flex-direction: row;
}

.flex-column {
  display: flex;
  flex-direction: column;
}

.space-between {
  justify-content: space-between;
}

.align-center {
  align-items: center;
}
</style>
