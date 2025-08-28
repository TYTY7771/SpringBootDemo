package com.example1.springbootdemo.entity;

import java.time.LocalDateTime;


//Todo待办事项实体类
//包含ID、任务内容、是否完成等字段

public class Todo {
    
    
    //主键ID
    
    private Long id;
    
    
    //任务内容
    
    private String content;
    
    
    //是否完成
    
    private Boolean completed;
    
    
    //创建时间
    
    private LocalDateTime createTime;
    
    
    //更新时间
    
    private LocalDateTime updateTime;
    
    
    //任务优先级（可选字段）
    //1-低优先级，2-中优先级，3-高优先级
    
    private Integer priority;
    
    
    //任务描述（可选字段）
    
    private String description;
    
    // 无参构造函数
    public Todo() {
        this.completed = false;
        this.createTime = LocalDateTime.now();
        this.updateTime = LocalDateTime.now();
        this.priority = 1; // 默认低优先级
    }
    
    // 带参构造函数
    public Todo(String content) {
        this();
        this.content = content;
    }
    
    public Todo(String content, String description) {
        this(content);
        this.description = description;
    }
    
    // Getter和Setter方法
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
        this.updateTime = LocalDateTime.now();
    }
    
    public Boolean getCompleted() {
        return completed;
    }
    
    public void setCompleted(Boolean completed) {
        this.completed = completed;
        this.updateTime = LocalDateTime.now();
    }
    
    public LocalDateTime getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
    
    public LocalDateTime getUpdateTime() {
        return updateTime;
    }
    
    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
    
    public Integer getPriority() {
        return priority;
    }
    
    public void setPriority(Integer priority) {
        this.priority = priority;
        this.updateTime = LocalDateTime.now();
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
        this.updateTime = LocalDateTime.now();
    }
    
    // 便捷方法
    
    //标记任务为已完成
    
    public void markAsCompleted() {
        this.completed = true;
        this.updateTime = LocalDateTime.now();
    }
    
    
    //标记任务为未完成
    
    public void markAsIncomplete() {
        this.completed = false;
        this.updateTime = LocalDateTime.now();
    }
    
    
    //检查任务是否已完成
    
    public boolean isCompleted() {
        return this.completed != null && this.completed;
    }
    
    @Override
    public String toString() {
        return "Todo{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", completed=" + completed +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", priority=" + priority +
                ", description='" + description + '\'' +
                '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        Todo todo = (Todo) o;
        
        return id != null ? id.equals(todo.id) : todo.id == null;
    }
    
    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}