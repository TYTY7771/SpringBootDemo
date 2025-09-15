package com.example1.springbootdemo.controller;

import com.example1.springbootdemo.dto.TodoRequest;
import com.example1.springbootdemo.dto.TodoUpdateRequest;
import com.example1.springbootdemo.entity.Todo;
import com.example1.springbootdemo.service.TodoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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


/**
 * Todo待办事项控制器
 * 提供RESTful API接口处理HTTP请求
 */
@Tag(name = "Todo管理", description = "Todo任务的增删改查操作")
@RestController
@RequestMapping("/api/todos")
@CrossOrigin(origins = "*") // 允许跨域请求
@Validated
public class TodoController {
    
    @Autowired
    private TodoService todoService;
    
    
    /**
     * 创建新的Todo任务
     */
    @Operation(summary = "创建Todo任务", description = "创建一个新的Todo任务")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "创建成功",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "400", description = "请求参数错误")
    })
    @PostMapping
    public ResponseEntity<Map<String, Object>> createTodo(
            @Parameter(description = "Todo任务请求对象", required = true)
            @Valid @RequestBody TodoRequest request) {
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
    
    
    /**
     * 获取所有Todo任务或根据条件筛选
     */
    @Operation(summary = "获取Todo任务列表", description = "获取所有Todo任务，支持按状态、优先级、关键词筛选和排序")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "获取成功",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Map.class)))
    })
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllTodos(
            @Parameter(description = "任务状态过滤 (completed/incomplete)", example = "completed")
            @RequestParam(required = false) String status,
            @Parameter(description = "优先级过滤 (1-3)", example = "1")
            @RequestParam(required = false) Integer priority,
            @Parameter(description = "关键词搜索", example = "学习")
            @RequestParam(required = false) String keyword,
            @Parameter(description = "排序字段", example = "createTime")
            @RequestParam(required = false, defaultValue = "createTime") String sortBy,
            @Parameter(description = "排序方向", example = "desc")
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
    
    
    /**
     * 根据ID获取单个Todo任务
     */
    @Operation(summary = "根据ID获取Todo任务", description = "通过任务ID获取单个Todo任务详情")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "获取成功",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "404", description = "任务不存在")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getTodoById(
            @Parameter(description = "任务ID", required = true, example = "1")
            @PathVariable @Min(value = 1, message = "ID必须大于0") Long id) {
        Map<String, Object> response = new HashMap<>();
        
        Todo todo = todoService.getTodoById(id);
        
        response.put("success", true);
        response.put("message", "获取Todo任务成功");
        response.put("data", todo);
        
        return ResponseEntity.ok(response);
    }
    
    
    /**
     * 更新Todo任务
     */
    @Operation(summary = "更新Todo任务", description = "根据ID更新Todo任务的内容、描述或优先级")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "更新成功",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "404", description = "任务不存在"),
            @ApiResponse(responseCode = "400", description = "请求参数错误")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateTodo(
            @Parameter(description = "任务ID", required = true, example = "1")
            @PathVariable @Min(value = 1, message = "ID必须大于0") Long id,
            @Parameter(description = "Todo任务更新请求对象", required = true)
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
    
    
    /**
     * 切换Todo任务完成状态
     */
    @Operation(summary = "切换任务状态", description = "切换Todo任务的完成状态（已完成↔未完成）")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "状态切换成功",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "404", description = "任务不存在")
    })
    @PatchMapping("/{id}/toggle")
    public ResponseEntity<Map<String, Object>> toggleTodoStatus(
            @Parameter(description = "任务ID", required = true, example = "1")
            @PathVariable @Min(value = 1, message = "ID必须大于0") Long id) {
        Map<String, Object> response = new HashMap<>();
        
        Todo todo = todoService.toggleTodoStatus(id);
        
        response.put("success", true);
        response.put("message", "Todo任务状态切换成功");
        response.put("data", todo);
        
        return ResponseEntity.ok(response);
    }
    
    
    /**
     * 删除单个Todo任务
     */
    @Operation(summary = "删除Todo任务", description = "根据ID删除指定的Todo任务")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "删除成功",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "404", description = "任务不存在")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteTodo(
            @Parameter(description = "任务ID", required = true, example = "1")
            @PathVariable @Min(value = 1, message = "ID必须大于0") Long id) {
        Map<String, Object> response = new HashMap<>();
        
        Todo deletedTodo = todoService.deleteTodo(id);
        
        response.put("success", true);
        response.put("message", "Todo任务删除成功");
        response.put("data", deletedTodo);
        
        return ResponseEntity.ok(response);
    }
    
    
    /**
     * 批量删除已完成的Todo任务
     */
    @Operation(summary = "删除已完成任务", description = "批量删除所有已完成的Todo任务")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "删除成功",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Map.class)))
    })
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

    
    /**
     * 删除所有Todo任务
     */
    @Operation(summary = "删除所有任务", description = "删除所有Todo任务（危险操作）")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "删除成功",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Map.class)))
    })
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
    
    
    /**
     * 获取Todo任务统计信息
     */
    @Operation(summary = "获取任务统计", description = "获取Todo任务的统计信息，包括总数、已完成数、未完成数")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "获取成功",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Map.class)))
    })
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