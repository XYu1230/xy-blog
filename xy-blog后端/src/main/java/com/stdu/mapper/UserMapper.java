package com.stdu.mapper;

import com.stdu.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 用户Mapper接口
 */
@Mapper
public interface UserMapper {
    
    /**
     * 根据用户名查询用户
     */
    User findByUsername(String username);
    
    /**
     * 根据ID查询用户
     */
    User findById(Long id);
    
    /**
     * 插入用户
     */
    int insert(User user);
    
    /**
     * 更新用户信息
     */
    int update(User user);
    
    /**
     * 获取用户的博客数量
     */
    int countUserBlogs(Long userId);
    
    /**
     * 获取用户收到的点赞数量
     */
    int countUserLikes(Long userId);
    
    /**
     * 获取用户发表的评论数量
     */
    int countUserComments(Long userId);
} 