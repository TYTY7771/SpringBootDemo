package com.example1.springbootdemo.repository;

import com.example1.springbootdemo.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Todo数据访问层接口
 * 继承JpaRepository提供基本的CRUD操作
 * 
 * @author SpringBootDemo
 * @since 1.0
 */
@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {
    
    /**
     * 根据完成状态查询待办事项
     * 
     * @param completed 完成状态
     * @return 待办事项列表
     */
    List<Todo> findByCompleted(Boolean completed);
    
    /**
     * 根据优先级查询待办事项
     * 
     * @param priority 优先级
     * @return 待办事项列表
     */
    List<Todo> findByPriority(Integer priority);
    
    /**
     * 根据完成状态和优先级查询待办事项
     * 
     * @param completed 完成状态
     * @param priority 优先级
     * @return 待办事项列表
     */
    List<Todo> findByCompletedAndPriority(Boolean completed, Integer priority);
    
    /**
     * 统计指定状态的待办事项数量
     * 
     * @param completed 完成状态
     * @return 数量
     */
    long countByCompleted(Boolean completed);
}