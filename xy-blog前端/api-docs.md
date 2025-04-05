# 博客系统API接口文档

## 基本信息

- 基础URL: `http://localhost:3000/api`
- 请求头: 除登录和注册接口外，其他接口需要在请求头中添加token
  ```
  Authorization: Bearer {token}
  ```
- 响应格式: 所有接口返回格式统一为JSON
  ```json
  {
    "code": 200,           // 状态码：200成功，非200失败
    "message": "success",  // 响应信息
    "data": {}             // 响应数据
  }
  ```

## 用户相关接口

### 1. 用户注册

- 请求路径: `/user/register`
- 请求方法: `POST`
- 请求参数:

| 参数名   | 类型   | 是否必须 | 说明     |
| -------- | ------ | -------- | -------- |
| username | string | 是       | 用户名   |
| password | string | 是       | 密码     |
| nickname | string | 是       | 用户昵称 |

- 响应示例:

```json
{
  "code": 200,
  "message": "注册成功",
  "data": null
}
```

### 2. 用户登录

- 请求路径: `/user/login`
- 请求方法: `POST`
- 请求参数:

| 参数名   | 类型   | 是否必须 | 说明   |
| -------- | ------ | -------- | ------ |
| username | string | 是       | 用户名 |
| password | string | 是       | 密码   |

- 响应示例:

```json
{
  "code": 200,
  "message": "登录成功",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "userInfo": {
      "id": 1,
      "username": "test",
      "nickname": "测试用户",
      "avatar": "https://example.com/avatar.png",
      "createTime": "2023-01-01T00:00:00.000Z"
    }
  }
}
```

### 3. 获取用户信息

- 请求路径: `/user/info`
- 请求方法: `GET`
- 请求参数: 无
- 响应示例:

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "userInfo": {
      "id": 1,
      "username": "test",
      "nickname": "测试用户",
      "avatar": "https://example.com/avatar.png",
      "createTime": "2023-01-01T00:00:00.000Z"
    },
    "stats": {
      "blogCount": 10,
      "likeCount": 20,
      "commentCount": 5
    }
  }
}
```

### 4. 更新用户信息

- 请求路径: `/user/info`
- 请求方法: `PUT`
- 请求参数:

| 参数名   | 类型   | 是否必须 | 说明     |
| -------- | ------ | -------- | -------- |
| nickname | string | 否       | 用户昵称 |
| avatar   | string | 否       | 头像URL  |

- 响应示例:

```json
{
  "code": 200,
  "message": "更新成功",
  "data": {
    "userInfo": {
      "id": 1,
      "username": "test",
      "nickname": "新昵称",
      "avatar": "https://example.com/new-avatar.png",
      "createTime": "2023-01-01T00:00:00.000Z"
    }
  }
}
```

## 博客相关接口

### 1. 获取博客列表

- 请求路径: `/blogs`
- 请求方法: `GET`
- 请求参数:

| 参数名   | 类型   | 是否必须 | 说明                   |
| -------- | ------ | -------- | ---------------------- |
| page     | number | 否       | 页码，默认1            |
| pageSize | number | 否       | 每页数量，默认10       |
| tag      | string | 否       | 按标签筛选             |
| keyword  | string | 否       | 搜索关键词（标题/内容) |

- 响应示例:

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "total": 100,
    "page": 1,
    "pageSize": 10,
    "data": [
      {
        "id": 1,
        "title": "博客标题",
        "summary": "博客摘要...",
        "tags": ["前端", "JavaScript"],
        "createTime": "2023-01-01T00:00:00.000Z",
        "updateTime": "2023-01-01T00:00:00.000Z",
        "author": {
          "id": 1,
          "nickname": "测试用户",
          "avatar": "https://example.com/avatar.png"
        },
        "likeCount": 10,
        "commentCount": 5,
        "isLiked": true
      }
      // ...更多博客
    ]
  }
}
```

### 2. 获取博客详情

