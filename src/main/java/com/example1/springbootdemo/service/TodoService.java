package com.example1.springbootdemo.service;

import com.example1.springbootdemo.entity.Todo;
import com.example1.springbootdemo.exception.TodoNotFoundException;
import com.example1.springbootdemo.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Todo待办事项业务逻辑服务类
 * 使用TodoRepository与MySQL数据库进行交互
 */
@Service
public class TodoService {
    
    @Autowired
    private TodoRepository todoRepository;
    
    /**
     * 创建新的Todo任务
     * @param content 任务内容
     * @return 创建的Todo对象
     */
    public Todo createTodo(String content) {
        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("任务内容不能为空");
        }
        
        Todo todo = new Todo(content.trim());
        return todoRepository.save(todo);
    }
    
    /**
     * 创建新的Todo任务（带描述）
     * @param content 任务内容
     * @param description 任务描述
     * @return 创建的Todo对象
     */
    public Todo createTodo(String content, String description) {
        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("任务内容不能为空");
        }
        
        Todo todo = new Todo(content.trim());
        todo.setDescription(description);
        return todoRepository.save(todo);
    }
    
    /**
     * 创建新的Todo任务（完整参数）
     * @param content 任务内容
     * @param description 任务描述
     * @param priority 优先级
     * @return 创建的Todo对象
     */
    public Todo createTodo(String content, String description, Integer priority) {
        Todo todo = createTodo(content, description);
        if (priority != null) {
            if (priority < 1 || priority > 3) {
                throw new IllegalArgumentException("优先级必须在1-3之间");
            }
            todo.setPriority(priority);
        }
        return todo;
    }
    
    /**
     * 根据ID查询Todo任务
     * @param id 任务ID
     * @return Todo对象
     * @throws TodoNotFoundException 如果Todo不存在
     */
    public Todo getTodoById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Todo ID不能为空");
        }
        
        return todoRepository.findById(id)
                .orElseThrow(() -> new TodoNotFoundException("Todo任务不存在，ID: " + id));
    }
    
    /**
     * 获取所有Todo任务
     * @return 所有Todo任务的列表
     */
    public List<Todo> getAllTodos() {
        return todoRepository.findAll();
    }
    
    /**
     * 获取已完成的Todo任务
     * @return 已完成的Todo任务列表
     */
    public List<Todo> getCompletedTodos() {
        return todoRepository.findByCompleted(true);
    }
    
    /**
     * 获取未完成的Todo任务
     * @return 未完成的Todo任务列表
     */
    public List<Todo> getIncompleteTodos() {
        return todoRepository.findByCompleted(false);
    }
    
    /**
     * 根据优先级查询Todo任务
     * @param priority 优先级
     * @return 指定优先级的Todo任务列表
     */
    public List<Todo> getTodosByPriority(Integer priority) {
        if (priority == null) {
            return new ArrayList<>();
        }
        return todoRepository.findByPriority(priority);
    }
    
    /**
     * 根据关键词搜索Todo任务
     * @param keyword 搜索关键词
     * @return 包含关键词的Todo任务列表
     */
    public List<Todo> searchTodos(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllTodos();
        }
        
        String lowerKeyword = keyword.toLowerCase().trim();
        return todoRepository.findAll().stream()
                .filter(todo -> {
                    String content = todo.getContent() != null ? todo.getContent().toLowerCase() : "";
                    String description = todo.getDescription() != null ? todo.getDescription().toLowerCase() : "";
                    return content.contains(lowerKeyword) || description.contains(lowerKeyword);
                })
                .collect(Collectors.toList());
    }
    
    /**
     * 通用更新Todo任务方法
     * @param todo 要更新的Todo对象
     * @return 更新后的Todo对象
     */
    public Todo updateTodo(Todo todo) {
        if (todo == null || todo.getId() == null) {
            throw new IllegalArgumentException("Todo对象或ID不能为空");
        }
        
        // 验证Todo是否存在
        if (!todoRepository.existsById(todo.getId())) {
            throw new TodoNotFoundException("ID为 " + todo.getId() + " 的Todo任务不存在");
        }
        
        // 验证内容
        if (todo.getContent() != null && todo.getContent().trim().isEmpty()) {
            throw new IllegalArgumentException("任务内容不能为空");
        }
        
        // 验证优先级
        if (todo.getPriority() != null && (todo.getPriority() < 1 || todo.getPriority() > 3)) {
            throw new IllegalArgumentException("优先级必须在1-3之间");
        }
        
        // 更新数据库中的Todo
        return todoRepository.save(todo);
    }
    
    /**
     * 更新Todo任务内容
     * @param id 任务ID
     * @param content 新的任务内容
     * @return 更新后的Todo对象
     * @throws TodoNotFoundException 如果Todo不存在
     */
    public Todo updateTodoContent(Long id, String content) {
        Todo todo = getTodoById(id);
        
        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("任务内容不能为空");
        }
        
        todo.setContent(content.trim());
        return todoRepository.save(todo);
    }
    
    /**
     * 更新Todo任务描述
     * @param id 任务ID
     * @param description 新的任务描述
     * @return 更新后的Todo对象
     * @throws TodoNotFoundException 如果Todo不存在
     */
    public Todo updateTodoDescription(Long id, String description) {
        Todo todo = getTodoById(id);
        
        todo.setDescription(description);
        return todoRepository.save(todo);
    }
    
    /**
     * 更新Todo任务优先级
     * @param id 任务ID
     * @param priority 新的优先级（1-3）
     * @return 更新后的Todo对象
     * @throws TodoNotFoundException 如果Todo不存在
     */
    public Todo updateTodoPriority(Long id, Integer priority) {
        Todo todo = getTodoById(id);
        
        if (priority != null && (priority < 1 || priority > 3)) {
            throw new IllegalArgumentException("优先级必须在1-3之间");
        }
        
        todo.setPriority(priority);
        return todoRepository.save(todo);
    }
    
    /**
     * 标记Todo任务为已完成
     * @param id 任务ID
     * @return 更新后的Todo对象
     * @throws TodoNotFoundException 如果Todo不存在
     */
    public Todo markTodoAsCompleted(Long id) {
        Todo todo = getTodoById(id);
        
        todo.markAsCompleted();
        return todoRepository.save(todo);
    }
    
    /**
     * 标记Todo任务为未完成
     * @param id 任务ID
     * @return 更新后的Todo对象
     * @throws TodoNotFoundException 如果Todo不存在
     */
    public Todo markTodoAsIncomplete(Long id) {
        Todo todo = getTodoById(id);
        
        todo.markAsIncomplete();
        return todoRepository.save(todo);
    }
    
    /**
     * 切换Todo任务的完成状态
     * @param id 任务ID
     * @return 更新后的Todo对象
     * @throws TodoNotFoundException 如果Todo不存在
     */
    public Todo toggleTodoStatus(Long id) {
        Todo todo = getTodoById(id);
        
        if (todo.isCompleted()) {
            todo.markAsIncomplete();
        } else {
            todo.markAsCompleted();
        }
        
        return todoRepository.save(todo);
    }
    
    /**
     * 删除Todo任务
     * @param id 任务ID
     * @return 被删除的Todo对象
     * @throws TodoNotFoundException 如果Todo不存在
     */
    public Todo deleteTodo(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("任务ID不能为空");
        }
        
        Todo todo = getTodoById(id);
        todoRepository.deleteById(id);
        return todo;
    }
    
    /**
     * 删除所有已完成的Todo任务
     * @return 被删除的Todo任务数量
     */
    public int deleteCompletedTodos() {
        List<Todo> completedTodos = getCompletedTodos();
        int deletedCount = completedTodos.size();
        
        for (Todo todo : completedTodos) {
            todoRepository.deleteById(todo.getId());
        }
        
        return deletedCount;
    }
    
    /**
     * 清空所有Todo任务
     * @return 被删除的Todo任务数量
     */
    public int deleteAllTodos() {
        long count = todoRepository.count();
        todoRepository.deleteAll();
        return (int) count;
    }
    
    /**
     * 获取Todo任务总数
     * @return 任务总数
     */
    public int getTotalCount() {
        return (int) todoRepository.count();
    }
    
    /**
     * 获取已完成任务数量
     * @return 已完成任务数量
     */
    public int getCompletedCount() {
        return getCompletedTodos().size();
    }
    
    /**
     * 获取未完成任务数量
     * @return 未完成任务数量
     */
    public int getIncompleteCount() {
        return getIncompleteTodos().size();
    }
    
    /**
     * 检查Todo任务是否存在
     * @param id 任务ID
     * @return 如果存在返回true，否则返回false
     */
    public boolean existsById(Long id) {
        return id != null && todoRepository.existsById(id);
    }
    
    /**
     * 获取按创建时间排序的Todo任务列表
     * @param ascending 是否升序排列
     * @return 排序后的Todo任务列表
     */
    public List<Todo> getTodosSortedByCreateTime(boolean ascending) {
        Comparator<Todo> comparator = Comparator.comparing(Todo::getCreateTime);
        if (!ascending) {
            comparator = comparator.reversed();
        }
        
        return todoRepository.findAll().stream()
                .sorted(comparator)
                .collect(Collectors.toList());
    }
    
    /**
     * 获取按优先级排序的Todo任务列表
     * @param ascending 是否升序排列
     * @return 排序后的Todo任务列表
     */
    public List<Todo> getTodosSortedByPriority(boolean ascending) {
        Comparator<Todo> comparator = Comparator.comparing(Todo::getPriority, Comparator.nullsLast(Comparator.naturalOrder()));
        if (!ascending) {
            comparator = comparator.reversed();
        }
        
        return todoRepository.findAll().stream()
                .sorted(comparator)
                .collect(Collectors.toList());
    }
}