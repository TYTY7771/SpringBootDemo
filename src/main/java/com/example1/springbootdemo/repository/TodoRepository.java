package com.example1.springbootdemo.repository;

import com.example1.springbootdemo.entity.Todo;
import org.springframework.data.domain.Sort;
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
    
    /**
     * 根据内容包含关键词搜索待办事项（忽略大小写）
     * 
     * @param content 搜索关键词
     * @return 匹配的待办事项列表
     */
    List<Todo> findByContentContainingIgnoreCase(String content);
    
    /**
     * 根据描述包含关键词搜索待办事项（忽略大小写）
     * 
     * @param description 搜索关键词
     * @return 匹配的待办事项列表
     */
    List<Todo> findByDescriptionContainingIgnoreCase(String description);
    
    /**
     * 根据内容或描述包含关键词搜索待办事项（忽略大小写）
     * 
     * @param content 内容搜索关键词
     * @param description 描述搜索关键词
     * @return 匹配的待办事项列表
     */
    List<Todo> findByContentContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String content, String description);
    
    /**
     * 获取所有待办事项并按创建时间排序
     * 
     * @param sort 排序规则
     * @return 排序后的待办事项列表
     */
    List<Todo> findAllByOrderByCreateTimeAsc();
    
    /**
     * 获取所有待办事项并按创建时间倒序排序
     * 
     * @return 排序后的待办事项列表
     */
    List<Todo> findAllByOrderByCreateTimeDesc();
    
    /**
     * 获取所有待办事项并按优先级升序排序
     * 
     * @return 排序后的待办事项列表
     */
    List<Todo> findAllByOrderByPriorityAsc();
    
    /**
     * 获取所有待办事项并按优先级降序排序
     * 
     * @return 排序后的待办事项列表
     */
    List<Todo> findAllByOrderByPriorityDesc();
    
    /**
     * 使用动态排序查询所有待办事项
     * 
     * @param sort 排序规则
     * @return 排序后的待办事项列表
     */
    List<Todo> findAll(Sort sort);
}