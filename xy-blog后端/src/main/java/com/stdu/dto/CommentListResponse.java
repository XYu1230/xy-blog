package com.stdu.dto;

import lombok.Data;

import java.util.List;

/**
 * 评论列表响应DTO
 */
@Data
public class CommentListResponse {
    private Integer total;
    private Integer page;
    private Integer pageSize;
    private List<CommentResponse> data;
} 