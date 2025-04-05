package com.stdu.mapper;

import com.stdu.entity.Tag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 标签Mapper接口
 */
@Mapper
public interface TagMapper {
    
    /**
     * 根据ID查询标签
     */
    Tag findById(Integer id);
    
    /**
     * 根据名称查询标签
     */
    Tag findByName(String name);
    
    /**
     * 查询所有标签
     */
    List<Tag> findAll();
    
    /**
     * 根据博客ID查询标签
     */
    List<Tag> findByBlogId(Integer blogId);
    
    /**
     * 插入标签
     */
    int insert(Tag tag);
} 