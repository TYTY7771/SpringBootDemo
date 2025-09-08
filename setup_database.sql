-- MySQL数据库和表创建脚本
-- 用于Spring Boot Todo应用

-- 创建数据库
CREATE DATABASE IF NOT EXISTS todo_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 使用数据库
USE todo_db;

-- 创建todo表
CREATE TABLE IF NOT EXISTS todo (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    content VARCHAR(500) NOT NULL COMMENT '任务内容',
    description TEXT COMMENT '任务描述',
    completed BOOLEAN DEFAULT FALSE COMMENT '是否完成',
    priority INT DEFAULT 1 COMMENT '优先级(1-低,2-中,3-高)',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    
    -- 添加索引
    INDEX idx_completed (completed),
    INDEX idx_priority (priority),
    INDEX idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='待办事项表';

-- 插入一些示例数据
INSERT INTO todo (content, description, priority) VALUES 
('学习Spring Boot', '深入学习Spring Boot框架的核心概念和实践', 3),
('完成项目文档', '编写项目的技术文档和用户手册', 2),
('代码重构', '优化现有代码结构，提高可维护性', 1);

-- 查看创建的表结构
DESCRIBE todo;

-- 查看插入的数据
SELECT * FROM todo;

-- 显示创建完成信息
SELECT 'Database and table created successfully!' AS message;