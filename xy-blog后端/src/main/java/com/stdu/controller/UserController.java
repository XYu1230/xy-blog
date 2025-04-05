package com.stdu.controller;

import com.fasterxml.jackson.databind.ser.Serializers;
import com.stdu.context.BaseContext;
import com.stdu.dto.*;
import com.stdu.result.Result;
import com.stdu.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 用户控制器
 */
@RestController
@RequestMapping("api/user")
@CrossOrigin(origins = "*")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    /**
     * 用户注册
     */
    @PostMapping("/register")
    public Result register(@RequestBody @Valid UserRegisterRequest request) {
        userService.register(request);
        return Result.success();
    }
    
    /**
     * 用户登录
     */
    @PostMapping("/login")
    public Result<UserLoginResponse> login(@RequestBody @Valid UserLoginRequest request) {
        UserLoginResponse response = userService.login(request);
        return Result.success(response);
    }
    
    /**
     * 获取用户信息
     */
    @GetMapping("/info")
    public Result<Object> getUserInfo(HttpServletRequest request) {
        Long userId = BaseContext.getCurrentId();
        final UserInfoResponse userInfoData = userService.getUserInfo(userId);
        final UserStatsResponse statsData = userService.getUserStats(userId);
        
        return Result.success(new Object() {
            public final UserInfoResponse userInfo = userInfoData;
            public final UserStatsResponse stats = statsData;
        });
    }
    
    /**
     * 更新用户信息
     */
    @PutMapping("/info")
    public Result<Object> updateUserInfo(HttpServletRequest request, @RequestBody @Valid UserUpdateRequest updateRequest) {
        Long userId = BaseContext.getCurrentId();
        final UserInfoResponse userInfoData = userService.updateUserInfo(userId, updateRequest);

        return Result.success(new Object() {
            public final UserInfoResponse userInfo = userInfoData;
        });
    }
} 