# Crabapple Sensitive（海棠中文敏感词服务）

基于 [crabapple-commons](https://github.com/xuechong1989/crabapple-commons) 构建的**中文敏感词检测与词库管理服务**，面向中文评论、弹幕、文档、客服话术等内容的关键词过滤场景。

## 为什么是中文敏感词服务

- 词库和匹配逻辑以**中文关键词**为主，按连续文本扫描，不依赖中文分词；
- 适合“包含/命中哪些词”的规则过滤，不替代语义模型、人工审核或法律合规判断；
- 当前不做拼音、同音字、繁简转换、火星文/拆字变体归一化；需要覆盖变体时，请把变体作为独立词条加入词库；
- 也支持英文/数字关键词，但项目定位不是通用多语言 NLP 审核平台。

## 项目定位与适用范围

这是一个**专注中文关键词过滤的后端 HTTP 服务**，默认监听 `8081`，不是完整的内容审核平台，也不带管理后台 UI。

- 适合中文内容审核/评论过滤/文档检测系统的后端组件；
- 适合需要私有部署、自己维护中文词库的场景；
- 提供 JSON API，调用方可以是 Java/Go/Python/Node 等任意服务；
- 不做分词、语义理解、上下文判断、图片 OCR 或人工复核；
- 当前没有单元测试；生产使用前请补充集成测试和压测；
- 词库内容需自行评估合规性，公开词表不构成法律意见。

## 功能范围

- **中文敏感词检测**：对单条中文文本做连续扫描，返回命中的词条列表
- **中文词库管理**：新增、修改、删除、去重、TXT 批量导入、Excel 上传
- 短于 2 个字符的词条会被忽略；纯 2 位 ASCII 词条也不会进入匹配结构
- 客户端令牌：SM2 公钥加密生成 accessToken，随机数防重放
- 管理员口令登录：必须显式配置 `auth_admin_key`，未配置则禁用
- 操作日志分页
- 系统参数从 `t_properties` 读取
- 缓存：本地缓存默认开启，Redis 可选
- 安全能力：XSS / CSRF / 同源 / 限流 / 上传防护 / 传参加密（默认关闭）

## 技术栈

| 层级 | 技术 |
|---|---|
| 语言 | Java 21 |
| 框架 | Spring Boot 4.1 |
| 持久层 | MyBatis 3 + Druid |
| 缓存 | 本地缓存 / Redis（可选） |
| 国密 | SM2 / SM3 / SM4（crabapple-commons 内置） |
| 构建 | Maven 3.9+ |
| 基础库 | [crabapple-commons](https://github.com/xuechong1989/crabapple-commons) |

## 环境要求

- JDK 21
- Maven 3.9+
- MySQL 8.x
- 可选：Redis、腾讯云短信

## 部署步骤

### 1. 先安装基础库

```bash
git clone https://github.com/xuechong1989/crabapple-commons.git
cd crabapple-commons
mvn clean install
```

### 2. 初始化数据库

```bash
mysql -u root -p -e "CREATE DATABASE crabapple_sensitive DEFAULT CHARACTER SET utf8mb4;"
mysql -u root -p crabapple_sensitive < export/schema.sql
```

`export/schema.sql` 包含 `DROP TABLE IF EXISTS`，只应在新库执行，不要直接导入已有业务库。

表说明：

| 表 | 用途 |
|---|---|
| `t_sensitive_word` | 敏感词、类别、备注 |
| `t_auth` | 客户端节点与 accessToken 记录 |
| `t_user` | 后台用户 |
| `t_properties` | 系统参数（登录口令、SM2 密钥、导入路径） |
| `t_log` | 操作日志 |

### 3. 准备服务配置

```bash
cp src/main/resources/application.properties.example src/main/resources/application.properties
```

修改数据库连接：

```properties
db.url=jdbc:mysql://127.0.0.1:3306/crabapple_sensitive?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai
db.username=change-me
db.password=change-me
server.port=8081
```

`application.properties` 负责数据库、日志、缓存、短信、安全过滤等基础配置；`auth_admin_key`、SM2 密钥、词库导入路径等业务参数默认从数据库 `t_properties` 读取，`t_properties` 没有对应记录时会回退到 `application.properties`。建议把这些业务参数放在 `t_properties`，便于运行期修改和审计。

### 4. 写入 `t_properties` 初始参数

服务启动前，至少插入管理员登录口令；需要客户端令牌时再插入 SM2 密钥。

```sql
INSERT INTO t_properties
(id_, name_, alias_, global_, encrypt_, value_, category_, description_, create_by_, create_time_, update_by_, update_time_)
VALUES
(1, '管理员口令', 'auth_admin_key', 'yes', 'no', 'change-me-strong-key', 'auth', '管理员登录口令', 0, '2026-01-01 00:00:00', 0, '2026-01-01 00:00:00'),
(2, 'SM2公钥', 'auth_sm2_publickey', 'yes', 'no', '<SM2 public key>', 'auth', '客户端 accessToken 公钥', 0, '2026-01-01 00:00:00', 0, '2026-01-01 00:00:00'),
(3, 'SM2私钥', 'auth_sm2_privatekey', 'yes', 'no', '<SM2 private key>', 'auth', '仅保存在服务端', 0, '2026-01-01 00:00:00', 0, '2026-01-01 00:00:00'),
(4, '词库导入路径', 'sensitive_word_import_path', 'yes', 'no', './data/sensitive-words.txt', 'word', '相对启动目录的 TXT 路径', 0, '2026-01-01 00:00:00', 0, '2026-01-01 00:00:00');
```

`auth_admin_key` 留空时 `/auth/login` 会直接拒绝，避免出现万能口令。SM2 密钥可用 `crabapple-commons` 的 `Sm2Util.generateKeyMap()` 或标准 SM2 工具生成，私钥不要提交到 Git。

### 5. 导入词库

仓库提供去重后的 [export/sensitive-words.txt](export/sensitive-words.txt)（110,855 行）。两种导入方式：

**方式 A：TXT 批量导入（推荐）**

把文件放到应用启动目录下：

```bash
mkdir -p data
cp export/sensitive-words.txt data/sensitive-words.txt
```

启动服务后，先登录获取 token，再调用：

```bash
curl -X POST http://127.0.0.1:8081/api/crabapple/sensitive/sensitiveWord/insertByTxt \
  -b cookies.txt
```

路径由 `t_properties.sensitive_word_import_path` 控制，默认 `./data/sensitive-words.txt`。

**方式 B：上传 Excel**

`POST /api/crabapple/sensitive/sensitiveWord/upload` 接收 `xlsx` / `xls` 文件。代码按首行后逐行读取，关键词取第 2 列（索引 1），行至少需要 4 列；建议先小批量验证格式。

> `export/sensitive-words.sql`（6.3MB 原始 mysqldump）不随公开仓库分发，已被 `.gitignore` 忽略；需要原始 SQL 时请在本地保留或从 TXT 重新导入。

### 6. 构建并启动

```bash
cd crabapple-sensitive
mvn clean package
java -jar target/crabapple-sensitive-21-4.2.00.jar
```

开发模式：

```bash
mvn spring-boot:run
```

默认端口 `8081`。

## 认证与接口调用

**除 `/api/crabapple/sensitive/auth/login` 外，所有接口都要求有效登录 token。** 登录成功后 token 会写入 cookie，并出现在响应 `S2cDto` 的 `token` 参数中；后续请求可带 cookie，也可带 `token` 请求参数。

### 管理员登录

`auth_admin_key` 必须在 `t_properties` 中配置，且请求参数名为 `key`：

```bash
curl -c cookies.txt -X POST http://127.0.0.1:8081/api/crabapple/sensitive/auth/login \
  --data-urlencode 'key=change-me-strong-key'
```

响应中的 `token` 可用于后续请求；`-c cookies.txt` 会把 cookie 保存下来。

### 敏感词检测

请求参数名是 **`question`**，不是 `text`：

```bash
curl -b cookies.txt -X POST http://127.0.0.1:8081/api/crabapple/sensitive/sensitiveWord/check \
  --data-urlencode 'question=这里是要检测的文本'
```

返回 `sensitiveList`（命中的敏感词），未命中时为空。

### 词库 CRUD

`insert` / `update` / `delete` 的请求参数是 `text`（JSON 字符串）：

```bash
# 新增：text 是 JSON
curl -b cookies.txt -X POST http://127.0.0.1:8081/api/crabapple/sensitive/sensitiveWord/insert \
  --data-urlencode 'text={"keyword":"示例词","type":"其它关键词","desc":"备注"}'

# 修改：text 至少包含 id
curl -b cookies.txt -X POST http://127.0.0.1:8081/api/crabapple/sensitive/sensitiveWord/update \
  --data-urlencode 'text={"id":123456789,"keyword":"新词","type":"其它关键词","desc":"备注"}'

# 删除：text 里的 ids 是逗号分隔的 ID
curl -b cookies.txt -X POST http://127.0.0.1:8081/api/crabapple/sensitive/sensitiveWord/delete \
  --data-urlencode 'text={"ids":"123456789,987654321"}'
```

分页查询使用 form 参数 `keyword`、`type`、`desc`、`startIndex`、`pageSize`：

```bash
curl -b cookies.txt -X POST http://127.0.0.1:8081/api/crabapple/sensitive/sensitiveWord/getList \
  --data-urlencode 'keyword=' \
  --data-urlencode 'startIndex=0' \
  --data-urlencode 'pageSize=20'
```

`sortByCount` 当前按关键词长度排序，不是按命中次数；结果字段以实际响应为准。

### 主要接口

| 接口 | 说明 |
|---|---|
| `POST /api/crabapple/sensitive/auth/login` | 管理员口令登录（`key`） |
| `POST /api/crabapple/sensitive/auth/generate` | 为客户端节点生成 accessToken（需登录） |
| `POST /api/crabapple/sensitive/auth/getList` | 客户端节点列表（需登录） |
| `POST /api/crabapple/sensitive/sensitiveWord/check` | 检测（`question`） |
| `POST /api/crabapple/sensitive/sensitiveWord/insert` | 新增（`text` JSON） |
| `POST /api/crabapple/sensitive/sensitiveWord/update` | 修改（`text` JSON） |
| `POST /api/crabapple/sensitive/sensitiveWord/delete` | 删除（`text.ids`） |
| `POST /api/crabapple/sensitive/sensitiveWord/deleteRepeatKeyword` | 去重（需登录） |
| `POST /api/crabapple/sensitive/sensitiveWord/insertByTxt` | 从 TXT 批量导入（需登录） |
| `POST /api/crabapple/sensitive/sensitiveWord/upload` | 上传 xlsx/xls（需登录） |
| `POST /api/crabapple/sensitive/sensitiveWord/getList` | 词库分页（需登录） |
| `POST /api/crabapple/sensitive/sensitiveWord/sortByCount` | 按关键词长度排序（需登录） |
| `POST /api/crabapple/sensitive/log/getList` | 操作日志（需登录） |

请求/响应统一使用 `S2cDto` 包装。

## 配置说明

> 配置读取顺序：`t_properties` 中 `alias_` 命中且值非空时优先；没有记录或值为空时回退到 `application.properties`。管理员口令、SM2 密钥、词库导入路径建议放 `t_properties`。

`application.properties` 中的主要项：

| 配置 | 说明 |
|---|---|
| `server.port` | 服务端口，默认 `8081` |
| `db.type` / `db.url` / `db.username` / `db.password` | 数据库连接 |
| `db.package` | Mapper 接口包，`xc.crabapple.sensitive.module` |
| `db.xml.url` | Mapper XML 路径，`xc/crabapple/sensitive/module/**/*.xml` |
| `log.file.path` | 日志目录，默认 `./logs` |
| `cache_redis_enable` | 是否启用 Redis，默认 `no` |
| `cache_redis_password` / `cache_redis_cipher_key` | Redis 口令与 SM4 密钥 |
| `sms_tencent_*` | 腾讯云短信配置，默认关闭 |
| `security_crossdomain_value` | 允许的跨域来源，示例为 `http://localhost:8081` |
| `security_xss_enable` / `security_csrf_enable` / `security_sameorigin_enable` | 安全开关，默认关闭 |

业务参数在 `t_properties`：

| alias_ | 说明 |
|---|---|
| `auth_admin_key` | 管理员登录口令；空值禁用登录 |
| `auth_sm2_publickey` / `auth_sm2_privatekey` | 客户端令牌 SM2 密钥对 |
| `auth_client_id` / `auth_client_ip` | 生成 accessToken 的客户端标识/IP |
| `sensitive_word_import_path` | TXT 导入路径，默认 `./data/sensitive-words.txt` |
| `loginTimeLimited` / `lastReqTimeLimited` | 登录/空闲超时秒数 |

## 生产部署与安全

- 必须修改 `auth_admin_key`，不要使用示例值；SM2 私钥只放在服务端。
- 敏感词接口默认需要登录 token；仍建议放在内网/网关后，增加 IP 白名单、TLS、限流和审计。
- `security_crossdomain_value` 不要设为 `*`；按实际调用方域名配置。
- 词库内容可能包含政治、暴力、色情等类别；公开/再分发前请自行评估当地法律法规与平台政策。
- 词库与数据库纳入备份；升级前备份 `t_properties`、`t_sensitive_word`、`t_auth`。
- `export/schema.sql` 含 `DROP TABLE`，只用于新库。
- 当前没有单元测试；上线前请补充导入、检测、权限和并发测试。

## 测试与限制

```bash
mvn clean package
```

- `src/test` 为空，CI 只做编译/打包。
- `sortByCount` 按关键词长度排序，不统计命中次数。
- 词表为公开网络与项目历史积累，准确性和完整性不作保证。
- 默认不带管理 UI，需要自行开发调用端。

## 许可证

[Apache License 2.0](LICENSE)。第三方组件许可见 [NOTICE](NOTICE)；敏感词表使用前请阅读 [export/README.md](export/README.md) 的合规说明。
