package com.stdu.service;

import com.stdu.entity.Tag;

import java.util.List;

/**
 * 标签服务接口
 */
public interface TagService {
    
    /**
     * 获取所有标签
     */
    List<Tag> getAllTags();
    
    /**
     * 根据名称获取标签，不存在则创建
     */
    Tag getOrCreateTag(String name);
    
    /**
     * 根据博客ID获取标签
     */
    List<Tag> getTagsByBlogId(Integer blogId);
} 