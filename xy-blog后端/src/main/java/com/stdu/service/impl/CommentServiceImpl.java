package com.stdu.service.impl;

import com.stdu.dto.CommentListResponse;
import com.stdu.dto.CommentRequest;
import com.stdu.dto.CommentResponse;
import com.stdu.dto.UserInfoResponse;
import com.stdu.entity.Blog;
import com.stdu.entity.Comment;
import com.stdu.entity.User;
import com.stdu.exception.BusinessException;
import com.stdu.mapper.BlogMapper;
import com.stdu.mapper.CommentMapper;
import com.stdu.service.CommentService;
import com.stdu.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 评论服务实现类
 */
@Service
public class CommentServiceImpl implements CommentService {
    
    @Autowired
    private CommentMapper commentMapper;
    
    @Autowired
    private BlogMapper blogMapper;
    
    @Autowired
    private UserService userService;
    
    @Override
    public CommentListResponse getCommentList(Integer blogId, Integer page, Integer pageSize) {
        // 验证博客
        Blog blog = blogMapper.findById(blogId);
        if (blog == null) {
            throw new BusinessException("博客不存在");
        }
        
        // 计算分页参数
        Integer offset = (page - 1) * pageSize;
        Integer limit = pageSize;
        
        // 查询评论列表
        List<Comment> comments = commentMapper.findByBlogId(blogId, offset, limit);
        int total = commentMapper.countByBlogId(blogId);
        
        // 转换为响应对象
        List<CommentResponse> commentResponses = comments.stream().map(comment -> {
            CommentResponse response = new CommentResponse();
            BeanUtils.copyProperties(comment, response);
            
            // 设置用户信息
            if (comment.getUser() != null) {
                UserInfoResponse user = new UserInfoResponse();
                BeanUtils.copyProperties(comment.getUser(), user);
                response.setUser(user);
            }
            
            return response;
        }).collect(Collectors.toList());
        
        // 构建响应
        CommentListResponse response = new CommentListResponse();
        response.setTotal(total);
        response.setPage(page);
        response.setPageSize(pageSize);
        response.setData(commentResponses);
        
        return response;
    }
    
    @Override
    @Transactional
    public CommentResponse createComment(Integer blogId, Long userId, CommentRequest request) {
        // 验证博客
        Blog blog = blogMapper.findById(blogId);
        if (blog == null) {
            throw new BusinessException("博客不存在");
        }
        
        // 验证用户
        User user = userService.getUserById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        
        // 创建评论
        Comment comment = new Comment();
        comment.setBlogId(blogId);
        comment.setUserId(userId);
        comment.setContent(request.getContent());
        comment.setCreateTime(new Date());
        
        commentMapper.insert(comment);
        
        // 更新博客评论数
        int commentCount = commentMapper.countByBlogId(blogId);
        blogMapper.updateCommentCount(blogId, commentCount);
        
        // 查询完整评论信息
        Comment fullComment = commentMapper.findById(comment.getId());
        
        // 转换为响应对象
        CommentResponse response = new CommentResponse();
        BeanUtils.copyProperties(fullComment, response);
        
        // 设置用户信息
        if (fullComment.getUser() != null) {
            UserInfoResponse userInfo = new UserInfoResponse();
            BeanUtils.copyProperties(fullComment.getUser(), userInfo);
            response.setUser(userInfo);
        }
        
        return response;
    }

    @Override
    @Transactional
    public void deleteComment(Integer commentId, Long userId) {
        // 验证评论
        Comment comment = commentMapper.findById(commentId);
        if (comment == null) {
            throw new BusinessException("评论不存在");
        }
        
        // 验证权限（评论作者或博客作者可删除评论）
        Blog blog = blogMapper.findById(comment.getBlogId());
        if (!comment.getUserId().equals(userId) && !blog.getUserId().equals(userId)) {
            throw new BusinessException("无权限删除此评论");
        }
        
        // 删除评论
        commentMapper.deleteById(commentId);
        
        // 更新博客评论数
        int commentCount = commentMapper.countByBlogId(comment.getBlogId());
        blogMapper.updateCommentCount(comment.getBlogId(), commentCount);
    }
} 