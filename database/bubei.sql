/*
 Navicat Premium Data Transfer

 Source Server         : root
 Source Server Type    : MySQL
 Source Server Version : 80026
 Source Host           : localhost:3306
 Source Schema         : bubei

 Target Server Type    : MySQL
 Target Server Version : 80026
 File Encoding         : 65001

 Date: 26/11/2021 19:16:26
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_book
-- ----------------------------
DROP TABLE IF EXISTS `sys_book`;
CREATE TABLE `sys_book`  (
  `book_id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '词书id',
  `book_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '词书名字',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '词书描述',
  PRIMARY KEY (`book_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_collection
-- ----------------------------
DROP TABLE IF EXISTS `sys_collection`;
CREATE TABLE `sys_collection`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` int NOT NULL COMMENT '用户id',
  `word_id` int NOT NULL COMMENT '收藏的单词id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_review_set
-- ----------------------------
DROP TABLE IF EXISTS `sys_review_set`;
CREATE TABLE `sys_review_set`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` int NOT NULL COMMENT '用户id',
  `word_id` int NOT NULL COMMENT '单词id',
  `know_count` int NOT NULL DEFAULT 0 COMMENT '单词复习中 认识的次数',
  `vague_count` int NOT NULL DEFAULT 0 COMMENT '单词复习中 模糊的次数',
  `forget_count` int NOT NULL DEFAULT 0 COMMENT '单词复习中 忘记的次数',
  `study_date` date NOT NULL COMMENT '学习该单词的日期（添加到复习单词表的日期）',
  `date_interval` int NOT NULL DEFAULT 1 COMMENT '下一次学习日期间隔',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_study_set
-- ----------------------------
DROP TABLE IF EXISTS `sys_study_set`;
CREATE TABLE `sys_study_set`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` int NOT NULL COMMENT '用户id',
  `word_id` int NOT NULL COMMENT '单词id',
  `word_count` int NOT NULL DEFAULT 0 COMMENT '单词学习次数',
  `word_status` int NOT NULL DEFAULT 1 COMMENT '单词学习阶段（1 忘记， 2 模糊， 3 认识）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '用户表的主键',
  `tell` int NOT NULL COMMENT '用户的手机号，作为登录账号时使用',
  `password` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户的密码，作为登录账号时使用',
  `book_id` bigint NULL DEFAULT NULL COMMENT '用户选择的词书，保存词书的id',
  `study_set_id` int NULL DEFAULT NULL COMMENT '用户的学习单词集id',
  `review_set_id` int NULL DEFAULT NULL COMMENT '用户的复习单词集id',
  `collection_id` int NULL DEFAULT NULL COMMENT '用户单词收藏夹id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_word
-- ----------------------------
DROP TABLE IF EXISTS `sys_word`;
CREATE TABLE `sys_word`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `english` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `chinese` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `sent` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `level` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5733 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
