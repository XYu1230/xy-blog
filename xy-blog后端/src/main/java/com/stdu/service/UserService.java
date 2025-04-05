package com.stdu.service;

import com.stdu.dto.*;
import com.stdu.entity.User;

/**
 * 用户服务接口
 */
public interface UserService {
    
    /**
     * 用户注册
     */
    void register(UserRegisterRequest request);
    
    /**
     * 用户登录
     */
    UserLoginResponse login(UserLoginRequest request);
    
    /**
     * 获取用户信息
     */
    UserInfoResponse getUserInfo(Long userId);
    
    /**
     * 获取用户统计信息
     */
    UserStatsResponse getUserStats(Long userId);
    
    /**
     * 更新用户信息
     */
    UserInfoResponse updateUserInfo(Long userId, UserUpdateRequest request);
    
    /**
     * 根据ID获取用户
     */
    User getUserById(Long userId);
} 