package com.stdu.entity;

import lombok.Data;

import java.util.Date;

/**
 * 点赞实体类
 */
@Data
public class Like {
    private Integer id;
    private Integer blogId;
    private Long userId;
    private Date createTime;
} 