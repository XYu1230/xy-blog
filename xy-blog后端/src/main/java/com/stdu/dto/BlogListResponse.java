package com.stdu.dto;

import lombok.Data;

import java.util.List;

/**
 * 博客列表响应DTO
 */
@Data
public class BlogListResponse {
    private Integer total;
    private Integer page;
    private Integer pageSize;
    private List<BlogResponse> data;
} 