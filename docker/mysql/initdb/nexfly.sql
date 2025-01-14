-- MySQL dump 10.13  Distrib 8.1.0, for Win64 (x86_64)
--
-- Host: localhost    Database: nexfly
-- ------------------------------------------------------
-- Server version	8.0.39

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

--
-- Table structure for table `account`
--

DROP TABLE IF EXISTS `account`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `account` (
                           `account_id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
                           `username` varchar(100) COLLATE utf8mb4_general_ci NOT NULL,
                           `nickname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
                           `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
                           `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
                           `password_salt` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
                           `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
                           `language` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
                           `theme` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
                           `timezone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
                           `last_login_time` timestamp NULL DEFAULT NULL,
                           `last_login_ip` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
                           `status` int NOT NULL DEFAULT '1' COMMENT '1正常、0禁用',
                           `create_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                           `update_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                           `create_by` bigint NOT NULL,
                           `update_by` bigint NOT NULL,
                           PRIMARY KEY (`account_id`),
                           UNIQUE KEY `account_UN` (`username`),
                           UNIQUE KEY `account_email_IDX` (`email`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='登录账号表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `account`
--

LOCK TABLES `account` WRITE;
/*!40000 ALTER TABLE `account` DISABLE KEYS */;
INSERT INTO `account` VALUES (1,'wangjun','王军','wjun_java@163.com','$2a$10$wb7/PQN5HluZA6a3qXDxzu0yr9s4EQN/dXqYT3D6CyL.2gT1yszZa','1','http://gips3.baidu.com/it/u=3886271102,3123389489&fm=3028&app=3028&f=JPEG&fmt=auto?w=1280&h=960','Chinese','Bright','UTC+8	Asia/Shanghai',NULL,NULL,1,'2024-07-30 03:41:27','2024-07-30 03:41:27',0,0),(3,'admin','admin',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,'2025-01-02 02:59:23','2025-01-02 02:59:23',0,0);
/*!40000 ALTER TABLE `account` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `account_openid`
--

DROP TABLE IF EXISTS `account_openid`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `account_openid` (
                                  `openid_id` bigint unsigned NOT NULL AUTO_INCREMENT,
                                  `openid` varchar(100) COLLATE utf8mb4_general_ci NOT NULL,
                                  `client_id` varchar(100) COLLATE utf8mb4_general_ci NOT NULL,
                                  `username` varchar(100) COLLATE utf8mb4_general_ci NOT NULL,
                                  `create_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
                                  `update_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
                                  PRIMARY KEY (`openid_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `account_openid`
--

LOCK TABLES `account_openid` WRITE;
/*!40000 ALTER TABLE `account_openid` DISABLE KEYS */;
INSERT INTO `account_openid` VALUES (1,'1068898686148415488','nexfly-client','admin','2025-01-02 02:59:25','2025-01-02 02:59:25');
/*!40000 ALTER TABLE `account_openid` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `account_org`
--

DROP TABLE IF EXISTS `account_org`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `account_org` (
                               `account_id` bigint NOT NULL,
                               `org_id` bigint NOT NULL,
                               `status` int NOT NULL DEFAULT '1' COMMENT '当前使用的组织机构',
                               PRIMARY KEY (`account_id`,`org_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `account_org`
--

LOCK TABLES `account_org` WRITE;
/*!40000 ALTER TABLE `account_org` DISABLE KEYS */;
INSERT INTO `account_org` VALUES (1,1,1),(1,2,0);
/*!40000 ALTER TABLE `account_org` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `app`
--

DROP TABLE IF EXISTS `app`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `app` (
                       `app_id` bigint unsigned NOT NULL AUTO_INCREMENT,
                       `org_id` bigint NOT NULL,
                       `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                       `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                       `app_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                       `status` int NOT NULL DEFAULT '1',
                       `icon` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                       `flow_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                       `create_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
                       `update_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
                       `create_by` bigint NOT NULL,
                       `update_by` bigint NOT NULL,
                       PRIMARY KEY (`app_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `app`
--

LOCK TABLES `app` WRITE;
/*!40000 ALTER TABLE `app` DISABLE KEYS */;
INSERT INTO `app` VALUES (1,1,'fastcms','fastcms','chat',1,'',NULL,'2024-08-27 00:58:56','2024-08-27 00:58:56',1,1);
/*!40000 ALTER TABLE `app` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `app_config`
--

DROP TABLE IF EXISTS `app_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `app_config` (
                              `config_id` bigint unsigned NOT NULL AUTO_INCREMENT,
                              `app_id` bigint NOT NULL,
                              `org_id` bigint NOT NULL,
                              `pre_prompt` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '应用角色设定词',
                              `form_variable` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '变量配置',
                              `empty_response` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '空回复',
                              `prologue` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '开场白',
                              `file_upload` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
                              `text_to_speech` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
                              `speech_to_text` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
                              `quote` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '0' COMMENT '是否显示引用',
                              `similarity_threshold` float DEFAULT NULL,
                              `top_n` int DEFAULT NULL,
                              `vector_similarity_weight` float DEFAULT NULL,
                              `create_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
                              `create_by` bigint DEFAULT NULL,
                              `update_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
                              `update_by` bigint DEFAULT NULL,
                              PRIMARY KEY (`config_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `app_config`
--

LOCK TABLES `app_config` WRITE;
/*!40000 ALTER TABLE `app_config` DISABLE KEYS */;
INSERT INTO `app_config` VALUES (1,1,1,'你是一个基于fastcms开源项目的开发高手，你会开发fastcms模板，插件，以及熟悉fastcms系统的各种配置，fastcms是一个由java，springboot框架开发的插件化架构的开源项目，其github开源地址是：https://github.com/my-fastcms/fastcms.git','[{\"key\":\"question_answer_context\",\"optional\":true}]','抱歉，暂时没有找到相关数据','我是你的fastcms小助手，请问有什么可以帮到您的？',NULL,NULL,NULL,'1',0.48,6,0.3,'2024-08-27 01:00:45',1,'2024-08-27 01:00:45',1);
/*!40000 ALTER TABLE `app_config` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `app_conversation`
--

DROP TABLE IF EXISTS `app_conversation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `app_conversation` (
                                    `conversation_id` bigint unsigned NOT NULL AUTO_INCREMENT,
                                    `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                                    `app_id` bigint NOT NULL,
                                    `create_by` bigint NOT NULL,
                                    `create_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
                                    `update_by` bigint NOT NULL,
                                    `update_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
                                    PRIMARY KEY (`conversation_id`)
) ENGINE=InnoDB AUTO_INCREMENT=76 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='会话表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `app_dataset`
--

DROP TABLE IF EXISTS `app_dataset`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `app_dataset` (
                               `app_id` bigint NOT NULL,
                               `dataset_id` bigint NOT NULL,
                               PRIMARY KEY (`app_id`,`dataset_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `app_dataset`
--

LOCK TABLES `app_dataset` WRITE;
/*!40000 ALTER TABLE `app_dataset` DISABLE KEYS */;
INSERT INTO `app_dataset` VALUES (1,1);
/*!40000 ALTER TABLE `app_dataset` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `app_message`
--

DROP TABLE IF EXISTS `app_message`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `app_message` (
                               `message_id` bigint unsigned NOT NULL AUTO_INCREMENT,
                               `conversation_id` bigint NOT NULL,
                               `app_id` bigint DEFAULT NULL,
                               `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'ai回复',
                               `role` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '消息类型，user，assistant',
                               `document_ids` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                               `create_by` bigint NOT NULL,
                               `create_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
                               `update_by` bigint NOT NULL,
                               `update_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
                               PRIMARY KEY (`message_id`)
) ENGINE=InnoDB AUTO_INCREMENT=458 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `app_model`
--

DROP TABLE IF EXISTS `app_model`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `app_model` (
                             `app_model_id` bigint unsigned NOT NULL AUTO_INCREMENT,
                             `app_id` bigint NOT NULL,
                             `model_id` bigint NOT NULL COMMENT '模型id',
                             `model_config` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT 'app模型参数配置',
                             `create_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
                             `create_by` bigint DEFAULT NULL,
                             `update_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
                             `update_by` bigint DEFAULT NULL,
                             PRIMARY KEY (`app_model_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `app_model`
--

LOCK TABLES `app_model` WRITE;
/*!40000 ALTER TABLE `app_model` DISABLE KEYS */;
INSERT INTO `app_model` VALUES (1,1,14,'{\"frequencyPenalty\":0.26,\"maxTokens\":4077,\"presencePenalty\":0.33,\"temperature\":0.1,\"topP\":0.74}','2024-08-26 13:47:47',1,'2024-08-26 13:47:47',1),(2,1,13,'{\"frequencyPenalty\":0.7,\"maxTokens\":4096,\"presencePenalty\":0.4,\"temperature\":0.1,\"topP\":0.3}','2024-11-17 09:31:42',1,'2024-11-17 09:31:42',1);
/*!40000 ALTER TABLE `app_model` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `attachment`
--

DROP TABLE IF EXISTS `attachment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `attachment` (
                              `attachment_id` bigint unsigned NOT NULL AUTO_INCREMENT,
                              `file_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                              `org_id` bigint NOT NULL,
                              `path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                              `size` bigint NOT NULL,
                              `type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                              `create_by` bigint NOT NULL,
                              `create_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
                              `update_by` bigint NOT NULL,
                              `update_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
                              PRIMARY KEY (`attachment_id`)
) ENGINE=InnoDB AUTO_INCREMENT=67 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Table structure for table `dataset`
--

DROP TABLE IF EXISTS `dataset`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `dataset` (
                           `dataset_id` bigint unsigned NOT NULL AUTO_INCREMENT,
                           `org_id` bigint NOT NULL,
                           `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                           `description` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                           `embed_model_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                           `vs_index_node_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '向量数据库className',
                           `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                           `language` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                           `parser_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                           `parser_config` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
                           `status` int NOT NULL DEFAULT '1',
                           `create_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
                           `create_by` bigint NOT NULL,
                           `update_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
                           `update_by` bigint NOT NULL,
                           PRIMARY KEY (`dataset_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dataset`
--

LOCK TABLES `dataset` WRITE;
/*!40000 ALTER TABLE `dataset` DISABLE KEYS */;
INSERT INTO `dataset` VALUES (1,1,'fastcms','fastcms数据集','Tongyi-Qianwen@text-embedding-v2@embedding','Class78e1fcb83c854cf99230b0d76de96012','','Chinese','naive','{\"raptor\":{},\"chunk_token_num\":128,\"delimiter\":\"\\\\n!?;。；！？\",\"layout_recognize\":true}',1,'2024-08-25 09:42:48',1,'2024-08-25 09:42:48',1);
/*!40000 ALTER TABLE `dataset` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `document`
--

DROP TABLE IF EXISTS `document`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `document` (
                            `document_id` bigint unsigned NOT NULL AUTO_INCREMENT,
                            `org_id` bigint NOT NULL,
                            `dataset_id` bigint NOT NULL,
                            `file_id` bigint NOT NULL COMMENT '关联文件',
                            `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                            `data_source` int NOT NULL COMMENT '1、本地上传，2、网络爬虫',
                            `word_count` int DEFAULT NULL,
                            `token_count` int DEFAULT NULL,
                            `data_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '网页url地址',
                            `process_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '1' COMMENT '文档处理方式',
                            `process_status` int NOT NULL DEFAULT '0' COMMENT '文档处理状态0，1，2，3',
                            `status` int NOT NULL DEFAULT '1',
                            `create_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
                            `create_by` bigint NOT NULL,
                            `update_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
                            `update_by` bigint DEFAULT NULL,
                            PRIMARY KEY (`document_id`)
) ENGINE=InnoDB AUTO_INCREMENT=50 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Table structure for table `document_segment`
--

DROP TABLE IF EXISTS `document_segment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `document_segment` (
                                    `segment_id` bigint unsigned NOT NULL AUTO_INCREMENT,
                                    `document_id` bigint NOT NULL,
                                    `org_id` bigint NOT NULL,
                                    `dataset_id` bigint NOT NULL,
                                    `position` int DEFAULT NULL,
                                    `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '分片后的文档内容',
                                    `content_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '分片后的文档id',
                                    `word_count` int DEFAULT NULL,
                                    `token_count` int DEFAULT NULL,
                                    `keywords` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
                                    `status` int DEFAULT '1',
                                    `hit_count` int DEFAULT NULL,
                                    `create_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
                                    `create_by` bigint NOT NULL,
                                    `update_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
                                    `update_by` bigint DEFAULT NULL,
                                    PRIMARY KEY (`segment_id`)
) ENGINE=InnoDB AUTO_INCREMENT=190 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `organization`
--

DROP TABLE IF EXISTS `organization`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `organization` (
                                `org_id` bigint unsigned NOT NULL AUTO_INCREMENT,
                                `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                                `status` int DEFAULT '1' COMMENT '1正常、0禁用',
                                `create_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                `update_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                `create_by` bigint NOT NULL DEFAULT '0',
                                `update_by` bigint NOT NULL DEFAULT '0',
                                PRIMARY KEY (`org_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='组织机构表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `organization`
--

LOCK TABLES `organization` WRITE;
/*!40000 ALTER TABLE `organization` DISABLE KEYS */;
INSERT INTO `organization` VALUES (1,'我的组织',1,'2024-08-06 02:36:24','2024-08-06 02:36:24',0,0),(2,'我的团队',1,'2024-08-11 13:33:22','2024-08-11 13:33:22',0,0);
/*!40000 ALTER TABLE `organization` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `provider`
--

DROP TABLE IF EXISTS `provider`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `provider` (
                            `provider_id` bigint unsigned NOT NULL AUTO_INCREMENT,
                            `org_id` bigint NOT NULL COMMENT '组织机构id',
                            `provider_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                            `api_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                            `api_url` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
                            `create_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
                            `update_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
                            `create_by` bigint NOT NULL,
                            `update_by` bigint NOT NULL,
                            PRIMARY KEY (`provider_id`),
                            KEY `provider_org_id_IDX` (`org_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='大语言模型提供者';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `provider`
--

LOCK TABLES `provider` WRITE;
/*!40000 ALTER TABLE `provider` DISABLE KEYS */;
INSERT INTO `provider` VALUES (5,1,'Tongyi-Qianwen','sk-6ddaa19b67a345528f40ecc5e6c2373b',NULL,'2024-10-20 13:11:03','2024-10-20 13:11:03',1,1),(6,1,'Ollama',NULL,'http://localhost:11434/','2024-10-21 03:13:30','2024-10-21 03:13:30',1,1);
/*!40000 ALTER TABLE `provider` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `provider_model`
--

DROP TABLE IF EXISTS `provider_model`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `provider_model` (
                                  `model_id` bigint unsigned NOT NULL AUTO_INCREMENT,
                                  `org_id` bigint NOT NULL COMMENT '组织',
                                  `provider_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '模型提供商',
                                  `model_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '大模型名称',
                                  `model_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '模型类型对应spring的ModelFactory的bean name',
                                  `create_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                  `update_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                  `create_by` bigint NOT NULL,
                                  `update_by` bigint NOT NULL,
                                  PRIMARY KEY (`model_id`),
                                  UNIQUE KEY `provider_model_un` (`provider_name`,`model_name`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='模型表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `provider_model`
--

LOCK TABLES `provider_model` WRITE;
/*!40000 ALTER TABLE `provider_model` DISABLE KEYS */;
INSERT INTO `provider_model` VALUES (12,1,'Tongyi-Qianwen','qwen-plus','chat','2024-10-20 13:11:03','2024-10-20 13:11:03',1,1),(13,1,'Tongyi-Qianwen','qwen-turbo','chat','2024-10-20 13:11:03','2024-10-20 13:11:03',1,1),(14,1,'Tongyi-Qianwen','qwen-vl-plus','image2text','2024-10-20 13:11:03','2024-10-20 13:11:03',1,1),(15,1,'Tongyi-Qianwen','sambert-zhide-v1','tts','2024-10-20 13:11:03','2024-10-20 13:11:03',1,1),(16,1,'Tongyi-Qianwen','sambert-zhiru-v1','tts','2024-10-20 13:11:03','2024-10-20 13:11:03',1,1),(17,1,'Tongyi-Qianwen','text-embedding-v2','embedding','2024-10-20 13:11:03','2024-10-20 13:11:03',1,1),(18,1,'Tongyi-Qianwen','text-embedding-v3','embedding','2024-10-20 13:11:03','2024-10-20 13:11:03',1,1),(19,1,'Ollama','llama3.1','chat','2024-10-21 03:13:30','2024-10-21 03:13:30',1,1),(20,1,'Tongyi-Qianwen','qwen-vl-max','chat','2024-11-17 09:31:42','2024-11-17 09:31:42',1,1);
/*!40000 ALTER TABLE `provider_model` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_provider`
--

DROP TABLE IF EXISTS `sys_provider`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_provider` (
                                `sys_provider_id` bigint unsigned NOT NULL AUTO_INCREMENT,
                                `name` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL,
                                `logo` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
                                `tags` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
                                `status` int DEFAULT NULL,
                                `create_at` timestamp NULL DEFAULT NULL,
                                `update_at` timestamp NULL DEFAULT NULL,
                                `create_by` bigint NOT NULL DEFAULT '0',
                                `update_by` bigint NOT NULL DEFAULT '0',
                                PRIMARY KEY (`sys_provider_id`)
) ENGINE=InnoDB AUTO_INCREMENT=43 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_provider`
--

LOCK TABLES `sys_provider` WRITE;
/*!40000 ALTER TABLE `sys_provider` DISABLE KEYS */;
INSERT INTO `sys_provider` VALUES (1,'01.AI','','LLM,IMAGE2TEXT',0,'2024-10-20 03:45:43','2024-10-20 03:45:43',0,0),(2,'Anthropic','','LLM',1,'2024-10-20 03:45:43','2024-10-20 03:45:43',0,0),(3,'Azure-OpenAI','','LLM,TEXT EMBEDDING,SPEECH2TEXT,MODERATION',1,'2024-10-20 03:45:43','2024-10-20 03:45:43',0,0),(4,'BAAI','','TEXT EMBEDDING, TEXT RE-RANK',1,'2024-10-20 03:45:43','2024-10-20 03:45:43',0,0),(5,'BaiChuan','','LLM,TEXT EMBEDDING',1,'2024-10-20 03:45:43','2024-10-20 03:45:43',0,0),(6,'BaiduYiyan','','LLM',1,'2024-10-20 03:45:43','2024-10-20 03:45:43',0,0),(7,'Bedrock','','LLM,TEXT EMBEDDING',1,'2024-10-20 03:45:43','2024-10-20 03:45:43',0,0),(8,'cohere','','LLM,TEXT EMBEDDING, TEXT RE-RANK',1,'2024-10-20 03:45:43','2024-10-20 03:45:43',0,0),(9,'DeepSeek','','LLM',1,'2024-10-20 03:45:43','2024-10-20 03:45:43',0,0),(10,'FastEmbed','','TEXT EMBEDDING',1,'2024-10-20 03:45:43','2024-10-20 03:45:43',0,0),(11,'Fish Audio','','TTS',1,'2024-10-20 03:45:43','2024-10-20 03:45:43',0,0),(12,'Gemini','','LLM,TEXT EMBEDDING,IMAGE2TEXT',1,'2024-10-20 03:45:43','2024-10-20 03:45:43',0,0),(13,'Google Cloud','','LLM',1,'2024-10-20 03:45:43','2024-10-20 03:45:43',0,0),(14,'Groq','','LLM',1,'2024-10-20 03:45:43','2024-10-20 03:45:43',0,0),(15,'Jina','','TEXT EMBEDDING, TEXT RE-RANK',1,'2024-10-20 03:45:43','2024-10-20 03:45:43',0,0),(16,'LeptonAI','','LLM',1,'2024-10-20 03:45:43','2024-10-20 03:45:43',0,0),(17,'LM-Studio','','LLM,TEXT EMBEDDING,IMAGE2TEXT',1,'2024-10-20 03:45:43','2024-10-20 03:45:43',0,0),(18,'LocalAI','','LLM,TEXT EMBEDDING,SPEECH2TEXT,MODERATION',1,'2024-10-20 03:45:43','2024-10-20 03:45:43',0,0),(19,'MiniMax','','LLM,TEXT EMBEDDING',1,'2024-10-20 03:45:43','2024-10-20 03:45:43',0,0),(20,'Mistral','','LLM,TEXT EMBEDDING',1,'2024-10-20 03:45:43','2024-10-20 03:45:43',0,0),(21,'Moonshot','','LLM,TEXT EMBEDDING',1,'2024-10-20 03:45:43','2024-10-20 03:45:43',0,0),(22,'novita.ai','','LLM',1,'2024-10-20 03:45:43','2024-10-20 03:45:43',0,0),(23,'NVIDIA','','LLM,TEXT EMBEDDING, TEXT RE-RANK',1,'2024-10-20 03:45:43','2024-10-20 03:45:43',0,0),(24,'Ollama','','LLM,TEXT EMBEDDING,SPEECH2TEXT,MODERATION',1,'2024-10-20 03:45:43','2024-10-20 03:45:43',0,0),(25,'OpenAI','','LLM,TEXT EMBEDDING,SPEECH2TEXT,MODERATION',1,'2024-10-20 03:45:43','2024-10-20 03:45:43',0,0),(26,'OpenAI-API-Compatible','','LLM,TEXT EMBEDDING,SPEECH2TEXT,MODERATION',1,'2024-10-20 03:45:43','2024-10-20 03:45:43',0,0),(27,'OpenRouter','','LLM,IMAGE2TEXT',1,'2024-10-20 03:45:43','2024-10-20 03:45:43',0,0),(28,'PerfXCloud','','LLM,TEXT EMBEDDING',1,'2024-10-20 03:45:43','2024-10-20 03:45:43',0,0),(29,'Replicate','','LLM,TEXT EMBEDDING',1,'2024-10-20 03:45:43','2024-10-20 03:45:43',0,0),(30,'SILICONFLOW','','LLM,TEXT EMBEDDING,TEXT RE-RANK',1,'2024-10-20 03:45:43','2024-10-20 03:45:43',0,0),(31,'StepFun','','LLM',1,'2024-10-20 03:45:43','2024-10-20 03:45:43',0,0),(32,'Tencent Cloud','','SPEECH2TEXT',1,'2024-10-20 03:45:43','2024-10-20 03:45:43',0,0),(33,'Tencent Hunyuan','','LLM,IMAGE2TEXT',1,'2024-10-20 03:45:43','2024-10-20 03:45:43',0,0),(34,'TogetherAI','','LLM,TEXT EMBEDDING,IMAGE2TEXT',1,'2024-10-20 03:45:43','2024-10-20 03:45:43',0,0),(35,'Tongyi-Qianwen','','LLM,TEXT EMBEDDING,SPEECH2TEXT,MODERATION',1,'2024-10-20 03:45:43','2024-10-20 03:45:43',0,0),(36,'Upstage','','LLM,TEXT EMBEDDING',1,'2024-10-20 03:45:43','2024-10-20 03:45:43',0,0),(37,'VolcEngine','','LLM, TEXT EMBEDDING',1,'2024-10-20 03:45:43','2024-10-20 03:45:43',0,0),(38,'Voyage AI','','TEXT EMBEDDING, TEXT RE-RANK',1,'2024-10-20 03:45:43','2024-10-20 03:45:43',0,0),(39,'Xinference','','LLM,TEXT EMBEDDING,SPEECH2TEXT,MODERATION,TEXT RE-RANK',1,'2024-10-20 03:45:43','2024-10-20 03:45:43',0,0),(40,'XunFei Spark','','LLM',1,'2024-10-20 03:45:43','2024-10-20 03:45:43',0,0),(41,'Youdao','','LLM,TEXT EMBEDDING,SPEECH2TEXT,MODERATION',0,'2024-10-20 03:45:43','2024-10-20 03:45:43',0,0),(42,'ZHIPU-AI','','LLM,TEXT EMBEDDING,SPEECH2TEXT,MODERATION',1,'2024-10-20 03:45:43','2024-10-20 03:45:43',0,0);
/*!40000 ALTER TABLE `sys_provider` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_provider_model`
--

DROP TABLE IF EXISTS `sys_provider_model`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_provider_model` (
                                      `sys_provider_model_id` bigint unsigned NOT NULL AUTO_INCREMENT,
                                      `model_name` varchar(128) COLLATE utf8mb4_general_ci DEFAULT NULL,
                                      `model_type` varchar(128) COLLATE utf8mb4_general_ci DEFAULT NULL,
                                      `provider_name` varchar(128) COLLATE utf8mb4_general_ci DEFAULT NULL,
                                      `max_tokens` int DEFAULT NULL,
                                      `tags` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
                                      `status` varchar(1) COLLATE utf8mb4_general_ci DEFAULT NULL,
                                      `create_at` timestamp NULL DEFAULT NULL,
                                      `update_at` timestamp NULL DEFAULT NULL,
                                      `create_by` bigint NOT NULL DEFAULT '0',
                                      `update_by` bigint NOT NULL DEFAULT '0',
                                      PRIMARY KEY (`sys_provider_model_id`)
) ENGINE=InnoDB AUTO_INCREMENT=509 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_provider_model`
--

LOCK TABLES `sys_provider_model` WRITE;
/*!40000 ALTER TABLE `sys_provider_model` DISABLE KEYS */;
INSERT INTO `sys_provider_model` VALUES (1,'01-ai/Yi-1.5-34B-Chat-16K','chat','SILICONFLOW',16384,'LLM,CHAT,16k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(2,'01-ai/Yi-1.5-6B-Chat','chat','SILICONFLOW',4096,'LLM,CHAT,4k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(3,'01-ai/Yi-1.5-9B-Chat-16K','chat','SILICONFLOW',16384,'LLM,CHAT,16k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(4,'01-ai/yi-34b','chat','OpenRouter',4096,'LLM CHAT 4K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(5,'01-ai/yi-34b-200k','chat','OpenRouter',200000,'LLM CHAT 195K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(6,'01-ai/yi-34b-chat','chat','OpenRouter',4096,'LLM CHAT 4K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(7,'01-ai/yi-6b','chat','OpenRouter',4096,'LLM CHAT 4K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(8,'01-ai/yi-large','chat','OpenRouter',32768,'LLM CHAT 32K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(9,'abab5.5s-chat','chat','MiniMax',8192,'LLM,CHAT,8k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(10,'abab6.5-chat','chat','MiniMax',8192,'LLM,CHAT,8k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(11,'abab6.5g-chat','chat','MiniMax',8192,'LLM,CHAT,8k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(12,'abab6.5s-chat','chat','MiniMax',245760,'LLM,CHAT,245k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(13,'abab6.5t-chat','chat','MiniMax',8192,'LLM,CHAT,8k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(14,'adept/fuyu-8b','image2text','NVIDIA',4096,'LLM,IMAGE2TEXT,4K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(15,'ai21.j2-mid-v1','chat','Bedrock',8191,'LLM,CHAT,8k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(16,'ai21.j2-ultra-v1','chat','Bedrock',8191,'LLM,CHAT,8k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(17,'ai21/jamba-instruct','chat','OpenRouter',256000,'LLM CHAT 250K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(18,'aisingapore/sea-lion-7b-instruct','chat','NVIDIA',4096,'LLM,CHAT,4K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(19,'allenai/olmo-7b-instruct','chat','OpenRouter',2048,'LLM CHAT 2K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(20,'alpindale/goliath-120b','chat','OpenRouter',6144,'LLM CHAT 6K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(21,'alpindale/magnum-72b','chat','OpenRouter',16384,'LLM CHAT 16K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(22,'amazon.titan-embed-text-v2:0','embedding','Bedrock',8192,'TEXT EMBEDDING','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(23,'amazon.titan-text-express-v1','chat','Bedrock',8192,'LLM,CHAT,8k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(24,'amazon.titan-text-lite-v1','chat','Bedrock',4096,'LLM,CHAT,4k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(25,'amazon.titan-text-premier-v1:0','chat','Bedrock',32768,'LLM,CHAT,32k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(26,'anthropic.claude-3-5-sonnet-20240620-v1:0','chat','Bedrock',204800,'LLM,CHAT,200k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(27,'anthropic.claude-3-haiku-20240307-v1:0','chat','Bedrock',204800,'LLM,CHAT,200k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(28,'anthropic.claude-3-opus-20240229-v1:0','chat','Bedrock',204800,'LLM,CHAT,200k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(29,'anthropic.claude-3-sonnet-20240229-v1:0','chat','Bedrock',204800,'LLM,CHAT,200k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(30,'anthropic.claude-instant-v1','chat','Bedrock',102400,'LLM,CHAT,100k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(31,'anthropic.claude-v2','chat','Bedrock',102400,'LLM,CHAT,100k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(32,'anthropic.claude-v2:1','chat','Bedrock',204800,'LLM,CHAT,200k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(33,'anthropic/claude-1','chat','OpenRouter',100000,'LLM CHAT 98K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(34,'anthropic/claude-1.2','chat','OpenRouter',100000,'LLM CHAT 98K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(35,'anthropic/claude-2','chat','OpenRouter',200000,'LLM CHAT 195K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(36,'anthropic/claude-2.0','chat','OpenRouter',100000,'LLM CHAT 98K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(37,'anthropic/claude-2.0:beta','chat','OpenRouter',100000,'LLM CHAT 98K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(38,'anthropic/claude-2.1','chat','OpenRouter',200000,'LLM CHAT 195K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(39,'anthropic/claude-2.1:beta','chat','OpenRouter',200000,'LLM CHAT 195K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(40,'anthropic/claude-2:beta','chat','OpenRouter',200000,'LLM CHAT 195K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(41,'anthropic/claude-3-haiku','image2text','OpenRouter',200000,'LLM IMAGE2TEXT 195K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(42,'anthropic/claude-3-haiku:beta','image2text','OpenRouter',200000,'LLM IMAGE2TEXT 195K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(43,'anthropic/claude-3-opus','image2text','OpenRouter',200000,'LLM IMAGE2TEXT 195K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(44,'anthropic/claude-3-opus:beta','image2text','OpenRouter',200000,'LLM IMAGE2TEXT 195K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(45,'anthropic/claude-3-sonnet','image2text','OpenRouter',200000,'LLM IMAGE2TEXT 195K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(46,'anthropic/claude-3-sonnet:beta','image2text','OpenRouter',200000,'LLM IMAGE2TEXT 195K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(47,'anthropic/claude-3.5-sonnet','image2text','OpenRouter',200000,'LLM IMAGE2TEXT 195K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(48,'anthropic/claude-3.5-sonnet:beta','image2text','OpenRouter',200000,'LLM IMAGE2TEXT 195K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(49,'anthropic/claude-instant-1','chat','OpenRouter',100000,'LLM CHAT 98K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(50,'anthropic/claude-instant-1.0','chat','OpenRouter',100000,'LLM CHAT 98K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(51,'anthropic/claude-instant-1.1','chat','OpenRouter',100000,'LLM CHAT 98K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(52,'anthropic/claude-instant-1:beta','chat','OpenRouter',100000,'LLM CHAT 98K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(53,'austism/chronos-hermes-13b','chat','OpenRouter',4096,'LLM CHAT 4K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(54,'BAAI/bge-base-en-v1.5','embedding','FastEmbed',512,'TEXT EMBEDDING,','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(55,'BAAI/bge-large-en-v1.5','embedding','FastEmbed',512,'TEXT EMBEDDING,','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(56,'BAAI/bge-large-en-v1.5','embedding','PerfXCloud',512,'TEXT EMBEDDING','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(57,'BAAI/bge-large-en-v1.5','embedding','SILICONFLOW',512,'TEXT EMBEDDING,512','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(58,'BAAI/bge-large-zh-v1.5','embedding','BAAI',1024,'TEXT EMBEDDING,','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(59,'BAAI/bge-large-zh-v1.5','embedding','PerfXCloud',1024,'TEXT EMBEDDING','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(60,'BAAI/bge-large-zh-v1.5','embedding','SILICONFLOW',512,'TEXT EMBEDDING,512','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(61,'baai/bge-m3','embedding','NVIDIA',8192,'TEXT EMBEDDING,8K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(62,'BAAI/bge-m3','embedding','PerfXCloud',8192,'TEXT EMBEDDING','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(63,'BAAI/bge-m3','embedding','SILICONFLOW',8192,'TEXT EMBEDDING,8K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(64,'BAAI/bge-reranker-v2-m3','rerank','BAAI',2048,'RE-RANK,2k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(65,'BAAI/bge-reranker-v2-m3','rerank','SILICONFLOW',1024,'RE-RANK, 512','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(66,'BAAI/bge-small-en-v1.5','embedding','FastEmbed',512,'TEXT EMBEDDING,','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(67,'BAAI/bge-small-zh-v1.5','embedding','FastEmbed',512,'TEXT EMBEDDING,','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(68,'Baichuan-Text-Embedding','embedding','BaiChuan',512,'TEXT EMBEDDING','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(69,'Baichuan2-Turbo','chat','BaiChuan',32768,'LLM,CHAT,32K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(70,'Baichuan2-Turbo-192k','chat','BaiChuan',196608,'LLM,CHAT,192K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(71,'Baichuan3-Turbo','chat','BaiChuan',32768,'LLM,CHAT,32K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(72,'Baichuan3-Turbo-128k','chat','BaiChuan',131072,'LLM,CHAT,128K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(73,'Baichuan4','chat','BaiChuan',131072,'LLM,CHAT,128K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(74,'chatglm3-6b','chat','PerfXCloud',8192,'LLM,CHAT,8k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(75,'claude-2.0','chat','Anthropic',102400,'LLM,CHAT,100k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(76,'claude-2.1','chat','Anthropic',204800,'LLM,CHAT,200k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(77,'claude-3-5-sonnet-20240620','chat','Anthropic',204800,'LLM,CHAT,200k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(78,'claude-3-haiku-20240307','chat','Anthropic',204800,'LLM,CHAT,200k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(79,'claude-3-opus-20240229','chat','Anthropic',204800,'LLM,CHAT,200k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(80,'claude-3-sonnet-20240229','chat','Anthropic',204800,'LLM,CHAT,200k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(81,'claude-instant-1.2','chat','Anthropic',102400,'LLM,CHAT,100k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(82,'codestral-latest','chat','Mistral',32000,'LLM,CHAT,32k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(83,'cognitivecomputations/dolphin-mixtral-8x22b','chat','novita.ai',16000,'LLM,CHAT,15k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(84,'cognitivecomputations/dolphin-mixtral-8x22b','chat','OpenRouter',65536,'LLM CHAT 64K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(85,'cognitivecomputations/dolphin-mixtral-8x7b','chat','OpenRouter',32768,'LLM CHAT 32K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(86,'cohere.command-light-text-v14','chat','Bedrock',4096,'LLM,CHAT,4k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(87,'cohere.command-r-plus-v1:0','chat','Bedrock',128000,'LLM,CHAT,128k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(88,'cohere.command-r-v1:0','chat','Bedrock',131072,'LLM,CHAT,128k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(89,'cohere.command-text-v14','chat','Bedrock',4096,'LLM,CHAT,4k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(90,'cohere.embed-english-v3','embedding','Bedrock',2048,'TEXT EMBEDDING','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(91,'cohere.embed-multilingual-v3','embedding','Bedrock',2048,'TEXT EMBEDDING','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(92,'cohere/command','chat','OpenRouter',4096,'LLM CHAT 4K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(93,'cohere/command-r','chat','OpenRouter',128000,'LLM CHAT 125K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(94,'cohere/command-r-plus','chat','OpenRouter',128000,'LLM CHAT 125K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(95,'command','chat','cohere',4096,'LLM,CHAT,4k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(96,'command-light','chat','cohere',4096,'LLM,CHAT,4k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(97,'command-light-nightly','chat','cohere',4096,'LLM,CHAT,4k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(98,'command-nightly','chat','cohere',131072,'LLM,CHAT,128k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(99,'command-r','chat','cohere',131072,'LLM,CHAT,128k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(100,'command-r-plus','chat','cohere',131072,'LLM,CHAT,128k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(101,'databricks/dbrx-instruct','chat','NVIDIA',16384,'LLM,CHAT,16K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(102,'databricks/dbrx-instruct','chat','OpenRouter',32768,'LLM CHAT 32K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(103,'deepseek-ai/DeepSeek-Coder-V2-Instruct','chat','SILICONFLOW',32768,'LLM,CHAT,32k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(104,'deepseek-ai/deepseek-llm-67b-chat','chat','SILICONFLOW',4096,'LLM,CHAT,4k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(105,'deepseek-ai/DeepSeek-V2-Chat','chat','SILICONFLOW',32768,'LLM,CHAT,32k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(106,'deepseek-chat','chat','DeepSeek',32768,'LLM,CHAT,','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(107,'deepseek-coder','chat','DeepSeek',16385,'LLM,CHAT,','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(108,'deepseek-v2-chat','chat','PerfXCloud',4096,'LLM,CHAT,4k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(109,'deepseek-v2-lite-chat','chat','PerfXCloud',2048,'LLM,CHAT,2k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(110,'deepseek/deepseek-chat','chat','OpenRouter',128000,'LLM CHAT 125K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(111,'deepseek/deepseek-coder','chat','OpenRouter',128000,'LLM CHAT 125K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(112,'dolphin-mixtral-8x7b','chat','LeptonAI',32768,'LLM,CHAT,32k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(113,'Doubao-pro-128k','chat','VolcEngine',131072,'LLM,CHAT,128k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(114,'Doubao-pro-32k','chat','VolcEngine',32768,'LLM,CHAT,32k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(115,'Doubao-pro-4k','chat','VolcEngine',4096,'LLM,CHAT,4k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(116,'embed-english-light-v2.0','embedding','cohere',512,'TEXT EMBEDDING','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(117,'embed-english-light-v3.0','embedding','cohere',512,'TEXT EMBEDDING','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(118,'embed-english-v2.0','embedding','cohere',512,'TEXT EMBEDDING','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(119,'embed-english-v3.0','embedding','cohere',512,'TEXT EMBEDDING','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(120,'embed-multilingual-light-v3.0','embedding','cohere',512,'TEXT EMBEDDING','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(121,'embed-multilingual-v2.0','embedding','cohere',256,'TEXT EMBEDDING','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(122,'embed-multilingual-v3.0','embedding','cohere',512,'TEXT EMBEDDING','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(123,'embedding-2','embedding','ZHIPU-AI',512,'TEXT EMBEDDING','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(124,'embedding-3','embedding','ZHIPU-AI',512,'TEXT EMBEDDING','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(125,'fireworks/firellava-13b','image2text','OpenRouter',4096,'LLM IMAGE2TEXT 4K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(126,'gemini-1.0-pro','chat','Gemini',30720,'LLM,CHAT,30K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(127,'gemini-1.0-pro-vision-latest','image2text','Gemini',12288,'LLM,IMAGE2TEXT,12K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(128,'gemini-1.5-flash-latest','chat','Gemini',1048576,'LLM,CHAT,1024K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(129,'gemini-1.5-pro-latest','chat','Gemini',1048576,'LLM,CHAT,1024K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(130,'gemma-7b','chat','LeptonAI',8192,'LLM,CHAT,8k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(131,'gemma-7b-it','chat','Groq',8192,'LLM,CHAT,15k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(132,'gemma2-9b-it','chat','Groq',8192,'LLM,CHAT,15k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(133,'glm-3-turbo','chat','ZHIPU-AI',128000,'LLM,CHAT,','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(134,'glm-4','chat','ZHIPU-AI',128000,'LLM,CHAT,','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(135,'glm-4-air','chat','ZHIPU-AI',128000,'LLM,CHAT,','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(136,'glm-4-airx','chat','ZHIPU-AI',8000,'LLM,CHAT,','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(137,'glm-4-flash','chat','ZHIPU-AI',128000,'LLM,CHAT,','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(138,'glm-4-long','chat','ZHIPU-AI',1000000,'LLM,CHAT,','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(139,'glm-4v','image2text','ZHIPU-AI',2000,'LLM,CHAT,IMAGE2TEXT','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(140,'google/deplot','image2text','NVIDIA',4096,'LLM,IMAGE2TEXT,4K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(141,'google/gemini-flash-1.5','image2text','OpenRouter',2800000,'LLM IMAGE2TEXT 2734K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(142,'google/gemini-pro','chat','OpenRouter',91728,'LLM CHAT 89K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(143,'google/gemini-pro-1.5','image2text','OpenRouter',2800000,'LLM IMAGE2TEXT 2734K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(144,'google/gemini-pro-vision','image2text','OpenRouter',45875,'LLM IMAGE2TEXT 44K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(145,'google/gemma-2-27b-it','chat','NVIDIA',8192,'LLM,CHAT,8K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(146,'google/gemma-2-27b-it','chat','SILICONFLOW',8192,'LLM,CHAT,8k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(147,'google/gemma-2-9b-it','chat','novita.ai',8192,'LLM,CHAT,8k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(148,'google/gemma-2-9b-it','chat','NVIDIA',8192,'LLM,CHAT,8K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(149,'google/gemma-2-9b-it','chat','OpenRouter',8192,'LLM CHAT 8K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(150,'google/gemma-2-9b-it','chat','SILICONFLOW',8192,'LLM,CHAT,8k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(151,'google/gemma-2-9b-it:free','chat','OpenRouter',8192,'LLM CHAT 8K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(152,'google/gemma-2b','chat','NVIDIA',16384,'LLM,CHAT,16K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(153,'google/gemma-7b','chat','NVIDIA',32768,'LLM,CHAT,32K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(154,'google/gemma-7b-it','chat','OpenRouter',8192,'LLM CHAT 8K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(155,'google/gemma-7b-it:free','chat','OpenRouter',8192,'LLM CHAT 8K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(156,'google/gemma-7b-it:nitro','chat','OpenRouter',8192,'LLM CHAT 8K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(157,'google/paligemma','image2text','NVIDIA',4096,'LLM,IMAGE2TEXT,4K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(158,'google/palm-2-chat-bison','chat','OpenRouter',25804,'LLM CHAT 25K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(159,'google/palm-2-chat-bison-32k','chat','OpenRouter',91750,'LLM CHAT 89K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(160,'google/palm-2-codechat-bison','chat','OpenRouter',20070,'LLM CHAT 19K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(161,'google/palm-2-codechat-bison-32k','chat','OpenRouter',91750,'LLM CHAT 89K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(162,'google/recurrentgemma-2b','chat','NVIDIA',4096,'LLM,CHAT,4K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(163,'gpt-3.5-turbo','chat','OpenAI',4096,'LLM,CHAT,4K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(164,'gpt-3.5-turbo-16k-0613','chat','OpenAI',16385,'LLM,CHAT,16k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(165,'gpt-35-turbo','chat','Azure-OpenAI',4096,'LLM,CHAT,4K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(166,'gpt-35-turbo-16k','chat','Azure-OpenAI',16385,'LLM,CHAT,16k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(167,'gpt-4','chat','Azure-OpenAI',8191,'LLM,CHAT,8K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(168,'gpt-4','chat','OpenAI',8191,'LLM,CHAT,8K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(169,'gpt-4-32k','chat','Azure-OpenAI',32768,'LLM,CHAT,32K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(170,'gpt-4-32k','chat','OpenAI',32768,'LLM,CHAT,32K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(171,'gpt-4-turbo','chat','Azure-OpenAI',8191,'LLM,CHAT,8K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(172,'gpt-4-turbo','chat','OpenAI',8191,'LLM,CHAT,8K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(173,'gpt-4-vision-preview','image2text','Azure-OpenAI',765,'LLM,CHAT,IMAGE2TEXT','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(174,'gpt-4-vision-preview','image2text','OpenAI',765,'LLM,CHAT,IMAGE2TEXT','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(175,'gpt-4o','chat,image2text','Azure-OpenAI',128000,'LLM,CHAT,128K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(176,'gpt-4o','chat','OpenAI',128000,'LLM,CHAT,128K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(177,'gpt-4o-mini','image2text','Azure-OpenAI',128000,'LLM,CHAT,128K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(178,'gpt-4o-mini','chat','OpenAI',128000,'LLM,CHAT,128K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(179,'gryphe/mythomax-l2-13b','chat','novita.ai',4096,'LLM,CHAT,4k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(180,'gryphe/mythomax-l2-13b','chat','OpenRouter',4096,'LLM CHAT 4K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(181,'gryphe/mythomax-l2-13b:extended','chat','OpenRouter',8192,'LLM CHAT 8K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(182,'gryphe/mythomax-l2-13b:nitro','chat','OpenRouter',4096,'LLM CHAT 4K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(183,'gryphe/mythomist-7b','chat','OpenRouter',32768,'LLM CHAT 32K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(184,'gryphe/mythomist-7b:free','chat','OpenRouter',32768,'LLM CHAT 32K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(185,'huggingfaceh4/zephyr-7b-beta:free','chat','OpenRouter',4096,'LLM CHAT 4K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(186,'hunyuan-lite','chat','Tencent Hunyuan',262144,'LLM,CHAT,256k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(187,'hunyuan-pro','chat','Tencent Hunyuan',32768,'LLM,CHAT,32k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(188,'hunyuan-standard','chat','Tencent Hunyuan',32768,'LLM,CHAT,32k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(189,'hunyuan-standard-256K','chat','Tencent Hunyuan',262144,'LLM,CHAT,256k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(190,'hunyuan-vision','image2text','Tencent Hunyuan',8192,'LLM,IMAGE2TEXT,8k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(191,'Iiuhaotian/Ilava-v1.6-34b','image2text','NVIDIA',4096,'LLM,IMAGE2TEXT,4K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(192,'Iiuhaotian/Ilava-v1.6-mistral-7b','image2text','NVIDIA',4096,'LLM,IMAGE2TEXT,4K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(193,'intel/neural-chat-7b','chat','OpenRouter',4096,'LLM CHAT 4K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(194,'internlm/internlm2_5-20b-chat','chat','SILICONFLOW',32768,'LLM,CHAT,32k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(195,'internlm/internlm2_5-7b-chat','chat','SILICONFLOW',32768,'LLM,CHAT,32k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(196,'jina-colbert-v1-en','rerank','Jina',8196,'RE-RANK,8k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(197,'jina-embeddings-v2-base-code','embedding','Jina',8196,'TEXT EMBEDDING','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(198,'jina-embeddings-v2-base-de','embedding','Jina',8196,'TEXT EMBEDDING','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(199,'jina-embeddings-v2-base-en','embedding','Jina',8196,'TEXT EMBEDDING','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(200,'jina-embeddings-v2-base-es','embedding','Jina',8196,'TEXT EMBEDDING','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(201,'jina-embeddings-v2-base-zh','embedding','Jina',8196,'TEXT EMBEDDING','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(202,'jina-reranker-v1-base-en','rerank','Jina',8196,'RE-RANK,8k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(203,'jina-reranker-v1-tiny-en','rerank','Jina',8196,'RE-RANK,8k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(204,'jina-reranker-v1-turbo-en','rerank','Jina',8196,'RE-RANK,8k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(205,'jinaai/jina-embeddings-v2-base-en','embedding','FastEmbed',2147483647,'TEXT EMBEDDING,','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(206,'jinaai/jina-embeddings-v2-small-en','embedding','FastEmbed',2147483647,'TEXT EMBEDDING,','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(207,'jondurbin/airoboros-l2-70b','chat','novita.ai',4096,'LLM,CHAT,4k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(208,'koboldai/psyfighter-13b-2','chat','OpenRouter',4096,'LLM CHAT 4K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(209,'liuhaotian/llava-yi-34b','image2text','OpenRouter',4096,'LLM IMAGE2TEXT 4K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(210,'lizpreciatior/lzlv-70b-fp16-hf','chat','OpenRouter',4096,'LLM CHAT 4K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(211,'llama-3.1-70b-versatile','chat','Groq',131072,'LLM,CHAT,128k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(212,'llama-3.1-8b-instant','chat','Groq',131072,'LLM,CHAT,128k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(213,'llama2-13b','chat','LeptonAI',4096,'LLM,CHAT,4K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(214,'llama3-1-405b','chat','LeptonAI',8192,'LLM,CHAT,8k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(215,'llama3-1-70b','chat','LeptonAI',8192,'LLM,CHAT,8k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(216,'llama3-1-8b','chat','LeptonAI',4096,'LLM,CHAT,4k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(217,'llama3-70b','chat','LeptonAI',8192,'LLM,CHAT,8k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(218,'llama3-70b-8192','chat','Groq',8192,'LLM,CHAT,6k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(219,'llama3-8b','chat','LeptonAI',8192,'LLM,CHAT,8K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(220,'llama3-8b-8192','chat','Groq',8192,'LLM,CHAT,30k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(221,'Llama3-Chinese_v2','chat','PerfXCloud',8192,'LLM,CHAT,8k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(222,'llama3.1:405b','chat','PerfXCloud',131072,'LLM,CHAT,128k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(223,'lynn/soliloquy-l3','chat','OpenRouter',24576,'LLM CHAT 24K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(224,'lzlv_70b','chat','novita.ai',4096,'LLM,CHAT,4k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(225,'maidalun1020/bce-embedding-base_v1','embedding','Youdao',512,'TEXT EMBEDDING,','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(226,'maidalun1020/bce-reranker-base_v1','rerank','Youdao',512,'RE-RANK, 512','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(227,'mancer/weaver','chat','OpenRouter',8000,'LLM CHAT 8K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(228,'mediatek/breeze-7b-instruct','chat','NVIDIA',8192,'LLM,CHAT,8K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(229,'Meta-Llama-3-70B-Instruct-GPTQ-Int4','chat','PerfXCloud',1024,'LLM,CHAT,1k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(230,'Meta-Llama-3-8B-Instruct','chat','PerfXCloud',8192,'LLM,CHAT,8k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(231,'Meta-Llama-3.1-8B-Instruct','chat','PerfXCloud',4096,'LLM,CHAT,4k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(232,'meta-llama/codellama-34b-instruct','chat','OpenRouter',8192,'LLM CHAT 8K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(233,'meta-llama/codellama-70b-instruct','chat','OpenRouter',2048,'LLM CHAT 2K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(234,'meta-llama/llama-2-13b-chat','chat','OpenRouter',4096,'LLM CHAT 4K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(235,'meta-llama/llama-2-70b-chat','chat','OpenRouter',4096,'LLM CHAT 4K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(236,'meta-llama/llama-2-70b-chat:nitro','chat','OpenRouter',4096,'LLM CHAT 4K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(237,'meta-llama/llama-3-70b','chat','OpenRouter',8192,'LLM CHAT 8K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(238,'meta-llama/llama-3-70b-instruct','chat','novita.ai',8192,'LLM,CHAT,8k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(239,'meta-llama/llama-3-70b-instruct','chat','OpenRouter',8192,'LLM CHAT 8K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(240,'meta-llama/llama-3-70b-instruct:nitro','chat','OpenRouter',8192,'LLM CHAT 8K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(241,'meta-llama/llama-3-8b','chat','OpenRouter',8192,'LLM CHAT 8K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(242,'meta-llama/llama-3-8b-instruct','chat','novita.ai',8192,'LLM,CHAT,8k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(243,'meta-llama/llama-3-8b-instruct','chat','OpenRouter',8192,'LLM CHAT 8K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(244,'meta-llama/llama-3-8b-instruct:extended','chat','OpenRouter',16384,'LLM CHAT 16K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(245,'meta-llama/llama-3-8b-instruct:free','chat','OpenRouter',8192,'LLM CHAT 8K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(246,'meta-llama/llama-3-8b-instruct:nitro','chat','OpenRouter',8192,'LLM CHAT 8K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(247,'meta-llama/llama-3.1-405b-instruct','chat','novita.ai',32768,'LLM,CHAT,32k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(248,'meta-llama/llama-3.1-70b-instruct','chat','novita.ai',8192,'LLM,CHAT,8k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(249,'meta-llama/llama-3.1-8b-instruct','chat','novita.ai',8192,'LLM,CHAT,8k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(250,'meta-llama/llama-guard-2-8b','chat','OpenRouter',8192,'LLM CHAT 8K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(251,'meta-llama/Meta-Llama-3-70B-Instruct','chat','SILICONFLOW',32768,'LLM,CHAT,32k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(252,'meta-llama/Meta-Llama-3-8B-Instruct','chat','SILICONFLOW',8192,'LLM,CHAT,8k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(253,'meta-llama/Meta-Llama-3.1-405B-Instruct','chat','SILICONFLOW',32768,'LLM,CHAT,32k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(254,'meta-llama/Meta-Llama-3.1-70B-Instruct','chat','SILICONFLOW',32768,'LLM,CHAT,32k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(255,'meta-llama/Meta-Llama-3.1-8B-Instruct','chat','SILICONFLOW',8192,'LLM,CHAT,8k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(256,'meta.llama2-13b-chat-v1','chat','Bedrock',4096,'LLM,CHAT,4k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(257,'meta.llama2-70b-chat-v1','chat','Bedrock',4096,'LLM,CHAT,4k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(258,'meta.llama3-70b-instruct-v1:0','chat','Bedrock',8192,'LLM,CHAT,8k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(259,'meta.llama3-8b-instruct-v1:0','chat','Bedrock',8192,'LLM,CHAT,8k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(260,'meta/llama2-70b','chat','NVIDIA',4096,'LLM,CHAT,4K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(261,'meta/llama3-70b','chat','NVIDIA',8192,'LLM,CHAT,8K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(262,'meta/llama3-8b','chat','NVIDIA',8192,'LLM,CHAT,8K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(263,'microsoft/kosmos-2','image2text','NVIDIA',4096,'LLM,IMAGE2TEXT,4K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(264,'microsoft/phi-3-medium-128k-instruct','chat','NVIDIA',131072,'LLM,CHAT,128K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(265,'microsoft/phi-3-medium-128k-instruct','chat','OpenRouter',128000,'LLM CHAT 125K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(266,'microsoft/phi-3-medium-128k-instruct:free','chat','OpenRouter',128000,'LLM CHAT 125K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(267,'microsoft/phi-3-medium-4k-instruct','chat','NVIDIA',4096,'LLM,CHAT,4K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(268,'microsoft/phi-3-medium-4k-instruct','chat','OpenRouter',4000,'LLM CHAT 4K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(269,'microsoft/phi-3-mini-128k-instruct','chat','OpenRouter',128000,'LLM CHAT 125K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(270,'microsoft/phi-3-mini-128k-instruct:free','chat','OpenRouter',128000,'LLM CHAT 125K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(271,'microsoft/phi-3-mini-4k-instruct','chat','NVIDIA',4096,'LLM,CHAT,4K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(272,'microsoft/phi-3-small-128k-instruct','chat','NVIDIA',131072,'LLM,CHAT,128K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(273,'microsoft/phi-3-small-8k-instruct','chat','NVIDIA',8192,'LLM,CHAT,8K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(274,'microsoft/phi-3-vision-128k-instruct','image2text','NVIDIA',131072,'LLM,IMAGE2TEXT,128K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(275,'microsoft/wizardlm-2-7b','chat','novita.ai',32768,'LLM,CHAT,32k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(276,'microsoft/wizardlm-2-7b','chat','OpenRouter',32000,'LLM CHAT 32K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(277,'microsoft/wizardlm-2-8x22b','chat','novita.ai',65535,'LLM,CHAT,64k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(278,'microsoft/wizardlm-2-8x22b','chat','OpenRouter',65536,'LLM CHAT 64K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(279,'microsoftphi-3-mini-128k-instruct','chat','NVIDIA',131072,'LLM,CHAT,128K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(280,'MindChat-Qwen-7B-v2','chat','PerfXCloud',2048,'LLM,CHAT,2k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(281,'mistral-7b','chat','LeptonAI',8192,'LLM,CHAT,8K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(282,'Mistral-7B-Instruct','chat','PerfXCloud',32768,'LLM,CHAT,32k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(283,'mistral-8x7b','chat','LeptonAI',8192,'LLM,CHAT,8K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(284,'mistral-embed','embedding','Mistral',8192,'LLM,CHAT,8k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(285,'mistral-large-latest','chat','Mistral',32000,'LLM,CHAT,32k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(286,'mistral-medium-latest','chat','Mistral',32000,'LLM,CHAT,32k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(287,'mistral-small-latest','chat','Mistral',32000,'LLM,CHAT,32k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(288,'mistral.mistral-7b-instruct-v0:2','chat','Bedrock',8192,'LLM,CHAT,8k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(289,'mistral.mistral-large-2402-v1:0','chat','Bedrock',8192,'LLM,CHAT,8k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(290,'mistral.mistral-small-2402-v1:0','chat','Bedrock',8192,'LLM,CHAT,8k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(291,'mistral.mixtral-8x7b-instruct-v0:1','chat','Bedrock',4096,'LLM,CHAT,4k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(292,'mistralai/mistral-7b-instruct','chat','novita.ai',32768,'LLM,CHAT,32k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(293,'mistralai/mistral-7b-instruct','chat','NVIDIA',4096,'LLM,CHAT,4K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(294,'mistralai/mistral-7b-instruct','chat','OpenRouter',32768,'LLM CHAT 32K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(295,'mistralai/mistral-7b-instruct-v0.1','chat','OpenRouter',4096,'LLM CHAT 4K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(296,'mistralai/mistral-7b-instruct-v0.2','chat','OpenRouter',32768,'LLM CHAT 32K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(297,'mistralai/Mistral-7B-Instruct-v0.2','chat','SILICONFLOW',32768,'LLM,CHAT,32k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(298,'mistralai/mistral-7b-instruct-v0.3','chat','NVIDIA',4096,'LLM,CHAT,4K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(299,'mistralai/mistral-7b-instruct-v0.3','chat','OpenRouter',32768,'LLM CHAT 32K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(300,'mistralai/mistral-7b-instruct:free','chat','OpenRouter',32768,'LLM CHAT 32K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(301,'mistralai/mistral-7b-instruct:nitro','chat','OpenRouter',32768,'LLM CHAT 32K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(302,'mistralai/mistral-large','chat','NVIDIA',32768,'LLM,CHAT,32K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(303,'mistralai/mistral-large','chat','OpenRouter',32000,'LLM CHAT 32K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(304,'mistralai/mistral-medium','chat','OpenRouter',32000,'LLM CHAT 32K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(305,'mistralai/mistral-nemo','chat','novita.ai',32768,'LLM,CHAT,32k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(306,'mistralai/mistral-small','chat','OpenRouter',32000,'LLM CHAT 32K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(307,'mistralai/mistral-tiny','chat','OpenRouter',32000,'LLM CHAT 32K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(308,'mistralai/mixtral-8x22b','chat','OpenRouter',65536,'LLM CHAT 64K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(309,'mistralai/mixtral-8x22b-instruct','chat','NVIDIA',65536,'LLM,CHAT,64K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(310,'mistralai/mixtral-8x22b-instruct','chat','OpenRouter',65536,'LLM CHAT 64K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(311,'mistralai/mixtral-8x7b','chat','OpenRouter',32768,'LLM CHAT 32K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(312,'mistralai/mixtral-8x7b-instruct','chat','NVIDIA',32768,'LLM,CHAT,32K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(313,'mistralai/mixtral-8x7b-instruct','chat','OpenRouter',32768,'LLM CHAT 32K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(314,'mistralai/Mixtral-8x7B-Instruct-v0.1','chat','SILICONFLOW',32768,'LLM,CHAT,32k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(315,'mistralai/mixtral-8x7b-instruct:nitro','chat','OpenRouter',32768,'LLM CHAT 32K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(316,'mixtral-8x7b-32768','chat','Groq',32768,'LLM,CHAT,5k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(317,'Mixtral-8x7B-Instruct-v0.1-GPTQ','chat','PerfXCloud',32768,'LLM,CHAT,32k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(318,'moonshot-v1-128k','chat','Moonshot',128000,'LLM,CHAT','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(319,'moonshot-v1-32k','chat','Moonshot',32768,'LLM,CHAT,','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(320,'moonshot-v1-8k','chat','Moonshot',7900,'LLM,CHAT,','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(321,'netease-youdao/bce-embedding-base_v1','embedding','SILICONFLOW',512,'TEXT EMBEDDING,512','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(322,'netease-youdao/bce-reranker-base_v1','rerank','SILICONFLOW',1024,'RE-RANK, 512','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(323,'neversleep/llama-3-lumimaid-70b','chat','OpenRouter',8192,'LLM CHAT 8K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(324,'neversleep/llama-3-lumimaid-8b','chat','OpenRouter',24576,'LLM CHAT 24K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(325,'neversleep/llama-3-lumimaid-8b:extended','chat','OpenRouter',24576,'LLM CHAT 24K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(326,'neversleep/noromaid-20b','chat','OpenRouter',8192,'LLM CHAT 8K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(327,'neversleep/noromaid-mixtral-8x7b-instruct','chat','OpenRouter',8000,'LLM CHAT 8K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(328,'nomic-ai/nomic-embed-text-v1.5','embedding','FastEmbed',8192,'TEXT EMBEDDING,','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(329,'Nous-Hermes-2-Mixtral-8x7B-DPO','chat','novita.ai',32768,'LLM,CHAT,32k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(330,'nous-hermes-llama2','chat','LeptonAI',4096,'LLM,CHAT,4k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(331,'nousresearch/hermes-2-pro-llama-3-8b','chat','novita.ai',8192,'LLM,CHAT,8k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(332,'nousresearch/hermes-2-pro-llama-3-8b','chat','OpenRouter',8192,'LLM CHAT 8K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(333,'nousresearch/hermes-2-theta-llama-3-8b','chat','OpenRouter',16384,'LLM CHAT 16K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(334,'nousresearch/nous-capybara-34b','chat','OpenRouter',200000,'LLM CHAT 195K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(335,'nousresearch/nous-capybara-7b','chat','OpenRouter',8192,'LLM CHAT 8K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(336,'nousresearch/nous-capybara-7b:free','chat','OpenRouter',8192,'LLM CHAT 8K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(337,'nousresearch/nous-hermes-2-mistral-7b-dpo','chat','OpenRouter',8192,'LLM CHAT 8K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(338,'nousresearch/nous-hermes-2-mixtral-8x7b-dpo','chat','OpenRouter',32768,'LLM CHAT 32K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(339,'nousresearch/nous-hermes-2-mixtral-8x7b-sft','chat','OpenRouter',32768,'LLM CHAT 32K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(340,'nousresearch/nous-hermes-llama2-13b','chat','novita.ai',4096,'LLM,CHAT,4k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(341,'nousresearch/nous-hermes-llama2-13b','chat','OpenRouter',4096,'LLM CHAT 4K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(342,'nousresearch/nous-hermes-yi-34b','chat','OpenRouter',4096,'LLM CHAT 4K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(343,'nv-mistralai/mistral-nemo-12b-instruct','chat','NVIDIA',131072,'LLM,CHAT,128K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(344,'nvidia/embed-qa-4','embedding','NVIDIA',512,'TEXT EMBEDDING,512','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(345,'nvidia/llama3-chatqa-1.5-70b','chat','NVIDIA',4096,'LLM,CHAT,4K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(346,'nvidia/llama3-chatqa-1.5-8b','chat','NVIDIA',4096,'LLM,CHAT,4K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(347,'nvidia/nemotron-4-340b-instruct','chat','NVIDIA',4096,'LLM,CHAT,4K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(348,'nvidia/nemotron-4-340b-instruct','chat','OpenRouter',4096,'LLM CHAT 4K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(349,'nvidia/nemotron-4-340b-reward','chat','NVIDIA',4096,'LLM,CHAT,4K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(350,'nvidia/neva-22b','image2text','NVIDIA',4096,'LLM,IMAGE2TEXT,4K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(351,'nvidia/nv-embed-v1','embedding','NVIDIA',32768,'TEXT EMBEDDING,32K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(352,'nvidia/nv-embedqa-e5-v5','embedding','NVIDIA',512,'TEXT EMBEDDING,512','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(353,'nvidia/nv-embedqa-mistral-7b-v2','embedding','NVIDIA',512,'TEXT EMBEDDING,512','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(354,'nvidia/nv-rerankqa-mistral-4b-v3','rerank','NVIDIA',512,'RE-RANK,512','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(355,'nvidia/rerank-qa-mistral-4b','rerank','NVIDIA',512,'RE-RANK,512','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(356,'open-mistral-7b','chat','Mistral',32000,'LLM,CHAT,32k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(357,'open-mixtral-8x22b','chat','Mistral',64000,'LLM,CHAT,64k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(358,'open-mixtral-8x7b','chat','Mistral',32000,'LLM,CHAT,32k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(359,'open-orca/mistral-7b-openorca','chat','OpenRouter',8192,'LLM CHAT 8K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(360,'openai/gpt-3.5-turbo','chat','OpenRouter',16385,'LLM CHAT 16K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(361,'openai/gpt-3.5-turbo-0125','chat','OpenRouter',16385,'LLM CHAT 16K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(362,'openai/gpt-3.5-turbo-0301','chat','OpenRouter',4095,'LLM CHAT 4K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(363,'openai/gpt-3.5-turbo-0613','chat','OpenRouter',4095,'LLM CHAT 4K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(364,'openai/gpt-3.5-turbo-1106','chat','OpenRouter',16385,'LLM CHAT 16K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(365,'openai/gpt-3.5-turbo-16k','chat','OpenRouter',16385,'LLM CHAT 16K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(366,'openai/gpt-3.5-turbo-instruct','chat','OpenRouter',4095,'LLM CHAT 4K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(367,'openai/gpt-4','chat','OpenRouter',8191,'LLM CHAT 8K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(368,'openai/gpt-4-0314','chat','OpenRouter',8191,'LLM CHAT 8K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(369,'openai/gpt-4-1106-preview','chat','OpenRouter',128000,'LLM CHAT 125K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(370,'openai/gpt-4-32k','chat','OpenRouter',32767,'LLM CHAT 32K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(371,'openai/gpt-4-32k-0314','chat','OpenRouter',32767,'LLM CHAT 32K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(372,'openai/gpt-4-turbo','image2text','OpenRouter',128000,'LLM IMAGE2TEXT 125K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(373,'openai/gpt-4-turbo-preview','chat','OpenRouter',128000,'LLM CHAT 125K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(374,'openai/gpt-4-vision-preview','image2text','OpenRouter',128000,'LLM IMAGE2TEXT 125K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(375,'openai/gpt-4o','image2text','OpenRouter',128000,'LLM IMAGE2TEXT 125K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(376,'openai/gpt-4o-2024-05-13','image2text','OpenRouter',128000,'LLM IMAGE2TEXT 125K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(377,'openchat-3-5','chat','LeptonAI',4096,'LLM,CHAT,4k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(378,'openchat/openchat-7b','chat','novita.ai',4096,'LLM,CHAT,4k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(379,'openchat/openchat-7b','chat','OpenRouter',8192,'LLM CHAT 8K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(380,'openchat/openchat-7b:free','chat','OpenRouter',8192,'LLM CHAT 8K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(381,'openchat/openchat-8b','chat','OpenRouter',8192,'LLM CHAT 8K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(382,'openrouter/auto','chat','OpenRouter',200000,'LLM CHAT 195K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(383,'openrouter/cinematika-7b','chat','OpenRouter',8000,'LLM CHAT 8K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(384,'openrouter/flavor-of-the-week','chat','OpenRouter',32000,'LLM CHAT 32K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(385,'perplexity/llama-3-sonar-large-32k-chat','chat','OpenRouter',32768,'LLM CHAT 32K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(386,'perplexity/llama-3-sonar-large-32k-online','chat','OpenRouter',28000,'LLM CHAT 28K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(387,'perplexity/llama-3-sonar-small-32k-chat','chat','OpenRouter',32768,'LLM CHAT 32K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(388,'perplexity/llama-3-sonar-small-32k-online','chat','OpenRouter',28000,'LLM CHAT 28K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(389,'phi-2','chat','PerfXCloud',2048,'LLM,CHAT,2k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(390,'phind/phind-codellama-34b','chat','OpenRouter',4096,'LLM CHAT 4K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(391,'Pro/01-ai/Yi-1.5-6B-Chat','chat','SILICONFLOW',4096,'LLM,CHAT,4k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(392,'Pro/01-ai/Yi-1.5-9B-Chat-16K','chat','SILICONFLOW',16384,'LLM,CHAT,16k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(393,'Pro/google/gemma-2-9b-it','chat','SILICONFLOW',8192,'LLM,CHAT,8k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(394,'Pro/internlm/internlm2_5-7b-chat','chat','SILICONFLOW',32768,'LLM,CHAT,32k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(395,'Pro/meta-llama/Meta-Llama-3-8B-Instruct','chat','SILICONFLOW',8192,'LLM,CHAT,8k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(396,'Pro/meta-llama/Meta-Llama-3.1-8B-Instruct','chat','SILICONFLOW',32768,'LLM,CHAT,32k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(397,'Pro/mistralai/Mistral-7B-Instruct-v0.2','chat','SILICONFLOW',32768,'LLM,CHAT,32k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(398,'Pro/Qwen/Qwen1.5-7B-Chat','chat','SILICONFLOW',32768,'LLM,CHAT,32k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(399,'Pro/Qwen/Qwen2-1.5B-Instruct','chat','SILICONFLOW',32768,'LLM,CHAT,32k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(400,'Pro/Qwen/Qwen2-7B-Instruct','chat','SILICONFLOW',32768,'LLM,CHAT,32k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(401,'Pro/THUDM/chatglm3-6b','chat','SILICONFLOW',32768,'LLM,CHAT,32k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(402,'Pro/THUDM/glm-4-9b-chat','chat','SILICONFLOW',32768,'LLM,CHAT,32k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(403,'pygmalionai/mythalion-13b','chat','OpenRouter',8192,'LLM CHAT 8K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(404,'qwen-plus','chat','Tongyi-Qianwen',32768,'LLM,CHAT,32K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(405,'qwen-turbo','chat','Tongyi-Qianwen',8191,'LLM,CHAT,8K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(406,'qwen-vl-plus','image2text','Tongyi-Qianwen',765,'LLM,CHAT,IMAGE2TEXT','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(407,'qwen/qwen-110b-chat','chat','OpenRouter',32768,'LLM CHAT 32K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(408,'qwen/qwen-14b-chat','chat','OpenRouter',32768,'LLM CHAT 32K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(409,'qwen/qwen-2-72b-instruct','chat','OpenRouter',32768,'LLM CHAT 32K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(410,'qwen/qwen-32b-chat','chat','OpenRouter',32768,'LLM CHAT 32K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(411,'qwen/qwen-4b-chat','chat','OpenRouter',32768,'LLM CHAT 32K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(412,'qwen/qwen-72b-chat','chat','OpenRouter',32768,'LLM CHAT 32K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(413,'qwen/qwen-7b-chat','chat','OpenRouter',32768,'LLM CHAT 32K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(414,'Qwen/Qwen1.5-110B-Chat','chat','SILICONFLOW',32768,'LLM,CHAT,32k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(415,'Qwen/Qwen1.5-14B-Chat','chat','SILICONFLOW',32768,'LLM,CHAT,32k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(416,'Qwen/Qwen1.5-32B-Chat','chat','SILICONFLOW',32768,'LLM,CHAT,32k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(417,'Qwen/Qwen1.5-7B-Chat','chat','SILICONFLOW',32768,'LLM,CHAT,32k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(418,'Qwen/Qwen2-1.5B-Instruct','chat','SILICONFLOW',32768,'LLM,CHAT,32k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(419,'Qwen/Qwen2-57B-A14B-Instruct','chat','SILICONFLOW',32768,'LLM,CHAT,32k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(420,'Qwen/Qwen2-72B-Instruct','chat','SILICONFLOW',32768,'LLM,CHAT,32k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(421,'Qwen/Qwen2-7B-Instruct','chat','SILICONFLOW',32768,'LLM,CHAT,32k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(422,'Qwen/Qwen2-Math-72B-Instruct','chat','SILICONFLOW',32768,'LLM,CHAT,32k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(423,'Qwen1.5-72B-Chat-GPTQ-Int4','chat','PerfXCloud',2048,'LLM,CHAT,2k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(424,'Qwen1.5-7B','chat','PerfXCloud',32768,'LLM,CHAT,32k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(425,'Qwen2-72B-Instruct','chat','PerfXCloud',131072,'LLM,CHAT,128k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(426,'Qwen2-72B-Instruct-awq-int4','chat','PerfXCloud',32768,'LLM,CHAT,32k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(427,'Qwen2-72B-Instruct-GPTQ-Int4','chat','PerfXCloud',2048,'LLM,CHAT,2k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(428,'Qwen2-7B','chat','PerfXCloud',131072,'LLM,CHAT,128k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(429,'Qwen2-7B-Instruct','chat','PerfXCloud',32768,'LLM,CHAT,32k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(430,'recursal/eagle-7b','chat','OpenRouter',10000,'LLM CHAT 9K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(431,'recursal/rwkv-5-3b-ai-town','chat','OpenRouter',10000,'LLM CHAT 9K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(432,'rerank-1','rerank','Voyage AI',8000,'RE-RANK, 8000','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(433,'rerank-english-v2.0','rerank','cohere',8196,'RE-RANK,512','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(434,'rerank-english-v3.0','rerank','cohere',4096,'RE-RANK,4k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(435,'rerank-lite-1','rerank','Voyage AI',4000,'RE-RANK, 4000','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(436,'rerank-multilingual-v2.0','rerank','cohere',512,'RE-RANK,512','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(437,'rerank-multilingual-v3.0','rerank','cohere',4096,'RE-RANK,4k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(438,'rwkv/rwkv-5-world-3b','chat','OpenRouter',10000,'LLM CHAT 9K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(439,'sambert-zhide-v1','tts','Tongyi-Qianwen',2048,'TTS','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(440,'sambert-zhiru-v1','tts','Tongyi-Qianwen',2048,'TTS','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(441,'sao10k/fimbulvetr-11b-v2','chat','OpenRouter',8192,'LLM CHAT 8K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(442,'sao10k/l3-70b-euryale-v2.1','chat','novita.ai',16000,'LLM,CHAT,15k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(443,'sao10k/l3-euryale-70b','chat','OpenRouter',8192,'LLM CHAT 8K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(444,'sao10k/l3-stheno-8b','chat','OpenRouter',32000,'LLM CHAT 32K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(445,'seallms/seallm-7b-v2.5','chat','NVIDIA',4096,'LLM,CHAT,4K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(446,'sentence-transformers/all-MiniLM-L6-v2','embedding','FastEmbed',512,'TEXT EMBEDDING,','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(447,'snowflake/arctic','chat','NVIDIA',4096,'LLM,CHAT,4K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(448,'snowflake/arctic-embed-l','embedding','NVIDIA',512,'TEXT EMBEDDING,512','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(449,'snowflake/snowflake-arctic-instruct','chat','OpenRouter',4096,'LLM CHAT 4K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(450,'solar-1-mini-chat','chat','Upstage',32768,'LLM,CHAT,32k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(451,'solar-1-mini-chat-ja','chat','Upstage',32768,'LLM,CHAT,32k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(452,'SOLAR-10_7B-Instruct','chat','PerfXCloud',4096,'LLM,CHAT,4k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(453,'solar-embedding-1-large-passage','embedding','Upstage',4000,'TEXT EMBEDDING','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(454,'solar-embedding-1-large-query','embedding','Upstage',4000,'TEXT EMBEDDING','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(455,'sophosympatheia/midnight-rose-70b','chat','novita.ai',4096,'LLM,CHAT,4k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(456,'sophosympatheia/midnight-rose-70b','chat','OpenRouter',4096,'LLM CHAT 4K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(457,'step-1-128k','chat','StepFun',131072,'LLM,CHAT,128k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(458,'step-1-256k','chat','StepFun',262144,'LLM,CHAT,256k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(459,'step-1-32k','chat','StepFun',32768,'LLM,CHAT,32k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(460,'step-1-8k','chat','StepFun',8192,'LLM,CHAT,8k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(461,'step-1v-8k','image2text','StepFun',8192,'LLM,CHAT,IMAGE2TEXT','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(462,'teknium/openhermes-2-mistral-7b','chat','OpenRouter',8192,'LLM CHAT 8K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(463,'teknium/openhermes-2.5-mistral-7b','chat','novita.ai',4096,'LLM,CHAT,4k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(464,'teknium/openhermes-2.5-mistral-7b','chat','OpenRouter',4096,'LLM CHAT 4K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(465,'text-embedding-004','embedding','Gemini',2048,'TEXT EMBEDDING','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(466,'text-embedding-3-large','embedding','Azure-OpenAI',8191,'TEXT EMBEDDING,8K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(467,'text-embedding-3-large','embedding','OpenAI',8191,'TEXT EMBEDDING,8K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(468,'text-embedding-3-small','embedding','Azure-OpenAI',8191,'TEXT EMBEDDING,8K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(469,'text-embedding-3-small','embedding','OpenAI',8191,'TEXT EMBEDDING,8K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(470,'text-embedding-ada-002','embedding','Azure-OpenAI',8191,'TEXT EMBEDDING,8K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(471,'text-embedding-ada-002','embedding','OpenAI',8191,'TEXT EMBEDDING,8K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(472,'text-embedding-v2','embedding','Tongyi-Qianwen',2048,'TEXT EMBEDDING,2K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(473,'text-embedding-v3','embedding','Tongyi-Qianwen',8192,'TEXT EMBEDDING,8K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(474,'THUDM/chatglm3-6b','chat','SILICONFLOW',32768,'LLM,CHAT,32k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(475,'THUDM/glm-4-9b-chat','chat','SILICONFLOW',32768,'LLM,CHAT,32k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(476,'togethercomputer/stripedhyena-hessian-7b','chat','OpenRouter',32768,'LLM CHAT 32K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(477,'togethercomputer/stripedhyena-nous-7b','chat','OpenRouter',32768,'LLM CHAT 32K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(478,'toppy-m-7b','chat','LeptonAI',4096,'LLM,CHAT,4k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(479,'undi95/remm-slerp-l2-13b','chat','OpenRouter',4096,'LLM CHAT 4K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(480,'undi95/remm-slerp-l2-13b:extended','chat','OpenRouter',6144,'LLM CHAT 6K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(481,'undi95/toppy-m-7b','chat','OpenRouter',4096,'LLM CHAT 4K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(482,'undi95/toppy-m-7b:free','chat','OpenRouter',4096,'LLM CHAT 4K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(483,'undi95/toppy-m-7b:nitro','chat','OpenRouter',4096,'LLM CHAT 4K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(484,'upstage/solar-10.7b-instruct','chat','NVIDIA',4096,'LLM,CHAT,4K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(485,'voyage-2','embedding','Voyage AI',4000,'TEXT EMBEDDING,4000','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(486,'voyage-code-2','embedding','Voyage AI',16000,'TEXT EMBEDDING,16000','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(487,'voyage-finance-2','embedding','Voyage AI',32000,'TEXT EMBEDDING,32000','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(488,'voyage-large-2','embedding','Voyage AI',16000,'TEXT EMBEDDING,16000','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(489,'voyage-large-2-instruct','embedding','Voyage AI',16000,'TEXT EMBEDDING,16000','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(490,'voyage-law-2','embedding','Voyage AI',16000,'TEXT EMBEDDING,16000','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(491,'voyage-multilingual-2','embedding','Voyage AI',32000,'TEXT EMBEDDING,32000','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(492,'whisper-1','speech2text','Azure-OpenAI',26214400,'SPEECH2TEXT','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(493,'whisper-1','speech2text','OpenAI',26214400,'SPEECH2TEXT','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(494,'wizardlm-2-7b','chat','LeptonAI',32768,'LLM,CHAT,32k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(495,'wizardlm-2-8x22b','chat','LeptonAI',65536,'LLM,CHAT,64K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(496,'xwin-lm/xwin-lm-70b','chat','OpenRouter',8192,'LLM CHAT 8K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(497,'Yi-1_5-9B-Chat-16K','chat','PerfXCloud',16384,'LLM,CHAT,16k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(498,'yi-large','chat','01.AI',32768,'LLM,CHAT,32k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(499,'yi-large-fc','chat','01.AI',32768,'LLM,CHAT,32k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(500,'yi-large-preview','chat','01.AI',16384,'LLM,CHAT,16k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(501,'yi-large-rag','chat','01.AI',16384,'LLM,CHAT,16k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(502,'yi-large-turbo','chat','01.AI',16384,'LLM,CHAT,16k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(503,'yi-medium','chat','01.AI',16384,'LLM,CHAT,16k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(504,'yi-medium-200k','chat','01.AI',204800,'LLM,CHAT,200k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(505,'yi-spark','chat','01.AI',16384,'LLM,CHAT,16k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(506,'yi-vision','image2text','01.AI',16384,'LLM,CHAT,IMAGE2TEXT,16k','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(507,'llama3.1','chat','Ollama',4096,'LLM CHAT 8K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0),(508,'qwen-vl-max','chat','Tongyi-Qianwen',4096,'LLM CHAT 32K','1','2024-10-20 03:40:55','2024-10-20 03:40:55',0,0);
/*!40000 ALTER TABLE `sys_provider_model` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'nexfly'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-01-13 22:10:26
