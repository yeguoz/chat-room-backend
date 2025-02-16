-- --------------------------------------------------------
-- 主机:                           127.0.0.1
-- 服务器版本:                        8.0.12 - MySQL Community Server - GPL
-- 服务器操作系统:                      Win64
-- HeidiSQL 版本:                  11.0.0.5919
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


-- 导出 chat_room 的数据库结构
CREATE DATABASE IF NOT EXISTS `chat_room` /*!40100 DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci */;
USE `chat_room`;

-- 导出  表 chat_room.group_member 结构
CREATE TABLE IF NOT EXISTS `group_member` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '成员id',
  `group_room_id` bigint(20) unsigned NOT NULL COMMENT '群聊室id',
  `uid` bigint(20) unsigned NOT NULL COMMENT '用户id',
  `role` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0 普通成员 1 管理员',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0正常 1删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uid` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='群聊成员';

-- 数据导出被取消选择。

-- 导出  表 chat_room.group_room 结构
CREATE TABLE IF NOT EXISTS `group_room` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '群聊 id',
  `room_id` bigint(20) unsigned NOT NULL COMMENT '房间 id',
  `name` varchar(512) COLLATE utf8_unicode_ci NOT NULL DEFAULT '无名' COMMENT '群聊名字',
  `is_global_group` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0不是 1是',
  `avatar` varchar(512) COLLATE utf8_unicode_ci DEFAULT '' COMMENT '群聊头像',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0 正常 1 删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `room_id` (`room_id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='群聊';

-- 数据导出被取消选择。

-- 导出  表 chat_room.message 结构
CREATE TABLE IF NOT EXISTS `message` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '消息id',
  `room_id` bigint(20) unsigned NOT NULL COMMENT '聊天室id',
  `from_uid` bigint(20) unsigned NOT NULL COMMENT '发送消息人uid',
  `content` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_520_ci NOT NULL COMMENT '消息内容',
  `is_read` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0未读 1已读',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0正常 1删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_as_cs COMMENT='聊天消息';

-- 数据导出被取消选择。

-- 导出  表 chat_room.room 结构
CREATE TABLE IF NOT EXISTS `room` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '聊天室id',
  `type` tinyint(4) NOT NULL COMMENT '0 单聊 1 群聊',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0 正常 1 删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='聊天室';

-- 数据导出被取消选择。

-- 导出  表 chat_room.single_room 结构
CREATE TABLE IF NOT EXISTS `single_room` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `room_id` bigint(20) unsigned NOT NULL COMMENT '聊天室id',
  `uid1` bigint(20) unsigned NOT NULL COMMENT '用户1 id',
  `uid2` bigint(20) unsigned NOT NULL COMMENT '用户2 id',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0 正常 1 删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `room_id_uid1_uid2` (`room_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='单聊';

-- 数据导出被取消选择。

-- 导出  表 chat_room.user 结构
CREATE TABLE IF NOT EXISTS `user` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `email` varchar(255) COLLATE utf8_unicode_ci NOT NULL COMMENT '用户邮箱',
  `username` varchar(50) COLLATE utf8_unicode_ci NOT NULL COMMENT '用户名 需要唯一',
  `password` varchar(512) COLLATE utf8_unicode_ci NOT NULL COMMENT '密码',
  `avatar` varchar(512) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT 'https://cdn.jsdelivr.net/gh/ye-guo/Images/images/81cecc31ebcc31f3631ceb14cc621ed9.jpeg' COMMENT '头像',
  `gender` tinyint(4) DEFAULT '-1' COMMENT '0 女 1 男 -1 未知',
  `active_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0 下线 1在线',
  `last_online_time` datetime DEFAULT NULL COMMENT '最后上线时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_Deleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0 未删除 1 删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=0 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='用户表';

-- 数据导出被取消选择。

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
