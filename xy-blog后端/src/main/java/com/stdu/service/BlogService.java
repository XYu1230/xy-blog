package com.stdu.service;

import com.stdu.dto.BlogListResponse;
import com.stdu.dto.BlogRequest;
import com.stdu.dto.BlogResponse;

/**
 * 博客服务接口
 */
public interface BlogService {
    
    /**
     * 获取博客列表
     */
    BlogListResponse getBlogList(String keyword, String tag, Integer page, Integer pageSize, Long userId);
    
    /**
     * 获取博客详情
     */
    BlogResponse getBlogDetail(Integer blogId, Long userId);
    
    /**
     * 发布博客
     */
    BlogResponse createBlog(Long userId, BlogRequest request);
    
    /**
     * 更新博客
     */
    BlogResponse updateBlog(Integer blogId, Long userId, BlogRequest request);
    
    /**
     * 删除博客
     */
    void deleteBlog(Integer blogId, Long userId);
    
    /**
     * 点赞博客
     */
    Integer likeBlog(Integer blogId, Long userId);
    
    /**
     * 取消点赞
     */
    Integer unlikeBlog(Integer blogId, Long userId);
} 