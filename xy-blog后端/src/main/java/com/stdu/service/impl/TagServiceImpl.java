package com.stdu.service.impl;

import com.stdu.entity.Tag;
import com.stdu.mapper.TagMapper;
import com.stdu.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 标签服务实现类
 */
@Service
public class TagServiceImpl implements TagService {
    
    @Autowired
    private TagMapper tagMapper;
    
    @Override
    public List<Tag> getAllTags() {
        return tagMapper.findAll();
    }
    
    @Override
    public Tag getOrCreateTag(String name) {
        Tag tag = tagMapper.findByName(name);
        if (tag == null) {
            tag = new Tag();
            tag.setName(name);
            tag.setCreateTime(new Date());
            tagMapper.insert(tag);
        }
        return tag;
    }
    
    @Override
    public List<Tag> getTagsByBlogId(Integer blogId) {
        return tagMapper.findByBlogId(blogId);
    }
} 