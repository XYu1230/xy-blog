package com.stdu.mapper;

import com.stdu.entity.Like;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 点赞Mapper接口
 */
@Mapper
public interface LikeMapper {
    
    /**
     * 根据博客ID和用户ID查询点赞
     */
    Like findByBlogIdAndUserId(@Param("blogId") Integer blogId, @Param("userId") Long userId);
    
    /**
     * 根据博客ID查询点赞列表
     */
    List<Like> findByBlogId(Integer blogId);
    
    /**
     * 查询博客点赞总数
     */
    int countByBlogId(Integer blogId);
    
    /**
     * 插入点赞
     */
    int insert(Like like);
    
    /**
     * 删除点赞
     */
    int deleteByBlogIdAndUserId(@Param("blogId") Integer blogId, @Param("userId") Long userId);
    
    /**
     * 根据博客ID删除所有点赞
     */
    int deleteByBlogId(Integer blogId);
} 