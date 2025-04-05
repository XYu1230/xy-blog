package com.stdu.dto;

import lombok.Data;

import java.util.Date;

/**
 * 用户信息响应DTO
 */
@Data
public class UserInfoResponse {
    private Integer id;
    private String username;
    private String nickname;
    private String avatar;
    private Date createTime;
} 