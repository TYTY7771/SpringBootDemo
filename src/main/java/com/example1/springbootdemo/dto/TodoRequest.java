package com.example1.springbootdemo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;

/**
 * Todo创建请求DTO
 * 用于接收创建Todo任务的请求参数
 */
public class TodoRequest {
    
    /**
     * 任务内容
     */
    @NotBlank(message = "任务内容不能为空")
    @Size(max = 500, message = "任务内容长度不能超过500个字符")
    private String content;
    
    /**
     * 任务描述（可选）
     */
    @Size(max = 1000, message = "任务描述长度不能超过1000个字符")
    private String description;
    
    /**
     * 任务优先级（可选）
     * 1-低优先级，2-中优先级，3-高优先级
     */
    @Min(value = 1, message = "优先级必须在1-3之间")
    @Max(value = 3, message = "优先级必须在1-3之间")
    private Integer priority;
    
    /**
     * 无参构造函数
     */
    public TodoRequest() {
    }
    
    /**
     * 带参构造函数
     * @param content 任务内容
     */
    public TodoRequest(String content) {
        this.content = content;
    }
    
    /**
     * 带参构造函数
     * @param content 任务内容
     * @param description 任务描述
     */
    public TodoRequest(String content, String description) {
        this.content = content;
        this.description = description;
    }
    
    /**
     * 完整构造函数
     * @param content 任务内容
     * @param description 任务描述
     * @param priority 优先级
     */
    public TodoRequest(String content, String description, Integer priority) {
        this.content = content;
        this.description = description;
        this.priority = priority;
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
    
    @Override
    public String toString() {
        return "TodoRequest{" +
                "content='" + content + '\'' +
                ", description='" + description + '\'' +
                ", priority=" + priority +
                '}';
    }
}