- 请求路径: `/blogs/:id`
- 请求方法: `GET`
- 请求参数: 无
- 响应示例:

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "title": "博客标题",
    "content": "博客正文内容...",
    "summary": "博客摘要...",
    "tags": ["前端", "JavaScript"],
    "createTime": "2023-01-01T00:00:00.000Z",
    "updateTime": "2023-01-01T00:00:00.000Z",
    "author": {
      "id": 1,
      "nickname": "测试用户",
      "avatar": "https://example.com/avatar.png"
    },
    "likeCount": 10,
    "commentCount": 5,
    "isLiked": true
  }
}
```

### 3. 发布博客

- 请求路径: `/blogs`
- 请求方法: `POST`
- 请求参数:

| 参数名  | 类型     | 是否必须 | 说明                 |
| ------- | -------- | -------- | -------------------- |
| title   | string   | 是       | 博客标题             |
| content | string   | 是       | 博客内容             |
| summary | string   | 否       | 博客摘要，不填自动生成 |
| tags    | string[] | 否       | 博客标签，最多3个    |

- 响应示例:

```json
{
  "code": 200,
  "message": "发布成功",
  "data": {
    "id": 1,
    "title": "博客标题",
    "content": "博客正文内容...",
    "summary": "博客摘要...",
    "tags": ["前端", "JavaScript"],
    "createTime": "2023-01-01T00:00:00.000Z",
    "updateTime": "2023-01-01T00:00:00.000Z"
  }
}
```

### 4. 更新博客

- 请求路径: `/blogs/:id`
- 请求方法: `PUT`
- 请求参数:

| 参数名  | 类型     | 是否必须 | 说明              |
| ------- | -------- | -------- | ----------------- |
| title   | string   | 否       | 博客标题          |
| content | string   | 否       | 博客内容          |
| summary | string   | 否       | 博客摘要          |
| tags    | string[] | 否       | 博客标签，最多3个 |

- 响应示例:

```json
{
  "code": 200,
  "message": "更新成功",
  "data": {
    "id": 1,
    "title": "更新后的标题",
    "content": "更新后的内容...",
    "summary": "更新后的摘要...",
    "tags": ["前端", "Vue"],
    "createTime": "2023-01-01T00:00:00.000Z",
    "updateTime": "2023-01-02T00:00:00.000Z"
  }
}
```

### 5. 删除博客

- 请求路径: `/blogs/:id`
- 请求方法: `DELETE`
- 请求参数: 无
- 响应示例:

```json
{
  "code": 200,
  "message": "删除成功",
  "data": null
}
```

### 6. 点赞博客

- 请求路径: `/blogs/:id/like`
- 请求方法: `POST`
- 请求参数: 无
- 响应示例:

```json
{
  "code": 200,
  "message": "点赞成功",
  "data": {
    "likeCount": 11
  }
}
```

### 7. 取消点赞

- 请求路径: `/blogs/:id/like`
- 请求方法: `DELETE`
- 请求参数: 无
- 响应示例:

```json
{
  "code": 200,
  "message": "取消点赞成功",
  "data": {
    "likeCount": 10
  }
}
```

## 评论相关接口

### 1. 获取博客评论列表

- 请求路径: `/blogs/:blogId/comments`
- 请求方法: `GET`
- 请求参数:

| 参数名   | 类型   | 是否必须 | 说明             |
| -------- | ------ | -------- | ---------------- |
| page     | number | 否       | 页码，默认1      |
| pageSize | number | 否       | 每页数量，默认10 |

- 响应示例:

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "total": 5,
    "page": 1,
    "pageSize": 10,
    "data": [
      {
        "id": 1,
        "content": "评论内容...",
        "createTime": "2023-01-01T00:00:00.000Z",
        "user": {
          "id": 2,
          "nickname": "评论用户",
          "avatar": "https://example.com/avatar2.png"
        }
      }
      // ...更多评论
    ]
  }
}
```

### 2. 发表评论

- 请求路径: `/blogs/:blogId/comments`
- 请求方法: `POST`
- 请求参数:

| 参数名  | 类型   | 是否必须 | 说明     |
| ------- | ------ | -------- | -------- |
| content | string | 是       | 评论内容 |

- 响应示例:

```json
{
  "code": 200,
  "message": "评论成功",
  "data": {
    "id": 6,
    "content": "新评论内容...",
    "createTime": "2023-01-02T00:00:00.000Z",
    "user": {
      "id": 1,
      "nickname": "测试用户",
      "avatar": "https://example.com/avatar.png"
    }
  }
}
```

### 3. 删除评论

- 请求路径: `/comments/:id`
- 请求方法: `DELETE`
- 请求参数: 无
- 响应示例:

```json
{
  "code": 200,
  "message": "删除成功",
  "data": null
}
```

