package com.stdu.service.impl;

import com.stdu.dto.BlogListResponse;
import com.stdu.dto.BlogRequest;
import com.stdu.dto.BlogResponse;
import com.stdu.dto.UserInfoResponse;
import com.stdu.entity.Blog;
import com.stdu.entity.BlogTag;
import com.stdu.entity.Like;
import com.stdu.entity.Tag;
import com.stdu.entity.User;
import com.stdu.exception.BusinessException;
import com.stdu.mapper.BlogMapper;
import com.stdu.mapper.BlogTagMapper;
import com.stdu.mapper.CommentMapper;
import com.stdu.mapper.LikeMapper;
import com.stdu.mapper.TagMapper;
import com.stdu.service.BlogService;
import com.stdu.service.TagService;
import com.stdu.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 博客服务实现类
 */
@Service
public class BlogServiceImpl implements BlogService {
    
    @Autowired
    private BlogMapper blogMapper;
    
    @Autowired
    private TagService tagService;
    
    @Autowired
    private TagMapper tagMapper;
    
    @Autowired
    private BlogTagMapper blogTagMapper;
    
    @Autowired
    private LikeMapper likeMapper;
    
    @Autowired
    private CommentMapper commentMapper;
    
    @Autowired
    private UserService userService;
    
    @Override
    public BlogListResponse getBlogList(String keyword, String tag, Integer page, Integer pageSize, Long userId) {
        // 计算分页参数
        Integer offset = (page - 1) * pageSize;
        Integer limit = pageSize;
        
        // 处理标签查询
        Integer tagId = null;
        if (StringUtils.hasText(tag)) {
            Tag tagEntity = tagMapper.findByName(tag);
            if (tagEntity != null) {
                tagId = tagEntity.getId();
            }
        }
        
        // 查询博客列表
        List<Blog> blogs = blogMapper.findList(keyword, tagId, offset, limit);
        int total = blogMapper.count(keyword, tagId);
        
        // 转换为响应对象
        List<BlogResponse> blogResponses = blogs.stream().map(blog -> {
            BlogResponse response = new BlogResponse();
            BeanUtils.copyProperties(blog, response);
            
            // 设置作者信息
            if (blog.getAuthor() != null) {
                UserInfoResponse author = new UserInfoResponse();
                BeanUtils.copyProperties(blog.getAuthor(), author);
                response.setAuthor(author);
            }
            
            // 设置标签
            List<Tag> tags = tagService.getTagsByBlogId(blog.getId());
            response.setTags(tags.stream().map(Tag::getName).collect(Collectors.toList()));
            
            // 设置点赞数和评论数
            response.setLikeCount(likeMapper.countByBlogId(blog.getId()));
            response.setCommentCount(commentMapper.countByBlogId(blog.getId()));
            
            // 设置当前用户是否点赞
            if (userId != null) {
                Like like = likeMapper.findByBlogIdAndUserId(blog.getId(), userId);
                response.setIsLiked(like != null);
            } else {
                response.setIsLiked(false);
            }
            
            return response;
        }).collect(Collectors.toList());
        
        // 构建响应
        BlogListResponse response = new BlogListResponse();
        response.setTotal(total);
        response.setPage(page);
        response.setPageSize(pageSize);
        response.setData(blogResponses);
        
        return response;
    }
    
    @Override
    public BlogResponse getBlogDetail(Integer blogId, Long userId) {
        Blog blog = blogMapper.findById(blogId);
        if (blog == null) {
            throw new BusinessException("博客不存在");
        }
        
        // 转换为响应对象
        BlogResponse response = new BlogResponse();
        BeanUtils.copyProperties(blog, response);
        
        // 设置作者信息
        if (blog.getAuthor() != null) {
            UserInfoResponse author = new UserInfoResponse();
            BeanUtils.copyProperties(blog.getAuthor(), author);
            response.setAuthor(author);
        }
        
        // 设置标签
        List<Tag> tags = tagService.getTagsByBlogId(blog.getId());
        response.setTags(tags.stream().map(Tag::getName).collect(Collectors.toList()));
        
        // 设置点赞数和评论数
        response.setLikeCount(likeMapper.countByBlogId(blog.getId()));
        response.setCommentCount(commentMapper.countByBlogId(blog.getId()));
        
        // 设置当前用户是否点赞
        if (userId != null) {
            Like like = likeMapper.findByBlogIdAndUserId(blog.getId(), userId);
            response.setIsLiked(like != null);
        } else {
            response.setIsLiked(false);
        }
        
        return response;
    }
    
