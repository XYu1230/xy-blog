package com.stdu.dto;

import lombok.Data;

/**
 * 用户统计信息响应DTO
 */
@Data
public class UserStatsResponse {
    private Integer blogCount;
    private Integer likeCount;
    private Integer commentCount;
} 