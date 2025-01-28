/*
 Navicat Premium Dump SQL

 Source Server         : ruoyi-vue-pro
 Source Server Type    : MySQL
 Source Server Version : 80403 (8.4.3)
 Source Host           : localhost:3306
 Source Schema         : liteflow-agent

 Target Server Type    : MySQL
 Target Server Version : 80403 (8.4.3)
 File Encoding         : 65001

 Date: 28/01/2025 23:00:01
*/

create database liteflow-agent;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_chat
-- ----------------------------
DROP TABLE IF EXISTS `t_chat`;
CREATE TABLE `t_chat` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `question` longtext COLLATE utf8mb4_general_ci COMMENT '问题',
  `answer` longtext COLLATE utf8mb4_general_ci COMMENT '回答',
  `conversation_id` varchar(32) COLLATE utf8mb4_general_ci NOT NULL COMMENT '问答 id',
  `user_id` varchar(32) COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户 id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL  COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `conversation_Id_idx` (`conversation_id`) USING BTREE,
  KEY `uid_create_time_idx` (`user_id`,`create_time`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

SET FOREIGN_KEY_CHECKS = 1;
