package com.stdu.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 博客响应DTO
 */
@Data
public class BlogResponse {
    private Integer id;
    private String title;
    private String content;
    private String summary;
    private List<String> tags;
    private Date createTime;
    private Date updateTime;
    private UserInfoResponse author;
    private Integer likeCount;
    private Integer commentCount;
    private Boolean isLiked;
} 