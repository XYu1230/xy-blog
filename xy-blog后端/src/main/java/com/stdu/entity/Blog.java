package com.stdu.entity;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 博客实体类
 */
@Data
public class Blog {
    private Integer id;
    private Long userId;
    private String title;
    private String content;
    private String summary;
    private Integer likeCount;
    private Integer commentCount;
    private Date createTime;
    private Date updateTime;
    
    // 非数据库字段
    private User author;
    private List<Tag> tags;
    private Boolean isLiked;
} 