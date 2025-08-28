package com.example1.springbootdemo.service;

import com.example1.springbootdemo.entity.Todo;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * Todo待办事项业务逻辑服务类
 * 使用HashMap模拟数据库进行数据存储和操作
 */
@Service
public class TodoService {
    
    // 使用HashMap模拟数据库存储
    private final Map<Long, Todo> todoDatabase = new HashMap<>();
    
    // 用于生成自增ID
    private final AtomicLong idGenerator = new AtomicLong(1);
    
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
        todo.setId(idGenerator.getAndIncrement());
        todoDatabase.put(todo.getId(), todo);
        
        return todo;
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
        
        Todo todo = new Todo(content.trim(), description);
        todo.setId(idGenerator.getAndIncrement());
        todoDatabase.put(todo.getId(), todo);
        
        return todo;
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
        if (priority != null && priority >= 1 && priority <= 3) {
            todo.setPriority(priority);
        }
        return todo;
    }
    
    /**
     * 根据ID查询Todo任务
     * @param id 任务ID
     * @return Todo对象，如果不存在则返回null
     */
    public Todo getTodoById(Long id) {
        if (id == null) {
            return null;
        }
        return todoDatabase.get(id);
    }
    
    /**
     * 获取所有Todo任务
     * @return 所有Todo任务的列表
     */
    public List<Todo> getAllTodos() {
        return new ArrayList<>(todoDatabase.values());
    }
    
    /**
     * 获取已完成的Todo任务
     * @return 已完成的Todo任务列表
     */
    public List<Todo> getCompletedTodos() {
        return todoDatabase.values().stream()
                .filter(Todo::isCompleted)
                .collect(Collectors.toList());
    }
    
    /**
     * 获取未完成的Todo任务
     * @return 未完成的Todo任务列表
     */
    public List<Todo> getIncompleteTodos() {
        return todoDatabase.values().stream()
                .filter(todo -> !todo.isCompleted())
                .collect(Collectors.toList());
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
        return todoDatabase.values().stream()
                .filter(todo -> priority.equals(todo.getPriority()))
                .collect(Collectors.toList());
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
        return todoDatabase.values().stream()
                .filter(todo -> {
                    String content = todo.getContent() != null ? todo.getContent().toLowerCase() : "";
                    String description = todo.getDescription() != null ? todo.getDescription().toLowerCase() : "";
                    return content.contains(lowerKeyword) || description.contains(lowerKeyword);
                })
                .collect(Collectors.toList());
    }
    
    /**
     * 更新Todo任务内容
     * @param id 任务ID
     * @param content 新的任务内容
     * @return 更新后的Todo对象，如果任务不存在则返回null
     */
    public Todo updateTodoContent(Long id, String content) {
        Todo todo = getTodoById(id);
        if (todo == null) {
            return null;
        }
        
        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("任务内容不能为空");
        }
        
        todo.setContent(content.trim());
        return todo;
    }
    
    /**
     * 更新Todo任务描述
     * @param id 任务ID
     * @param description 新的任务描述
     * @return 更新后的Todo对象，如果任务不存在则返回null
     */
    public Todo updateTodoDescription(Long id, String description) {
        Todo todo = getTodoById(id);
        if (todo == null) {
            return null;
        }
        
        todo.setDescription(description);
        return todo;
    }
    
    /**
     * 更新Todo任务优先级
     * @param id 任务ID
     * @param priority 新的优先级（1-3）
     * @return 更新后的Todo对象，如果任务不存在则返回null
     */
    public Todo updateTodoPriority(Long id, Integer priority) {
        Todo todo = getTodoById(id);
        if (todo == null) {
            return null;
        }
        
        if (priority != null && (priority < 1 || priority > 3)) {
            throw new IllegalArgumentException("优先级必须在1-3之间");
        }
        
        todo.setPriority(priority);
        return todo;
    }
    
    /**
     * 标记Todo任务为已完成
     * @param id 任务ID
     * @return 更新后的Todo对象，如果任务不存在则返回null
     */
    public Todo markTodoAsCompleted(Long id) {
        Todo todo = getTodoById(id);
        if (todo == null) {
            return null;
        }
        
        todo.markAsCompleted();
        return todo;
    }
    
    /**
     * 标记Todo任务为未完成
     * @param id 任务ID
     * @return 更新后的Todo对象，如果任务不存在则返回null
     */
    public Todo markTodoAsIncomplete(Long id) {
        Todo todo = getTodoById(id);
        if (todo == null) {
            return null;
        }
        
        todo.markAsIncomplete();
        return todo;
    }
    
    /**
     * 切换Todo任务的完成状态
     * @param id 任务ID
     * @return 更新后的Todo对象，如果任务不存在则返回null
     */
    public Todo toggleTodoStatus(Long id) {
        Todo todo = getTodoById(id);
        if (todo == null) {
            return null;
        }
        
        if (todo.isCompleted()) {
            todo.markAsIncomplete();
        } else {
            todo.markAsCompleted();
        }
        
        return todo;
    }
    
    /**
     * 删除Todo任务
     * @param id 任务ID
     * @return 被删除的Todo对象，如果任务不存在则返回null
     */
    public Todo deleteTodo(Long id) {
        if (id == null) {
            return null;
        }
        return todoDatabase.remove(id);
    }
    
    /**
     * 删除所有已完成的Todo任务
     * @return 被删除的Todo任务数量
     */
    public int deleteCompletedTodos() {
        List<Long> completedIds = todoDatabase.values().stream()
                .filter(Todo::isCompleted)
                .map(Todo::getId)
                .collect(Collectors.toList());
        
        completedIds.forEach(todoDatabase::remove);
        return completedIds.size();
    }
    
    /**
     * 清空所有Todo任务
     * @return 被删除的Todo任务数量
     */
    public int deleteAllTodos() {
        int count = todoDatabase.size();
        todoDatabase.clear();
        // 重置ID生成器
        idGenerator.set(1);
        return count;
    }
    
    /**
     * 获取Todo任务总数
     * @return 任务总数
     */
    public int getTotalCount() {
        return todoDatabase.size();
    }
    
    /**
     * 获取已完成任务数量
     * @return 已完成任务数量
     */
    public int getCompletedCount() {
        return (int) todoDatabase.values().stream()
                .filter(Todo::isCompleted)
                .count();
    }
    
    /**
     * 获取未完成任务数量
     * @return 未完成任务数量
     */
    public int getIncompleteCount() {
        return getTotalCount() - getCompletedCount();
    }
    
    /**
     * 检查Todo任务是否存在
     * @param id 任务ID
     * @return 如果存在返回true，否则返回false
     */
    public boolean existsById(Long id) {
        return id != null && todoDatabase.containsKey(id);
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
        
        return todoDatabase.values().stream()
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
        
        return todoDatabase.values().stream()
                .sorted(comparator)
                .collect(Collectors.toList());
    }
}