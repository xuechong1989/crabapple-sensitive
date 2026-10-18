# 敏感词库导出

- 导出时间：2026-10-18
- 来源库：MySQL 8.4（源库名不随仓库公开）
- 导出方式：`mysqldump` + `mysql --batch`

## 文件说明

| 文件 | 内容 | 是否随仓库分发 |
| --- | --- | --- |
| `schema.sql` | `t_sensitive_word` / `t_properties` / `t_user` / `t_log` / `t_auth` 建表语句，无数据 | 是 |
| `sensitive-words.txt` | 去重、去首尾空白后的关键词，110,855 行，可直接用于 `insertByTxt` | 是 |
| `sensitive-words.sql` | `t_sensitive_word` 全量原始数据（111,344 行，保留原始 `type_` / `desc_`） | 否，仅保留在本地 |

`export/sensitive-words.sql` 已被 `.gitignore` 忽略，不进入公开仓库；需要原始 SQL 时可在本地保留，或从 `sensitive-words.txt` 重新导入。

## 数据质量说明

- 原始 111,344 行中有 110,857 个不同关键词，存在 487 个重复。
- `type_` 存在 `其它\n关键词`、`A5包含…` 等历史脏值；TXT 只保留关键词。
- 5 个关键词包含 Unicode 段落分隔符（U+2029），TXT 中已替换为空格。
- `t_sensitive_word` 原始表没有命中次数字段；`sortByCount` 当前按关键词长度排序，不依赖命中次数字段。

## 导入

```bash
mysql -u root -p -e "CREATE DATABASE crabapple_sensitive DEFAULT CHARACTER SET utf8mb4;"
mysql -u root -p crabapple_sensitive < schema.sql

# 方式一：应用内“从文本导入”，将 sensitive-words.txt 放到启动目录的 data/sensitive-words.txt
# 方式二：自行把 TXT 转成 INSERT 语句导入 t_sensitive_word
```

> `schema.sql` 含 `DROP TABLE IF EXISTS`，只应在**新库**执行；不要直接导入已有业务库。

## 内容与合规

- 词表来自公开网络与项目历史积累，可能包含政治、暴力、色情、广告等类别；仅用于内容审核、过滤与测试。
- 公开或再分发前请自行评估当地法律法规与平台政策；如不希望包含某类词，请在导入前自行过滤。
- 本项目不对词表内容的完整性、准确性或适用性作保证；生产环境请结合业务审核标准维护自己的词库。
