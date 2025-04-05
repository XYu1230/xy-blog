package com.stdu.mapper;

import com.stdu.entity.BlogTag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 博客-标签关联Mapper接口
 */
@Mapper
public interface BlogTagMapper {
    
    /**
     * 根据博客ID查询关联
     */
    List<BlogTag> findByBlogId(Integer blogId);
    
    /**
     * 根据标签ID查询关联
     */
    List<BlogTag> findByTagId(Integer tagId);
    
    /**
     * 插入关联
     */
    int insert(BlogTag blogTag);
    
    /**
     * 批量插入关联
     */
    int batchInsert(@Param("list") List<BlogTag> list);
    
    /**
     * 删除博客的所有标签关联
     */
    int deleteByBlogId(Integer blogId);
} 