package com.stdu.service.impl;

import com.stdu.constant.JwtClaimsConstant;
import com.stdu.dto.*;
import com.stdu.entity.User;
import com.stdu.exception.BusinessException;
import com.stdu.mapper.UserMapper;
import com.stdu.properties.JwtProperties;
import com.stdu.service.UserService;
import com.stdu.util.JwtUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

/**
 * 用户服务实现类
 */
@Service
public class UserServiceImpl implements UserService {
    
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtProperties jwtProperties;
    
    @Override
    public void register(UserRegisterRequest request) {
        // 检查用户名是否已存在
        User existUser = userMapper.findByUsername(request.getUsername());
        if (existUser != null) {
            throw new BusinessException("用户名已存在");
        }
        
        // 创建新用户
        User user = new User();
        user.setUsername(request.getUsername());
        // 密码加密存储
        user.setPassword(DigestUtils.md5DigestAsHex(request.getPassword().getBytes(StandardCharsets.UTF_8)));
        user.setNickname(request.getNickname());
        user.setAvatar("/static/default-avatar.png");
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        
        userMapper.insert(user);
    }
    
    @Override
    public UserLoginResponse login(UserLoginRequest request) {
        // 查询用户
        User user = userMapper.findByUsername(request.getUsername());
        if (user == null) {
            throw new BusinessException("用户名或密码错误");
        }
        
        // 验证密码
        String encryptedPassword = DigestUtils.md5DigestAsHex(request.getPassword().getBytes(StandardCharsets.UTF_8));
        if (!user.getPassword().equals(encryptedPassword)) {
            throw new BusinessException("用户名或密码错误");
        }
        
        // 生成token
        Map<String,Object> claims = Map.of(JwtClaimsConstant.USER_ID, String.valueOf(user.getId()));
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);
        
        // 构建响应
        UserLoginResponse response = new UserLoginResponse();
        response.setToken(token);
        
        UserInfoResponse userInfo = new UserInfoResponse();
        BeanUtils.copyProperties(user, userInfo);
        response.setUserInfo(userInfo);
        
        return response;
    }
    
    @Override
    public UserInfoResponse getUserInfo(Long userId) {
        User user = userMapper.findById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        
        UserInfoResponse response = new UserInfoResponse();
        BeanUtils.copyProperties(user, response);
        return response;
    }
    
    @Override
    public UserStatsResponse getUserStats(Long userId) {
        User user = userMapper.findById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        
        UserStatsResponse stats = new UserStatsResponse();
        stats.setBlogCount(userMapper.countUserBlogs(userId));
        stats.setLikeCount(userMapper.countUserLikes(userId));
        stats.setCommentCount(userMapper.countUserComments(userId));
        
        return stats;
    }
    
    @Override
    public UserInfoResponse updateUserInfo(Long userId, UserUpdateRequest request) {
        User user = userMapper.findById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        
        if (request.getNickname() != null) {
            user.setNickname(request.getNickname());
        }
        
        if (request.getAvatar() != null) {
            user.setAvatar(request.getAvatar());
        }
        
        user.setUpdateTime(new Date());
        userMapper.update(user);
        
        UserInfoResponse response = new UserInfoResponse();
        BeanUtils.copyProperties(user, response);
        return response;
    }
    
    @Override
    public User getUserById(Long userId) {
        return userMapper.findById(userId);
    }
} 