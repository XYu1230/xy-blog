<template>
  <view class="container">
    <view class="post-form card">
      <view class="form-item">
        <input
          type="text"
          class="input-box"
          v-model="blogData.title"
          placeholder="请输入博客标题"
          maxlength="50"
        />
        <text class="input-count">{{ blogData.title.length }}/50</text>
      </view>

      <view class="form-item">
        <textarea
          class="textarea-box"
          v-model="blogData.content"
          placeholder="请输入博客内容..."
          maxlength="5000"
        ></textarea>
        <text class="input-count">{{ blogData.content.length }}/5000</text>
      </view>

      <view class="form-item">
        <view class="tags-wrapper">
          <view class="tags-title">文章标签（最多3个）</view>
          <view class="tags-box">
            <view
              class="tag-item"
              :class="{ active: blogData.tags.includes(tag) }"
              v-for="(tag, index) in availableTags"
              :key="index"
              @click="toggleTag(tag)"
            >
              {{ tag }}
            </view>
          </view>
          <view class="add-tag-box">
            <input
              type="text"
              class="tag-input"
              v-model="customTag"
              placeholder="添加自定义标签"
              maxlength="10"
            />
            <button class="add-tag-btn" @click="addCustomTag">添加</button>
          </view>
        </view>
      </view>

      <view class="form-item">
        <view class="summary-title">文章摘要</view>
        <textarea
          class="summary-box"
          v-model="blogData.summary"
          placeholder="请输入文章摘要，不填将自动提取正文前100字"
          maxlength="200"
        ></textarea>
        <text class="input-count">{{ blogData.summary.length }}/200</text>
      </view>

      <view class="form-actions">
        <button class="submit-btn btn-primary" @click="submitBlog">发布博客</button>
      </view>
    </view>
  </view>
</template>

<script>
import { blogApi } from "@/api/index.js";

export default {
  data() {
    return {
      blogData: {
        title: "",
        content: "",
        summary: "",
        tags: [],
      },
      availableTags: ["前端", "JavaScript", "Vue", "React", "Node.js", "后端", "数据库", "算法", "面试", "其他"],
      customTag: "",
      isSubmitting: false,
      titleLimit: 50,
      contentLimit: 5000,
      summaryLimit: 200,
    };
  },
  onLoad() {
    // 检查用户是否登录
    const token = uni.getStorageSync("token");
    if (!token) {
      uni.showToast({
        title: "请先登录",
        icon: "none",
      });
      
      setTimeout(() => {
        uni.redirectTo({
          url: "/pages/login/login",
        });
      }, 1500);
    }
  },
  methods: {
    // 切换标签选择
    toggleTag(tag) {
      const index = this.blogData.tags.indexOf(tag);
      if (index === -1) {
        // 最多选择3个标签
        if (this.blogData.tags.length >= 3) {
          uni.showToast({
            title: "最多选择3个标签",
            icon: "none",
          });
          return;
        }
        this.blogData.tags.push(tag);
      } else {
        this.blogData.tags.splice(index, 1);
      }
    },
    
    // 添加自定义标签
    addCustomTag() {
      if (!this.customTag.trim()) {
        uni.showToast({
          title: "标签不能为空",
          icon: "none",
        });
        return;
      }
      
      // 最多选择3个标签
      if (this.blogData.tags.length >= 3) {
        uni.showToast({
          title: "最多选择3个标签",
          icon: "none",
        });
        return;
      }
      
      // 标签长度限制
      if (this.customTag.length > 10) {
        uni.showToast({
          title: "标签最多10个字符",
          icon: "none",
        });
        return;
      }
      
      // 检查是否已存在
      if (this.blogData.tags.includes(this.customTag)) {
        uni.showToast({
          title: "标签已存在",
          icon: "none",
        });
        return;
      }
      
      this.blogData.tags.push(this.customTag);
      this.customTag = "";
    },
    
    // 提交博客
    async submitBlog() {
      // 表单验证
      if (!this.blogData.title.trim()) {
        uni.showToast({
          title: "请输入标题",
          icon: "none",
        });
        return;
      }
      
      if (!this.blogData.content.trim()) {
        uni.showToast({
          title: "请输入内容",
          icon: "none",
        });
        return;
      }
      
      // 如果没有摘要，自动生成
      if (!this.blogData.summary.trim()) {
        // 从内容中提取前100个字符作为摘要
        this.blogData.summary = this.blogData.content.replace(/<[^>]+>/g, "").substring(0, 100);
      }
      
      this.isSubmitting = true;
      
      try {
        const res = await blogApi.publishBlog(this.blogData);
        
        uni.showToast({
          title: "发布成功",
          icon: "success",
        });
        
        // 发布成功后跳转到博客详情页
        setTimeout(() => {
          uni.redirectTo({
            url: `/pages/detail/detail?id=${res.data.id}`,
          });
        }, 1500);
      } catch (error) {
        console.error("发布博客失败:", error);
      } finally {
        this.isSubmitting = false;
      }
    },
  },
};
</script>

<style lang="scss">
.container {
  padding: 20rpx;
  background-color: #f5f5f5;
  min-height: 100vh;
}

.post-form {
  padding: 30rpx;
}

.form-item {
  margin-bottom: 30rpx;
  position: relative;
}

.input-box {
  width: 100%;
  height: 90rpx;
  font-size: 32rpx;
  padding: 0 20rpx;
  box-sizing: border-box;
  border-bottom: 1px solid #eee;
}

.textarea-box {
  width: 100%;
  height: 500rpx;
  font-size: 30rpx;
  padding: 20rpx;
  box-sizing: border-box;
  border: 1px solid #eee;
  border-radius: 10rpx;
  background-color: #f9f9f9;
}

.input-count {
  position: absolute;
  right: 20rpx;
  bottom: 10rpx;
  font-size: 24rpx;
  color: #999;
}

.tags-wrapper {
  margin-bottom: 30rpx;
}

.tags-title,
.summary-title {
  font-size: 30rpx;
  font-weight: bold;
  margin-bottom: 20rpx;
}

.tags-box {
  display: flex;
  flex-wrap: wrap;
  margin-bottom: 20rpx;
}

.tag-item {
  padding: 10rpx 30rpx;
  background-color: #f0f8ff;
  color: #333;
  border-radius: 30rpx;
  margin-right: 20rpx;
  margin-bottom: 20rpx;
  font-size: 26rpx;
}

.tag-item.active {
  background-color: #007aff;
  color: #fff;
}

.add-tag-box {
  display: flex;
  align-items: center;
}

.tag-input {
  flex: 1;
  height: 70rpx;
  font-size: 28rpx;
  padding: 0 20rpx;
  border: 1px solid #eee;
  border-radius: 10rpx;
}

.add-tag-btn {
  width: 150rpx;
  height: 70rpx;
  line-height: 70rpx;
  font-size: 28rpx;
  background-color: #007aff;
  color: #fff;
  margin-left: 20rpx;
}

.summary-box {
  width: 100%;
  height: 200rpx;
  font-size: 28rpx;
  padding: 20rpx;
  box-sizing: border-box;
  border: 1px solid #eee;
  border-radius: 10rpx;
  background-color: #f9f9f9;
}

.form-actions {
  margin-top: 50rpx;
}

.submit-btn {
  width: 100%;
  height: 90rpx;
  line-height: 90rpx;
  font-size: 32rpx;
}
</style>