    @Override
    @Transactional
    public BlogResponse createBlog(Long userId, BlogRequest request) {
        // 验证用户
        User user = userService.getUserById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        
        // 创建博客
        Blog blog = new Blog();
        blog.setUserId(userId);
        blog.setTitle(request.getTitle());
        blog.setContent(request.getContent());
        
        // 处理摘要
        if (StringUtils.hasText(request.getSummary())) {
            blog.setSummary(request.getSummary());
        } else {
            // 自动生成摘要
            String content = request.getContent();
            if (content.length() > 200) {
                blog.setSummary(content.substring(0, 200) + "...");
            } else {
                blog.setSummary(content);
            }
        }
        
        blog.setCreateTime(new Date());
        blog.setUpdateTime(new Date());
        
        blogMapper.insert(blog);
        
        // 处理标签
        if (request.getTags() != null && !request.getTags().isEmpty()) {
            List<BlogTag> blogTags = new ArrayList<>();
            for (String tagName : request.getTags()) {
                Tag tag = tagService.getOrCreateTag(tagName);
                BlogTag blogTag = new BlogTag();
                blogTag.setBlogId(blog.getId());
                blogTag.setTagId(tag.getId());
                blogTags.add(blogTag);
            }
            if (!blogTags.isEmpty()) {
                blogTagMapper.batchInsert(blogTags);
            }
        }
        
        // 返回博客详情
        return getBlogDetail(blog.getId(), userId);
    }
    
    @Override
    @Transactional
    public BlogResponse updateBlog(Integer blogId, Long userId, BlogRequest request) {
        // 验证博客
        Blog blog = blogMapper.findById(blogId);
        if (blog == null) {
            throw new BusinessException("博客不存在");
        }
        
        // 验证权限
        if (!blog.getUserId().equals(userId)) {
            throw new BusinessException("无权限修改此博客");
        }
        
        // 更新博客
        if (StringUtils.hasText(request.getTitle())) {
            blog.setTitle(request.getTitle());
        }
        
        if (StringUtils.hasText(request.getContent())) {
            blog.setContent(request.getContent());
        }
        
        // 处理摘要
        if (StringUtils.hasText(request.getSummary())) {
            blog.setSummary(request.getSummary());
        } else if (StringUtils.hasText(request.getContent())) {
            // 自动更新摘要
            String content = request.getContent();
            if (content.length() > 200) {
                blog.setSummary(content.substring(0, 200) + "...");
            } else {
                blog.setSummary(content);
            }
        }
        
        blog.setUpdateTime(new Date());
        
        blogMapper.update(blog);
        
        // 处理标签
        if (request.getTags() != null) {
            // 删除原有标签关联
            blogTagMapper.deleteByBlogId(blogId);
            
            // 添加新标签关联
            if (!request.getTags().isEmpty()) {
                List<BlogTag> blogTags = new ArrayList<>();
                for (String tagName : request.getTags()) {
                    Tag tag = tagService.getOrCreateTag(tagName);
                    BlogTag blogTag = new BlogTag();
                    blogTag.setBlogId(blog.getId());
                    blogTag.setTagId(tag.getId());
                    blogTags.add(blogTag);
                }
                if (!blogTags.isEmpty()) {
                    blogTagMapper.batchInsert(blogTags);
                }
            }
        }
        
        // 返回博客详情
        return getBlogDetail(blog.getId(), userId);
    }
    
    @Override
    @Transactional
    public void deleteBlog(Integer blogId, Long userId) {
        // 验证博客
        Blog blog = blogMapper.findById(blogId);
        if (blog == null) {
            throw new BusinessException("博客不存在");
        }
        
        // 验证权限
        if (!blog.getUserId().equals(userId)) {
            throw new BusinessException("无权限删除此博客");
        }
        
        // 删除博客（级联删除会自动删除关联的标签、评论和点赞）
        blogMapper.deleteById(blogId);
    }
    
    @Override
    @Transactional
    public Integer likeBlog(Integer blogId, Long userId) {
        // 验证博客
        Blog blog = blogMapper.findById(blogId);
        if (blog == null) {
            throw new BusinessException("博客不存在");
        }
        
        // 检查是否已点赞
        Like existLike = likeMapper.findByBlogIdAndUserId(blogId, userId);
        if (existLike != null) {
            throw new BusinessException("已经点赞过此博客");
        }
        
        // 添加点赞
        Like like = new Like();
        like.setBlogId(blogId);
        like.setUserId(userId);
        like.setCreateTime(new Date());
        likeMapper.insert(like);
        
        // 更新博客点赞数
        int likeCount = likeMapper.countByBlogId(blogId);
        blogMapper.updateLikeCount(blogId, likeCount);
        
        return likeCount;
    }
    
    @Override
    @Transactional
    public Integer unlikeBlog(Integer blogId, Long userId) {
        // 验证博客
        Blog blog = blogMapper.findById(blogId);
        if (blog == null) {
            throw new BusinessException("博客不存在");
        }
        
        // 检查是否已点赞
        Like existLike = likeMapper.findByBlogIdAndUserId(blogId, userId);
        if (existLike == null) {
            throw new BusinessException("尚未点赞此博客");
        }
        
        // 删除点赞
        likeMapper.deleteByBlogIdAndUserId(blogId, userId);
        
        // 更新博客点赞数
        int likeCount = likeMapper.countByBlogId(blogId);
        blogMapper.updateLikeCount(blogId, likeCount);
        
        return likeCount;
    }
} 