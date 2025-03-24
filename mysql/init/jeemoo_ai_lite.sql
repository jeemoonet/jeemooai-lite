-- MySQL dump 10.13  Distrib 8.0.22, for Linux (x86_64)
--
-- Host: 127.0.0.1    Database: jeemoo_ai_lite
-- ------------------------------------------------------
-- Server version	5.7.36

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `ai_company`
--

DROP TABLE IF EXISTS `ai_company`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ai_company` (
  `company_id` bigint(20) unsigned NOT NULL COMMENT 'ID',
  `user_id` bigint(20) unsigned DEFAULT NULL COMMENT '用户ID',
  `person_mobile` varchar(255) DEFAULT NULL COMMENT '个人手机',
  `company_name` varchar(255) DEFAULT NULL COMMENT '公司名称',
  `industry_type` varchar(255) DEFAULT NULL COMMENT '行业类型',
  `industry_id` bigint(20) unsigned DEFAULT NULL COMMENT '行业id',
  `employee_num` varchar(255) DEFAULT NULL COMMENT '员工人数',
  `position_name` varchar(255) DEFAULT NULL COMMENT '职位',
  `company_address` varchar(255) DEFAULT NULL COMMENT '公司地址',
  `license_code` varchar(255) DEFAULT NULL COMMENT '营业执照代码',
  `business_license` varchar(255) DEFAULT NULL COMMENT '营业执照',
  `organization_code_certificate` varchar(255) DEFAULT NULL COMMENT '组织机构代码证书',
  `logo` varchar(255) DEFAULT NULL COMMENT '公司标志',
  `company_desc` text COMMENT '公司描述',
  `person_name` varchar(255) DEFAULT NULL COMMENT '联系人姓名',
  `deleted` tinyint(3) unsigned DEFAULT '0' COMMENT '删除标记',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_by` varchar(255) DEFAULT NULL COMMENT '创建者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_by` varchar(255) DEFAULT NULL COMMENT '更新者',
  `company_address_code` json DEFAULT NULL,
  `user_count` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '企业当前人数',
  `user_count_max` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '最大人数',
  `integral` int(11) NOT NULL DEFAULT '0' COMMENT '企业剩余积分',
  `integral_total` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '已用积分',
  `document_word_count` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '企业文档字数',
  `document_word_count_max` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '最大文档字数',
  `is_trial` tinyint(3) unsigned NOT NULL DEFAULT '1' COMMENT '1=体验版',
  `advanced_package` varchar(100) NOT NULL DEFAULT '体验版' COMMENT '套餐名称',
  `advanced_end_time` datetime DEFAULT NULL COMMENT '有效期',
  `advanced_package_id` bigint(20) unsigned NOT NULL DEFAULT '0',
  `advanced_package_level` tinyint(3) unsigned NOT NULL DEFAULT '0',
  `channel_sn` varchar(100) DEFAULT NULL COMMENT '渠道编号',
  `remark` text COMMENT '备注',
  PRIMARY KEY (`company_id`) USING BTREE,
  KEY `ai_company_user_id_IDX` (`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='企业资料';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ai_company`
--

LOCK TABLES `ai_company` WRITE;
/*!40000 ALTER TABLE `ai_company` DISABLE KEYS */;
INSERT INTO `ai_company` VALUES (1,1,NULL,'积木大脑',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,0,'2025-02-19 15:13:12',NULL,'2025-02-19 15:13:12',NULL,NULL,5,5,97,3,0,1000000,1,'体验版','2025-02-26 15:13:11',0,0,'',NULL);
/*!40000 ALTER TABLE `ai_company` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ai_document`
--

DROP TABLE IF EXISTS `ai_document`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ai_document` (
  `document_id` bigint(20) unsigned NOT NULL,
  `company_id` bigint(20) unsigned NOT NULL DEFAULT '0',
  `category_id` bigint(20) unsigned NOT NULL DEFAULT '0',
  `user_id` bigint(20) unsigned NOT NULL,
  `document_type` varchar(100) NOT NULL COMMENT '文档类型：pdf,txt,word,url,wechat	',
  `document_name` varchar(100) NOT NULL COMMENT '名称',
  `document_desc` text COMMENT '简介',
  `url` varchar(500) NOT NULL,
  `file_size` int(11) NOT NULL COMMENT '字节数',
  `status` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '状态：0=未处理；1=进行中；2=已完成；3=失败',
  `page_number` int(10) unsigned NOT NULL DEFAULT '1' COMMENT '页数',
  `remark` text COMMENT '备注，url抓取记录，错误记录',
  `create_time` datetime DEFAULT NULL,
  `create_by` varchar(100) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `update_by` varchar(100) DEFAULT NULL,
  `deleted` tinyint(3) unsigned NOT NULL DEFAULT '0',
  `split_char` varchar(10) DEFAULT NULL,
  `length` int(10) unsigned DEFAULT NULL,
  `split_name` varchar(100) DEFAULT NULL,
  `split_id` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`document_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='文档';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ai_document`
--

LOCK TABLES `ai_document` WRITE;
/*!40000 ALTER TABLE `ai_document` DISABLE KEYS */;
/*!40000 ALTER TABLE `ai_document` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ai_document_category`
--

DROP TABLE IF EXISTS `ai_document_category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ai_document_category` (
  `category_id` bigint(20) unsigned NOT NULL,
  `category_name` varchar(100) NOT NULL,
  `category_icon` varchar(100) DEFAULT NULL,
  `parent_id` bigint(20) unsigned NOT NULL DEFAULT '0',
  `create_time` varchar(100) DEFAULT NULL,
  `create_by` varchar(100) DEFAULT NULL,
  `update_time` varchar(100) DEFAULT NULL,
  `update_by` varchar(100) DEFAULT NULL,
  `deleted` tinyint(3) unsigned NOT NULL DEFAULT '0',
  `company_id` bigint(20) unsigned NOT NULL DEFAULT '0',
  `path` text NOT NULL,
  `user_id` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '创建人',
  `is_my` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '1=自己的分类，0=共享分类',
  PRIMARY KEY (`category_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='知识库分类';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ai_document_category`
--

LOCK TABLES `ai_document_category` WRITE;
/*!40000 ALTER TABLE `ai_document_category` DISABLE KEYS */;
/*!40000 ALTER TABLE `ai_document_category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ai_document_entity`
--

DROP TABLE IF EXISTS `ai_document_entity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ai_document_entity` (
  `id` bigint(20) unsigned NOT NULL,
  `document_id` bigint(20) unsigned NOT NULL,
  `entity_id` varchar(100) NOT NULL,
  `create_time` datetime DEFAULT NULL,
  `create_by` varchar(100) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `update_by` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='文档向量关系';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ai_document_entity`
--

LOCK TABLES `ai_document_entity` WRITE;
/*!40000 ALTER TABLE `ai_document_entity` DISABLE KEYS */;
/*!40000 ALTER TABLE `ai_document_entity` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ai_models`
--

DROP TABLE IF EXISTS `ai_models`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ai_models` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `model_name` varchar(100) NOT NULL,
  `platform_name` varchar(100) DEFAULT NULL,
  `model_label` varchar(100) NOT NULL,
  `is_default` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '1=default',
  `is_reasoning_default` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '推理默认模型',
  `icon` varchar(500) DEFAULT NULL COMMENT 'icon',
  `platform` varchar(100) NOT NULL,
  `create_time` datetime DEFAULT NULL,
  `create_by` varchar(100) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `update_by` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8mb4 COMMENT='模型';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ai_models`
--

LOCK TABLES `ai_models` WRITE;
/*!40000 ALTER TABLE `ai_models` DISABLE KEYS */;
/*!40000 ALTER TABLE `ai_models` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ai_models_config`
--

DROP TABLE IF EXISTS `ai_models_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ai_models_config` (
  `config_id` int(11) NOT NULL AUTO_INCREMENT,
  `platform` varchar(100) NOT NULL COMMENT '平台',
  `id` varchar(500) DEFAULT NULL,
  `key` text,
  `url` varchar(500) DEFAULT NULL,
  `secret` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`config_id`),
  UNIQUE KEY `ai_models_config_unique` (`platform`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COMMENT='模型配置';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ai_models_config`
--

LOCK TABLES `ai_models_config` WRITE;
/*!40000 ALTER TABLE `ai_models_config` DISABLE KEYS */;
INSERT INTO `ai_models_config` VALUES (1,'qian_fan',NULL,'',NULL,''),(2,'oneapi',NULL,'','',NULL),(3,'bing',NULL,NULL,'https://api.bing.microsoft.com/v7.0/search','');
/*!40000 ALTER TABLE `ai_models_config` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ai_prompt`
--

DROP TABLE IF EXISTS `ai_prompt`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ai_prompt` (
  `prompt_id` bigint(20) unsigned NOT NULL,
  `prompt_name` varchar(100) NOT NULL COMMENT '标题',
  `prompt_desc` varchar(200) DEFAULT NULL COMMENT '介绍',
  `prompt_type` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '提示器类型：0=普通助手，1=高级助手',
  `prompt_icon` varchar(500) DEFAULT NULL,
  `user_id` bigint(20) unsigned NOT NULL COMMENT '创建人',
  `init_prompt` text COMMENT '初始化提示语(发送给gpt的)',
  `init_message` varchar(500) DEFAULT NULL COMMENT '初始化提示语(用户看到的)',
  `is_public` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '1=公开',
  `is_share` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '是否可分享',
  `is_recommend` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '1=推荐',
  `is_new` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '1=最新',
  `is_context` tinyint(3) unsigned NOT NULL DEFAULT '1' COMMENT '1=使用上下文',
  `use_num` int(11) NOT NULL DEFAULT '0' COMMENT '使用人数',
  `model` varchar(100) DEFAULT NULL,
  `max_tokens` int(10) unsigned NOT NULL DEFAULT '1000',
  `temperature` decimal(2,1) DEFAULT NULL,
  `sort_number` int(11) NOT NULL DEFAULT '0' COMMENT '倒序',
  `category_id` bigint(20) unsigned DEFAULT NULL COMMENT '分类',
  `company_id` bigint(20) unsigned NOT NULL,
  `status` int(10) unsigned NOT NULL DEFAULT '1',
  `doc_category_ids` json DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `create_by` varchar(100) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `update_by` varchar(100) DEFAULT NULL,
  `deleted` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '2=已删除',
  `top_k` int(10) unsigned DEFAULT '4',
  `img_url` varchar(500) DEFAULT NULL COMMENT '头像',
  `background` varchar(500) DEFAULT NULL COMMENT '背景',
  `voice` varchar(100) DEFAULT NULL COMMENT '语音标识',
  `is_minprogram_share` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '1=小程序分享',
  `is_robot_share` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '1=机器人分享',
  `is_show_img` tinyint(3) unsigned NOT NULL DEFAULT '0',
  `is_doce_show` tinyint(3) unsigned NOT NULL DEFAULT '1',
  `repository_id` bigint(20) unsigned NOT NULL DEFAULT '0',
  `share_img_url` varchar(500) DEFAULT NULL,
  `is_window_star` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '1=开启评分',
  `theme_color` varchar(100) NOT NULL DEFAULT '#5657f6',
  `digit_person` bigint(20) unsigned DEFAULT NULL,
  `is_digit_person_share` tinyint(3) unsigned NOT NULL DEFAULT '0',
  `is_recommend_questions` tinyint(3) unsigned NOT NULL DEFAULT '0',
  `search_type` varchar(100) NOT NULL DEFAULT 'vector_search',
  `score` decimal(3,2) NOT NULL DEFAULT '0.50',
  `recommend_question_prompt` text,
  `workflow_id` varchar(100) DEFAULT NULL,
  `history_count` int(10) unsigned NOT NULL DEFAULT '6',
  `long_history` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '1=开启长期记忆',
  `is_rtc_share` tinyint(3) DEFAULT '0' COMMENT '实时语音分享',
  PRIMARY KEY (`prompt_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='提示器';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ai_prompt`
--

LOCK TABLES `ai_prompt` WRITE;
/*!40000 ALTER TABLE `ai_prompt` DISABLE KEYS */;
/*!40000 ALTER TABLE `ai_prompt` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ai_prompt_category`
--

DROP TABLE IF EXISTS `ai_prompt_category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ai_prompt_category` (
  `category_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `category_name` varchar(100) NOT NULL COMMENT '分类名称',
  `category_icon` varchar(100) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `create_by` varchar(100) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `update_by` varchar(100) DEFAULT NULL,
  `deleted` tinyint(3) unsigned NOT NULL DEFAULT '0',
  `company_id` bigint(20) unsigned NOT NULL DEFAULT '0',
  `sort_num` int(10) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`category_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1897092232257335298 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='提示器分类';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ai_prompt_category`
--

LOCK TABLES `ai_prompt_category` WRITE;
/*!40000 ALTER TABLE `ai_prompt_category` DISABLE KEYS */;
INSERT INTO `ai_prompt_category` VALUES (1895092474576711681,'商务办公',NULL,'2025-02-27 20:43:50',NULL,'2025-02-27 20:43:50',NULL,2,1,0),(1895092474702540801,'文案生成','','2025-02-27 20:43:50',NULL,'2025-03-05 11:06:05','admin',0,1,6),(1895092474786426881,'提效工具',NULL,'2025-02-27 20:43:50',NULL,'2025-02-27 20:43:50',NULL,0,1,0),(1895092475260383234,'文字处理',NULL,'2025-02-27 20:43:50',NULL,'2025-02-27 20:43:50',NULL,2,1,0),(1895092475671425026,'大语言模型','','2025-02-27 20:43:50',NULL,'2025-03-05 11:06:16','admin',2,1,5),(1895092476178935809,'休闲娱乐','','2025-02-27 20:43:50',NULL,'2025-03-05 11:05:58','admin',0,1,2),(1895092476585783297,'IT技术',NULL,'2025-02-27 20:43:50',NULL,'2025-02-27 20:43:50',NULL,0,1,0),(1897092232257335297,'新增分类','','2025-03-05 09:10:09','admin','2025-03-05 09:10:09','admin',0,1,1);
/*!40000 ALTER TABLE `ai_prompt_category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ai_prompt_document`
--

DROP TABLE IF EXISTS `ai_prompt_document`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ai_prompt_document` (
  `id` bigint(20) unsigned NOT NULL,
  `prompt_id` bigint(20) unsigned DEFAULT NULL,
  `document_id` bigint(20) unsigned DEFAULT NULL,
  `category_id` bigint(20) unsigned DEFAULT NULL,
  `create_time` varchar(100) DEFAULT NULL,
  `create_by` varchar(100) DEFAULT NULL,
  `update_time` varchar(100) DEFAULT NULL,
  `update_by` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `ai_prompt_document_prompt_id_IDX` (`prompt_id`,`document_id`,`category_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='提示器文档';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ai_prompt_document`
--

LOCK TABLES `ai_prompt_document` WRITE;
/*!40000 ALTER TABLE `ai_prompt_document` DISABLE KEYS */;
/*!40000 ALTER TABLE `ai_prompt_document` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ai_prompt_tips`
--

DROP TABLE IF EXISTS `ai_prompt_tips`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ai_prompt_tips` (
  `id` bigint(20) unsigned NOT NULL,
  `title` varchar(100) NOT NULL,
  `prompt_id` bigint(20) unsigned NOT NULL,
  `message` varchar(500) NOT NULL COMMENT '提示语',
  `create_time` datetime DEFAULT NULL,
  `create_by` varchar(100) DEFAULT NULL,
  `deleted` tinyint(4) NOT NULL DEFAULT '0',
  `update_by` varchar(100) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='提示器常用语';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ai_prompt_tips`
--

LOCK TABLES `ai_prompt_tips` WRITE;
/*!40000 ALTER TABLE `ai_prompt_tips` DISABLE KEYS */;
/*!40000 ALTER TABLE `ai_prompt_tips` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ai_recent`
--

DROP TABLE IF EXISTS `ai_recent`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ai_recent` (
  `user_id` bigint(20) unsigned NOT NULL,
  `prompt_id` bigint(20) unsigned NOT NULL,
  `create_timestamp` int(10) unsigned NOT NULL,
  PRIMARY KEY (`user_id`,`prompt_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='最近使用';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ai_recent`
--

LOCK TABLES `ai_recent` WRITE;
/*!40000 ALTER TABLE `ai_recent` DISABLE KEYS */;
/*!40000 ALTER TABLE `ai_recent` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ai_record`
--

DROP TABLE IF EXISTS `ai_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ai_record` (
  `message_id` bigint(20) unsigned NOT NULL,
  `company_id` bigint(20) unsigned NOT NULL DEFAULT '0',
  `window_id` bigint(20) unsigned NOT NULL,
  `user_id` bigint(20) unsigned NOT NULL,
  `content` longtext COMMENT '内容',
  `reasoning_content` longtext COMMENT '推理内容',
  `role` varchar(64) DEFAULT NULL COMMENT 'user=用户发送,assistant=机器人回复	',
  `is_init_message` tinyint(3) unsigned NOT NULL DEFAULT '0',
  `is_like` tinyint(3) unsigned NOT NULL DEFAULT '0',
  `create_by` varchar(64) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_by` varchar(64) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `create_timestamp` bigint(20) unsigned NOT NULL DEFAULT '0',
  `master_uuid` varchar(100) DEFAULT NULL,
  `doc_info` json DEFAULT NULL,
  `deleted` tinyint(3) unsigned NOT NULL DEFAULT '0',
  `prompt_id` bigint(20) unsigned DEFAULT NULL,
  `next_id` bigint(20) unsigned DEFAULT NULL COMMENT '回复的消息id',
  `member_id` bigint(20) unsigned DEFAULT NULL,
  `avatar` varchar(500) DEFAULT NULL,
  `is_error` tinyint(3) unsigned NOT NULL DEFAULT '0',
  `is_context` tinyint(4) NOT NULL DEFAULT '1',
  `type` varchar(100) NOT NULL DEFAULT 'text',
  `search_info` json DEFAULT NULL,
  `long_history` tinyint(3) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`message_id`) USING BTREE,
  KEY `chat_record_user_id_index` (`user_id`) USING BTREE,
  KEY `chat_record_window_id_index` (`window_id`) USING BTREE,
  KEY `chat_record_master_uuid_IDX` (`master_uuid`) USING BTREE,
  KEY `ai_record_long_history_IDX` (`long_history`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='聊天记录';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ai_record`
--

LOCK TABLES `ai_record` WRITE;
/*!40000 ALTER TABLE `ai_record` DISABLE KEYS */;
/*!40000 ALTER TABLE `ai_record` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ai_record_file`
--

DROP TABLE IF EXISTS `ai_record_file`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ai_record_file` (
  `uuid` varchar(100) NOT NULL,
  `file_type` varchar(100) DEFAULT NULL COMMENT '文件类型',
  `file_url` varchar(500) DEFAULT NULL COMMENT '文件url地址',
  `record_id` bigint(20) DEFAULT NULL,
  `file_name` varchar(100) DEFAULT NULL COMMENT '文件名',
  `icon` varchar(500) DEFAULT NULL COMMENT 'icon',
  `file_size` bigint(20) DEFAULT NULL COMMENT '文件大小',
  `content` longtext COMMENT '识别内容',
  `create_time` datetime DEFAULT NULL,
  `create_by` varchar(100) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `update_by` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='对话文件';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ai_record_file`
--

LOCK TABLES `ai_record_file` WRITE;
/*!40000 ALTER TABLE `ai_record_file` DISABLE KEYS */;
/*!40000 ALTER TABLE `ai_record_file` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ai_record_like`
--

DROP TABLE IF EXISTS `ai_record_like`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ai_record_like` (
  `id` bigint(20) unsigned NOT NULL,
  `message_id` bigint(20) unsigned NOT NULL,
  `user_id` bigint(20) unsigned NOT NULL,
  `content` text,
  `is_like` tinyint(3) unsigned NOT NULL,
  `create_time` datetime DEFAULT NULL,
  `create_by` varchar(100) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `update_by` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='聊天记录点赞不喜欢';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ai_record_like`
--

LOCK TABLES `ai_record_like` WRITE;
/*!40000 ALTER TABLE `ai_record_like` DISABLE KEYS */;
/*!40000 ALTER TABLE `ai_record_like` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ai_request_log`
--

DROP TABLE IF EXISTS `ai_request_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ai_request_log` (
  `uuid` char(36) NOT NULL,
  `user_id` bigint(20) unsigned NOT NULL,
  `company_id` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '企业id',
  `question` longtext,
  `member_id` bigint(20) unsigned NOT NULL DEFAULT '0',
  `key` varchar(100) DEFAULT NULL,
  `word_count` int(10) unsigned NOT NULL DEFAULT '0',
  `request_time` int(10) unsigned NOT NULL DEFAULT '0',
  `model` varchar(100) DEFAULT NULL,
  `error_message` text,
  `send_message` json DEFAULT NULL COMMENT '发送给gpt的数据',
  `receive_message` text COMMENT 'gpt返回的数据',
  `prompt_id` bigint(20) unsigned DEFAULT NULL,
  `ip` varchar(100) DEFAULT NULL,
  `user_agent` text,
  `create_time` datetime NOT NULL,
  `create_by` varchar(100) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `update_by` varchar(100) DEFAULT NULL,
  `integral` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '消耗积分',
  `function_name` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`uuid`) USING BTREE,
  KEY `ai_request_log_company_id_IDX` (`company_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='请求记录';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ai_request_log`
--

LOCK TABLES `ai_request_log` WRITE;
/*!40000 ALTER TABLE `ai_request_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `ai_request_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ai_split_rule`
--

DROP TABLE IF EXISTS `ai_split_rule`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ai_split_rule` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `split_char` varchar(10) NOT NULL,
  `length` int(10) unsigned NOT NULL,
  `name` varchar(100) NOT NULL,
  `create_time` datetime DEFAULT NULL,
  `create_by` varchar(100) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `update_by` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='拆分策略';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ai_split_rule`
--

LOCK TABLES `ai_split_rule` WRITE;
/*!40000 ALTER TABLE `ai_split_rule` DISABLE KEYS */;
/*!40000 ALTER TABLE `ai_split_rule` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ai_window`
--

DROP TABLE IF EXISTS `ai_window`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ai_window` (
  `window_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `company_id` bigint(20) unsigned NOT NULL DEFAULT '0',
  `user_id` bigint(20) unsigned NOT NULL,
  `window_name` varchar(100) DEFAULT NULL COMMENT '窗口名称',
  `talk_type` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '0=普通，1=测试，2=外部客服，3=工作流debug',
  `prompt_id` bigint(20) unsigned DEFAULT NULL,
  `customer_uuid` char(36) DEFAULT NULL COMMENT '外部连接code',
  `deleted` tinyint(3) unsigned NOT NULL DEFAULT '0',
  `create_by` varchar(64) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_by` varchar(64) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `member_id` bigint(20) unsigned NOT NULL DEFAULT '0',
  `star` decimal(2,1) DEFAULT NULL COMMENT '评分',
  `model` varchar(100) DEFAULT NULL COMMENT '测试模型',
  `uuid` varchar(100) NOT NULL DEFAULT '-' COMMENT '测试模型使用',
  `platform` varchar(100) NOT NULL DEFAULT 'pc',
  `common_user_id` varchar(100) DEFAULT NULL COMMENT '公共接口唯一用户id',
  PRIMARY KEY (`window_id`) USING BTREE,
  KEY `chat_window_user_id_index` (`user_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1897186935518625794 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='窗口';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ai_window`
--

LOCK TABLES `ai_window` WRITE;
/*!40000 ALTER TABLE `ai_window` DISABLE KEYS */;
/*!40000 ALTER TABLE `ai_window` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_config`
--

DROP TABLE IF EXISTS `sys_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_config` (
  `config_id` bigint(20) NOT NULL COMMENT '参数主键',
  `config_name` varchar(100) DEFAULT '' COMMENT '参数名称',
  `config_key` varchar(100) DEFAULT '' COMMENT '参数键名',
  `config_value` text COMMENT '参数键值',
  `config_type` char(1) DEFAULT 'N' COMMENT '系统内置（Y是 N否）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`config_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='参数配置表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_config`
--

LOCK TABLES `sys_config` WRITE;
/*!40000 ALTER TABLE `sys_config` DISABLE KEYS */;
INSERT INTO `sys_config` VALUES (1,'主框架页-默认皮肤样式名称','sys.index.skinName','skin-blue','Y','admin','2023-09-07 10:18:27','',NULL,'蓝色 skin-blue、绿色 skin-green、紫色 skin-purple、红色 skin-red、黄色 skin-yellow'),(2,'用户管理-账号初始密码','sys.user.initPassword','123456','Y','admin','2023-09-07 10:18:27','',NULL,'初始化密码 123456'),(3,'主框架页-侧边栏主题','sys.index.sideTheme','theme-dark','Y','admin','2023-09-07 10:18:27','',NULL,'深色主题theme-dark，浅色主题theme-light'),(4,'账号自助-验证码开关','sys.account.captchaEnabled','true','Y','admin','2023-09-07 10:18:27','',NULL,'是否开启验证码功能（true开启，false关闭）'),(5,'账号自助-是否开启用户注册功能','sys.account.registerUser','true','Y','admin','2023-09-07 10:18:27','admin','2023-09-07 15:30:05','是否开启注册用户功能（true开启，false关闭）'),(11,'OSS预览列表资源开关','sys.oss.previewListResource','true','Y','admin','2023-09-07 10:18:27','',NULL,'true:开启, false:关闭'),(1764894018063653456,'助手生成模板','prompt_template','{\"promptDescTemplate\":\"${name}\",\"initMessageTemplate\":\"您好，我是${name}，有什么可以帮您？\",\"initPromptTemplate\":\"# # Role:Prompt工程师\\n1. Don\'t break character under any circumstance.\\n2. Don\'t talk nonsense and make up facts.\\n\\n## Profile:\\n- Author:JEEMOO\\n- Version:1.0\\n- Language:中文\\n- Description:你是一名优秀的Prompt工程师，你熟悉[CRISPE提示框架]，并擅长将常规的Prompt转化为符合[CRISPE提示框架]的优秀Prompt，并输出符合预期的回复。\\n\\n## Constrains:\\n- Role: 基于我的Prompt，思考最适合扮演的1个或多个角色，该角色是这个领域最资深的专家，也最适合解决我的问题。\\n- Profile: 基于我的Prompt，思考我为什么会提出这个问题，陈述我提出这个问题的原因、背景、上下文。\\n- Goals: 基于我的Prompt，思考我需要提给chatGPT的任务清单，完成这些任务，便可以解决我的问题。\\n- Skill：基于我的Prompt，思考我需要提给chatGPT的任务清单，完成这些任务，便可以解决我的问题。\\n- OutputFormat: 基于我的Prompt，基于我OutputFormat实例进行输出。\\n- Workflow: 基于我的Prompt，要求提供几个不同的例子，更好的进行解释。\\n- Don\'t break character under any circumstance.\\n- Don\'t talk nonsense and make up facts.\\n\\n## Skill:\\n1. 熟悉[CRISPE提示框架]。\\n2. 能够将常规的Prompt转化为符合[CRISPE提示框架]的优秀Prompt。\\n\\n## Workflow:\\n1. 分析我的问题(Prompt)。\\n2. 根据[CRISPE提示框架]的要求，确定最适合扮演的角色。\\n3. 根据我的问题(Prompt)的原因、背景和上下文，构建一个符合[CRISPE提示框架]的优秀Prompt。\\n4. Workflow，基于我的问题进行写出Workflow，回复不低于5个步骤\\n5. Initialization，内容一定要是基于我提问的问题\\n6. 生成回复，确保回复符合预期。\\n\\n## OutputFormat:\\n    、、、\\n    # Role:角色名称\\n    \\n    ## Profile:\\n    - Author: YZFly\\n    - Version: 0.1\\n    - Language: 中文\\n    - Description: Describe your role. Give an overview of the character\'s characteristics and skills\\n    \\n    ### Skill:\\n    1.技能描述1\\n    2.技能描述2\\n    3.技能描述3\\n    4.技能描述4\\n    5.技能描述5\\n    \\n    ## Goals:\\n    1.目标1\\n    2.目标2\\n    3.目标3\\n    4.目标4\\n    5.目标5\\n    \\n    ## Constrains:\\n    1.约束条件1\\n    2.约束条件2\\n    3.约束条件3\\n    4.约束条件4\\n    5.约束条件5\\n    \\n    ## OutputFormat:\\n    1.输出要求1\\n    2.输出要求2\\n    3.输出要求3\\n    4.输出要求4\\n    5.输出要求5\\n    \\n    ## Workflow:\\n    1. First, xxx\\n    2. Then, xxx\\n    3. Finally, xxx\\n    \\n    ## Initialization:\\n    As a/an , you must follow the , you must talk to user in default ，you must greet the user. Then introduce yourself and introduce the .\\n    、、、\\n\\n## Initialization：\\n    接下来我会给出我的问题(Prompt)，请根据我的Prompt\\n    1.基于[CRISPE提示框架]，请一步一步进行输出，直到最终输出[优化Promot]；\\n    2.输出完毕之后，请咨询我是否有需要改进的意见，如果有建议，请结合建议重新基于[CRISPE提示框架]输出。\\n    要求：请避免讨论[CRISPE提示框架]里的内容；\\n    不需要重复内容，如果你准备好了，告诉我。\",\"model\":\"gpt-3.5-turbo-16k\",\"max_tokens\":4000}','Y','',NULL,'admin','2024-04-03 16:11:19',NULL),(1764894018063659009,'个人版赠送积分','person_integral','1000','Y','admin','2024-03-05 14:01:39','admin','2024-03-05 14:01:39',NULL),(1802537736012673026,'关于我们','sys.miniapp.about_us','积木网成立于2007年，专注企业数字化服务、2B/2C端应用解决方案，先后服务近千家企业客户，包括世界500强、行业龙头及中小企业客户。公司通过ISO9001质量管理认证、ISO27001信息安全管理认证、高新技术企业等。有专业AI模型训练及算法工程师，与国内外同行有广泛合作及AI应用开发经验。\n\n- 联系电话：4000-010-873\n- 联系邮箱：service@jeemoo.net\n- 联系地址：北京朝阳区八里桥南院传媒楼2层','Y','admin','2024-06-17 11:04:20','admin','2024-06-21 10:35:51',NULL),(1808683149635301378,'是否联网','is_need_search','1','Y','admin','2024-07-04 10:04:01','admin','2025-02-17 15:40:28',NULL),(1808683272062840834,'搜索结果提示词','search_prompt','# 以下内容是基于用户发送的消息的搜索结果:\\n{search_results}\\n在我给你的搜索结果中，每个结果都是[webpage X begin]...[webpage X end]格式的，X代表每篇文章的数字索引。请在适当的情况下在句子末尾引用上下文。请按照引用编号[citation:X]的格式在答案中对应部分引用上下文。如果一句话源自多个上下文，请列出所有相关的引用编号，例如[citation:3][citation:5]，切记不要将引用集中在最后返回引用编号，而是在答案对应部分列出。\\n在回答时，请注意以下几点：\\n- 今天是{cur_date}。\\n- 并非搜索结果的所有内容都与用户的问题密切相关，你需要结合问题，对搜索结果进行甄别、筛选。\\n- 对于列举类的问题（如列举所有航班信息），尽量将答案控制在10个要点以内，并告诉用户可以查看搜索来源、获得完整信息。优先提供信息完整、最相关的列举项；如非必要，不要主动告诉用户搜索结果未提供的内容。\\n- 对于创作类的问题（如写论文），请务必在正文的段落中引用对应的参考编号，例如[citation:3][citation:5]，不能只在文章末尾引用。你需要解读并概括用户的题目要求，选择合适的格式，充分利用搜索结果并抽取重要信息，生成符合用户要求、极具思想深度、富有创造力与专业性的答案。你的创作篇幅需要尽可能延长，对于每一个要点的论述要推测用户的意图，给出尽可能多角度的回答要点，且务必信息量大、论述详尽。\\n- 如果回答很长，请尽量结构化、分段落总结。如果需要分点作答，尽量控制在5个点以内，并合并相关的内容。\\n- 对于客观类的问答，如果问题的答案非常简短，可以适当补充一到两句相关信息，以丰富内容。\\n- 你需要根据用户要求和回答内容选择合适、美观的回答格式，确保可读性强。\\n- 你的回答应该综合多个相关网页来回答，不能重复引用一个网页。\\n- 除非用户要求，否则你回答的语言需要和用户提问的语言保持一致。\\n# 用户消息为：\\n{question}\\n','Y','admin','2024-07-04 10:04:30','admin','2025-02-17 14:57:32',NULL),(1808683665882820610,'搜索结果前缀','search_result_prefix','以下为实时联网搜索结果，请根据以下内容回答：','Y','admin','2024-07-04 10:06:04','admin','2024-07-04 10:06:04',NULL),(1808709860292780034,'推荐问题提示词','recommend_questions','任务：根据已提供回答和关键词，生成3个引导用户进行下一步提问的问句。避免与上一个问题意思重复。要求：- 每个问句都应该是一个完整的问题，并且尽量模拟真实用户的表达方式。-避免使用过于正式或技术性的语言，除非关键词本身具有这样的性质。- 问句应该具有一定的多样性，避免结构和用词过于相似。不要回复用户问题，不要回复用户问题，不要回复用户问题，返回json格式:{\"questions\":[]}','Y','admin','2024-07-04 11:50:10','admin','2024-07-10 11:20:51',NULL),(1891380185053814786,'文件提示词','file_prompt','[file name]: {file_name}\\n[file content begin]\\n{file_content}\\n[file content end]\\n{question}\\n','Y','admin','2025-02-17 14:52:31','admin','2025-02-17 14:56:42',NULL);
/*!40000 ALTER TABLE `sys_config` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_dept`
--

DROP TABLE IF EXISTS `sys_dept`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_dept` (
  `dept_id` bigint(20) NOT NULL COMMENT '部门id',
  `parent_id` bigint(20) DEFAULT '0' COMMENT '父部门id',
  `ancestors` varchar(500) DEFAULT '' COMMENT '祖级列表',
  `dept_name` varchar(30) DEFAULT '' COMMENT '部门名称',
  `order_num` int(4) DEFAULT '0' COMMENT '显示顺序',
  `leader` varchar(20) DEFAULT NULL COMMENT '负责人',
  `phone` varchar(11) DEFAULT NULL COMMENT '联系电话',
  `email` varchar(50) DEFAULT NULL COMMENT '邮箱',
  `status` char(1) DEFAULT '0' COMMENT '部门状态（0正常 1停用）',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`dept_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='部门表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_dept`
--

LOCK TABLES `sys_dept` WRITE;
/*!40000 ALTER TABLE `sys_dept` DISABLE KEYS */;
INSERT INTO `sys_dept` VALUES (100,0,'0','若依科技',0,'若依','15888888888','ry@qq.com','0','0','admin','2023-09-07 10:18:22','',NULL),(101,100,'0,100','深圳总公司',1,'若依','15888888888','ry@qq.com','0','0','admin','2023-09-07 10:18:22','',NULL),(102,100,'0,100','长沙分公司',2,'若依','15888888888','ry@qq.com','0','0','admin','2023-09-07 10:18:22','',NULL),(103,101,'0,100,101','研发部门',1,'若依','15888888888','ry@qq.com','0','0','admin','2023-09-07 10:18:22','',NULL),(104,101,'0,100,101','市场部门',2,'若依','15888888888','ry@qq.com','0','0','admin','2023-09-07 10:18:22','',NULL),(105,101,'0,100,101','测试部门',3,'若依','15888888888','ry@qq.com','0','0','admin','2023-09-07 10:18:22','',NULL),(106,101,'0,100,101','财务部门',4,'若依','15888888888','ry@qq.com','0','0','admin','2023-09-07 10:18:22','',NULL),(107,101,'0,100,101','运维部门',5,'若依','15888888888','ry@qq.com','0','0','admin','2023-09-07 10:18:22','',NULL),(108,102,'0,100,102','市场部门',1,'若依','15888888888','ry@qq.com','0','0','admin','2023-09-07 10:18:22','',NULL),(109,102,'0,100,102','财务部门',2,'若依','15888888888','ry@qq.com','0','0','admin','2023-09-07 10:18:22','',NULL);
/*!40000 ALTER TABLE `sys_dept` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_dict_data`
--

DROP TABLE IF EXISTS `sys_dict_data`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_dict_data` (
  `dict_code` bigint(20) NOT NULL COMMENT '字典编码',
  `dict_sort` int(4) DEFAULT '0' COMMENT '字典排序',
  `dict_label` varchar(100) DEFAULT '' COMMENT '字典标签',
  `dict_value` varchar(100) DEFAULT '' COMMENT '字典键值',
  `dict_type` varchar(100) DEFAULT '' COMMENT '字典类型',
  `css_class` varchar(100) DEFAULT NULL COMMENT '样式属性（其他样式扩展）',
  `list_class` varchar(100) DEFAULT NULL COMMENT '表格回显样式',
  `is_default` char(1) DEFAULT 'N' COMMENT '是否默认（Y是 N否）',
  `status` char(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`dict_code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='字典数据表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_dict_data`
--

LOCK TABLES `sys_dict_data` WRITE;
/*!40000 ALTER TABLE `sys_dict_data` DISABLE KEYS */;
INSERT INTO `sys_dict_data` VALUES (1,1,'男','0','sys_user_sex','','','Y','0','admin','2023-09-07 10:18:26','',NULL,'性别男'),(2,2,'女','1','sys_user_sex','','','N','0','admin','2023-09-07 10:18:26','',NULL,'性别女'),(3,3,'未知','2','sys_user_sex','','','N','0','admin','2023-09-07 10:18:26','',NULL,'性别未知'),(4,1,'显示','0','sys_show_hide','','primary','Y','0','admin','2023-09-07 10:18:26','',NULL,'显示菜单'),(5,2,'隐藏','1','sys_show_hide','','danger','N','0','admin','2023-09-07 10:18:26','',NULL,'隐藏菜单'),(6,1,'正常','0','sys_normal_disable','','primary','Y','0','admin','2023-09-07 10:18:26','',NULL,'正常状态'),(7,2,'停用','1','sys_normal_disable','','danger','N','0','admin','2023-09-07 10:18:26','',NULL,'停用状态'),(12,1,'是','Y','sys_yes_no','','primary','Y','0','admin','2023-09-07 10:18:27','',NULL,'系统默认是'),(13,2,'否','N','sys_yes_no','','danger','N','0','admin','2023-09-07 10:18:27','',NULL,'系统默认否'),(14,1,'通知','1','sys_notice_type','','warning','Y','0','admin','2023-09-07 10:18:27','',NULL,'通知'),(15,2,'公告','2','sys_notice_type','','success','N','0','admin','2023-09-07 10:18:27','',NULL,'公告'),(16,1,'正常','0','sys_notice_status','','primary','Y','0','admin','2023-09-07 10:18:27','',NULL,'正常状态'),(17,2,'关闭','1','sys_notice_status','','danger','N','0','admin','2023-09-07 10:18:27','',NULL,'关闭状态'),(18,1,'新增','1','sys_oper_type','','info','N','0','admin','2023-09-07 10:18:27','',NULL,'新增操作'),(19,2,'修改','2','sys_oper_type','','info','N','0','admin','2023-09-07 10:18:27','',NULL,'修改操作'),(20,3,'删除','3','sys_oper_type','','danger','N','0','admin','2023-09-07 10:18:27','',NULL,'删除操作'),(21,4,'授权','4','sys_oper_type','','primary','N','0','admin','2023-09-07 10:18:27','',NULL,'授权操作'),(22,5,'导出','5','sys_oper_type','','warning','N','0','admin','2023-09-07 10:18:27','',NULL,'导出操作'),(23,6,'导入','6','sys_oper_type','','warning','N','0','admin','2023-09-07 10:18:27','',NULL,'导入操作'),(24,7,'强退','7','sys_oper_type','','danger','N','0','admin','2023-09-07 10:18:27','',NULL,'强退操作'),(25,8,'生成代码','8','sys_oper_type','','warning','N','0','admin','2023-09-07 10:18:27','',NULL,'生成操作'),(26,9,'清空数据','9','sys_oper_type','','danger','N','0','admin','2023-09-07 10:18:27','',NULL,'清空操作'),(27,1,'成功','0','sys_common_status','','primary','N','0','admin','2023-09-07 10:18:27','',NULL,'正常状态'),(28,2,'失败','1','sys_common_status','','danger','N','0','admin','2023-09-07 10:18:27','',NULL,'停用状态'),(29,99,'其他','0','sys_oper_type','','info','N','0','admin','2023-09-07 10:18:27','',NULL,'其他操作'),(1700292057,0,'制造业','1700292057','industry_options',NULL,NULL,'N','0','','2023-11-14 12:30:02','','2023-11-14 12:30:02',NULL),(1702056807,0,'交通运输、仓储和邮政业','1702056807','industry_options',NULL,NULL,'N','0','','2023-11-14 12:30:02','','2023-11-14 12:30:02',NULL),(1702393715,0,'教育','1702393715','industry_options',NULL,NULL,'N','0','','2023-11-14 12:30:02','','2023-11-14 12:30:02',NULL),(1702442987,0,'信息传输、软件和信息技术服务业','1702442987','industry_options',NULL,NULL,'N','0','','2023-11-14 12:30:02','','2023-11-14 12:30:02',NULL),(1702881517,0,'其他服务业','default_indusrty','industry_options',NULL,NULL,'N','0','','2023-11-14 12:30:02','','2023-11-14 12:30:02',NULL),(1703524062,0,'水利、环境和公共设施管理业','1703524062','industry_options',NULL,NULL,'N','0','','2023-11-14 12:30:02','','2023-11-14 12:30:02',NULL),(1704212637,0,'批发和零售业','1704212637','industry_options',NULL,NULL,'N','0','','2023-11-14 12:30:02','','2023-11-14 12:30:02',NULL),(1704338210,0,'文化、体育和娱乐业','1704338210','industry_options',NULL,NULL,'N','0','','2023-11-14 12:30:02','','2023-11-14 12:30:02',NULL),(1705015294,0,'租赁和商务服务业','1705015294','industry_options',NULL,NULL,'N','0','','2023-11-14 12:30:02','','2023-11-14 12:30:02',NULL),(1706291111,0,'卫生和社会工作','1706291111','industry_options',NULL,NULL,'N','0','','2023-11-14 12:30:02','','2023-11-14 12:30:02',NULL),(1707709922,0,'住宿和餐饮业','1707709922','industry_options',NULL,NULL,'N','0','','2023-11-14 12:30:02','','2023-11-14 12:30:02',NULL),(1707807866,0,'采矿业','1707807866','industry_options',NULL,NULL,'N','0','','2023-11-14 12:30:02','','2023-11-14 12:30:02',NULL),(1708100486,0,'电力、燃气和水的生产和供应业','1708100486','industry_options',NULL,NULL,'N','0','','2023-11-14 12:30:02','','2023-11-14 12:30:02',NULL),(1708479680,0,'房地产业','1708479680','industry_options',NULL,NULL,'N','0','','2023-11-14 12:30:02','','2023-11-14 12:30:02',NULL),(1708580420,0,'居民服务、修理和其他服务业','1708580420','industry_options',NULL,NULL,'N','0','','2023-11-14 12:30:02','','2023-11-14 12:30:02',NULL),(1709148969,0,'金融业','1709148969','industry_options',NULL,NULL,'N','0','','2023-11-14 12:30:02','','2023-11-14 12:30:02',NULL),(1709603691,0,'农业、林业和渔业','1709603691','industry_options',NULL,NULL,'N','0','','2023-11-14 12:30:02','','2023-11-14 12:30:02',NULL),(1709690060,0,'建筑业','1709690060','industry_options',NULL,NULL,'N','0','','2023-11-14 12:30:02','','2023-11-14 12:30:02',NULL),(1709701229,0,'科学研究和技术服务业','1709701229','industry_options',NULL,NULL,'N','0','','2023-11-14 12:30:02','','2023-11-14 12:30:02',NULL),(1739841612732067841,0,'启用','1','status',NULL,'primary','N','0','admin','2023-12-27 10:52:20','admin','2023-12-27 10:52:20',NULL),(1739841662958858242,0,'禁用','0','status',NULL,'danger','N','0','admin','2023-12-27 10:52:32','admin','2023-12-27 10:52:32',NULL),(1862306820191387650,0,'Claude','Claude','model_platform_name',NULL,'default','N','0','admin','2024-11-29 09:25:21','admin','2024-11-29 11:44:41','https://openai-res.jeemoo.com/jeemooAi/2024/5/24/mp4j3nlkq87ag30zttngs3mkiuhs41j4.png'),(1862306852814684161,0,'DeepSeek','DeepSeek','model_platform_name',NULL,'default','N','0','admin','2024-11-29 09:25:28','admin','2024-11-29 11:44:00','https://openai-res.jeemoo.com/jeemooAi/2024/5/29/vdot78zo5rknaedxdbtkrji407sqoszi.png'),(1862306875526840322,0,'Llama','Llama','model_platform_name',NULL,'default','N','0','admin','2024-11-29 09:25:34','admin','2024-11-29 11:45:12','https://openai-res.jeemoo.com/jeemooAi/2024/7/23/fqr265y7dwslrpfrhe0i7osiw3ug0swt.jpeg'),(1862306899409207298,0,'MiniMax','MiniMax','model_platform_name',NULL,'default','N','0','admin','2024-11-29 09:25:39','admin','2024-11-29 11:44:15','https://openai-res.jeemoo.com/jeemooAi/2024/5/24/hjfg7pomm1abjlh8cxqdedlumf3j9yge.png'),(1862306919474757634,0,'Mistral','Mistral','model_platform_name',NULL,'default','N','0','admin','2024-11-29 09:25:44','admin','2024-11-29 11:44:25','https://openai-res.jeemoo.com/jeemooAi/2024/7/23/mt1soiaok7c7jku0rii6yy1e0cxhrkqk.png'),(1862306943034163201,0,'MoonShot','MoonShot','model_platform_name',NULL,'default','N','0','admin','2024-11-29 09:25:50','admin','2024-11-29 11:43:01','https://openai-res.jeemoo.com/jeemooAi/2024/5/24/eyuck92zw0iltget4okdtd4vv5xfl8ry.png'),(1862306965096202241,0,'积木大脑','积木大脑','model_platform_name',NULL,'default','N','0','admin','2024-11-29 09:25:55','admin','2024-11-29 11:43:12','https://openai-res.jeemoo.com/jeemooAi/2024/5/24/ybjczrffdnmn325gwpq3fz5nulimrgxf.jpeg'),(1862306986457792514,0,'千帆','千帆','model_platform_name',NULL,'default','N','0','admin','2024-11-29 09:26:00','admin','2024-11-29 11:45:00','https://openai-res.jeemoo.com/jeemooAi/2024/5/24/0yesesu1g5yltuntdb9gcnxhf00geio7.png'),(1862307010822504449,0,'千问','千问','model_platform_name',NULL,'default','N','0','admin','2024-11-29 09:26:06','admin','2024-11-29 11:43:48','https://openai-res.jeemoo.com/jeemooAi/2024/5/29/hh5wc060c0yq3q4zxdsb39dpz789qssw.png'),(1862307031538171905,0,'智普','智普','model_platform_name',NULL,'default','N','0','admin','2024-11-29 09:26:11','admin','2024-11-29 11:43:35','https://openai-res.jeemoo.com/jeemooAi/2024/5/24/frkm5bnaew4itm847bs8lt0rs8bh2jly.png'),(1862307052757155841,0,'百川','百川','model_platform_name',NULL,'default','N','0','admin','2024-11-29 09:26:16','admin','2024-11-29 11:43:25','https://openai-res.jeemoo.com/jeemooAi/2024/5/29/eb5ayqgopbx4oq885ry7xzv4wug0rgyi.png'),(1862307075096018946,0,'豆包','豆包','model_platform_name',NULL,'default','N','0','admin','2024-11-29 09:26:21','admin','2024-11-29 11:45:31','https://openai-res.jeemoo.com/jeemooAi/2024/7/5/8xs9126x3ansv8xf7r5qtz99nroig5ky.png'),(1862307095635525633,0,'零一万物','零一万物','model_platform_name',NULL,'default','N','0','admin','2024-11-29 09:26:26','admin','2024-11-29 11:42:47','https://openai-res.jeemoo.com/jeemooAi/2024/5/29/k7rxx1fvp8pguqsv5quj7fh863be49xo.png');
/*!40000 ALTER TABLE `sys_dict_data` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_dict_type`
--

DROP TABLE IF EXISTS `sys_dict_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_dict_type` (
  `dict_id` bigint(20) NOT NULL COMMENT '字典主键',
  `dict_name` varchar(100) DEFAULT '' COMMENT '字典名称',
  `dict_type` varchar(100) DEFAULT '' COMMENT '字典类型',
  `status` char(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`dict_id`) USING BTREE,
  UNIQUE KEY `dict_type` (`dict_type`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='字典类型表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_dict_type`
--

LOCK TABLES `sys_dict_type` WRITE;
/*!40000 ALTER TABLE `sys_dict_type` DISABLE KEYS */;
INSERT INTO `sys_dict_type` VALUES (1,'用户性别','sys_user_sex','0','admin','2023-09-07 10:18:26','',NULL,'用户性别列表'),(2,'菜单状态','sys_show_hide','0','admin','2023-09-07 10:18:26','',NULL,'菜单状态列表'),(3,'系统开关','sys_normal_disable','0','admin','2023-09-07 10:18:26','',NULL,'系统开关列表'),(6,'系统是否','sys_yes_no','0','admin','2023-09-07 10:18:26','',NULL,'系统是否列表'),(7,'通知类型','sys_notice_type','0','admin','2023-09-07 10:18:26','',NULL,'通知类型列表'),(8,'通知状态','sys_notice_status','0','admin','2023-09-07 10:18:26','',NULL,'通知状态列表'),(9,'操作类型','sys_oper_type','0','admin','2023-09-07 10:18:26','',NULL,'操作类型列表'),(10,'系统状态','sys_common_status','0','admin','2023-09-07 10:18:26','',NULL,'登录状态列表'),(1724281829071433730,'行业','industry_options','0','admin','2023-11-14 12:23:18','admin','2023-11-14 12:23:18','注册使用的行业列表'),(1862305175181164546,'模型平台','model_platform_name','0','admin','2024-11-29 09:18:48','admin','2024-11-29 09:18:48',NULL);
/*!40000 ALTER TABLE `sys_dict_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_logininfor`
--

DROP TABLE IF EXISTS `sys_logininfor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_logininfor` (
  `info_id` bigint(20) NOT NULL COMMENT '访问ID',
  `user_name` varchar(50) DEFAULT '' COMMENT '用户账号',
  `ipaddr` varchar(128) DEFAULT '' COMMENT '登录IP地址',
  `login_location` varchar(255) DEFAULT '' COMMENT '登录地点',
  `browser` varchar(50) DEFAULT '' COMMENT '浏览器类型',
  `os` varchar(50) DEFAULT '' COMMENT '操作系统',
  `status` char(1) DEFAULT '0' COMMENT '登录状态（0成功 1失败）',
  `msg` varchar(255) DEFAULT '' COMMENT '提示消息',
  `login_time` datetime DEFAULT NULL COMMENT '访问时间',
  PRIMARY KEY (`info_id`) USING BTREE,
  KEY `idx_sys_logininfor_s` (`status`) USING BTREE,
  KEY `idx_sys_logininfor_lt` (`login_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='系统访问记录';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_logininfor`
--

LOCK TABLES `sys_logininfor` WRITE;
/*!40000 ALTER TABLE `sys_logininfor` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_logininfor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_menu`
--

DROP TABLE IF EXISTS `sys_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_menu` (
  `menu_id` bigint(20) NOT NULL COMMENT '菜单ID',
  `menu_name` varchar(50) NOT NULL COMMENT '菜单名称',
  `parent_id` bigint(20) DEFAULT '0' COMMENT '父菜单ID',
  `order_num` int(4) DEFAULT '0' COMMENT '显示顺序',
  `path` varchar(200) DEFAULT '' COMMENT '路由地址',
  `component` varchar(255) DEFAULT NULL COMMENT '组件路径',
  `query_param` varchar(255) DEFAULT NULL COMMENT '路由参数',
  `is_frame` int(1) DEFAULT '1' COMMENT '是否为外链（0是 1否）',
  `is_cache` int(1) DEFAULT '0' COMMENT '是否缓存（0缓存 1不缓存）',
  `menu_type` char(1) DEFAULT '' COMMENT '菜单类型（M目录 C菜单 F按钮）',
  `visible` char(1) DEFAULT '0' COMMENT '显示状态（0显示 1隐藏）',
  `status` char(1) DEFAULT '0' COMMENT '菜单状态（0正常 1停用）',
  `perms` varchar(100) DEFAULT NULL COMMENT '权限标识',
  `icon` varchar(100) DEFAULT '#' COMMENT '菜单图标',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT '' COMMENT '备注',
  `is_company` tinyint(3) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`menu_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='菜单权限表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_menu`
--

LOCK TABLES `sys_menu` WRITE;
/*!40000 ALTER TABLE `sys_menu` DISABLE KEYS */;
INSERT INTO `sys_menu` VALUES (1,'系统管理',0,1000,'system',NULL,'',1,0,'M','0','0','','system','admin','2023-09-07 10:18:23','admin','2023-11-16 16:06:39','系统管理目录',0),(2,'系统监控',0,1002,'monitor',NULL,'',1,0,'M','0','0','','monitor','admin','2023-09-07 10:18:23','admin','2023-11-16 16:07:00','系统监控目录',0),(3,'系统工具',0,1001,'tool',NULL,'',1,0,'M','0','0','','tool','admin','2023-09-07 10:18:23','admin','2023-11-16 16:06:51','系统工具目录',0),(100,'用户管理',1,1,'user','system/user/index','',1,0,'C','0','0','system:user:list','user','admin','2023-09-07 10:18:23','',NULL,'用户管理菜单',0),(101,'角色管理',1,2,'role','system/role/index','',1,0,'C','0','0','system:role:list','peoples','admin','2023-09-07 10:18:23','',NULL,'角色管理菜单',0),(102,'菜单管理',1,3,'menu','system/menu/index','',1,0,'C','0','0','system:menu:list','tree-table','admin','2023-09-07 10:18:23','',NULL,'菜单管理菜单',0),(103,'部门管理',1,4,'dept','system/dept/index','',1,0,'C','0','0','system:dept:list','tree','admin','2023-09-07 10:18:23','',NULL,'部门管理菜单',0),(104,'岗位管理',1,5,'post','system/post/index','',1,0,'C','0','0','system:post:list','post','admin','2023-09-07 10:18:23','',NULL,'岗位管理菜单',0),(105,'字典管理',1,6,'dict','system/dict/index','',1,0,'C','0','0','system:dict:list','dict','admin','2023-09-07 10:18:23','',NULL,'字典管理菜单',0),(106,'参数设置',1,7,'config','system/config/index','',1,0,'C','0','0','system:config:list','edit','admin','2023-09-07 10:18:23','',NULL,'参数设置菜单',0),(107,'通知公告',1,8,'notice','system/notice/index','',1,0,'C','0','0','system:notice:list','message','admin','2023-09-07 10:18:23','',NULL,'通知公告菜单',0),(108,'日志管理',1,9,'log','','',1,0,'M','0','0','','log','admin','2023-09-07 10:18:23','',NULL,'日志管理菜单',0),(109,'在线用户',2,1,'online','monitor/online/index','',1,0,'C','0','0','monitor:online:list','online','admin','2023-09-07 10:18:23','',NULL,'在线用户菜单',0),(112,'缓存列表',2,6,'cacheList','monitor/cache/list','',1,0,'C','0','0','monitor:cache:list','redis-list','admin','2023-09-07 10:18:23','',NULL,'缓存列表菜单',0),(113,'缓存监控',2,5,'cache','monitor/cache/index','',1,0,'C','0','0','monitor:cache:list','redis','admin','2023-09-07 10:18:23','',NULL,'缓存监控菜单',0),(114,'表单构建',3,1,'build','tool/build/index','',1,0,'C','0','0','tool:build:list','build','admin','2023-09-07 10:18:23','',NULL,'表单构建菜单',0),(115,'代码生成',3,2,'gen','tool/gen/index','',1,0,'C','0','0','tool:gen:list','code','admin','2023-09-07 10:18:23','',NULL,'代码生成菜单',0),(117,'Admin监控',2,5,'Admin','monitor/admin/index','',1,0,'C','0','0','monitor:admin:list','dashboard','admin','2023-09-07 10:18:23','',NULL,'Admin监控菜单',0),(118,'文件管理',1,10,'oss','system/oss/index','',1,0,'C','0','0','system:oss:list','upload','admin','2023-09-07 10:18:23','',NULL,'文件管理菜单',0),(120,'任务调度中心',2,5,'XxlJob','monitor/xxljob/index','',1,0,'C','0','0','monitor:xxljob:list','job','admin','2023-09-07 10:18:23','',NULL,'Xxl-Job控制台菜单',0),(500,'操作日志',108,1,'operlog','monitor/operlog/index','',1,0,'C','0','0','monitor:operlog:list','form','admin','2023-09-07 10:18:23','',NULL,'操作日志菜单',0),(501,'登录日志',108,2,'logininfor','monitor/logininfor/index','',1,0,'C','0','0','monitor:logininfor:list','logininfor','admin','2023-09-07 10:18:23','',NULL,'登录日志菜单',0),(1001,'用户查询',100,1,'','','',1,0,'F','0','0','system:user:query','#','admin','2023-09-07 10:18:23','',NULL,'',0),(1002,'用户新增',100,2,'','','',1,0,'F','0','0','system:user:add','#','admin','2023-09-07 10:18:23','',NULL,'',0),(1003,'用户修改',100,3,'','','',1,0,'F','0','0','system:user:edit','#','admin','2023-09-07 10:18:23','',NULL,'',0),(1004,'用户删除',100,4,'','','',1,0,'F','0','0','system:user:remove','#','admin','2023-09-07 10:18:23','',NULL,'',0),(1005,'用户导出',100,5,'','','',1,0,'F','0','0','system:user:export','#','admin','2023-09-07 10:18:23','',NULL,'',0),(1006,'用户导入',100,6,'','','',1,0,'F','0','0','system:user:import','#','admin','2023-09-07 10:18:23','',NULL,'',0),(1007,'重置密码',100,7,'','','',1,0,'F','0','0','system:user:resetPwd','#','admin','2023-09-07 10:18:23','',NULL,'',0),(1008,'角色查询',101,1,'','','',1,0,'F','0','0','system:role:query','#','admin','2023-09-07 10:18:23','',NULL,'',0),(1009,'角色新增',101,2,'','','',1,0,'F','0','0','system:role:add','#','admin','2023-09-07 10:18:23','',NULL,'',0),(1010,'角色修改',101,3,'','','',1,0,'F','0','0','system:role:edit','#','admin','2023-09-07 10:18:23','',NULL,'',0),(1011,'角色删除',101,4,'','','',1,0,'F','0','0','system:role:remove','#','admin','2023-09-07 10:18:23','',NULL,'',0),(1012,'角色导出',101,5,'','','',1,0,'F','0','0','system:role:export','#','admin','2023-09-07 10:18:23','',NULL,'',0),(1013,'菜单查询',102,1,'','','',1,0,'F','0','0','system:menu:query','#','admin','2023-09-07 10:18:23','',NULL,'',0),(1014,'菜单新增',102,2,'','','',1,0,'F','0','0','system:menu:add','#','admin','2023-09-07 10:18:23','',NULL,'',0),(1015,'菜单修改',102,3,'','','',1,0,'F','0','0','system:menu:edit','#','admin','2023-09-07 10:18:23','',NULL,'',0),(1016,'菜单删除',102,4,'','','',1,0,'F','0','0','system:menu:remove','#','admin','2023-09-07 10:18:23','',NULL,'',0),(1017,'部门查询',103,1,'','','',1,0,'F','0','0','system:dept:query','#','admin','2023-09-07 10:18:23','',NULL,'',0),(1018,'部门新增',103,2,'','','',1,0,'F','0','0','system:dept:add','#','admin','2023-09-07 10:18:23','',NULL,'',0),(1019,'部门修改',103,3,'','','',1,0,'F','0','0','system:dept:edit','#','admin','2023-09-07 10:18:23','',NULL,'',0),(1020,'部门删除',103,4,'','','',1,0,'F','0','0','system:dept:remove','#','admin','2023-09-07 10:18:23','',NULL,'',0),(1021,'岗位查询',104,1,'','','',1,0,'F','0','0','system:post:query','#','admin','2023-09-07 10:18:23','',NULL,'',0),(1022,'岗位新增',104,2,'','','',1,0,'F','0','0','system:post:add','#','admin','2023-09-07 10:18:24','',NULL,'',0),(1023,'岗位修改',104,3,'','','',1,0,'F','0','0','system:post:edit','#','admin','2023-09-07 10:18:24','',NULL,'',0),(1024,'岗位删除',104,4,'','','',1,0,'F','0','0','system:post:remove','#','admin','2023-09-07 10:18:24','',NULL,'',0),(1025,'岗位导出',104,5,'','','',1,0,'F','0','0','system:post:export','#','admin','2023-09-07 10:18:24','',NULL,'',0),(1026,'字典查询',105,1,'#','','',1,0,'F','0','0','system:dict:query','#','admin','2023-09-07 10:18:24','',NULL,'',0),(1027,'字典新增',105,2,'#','','',1,0,'F','0','0','system:dict:add','#','admin','2023-09-07 10:18:24','',NULL,'',0),(1028,'字典修改',105,3,'#','','',1,0,'F','0','0','system:dict:edit','#','admin','2023-09-07 10:18:24','',NULL,'',0),(1029,'字典删除',105,4,'#','','',1,0,'F','0','0','system:dict:remove','#','admin','2023-09-07 10:18:24','',NULL,'',0),(1030,'字典导出',105,5,'#','','',1,0,'F','0','0','system:dict:export','#','admin','2023-09-07 10:18:24','',NULL,'',0),(1031,'参数查询',106,1,'#','','',1,0,'F','0','0','system:config:query','#','admin','2023-09-07 10:18:24','',NULL,'',0),(1032,'参数新增',106,2,'#','','',1,0,'F','0','0','system:config:add','#','admin','2023-09-07 10:18:24','',NULL,'',0),(1033,'参数修改',106,3,'#','','',1,0,'F','0','0','system:config:edit','#','admin','2023-09-07 10:18:24','',NULL,'',0),(1034,'参数删除',106,4,'#','','',1,0,'F','0','0','system:config:remove','#','admin','2023-09-07 10:18:24','',NULL,'',0),(1035,'参数导出',106,5,'#','','',1,0,'F','0','0','system:config:export','#','admin','2023-09-07 10:18:24','',NULL,'',0),(1036,'公告查询',107,1,'#','','',1,0,'F','0','0','system:notice:query','#','admin','2023-09-07 10:18:24','',NULL,'',0),(1037,'公告新增',107,2,'#','','',1,0,'F','0','0','system:notice:add','#','admin','2023-09-07 10:18:24','',NULL,'',0),(1038,'公告修改',107,3,'#','','',1,0,'F','0','0','system:notice:edit','#','admin','2023-09-07 10:18:24','',NULL,'',0),(1039,'公告删除',107,4,'#','','',1,0,'F','0','0','system:notice:remove','#','admin','2023-09-07 10:18:24','',NULL,'',0),(1040,'操作查询',500,1,'#','','',1,0,'F','0','0','monitor:operlog:query','#','admin','2023-09-07 10:18:24','',NULL,'',0),(1041,'操作删除',500,2,'#','','',1,0,'F','0','0','monitor:operlog:remove','#','admin','2023-09-07 10:18:24','',NULL,'',0),(1042,'日志导出',500,4,'#','','',1,0,'F','0','0','monitor:operlog:export','#','admin','2023-09-07 10:18:24','',NULL,'',0),(1043,'登录查询',501,1,'#','','',1,0,'F','0','0','monitor:logininfor:query','#','admin','2023-09-07 10:18:24','',NULL,'',0),(1044,'登录删除',501,2,'#','','',1,0,'F','0','0','monitor:logininfor:remove','#','admin','2023-09-07 10:18:24','',NULL,'',0);
/*!40000 ALTER TABLE `sys_menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_notice`
--

DROP TABLE IF EXISTS `sys_notice`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_notice` (
  `notice_id` bigint(20) NOT NULL COMMENT '公告ID',
  `notice_title` varchar(50) NOT NULL COMMENT '公告标题',
  `notice_type` char(1) NOT NULL COMMENT '公告类型（1通知 2公告）',
  `notice_content` longblob COMMENT '公告内容',
  `status` char(1) DEFAULT '0' COMMENT '公告状态（0正常 1关闭）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`notice_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='通知公告表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_notice`
--

LOCK TABLES `sys_notice` WRITE;
/*!40000 ALTER TABLE `sys_notice` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_notice` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_oper_log`
--

DROP TABLE IF EXISTS `sys_oper_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_oper_log` (
  `oper_id` bigint(20) NOT NULL COMMENT '日志主键',
  `title` varchar(50) DEFAULT '' COMMENT '模块标题',
  `business_type` int(2) DEFAULT '0' COMMENT '业务类型（0其它 1新增 2修改 3删除）',
  `method` varchar(100) DEFAULT '' COMMENT '方法名称',
  `request_method` varchar(10) DEFAULT '' COMMENT '请求方式',
  `operator_type` int(1) DEFAULT '0' COMMENT '操作类别（0其它 1后台用户 2手机端用户）',
  `oper_name` varchar(50) DEFAULT '' COMMENT '操作人员',
  `dept_name` varchar(50) DEFAULT '' COMMENT '部门名称',
  `oper_url` varchar(255) DEFAULT '' COMMENT '请求URL',
  `oper_ip` varchar(128) DEFAULT '' COMMENT '主机地址',
  `oper_location` varchar(255) DEFAULT '' COMMENT '操作地点',
  `oper_param` varchar(2000) DEFAULT '' COMMENT '请求参数',
  `json_result` varchar(2000) DEFAULT '' COMMENT '返回参数',
  `status` int(1) DEFAULT '0' COMMENT '操作状态（0正常 1异常）',
  `error_msg` varchar(2000) DEFAULT '' COMMENT '错误消息',
  `oper_time` datetime DEFAULT NULL COMMENT '操作时间',
  PRIMARY KEY (`oper_id`) USING BTREE,
  KEY `idx_sys_oper_log_bt` (`business_type`) USING BTREE,
  KEY `idx_sys_oper_log_s` (`status`) USING BTREE,
  KEY `idx_sys_oper_log_ot` (`oper_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='操作日志记录';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_oper_log`
--

LOCK TABLES `sys_oper_log` WRITE;
/*!40000 ALTER TABLE `sys_oper_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_oper_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_oss`
--

DROP TABLE IF EXISTS `sys_oss`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_oss` (
  `oss_id` bigint(20) NOT NULL COMMENT '对象存储主键',
  `file_name` varchar(255) NOT NULL DEFAULT '' COMMENT '文件名',
  `original_name` varchar(255) NOT NULL DEFAULT '' COMMENT '原名',
  `file_suffix` varchar(10) NOT NULL DEFAULT '' COMMENT '文件后缀名',
  `url` varchar(500) NOT NULL COMMENT 'URL地址',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_by` varchar(64) DEFAULT '' COMMENT '上传人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新人',
  `service` varchar(20) NOT NULL DEFAULT 'minio' COMMENT '服务商',
  PRIMARY KEY (`oss_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='OSS对象存储表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_oss`
--

LOCK TABLES `sys_oss` WRITE;
/*!40000 ALTER TABLE `sys_oss` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_oss` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_oss_config`
--

DROP TABLE IF EXISTS `sys_oss_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_oss_config` (
  `oss_config_id` bigint(20) NOT NULL COMMENT '主建',
  `config_key` varchar(20) NOT NULL DEFAULT '' COMMENT '配置key',
  `access_key` varchar(255) DEFAULT '' COMMENT 'accessKey',
  `secret_key` varchar(255) DEFAULT '' COMMENT '秘钥',
  `bucket_name` varchar(255) DEFAULT '' COMMENT '桶名称',
  `prefix` varchar(255) DEFAULT '' COMMENT '前缀',
  `endpoint` varchar(255) DEFAULT '' COMMENT '访问站点',
  `domain` varchar(255) DEFAULT '' COMMENT '自定义域名',
  `is_https` char(1) DEFAULT 'N' COMMENT '是否https（Y=是,N=否）',
  `region` varchar(255) DEFAULT '' COMMENT '域',
  `access_policy` char(1) NOT NULL DEFAULT '1' COMMENT '桶权限类型(0=private 1=public 2=custom)',
  `status` char(1) DEFAULT '1' COMMENT '是否默认（0=是,1=否）',
  `ext1` varchar(255) DEFAULT '' COMMENT '扩展字段',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`oss_config_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='对象存储配置表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_oss_config`
--

LOCK TABLES `sys_oss_config` WRITE;
/*!40000 ALTER TABLE `sys_oss_config` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_oss_config` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_post`
--

DROP TABLE IF EXISTS `sys_post`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_post` (
  `post_id` bigint(20) NOT NULL COMMENT '岗位ID',
  `post_code` varchar(64) NOT NULL COMMENT '岗位编码',
  `post_name` varchar(50) NOT NULL COMMENT '岗位名称',
  `post_sort` int(4) NOT NULL COMMENT '显示顺序',
  `status` char(1) NOT NULL COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`post_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='岗位信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_post`
--

LOCK TABLES `sys_post` WRITE;
/*!40000 ALTER TABLE `sys_post` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_post` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_role`
--

DROP TABLE IF EXISTS `sys_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_role` (
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `role_name` varchar(30) NOT NULL COMMENT '角色名称',
  `role_key` varchar(100) NOT NULL COMMENT '角色权限字符串',
  `role_sort` int(4) NOT NULL DEFAULT '0' COMMENT '显示顺序',
  `data_scope` char(1) DEFAULT '1' COMMENT '数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）',
  `menu_check_strictly` tinyint(1) DEFAULT '1' COMMENT '菜单树选择项是否关联显示',
  `dept_check_strictly` tinyint(1) DEFAULT '1' COMMENT '部门树选择项是否关联显示',
  `status` char(1) NOT NULL DEFAULT '0' COMMENT '角色状态（0正常 1停用）',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `company_id` bigint(20) unsigned DEFAULT '0',
  PRIMARY KEY (`role_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='角色信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_role`
--

LOCK TABLES `sys_role` WRITE;
/*!40000 ALTER TABLE `sys_role` DISABLE KEYS */;
INSERT INTO `sys_role` VALUES (1,'超级管理员','admin',1,'1',1,1,'0','0','admin','2023-09-07 10:18:23','',NULL,'超级管理员',0),(2,'普通角色','common',2,'2',1,1,'0','0','admin','2023-09-07 10:18:23','admin','2023-11-20 16:38:28','普通角色',0),(3,'企业管理员','company_manager',0,'1',1,1,'0','0','',NULL,'',NULL,NULL,1),(4,'企业员工','company_empylee',0,'1',1,1,'0','0','',NULL,'',NULL,NULL,1);
/*!40000 ALTER TABLE `sys_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_role_dept`
--

DROP TABLE IF EXISTS `sys_role_dept`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_role_dept` (
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `dept_id` bigint(20) NOT NULL COMMENT '部门ID',
  PRIMARY KEY (`role_id`,`dept_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='角色和部门关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_role_dept`
--

LOCK TABLES `sys_role_dept` WRITE;
/*!40000 ALTER TABLE `sys_role_dept` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_role_dept` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_role_menu`
--

DROP TABLE IF EXISTS `sys_role_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_role_menu` (
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `menu_id` bigint(20) NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`role_id`,`menu_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='角色和菜单关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_role_menu`
--

LOCK TABLES `sys_role_menu` WRITE;
/*!40000 ALTER TABLE `sys_role_menu` DISABLE KEYS */;
INSERT INTO `sys_role_menu` VALUES (2,1),(2,2),(2,3),(2,100),(2,101),(2,102),(2,103),(2,104),(2,105),(2,106),(2,107),(2,108),(2,109),(2,112),(2,113),(2,114),(2,115),(2,117),(2,118),(2,120),(2,500),(2,501),(2,1001),(2,1002),(2,1003),(2,1004),(2,1005),(2,1006),(2,1007),(2,1008),(2,1009),(2,1010),(2,1011),(2,1012),(2,1013),(2,1014),(2,1015),(2,1016),(2,1017),(2,1018),(2,1019),(2,1020),(2,1021),(2,1022),(2,1023),(2,1024),(2,1025),(2,1026),(2,1027),(2,1028),(2,1029),(2,1030),(2,1031),(2,1032),(2,1033),(2,1034),(2,1035),(2,1036),(2,1037),(2,1038),(2,1039),(2,1040),(2,1041),(2,1042),(2,1043),(2,1044),(2,1045),(2,1046),(2,1047),(2,1048),(2,1050),(2,1055),(2,1056),(2,1057),(2,1058),(2,1059),(2,1060),(2,1600),(2,1601),(2,1602),(2,1603),(2,1604),(2,1605),(2,1699673659745849345),(2,1699673659745849346),(2,1699673659745849347),(2,1699673659745849348),(2,1699673659745849349),(2,1699673659745849350),(2,1699673659980730370),(2,1699673659980730371),(2,1699673659980730372),(2,1699673659980730373),(2,1699673659980730374),(2,1699673659980730375),(2,1702969901827948546),(2,1702969901827948547),(2,1702969901827948548),(2,1702969901827948549),(2,1702969901827948550),(2,1702969901827948551),(2,1702974995478757377),(2,1702974995478757378),(2,1702974995478757379),(2,1702974995478757380),(2,1702974995478757381),(2,1702974995478757382),(2,1702980498686631938),(2,1702980498711797762),(2,1702980498711797763),(2,1702980498711797764),(2,1702980498711797765),(2,1702980498711797766),(2,1711564828476346369),(2,1711564828476346370),(2,1711564828476346371),(2,1711564828476346372),(2,1711564828476346373),(2,1711564828476346374),(2,1711564828681867266),(2,1711564828681867267),(2,1711564828681867268),(2,1711564828681867269),(2,1711564828681867270),(2,1711564828681867271),(2,1712031075202715649),(2,1712031075202715650),(2,1712031075202715651),(2,1712031075202715652),(2,1712031075202715653),(2,1712031075202715654),(2,1712031076205154305),(2,1712031076205154306),(2,1712031076205154307),(2,1712031076205154308),(2,1712031076205154309),(2,1712031076205154310),(2,1712737236453294082),(2,1712737817125322754),(2,1714164371067211777),(2,1714164371067211778),(2,1714164371067211779),(2,1714164371067211780),(2,1714164371067211781),(2,1714164371067211782),(2,1714487083660709890),(2,1714487083660709891),(2,1714487083660709892),(2,1714487083660709893),(2,1714487083660709894),(2,1714487083660709895),(2,1716742174111383554),(2,1716742174111383555),(2,1716742174111383556),(2,1716742174111383557),(2,1716742174111383558),(2,1716742174111383559),(2,1721734795303022593),(2,1721735147662307329),(2,1721735552295202817),(2,1722435862210732033),(2,1722435862210732034),(2,1722435862210732035),(2,1722435862210732036),(2,1722435862210732037),(2,1722435862210732038),(2,1725029268447444994),(2,1725030376930689026),(2,1725031792898359298),(2,1725032276480638978),(2,1725059478584762369),(2,1725061434195779586),(2,1725065005087809538),(2,1725337035618004993),(2,1725339793179947009),(1702549796442804226,1701860099223633922),(1702549796442804226,1701860572794109953),(1702549796442804226,1701860760673763329),(1702549796442804226,1701860927930023937),(1702549796442804226,1701861232440688641),(1702569842669944834,1701860099223633922),(1702569842669944834,1701860572794109953),(1702569842669944834,1701860760673763329),(1702569842669944834,1701860927930023937),(1702569842669944834,1701861232440688641),(1702569842669944834,1702988787809935361),(1702569842883854338,1701860099223633922),(1702569842883854338,1701860572794109953),(1702569842883854338,1701860760673763329),(1702569842883854338,1701860927930023937),(1702569842883854338,1702988787809935361),(1702569842883854338,1717397095118708737),(1702569842883854338,1717397178681827329),(1702573975196041218,1701860099223633922),(1702573975196041218,1701860572794109953),(1702573975196041218,1701860760673763329),(1702573975196041218,1701860927930023937),(1702573975196041218,1701861232440688641),(1702573975317676034,1701860099223633922),(1702573975317676034,1701860572794109953),(1702599386651045890,1701860099223633922),(1702599386651045890,1701860572794109953),(1702599386651045890,1701860760673763329),(1702599386651045890,1701860927930023937),(1702599386651045890,1701861232440688641),(1702599386831400961,1701860099223633922),(1702599386831400961,1701860572794109953),(1702947795799023618,1701860099223633922),(1702947795799023618,1701860572794109953),(1702947795799023618,1701860760673763329),(1702947795799023618,1701860927930023937),(1702947795799023618,1701861232440688641),(1702947796075847682,1701860099223633922),(1702947796075847682,1701860572794109953),(1702947796075847682,1701860760673763329),(1702947796075847682,1701860927930023937),(1702947796075847682,1702988787809935361),(1702947796075847682,1717397095118708737),(1702947796075847682,1717397178681827329),(1702947796075847682,1717397237041373186),(1702947796075847682,1717397290011238401),(1703649485489602562,1701860099223633922),(1703649485489602562,1701860572794109953),(1703649485489602562,1701860760673763329),(1703649485489602562,1701860927930023937),(1703649485489602562,1701861232440688641),(1703649485690929153,1701860099223633922),(1703649485690929153,1701860572794109953),(1703655747925213185,1701860099223633922),(1703655747925213185,1701860572794109953),(1703655747925213185,1701860760673763329),(1703655747925213185,1701860927930023937),(1703655747925213185,1701861232440688641),(1703655748013293569,1701860099223633922),(1703655748013293569,1701860572794109953),(1704338384943710209,1701860099223633922),(1704338384943710209,1701860572794109953),(1704338384943710209,1701860760673763329),(1704338384943710209,1701860927930023937),(1704338384943710209,1701861232440688641),(1704338385145036802,1701860099223633922),(1704338385145036802,1701860572794109953),(1704753887814250498,1701860099223633922),(1704753887814250498,1701860572794109953),(1704753887814250498,1701860760673763329),(1704753887814250498,1701860927930023937),(1704753887814250498,1701861232440688641),(1704753887977828354,1701860099223633922),(1704753887977828354,1701860572794109953),(1711626255367061505,1701860099223633922),(1711626255367061505,1701860572794109953),(1711626255367061505,1701860760673763329),(1711626255367061505,1701860927930023937),(1711626255367061505,1701861232440688641),(1711626255580971010,1701860099223633922),(1711626255580971010,1701860572794109953),(1712004671517896706,1701860099223633922),(1712004671517896706,1701860572794109953),(1712004671517896706,1701860760673763329),(1712004671517896706,1701860927930023937),(1712004671517896706,1701861232440688641),(1712004671731806209,1701860099223633922),(1712004671731806209,1701860572794109953),(1713750932726165505,1701860099223633922),(1713750932726165505,1701860572794109953),(1713750932726165505,1701860760673763329),(1713750932726165505,1701860927930023937),(1713750932726165505,1701861232440688641),(1713750932860383234,1701860099223633922),(1713750932860383234,1701860572794109953),(1722068575330988034,1701860099223633922),(1722068575330988034,1701860572794109953),(1722068575330988034,1701860760673763329),(1722068575330988034,1701860927930023937),(1722068575330988034,1701861232440688641),(1722068575330988034,1702988787809935361),(1722068575330988034,1717375493094182913),(1722068575330988034,1717375903955619842),(1722068575330988034,1717376015704461313),(1722068575330988034,1717376237285347329),(1722068575330988034,1717376332311498753),(1722068575330988034,1717376428121985026),(1722068575330988034,1717376533797474305),(1722068575330988034,1717376778317008898),(1722068575330988034,1717376818238394370),(1722068575330988034,1717376873708064770),(1722068575330988034,1717376971825418241),(1722068575330988034,1717377059587035137),(1722068575330988034,1717377133129961474),(1722068575330988034,1717377191489507330),(1722068575330988034,1717377242618073089),(1722068575330988034,1717377316458795009),(1722068575330988034,1717377419378626562),(1722068575330988034,1717377502996271105),(1722068575330988034,1717377558969257986),(1722068575330988034,1717377612291444738),(1722068575330988034,1717377664233705474),(1722068575330988034,1717377755103301633),(1722068575330988034,1717377875744067586),(1722068575330988034,1717378221648318465),(1722068575330988034,1717378430142976002),(1722068575330988034,1717378493791539201),(1722068575330988034,1717378630622318594),(1722068575330988034,1717378681243373569),(1722068575330988034,1717378725971431426),(1722068575330988034,1717378774604386306),(1722068575330988034,1717397095118708737),(1722068575330988034,1717397178681827329),(1722068575330988034,1717397237041373186),(1722068575330988034,1717397290011238401),(1722074367077609474,1701860099223633922),(1722074367077609474,1701860572794109953),(1722074367077609474,1701860760673763329),(1722074367077609474,1701860927930023937),(1722074367077609474,1701861232440688641),(1722074367207632897,1701860099223633922),(1722074367207632897,1701860572794109953),(1724277894273216513,1701860099223633922),(1724277894273216513,1701860572794109953),(1724277894273216513,1701860760673763329),(1724277894273216513,1701860927930023937),(1724277894273216513,1701861232440688641),(1724277894461960193,1701860099223633922),(1724277894461960193,1701860572794109953),(1724605909960118273,1701860099223633922),(1724605909960118273,1701860572794109953),(1724605909960118273,1701860760673763329),(1724605909960118273,1701860927930023937),(1724605909960118273,1701861232440688641),(1724605910136279041,1701860099223633922),(1724605910136279041,1701860572794109953),(1724671532505382914,1701860099223633922),(1724671532505382914,1701860572794109953),(1724671532505382914,1701860760673763329),(1724671532505382914,1701860927930023937),(1724671532505382914,1701861232440688641),(1724671532895453185,1701860099223633922),(1724671532895453185,1701860572794109953),(1724982437298515970,1701860099223633922),(1724982437298515970,1701860572794109953),(1724982437298515970,1701860760673763329),(1724982437298515970,1701860927930023937),(1724982437298515970,1701861232440688641),(1724982437298515970,1702988787809935361),(1724982437298515970,1717375493094182913),(1724982437298515970,1717375903955619842),(1724982437298515970,1717376015704461313),(1724982437298515970,1717376237285347329),(1724982437298515970,1717376332311498753),(1724982437298515970,1717376428121985026),(1724982437298515970,1717376533797474305),(1724982437298515970,1717376778317008898),(1724982437298515970,1717376818238394370),(1724982437298515970,1717376873708064770),(1724982437298515970,1717376971825418241),(1724982437298515970,1717377059587035137),(1724982437298515970,1717377133129961474),(1724982437298515970,1717377191489507330),(1724982437298515970,1717377242618073089),(1724982437298515970,1717377316458795009),(1724982437298515970,1717377419378626562),(1724982437298515970,1717377502996271105),(1724982437298515970,1717377558969257986),(1724982437298515970,1717377612291444738),(1724982437298515970,1717377664233705474),(1724982437298515970,1717377755103301633),(1724982437298515970,1717377875744067586),(1724982437298515970,1717378221648318465),(1724982437298515970,1717378430142976002),(1724982437298515970,1717378493791539201),(1724982437298515970,1717378630622318594),(1724982437298515970,1717378681243373569),(1724982437298515970,1717378725971431426),(1724982437298515970,1717378774604386306),(1724982437298515970,1717397095118708737),(1724982437298515970,1717397178681827329),(1724982437298515970,1717397237041373186),(1724982437298515970,1717397290011238401),(1724982577946112002,1701860099223633922),(1724982577946112002,1701860572794109953),(1724982577946112002,1701860760673763329),(1724982577946112002,1701860927930023937),(1724982577946112002,1702988787809935361),(1724982577946112002,1717397095118708737),(1724982577946112002,1717397178681827329),(1724982577946112002,1717397237041373186),(1724982577946112002,1717397290011238401),(1727945713845059585,1701860099223633922),(1727945713845059585,1701860572794109953),(1727945713845059585,1701860760673763329),(1727945713845059585,1701860927930023937),(1727945713845059585,1701861232440688641),(1727945713845059585,1702988787809935361),(1727945713845059585,1717375493094182913),(1727945713845059585,1717375903955619842),(1727945713845059585,1717376015704461313),(1727945713845059585,1717376237285347329),(1727945713845059585,1717376332311498753),(1727945713845059585,1717376428121985026),(1727945713845059585,1717376533797474305),(1727945713845059585,1717376778317008898),(1727945713845059585,1717376818238394370),(1727945713845059585,1717376873708064770),(1727945713845059585,1717376971825418241),(1727945713845059585,1717377059587035137),(1727945713845059585,1717377133129961474),(1727945713845059585,1717377191489507330),(1727945713845059585,1717377242618073089),(1727945713845059585,1717377316458795009),(1727945713845059585,1717377419378626562),(1727945713845059585,1717377502996271105),(1727945713845059585,1717377558969257986),(1727945713845059585,1717377612291444738),(1727945713845059585,1717377664233705474),(1727945713845059585,1717377755103301633),(1727945713845059585,1717377875744067586),(1727945713845059585,1717378221648318465),(1727945713845059585,1717378430142976002),(1727945713845059585,1717378493791539201),(1727945713845059585,1717378630622318594),(1727945713845059585,1717378681243373569),(1727945713845059585,1717378725971431426),(1727945713845059585,1717378774604386306),(1727945713845059585,1717397095118708737),(1727945713845059585,1717397178681827329),(1727945713845059585,1717397237041373186),(1727945713845059585,1717397290011238401),(1727945713845059585,1727902355944722434),(1727945713845059585,1727927505880739842),(1732676942016294914,1701860099223633922),(1732676942016294914,1701860572794109953),(1732676942016294914,1701860760673763329),(1732676942016294914,1701860927930023937),(1732676942016294914,1701861232440688641),(1732676942016294914,1702988787809935361),(1732676942016294914,1717375493094182913),(1732676942016294914,1717375903955619842),(1732676942016294914,1717376015704461313),(1732676942016294914,1717376237285347329),(1732676942016294914,1717376332311498753),(1732676942016294914,1717376428121985026),(1732676942016294914,1717376533797474305),(1732676942016294914,1717376778317008898),(1732676942016294914,1717376818238394370),(1732676942016294914,1717376873708064770),(1732676942016294914,1717376971825418241),(1732676942016294914,1717377059587035137),(1732676942016294914,1717377133129961474),(1732676942016294914,1717377191489507330),(1732676942016294914,1717377242618073089),(1732676942016294914,1717377316458795009),(1732676942016294914,1717377419378626562),(1732676942016294914,1717377502996271105),(1732676942016294914,1717377558969257986),(1732676942016294914,1717377612291444738),(1732676942016294914,1717377664233705474),(1732676942016294914,1717377755103301633),(1732676942016294914,1717377875744067586),(1732676942016294914,1717378221648318465),(1732676942016294914,1717378430142976002),(1732676942016294914,1717378493791539201),(1732676942016294914,1717378630622318594),(1732676942016294914,1717378681243373569),(1732676942016294914,1717378725971431426),(1732676942016294914,1717378774604386306),(1732676942016294914,1717397095118708737),(1732676942016294914,1717397178681827329),(1732676942016294914,1717397237041373186),(1732676942016294914,1717397290011238401),(1732676942016294914,1727902355944722434),(1732676942016294914,1727927505880739842),(1732676942016294914,1729350721668907009),(1732676942016294914,1729352010779852802),(1732676942016294914,1729352063217041410),(1732676942016294914,1729352131173154817),(1732676942016294914,1731867099609202689),(1732676942016294914,1731905348218183681),(1738411281370898434,1701860099223633922),(1738411281370898434,1701860572794109953),(1738411281370898434,1701860760673763329),(1738411281370898434,1701860927930023937),(1738411281370898434,1701861232440688641),(1738411281370898434,1702988787809935361),(1738411281370898434,1717375493094182913),(1738411281370898434,1717375903955619842),(1738411281370898434,1717376015704461313),(1738411281370898434,1717376237285347329),(1738411281370898434,1717376332311498753),(1738411281370898434,1717376428121985026),(1738411281370898434,1717376533797474305),(1738411281370898434,1717376778317008898),(1738411281370898434,1717376818238394370),(1738411281370898434,1717376873708064770),(1738411281370898434,1717376971825418241),(1738411281370898434,1717377059587035137),(1738411281370898434,1717377133129961474),(1738411281370898434,1717377191489507330),(1738411281370898434,1717377242618073089),(1738411281370898434,1717377316458795009),(1738411281370898434,1717377419378626562),(1738411281370898434,1717377502996271105),(1738411281370898434,1717377558969257986),(1738411281370898434,1717377612291444738),(1738411281370898434,1717377664233705474),(1738411281370898434,1717377755103301633),(1738411281370898434,1717377875744067586),(1738411281370898434,1717378221648318465),(1738411281370898434,1717378430142976002),(1738411281370898434,1717378493791539201),(1738411281370898434,1717378630622318594),(1738411281370898434,1717378681243373569),(1738411281370898434,1717378725971431426),(1738411281370898434,1717378774604386306),(1738411281370898434,1717397095118708737),(1738411281370898434,1717397178681827329),(1738411281370898434,1717397237041373186),(1738411281370898434,1717397290011238401),(1738411281370898434,1727902355944722434),(1738411281370898434,1727927505880739842),(1738411281370898434,1729350721668907009),(1738411281370898434,1729352010779852802),(1738411281370898434,1729352063217041410),(1738411281370898434,1729352131173154817),(1738411281370898434,1732958241348161538),(1739477581648166913,1739208277539188737),(1739477581648166913,1739208277539188738),(1739477581648166913,1739208277539188739),(1739477581648166913,1739208277539188740),(1739477581648166913,1739208277539188741),(1739477581648166913,1739208277539188742),(1740650903786344449,1701860099223633922),(1740650903786344449,1701860572794109953),(1740650903786344449,1701860760673763329),(1740650903786344449,1701860927930023937),(1740650903786344449,1701861232440688641),(1740650903786344449,1702988787809935361),(1740650903786344449,1717375493094182913),(1740650903786344449,1717375903955619842),(1740650903786344449,1717376015704461313),(1740650903786344449,1717376237285347329),(1740650903786344449,1717376332311498753),(1740650903786344449,1717376428121985026),(1740650903786344449,1717376533797474305),(1740650903786344449,1717376778317008898),(1740650903786344449,1717376818238394370),(1740650903786344449,1717376873708064770),(1740650903786344449,1717376971825418241),(1740650903786344449,1717377059587035137),(1740650903786344449,1717377133129961474),(1740650903786344449,1717377191489507330),(1740650903786344449,1717377242618073089),(1740650903786344449,1717377316458795009),(1740650903786344449,1717377419378626562),(1740650903786344449,1717377502996271105),(1740650903786344449,1717377558969257986),(1740650903786344449,1717377612291444738),(1740650903786344449,1717377664233705474),(1740650903786344449,1717377755103301633),(1740650903786344449,1717377875744067586),(1740650903786344449,1717378221648318465),(1740650903786344449,1717378430142976002),(1740650903786344449,1717378493791539201),(1740650903786344449,1717378630622318594),(1740650903786344449,1717378681243373569),(1740650903786344449,1717378725971431426),(1740650903786344449,1717378774604386306),(1740650903786344449,1717397095118708737),(1740650903786344449,1717397178681827329),(1740650903786344449,1717397237041373186),(1740650903786344449,1717397290011238401),(1740650903786344449,1727902355944722434),(1740650903786344449,1727927505880739842),(1740650903786344449,1729350721668907009),(1740650903786344449,1729352010779852802),(1740650903786344449,1729352063217041410),(1740650903786344449,1729352131173154817),(1740650903786344449,1731867099609202689),(1740650903786344449,1731905348218183681),(1740650903786344449,1732958241348161538),(1742067421157801986,1701860099223633922),(1742067421157801986,1701860572794109953),(1742067421157801986,1701860760673763329),(1742067421157801986,1701860927930023937),(1742067421157801986,1701861232440688641),(1742067421157801986,1702988787809935361),(1742067421157801986,1717375493094182913),(1742067421157801986,1717375903955619842),(1742067421157801986,1717376015704461313),(1742067421157801986,1717376237285347329),(1742067421157801986,1717376332311498753),(1742067421157801986,1717376428121985026),(1742067421157801986,1717376533797474305),(1742067421157801986,1717376778317008898),(1742067421157801986,1717376818238394370),(1742067421157801986,1717376873708064770),(1742067421157801986,1717376971825418241),(1742067421157801986,1717377059587035137),(1742067421157801986,1717377133129961474),(1742067421157801986,1717377191489507330),(1742067421157801986,1717377242618073089),(1742067421157801986,1717377316458795009),(1742067421157801986,1717377419378626562),(1742067421157801986,1717377502996271105),(1742067421157801986,1717377558969257986),(1742067421157801986,1717377612291444738),(1742067421157801986,1717377664233705474),(1742067421157801986,1717377755103301633),(1742067421157801986,1717377875744067586),(1742067421157801986,1717378221648318465),(1742067421157801986,1717378430142976002),(1742067421157801986,1717378493791539201),(1742067421157801986,1717378630622318594),(1742067421157801986,1717378681243373569),(1742067421157801986,1717378725971431426),(1742067421157801986,1717378774604386306),(1742067421157801986,1717397095118708737),(1742067421157801986,1717397178681827329),(1742067421157801986,1717397237041373186),(1742067421157801986,1717397290011238401),(1742067421157801986,1727902355944722434),(1742067421157801986,1727927505880739842),(1742067421157801986,1729350721668907009),(1742067421157801986,1729352010779852802),(1742067421157801986,1729352063217041410),(1742067421157801986,1729352131173154817),(1742067421157801986,1731867099609202689),(1742067421157801986,1731905348218183681),(1742067421157801986,1732958241348161538),(1744247404261351426,1701860099223633922),(1744247404261351426,1701860572794109953),(1744247404261351426,1701860760673763329),(1744247404261351426,1701860927930023937),(1744247404261351426,1701861232440688641),(1744247404261351426,1702988787809935361),(1744247404261351426,1717375493094182913),(1744247404261351426,1717375903955619842),(1744247404261351426,1717376015704461313),(1744247404261351426,1717376237285347329),(1744247404261351426,1717376332311498753),(1744247404261351426,1717376428121985026),(1744247404261351426,1717376533797474305),(1744247404261351426,1717376778317008898),(1744247404261351426,1717376818238394370),(1744247404261351426,1717376873708064770),(1744247404261351426,1717376971825418241),(1744247404261351426,1717377059587035137),(1744247404261351426,1717377133129961474),(1744247404261351426,1717377191489507330),(1744247404261351426,1717377242618073089),(1744247404261351426,1717377316458795009),(1744247404261351426,1717377419378626562),(1744247404261351426,1717377502996271105),(1744247404261351426,1717377558969257986),(1744247404261351426,1717377612291444738),(1744247404261351426,1717377664233705474),(1744247404261351426,1717377755103301633),(1744247404261351426,1717377875744067586),(1744247404261351426,1717378221648318465),(1744247404261351426,1717378430142976002),(1744247404261351426,1717378493791539201),(1744247404261351426,1717378630622318594),(1744247404261351426,1717378681243373569),(1744247404261351426,1717378725971431426),(1744247404261351426,1717378774604386306),(1744247404261351426,1717397095118708737),(1744247404261351426,1717397178681827329),(1744247404261351426,1717397237041373186),(1744247404261351426,1717397290011238401),(1744247404261351426,1727902355944722434),(1744247404261351426,1727927505880739842),(1744247404261351426,1729350721668907009),(1744247404261351426,1729352010779852802),(1744247404261351426,1729352063217041410),(1744247404261351426,1729352131173154817),(1744247404261351426,1731867099609202689),(1744247404261351426,1731905348218183681),(1744247404261351426,1732958241348161538),(1747526169280151553,1701860099223633922),(1747526169280151553,1701860572794109953),(1747526169280151553,1701860760673763329),(1747526169280151553,1701860927930023937),(1747526169280151553,1701861232440688641),(1747526169280151553,1702988787809935361),(1747526169280151553,1717375493094182913),(1747526169280151553,1717375903955619842),(1747526169280151553,1717376015704461313),(1747526169280151553,1717376237285347329),(1747526169280151553,1717376332311498753),(1747526169280151553,1717376428121985026),(1747526169280151553,1717376533797474305),(1747526169280151553,1717376778317008898),(1747526169280151553,1717376818238394370),(1747526169280151553,1717376873708064770),(1747526169280151553,1717376971825418241),(1747526169280151553,1717377059587035137),(1747526169280151553,1717377133129961474),(1747526169280151553,1717377191489507330),(1747526169280151553,1717377242618073089),(1747526169280151553,1717377316458795009),(1747526169280151553,1717377419378626562),(1747526169280151553,1717377502996271105),(1747526169280151553,1717377558969257986),(1747526169280151553,1717377612291444738),(1747526169280151553,1717377664233705474),(1747526169280151553,1717377755103301633),(1747526169280151553,1717377875744067586),(1747526169280151553,1717378221648318465),(1747526169280151553,1717378430142976002),(1747526169280151553,1717378493791539201),(1747526169280151553,1717378630622318594),(1747526169280151553,1717378681243373569),(1747526169280151553,1717378725971431426),(1747526169280151553,1717378774604386306),(1747526169280151553,1717397095118708737),(1747526169280151553,1717397178681827329),(1747526169280151553,1717397237041373186),(1747526169280151553,1717397290011238401),(1747526169280151553,1727902355944722434),(1747526169280151553,1727927505880739842),(1747526169280151553,1729350721668907009),(1747526169280151553,1729352010779852802),(1747526169280151553,1729352063217041410),(1747526169280151553,1729352131173154817),(1747526169280151553,1731867099609202689),(1747526169280151553,1731905348218183681),(1747526169280151553,1732958241348161538),(1747526169280151553,1744252406195396610),(1803681959498706946,1701860099223633922),(1803681959498706946,1701860572794109953),(1803681959498706946,1702988787809935361),(1805850316963307521,1701860099223633922),(1805850316963307521,1701860572794109953),(1805850316963307521,1701860760673763329),(1805850316963307521,1701860927930023937),(1805850316963307521,1701861232440688641),(1805850316963307521,1702988787809935361),(1805850316963307521,1717375493094182913),(1805850316963307521,1717375903955619842),(1805850316963307521,1717376015704461313),(1805850316963307521,1717376237285347329),(1805850316963307521,1717376332311498753),(1805850316963307521,1717376428121985026),(1805850316963307521,1717376533797474305),(1805850316963307521,1717376778317008898),(1805850316963307521,1717376818238394370),(1805850316963307521,1717376873708064770),(1805850316963307521,1717376971825418241),(1805850316963307521,1717377059587035137),(1805850316963307521,1717377133129961474),(1805850316963307521,1717377191489507330),(1805850316963307521,1717377242618073089),(1805850316963307521,1717377316458795009),(1805850316963307521,1717377419378626562),(1805850316963307521,1717377502996271105),(1805850316963307521,1717377558969257986),(1805850316963307521,1717377612291444738),(1805850316963307521,1717377664233705474),(1805850316963307521,1717377755103301633),(1805850316963307521,1717377875744067586),(1805850316963307521,1717378221648318465),(1805850316963307521,1717378430142976002),(1805850316963307521,1717378493791539201),(1805850316963307521,1717378630622318594),(1805850316963307521,1717378681243373569),(1805850316963307521,1717378725971431426),(1805850316963307521,1717378774604386306),(1805850316963307521,1717397095118708737),(1805850316963307521,1717397178681827329),(1805850316963307521,1717397237041373186),(1805850316963307521,1717397290011238401),(1805850316963307521,1727902355944722434),(1805850316963307521,1727927505880739842),(1805850316963307521,1729350721668907009),(1805850316963307521,1729352010779852802),(1805850316963307521,1729352063217041410),(1805850316963307521,1729352131173154817),(1805850316963307521,1731867099609202689),(1805850316963307521,1731905348218183681),(1805850316963307521,1732958241348161538),(1805850316963307521,1744252406195396610),(1805850316963307521,1802911758885830657),(1805850316963307521,1802912082296029186),(1805850316963307521,1802912179947814914),(1805850316963307521,1802912237430751233),(1805850316963307521,1802912294620086273),(1805850316963307521,1802916939551719426),(1805850316963307521,1802944658419204097),(1805850316963307521,1802944736143851522),(1805850316963307521,1802944796197896194),(1805850316963307521,1802944903597244417),(1805850316963307521,1802945055691096066),(1805850316963307521,1802945130123214849),(1805850316963307521,1802945187539042306),(1805850316963307521,1802945245399465985),(1821799495713902593,1701860099223633922),(1821799495713902593,1701860572794109953),(1821799495713902593,1701860760673763329),(1821799495713902593,1701860927930023937),(1821799495713902593,1701861232440688641),(1821799495713902593,1702988787809935361),(1821799495713902593,1717375493094182913),(1821799495713902593,1717375903955619842),(1821799495713902593,1717376015704461313),(1821799495713902593,1717376237285347329),(1821799495713902593,1717376332311498753),(1821799495713902593,1717376428121985026),(1821799495713902593,1717376533797474305),(1821799495713902593,1717376778317008898),(1821799495713902593,1717376818238394370),(1821799495713902593,1717376873708064770),(1821799495713902593,1717376971825418241),(1821799495713902593,1717377059587035137),(1821799495713902593,1717377133129961474),(1821799495713902593,1717377191489507330),(1821799495713902593,1717377242618073089),(1821799495713902593,1717377316458795009),(1821799495713902593,1717377419378626562),(1821799495713902593,1717377502996271105),(1821799495713902593,1717377558969257986),(1821799495713902593,1717377612291444738),(1821799495713902593,1717377664233705474),(1821799495713902593,1717377755103301633),(1821799495713902593,1717377875744067586),(1821799495713902593,1717378221648318465),(1821799495713902593,1717378430142976002),(1821799495713902593,1717378493791539201),(1821799495713902593,1717378630622318594),(1821799495713902593,1717378681243373569),(1821799495713902593,1717378725971431426),(1821799495713902593,1717378774604386306),(1821799495713902593,1717397095118708737),(1821799495713902593,1717397178681827329),(1821799495713902593,1717397237041373186),(1821799495713902593,1717397290011238401),(1821799495713902593,1727902355944722434),(1821799495713902593,1727927505880739842),(1821799495713902593,1729350721668907009),(1821799495713902593,1729352010779852802),(1821799495713902593,1729352063217041410),(1821799495713902593,1729352131173154817),(1821799495713902593,1731867099609202689),(1821799495713902593,1731905348218183681),(1821799495713902593,1732958241348161538),(1821799495713902593,1744252406195396610),(1821799495713902593,1802911758885830657),(1821799495713902593,1802912082296029186),(1821799495713902593,1802912179947814914),(1821799495713902593,1802912237430751233),(1821799495713902593,1802912294620086273),(1821799495713902593,1802916939551719426),(1821799495713902593,1802944658419204097),(1821799495713902593,1802944736143851522),(1821799495713902593,1802944796197896194),(1821799495713902593,1802944903597244417),(1821799495713902593,1802945055691096066),(1821799495713902593,1802945130123214849),(1821799495713902593,1802945187539042306),(1821799495713902593,1802945245399465985),(1825773378552860674,1701860099223633922),(1825773378552860674,1701860572794109953),(1825773378552860674,1701860760673763329),(1825773378552860674,1701860927930023937),(1825773378552860674,1701861232440688641),(1825773378552860674,1702988787809935361),(1825773378552860674,1717375493094182913),(1825773378552860674,1717375903955619842),(1825773378552860674,1717376015704461313),(1825773378552860674,1717376237285347329),(1825773378552860674,1717376332311498753),(1825773378552860674,1717376428121985026),(1825773378552860674,1717376533797474305),(1825773378552860674,1717376778317008898),(1825773378552860674,1717376818238394370),(1825773378552860674,1717376873708064770),(1825773378552860674,1717376971825418241),(1825773378552860674,1717377059587035137),(1825773378552860674,1717377133129961474),(1825773378552860674,1717377191489507330),(1825773378552860674,1717377242618073089),(1825773378552860674,1717377316458795009),(1825773378552860674,1717377419378626562),(1825773378552860674,1717377502996271105),(1825773378552860674,1717377558969257986),(1825773378552860674,1717377612291444738),(1825773378552860674,1717377664233705474),(1825773378552860674,1717377755103301633),(1825773378552860674,1717377875744067586),(1825773378552860674,1717378221648318465),(1825773378552860674,1717378430142976002),(1825773378552860674,1717378493791539201),(1825773378552860674,1717378630622318594),(1825773378552860674,1717378681243373569),(1825773378552860674,1717378725971431426),(1825773378552860674,1717378774604386306),(1825773378552860674,1717397095118708737),(1825773378552860674,1717397178681827329),(1825773378552860674,1717397237041373186),(1825773378552860674,1717397290011238401),(1825773378552860674,1727902355944722434),(1825773378552860674,1727927505880739842),(1825773378552860674,1729350721668907009),(1825773378552860674,1729352010779852802),(1825773378552860674,1729352063217041410),(1825773378552860674,1729352131173154817),(1825773378552860674,1731867099609202689),(1825773378552860674,1731905348218183681),(1825773378552860674,1732958241348161538),(1825773378552860674,1744252406195396610),(1825773378552860674,1802911758885830657),(1825773378552860674,1802912082296029186),(1825773378552860674,1802912179947814914),(1825773378552860674,1802912237430751233),(1825773378552860674,1802912294620086273),(1825773378552860674,1802916939551719426),(1825773378552860674,1802944658419204097),(1825773378552860674,1802944736143851522),(1825773378552860674,1802944796197896194),(1825773378552860674,1802944903597244417),(1825773378552860674,1802945055691096066),(1825773378552860674,1802945130123214849),(1825773378552860674,1802945187539042306),(1825773378552860674,1802945245399465985),(1849023106647924738,1701860099223633922),(1849023106647924738,1701860572794109953),(1849023106647924738,1701860760673763329),(1849023106647924738,1701860927930023937),(1849023106647924738,1701861232440688641),(1849023106647924738,1702988787809935361),(1849023106647924738,1717375493094182913),(1849023106647924738,1717375903955619842),(1849023106647924738,1717376015704461313),(1849023106647924738,1717376237285347329),(1849023106647924738,1717376332311498753),(1849023106647924738,1717376428121985026),(1849023106647924738,1717376533797474305),(1849023106647924738,1717376778317008898),(1849023106647924738,1717376818238394370),(1849023106647924738,1717376873708064770),(1849023106647924738,1717376971825418241),(1849023106647924738,1717377059587035137),(1849023106647924738,1717377133129961474),(1849023106647924738,1717377191489507330),(1849023106647924738,1717377242618073089),(1849023106647924738,1717377316458795009),(1849023106647924738,1717377419378626562),(1849023106647924738,1717377502996271105),(1849023106647924738,1717377558969257986),(1849023106647924738,1717377612291444738),(1849023106647924738,1717377664233705474),(1849023106647924738,1717377755103301633),(1849023106647924738,1717377875744067586),(1849023106647924738,1717378221648318465),(1849023106647924738,1717378430142976002),(1849023106647924738,1717378493791539201),(1849023106647924738,1717378630622318594),(1849023106647924738,1717378681243373569),(1849023106647924738,1717378725971431426),(1849023106647924738,1717378774604386306),(1849023106647924738,1717397095118708737),(1849023106647924738,1717397178681827329),(1849023106647924738,1717397237041373186),(1849023106647924738,1717397290011238401),(1849023106647924738,1727902355944722434),(1849023106647924738,1727927505880739842),(1849023106647924738,1729350721668907009),(1849023106647924738,1729352010779852802),(1849023106647924738,1729352063217041410),(1849023106647924738,1729352131173154817),(1849023106647924738,1731867099609202689),(1849023106647924738,1731905348218183681),(1849023106647924738,1732958241348161538),(1849023106647924738,1744252406195396610),(1849023106647924738,1802911758885830657),(1849023106647924738,1802912082296029186),(1849023106647924738,1802912179947814914),(1849023106647924738,1802912237430751233),(1849023106647924738,1802912294620086273),(1849023106647924738,1802916939551719426),(1849023106647924738,1802944658419204097),(1849023106647924738,1802944736143851522),(1849023106647924738,1802944796197896194),(1849023106647924738,1802944903597244417),(1849023106647924738,1802945055691096066),(1849023106647924738,1802945130123214849),(1849023106647924738,1802945187539042306),(1849023106647924738,1802945245399465985),(1892110164842573826,1701860099223633922),(1892110164842573826,1701860572794109953),(1892110164842573826,1701860760673763329),(1892110164842573826,1701860927930023937),(1892110164842573826,1701861232440688641),(1892110164842573826,1702988787809935361),(1892110164842573826,1717375493094182913),(1892110164842573826,1717375903955619842),(1892110164842573826,1717376015704461313),(1892110164842573826,1717376237285347329),(1892110164842573826,1717376332311498753),(1892110164842573826,1717376428121985026),(1892110164842573826,1717376533797474305),(1892110164842573826,1717376778317008898),(1892110164842573826,1717376818238394370),(1892110164842573826,1717376873708064770),(1892110164842573826,1717376971825418241),(1892110164842573826,1717377059587035137),(1892110164842573826,1717377133129961474),(1892110164842573826,1717377191489507330),(1892110164842573826,1717377242618073089),(1892110164842573826,1717377316458795009),(1892110164842573826,1717377419378626562),(1892110164842573826,1717377502996271105),(1892110164842573826,1717377558969257986),(1892110164842573826,1717377612291444738),(1892110164842573826,1717377664233705474),(1892110164842573826,1717377755103301633),(1892110164842573826,1717377875744067586),(1892110164842573826,1717378221648318465),(1892110164842573826,1717378430142976002),(1892110164842573826,1717378493791539201),(1892110164842573826,1717378630622318594),(1892110164842573826,1717378681243373569),(1892110164842573826,1717378725971431426),(1892110164842573826,1717378774604386306),(1892110164842573826,1717397095118708737),(1892110164842573826,1717397178681827329),(1892110164842573826,1717397237041373186),(1892110164842573826,1717397290011238401),(1892110164842573826,1727902355944722434),(1892110164842573826,1727927505880739842),(1892110164842573826,1729350721668907009),(1892110164842573826,1729352010779852802),(1892110164842573826,1729352063217041410),(1892110164842573826,1729352131173154817),(1892110164842573826,1731867099609202689),(1892110164842573826,1731905348218183681),(1892110164842573826,1732958241348161538),(1892110164842573826,1744252406195396610),(1892110164842573826,1802911758885830657),(1892110164842573826,1802912082296029186),(1892110164842573826,1802912179947814914),(1892110164842573826,1802912237430751233),(1892110164842573826,1802912294620086273),(1892110164842573826,1802916939551719426),(1892110164842573826,1802944658419204097),(1892110164842573826,1802944736143851522),(1892110164842573826,1802944796197896194),(1892110164842573826,1802944903597244417),(1892110164842573826,1802945055691096066),(1892110164842573826,1802945130123214849),(1892110164842573826,1802945187539042306),(1892110164842573826,1802945245399465985),(1892110770307133441,1701860099223633922),(1892110770307133441,1701860760673763329),(1892110770307133441,1701860927930023937),(1892110770307133441,1702988787809935361),(1892110770307133441,1717377059587035137),(1892110770307133441,1717377133129961474),(1892110770307133441,1717377191489507330),(1892110770307133441,1717377242618073089),(1892110770307133441,1717377316458795009),(1892110770307133441,1717397095118708737),(1892110770307133441,1717397178681827329),(1892110770307133441,1717397237041373186),(1892110770307133441,1717397290011238401),(1892110770307133441,1729350721668907009),(1892110770307133441,1729352010779852802),(1892110770307133441,1729352063217041410),(1892110770307133441,1729352131173154817),(1892110770307133441,1732958241348161538);
/*!40000 ALTER TABLE `sys_role_menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_user`
--

DROP TABLE IF EXISTS `sys_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_user` (
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `dept_id` bigint(20) DEFAULT NULL COMMENT '部门ID',
  `user_name` varchar(100) NOT NULL COMMENT '用户账号',
  `nick_name` varchar(200) NOT NULL COMMENT '用户昵称',
  `user_type` varchar(10) DEFAULT 'sys_user' COMMENT '用户类型（sys_user系统用户）',
  `email` varchar(50) DEFAULT '' COMMENT '用户邮箱',
  `phonenumber` varchar(11) DEFAULT '' COMMENT '手机号码',
  `sex` char(1) DEFAULT '0' COMMENT '用户性别（0男 1女 2未知）',
  `avatar` varchar(500) DEFAULT '' COMMENT '头像地址',
  `password` varchar(100) DEFAULT '' COMMENT '密码',
  `status` char(1) DEFAULT '0' COMMENT '帐号状态（0正常 1停用）',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `login_ip` varchar(128) DEFAULT '' COMMENT '最后登录IP',
  `login_date` datetime DEFAULT NULL COMMENT '最后登录时间',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `company_name` varchar(100) DEFAULT NULL,
  `company_id` bigint(20) unsigned NOT NULL DEFAULT '0',
  `is_alert` tinyint(3) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='用户信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user`
--

LOCK TABLES `sys_user` WRITE;
/*!40000 ALTER TABLE `sys_user` DISABLE KEYS */;
INSERT INTO `sys_user` VALUES (1,103,'admin','admin','app_user','admin@jeemoo.net','15888888888','0','/logo.png','$2a$10$G5Uv76UYV7SltkcdotXzhuFKm41lNXRUPPURREuIDjpIrbgLqzXUa','0','0','120.244.6.96','2025-03-05 15:26:54','admin','2023-09-07 10:18:22','admin','2025-03-05 15:26:54','管理员','积木大脑',1,1);
/*!40000 ALTER TABLE `sys_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_user_post`
--

DROP TABLE IF EXISTS `sys_user_post`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_user_post` (
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `post_id` bigint(20) NOT NULL COMMENT '岗位ID',
  PRIMARY KEY (`user_id`,`post_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='用户与岗位关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user_post`
--

LOCK TABLES `sys_user_post` WRITE;
/*!40000 ALTER TABLE `sys_user_post` DISABLE KEYS */;
INSERT INTO `sys_user_post` VALUES (1,1),(2,2);
/*!40000 ALTER TABLE `sys_user_post` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_user_role`
--

DROP TABLE IF EXISTS `sys_user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_user_role` (
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`user_id`,`role_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='用户和角色关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user_role`
--

LOCK TABLES `sys_user_role` WRITE;
/*!40000 ALTER TABLE `sys_user_role` DISABLE KEYS */;
INSERT INTO `sys_user_role` VALUES (1,1);
/*!40000 ALTER TABLE `sys_user_role` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-03-21 14:58:51
