package com.stdu.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 用户信息更新请求DTO
 */
@Data
public class UserUpdateRequest {
    
    @Size(max = 50, message = "昵称长度不能超过50")
    private String nickname;
    
    private String avatar;
} 