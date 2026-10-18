# Crabapple Sensitive（海棠敏感词服务）

基于 [crabapple-commons](https://github.com/xuechong1989/crabapple-commons) 构建的敏感词检测与词库管理服务，演示国密传输加密、客户端令牌、敏感词匹配与词库维护。

## 功能

- **敏感词检测**：单条/批量检测、命中统计、按关键词长度排序
- **词库管理**：新增、修改、删除、去重、文本批量导入、文件上传
- **客户端令牌**：SM2 非对称加密生成 accessToken，随机数防重放
- **管理员口令登录**：需显式配置 `auth_admin_key`，未配置时自动禁用
- **日志**：操作日志分页查询；系统参数通过 `t_properties` 读取
- **安全能力**：XSS / CSRF / 同源 / 限流 / 上传防护 / 传参加密（默认关闭）

## 技术栈

| 层级 | 技术 |
| --- | --- |
| 语言 | Java 21 |
| 框架 | Spring Boot 4 |
| 持久层 | MyBatis 3 + Druid |
| 缓存 | 本地缓存 / Redis（可选） |
| 国密 | SM2 / SM3 / SM4（crabapple-commons 内置） |
| 构建 | Maven 3.9+ |

## 目录结构

```
crabapple-sensitive
├── pom.xml
└── src/main
    ├── java/xc/crabapple/sensitive
    │   ├── common          配置、领域对象、工具类
    │   └── module          auth / sensitiveWord / properties / log
    └── resources
        └── application.properties.example
```

## 快速开始

### 1. 安装基础库

```bash
git clone https://github.com/xuechong1989/crabapple-commons.git
cd crabapple-commons
mvn clean install
```

### 2. 准备配置

```bash
cd crabapple-sensitive
cp src/main/resources/application.properties.example src/main/resources/application.properties
```

完整配置项见 [application.properties.example](src/main/resources/application.properties.example)。

### 3. 生成 SM2 密钥对

`auth_sm2_publickey` / `auth_sm2_privatekey` 必须由部署方自行生成，私钥只保存在部署环境。可使用 `crabapple-commons` 的 `Sm2Util.generateKeyMap()` 生成，或使用任意标准 SM2 工具。

```java
Map<String, String> keyMap = Sm2Util.generateKeyMap();
// keyMap.get("publicKey") / keyMap.get("privateKey")
```

### 4. 初始化数据库

创建数据库（默认 `crabapple_sensitive`），准备以下表：

| 表 | 用途 |
| --- | --- |
| `t_sensitive_word` | 敏感词、类别、备注 |
| `t_auth` | 受信任客户端节点与 accessToken 记录 |
| `t_user` | 后台用户 |
| `t_properties` | 键值配置 |
| `t_log` | 操作日志 |

建表脚本与去重词表见 [export/README.md](export/README.md)。

字段定义可参考 `src/main/java/xc/crabapple/sensitive/module/**/*Mapper.xml` 与对应 DAO。

### 5. 启动

```bash
mvn spring-boot:run
# 或
mvn clean package
java -jar target/crabapple-sensitive-21-4.2.00.jar
```

默认端口 `8081`。

## 主要接口

| 接口 | 说明 |
| --- | --- |
| `POST /api/crabapple/sensitive/auth/login` | 管理员口令登录（需配置 `auth_admin_key`） |
| `POST /api/crabapple/sensitive/auth/generate` | 为客户端节点生成 accessToken |
| `POST /api/crabapple/sensitive/auth/getList` | 客户端节点列表 |
| `POST /api/crabapple/sensitive/sensitiveWord/check` | 敏感词检测 |
| `POST /api/crabapple/sensitive/sensitiveWord/insert` | 新增敏感词 |
| `POST /api/crabapple/sensitive/sensitiveWord/update` | 修改敏感词 |
| `POST /api/crabapple/sensitive/sensitiveWord/delete` | 删除敏感词 |
| `POST /api/crabapple/sensitive/sensitiveWord/deleteRepeatKeyword` | 去重 |
| `POST /api/crabapple/sensitive/sensitiveWord/insertByTxt` | 从文本文件批量导入 |
| `POST /api/crabapple/sensitive/sensitiveWord/upload` | 上传词库文件 |
| `POST /api/crabapple/sensitive/sensitiveWord/getList` | 词库分页 |
| `POST /api/crabapple/sensitive/sensitiveWord/sortByCount` | 按关键词长度排序 |
| `POST /api/crabapple/sensitive/log/getList` | 操作日志 |

请求/响应统一使用 `S2cDto` 包装。

## 需要自行配置的密钥

仓库不包含任何真实密钥。首次部署请复制 [application.properties.example](src/main/resources/application.properties.example) 并填写：

| 配置项 | 说明 |
| --- | --- |
| `db.password` | 数据库口令 |
| `auth_admin_key` | 管理员口令；留空则禁用 `/auth/login` |
| `auth_sm2_publickey` / `auth_sm2_privatekey` | 客户端令牌的 SM2 密钥对，启用令牌功能时必填 |
| `sms_tencent_secretid` / `sms_tencent_secretkey` | 腾讯云短信凭据，启用短信时必填 |
| `cache_redis_password` / `cache_redis_cipher_key` | Redis 口令与 SM4 密钥，启用 Redis 时必填 |
| `security_tc_c2s_*` / `security_tc_s2c_*` | 传参加密的 SM2/SM4 密钥，启用时必填 |

> 短信、Redis、传参加密默认关闭；未填写的配置项不会使用任何默认密钥。

## 安全说明

- 仓库中不包含任何真实 SM2/SM4 密钥、短信凭据或内部地址。
- `auth_admin_key` 为空时管理员口令登录入口直接拒绝，避免可预测口令。
- accessToken 使用 SM2 公钥加密，私钥仅在服务端；随机数使用一次后失效。
- 短信、Redis、传参加密默认关闭。
- 生产部署请通过环境变量或配置中心注入密钥，不要提交到 Git。

## 许可证

[Apache License 2.0](LICENSE)。第三方组件许可见 [NOTICE](NOTICE)。
