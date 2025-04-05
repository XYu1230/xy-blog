package com.stdu.controller;

import com.fasterxml.jackson.databind.ser.Serializers;
import com.stdu.context.BaseContext;
import com.stdu.dto.CommentListResponse;
import com.stdu.dto.CommentRequest;
import com.stdu.dto.CommentResponse;
import com.stdu.result.Result;
import com.stdu.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 评论控制器
 */
@RestController
public class CommentController {
    
    @Autowired
    private CommentService commentService;
    
    /**
     * 获取博客评论列表
     */
    @GetMapping("/api/blogs/{blogId}/comments")
    public Result<CommentListResponse> getCommentList(
            @PathVariable Integer blogId,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        CommentListResponse response = commentService.getCommentList(blogId, page, pageSize);
        return Result.success(response);
    }
    
    /**
     * 发表评论
     */
    @PostMapping("/api/blogs/{blogId}/comments")
    public Result<CommentResponse> createComment(
            @PathVariable Integer blogId,
            @RequestBody @Valid CommentRequest request,
            HttpServletRequest servletRequest) {
        Long userId = BaseContext.getCurrentId();
        CommentResponse response = commentService.createComment(blogId, userId, request);
        return Result.success(response);
    }
    
    /**
     * 删除评论
     */
    @DeleteMapping("/api/comments/{id}")
    public Result deleteComment(@PathVariable Integer id, HttpServletRequest request) {
        Long userId = BaseContext.getCurrentId();
        commentService.deleteComment(id, userId);
        return Result.success( );
    }
} 