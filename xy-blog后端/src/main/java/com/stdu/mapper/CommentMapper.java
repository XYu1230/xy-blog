package com.stdu.mapper;

import com.stdu.entity.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 评论Mapper接口
 */
@Mapper
public interface CommentMapper {
    
    /**
     * 根据ID查询评论
     */
    Comment findById(Integer id);
    
    /**
     * 根据博客ID查询评论列表
     */
    List<Comment> findByBlogId(@Param("blogId") Integer blogId, 
                              @Param("offset") Integer offset, 
                              @Param("limit") Integer limit);
    
    /**
     * 查询博客评论总数
     */
    int countByBlogId(Integer blogId);
    
    /**
     * 插入评论
     */
    int insert(Comment comment);
    
    /**
     * 删除评论
     */
    int deleteById(Integer id);
    
    /**
     * 根据博客ID删除所有评论
     */
    int deleteByBlogId(Integer blogId);
} 