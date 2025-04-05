package com.stdu.service;

import com.stdu.dto.CommentListResponse;
import com.stdu.dto.CommentRequest;
import com.stdu.dto.CommentResponse;

/**
 * 评论服务接口
 */
public interface CommentService {
    
    /**
     * 获取博客评论列表
     */
    CommentListResponse getCommentList(Integer blogId, Integer page, Integer pageSize);
    
    /**
     * 发表评论
     */
    CommentResponse createComment(Integer blogId, Long userId, CommentRequest request);
    
    /**
     * 删除评论
     */
    void deleteComment(Integer commentId, Long userId);
} 