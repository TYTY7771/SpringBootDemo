package com.example1.springbootdemo.dto;

import jakarta.validation.constraints.*;

/**
 * Todo更新请求DTO类
 * 用于接收和验证更新Todo的请求参数
 * 所有字段都是可选的，只更新提供的字段
 */
public class TodoUpdateRequest {
    
    @Size(min = 1, max = 500, message = "任务内容不能为空且不能超过500个字符")
    private String content;
    
    @Size(max = 1000, message = "任务描述不能超过1000个字符")
    private String description;
    
    @Min(value = 1, message = "优先级必须在1-3之间")
    @Max(value = 3, message = "优先级必须在1-3之间")
    private Integer priority;
    
    private Boolean completed;
    
    // 无参构造函数
    public TodoUpdateRequest() {}
    
    // 全参构造函数
    public TodoUpdateRequest(String content, String description, Integer priority, Boolean completed) {
        this.content = content;
        this.description = description;
        this.priority = priority;
        this.completed = completed;
    }
    
    // Getter和Setter方法
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Integer getPriority() {
        return priority;
    }
    
    public void setPriority(Integer priority) {
        this.priority = priority;
    }
    
    public Boolean getCompleted() {
        return completed;
    }
    
    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }
    
    // 检查是否有任何字段被设置
    public boolean hasAnyField() {
        return content != null || description != null || priority != null || completed != null;
    }
    
    @Override
    public String toString() {
        return "TodoUpdateRequest{" +
                "content='" + content + '\'' +
                ", description='" + description + '\'' +
                ", priority=" + priority +
                ", completed=" + completed +
                '}';
    }
}