# 博客系统后端

基于Spring Boot的博客系统后端，提供用户管理、博客发布、评论、点赞等功能。

## 技术栈

- Spring Boot 3.4.3
- MyBatis
- MySQL
- JWT (JSON Web Token)

## 功能特性

- 用户注册、登录、信息管理
- 博客发布、编辑、删除
- 博客标签管理
- 博客评论
- 博客点赞
- 博客列表查询（支持分页、关键词搜索、标签筛选）

## 环境要求

- JDK 17+
- Maven 3.6+
- MySQL 5.7+

## 快速开始

### 1. 克隆项目

```bash
git clone https://github.com/yourusername/my-blog.git
cd my-blog
```

### 2. 配置数据库

创建数据库并导入初始化SQL脚本：

```bash
mysql -u root -p < src/main/resources/db/schema.sql
```

或者手动创建数据库并执行脚本内容。

### 3. 修改配置

编辑 `src/main/resources/application.properties` 文件，配置数据库连接信息：

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/my_blog?useUnicode=true&characterEncoding=utf8&serverTimezone=GMT%2B8
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### 4. 构建并运行

```bash
mvn clean package
java -jar target/my-blog-0.0.1-SNAPSHOT.jar
```

或者使用Maven直接运行：

```bash
mvn spring-boot:run
```

应用将在 http://localhost:3000 启动。

## API文档

API接口文档详见 [API文档](API.md)。

## 项目结构

```
src/main/java/com/stdu/
├── config/           # 配置类
├── controller/       # 控制器
├── dto/              # 数据传输对象
├── entity/           # 实体类
├── exception/        # 异常处理
├── mapper/           # MyBatis Mapper接口
├── service/          # 服务接口
│   └── impl/         # 服务实现
└── util/             # 工具类

src/main/resources/
├── mapper/           # MyBatis XML映射文件
├── db/               # 数据库脚本
└── application.properties  # 应用配置
```

## 许可证

MIT 