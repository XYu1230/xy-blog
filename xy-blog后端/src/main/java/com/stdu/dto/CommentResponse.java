package com.stdu.dto;

import lombok.Data;

import java.util.Date;

/**
 * 评论响应DTO
 */
@Data
public class CommentResponse {
    private Integer id;
    private String content;
    private Date createTime;
    private UserInfoResponse user;
} 