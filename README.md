# Spring Boot Todo 管理系统

一个基于 Spring Boot 3 构建的现代化 Todo 任务管理系统，提供完整的 RESTful API 接口和交互式 API 文档。

## 项目简介

本项目是一个功能完整的 Todo 任务管理系统，采用现代化的技术栈构建：

- **后端框架**: Spring Boot 3.2.1
- **数据库**: H2 内存数据库（开发环境）/ MySQL（生产环境）
- **数据访问**: Spring Data JPA
- **API 文档**: SpringDoc OpenAPI 3 (Swagger)
- **构建工具**: Maven
- **Java 版本**: JDK 17+

### 主要功能

- ✅ 创建、查询、更新、删除 Todo 任务
- ✅ 任务状态管理（待完成/已完成）
- ✅ 批量操作（批量删除已完成任务、清空所有任务）
- ✅ 任务统计功能
- ✅ 数据验证和异常处理
- ✅ 交互式 API 文档（Swagger UI）

## 环境要求

### 必需环境
- **Java**: JDK 17 或更高版本
- **Maven**: 3.6+ （或使用项目自带的 Maven Wrapper）

### 可选环境
- **MySQL**: 8.0+（生产环境使用）
- **IDE**: IntelliJ IDEA、Eclipse 或 VS Code

## 快速启动

### 1. 克隆项目
```bash
git clone <repository-url>
cd springbootDemo
```

### 2. 使用 Maven Wrapper 启动（推荐）

**Windows:**
```cmd
.\mvnw.cmd spring-boot:run
```

**Linux/macOS:**
```bash
./mvnw spring-boot:run
```

### 3. 使用本地 Maven 启动
```bash
mvn spring-boot:run
```

### 4. 访问应用

启动成功后，应用将运行在 `http://localhost:8080`

- **应用主页**: http://localhost:8080
- **Swagger UI**: http://localhost:8080/swagger-ui/index.html
- **OpenAPI JSON**: http://localhost:8080/v3/api-docs
- **H2 数据库控制台**: http://localhost:8080/h2-console

## API 接口文档

### 在线文档

访问 [Swagger UI](http://localhost:8080/swagger-ui/index.html) 查看完整的交互式 API 文档，支持在线测试所有接口。

### API 接口列表

#### Todo 管理接口

| 方法 | 路径 | 描述 | 参数 |
|------|------|------|------|
| `POST` | `/api/todos` | 创建新的 Todo 任务 | `CreateTodoRequest` |
| `GET` | `/api/todos` | 获取所有 Todo 任务 | 无 |
| `GET` | `/api/todos/{id}` | 根据 ID 获取特定 Todo 任务 | `id`: 任务ID |
| `PUT` | `/api/todos/{id}` | 更新指定 Todo 任务 | `id`: 任务ID, `UpdateTodoRequest` |
| `PATCH` | `/api/todos/{id}/toggle` | 切换任务完成状态 | `id`: 任务ID |
| `DELETE` | `/api/todos/{id}` | 删除指定 Todo 任务 | `id`: 任务ID |
| `DELETE` | `/api/todos/completed` | 批量删除已完成的任务 | 无 |
| `DELETE` | `/api/todos/all` | 删除所有任务 | 无 |
| `GET` | `/api/todos/stats` | 获取任务统计信息 | 无 |

#### 数据模型

**Todo 实体**
```json
{
  "id": 1,
  "title": "任务标题",
  "description": "任务描述",
  "completed": false,
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T10:30:00"
}
```

**创建请求 (CreateTodoRequest)**
```json
{
  "title": "任务标题",
  "description": "任务描述"
}
```

**更新请求 (UpdateTodoRequest)**
```json
{
  "title": "更新的标题",
  "description": "更新的描述",
  "completed": true
}
```

**统计响应 (TodoStatsResponse)**
```json
{
  "total": 10,
  "completed": 3,
  "pending": 7
}
```

### 使用示例

#### 创建新任务
```bash
curl -X POST "http://localhost:8080/api/todos" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "学习 Spring Boot",
    "description": "完成 Spring Boot 教程的学习"
  }'
```

#### 获取所有任务
```bash
curl -X GET "http://localhost:8080/api/todos"
```

#### 切换任务状态
```bash
curl -X PATCH "http://localhost:8080/api/todos/1/toggle"
```

## 数据库配置

### 开发环境（H2 内存数据库）

默认使用 H2 内存数据库，无需额外配置。数据库控制台访问：
- URL: http://localhost:8080/h2-console
- JDBC URL: `jdbc:h2:mem:testdb`
- 用户名: `sa`
- 密码: （空）

### 生产环境（MySQL）

在 `application-prod.properties` 中配置 MySQL 连接：

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/todoapp
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
spring.jpa.hibernate.ddl-auto=update
```

使用生产配置启动：
```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=prod
```

## 项目结构

```
src/main/java/com/example1/springbootdemo/
├── SpringbootDemoApplication.java    # 应用程序入口
├── config/
│   └── SwaggerConfig.java           # Swagger/OpenAPI 配置
├── controller/
│   └── TodoController.java          # REST API 控制器
├── dto/
│   ├── CreateTodoRequest.java       # 创建请求 DTO
│   ├── TodoStatsResponse.java       # 统计响应 DTO
│   └── UpdateTodoRequest.java       # 更新请求 DTO
├── entity/
│   └── Todo.java                    # Todo 实体类
├── exception/
│   ├── GlobalExceptionHandler.java  # 全局异常处理器
│   └── TodoNotFoundException.java   # 自定义异常
├── repository/
│   └── TodoRepository.java          # 数据访问层
└── service/
    └── TodoService.java             # 业务逻辑层
```

## 开发说明

### 代码规范
- 使用 JPA 命名规范方法进行数据库操作
- 遵循 RESTful API 设计原则
- 完整的异常处理和数据验证
- 详细的 API 文档注解

### 技术特性
- **JPA 命名规范**: 使用方法名自动生成 SQL 查询
- **数据验证**: 使用 `@Valid` 和 `@NotBlank` 等注解
- **异常处理**: 统一的异常处理机制
- **API 文档**: 完整的 Swagger/OpenAPI 文档

## 故障排除

### 常见问题

1. **端口占用**
   ```
   Web server failed to start. Port 8080 was already in use.
   ```
   解决方案：更改端口或停止占用 8080 端口的进程
   ```properties
   server.port=8081
   ```

2. **Java 版本不兼容**
   ```
   Unsupported class file major version
   ```
   解决方案：确保使用 JDK 17 或更高版本

3. **Maven 依赖问题**
   ```bash
   ./mvnw clean install
   ```

## 贡献指南

1. Fork 本项目
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 开启 Pull Request

## 许可证

本项目采用 MIT 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情。

## 联系方式

- 项目维护者: [Your Name]
- 邮箱: [your.email@example.com]
- 项目链接: [https://github.com/yourusername/springbootDemo](https://github.com/yourusername/springbootDemo)

---

**快速开始**: 运行 `./mvnw spring-boot:run` 然后访问 http://localhost:8080/swagger-ui/index.html 查看 API 文档！