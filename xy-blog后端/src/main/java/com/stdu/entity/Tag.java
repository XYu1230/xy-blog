package com.stdu.entity;

import lombok.Data;

import java.util.Date;

/**
 * 标签实体类
 */
@Data
public class Tag {
    private Integer id;
    private String name;
    private Date createTime;
} 