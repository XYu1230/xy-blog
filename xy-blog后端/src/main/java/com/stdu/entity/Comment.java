package com.stdu.entity;

import lombok.Data;

import java.util.Date;

/**
 * 评论实体类
 */
@Data
public class Comment {
    private Integer id;
    private Integer blogId;
    private Long userId;
    private String content;
    private Date createTime;
    
    // 非数据库字段
    private User user;
} 