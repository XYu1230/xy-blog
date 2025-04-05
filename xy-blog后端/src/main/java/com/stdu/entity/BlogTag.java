package com.stdu.entity;

import lombok.Data;

/**
 * 博客-标签关联实体类
 */
@Data
public class BlogTag {
    private Integer id;
    private Integer blogId;
    private Integer tagId;
} 