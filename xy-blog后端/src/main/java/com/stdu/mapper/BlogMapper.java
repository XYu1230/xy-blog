package com.stdu.mapper;

import com.stdu.entity.Blog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 博客Mapper接口
 */
@Mapper
public interface BlogMapper {
    
    /**
     * 根据ID查询博客
     */
    Blog findById(Integer id);
    
    /**
     * 查询博客列表
     */
    List<Blog> findList(@Param("keyword") String keyword, 
                        @Param("tagId") Integer tagId, 
                        @Param("offset") Integer offset, 
                        @Param("limit") Integer limit);
    
    /**
     * 查询博客总数
     */
    int count(@Param("keyword") String keyword, @Param("tagId") Integer tagId);
    
    /**
     * 插入博客
     */
    int insert(Blog blog);
    
    /**
     * 更新博客
     */
    int update(Blog blog);
    
    /**
     * 删除博客
     */
    int deleteById(Integer id);
    
    /**
     * 更新博客的评论数
     */
    int updateCommentCount(@Param("blogId") Integer blogId, @Param("count") Integer count);
    
    /**
     * 更新博客的点赞数
     */
    int updateLikeCount(@Param("blogId") Integer blogId, @Param("count") Integer count);
} 