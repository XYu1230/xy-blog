package com.stdu.dto;

import lombok.Data;

/**
 * 用户登录响应DTO
 */
@Data
public class UserLoginResponse {
    private String token;
    private UserInfoResponse userInfo;
} 