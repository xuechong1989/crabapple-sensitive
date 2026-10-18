
/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
DROP TABLE IF EXISTS `t_sensitive_word`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_sensitive_word` (
  `id_` bigint NOT NULL COMMENT '敏感词id',
  `keyword_` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs DEFAULT NULL COMMENT '关键词',
  `type_` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs DEFAULT NULL COMMENT '类别',
  `desc_` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id_`) USING BTREE,
  KEY `i_k` (`keyword_`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_as_cs ROW_FORMAT=DYNAMIC COMMENT='敏感词';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `t_properties`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_properties` (
  `id_` bigint NOT NULL COMMENT 'id',
  `name_` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs DEFAULT NULL COMMENT '名称',
  `alias_` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs DEFAULT NULL COMMENT '别名',
  `global_` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs DEFAULT NULL COMMENT '是否全局',
  `encrypt_` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs DEFAULT NULL COMMENT '是否储存加密',
  `value_` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs COMMENT '参数值',
  `category_` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs DEFAULT NULL COMMENT '分类',
  `description_` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs DEFAULT NULL COMMENT '描述',
  `create_by_` bigint DEFAULT NULL COMMENT '创建人',
  `create_time_` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs DEFAULT NULL COMMENT '创建时间',
  `update_by_` bigint DEFAULT NULL COMMENT '修改人',
  `update_time_` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id_`) USING BTREE,
  KEY `i_alias` (`alias_`) USING BTREE,
  KEY `i_name` (`name_`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_as_cs ROW_FORMAT=DYNAMIC COMMENT='系统参数表';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `t_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_user` (
  `id_` bigint NOT NULL COMMENT '用户id',
  `nike_name_` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs DEFAULT NULL COMMENT '用户昵称',
  `password_` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs DEFAULT NULL COMMENT '用户密码',
  `mobile_` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs DEFAULT NULL COMMENT '用户手机号',
  `user_no_` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs DEFAULT NULL COMMENT '用户账号',
  `mail_` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs DEFAULT NULL COMMENT '用户邮箱',
  `sex_` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs DEFAULT NULL COMMENT '用户性别',
  `age_` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs DEFAULT NULL COMMENT '用户年龄',
  `access_status_` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs DEFAULT NULL COMMENT '用户状态',
  `sensitivity_number_` int DEFAULT NULL COMMENT '出发敏感词次数',
  `appeal_stauts_` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs DEFAULT NULL COMMENT '申诉状态',
  `is_admin_` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs DEFAULT NULL COMMENT '是否是管理员用户',
  `create_time_` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs DEFAULT NULL COMMENT '创建时间',
  `login_count_` int NOT NULL COMMENT '登录失败次数',
  `openid_` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs DEFAULT NULL COMMENT '微信唯一标识',
  PRIMARY KEY (`id_`) USING BTREE,
  KEY `i_mail` (`mail_`) USING BTREE,
  KEY `i_mobile` (`mobile_`) USING BTREE,
  KEY `i_un` (`user_no_`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_as_cs ROW_FORMAT=DYNAMIC COMMENT='用户表';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `t_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `t_log` (
  `id_` bigint NOT NULL COMMENT '主键id',
  `module_` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs DEFAULT NULL COMMENT '功能模块',
  `sub_module_` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs DEFAULT NULL COMMENT '操作',
  `nike_name_` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs DEFAULT NULL COMMENT '用户昵称',
  `user_no_` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs DEFAULT NULL COMMENT '用户账号',
  `mobile_` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs DEFAULT NULL COMMENT '用户手机号',
  `content_` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs COMMENT '内容',
  `action_` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs COMMENT '接口',
  `ip_` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs DEFAULT NULL COMMENT '用户ip',
  `user_agent_` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs COMMENT '用户浏览器信息',
  `create_time_` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs DEFAULT NULL COMMENT '创建时间',
  `create_by_` bigint DEFAULT NULL COMMENT '创建人id',
  PRIMARY KEY (`id_`) USING BTREE,
  KEY `i_mobile` (`mobile_`) USING BTREE,
  KEY `i_module` (`module_`) USING BTREE,
  KEY `i_nn` (`nike_name_`) USING BTREE,
  KEY `i_sm` (`sub_module_`) USING BTREE,
  KEY `i_un` (`user_no_`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_as_cs ROW_FORMAT=DYNAMIC COMMENT='日志记录表';
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;


DROP TABLE IF EXISTS `t_auth`;
CREATE TABLE `t_auth` (
  `id_` bigint NOT NULL COMMENT '主键',
  `clientId_` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL COMMENT '客户端节点标识',
  `clientIp_` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs DEFAULT NULL COMMENT '客户端IP',
  PRIMARY KEY (`id_`) USING BTREE,
  UNIQUE KEY `i_clientId` (`clientId_`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_as_cs ROW_FORMAT=DYNAMIC COMMENT='客户端令牌';
