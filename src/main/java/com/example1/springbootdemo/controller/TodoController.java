package com.example1.springbootdemo.controller;

import com.example1.springbootdemo.dto.TodoRequest;
import com.example1.springbootdemo.dto.TodoUpdateRequest;
import com.example1.springbootdemo.entity.Todo;
import com.example1.springbootdemo.service.TodoService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


//Todo待办事项控制器
//提供RESTful API接口处理HTTP请求

@RestController
@RequestMapping("/api/todos")
@CrossOrigin(origins = "*") // 允许跨域请求
@Validated
public class TodoController {
    
    @Autowired
    private TodoService todoService;
    
    
    //创建新的Todo任务
    //OST /api/todos
    
    @PostMapping
    public ResponseEntity<Map<String, Object>> createTodo(@Valid @RequestBody TodoRequest request) {
        Map<String, Object> response = new HashMap<>();
        
        Todo todo;
        if (request.getDescription() != null && request.getPriority() != null) {
            todo = todoService.createTodo(request.getContent(), request.getDescription(), request.getPriority());
        } else if (request.getDescription() != null) {
            todo = todoService.createTodo(request.getContent(), request.getDescription());
        } else {
            todo = todoService.createTodo(request.getContent());
        }
        
        response.put("success", true);
        response.put("message", "Todo任务创建成功");
        response.put("data", todo);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    
    //获取所有Todo任务
    //GET /api/todos
    
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllTodos(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Integer priority,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false, defaultValue = "createTime") String sortBy,
            @RequestParam(required = false, defaultValue = "desc") String sortOrder) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<Todo> todos;
            
            // 根据不同条件查询
            if (keyword != null && !keyword.trim().isEmpty()) {
                todos = todoService.searchTodos(keyword);
            } else if ("completed".equals(status)) {
                todos = todoService.getCompletedTodos();
            } else if ("incomplete".equals(status)) {
                todos = todoService.getIncompleteTodos();
            } else if (priority != null) {
                todos = todoService.getTodosByPriority(priority);
            } else {
                // 根据排序条件获取
                boolean ascending = "asc".equalsIgnoreCase(sortOrder);
                if ("priority".equals(sortBy)) {
                    todos = todoService.getTodosSortedByPriority(ascending);
                } else {
                    todos = todoService.getTodosSortedByCreateTime(ascending);
                }
            }
            
            response.put("success", true);
            response.put("message", "获取Todo任务列表成功");
            response.put("data", todos);
            response.put("total", todos.size());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取Todo任务列表失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    
    //根据ID获取单个Todo任务
    //GET /api/todos/{id}
    
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getTodoById(@PathVariable @Min(value = 1, message = "ID必须大于0") Long id) {
        Map<String, Object> response = new HashMap<>();
        
        Todo todo = todoService.getTodoById(id);
        
        response.put("success", true);
        response.put("message", "获取Todo任务成功");
        response.put("data", todo);
        
        return ResponseEntity.ok(response);
    }
    
    
    //更新Todo任务
    //UT /api/todos/{id}
    
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateTodo(
            @PathVariable @Min(value = 1, message = "ID必须大于0") Long id, 
            @Valid @RequestBody TodoUpdateRequest request) {
        Map<String, Object> response = new HashMap<>();
        
        Todo todo = todoService.getTodoById(id);
        
        // 更新字段
        if (request.getContent() != null) {
            todo.setContent(request.getContent());
        }
        
        if (request.getDescription() != null) {
            todo.setDescription(request.getDescription());
        }
        
        if (request.getPriority() != null) {
            todo.setPriority(request.getPriority());
        }
        
        if (request.getCompleted() != null) {
            todo.setCompleted(request.getCompleted());
        }
        
        Todo updatedTodo = todoService.updateTodo(todo);
        
        response.put("success", true);
        response.put("message", "Todo任务更新成功");
        response.put("data", updatedTodo);
        
        return ResponseEntity.ok(response);
    }
    
    
    //切换Todo任务完成状态
    //PATCH /api/todos/{id}/toggle
    
    @PatchMapping("/{id}/toggle")
    public ResponseEntity<Map<String, Object>> toggleTodoStatus(@PathVariable @Min(value = 1, message = "ID必须大于0") Long id) {
        Map<String, Object> response = new HashMap<>();
        
        Todo todo = todoService.toggleTodoStatus(id);
        
        response.put("success", true);
        response.put("message", "Todo任务状态切换成功");
        response.put("data", todo);
        
        return ResponseEntity.ok(response);
    }
    
    
    //删除单个Todo任务
    //DELETE /api/todos/{id}
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteTodo(@PathVariable @Min(value = 1, message = "ID必须大于0") Long id) {
        Map<String, Object> response = new HashMap<>();
        
        Todo deletedTodo = todoService.deleteTodo(id);
        
        response.put("success", true);
        response.put("message", "Todo任务删除成功");
        response.put("data", deletedTodo);
        
        return ResponseEntity.ok(response);
    }
    
    
    //删除所有已完成的Todo任务
    //DELETE /api/todos/completed
    
    @DeleteMapping("/completed")
    public ResponseEntity<Map<String, Object>> deleteCompletedTodos() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            int deletedCount = todoService.deleteCompletedTodos();
            
            response.put("success", true);
            response.put("message", "已完成的Todo任务删除成功");
            response.put("deletedCount", deletedCount);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "删除已完成Todo任务失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    
    //清空所有Todo任务
    //DELETE /api/todos
    
    @DeleteMapping
    public ResponseEntity<Map<String, Object>> deleteAllTodos() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            int deletedCount = todoService.deleteAllTodos();
            
            response.put("success", true);
            response.put("message", "所有Todo任务清空成功");
            response.put("deletedCount", deletedCount);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "清空Todo任务失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    
    //获取Todo任务统计信息
    //GET /api/todos/stats
    
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getTodoStats() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Map<String, Object> stats = new HashMap<>();
            stats.put("total", todoService.getTotalCount());
            stats.put("completed", todoService.getCompletedCount());
            stats.put("incomplete", todoService.getIncompleteCount());
            
            response.put("success", true);
            response.put("message", "获取Todo统计信息成功");
            response.put("data", stats);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取Todo统计信息失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}