## 错误状态码

| 状态码 | 说明                 |
| ------ | -------------------- |
| 200    | 请求成功             |
| 400    | 请求参数错误         |
| 401    | 未认证（未登录或token失效） |
| 403    | 权限不足             |
| 404    | 资源不存在           |
| 500    | 服务器内部错误       |

## 数据库表设计

### 1. 用户表 (users)

| 字段名     | 类型         | 说明         |
| ---------- | ------------ | ------------ |
| id         | int          | 主键，自增   |
| username   | varchar(50)  | 用户名，唯一 |
| password   | varchar(255) | 密码，加密存储 |
| nickname   | varchar(50)  | 昵称         |
| avatar     | varchar(255) | 头像URL      |
| create_time | datetime     | 创建时间     |
| update_time | datetime     | 更新时间     |

### 2. 博客表 (blogs)

| 字段名     | 类型         | 说明       |
| ---------- | ------------ | ---------- |
| id         | int          | 主键，自增 |
| user_id    | int          | 作者ID     |
| title      | varchar(100) | 博客标题   |
| content    | text         | 博客内容   |
| summary    | varchar(255) | 博客摘要   |
| like_count | int          | 点赞数     |
| comment_count | int       | 评论数     |
| create_time | datetime     | 创建时间   |
| update_time | datetime     | 更新时间   |

### 3. 标签表 (tags)

| 字段名 | 类型        | 说明       |
| ------ | ----------- | ---------- |
| id     | int         | 主键，自增 |
| name   | varchar(50) | 标签名称   |
| create_time | datetime | 创建时间   |

### 4. 博客标签关联表 (blog_tags)

| 字段名  | 类型 | 说明   |
| ------- | ---- | ------ |
| blog_id | int  | 博客ID |
| tag_id  | int  | 标签ID |

### 5. 点赞表 (likes)

| 字段名     | 类型     | 说明       |
| ---------- | -------- | ---------- |
| id         | int      | 主键，自增 |
| user_id    | int      | 用户ID     |
| blog_id    | int      | 博客ID     |
| create_time | datetime | 点赞时间   |

CREATE TABLE `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(255) NOT NULL COMMENT '密码(加密存储)',
  `nickname` varchar(50) NOT NULL COMMENT '昵称',
  `avatar` varchar(255) DEFAULT '/static/default-avatar.png' COMMENT '头像URL',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

CREATE TABLE `blogs` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL COMMENT '作者ID',
  `title` varchar(100) NOT NULL COMMENT '博客标题',
  `content` text NOT NULL COMMENT '博客内容',
  `summary` varchar(255) DEFAULT NULL COMMENT '博客摘要',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_create_time` (`create_time`),
  CONSTRAINT `fk_blogs_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='博客表';

CREATE TABLE `tags` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL COMMENT '标签名称',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='标签表';

CREATE TABLE `blog_tags` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `blog_id` int(11) NOT NULL COMMENT '博客ID',
  `tag_id` int(11) NOT NULL COMMENT '标签ID',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_blog_tag` (`blog_id`,`tag_id`),
  KEY `idx_tag_id` (`tag_id`),
  CONSTRAINT `fk_blog_tags_blog_id` FOREIGN KEY (`blog_id`) REFERENCES `blogs` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_blog_tags_tag_id` FOREIGN KEY (`tag_id`) REFERENCES `tags` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='博客-标签关联表';

CREATE TABLE `comments` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `blog_id` int(11) NOT NULL COMMENT '博客ID',
  `user_id` int(11) NOT NULL COMMENT '评论用户ID',
  `content` varchar(500) NOT NULL COMMENT '评论内容',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_blog_id` (`blog_id`),
  KEY `idx_user_id` (`user_id`),
  CONSTRAINT `fk_comments_blog_id` FOREIGN KEY (`blog_id`) REFERENCES `blogs` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_comments_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='评论表';

CREATE TABLE `likes` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `blog_id` int(11) NOT NULL COMMENT '博客ID',
  `user_id` int(11) NOT NULL COMMENT '点赞用户ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_blog_user` (`blog_id`,`user_id`),
  KEY `idx_user_id` (`user_id`),
  CONSTRAINT `fk_likes_blog_id` FOREIGN KEY (`blog_id`) REFERENCES `blogs` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_likes_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='点赞表'; 