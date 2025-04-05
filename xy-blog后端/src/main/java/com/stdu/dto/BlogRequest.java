package com.stdu.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

/**
 * 博客请求DTO
 */
@Data
public class BlogRequest {
    
    @NotBlank(message = "标题不能为空")
    @Size(max = 100, message = "标题长度不能超过100")
    private String title;
    
    @NotBlank(message = "内容不能为空")
    private String content;
    
    @Size(max = 255, message = "摘要长度不能超过255")
    private String summary;
    
    @Size(max = 3, message = "标签数量不能超过3个")
    private List<String> tags;
} 