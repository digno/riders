# Host: 192.168.5.69  (Version: 5.5.9-log)
# Date: 2013-08-16 15:24:20
# Generator: MySQL-Front 5.3  (Build 3.20)

/*!40101 SET NAMES utf8 */;

DROP DATABASE IF EXISTS `riders`;
CREATE DATABASE `riders` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `riders`;

#
# Source for table "activity"
#

DROP TABLE IF EXISTS `activity`;
CREATE TABLE `activity` (
  `aid` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '活动 ID',
  `owner_id` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '组织者（参照user表id）',
  `range` tinyint(4) NOT NULL COMMENT '邀请范围',
  `tidlist` varchar(16) DEFAULT NULL COMMENT '车队id',
  `ctime` datetime NOT NULL COMMENT '创建时间',
  `title` varchar(255) DEFAULT NULL,
  `content` varchar(255) NOT NULL COMMENT '活动内容',
  `expire_time` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`aid`),
  KEY `ownerid` (`owner_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='活动表';

#
# Data for table "activity"
#


#
# Source for table "activity_members"
#

DROP TABLE IF EXISTS `activity_members`;
CREATE TABLE `activity_members` (
  `aid` int(11) unsigned NOT NULL COMMENT '活动ID',
  `uid` int(11) unsigned NOT NULL COMMENT '成员ID（参照user表id）',
  `jointime` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '加入时间',
  PRIMARY KEY (`aid`,`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='活动关系表';

#
# Data for table "activity_members"
#


#
# Source for table "comment"
#

DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment` (
  `cid` int(11) NOT NULL AUTO_INCREMENT,
  `aid` int(11) unsigned NOT NULL COMMENT '活动 ID',
  `uid` int(11) unsigned NOT NULL COMMENT '用户ID（参照user表id）',
  `ctime` datetime NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '留言时间',
  `content` varchar(512) NOT NULL COMMENT '内容',
  PRIMARY KEY (`cid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='活动留言表';

#
# Data for table "comment"
#


#
# Source for table "friend"
#

DROP TABLE IF EXISTS `friend`;
CREATE TABLE `friend` (
  `uid` int(11) unsigned NOT NULL COMMENT '用户ID（参照user表id）',
  `fid` int(11) unsigned NOT NULL COMMENT '好友ID（参照user表id）',
  PRIMARY KEY (`uid`,`fid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# Data for table "friend"
#


#
# Source for table "line"
#

DROP TABLE IF EXISTS `line`;
CREATE TABLE `line` (
  `lid` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '路线ID',
  `city` varchar(32) NOT NULL COMMENT '城市',
  `name` varchar(32) NOT NULL COMMENT '线路名',
  `hot` tinyint(4) NOT NULL COMMENT '热度',
  `strength` tinyint(4) NOT NULL COMMENT '强度',
  `content` varchar(512) NOT NULL COMMENT '路线描述',
  `url` varchar(512) NOT NULL DEFAULT '' COMMENT '链接地址',
  PRIMARY KEY (`lid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='精品线路';

#
# Data for table "line"
#


#
# Source for table "report"
#

DROP TABLE IF EXISTS `report`;
CREATE TABLE `report` (
  `rid` int(11) NOT NULL AUTO_INCREMENT,
  `uid` int(10) unsigned NOT NULL COMMENT '用户ID',
  `client_name` varchar(64) NOT NULL COMMENT '客户端名称',
  `client_version` double NOT NULL COMMENT '客户端版本号',
  `os_version` varchar(512) NOT NULL COMMENT '操作系统',
  `resolution` varchar(16) DEFAULT NULL COMMENT '分辨率',
  `mobile` varchar(16) DEFAULT NULL COMMENT '手机号码',
  `update_time` datetime NOT NULL COMMENT '上报时间',
  `device_token` varchar(32) DEFAULT NULL COMMENT '设备标识',
  `push_type` tinyint(4) DEFAULT NULL COMMENT '推送类型',
  PRIMARY KEY (`rid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='上报数据表';

#
# Data for table "report"
#


#
# Source for table "ss_admin"
#

DROP TABLE IF EXISTS `ss_admin`;
CREATE TABLE `ss_admin` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `login_name` varchar(255) DEFAULT '',
  `name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `salt` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `group_id` tinyint(3) DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='管理员表';

#
# Data for table "ss_admin"
#


#
# Source for table "ss_admin_role"
#

DROP TABLE IF EXISTS `ss_admin_role`;
CREATE TABLE `ss_admin_role` (
  `admin_id` int(11) NOT NULL DEFAULT '0',
  `role_id` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`admin_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='管理员角色对应表';

#
# Data for table "ss_admin_role"
#


#
# Source for table "ss_group"
#

DROP TABLE IF EXISTS `ss_group`;
CREATE TABLE `ss_group` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `master_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='管理员分组表';

#
# Data for table "ss_group"
#


#
# Source for table "ss_role"
#

DROP TABLE IF EXISTS `ss_role`;
CREATE TABLE `ss_role` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `permissions` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='管理员角色表';

#
# Data for table "ss_role"
#


#
# Source for table "suggestion"
#

DROP TABLE IF EXISTS `suggestion`;
CREATE TABLE `suggestion` (
  `sid` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `uid` int(10) unsigned NOT NULL COMMENT '用户ID',
  `type` tinyint(4) NOT NULL COMMENT '意见与建议的类型',
  `content` varchar(512) NOT NULL COMMENT '建议内容',
  PRIMARY KEY (`sid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='版本信息表';

#
# Data for table "suggestion"
#


#
# Source for table "team"
#

DROP TABLE IF EXISTS `team`;
CREATE TABLE `team` (
  `tid` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '车队ID',
  `master_id` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '队长ID（参照user表id）',
  `ctime` datetime NOT NULL COMMENT '创建时间',
  `declaration` varchar(128) DEFAULT NULL COMMENT '车队口号',
  PRIMARY KEY (`tid`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

#
# Data for table "team"
#

INSERT INTO `team` VALUES (5,25,'2013-06-27 16:26:22','123456787909'),(7,25,'2013-06-28 10:39:05','欢迎各位加入到我的车队中来。');

#
# Source for table "team_members"
#

DROP TABLE IF EXISTS `team_members`;
CREATE TABLE `team_members` (
  `tid` int(11) unsigned NOT NULL COMMENT '车队 ID',
  `uid` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '成员ID（参照user表id）',
  `jointime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`tid`,`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='车队关系表';

#
# Data for table "team_members"
#


#
# Source for table "user"
#

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `uid` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `email` varchar(128) NOT NULL COMMENT '用户名（邮箱）',
  `pwd` varchar(255) DEFAULT NULL,
  `nickname` varchar(32) DEFAULT NULL COMMENT '昵称',
  `age` tinyint(4) DEFAULT NULL COMMENT '年龄',
  `sex` int(11) DEFAULT NULL COMMENT '性别',
  `brithday` date DEFAULT NULL,
  `tools` varchar(128) DEFAULT NULL COMMENT '常备工具',
  `brand` varchar(128) DEFAULT NULL COMMENT '座驾以及型号',
  `ctime` datetime NOT NULL COMMENT '注册日期',
  `picture` varchar(128) DEFAULT NULL COMMENT '头像（存储路径）',
  `signature` varchar(32) DEFAULT NULL COMMENT '个性签名',
  `mobile` varchar(16) DEFAULT NULL COMMENT '手机号',
  `roles` varchar(255) NOT NULL,
  `salt` varchar(64) NOT NULL,
  PRIMARY KEY (`uid`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8;

#
# Data for table "user"
#

INSERT INTO `user` VALUES (1,'lvqi@leadtone.com','1234',NULL,NULL,NULL,NULL,NULL,NULL,'2013-06-09 18:04:52',NULL,NULL,NULL,'',''),(3,'xiaopi@leadtone.com','1234',NULL,NULL,NULL,NULL,NULL,NULL,'2013-06-09 18:09:16',NULL,NULL,NULL,'',''),(5,'xiaoge@leadtone.com','1234',NULL,NULL,NULL,NULL,NULL,NULL,'2013-06-09 18:09:34',NULL,NULL,NULL,'',''),(9,'amian@leadtone.com','1234',NULL,NULL,NULL,NULL,NULL,NULL,'2013-06-13 11:01:57',NULL,NULL,NULL,'',''),(11,'xiaodu@leadtone.com','1234',NULL,NULL,NULL,NULL,NULL,NULL,'2013-06-13 11:03:01',NULL,NULL,NULL,'',''),(25,'piluchun@126.com','f66151c731cbd71a9fcfa1ca244f0dd362b63985','龙须仔',29,1,NULL,NULL,NULL,'2013-06-24 10:34:47','','','','user','2218a3ce93f0d21b');

#
# Source for table "version"
#

DROP TABLE IF EXISTS `version`;
CREATE TABLE `version` (
  `client_name` varchar(64) NOT NULL COMMENT '客户端名称',
  `client_version` double NOT NULL COMMENT '客户端版本号',
  `url` varchar(512) NOT NULL COMMENT '下载地址',
  PRIMARY KEY (`client_name`,`client_version`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='版本信息表';

#
# Data for table "version"
#

