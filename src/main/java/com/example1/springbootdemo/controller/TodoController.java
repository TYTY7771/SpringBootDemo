package com.example1.springbootdemo.controller;

import com.example1.springbootdemo.entity.Todo;
import com.example1.springbootdemo.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


//Todo待办事项控制器
//提供RESTful API接口处理HTTP请求

@RestController
@RequestMapping("/api/todos")
@CrossOrigin(origins = "*") // 允许跨域请求
public class TodoController {
    
    @Autowired
    private TodoService todoService;
    
    
    //创建新的Todo任务
    //OST /api/todos
    
    @PostMapping
    public ResponseEntity<Map<String, Object>> createTodo(@RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            String content = (String) request.get("content");
            String description = (String) request.get("description");
            Integer priority = request.get("priority") != null ? 
                Integer.valueOf(request.get("priority").toString()) : null;
            
            Todo todo;
            if (description != null && priority != null) {
                todo = todoService.createTodo(content, description, priority);
            } else if (description != null) {
                todo = todoService.createTodo(content, description);
            } else {
                todo = todoService.createTodo(content);
            }
            
            response.put("success", true);
            response.put("message", "Todo任务创建成功");
            response.put("data", todo);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "创建Todo任务失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
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
    public ResponseEntity<Map<String, Object>> getTodoById(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Todo todo = todoService.getTodoById(id);
            
            if (todo == null) {
                response.put("success", false);
                response.put("message", "Todo任务不存在");
                return ResponseEntity.notFound().build();
            }
            
            response.put("success", true);
            response.put("message", "获取Todo任务成功");
            response.put("data", todo);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取Todo任务失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    
    //更新Todo任务
    //UT /api/todos/{id}
    
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateTodo(
            @PathVariable Long id, 
            @RequestBody Map<String, Object> request) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            if (!todoService.existsById(id)) {
                response.put("success", false);
                response.put("message", "Todo任务不存在");
                return ResponseEntity.notFound().build();
            }
            
            Todo updatedTodo = null;
            
            // 更新内容
            if (request.containsKey("content")) {
                String content = (String) request.get("content");
                updatedTodo = todoService.updateTodoContent(id, content);
            }
            
            // 更新描述
            if (request.containsKey("description")) {
                String description = (String) request.get("description");
                updatedTodo = todoService.updateTodoDescription(id, description);
            }
            
            // 更新优先级
            if (request.containsKey("priority")) {
                Integer priority = request.get("priority") != null ? 
                    Integer.valueOf(request.get("priority").toString()) : null;
                updatedTodo = todoService.updateTodoPriority(id, priority);
            }
            
            // 更新完成状态
            if (request.containsKey("completed")) {
                Boolean completed = (Boolean) request.get("completed");
                if (completed != null && completed) {
                    updatedTodo = todoService.markTodoAsCompleted(id);
                } else {
                    updatedTodo = todoService.markTodoAsIncomplete(id);
                }
            }
            
            // 如果没有任何更新操作，获取当前Todo
            if (updatedTodo == null) {
                updatedTodo = todoService.getTodoById(id);
            }
            
            response.put("success", true);
            response.put("message", "Todo任务更新成功");
            response.put("data", updatedTodo);
            
            return ResponseEntity.ok(response);
            
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "更新Todo任务失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    
    //切换Todo任务完成状态
    //PATCH /api/todos/{id}/toggle
    
    @PatchMapping("/{id}/toggle")
    public ResponseEntity<Map<String, Object>> toggleTodoStatus(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Todo todo = todoService.toggleTodoStatus(id);
            
            if (todo == null) {
                response.put("success", false);
                response.put("message", "Todo任务不存在");
                return ResponseEntity.notFound().build();
            }
            
            response.put("success", true);
            response.put("message", "Todo任务状态切换成功");
            response.put("data", todo);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "切换Todo任务状态失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    
    //删除单个Todo任务
    //DELETE /api/todos/{id}
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteTodo(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Todo deletedTodo = todoService.deleteTodo(id);
            
            if (deletedTodo == null) {
                response.put("success", false);
                response.put("message", "Todo任务不存在");
                return ResponseEntity.notFound().build();
            }
            
            response.put("success", true);
            response.put("message", "Todo任务删除成功");
            response.put("data", deletedTodo);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "删除Todo任务失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
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