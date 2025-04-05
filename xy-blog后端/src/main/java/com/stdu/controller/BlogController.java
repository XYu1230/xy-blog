package com.stdu.controller;

import com.stdu.context.BaseContext;
import com.stdu.dto.BlogListResponse;
import com.stdu.dto.BlogRequest;
import com.stdu.dto.BlogResponse;
import com.stdu.result.Result;
import com.stdu.service.BlogService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 博客控制器
 */
@RestController
@RequestMapping("/api/blogs")
@CrossOrigin(origins = "*")
public class BlogController {
    
    @Autowired
    private BlogService blogService;
    
    /**
     * 获取博客列表
     */
    @GetMapping
    public Result<BlogListResponse> getBlogList(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String tag,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize,
            HttpServletRequest request) {
        Long userId = BaseContext.getCurrentId();
        BlogListResponse response = blogService.getBlogList(keyword, tag, page, pageSize, userId);
        return Result.success(response);
    }
    
    /**
     * 获取博客详情
     */
    @GetMapping("/{id}")
    public Result<BlogResponse> getBlogDetail(@PathVariable Integer id, HttpServletRequest request) {
        Long userId = BaseContext.getCurrentId();
        BlogResponse response = blogService.getBlogDetail(id, userId);
        return Result.success(response);
    }
    
    /**
     * 发布博客
     */
    @PostMapping
    public Result<BlogResponse> createBlog(@RequestBody @Valid BlogRequest request, HttpServletRequest servletRequest) {
        Long userId = BaseContext.getCurrentId();
        BlogResponse response = blogService.createBlog(userId, request);
        return Result.success(response);
    }
    
    /**
     * 更新博客
     */
    @PutMapping("/{id}")
    public Result<BlogResponse> updateBlog(
            @PathVariable Integer id,
            @RequestBody @Valid BlogRequest request,
            HttpServletRequest servletRequest) {
        Long userId = BaseContext.getCurrentId();
        BlogResponse response = blogService.updateBlog(id, userId, request);
        return Result.success(response);
    }
    
    /**
     * 删除博客
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteBlog(@PathVariable Integer id, HttpServletRequest request) {
        Long userId = BaseContext.getCurrentId();
        blogService.deleteBlog(id, userId);
        return Result.success(null);
    }
    
    /**
     * 点赞博客
     */
    @PostMapping("/{id}/like")
    public Result<Object> likeBlog(@PathVariable Integer id, HttpServletRequest request) {
        Long userId = BaseContext.getCurrentId();
        final Integer likeCountValue = blogService.likeBlog(id, userId);
        return Result.success(new Object() {
            public final Integer likeCount = likeCountValue;
        });
    }
    
    /**
     * 取消点赞
     */
    @DeleteMapping("/{id}/like")
    public Result<Object> unlikeBlog(@PathVariable Integer id, HttpServletRequest request) {
        Long userId = BaseContext.getCurrentId();
        final Integer likeCountValue = blogService.unlikeBlog(id, userId);
        return Result.success(new Object() {
            public final Integer likeCount = likeCountValue;
        });
    }
} 