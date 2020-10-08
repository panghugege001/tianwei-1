/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50730
Source Host           : localhost:3306
Source Database       : tianwei

Target Server Type    : MYSQL
Target Server Version : 50730
File Encoding         : 65001

Date: 2020-10-09 05:13:03
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `abc_transfers`
-- ----------------------------
DROP TABLE IF EXISTS `abc_transfers`;
CREATE TABLE `abc_transfers` (
  `transfer_id` int(11) NOT NULL AUTO_INCREMENT,
  `amount` decimal(14,4) unsigned NOT NULL COMMENT '充值金额',
  `balance` decimal(14,4) unsigned NOT NULL DEFAULT '0.0000' COMMENT '银行额度',
  `jyhm` varchar(50) NOT NULL DEFAULT '',
  `jyfs` varchar(50) NOT NULL DEFAULT '',
  `jyqd` varchar(50) NOT NULL DEFAULT '',
  `jysm` varchar(50) NOT NULL DEFAULT '',
  `jyzy` varchar(50) NOT NULL DEFAULT '',
  `accept_name` varchar(30) NOT NULL,
  `accept_card_num` varchar(19) NOT NULL DEFAULT '',
  `accept_area` varchar(50) NOT NULL,
  `pay_date` datetime NOT NULL COMMENT '付款时间',
  `admin_id` smallint(5) unsigned NOT NULL COMMENT '管理员ID',
  `status` tinyint(3) unsigned NOT NULL COMMENT '0未处理 1已充值 2充值超时处理',
  `date` datetime NOT NULL COMMENT '记录添加时间',
  `timecha` varchar(50) DEFAULT NULL,
  `overtime` int(1) DEFAULT '0',
  PRIMARY KEY (`transfer_id`),
  UNIQUE KEY `paydate` (`accept_card_num`,`amount`,`pay_date`,`balance`) USING BTREE,
  KEY `status` (`status`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='转账记录表';

-- ----------------------------
-- Records of abc_transfers
-- ----------------------------

-- ----------------------------
-- Table structure for `accountinfo`
-- ----------------------------
DROP TABLE IF EXISTS `accountinfo`;
CREATE TABLE `accountinfo` (
  `loginname` varchar(12) NOT NULL,
  `accountName` varchar(50) NOT NULL,
  `accountNo` varchar(32) NOT NULL,
  `bank` varchar(50) NOT NULL,
  `accountType` varchar(20) NOT NULL,
  `accountCity` varchar(50) NOT NULL,
  `bankAddress` varchar(100) NOT NULL,
  `createtime` datetime NOT NULL,
  `lastModifyTime` datetime DEFAULT NULL,
  PRIMARY KEY (`loginname`),
  UNIQUE KEY `loginname` (`loginname`) USING BTREE,
  KEY `accountName` (`accountName`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of accountinfo
-- ----------------------------

-- ----------------------------
-- Table structure for `actionlogs`
-- ----------------------------
DROP TABLE IF EXISTS `actionlogs`;
CREATE TABLE `actionlogs` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `loginname` varchar(20) NOT NULL,
  `action` varchar(20) DEFAULT NULL,
  `createtime` datetime DEFAULT NULL,
  `remark` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`) USING BTREE,
  KEY `loginname` (`loginname`) USING BTREE,
  KEY `action` (`action`) USING BTREE,
  KEY `createtime` (`createtime`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=6707006 DEFAULT CHARSET=utf8 COMMENT='InnoDB free: 11264 kB';

-- ----------------------------
-- Records of actionlogs
-- ----------------------------
INSERT INTO `actionlogs` VALUES ('6706700', 'test01', 'LOGIN', '2018-11-28 17:53:38', 'ip:45.32.9.147;最后登录地址：美国;客户操作系统：Windows 7');
INSERT INTO `actionlogs` VALUES ('6706701', 'test01', 'LOGIN', '2018-11-28 18:05:16', 'ip:45.32.9.147;最后登录地址：美国;客户操作系统：Windows 7');
INSERT INTO `actionlogs` VALUES ('6706702', 'windtest', 'MODIFY_CUS_INFO', '2018-12-01 11:35:01', '130.105.170.172;原电话[s4bu/riE19ITUa/8N+/aKg==]改为[null];原QQ[null]改为[];原wechat[null]改为[];');
INSERT INTO `actionlogs` VALUES ('6706703', 'windtest', 'LOGIN', '2018-12-01 15:18:45', 'ip:130.105.170.172;最后登录地址：美国;客户操作系统：Windows 7');
INSERT INTO `actionlogs` VALUES ('6706704', 'windtest', 'LOGIN', '2018-12-02 13:31:05', 'ip:130.105.170.172;最后登录地址：美国;客户操作系统：Windows 7');
INSERT INTO `actionlogs` VALUES ('6706705', 'windtest', 'LOGIN', '2018-12-02 14:14:14', 'ip:130.105.170.172;最后登录地址：美国;客户操作系统：Windows 7');
INSERT INTO `actionlogs` VALUES ('6706706', 'windtest', 'LOGIN', '2018-12-02 16:46:21', 'ip:130.105.170.172;最后登录地址：美国;客户操作系统：Windows 7');
INSERT INTO `actionlogs` VALUES ('6706707', 'windtest', 'LOGIN', '2018-12-06 12:35:33', 'ip:45.32.9.147;最后登录地址：美国;客户操作系统：未知');
INSERT INTO `actionlogs` VALUES ('6706708', 'windtest', 'LOGIN', '2018-12-07 15:06:32', 'ip:130.105.170.172;最后登录地址：美国;客户操作系统：未知');
INSERT INTO `actionlogs` VALUES ('6706709', 'windtest', 'LOGIN', '2018-12-08 14:23:26', 'ip:45.32.9.147;最后登录地址：美国;客户操作系统：未知');
INSERT INTO `actionlogs` VALUES ('6706710', 'windtest', 'LOGIN', '2018-12-09 21:14:07', 'ip:45.32.9.147;最后登录地址：美国;客户操作系统：未知');
INSERT INTO `actionlogs` VALUES ('6706711', 'windtest', 'LOGIN', '2018-12-10 11:27:47', 'ip:180.191.99.143;最后登录地址：菲律宾 Globe通讯公司;客户操作系统：未知');
INSERT INTO `actionlogs` VALUES ('6706712', 'windtest', 'LOGIN', '2018-12-10 12:03:23', 'ip:45.32.9.147;最后登录地址：美国;客户操作系统：未知');
INSERT INTO `actionlogs` VALUES ('6706713', 'windtest', 'LOGIN', '2018-12-10 13:04:52', 'ip:45.32.9.147;最后登录地址：美国;客户操作系统：未知');
INSERT INTO `actionlogs` VALUES ('6706714', 'windtest', 'LOGIN', '2018-12-10 13:31:37', 'ip:180.191.99.143;最后登录地址：菲律宾 Globe通讯公司;客户操作系统：未知');
INSERT INTO `actionlogs` VALUES ('6706715', 'windtest', 'LOGIN', '2018-12-10 14:24:02', 'ip:180.191.99.143;最后登录地址：菲律宾 Globe通讯公司;客户操作系统：未知');
INSERT INTO `actionlogs` VALUES ('6706716', 'windtest', 'LOGIN', '2018-12-10 15:48:15', 'ip:180.191.99.143;最后登录地址：菲律宾 Globe通讯公司;客户操作系统：未知');
INSERT INTO `actionlogs` VALUES ('6706717', 'windtest', 'LOGIN', '2018-12-12 14:26:45', 'ip:45.32.9.147;最后登录地址：美国;客户操作系统：Macintosh');
INSERT INTO `actionlogs` VALUES ('6706718', 'twtest01', 'LOGIN', '2018-12-13 00:06:50', 'ip:45.32.9.147;最后登录地址：美国;客户操作系统：未知');
INSERT INTO `actionlogs` VALUES ('6706719', 'twtest02', 'LOGIN', '2018-12-13 00:51:41', 'ip:45.32.9.147;最后登录地址：美国;客户操作系统：Windows 7');
INSERT INTO `actionlogs` VALUES ('6706720', 'twtest01', 'LOGIN', '2018-12-13 00:52:43', 'ip:45.32.9.147;最后登录地址：美国;客户操作系统：未知');
INSERT INTO `actionlogs` VALUES ('6706721', 'twtest02', 'LOGIN', '2018-12-13 01:00:27', 'ip:130.105.170.172;最后登录地址：美国;客户操作系统：Macintosh');
INSERT INTO `actionlogs` VALUES ('6706722', 'twtest01', 'LOGIN', '2018-12-13 01:24:52', 'ip:219.92.12.93;最后登录地址：马来西亚;客户操作系统：未知');
INSERT INTO `actionlogs` VALUES ('6706723', 'twtest01', 'LOGIN', '2018-12-13 01:28:02', 'ip:211.24.92.207;最后登录地址：马来西亚;客户操作系统：Linux');
INSERT INTO `actionlogs` VALUES ('6706724', 'twtest01', 'LOGIN', '2018-12-13 11:06:43', 'ip:219.92.12.93;最后登录地址：马来西亚;客户操作系统：未知');
INSERT INTO `actionlogs` VALUES ('6706725', 'twtest02', 'LOGIN', '2018-12-13 11:34:23', 'ip:45.32.9.147;最后登录地址：美国;客户操作系统：Windows 7');
INSERT INTO `actionlogs` VALUES ('6706726', 'twtest02', 'LOGIN', '2018-12-13 11:40:23', 'ip:130.105.170.172;最后登录地址：美国;客户操作系统：Linux');
INSERT INTO `actionlogs` VALUES ('6706727', 'twtest01', 'LOGIN', '2018-12-14 00:25:17', 'ip:45.32.9.147;最后登录地址：美国;客户操作系统：未知');
INSERT INTO `actionlogs` VALUES ('6706728', 'twtest01', 'LOGIN', '2018-12-14 00:36:40', 'ip:211.24.92.207;最后登录地址：马来西亚;客户操作系统：未知');
INSERT INTO `actionlogs` VALUES ('6706729', 'twtest01', 'LOGIN', '2018-12-14 00:38:19', 'ip:219.92.12.93;最后登录地址：马来西亚;客户操作系统：Linux');
INSERT INTO `actionlogs` VALUES ('6706730', 'twtest01', 'LOGIN', '2018-12-14 02:17:16', 'ip:45.32.9.147;最后登录地址：美国;客户操作系统：未知');
INSERT INTO `actionlogs` VALUES ('6706731', 'windtest', 'LOGIN', '2018-12-14 10:55:46', 'ip:45.32.9.147;最后登录地址：美国;客户操作系统：未知');
INSERT INTO `actionlogs` VALUES ('6706732', 'twtest01', 'LOGIN', '2018-12-14 20:08:13', 'ip:180.191.159.231;最后登录地址：菲律宾 Globe通讯公司;客户操作系统：Linux');
INSERT INTO `actionlogs` VALUES ('6706733', 'twtest01', 'LOGIN', '2018-12-15 06:58:08', 'ip:112.206.176.11;最后登录地址：菲律宾 PLDT通讯公司;客户操作系统：未知');
INSERT INTO `actionlogs` VALUES ('6706734', 'twtest01', 'LOGIN', '2018-12-16 11:57:24', 'ip:45.32.9.147;最后登录地址：美国;客户操作系统：未知');
INSERT INTO `actionlogs` VALUES ('6706735', 'windtest', 'LOGIN', '2018-12-16 14:05:48', 'ip:45.32.9.147;最后登录地址：美国;客户操作系统：未知');
INSERT INTO `actionlogs` VALUES ('6706736', 'twtest01', 'LOGIN', '2018-12-16 14:07:12', 'ip:219.92.12.93;最后登录地址：马来西亚;客户操作系统：Windows 7');
INSERT INTO `actionlogs` VALUES ('6706737', 'twtest01', 'LOGIN', '2018-12-16 14:20:40', 'ip:134.17.31.226;最后登录地址：白俄罗斯;客户操作系统：未知');
INSERT INTO `actionlogs` VALUES ('6706738', 'twtest01', 'LOGIN', '2018-12-16 15:04:54', 'ip:45.32.9.147;最后登录地址：美国;客户操作系统：未知');
INSERT INTO `actionlogs` VALUES ('6706739', 'twtest01', 'MODIFY_CUS_INFO', '2018-12-16 15:05:41', '45.32.9.147;原电话[o6IqH86vWekTM1MSxPFGbQ==]改为[null];原QQ[null]改为[];原wechat[null]改为[];');
INSERT INTO `actionlogs` VALUES ('6706740', 'twtest01', 'LOGIN', '2018-12-16 16:51:30', 'ip:134.17.31.226;最后登录地址：白俄罗斯;客户操作系统：未知');
INSERT INTO `actionlogs` VALUES ('6706741', 'twtest01', 'LOGIN', '2018-12-16 17:03:15', 'ip:134.17.31.226;最后登录地址：白俄罗斯;客户操作系统：未知');
INSERT INTO `actionlogs` VALUES ('6706742', 'twtest01', 'LOGIN', '2018-12-16 17:29:33', 'ip:211.24.92.207;最后登录地址：马来西亚;客户操作系统：Windows 7');
INSERT INTO `actionlogs` VALUES ('6706743', 'twtest01', 'LOGIN', '2018-12-16 17:38:19', 'ip:45.32.9.147;最后登录地址：美国;客户操作系统：未知');
INSERT INTO `actionlogs` VALUES ('6706744', 'twtest01', 'LOGIN', '2018-12-16 18:10:45', 'ip:219.92.12.93;最后登录地址：马来西亚;客户操作系统：Windows 7');
INSERT INTO `actionlogs` VALUES ('6706745', 'twtest01', 'LOGIN', '2018-12-16 18:17:10', 'ip:134.17.31.226;最后登录地址：白俄罗斯;客户操作系统：未知');
INSERT INTO `actionlogs` VALUES ('6706746', 'twtest01', 'LOGIN', '2018-12-16 19:29:23', 'ip:219.92.12.93;最后登录地址：马来西亚;客户操作系统：Windows 7');
INSERT INTO `actionlogs` VALUES ('6706747', 'twtest01', 'LOGIN', '2018-12-16 20:16:31', 'ip:134.17.31.226;最后登录地址：白俄罗斯;客户操作系统：未知');
INSERT INTO `actionlogs` VALUES ('6706748', 'twtest01', 'LOGIN', '2018-12-17 18:45:56', 'ip:211.24.92.207;最后登录地址：马来西亚;客户操作系统：未知');
INSERT INTO `actionlogs` VALUES ('6706749', 'windtest', 'LOGIN', '2018-12-18 10:31:24', 'ip:130.105.170.172;最后登录地址：美国;客户操作系统：未知');
INSERT INTO `actionlogs` VALUES ('6706750', 'windtest', 'LOGIN', '2018-12-19 03:19:34', 'ip:180.191.100.232;最后登录地址：菲律宾 Globe通讯公司;客户操作系统：未知');
INSERT INTO `actionlogs` VALUES ('6706751', 'windtest', 'LOGIN', '2018-12-19 03:31:20', 'ip:180.191.100.232;最后登录地址：菲律宾 Globe通讯公司;客户操作系统：未知');
INSERT INTO `actionlogs` VALUES ('6706752', 'windtest', 'LOGIN', '2018-12-19 10:54:49', 'ip:45.32.9.147;最后登录地址：美国;客户操作系统：未知');
INSERT INTO `actionlogs` VALUES ('6706753', 'skytest', 'MODIFY_CUS_INFO', '2018-12-19 19:04:27', '45.32.9.147;原电话[iX5YyqAyJrKjfSBmVOCWUA==]改为[null];原QQ[null]改为[];原wechat[null]改为[];');
INSERT INTO `actionlogs` VALUES ('6706754', 'windtest', 'LOGIN', '2018-12-20 10:49:22', 'ip:45.32.9.147;最后登录地址：美国;客户操作系统：未知');
INSERT INTO `actionlogs` VALUES ('6706755', 'windtest', 'LOGIN', '2018-12-20 15:00:23', 'ip:45.32.9.147;最后登录地址：美国;客户操作系统：Macintosh');
INSERT INTO `actionlogs` VALUES ('6706756', 'skytest', 'LOGIN', '2018-12-20 15:03:57', 'ip:45.32.9.147;最后登录地址：美国;客户操作系统：Windows 7');
INSERT INTO `actionlogs` VALUES ('6706757', 'windtest', 'LOGIN', '2018-12-20 21:48:13', 'ip:180.191.159.144;最后登录地址：菲律宾 Globe通讯公司;客户操作系统：Windows 7');
INSERT INTO `actionlogs` VALUES ('6706758', 'windtest', 'LOGIN', '2018-12-20 22:30:10', 'ip:180.191.100.232;最后登录地址：菲律宾 Globe通讯公司;客户操作系统：未知');
INSERT INTO `actionlogs` VALUES ('6706759', 'windtest', 'LOGIN', '2018-12-20 23:00:02', 'ip:180.191.100.232;最后登录地址：菲律宾 Globe通讯公司;客户操作系统：未知');
INSERT INTO `actionlogs` VALUES ('6706760', 'windtest', 'LOGIN', '2018-12-20 23:00:14', 'ip:180.191.159.144;最后登录地址：菲律宾 Globe通讯公司;客户操作系统：Linux');
INSERT INTO `actionlogs` VALUES ('6706761', 'windtest', 'LOGIN', '2018-12-20 23:05:41', 'ip:180.191.100.232;最后登录地址：菲律宾 Globe通讯公司;客户操作系统：未知');
INSERT INTO `actionlogs` VALUES ('6706762', 'windtest', 'LOGIN', '2018-12-20 23:33:22', 'ip:180.191.159.144;最后登录地址：菲律宾 Globe通讯公司;客户操作系统：Windows 7');
INSERT INTO `actionlogs` VALUES ('6706763', 'windtest', 'LOGIN', '2018-12-20 23:59:17', 'ip:180.191.100.232;最后登录地址：菲律宾 Globe通讯公司;客户操作系统：未知');
INSERT INTO `actionlogs` VALUES ('6706764', 'windtest', 'LOGIN', '2018-12-21 02:00:27', 'ip:180.190.112.117;最后登录地址：菲律宾;客户操作系统：未知');
INSERT INTO `actionlogs` VALUES ('6706765', 'windtest', 'LOGIN', '2018-12-21 02:06:30', 'ip:180.190.112.117;最后登录地址：菲律宾;客户操作系统：未知');
INSERT INTO `actionlogs` VALUES ('6706766', 'windtest', 'LOGIN', '2018-12-21 02:17:22', 'ip:180.190.112.117;最后登录地址：菲律宾;客户操作系统：未知');
INSERT INTO `actionlogs` VALUES ('6706767', 'windtest', 'LOGIN', '2018-12-21 02:26:30', 'ip:180.190.112.117;最后登录地址：菲律宾;客户操作系统：未知');
INSERT INTO `actionlogs` VALUES ('6706768', 'windtest', 'LOGIN', '2018-12-21 02:30:52', 'ip:180.190.112.117;最后登录地址：菲律宾;客户操作系统：未知');
INSERT INTO `actionlogs` VALUES ('6706769', 'windtest', 'LOGIN', '2018-12-21 02:32:50', 'ip:180.190.112.117;最后登录地址：菲律宾;客户操作系统：未知');
INSERT INTO `actionlogs` VALUES ('6706770', 'skytest', 'LOGIN', '2018-12-21 10:50:32', 'ip:45.32.9.147;最后登录地址：美国;客户操作系统：Windows 7');
INSERT INTO `actionlogs` VALUES ('6706771', 'windtest', 'LOGIN', '2018-12-21 11:13:33', 'ip:130.105.170.172;最后登录地址：美国;客户操作系统：未知');
INSERT INTO `actionlogs` VALUES ('6706772', 'wadetest', 'LOGIN', '2018-12-21 11:16:40', 'ip:45.32.9.147;最后登录地址：美国;客户操作系统：未知');
INSERT INTO `actionlogs` VALUES ('6706773', 'wadetest', 'LOGIN', '2018-12-21 11:18:57', 'ip:45.32.9.147;最后登录地址：美国;客户操作系统：未知');
INSERT INTO `actionlogs` VALUES ('6706774', 'windtest', 'MODIFY_CUS_INFO', '2018-12-21 11:28:38', '130.105.170.172;原电话[s4bu/riE19ITUa/8N+/aKg==]改为[null];原QQ[null]改为[12343211];原wechat[null]改为[];');
INSERT INTO `actionlogs` VALUES ('6706775', 'windtest', 'MODIFY_CUS_INFO', '2018-12-21 11:28:47', '130.105.170.172;原电话[s4bu/riE19ITUa/8N+/aKg==]改为[null];原QQ[12343211]改为[123111122222];原wechat[null]改为[];');
INSERT INTO `actionlogs` VALUES ('6706776', 'twtest02', 'LOGIN', '2018-12-21 12:05:26', 'ip:130.105.170.172;最后登录地址：美国;客户操作系统：Linux');
INSERT INTO `actionlogs` VALUES ('6706777', 'skytest', 'LOGIN', '2018-12-21 12:07:28', 'ip:45.32.9.147;最后登录地址：美国;客户操作系统：Windows 7');
INSERT INTO `actionlogs` VALUES ('6706778', 'windtest', 'LOGIN', '2018-12-21 12:17:11', 'ip:130.105.170.172;最后登录地址：美国;客户操作系统：Macintosh');
INSERT INTO `actionlogs` VALUES ('6706779', 'windtest', 'LOGIN', '2018-12-22 00:51:15', 'ip:180.190.112.186;最后登录地址：菲律宾;客户操作系统：未知');
INSERT INTO `actionlogs` VALUES ('6706780', 'windtest', 'LOGIN', '2018-12-22 01:29:34', 'ip:180.190.112.186;最后登录地址：菲律宾;客户操作系统：未知');
INSERT INTO `actionlogs` VALUES ('6706781', 'twtest01', 'LOGIN', '2018-12-22 01:36:15', 'ip:112.206.187.194;最后登录地址：菲律宾 PLDT通讯公司;客户操作系统：未知');
INSERT INTO `actionlogs` VALUES ('6706782', 'twtest01', 'LOGIN', '2018-12-22 01:41:14', 'ip:112.206.187.194;最后登录地址：菲律宾 PLDT通讯公司;客户操作系统：未知');
INSERT INTO `actionlogs` VALUES ('6706783', 'windtest', 'LOGIN', '2018-12-22 01:49:10', 'ip:180.190.112.186;最后登录地址：菲律宾;客户操作系统：未知');
INSERT INTO `actionlogs` VALUES ('6706784', 'windtest', 'LOGIN', '2018-12-22 11:28:47', 'ip:45.32.9.147;最后登录地址：美国;客户操作系统：Macintosh');
INSERT INTO `actionlogs` VALUES ('6706785', 'windtest', 'LOGIN', '2018-12-22 16:25:59', 'ip:45.32.9.147;最后登录地址：美国;客户操作系统：未知');
INSERT INTO `actionlogs` VALUES ('6706786', 'windtest', 'LOGIN', '2018-12-23 00:37:59', 'ip:180.190.112.11;最后登录地址：菲律宾;客户操作系统：未知');
INSERT INTO `actionlogs` VALUES ('6706787', 'windtest', 'LOGIN', '2018-12-23 02:05:43', 'ip:180.191.159.144;最后登录地址：菲律宾 Globe通讯公司;客户操作系统：Macintosh');
INSERT INTO `actionlogs` VALUES ('6706788', 'windtest', 'LOGIN', '2018-12-23 02:08:57', 'ip:180.191.159.144;最后登录地址：菲律宾 Globe通讯公司;客户操作系统：Macintosh');
INSERT INTO `actionlogs` VALUES ('6706789', 'windtest', 'LOGIN', '2018-12-23 02:36:56', 'ip:180.190.112.11;最后登录地址：菲律宾;客户操作系统：Macintosh');
INSERT INTO `actionlogs` VALUES ('6706790', 'twtest01', 'LOGIN', '2018-12-23 07:50:23', 'ip:45.32.9.147;最后登录地址：美国;客户操作系统：未知');
INSERT INTO `actionlogs` VALUES ('6706791', 'twtest01', 'MODIFY_BANK_INFO', '2018-12-23 07:52:45', '修改银行支付密码');
INSERT INTO `actionlogs` VALUES ('6706792', 'twtest01', 'MODIFY_CUS_INFO', '2018-12-23 07:53:01', '45.32.9.147;原电话[o6IqH86vWekTM1MSxPFGbQ==]改为[null];原QQ[null]改为[];原wechat[null]改为[];');
INSERT INTO `actionlogs` VALUES ('6706793', 'twtest01', 'LOGIN', '2018-12-23 09:37:57', 'ip:45.32.9.147;最后登录地址：美国;客户操作系统：未知');
INSERT INTO `actionlogs` VALUES ('6706794', 'skytest', 'LOGIN', '2018-12-23 13:22:40', 'ip:45.32.9.147;最后登录地址：美国;客户操作系统：Windows 7');
INSERT INTO `actionlogs` VALUES ('6706795', 'windtest', 'LOGIN', '2018-12-23 14:36:07', 'ip:45.32.9.147;最后登录地址：美国;客户操作系统：未知');
INSERT INTO `actionlogs` VALUES ('6706796', 'wadetest', 'LOGIN', '2018-12-23 14:41:12', 'ip:45.32.9.147;最后登录地址：美国;客户操作系统：未知');
INSERT INTO `actionlogs` VALUES ('6706797', 'skytest', 'LOGIN', '2018-12-23 14:42:01', 'ip:45.32.9.147;最后登录地址：美国;客户操作系统：未知');
INSERT INTO `actionlogs` VALUES ('6706798', 'windtest', 'LOGIN', '2018-12-23 15:05:30', 'ip:45.32.9.147;最后登录地址：美国;客户操作系统：未知');
INSERT INTO `actionlogs` VALUES ('6706799', 'twtest01', 'LOGIN', '2018-12-23 15:07:39', 'ip:112.206.187.194;最后登录地址：菲律宾 PLDT通讯公司;客户操作系统：未知');
INSERT INTO `actionlogs` VALUES ('6706800', 'windtest', 'LOGIN', '2018-12-23 22:45:49', 'ip:180.191.159.144;最后登录地址：菲律宾 Globe通讯公司;客户操作系统：Windows 7');
INSERT INTO `actionlogs` VALUES ('6706801', 'windtest', 'LOGIN', '2018-12-24 00:45:17', 'ip:180.190.112.11;最后登录地址：菲律宾;客户操作系统：未知');
INSERT INTO `actionlogs` VALUES ('6706802', 'windtest', 'LOGIN', '2018-12-24 10:55:28', 'ip:45.32.9.147;最后登录地址：美国;客户操作系统：未知');
INSERT INTO `actionlogs` VALUES ('6706803', 'twtest01', 'LOGIN', '2018-12-24 11:01:57', 'ip:112.206.187.194;最后登录地址：菲律宾 PLDT通讯公司;客户操作系统：未知');
INSERT INTO `actionlogs` VALUES ('6706804', 'skytest', 'LOGIN', '2018-12-24 13:55:22', 'ip:45.32.9.147;最后登录地址：美国;客户操作系统：Windows 7');
INSERT INTO `actionlogs` VALUES ('6706805', 'twtest02', 'LOGIN', '2018-12-24 14:01:11', 'ip:130.105.170.172;最后登录地址：美国;客户操作系统：Linux');
INSERT INTO `actionlogs` VALUES ('6706806', 'skytest', 'LOGIN', '2018-12-24 16:43:29', 'ip:45.32.9.147;最后登录地址：美国;客户操作系统：Windows 7');
INSERT INTO `actionlogs` VALUES ('6706807', 'windtest', 'LOGIN', '2018-12-25 00:41:01', 'ip:180.191.159.193;最后登录地址：菲律宾 Globe通讯公司;客户操作系统：Macintosh');
INSERT INTO `actionlogs` VALUES ('6706808', 'twtest01', 'LOGIN', '2018-12-25 00:59:17', 'ip:45.32.9.147;最后登录地址：美国;客户操作系统：Macintosh');
INSERT INTO `actionlogs` VALUES ('6706809', 'windtest', 'LOGIN', '2018-12-25 02:30:50', 'ip:180.190.112.11;最后登录地址：菲律宾;客户操作系统：未知');
INSERT INTO `actionlogs` VALUES ('6706810', 'twnick', 'LOGIN', '2018-12-25 03:05:15', 'ip:45.32.9.147;最后登录地址：美国;客户操作系统：未知');
INSERT INTO `actionlogs` VALUES ('6706811', 'twnick', 'MODIFY_CUS_INFO', '2018-12-25 03:07:31', '45.32.9.147;原电话[2t6ZP2UUaReI/fWqkHVWVw==]改为[null];原QQ[null]改为[55123132];原wechat[null]改为[51532];');
INSERT INTO `actionlogs` VALUES ('6706812', 'skytest', 'LOGIN', '2018-12-25 14:38:48', 'ip:45.32.9.147;最后登录地址：美国;客户操作系统：Windows 7');
INSERT INTO `actionlogs` VALUES ('6706813', 'windtest', 'LOGIN', '2018-12-25 14:42:45', 'ip:45.32.9.147;最后登录地址：美国;客户操作系统：Macintosh');
INSERT INTO `actionlogs` VALUES ('6706814', 'wadetest', 'LOGIN', '2018-12-26 14:39:43', 'ip:45.32.9.147;最后登录地址：美国;客户操作系统：未知');
INSERT INTO `actionlogs` VALUES ('6706815', 'twtest01', 'LOGIN', '2018-12-26 23:29:45', 'ip:112.208.155.161;最后登录地址：菲律宾;客户操作系统：未知');
INSERT INTO `actionlogs` VALUES ('6706816', 'skytest', 'LOGIN', '2018-12-28 12:06:38', 'ip:45.32.9.147;最后登录地址：美国;客户操作系统：Windows 7');
INSERT INTO `actionlogs` VALUES ('6706817', 'windtest', 'LOGIN', '2018-12-28 16:29:41', 'ip:45.32.9.147;最后登录地址：美国;客户操作系统：未知');
INSERT INTO `actionlogs` VALUES ('6706818', 'windtest', 'LOGIN', '2018-12-29 13:28:49', 'ip:45.32.9.147;最后登录地址：美国;客户操作系统：Macintosh');
INSERT INTO `actionlogs` VALUES ('6706819', 'kavin998', 'LOGIN', '2020-09-03 20:53:28', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：未知');
INSERT INTO `actionlogs` VALUES ('6706820', 'kavin998', 'LOGIN', '2020-09-03 21:09:31', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：未知');
INSERT INTO `actionlogs` VALUES ('6706821', 'kavin998', 'LOGIN', '2020-09-03 21:42:59', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：未知');
INSERT INTO `actionlogs` VALUES ('6706822', 'kavin998', 'LOGIN', '2020-09-03 21:50:36', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：未知');
INSERT INTO `actionlogs` VALUES ('6706823', 'kavin998', 'LOGIN', '2020-09-03 23:25:36', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：未知');
INSERT INTO `actionlogs` VALUES ('6706824', 'kavin998', 'LOGIN', '2020-09-04 11:42:02', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：未知');
INSERT INTO `actionlogs` VALUES ('6706825', 'kavin998', 'LOGIN', '2020-09-04 11:59:41', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：未知');
INSERT INTO `actionlogs` VALUES ('6706826', 'kavin998', 'LOGIN', '2020-09-04 17:01:24', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：未知');
INSERT INTO `actionlogs` VALUES ('6706827', 'kavin998', 'LOGIN', '2020-09-04 19:22:06', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：未知');
INSERT INTO `actionlogs` VALUES ('6706828', 'kavin998', 'LOGIN', '2020-09-04 20:50:59', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：未知');
INSERT INTO `actionlogs` VALUES ('6706829', 'kavin998', 'LOGIN', '2020-09-04 21:08:33', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：未知');
INSERT INTO `actionlogs` VALUES ('6706830', 'kavin998', 'LOGIN', '2020-09-04 21:21:44', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：未知');
INSERT INTO `actionlogs` VALUES ('6706831', 'kavin998', 'MODIFY_CUS_INFO', '2020-09-04 21:22:17', '0:0:0:0:0:0:0:1;原昵称[]改为[null];原电话[2s9u1ZUKlhVCLlABbsMmWw==]改为[null];');
INSERT INTO `actionlogs` VALUES ('6706832', 'kavin998', 'LOGIN', '2020-09-05 21:45:09', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：未知');
INSERT INTO `actionlogs` VALUES ('6706833', 'kavin998', 'LOGIN', '2020-09-06 13:22:37', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：未知');
INSERT INTO `actionlogs` VALUES ('6706834', 'kavin998', 'LOGIN', '2020-09-06 20:06:55', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：未知');
INSERT INTO `actionlogs` VALUES ('6706835', 'kavin998', 'LOGIN', '2020-09-06 22:17:57', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：Linux');
INSERT INTO `actionlogs` VALUES ('6706836', 'kavin998', 'LOGIN', '2020-09-07 18:20:56', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：Linux');
INSERT INTO `actionlogs` VALUES ('6706837', 'kavin998', 'LOGIN', '2020-09-09 00:07:47', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：Linux');
INSERT INTO `actionlogs` VALUES ('6706838', 'kavin998', 'LOGIN', '2020-09-09 11:07:46', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：Linux');
INSERT INTO `actionlogs` VALUES ('6706839', 'kavin998', 'LOGIN', '2020-09-09 15:31:05', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：Linux');
INSERT INTO `actionlogs` VALUES ('6706840', 'kavin998', 'LOGIN', '2020-09-09 16:18:05', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：Linux');
INSERT INTO `actionlogs` VALUES ('6706841', 'kavin998', 'LOGIN', '2020-09-09 19:53:17', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：Linux');
INSERT INTO `actionlogs` VALUES ('6706842', 'kavin998', 'LOGIN', '2020-09-09 21:34:42', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：Linux');
INSERT INTO `actionlogs` VALUES ('6706843', 'kavin998', 'LOGIN', '2020-09-09 21:48:32', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：Linux');
INSERT INTO `actionlogs` VALUES ('6706844', 'kavin998', 'LOGIN', '2020-09-11 20:54:05', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：Linux');
INSERT INTO `actionlogs` VALUES ('6706845', 'kavin998', 'LOGIN', '2020-09-11 23:57:02', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：Macintosh');
INSERT INTO `actionlogs` VALUES ('6706846', 'kavin998', 'LOGIN', '2020-09-12 20:08:02', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：Macintosh');
INSERT INTO `actionlogs` VALUES ('6706847', 'kavin998', 'LOGIN', '2020-09-12 21:33:36', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：Macintosh');
INSERT INTO `actionlogs` VALUES ('6706848', 'kavin998', 'LOGIN', '2020-09-13 10:33:31', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：Macintosh');
INSERT INTO `actionlogs` VALUES ('6706849', 'kavin998', 'LOGIN', '2020-09-13 10:54:14', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：Macintosh');
INSERT INTO `actionlogs` VALUES ('6706850', 'kavin998', 'LOGIN', '2020-09-13 22:17:05', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：Macintosh');
INSERT INTO `actionlogs` VALUES ('6706851', 'kavin998', 'LOGIN', '2020-09-13 22:23:38', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：Macintosh');
INSERT INTO `actionlogs` VALUES ('6706852', 'kavin998', 'LOGIN', '2020-09-13 23:06:27', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：Macintosh');
INSERT INTO `actionlogs` VALUES ('6706853', 'kavin998', 'LOGIN', '2020-09-14 20:09:54', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：Macintosh');
INSERT INTO `actionlogs` VALUES ('6706854', 'kavin998', 'LOGIN', '2020-09-14 23:56:44', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：Macintosh');
INSERT INTO `actionlogs` VALUES ('6706855', 'kavin998', 'LOGIN', '2020-09-15 00:44:09', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：Macintosh');
INSERT INTO `actionlogs` VALUES ('6706856', 'kavin998', 'LOGIN', '2020-09-15 00:56:24', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：Macintosh');
INSERT INTO `actionlogs` VALUES ('6706857', 'kavin998', 'LOGIN', '2020-09-15 01:28:42', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：Macintosh');
INSERT INTO `actionlogs` VALUES ('6706858', 'kavin998', 'LOGIN', '2020-09-15 10:41:52', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：Macintosh');
INSERT INTO `actionlogs` VALUES ('6706859', 'kavin998', 'LOGIN', '2020-09-15 11:15:25', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：Macintosh');
INSERT INTO `actionlogs` VALUES ('6706860', 'kavin998', 'LOGIN', '2020-09-15 11:26:45', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：Macintosh');
INSERT INTO `actionlogs` VALUES ('6706861', 'kavin998', 'LOGIN', '2020-09-15 19:22:43', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：Macintosh');
INSERT INTO `actionlogs` VALUES ('6706862', 'kavin998', 'LOGIN', '2020-09-15 19:57:42', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：Macintosh');
INSERT INTO `actionlogs` VALUES ('6706863', 'kavin998', 'LOGIN', '2020-09-15 19:59:53', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：Macintosh');
INSERT INTO `actionlogs` VALUES ('6706864', 'kavin998', 'LOGIN', '2020-09-15 21:52:50', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：Macintosh');
INSERT INTO `actionlogs` VALUES ('6706865', 'kavin998', 'LOGIN', '2020-09-16 11:00:21', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：Macintosh');
INSERT INTO `actionlogs` VALUES ('6706866', 'kavin998', 'LOGIN', '2020-09-16 19:47:15', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：Macintosh');
INSERT INTO `actionlogs` VALUES ('6706867', 'kavin998', 'LOGIN', '2020-09-17 14:58:29', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：Macintosh');
INSERT INTO `actionlogs` VALUES ('6706868', 'kavin998', 'LOGIN', '2020-09-17 19:24:47', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：Macintosh');
INSERT INTO `actionlogs` VALUES ('6706869', 'kavin998', 'LOGIN', '2020-09-17 20:38:38', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：Macintosh');
INSERT INTO `actionlogs` VALUES ('6706870', 'kavin998', 'LOGIN', '2020-09-18 00:09:56', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：Macintosh');
INSERT INTO `actionlogs` VALUES ('6706871', 'kavin998', 'LOGIN', '2020-09-18 20:30:37', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：Macintosh');
INSERT INTO `actionlogs` VALUES ('6706872', 'kavin998', 'LOGIN', '2020-09-19 14:11:43', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：Macintosh');
INSERT INTO `actionlogs` VALUES ('6706873', 'kavin998', 'LOGIN', '2020-09-20 01:10:35', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：Macintosh');
INSERT INTO `actionlogs` VALUES ('6706874', 'kavin998', 'LOGIN', '2020-09-20 02:16:25', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：Macintosh');
INSERT INTO `actionlogs` VALUES ('6706875', 'kavin998', 'LOGIN', '2020-09-20 10:58:52', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：Macintosh');
INSERT INTO `actionlogs` VALUES ('6706876', 'kavin998', 'LOGIN', '2020-09-20 11:08:03', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：Macintosh');
INSERT INTO `actionlogs` VALUES ('6706877', 'kavin998', 'LOGIN', '2020-09-20 11:20:14', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：Macintosh');
INSERT INTO `actionlogs` VALUES ('6706878', 'kavin998', 'LOGIN', '2020-09-20 12:26:54', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：Macintosh');
INSERT INTO `actionlogs` VALUES ('6706879', 'kavin998', 'LOGIN', '2020-09-20 12:29:45', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：Macintosh');
INSERT INTO `actionlogs` VALUES ('6706880', 'kavin998', 'LOGIN', '2020-09-20 12:31:15', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：Macintosh');
INSERT INTO `actionlogs` VALUES ('6706881', 'kavin998', 'LOGIN', '2020-09-20 13:49:06', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：Macintosh');
INSERT INTO `actionlogs` VALUES ('6706882', 'austin998', 'MODIFY_CUS_INFO', '2020-09-20 14:57:54', '0:0:0:0:0:0:0:1;原昵称[]改为[null];原电话[o3jPTCyBXJaYn+hiEC3HLQ==]改为[null];原wechat[null]改为[];');
INSERT INTO `actionlogs` VALUES ('6706883', 'kavin998', 'LOGIN', '2020-09-20 18:52:45', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：Macintosh');
INSERT INTO `actionlogs` VALUES ('6706884', 'austin998', 'LOGIN', '2020-09-20 18:53:32', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：Macintosh');
INSERT INTO `actionlogs` VALUES ('6706885', 'kavin998', 'LOGIN', '2020-09-20 22:06:22', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：Macintosh');
INSERT INTO `actionlogs` VALUES ('6706886', 'austin998', 'LOGIN', '2020-09-20 22:16:29', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：Macintosh');
INSERT INTO `actionlogs` VALUES ('6706887', 'kavin998', 'LOGIN', '2020-09-21 11:41:04', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：Macintosh');
INSERT INTO `actionlogs` VALUES ('6706888', 'austin998', 'LOGIN', '2020-09-21 12:38:41', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：Macintosh');
INSERT INTO `actionlogs` VALUES ('6706889', 'austin998', 'LOGIN', '2020-09-21 14:41:38', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：Macintosh');
INSERT INTO `actionlogs` VALUES ('6706890', 'kavin998', 'LOGIN', '2020-09-21 15:35:40', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：Macintosh');
INSERT INTO `actionlogs` VALUES ('6706891', 'austin998', 'LOGIN', '2020-09-21 16:32:53', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：Macintosh');
INSERT INTO `actionlogs` VALUES ('6706892', 'kavin998', 'LOGIN', '2020-09-21 16:50:44', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：Macintosh');
INSERT INTO `actionlogs` VALUES ('6706893', 'austin998', 'LOGIN', '2020-09-21 16:51:08', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：Macintosh');
INSERT INTO `actionlogs` VALUES ('6706894', 'kavin998', 'LOGIN', '2020-09-21 20:45:17', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：Macintosh');
INSERT INTO `actionlogs` VALUES ('6706895', 'austin998', 'LOGIN', '2020-09-21 21:03:07', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：Macintosh');
INSERT INTO `actionlogs` VALUES ('6706896', 'kavin998', 'LOGIN', '2020-09-24 23:27:35', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：Macintosh');
INSERT INTO `actionlogs` VALUES ('6706897', 'kavin998', 'LOGIN', '2020-09-25 15:04:46', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：Macintosh');
INSERT INTO `actionlogs` VALUES ('6706898', 'kavin998', 'LOGIN', '2020-09-26 19:59:11', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：Macintosh');
INSERT INTO `actionlogs` VALUES ('6706899', 'kavin998', 'LOGIN', '2020-09-26 22:34:01', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：Macintosh');
INSERT INTO `actionlogs` VALUES ('6706900', 'austin998', 'LOGIN', '2020-09-26 22:39:00', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：Macintosh');
INSERT INTO `actionlogs` VALUES ('6706901', 'kavin998', 'LOGIN', '2020-09-26 22:44:21', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：Macintosh');
INSERT INTO `actionlogs` VALUES ('6706902', 'kavin998', 'LOGIN', '2020-09-26 23:48:52', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：Macintosh');
INSERT INTO `actionlogs` VALUES ('6706903', 'kavin998', 'LOGIN', '2020-09-27 13:13:00', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：Macintosh');
INSERT INTO `actionlogs` VALUES ('6706904', 'kavin998', 'LOGIN', '2020-09-27 13:23:30', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：Macintosh');
INSERT INTO `actionlogs` VALUES ('6706905', 'kavin123', 'MODIFY_CUS_INFO', '2020-09-27 15:39:15', '0:0:0:0:0:0:0:1;原昵称[]改为[null];原电话[UddJ8KqJis/XWac8CU8uxw==]改为[null];原wechat[null]改为[];');
INSERT INTO `actionlogs` VALUES ('6706906', 'kavin998', 'LOGIN', '2020-09-27 16:01:56', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：Macintosh');
INSERT INTO `actionlogs` VALUES ('6706907', 'kavin998', 'LOGIN', '2020-09-27 19:01:58', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：Macintosh');
INSERT INTO `actionlogs` VALUES ('6706908', 'kavin998', 'LOGIN', '2020-09-27 20:31:16', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：Macintosh');
INSERT INTO `actionlogs` VALUES ('6706909', 'kavin998', 'LOGIN', '2020-09-27 20:45:50', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：Macintosh');
INSERT INTO `actionlogs` VALUES ('6706910', 'kavin998', 'LOGIN', '2020-09-27 21:12:15', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：Macintosh');
INSERT INTO `actionlogs` VALUES ('6706911', 'kavin998', 'WRONG_PWD', '2020-09-27 23:31:27', '0:0:0:0:0:0:0:1');
INSERT INTO `actionlogs` VALUES ('6706912', 'kavin998', 'LOGIN', '2020-09-27 23:31:34', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：Macintosh');
INSERT INTO `actionlogs` VALUES ('6706913', 'kavin998', 'LOGIN', '2020-09-28 00:07:27', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：Macintosh');
INSERT INTO `actionlogs` VALUES ('6706914', 'kavin998', 'LOGIN', '2020-09-29 13:20:57', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：Linux');
INSERT INTO `actionlogs` VALUES ('6706915', 'kavin998', 'LOGIN', '2020-09-29 16:28:56', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：Linux');
INSERT INTO `actionlogs` VALUES ('6706916', 'kavin998', 'LOGIN', '2020-09-29 16:50:54', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：Macintosh');
INSERT INTO `actionlogs` VALUES ('6706917', 'kavin998', 'LOGIN', '2020-09-29 19:20:17', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：Macintosh');
INSERT INTO `actionlogs` VALUES ('6706918', 'kavin998', 'LOGIN', '2020-09-29 19:23:59', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：Macintosh');
INSERT INTO `actionlogs` VALUES ('6706919', 'kavin998', 'LOGIN', '2020-09-29 22:25:13', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：Macintosh');
INSERT INTO `actionlogs` VALUES ('6706920', 'kavin998', 'LOGIN', '2020-09-29 23:59:46', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：Macintosh');
INSERT INTO `actionlogs` VALUES ('6706921', 'kavin998', 'LOGIN', '2020-09-30 00:42:53', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：Macintosh');
INSERT INTO `actionlogs` VALUES ('6706922', 'kavin998', 'LOGIN', '2020-09-30 00:48:52', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：Macintosh');
INSERT INTO `actionlogs` VALUES ('6706923', 'kavin998', 'LOGIN', '2020-10-01 14:02:07', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：Macintosh');
INSERT INTO `actionlogs` VALUES ('6706924', 'kavin998', 'LOGIN', '2020-10-01 15:25:41', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：Macintosh');
INSERT INTO `actionlogs` VALUES ('6706925', 'kavin998', 'LOGIN', '2020-10-01 16:31:31', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：Macintosh');
INSERT INTO `actionlogs` VALUES ('6706926', 'kavin998', 'LOGIN', '2020-10-01 20:24:05', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：Macintosh');
INSERT INTO `actionlogs` VALUES ('6706927', 'kavin998', 'LOGIN', '2020-10-02 15:34:02', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：Macintosh');
INSERT INTO `actionlogs` VALUES ('6706928', 'kavin998', 'LOGIN', '2020-10-02 23:15:14', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：Macintosh');
INSERT INTO `actionlogs` VALUES ('6706929', 'kavin998', 'LOGIN', '2020-10-02 23:29:15', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：Macintosh');
INSERT INTO `actionlogs` VALUES ('6706930', 'kavin998', 'LOGIN', '2020-10-02 23:48:02', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：Macintosh');
INSERT INTO `actionlogs` VALUES ('6706931', 'kavin998', 'LOGIN', '2020-10-02 23:50:58', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：Macintosh');
INSERT INTO `actionlogs` VALUES ('6706932', 'kavin998', 'LOGIN', '2020-10-02 23:53:56', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：Macintosh');
INSERT INTO `actionlogs` VALUES ('6706933', 'kavin998', 'LOGIN', '2020-10-02 23:56:48', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：Macintosh');
INSERT INTO `actionlogs` VALUES ('6706934', 'kavin998', 'LOGIN', '2020-10-03 00:27:52', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：Macintosh');
INSERT INTO `actionlogs` VALUES ('6706935', 'kavin998', 'LOGIN', '2020-10-03 13:25:20', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：Macintosh');
INSERT INTO `actionlogs` VALUES ('6706936', 'kavin998', 'LOGIN', '2020-10-03 13:35:59', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：Macintosh');
INSERT INTO `actionlogs` VALUES ('6706937', 'kavin998', 'LOGIN', '2020-10-03 14:50:20', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：Macintosh');
INSERT INTO `actionlogs` VALUES ('6706938', 'kavin998', 'LOGIN', '2020-10-03 14:59:11', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：Macintosh');
INSERT INTO `actionlogs` VALUES ('6706939', 'kavin998', 'LOGIN', '2020-10-03 16:25:59', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：Macintosh');
INSERT INTO `actionlogs` VALUES ('6706940', 'kavin998', 'LOGIN', '2020-10-03 16:44:44', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：Macintosh');
INSERT INTO `actionlogs` VALUES ('6706941', 'kavin998', 'LOGIN', '2020-10-03 19:48:50', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：Macintosh');
INSERT INTO `actionlogs` VALUES ('6706942', 'austin998', 'LOGIN', '2020-10-03 20:35:05', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：Macintosh');
INSERT INTO `actionlogs` VALUES ('6706943', 'kavin998', 'LOGIN', '2020-10-04 18:48:44', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：Macintosh');
INSERT INTO `actionlogs` VALUES ('6706944', 'austin998', 'LOGIN', '2020-10-04 18:50:27', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：Macintosh');
INSERT INTO `actionlogs` VALUES ('6706945', 'kavin998', 'LOGIN', '2020-10-05 00:45:34', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：Linux');
INSERT INTO `actionlogs` VALUES ('6706946', 'kavin998', 'LOGIN', '2020-10-05 12:19:12', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：Linux');
INSERT INTO `actionlogs` VALUES ('6706947', 'austin998', 'LOGIN', '2020-10-05 13:23:29', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：Linux');
INSERT INTO `actionlogs` VALUES ('6706948', 'kavin998', 'LOGIN', '2020-10-05 13:38:12', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：Linux');
INSERT INTO `actionlogs` VALUES ('6706949', 'austin998', 'LOGIN', '2020-10-05 13:44:07', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：Linux');
INSERT INTO `actionlogs` VALUES ('6706950', 'kavin998', 'LOGIN', '2020-10-05 15:30:58', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：Linux');
INSERT INTO `actionlogs` VALUES ('6706951', 'kavin998', 'LOGIN', '2020-10-05 23:13:34', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：Linux');
INSERT INTO `actionlogs` VALUES ('6706952', 'kavin998', 'LOGIN', '2020-10-06 12:47:26', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：未知');
INSERT INTO `actionlogs` VALUES ('6706953', 'kavin998', 'LOGIN', '2020-10-06 16:27:57', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：未知');
INSERT INTO `actionlogs` VALUES ('6706954', 'kavin998', 'LOGIN', '2020-10-06 16:53:11', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：未知');
INSERT INTO `actionlogs` VALUES ('6706955', 'kavin998', 'LOGIN', '2020-10-06 17:02:05', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：未知');
INSERT INTO `actionlogs` VALUES ('6706956', 'austin998', 'LOGIN', '2020-10-06 17:03:02', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：未知');
INSERT INTO `actionlogs` VALUES ('6706957', 'kavin998', 'LOGIN', '2020-10-06 17:22:54', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：未知');
INSERT INTO `actionlogs` VALUES ('6706958', 'austin998', 'LOGIN', '2020-10-06 18:04:56', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：未知');
INSERT INTO `actionlogs` VALUES ('6706959', 'kavin998', 'LOGIN', '2020-10-06 18:45:19', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：未知');
INSERT INTO `actionlogs` VALUES ('6706960', 'kavin998', 'LOGIN', '2020-10-06 19:45:42', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：未知');
INSERT INTO `actionlogs` VALUES ('6706961', 'kavin998', 'LOGIN', '2020-10-06 21:46:01', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：未知');
INSERT INTO `actionlogs` VALUES ('6706962', 'kavin998', 'LOGIN', '2020-10-06 22:10:03', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：未知');
INSERT INTO `actionlogs` VALUES ('6706963', 'kavin998', 'LOGIN', '2020-10-06 22:23:11', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：未知');
INSERT INTO `actionlogs` VALUES ('6706964', 'kavin998', 'LOGIN', '2020-10-06 22:30:29', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：未知');
INSERT INTO `actionlogs` VALUES ('6706965', 'kavin998', 'LOGIN', '2020-10-06 22:33:53', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：未知');
INSERT INTO `actionlogs` VALUES ('6706966', 'kavin998', 'LOGIN', '2020-10-06 22:42:53', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：未知');
INSERT INTO `actionlogs` VALUES ('6706967', 'kavin998', 'LOGIN', '2020-10-06 22:47:34', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：未知');
INSERT INTO `actionlogs` VALUES ('6706968', 'kavin998', 'LOGIN', '2020-10-06 22:51:16', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：未知');
INSERT INTO `actionlogs` VALUES ('6706969', 'kavin998', 'LOGIN', '2020-10-06 22:59:26', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：未知');
INSERT INTO `actionlogs` VALUES ('6706970', 'kavin998', 'LOGIN', '2020-10-07 01:04:25', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：未知');
INSERT INTO `actionlogs` VALUES ('6706971', 'austin998', 'LOGIN', '2020-10-07 01:15:12', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：未知');
INSERT INTO `actionlogs` VALUES ('6706972', 'kavin998', 'LOGIN', '2020-10-07 10:41:34', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：未知');
INSERT INTO `actionlogs` VALUES ('6706973', 'austin998', 'LOGIN', '2020-10-07 10:42:02', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：未知');
INSERT INTO `actionlogs` VALUES ('6706974', 'austin998', 'LOGIN', '2020-10-07 15:08:45', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：未知');
INSERT INTO `actionlogs` VALUES ('6706975', 'austin998', 'LOGIN', '2020-10-07 16:15:04', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：未知');
INSERT INTO `actionlogs` VALUES ('6706976', 'austin998', 'LOGIN', '2020-10-07 16:41:12', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：未知');
INSERT INTO `actionlogs` VALUES ('6706977', 'austin998', 'LOGIN', '2020-10-07 17:24:57', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：未知');
INSERT INTO `actionlogs` VALUES ('6706978', 'kavin998', 'LOGIN', '2020-10-07 17:29:19', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：未知');
INSERT INTO `actionlogs` VALUES ('6706979', 'austin998', 'LOGIN', '2020-10-07 17:29:38', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：未知');
INSERT INTO `actionlogs` VALUES ('6706980', 'austin998', 'LOGIN', '2020-10-07 17:36:20', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：未知');
INSERT INTO `actionlogs` VALUES ('6706981', 'austin998', 'LOGIN', '2020-10-07 17:36:50', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：未知');
INSERT INTO `actionlogs` VALUES ('6706982', 'austin998', 'LOGIN', '2020-10-07 17:40:32', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：未知');
INSERT INTO `actionlogs` VALUES ('6706983', 'kavin998', 'LOGIN', '2020-10-07 17:43:51', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：未知');
INSERT INTO `actionlogs` VALUES ('6706984', 'austin998', 'LOGIN', '2020-10-08 01:52:56', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：未知');
INSERT INTO `actionlogs` VALUES ('6706985', 'austin998', 'LOGIN', '2020-10-08 01:57:44', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：未知');
INSERT INTO `actionlogs` VALUES ('6706986', 'austin998', 'LOGIN', '2020-10-08 02:16:47', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：未知');
INSERT INTO `actionlogs` VALUES ('6706987', 'austin998', 'LOGIN', '2020-10-08 02:19:33', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：未知');
INSERT INTO `actionlogs` VALUES ('6706988', 'austin998', 'LOGIN', '2020-10-08 13:03:03', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：未知');
INSERT INTO `actionlogs` VALUES ('6706989', 'austin998', 'LOGIN', '2020-10-08 14:19:55', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：未知');
INSERT INTO `actionlogs` VALUES ('6706990', 'austin998', 'LOGIN', '2020-10-08 15:16:14', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：未知');
INSERT INTO `actionlogs` VALUES ('6706991', 'austin998', 'LOGIN', '2020-10-08 16:36:59', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：未知');
INSERT INTO `actionlogs` VALUES ('6706992', 'austin998', 'LOGIN', '2020-10-08 18:10:28', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：未知');
INSERT INTO `actionlogs` VALUES ('6706993', 'austin998', 'LOGIN', '2020-10-08 19:27:44', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：未知');
INSERT INTO `actionlogs` VALUES ('6706994', 'kavin998', 'LOGIN', '2020-10-08 19:44:10', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：未知');
INSERT INTO `actionlogs` VALUES ('6706995', 'austin998', 'LOGIN', '2020-10-08 19:44:55', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：未知');
INSERT INTO `actionlogs` VALUES ('6706996', 'austin998', 'LOGIN', '2020-10-08 19:48:53', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：未知');
INSERT INTO `actionlogs` VALUES ('6706997', 'austin998', 'LOGIN', '2020-10-08 19:55:31', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：未知');
INSERT INTO `actionlogs` VALUES ('6706998', 'austin998', 'LOGIN', '2020-10-09 00:54:20', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：Linux');
INSERT INTO `actionlogs` VALUES ('6706999', 'austin998', 'LOGIN', '2020-10-09 02:16:51', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：Linux');
INSERT INTO `actionlogs` VALUES ('6707000', 'austin998', 'LOGIN', '2020-10-09 02:25:43', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：Linux');
INSERT INTO `actionlogs` VALUES ('6707001', 'austin998', 'LOGIN', '2020-10-09 03:57:10', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：未知');
INSERT INTO `actionlogs` VALUES ('6707002', 'austin998', 'LOGIN', '2020-10-09 04:04:32', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：未知');
INSERT INTO `actionlogs` VALUES ('6707003', 'kavin998', 'LOGIN', '2020-10-09 04:21:19', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：未知');
INSERT INTO `actionlogs` VALUES ('6707004', 'austin998', 'LOGIN', '2020-10-09 04:21:43', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：未知');
INSERT INTO `actionlogs` VALUES ('6707005', 'kavin998', 'LOGIN', '2020-10-09 05:04:58', 'ip:0:0:0:0:0:0:0:1;最后登录地址：;客户操作系统：未知');

-- ----------------------------
-- Table structure for `activity`
-- ----------------------------
DROP TABLE IF EXISTS `activity`;
CREATE TABLE `activity` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `activityName` varchar(50) DEFAULT NULL,
  `activityPercent` double DEFAULT NULL,
  `activityStart` datetime DEFAULT NULL,
  `activityEnd` datetime DEFAULT NULL,
  `backstageStart` datetime DEFAULT NULL,
  `backstageEnd` datetime DEFAULT NULL,
  `createDate` datetime DEFAULT NULL,
  `activityStatus` int(11) DEFAULT '0' COMMENT '0表示未启用 1表示启用',
  `userrole` varchar(50) DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=50 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of activity
-- ----------------------------
INSERT INTO `activity` VALUES ('1', '红包雨活动', null, null, null, null, null, '2020-09-15 01:03:56', null, 'kavin998', '主账户转入红包雨账户 0.0->11.0');
INSERT INTO `activity` VALUES ('2', '红包雨活动', null, null, null, null, null, '2020-09-15 01:04:25', null, 'kavin998', '主账户转入红包雨账户 11.0->22.0');
INSERT INTO `activity` VALUES ('3', '红包雨活动', null, null, null, null, null, '2020-09-15 01:49:24', null, 'kavin998', '红包雨余额转入MG 22.0->11.0');
INSERT INTO `activity` VALUES ('4', '红包雨活动', null, null, null, null, null, '2020-09-15 10:42:11', null, 'kavin998', '主账户转入红包雨账户 11.0->111.0');
INSERT INTO `activity` VALUES ('5', '红包雨活动', null, null, null, null, null, '2020-09-15 10:42:25', null, 'kavin998', '红包雨余额转入MG 111.0->100.0');
INSERT INTO `activity` VALUES ('6', '红包雨活动', null, null, null, null, null, '2020-09-15 11:00:25', null, 'kavin998', '红包雨余额转入MG 100.0->89.0');
INSERT INTO `activity` VALUES ('18', '红包雨活动', null, null, null, null, null, '2020-09-15 11:45:35', null, 'kavin998', '红包雨余额转入QT 89.0->78.0');
INSERT INTO `activity` VALUES ('19', '红包雨活动', null, null, null, null, null, '2020-09-20 18:43:18', null, 'austin998', '主账户转入红包雨账户 10.0->160.0');
INSERT INTO `activity` VALUES ('20', '红包雨活动', null, null, null, null, null, '2020-09-20 19:41:13', null, 'austin998', '红包雨余额转入CQ9 160.0->149.0');
INSERT INTO `activity` VALUES ('21', '红包雨活动', null, null, null, null, null, '2020-09-20 19:41:51', null, 'austin998', '红包雨余额转入CQ9 149.0->138.0');
INSERT INTO `activity` VALUES ('22', '红包雨活动', null, null, null, null, null, '2020-09-20 20:04:01', null, 'austin998', '红包雨余额转入CQ9 138.0->125.0');
INSERT INTO `activity` VALUES ('23', '红包雨活动', null, null, null, null, null, '2020-09-20 20:05:18', null, 'austin998', '红包雨余额转入CQ9 125.0->113.0');
INSERT INTO `activity` VALUES ('24', '红包雨活动', null, null, null, null, null, '2020-09-20 20:06:20', null, 'austin998', '红包雨余额转入CQ9 113.0->100.0');
INSERT INTO `activity` VALUES ('25', '红包雨活动', null, null, null, null, null, '2020-09-20 20:07:52', null, 'austin998', '红包雨余额转入CQ9 100.0->89.0');
INSERT INTO `activity` VALUES ('26', '红包雨活动', null, null, null, null, null, '2020-09-20 20:08:43', null, 'austin998', '红包雨余额转入CQ9 89.0->78.0');
INSERT INTO `activity` VALUES ('27', '红包雨活动', null, null, null, null, null, '2020-09-20 20:10:12', null, 'austin998', '红包雨余额转入CQ9 78.0->67.0');
INSERT INTO `activity` VALUES ('28', '红包雨活动', null, null, null, null, null, '2020-09-20 20:10:44', null, 'austin998', '主账户转入红包雨账户 67.0->179.0');
INSERT INTO `activity` VALUES ('29', '红包雨活动', null, null, null, null, null, '2020-09-20 20:10:58', null, 'austin998', '红包雨余额转入CQ9 179.0->157.0');
INSERT INTO `activity` VALUES ('30', '红包雨活动', null, null, null, null, null, '2020-09-27 13:30:38', null, 'kavin998', '红包雨余额转入CQ9 78.0->66.0');
INSERT INTO `activity` VALUES ('31', '红包雨活动', null, null, null, null, null, '2020-09-27 13:34:43', null, 'kavin998', '红包雨余额转入CQ9 66.0->54.0');
INSERT INTO `activity` VALUES ('32', '红包雨活动', null, null, null, null, null, '2020-09-27 13:37:34', null, 'kavin998', '红包雨余额转入CQ9 54.0->43.0');
INSERT INTO `activity` VALUES ('33', '红包雨活动', null, null, null, null, null, '2020-09-27 14:00:24', null, 'kavin998', '红包雨余额转入PG 43.0->32.0');
INSERT INTO `activity` VALUES ('34', '红包雨活动', null, null, null, null, null, '2020-09-27 14:02:34', null, 'kavin998', '红包雨余额转入PG 32.0->12.0');
INSERT INTO `activity` VALUES ('35', '红包雨活动', null, null, null, null, null, '2020-09-30 00:43:20', null, 'kavin998', '主账户转入红包雨账户 12.0->912.0');
INSERT INTO `activity` VALUES ('36', '红包雨活动', null, null, null, null, null, '2020-09-30 00:43:37', null, 'kavin998', '红包雨余额转入BG912.0->900.0');
INSERT INTO `activity` VALUES ('37', '红包雨活动', null, null, null, null, null, '2020-09-30 00:44:04', null, 'kavin998', '红包雨余额转入BG900.0->888.0');
INSERT INTO `activity` VALUES ('38', '红包雨活动', null, null, null, null, null, '2020-09-30 00:45:15', null, 'kavin998', '红包雨余额转入BG888.0->876.0');
INSERT INTO `activity` VALUES ('39', '红包雨活动', null, null, null, null, null, '2020-09-30 00:49:07', null, 'kavin998', '红包雨余额转入BG876.0->864.0');
INSERT INTO `activity` VALUES ('40', '红包雨活动', null, null, null, null, null, '2020-10-05 00:46:19', null, 'kavin998', '红包雨余额转入MG 864.0->853.0');
INSERT INTO `activity` VALUES ('41', '红包雨活动', null, null, null, null, null, '2020-10-05 15:12:37', null, 'austin998', '红包雨余额转入SBA157.0->147.0');
INSERT INTO `activity` VALUES ('42', '红包雨活动', null, null, null, null, null, '2020-10-05 15:13:37', null, 'austin998', '红包雨余额转入MG 147.0->137.0');
INSERT INTO `activity` VALUES ('43', '红包雨活动', null, null, null, null, null, '2020-10-05 15:20:59', null, 'austin998', '红包雨余额转入SBA137.0->126.0');
INSERT INTO `activity` VALUES ('44', '红包雨活动', null, null, null, null, null, '2020-10-05 15:34:10', null, 'kavin998', '红包雨余额转入SBA853.0->842.0');
INSERT INTO `activity` VALUES ('45', '红包雨活动', null, null, null, null, null, '2020-10-05 15:34:35', null, 'kavin998', '红包雨余额转入SBA842.0->831.0');
INSERT INTO `activity` VALUES ('46', '红包雨活动', null, null, null, null, null, '2020-10-05 15:38:22', null, 'kavin998', '红包雨余额转入SBA831.0->820.0');
INSERT INTO `activity` VALUES ('47', '红包雨活动', null, null, null, null, null, '2020-10-05 15:41:41', null, 'kavin998', '红包雨余额转入SBA820.0->670.0');
INSERT INTO `activity` VALUES ('48', '红包雨活动', null, null, null, null, null, '2020-10-08 18:41:19', null, 'austin998', '主账户转入红包雨账户 126.0->137.0');
INSERT INTO `activity` VALUES ('49', '红包雨活动', null, null, null, null, null, '2020-10-08 18:41:26', null, 'austin998', '红包雨余额转入QT 137.0->126.0');

-- ----------------------------
-- Table structure for `activity_calendar`
-- ----------------------------
DROP TABLE IF EXISTS `activity_calendar`;
CREATE TABLE `activity_calendar` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL COMMENT '活动名称',
  `content` varchar(20000) NOT NULL COMMENT '活动内容',
  `activityDate` date DEFAULT NULL COMMENT '活动日期',
  `flag` int(2) NOT NULL COMMENT '是否禁用 1启用 2禁止',
  `creator` varchar(28) NOT NULL COMMENT '创建人',
  `createtime` datetime NOT NULL COMMENT '创建时间',
  `orderBy` int(2) DEFAULT '0' COMMENT '排序',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of activity_calendar
-- ----------------------------

-- ----------------------------
-- Table structure for `activity_config`
-- ----------------------------
DROP TABLE IF EXISTS `activity_config`;
CREATE TABLE `activity_config` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键标识ID',
  `title` varchar(50) DEFAULT NULL COMMENT '活动名称',
  `amount` double DEFAULT NULL COMMENT '奖励金额',
  `level` varchar(20) DEFAULT NULL COMMENT '等级',
  `createTime` datetime DEFAULT NULL COMMENT '创建时间',
  `updateTime` datetime DEFAULT NULL COMMENT '更新时间',
  `scope` varchar(20) DEFAULT NULL COMMENT '范围',
  `times` int(11) DEFAULT NULL COMMENT '区间内的次数',
  `entrance` varchar(50) DEFAULT NULL COMMENT '平台通道',
  `status` int(11) DEFAULT NULL COMMENT '开关',
  `deposit` double DEFAULT NULL COMMENT '存款额',
  `bet` double DEFAULT NULL COMMENT '投注额',
  `multiple` int(11) DEFAULT NULL COMMENT '倍数',
  `multiplestatus` int(11) DEFAULT NULL COMMENT '倍数开关',
  `platform` varchar(100) DEFAULT NULL COMMENT '转入平台',
  `depositStartTime` datetime DEFAULT NULL COMMENT '存款开始时间',
  `depositEndTime` datetime DEFAULT NULL COMMENT '存款结束时间',
  `activityStartTime` datetime DEFAULT NULL COMMENT '活动开始时间',
  `activityEndTime` datetime DEFAULT NULL COMMENT '活动结束时间',
  `betStartTime` datetime DEFAULT NULL COMMENT '投注开始时间',
  `betEndTime` datetime DEFAULT NULL COMMENT '投注结束时间',
  `machineCode` int(11) DEFAULT '0' COMMENT '验证机器码开关',
  `remark` varchar(100) DEFAULT NULL COMMENT '备注',
  `englishTitle` varchar(50) NOT NULL,
  `sidCount` int(11) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of activity_config
-- ----------------------------

-- ----------------------------
-- Table structure for `activity_history`
-- ----------------------------
DROP TABLE IF EXISTS `activity_history`;
CREATE TABLE `activity_history` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userName` varchar(50) DEFAULT NULL COMMENT '用户名',
  `level` int(11) DEFAULT NULL COMMENT '等级',
  `accountName` varchar(50) DEFAULT NULL COMMENT '真实姓名',
  `phone` varchar(50) DEFAULT NULL COMMENT '手机号',
  `address` varchar(200) DEFAULT NULL COMMENT '地址',
  `createTime` datetime DEFAULT NULL COMMENT '创建时间',
  `status` varchar(50) DEFAULT NULL COMMENT '领取的状态',
  `scope` varchar(50) DEFAULT NULL COMMENT '区间的唯一标识',
  `title` varchar(100) DEFAULT NULL COMMENT '活动名称',
  `remark` varchar(100) DEFAULT NULL COMMENT '备注',
  `times` int(11) DEFAULT NULL,
  `englishTitle` varchar(50) DEFAULT NULL,
  `sid` varchar(100) DEFAULT NULL,
  `activityId` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of activity_history
-- ----------------------------

-- ----------------------------
-- Table structure for `agdata`
-- ----------------------------
DROP TABLE IF EXISTS `agdata`;
CREATE TABLE `agdata` (
  `billNo` varchar(32) NOT NULL COMMENT '流水号',
  `gameCode` varchar(32) DEFAULT NULL COMMENT '局号',
  `playName` varchar(32) DEFAULT NULL COMMENT '玩家账号',
  `betAmount` double(24,2) DEFAULT NULL COMMENT '投注额度',
  `validBetAmount` double(24,2) DEFAULT NULL COMMENT '有投注额度',
  `netAmount` double(24,2) DEFAULT NULL COMMENT '玩家输赢',
  `beforeCredit` double(24,2) DEFAULT NULL COMMENT '下注前额度',
  `gameType` varchar(32) DEFAULT NULL COMMENT '游戏类型',
  `deviceType` varchar(8) DEFAULT NULL COMMENT '设备类型',
  `betTime` datetime DEFAULT NULL COMMENT '下注时间',
  `recalcuTime` datetime DEFAULT NULL COMMENT '派彩时间',
  `platformType` varchar(16) DEFAULT NULL COMMENT '平台类型',
  PRIMARY KEY (`billNo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of agdata
-- ----------------------------

-- ----------------------------
-- Table structure for `agent_address`
-- ----------------------------
DROP TABLE IF EXISTS `agent_address`;
CREATE TABLE `agent_address` (
  `address` varchar(200) NOT NULL,
  `loginname` varchar(255) NOT NULL,
  `deleteflag` int(2) NOT NULL DEFAULT '0',
  `createtime` datetime NOT NULL,
  PRIMARY KEY (`address`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of agent_address
-- ----------------------------

-- ----------------------------
-- Table structure for `agent_customer`
-- ----------------------------
DROP TABLE IF EXISTS `agent_customer`;
CREATE TABLE `agent_customer` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(32) DEFAULT '',
  `email` varchar(32) DEFAULT '',
  `phone` varchar(32) DEFAULT '',
  `isreg` int(1) DEFAULT '0',
  `isdeposit` int(1) DEFAULT '0',
  `phonestatus` int(1) DEFAULT '0',
  `userstatus` int(1) DEFAULT '0',
  `cs` varchar(32) DEFAULT '',
  `remark` varchar(100) DEFAULT '',
  `createTime` datetime DEFAULT NULL,
  `batch` int(11) DEFAULT '1',
  `shippingcode` varchar(50) DEFAULT NULL,
  `type` char(4) DEFAULT NULL,
  `qq` varchar(255) DEFAULT NULL,
  `noticeTime` datetime DEFAULT NULL,
  `win` double(20,2) DEFAULT NULL,
  `emailflag` int(2) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of agent_customer
-- ----------------------------

-- ----------------------------
-- Table structure for `agprofit`
-- ----------------------------
DROP TABLE IF EXISTS `agprofit`;
CREATE TABLE `agprofit` (
  `pno` varchar(20) NOT NULL,
  `proposer` varchar(20) NOT NULL,
  `createTime` datetime NOT NULL,
  `type` int(3) NOT NULL,
  `quickly` int(1) NOT NULL DEFAULT '0',
  `loginname` varchar(20) DEFAULT NULL,
  `amount` double(24,2) DEFAULT NULL,
  `agent` varchar(20) DEFAULT '',
  `flag` int(1) NOT NULL DEFAULT '0',
  `whereisfrom` varchar(10) NOT NULL DEFAULT '前台',
  `remark` varchar(500) DEFAULT NULL,
  `generateType` varchar(20) DEFAULT NULL,
  `platform` varchar(12) DEFAULT NULL,
  `bettotal` double(24,2) DEFAULT '0.00',
  PRIMARY KEY (`pno`),
  KEY `proposer` (`proposer`) USING BTREE,
  KEY `type` (`type`) USING BTREE,
  KEY `loginname` (`loginname`) USING BTREE,
  KEY `flag` (`flag`) USING BTREE,
  KEY `createTime` (`createTime`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of agprofit
-- ----------------------------

-- ----------------------------
-- Table structure for `agtrygame`
-- ----------------------------
DROP TABLE IF EXISTS `agtrygame`;
CREATE TABLE `agtrygame` (
  `agname` varchar(20) NOT NULL,
  `agpassword` varchar(50) DEFAULT NULL,
  `agphone` varchar(15) DEFAULT NULL,
  `agip` varchar(50) NOT NULL,
  `agregdate` datetime NOT NULL,
  `agislogin` int(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`agname`),
  UNIQUE KEY `agname` (`agname`) USING BTREE,
  UNIQUE KEY `agphone` (`agphone`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of agtrygame
-- ----------------------------
INSERT INTO `agtrygame` VALUES ('kt_0025357144', 'kt_0025357144', null, '66.249.79.119', '2018-12-10 02:53:57', '1');
INSERT INTO `agtrygame` VALUES ('kt_0025357340', 'kt_0025357340', null, '66.249.79.115', '2018-12-10 02:53:57', '1');
INSERT INTO `agtrygame` VALUES ('kt_0040934393', 'kt_0040934393', null, '66.249.79.117', '2018-12-10 04:09:34', '1');
INSERT INTO `agtrygame` VALUES ('kt_0040934581', 'kt_0040934581', null, '66.249.79.115', '2018-12-10 04:09:34', '1');
INSERT INTO `agtrygame` VALUES ('kt_0044105209', 'kt_0044105209', null, '66.249.79.117', '2018-12-10 04:41:05', '1');
INSERT INTO `agtrygame` VALUES ('kt_0044105447', 'kt_0044105447', null, '66.249.79.119', '2018-12-10 04:41:05', '1');
INSERT INTO `agtrygame` VALUES ('kt_0050000296', 'kt_0050000296', null, '66.249.79.119', '2018-12-10 05:00:00', '1');
INSERT INTO `agtrygame` VALUES ('kt_0050000485', 'kt_0050000485', null, '66.249.79.117', '2018-12-10 05:00:00', '1');
INSERT INTO `agtrygame` VALUES ('kt_0050618156', 'kt_0050618156', null, '66.249.79.115', '2018-12-10 05:06:18', '1');
INSERT INTO `agtrygame` VALUES ('kt_0050618344', 'kt_0050618344', null, '66.249.79.119', '2018-12-10 05:06:18', '1');
INSERT INTO `agtrygame` VALUES ('kt_0111146689', 'kt_0111146689', null, '66.249.79.117', '2018-12-10 11:11:46', '1');
INSERT INTO `agtrygame` VALUES ('kt_0111146880', 'kt_0111146880', null, '66.249.79.115', '2018-12-10 11:11:46', '1');
INSERT INTO `agtrygame` VALUES ('kt_0111805161', 'kt_0111805161', null, '66.249.79.117', '2018-12-10 11:18:05', '1');
INSERT INTO `agtrygame` VALUES ('kt_0111805347', 'kt_0111805347', null, '66.249.79.115', '2018-12-10 11:18:05', '1');
INSERT INTO `agtrygame` VALUES ('kt_0112423136', 'kt_0112423136', null, '66.249.79.119', '2018-12-10 11:24:23', '1');
INSERT INTO `agtrygame` VALUES ('kt_0112423323', 'kt_0112423323', null, '66.249.79.119', '2018-12-10 11:24:23', '1');
INSERT INTO `agtrygame` VALUES ('kt_0113700094', 'kt_0113700094', null, '66.249.79.115', '2018-12-10 11:37:00', '1');
INSERT INTO `agtrygame` VALUES ('kt_0113700321', 'kt_0113700321', null, '66.249.79.115', '2018-12-10 11:37:00', '1');
INSERT INTO `agtrygame` VALUES ('kt_0114318375', 'kt_0114318375', null, '66.249.79.119', '2018-12-10 11:43:18', '1');
INSERT INTO `agtrygame` VALUES ('kt_0114318563', 'kt_0114318563', null, '66.249.79.119', '2018-12-10 11:43:18', '1');
INSERT INTO `agtrygame` VALUES ('kt_0114936213', 'kt_0114936213', null, '66.249.79.117', '2018-12-10 11:49:36', '1');
INSERT INTO `agtrygame` VALUES ('kt_0114936401', 'kt_0114936401', null, '66.249.79.119', '2018-12-10 11:49:36', '1');
INSERT INTO `agtrygame` VALUES ('kt_0120212142', 'kt_0120212142', null, '66.249.79.119', '2018-12-10 12:02:12', '1');
INSERT INTO `agtrygame` VALUES ('kt_0120212329', 'kt_0120212329', null, '66.249.79.119', '2018-12-10 12:02:12', '1');
INSERT INTO `agtrygame` VALUES ('kt_0121448448', 'kt_0121448448', null, '66.249.79.117', '2018-12-10 12:14:48', '1');
INSERT INTO `agtrygame` VALUES ('kt_0121448635', 'kt_0121448635', null, '66.249.79.119', '2018-12-10 12:14:48', '1');
INSERT INTO `agtrygame` VALUES ('kt_0122106175', 'kt_0122106175', null, '66.249.79.115', '2018-12-10 12:21:06', '1');
INSERT INTO `agtrygame` VALUES ('kt_0122106363', 'kt_0122106363', null, '66.249.79.117', '2018-12-10 12:21:06', '1');
INSERT INTO `agtrygame` VALUES ('kt_0122724251', 'kt_0122724251', null, '66.249.79.117', '2018-12-10 12:27:24', '1');
INSERT INTO `agtrygame` VALUES ('kt_0122724439', 'kt_0122724439', null, '66.249.79.115', '2018-12-10 12:27:24', '1');
INSERT INTO `agtrygame` VALUES ('kt_0124003214', 'kt_0124003214', null, '66.249.79.115', '2018-12-10 12:40:03', '1');
INSERT INTO `agtrygame` VALUES ('kt_0124003406', 'kt_0124003406', null, '66.249.79.119', '2018-12-10 12:40:03', '1');
INSERT INTO `agtrygame` VALUES ('kt_0143326139', 'kt_0143326139', null, '66.249.79.117', '2018-12-10 14:33:26', '1');
INSERT INTO `agtrygame` VALUES ('kt_0143326326', 'kt_0143326326', null, '66.249.79.115', '2018-12-10 14:33:26', '1');
INSERT INTO `agtrygame` VALUES ('kt_0143944299', 'kt_0143944299', null, '66.249.79.119', '2018-12-10 14:39:44', '1');
INSERT INTO `agtrygame` VALUES ('kt_0143944487', 'kt_0143944487', null, '66.249.79.119', '2018-12-10 14:39:44', '1');
INSERT INTO `agtrygame` VALUES ('kt_0164546049', 'kt_0164546049', null, '66.249.79.117', '2018-12-10 16:45:46', '1');
INSERT INTO `agtrygame` VALUES ('kt_0164546238', 'kt_0164546238', null, '66.249.79.119', '2018-12-10 16:45:46', '1');
INSERT INTO `agtrygame` VALUES ('kt_0165204181', 'kt_0165204181', null, '66.249.79.115', '2018-12-10 16:52:04', '1');
INSERT INTO `agtrygame` VALUES ('kt_0165204369', 'kt_0165204369', null, '66.249.79.115', '2018-12-10 16:52:04', '1');
INSERT INTO `agtrygame` VALUES ('kt_0165822144', 'kt_0165822144', null, '66.249.79.115', '2018-12-10 16:58:22', '1');
INSERT INTO `agtrygame` VALUES ('kt_0165822331', 'kt_0165822331', null, '66.249.79.119', '2018-12-10 16:58:22', '1');
INSERT INTO `agtrygame` VALUES ('kt_0170440232', 'kt_0170440232', null, '66.249.79.117', '2018-12-10 17:04:40', '1');
INSERT INTO `agtrygame` VALUES ('kt_0170440420', 'kt_0170440420', null, '66.249.79.115', '2018-12-10 17:04:40', '1');
INSERT INTO `agtrygame` VALUES ('kt_0213536246', 'kt_0213536246', null, '66.249.79.119', '2018-12-10 21:35:36', '1');
INSERT INTO `agtrygame` VALUES ('kt_0213536437', 'kt_0213536437', null, '66.249.79.115', '2018-12-10 21:35:36', '1');
INSERT INTO `agtrygame` VALUES ('kt_0221946543', 'kt_0221946543', null, '66.249.79.117', '2018-12-10 22:19:46', '1');
INSERT INTO `agtrygame` VALUES ('kt_0221946731', 'kt_0221946731', null, '66.249.79.119', '2018-12-10 22:19:46', '1');
INSERT INTO `agtrygame` VALUES ('kt_0233521240', 'kt_0233521240', null, '66.249.79.117', '2018-12-10 23:35:21', '1');
INSERT INTO `agtrygame` VALUES ('kt_0233521434', 'kt_0233521434', null, '66.249.79.119', '2018-12-10 23:35:21', '1');
INSERT INTO `agtrygame` VALUES ('kt_0235730349', 'kt_0235730349', null, '158.69.27.211', '2018-11-30 23:57:30', '5');
INSERT INTO `agtrygame` VALUES ('kt_0235811076', 'kt_0235811076', null, '192.99.47.111', '2018-11-30 23:58:11', '5');
INSERT INTO `agtrygame` VALUES ('kt_1000211428', 'kt_1000211428', null, '158.69.225.37', '2018-12-01 00:02:11', '5');
INSERT INTO `agtrygame` VALUES ('kt_1101915602', 'kt_1101915602', null, '38.100.21.64', '2018-12-21 10:19:15', '1');
INSERT INTO `agtrygame` VALUES ('kt_1144335719', 'kt_1144335719', null, '66.249.79.70', '2018-12-21 14:43:35', '1');
INSERT INTO `agtrygame` VALUES ('kt_1144335910', 'kt_1144335910', null, '66.249.79.72', '2018-12-21 14:43:35', '1');
INSERT INTO `agtrygame` VALUES ('kt_1182704388', 'kt_1182704388', null, '66.249.79.208', '2018-12-11 18:27:04', '1');
INSERT INTO `agtrygame` VALUES ('kt_1182704491', 'kt_1182704491', null, '66.249.79.210', '2018-12-11 18:27:04', '1');
INSERT INTO `agtrygame` VALUES ('kt_1182727321', 'kt_1182727321', null, '66.249.79.208', '2018-12-11 18:27:27', '1');
INSERT INTO `agtrygame` VALUES ('kt_1182727508', 'kt_1182727508', null, '66.249.79.208', '2018-12-11 18:27:27', '1');
INSERT INTO `agtrygame` VALUES ('kt_1223809684', 'kt_1223809684', null, '66.249.79.70', '2018-12-21 22:38:09', '1');
INSERT INTO `agtrygame` VALUES ('kt_1223809875', 'kt_1223809875', null, '66.249.79.70', '2018-12-21 22:38:09', '1');
INSERT INTO `agtrygame` VALUES ('kt_1235357316', 'kt_1235357316', null, '66.249.79.72', '2018-12-21 23:53:57', '1');
INSERT INTO `agtrygame` VALUES ('kt_1235357512', 'kt_1235357512', null, '66.249.79.74', '2018-12-21 23:53:57', '1');
INSERT INTO `agtrygame` VALUES ('kt_2010944537', 'kt_2010944537', null, '66.249.79.70', '2018-12-22 01:09:44', '1');
INSERT INTO `agtrygame` VALUES ('kt_2010944726', 'kt_2010944726', null, '66.249.79.72', '2018-12-22 01:09:44', '1');
INSERT INTO `agtrygame` VALUES ('kt_2022531927', 'kt_2022531927', null, '66.249.79.74', '2018-12-22 02:25:31', '1');
INSERT INTO `agtrygame` VALUES ('kt_2022532147', 'kt_2022532147', null, '66.249.79.72', '2018-12-22 02:25:32', '1');
INSERT INTO `agtrygame` VALUES ('kt_2050144554', 'kt_2050144554', null, '66.249.79.117', '2018-12-22 05:01:44', '1');
INSERT INTO `agtrygame` VALUES ('kt_2050144749', 'kt_2050144749', null, '66.249.79.119', '2018-12-22 05:01:44', '1');
INSERT INTO `agtrygame` VALUES ('kt_2051946649', 'kt_2051946649', null, '66.249.79.117', '2018-12-22 05:19:46', '1');
INSERT INTO `agtrygame` VALUES ('kt_2051946850', 'kt_2051946850', null, '66.249.79.115', '2018-12-22 05:19:46', '1');
INSERT INTO `agtrygame` VALUES ('kt_2132612985', 'kt_2132612985', null, '66.249.79.70', '2018-12-12 13:26:12', '1');
INSERT INTO `agtrygame` VALUES ('kt_2132613200', 'kt_2132613200', null, '66.249.79.72', '2018-12-12 13:26:13', '1');
INSERT INTO `agtrygame` VALUES ('kt_2141925748', 'kt_2141925748', null, '66.249.79.100', '2018-12-12 14:19:25', '1');
INSERT INTO `agtrygame` VALUES ('kt_2141925848', 'kt_2141925848', null, '66.249.79.97', '2018-12-12 14:19:25', '1');
INSERT INTO `agtrygame` VALUES ('kt_2170227149', 'kt_2170227149', null, '66.249.75.15', '2018-12-02 17:02:27', '1');
INSERT INTO `agtrygame` VALUES ('kt_2170227419', 'kt_2170227419', null, '66.249.75.15', '2018-12-02 17:02:27', '1');
INSERT INTO `agtrygame` VALUES ('kt_2170229210', 'kt_2170229210', null, '66.249.75.15', '2018-12-02 17:02:29', '1');
INSERT INTO `agtrygame` VALUES ('kt_2170229472', 'kt_2170229472', null, '66.249.75.15', '2018-12-02 17:02:29', '1');
INSERT INTO `agtrygame` VALUES ('kt_2170231248', 'kt_2170231248', null, '66.249.75.15', '2018-12-02 17:02:31', '1');
INSERT INTO `agtrygame` VALUES ('kt_2170231511', 'kt_2170231511', null, '66.249.75.15', '2018-12-02 17:02:31', '1');
INSERT INTO `agtrygame` VALUES ('kt_2170233304', 'kt_2170233304', null, '66.249.75.15', '2018-12-02 17:02:33', '1');
INSERT INTO `agtrygame` VALUES ('kt_2170233566', 'kt_2170233566', null, '66.249.75.15', '2018-12-02 17:02:33', '1');
INSERT INTO `agtrygame` VALUES ('kt_2170236300', 'kt_2170236300', null, '66.249.75.15', '2018-12-02 17:02:36', '1');
INSERT INTO `agtrygame` VALUES ('kt_2170236564', 'kt_2170236564', null, '66.249.75.15', '2018-12-02 17:02:36', '1');
INSERT INTO `agtrygame` VALUES ('kt_2170238995', 'kt_2170238995', null, '66.249.75.15', '2018-12-02 17:02:38', '1');
INSERT INTO `agtrygame` VALUES ('kt_2170239258', 'kt_2170239258', null, '66.249.75.15', '2018-12-02 17:02:39', '1');
INSERT INTO `agtrygame` VALUES ('kt_2170240079', 'kt_2170240079', null, '66.249.75.15', '2018-12-02 17:02:40', '1');
INSERT INTO `agtrygame` VALUES ('kt_2170240343', 'kt_2170240343', null, '66.249.75.15', '2018-12-02 17:02:40', '1');
INSERT INTO `agtrygame` VALUES ('kt_2170241179', 'kt_2170241179', null, '66.249.75.15', '2018-12-02 17:02:41', '1');
INSERT INTO `agtrygame` VALUES ('kt_2170241442', 'kt_2170241442', null, '66.249.75.15', '2018-12-02 17:02:41', '1');
INSERT INTO `agtrygame` VALUES ('kt_2170241735', 'kt_2170241735', null, '66.249.75.15', '2018-12-02 17:02:41', '1');
INSERT INTO `agtrygame` VALUES ('kt_2170241997', 'kt_2170241997', null, '66.249.75.15', '2018-12-02 17:02:41', '1');
INSERT INTO `agtrygame` VALUES ('kt_2170242860', 'kt_2170242860', null, '66.249.75.15', '2018-12-02 17:02:42', '1');
INSERT INTO `agtrygame` VALUES ('kt_2170243127', 'kt_2170243127', null, '66.249.75.15', '2018-12-02 17:02:43', '1');
INSERT INTO `agtrygame` VALUES ('kt_2170244004', 'kt_2170244004', null, '66.249.75.15', '2018-12-02 17:02:44', '1');
INSERT INTO `agtrygame` VALUES ('kt_2170244269', 'kt_2170244269', null, '66.249.75.15', '2018-12-02 17:02:44', '1');
INSERT INTO `agtrygame` VALUES ('kt_2170245134', 'kt_2170245134', null, '66.249.75.15', '2018-12-02 17:02:45', '1');
INSERT INTO `agtrygame` VALUES ('kt_2170245396', 'kt_2170245396', null, '66.249.75.15', '2018-12-02 17:02:45', '1');
INSERT INTO `agtrygame` VALUES ('kt_2170245705', 'kt_2170245705', null, '66.249.75.15', '2018-12-02 17:02:45', '1');
INSERT INTO `agtrygame` VALUES ('kt_2170245967', 'kt_2170245967', null, '66.249.75.15', '2018-12-02 17:02:45', '1');
INSERT INTO `agtrygame` VALUES ('kt_2170246284', 'kt_2170246284', null, '66.249.75.15', '2018-12-02 17:02:46', '1');
INSERT INTO `agtrygame` VALUES ('kt_2170246546', 'kt_2170246546', null, '66.249.75.15', '2018-12-02 17:02:46', '1');
INSERT INTO `agtrygame` VALUES ('kt_2170247466', 'kt_2170247466', null, '66.249.75.15', '2018-12-02 17:02:47', '1');
INSERT INTO `agtrygame` VALUES ('kt_2170247730', 'kt_2170247730', null, '66.249.75.15', '2018-12-02 17:02:47', '1');
INSERT INTO `agtrygame` VALUES ('kt_2170248670', 'kt_2170248670', null, '66.249.75.15', '2018-12-02 17:02:48', '1');
INSERT INTO `agtrygame` VALUES ('kt_2170248935', 'kt_2170248935', null, '66.249.75.15', '2018-12-02 17:02:48', '1');
INSERT INTO `agtrygame` VALUES ('kt_2170249278', 'kt_2170249278', null, '66.249.75.15', '2018-12-02 17:02:49', '1');
INSERT INTO `agtrygame` VALUES ('kt_2170249543', 'kt_2170249543', null, '66.249.75.15', '2018-12-02 17:02:49', '1');
INSERT INTO `agtrygame` VALUES ('kt_2170249883', 'kt_2170249883', null, '66.249.75.15', '2018-12-02 17:02:49', '1');
INSERT INTO `agtrygame` VALUES ('kt_2170250147', 'kt_2170250147', null, '66.249.75.15', '2018-12-02 17:02:50', '1');
INSERT INTO `agtrygame` VALUES ('kt_2170250496', 'kt_2170250496', null, '66.249.75.15', '2018-12-02 17:02:50', '1');
INSERT INTO `agtrygame` VALUES ('kt_2170250761', 'kt_2170250761', null, '66.249.75.15', '2018-12-02 17:02:50', '1');
INSERT INTO `agtrygame` VALUES ('kt_2170251115', 'kt_2170251115', null, '66.249.75.15', '2018-12-02 17:02:51', '1');
INSERT INTO `agtrygame` VALUES ('kt_2170251379', 'kt_2170251379', null, '66.249.75.15', '2018-12-02 17:02:51', '1');
INSERT INTO `agtrygame` VALUES ('kt_2170251731', 'kt_2170251731', null, '66.249.75.15', '2018-12-02 17:02:51', '1');
INSERT INTO `agtrygame` VALUES ('kt_2170251994', 'kt_2170251994', null, '66.249.75.15', '2018-12-02 17:02:51', '1');
INSERT INTO `agtrygame` VALUES ('kt_2170252352', 'kt_2170252352', null, '66.249.75.15', '2018-12-02 17:02:52', '1');
INSERT INTO `agtrygame` VALUES ('kt_2170252617', 'kt_2170252617', null, '66.249.75.15', '2018-12-02 17:02:52', '1');
INSERT INTO `agtrygame` VALUES ('kt_2170252978', 'kt_2170252978', null, '66.249.75.15', '2018-12-02 17:02:52', '1');
INSERT INTO `agtrygame` VALUES ('kt_2170253243', 'kt_2170253243', null, '66.249.75.15', '2018-12-02 17:02:53', '1');
INSERT INTO `agtrygame` VALUES ('kt_2170253605', 'kt_2170253605', null, '66.249.75.15', '2018-12-02 17:02:53', '1');
INSERT INTO `agtrygame` VALUES ('kt_2170253871', 'kt_2170253871', null, '66.249.75.15', '2018-12-02 17:02:53', '1');
INSERT INTO `agtrygame` VALUES ('kt_2170254236', 'kt_2170254236', null, '66.249.75.15', '2018-12-02 17:02:54', '1');
INSERT INTO `agtrygame` VALUES ('kt_2170254509', 'kt_2170254509', null, '66.249.75.15', '2018-12-02 17:02:54', '1');
INSERT INTO `agtrygame` VALUES ('kt_2170254868', 'kt_2170254868', null, '66.249.75.15', '2018-12-02 17:02:54', '1');
INSERT INTO `agtrygame` VALUES ('kt_2170255132', 'kt_2170255132', null, '66.249.75.15', '2018-12-02 17:02:55', '1');
INSERT INTO `agtrygame` VALUES ('kt_2170255504', 'kt_2170255504', null, '66.249.75.15', '2018-12-02 17:02:55', '1');
INSERT INTO `agtrygame` VALUES ('kt_2170255769', 'kt_2170255769', null, '66.249.75.15', '2018-12-02 17:02:55', '1');
INSERT INTO `agtrygame` VALUES ('kt_2170256142', 'kt_2170256142', null, '66.249.75.15', '2018-12-02 17:02:56', '1');
INSERT INTO `agtrygame` VALUES ('kt_2170256405', 'kt_2170256405', null, '66.249.75.15', '2018-12-02 17:02:56', '1');
INSERT INTO `agtrygame` VALUES ('kt_2170256782', 'kt_2170256782', null, '66.249.75.15', '2018-12-02 17:02:56', '1');
INSERT INTO `agtrygame` VALUES ('kt_2170257047', 'kt_2170257047', null, '66.249.75.15', '2018-12-02 17:02:57', '1');
INSERT INTO `agtrygame` VALUES ('kt_2170257425', 'kt_2170257425', null, '66.249.75.15', '2018-12-02 17:02:57', '1');
INSERT INTO `agtrygame` VALUES ('kt_2170257690', 'kt_2170257690', null, '66.249.75.15', '2018-12-02 17:02:57', '1');
INSERT INTO `agtrygame` VALUES ('kt_2170258069', 'kt_2170258069', null, '66.249.75.15', '2018-12-02 17:02:58', '1');
INSERT INTO `agtrygame` VALUES ('kt_2170258333', 'kt_2170258333', null, '66.249.75.15', '2018-12-02 17:02:58', '1');
INSERT INTO `agtrygame` VALUES ('kt_2170258715', 'kt_2170258715', null, '66.249.75.15', '2018-12-02 17:02:58', '1');
INSERT INTO `agtrygame` VALUES ('kt_2170258983', 'kt_2170258983', null, '66.249.75.15', '2018-12-02 17:02:58', '1');
INSERT INTO `agtrygame` VALUES ('kt_2170259365', 'kt_2170259365', null, '66.249.75.15', '2018-12-02 17:02:59', '1');
INSERT INTO `agtrygame` VALUES ('kt_2170259631', 'kt_2170259631', null, '66.249.75.15', '2018-12-02 17:02:59', '1');
INSERT INTO `agtrygame` VALUES ('kt_2170300011', 'kt_2170300011', null, '66.249.75.15', '2018-12-02 17:03:00', '1');
INSERT INTO `agtrygame` VALUES ('kt_2170300292', 'kt_2170300292', null, '66.249.75.15', '2018-12-02 17:03:00', '1');
INSERT INTO `agtrygame` VALUES ('kt_2170300663', 'kt_2170300663', null, '66.249.75.15', '2018-12-02 17:03:00', '1');
INSERT INTO `agtrygame` VALUES ('kt_2170300931', 'kt_2170300931', null, '66.249.75.15', '2018-12-02 17:03:00', '1');
INSERT INTO `agtrygame` VALUES ('kt_2170301319', 'kt_2170301319', null, '66.249.75.15', '2018-12-02 17:03:01', '1');
INSERT INTO `agtrygame` VALUES ('kt_2170301586', 'kt_2170301586', null, '66.249.75.15', '2018-12-02 17:03:01', '1');
INSERT INTO `agtrygame` VALUES ('kt_2170301973', 'kt_2170301973', null, '66.249.75.15', '2018-12-02 17:03:01', '1');
INSERT INTO `agtrygame` VALUES ('kt_2170302236', 'kt_2170302236', null, '66.249.75.15', '2018-12-02 17:03:02', '1');
INSERT INTO `agtrygame` VALUES ('kt_2170302625', 'kt_2170302625', null, '66.249.75.15', '2018-12-02 17:03:02', '1');
INSERT INTO `agtrygame` VALUES ('kt_2170302888', 'kt_2170302888', null, '66.249.75.15', '2018-12-02 17:03:02', '1');
INSERT INTO `agtrygame` VALUES ('kt_2170303284', 'kt_2170303284', null, '66.249.75.15', '2018-12-02 17:03:03', '1');
INSERT INTO `agtrygame` VALUES ('kt_2170303548', 'kt_2170303548', null, '66.249.75.15', '2018-12-02 17:03:03', '1');
INSERT INTO `agtrygame` VALUES ('kt_2170303941', 'kt_2170303941', null, '66.249.75.15', '2018-12-02 17:03:03', '1');
INSERT INTO `agtrygame` VALUES ('kt_2170304205', 'kt_2170304205', null, '66.249.75.15', '2018-12-02 17:03:04', '1');
INSERT INTO `agtrygame` VALUES ('kt_2170304600', 'kt_2170304600', null, '66.249.75.15', '2018-12-02 17:03:04', '1');
INSERT INTO `agtrygame` VALUES ('kt_2170304865', 'kt_2170304865', null, '66.249.75.15', '2018-12-02 17:03:04', '1');
INSERT INTO `agtrygame` VALUES ('kt_2170305259', 'kt_2170305259', null, '66.249.75.15', '2018-12-02 17:03:05', '1');
INSERT INTO `agtrygame` VALUES ('kt_2170305526', 'kt_2170305526', null, '66.249.75.15', '2018-12-02 17:03:05', '1');
INSERT INTO `agtrygame` VALUES ('kt_2170305921', 'kt_2170305921', null, '66.249.75.15', '2018-12-02 17:03:05', '1');
INSERT INTO `agtrygame` VALUES ('kt_2170306186', 'kt_2170306186', null, '66.249.75.15', '2018-12-02 17:03:06', '1');
INSERT INTO `agtrygame` VALUES ('kt_2170306583', 'kt_2170306583', null, '66.249.75.15', '2018-12-02 17:03:06', '1');
INSERT INTO `agtrygame` VALUES ('kt_2170306846', 'kt_2170306846', null, '66.249.75.15', '2018-12-02 17:03:06', '1');
INSERT INTO `agtrygame` VALUES ('kt_2170307248', 'kt_2170307248', null, '66.249.75.15', '2018-12-02 17:03:07', '1');
INSERT INTO `agtrygame` VALUES ('kt_2170307512', 'kt_2170307512', null, '66.249.75.15', '2018-12-02 17:03:07', '1');
INSERT INTO `agtrygame` VALUES ('kt_2170307912', 'kt_2170307912', null, '66.249.75.15', '2018-12-02 17:03:07', '1');
INSERT INTO `agtrygame` VALUES ('kt_2170308177', 'kt_2170308177', null, '66.249.75.15', '2018-12-02 17:03:08', '1');
INSERT INTO `agtrygame` VALUES ('kt_2170308578', 'kt_2170308578', null, '66.249.75.15', '2018-12-02 17:03:08', '1');
INSERT INTO `agtrygame` VALUES ('kt_2170308841', 'kt_2170308841', null, '66.249.75.15', '2018-12-02 17:03:08', '1');
INSERT INTO `agtrygame` VALUES ('kt_2223643634', 'kt_2223643634', null, '66.249.79.119', '2018-12-22 22:36:43', '1');
INSERT INTO `agtrygame` VALUES ('kt_2223643832', 'kt_2223643832', null, '66.249.79.117', '2018-12-22 22:36:43', '1');
INSERT INTO `agtrygame` VALUES ('kt_2224544552', 'kt_2224544552', null, '66.249.79.119', '2018-12-22 22:45:44', '1');
INSERT INTO `agtrygame` VALUES ('kt_2224544743', 'kt_2224544743', null, '66.249.79.115', '2018-12-22 22:45:44', '1');
INSERT INTO `agtrygame` VALUES ('kt_2230346545', 'kt_2230346545', null, '66.249.79.115', '2018-12-22 23:03:46', '1');
INSERT INTO `agtrygame` VALUES ('kt_2230346740', 'kt_2230346740', null, '66.249.79.117', '2018-12-22 23:03:46', '1');
INSERT INTO `agtrygame` VALUES ('kt_3022923679', 'kt_3022923679', null, '66.249.79.117', '2018-12-13 02:29:23', '1');
INSERT INTO `agtrygame` VALUES ('kt_3022923868', 'kt_3022923868', null, '66.249.79.119', '2018-12-13 02:29:23', '1');
INSERT INTO `agtrygame` VALUES ('kt_3030527792', 'kt_3030527792', null, '66.249.79.119', '2018-12-13 03:05:27', '1');
INSERT INTO `agtrygame` VALUES ('kt_3030527979', 'kt_3030527979', null, '66.249.79.119', '2018-12-13 03:05:27', '1');
INSERT INTO `agtrygame` VALUES ('kt_3031428557', 'kt_3031428557', null, '66.249.79.117', '2018-12-13 03:14:28', '1');
INSERT INTO `agtrygame` VALUES ('kt_3031428743', 'kt_3031428743', null, '66.249.79.119', '2018-12-13 03:14:28', '1');
INSERT INTO `agtrygame` VALUES ('kt_3035032668', 'kt_3035032668', null, '66.249.79.119', '2018-12-13 03:50:32', '1');
INSERT INTO `agtrygame` VALUES ('kt_3035032855', 'kt_3035032855', null, '66.249.79.117', '2018-12-13 03:50:32', '1');
INSERT INTO `agtrygame` VALUES ('kt_3044442686', 'kt_3044442686', null, '66.249.79.115', '2018-12-13 04:44:42', '1');
INSERT INTO `agtrygame` VALUES ('kt_3044442873', 'kt_3044442873', null, '66.249.79.119', '2018-12-13 04:44:42', '1');
INSERT INTO `agtrygame` VALUES ('kt_3045339564', 'kt_3045339564', null, '66.249.79.115', '2018-12-13 04:53:39', '1');
INSERT INTO `agtrygame` VALUES ('kt_3045339751', 'kt_3045339751', null, '66.249.79.115', '2018-12-13 04:53:39', '1');
INSERT INTO `agtrygame` VALUES ('kt_3115727645', 'kt_3115727645', null, '66.249.79.115', '2018-12-13 11:57:27', '1');
INSERT INTO `agtrygame` VALUES ('kt_3115727838', 'kt_3115727838', null, '66.249.79.119', '2018-12-13 11:57:27', '1');
INSERT INTO `agtrygame` VALUES ('kt_3122430534', 'kt_3122430534', null, '66.249.79.119', '2018-12-13 12:24:30', '1');
INSERT INTO `agtrygame` VALUES ('kt_3122430725', 'kt_3122430725', null, '66.249.79.115', '2018-12-13 12:24:30', '1');
INSERT INTO `agtrygame` VALUES ('kt_3150737273', 'kt_3150737273', null, '66.249.79.208', '2018-12-13 15:07:37', '1');
INSERT INTO `agtrygame` VALUES ('kt_3150737463', 'kt_3150737463', null, '66.249.79.208', '2018-12-13 15:07:37', '1');
INSERT INTO `agtrygame` VALUES ('kt_3223846068', 'kt_3223846068', null, '66.249.75.196', '2018-12-13 22:38:46', '1');
INSERT INTO `agtrygame` VALUES ('kt_3223846210', 'kt_3223846210', null, '66.249.75.193', '2018-12-13 22:38:46', '1');
INSERT INTO `agtrygame` VALUES ('kt_4120959356', 'kt_4120959356', null, '66.249.75.1', '2018-12-14 12:09:59', '1');
INSERT INTO `agtrygame` VALUES ('kt_4120959641', 'kt_4120959641', null, '66.249.75.1', '2018-12-14 12:09:59', '1');
INSERT INTO `agtrygame` VALUES ('kt_4183120394', 'kt_4183120394', null, '66.249.73.65', '2018-12-04 18:31:20', '1');
INSERT INTO `agtrygame` VALUES ('kt_4183120675', 'kt_4183120675', null, '66.249.73.71', '2018-12-04 18:31:20', '1');
INSERT INTO `agtrygame` VALUES ('kt_4214126595', 'kt_4214126595', null, '38.100.21.68', '2018-12-04 21:41:26', '1');
INSERT INTO `agtrygame` VALUES ('kt_5160128300', 'kt_5160128300', null, '66.249.79.103', '2018-12-25 16:01:28', '1');
INSERT INTO `agtrygame` VALUES ('kt_5160128499', 'kt_5160128499', null, '66.249.79.97', '2018-12-25 16:01:28', '1');
INSERT INTO `agtrygame` VALUES ('kt_5171454222', 'kt_5171454222', null, '66.249.64.114', '2018-12-15 17:14:54', '1');
INSERT INTO `agtrygame` VALUES ('kt_5171454595', 'kt_5171454595', null, '66.249.64.114', '2018-12-15 17:14:54', '1');
INSERT INTO `agtrygame` VALUES ('kt_5171714802', 'kt_5171714802', null, '66.249.79.103', '2018-12-25 17:17:14', '1');
INSERT INTO `agtrygame` VALUES ('kt_5171714993', 'kt_5171714993', null, '66.249.79.103', '2018-12-25 17:17:14', '1');
INSERT INTO `agtrygame` VALUES ('kt_5183303984', 'kt_5183303984', null, '66.249.79.97', '2018-12-25 18:33:03', '1');
INSERT INTO `agtrygame` VALUES ('kt_5183304174', 'kt_5183304174', null, '66.249.79.103', '2018-12-25 18:33:04', '1');
INSERT INTO `agtrygame` VALUES ('kt_5194849414', 'kt_5194849414', null, '66.249.79.97', '2018-12-25 19:48:49', '1');
INSERT INTO `agtrygame` VALUES ('kt_5194849605', 'kt_5194849605', null, '66.249.79.100', '2018-12-25 19:48:49', '1');
INSERT INTO `agtrygame` VALUES ('kt_5210438609', 'kt_5210438609', null, '66.249.79.97', '2018-12-25 21:04:38', '1');
INSERT INTO `agtrygame` VALUES ('kt_5210438801', 'kt_5210438801', null, '66.249.79.100', '2018-12-25 21:04:38', '1');
INSERT INTO `agtrygame` VALUES ('kt_6010408991', 'kt_6010408991', null, '66.249.66.197', '2018-12-06 01:04:08', '1');
INSERT INTO `agtrygame` VALUES ('kt_6010409307', 'kt_6010409307', null, '66.249.66.197', '2018-12-06 01:04:09', '1');
INSERT INTO `agtrygame` VALUES ('kt_8003300494', 'kt_8003300494', null, '66.249.79.74', '2018-12-18 00:33:00', '1');
INSERT INTO `agtrygame` VALUES ('kt_8003300613', 'kt_8003300613', null, '66.249.79.70', '2018-12-18 00:33:00', '1');
INSERT INTO `agtrygame` VALUES ('kt_8064058262', 'kt_8064058262', null, '66.249.79.72', '2018-12-08 06:40:58', '1');
INSERT INTO `agtrygame` VALUES ('kt_8064058474', 'kt_8064058474', null, '66.249.79.70', '2018-12-08 06:40:58', '1');
INSERT INTO `agtrygame` VALUES ('kt_8071846149', 'kt_8071846149', null, '66.249.79.70', '2018-12-08 07:18:46', '1');
INSERT INTO `agtrygame` VALUES ('kt_8071846390', 'kt_8071846390', null, '66.249.79.74', '2018-12-08 07:18:46', '1');
INSERT INTO `agtrygame` VALUES ('kt_8073740240', 'kt_8073740240', null, '66.249.79.115', '2018-12-08 07:37:40', '1');
INSERT INTO `agtrygame` VALUES ('kt_8073740428', 'kt_8073740428', null, '66.249.79.115', '2018-12-08 07:37:40', '1');
INSERT INTO `agtrygame` VALUES ('kt_8081528303', 'kt_8081528303', null, '66.249.79.74', '2018-12-08 08:15:28', '1');
INSERT INTO `agtrygame` VALUES ('kt_8081528666', 'kt_8081528666', null, '66.249.79.74', '2018-12-08 08:15:28', '1');
INSERT INTO `agtrygame` VALUES ('kt_8093107885', 'kt_8093107885', null, '66.249.79.115', '2018-12-08 09:31:07', '1');
INSERT INTO `agtrygame` VALUES ('kt_8093108075', 'kt_8093108075', null, '66.249.79.115', '2018-12-08 09:31:08', '1');
INSERT INTO `agtrygame` VALUES ('kt_8093723099', 'kt_8093723099', null, '66.249.79.115', '2018-12-08 09:37:23', '1');
INSERT INTO `agtrygame` VALUES ('kt_8093723288', 'kt_8093723288', null, '66.249.79.115', '2018-12-08 09:37:23', '1');
INSERT INTO `agtrygame` VALUES ('kt_8095000092', 'kt_8095000092', null, '66.249.79.115', '2018-12-08 09:50:00', '1');
INSERT INTO `agtrygame` VALUES ('kt_8095000210', 'kt_8095000210', null, '66.249.79.117', '2018-12-08 09:50:00', '1');
INSERT INTO `agtrygame` VALUES ('kt_8095618148', 'kt_8095618148', null, '66.249.79.74', '2018-12-08 09:56:18', '1');
INSERT INTO `agtrygame` VALUES ('kt_8095618343', 'kt_8095618343', null, '66.249.79.72', '2018-12-08 09:56:18', '1');
INSERT INTO `agtrygame` VALUES ('kt_8100236421', 'kt_8100236421', null, '66.249.79.70', '2018-12-08 10:02:36', '1');
INSERT INTO `agtrygame` VALUES ('kt_8100236609', 'kt_8100236609', null, '66.249.79.74', '2018-12-08 10:02:36', '1');
INSERT INTO `agtrygame` VALUES ('kt_8100854143', 'kt_8100854143', null, '66.249.79.119', '2018-12-08 10:08:54', '1');
INSERT INTO `agtrygame` VALUES ('kt_8100854332', 'kt_8100854332', null, '66.249.79.117', '2018-12-08 10:08:54', '1');
INSERT INTO `agtrygame` VALUES ('kt_8101512150', 'kt_8101512150', null, '66.249.79.115', '2018-12-08 10:15:12', '1');
INSERT INTO `agtrygame` VALUES ('kt_8101512250', 'kt_8101512250', null, '66.249.79.119', '2018-12-08 10:15:12', '1');
INSERT INTO `agtrygame` VALUES ('kt_8102130143', 'kt_8102130143', null, '66.249.79.119', '2018-12-08 10:21:30', '1');
INSERT INTO `agtrygame` VALUES ('kt_8102130244', 'kt_8102130244', null, '66.249.79.117', '2018-12-08 10:21:30', '1');
INSERT INTO `agtrygame` VALUES ('kt_8102749684', 'kt_8102749684', null, '66.249.79.115', '2018-12-08 10:27:49', '1');
INSERT INTO `agtrygame` VALUES ('kt_8102749872', 'kt_8102749872', null, '66.249.79.115', '2018-12-08 10:27:49', '1');
INSERT INTO `agtrygame` VALUES ('kt_8104025235', 'kt_8104025235', null, '66.249.79.119', '2018-12-08 10:40:25', '1');
INSERT INTO `agtrygame` VALUES ('kt_8104025422', 'kt_8104025422', null, '66.249.79.117', '2018-12-08 10:40:25', '1');
INSERT INTO `agtrygame` VALUES ('kt_8104646207', 'kt_8104646207', null, '66.249.79.117', '2018-12-08 10:46:46', '1');
INSERT INTO `agtrygame` VALUES ('kt_8104646396', 'kt_8104646396', null, '66.249.79.117', '2018-12-08 10:46:46', '1');
INSERT INTO `agtrygame` VALUES ('kt_8105920150', 'kt_8105920150', null, '66.249.79.72', '2018-12-08 10:59:20', '1');
INSERT INTO `agtrygame` VALUES ('kt_8105920336', 'kt_8105920336', null, '66.249.79.72', '2018-12-08 10:59:20', '1');
INSERT INTO `agtrygame` VALUES ('kt_8115603264', 'kt_8115603264', null, '66.249.79.208', '2018-12-08 11:56:03', '1');
INSERT INTO `agtrygame` VALUES ('kt_8115603461', 'kt_8115603461', null, '66.249.79.210', '2018-12-08 11:56:03', '1');
INSERT INTO `agtrygame` VALUES ('kt_8124009056', 'kt_8124009056', null, '66.249.79.115', '2018-12-08 12:40:09', '1');
INSERT INTO `agtrygame` VALUES ('kt_8124009256', 'kt_8124009256', null, '66.249.79.119', '2018-12-08 12:40:09', '1');
INSERT INTO `agtrygame` VALUES ('kt_8142058147', 'kt_8142058147', null, '66.249.79.115', '2018-12-08 14:20:58', '1');
INSERT INTO `agtrygame` VALUES ('kt_8142058337', 'kt_8142058337', null, '66.249.79.115', '2018-12-08 14:20:58', '1');
INSERT INTO `agtrygame` VALUES ('kt_8163934150', 'kt_8163934150', null, '66.249.79.72', '2018-12-08 16:39:34', '1');
INSERT INTO `agtrygame` VALUES ('kt_8163934355', 'kt_8163934355', null, '66.249.79.70', '2018-12-08 16:39:34', '1');
INSERT INTO `agtrygame` VALUES ('kt_8165210281', 'kt_8165210281', null, '66.249.79.70', '2018-12-08 16:52:10', '1');
INSERT INTO `agtrygame` VALUES ('kt_8165210471', 'kt_8165210471', null, '66.249.79.74', '2018-12-08 16:52:10', '1');
INSERT INTO `agtrygame` VALUES ('kt_8165828145', 'kt_8165828145', null, '66.249.79.72', '2018-12-08 16:58:28', '1');
INSERT INTO `agtrygame` VALUES ('kt_8165828344', 'kt_8165828344', null, '66.249.79.72', '2018-12-08 16:58:28', '1');
INSERT INTO `agtrygame` VALUES ('kt_8170446376', 'kt_8170446376', null, '66.249.79.70', '2018-12-08 17:04:46', '1');
INSERT INTO `agtrygame` VALUES ('kt_8170446563', 'kt_8170446563', null, '66.249.79.72', '2018-12-08 17:04:46', '1');
INSERT INTO `agtrygame` VALUES ('kt_8173618206', 'kt_8173618206', null, '66.249.79.70', '2018-12-08 17:36:18', '1');
INSERT INTO `agtrygame` VALUES ('kt_8173618422', 'kt_8173618422', null, '66.249.79.70', '2018-12-08 17:36:18', '1');
INSERT INTO `agtrygame` VALUES ('kt_8175512157', 'kt_8175512157', null, '66.249.79.72', '2018-12-08 17:55:12', '1');
INSERT INTO `agtrygame` VALUES ('kt_8175512432', 'kt_8175512432', null, '66.249.79.72', '2018-12-08 17:55:12', '1');
INSERT INTO `agtrygame` VALUES ('kt_8181406271', 'kt_8181406271', null, '66.249.79.70', '2018-12-08 18:14:06', '1');
INSERT INTO `agtrygame` VALUES ('kt_8181406459', 'kt_8181406459', null, '66.249.79.74', '2018-12-08 18:14:06', '1');
INSERT INTO `agtrygame` VALUES ('kt_8182024044', 'kt_8182024044', null, '66.249.79.72', '2018-12-08 18:20:24', '1');
INSERT INTO `agtrygame` VALUES ('kt_8182024232', 'kt_8182024232', null, '66.249.79.74', '2018-12-08 18:20:24', '1');
INSERT INTO `agtrygame` VALUES ('kt_8185813051', 'kt_8185813051', null, '66.249.79.70', '2018-12-08 18:58:13', '1');
INSERT INTO `agtrygame` VALUES ('kt_8185813243', 'kt_8185813243', null, '66.249.79.72', '2018-12-08 18:58:13', '1');
INSERT INTO `agtrygame` VALUES ('kt_8195456148', 'kt_8195456148', null, '66.249.79.115', '2018-12-08 19:54:56', '1');
INSERT INTO `agtrygame` VALUES ('kt_8195456337', 'kt_8195456337', null, '66.249.79.115', '2018-12-08 19:54:56', '1');
INSERT INTO `agtrygame` VALUES ('kt_8200114141', 'kt_8200114141', null, '66.249.79.115', '2018-12-08 20:01:14', '1');
INSERT INTO `agtrygame` VALUES ('kt_8200114333', 'kt_8200114333', null, '66.249.79.117', '2018-12-08 20:01:14', '1');
INSERT INTO `agtrygame` VALUES ('kt_8200732149', 'kt_8200732149', null, '66.249.79.115', '2018-12-08 20:07:32', '1');
INSERT INTO `agtrygame` VALUES ('kt_8200732377', 'kt_8200732377', null, '66.249.79.115', '2018-12-08 20:07:32', '1');
INSERT INTO `agtrygame` VALUES ('kt_8203245992', 'kt_8203245992', null, '66.249.79.117', '2018-12-08 20:32:45', '1');
INSERT INTO `agtrygame` VALUES ('kt_8203246180', 'kt_8203246180', null, '66.249.79.117', '2018-12-08 20:32:46', '1');
INSERT INTO `agtrygame` VALUES ('kt_8213546102', 'kt_8213546102', null, '66.249.79.115', '2018-12-08 21:35:46', '1');
INSERT INTO `agtrygame` VALUES ('kt_8213546322', 'kt_8213546322', null, '66.249.79.115', '2018-12-08 21:35:46', '1');
INSERT INTO `agtrygame` VALUES ('kt_8220716151', 'kt_8220716151', null, '66.249.79.117', '2018-12-08 22:07:16', '1');
INSERT INTO `agtrygame` VALUES ('kt_8220716340', 'kt_8220716340', null, '66.249.79.115', '2018-12-08 22:07:16', '1');
INSERT INTO `agtrygame` VALUES ('kt_8223846242', 'kt_8223846242', null, '66.249.79.115', '2018-12-08 22:38:46', '1');
INSERT INTO `agtrygame` VALUES ('kt_8223846440', 'kt_8223846440', null, '66.249.79.119', '2018-12-08 22:38:46', '1');
INSERT INTO `agtrygame` VALUES ('kt_8231016398', 'kt_8231016398', null, '66.249.79.115', '2018-12-08 23:10:16', '1');
INSERT INTO `agtrygame` VALUES ('kt_8231016585', 'kt_8231016585', null, '66.249.79.117', '2018-12-08 23:10:16', '1');
INSERT INTO `agtrygame` VALUES ('kt_8234146061', 'kt_8234146061', null, '66.249.79.115', '2018-12-08 23:41:46', '1');
INSERT INTO `agtrygame` VALUES ('kt_8234146250', 'kt_8234146250', null, '66.249.79.117', '2018-12-08 23:41:46', '1');
INSERT INTO `agtrygame` VALUES ('kt_9000040227', 'kt_9000040227', null, '66.249.79.115', '2018-12-09 00:00:40', '1');
INSERT INTO `agtrygame` VALUES ('kt_9000040417', 'kt_9000040417', null, '66.249.79.119', '2018-12-09 00:00:40', '1');
INSERT INTO `agtrygame` VALUES ('kt_9185646227', 'kt_9185646227', null, '66.249.79.70', '2018-12-09 18:56:46', '1');
INSERT INTO `agtrygame` VALUES ('kt_9185646419', 'kt_9185646419', null, '66.249.79.70', '2018-12-09 18:56:46', '1');
INSERT INTO `agtrygame` VALUES ('kt_9202948162', 'kt_9202948162', null, '66.249.79.72', '2018-12-09 20:29:48', '1');
INSERT INTO `agtrygame` VALUES ('kt_9202948353', 'kt_9202948353', null, '66.249.79.72', '2018-12-09 20:29:48', '1');
INSERT INTO `agtrygame` VALUES ('kt_9203050427', 'kt_9203050427', null, '66.249.79.70', '2018-12-09 20:30:50', '1');
INSERT INTO `agtrygame` VALUES ('kt_9203050529', 'kt_9203050529', null, '66.249.79.72', '2018-12-09 20:30:50', '1');
INSERT INTO `agtrygame` VALUES ('kt_9222224592', 'kt_9222224592', null, '89.234.68.78', '2018-11-29 22:22:24', '1');

-- ----------------------------
-- Table structure for `agtryprofit`
-- ----------------------------
DROP TABLE IF EXISTS `agtryprofit`;
CREATE TABLE `agtryprofit` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `loginname` varchar(20) NOT NULL,
  `createTime` datetime DEFAULT NULL,
  `amount` double(24,2) DEFAULT NULL,
  `remark` varchar(500) DEFAULT NULL,
  `platform` varchar(8) DEFAULT NULL,
  `bettotal` double(24,2) DEFAULT '0.00',
  `betnum` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `loginname` (`loginname`) USING BTREE,
  KEY `createTime` (`createTime`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of agtryprofit
-- ----------------------------

-- ----------------------------
-- Table structure for `ag_config`
-- ----------------------------
DROP TABLE IF EXISTS `ag_config`;
CREATE TABLE `ag_config` (
  `platform` varchar(255) NOT NULL DEFAULT '' COMMENT '平台',
  `flag` int(11) NOT NULL COMMENT '状态',
  `createTime` datetime DEFAULT NULL COMMENT '创建时间',
  `lastupdateTime` datetime DEFAULT NULL COMMENT '最新时间',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`platform`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ag_config
-- ----------------------------

-- ----------------------------
-- Table structure for `alipay_transfers`
-- ----------------------------
DROP TABLE IF EXISTS `alipay_transfers`;
CREATE TABLE `alipay_transfers` (
  `transfer_id` varchar(255) NOT NULL,
  `pay_date` datetime NOT NULL COMMENT '付款时间',
  `trade_no` varchar(255) NOT NULL COMMENT '支付宝交易单号(流水号)',
  `order_no` varchar(255) DEFAULT NULL COMMENT '商户订单号',
  `trade_type` varchar(255) DEFAULT NULL COMMENT '交易类型',
  `amount` decimal(10,2) NOT NULL COMMENT '交易金额',
  `fee` decimal(10,2) DEFAULT '0.00' COMMENT '手续费',
  `balance` decimal(10,2) NOT NULL COMMENT '余额',
  `notes` varchar(255) DEFAULT NULL COMMENT '附言',
  `accept_name` varchar(255) NOT NULL COMMENT '收款人',
  `accept_no` varchar(255) NOT NULL COMMENT '收款账号',
  `admin_id` smallint(6) DEFAULT NULL COMMENT '管理员ID',
  `status` tinyint(4) DEFAULT NULL COMMENT '0未处理 1已充值 2充值超时处理',
  `date` datetime DEFAULT NULL COMMENT '记录添加时间',
  `timecha` varchar(255) DEFAULT NULL COMMENT '时间差',
  `overtime` int(11) DEFAULT '0' COMMENT '是否超时',
  `paytype` int(10) NOT NULL DEFAULT '1' COMMENT '1.附言方式2。二维码方式',
  PRIMARY KEY (`transfer_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of alipay_transfers
-- ----------------------------

-- ----------------------------
-- Table structure for `announcement`
-- ----------------------------
DROP TABLE IF EXISTS `announcement`;
CREATE TABLE `announcement` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` varchar(20) NOT NULL DEFAULT '',
  `title` varchar(100) NOT NULL,
  `content` varchar(20000) NOT NULL,
  `createtime` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of announcement
-- ----------------------------

-- ----------------------------
-- Table structure for `api_call_record`
-- ----------------------------
DROP TABLE IF EXISTS `api_call_record`;
CREATE TABLE `api_call_record` (
  `version_key` varchar(255) NOT NULL,
  `frequency` int(11) NOT NULL DEFAULT '0',
  `createtime` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `updatetime` timestamp NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`version_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of api_call_record
-- ----------------------------

-- ----------------------------
-- Table structure for `app_package_version`
-- ----------------------------
DROP TABLE IF EXISTS `app_package_version`;
CREATE TABLE `app_package_version` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `createTime` datetime NOT NULL COMMENT '创建时间',
  `modifyTime` datetime NOT NULL COMMENT '修改时间',
  `versionCode` varchar(20) NOT NULL COMMENT '版本号',
  `versionTitle` varchar(150) NOT NULL COMMENT '版本标题',
  `upgradeLog` varchar(1000) NOT NULL DEFAULT '' COMMENT '升级日志',
  `plat` varchar(20) NOT NULL COMMENT '平台(ios, android)',
  `operator` varchar(20) NOT NULL DEFAULT '' COMMENT '操作员',
  `status` int(2) NOT NULL COMMENT '状态 (0： 停用或未发行-默认 1：启用或发行 )',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`) USING BTREE,
  UNIQUE KEY `versionCode_plat` (`versionCode`,`plat`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of app_package_version
-- ----------------------------

-- ----------------------------
-- Table structure for `app_package_version_custom`
-- ----------------------------
DROP TABLE IF EXISTS `app_package_version_custom`;
CREATE TABLE `app_package_version_custom` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `versionId` int(11) NOT NULL COMMENT '版本ID，对应主表app_package_version.id',
  `createTime` datetime NOT NULL COMMENT '创建时间',
  `modifyTime` datetime NOT NULL COMMENT '修改时间',
  `versionCode` varchar(20) NOT NULL COMMENT '版本号',
  `agentAccount` varchar(50) NOT NULL COMMENT '代码账号',
  `agentCode` varchar(50) NOT NULL COMMENT '代理码',
  `plat` varchar(20) NOT NULL COMMENT '平台(ios, android)',
  `packageName` varchar(150) NOT NULL DEFAULT '' COMMENT '包名',
  `isUpgrade` bit(1) NOT NULL COMMENT '是否升级，代理第一次使用定制版时仅可安装',
  `isForceUpgrade` bit(1) NOT NULL COMMENT '是否强制升级',
  `status` int(2) NOT NULL COMMENT '状态 (0： 停用或未发行-默认 1：启用或发行 )',
  `pakStatus` int(2) NOT NULL COMMENT '打包状态(0-未打包， 1-打包中 ， 2-打包完成， 11-打包错误)',
  `packageUrl` varchar(255) NOT NULL DEFAULT '' COMMENT '包链接',
  `publishUrl` varchar(255) NOT NULL DEFAULT '' COMMENT '发行链接',
  `qrCodeUrl` varchar(255) NOT NULL DEFAULT '' COMMENT '二维码链接',
  `operator` varchar(20) NOT NULL DEFAULT '' COMMENT '操作员',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`) USING BTREE,
  UNIQUE KEY `versionCode_agentCode_plat` (`versionCode`,`agentCode`,`plat`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of app_package_version_custom
-- ----------------------------

-- ----------------------------
-- Table structure for `app_preferential`
-- ----------------------------
DROP TABLE IF EXISTS `app_preferential`;
CREATE TABLE `app_preferential` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `loginname` varchar(50) NOT NULL,
  `transferId` bigint(20) NOT NULL,
  `version` int(11) NOT NULL,
  `createtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of app_preferential
-- ----------------------------

-- ----------------------------
-- Table structure for `app_specialtopic`
-- ----------------------------
DROP TABLE IF EXISTS `app_specialtopic`;
CREATE TABLE `app_specialtopic` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `picture_url` varchar(500) NOT NULL COMMENT '专题图片地址（存放在ftp服务器上的相对地址）',
  `topic_url` varchar(500) NOT NULL COMMENT '官网专题地址（官网专题的完整地址）',
  `status` int(2) NOT NULL DEFAULT '0' COMMENT '状态    0： 停用  1：启用    默认0',
  `title` varchar(50) DEFAULT NULL COMMENT '专题标题',
  `createTime` datetime DEFAULT NULL COMMENT '创建时间',
  `action_url` varchar(500) NOT NULL COMMENT '首页-滚动 Banner跳转的APP应用内链接',
  PRIMARY KEY (`id`) USING HASH,
  KEY `key` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='品牌app专题图片与专题跳转对应表';

-- ----------------------------
-- Records of app_specialtopic
-- ----------------------------

-- ----------------------------
-- Table structure for `app_version`
-- ----------------------------
DROP TABLE IF EXISTS `app_version`;
CREATE TABLE `app_version` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `title` varchar(200) NOT NULL COMMENT '标题',
  `version` varchar(10) NOT NULL COMMENT '版本',
  `upgrade_context` varchar(1000) NOT NULL COMMENT '升级内容',
  `plat` varchar(300) NOT NULL COMMENT '平台  android  还是  ios',
  `force` varchar(10) NOT NULL DEFAULT 'N' COMMENT '是否强制升级  Y:是   N：不是',
  `download_url` varchar(300) NOT NULL COMMENT 'app软件包更新地址',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `id` (`id`),
  KEY `version` (`version`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='app版本信息表';

-- ----------------------------
-- Records of app_version
-- ----------------------------

-- ----------------------------
-- Table structure for `autotask`
-- ----------------------------
DROP TABLE IF EXISTS `autotask`;
CREATE TABLE `autotask` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `taskType` varchar(20) NOT NULL,
  `totalRecords` int(11) NOT NULL,
  `failRecords` int(11) NOT NULL DEFAULT '0',
  `finishRecords` int(11) NOT NULL DEFAULT '0',
  `refreshTime` datetime DEFAULT NULL,
  `startTime` datetime NOT NULL,
  `endTime` datetime DEFAULT NULL,
  `flag` int(11) NOT NULL DEFAULT '0',
  `remark` varchar(500) DEFAULT NULL,
  `operator` varchar(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of autotask
-- ----------------------------

-- ----------------------------
-- Table structure for `bankcardinfo`
-- ----------------------------
DROP TABLE IF EXISTS `bankcardinfo`;
CREATE TABLE `bankcardinfo` (
  `id` int(10) NOT NULL,
  `issuebankname` varchar(300) DEFAULT NULL,
  `cardname` varchar(300) DEFAULT NULL,
  `cardlength` int(5) DEFAULT NULL,
  `masteraccount` varchar(255) DEFAULT NULL,
  `identifylegth` int(5) DEFAULT NULL,
  `identifycode` varchar(20) DEFAULT NULL,
  `cardtype` varchar(300) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `issuebankname` (`issuebankname`(255)) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of bankcardinfo
-- ----------------------------
INSERT INTO `bankcardinfo` VALUES ('53', '邮政银行', '绿卡银联标准卡', '19', '622150xxxxxxxxxxxxx', '6', '622150', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('54', '邮政银行', '绿卡银联标准卡', '19', '622151xxxxxxxxxxxxx', '6', '622151', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('55', '邮政银行', '绿卡专用卡', '19', '622181xxxxxxxxxxxxx', '6', '622181', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('56', '邮政银行', '绿卡银联标准卡', '19', '622188xxxxxxxxxxxxx', '6', '622188', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('57', '邮政银行', '绿卡(银联卡)', '19', '955100xxxxxxxxxxxxx', '6', '955100', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('58', '邮政银行', '绿卡VIP卡', '19', '621095xxxxxxxxxxxxx', '6', '621095', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('59', '邮政银行', '银联标准卡', '19', '620062xxxxxxxxxxxxx', '6', '620062', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('60', '邮政银行', '中职学生资助卡', '19', '621285xxxxxxxxxxxxx', '6', '621285', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('61', '邮政银行', 'IC绿卡通VIP卡', '19', '621798xxxxxxxxxxxxx', '6', '621798', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('62', '邮政银行', 'IC绿卡通', '19', '621799xxxxxxxxxxxxx', '6', '621799', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('63', '邮政银行', 'IC联名卡', '19', '621797xxxxxxxxxxxxx', '6', '621797', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('64', '邮政银行', '绿卡银联标准卡', '19', '622199xxxxxxxxxxxxx', '6', '622199', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('65', '邮政银行', '绿卡通', '19', '621096xxxxxxxxxxxxx', '6', '621096', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('66', '邮政银行', '绿卡储蓄卡(银联卡)', '19', '62215049xxxxxxxxxxx', '8', '62215049', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('67', '邮政银行', '绿卡储蓄卡(银联卡)', '19', '62215050xxxxxxxxxxx', '8', '62215050', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('68', '邮政银行', '绿卡储蓄卡(银联卡)', '19', '62215051xxxxxxxxxxx', '8', '62215051', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('69', '邮政银行', '绿卡储蓄卡(银联卡)', '19', '62218849xxxxxxxxxxx', '8', '62218849', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('70', '邮政银行', '绿卡储蓄卡(银联卡)', '19', '62218850xxxxxxxxxxx', '8', '62218850', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('71', '邮政银行', '绿卡储蓄卡(银联卡)', '19', '62218851xxxxxxxxxxx', '8', '62218851', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('72', '邮政银行', '武警军人保障卡', '19', '621622xxxxxxxxxxxxx', '6', '621622', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('73', '邮政银行', '中国旅游卡（金卡）', '19', '623219xxxxxxxxxxxxx', '6', '623219', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('74', '邮政银行', '普通高中学生资助卡', '19', '621674xxxxxxxxxxxxx', '6', '621674', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('75', '邮政银行', '中国旅游卡（普卡）', '19', '623218xxxxxxxxxxxxx', '6', '623218', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('76', '邮政银行', '福农卡', '19', '621599xxxxxxxxxxxxx', '6', '621599', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('77', '邮政银行', '绿卡通IC卡-白金卡', '19', '623698xxxxxxxxxxxxx', '6', '623698', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('78', '邮政银行', '绿卡通IC卡-钻石卡', '19', '623699xxxxxxxxxxxxx', '6', '623699', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('79', '邮政银行', '绿卡通IC联名卡', '19', '623686xxxxxxxxxxxxx', '6', '623686', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('80', '邮政银行', '绿卡芯片卡', '19', '622182xxxxxxxxxxxxx', '6', '622182', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('81', '邮政银行', '绿卡通IC卡福农卡', '19', '625586xxxxxxxxxxxxx', '6', '625586', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('82', '邮政银行', '绿卡通', '19', '621098xxxxxxxxxxxxx', '6', '621098', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('83', '邮政银行', '--', '19', '620529xxxxxxxxxxxxx', '6', '620529', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('84', '邮政银行', '绿卡通IC卡全国联名卡', '19', '622180xxxxxxxxxxxxx', '6', '622180', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('85', '邮政银行', '绿卡通（青年卡）', '19', '622187xxxxxxxxxxxxx', '6', '622187', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('86', '邮政银行', '绿卡通（+薪卡）', '19', '622189xxxxxxxxxxxxx', '6', '622189', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('114', '工商银行', '牡丹灵通卡', '18', '620200xxxxxxxxxxxx', '6', '620200', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('115', '工商银行', '牡丹灵通卡', '18', '620302xxxxxxxxxxxx', '6', '620302', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('116', '工商银行', '牡丹灵通卡', '18', '620402xxxxxxxxxxxx', '6', '620402', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('117', '工商银行', '牡丹灵通卡', '18', '620403xxxxxxxxxxxx', '6', '620403', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('118', '工商银行', '牡丹灵通卡', '18', '620404xxxxxxxxxxxx', '6', '620404', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('120', '工商银行', '牡丹灵通卡', '18', '620406xxxxxxxxxxxx', '6', '620406', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('121', '工商银行', '牡丹灵通卡', '18', '620407xxxxxxxxxxxx', '6', '620407', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('123', '工商银行', '牡丹灵通卡', '18', '620409xxxxxxxxxxxx', '6', '620409', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('124', '工商银行', '牡丹灵通卡', '18', '620410xxxxxxxxxxxx', '6', '620410', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('125', '工商银行', '牡丹灵通卡', '18', '620411xxxxxxxxxxxx', '6', '620411', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('126', '工商银行', '牡丹灵通卡', '18', '620412xxxxxxxxxxxx', '6', '620412', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('127', '工商银行', '牡丹灵通卡', '18', '620502xxxxxxxxxxxx', '6', '620502', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('128', '工商银行', '牡丹灵通卡', '18', '620503xxxxxxxxxxxx', '6', '620503', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('129', '工商银行', '牡丹灵通卡', '18', '620405xxxxxxxxxxxx', '6', '620405', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('130', '工商银行', '牡丹灵通卡', '18', '620408xxxxxxxxxxxx', '6', '620408', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('131', '工商银行', '牡丹灵通卡', '18', '620512xxxxxxxxxxxx', '6', '620512', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('132', '工商银行', '牡丹灵通卡', '18', '620602xxxxxxxxxxxx', '6', '620602', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('133', '工商银行', '牡丹灵通卡', '18', '620604xxxxxxxxxxxx', '6', '620604', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('134', '工商银行', '牡丹灵通卡', '18', '620607xxxxxxxxxxxx', '6', '620607', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('135', '工商银行', '牡丹灵通卡', '18', '620611xxxxxxxxxxxx', '6', '620611', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('136', '工商银行', '牡丹灵通卡', '18', '620612xxxxxxxxxxxx', '6', '620612', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('137', '工商银行', '牡丹灵通卡', '18', '620704xxxxxxxxxxxx', '6', '620704', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('138', '工商银行', '牡丹灵通卡', '18', '620706xxxxxxxxxxxx', '6', '620706', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('139', '工商银行', '牡丹灵通卡', '18', '620707xxxxxxxxxxxx', '6', '620707', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('140', '工商银行', '牡丹灵通卡', '18', '620708xxxxxxxxxxxx', '6', '620708', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('141', '工商银行', '牡丹灵通卡', '18', '620709xxxxxxxxxxxx', '6', '620709', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('142', '工商银行', '牡丹灵通卡', '18', '620710xxxxxxxxxxxx', '6', '620710', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('143', '工商银行', '牡丹灵通卡', '18', '620609xxxxxxxxxxxx', '6', '620609', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('144', '工商银行', '牡丹灵通卡', '18', '620712xxxxxxxxxxxx', '6', '620712', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('145', '工商银行', '牡丹灵通卡', '18', '620713xxxxxxxxxxxx', '6', '620713', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('146', '工商银行', '牡丹灵通卡', '18', '620714xxxxxxxxxxxx', '6', '620714', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('147', '工商银行', '牡丹灵通卡', '18', '620802xxxxxxxxxxxx', '6', '620802', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('148', '工商银行', '牡丹灵通卡', '18', '620711xxxxxxxxxxxx', '6', '620711', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('149', '工商银行', '牡丹灵通卡', '18', '620904xxxxxxxxxxxx', '6', '620904', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('150', '工商银行', '牡丹灵通卡', '18', '620905xxxxxxxxxxxx', '6', '620905', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('151', '工商银行', '牡丹灵通卡', '18', '621001xxxxxxxxxxxx', '6', '621001', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('152', '工商银行', '牡丹灵通卡', '18', '620902xxxxxxxxxxxx', '6', '620902', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('153', '工商银行', '牡丹灵通卡', '18', '621103xxxxxxxxxxxx', '6', '621103', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('154', '工商银行', '牡丹灵通卡', '18', '621105xxxxxxxxxxxx', '6', '621105', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('155', '工商银行', '牡丹灵通卡', '18', '621106xxxxxxxxxxxx', '6', '621106', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('156', '工商银行', '牡丹灵通卡', '18', '621107xxxxxxxxxxxx', '6', '621107', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('157', '工商银行', '牡丹灵通卡', '18', '621102xxxxxxxxxxxx', '6', '621102', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('158', '工商银行', '牡丹灵通卡', '18', '621203xxxxxxxxxxxx', '6', '621203', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('159', '工商银行', '牡丹灵通卡', '18', '621204xxxxxxxxxxxx', '6', '621204', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('160', '工商银行', '牡丹灵通卡', '18', '621205xxxxxxxxxxxx', '6', '621205', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('161', '工商银行', '牡丹灵通卡', '18', '621206xxxxxxxxxxxx', '6', '621206', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('162', '工商银行', '牡丹灵通卡', '18', '621207xxxxxxxxxxxx', '6', '621207', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('163', '工商银行', '牡丹灵通卡', '18', '621208xxxxxxxxxxxx', '6', '621208', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('164', '工商银行', '牡丹灵通卡', '18', '621209xxxxxxxxxxxx', '6', '621209', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('165', '工商银行', '牡丹灵通卡', '18', '621210xxxxxxxxxxxx', '6', '621210', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('166', '工商银行', '牡丹灵通卡', '18', '621302xxxxxxxxxxxx', '6', '621302', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('167', '工商银行', '牡丹灵通卡', '18', '621303xxxxxxxxxxxx', '6', '621303', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('168', '工商银行', '牡丹灵通卡', '18', '621202xxxxxxxxxxxx', '6', '621202', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('169', '工商银行', '牡丹灵通卡', '18', '621305xxxxxxxxxxxx', '6', '621305', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('170', '工商银行', '牡丹灵通卡', '18', '621306xxxxxxxxxxxx', '6', '621306', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('171', '工商银行', '牡丹灵通卡', '18', '621307xxxxxxxxxxxx', '6', '621307', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('172', '工商银行', '牡丹灵通卡', '18', '621309xxxxxxxxxxxx', '6', '621309', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('173', '工商银行', '牡丹灵通卡', '18', '621311xxxxxxxxxxxx', '6', '621311', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('174', '工商银行', '牡丹灵通卡', '18', '621313xxxxxxxxxxxx', '6', '621313', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('175', '工商银行', '牡丹灵通卡', '18', '621211xxxxxxxxxxxx', '6', '621211', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('176', '工商银行', '牡丹灵通卡', '18', '621315xxxxxxxxxxxx', '6', '621315', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('177', '工商银行', '牡丹灵通卡', '18', '621304xxxxxxxxxxxx', '6', '621304', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('178', '工商银行', '牡丹灵通卡', '18', '621402xxxxxxxxxxxx', '6', '621402', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('179', '工商银行', '牡丹灵通卡', '18', '621404xxxxxxxxxxxx', '6', '621404', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('180', '工商银行', '牡丹灵通卡', '18', '621405xxxxxxxxxxxx', '6', '621405', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('181', '工商银行', '牡丹灵通卡', '18', '621406xxxxxxxxxxxx', '6', '621406', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('182', '工商银行', '牡丹灵通卡', '18', '621407xxxxxxxxxxxx', '6', '621407', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('183', '工商银行', '牡丹灵通卡', '18', '621408xxxxxxxxxxxx', '6', '621408', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('184', '工商银行', '牡丹灵通卡', '18', '621409xxxxxxxxxxxx', '6', '621409', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('185', '工商银行', '牡丹灵通卡', '18', '621410xxxxxxxxxxxx', '6', '621410', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('186', '工商银行', '牡丹灵通卡', '18', '621502xxxxxxxxxxxx', '6', '621502', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('187', '工商银行', '牡丹灵通卡', '18', '621317xxxxxxxxxxxx', '6', '621317', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('188', '工商银行', '牡丹灵通卡', '18', '621511xxxxxxxxxxxx', '6', '621511', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('189', '工商银行', '牡丹灵通卡', '18', '621602xxxxxxxxxxxx', '6', '621602', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('190', '工商银行', '牡丹灵通卡', '18', '621603xxxxxxxxxxxx', '6', '621603', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('191', '工商银行', '牡丹灵通卡', '18', '621604xxxxxxxxxxxx', '6', '621604', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('192', '工商银行', '牡丹灵通卡', '18', '621605xxxxxxxxxxxx', '6', '621605', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('193', '工商银行', '牡丹灵通卡', '18', '621608xxxxxxxxxxxx', '6', '621608', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('194', '工商银行', '牡丹灵通卡', '18', '621609xxxxxxxxxxxx', '6', '621609', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('195', '工商银行', '牡丹灵通卡', '18', '621610xxxxxxxxxxxx', '6', '621610', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('196', '工商银行', '牡丹灵通卡', '18', '621611xxxxxxxxxxxx', '6', '621611', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('197', '工商银行', '牡丹灵通卡', '18', '621612xxxxxxxxxxxx', '6', '621612', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('198', '工商银行', '牡丹灵通卡', '18', '621613xxxxxxxxxxxx', '6', '621613', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('199', '工商银行', '牡丹灵通卡', '18', '621614xxxxxxxxxxxx', '6', '621614', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('200', '工商银行', '牡丹灵通卡', '18', '621615xxxxxxxxxxxx', '6', '621615', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('201', '工商银行', '牡丹灵通卡', '18', '621616xxxxxxxxxxxx', '6', '621616', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('202', '工商银行', '牡丹灵通卡', '18', '621617xxxxxxxxxxxx', '6', '621617', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('203', '工商银行', '牡丹灵通卡', '18', '621607xxxxxxxxxxxx', '6', '621607', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('204', '工商银行', '牡丹灵通卡', '18', '621606xxxxxxxxxxxx', '6', '621606', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('205', '工商银行', '牡丹灵通卡', '18', '621804xxxxxxxxxxxx', '6', '621804', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('206', '工商银行', '牡丹灵通卡', '18', '621807xxxxxxxxxxxx', '6', '621807', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('207', '工商银行', '牡丹灵通卡', '18', '621813xxxxxxxxxxxx', '6', '621813', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('208', '工商银行', '牡丹灵通卡', '18', '621814xxxxxxxxxxxx', '6', '621814', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('209', '工商银行', '牡丹灵通卡', '18', '621817xxxxxxxxxxxx', '6', '621817', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('210', '工商银行', '牡丹灵通卡', '18', '621901xxxxxxxxxxxx', '6', '621901', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('211', '工商银行', '牡丹灵通卡', '18', '621904xxxxxxxxxxxx', '6', '621904', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('212', '工商银行', '牡丹灵通卡', '18', '621905xxxxxxxxxxxx', '6', '621905', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('213', '工商银行', '牡丹灵通卡', '18', '621906xxxxxxxxxxxx', '6', '621906', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('214', '工商银行', '牡丹灵通卡', '18', '621907xxxxxxxxxxxx', '6', '621907', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('215', '工商银行', '牡丹灵通卡', '18', '621908xxxxxxxxxxxx', '6', '621908', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('216', '工商银行', '牡丹灵通卡', '18', '621909xxxxxxxxxxxx', '6', '621909', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('217', '工商银行', '牡丹灵通卡', '18', '621910xxxxxxxxxxxx', '6', '621910', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('218', '工商银行', '牡丹灵通卡', '18', '621911xxxxxxxxxxxx', '6', '621911', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('219', '工商银行', '牡丹灵通卡', '18', '621912xxxxxxxxxxxx', '6', '621912', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('220', '工商银行', '牡丹灵通卡', '18', '621913xxxxxxxxxxxx', '6', '621913', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('221', '工商银行', '牡丹灵通卡', '18', '621915xxxxxxxxxxxx', '6', '621915', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('222', '工商银行', '牡丹灵通卡', '18', '622002xxxxxxxxxxxx', '6', '622002', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('223', '工商银行', '牡丹灵通卡', '18', '621903xxxxxxxxxxxx', '6', '621903', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('224', '工商银行', '牡丹灵通卡', '18', '622004xxxxxxxxxxxx', '6', '622004', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('225', '工商银行', '牡丹灵通卡', '18', '622005xxxxxxxxxxxx', '6', '622005', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('226', '工商银行', '牡丹灵通卡', '18', '622006xxxxxxxxxxxx', '6', '622006', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('227', '工商银行', '牡丹灵通卡', '18', '622007xxxxxxxxxxxx', '6', '622007', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('228', '工商银行', '牡丹灵通卡', '18', '622008xxxxxxxxxxxx', '6', '622008', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('229', '工商银行', '牡丹灵通卡', '18', '622010xxxxxxxxxxxx', '6', '622010', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('230', '工商银行', '牡丹灵通卡', '18', '622011xxxxxxxxxxxx', '6', '622011', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('231', '工商银行', '牡丹灵通卡', '18', '622012xxxxxxxxxxxx', '6', '622012', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('232', '工商银行', '牡丹灵通卡', '18', '621914xxxxxxxxxxxx', '6', '621914', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('233', '工商银行', '牡丹灵通卡', '18', '622015xxxxxxxxxxxx', '6', '622015', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('234', '工商银行', '牡丹灵通卡', '18', '622016xxxxxxxxxxxx', '6', '622016', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('235', '工商银行', '牡丹灵通卡', '18', '622003xxxxxxxxxxxx', '6', '622003', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('236', '工商银行', '牡丹灵通卡', '18', '622018xxxxxxxxxxxx', '6', '622018', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('237', '工商银行', '牡丹灵通卡', '18', '622019xxxxxxxxxxxx', '6', '622019', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('238', '工商银行', '牡丹灵通卡', '18', '622020xxxxxxxxxxxx', '6', '622020', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('239', '工商银行', '牡丹灵通卡', '18', '622102xxxxxxxxxxxx', '6', '622102', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('240', '工商银行', '牡丹灵通卡', '18', '622103xxxxxxxxxxxx', '6', '622103', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('241', '工商银行', '牡丹灵通卡', '18', '622104xxxxxxxxxxxx', '6', '622104', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('242', '工商银行', '牡丹灵通卡', '18', '622105xxxxxxxxxxxx', '6', '622105', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('243', '工商银行', '牡丹灵通卡', '18', '622013xxxxxxxxxxxx', '6', '622013', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('244', '工商银行', '牡丹灵通卡', '18', '622111xxxxxxxxxxxx', '6', '622111', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('245', '工商银行', '牡丹灵通卡', '18', '622114xxxxxxxxxxxx', '6', '622114', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('246', '工商银行', '牡丹灵通卡', '18', '622017xxxxxxxxxxxx', '6', '622017', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('252', '工商银行', '牡丹灵通卡', '18', '622110xxxxxxxxxxxx', '6', '622110', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('269', '工商银行', '牡丹灵通卡', '18', '622303xxxxxxxxxxxx', '6', '622303', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('270', '工商银行', '牡丹灵通卡', '18', '622304xxxxxxxxxxxx', '6', '622304', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('271', '工商银行', '牡丹灵通卡', '18', '622305xxxxxxxxxxxx', '6', '622305', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('272', '工商银行', '牡丹灵通卡', '18', '622306xxxxxxxxxxxx', '6', '622306', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('273', '工商银行', '牡丹灵通卡', '18', '622307xxxxxxxxxxxx', '6', '622307', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('274', '工商银行', '牡丹灵通卡', '18', '622308xxxxxxxxxxxx', '6', '622308', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('275', '工商银行', '牡丹灵通卡', '18', '622309xxxxxxxxxxxx', '6', '622309', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('277', '工商银行', '牡丹灵通卡', '18', '622314xxxxxxxxxxxx', '6', '622314', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('278', '工商银行', '牡丹灵通卡', '18', '622315xxxxxxxxxxxx', '6', '622315', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('279', '工商银行', '牡丹灵通卡', '18', '622317xxxxxxxxxxxx', '6', '622317', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('280', '工商银行', '牡丹灵通卡', '18', '622302xxxxxxxxxxxx', '6', '622302', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('281', '工商银行', '牡丹灵通卡', '18', '622402xxxxxxxxxxxx', '6', '622402', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('282', '工商银行', '牡丹灵通卡', '18', '622403xxxxxxxxxxxx', '6', '622403', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('283', '工商银行', '牡丹灵通卡', '18', '622404xxxxxxxxxxxx', '6', '622404', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('284', '工商银行', '牡丹灵通卡', '18', '622313xxxxxxxxxxxx', '6', '622313', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('285', '工商银行', '牡丹灵通卡', '18', '622504xxxxxxxxxxxx', '6', '622504', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('286', '工商银行', '牡丹灵通卡', '18', '622505xxxxxxxxxxxx', '6', '622505', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('287', '工商银行', '牡丹灵通卡', '18', '622509xxxxxxxxxxxx', '6', '622509', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('288', '工商银行', '牡丹灵通卡', '18', '622513xxxxxxxxxxxx', '6', '622513', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('289', '工商银行', '牡丹灵通卡', '18', '622517xxxxxxxxxxxx', '6', '622517', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('290', '工商银行', '牡丹灵通卡', '18', '622502xxxxxxxxxxxx', '6', '622502', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('291', '工商银行', '牡丹灵通卡', '18', '622604xxxxxxxxxxxx', '6', '622604', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('292', '工商银行', '牡丹灵通卡', '18', '622605xxxxxxxxxxxx', '6', '622605', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('293', '工商银行', '牡丹灵通卡', '18', '622606xxxxxxxxxxxx', '6', '622606', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('294', '工商银行', '牡丹灵通卡', '18', '622510xxxxxxxxxxxx', '6', '622510', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('295', '工商银行', '牡丹灵通卡', '18', '622703xxxxxxxxxxxx', '6', '622703', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('296', '工商银行', '牡丹灵通卡', '18', '622715xxxxxxxxxxxx', '6', '622715', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('297', '工商银行', '牡丹灵通卡', '18', '622806xxxxxxxxxxxx', '6', '622806', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('298', '工商银行', '牡丹灵通卡', '18', '622902xxxxxxxxxxxx', '6', '622902', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('299', '工商银行', '牡丹灵通卡', '18', '622903xxxxxxxxxxxx', '6', '622903', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('300', '工商银行', '牡丹灵通卡', '18', '622706xxxxxxxxxxxx', '6', '622706', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('301', '工商银行', '牡丹灵通卡', '18', '623002xxxxxxxxxxxx', '6', '623002', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('302', '工商银行', '牡丹灵通卡', '18', '623006xxxxxxxxxxxx', '6', '623006', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('303', '工商银行', '牡丹灵通卡', '18', '623008xxxxxxxxxxxx', '6', '623008', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('304', '工商银行', '牡丹灵通卡', '18', '623011xxxxxxxxxxxx', '6', '623011', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('305', '工商银行', '牡丹灵通卡', '18', '623012xxxxxxxxxxxx', '6', '623012', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('306', '工商银行', '牡丹灵通卡', '18', '622904xxxxxxxxxxxx', '6', '622904', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('307', '工商银行', '牡丹灵通卡', '18', '623015xxxxxxxxxxxx', '6', '623015', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('308', '工商银行', '牡丹灵通卡', '18', '623100xxxxxxxxxxxx', '6', '623100', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('309', '工商银行', '牡丹灵通卡', '18', '623202xxxxxxxxxxxx', '6', '623202', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('310', '工商银行', '牡丹灵通卡', '18', '623301xxxxxxxxxxxx', '6', '623301', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('311', '工商银行', '牡丹灵通卡', '18', '623400xxxxxxxxxxxx', '6', '623400', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('312', '工商银行', '牡丹灵通卡', '18', '623500xxxxxxxxxxxx', '6', '623500', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('313', '工商银行', '牡丹灵通卡', '18', '623602xxxxxxxxxxxx', '6', '623602', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('314', '工商银行', '牡丹灵通卡', '18', '623803xxxxxxxxxxxx', '6', '623803', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('315', '工商银行', '牡丹灵通卡', '18', '623901xxxxxxxxxxxx', '6', '623901', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('316', '工商银行', '牡丹灵通卡', '18', '623014xxxxxxxxxxxx', '6', '623014', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('317', '工商银行', '牡丹灵通卡', '18', '624100xxxxxxxxxxxx', '6', '624100', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('318', '工商银行', '牡丹灵通卡', '18', '624200xxxxxxxxxxxx', '6', '624200', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('319', '工商银行', '牡丹灵通卡', '18', '624301xxxxxxxxxxxx', '6', '624301', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('320', '工商银行', '牡丹灵通卡', '18', '624402xxxxxxxxxxxx', '6', '624402', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('327', '工商银行', '牡丹灵通卡', '18', '623700xxxxxxxxxxxx', '6', '623700', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('329', '工商银行', '牡丹灵通卡', '18', '624000xxxxxxxxxxxx', '6', '624000', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('330', '工商银行', '牡丹灵通卡(银联卡)', '19', '9558xxxxxxxxxxxxxxx', '4', '9558', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('338', '工商银行', '牡丹社会保障卡', '19', '900000xxxxxxxxxxxxx', '6', '900000', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('342', '工商银行', '福农灵通卡', '19', '621558xxxxxxxxxxxxx', '6', '621558', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('343', '工商银行', '福农灵通卡', '19', '621559xxxxxxxxxxxxx', '6', '621559', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('344', '工商银行', '灵通卡', '19', '621722XXXXXXXXXXXXX', '6', '621722', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('345', '工商银行', '灵通卡', '19', '621723XXXXXXXXXXXXX', '6', '621723', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('346', '工商银行', '牡丹卡普卡', '19', '621226xxxxxxxxxxxxx', '6', '621226', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('347', '工商银行', '国际借记卡', '16', '402791xxxxxxxxxx', '6', '402791', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('348', '工商银行', '国际借记卡', '16', '427028xxxxxxxxxx', '6', '427028', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('349', '工商银行', '国际借记卡', '16', '427038xxxxxxxxxx', '6', '427038', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('350', '工商银行', '国际借记卡', '16', '548259xxxxxxxxxx', '6', '548259', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('356', '工商银行', '预付芯片卡', '19', '620516xxxxxxxxxxxxx', '6', '620516', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('357', '工商银行', '灵通卡', '19', '621721XXXXXXXXXXXXX', '6', '621721', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('358', '工商银行', '牡丹宁波市民卡', '19', '900010xxxxxxxxxxxxx', '6', '900010', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('365', '工商银行', '工银财富卡', '19', '621288xxxxxxxxxxxxx', '6', '621288', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('390', '工商银行', '中国旅行卡', '19', '620086xxxxxxxxxxxxx', '6', '620086', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('391', '工商银行', '普通高中学生资助卡', '19', '621670xxxxxxxxxxxxx', '6', '621670', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('392', '工商银行', '财智账户卡', '19', '623260xxxxxxxxxxxxx', '6', '623260', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('393', '工商银行', '银联标准卡', '19', '620058xxxxxxxxxxxxx', '6', '620058', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('394', '工商银行', '牡丹卡普卡', '19', '621225xxxxxxxxxxxxx', '6', '621225', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('395', '工商银行', '理财金账户金卡', '19', '621227xxxxxxxxxxxxx', '6', '621227', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('396', '工商银行', '中职学生资助卡', '19', '621281xxxxxxxxxxxxx', '6', '621281', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('397', '工商银行', '武警军人保障卡', '19', '621618xxxxxxxxxxxxx', '6', '621618', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('398', '工商银行', '灵通卡', '19', '622200xxxxxxxxxxxxx', '6', '622200', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('399', '工商银行', 'E时代卡', '19', '622202xxxxxxxxxxxxx', '6', '622202', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('400', '工商银行', 'E时代卡', '19', '622203xxxxxxxxxxxxx', '6', '622203', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('401', '工商银行', '理财金卡', '19', '622208xxxxxxxxxxxxx', '6', '622208', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('402', '工商银行', '借记卡', '19', '623062xxxxxxxxxxxxx', '6', '623062', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('403', '工商银行', '--', '19', '623271xxxxxxxxxxxxx', '6', '623271', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('404', '工商银行', '--', '19', '623272xxxxxxxxxxxxx', '6', '623272', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('405', '工商银行', '借记白金卡', '19', '621218xxxxxxxxxxxxx', '6', '621218', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('406', '工商银行', '借记卡灵通卡', '19', '621475xxxxxxxxxxxxx', '6', '621475', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('407', '工商银行', '借记卡金卡', '19', '621476xxxxxxxxxxxxx', '6', '621476', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('408', '工商银行', '中国旅游卡', '19', '623229xxxxxxxxxxxxx', '6', '623229', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('523', '农业银行', '金穗借记卡', '19', '103xxxxxxxxxxxxxxxx', '3', '103', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('527', '农业银行', '中国旅游卡', '19', '623206xxxxxxxxxxxxx', '6', '623206', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('528', '农业银行', '普通高中学生资助卡', '19', '621671xxxxxxxxxxxxx', '6', '621671', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('529', '农业银行', '银联标准卡', '19', '620059xxxxxxxxxxxxx', '6', '620059', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('545', '农业银行', '中职学生资助卡', '19', '621282xxxxxxxxxxxxx', '6', '621282', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('546', '农业银行', '专用惠农卡', '19', '621336xxxxxxxxxxxxx', '6', '621336', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('547', '农业银行', '武警军人保障卡', '19', '621619xxxxxxxxxxxxx', '6', '621619', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('548', '农业银行', '金穗校园卡(银联卡)', '19', '622821xxxxxxxxxxxxx', '6', '622821', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('549', '农业银行', '金穗星座卡(银联卡)', '19', '622822xxxxxxxxxxxxx', '6', '622822', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('550', '农业银行', '金穗社保卡(银联卡)', '19', '622823xxxxxxxxxxxxx', '6', '622823', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('551', '农业银行', '金穗旅游卡(银联卡)', '19', '622824xxxxxxxxxxxxx', '6', '622824', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('552', '农业银行', '金穗青年卡(银联卡)', '19', '622825xxxxxxxxxxxxx', '6', '622825', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('553', '农业银行', '复合介质金穗通宝卡', '19', '622826xxxxxxxxxxxxx', '6', '622826', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('554', '农业银行', '金穗海通卡', '19', '622827xxxxxxxxxxxxx', '6', '622827', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('555', '农业银行', '退役金卡', '19', '622828xxxxxxxxxxxxx', '6', '622828', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('558', '农业银行', '金穗通宝卡(银联卡)', '19', '622840xxxxxxxxxxxxx', '6', '622840', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('559', '农业银行', '金穗惠农卡', '19', '622841xxxxxxxxxxxxx', '6', '622841', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('560', '农业银行', '金穗通宝银卡', '19', '622843xxxxxxxxxxxxx', '6', '622843', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('561', '农业银行', '金穗通宝卡(银联卡)', '19', '622844xxxxxxxxxxxxx', '6', '622844', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('562', '农业银行', '金穗通宝卡(银联卡)', '19', '622845xxxxxxxxxxxxx', '6', '622845', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('563', '农业银行', '金穗通宝卡', '19', '622846xxxxxxxxxxxxx', '6', '622846', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('564', '农业银行', '金穗通宝卡(银联卡)', '19', '622847xxxxxxxxxxxxx', '6', '622847', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('565', '农业银行', '金穗通宝卡(银联卡)', '19', '622848xxxxxxxxxxxxx', '6', '622848', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('566', '农业银行', '金穗通宝钻石卡', '19', '622849xxxxxxxxxxxxx', '6', '622849', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('567', '农业银行', '掌尚钱包', '19', '623018xxxxxxxxxxxxx', '6', '623018', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('572', '农业银行', '借记卡(银联卡)', '19', '95595xxxxxxxxxxxxxx', '5', '95595', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('573', '农业银行', '借记卡(银联卡)', '19', '95596xxxxxxxxxxxxxx', '5', '95596', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('574', '农业银行', '借记卡(银联卡)', '19', '95597xxxxxxxxxxxxxx', '5', '95597', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('575', '农业银行', '借记卡(银联卡)', '19', '95598xxxxxxxxxxxxxx', '5', '95598', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('576', '农业银行', '借记卡(银联卡)', '19', '95599xxxxxxxxxxxxxx', '5', '95599', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('592', '中国银行', '联名卡', '19', '621660xxxxxxxxxxxxx', '6', '621660', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('593', '中国银行', '个人普卡', '19', '621661xxxxxxxxxxxxx', '6', '621661', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('594', '中国银行', '员工普卡', '19', '621663xxxxxxxxxxxxx', '6', '621663', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('595', '中国银行', '理财普卡', '19', '621667xxxxxxxxxxxxx', '6', '621667', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('596', '中国银行', '理财金卡', '19', '621668xxxxxxxxxxxxx', '6', '621668', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('597', '中国银行', '理财白金卡', '19', '621666xxxxxxxxxxxxx', '6', '621666', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('607', '中国银行', '长城电子借记卡', '19', '456351xxxxxxxxxxxxx', '6', '456351', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('639', '中国银行', '长城电子借记卡', '19', '601382xxxxxxxxxxxxx', '6', '601382', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('642', '中国银行', '一卡双账户普卡', '19', '621256xxxxxxxxxxxxx', '6', '621256', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('643', '中国银行', '财互通卡', '19', '621212xxxxxxxxxxxxx', '6', '621212', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('644', '中国银行', '电子现金卡', '16', '620514xxxxxxxxxx', '6', '620514', '预付费卡');
INSERT INTO `bankcardinfo` VALUES ('648', '中国银行', '中职学生资助卡', '19', '621283xxxxxxxxxxxxx', '6', '621283', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('649', '中国银行', '银联标准卡', '19', '620061xxxxxxxxxxxxx', '6', '620061', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('650', '中国银行', '金融IC卡', '19', '621725xxxxxxxxxxxxx', '6', '621725', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('651', '中国银行', '长城社会保障卡', '19', '620040xxxxxxxxxxxxx', '6', '620040', '预付费卡');
INSERT INTO `bankcardinfo` VALUES ('653', '中国银行', '社保联名卡', '19', '621330xxxxxxxxxxxxx', '6', '621330', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('654', '中国银行', '社保联名卡', '19', '621331xxxxxxxxxxxxx', '6', '621331', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('655', '中国银行', '医保联名卡', '19', '621332xxxxxxxxxxxxx', '6', '621332', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('656', '中国银行', '医保联名卡', '19', '621333xxxxxxxxxxxxx', '6', '621333', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('657', '中国银行', '公司借记卡', '19', '621297xxxxxxxxxxxxx', '6', '621297', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('659', '中国银行', '长城福农借记卡金卡', '19', '621568xxxxxxxxxxxxx', '6', '621568', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('660', '中国银行', '长城福农借记卡普卡', '19', '621569xxxxxxxxxxxxx', '6', '621569', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('667', '中国银行', '中国旅游卡', '19', '623208xxxxxxxxxxxxx', '6', '623208', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('668', '中国银行', '武警军人保障卡', '19', '621620xxxxxxxxxxxxx', '6', '621620', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('669', '中国银行', '社保联名借记IC卡', '19', '621756xxxxxxxxxxxxx', '6', '621756', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('670', '中国银行', '社保联名借记IC卡', '19', '621757xxxxxxxxxxxxx', '6', '621757', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('671', '中国银行', '医保联名借记IC卡', '19', '621758xxxxxxxxxxxxx', '6', '621758', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('672', '中国银行', '医保联名借记IC卡', '19', '621759xxxxxxxxxxxxx', '6', '621759', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('673', '中国银行', '借记IC个人普卡', '19', '621785xxxxxxxxxxxxx', '6', '621785', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('674', '中国银行', '借记IC个人金卡', '19', '621786xxxxxxxxxxxxx', '6', '621786', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('675', '中国银行', '借记IC个人普卡', '19', '621787xxxxxxxxxxxxx', '6', '621787', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('676', '中国银行', '借记IC白金卡', '19', '621788xxxxxxxxxxxxx', '6', '621788', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('677', '中国银行', '借记IC钻石卡', '19', '621789xxxxxxxxxxxxx', '6', '621789', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('678', '中国银行', '借记IC联名卡', '19', '621790xxxxxxxxxxxxx', '6', '621790', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('679', '中国银行', '普通高中学生资助卡', '19', '621672xxxxxxxxxxxxx', '6', '621672', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('683', '中国银行', '理财银卡', '19', '621669xxxxxxxxxxxxx', '6', '621669', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('684', '中国银行', '个人金卡', '19', '621662xxxxxxxxxxxxx', '6', '621662', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('685', '中国银行', '长城代发薪借记IC卡（钻石卡）', '19', '623571xxxxxxxxxxxxx', '6', '623571', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('686', '中国银行', '长城代发薪借记IC卡（白金卡）', '19', '623572xxxxxxxxxxxxx', '6', '623572', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('687', '中国银行', '长城代发薪借记IC卡（普卡）', '19', '623575xxxxxxxxxxxxx', '6', '623575', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('688', '中国银行', '中银单位结算卡', '19', '623263xxxxxxxxxxxxx', '6', '623263', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('689', '中国银行', '银联保障类借记IC卡（金卡）', '19', '623184xxxxxxxxxxxxx', '6', '623184', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('690', '中国银行', '银联保障类借记IC卡（白金卡）', '19', '623569xxxxxxxxxxxxx', '6', '623569', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('691', '中国银行', '银联多币借记IC卡（钻石卡）', '19', '623586xxxxxxxxxxxxx', '6', '623586', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('693', '中国银行', '长城代发薪借记IC卡（金卡）', '19', '623573xxxxxxxxxxxxx', '6', '623573', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('694', '中国银行', '员工金卡', '19', '621665xxxxxxxxxxxxx', '6', '621665', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('695', '中国银行', '--', '19', '627025xxxxxxxxxxxxx', '6', '627025', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('696', '中国银行', '--', '19', '627026xxxxxxxxxxxxx', '6', '627026', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('697', '中国银行', '--', '19', '627027xxxxxxxxxxxxx', '6', '627027', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('698', '中国银行', '--', '19', '627028xxxxxxxxxxxxx', '6', '627028', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('736', '中国银行', '长城宁波市民卡', '19', '620513xxxxxxxxxxxxx', '6', '620513', '预付费卡');
INSERT INTO `bankcardinfo` VALUES ('742', '建设银行', '中职学生资助卡', '19', '621284xxxxxxxxxxxxx', '6', '621284', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('743', '建设银行', '乐当家银卡VISA', '16', '421349xxxxxxxxxx', '6', '421349', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('744', '建设银行', '乐当家金卡VISA', '16', '434061xxxxxxxxxx', '6', '434061', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('745', '建设银行', '乐当家白金卡', '16', '434062xxxxxxxxxx', '6', '434062', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('747', '建设银行', '龙卡储蓄卡', '19', '436742xxxxxxxxxxxxx', '6', '436742', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('750', '建设银行', '乐当家', '16', '524094xxxxxxxxxx', '6', '524094', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('751', '建设银行', '乐当家', '16', '526410xxxxxxxxxx', '6', '526410', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('755', '建设银行', '乐当家白金卡', '16', '552245xxxxxxxxxx', '6', '552245', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('756', '建设银行', '金融复合IC卡', '19', '589970xxxxxxxxxxxxx', '6', '589970', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('757', '建设银行', '银联标准卡', '19', '620060xxxxxxxxxxxxx', '6', '620060', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('758', '建设银行', '银联理财钻石卡', '16', '621080xxxxxxxxxx', '6', '621080', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('759', '建设银行', '金融IC卡', '19', '621081xxxxxxxxxxxxx', '6', '621081', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('760', '建设银行', '理财白金卡', '16', '621466xxxxxxxxxx', '6', '621466', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('761', '建设银行', '社保IC卡', '19', '621467xxxxxxxxxxxxx', '6', '621467', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('762', '建设银行', '财富卡私人银行卡', '16', '621488xxxxxxxxxx', '6', '621488', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('763', '建设银行', '理财金卡', '16', '621499xxxxxxxxxx', '6', '621499', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('764', '建设银行', '福农卡', '19', '621598xxxxxxxxxxxxx', '6', '621598', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('765', '建设银行', '武警军人保障卡', '19', '621621xxxxxxxxxxxxx', '6', '621621', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('766', '建设银行', '龙卡通', '19', '621700xxxxxxxxxxxxx', '6', '621700', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('767', '建设银行', '银联储蓄卡', '19', '622280xxxxxxxxxxxxx', '6', '622280', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('768', '建设银行', '龙卡储蓄卡(银联卡)', '19', '622700xxxxxxxxxxxxx', '6', '622700', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('770', '建设银行', '理财白金卡', '16', '622966xxxxxxxxxx', '6', '622966', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('771', '建设银行', '理财金卡', '16', '622988xxxxxxxxxx', '6', '622988', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('775', '建设银行', '建行陆港通龙卡', '16', '621082xxxxxxxxxx', '6', '621082', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('776', '建设银行', '单位结算卡', '16', '623251xxxxxxxxxx', '6', '623251', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('777', '建设银行', '普通高中学生资助卡', '19', '621673xxxxxxxxxxxxx', '6', '621673', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('778', '建设银行', '中国旅游卡', '19', '623211xxxxxxxxxxxxx', '6', '623211', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('779', '建设银行', '结算通借记卡', '19', '623668xxxxxxxxxxxxx', '6', '623668', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('780', '建设银行', '代发工资借记IC卡', '19', '623094xxxxxxxxxxxxx', '6', '623094', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('781', '建设银行', '全国联名卡', '19', '623669xxxxxxxxxxxxx', '6', '623669', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('824', '交通银行', '交行预付卡', '19', '620021xxxxxxxxxxxxx', '6', '620021', '预付费卡');
INSERT INTO `bankcardinfo` VALUES ('825', '交通银行', '世博预付IC卡', '19', '620521xxxxxxxxxxxxx', '6', '620521', '预付费卡');
INSERT INTO `bankcardinfo` VALUES ('826', '交通银行', '太平洋互连卡', '17', '405512xxxxxxxxxxx', '8', '00405512', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('829', '交通银行', '太平洋万事顺卡', '17', '601428xxxxxxxxxxx', '8', '00601428', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('830', '交通银行', '太平洋互连卡(银联卡)', '17', '405512xxxxxxxxxxx', '6', '405512', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('839', '交通银行', '太平洋万事顺卡', '17', '601428xxxxxxxxxxx', '6', '601428', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('847', '交通银行', '太平洋借记卡', '17', '622258xxxxxxxxxxx', '6', '622258', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('848', '交通银行', '太平洋借记卡', '17', '622259xxxxxxxxxxx', '6', '622259', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('850', '交通银行', '太平洋借记卡', '19', '622261xxxxxxxxxxxxx', '6', '622261', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('855', '交通银行', '太平洋互连卡', '17', '405512xxxxxxxxxxx', '8', '66405512', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('857', '交通银行', '太平洋借记卡', '19', '622260xxxxxxxxxxxxx', '6', '622260', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('858', '交通银行', '太平洋万事顺卡', '17', '601428xxxxxxxxxxx', '8', '66601428', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('865', '交通银行', '交银IC卡', '19', '622262xxxxxxxxxxxxx', '6', '622262', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('866', '交通银行', '交通银行单位结算卡', '19', '623261xxxxxxxxxxxxx', '6', '623261', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('875', '中信银行', '中信借记卡', '16', '433670xxxxxxxxxx', '6', '433670', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('876', '中信银行', '中信借记卡', '16', '433680xxxxxxxxxx', '6', '433680', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('877', '中信银行', '中信国际借记卡', '16', '442729xxxxxxxxxx', '6', '442729', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('878', '中信银行', '中信国际借记卡', '16', '442730xxxxxxxxxx', '6', '442730', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('879', '中信银行', '中国旅行卡', '16', '620082xxxxxxxxxx', '6', '620082', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('880', '中信银行', '中信借记卡(银联卡)', '16', '622690xxxxxxxxxx', '6', '622690', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('881', '中信银行', '中信借记卡(银联卡)', '16', '622691xxxxxxxxxx', '6', '622691', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('882', '中信银行', '中信贵宾卡(银联卡)', '16', '622692xxxxxxxxxx', '6', '622692', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('883', '中信银行', '中信理财宝金卡', '16', '622696xxxxxxxxxx', '6', '622696', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('884', '中信银行', '中信理财宝白金卡', '16', '622698xxxxxxxxxx', '6', '622698', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('885', '中信银行', '中信钻石卡', '16', '622998xxxxxxxxxx', '6', '622998', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('886', '中信银行', '中信钻石卡', '16', '622999xxxxxxxxxx', '6', '622999', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('887', '中信银行', '中信借记卡', '16', '433671xxxxxxxxxx', '6', '433671', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('888', '中信银行', '中信理财宝(银联卡)', '16', '968807xxxxxxxxxx', '6', '968807', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('889', '中信银行', '中信理财宝(银联卡)', '16', '968808xxxxxxxxxx', '6', '968808', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('890', '中信银行', '中信理财宝(银联卡)', '16', '968809xxxxxxxxxx', '6', '968809', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('891', '中信银行', '借记卡', '16', '621771xxxxxxxxxx', '6', '621771', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('892', '中信银行', '理财宝IC卡', '16', '621767xxxxxxxxxx', '6', '621767', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('893', '中信银行', '理财宝IC卡', '16', '621768xxxxxxxxxx', '6', '621768', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('894', '中信银行', '理财宝IC卡', '16', '621770xxxxxxxxxx', '6', '621770', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('895', '中信银行', '理财宝IC卡', '16', '621772xxxxxxxxxx', '6', '621772', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('896', '中信银行', '理财宝IC卡', '16', '621773xxxxxxxxxx', '6', '621773', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('897', '中信银行', '主账户复合电子现金卡', '19', '620527xxxxxxxxxxxxx', '6', '620527', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('898', '中信银行', '中信银行钻石卡', '16', '621769xxxxxxxxxx', '6', '621769', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('899', '中信银行', '浙江高院案款管理系统专用卡', '16', '623280xxxxxxxxxx', '6', '623280', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('900', '光大银行', '阳光卡', '16', '303xxxxxxxxxxxxx', '3', '303', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('904', '光大银行', '阳光卡(银联卡)', '16', '622660xxxxxxxxxx', '6', '622660', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('905', '光大银行', '阳光卡(银联卡)', '16', '622662xxxxxxxxxx', '6', '622662', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('906', '光大银行', '阳光卡(银联卡)', '16', '622663xxxxxxxxxx', '6', '622663', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('907', '光大银行', '阳光卡(银联卡)', '16', '622664xxxxxxxxxx', '6', '622664', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('908', '光大银行', '阳光卡(银联卡)', '16', '622665xxxxxxxxxx', '6', '622665', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('909', '光大银行', '阳光卡(银联卡)', '16', '622666xxxxxxxxxx', '6', '622666', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('910', '光大银行', '阳光卡(银联卡)', '16', '622667xxxxxxxxxx', '6', '622667', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('911', '光大银行', '阳光卡(银联卡)', '16', '622669xxxxxxxxxx', '6', '622669', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('912', '光大银行', '阳光卡(银联卡)', '16', '622670xxxxxxxxxx', '6', '622670', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('913', '光大银行', '阳光卡(银联卡)', '16', '622671xxxxxxxxxx', '6', '622671', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('914', '光大银行', '阳光卡(银联卡)', '16', '622672xxxxxxxxxx', '6', '622672', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('915', '光大银行', '阳光卡(银联卡)', '16', '622668xxxxxxxxxx', '6', '622668', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('916', '光大银行', '阳光卡(银联卡)', '16', '622661xxxxxxxxxx', '6', '622661', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('917', '光大银行', '阳光卡(银联卡)', '16', '622674xxxxxxxxxx', '6', '622674', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('918', '光大银行', '阳光卡(银联卡)', '16', '90030xxxxxxxxxxx', '5', '90030', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('919', '光大银行', '阳光卡(银联卡)', '16', '622673xxxxxxxxxx', '6', '622673', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('920', '光大银行', '借记卡普卡', '16', '620518xxxxxxxxxx', '6', '620518', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('921', '光大银行', '社会保障IC卡', '16', '621489xxxxxxxxxx', '6', '621489', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('922', '光大银行', 'IC借记卡普卡', '16', '621492xxxxxxxxxx', '6', '621492', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('923', '光大银行', '手机支付卡', '19', '620535xxxxxxxxxxxxx', '6', '620535', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('924', '光大银行', '联名IC卡普卡', '16', '623156xxxxxxxxxx', '6', '623156', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('925', '光大银行', '借记IC卡白金卡', '16', '621490xxxxxxxxxx', '6', '621490', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('926', '光大银行', '借记IC卡金卡', '16', '621491xxxxxxxxxx', '6', '621491', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('927', '光大银行', '阳光旅行卡', '16', '620085xxxxxxxxxx', '6', '620085', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('928', '光大银行', '借记IC卡钻石卡', '16', '623155xxxxxxxxxx', '6', '623155', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('929', '光大银行', '联名IC卡金卡', '16', '623157xxxxxxxxxx', '6', '623157', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('930', '光大银行', '联名IC卡白金卡', '16', '623158xxxxxxxxxx', '6', '623158', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('931', '光大银行', '联名IC卡钻石卡', '16', '623159xxxxxxxxxx', '6', '623159', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('932', '光大银行', '--', '16', '623253xxxxxxxxxx', '6', '623253', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('933', '华夏银行', '华夏卡(银联卡)', '16', '999999xxxxxxxxxx', '6', '999999', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('934', '华夏银行', '华夏白金卡', '16', '621222xxxxxxxxxx', '6', '621222', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('935', '华夏银行', '华夏普卡', '16', '623020xxxxxxxxxx', '6', '623020', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('936', '华夏银行', '华夏金卡', '16', '623021xxxxxxxxxx', '6', '623021', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('937', '华夏银行', '华夏白金卡', '16', '623022xxxxxxxxxx', '6', '623022', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('938', '华夏银行', '华夏钻石卡', '16', '623023xxxxxxxxxx', '6', '623023', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('939', '华夏银行', '华夏卡(银联卡)', '16', '622630xxxxxxxxxx', '6', '622630', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('940', '华夏银行', '华夏至尊金卡(银联卡)', '16', '622631xxxxxxxxxx', '6', '622631', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('941', '华夏银行', '华夏丽人卡(银联卡)', '16', '622632xxxxxxxxxx', '6', '622632', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('942', '华夏银行', '华夏万通卡', '16', '622633xxxxxxxxxx', '6', '622633', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('943', '华夏银行', '--', '16', '620552xxxxxxxxxx', '6', '620552', '预付费卡');
INSERT INTO `bankcardinfo` VALUES ('944', '民生银行', '民生借记卡(银联卡)', '16', '622615xxxxxxxxxx', '6', '622615', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('945', '民生银行', '民生银联借记卡－金卡', '16', '622616xxxxxxxxxx', '6', '622616', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('946', '民生银行', '钻石卡', '16', '622618xxxxxxxxxx', '6', '622618', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('947', '民生银行', '民生借记卡(银联卡)', '16', '622622xxxxxxxxxx', '6', '622622', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('948', '民生银行', '民生借记卡(银联卡)', '16', '622617xxxxxxxxxx', '6', '622617', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('949', '民生银行', '民生借记卡(银联卡)', '16', '622619xxxxxxxxxx', '6', '622619', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('950', '民生银行', '民生借记卡', '16', '415599xxxxxxxxxx', '6', '415599', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('951', '民生银行', '民生国际卡', '16', '421393xxxxxxxxxx', '6', '421393', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('952', '民生银行', '民生国际卡(银卡)', '16', '421865xxxxxxxxxx', '6', '421865', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('955', '民生银行', '民生国际卡', '16', '472067xxxxxxxxxx', '6', '472067', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('956', '民生银行', '民生国际卡', '16', '472068xxxxxxxxxx', '6', '472068', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('957', '民生银行', '薪资理财卡', '16', '622620xxxxxxxxxx', '6', '622620', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('958', '民生银行', '借记卡普卡', '16', '621691xxxxxxxxxx', '6', '621691', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('959', '民生银行', '借记卡', '16', '623255xxxxxxxxxx', '6', '623255', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('960', '民生银行', '借记卡', '16', '900003xxxxxxxxxx', '6', '900003', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('961', '民生银行', '--', '16', '623258xxxxxxxxxx', '6', '623258', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('962', '民生银行', '民生银行银联借记卡银卡', '16', '623683xxxxxxxxxx', '6', '623683', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('1023', '广发银行', '广发理财通卡', '19', '622568xxxxxxxxxxxxx', '6', '622568', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('1025', '广发银行', '广发理财通(银联卡)', '19', '9111xxxxxxxxxxxxxxx', '4', '9111', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('1031', '广发银行', '理财通卡', '19', '621462xxxxxxxxxxxxx', '6', '621462', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('1039', '广发银行', '广发青年银行预付卡', '19', '620037xxxxxxxxxxxxx', '6', '620037', '预付费卡');
INSERT INTO `bankcardinfo` VALUES ('1041', '广发银行', '广发理财通', '19', '6858001xxxxxxxxxxxx', '7', '6858001', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('1042', '广发银行', '广发理财通', '19', '6858009xxxxxxxxxxxx', '7', '6858009', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('1043', '广发银行', '广发财富管理多币IC卡', '19', '623506xxxxxxxxxxxxx', '6', '623506', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('1044', '广发银行', '借记卡', '19', '623259xxxxxxxxxxxxx', '6', '623259', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('1045', '平安银行', '发展借记卡', '16', '412963xxxxxxxxxx', '6', '412963', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('1046', '平安银行', '国际借记卡', '16', '415752xxxxxxxxxx', '6', '415752', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('1047', '平安银行', '国际借记卡', '16', '415753xxxxxxxxxx', '6', '415753', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('1048', '平安银行', '发展卡(银联卡)', '16', '622538xxxxxxxxxx', '6', '622538', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('1049', '平安银行', '发展借记卡(银联卡)', '16', '998800xxxxxxxxxx', '6', '998800', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('1050', '平安银行', '发展借记卡', '16', '412962xxxxxxxxxx', '6', '412962', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('1051', '平安银行', '公益预付卡', '16', '620010xxxxxxxxxx', '6', '620010', '预付费卡');
INSERT INTO `bankcardinfo` VALUES ('1052', '平安银行', '聚财卡金卡', '16', '622535xxxxxxxxxx', '6', '622535', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('1053', '平安银行', '聚财卡VIP金卡', '16', '622536xxxxxxxxxx', '6', '622536', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('1054', '平安银行', '聚财卡白金卡和钻石卡', '16', '622539xxxxxxxxxx', '6', '622539', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('1055', '平安银行', '聚财卡钻石卡', '16', '622983xxxxxxxxxx', '6', '622983', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('1070', '招商银行', '招行一卡通', '15', '690755xxxxxxxxx', '6', '690755', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('1071', '招商银行', '一卡通(银联卡)', '16', '95555xxxxxxxxxxx', '5', '95555', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('1074', '招商银行', '两地一卡通', '16', '402658xxxxxxxxxx', '6', '402658', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('1075', '招商银行', '招行国际卡(银联卡)', '16', '410062xxxxxxxxxx', '6', '410062', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('1076', '招商银行', '招行国际卡(银联卡)', '16', '468203xxxxxxxxxx', '6', '468203', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('1078', '招商银行', '招行国际卡(银联卡)', '16', '512425xxxxxxxxxx', '6', '512425', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('1079', '招商银行', '招行国际卡(银联卡)', '16', '524011xxxxxxxxxx', '6', '524011', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('1087', '招商银行', '电子现金卡', '19', '620520xxxxxxxxxxxxx', '6', '620520', '预付费卡');
INSERT INTO `bankcardinfo` VALUES ('1088', '招商银行', '金葵花卡', '16', '621286xxxxxxxxxx', '6', '621286', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('1089', '招商银行', '银联IC普卡', '16', '621483xxxxxxxxxx', '6', '621483', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('1090', '招商银行', '银联IC金卡', '16', '621485xxxxxxxxxx', '6', '621485', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('1091', '招商银行', '银联金葵花IC卡', '16', '621486xxxxxxxxxx', '6', '621486', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('1094', '招商银行', '一卡通(银联卡)', '16', '622580xxxxxxxxxx', '6', '622580', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('1096', '招商银行', '一卡通(银联卡)', '16', '622588xxxxxxxxxx', '6', '622588', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('1097', '招商银行', '公司卡(银联卡)', '16', '622598xxxxxxxxxx', '6', '622598', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('1098', '招商银行', '金卡', '16', '622609xxxxxxxxxx', '6', '622609', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('1100', '招商银行', '招商银行钻石IC卡', '16', '623126xxxxxxxxxx', '6', '623126', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('1101', '招商银行', '招商银行私人银行IC卡', '16', '623136xxxxxxxxxx', '6', '623136', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('1102', '招商银行', '招商银行“公司一卡通”IC卡', '16', '623262xxxxxxxxxx', '6', '623262', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('1116', '兴业银行', '兴业卡', '16', '90592xxxxxxxxxxx', '5', '90592', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('1117', '兴业银行', '兴业卡(银联卡)', '18', '966666xxxxxxxxxxxx', '6', '966666', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('1118', '兴业银行', '兴业卡(银联标准卡)', '18', '622909xxxxxxxxxxxx', '6', '622909', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('1119', '兴业银行', '兴业自然人生理财卡', '18', '622908xxxxxxxxxxxx', '6', '622908', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('1120', '兴业银行', '兴业智能卡(银联卡)', '18', '438588xxxxxxxxxxxx', '6', '438588', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('1121', '兴业银行', '兴业智能卡', '18', '438589xxxxxxxxxxxx', '6', '438589', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('1169', '浦发银行', '东方轻松理财卡', '16', '622516xxxxxxxxxx', '6', '622516', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('1171', '浦发银行', '东方轻松理财卡', '16', '622518xxxxxxxxxx', '6', '622518', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('1173', '浦发银行', '东方卡(银联卡)', '16', '622521xxxxxxxxxx', '6', '622521', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('1174', '浦发银行', '东方卡(银联卡)', '16', '622522xxxxxxxxxx', '6', '622522', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('1175', '浦发银行', '东方卡(银联卡)', '16', '622523xxxxxxxxxx', '6', '622523', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('1177', '浦发银行', '东方卡', '16', '84301xxxxxxxxxxx', '5', '84301', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('1178', '浦发银行', '东方卡', '16', '84336xxxxxxxxxxx', '5', '84336', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('1179', '浦发银行', '东方卡', '16', '84373xxxxxxxxxxx', '5', '84373', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('1181', '浦发银行', '东方卡', '16', '84390xxxxxxxxxxx', '5', '84390', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('1182', '浦发银行', '东方卡', '16', '87000xxxxxxxxxxx', '5', '87000', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('1183', '浦发银行', '东方卡', '16', '87010xxxxxxxxxxx', '5', '87010', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('1184', '浦发银行', '东方卡', '16', '87030xxxxxxxxxxx', '5', '87030', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('1185', '浦发银行', '东方卡', '16', '87040xxxxxxxxxxx', '5', '87040', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('1186', '浦发银行', '东方卡', '16', '84380xxxxxxxxxxx', '5', '84380', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('1187', '浦发银行', '东方卡', '16', '984301xxxxxxxxxx', '6', '984301', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('1188', '浦发银行', '东方卡', '16', '984303xxxxxxxxxx', '6', '984303', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('1189', '浦发银行', '东方卡', '16', '84361xxxxxxxxxxx', '5', '84361', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('1190', '浦发银行', '东方卡', '16', '87050xxxxxxxxxxx', '5', '87050', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('1194', '浦发银行', '移动联名卡', '16', '621351xxxxxxxxxx', '6', '621351', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('1195', '浦发银行', '轻松理财消贷易卡', '16', '621390xxxxxxxxxx', '6', '621390', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('1196', '浦发银行', '轻松理财普卡（复合卡）', '16', '621792xxxxxxxxxx', '6', '621792', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('1199', '浦发银行', '东方借记卡（复合卡）', '16', '621791xxxxxxxxxx', '6', '621791', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('1200', '浦发银行', '东方卡', '16', '84342xxxxxxxxxxx', '5', '84342', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('1201', '浦东银行', '电子现金卡（IC卡）', '19', '620530xxxxxxxxxxxxx', '6', '620530', '预付费卡');
INSERT INTO `bankcardinfo` VALUES ('1204', '浦发银行', '轻松理财金卡（复合卡）', '16', '621793xxxxxxxxxx', '6', '621793', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('1205', '浦发银行', '轻松理财白金卡（复合卡）', '16', '621795xxxxxxxxxx', '6', '621795', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('1206', '浦发银行', '轻松理财钻石卡（复合卡）', '16', '621796xxxxxxxxxx', '6', '621796', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('1208', '浦发银行', '轻松理财米卡（复合卡）', '16', '623250xxxxxxxxxx', '6', '623250', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('1209', '浦发银行', '轻松理财普卡', '16', '621352xxxxxxxxxx', '6', '621352', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('1210', '浦发银行', '东方卡', '16', '84385xxxxxxxxxxx', '5', '84385', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('1225', '浙商银行', '商卡', '19', '621019xxxxxxxxxxxxx', '6', '621019', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('1228', '浙商银行', '商卡', '19', '6223091100xxxxxxxxx', '10', '6223091100', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('1229', '浙商银行', '商卡', '19', '6223092900xxxxxxxxx', '10', '6223092900', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('1230', '浙商银行', '商卡(银联卡)', '19', '6223093310xxxxxxxxx', '10', '6223093310', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('1231', '浙商银行', '商卡(银联卡)', '19', '6223093320xxxxxxxxx', '10', '6223093320', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('1232', '浙商银行', '商卡(银联卡)', '19', '6223093330xxxxxxxxx', '10', '6223093330', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('1233', '浙商银行', '商卡', '19', '6223093370xxxxxxxxx', '10', '6223093370', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('1234', '浙商银行', '商卡(银联卡)', '19', '6223093380xxxxxxxxx', '10', '6223093380', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('1235', '浙商银行', '商卡(银联卡)', '19', '6223096510xxxxxxxxx', '10', '6223096510', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('1236', '浙商银行', '商卡', '19', '6223097910xxxxxxxxx', '10', '6223097910', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('1237', '渤海银行', '浩瀚金卡', '16', '621268xxxxxxxxxx', '6', '621268', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('1238', '渤海银行', '渤海银行借记卡', '16', '622884xxxxxxxxxx', '6', '622884', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('1239', '渤海银行', '金融IC卡', '16', '621453xxxxxxxxxx', '6', '621453', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('1240', '渤海银行', '渤海银行公司借记卡', '16', '622684xxxxxxxxxx', '6', '622684', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('1242', '平安银行', '--', '19', '623269xxxxxxxxxxxxx', '6', '623269', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('1369', '平安银行', '新磁条借记卡', '19', '621626xxxxxxxxxxxxx', '6', '621626', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('1370', '平安银行', '平安银行IC借记卡', '19', '623058xxxxxxxxxxxxx', '6', '623058', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('1371', '平安银行', '万事顺卡', '16', '602907xxxxxxxxxx', '6', '602907', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('1372', '平安银行', '平安银行借记卡', '16', '622986xxxxxxxxxx', '6', '622986', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('1373', '平安银行', '平安银行借记卡', '16', '622989xxxxxxxxxx', '6', '622989', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('1374', '平安银行', '万事顺借记卡', '16', '622298xxxxxxxxxx', '6', '622298', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('1508', '徽商银行', '', '19', '622877xxxxxxxxxxxxx', '6', '622877', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('1509', '徽商银行', '黄山卡', '19', '622879xxxxxxxxxxxxx', '6', '622879', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('1510', '徽商银行', '黄山卡', '17', '603601xxxxxxxxxxx', '6', '603601', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('1511', '徽商银行', '借记卡', '19', '621775xxxxxxxxxxxxx', '6', '621775', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('1512', '徽商银行', '徽商银行中国旅游卡（安徽）', '19', '623203xxxxxxxxxxxxx', '6', '623203', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('1513', '徽商银行', '黄山卡(银联卡)', '17', '622137xxxxxxxxxxx', '6', '622137', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('1514', '徽商银行', '黄山卡(银联卡)', '17', '622327xxxxxxxxxxx', '6', '622327', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('1515', '徽商银行', '黄山卡(银联卡)', '17', '622340xxxxxxxxxxx', '6', '622340', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('1516', '徽商银行', '黄山卡(银联卡)', '17', '622366xxxxxxxxxxx', '6', '622366', '借记卡');
INSERT INTO `bankcardinfo` VALUES ('3790', '徽商银行', '云逸卡', '19', '623664xxxxxxxxxxxxx', '6', '623664', '借记卡');

-- ----------------------------
-- Table structure for `bankcreditlogs`
-- ----------------------------
DROP TABLE IF EXISTS `bankcreditlogs`;
CREATE TABLE `bankcreditlogs` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `bankname` varchar(100) NOT NULL,
  `createtime` datetime DEFAULT NULL,
  `type` varchar(20) NOT NULL,
  `credit` double(24,2) NOT NULL DEFAULT '0.00',
  `newCredit` double(24,2) NOT NULL DEFAULT '0.00',
  `remit` double(24,2) NOT NULL DEFAULT '0.00',
  `remark` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`) USING BTREE,
  UNIQUE KEY `union` (`createtime`,`bankname`,`credit`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of bankcreditlogs
-- ----------------------------

-- ----------------------------
-- Table structure for `bankinfo`
-- ----------------------------
DROP TABLE IF EXISTS `bankinfo`;
CREATE TABLE `bankinfo` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) DEFAULT NULL,
  `type` int(1) DEFAULT '0' COMMENT '功能分类,1是存款账户，2是提款账户',
  `bankname` varchar(40) DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  `useable` int(1) DEFAULT '0',
  `amount` double(24,2) DEFAULT '0.00',
  `vpnname` varchar(50) DEFAULT NULL,
  `vpnpassword` varchar(50) DEFAULT NULL,
  `accountno` varchar(50) DEFAULT NULL,
  `loginname` varchar(50) DEFAULT NULL,
  `password` varchar(50) DEFAULT NULL,
  `userrole` varchar(50) DEFAULT NULL,
  `isshow` int(1) DEFAULT '0',
  `banktype` int(1) DEFAULT '0' COMMENT '0表示内部账号 1表示外部账号',
  `bankcard` varchar(255) DEFAULT NULL,
  `usb` varchar(255) DEFAULT NULL,
  `realname` varchar(255) DEFAULT NULL,
  `updatetime` datetime DEFAULT NULL,
  `bankamount` double DEFAULT '0',
  `remoteip` varchar(255) DEFAULT NULL,
  `istransfer` int(11) DEFAULT '0' COMMENT '0:关闭转账 1：开启转账',
  `isactive` int(10) DEFAULT '0' COMMENT '0.不活跃1.活跃',
  `transferswitch` int(10) DEFAULT '0' COMMENT '开启转账 1.开启  0.关闭',
  `samebankswitch` int(10) DEFAULT '0' COMMENT '开启同行 1.开启  0.关闭',
  `samebank` varchar(255) DEFAULT NULL COMMENT '同行银行',
  `crossbank` varchar(255) DEFAULT NULL COMMENT '跨行银行',
  `transfermoney` double(20,2) DEFAULT '0.00' COMMENT '转账额度',
  `autopay` int(10) DEFAULT '0' COMMENT '自动付款1.是0.否',
  `fee` double(20,2) DEFAULT '0.00' COMMENT '手续费',
  `zfbImgCode` varchar(1000) DEFAULT NULL,
  `scanAccount` int(10) DEFAULT '0' COMMENT '0.不是扫描账号1.扫描账号',
  `paytype` int(2) DEFAULT '0' COMMENT '支付宝 or 网银',
  `depositMin` double(24,2) DEFAULT '0.00',
  `depositMax` double(24,2) DEFAULT '0.00',
  PRIMARY KEY (`Id`),
  UNIQUE KEY `loginname` (`loginname`) USING BTREE,
  KEY `username` (`username`) USING BTREE,
  KEY `type` (`type`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of bankinfo
-- ----------------------------

-- ----------------------------
-- Table structure for `banktransfercons`
-- ----------------------------
DROP TABLE IF EXISTS `banktransfercons`;
CREATE TABLE `banktransfercons` (
  `pno` varchar(20) NOT NULL,
  `title` varchar(20) NOT NULL,
  `loginname` varchar(20) NOT NULL,
  `payType` varchar(20) DEFAULT NULL,
  `firstCash` double(24,2) NOT NULL,
  `tryCredit` double(24,2) NOT NULL,
  `remark` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`pno`),
  UNIQUE KEY `pno` (`pno`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of banktransfercons
-- ----------------------------

-- ----------------------------
-- Table structure for `betrank`
-- ----------------------------
DROP TABLE IF EXISTS `betrank`;
CREATE TABLE `betrank` (
  `sumbet` double(10,2) DEFAULT NULL,
  `loginname` varchar(20) DEFAULT NULL,
  `type` varchar(5) DEFAULT NULL,
  `lastarea` varchar(20) DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  `id` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of betrank
-- ----------------------------

-- ----------------------------
-- Table structure for `betrecords`
-- ----------------------------
DROP TABLE IF EXISTS `betrecords`;
CREATE TABLE `betrecords` (
  `billNo` varchar(20) NOT NULL DEFAULT '',
  `billTime` datetime NOT NULL,
  `gmCode` varchar(10) DEFAULT NULL,
  `passport` varchar(16) DEFAULT NULL,
  `drawNo` varchar(20) DEFAULT NULL,
  `playCode` varchar(20) DEFAULT NULL,
  `billAmount` double(24,2) DEFAULT NULL,
  `result` double(24,2) DEFAULT NULL,
  PRIMARY KEY (`billNo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of betrecords
-- ----------------------------

-- ----------------------------
-- Table structure for `bg_data`
-- ----------------------------
DROP TABLE IF EXISTS `bg_data`;
CREATE TABLE `bg_data` (
  `billNo` varchar(50) NOT NULL COMMENT '游戏平台记录id',
  `account` varchar(50) DEFAULT NULL COMMENT '会员名称',
  `gameType` varchar(255) DEFAULT NULL COMMENT '下注游戏类型',
  `betAmount` double(20,3) DEFAULT '0.000' COMMENT '下注额',
  `validAmount` double(20,3) DEFAULT '0.000' COMMENT '有效投注额',
  `winAmount` double(20,3) DEFAULT '0.000' COMMENT '输赢',
  `betTime` timestamp NULL DEFAULT NULL COMMENT '下注时间',
  `settle` int(11) DEFAULT '0' COMMENT '-1撒消,0未结算,1已结算',
  `gameKind` varchar(30) DEFAULT NULL COMMENT '游戏种类',
  `firstKind` varchar(20) DEFAULT NULL COMMENT '一级分类',
  `addTime` timestamp NULL DEFAULT NULL COMMENT '时间',
  `gmtBetTime` timestamp NULL DEFAULT NULL COMMENT '下注时间转北京时间',
  `statTime` date DEFAULT NULL COMMENT '报表统计时间',
  `updateTime` timestamp NULL DEFAULT NULL COMMENT '修改时间',
  `amesTime` timestamp NULL DEFAULT NULL COMMENT '美东时间',
  `rebateTime` date DEFAULT NULL COMMENT '返水统计时间',
  `betContent` text COMMENT '投注内容',
  PRIMARY KEY (`billNo`),
  UNIQUE KEY `index_bill_no` (`billNo`),
  KEY `idx_account` (`account`,`gmtBetTime`),
  KEY `idx_ames_time` (`amesTime`),
  KEY `idx_bet_time` (`betTime`),
  KEY `idx_gmt_bet_time` (`gmtBetTime`),
  KEY `idx_rebate_time` (`rebateTime`,`settle`,`account`,`gameKind`),
  KEY `idx_stat_time` (`statTime`,`settle`,`account`,`gameKind`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of bg_data
-- ----------------------------
INSERT INTO `bg_data` VALUES ('7231989322', 'tw_live_kavin998', '1', '60.000', '0.000', '0.000', '2020-10-01 06:07:32', '1', '下注庄赢', 'LIVE', '2020-10-01 18:15:00', '2020-10-01 18:07:32', '2020-10-01', '2020-10-01 18:15:00', '2020-10-01 06:07:32', '2020-10-01', null);
INSERT INTO `bg_data` VALUES ('7231992284', 'tw_live_kavin998', '1', '70.000', '70.000', '70.000', '2020-10-01 06:08:36', '1', '下注庄赢', 'LIVE', '2020-10-01 18:15:00', '2020-10-01 18:08:36', '2020-10-01', '2020-10-01 18:15:00', '2020-10-01 06:08:36', '2020-10-01', null);
INSERT INTO `bg_data` VALUES ('7232003708', 'tw_live_kavin998', '1', '50.000', '47.500', '-47.500', '2020-10-01 06:09:48', '1', '下注庄赢', 'LIVE', '2020-10-01 18:15:00', '2020-10-01 18:09:48', '2020-10-01', '2020-10-01 18:15:00', '2020-10-01 06:09:48', '2020-10-01', null);

-- ----------------------------
-- Table structure for `businessproposal`
-- ----------------------------
DROP TABLE IF EXISTS `businessproposal`;
CREATE TABLE `businessproposal` (
  `pno` varchar(20) NOT NULL,
  `proposer` varchar(20) NOT NULL,
  `createTime` datetime NOT NULL,
  `type` int(3) NOT NULL,
  `amount` double(24,2) DEFAULT NULL,
  `flag` int(1) NOT NULL DEFAULT '0',
  `whereisfrom` varchar(10) NOT NULL DEFAULT '后台',
  `remark` varchar(500) DEFAULT NULL,
  `bankaccount` varchar(30) DEFAULT NULL,
  `depositbank` varchar(30) DEFAULT NULL COMMENT '收款人银行',
  `attachment` varchar(30) DEFAULT NULL COMMENT '放置附件的名称',
  `depositname` varchar(30) DEFAULT NULL COMMENT '收款人名字',
  `depositaccount` varchar(30) DEFAULT NULL COMMENT '收款人帐号',
  `excattachment` varchar(30) DEFAULT NULL,
  `bankaccountid` int(11) DEFAULT NULL,
  `actualmoney` double(24,2) DEFAULT '0.00',
  `belong` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`pno`),
  KEY `proposer` (`proposer`) USING BTREE,
  KEY `type` (`type`) USING BTREE,
  KEY `flag` (`flag`) USING BTREE,
  KEY `createTime` (`createTime`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of businessproposal
-- ----------------------------

-- ----------------------------
-- Table structure for `b_red_envelope_activity`
-- ----------------------------
DROP TABLE IF EXISTS `b_red_envelope_activity`;
CREATE TABLE `b_red_envelope_activity` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '编号，主键，自动增长',
  `title` varchar(50) NOT NULL COMMENT '活动标题',
  `min_bonus` double(10,2) NOT NULL COMMENT '活动最小红利',
  `max_bonus` double(10,2) NOT NULL COMMENT '活动最大红利',
  `vip` varchar(50) NOT NULL COMMENT '等级',
  `times` int(10) NOT NULL COMMENT '领取次数',
  `platform_id` varchar(20) NOT NULL COMMENT '转入平台编号',
  `platform_name` varchar(50) NOT NULL COMMENT '转入平台名称',
  `multiples` int(10) DEFAULT NULL COMMENT '流水倍数',
  `deposit_amount` double(10,2) DEFAULT NULL COMMENT '存款额',
  `deposit_start_time` datetime DEFAULT NULL COMMENT '存款开始时间',
  `deposit_end_time` datetime DEFAULT NULL COMMENT '存款结束时间',
  `bet_amount` double(10,2) DEFAULT NULL COMMENT '投注额',
  `bet_start_time` datetime DEFAULT NULL COMMENT '投注开始时间',
  `bet_end_time` datetime DEFAULT NULL COMMENT '投注结束时间',
  `start_time` datetime NOT NULL COMMENT '活动开始时间',
  `end_time` datetime NOT NULL COMMENT '活动结束时间',
  `delete_flag` char(1) NOT NULL COMMENT '删除标志，Y:已删除/N:未删除',
  `create_user` varchar(20) NOT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_user` varchar(20) NOT NULL COMMENT '修改人',
  `update_time` datetime NOT NULL COMMENT '修改时间',
  `machineCode` int(11) DEFAULT '0',
  `remark` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of b_red_envelope_activity
-- ----------------------------

-- ----------------------------
-- Table structure for `call_config`
-- ----------------------------
DROP TABLE IF EXISTS `call_config`;
CREATE TABLE `call_config` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `company` varchar(25) NOT NULL,
  `url` varchar(255) NOT NULL,
  `serviceid` varchar(255) DEFAULT NULL,
  `flag` int(10) DEFAULT '0' COMMENT '0.未开启 1.开启',
  `createtime` datetime DEFAULT NULL,
  `updatetime` datetime DEFAULT NULL,
  `type` int(5) NOT NULL DEFAULT '0' COMMENT '是否强制用注册的手机号0.不用1.使用',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of call_config
-- ----------------------------

-- ----------------------------
-- Table structure for `call_info`
-- ----------------------------
DROP TABLE IF EXISTS `call_info`;
CREATE TABLE `call_info` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `caller` varchar(50) DEFAULT NULL,
  `called` varchar(50) DEFAULT NULL,
  `begin_time` varchar(50) DEFAULT NULL,
  `end_time` varchar(50) DEFAULT NULL,
  `duration` varchar(50) DEFAULT NULL,
  `record_path` varchar(200) DEFAULT NULL,
  `call_type` varchar(50) DEFAULT NULL,
  `trunk` varchar(255) DEFAULT NULL,
  `uuid` varchar(100) DEFAULT NULL,
  `hangupcause` varchar(255) DEFAULT NULL,
  `createtime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=2972228 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of call_info
-- ----------------------------

-- ----------------------------
-- Table structure for `cashin`
-- ----------------------------
DROP TABLE IF EXISTS `cashin`;
CREATE TABLE `cashin` (
  `pno` varchar(20) NOT NULL,
  `title` varchar(20) NOT NULL,
  `loginname` varchar(20) NOT NULL,
  `aliasName` varchar(20) DEFAULT NULL,
  `money` double(24,2) NOT NULL,
  `accountNo` varchar(50) DEFAULT NULL,
  `corpBankName` varchar(50) DEFAULT NULL,
  `remark` varchar(500) DEFAULT NULL,
  `cashintime` datetime DEFAULT NULL,
  `fee` double(8,2) DEFAULT '0.00' COMMENT '手续费',
  PRIMARY KEY (`pno`),
  UNIQUE KEY `pno` (`pno`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of cashin
-- ----------------------------

-- ----------------------------
-- Table structure for `cashinrecord`
-- ----------------------------
DROP TABLE IF EXISTS `cashinrecord`;
CREATE TABLE `cashinrecord` (
  `pno` varchar(20) NOT NULL DEFAULT '',
  `code` varchar(20) DEFAULT NULL,
  `loginname` varchar(20) DEFAULT NULL,
  `money` double(24,2) DEFAULT NULL,
  `createtime` datetime DEFAULT NULL,
  `flag` int(1) DEFAULT '0',
  `bankaccount` varchar(30) DEFAULT NULL,
  `saveway` varchar(30) DEFAULT NULL,
  `bankname` varchar(30) DEFAULT NULL,
  `operator` varchar(30) DEFAULT NULL,
  `aliasName` varchar(20) DEFAULT NULL,
  `remark` varchar(150) DEFAULT NULL,
  PRIMARY KEY (`pno`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of cashinrecord
-- ----------------------------

-- ----------------------------
-- Table structure for `cashout`
-- ----------------------------
DROP TABLE IF EXISTS `cashout`;
CREATE TABLE `cashout` (
  `pno` varchar(20) NOT NULL,
  `title` varchar(20) NOT NULL,
  `loginname` varchar(20) NOT NULL,
  `money` double(24,2) NOT NULL,
  `accountName` varchar(50) NOT NULL,
  `accountType` varchar(20) NOT NULL,
  `accountCity` varchar(50) NOT NULL,
  `bankAddress` varchar(100) NOT NULL,
  `accountNo` varchar(32) NOT NULL,
  `bank` varchar(50) NOT NULL,
  `phone` varchar(30) DEFAULT NULL,
  `email` varchar(50) DEFAULT NULL,
  `ip` varchar(16) DEFAULT NULL,
  `remark` varchar(500) DEFAULT NULL,
  `notifyNote` varchar(10) DEFAULT NULL,
  `notifyPhone` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`pno`),
  UNIQUE KEY `pno` (`pno`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of cashout
-- ----------------------------

-- ----------------------------
-- Table structure for `change_line_user`
-- ----------------------------
DROP TABLE IF EXISTS `change_line_user`;
CREATE TABLE `change_line_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `changelinetime` datetime DEFAULT NULL,
  `username` varchar(100) DEFAULT '',
  `codeafter` varchar(32) DEFAULT '',
  `codebefore` varchar(32) DEFAULT '',
  `createTime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of change_line_user
-- ----------------------------

-- ----------------------------
-- Table structure for `chess_data`
-- ----------------------------
DROP TABLE IF EXISTS `chess_data`;
CREATE TABLE `chess_data` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `acc` varchar(255) DEFAULT NULL COMMENT '玩家账号',
  `logid` int(11) DEFAULT NULL,
  `ctime` datetime DEFAULT NULL COMMENT '创建时间',
  `sessionid` varchar(64) DEFAULT NULL COMMENT '记录单号',
  `uid` int(11) DEFAULT NULL COMMENT '用户ID',
  `kind` varchar(255) DEFAULT NULL COMMENT '类型',
  `prev` int(11) DEFAULT NULL COMMENT '变化之前携带金额',
  `chg` int(11) DEFAULT NULL COMMENT '本次操作的变化金额',
  `leftamount` int(11) DEFAULT NULL COMMENT '操作之后剩余金额',
  `allput` int(11) DEFAULT NULL COMMENT '总投注',
  `realput` int(11) DEFAULT NULL COMMENT '有效投注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of chess_data
-- ----------------------------

-- ----------------------------
-- Table structure for `cmb_transfers`
-- ----------------------------
DROP TABLE IF EXISTS `cmb_transfers`;
CREATE TABLE `cmb_transfers` (
  `transfer_id` int(11) NOT NULL AUTO_INCREMENT,
  `amount` decimal(14,4) unsigned NOT NULL COMMENT '充值金额',
  `balance` decimal(14,4) unsigned NOT NULL DEFAULT '0.0000' COMMENT '银行额度',
  `jylx` varchar(10) DEFAULT NULL,
  `notes` varchar(150) NOT NULL DEFAULT '' COMMENT '附言',
  `accept_name` varchar(50) NOT NULL DEFAULT '',
  `accept_card_num` varchar(19) NOT NULL DEFAULT '',
  `pay_date` datetime NOT NULL COMMENT '付款时间',
  `admin_id` smallint(5) unsigned NOT NULL COMMENT '管理员ID',
  `status` tinyint(3) unsigned NOT NULL COMMENT '0未处理 1已充值 2充值超时处理',
  `date` datetime NOT NULL COMMENT '记录添加时间',
  `timecha` varchar(50) DEFAULT NULL,
  `overtime` int(1) DEFAULT '0',
  `loginname` varchar(100) DEFAULT NULL,
  `remark` varchar(100) DEFAULT NULL,
  `uaccountname` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`transfer_id`),
  UNIQUE KEY `aab` (`accept_card_num`,`amount`,`balance`,`pay_date`) USING BTREE,
  KEY `status` (`status`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='转账记录表';

-- ----------------------------
-- Records of cmb_transfers
-- ----------------------------

-- ----------------------------
-- Table structure for `collectpromo`
-- ----------------------------
DROP TABLE IF EXISTS `collectpromo`;
CREATE TABLE `collectpromo` (
  `pno` varchar(20) NOT NULL COMMENT '提案号',
  `username` varchar(16) NOT NULL COMMENT '用户名',
  `amount` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '输赢额',
  `times` int(255) DEFAULT '10' COMMENT '提款所需流水倍数',
  `status` char(1) NOT NULL DEFAULT '0' COMMENT '状态（0：未领取   1：已领取  2：已处理  3:已取消）',
  `betting` decimal(10,2) DEFAULT NULL COMMENT '已投注额(0点到领取优惠时PT游戏总投注额)',
  `creattime` datetime DEFAULT NULL COMMENT '创建时间',
  `platform` varchar(16) NOT NULL COMMENT ' 游戏平台',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `collectID` int(255) DEFAULT NULL,
  PRIMARY KEY (`pno`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of collectpromo
-- ----------------------------

-- ----------------------------
-- Table structure for `commissionrecords`
-- ----------------------------
DROP TABLE IF EXISTS `commissionrecords`;
CREATE TABLE `commissionrecords` (
  `loginname` varchar(20) NOT NULL COMMENT '合作伙伴账号',
  `parent` varchar(20) NOT NULL COMMENT '代理',
  `cashinAmount` double(24,2) NOT NULL COMMENT '存取款总数',
  `cashoutAmount` double(24,2) NOT NULL,
  `firstDepositAmount` double(24,2) NOT NULL,
  `ximaAmount` double(24,2) NOT NULL COMMENT '结算结果',
  `otherAmount` double(24,2) NOT NULL,
  `year` int(4) NOT NULL COMMENT '年份',
  `month` int(2) NOT NULL COMMENT '月份',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `agAmount` double(24,2) DEFAULT '0.00',
  `sixLotteryBet` double(24,2) DEFAULT '0.00',
  `win` double(24,2) DEFAULT '0.00',
  PRIMARY KEY (`loginname`,`year`,`month`),
  KEY `subLoginname` (`parent`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of commissionrecords
-- ----------------------------

-- ----------------------------
-- Table structure for `commissions`
-- ----------------------------
DROP TABLE IF EXISTS `commissions`;
CREATE TABLE `commissions` (
  `loginname` varchar(20) CHARACTER SET utf8 NOT NULL COMMENT '合作伙伴账号',
  `year` int(4) NOT NULL COMMENT '年份',
  `month` int(2) NOT NULL COMMENT '月份',
  `flag` int(1) NOT NULL DEFAULT '1' COMMENT '状徿',
  `subCount` int(11) NOT NULL COMMENT '下级个数',
  `eaProfitAmount` double(24,2) NOT NULL,
  `amount` double(24,2) DEFAULT NULL,
  `createTime` datetime DEFAULT NULL,
  `excuteTime` datetime DEFAULT NULL,
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `activeuser` int(11) DEFAULT '0' COMMENT '活跃会员数',
  `crate` double(12,2) DEFAULT '0.30',
  PRIMARY KEY (`loginname`,`year`,`month`),
  KEY `flag` (`flag`) USING BTREE,
  KEY `year` (`year`) USING BTREE,
  KEY `month` (`month`) USING BTREE,
  KEY `loginname` (`loginname`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of commissions
-- ----------------------------

-- ----------------------------
-- Table structure for `concert`
-- ----------------------------
DROP TABLE IF EXISTS `concert`;
CREATE TABLE `concert` (
  `id` int(2) NOT NULL AUTO_INCREMENT,
  `loginname` varchar(50) DEFAULT NULL,
  `startTime` datetime DEFAULT NULL,
  `endTime` datetime DEFAULT NULL,
  `bet` double(20,2) DEFAULT NULL,
  `createTime` datetime DEFAULT NULL,
  `lastTime` datetime DEFAULT NULL,
  `active` int(2) DEFAULT NULL,
  `display` int(2) DEFAULT NULL,
  `round` int(2) DEFAULT NULL,
  `type` int(2) DEFAULT NULL,
  `ranking` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`) USING BTREE
) ENGINE=MyISAM AUTO_INCREMENT=95 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of concert
-- ----------------------------

-- ----------------------------
-- Table structure for `concessions`
-- ----------------------------
DROP TABLE IF EXISTS `concessions`;
CREATE TABLE `concessions` (
  `pno` varchar(20) NOT NULL,
  `title` varchar(20) NOT NULL,
  `loginname` varchar(20) NOT NULL,
  `payType` varchar(20) DEFAULT NULL,
  `firstCash` double(24,2) NOT NULL,
  `tryCredit` double(24,2) NOT NULL,
  `remark` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`pno`),
  KEY `pno` (`pno`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of concessions
-- ----------------------------

-- ----------------------------
-- Table structure for `config`
-- ----------------------------
DROP TABLE IF EXISTS `config`;
CREATE TABLE `config` (
  `configid` int(4) unsigned NOT NULL AUTO_INCREMENT COMMENT '系统配置ID',
  `parentid` int(4) unsigned NOT NULL DEFAULT '0' COMMENT '配置项父ID',
  `configkey` varchar(30) NOT NULL COMMENT '系统配置名称',
  `configvalue` varchar(500) NOT NULL COMMENT '系统配置值',
  `defaultvalue` varchar(255) NOT NULL COMMENT '系统配置默认值',
  `configvaluetype` varchar(10) NOT NULL COMMENT '系统配置数据类型',
  `forminputtype` varchar(10) NOT NULL DEFAULT 'input' COMMENT '表单提交类型',
  `channelid` tinyint(2) unsigned NOT NULL DEFAULT '0' COMMENT '栏目ID',
  `title` varchar(255) NOT NULL COMMENT '配置标题',
  `description` varchar(255) NOT NULL DEFAULT '' COMMENT '配置描述',
  `isdisabled` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '配置项是否禁用',
  PRIMARY KEY (`configid`),
  KEY `idx_disable` (`isdisabled`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统配置';

-- ----------------------------
-- Records of config
-- ----------------------------

-- ----------------------------
-- Table structure for `const`
-- ----------------------------
DROP TABLE IF EXISTS `const`;
CREATE TABLE `const` (
  `id` varchar(50) NOT NULL,
  `value` varchar(100) NOT NULL,
  `shopno` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of const
-- ----------------------------
INSERT INTO `const` VALUES ('761游戏', '1', '');
INSERT INTO `const` VALUES ('AGIN游戏', '1', null);
INSERT INTO `const` VALUES ('bbinele', '1', null);
INSERT INTO `const` VALUES ('BBIN游戏', '1', '');
INSERT INTO `const` VALUES ('BG视讯', '1', null);
INSERT INTO `const` VALUES ('cq9', '1', null);
INSERT INTO `const` VALUES ('CQ9游戏', '1', null);
INSERT INTO `const` VALUES ('dt', '1', null);
INSERT INTO `const` VALUES ('DT游戏', '1', null);
INSERT INTO `const` VALUES ('mg', '1', null);
INSERT INTO `const` VALUES ('MG游戏', '1', null);
INSERT INTO `const` VALUES ('pg', '1', null);
INSERT INTO `const` VALUES ('PG游戏', '1', null);
INSERT INTO `const` VALUES ('png', '1', null);
INSERT INTO `const` VALUES ('PNG游戏', '1', '');
INSERT INTO `const` VALUES ('PT8元自助优惠', '1', null);
INSERT INTO `const` VALUES ('pttiger', '1', null);
INSERT INTO `const` VALUES ('qt', '1', null);
INSERT INTO `const` VALUES ('SBA体育', '1', null);
INSERT INTO `const` VALUES ('sw', '1', null);
INSERT INTO `const` VALUES ('SW游戏', '1', null);
INSERT INTO `const` VALUES ('ttg', '1', null);
INSERT INTO `const` VALUES ('代付', '1', null);
INSERT INTO `const` VALUES ('体验金周赛', '1', null);
INSERT INTO `const` VALUES ('体验金短信', '1', null);
INSERT INTO `const` VALUES ('信付通代付', '1', null);
INSERT INTO `const` VALUES ('捕鱼大师', '1', null);
INSERT INTO `const` VALUES ('短信反转验证', '0', null);
INSERT INTO `const` VALUES ('自助体验金', '1', null);
INSERT INTO `const` VALUES ('西游捕鱼', '1', null);
INSERT INTO `const` VALUES ('转账AGIN', '1', null);
INSERT INTO `const` VALUES ('转账BBIN', '1', null);
INSERT INTO `const` VALUES ('转账BG', '1', null);
INSERT INTO `const` VALUES ('转账CHESS', '1', '');
INSERT INTO `const` VALUES ('转账CQ9', '1', null);
INSERT INTO `const` VALUES ('转账DT', '1', null);
INSERT INTO `const` VALUES ('转账MG', '1', null);
INSERT INTO `const` VALUES ('转账NEWPT', '1', null);
INSERT INTO `const` VALUES ('转账PG', '1', null);
INSERT INTO `const` VALUES ('转账PNG', '1', null);
INSERT INTO `const` VALUES ('转账QT', '1', null);
INSERT INTO `const` VALUES ('转账SBA', '1', null);
INSERT INTO `const` VALUES ('转账SW', '1', null);
INSERT INTO `const` VALUES ('转账TTG', '1', '');
INSERT INTO `const` VALUES ('邮箱', '0', '');
INSERT INTO `const` VALUES ('银行秒付(后台)', '1', null);

-- ----------------------------
-- Table structure for `constraint_address_config`
-- ----------------------------
DROP TABLE IF EXISTS `constraint_address_config`;
CREATE TABLE `constraint_address_config` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号，主键，自动增长',
  `type_id` varchar(2) NOT NULL COMMENT '类型ID',
  `type_name` varchar(20) NOT NULL COMMENT '类型名称',
  `value` varchar(20) NOT NULL COMMENT 'IP地址/禁用地区',
  `delete_flag` char(1) NOT NULL COMMENT '删除标志，0:已删除 1:未删除，默认为1',
  `created_user` varchar(20) NOT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `updated_user` varchar(20) NOT NULL COMMENT '修改人',
  `update_time` datetime NOT NULL COMMENT '修改时间',
  `remark` varchar(155) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of constraint_address_config
-- ----------------------------
INSERT INTO `constraint_address_config` VALUES ('6', '3', '拒绝地区', '香港', '1', 'wind', '2018-12-24 16:27:37', 'wind', '2018-12-24 16:27:37', '');

-- ----------------------------
-- Table structure for `coupon_config`
-- ----------------------------
DROP TABLE IF EXISTS `coupon_config`;
CREATE TABLE `coupon_config` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '编号，主键，自动增长',
  `platform_id` varchar(20) DEFAULT NULL COMMENT '游戏平台编号',
  `platform_name` varchar(50) DEFAULT NULL COMMENT '游戏平台名称',
  `coupon_type` varchar(10) NOT NULL COMMENT '优惠券类型(419:红包优惠券/319:存送优惠券)',
  `coupon_code` varchar(100) NOT NULL COMMENT '优惠券代码',
  `percent` double(10,2) DEFAULT NULL COMMENT '赠送百分比',
  `gift_amount` double(10,2) DEFAULT NULL COMMENT '赠送金额',
  `bet_multiples` int(10) NOT NULL COMMENT '流水倍数',
  `min_amount` double(10,2) DEFAULT NULL COMMENT '最低转账金额',
  `max_amount` double(10,2) DEFAULT NULL COMMENT '最高转账金额',
  `limit_money` double(10,2) DEFAULT NULL COMMENT '赠送金额上限',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `status` varchar(10) NOT NULL COMMENT '状态(0:待审核/1:已审核/2:已领取)',
  `is_delete` char(1) NOT NULL COMMENT '是否已删除(Y:是/N:否)',
  `receive_time` datetime DEFAULT NULL COMMENT '领取时间',
  `login_name` varchar(50) DEFAULT NULL COMMENT '领取账号',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `create_user` varchar(50) NOT NULL COMMENT '创建人',
  `delete_time` datetime DEFAULT NULL COMMENT '删除时间',
  `delete_user` varchar(50) DEFAULT NULL COMMENT '删除人',
  `audit_time` datetime DEFAULT NULL COMMENT '审核时间',
  `audit_user` varchar(50) DEFAULT NULL COMMENT '审核人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of coupon_config
-- ----------------------------

-- ----------------------------
-- Table structure for `cpu_compare`
-- ----------------------------
DROP TABLE IF EXISTS `cpu_compare`;
CREATE TABLE `cpu_compare` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `username` varchar(32) NOT NULL COMMENT '用户名',
  `ip` varchar(64) DEFAULT NULL COMMENT 'ip',
  `iesnarecpu` varchar(32) DEFAULT NULL COMMENT 'iesnare设备信息',
  `ourcpu` varchar(32) DEFAULT NULL COMMENT '我们采集的设备信息',
  `createTime` datetime DEFAULT NULL COMMENT '创建时间',
  `remark` varchar(32) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=127550 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of cpu_compare
-- ----------------------------

-- ----------------------------
-- Table structure for `cq_data`
-- ----------------------------
DROP TABLE IF EXISTS `cq_data`;
CREATE TABLE `cq_data` (
  `billNo` varchar(32) NOT NULL COMMENT '流水号',
  `gameCode` varchar(32) DEFAULT NULL COMMENT '游戏编码',
  `playName` varchar(32) DEFAULT NULL COMMENT '玩家账号',
  `betAmount` double(24,2) DEFAULT NULL COMMENT '投注额',
  `validBetAmount` double(24,2) DEFAULT NULL COMMENT '有效投注额',
  `returnAmount` double(24,2) DEFAULT NULL COMMENT '结算金额',
  `balance` double(24,2) DEFAULT NULL COMMENT '余额',
  `gameType` varchar(32) DEFAULT NULL COMMENT '游戏类型',
  `deviceType` varchar(8) DEFAULT NULL COMMENT '设备类型(web或mobile)',
  `betTime` datetime DEFAULT NULL COMMENT '下注时间',
  `platform` varchar(16) DEFAULT NULL COMMENT '平台',
  `settleTime` datetime DEFAULT NULL COMMENT '派彩时间',
  `createTime` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`billNo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of cq_data
-- ----------------------------
INSERT INTO `cq_data` VALUES ('3565116900', '186', 'kavin998', '15.00', '15.00', '204000.00', '408170.00', 'slot', 'mobile', '2020-09-20 22:06:37', 'cq9', '2020-09-20 22:08:50', '2020-09-21 12:33:00');
INSERT INTO `cq_data` VALUES ('3566297468', '186', 'austin998', '15.00', '15.00', '204000.00', '204044.00', 'slot', 'mobile', '2020-09-21 13:12:10', 'cq9', '2020-09-21 13:14:46', '2020-09-21 13:18:00');
INSERT INTO `cq_data` VALUES ('3566615507', '31', 'austin998', '17.60', '17.60', '40.00', '204071.40', 'slot', 'mobile', '2020-09-21 17:57:01', 'cq9', '2020-09-21 17:57:11', '2020-09-21 18:03:00');
INSERT INTO `cq_data` VALUES ('3566615680', '31', 'austin998', '17.60', '17.60', '1558.00', '205611.80', 'slot', 'mobile', '2020-09-21 17:57:12', 'cq9', '2020-09-21 17:58:10', '2020-09-21 18:06:00');
INSERT INTO `cq_data` VALUES ('3566800052', '9', 'austin998', '17.60', '17.60', '0.00', '205594.20', 'slot', 'mobile', '2020-09-21 21:04:15', 'cq9', '2020-09-21 21:04:23', '2020-09-21 21:12:00');
INSERT INTO `cq_data` VALUES ('3566800810', '9', 'austin998', '17.60', '17.60', '0.00', '205576.60', 'slot', 'mobile', '2020-09-21 21:05:01', 'cq9', '2020-09-21 21:05:04', '2020-09-21 21:12:00');
INSERT INTO `cq_data` VALUES ('3566800881', '9', 'austin998', '17.60', '17.60', '0.00', '205559.00', 'slot', 'mobile', '2020-09-21 21:05:06', 'cq9', '2020-09-21 21:05:09', '2020-09-21 21:12:00');
INSERT INTO `cq_data` VALUES ('3566800984', '9', 'austin998', '17.60', '17.60', '0.00', '205541.40', 'slot', 'mobile', '2020-09-21 21:05:13', 'cq9', '2020-09-21 21:05:15', '2020-09-21 21:12:00');
INSERT INTO `cq_data` VALUES ('3566801081', '9', 'austin998', '17.60', '17.60', '0.00', '205523.80', 'slot', 'mobile', '2020-09-21 21:05:18', 'cq9', '2020-09-21 21:05:20', '2020-09-21 21:12:00');
INSERT INTO `cq_data` VALUES ('3566801273', '9', 'austin998', '264.00', '264.00', '0.00', '205259.80', 'slot', 'mobile', '2020-09-21 21:05:28', 'cq9', '2020-09-21 21:05:31', '2020-09-21 21:12:00');
INSERT INTO `cq_data` VALUES ('3566801360', '9', 'austin998', '264.00', '264.00', '600.00', '205595.80', 'slot', 'mobile', '2020-09-21 21:05:33', 'cq9', '2020-09-21 21:05:36', '2020-09-21 21:12:00');
INSERT INTO `cq_data` VALUES ('3566801466', '9', 'austin998', '264.00', '264.00', '0.00', '205331.80', 'slot', 'mobile', '2020-09-21 21:05:39', 'cq9', '2020-09-21 21:05:42', '2020-09-21 21:12:00');
INSERT INTO `cq_data` VALUES ('3566801581', '9', 'austin998', '88.00', '88.00', '0.00', '205243.80', 'slot', 'mobile', '2020-09-21 21:05:45', 'cq9', '2020-09-21 21:05:48', '2020-09-21 21:12:00');
INSERT INTO `cq_data` VALUES ('3566801651', '9', 'austin998', '88.00', '88.00', '0.00', '205155.80', 'slot', 'mobile', '2020-09-21 21:05:50', 'cq9', '2020-09-21 21:05:53', '2020-09-21 21:12:00');
INSERT INTO `cq_data` VALUES ('3566801776', '9', 'austin998', '440.00', '440.00', '0.00', '204715.80', 'slot', 'mobile', '2020-09-21 21:05:57', 'cq9', '2020-09-21 21:06:00', '2020-09-21 21:12:00');
INSERT INTO `cq_data` VALUES ('3566801861', '9', 'austin998', '440.00', '440.00', '500.00', '204775.80', 'slot', 'mobile', '2020-09-21 21:06:02', 'cq9', '2020-09-21 21:06:12', '2020-09-21 21:12:00');
INSERT INTO `cq_data` VALUES ('3566802030', '9', 'austin998', '440.00', '440.00', '125.00', '204460.80', 'slot', 'mobile', '2020-09-21 21:06:12', 'cq9', '2020-09-21 21:06:31', '2020-09-21 21:12:00');
INSERT INTO `cq_data` VALUES ('BU5f67628dd6799983ce6ea324', 'BU02', 'kavin998', '18.00', '18.00', '0.00', '408152.00', 'slot', 'mobile', '2020-09-20 22:09:17', 'cq9', '2020-09-20 22:09:22', '2020-09-21 12:33:00');
INSERT INTO `cq_data` VALUES ('BU5f676299d6799983ce6ea325', 'BU02', 'kavin998', '18.00', '18.00', '0.00', '408134.00', 'slot', 'mobile', '2020-09-20 22:09:29', 'cq9', '2020-09-20 22:09:35', '2020-09-21 12:33:00');
INSERT INTO `cq_data` VALUES ('BU5f67629fd6799983ce6ea326', 'BU02', 'kavin998', '18.00', '18.00', '40.00', '408156.00', 'slot', 'mobile', '2020-09-20 22:09:35', 'cq9', '2020-09-20 22:09:45', '2020-09-21 12:33:00');
INSERT INTO `cq_data` VALUES ('BU5f6762aad6799983ce6ea327', 'BU02', 'kavin998', '18.00', '18.00', '0.00', '408138.00', 'slot', 'mobile', '2020-09-20 22:09:46', 'cq9', '2020-09-20 22:09:52', '2020-09-21 12:33:00');
INSERT INTO `cq_data` VALUES ('BU5f6762b0d6799983ce6ea328', 'BU02', 'kavin998', '18.00', '18.00', '0.00', '408120.00', 'slot', 'mobile', '2020-09-20 22:09:52', 'cq9', '2020-09-20 22:10:13', '2020-09-21 12:33:00');
INSERT INTO `cq_data` VALUES ('BU5f6762c5d6799983ce6ea329', 'BU02', 'kavin998', '18.00', '18.00', '4.00', '408106.00', 'slot', 'mobile', '2020-09-20 22:10:13', 'cq9', '2020-09-20 22:10:21', '2020-09-21 12:33:00');
INSERT INTO `cq_data` VALUES ('BU5f6762d50c324a0317517831', 'BU02', 'kavin998', '18.00', '18.00', '4.00', '408092.00', 'slot', 'mobile', '2020-09-20 22:10:29', 'cq9', '2020-09-20 22:10:37', '2020-09-21 12:33:00');
INSERT INTO `cq_data` VALUES ('BU5f6762dd0c324a0317517832', 'BU02', 'kavin998', '18.00', '18.00', '0.00', '408074.00', 'slot', 'mobile', '2020-09-20 22:10:37', 'cq9', '2020-09-20 22:10:43', '2020-09-21 12:33:00');
INSERT INTO `cq_data` VALUES ('BU5f6762e30c324a0317517833', 'BU02', 'kavin998', '18.00', '18.00', '0.00', '408056.00', 'slot', 'mobile', '2020-09-20 22:10:43', 'cq9', '2020-09-20 22:10:49', '2020-09-21 12:33:00');
INSERT INTO `cq_data` VALUES ('BU5f6762e9d6799983ce6ea32a', 'BU02', 'kavin998', '18.00', '18.00', '10.00', '408048.00', 'slot', 'mobile', '2020-09-20 22:10:49', 'cq9', '2020-09-20 22:10:57', '2020-09-21 12:33:00');
INSERT INTO `cq_data` VALUES ('BU5f6762f1d6799983ce6ea32b', 'BU02', 'kavin998', '18.00', '18.00', '20.00', '408050.00', 'slot', 'mobile', '2020-09-20 22:10:57', 'cq9', '2020-09-20 22:17:34', '2020-09-21 12:33:00');
INSERT INTO `cq_data` VALUES ('BU5f676470d6799983ce6ea32c', 'BU02', 'austin998', '18.00', '18.00', '14.00', '147.00', 'slot', 'mobile', '2020-09-20 22:17:20', 'cq9', '2020-09-20 22:17:32', '2020-09-21 12:33:00');
INSERT INTO `cq_data` VALUES ('BU5f67647c0c324a0317517834', 'BU02', 'austin998', '18.00', '18.00', '0.00', '129.00', 'slot', 'mobile', '2020-09-20 22:17:32', 'cq9', '2020-09-20 22:17:37', '2020-09-21 12:33:00');
INSERT INTO `cq_data` VALUES ('BU5f676482d6799983ce6ea32d', 'BU02', 'austin998', '18.00', '18.00', '20.00', '131.00', 'slot', 'mobile', '2020-09-20 22:17:38', 'cq9', '2020-09-20 22:17:46', '2020-09-21 12:33:00');
INSERT INTO `cq_data` VALUES ('BU5f67648ad6799983ce6ea32e', 'BU02', 'austin998', '18.00', '18.00', '0.00', '113.00', 'slot', 'mobile', '2020-09-20 22:17:46', 'cq9', '2020-09-20 22:17:52', '2020-09-21 12:33:00');
INSERT INTO `cq_data` VALUES ('BU5f6764900c324a0317517835', 'BU02', 'austin998', '18.00', '18.00', '0.00', '95.00', 'slot', 'mobile', '2020-09-20 22:17:52', 'cq9', '2020-09-20 22:17:58', '2020-09-21 12:33:00');
INSERT INTO `cq_data` VALUES ('BU5f6764960c324a0317517836', 'BU02', 'austin998', '18.00', '18.00', '0.00', '77.00', 'slot', 'mobile', '2020-09-20 22:17:58', 'cq9', '2020-09-20 22:18:05', '2020-09-21 12:33:00');
INSERT INTO `cq_data` VALUES ('BU5f67649d0c324a0317517837', 'BU02', 'austin998', '18.00', '18.00', '0.00', '59.00', 'slot', 'mobile', '2020-09-20 22:18:05', 'cq9', '2020-09-20 22:24:34', '2020-09-21 12:33:00');
INSERT INTO `cq_data` VALUES ('BU5f6836d80c324a0317517e66', 'BU02', 'austin998', '18.00', '18.00', '4.00', '204030.00', 'slot', 'mobile', '2020-09-21 13:15:04', 'cq9', '2020-09-21 13:15:14', '2020-09-21 13:18:00');
INSERT INTO `cq_data` VALUES ('BU5f6836e20c324a0317517e67', 'BU02', 'austin998', '18.00', '18.00', '0.00', '204012.00', 'slot', 'mobile', '2020-09-21 13:15:14', 'cq9', '2020-09-21 13:20:34', '2020-09-21 13:24:00');
INSERT INTO `cq_data` VALUES ('BU5f684b570c324a0317517e6f', 'BU05', 'austin998', '18.00', '18.00', '98.00', '204092.00', 'slot', 'mobile', '2020-09-21 14:42:31', 'cq9', '2020-09-21 14:42:41', '2020-09-21 14:45:00');
INSERT INTO `cq_data` VALUES ('BU5f684b660c324a0317517e70', 'BU05', 'austin998', '18.00', '18.00', '0.00', '204074.00', 'slot', 'mobile', '2020-09-21 14:42:46', 'cq9', '2020-09-21 14:42:51', '2020-09-21 14:45:00');
INSERT INTO `cq_data` VALUES ('BU5f684b750c324a0317517e71', 'BU05', 'austin998', '9.00', '9.00', '0.00', '204065.00', 'slot', 'mobile', '2020-09-21 14:43:01', 'cq9', '2020-09-21 14:43:06', '2020-09-21 14:45:00');
INSERT INTO `cq_data` VALUES ('BU5f684b830c324a0317517e72', 'BU05', 'austin998', '9.00', '9.00', '0.00', '204056.00', 'slot', 'mobile', '2020-09-21 14:43:15', 'cq9', '2020-09-21 14:43:20', '2020-09-21 14:45:00');
INSERT INTO `cq_data` VALUES ('BU5f684b880c324a0317517e73', 'BU05', 'austin998', '9.00', '9.00', '15.00', '204062.00', 'slot', 'mobile', '2020-09-21 14:43:20', 'cq9', '2020-09-21 14:43:28', '2020-09-21 14:45:00');
INSERT INTO `cq_data` VALUES ('BU5f684b910c324a0317517e74', 'BU05', 'austin998', '9.00', '9.00', '0.00', '204053.00', 'slot', 'mobile', '2020-09-21 14:43:29', 'cq9', '2020-09-21 14:43:34', '2020-09-21 14:45:00');
INSERT INTO `cq_data` VALUES ('BU5f684b96d6799983ce6ea988', 'BU05', 'austin998', '9.00', '9.00', '5.00', '204049.00', 'slot', 'mobile', '2020-09-21 14:43:34', 'cq9', '2020-09-21 14:43:43', '2020-09-21 14:45:00');

-- ----------------------------
-- Table structure for `creditlogs`
-- ----------------------------
DROP TABLE IF EXISTS `creditlogs`;
CREATE TABLE `creditlogs` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `loginname` varchar(255) NOT NULL,
  `createtime` datetime DEFAULT NULL,
  `type` varchar(30) NOT NULL,
  `credit` double(24,2) NOT NULL DEFAULT '0.00',
  `newCredit` double(24,2) NOT NULL DEFAULT '0.00',
  `remit` double(24,2) NOT NULL DEFAULT '0.00',
  `remark` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`) USING BTREE,
  KEY `loginname` (`loginname`) USING BTREE,
  KEY `createtime` (`createtime`) USING BTREE,
  KEY `type` (`type`) USING BTREE,
  KEY `remit` (`remit`) USING BTREE,
  KEY `remark` (`remark`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=272 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of creditlogs
-- ----------------------------
INSERT INTO `creditlogs` VALUES ('124', 'twtest01', '2018-12-16 15:04:15', 'CHANGE_QUOTAL', '0.00', '500.00', '500.00', 'referenceNo:1;说明:');
INSERT INTO `creditlogs` VALUES ('125', 'kavin998', '2020-09-05 22:05:00', 'CHANGE_QUOTAL', '0.00', '10000.00', '10000.00', 'referenceNo:2;手工存款说明:手工存款kavin');
INSERT INTO `creditlogs` VALUES ('126', 'kavin998', '2020-09-13 11:02:31', 'TRANSFER_MGIN', '10000.00', '9999.00', '-1.00', 'referenceNo:2509131103042458783;');
INSERT INTO `creditlogs` VALUES ('127', 'kavin998', '2020-09-13 22:53:25', 'TRANSFER_DTIN', '9999.00', '9987.00', '-12.00', 'referenceNo:2509132253293454457;');
INSERT INTO `creditlogs` VALUES ('128', 'kavin998', '2020-09-13 22:53:51', 'TRANSFER_DTIN', '9987.00', '9975.00', '-12.00', 'referenceNo:2509132253511176557;');
INSERT INTO `creditlogs` VALUES ('129', 'kavin998', '2020-09-13 22:59:08', 'TRANSFER_DTGOUT', '9975.00', '9997.00', '22.00', 'referenceNo:2509132259085888991;');
INSERT INTO `creditlogs` VALUES ('130', 'kavin998', '2020-09-13 22:59:33', 'TRANSFER_DTIN', '9997.00', '9990.00', '-7.00', 'referenceNo:2509132259331288659;');
INSERT INTO `creditlogs` VALUES ('131', 'kavin998', '2020-09-14 20:10:33', 'TRANSFER_MGIN', '9990.00', '9974.00', '-16.00', 'referenceNo:2509142010343089281;');
INSERT INTO `creditlogs` VALUES ('132', 'kavin998', '2020-09-14 20:21:05', 'TRANSFER_MGIN', '9974.00', '9960.00', '-14.00', 'referenceNo:2509142021055542204;');
INSERT INTO `creditlogs` VALUES ('133', 'kavin998', '2020-09-14 20:27:51', 'TRANSFER_MGIN', '9960.00', '9949.00', '-11.00', 'referenceNo:2509142027516914514;');
INSERT INTO `creditlogs` VALUES ('134', 'kavin998', '2020-09-14 21:02:30', 'TRANSFER_MGIN', '9949.00', '9938.00', '-11.00', 'referenceNo:2509142102366849901;');
INSERT INTO `creditlogs` VALUES ('135', 'kavin998', '2020-09-14 21:30:18', 'TRANSFER_MGOUT', '9938.00', '9950.00', '12.00', 'referenceNo:2509142130196288151;');
INSERT INTO `creditlogs` VALUES ('136', 'kavin998', '2020-09-14 21:30:45', 'TRANSFER_DTGOUT', '9950.00', '9951.00', '1.00', 'referenceNo:2509142130449855928;');
INSERT INTO `creditlogs` VALUES ('137', 'kavin998', '2020-09-15 01:03:56', 'TRANSFER_REDRAINOUT', '9951.00', '9940.00', '-11.00', 'referenceNo:2509150103567813027;用户主账户转入红包雨账户11.0元');
INSERT INTO `creditlogs` VALUES ('138', 'kavin998', '2020-09-15 01:04:25', 'TRANSFER_REDRAINOUT', '9940.00', '9929.00', '-11.00', 'referenceNo:2509150104252028506;用户主账户转入红包雨账户11.0元');
INSERT INTO `creditlogs` VALUES ('139', 'kavin998', '2020-09-15 01:49:24', 'TRANSFER_REDRAIN_OUT', '9929.00', '9929.00', '0.00', 'referenceNo:2509150149247035766;红包雨余额转入MG平台,10倍流水，转入11.0');
INSERT INTO `creditlogs` VALUES ('140', 'kavin998', '2020-09-15 10:42:11', 'TRANSFER_REDRAINOUT', '9929.00', '9829.00', '-100.00', 'referenceNo:2509151042112616584;用户主账户转入红包雨账户100.0元');
INSERT INTO `creditlogs` VALUES ('141', 'kavin998', '2020-09-15 10:42:25', 'TRANSFER_REDRAIN_OUT', '9829.00', '9829.00', '0.00', 'referenceNo:2509151042257256825;红包雨余额转入MG平台,10倍流水，转入11.0');
INSERT INTO `creditlogs` VALUES ('142', 'kavin998', '2020-09-15 11:00:25', 'TRANSFER_REDRAIN_OUT', '9829.00', '9829.00', '0.00', 'referenceNo:2509151100259367533;红包雨余额转入MG平台,10倍流水，转入11.0');
INSERT INTO `creditlogs` VALUES ('154', 'kavin998', '2020-09-15 11:45:35', 'TRANSFER_REDRAIN_OUT', '9829.00', '9829.00', '0.00', 'referenceNo:2509151145350363481;红包雨余额转入DT平台,0倍流水，转入11.0');
INSERT INTO `creditlogs` VALUES ('155', 'kavin998', '2020-09-17 20:39:12', 'XIMA_CONS', '9829.00', '9833.10', '4.10', 'referenceNo:629200917rrkqhuep4o;mg自助洗码;秒反水;executed;自动审核');
INSERT INTO `creditlogs` VALUES ('156', 'kavin998', '2020-09-17 21:58:46', 'TRANSFER_DTIN', '9833.10', '8833.10', '-1000.00', 'referenceNo:2509172158465575983;');
INSERT INTO `creditlogs` VALUES ('157', 'kavin998', '2020-09-17 21:59:02', 'TRANSFER_MGIN', '8833.10', '8333.10', '-500.00', 'referenceNo:2509172159026061841;');
INSERT INTO `creditlogs` VALUES ('158', 'kavin998', '2020-09-20 15:12:25', 'CHANGE_QUOTAL', '8333.10', '9444.10', '1111.00', 'referenceNo:5;说明:热热热');
INSERT INTO `creditlogs` VALUES ('159', 'austin998', '2020-09-20 15:13:29', 'CHANGE_QUOTAL', '0.00', '10000.00', '10000.00', 'referenceNo:6;说明:同仁堂率');
INSERT INTO `creditlogs` VALUES ('160', 'austin998', '2020-09-20 17:10:50', 'TRANSFER_DTIN', '10000.00', '9988.00', '-12.00', 'referenceNo:2509201710507809157;');
INSERT INTO `creditlogs` VALUES ('161', 'austin998', '2020-09-20 17:22:46', 'TRANSFER_CQ9IN', '9988.00', '9975.00', '-13.00', 'referenceNo:2509201722460522002;');
INSERT INTO `creditlogs` VALUES ('162', 'austin998', '2020-09-20 17:24:17', 'TRANSFER_CQ9IN', '9975.00', '9964.00', '-11.00', 'referenceNo:2509201724168310989;');
INSERT INTO `creditlogs` VALUES ('163', 'austin998', '2020-09-20 17:24:42', 'TRANSFER_CQ9IN', '9964.00', '9953.00', '-11.00', 'referenceNo:2509201724424482156;');
INSERT INTO `creditlogs` VALUES ('164', 'austin998', '2020-09-20 17:28:12', 'TRANSFER_CQ9IN', '9953.00', '9942.00', '-11.00', 'referenceNo:2509201728119148344;');
INSERT INTO `creditlogs` VALUES ('165', 'austin998', '2020-09-20 17:28:44', 'TRANSFER_CQ9OUT', '9942.00', '9948.00', '6.00', 'referenceNo:2509201728440109108;');
INSERT INTO `creditlogs` VALUES ('166', 'austin998', '2020-09-20 17:30:42', 'TRANSFER_CQ9OUT', '9948.00', '9954.00', '6.00', 'referenceNo:2509201730417880063;');
INSERT INTO `creditlogs` VALUES ('167', 'austin998', '2020-09-20 17:43:14', 'TRANSFER_MGIN', '9954.00', '9888.00', '-66.00', 'referenceNo:2509201743145090490;');
INSERT INTO `creditlogs` VALUES ('168', 'austin998', '2020-09-20 17:43:28', 'TRANSFER_MGOUT', '9888.00', '9894.00', '6.00', 'referenceNo:2509201743286844307;');
INSERT INTO `creditlogs` VALUES ('169', 'austin998', '2020-09-20 17:43:52', 'TRANSFER_MGOUT', '9894.00', '9898.00', '4.00', 'referenceNo:2509201743527328675;');
INSERT INTO `creditlogs` VALUES ('170', 'austin998', '2020-09-20 18:28:38', 'TRANSFER_MGOUT', '9898.00', '9900.00', '2.00', 'referenceNo:2509201828390672122;');
INSERT INTO `creditlogs` VALUES ('171', 'austin998', '2020-09-20 18:29:21', 'TRANSFER_MGOUT', '9900.00', '9902.00', '2.00', 'referenceNo:2509201829218932452;');
INSERT INTO `creditlogs` VALUES ('172', 'austin998', '2020-09-20 18:29:48', 'TRANSFER_MGIN', '9902.00', '9901.00', '-1.00', 'referenceNo:2509201829481920098;');
INSERT INTO `creditlogs` VALUES ('173', 'austin998', '2020-09-20 18:30:45', 'TRANSFER_MGIN', '9901.00', '9900.00', '-1.00', 'referenceNo:2509201830450286539;');
INSERT INTO `creditlogs` VALUES ('174', 'austin998', '2020-09-20 18:31:22', 'TRANSFER_MGIN', '9900.00', '9899.00', '-1.00', 'referenceNo:2509201831235926501;');
INSERT INTO `creditlogs` VALUES ('175', 'austin998', '2020-09-20 18:39:42', 'TRANSFER_CQ9OUT', '9899.00', '9903.00', '4.00', 'referenceNo:2509201839429127651;');
INSERT INTO `creditlogs` VALUES ('176', 'austin998', '2020-09-20 18:43:19', 'TRANSFER_REDRAINOUT', '9903.00', '9753.00', '-150.00', 'referenceNo:2509201843189101506;用户主账户转入红包雨账户150.0元');
INSERT INTO `creditlogs` VALUES ('177', 'austin998', '2020-09-20 19:41:13', 'TRANSFER_REDRAIN_OUT', '9753.00', '9753.00', '0.00', 'referenceNo:2509201941137638007;红包雨余额转入CQ9平台,10倍流水，转入11.0');
INSERT INTO `creditlogs` VALUES ('178', 'austin998', '2020-09-20 19:41:51', 'TRANSFER_REDRAIN_OUT', '9753.00', '9753.00', '0.00', 'referenceNo:2509201941518869232;红包雨余额转入CQ9平台,10倍流水，转入11.0');
INSERT INTO `creditlogs` VALUES ('179', 'austin998', '2020-09-20 20:04:01', 'TRANSFER_REDRAIN_OUT', '9753.00', '9753.00', '0.00', 'referenceNo:2509202004016085633;红包雨余额转入CQ9平台,10倍流水，转入13.0');
INSERT INTO `creditlogs` VALUES ('180', 'austin998', '2020-09-20 20:05:18', 'TRANSFER_REDRAIN_OUT', '9753.00', '9753.00', '0.00', 'referenceNo:2509202005184390387;红包雨余额转入CQ9平台,10倍流水，转入12.0');
INSERT INTO `creditlogs` VALUES ('181', 'austin998', '2020-09-20 20:06:20', 'TRANSFER_REDRAIN_OUT', '9753.00', '9753.00', '0.00', 'referenceNo:2509202006203638718;红包雨余额转入CQ9平台,10倍流水，转入13.0');
INSERT INTO `creditlogs` VALUES ('182', 'austin998', '2020-09-20 20:07:52', 'TRANSFER_REDRAIN_OUT', '9753.00', '9753.00', '0.00', 'referenceNo:2509202007524053089;红包雨余额转入CQ9平台,10倍流水，转入11.0');
INSERT INTO `creditlogs` VALUES ('183', 'austin998', '2020-09-20 20:08:43', 'TRANSFER_REDRAIN_OUT', '9753.00', '9753.00', '0.00', 'referenceNo:2509202008439291102;红包雨余额转入CQ9平台,10倍流水，转入11.0');
INSERT INTO `creditlogs` VALUES ('184', 'austin998', '2020-09-20 20:10:12', 'TRANSFER_REDRAIN_OUT', '9753.00', '9753.00', '0.00', 'referenceNo:2509202010128874065;红包雨余额转入CQ9平台,10倍流水，转入11.0');
INSERT INTO `creditlogs` VALUES ('185', 'austin998', '2020-09-20 20:10:44', 'TRANSFER_REDRAINOUT', '9753.00', '9641.00', '-112.00', 'referenceNo:2509202010445887524;用户主账户转入红包雨账户112.0元');
INSERT INTO `creditlogs` VALUES ('186', 'austin998', '2020-09-20 20:10:58', 'TRANSFER_REDRAIN_OUT', '9641.00', '9641.00', '0.00', 'referenceNo:2509202010588314715;红包雨余额转入CQ9平台,10倍流水，转入22.0');
INSERT INTO `creditlogs` VALUES ('187', 'austin998', '2020-09-21 21:06:53', 'XIMA_CONS', '9641.00', '9647.12', '6.12', 'referenceNo:633200921lufizym2gq;cq9自助洗码;秒反水;executed;自动审核');
INSERT INTO `creditlogs` VALUES ('188', 'kavin998', '2020-09-24 23:30:57', 'TRANSFER_MGIN', '9444.10', '9433.10', '-11.00', 'referenceNo:2509242330576197013;');
INSERT INTO `creditlogs` VALUES ('189', 'kavin998', '2020-09-27 00:03:57', 'TRANSFER_PGIN', '9433.10', '9421.10', '-12.00', 'referenceNo:2509270004019952065;');
INSERT INTO `creditlogs` VALUES ('190', 'kavin998', '2020-09-27 00:04:51', 'TRANSFER_PGIN', '9421.10', '9408.10', '-13.00', 'referenceNo:2509270004518207499;');
INSERT INTO `creditlogs` VALUES ('191', 'kavin998', '2020-09-27 00:06:01', 'TRANSFER_PGIN', '9408.10', '9397.10', '-11.00', 'referenceNo:2509270006017070363;');
INSERT INTO `creditlogs` VALUES ('192', 'kavin998', '2020-09-27 00:08:53', 'TRANSFER_PGIN', '9397.10', '9386.10', '-11.00', 'referenceNo:2509270008536809127;');
INSERT INTO `creditlogs` VALUES ('193', 'kavin998', '2020-09-27 00:09:14', 'TRANSFER_PGIN', '9386.10', '9375.10', '-11.00', 'referenceNo:2509270009142666135;');
INSERT INTO `creditlogs` VALUES ('194', 'kavin998', '2020-09-27 00:09:32', 'TRANSFER_PGIN', '9375.10', '9363.10', '-12.00', 'referenceNo:2509270009319053008;');
INSERT INTO `creditlogs` VALUES ('195', 'kavin998', '2020-09-27 00:10:38', 'TRANSFER_PGIN', '9363.10', '9330.10', '-33.00', 'referenceNo:2509270010381657748;');
INSERT INTO `creditlogs` VALUES ('196', 'kavin998', '2020-09-27 00:12:35', 'TRANSFER_PGIN', '9330.10', '9319.10', '-11.00', 'referenceNo:2509270012356016828;');
INSERT INTO `creditlogs` VALUES ('197', 'kavin998', '2020-09-27 00:13:11', 'TRANSFER_PGIN', '9319.10', '9307.10', '-12.00', 'referenceNo:2509270013119088722;');
INSERT INTO `creditlogs` VALUES ('198', 'kavin998', '2020-09-27 00:13:56', 'TRANSFER_PGIN', '9307.10', '9284.10', '-23.00', 'referenceNo:2509270013566858581;');
INSERT INTO `creditlogs` VALUES ('199', 'kavin998', '2020-09-27 00:16:10', 'TRANSFER_PGIN', '9284.10', '9272.10', '-12.00', 'referenceNo:2509270016108072882;');
INSERT INTO `creditlogs` VALUES ('200', 'kavin998', '2020-09-27 00:22:08', 'TRANSFER_PGOUT', '9272.10', '9284.10', '12.00', 'referenceNo:2509270022079774546;');
INSERT INTO `creditlogs` VALUES ('201', 'kavin998', '2020-09-27 13:30:38', 'TRANSFER_REDRAIN_OUT', '9284.10', '9284.10', '0.00', 'referenceNo:2509271330380312589;红包雨余额转入CQ9平台,10倍流水，转入12.0');
INSERT INTO `creditlogs` VALUES ('202', 'kavin998', '2020-09-27 13:34:43', 'TRANSFER_REDRAIN_OUT', '9284.10', '9284.10', '0.00', 'referenceNo:2509271334434830989;红包雨余额转入CQ9平台,10倍流水，转入12.0');
INSERT INTO `creditlogs` VALUES ('203', 'kavin998', '2020-09-27 13:37:34', 'TRANSFER_REDRAIN_OUT', '9284.10', '9284.10', '0.00', 'referenceNo:2509271337341478395;红包雨余额转入CQ9平台,10倍流水，转入11.0');
INSERT INTO `creditlogs` VALUES ('204', 'kavin998', '2020-09-27 14:00:24', 'TRANSFER_REDRAIN_OUT', '9284.10', '9284.10', '0.00', 'referenceNo:2509271400246584724;红包雨余额转入PG平台,10倍流水，转入11.0');
INSERT INTO `creditlogs` VALUES ('205', 'kavin998', '2020-09-27 14:02:34', 'TRANSFER_REDRAIN_OUT', '9284.10', '9284.10', '0.00', 'referenceNo:2509271402343348454;红包雨余额转入PG平台,10倍流水，转入20.0');
INSERT INTO `creditlogs` VALUES ('206', 'kavin998', '2020-09-27 15:19:45', 'TRANSFER_MGIN', '9284.10', '9272.10', '-12.00', 'referenceNo:2509271519458051119;');
INSERT INTO `creditlogs` VALUES ('207', 'kavin998', '2020-09-27 15:20:22', 'TRANSFER_DTIN', '9272.10', '9260.10', '-12.00', 'referenceNo:2509271520217850746;');
INSERT INTO `creditlogs` VALUES ('208', 'kavin998', '2020-09-27 21:02:53', 'TRANSFER_DTIN', '9260.10', '9149.10', '-111.00', 'referenceNo:2509272102535479558;');
INSERT INTO `creditlogs` VALUES ('209', 'kavin998', '2020-09-27 21:14:40', 'TRANSFER_MGIN', '9149.10', '9137.10', '-12.00', 'referenceNo:2509272114412283971;');
INSERT INTO `creditlogs` VALUES ('210', 'kavin998', '2020-09-27 21:15:16', 'TRANSFER_MGIN', '9137.10', '9125.10', '-12.00', 'referenceNo:2509272115547640341;');
INSERT INTO `creditlogs` VALUES ('211', 'kavin998', '2020-09-29 22:41:49', 'TRANSFER_BGIN', '9125.10', '9014.10', '-111.00', 'referenceNo:2509292241500691244;');
INSERT INTO `creditlogs` VALUES ('212', 'kavin998', '2020-09-29 22:58:44', 'TRANSFER_BGIN', '9014.10', '8902.10', '-112.00', 'referenceNo:2509292258459878076;');
INSERT INTO `creditlogs` VALUES ('213', 'kavin998', '2020-09-29 23:12:38', 'TRANSFER_BGIN', '8902.10', '8779.10', '-123.00', 'referenceNo:2509292312382544342;');
INSERT INTO `creditlogs` VALUES ('214', 'kavin998', '2020-09-29 23:15:31', 'TRANSFER_BGIN', '8779.10', '8768.10', '-11.00', 'referenceNo:2509292315315291093;');
INSERT INTO `creditlogs` VALUES ('215', 'kavin998', '2020-09-29 23:26:37', 'TRANSFER_BGIN', '8768.10', '8657.10', '-111.00', 'referenceNo:2509292327523501228;');
INSERT INTO `creditlogs` VALUES ('216', 'kavin998', '2020-09-29 23:28:04', 'TRANSFER_BGIN', '8657.10', '8646.10', '-11.00', 'referenceNo:2509292328044872024;');
INSERT INTO `creditlogs` VALUES ('217', 'kavin998', '2020-09-29 23:30:55', 'TRANSFER_BGIN', '8646.10', '8635.10', '-11.00', 'referenceNo:2509292330566470705;');
INSERT INTO `creditlogs` VALUES ('218', 'kavin998', '2020-09-29 23:33:05', 'TRANSFER_BGIN', '8635.10', '8633.10', '-2.00', 'referenceNo:2509292333060310819;');
INSERT INTO `creditlogs` VALUES ('219', 'kavin998', '2020-09-29 23:35:34', 'TRANSFER_BGIN', '8633.10', '8631.10', '-2.00', 'referenceNo:2509292335358151445;');
INSERT INTO `creditlogs` VALUES ('220', 'kavin998', '2020-09-29 23:36:12', 'TRANSFER_BGIN', '8631.10', '8629.10', '-2.00', 'referenceNo:2509292337365965150;');
INSERT INTO `creditlogs` VALUES ('221', 'kavin998', '2020-09-29 23:43:34', 'TRANSFER_BGIN', '8629.10', '8627.10', '-2.00', 'referenceNo:2509292343354258566;');
INSERT INTO `creditlogs` VALUES ('222', 'kavin998', '2020-09-29 23:44:13', 'TRANSFER_BGIN', '8627.10', '8625.10', '-2.00', 'referenceNo:2509292344134712107;');
INSERT INTO `creditlogs` VALUES ('223', 'kavin998', '2020-09-29 23:54:48', 'TRANSFER_BGIN', '8625.10', '8503.10', '-122.00', 'referenceNo:2509292354497067541;');
INSERT INTO `creditlogs` VALUES ('224', 'kavin998', '2020-09-29 23:55:32', 'TRANSFER_BGOUT', '8503.10', '8505.10', '2.00', 'referenceNo:2509292355329169314;');
INSERT INTO `creditlogs` VALUES ('225', 'kavin998', '2020-09-30 00:43:20', 'TRANSFER_REDRAINOUT', '8505.10', '7605.10', '-900.00', 'referenceNo:2509300043199774223;用户主账户转入红包雨账户900.0元');
INSERT INTO `creditlogs` VALUES ('226', 'kavin998', '2020-09-30 00:43:37', 'TRANSFER_REDRAIN_OUT', '7605.10', '7605.10', '0.00', 'referenceNo:2509300043375888421;红包雨余额转入BG平台,10倍流水，转入12.0');
INSERT INTO `creditlogs` VALUES ('227', 'kavin998', '2020-09-30 00:44:04', 'TRANSFER_REDRAIN_OUT', '7605.10', '7605.10', '0.00', 'referenceNo:2509300044042767139;红包雨余额转入BG平台,10倍流水，转入12.0');
INSERT INTO `creditlogs` VALUES ('228', 'kavin998', '2020-09-30 00:45:15', 'TRANSFER_REDRAIN_OUT', '7605.10', '7605.10', '0.00', 'referenceNo:2509300045156775794;红包雨余额转入BG平台,10倍流水，转入12.0');
INSERT INTO `creditlogs` VALUES ('229', 'kavin998', '2020-09-30 00:49:07', 'TRANSFER_REDRAIN_OUT', '7605.10', '7605.10', '0.00', 'referenceNo:2509300049070071472;红包雨余额转入BG平台,10倍流水，转入12.0');
INSERT INTO `creditlogs` VALUES ('230', 'kavin998', '2020-10-01 14:05:16', 'TRANSFER_BGIN', '7605.10', '4271.10', '-3334.00', 'referenceNo:2510011405162754712;');
INSERT INTO `creditlogs` VALUES ('231', 'kavin998', '2020-10-01 14:06:18', 'TRANSFER_BGIN', '4271.10', '4269.10', '-2.00', 'referenceNo:2510011406181995260;');
INSERT INTO `creditlogs` VALUES ('232', 'kavin998', '2020-10-01 14:06:29', 'TRANSFER_BGIN', '4269.10', '4035.10', '-234.00', 'referenceNo:2510011406289288915;');
INSERT INTO `creditlogs` VALUES ('233', 'kavin998', '2020-10-01 14:06:39', 'TRANSFER_BGIN', '4035.10', '4000.10', '-35.00', 'referenceNo:2510011406394948935;');
INSERT INTO `creditlogs` VALUES ('234', 'kavin998', '2020-10-01 16:32:24', 'TRANSFER_BGIN', '4000.10', '3778.10', '-222.00', 'referenceNo:2510011632260282327;');
INSERT INTO `creditlogs` VALUES ('235', 'kavin998', '2020-10-01 16:33:01', 'TRANSFER_BGIN', '3778.10', '3776.10', '-2.00', 'referenceNo:2510011633020690621;');
INSERT INTO `creditlogs` VALUES ('236', 'kavin998', '2020-10-01 16:33:08', 'TRANSFER_BGIN', '3776.10', '3554.10', '-222.00', 'referenceNo:2510011633101712364;');
INSERT INTO `creditlogs` VALUES ('237', 'kavin998', '2020-10-01 16:33:19', 'TRANSFER_BGIN', '3554.10', '3464.10', '-90.00', 'referenceNo:2510011633190424926;');
INSERT INTO `creditlogs` VALUES ('238', 'austin998', '2020-10-03 21:15:22', 'TRANSFER_BBININ', '9647.12', '9425.12', '-222.00', 'referenceNo:2510032115248633230;');
INSERT INTO `creditlogs` VALUES ('239', 'austin998', '2020-10-03 21:42:53', 'TRANSFER_BBINOUT', '9425.12', '9437.12', '12.00', 'referenceNo:2510032142553464306;');
INSERT INTO `creditlogs` VALUES ('240', 'austin998', '2020-10-04 19:25:26', 'TRANSFER_BBININ', '9437.12', '9005.12', '-432.00', 'referenceNo:2510041925268025625;');
INSERT INTO `creditlogs` VALUES ('241', 'kavin998', '2020-10-05 00:46:19', 'TRANSFER_REDRAIN_OUT', '3464.10', '3464.10', '0.00', 'referenceNo:2510050046195463692;红包雨余额转入MG平台,10倍流水，转入11.0');
INSERT INTO `creditlogs` VALUES ('242', 'austin998', '2020-10-05 14:05:09', 'TRANSFER_SBAIN', '9005.12', '8983.12', '-22.00', 'referenceNo:2510051405172420496;');
INSERT INTO `creditlogs` VALUES ('243', 'austin998', '2020-10-05 14:05:27', 'TRANSFER_SBAIN', '8983.12', '8961.12', '-22.00', 'referenceNo:2510051405272854133;');
INSERT INTO `creditlogs` VALUES ('244', 'austin998', '2020-10-05 14:06:50', 'TRANSFER_SBAIN', '8961.12', '8939.12', '-22.00', 'referenceNo:2510051406498136706;');
INSERT INTO `creditlogs` VALUES ('245', 'austin998', '2020-10-05 14:07:33', 'TRANSFER_SBAIN', '8939.12', '8917.12', '-22.00', 'referenceNo:2510051407333084950;');
INSERT INTO `creditlogs` VALUES ('246', 'austin998', '2020-10-05 14:11:31', 'TRANSFER_SBAIN', '8917.12', '8895.12', '-22.00', 'referenceNo:2510051411311373061;');
INSERT INTO `creditlogs` VALUES ('247', 'austin998', '2020-10-05 14:12:05', 'TRANSFER_SBAIN', '8895.12', '8873.12', '-22.00', 'referenceNo:2510051412047734551;');
INSERT INTO `creditlogs` VALUES ('248', 'austin998', '2020-10-05 14:12:28', 'TRANSFER_SBAIN', '8873.12', '8851.12', '-22.00', 'referenceNo:2510051412280713891;');
INSERT INTO `creditlogs` VALUES ('249', 'austin998', '2020-10-05 14:15:09', 'TRANSFER_SBAIN', '8851.12', '8829.12', '-22.00', 'referenceNo:2510051415091828189;');
INSERT INTO `creditlogs` VALUES ('250', 'austin998', '2020-10-05 14:15:22', 'TRANSFER_SBAIN', '8829.12', '8807.12', '-22.00', 'referenceNo:2510051415220546793;');
INSERT INTO `creditlogs` VALUES ('251', 'austin998', '2020-10-05 14:15:52', 'TRANSFER_SBAIN', '8807.12', '8785.12', '-22.00', 'referenceNo:2510051415524310408;');
INSERT INTO `creditlogs` VALUES ('252', 'austin998', '2020-10-05 14:18:38', 'TRANSFER_SBAIN', '8785.12', '8763.12', '-22.00', 'referenceNo:2510051418385347512;');
INSERT INTO `creditlogs` VALUES ('253', 'austin998', '2020-10-05 14:19:15', 'TRANSFER_SBAIN', '8763.12', '8741.12', '-22.00', 'referenceNo:2510051419154124382;');
INSERT INTO `creditlogs` VALUES ('254', 'austin998', '2020-10-05 14:19:40', 'TRANSFER_SBAIN', '8741.12', '8719.12', '-22.00', 'referenceNo:2510051420340744864;');
INSERT INTO `creditlogs` VALUES ('255', 'austin998', '2020-10-05 14:22:45', 'TRANSFER_SBAIN', '8719.12', '8697.12', '-22.00', 'referenceNo:2510051422448633680;');
INSERT INTO `creditlogs` VALUES ('256', 'austin998', '2020-10-05 14:24:03', 'TRANSFER_SBAIN', '8697.12', '8675.12', '-22.00', 'referenceNo:2510051424027231452;');
INSERT INTO `creditlogs` VALUES ('257', 'austin998', '2020-10-05 14:31:17', 'TRANSFER_SBAIN', '8675.12', '8653.12', '-22.00', 'referenceNo:2510051431168752139;');
INSERT INTO `creditlogs` VALUES ('258', 'austin998', '2020-10-05 14:31:31', 'TRANSFER_SBAIN', '8653.12', '8631.12', '-22.00', 'referenceNo:2510051433049834247;');
INSERT INTO `creditlogs` VALUES ('259', 'austin998', '2020-10-05 15:08:08', 'TRANSFER_SBAIN', '8631.12', '8609.12', '-22.00', 'referenceNo:2510051508085363313;');
INSERT INTO `creditlogs` VALUES ('260', 'austin998', '2020-10-05 15:08:22', 'TRANSFER_SBAIN', '8609.12', '8409.12', '-200.00', 'referenceNo:2510051508223879700;');
INSERT INTO `creditlogs` VALUES ('261', 'austin998', '2020-10-05 15:12:37', 'TRANSFER_REDRAIN_OUT', '8409.12', '8409.12', '0.00', 'referenceNo:2510051512370429742;红包雨余额转入SBA平台,10倍流水，转入10.0');
INSERT INTO `creditlogs` VALUES ('262', 'austin998', '2020-10-05 15:13:37', 'TRANSFER_REDRAIN_OUT', '8409.12', '8409.12', '0.00', 'referenceNo:2510051513372869889;红包雨余额转入MG平台,10倍流水，转入10.0');
INSERT INTO `creditlogs` VALUES ('263', 'austin998', '2020-10-05 15:20:59', 'TRANSFER_REDRAIN_OUT', '8409.12', '8409.12', '0.00', 'referenceNo:2510051520598370979;红包雨余额转入SBA平台,10倍流水，转入11.0');
INSERT INTO `creditlogs` VALUES ('264', 'kavin998', '2020-10-05 15:34:10', 'TRANSFER_REDRAIN_OUT', '3464.10', '3464.10', '0.00', 'referenceNo:2510051534109666732;红包雨余额转入SBA平台,10倍流水，转入11.0');
INSERT INTO `creditlogs` VALUES ('265', 'kavin998', '2020-10-05 15:34:35', 'TRANSFER_REDRAIN_OUT', '3464.10', '3464.10', '0.00', 'referenceNo:2510051534354061885;红包雨余额转入SBA平台,10倍流水，转入11.0');
INSERT INTO `creditlogs` VALUES ('266', 'kavin998', '2020-10-05 15:38:22', 'TRANSFER_REDRAIN_OUT', '3464.10', '3464.10', '0.00', 'referenceNo:2510051538227345512;红包雨余额转入SBA平台,10倍流水，转入11.0');
INSERT INTO `creditlogs` VALUES ('267', 'kavin998', '2020-10-05 15:41:41', 'TRANSFER_REDRAIN_OUT', '3464.10', '3464.10', '0.00', 'referenceNo:2510051541417749202;红包雨余额转入SBA平台,10倍流水，转入150.0');
INSERT INTO `creditlogs` VALUES ('268', 'austin998', '2020-10-08 18:33:04', 'TRANSFER_DTIN', '8409.12', '8399.12', '-10.00', 'referenceNo:2510081833041560962;');
INSERT INTO `creditlogs` VALUES ('269', 'austin998', '2020-10-08 18:33:14', 'TRANSFER_DTIN', '8399.12', '8389.12', '-10.00', 'referenceNo:2510081833139792441;');
INSERT INTO `creditlogs` VALUES ('270', 'austin998', '2020-10-08 18:41:19', 'TRANSFER_REDRAINOUT', '8389.12', '8378.12', '-11.00', 'referenceNo:2510081841197363470;用户主账户转入红包雨账户11.0元');
INSERT INTO `creditlogs` VALUES ('271', 'austin998', '2020-10-08 18:41:26', 'TRANSFER_REDRAIN_OUT', '8378.12', '8378.12', '0.00', 'referenceNo:2510081841265400997;红包雨余额转入DT平台,0倍流水，转入11.0');

-- ----------------------------
-- Table structure for `creditlog_extend`
-- ----------------------------
DROP TABLE IF EXISTS `creditlog_extend`;
CREATE TABLE `creditlog_extend` (
  `id` int(18) NOT NULL COMMENT 'creditlog表主键',
  `orderId` varchar(30) NOT NULL COMMENT '订单号',
  `createTime` datetime DEFAULT NULL COMMENT '创建时间',
  `remark` varchar(30) DEFAULT NULL COMMENT '备注',
  `ext1` varchar(30) DEFAULT NULL COMMENT '扩展字段',
  `ext2` double(16,2) DEFAULT NULL COMMENT '扩展字段',
  `ext3` int(16) DEFAULT NULL COMMENT '扩展字段',
  `ext4` varchar(30) DEFAULT NULL COMMENT '扩展字段'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of creditlog_extend
-- ----------------------------

-- ----------------------------
-- Table structure for `credit_warnlogs`
-- ----------------------------
DROP TABLE IF EXISTS `credit_warnlogs`;
CREATE TABLE `credit_warnlogs` (
  `id` int(11) NOT NULL,
  `loginname` varchar(100) DEFAULT NULL,
  `createtime` datetime DEFAULT NULL,
  `type` varchar(30) NOT NULL,
  `credit` double(24,2) NOT NULL DEFAULT '0.00',
  `newCredit` double(24,2) NOT NULL DEFAULT '0.00',
  `remit` double(24,2) NOT NULL DEFAULT '0.00',
  `remark` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`) USING BTREE,
  KEY `loginname` (`loginname`) USING BTREE,
  KEY `createtime` (`createtime`) USING BTREE,
  KEY `type` (`type`) USING BTREE,
  KEY `remit` (`remit`) USING BTREE,
  KEY `remark` (`remark`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of credit_warnlogs
-- ----------------------------

-- ----------------------------
-- Table structure for `deposit_order`
-- ----------------------------
DROP TABLE IF EXISTS `deposit_order`;
CREATE TABLE `deposit_order` (
  `depositId` varchar(300) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '存款随机码（也为主键）',
  `loginname` varchar(255) DEFAULT NULL,
  `bankname` varchar(255) DEFAULT NULL COMMENT '支付宝、招商银行、广发银行',
  `bankno` varchar(255) DEFAULT NULL COMMENT '银行卡号',
  `status` int(10) NOT NULL DEFAULT '0' COMMENT '0.未处理1.已处理',
  `createtime` datetime DEFAULT NULL,
  `updatetime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `remark` varchar(255) DEFAULT NULL,
  `spare` varchar(255) DEFAULT NULL COMMENT '备用字段',
  `spare1` varchar(255) DEFAULT NULL COMMENT '备用字段',
  `accountname` varchar(255) DEFAULT NULL COMMENT '开户人',
  `ubankname` varchar(100) DEFAULT NULL COMMENT '玩家银行卡名称',
  `uaccountname` varchar(100) DEFAULT NULL COMMENT '玩家持卡人姓名',
  `amount` double(32,2) DEFAULT NULL COMMENT '存款金额',
  `ubankno` varchar(100) DEFAULT NULL COMMENT '玩家存款卡号',
  `flag` int(8) DEFAULT '1' COMMENT '删除标示1 使用 0弃用',
  `type` varchar(32) DEFAULT NULL COMMENT '存款类别',
  `realname` varchar(64) DEFAULT NULL COMMENT '玩家注册姓名',
  PRIMARY KEY (`depositId`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of deposit_order
-- ----------------------------

-- ----------------------------
-- Table structure for `experience_gold_t`
-- ----------------------------
DROP TABLE IF EXISTS `experience_gold_t`;
CREATE TABLE `experience_gold_t` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `title` varchar(50) NOT NULL COMMENT '优惠名称',
  `alias_Title` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `amount` double(10,0) DEFAULT NULL COMMENT '体验金额度',
  `max_money` double(10,0) DEFAULT NULL COMMENT '限制最大金额',
  `min_money` double(10,0) DEFAULT NULL COMMENT '限制最小金额',
  `is_used` int(11) NOT NULL DEFAULT '0',
  `start_time` datetime DEFAULT NULL,
  `end_time` datetime DEFAULT NULL,
  `times` int(11) DEFAULT '20',
  `times_flag` int(11) DEFAULT '1' COMMENT '1.天2.周3.月4.年',
  `vip` varchar(20) DEFAULT NULL,
  `machine_code_enabled` int(10) DEFAULT NULL COMMENT '是否启用机器码验证，0:否/1:是',
  `machine_code_times` int(10) DEFAULT NULL COMMENT '机器码使用次数',
  `platform_name` varchar(50) DEFAULT NULL COMMENT '游戏平台',
  `is_phone` varchar(50) DEFAULT NULL COMMENT '申请通道 1官网、2WEB、3安卓APP、4苹果APP',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of experience_gold_t
-- ----------------------------
INSERT INTO `experience_gold_t` VALUES ('1', '自助体验金', 'PT自助体验金', '2018-12-24 14:12:12', '2018-12-24 14:14:40', '38', '100', '1', '1', '2018-12-26 00:00:00', '2022-12-24 00:00:00', '1', '4', '0,1,2,3,4,5,6,7,8', '1', '1', 'PT', '1,2,3,4');
INSERT INTO `experience_gold_t` VALUES ('2', '自助体验金', 'SW自助体验金', '2018-12-24 14:16:18', '2018-12-24 14:16:22', '38', '388', '1', '1', '2018-12-26 00:00:00', '2022-12-24 00:00:00', '1', '4', '0,1,2,3,4,5,6,7,8', '1', '1', 'SW', '1,2,3,4');
INSERT INTO `experience_gold_t` VALUES ('3', '自助体验金', 'MG自助体验金', '2018-12-24 14:17:30', '2018-12-24 14:17:36', '38', '388', '1', '1', '2018-12-26 00:00:00', '2020-12-24 00:00:00', '1', '4', '0,1,2,3,4,5,6,7,8', '1', '1', 'MG', '1,2,3,4');
INSERT INTO `experience_gold_t` VALUES ('4', '自助体验金', 'DT自助体验金', '2018-12-24 14:19:30', '2018-12-24 14:19:34', '38', '388', '1', '1', '2018-12-26 00:00:00', '2020-12-24 00:00:00', '1', '4', '0,1,2,3,4,5,6,7,8', '1', '1', 'DT', '1,2,3,4');
INSERT INTO `experience_gold_t` VALUES ('5', '自助体验金', 'QT自助体验金', '2018-12-24 14:23:16', '2018-12-24 14:23:19', '38', '388', '1', '1', '2018-12-26 00:00:00', '2022-12-24 00:00:00', '1', '4', '0,1,2,3,4,5,6,7,8', '1', '1', 'QT', '1,2,3,4');
INSERT INTO `experience_gold_t` VALUES ('6', '自助体验金', 'PNG自助体验金', '2018-12-24 14:44:35', '2018-12-24 14:44:39', '38', '388', '1', '1', '2018-12-26 00:00:00', '2022-12-24 00:00:00', '1', '4', '0,1,2,3,4,5,6,7,8', '1', '1', 'PNG', '1,2,3,4');
INSERT INTO `experience_gold_t` VALUES ('7', '自助体验金', 'TTG自助体验金', '2018-12-24 14:45:14', '2018-12-24 14:45:18', '38', '100', '1', '1', '2018-12-26 00:00:00', '2022-12-24 00:00:00', '1', '4', '0,1,2,3,4,5,6,7,8', '1', '1', 'TTG', '1,2,3,4');

-- ----------------------------
-- Table structure for `friendbonus`
-- ----------------------------
DROP TABLE IF EXISTS `friendbonus`;
CREATE TABLE `friendbonus` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `toplineuser` varchar(32) DEFAULT NULL,
  `money` double(32,2) DEFAULT NULL COMMENT '奖励积分',
  `createtime` datetime DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of friendbonus
-- ----------------------------

-- ----------------------------
-- Table structure for `friendbonusrecord`
-- ----------------------------
DROP TABLE IF EXISTS `friendbonusrecord`;
CREATE TABLE `friendbonusrecord` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `toplineuser` varchar(32) DEFAULT NULL,
  `downlineuser` varchar(32) DEFAULT NULL,
  `type` varchar(2) DEFAULT NULL COMMENT '类型，1收入 2支出',
  `money` double(24,2) DEFAULT NULL COMMENT '奖励金额',
  `createtime` datetime DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  `distributedate` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of friendbonusrecord
-- ----------------------------

-- ----------------------------
-- Table structure for `guestbook`
-- ----------------------------
DROP TABLE IF EXISTS `guestbook`;
CREATE TABLE `guestbook` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `referenceId` int(11) DEFAULT NULL COMMENT '引用id如果不等于null',
  `adminname` varchar(50) DEFAULT NULL,
  `username` varchar(50) DEFAULT NULL,
  `title` varchar(50) DEFAULT NULL COMMENT '留言标题',
  `rcount` int(11) DEFAULT NULL COMMENT '回复擿',
  `content` varchar(2000) DEFAULT '' COMMENT '留言内容',
  `ipaddress` varchar(30) DEFAULT NULL COMMENT '留言IP',
  `email` varchar(50) DEFAULT NULL COMMENT 'email',
  `phone` varchar(50) DEFAULT NULL COMMENT '电话号码',
  `qq` varchar(20) DEFAULT NULL COMMENT 'qq',
  `createdate` datetime DEFAULT NULL,
  `flag` int(1) DEFAULT '1',
  `adminstatus` bigint(20) DEFAULT '0' COMMENT '0表示未读状态 1表示已读状态',
  `userstatus` bigint(20) DEFAULT '0' COMMENT '0表示未读状态 1表示已读状态',
  `updateid` varchar(100) DEFAULT NULL,
  `isadmin` bigint(1) DEFAULT '0' COMMENT '0表示管理员发帖 1表示用户发帖',
  `message` int(1) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of guestbook
-- ----------------------------

-- ----------------------------
-- Table structure for `guild`
-- ----------------------------
DROP TABLE IF EXISTS `guild`;
CREATE TABLE `guild` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `part` varchar(13) NOT NULL COMMENT '工会分组',
  `president` varchar(20) DEFAULT NULL COMMENT '工会会长',
  `name` varchar(50) NOT NULL COMMENT '工会名字',
  `start_time` date DEFAULT NULL COMMENT '开始报名时间',
  `end_time` date DEFAULT NULL COMMENT '结束报名时间',
  `level` varchar(28) DEFAULT NULL COMMENT '用户等级',
  `max` int(6) DEFAULT NULL COMMENT '工会上限人数',
  `game` varchar(30) DEFAULT NULL COMMENT '游戏平台',
  `state` int(4) DEFAULT NULL COMMENT '状态,0表示未开启，1表示开启',
  `createTime` date DEFAULT NULL COMMENT '创建时间',
  `updateTime` date DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(100) DEFAULT NULL COMMENT '备注',
  `creator` varchar(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of guild
-- ----------------------------

-- ----------------------------
-- Table structure for `guild_record`
-- ----------------------------
DROP TABLE IF EXISTS `guild_record`;
CREATE TABLE `guild_record` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `guild_id` int(20) NOT NULL COMMENT '工会id',
  `deposet` double(20,2) DEFAULT NULL COMMENT '总存款金额',
  `game_amount` double(20,2) DEFAULT NULL COMMENT '游戏总流水',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of guild_record
-- ----------------------------

-- ----------------------------
-- Table structure for `guild_staff`
-- ----------------------------
DROP TABLE IF EXISTS `guild_staff`;
CREATE TABLE `guild_staff` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `guild_id` int(20) NOT NULL COMMENT '工会id',
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `join_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '报名时间',
  `deposet` double(20,2) DEFAULT '0.00' COMMENT '总存款金额',
  `game_amount` double(20,2) DEFAULT '0.00' COMMENT '游戏总流水',
  `state` int(4) DEFAULT NULL COMMENT '状态',
  `remark` varchar(100) DEFAULT NULL,
  `updatetime` timestamp NULL DEFAULT NULL COMMENT '更新时间',
  `rangeDeposet` double(20,2) DEFAULT '0.00',
  `rangeGameAmount` double(20,2) DEFAULT '0.00',
  `name` varchar(50) DEFAULT NULL,
  `day` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of guild_staff
-- ----------------------------

-- ----------------------------
-- Table structure for `guild_user`
-- ----------------------------
DROP TABLE IF EXISTS `guild_user`;
CREATE TABLE `guild_user` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `guild_id` int(20) NOT NULL COMMENT '工会id',
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `join_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '报名时间',
  `state` int(4) DEFAULT NULL COMMENT '状态',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of guild_user
-- ----------------------------

-- ----------------------------
-- Table structure for `g_gameinfo`
-- ----------------------------
DROP TABLE IF EXISTS `g_gameinfo`;
CREATE TABLE `g_gameinfo` (
  `id` int(9) NOT NULL AUTO_INCREMENT,
  `starttime` datetime NOT NULL COMMENT '牌局起始时间',
  `endtime` datetime NOT NULL COMMENT '局牌结束时间',
  `addtime` datetime NOT NULL COMMENT '加添时间',
  `code` varchar(20) NOT NULL COMMENT '牌局编号',
  `gamecode` varchar(10) NOT NULL COMMENT '戏游码代',
  `loginname` varchar(30) NOT NULL,
  `betamount` double(8,2) DEFAULT '0.00' COMMENT '注投额度',
  `hold` double(8,2) DEFAULT '0.00' COMMENT '赢值输',
  `validbetamount` double(8,2) DEFAULT '0.00' COMMENT '有效投注额',
  `result` varchar(50) DEFAULT '' COMMENT '牌局结果',
  `flag` int(1) NOT NULL DEFAULT '0' COMMENT '否是结算；默认为0 未结算，1 已结算',
  `betdetail` varchar(500) DEFAULT '' COMMENT '记录玩家的投注情况，如：闲100|庄50|...',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of g_gameinfo
-- ----------------------------

-- ----------------------------
-- Table structure for `g_record`
-- ----------------------------
DROP TABLE IF EXISTS `g_record`;
CREATE TABLE `g_record` (
  `id` int(7) NOT NULL AUTO_INCREMENT,
  `path` varchar(50) NOT NULL,
  `addtime` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `g_record_path` (`path`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of g_record
-- ----------------------------

-- ----------------------------
-- Table structure for `hb_bonus`
-- ----------------------------
DROP TABLE IF EXISTS `hb_bonus`;
CREATE TABLE `hb_bonus` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(64) DEFAULT NULL,
  `money` double DEFAULT NULL,
  `updatetime` datetime DEFAULT NULL,
  `remark` varchar(500) DEFAULT NULL,
  `version` int(11) DEFAULT '0',
  `phone` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of hb_bonus
-- ----------------------------

-- ----------------------------
-- Table structure for `hb_config`
-- ----------------------------
DROP TABLE IF EXISTS `hb_config`;
CREATE TABLE `hb_config` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `title` varchar(50) NOT NULL COMMENT '优惠名称',
  `betMultiples` int(20) DEFAULT '15' COMMENT '流水倍数要求',
  `limitStartMoney` double DEFAULT NULL COMMENT '存款下限金额',
  `limitEndMoney` double DEFAULT NULL COMMENT '存款上限金额',
  `createtime` datetime DEFAULT NULL COMMENT '创建时间',
  `updatetime` datetime DEFAULT NULL COMMENT '更新时间',
  `amount` double DEFAULT NULL COMMENT '红包金额',
  `isused` int(11) NOT NULL DEFAULT '0' COMMENT '是否启用',
  `starttime` datetime DEFAULT NULL COMMENT '启用开始时间',
  `endtime` datetime DEFAULT NULL COMMENT '启用结束时间',
  `times` int(11) DEFAULT '1' COMMENT '可使用次数',
  `timesflag` int(11) DEFAULT '1' COMMENT '1.天2.周3.月4.年',
  `vip` varchar(20) DEFAULT NULL,
  `type` int(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of hb_config
-- ----------------------------
INSERT INTO `hb_config` VALUES ('23', '存200免费领8元', null, '200', '500', '2018-12-23 13:22:24', '2018-12-24 11:04:55', '8', '1', '2018-12-26 00:00:00', '2020-12-23 00:00:00', '1', '1', '012345678', '0');
INSERT INTO `hb_config` VALUES ('24', '存500免费领18元', '1', '500', '1000', '2018-12-23 14:04:27', '2018-12-24 11:04:53', '18', '1', '2018-12-26 00:00:00', '2020-12-23 00:00:00', '1', '1', '012345678', '0');
INSERT INTO `hb_config` VALUES ('25', '存1000免费领38元', '1', '1000', '5000', '2018-12-23 14:38:42', '2018-12-24 11:04:51', '38', '1', '2018-12-26 00:00:00', '2020-12-23 00:00:00', '1', '1', '012345678', '0');
INSERT INTO `hb_config` VALUES ('26', '存5000免费领188元', '1', '5000', '50000', '2018-12-24 11:04:36', '2018-12-24 11:04:49', '188', '1', '2018-12-26 00:00:00', '2020-12-23 00:00:00', '1', '1', '012345678', '0');

-- ----------------------------
-- Table structure for `hb_record`
-- ----------------------------
DROP TABLE IF EXISTS `hb_record`;
CREATE TABLE `hb_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `hbid` int(11) DEFAULT NULL,
  `username` varchar(64) DEFAULT NULL,
  `type` varchar(2) DEFAULT NULL,
  `money` double DEFAULT NULL,
  `updatetime` datetime DEFAULT NULL,
  `remark` varchar(500) DEFAULT NULL,
  `deposit` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of hb_record
-- ----------------------------

-- ----------------------------
-- Table structure for `hd_image`
-- ----------------------------
DROP TABLE IF EXISTS `hd_image`;
CREATE TABLE `hd_image` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `imageName` varchar(255) DEFAULT NULL COMMENT '活动名称',
  `image001` varchar(255) DEFAULT NULL COMMENT '官网首页小图',
  `image002` varchar(255) DEFAULT NULL COMMENT '优惠活动小图',
  `image003` varchar(255) DEFAULT NULL COMMENT '优惠活动大图',
  `image004` varchar(255) DEFAULT NULL,
  `imageStatus` int(1) DEFAULT '0' COMMENT '0表示不显示 1表示显示',
  `imageIp` varchar(255) DEFAULT NULL COMMENT '服务器更新ip',
  `createDate` datetime DEFAULT NULL,
  `imageStart` datetime DEFAULT NULL,
  `imageEnd` datetime DEFAULT NULL,
  `imageType` int(11) DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of hd_image
-- ----------------------------

-- ----------------------------
-- Table structure for `icbc_logs`
-- ----------------------------
DROP TABLE IF EXISTS `icbc_logs`;
CREATE TABLE `icbc_logs` (
  `log_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `card_num` char(19) NOT NULL,
  `type` tinyint(3) unsigned NOT NULL COMMENT '1登录2到期',
  `date` datetime NOT NULL,
  PRIMARY KEY (`log_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='登录到期日志';

-- ----------------------------
-- Records of icbc_logs
-- ----------------------------

-- ----------------------------
-- Table structure for `icbc_transfers`
-- ----------------------------
DROP TABLE IF EXISTS `icbc_transfers`;
CREATE TABLE `icbc_transfers` (
  `transfer_id` int(11) NOT NULL AUTO_INCREMENT,
  `currserial_num` varchar(30) DEFAULT '',
  `name` char(50) NOT NULL,
  `card_num` varchar(19) NOT NULL DEFAULT '',
  `area` varchar(100) NOT NULL,
  `amount` decimal(14,4) unsigned NOT NULL COMMENT '充值金额',
  `fee` decimal(14,4) unsigned NOT NULL DEFAULT '0.0000' COMMENT '手续费',
  `notes` varchar(100) NOT NULL DEFAULT '' COMMENT '附言',
  `accept_name` char(20) NOT NULL,
  `accept_card_num` varchar(19) NOT NULL DEFAULT '',
  `accept_area` varchar(100) NOT NULL,
  `pay_date` datetime NOT NULL COMMENT '付款时间',
  `admin_id` smallint(5) unsigned NOT NULL COMMENT '管理员ID',
  `status` tinyint(3) unsigned NOT NULL COMMENT '0未处理 1已充值 2充值超时处理',
  `date` datetime NOT NULL COMMENT '记录添加时间',
  `timecha` varchar(50) DEFAULT NULL,
  `overtime` int(1) DEFAULT '0',
  PRIMARY KEY (`transfer_id`),
  UNIQUE KEY `pay_date` (`pay_date`,`accept_card_num`,`card_num`,`name`,`amount`) USING BTREE,
  KEY `status` (`status`) USING BTREE,
  KEY `currserial_num` (`currserial_num`) USING BTREE,
  KEY `accept_card_num` (`accept_card_num`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='转账记录表';

-- ----------------------------
-- Records of icbc_transfers
-- ----------------------------

-- ----------------------------
-- Table structure for `iesnare`
-- ----------------------------
DROP TABLE IF EXISTS `iesnare`;
CREATE TABLE `iesnare` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `loginname` varchar(20) NOT NULL COMMENT '登录名',
  `device` varchar(255) NOT NULL COMMENT 'IESanre返回的设备标识',
  `ip` varchar(32) DEFAULT NULL COMMENT 'IP',
  `remark` varchar(64) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of iesnare
-- ----------------------------

-- ----------------------------
-- Table structure for `innodb_monitor`
-- ----------------------------
DROP TABLE IF EXISTS `innodb_monitor`;
CREATE TABLE `innodb_monitor` (
  `a` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of innodb_monitor
-- ----------------------------

-- ----------------------------
-- Table structure for `internal_agency`
-- ----------------------------
DROP TABLE IF EXISTS `internal_agency`;
CREATE TABLE `internal_agency` (
  `loginname` varchar(255) NOT NULL,
  `type` int(5) DEFAULT NULL,
  `createtime` datetime DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`loginname`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of internal_agency
-- ----------------------------

-- ----------------------------
-- Table structure for `intransfer`
-- ----------------------------
DROP TABLE IF EXISTS `intransfer`;
CREATE TABLE `intransfer` (
  `pno` varchar(20) NOT NULL DEFAULT '',
  `wherefrom` varchar(30) DEFAULT NULL,
  `whereto` varchar(30) DEFAULT NULL,
  `amount` double(12,2) DEFAULT '0.00',
  `createtime` datetime DEFAULT NULL,
  `operator` varchar(30) DEFAULT NULL,
  `remark` varchar(150) DEFAULT NULL,
  `flag` int(1) DEFAULT '0',
  `fromto` varchar(50) DEFAULT NULL,
  `fee` double(8,2) DEFAULT '0.00',
  `transferflag` int(1) DEFAULT '0' COMMENT '0手工转账 1自动转账',
  PRIMARY KEY (`pno`),
  KEY `from` (`wherefrom`) USING BTREE,
  KEY `to` (`whereto`) USING BTREE,
  KEY `flag` (`flag`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of intransfer
-- ----------------------------

-- ----------------------------
-- Table structure for `jc_data`
-- ----------------------------
DROP TABLE IF EXISTS `jc_data`;
CREATE TABLE `jc_data` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `PLAYERNAME` varchar(50) DEFAULT NULL,
  `STARTTIME` datetime DEFAULT NULL,
  `ENDTIME` datetime DEFAULT NULL,
  `ORDERSUM` double(20,0) DEFAULT NULL,
  `ACTUAL` double(50,2) DEFAULT NULL,
  `BONUS` double(50,2) DEFAULT NULL,
  `WIN` double(50,2) DEFAULT NULL,
  `CREATETIME` datetime DEFAULT NULL,
  `UPDATETIME` datetime DEFAULT NULL,
  `VIPLEVEL` int(5) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=379 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of jc_data
-- ----------------------------

-- ----------------------------
-- Table structure for `keyword`
-- ----------------------------
DROP TABLE IF EXISTS `keyword`;
CREATE TABLE `keyword` (
  `value` varchar(255) NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of keyword
-- ----------------------------

-- ----------------------------
-- Table structure for `latest_preferential`
-- ----------------------------
DROP TABLE IF EXISTS `latest_preferential`;
CREATE TABLE `latest_preferential` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号，主键，自动增长',
  `type` varchar(3) NOT NULL COMMENT '优惠类型',
  `activity_title` varchar(100) NOT NULL COMMENT '活动标题',
  `activity_summary` varchar(500) NOT NULL COMMENT '活动简介',
  `activity_content` text COMMENT '活动内容',
  `activity_image_url` varchar(100) DEFAULT NULL COMMENT '活动图片路径',
  `activity_start_time` datetime DEFAULT NULL COMMENT '活动开始时间',
  `activity_end_time` datetime DEFAULT NULL COMMENT '活动结束时间',
  `is_new` char(1) DEFAULT NULL COMMENT '是否最新优惠，0:否 1:是',
  `new_image_url` varchar(100) DEFAULT NULL COMMENT '最新优惠图片路径',
  `is_active` char(1) NOT NULL COMMENT '是否开启，0:禁用 1:开启',
  `is_phone` char(1) DEFAULT NULL COMMENT '是否手机端优惠，0:否 1:是',
  `receive_number` int(11) DEFAULT NULL COMMENT '领取人数',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `created_user` varchar(20) NOT NULL COMMENT '创建人',
  `update_time` datetime NOT NULL COMMENT '修改时间',
  `updated_user` varchar(20) NOT NULL COMMENT '修改人',
  `openUrl` varchar(1000) NOT NULL COMMENT '龙八没有此字段，千亿的专题优惠，论坛优惠需要跳转到网页，该字段为网页URL',
  `isQyStyle` char(1) DEFAULT NULL COMMENT '加入到千亿风采',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of latest_preferential
-- ----------------------------

-- ----------------------------
-- Table structure for `losepromo`
-- ----------------------------
DROP TABLE IF EXISTS `losepromo`;
CREATE TABLE `losepromo` (
  `pno` varchar(20) NOT NULL COMMENT '提案号',
  `username` varchar(16) NOT NULL COMMENT '用户名',
  `amount` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '输赢额',
  `deduct` decimal(8,2) NOT NULL DEFAULT '0.00' COMMENT '扣款（已领取的优惠）',
  `rate` decimal(2,2) NOT NULL COMMENT '反赠比例',
  `promo` decimal(8,2) NOT NULL,
  `times` tinyint(4) DEFAULT '10' COMMENT '提款所需流水倍数',
  `status` char(1) NOT NULL DEFAULT '0' COMMENT '状态（0：未领取   1：已领取  2：已处理  3:已取消）',
  `betting` decimal(10,2) DEFAULT NULL COMMENT '已投注额(0点到领取优惠时PT游戏总投注额)',
  `creattime` datetime DEFAULT NULL COMMENT '创建时间',
  `gettime` datetime DEFAULT NULL COMMENT '领取时间',
  `promodate` varchar(12) NOT NULL COMMENT '反赠日期（派发的是哪一天的反赠）',
  `platform` varchar(16) NOT NULL COMMENT ' 游戏平台',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`pno`),
  UNIQUE KEY `uc_promo` (`username`,`promodate`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of losepromo
-- ----------------------------

-- ----------------------------
-- Table structure for `lottery_item`
-- ----------------------------
DROP TABLE IF EXISTS `lottery_item`;
CREATE TABLE `lottery_item` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` varchar(50) NOT NULL,
  `item_name` varchar(50) NOT NULL,
  `percent` double NOT NULL DEFAULT '0',
  `period` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of lottery_item
-- ----------------------------

-- ----------------------------
-- Table structure for `lucky_draw_present`
-- ----------------------------
DROP TABLE IF EXISTS `lucky_draw_present`;
CREATE TABLE `lucky_draw_present` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '编号，主键，自动增长',
  `title` varchar(50) DEFAULT NULL COMMENT '显示名称',
  `image_url` varchar(100) DEFAULT NULL COMMENT '显示图片',
  `points_present_id` int(10) DEFAULT NULL COMMENT '商品编号，对应points_present表的id',
  `points_present_type` varchar(50) DEFAULT NULL COMMENT '商品类型，对应points_present表的type',
  `property` text COMMENT '抽奖属性',
  `weight` int(10) DEFAULT NULL COMMENT '权重',
  `create_user` varchar(50) NOT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_user` varchar(50) NOT NULL COMMENT '修改人',
  `update_time` datetime NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of lucky_draw_present
-- ----------------------------

-- ----------------------------
-- Table structure for `member_signrecord`
-- ----------------------------
DROP TABLE IF EXISTS `member_signrecord`;
CREATE TABLE `member_signrecord` (
  `loginname` varchar(30) NOT NULL,
  `flag` int(1) NOT NULL DEFAULT '0' COMMENT '登录状态；0 已登录，1 退出',
  PRIMARY KEY (`loginname`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of member_signrecord
-- ----------------------------
INSERT INTO `member_signrecord` VALUES ('kavin123', '1');
INSERT INTO `member_signrecord` VALUES ('kavin998', '1');
INSERT INTO `member_signrecord` VALUES ('skytest', '1');
INSERT INTO `member_signrecord` VALUES ('test01', '1');
INSERT INTO `member_signrecord` VALUES ('twtest01', '1');
INSERT INTO `member_signrecord` VALUES ('twtest02', '1');
INSERT INTO `member_signrecord` VALUES ('wadetest', '1');
INSERT INTO `member_signrecord` VALUES ('windtest', '1');

-- ----------------------------
-- Table structure for `merchantpay`
-- ----------------------------
DROP TABLE IF EXISTS `merchantpay`;
CREATE TABLE `merchantpay` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `merchantcode` varchar(32) DEFAULT NULL COMMENT '商户号',
  `createtime` datetime DEFAULT NULL COMMENT '创建时间',
  `updatetime` datetime DEFAULT NULL COMMENT '修改时间',
  `updateby` varchar(32) DEFAULT NULL COMMENT '修改操作人',
  `payname` varchar(255) DEFAULT NULL COMMENT '商户名称',
  `type` int(11) DEFAULT NULL COMMENT '支付类型标识码',
  `signkey` varchar(2048) DEFAULT NULL COMMENT 'key值',
  `notifyurl` varchar(256) DEFAULT NULL COMMENT '回调url',
  `shopurl` varchar(256) DEFAULT NULL COMMENT '商城url',
  `fee` double(32,4) DEFAULT NULL COMMENT '自身扣除费率 微信0.992 网银 支付宝0.991',
  `payswitch` int(2) DEFAULT NULL COMMENT '支付开关',
  `amountCut` int(2) DEFAULT NULL COMMENT '1000+',
  `payplatform` varchar(24) DEFAULT NULL COMMENT '支付名称简写',
  `phonefee` double(32,4) DEFAULT NULL COMMENT '支付平台费率 手机端费率(2.5%对应97.5)',
  `pcfee` double(32,4) DEFAULT NULL COMMENT '支付平台费率 pc端费率(1.5%对应98.5)',
  `minpay` double(24,2) DEFAULT NULL COMMENT '最低支付金额',
  `maxpay` double(24,2) DEFAULT NULL COMMENT '最高支付金额',
  `levels` varchar(24) DEFAULT NULL COMMENT '允许支付等级',
  `apiUrl` varchar(256) DEFAULT NULL COMMENT '第三方支付接口url',
  `systemCode` varchar(20) DEFAULT NULL COMMENT '系统编码，比如ws',
  `appPay` int(2) DEFAULT '0' COMMENT '是否调用客户端 0否 1是',
  `payUrl` varchar(80) DEFAULT NULL COMMENT '第三方或者商城支付调用地址',
  `payWay` int(3) DEFAULT NULL COMMENT '支付方式，1.支付宝 2.微信 3.在线支付 4.快捷支付 5.点卡支付',
  `payCenterUrl` varchar(128) DEFAULT NULL COMMENT '支付中心接口URL',
  `remain` varchar(3000) DEFAULT NULL COMMENT '接口请求的留余字段，json格式',
  `useable` int(1) DEFAULT NULL,
  `remark` varchar(200) DEFAULT NULL,
  `as_name` varchar(100) DEFAULT '',
  `amount` double(20,2) DEFAULT '0.00',
  `usetype` int(11) DEFAULT '3',
  `show_name` varchar(24) DEFAULT '' COMMENT '显示名字',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=85 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of merchantpay
-- ----------------------------
INSERT INTO `merchantpay` VALUES ('1', '17537', '2018-04-06 14:59:28', '2018-04-06 14:59:31', null, '高通QQ', '0', 'f3a51513481858c9a6955f34209b037b', 'http://pcb.dayungame.vip:2080/third/rt.php?rt=/gt/zfb_wx_return', 'http://www.gdchunkaiwlkj.com/paySuccess_s.jsp', '0.0000', '1', '0', 'gtqq', '0.0000', '0.0000', '10.00', '5000.00', '6', 'http://tj.gaotongpay.com/PayBank.aspx', 'tw', '0', 'http://tj.gaotongpay.com/PayBank.aspx\r\n', '7', '/gt/zfb_wx', '{\"pc\":\"QQPAY\",\"wap\":\"QQPAYWAP\"}', '1', '', '', '0.00', '3', 'QQ钱包支付');
INSERT INTO `merchantpay` VALUES ('2', '100000000003664', '2018-05-27 20:10:00', '2018-05-27 13:16:17', '', '信付通支付宝', '0', 'vmpP7aGZymtdjkhHXmcOTtkPlaKCeyVGsrcD2hLVq4i73ThgA7Pn9yQa081weOO8', 'http://pcb.dayungame.vip:2080/third/rt.php?rt=/xft/online_pay_return', '', '0.0000', '1', '0', 'xftzfb', '0.0000', '0.0000', '2.00', '3000.00', '6', 'https://ebank.xfuoo.com/payment/v1/order/', 'tw', '0', 'https://ebank.xfuoo.com/payment/v1/order/', '1', '/xft/online_pay', '{\"charset\":\"UTF-8\",\"paymentType\":\"1\",\"defaultbank\":\"ALIPAY\",\"paymethod\":\"directPay\",\"service\":\"online_pay\",\"signType\":\"SHA\",\"pc\":\"web\",\"isApp\":\"H5\"}', '1', null, null, '0.00', '3', '支付宝支付');
INSERT INTO `merchantpay` VALUES ('3', '6000030004', '2017-02-01 09:40:14', '2018-06-03 10:38:57', '', '速汇宝微信', '0', 'MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALqgWK86Gs2niBaPwaIpB/jKmpBNwdf6GZhRRTCU028k6m9BY4QqUxL8Wq+3+i86+JfAAqUkKuaLnCvT2EKW/IdJmpUuNASHCK7v7I7aCDYyJab8ZVW2JBrEGbbCDrFt+qY8qRt8MgSie2mWyELud+JJCXfq00gOPiw66h2uc5ilAgMBAAECgYAJt1dmqAvFMTewlQNrwd8cYM/zO1kgOXEjsPd9NAJeAvoBmNHpi5qPnnvKMjXgR52xqNl6Z4ukPRiAX6MetGWpPqvGu6IayOmonG0q2b00Ke/i6e20dgWU9xdYaKXCSrhhl2V9f9/JOnAMilFGofx9utgbJuaFPAkQNflwOCkJAQJBAOdtph6ciLw1oGORUy1/fXr1knwQCn2IZ9JbwVRVP2lun560+/vnRoLfkQ7g47CIIT11dHw1O1b4JLACrrVgblUCQQDOcPSB36FhczSBuAYI91JwqBAp2l3ZUViO+uuGeLMbdJfj8yfNU10EzXEc7QDCp3J24eTlCcgY8HpCc7lINjERAkEAyGuZFaGSeQwoHpJYamqijL3nYjGyJAzhfLehshsxk2kOKy+pmfosgmKDCVORW+xIfSUfAAJJpYO4ogVwr/sXEQJADccwKnLYfH6J5JZ5oKORFv/AhGqUlOp+9Jv+mCz6Fz0C3i3eXC/SJ33mrL2MrXErjI+rDTZH6pIyNrkuholHYQJBAM8LZs+EMGsTwWFqYK+RROcIVpMFHRuoUPH9PhHmeFAoVs/meiT8CSoI3nVjUGL3M1Le7mutW2jYMFNIFx/44Jo=', 'http://pcb.dayungame.vip:2080/third/rt.php?rt=/shb/zfb_wx_return.php', 'http://www.gztonglfzscl.com', '0.0000', '1', '0', 'shbwx', '0.0000', '0.0000', '1.00', '3000.00', '6', '', 'tw', '0', 'https://api.zfbill.net/gateway/api/scanpay', '2', '/shb/zfb_wx', '{\"input_charset\":\"UTF-8\",\"interface_version\":\"V3.1\",\"sign_type\":\"RSA-S\",\"service_type\":\"weixin_scan\",\"zfPublicKey\":\"MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCCNaCudrEWtwwCFDLze6MG+vGuLADJNHPqOmhZxSgPCewqd9xlnUrLal+hvSLUvdigkqoS0wL1Csd4RQb9dLLd/lz2+qBUFMHMVXxvo4ECKFy8ugCT3x/hY+zEfy8FVW1gv5Jrgsxk3bbkuHIo3VN8PqHCmUyvNcbE0kapmQI5RQIDAQAB\"}', '1', null, null, '0.00', '3', '微信支付5');
INSERT INTO `merchantpay` VALUES ('4', '17537', '2018-04-06 14:59:28', '2018-04-06 14:59:31', '', '高通支付宝', '0', 'f3a51513481858c9a6955f34209b037b', 'http://pcb.dayungame.vip:2080/third/rt.php?rt=/gt/zfb_wx_return', 'http://www.gdchunkaiwlkj.com/paySuccess_s.jsp', '0.0000', '1', '0', 'gtzfb', '0.0000', '0.0000', '10.00', '5000.00', '6', 'http://tj.gaotongpay.com/PayBank.aspx', 'tw', '0', 'http://tj.gaotongpay.com/PayBank.aspx\r\n', '1', '/gt/zfb_wx', '{\"pc\":\"ALIPAY\",\"wap\":\"ALIPAYWAP\"}', '1', '', '', '0.00', '3', '支付宝支付');
INSERT INTO `merchantpay` VALUES ('5', '17537', '2018-04-06 14:59:28', '2018-04-06 14:59:31', '', '高通京东', '0', 'f3a51513481858c9a6955f34209b037b', 'http://pcb.dayungame.vip:2080/third/rt.php?rt=/gt/zfb_wx_return', 'http://www.gdchunkaiwlkj.com/paySuccess_s.jsp', '0.0000', '1', '0', 'gtjd', '0.0000', '0.0000', '10.00', '1000.00', '6', 'http://tj.gaotongpay.com/PayBank.aspx', 'tw', '0', 'http://tj.gaotongpay.com/PayBank.aspx\r\n', '10', '/gt/zfb_wx', '{\"pc\":\"JDPAY\",\"wap\":\"JDPAYWAP\"}', '1', '', '', '0.00', '3', '京东支付1');
INSERT INTO `merchantpay` VALUES ('6', '17537', '2018-04-06 14:59:28', '2018-04-06 14:59:31', '', '高通银联', '0', 'f3a51513481858c9a6955f34209b037b', 'http://pcb.dayungame.vip:2080/third/rt.php?rt=/gt/zfb_wx_return', 'http://www.gdchunkaiwlkj.com/paySuccess_s.jsp', '0.0000', '1', '0', 'gtyl', '0.0000', '0.0000', '10.00', '5000.00', '6', 'http://tj.gaotongpay.com/PayBank.aspx', 'tw', '0', 'http://tj.gaotongpay.com/PayBank.aspx\r\n', '13', '/gt/zfb_wx', '{\"pc\":\"UNIONPAY\",\"wap\":\"UNIONPAY\"}', '1', '', '', '0.00', '3', '银联支付');
INSERT INTO `merchantpay` VALUES ('7', '18030423104438', '2018-05-31 20:10:00', '2018-05-31 20:10:00', '', '百盛银联', '0', '50def9ed401f1c4ad9a69e70e2fc7d06', 'http://pcb.dayungame.vip:2080/third/rt.php?rt=/bs/online_pay_return.php', 'http://www.gdanguangwlkj.com', '0.0000', '1', '0', 'bsyl', '0.0000', '0.0000', '1.00', '3000.00', '6', 'https://ebank.baishengpay.com/Payment/Gateway', 'tw', '0', 'https://ebank.baishengpay.com/Payment/Gateway', '13', '/bs/online_pay', '{\"pc\":\"UNIONPAY_QRCODE_PAY\",\"wap\":\"UNIONPAY_QRCODE_PAY\"}', '1', null, null, '0.00', '3', '银联支付');
INSERT INTO `merchantpay` VALUES ('8', '18030423104438', '2018-05-31 20:10:00', '2018-05-31 20:10:00', '', '百盛京东支付', '0', '50def9ed401f1c4ad9a69e70e2fc7d06', 'http://pcb.dayungame.vip:2080/third/rt.php?rt=/bs/online_pay_return.php', 'http://www.gdanguangwlkj.com', '0.0000', '1', '0', 'bsjd', '0.0000', '0.0000', '1.00', '3000.00', '6', 'https://ebank.baishengpay.com/Payment/Gateway', 'tw', '0', 'https://ebank.baishengpay.com/Payment/Gateway', '10', '/bs/online_pay', '{\"pc\":\"JD_QRCODE_PAY\",\"wap\":\"JD_WAP_PAY\"}', '1', null, null, '0.00', '3', '京东支付');
INSERT INTO `merchantpay` VALUES ('10', '18030423104438', '2018-05-31 20:10:00', '2018-05-31 20:10:00', '', '百盛QQ支付', '0', '50def9ed401f1c4ad9a69e70e2fc7d06', 'http://pcb.dayungame.vip:2080/third/rt.php?rt=/bs/online_pay_return.php', 'http://www.gdchunkaiwlkj.com/paySuccess_s.jsp', '0.0000', '1', '0', 'bsqq', '0.0000', '0.0000', '1.00', '3000.00', '6', 'https://ebank.baishengpay.com/Payment/Gateway', 'tw', '0', 'https://ebank.baishengpay.com/Payment/Gateway', '7', '/bs/online_pay', '{\"pc\":\"QQ_QRCODE_PAY\",\"wap\":\"QQ_WAP_PAY\"}', '1', null, null, '0.00', '3', 'QQ支付');
INSERT INTO `merchantpay` VALUES ('13', '18030423104438', '2018-05-31 20:10:00', '2018-05-31 20:10:00', '', '百盛快捷', '0', '50def9ed401f1c4ad9a69e70e2fc7d06', 'http://pcb.dayungame.vip:2080/third/rt.php?rt=/bs/online_pay_return.php', 'http://www.gdanguangwlkj.com', '0.0000', '1', '0', 'bskj', '0.0000', '0.0000', '1.00', '3000.00', '6', 'https://ebank.baishengpay.com/Payment/Gateway', 'tw', '0', 'https://ebank.baishengpay.com/Payment/Gateway', '4', '/bs/online_pay', '{\"paymentTypeCode\":\"ONLINE_BANK_QUICK_PAY\"}', '1', null, null, '0.00', '3', '在线支付3');
INSERT INTO `merchantpay` VALUES ('14', '17537', '2018-04-06 14:59:28', '2018-04-06 14:59:31', '', '高通微信', '0', 'f3a51513481858c9a6955f34209b037b', 'http://pcb.dayungame.vip:2080/third/rt.php?rt=/gt/zfb_wx_return', 'http://www.gdchunkaiwlkj.com/paySuccess_s.jsp', '0.0000', '1', '0', 'gtwx', '0.0000', '0.0000', '100.00', '5000.00', '6', 'http://tj.gaotongpay.com/PayBank.aspx', 'tw', '0', 'http://tj.gaotongpay.com/PayBank.aspx', '2', '/gt/zfb_wx', '{\"pc\":\"WEIXIN\",\"wap\":\"WEIXINWAP\"}', '1', '', '', '0.00', '3', '微信支付');
INSERT INTO `merchantpay` VALUES ('15', '17537', '2018-04-06 14:59:28', '2018-04-06 14:59:31', '', '高通微信条码', '0', 'f3a51513481858c9a6955f34209b037b', 'http://pcb.dayungame.vip:2080/third/rt.php?rt=/gt/zfb_wx_return', 'http://www.gdchunkaiwlkj.com/paySuccess_s.jsp', '0.0000', '1', '0', 'gtwx', '0.0000', '0.0000', '10.00', '10000.00', '6', 'http://tj.gaotongpay.com/PayBank.aspx', 'tw', '0', 'http://tj.gaotongpay.com/PayBank.aspx\r\n', '2', '/gt/zfb_wx', '{\"pc\":\"WEIXINBARCODE\",\"wap\":\"WEIXINBARCODE\"}', '1', '', '', '0.00', '3', '微信条码支付');
INSERT INTO `merchantpay` VALUES ('16', '17537', '2018-04-06 14:59:28', '2018-04-06 14:59:31', '', '高通快捷', '0', 'f3a51513481858c9a6955f34209b037b', 'http://pcb.dayungame.vip:2080/third/rt.php?rt=/gt/zfb_wx_return', 'http://www.gdchunkaiwlkj.com/paySuccess_s.jsp', '0.0000', '1', '0', 'gtkj', '0.0000', '0.0000', '1.00', '5000.00', '6', 'http://tj.gaotongpay.com/PayBank.aspx', 'tw', '0', 'http://tj.gaotongpay.com/PayBank.aspx\r\n', '4', '/gt/zfb_wx', '{\"pc\":\"UNIONWAPPAY\",\"wap\":\"UNIONWAPPAY\"}', '1', '', '', '0.00', '3', '快捷支付1');
INSERT INTO `merchantpay` VALUES ('18', '18030423104438', '2018-05-31 20:10:00', '2018-05-31 20:10:00', '', '百盛支付宝', '0', '50def9ed401f1c4ad9a69e70e2fc7d06', 'http://pcb.dayungame.vip:2080/third/rt.php?rt=/bs/online_pay_return', 'http://www.gdanguangwlkj.com', '0.0000', '1', '0', 'bszfb', '0.0000', '0.0000', '10.00', '3000.00', '6', 'https://ebank.baishengpay.com/Payment/Gateway', 'tw', '0', 'https://ebank.baishengpay.com/Payment/Gateway', '1', '/bs/online_pay', '{\"pc\":\"ALIPAY_QRCODE_PAY_1\",\"wap\":\"ALIPAY_WAP_PAY\"}', '1', '', '', '0.00', '3', '支付宝支付');
INSERT INTO `merchantpay` VALUES ('20', '18030423104438', '2018-05-31 20:10:00', '2018-05-31 20:10:00', '', '百盛微信支付', '0', '50def9ed401f1c4ad9a69e70e2fc7d06', 'http://pcb.dayungame.vip:2080/third/rt.php?rt=/bs/online_pay_return.php', 'http://www.gdchunkaiwlkj.com/paySuccess_s.jsp', '0.0000', '1', '0', 'bswx', '0.0000', '0.0000', '1.00', '3000.00', '6', 'https://ebank.baishengpay.com/Payment/Gateway', 'tw', '0', 'https://ebank.baishengpay.com/Payment/Gateway', '2', '/bs/online_pay', '{\"pc\":\"WECHAT_QRCODE_PAY\",\"wap\":\"WECHAT_WAP_PAY\"}', '1', '', '', '0.00', '3', '微信支付1');
INSERT INTO `merchantpay` VALUES ('21', '101057', '2018-05-31 20:10:00', '2018-05-31 20:10:00', '', '博士QQ支付', '0', 'a8fa13e7783e5a7c46dcd1ca51708ff7', 'http://pcb.dayungame.vip:2080/third/rt.php?rt=/bp/online_pay_return.php', 'http://www.gdchunkaiwlkj.com/paySuccess_s.jsp', '0.0000', '1', '0', 'bpqq', '0.0000', '0.0000', '1.00', '3000.00', '6', 'https://ebank.baishengpay.com/Payment/Gateway', 'tw', '0', 'https://pay.api11.com', '7', '/bp/online_pay', '{\"pc\":\"QQ\",\"wap\":\"QQ_WAP\"}', '1', '', '', '0.00', '3', 'QQ支付1');
INSERT INTO `merchantpay` VALUES ('22', '101057', '2018-06-06 20:10:00', '2018-06-06 20:10:00', '', '博士支付宝', '0', 'a8fa13e7783e5a7c46dcd1ca51708ff7', 'http://pcb.dayungame.vip:2080/third/rt.php?rt=/bp/online_pay_return.php', 'http://www.gdchunkaiwlkj.com/paySuccess_s.jsp', '0.0000', '1', '0', 'bpzfb', '0.0000', '0.0000', '5.00', '2000.00', '6', 'https://ebank.baishengpay.com/Payment/Gateway', 'tw', '0', 'https://pay.api11.com', '1', '/bp/online_pay', '{\"pc\":\"ALIPAY\",\"wap\":\"ALIPAY_WAP\"}', '1', '', '', '0.00', '3', '支付宝支付2');
INSERT INTO `merchantpay` VALUES ('23', '101057', '2018-06-06 20:10:00', '2018-06-06 20:10:00', '', '博士网银', '0', 'a8fa13e7783e5a7c46dcd1ca51708ff7', 'http://pcb.dayungame.vip:2080/third/rt.php?rt=/bp/online_pay_return.php', 'http://www.gdchunkaiwlkj.com/paySuccess_s.jsp', '0.0000', '1', '0', 'bpwy', '0.0000', '0.0000', '1.00', '20000.00', '6', 'https://ebank.baishengpay.com/Payment/Gateway', 'tw', '0', 'https://pay.api11.com', '3', '/bp/online_pay', '{\"pc\":\"ALIPAY\",\"wap\":\"ALIPAY_WAP\"}', '1', '', '', '0.00', '3', '在线支付');
INSERT INTO `merchantpay` VALUES ('24', '101057', '2018-06-06 20:10:00', '2018-06-06 20:10:00', '', '博士快捷', '0', 'a8fa13e7783e5a7c46dcd1ca51708ff7', 'http://pcb.dayungame.vip:2080/third/rt.php?rt=/bp/online_pay_return.php', 'http://www.gdchunkaiwlkj.com/paySuccess_s.jsp', '0.0000', '1', '0', 'bpkj', '0.0000', '0.0000', '1.00', '5000.00', '6', 'https://ebank.baishengpay.com/Payment/Gateway', 'tw', '0', 'https://pay.api11.com', '4', '/bp/online_pay', '{\"pc\":\"EXPRESS\",\"wap\":\"EXPRESS\"}', '1', '', '', '0.00', '3', '快捷支付');
INSERT INTO `merchantpay` VALUES ('25', '2696', '2018-06-06 20:10:00', '2018-06-06 20:10:00', '', '乐卡微信', '0', '8761d4a75a224dbbb08a0186d9c2c72e', 'http://pcb.dayungame.vip:2080/lk/rt.php', 'http://www.gdchunkaiwlkj.com/paySuccess_s.jsp', '0.0000', '1', '0', 'lkwx', '0.0000', '0.0000', '1.00', '5000.00', '6', 'http://api.02lka.com/chargebank.aspx', 'tw', '0', 'http://api.02lka.com/chargebank.aspx', '2', '/lk/online_pay', '{\"pc\":\"1004\",\"wap\":\"1004\"}', '1', '', '', '0.00', '3', '乐卡微信');
INSERT INTO `merchantpay` VALUES ('26', '2696', '2018-06-06 20:10:00', '2018-06-06 20:10:00', '', '乐卡支付宝', '0', '8761d4a75a224dbbb08a0186d9c2c72e', 'http://pcb.dayungame.vip:2080/lk/rt.php', 'http://www.gdchunkaiwlkj.com/paySuccess_s.jsp', '0.0000', '1', '0', 'lkzfb', '0.0000', '0.0000', '1.00', '5000.00', '6', 'http://api.02lka.com/chargebank.aspx', 'tw', '0', 'http://api.02lka.com/chargebank.aspx', '1', '/lk/online_pay', '{\"pc\":\"992\",\"wap\":\"2098\"}', '1', '', '', '0.00', '3', '支付宝支付');
INSERT INTO `merchantpay` VALUES ('27', '2696', '2018-06-06 20:10:00', '2018-06-06 20:10:00', '', '乐卡QQ', '0', '8761d4a75a224dbbb08a0186d9c2c72e', 'http://pcb.dayungame.vip:2080/lk/rt.php', 'http://www.gdchunkaiwlkj.com/paySuccess_s.jsp', '0.0000', '1', '0', 'lkqq', '0.0000', '0.0000', '1.00', '5000.00', '6', 'http://api.02lka.com/chargebank.aspx', 'tw', '0', 'http://api.02lka.com/chargebank.aspx', '7', '/lk/online_pay', '{\"pc\":\"2100\",\"wap\":\"2097\"}', '1', '', '', '0.00', '3', '乐卡QQ');
INSERT INTO `merchantpay` VALUES ('28', '2696', '2018-06-06 20:10:00', '2018-06-06 20:10:00', '', '乐卡网银', '0', '8761d4a75a224dbbb08a0186d9c2c72e', 'http://pcb.dayungame.vip:2080/lk/rt.php', 'http://www.gdchunkaiwlkj.com/paySuccess_s.jsp', '0.0000', '1', '0', 'lkwy', '0.0000', '0.0000', '1.00', '5000.00', '6', 'http://api.02lka.com/chargebank.aspx', 'tw', '0', 'http://api.02lka.com/chargebank.aspx', '3', '/lk/online_pay', '{\"pc\":\"0\",\"wap\":\"0\"}', '1', '', '', '0.00', '3', '在线支付2');
INSERT INTO `merchantpay` VALUES ('29', '10023', '2018-06-06 20:10:00', '2018-06-06 20:10:00', '', '怡秒网银', '0', 'q5mxn65rnue12b338821vqbbq6nryuzx', 'http://pcb.dayungame.vip:2080/third/rt.php?rt=/ym/online_pay_return.php', 'http://www.gdchunkaiwlkj.com/paySuccess_s.jsp', '0.0000', '1', '0', 'ymwy', '0.0000', '0.0000', '1.00', '5000.00', '6', 'https://scckym.com/Pay_Index.html', 'tw', '0', 'https://scckym.com/Pay_Index.html', '4', '/ym/online_pay', '{\"pc\":\"907\",\"wap\":\"907\"}', '1', '', '', '0.00', '3', '快捷支付2');
INSERT INTO `merchantpay` VALUES ('30', '101057', '2018-06-06 20:10:00', '2018-06-06 20:10:00', '', '博士微信', '0', 'a8fa13e7783e5a7c46dcd1ca51708ff7', 'http://pcb.dayungame.vip:2080/third/rt.php?rt=/bp/online_pay_return.php', 'http://www.gdchunkaiwlkj.com/paySuccess_s.jsp', '0.0000', '1', '0', 'bpwx', '0.0000', '0.0000', '1.00', '5000.00', '6', 'https://ebank.baishengpay.com/Payment/Gateway', 'tw', '0', 'https://pay.api11.com', '2', '/bp/online_pay', '{\"pc\":\"WECHAT\",\"wap\":\"WECHAT_WAP\"}', '1', '', '', '0.00', '3', '微信支付');
INSERT INTO `merchantpay` VALUES ('31', 'CHANG1527741758731', '2018-05-31 20:10:00', '2018-05-31 20:10:00', '', '畅汇QQ支付', '0', '5ce0968b80544d18855e5200c9408be0', 'http://pcb.dayungame.vip:2080/third/rt.php?rt=/ch/online_pay_return.php', 'http://www.gdchunkaiwlkj.com/paySuccess_s.jsp', '0.0000', '1', '0', 'chqq', '0.0000', '0.0000', '1.00', '3000.00', '6', 'http://gateway.xjdbu.com/controller.action', 'tw', '0', 'http://gateway.xjdbu.com/controller.action', '7', '/ch/online_pay', '{\"pc\":\"QQ\",\"wap\":\"QQWAP\"}', '1', '', '', '0.00', '3', 'QQ支付3');
INSERT INTO `merchantpay` VALUES ('32', 'CHANG1527741758731', '2018-05-31 20:10:00', '2018-05-31 20:10:00', '', '畅汇银联扫码', '0', '5ce0968b80544d18855e5200c9408be0', 'http://pcb.dayungame.vip:2080/third/rt.php?rt=/ch/online_pay_return.php', 'http://www.gdchunkaiwlkj.com/paySuccess_s.jsp', '0.0000', '1', '0', 'chyl', '0.0000', '0.0000', '1.00', '3000.00', '6', 'http://gateway.xjdbu.com/controller.action', 'tw', '0', 'http://gateway.xjdbu.com/controller.action', '13', '/ch/online_pay', '{\"pc\":\"UnionPay\",\"wap\":\"UnionPay\"}', '1', '', '', '0.00', '3', '银联支付');
INSERT INTO `merchantpay` VALUES ('34', 'CHANG1530242989933', '2018-05-31 20:10:00', '2018-05-31 20:10:00', '', '畅汇支付宝', '0', '6f09225b03cf48719c02a9731592d440', 'http://pcb.dayungame.vip:2080/third/rt.php?rt=/ch/online_pay_return.php', 'http://www.gdchunkaiwlkj.com/paySuccess_s.jsp', '0.0000', '1', '0', 'chzfb', '0.0000', '0.0000', '1.00', '3000.00', '6', 'https://ebank.baishengpay.com/Payment/Gateway', 'tw', '0', 'https://5u17.cn/controller.action', '1', '/ch/online_pay', '{\"pc\":\"ALIPAY\",\"wap\":\"ALIPAYWAP\"}', '1', '', '', '0.00', '3', '支付宝支付');
INSERT INTO `merchantpay` VALUES ('35', 'CHANG1527741758731', '2018-05-31 20:10:00', '2018-05-31 20:10:00', '', '畅汇银联快捷', '0', '5ce0968b80544d18855e5200c9408be0', 'http://pcb.dayungame.vip:2080/third/rt.php?rt=/ch/online_pay_return.php', 'http://www.gdchunkaiwlkj.com/paySuccess_s.jsp', '0.0000', '1', '0', 'chkj', '0.0000', '0.0000', '1.00', '3000.00', '6', 'http://gateway.xjdbu.com/controller.action', 'tw', '0', 'http://gateway.xjdbu.com/controller.action', '4', '/ch/online_pay', '{\"pc\":\"ALIPAY\",\"wap\":\"Nocard_H5\"}', '1', '', '', '0.00', '3', '银联快捷');
INSERT INTO `merchantpay` VALUES ('36', 'CHANG1527741758731', '2018-05-31 20:10:00', '2018-05-31 20:10:00', '', '畅汇京东', '0', '5ce0968b80544d18855e5200c9408be0', 'http://pcb.dayungame.vip:2080/third/rt.php?rt=/ch/online_pay_return.php', 'http://www.gdchunkaiwlkj.com/paySuccess_s.jsp', '0.0000', '1', '0', 'chjd', '0.0000', '0.0000', '1.00', '3000.00', '6', 'http://gateway.xjdbu.com/controller.action', 'tw', '0', 'http://gateway.xjdbu.com/controller.action', '10', '/ch/online_pay', '{\"pc\":\"JDPAY\",\"wap\":\"JDPAY\"}', '1', '', '', '0.00', '3', '京东支付');
INSERT INTO `merchantpay` VALUES ('37', '3729', '2018-06-06 20:10:00', '2018-06-06 20:10:00', '', '优付微信', '0', '103c470ce78b48638019c0a78860ca75', 'http://pcb.dayungame.vip:2080/uf/rt.php', 'http://www.gdchunkaiwlkj.com/paySuccess_s.jsp', '0.0000', '1', '0', 'ufwx', '0.0000', '0.0000', '10.00', '5000.00', '6', 'http://api.gzymc188.cn/chargebank.aspx', 'tw', '0', 'http://api.gzymc188.cn/chargebank.aspx', '2', '/uf/online_pay', '{\"pc\":\"1004\",\"wap\":\"2099\"}', '1', '', '', '0.00', '3', '微信支付2');
INSERT INTO `merchantpay` VALUES ('38', '3729', '2018-06-06 20:10:00', '2018-06-06 20:10:00', '', '优付支付宝', '0', '103c470ce78b48638019c0a78860ca75', 'http://pcb.dayungame.vip:2080/uf/rt.php', 'http://www.gdchunkaiwlkj.com/paySuccess_s.jsp', '0.0000', '1', '0', 'ufzfb', '0.0000', '0.0000', '10.00', '5000.00', '6', 'http://api.gzymc188.cn/chargebank.aspx', 'tw', '0', 'http://api.gzymc188.cn/chargebank.aspx', '1', '/uf/online_pay', '{\"pc\":\"992\",\"wap\":\"2098\"}', '1', '', '', '0.00', '3', '支付宝支付');
INSERT INTO `merchantpay` VALUES ('39', '3729', '2018-06-06 20:10:00', '2018-06-06 20:10:00', '', '优付QQ', '0', '103c470ce78b48638019c0a78860ca75', 'http://pcb.dayungame.vip:2080/uf/rt.php', 'http://www.gdchunkaiwlkj.com/paySuccess_s.jsp', '0.0000', '1', '0', 'ufqq', '0.0000', '0.0000', '1.00', '5000.00', '6', 'http://api.gzymc188.cn/chargebank.aspx', 'tw', '0', 'http://api.gzymc188.cn/chargebank.aspx', '7', '/uf/online_pay', '{\"pc\":\"2100\",\"wap\":\"2097\"}', '1', '', '', '0.00', '3', '优付QQ');
INSERT INTO `merchantpay` VALUES ('40', '3729', '2018-06-06 20:10:00', '2018-06-06 20:10:00', '', '优付网银', '0', '103c470ce78b48638019c0a78860ca75', 'http://pcb.dayungame.vip:2080/uf/rt.php', 'http://www.gdchunkaiwlkj.com/paySuccess_s.jsp', '0.0000', '1', '0', 'ufzfb', '0.0000', '0.0000', '10.00', '5000.00', '6', 'http://api.gzymc188.cn/chargebank.aspx', 'tw', '0', 'http://api.gzymc188.cn/chargebank.aspx', '3', '/uf/online_pay', '{\"pc\":\"0\",\"wap\":\"0\"}', '1', '', '', '0.00', '3', '优付网银');
INSERT INTO `merchantpay` VALUES ('41', '3729', '2018-06-06 20:10:00', '2018-06-06 20:10:00', '', '优付银联', '0', '103c470ce78b48638019c0a78860ca75', 'http://pcb.dayungame.vip:2080/uf/rt.php', 'http://www.gdchunkaiwlkj.com/paySuccess_s.jsp', '0.0000', '1', '0', 'ufzfb', '0.0000', '0.0000', '10.00', '5000.00', '6', 'http://api.gzymc188.cn/chargebank.aspx', 'tw', '0', 'http://api.gzymc188.cn/chargebank.aspx', '13', '/uf/online_pay', '{\"pc\":\"996\",\"wap\":\"996\"}', '1', '', '', '0.00', '3', '优付银联扫码');
INSERT INTO `merchantpay` VALUES ('42', 'T88882018062610001181', '2018-06-06 20:10:00', '2018-06-06 20:10:00', '', '么么支付宝', '0', '0730b6bef15d41c5a2d0e40e2544b514', 'http://pcb.dayungame.vip:2080/third/rt.php?rt=/mm/online_pay_return.php', 'http://www.gdchunkaiwlkj.com/paySuccess_s.jsp', '0.0000', '1', '0', 'mmzfb', '0.0000', '0.0000', '10.00', '5000.00', '6', 'http://gateway.imemepay.com/cnp/gateway', 'tw', '0', 'http://gateway.imemepay.com/cnp/gateway', '1', '/mm/online_pay', '{\"trx_key\":\"2a7dfbb3a3244039a6c89cdee0c3f358\",\"pc\":\"20303\",\"wap\":\"20203\"}', '1', '', '', '0.00', '3', '支付宝支付');
INSERT INTO `merchantpay` VALUES ('43', '10001036', '2018-06-06 20:10:00', '2018-06-06 20:10:00', '', '瑞亨支付宝', '0', '8873a742b2124e398f1f5b4b8958e3cc', 'http://pcb.dayungame.vip:2080/third/rt.php?rt=/rh/online_pay_return.php', 'http://www.gdchunkaiwlkj.com/paySuccess_s.jsp', '0.0000', '1', '0', 'rhzfb', '0.0000', '0.0000', '10.00', '5000.00', '6', 'http://www.ruihengpay.com/gateway/api/payment', 'tw', '0', 'http://www.ruihengpay.com/gateway/api/payment', '1', '/rh/online_pay', '{\"pc\":\"ALIPAY\",\"wap\":\"ALIPAYH5\"}', '1', '', '', '0.00', '3', '支付宝支付');
INSERT INTO `merchantpay` VALUES ('44', '8888692365', '2018-04-06 14:59:28', '2018-04-06 14:59:31', '', '云易付支付宝', '0', 'd5b192a78b904ae79fae907c749759e5', 'http://pcb.dayungame.vip:2080/third/rt.php?rt=/yyf/online_pay_return', 'http://www.gdchunkaiwlkj.com/paySuccess_s.jsp', '0.0000', '1', '0', 'yyfzfb', '0.0000', '0.0000', '1.00', '5000.00', '6', 'http://pay.norif.cn/pay/gateway', 'tw', '0', 'http://pay.norif.cn/pay/gateway', '1', '/yyf/online_pay', '{\"pc\":\"903\",\"wap\":\"904\"}', '1', '', '', '0.00', '3', '支付宝支付');
INSERT INTO `merchantpay` VALUES ('45', 'YFT88882018062610001332', '2018-06-06 20:10:00', '2018-06-06 20:10:00', '', '收付宝支付宝', '0', '4626795b20be4dbea357f9309c605320', 'http://pcb.dayungame.vip:2080/third/rt.php?rt=/sfb/online_pay_return.php', 'http://www.gdchunkaiwlkj.com/paySuccess_s.jsp', '0.0000', '1', '0', 'sfbzfb', '0.0000', '0.0000', '20.00', '5000.00', '6', 'https://gateway.gaogby.com/scanPay/initPay', 'tw', '0', 'https://gateway.gaogby.com/scanPay/initPay', '1', '/sfb/online_pay', '{\"payKey\":\"24bb37e8ebac46e9a3f9b923f5f6f8b3\",\"pc\":\"20000303\",\"wap\":\"20000203\"}', '1', '', '', '0.00', '3', '支付宝支付');
INSERT INTO `merchantpay` VALUES ('46', 'YFT88882018062610001332', '2018-06-06 20:10:00', '2018-06-06 20:10:00', '', '收付宝QQ', '0', '4626795b20be4dbea357f9309c605320', 'http://pcb.dayungame.vip:2080/third/rt.php?rt=/sfb/online_pay_return.php', 'http://www.gdchunkaiwlkj.com/paySuccess_s.jsp', '0.0000', '1', '0', 'sfbqq', '0.0000', '0.0000', '20.00', '5000.00', '6', 'https://gateway.gaogby.com/scanPay/initPay', 'tw', '0', 'https://gateway.gaogby.com/scanPay/initPay', '7', '/sfb/online_pay', '{\"payKey\":\"24bb37e8ebac46e9a3f9b923f5f6f8b3\",\"pc\":\"70000103\",\"wap\":\"70000203\"}', '1', '', '', '0.00', '3', 'QQ支付6');
INSERT INTO `merchantpay` VALUES ('47', 'YFT88882018062610001332', '2018-06-06 20:10:00', '2018-06-06 20:10:00', '', '收付宝银联', '0', '4626795b20be4dbea357f9309c605320', 'http://pcb.dayungame.vip:2080/third/rt.php?rt=/sfb/online_pay_return.php', 'http://www.gdchunkaiwlkj.com/paySuccess_s.jsp', '0.0000', '1', '0', 'sfbyl', '0.0000', '0.0000', '20.00', '5000.00', '6', 'https://gateway.gaogby.com/scanPay/initPay', 'tw', '0', 'https://gateway.gaogby.com/scanPay/initPay', '13', '/sfb/online_pay', '{\"payKey\":\"24bb37e8ebac46e9a3f9b923f5f6f8b3\",\"pc\":\"60000103\",\"wap\":\"60000203\"}', '1', '', '', '0.00', '3', '银联扫码2');
INSERT INTO `merchantpay` VALUES ('48', 'YFT88882018062610001332', '2018-06-06 20:10:00', '2018-06-06 20:10:00', '', '收付宝在线支付', '0', '4626795b20be4dbea357f9309c605320', 'http://pcb.dayungame.vip:2080/third/rt.php?rt=/sfb/online_pay_return.php', 'http://www.gdchunkaiwlkj.com/paySuccess_s.jsp', '0.0000', '1', '0', 'sfbwy', '0.0000', '0.0000', '20.00', '5000.00', '6', 'https://gateway.gaogby.com/scanPay/initPay', 'tw', '0', 'https://gateway.gaogby.com/scanPay/initPay', '3', '/sfb/online_pay', '{\"payKey\":\"24bb37e8ebac46e9a3f9b923f5f6f8b3\",\"pc\":\"50000103\",\"wap\":\"50000103\"}', '1', '', '', '0.00', '3', '在线支付');
INSERT INTO `merchantpay` VALUES ('49', 'YFT88882018062610001332', '2018-06-06 20:10:00', '2018-06-06 20:10:00', '', '收付宝快捷支付', '0', '4626795b20be4dbea357f9309c605320', 'http://pcb.dayungame.vip:2080/third/rt.php?rt=/sfb/online_pay_return.php', 'http://www.gdchunkaiwlkj.com/paySuccess_s.jsp', '0.0000', '1', '0', 'sfbkj', '0.0000', '0.0000', '20.00', '5000.00', '6', 'https://gateway.gaogby.com/scanPay/initPay', 'tw', '0', 'https://gateway.gaogby.com/scanPay/initPay', '4', '/sfb/online_pay', '{\"payKey\":\"24bb37e8ebac46e9a3f9b923f5f6f8b3\",\"pc\":\"40000103\",\"wap\":\"40000103\"}', '1', '', '', '0.00', '3', '快捷支付');
INSERT INTO `merchantpay` VALUES ('50', 'dysk1.jpg', '2018-06-06 20:10:00', '2018-06-06 20:10:00', '', '微信附言支付', '0', '4626795b20be4dbea357f9309c605320', 'http://pcb.dayungame.vip:2080/third/rt.php?rt=/sfb/online_pay_return.php', 'http://www.gdchunkaiwlkj.com/paySuccess_s.jsp', '0.0000', '1', '0', 'wxfy', '0.0000', '0.0000', '20.00', '5000.00', '6', 'https://gateway.gaogby.com/scanPay/initPay', 'tw', '0', 'https://gateway.gaogby.com/scanPay/initPay', '15', '/fy/zfb_wx', '{\"payKey\":\"24bb37e8ebac46e9a3f9b923f5f6f8b3\",\"pc\":\"40000103\",\"wap\":\"40000103\"}', '1', '', '', '0.00', '3', '微信附言');
INSERT INTO `merchantpay` VALUES ('51', 'YFT88882018062610001332', '2018-06-06 20:10:00', '2018-06-06 20:10:00', '', '收付宝微信', '0', '4626795b20be4dbea357f9309c605320', 'http://pcb.dayungame.vip:2080/third/rt.php?rt=/sfb/online_pay_return.php', 'http://www.gdchunkaiwlkj.com/paySuccess_s.jsp', '0.0000', '1', '0', 'sfbwx', '0.0000', '0.0000', '50.00', '5000.00', '6', 'https://gateway.gaogby.com/scanPay/initPay', 'tw', '0', 'https://gateway.gaogby.com/scanPay/initPay', '2', '/sfb/online_pay', '{\"payKey\":\"24bb37e8ebac46e9a3f9b923f5f6f8b3\",\"pc\":\"10000103\",\"wap\":\"10000203\"}', '1', '', '', '0.00', '3', '微信支付6');
INSERT INTO `merchantpay` VALUES ('52', '00013300000347', '2018-06-06 20:10:00', '2018-06-06 20:10:00', '', '天天微信', '0', '69y493hew9wqerh4p5607SUIN542WW15', 'http://pcb.dayungame.vip:2080/tt/rt.php', 'http://www.gdchunkaiwlkj.com/paySuccess_s.jsp', '0.0000', '1', '0', 'ttwx', '0.0000', '0.0000', '10.00', '5000.00', '6', 'https://www.zhizeng-pay.com/mas/mobile/create.do', 'tw', '0', 'https://www.zhizeng-pay.com/mas/mobile/create.do', '2', '/tt/online_pay', '{\"channel\":\"WEIXIN\",\"pc\":\"NATIVE\",\"wap\":\"WECHATH5\"}', '1', '', '', '0.00', '3', '微信支付7');
INSERT INTO `merchantpay` VALUES ('53', '00013300000347', '2018-06-06 20:10:00', '2018-06-06 20:10:00', '', '天天银联', '0', '69y493hew9wqerh4p5607SUIN542WW15', 'http://pcb.dayungame.vip:2080/tt/rt.php', 'http://www.gdchunkaiwlkj.com/paySuccess_s.jsp', '0.0000', '1', '0', 'ttyl', '0.0000', '0.0000', '10.00', '5000.00', '6', 'https://www.zhizeng-pay.com/mas/mobile/create.do', 'tw', '0', 'https://www.zhizeng-pay.com/mas/mobile/create.do', '13', '/tt/online_pay', '{\"channel\":\"UNIONPAY\",\"pc\":\"UnionpayScan\",\"wap\":\"UnionpayScan\"}', '1', '', '', '0.00', '3', '银联支付1');
INSERT INTO `merchantpay` VALUES ('54', '00013300000347', '2018-06-06 20:10:00', '2018-06-06 20:10:00', '', '天天支付宝', '0', '69y493hew9wqerh4p5607SUIN542WW15', 'http://pcb.dayungame.vip:2080/tt/rt.php', 'http://www.gdchunkaiwlkj.com/paySuccess_s.jsp', '0.0000', '1', '0', 'ttzfb', '0.0000', '0.0000', '10.00', '5000.00', '6', 'https://www.zhizeng-pay.com/mas/mobile/create.do', 'tw', '0', 'https://www.zhizeng-pay.com/mas/mobile/create.do', '1', '/tt/online_pay', '{\"channel\":\"ALIPAY\",\"pc\":\"AliPayScan\",\"wap\":\"AliPayH5\"}', '1', '', '', '0.00', '3', '支付宝支付');
INSERT INTO `merchantpay` VALUES ('55', '00013300000347', '2018-06-06 20:10:00', '2018-06-06 20:10:00', '', '天天QQ', '0', '69y493hew9wqerh4p5607SUIN542WW15', 'http://pcb.dayungame.vip:2080/tt/rt.php', 'http://www.gdchunkaiwlkj.com/paySuccess_s.jsp', '0.0000', '1', '0', 'ttqq', '0.0000', '0.0000', '10.00', '5000.00', '6', 'https://www.zhizeng-pay.com/mas/mobile/create.do', 'tw', '0', 'https://www.zhizeng-pay.com/mas/mobile/create.do', '7', '/tt/online_pay', '{\"channel\":\"ALIPAY\",\"pc\":\"QqScan\",\"wap\":\"QqScan\"}', '1', '', '', '0.00', '3', 'QQ支付2');
INSERT INTO `merchantpay` VALUES ('56', 'U1807091547110620EV', '2018-06-06 20:10:00', '2018-06-06 20:10:00', '', 'HPay支付宝', '0', '4f53222695d04bd1aafc4c0a605a29bb', 'http://pcb.dayungame.vip:2080/third/rt.php?rt=/hp/online_pay_return.php', 'http://www.gdchunkaiwlkj.com/paySuccess_s.jsp', '0.0000', '1', '0', 'hpzfb', '0.0000', '0.0000', '10.00', '5000.00', '6', 'https://payment.14493.com/api/scanpay', 'tw', '0', 'https://payment.14493.com/api/scanpay', '1', '/hp/online_pay', '{\"pc\":\"20303\",\"wap\":\"20203\"}', '1', '', '', '0.00', '3', '支付宝支付');
INSERT INTO `merchantpay` VALUES ('57', 'ep2018071641593719', '2018-06-06 20:10:00', '2018-06-06 20:10:00', '', '直付支付宝', '0', 'aeba13998642c5b345037ce51f3a0f0f67dde560', 'http://pcb.dayungame.vip:2080/zp/rt.php', 'http://www.gdchunkaiwlkj.com/paySuccess_s.jsp', '0.0000', '1', '0', 'zpzfb', '0.0000', '0.0000', '10.00', '5000.00', '6', 'https://e9f632d2.qhdlmf.com/gateway.php', 'tw', '0', 'https://e9f632d2.qhdlmf.com/gateway.php', '1', '/zp/online_pay', '{\"platform\":\"alipay\"}', '1', '', '', '0.00', '3', '支付宝支付');
INSERT INTO `merchantpay` VALUES ('58', '8113', '2018-06-06 20:10:00', '2018-06-06 20:10:00', '', '东方支付宝', '0', '93a60b3038ac69119c5116fbd4a05986', 'http://pcb.dayungame.vip:2080/df/rt.php', 'http://www.gdchunkaiwlkj.com/paySuccess_s.jsp', '0.0000', '1', '0', 'dfzfb', '0.0000', '0.0000', '10.00', '5000.00', '6', 'http://www.7ypay.com/api/Qrcode/pay_qrcode/', 'tw', '0', 'http://www.7ypay.com/api/Qrcode/pay_qrcode/', '1', '/df/online_pay', '{\"platform\":\"alipay\"}', '1', '', '', '0.00', '3', '支付宝支付3');
INSERT INTO `merchantpay` VALUES ('59', '101057', '2018-05-31 20:10:00', '2018-05-31 20:10:00', '', '博士京东支付', '0', 'a8fa13e7783e5a7c46dcd1ca51708ff7', 'http://pcb.dayungame.vip:2080/third/rt.php?rt=/bp/online_pay_return.php', 'http://www.gdchunkaiwlkj.com/paySuccess_s.jsp', '0.0000', '1', '0', 'bpjd', '0.0000', '0.0000', '1.00', '3000.00', '6', 'https://ebank.baishengpay.com/Payment/Gateway', 'tw', '0', 'https://pay.api11.com', '10', '/bp/online_pay', '{\"pc\":\"JD\",\"wap\":\"JD_WAP\"}', '1', '', '', '0.00', '3', '京东支付1');
INSERT INTO `merchantpay` VALUES ('60', 'WF69760', '2018-05-31 20:10:00', '2018-05-31 20:10:00', '', '五福QQ', '0', 'DMDTEZC9UFZAPYZYYTAUZ75K96IFMZ5D', 'http://pcb.dayungame.vip:2080/third/rt.php?rt=/wf/online_pay_return.php', 'http://www.gdchunkaiwlkj.com/paySuccess_s.jsp', '0.0000', '5', '0', 'wfqq', '0.0000', '0.0000', '1.00', '3000.00', '6', 'https://pay.8331vip.com/uniThirdPay', 'tw', '0', 'https://pay.8331vip.com/uniThirdPay', '7', '/wf/online_pay', '{\"pc\":\"QQ_NATIVE\",\"wap\":\"QQ_H5\"}', '1', '', '', '0.00', '3', 'QQ支付1');
INSERT INTO `merchantpay` VALUES ('61', 'WF69760', '2018-05-31 20:10:00', '2018-05-31 20:10:00', '', '五福支付宝', '0', 'DMDTEZC9UFZAPYZYYTAUZ75K96IFMZ5D', 'http://pcb.dayungame.vip:2080/third/rt.php?rt=/wf/online_pay_return.php', 'http://www.gdchunkaiwlkj.com/paySuccess_s.jsp', '0.0000', '1', '0', 'wfzfb', '0.0000', '0.0000', '1.00', '3000.00', '6', 'https://pay.8331vip.com/uniThirdPay', 'tw', '0', 'https://pay.8331vip.com/uniThirdPay', '1', '/wf/online_pay', '{\"pc\":\"ALIPAY_NATIVE\",\"wap\":\"ALIPAY_H5\"}', '1', '', '', '0.00', '3', '支付宝支付1');
INSERT INTO `merchantpay` VALUES ('62', 'WF70641', '2018-05-31 20:10:00', '2018-05-31 20:10:00', '', '五福微信', '0', '4F0ETNV5VUCQQB0S2H6LXS58JEP5545Z', 'http://pcb.dayungame.vip:2080/third/rt.php?rt=/wf/online_pay_return.php', 'http://www.gdchunkaiwlkj.com/paySuccess_s.jsp', '0.0000', '1', '0', 'wfwx', '0.0000', '0.0000', '1.00', '3000.00', '6', 'https://pay.8331vip.com/uniThirdPay', 'tw', '0', 'https://pay.8331vip.com/uniThirdPay', '2', '/wf/online_pay', '{\"pc\":\"WEIXIN_NATIVE\",\"wap\":\"WEIXIN_H5\"}', '1', '', '', '0.00', '3', '微信支付1');
INSERT INTO `merchantpay` VALUES ('63', '101057', '2018-06-06 20:10:00', '2018-06-06 20:10:00', '', '博士银联', '0', 'a8fa13e7783e5a7c46dcd1ca51708ff7', 'http://pcb.dayungame.vip:2080/third/rt.php?rt=/bp/online_pay_return.php', 'http://www.gdchunkaiwlkj.com/paySuccess_s.jsp', '0.0000', '1', '0', 'bpyl', '0.0000', '0.0000', '1.00', '5000.00', '6', 'https://ebank.baishengpay.com/Payment/Gateway', 'tw', '0', 'https://pay.api11.com', '13', '/bp/online_pay', '{\"pc\":\"CUP\",\"wap\":\"CUP\"}', '1', '', '', '0.00', '3', '银联支付');
INSERT INTO `merchantpay` VALUES ('64', '8120', '2018-07-28 20:10:00', '2018-06-06 20:10:00', '', '东方微信', '0', 'c1c5421d6c3d0c2622c2e42f380a73e8', 'http://pcb.dayungame.vip:2080/df/rt.php', 'http://www.gdchunkaiwlkj.com/paySuccess_s.jsp', '0.0000', '1', '0', 'dfwx', '0.0000', '0.0000', '1.00', '999.00', '6', 'http://www.7ypay.com/api/Qrcode/pay_qrcode/', 'tw', '0', 'http://www.7ypay.com/api/Qrcode/pay_qrcode/', '2', '/df/online_pay', '{\"platform\":\"weixin\"}', '1', '', '', '0.00', '3', '微信支付1');
INSERT INTO `merchantpay` VALUES ('65', '210001110014103', '2018-08-04 17:49:30', '2018-08-04 17:49:32', '', '迅联宝支付宝', '0', '808083b2efd42a1567ee6d2c3ddbf930', 'http://pcb.dayungame.vip:2080/xlb/zfb_wx_return.php', '', '0.0000', '1', '0', 'xlbzfb', '0.0000', '0.0000', '1.00', '3000.00', '6', 'http://trade.id888.cn:8880/cgi-bin/netpayment/pay_gate.cgi', 'tw', '0', 'http://pay.yaica.top:88/pay_shop.jsp', '1', '/xlb/zfb_wx', '{\"tradeSummary\":\"a\",\"choosePayType\":\"5\",\"overTime\":\"40\",\"apiName\":\"ZFB_PAY\",\"apiVersion\":\"1.0.0.0\"}', '1', '', '', '0.00', '3', '支付宝支付1');
INSERT INTO `merchantpay` VALUES ('66', '10811', '2018-05-29 16:50:00', '2018-05-29 16:50:00', '', '人人微信', '0', '07f59a0e76722c0dc0d0c656dae4c4c5', 'http://pcb.dayungame.vip:2080/third/rt.php?rt=/rr/online_pay_return.php', 'http://www.gdchunkaiwlkj.com/paySuccess_s.jsp', '0.0000', '1', '0', 'rrwx', '0.0000', '0.0000', '500.00', '10000.00', '6', 'https://qfgames.com/gateway', 'tw', '0', 'https://wx.hwjps.com/gateway', '2', '/rr/online_pay', '{\"wap\":\"3\",\"pc\":\"1\"}', '1', '', '', '0.00', '3', '微信支付1');
INSERT INTO `merchantpay` VALUES ('67', '20811', '2018-05-29 16:50:00', '2018-05-29 16:50:00', '', '人人支付宝', '0', '46cb6317a2d162ee9aecc1ac7ef546d5', 'http://pcb.dayungame.vip:2080/third/rt.php?rt=/rr/online_pay_return.php', 'http://www.gdchunkaiwlkj.com/paySuccess_s.jsp', '0.0000', '1', '0', 'rrwx', '0.0000', '0.0000', '500.00', '10000.00', '6', 'https://ali.qfgames.com/gateway', 'tw', '0', 'https://ali.hwjps.com/gateway', '1', '/rr/online_pay', '{\"wap\":\"4\",\"pc\":\"2\"}', '1', '', '', '0.00', '3', '支付宝支付1');
INSERT INTO `merchantpay` VALUES ('69', 'WF69760', '2018-05-31 20:10:00', '2018-05-31 20:10:00', '', '五福银联支付', '0', 'DMDTEZC9UFZAPYZYYTAUZ75K96IFMZ5D', 'http://pcb.dayungame.vip:2080/third/rt.php?rt=/wf/online_pay_return.php', 'http://www.gdchunkaiwlkj.com/paySuccess_s.jsp', '0.0000', '1', '0', 'wfyl', '0.0000', '0.0000', '1.00', '3000.00', '6', 'https://pay.8331vip.com/uniThirdPay', 'tw', '0', 'https://pay.8331vip.com/uniThirdPay', '13', '/wf/online_pay', '{\"pc\":\"UNIONPAY_NATIVE\",\"wap\":\"UNIONPAY_H5\"}', '1', '', '', '0.00', '3', '银联支付');
INSERT INTO `merchantpay` VALUES ('70', '210001110013843', '2018-08-04 17:49:30', '2018-08-04 17:49:32', '', '迅联宝网银', '0', 'b8206875b6b19293c17d7b1d8035fad3', 'http://pcb.dayungame.vip:2080/xlb/online_pay_return.php', '', '0.0000', '1', '0', 'xlbwy', '0.0000', '0.0000', '1.00', '10000.00', '6', 'http://trade.cd178.cn:8880/cgi-bin/netpayment/pay_gate.cgi', 'tw', '0', 'http://pay.yaica.top:88/xlbWy.jsp', '3', '/xlb/online_pay', '{\"tradeSummary\":\"a\",\"choosePayType\":\"1\",\"apiName\":\"WEB_PAY_B2C\",\"apiVersion\":\"1.0.0.0\"}', '1', '', '', '0.00', '2', '在线支付');
INSERT INTO `merchantpay` VALUES ('71', '210001110013846', '2018-08-04 17:49:30', '2018-08-04 17:49:32', '', '迅联宝网银H5', '0', '779103095f0b35a5bf2d2a0569e52597', 'http://pcb.dayungame.vip:2080/xlb/online_pay_return.php', '', '0.0000', '1', '0', 'xlbwy', '0.0000', '0.0000', '1.00', '10000.00', '6', 'http://trade.cd178.cn:8880/cgi-bin/netpayment/pay_gate.cgi', 'tw', '0', 'http://pay.yaica.top:88/xlbWy.jsp', '3', '/xlb/online_pay', '{\"tradeSummary\":\"a\",\"choosePayType\":\"1\",\"apiName\":\"WAP_PAY_B2C\",\"apiVersion\":\"1.0.0.0\"}', '1', '', '', '0.00', '1', '在线支付');
INSERT INTO `merchantpay` VALUES ('72', '15662041812', '2018-08-25 16:50:00', '2018-11-04 22:02:44', 'dycw', '美名互联支付宝', '0', 'u6(7[[-KDp)%jqZ=L9}xZl]G', 'http://pcb.dayungame.vip:2080/mmhl/rt.php', 'http://www.gdchunkaiwlkj.com/paySuccess_s.jsp', '0.0000', '1', '0', 'mmhlzfb', '0.0000', '0.0000', '10.00', '3000.00', '0,1,2,3,4,5,6', 'http://www.wl7s47.com', 'tw', '0', 'http://www.wl7s47.com/api/pay', '1', '/mmhl/online_pay', '{\"wap\":\"ALIH5\",\"pc\":\"ALICODE\"}', '1', '', '', '0.00', '3', '支付宝（推荐）');
INSERT INTO `merchantpay` VALUES ('73', 'YFT88882018082410001291', '2018-06-06 20:10:00', '2018-06-06 20:10:00', '', '捷易付QQ', '0', '3377af7235334d32ab864deca6cf3f1a', 'http://pcb.dayungame.vip:2080/third/rt.php?rt=/sfb/online_pay_return.php', 'http://www.gdchunkaiwlkj.com/paySuccess_s.jsp', '0.0000', '1', '0', 'jyfqq', '0.0000', '0.0000', '10.00', '3000.00', '6', 'https://gateway.gaogby.com/scanPay/initPay', 'tw', '0', 'https://gateway.meizi50.com/scanPay/initPay', '7', '/sfb/online_pay', '{\"payKey\":\"33e04fe725af4535866eeef651a81b88\",\"pc\":\"70000101\",\"wap\":\"70000201\"}', '1', '', '', '0.00', '3', 'QQ支付6');
INSERT INTO `merchantpay` VALUES ('74', 'YFT88882018082410001291', '2018-06-06 20:10:00', '2018-06-06 20:10:00', '', '捷易付微信', '0', '3377af7235334d32ab864deca6cf3f1a', 'http://pcb.dayungame.vip:2080/third/rt.php?rt=/sfb/online_pay_return.php', 'http://www.gdchunkaiwlkj.com/paySuccess_s.jsp', '0.0000', '1', '0', 'jyfwx', '0.0000', '0.0000', '100.00', '1000.00', '6', 'https://gateway.gaogby.com/scanPay/initPay', 'tw', '0', 'https://gateway.meizi50.com/scanPay/initPay', '2', '/sfb/online_pay', '{\"payKey\":\"33e04fe725af4535866eeef651a81b88\",\"pc\":\"10000103\",\"wap\":\"10000201\"}', '1', '', '', '0.00', '3', '微信支付6');
INSERT INTO `merchantpay` VALUES ('75', 'h0tX2z44Mt', '2018-09-08 16:50:00', '2018-09-08 16:50:00', '', '知付支付宝', '0', 'a22083f3db381fc4eb6f2e938966404a', 'http://pcb.dayungame.vip:2080/zhip/rt.php', 'http://www.gdchunkaiwlkj.com/paySuccess_s.jsp', '0.0000', '1', '0', 'zhipzfb', '0.0000', '0.0000', '50.00', '3000.00', '6', 'https://api.aido88.cn/api_deposit.shtml', 'tw', '0', 'https://api.aido88.cn/api_deposit.shtml', '1', '/zhip/online_pay', null, '1', '', '', '0.00', '3', '支付宝支付1');
INSERT INTO `merchantpay` VALUES ('76', '686cz0927', '2018-09-08 16:50:00', '2018-09-08 16:50:00', '', '686微信', '0', 'JcRyjjLQJBBrtNSUkjOjCVTfv8ApOx4Q', 'http://pcb.dayungame.vip:2080/third/rt.php?rt=/p686/online_pay_return.php', 'http://www.gdchunkaiwlkj.com/paySuccess_s.jsp', '0.0000', '1', '0', 'p686wx', '0.0000', '0.0000', '10.00', '300.00', '6', 'http://120.78.245.232/index.php/686cz/trade/pay', 'tw', '0', 'http://120.78.245.232/index.php/686cz/trade/pay', '2', '/p686/online_pay', '', '1', '', '', '0.00', '3', '微信支付7');
INSERT INTO `merchantpay` VALUES ('77', '210001110014103', '2018-08-04 17:49:30', '2018-08-04 17:49:32', '', '迅联宝微信H5', '0', '808083b2efd42a1567ee6d2c3ddbf930', 'http://pcb.dayungame.vip:2080/xlb/zfb_wx_return.php', '', '0.0000', '1', '0', 'xlbwx', '0.0000', '0.0000', '1.00', '3000.00', '6', 'http://trade.cd178.cn:8880/cgi-bin/netpayment/pay_gate.cgi', 'tw', '0', 'http://pay.yaica.top:88/pay_shop.jsp', '2', '/xlb/zfb_wx', '{\"tradeSummary\":\"a\",\"choosePayType\":\"5\",\"overTime\":\"40\",\"apiName\":\"WAP_PAY_B2C\",\"apiVersion\":\"1.0.0.0\"}', '1', '', '', '0.00', '3', '微信支付1');
INSERT INTO `merchantpay` VALUES ('78', 'TC18120701379', '2018-08-04 17:49:30', '2018-08-04 17:49:32', '', '天诚支付宝', '0', '220ba493daf504752ba84b94e19ea49e', 'http://pcb.dayungame.vip:2080/third/rt.php?rt=/tc/online_pay_return.php', 'http://www.gdchunkaiwlkj.com/paySuccess_s.jsp', '0.0000', '1', '0', 'tczfb', '0.0000', '0.0000', '10.00', '5000.00', '6', 'https://www.citysy.com/api/v3/cashier.php', 'tw', '0', 'https://www.citysy.com/api/v3/cashier.php', '1', '/tc/online_pay', '{\"wap\":\"aph5\",\"pc\":\"ap\"}', '1', '', '', '0.00', '3', '支付宝支付');
INSERT INTO `merchantpay` VALUES ('79', '100392', '2018-06-06 20:10:00', '2018-06-06 20:10:00', '', 'FF支付宝', '0', '248BB6E6630D1BAA2FCB50E71D417F70', 'http://pcb.dayungame.vip:2080/third/rt.php?rt=/ff/online_pay_return.php', 'http://www.gdchunkaiwlkj.com/paySuccess_s.jsp', '0.0000', '1', '0', 'ffzfb', '0.0000', '0.0000', '100.00', '5000.00', '6', 'http://www.ffpay.net/api/pay', 'tw', '0', 'http://www.ffpay.net/api/pay', '1', '/ff/online_pay', '{\"pc\":\"ALIPAY\",\"wap\":\"ALIPAY_WAP\"}', '1', '', '', '0.00', '3', '支付宝支付');
INSERT INTO `merchantpay` VALUES ('80', '100392', '2018-06-06 20:10:00', '2018-06-06 20:10:00', '', 'FF微信', '0', '248BB6E6630D1BAA2FCB50E71D417F70', 'http://pcb.dayungame.vip:2080/third/rt.php?rt=/ff/online_pay_return.php', 'http://www.gdchunkaiwlkj.com/paySuccess_s.jsp', '0.0000', '1', '0', 'ffwx', '0.0000', '0.0000', '100.00', '5000.00', '6', 'http://www.ffpay.net/api/pay', 'tw', '0', 'http://www.ffpay.net/api/pay', '2', '/ff/online_pay', '{\"pc\":\"WECHAT\",\"wap\":\"WECHAT_WAP\"}', '1', '', '', '0.00', '3', '微信支付');
INSERT INTO `merchantpay` VALUES ('81', '100392', '2018-06-06 20:10:00', '2018-06-06 20:10:00', '', 'FF快捷', '0', '248BB6E6630D1BAA2FCB50E71D417F70', 'http://pcb.dayungame.vip:2080/third/rt.php?rt=/ff/online_pay_return.php', 'http://www.gdchunkaiwlkj.com/paySuccess_s.jsp', '0.0000', '1', '0', 'ffkj', '0.0000', '0.0000', '100.00', '5000.00', '6', 'http://www.ffpay.net/api/pay', 'tw', '0', 'http://www.ffpay.net/api/pay', '4', '/ff/online_pay', '{\"pc\":\"EXPRESS\",\"wap\":\"EXPRESS\"}', '1', '', '', '0.00', '3', '快捷支付');
INSERT INTO `merchantpay` VALUES ('82', '100392', '2018-06-06 20:10:00', '2018-06-06 20:10:00', '', 'FF网银', '0', '248BB6E6630D1BAA2FCB50E71D417F70', 'http://pcb.dayungame.vip:2080/third/rt.php?rt=/ff/online_pay_return.php', 'http://www.gdchunkaiwlkj.com/paySuccess_s.jsp', '0.0000', '1', '0', 'ffwy', '0.0000', '0.0000', '100.00', '5000.00', '6', 'http://www.ffpay.net/api/pay', 'tw', '0', 'http://www.ffpay.net/api/pay', '3', '/ff/online_pay', '{\"pc\":\"ALIPAY\",\"wap\":\"ALIPAY_WAP\"}', '1', '', '', '0.00', '3', '在线支付');
INSERT INTO `merchantpay` VALUES ('83', '10057', '2018-06-06 20:10:00', '2018-06-06 20:10:00', '', '万码支付宝', '0', 'v90dmhbh6u0jij4x3irwg1p40cz3gb7p', 'http://pcb.dayungame.vip:2080/third/rt.php?rt=/ym/online_pay_return.php', 'http://www.gdchunkaiwlkj.com/paySuccess_s.jsp', '0.0000', '1', '0', 'wmzfb', '0.0000', '0.0000', '1.00', '5000.00', '6', 'http://rain.wmpayinc.com:8086/Pay_Index.html', 'tw', '0', 'http://rain.wmpayinc.com:8086/Pay_Index.html', '1', '/ym/online_pay', '{\"pc\":\"903\",\"wap\":\"903\"}', '1', '', '', '0.00', '3', '支付宝支付');
INSERT INTO `merchantpay` VALUES ('84', '10057', '2018-06-06 20:10:00', '2018-06-06 20:10:00', '', '万码微信', '0', 'v90dmhbh6u0jij4x3irwg1p40cz3gb7p', 'http://pcb.dayungame.vip:2080/third/rt.php?rt=/ym/online_pay_return.php', 'http://www.gdchunkaiwlkj.com/paySuccess_s.jsp', '0.0000', '1', '0', 'wmwx', '0.0000', '0.0000', '1.00', '5000.00', '6', 'http://rain.wmpayinc.com:8086/Pay_Index.html', 'tw', '0', 'http://rain.wmpayinc.com:8086/Pay_Index.html', '2', '/ym/online_pay', '{\"pc\":\"902\",\"wap\":\"902\"}', '1', '', '', '0.00', '3', '微信支付');

-- ----------------------------
-- Table structure for `mgaddfreegamelog`
-- ----------------------------
DROP TABLE IF EXISTS `mgaddfreegamelog`;
CREATE TABLE `mgaddfreegamelog` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `loginname` varchar(16) NOT NULL,
  `gameid` int(11) NOT NULL,
  `addDate` datetime NOT NULL,
  `operator` varchar(32) DEFAULT NULL COMMENT '操作人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of mgaddfreegamelog
-- ----------------------------

-- ----------------------------
-- Table structure for `mgfreegame`
-- ----------------------------
DROP TABLE IF EXISTS `mgfreegame`;
CREATE TABLE `mgfreegame` (
  `id` int(11) NOT NULL COMMENT '免费游戏ID',
  `description` varchar(32) NOT NULL COMMENT '游戏描述',
  `cost` int(10) NOT NULL COMMENT '费用',
  `playerCount` int(10) DEFAULT NULL COMMENT '当前获得该免费游戏的玩家数量',
  `startDate` datetime NOT NULL COMMENT '开始时间',
  `endDate` datetime NOT NULL COMMENT '结束时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of mgfreegame
-- ----------------------------

-- ----------------------------
-- Table structure for `mglog`
-- ----------------------------
DROP TABLE IF EXISTS `mglog`;
CREATE TABLE `mglog` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `loginname` varchar(32) NOT NULL COMMENT '用户名',
  `actiontype` varchar(10) NOT NULL COMMENT '操作类型',
  `seq` varchar(50) NOT NULL COMMENT 'mgs seq',
  `gameid` varchar(64) NOT NULL,
  `actiontime` datetime NOT NULL,
  `amount` int(255) DEFAULT NULL COMMENT '额度变量',
  `balance` int(255) DEFAULT NULL COMMENT 'mgs余额',
  `remark` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of mglog
-- ----------------------------

-- ----------------------------
-- Table structure for `mgplayer`
-- ----------------------------
DROP TABLE IF EXISTS `mgplayer`;
CREATE TABLE `mgplayer` (
  `loginname` varchar(20) NOT NULL,
  `balance` int(9) NOT NULL DEFAULT '0' COMMENT '余额（单位：分  MGS系统要求额度为整数）   ',
  `tokenA` varchar(32) DEFAULT NULL COMMENT '登录令牌',
  `tokenB` varchar(32) DEFAULT NULL,
  `updateTime` datetime NOT NULL COMMENT '令牌更新时间',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT 'tokenA 状态  1：有效  2：失效',
  PRIMARY KEY (`loginname`),
  UNIQUE KEY `tokenA` (`tokenA`) USING BTREE,
  UNIQUE KEY `tokenB` (`tokenB`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of mgplayer
-- ----------------------------

-- ----------------------------
-- Table structure for `mg_data`
-- ----------------------------
DROP TABLE IF EXISTS `mg_data`;
CREATE TABLE `mg_data` (
  `keyinfo` varchar(64) DEFAULT NULL COMMENT '填入的agentid',
  `colId` varchar(64) DEFAULT NULL COMMENT '第三方系统内部资讯',
  `agentId` int(11) DEFAULT NULL,
  `mbrId` int(11) DEFAULT NULL,
  `mbrCode` varchar(255) DEFAULT NULL COMMENT '玩家账号',
  `transId` varchar(64) NOT NULL COMMENT '交易id',
  `gameId` int(11) DEFAULT NULL COMMENT '游戏编号',
  `transType` varchar(32) DEFAULT NULL COMMENT '交易类型',
  `transTime` datetime DEFAULT NULL COMMENT '交易时间',
  `mgsGameId` varchar(32) DEFAULT NULL COMMENT '每盘游戏id',
  `mgsActionId` varchar(32) DEFAULT NULL COMMENT '后台所记录的Action ID',
  `amnt` double(16,6) DEFAULT NULL COMMENT '交易金额',
  `clrngAmnt` double(16,6) DEFAULT NULL COMMENT '彩池投注或奖金',
  `balance` double(16,6) DEFAULT NULL COMMENT '余额',
  `refTransId` varchar(64) DEFAULT NULL COMMENT 'Refund的transId',
  `refTransType` varchar(32) DEFAULT NULL COMMENT 'refund的引用类型',
  PRIMARY KEY (`transId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of mg_data
-- ----------------------------
INSERT INTO `mg_data` VALUES (null, 'MOBILE', '1406822334', null, 'kavin998', '1406822334', '1002', 'bet', '2020-09-17 15:18:45', '18', '71146567883', '5.000000', '0.000000', '64.000000', null, null);
INSERT INTO `mg_data` VALUES (null, 'MOBILE', '1406824989', null, 'kavin998', '1406824989', '1002', 'bet', '2020-09-17 15:18:43', '17', '71146567132', '5.000000', '0.000000', '69.000000', null, null);
INSERT INTO `mg_data` VALUES (null, 'MOBILE', '1406828634', null, 'kavin998', '1406828634', '1002', 'bet', '2020-09-17 15:18:50', '19', '71146570609', '5.000000', '0.000000', '59.000000', null, null);
INSERT INTO `mg_data` VALUES (null, 'MOBILE', '1406832318', null, 'kavin998', '1406832318', '1002', 'bet', '2020-09-17 15:18:55', '20', '71146573220', '20.000000', '0.000000', '74.000000', null, null);
INSERT INTO `mg_data` VALUES (null, 'MOBILE', '1406838024', null, 'kavin998', '1406838024', '1002', 'bet', '2020-09-17 15:18:59', '21', '71146575073', '10.000000', '0.000000', '79.000000', null, null);
INSERT INTO `mg_data` VALUES (null, 'MOBILE', '1406844614', null, 'kavin998', '1406844614', '1002', 'bet', '2020-09-17 15:18:55', '20', '71146573218', '5.000000', '0.000000', '54.000000', null, null);
INSERT INTO `mg_data` VALUES (null, 'MOBILE', '1406847074', null, 'kavin998', '1406847074', '1002', 'bet', '2020-09-17 15:19:27', '33', '71146590557', '30.000000', '0.000000', '224.000000', null, null);
INSERT INTO `mg_data` VALUES (null, 'MOBILE', '1406852303', null, 'kavin998', '1406852303', '1002', 'bet', '2020-09-17 15:19:10', '26', '71146581017', '10.000000', '0.000000', '74.000000', null, null);
INSERT INTO `mg_data` VALUES (null, 'MOBILE', '1406853034', null, 'kavin998', '1406853034', '1002', 'bet', '2020-09-17 15:19:15', '27', '71146583783', '30.000000', '0.000000', '44.000000', null, null);
INSERT INTO `mg_data` VALUES (null, 'MOBILE', '1406854640', null, 'kavin998', '1406854640', '1002', 'bet', '2020-09-17 15:19:05', '24', '71146578654', '5.000000', '0.000000', '64.000000', null, null);
INSERT INTO `mg_data` VALUES (null, 'MOBILE', '1406857463', null, 'kavin998', '1406857463', '1002', 'bet', '2020-09-17 15:19:05', '23', '71146578208', '5.000000', '0.000000', '69.000000', null, null);
INSERT INTO `mg_data` VALUES (null, 'MOBILE', '1406857554', null, 'kavin998', '1406857554', '1002', 'bet', '2020-09-17 15:19:05', '24', '71146578655', '10.000000', '0.000000', '74.000000', null, null);
INSERT INTO `mg_data` VALUES (null, 'MOBILE', '1406860194', null, 'kavin998', '1406860194', '1002', 'bet', '2020-09-17 15:19:03', '22', '71146577368', '5.000000', '0.000000', '74.000000', null, null);
INSERT INTO `mg_data` VALUES (null, 'MOBILE', '1406861489', null, 'kavin998', '1406861489', '1002', 'bet', '2020-09-17 15:19:15', '27', '71146583784', '360.000000', '0.000000', '404.000000', null, null);
INSERT INTO `mg_data` VALUES (null, 'MOBILE', '1406862535', null, 'kavin998', '1406862535', '1002', 'bet', '2020-09-17 15:18:59', '21', '71146575072', '5.000000', '0.000000', '69.000000', null, null);
INSERT INTO `mg_data` VALUES (null, 'MOBILE', '1406866059', null, 'kavin998', '1406866059', '1002', 'bet', '2020-09-17 15:19:09', '25', '71146580374', '5.000000', '0.000000', '69.000000', null, null);
INSERT INTO `mg_data` VALUES (null, 'MOBILE', '1406866179', null, 'kavin998', '1406866179', '1002', 'bet', '2020-09-17 15:19:10', '26', '71146581016', '5.000000', '0.000000', '64.000000', null, null);
INSERT INTO `mg_data` VALUES (null, 'MOBILE', '1406867726', null, 'kavin998', '1406867726', '1002', 'bet', '2020-09-17 15:19:24', '30', '71146588781', '30.000000', '0.000000', '314.000000', null, null);
INSERT INTO `mg_data` VALUES (null, 'MOBILE', '1406868264', null, 'kavin998', '1406868264', '1002', 'bet', '2020-09-17 15:19:29', '34', '71146591255', '30.000000', '0.000000', '194.000000', null, null);
INSERT INTO `mg_data` VALUES (null, 'MOBILE', '1406875263', null, 'kavin998', '1406875263', '1002', 'bet', '2020-09-17 15:19:25', '31', '71146589171', '30.000000', '0.000000', '284.000000', null, null);
INSERT INTO `mg_data` VALUES (null, 'MOBILE', '1406875717', null, 'kavin998', '1406875717', '1002', 'bet', '2020-09-17 15:19:29', '34', '71146591256', '120.000000', '0.000000', '314.000000', null, null);
INSERT INTO `mg_data` VALUES (null, 'MOBILE', '1406877529', null, 'kavin998', '1406877529', '1002', 'bet', '2020-09-17 15:19:20', '28', '71146586297', '30.000000', '0.000000', '374.000000', null, null);
INSERT INTO `mg_data` VALUES (null, 'MOBILE', '1406878286', null, 'kavin998', '1406878286', '1002', 'bet', '2020-09-17 15:19:26', '32', '71146589662', '30.000000', '0.000000', '254.000000', null, null);
INSERT INTO `mg_data` VALUES (null, 'MOBILE', '1406880850', null, 'kavin998', '1406880850', '1002', 'bet', '2020-09-17 15:19:23', '29', '71146588176', '30.000000', '0.000000', '344.000000', null, null);
INSERT INTO `mg_data` VALUES (null, 'MOBILE', '1427624676', null, 'kavin998', '1427624676', '1002', 'bet', '2020-09-17 20:42:48', '36', '71156265838', '1.000000', '0.000000', '314.000000', null, null);
INSERT INTO `mg_data` VALUES (null, 'MOBILE', '1427628737', null, 'kavin998', '1427628737', '1002', 'bet', '2020-09-17 20:42:36', '35', '71156259348', '1.000000', '0.000000', '313.000000', null, null);
INSERT INTO `mg_data` VALUES (null, 'MOBILE', '1427631599', null, 'kavin998', '1427631599', '1002', 'bet', '2020-09-17 20:42:36', '35', '71156259349', '2.000000', '0.000000', '315.000000', null, null);
INSERT INTO `mg_data` VALUES (null, 'MOBILE', '1427641854', null, 'kavin998', '1427641854', '1002', 'bet', '2020-09-17 20:42:59', '37', '71156271164', '1.000000', '0.000000', '314.000000', null, null);
INSERT INTO `mg_data` VALUES (null, 'MOBILE', '1427649358', null, 'kavin998', '1427649358', '1002', 'bet', '2020-09-17 20:42:48', '36', '71156265839', '1.000000', '0.000000', '315.000000', null, null);
INSERT INTO `mg_data` VALUES (null, 'MOBILE', '1427656332', null, 'kavin998', '1427656332', '1002', 'DEBIT', '2020-09-17 20:43:05', '38', '71156274457', '1.000000', '0.000000', '313.000000', null, null);
INSERT INTO `mg_data` VALUES (null, 'MOBILE', '1427661028', null, 'kavin998', '1427661028', '1002', 'DEBIT', '2020-09-17 20:43:22', '40', '71156283229', '1.000000', '0.000000', '312.350000', null, null);
INSERT INTO `mg_data` VALUES (null, 'MOBILE', '1427665951', null, 'kavin998', '1427665951', '1002', 'CREDIT', '2020-09-17 20:43:14', '39', '71156279258', '1.350000', '0.000000', '313.350000', null, null);
INSERT INTO `mg_data` VALUES (null, 'MOBILE', '1427668789', null, 'kavin998', '1427668789', '1002', 'CREDIT', '2020-09-17 20:43:22', '40', '71156283230', '0.400000', '0.000000', '312.750000', null, null);
INSERT INTO `mg_data` VALUES (null, 'MOBILE', '1427673529', null, 'kavin998', '1427673529', '1002', 'DEBIT', '2020-09-17 20:43:14', '39', '71156279257', '1.000000', '0.000000', '312.000000', null, null);
INSERT INTO `mg_data` VALUES (null, 'MOBILE', '1427703009', null, 'kavin998', '1427703009', '1002', 'DEBIT', '2020-09-17 20:44:03', '42', '71156304404', '1.000000', '0.000000', '310.900000', null, null);
INSERT INTO `mg_data` VALUES (null, 'MOBILE', '1427705011', null, 'kavin998', '1427705011', '1002', 'DEBIT', '2020-09-17 20:43:50', '41', '71156297916', '1.000000', '0.000000', '311.750000', null, null);
INSERT INTO `mg_data` VALUES (null, 'MOBILE', '1427709584', null, 'kavin998', '1427709584', '1002', 'CREDIT', '2020-09-17 20:43:50', '41', '71156297917', '0.150000', '0.000000', '311.900000', null, null);
INSERT INTO `mg_data` VALUES (null, 'MOBILE', '1427726670', null, 'kavin998', '1427726670', '1002', 'DEBIT', '2020-09-17 20:44:19', '44', '71156313070', '2.800000', '0.000000', '307.650000', null, null);
INSERT INTO `mg_data` VALUES (null, 'MOBILE', '1427728302', null, 'kavin998', '1427728302', '1002', 'CREDIT', '2020-09-17 20:44:13', '43', '71156309713', '2.100000', '0.000000', '310.450000', null, null);
INSERT INTO `mg_data` VALUES (null, 'MOBILE', '1427730474', null, 'kavin998', '1427730474', '1002', 'CREDIT', '2020-09-17 20:44:03', '42', '71156304575', '0.250000', '0.000000', '311.150000', null, null);
INSERT INTO `mg_data` VALUES (null, 'MOBILE', '1427731768', null, 'kavin998', '1427731768', '1002', 'DEBIT', '2020-09-17 20:44:13', '43', '71156309712', '2.800000', '0.000000', '308.350000', null, null);
INSERT INTO `mg_data` VALUES (null, 'MOBILE', '1427770264', null, 'kavin998', '1427770264', '1002', 'CREDIT', '2020-09-17 20:44:46', '45', '71156327274', '28.000000', '0.000000', '332.850000', null, null);
INSERT INTO `mg_data` VALUES (null, 'MOBILE', '1427778606', null, 'kavin998', '1427778606', '1002', 'DEBIT', '2020-09-17 20:44:46', '45', '71156327273', '2.800000', '0.000000', '304.850000', null, null);

-- ----------------------------
-- Table structure for `msg_readed`
-- ----------------------------
DROP TABLE IF EXISTS `msg_readed`;
CREATE TABLE `msg_readed` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `mid` int(11) NOT NULL COMMENT '站内信ID',
  `username` varchar(20) NOT NULL COMMENT '用户名',
  `createTime` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=37 DEFAULT CHARSET=utf8 COMMENT='已读公共站内信';

-- ----------------------------
-- Records of msg_readed
-- ----------------------------

-- ----------------------------
-- Table structure for `msusers`
-- ----------------------------
DROP TABLE IF EXISTS `msusers`;
CREATE TABLE `msusers` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `password` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of msusers
-- ----------------------------

-- ----------------------------
-- Table structure for `netpay`
-- ----------------------------
DROP TABLE IF EXISTS `netpay`;
CREATE TABLE `netpay` (
  `name` varchar(20) NOT NULL,
  `merno` varchar(20) DEFAULT NULL,
  `puturl` varchar(200) NOT NULL,
  `flag` int(1) NOT NULL,
  `code` varchar(20) DEFAULT NULL,
  `key` varchar(2000) DEFAULT NULL,
  `next` int(2) DEFAULT NULL,
  `type` int(1) DEFAULT NULL,
  `email` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`name`),
  UNIQUE KEY `name` (`name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='InnoDB free: 11264 kB';

-- ----------------------------
-- Records of netpay
-- ----------------------------

-- ----------------------------
-- Table structure for `newaccount`
-- ----------------------------
DROP TABLE IF EXISTS `newaccount`;
CREATE TABLE `newaccount` (
  `pno` varchar(20) NOT NULL,
  `title` varchar(20) NOT NULL,
  `loginname` varchar(20) NOT NULL,
  `pwd` varchar(20) NOT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `email` varchar(50) DEFAULT NULL,
  `aliasname` varchar(20) DEFAULT NULL,
  `remark` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`pno`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of newaccount
-- ----------------------------

-- ----------------------------
-- Table structure for `offer`
-- ----------------------------
DROP TABLE IF EXISTS `offer`;
CREATE TABLE `offer` (
  `pno` varchar(20) CHARACTER SET utf8 NOT NULL,
  `title` varchar(20) CHARACTER SET utf8 NOT NULL,
  `loginname` varchar(20) CHARACTER SET utf8 NOT NULL,
  `firstCash` double(24,2) NOT NULL,
  `money` double(24,2) NOT NULL,
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL,
  PRIMARY KEY (`pno`),
  UNIQUE KEY `pno` (`pno`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of offer
-- ----------------------------
INSERT INTO `offer` VALUES ('10120091524kav4pkhf', 'MONEY_CUSTOMER', 'kavin998', '0.00', '11.00', '红包雨余额转入DT平台,0倍流水，转入11.0');
INSERT INTO `offer` VALUES ('101200915dcvtqokqmg', 'MONEY_CUSTOMER', 'kavin998', '0.00', '11.00', '红包雨余额转入MG平台,10倍流水，转入11.0');
INSERT INTO `offer` VALUES ('101200915gscjaroeq3', 'MONEY_CUSTOMER', 'kavin998', '0.00', '11.00', '红包雨余额转入MG平台,10倍流水，转入11.0');
INSERT INTO `offer` VALUES ('101200915xoljnbhsne', 'MONEY_CUSTOMER', 'kavin998', '0.00', '11.00', '红包雨余额转入MG平台,10倍流水，转入11.0');
INSERT INTO `offer` VALUES ('1012009207saelfpyf7', 'MONEY_CUSTOMER', 'austin998', '0.00', '13.00', '红包雨余额转入CQ9平台,10倍流水，转入13.0');
INSERT INTO `offer` VALUES ('101200920c4sfxeclru', 'MONEY_CUSTOMER', 'austin998', '0.00', '13.00', '红包雨余额转入CQ9平台,10倍流水，转入13.0');
INSERT INTO `offer` VALUES ('101200920ciqjgw5705', 'MONEY_CUSTOMER', 'austin998', '0.00', '11.00', '红包雨余额转入CQ9平台,10倍流水，转入11.0');
INSERT INTO `offer` VALUES ('101200920dh1rxbzswe', 'MONEY_CUSTOMER', 'austin998', '0.00', '22.00', '红包雨余额转入CQ9平台,10倍流水，转入22.0');
INSERT INTO `offer` VALUES ('101200920f9evlwhixk', 'MONEY_CUSTOMER', 'austin998', '0.00', '11.00', '红包雨余额转入CQ9平台,10倍流水，转入11.0');
INSERT INTO `offer` VALUES ('101200920fr94t5ffh9', 'MONEY_CUSTOMER', 'austin998', '0.00', '11.00', '红包雨余额转入CQ9平台,10倍流水，转入11.0');
INSERT INTO `offer` VALUES ('101200920i0kljulcwy', 'MONEY_CUSTOMER', 'austin998', '0.00', '11.00', '红包雨余额转入CQ9平台,10倍流水，转入11.0');
INSERT INTO `offer` VALUES ('101200920ull1gbzfrq', 'MONEY_CUSTOMER', 'austin998', '0.00', '11.00', '红包雨余额转入CQ9平台,10倍流水，转入11.0');
INSERT INTO `offer` VALUES ('101200920xg7fu452v9', 'MONEY_CUSTOMER', 'austin998', '0.00', '12.00', '红包雨余额转入CQ9平台,10倍流水，转入12.0');
INSERT INTO `offer` VALUES ('1012009273q4fpfztpt', 'MONEY_CUSTOMER', 'kavin998', '0.00', '12.00', '红包雨余额转入CQ9平台,10倍流水，转入12.0');
INSERT INTO `offer` VALUES ('1012009277rm9gg0223', 'MONEY_CUSTOMER', 'kavin998', '0.00', '11.00', '红包雨余额转入CQ9平台,10倍流水，转入11.0');
INSERT INTO `offer` VALUES ('101200927fqno4qg542', 'MONEY_CUSTOMER', 'kavin998', '0.00', '12.00', '红包雨余额转入CQ9平台,10倍流水，转入12.0');
INSERT INTO `offer` VALUES ('101200927lfmgh6l3qp', 'MONEY_CUSTOMER', 'kavin998', '0.00', '11.00', '红包雨余额转入PG平台,10倍流水，转入11.0');
INSERT INTO `offer` VALUES ('101200927xhqnmqauad', 'MONEY_CUSTOMER', 'kavin998', '0.00', '20.00', '红包雨余额转入PG平台,10倍流水，转入20.0');
INSERT INTO `offer` VALUES ('101200930gf2zbdgh5f', 'MONEY_CUSTOMER', 'kavin998', '0.00', '12.00', '红包雨余额转入BG平台,10倍流水，转入12.0');
INSERT INTO `offer` VALUES ('101200930hixnjrxgj7', 'MONEY_CUSTOMER', 'kavin998', '0.00', '12.00', '红包雨余额转入BG平台,10倍流水，转入12.0');
INSERT INTO `offer` VALUES ('101200930m10bxunakg', 'MONEY_CUSTOMER', 'kavin998', '0.00', '12.00', '红包雨余额转入BG平台,10倍流水，转入12.0');
INSERT INTO `offer` VALUES ('101200930pmqlltukju', 'MONEY_CUSTOMER', 'kavin998', '0.00', '12.00', '红包雨余额转入BG平台,10倍流水，转入12.0');
INSERT INTO `offer` VALUES ('1012010050e2sbl3o8k', 'MONEY_CUSTOMER', 'kavin998', '0.00', '11.00', '红包雨余额转入SBA平台,10倍流水，转入11.0');
INSERT INTO `offer` VALUES ('1012010058bzzchcjwe', 'MONEY_CUSTOMER', 'kavin998', '0.00', '150.00', '红包雨余额转入SBA平台,10倍流水，转入150.0');
INSERT INTO `offer` VALUES ('101201005b8mgb6ifl3', 'MONEY_CUSTOMER', 'kavin998', '0.00', '11.00', '红包雨余额转入SBA平台,10倍流水，转入11.0');
INSERT INTO `offer` VALUES ('101201005bnao064ocp', 'MONEY_CUSTOMER', 'kavin998', '0.00', '11.00', '红包雨余额转入SBA平台,10倍流水，转入11.0');
INSERT INTO `offer` VALUES ('101201005etg63bsddi', 'MONEY_CUSTOMER', 'austin998', '0.00', '10.00', '红包雨余额转入MG平台,10倍流水，转入10.0');
INSERT INTO `offer` VALUES ('101201005q9s57kdsjw', 'MONEY_CUSTOMER', 'austin998', '0.00', '10.00', '红包雨余额转入SBA平台,10倍流水，转入10.0');
INSERT INTO `offer` VALUES ('101201005r8rslgdarh', 'MONEY_CUSTOMER', 'austin998', '0.00', '11.00', '红包雨余额转入SBA平台,10倍流水，转入11.0');
INSERT INTO `offer` VALUES ('101201005ycwc1xumsz', 'MONEY_CUSTOMER', 'kavin998', '0.00', '11.00', '红包雨余额转入MG平台,10倍流水，转入11.0');
INSERT INTO `offer` VALUES ('101201008ukdlqp4vyy', 'MONEY_CUSTOMER', 'austin998', '0.00', '11.00', '红包雨余额转入DT平台,0倍流水，转入11.0');

-- ----------------------------
-- Table structure for `online_token`
-- ----------------------------
DROP TABLE IF EXISTS `online_token`;
CREATE TABLE `online_token` (
  `loginname` varchar(50) NOT NULL,
  `token` varchar(200) NOT NULL,
  `createtime` datetime NOT NULL,
  `isused` int(11) DEFAULT '0' COMMENT '0.未使用1.已使用',
  PRIMARY KEY (`loginname`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of online_token
-- ----------------------------
INSERT INTO `online_token` VALUES ('k_kavin998', 'k_0q3CulMsfkqr5KZNGbhi0fRvkJRxHwQaWyhiOiB3de90Ahs', '2020-10-03 16:44:50', null);
INSERT INTO `online_token` VALUES ('k_austin998', 'k_ZkJDQNVAHc3F4FyaIGV4vlcEqFaEU2Y9xGspkAii0eo6glK', '2020-10-04 20:52:44', null);

-- ----------------------------
-- Table structure for `operationlogs`
-- ----------------------------
DROP TABLE IF EXISTS `operationlogs`;
CREATE TABLE `operationlogs` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `loginname` varchar(255) NOT NULL,
  `action` varchar(20) DEFAULT NULL,
  `createtime` datetime DEFAULT NULL,
  `remark` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`) USING BTREE,
  KEY `loginname` (`loginname`) USING BTREE,
  KEY `action` (`action`) USING BTREE,
  KEY `createtime` (`createtime`) USING BTREE,
  KEY `remark` (`remark`(255)) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2529212 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of operationlogs
-- ----------------------------
INSERT INTO `operationlogs` VALUES ('2529161', 'admin', 'LOGIN', '2018-12-04 23:55:36', '112.208.138.3');
INSERT INTO `operationlogs` VALUES ('2529162', 'don', 'LOGIN', '2018-12-04 23:59:33', '45.32.9.147');
INSERT INTO `operationlogs` VALUES ('2529163', 'wind', 'LOGIN', '2018-12-05 10:44:29', '180.190.113.46');
INSERT INTO `operationlogs` VALUES ('2529164', 'don', 'LOGIN', '2018-12-05 12:34:13', '180.190.113.46');
INSERT INTO `operationlogs` VALUES ('2529165', 'wind', 'MODIFY_OWN_PWD', '2018-12-16 15:03:04', null);
INSERT INTO `operationlogs` VALUES ('2529166', 'wind', 'LOGIN', '2018-12-16 15:03:14', '45.32.9.147');
INSERT INTO `operationlogs` VALUES ('2529167', 'twtest01', 'CHANGE_CREDIT_QUOTAL', '2018-12-16 15:03:54', 'wind为用户twtest01增加/扣除500.00');
INSERT INTO `operationlogs` VALUES ('2529168', 'wind', 'REPAIR_PAYORDER', '2018-12-16 15:04:15', '45.32.9.147  (额度增减单号:1 金额：500.0) 说明:');
INSERT INTO `operationlogs` VALUES ('2529169', 'wind', 'LOGIN', '2018-12-16 15:08:01', '45.32.9.147');
INSERT INTO `operationlogs` VALUES ('2529170', 'wind', 'LOGIN', '2018-12-22 11:10:57', '45.32.9.147');
INSERT INTO `operationlogs` VALUES ('2529171', 'wind', 'MODIFY_OWN_PWD', '2018-12-22 11:20:13', null);
INSERT INTO `operationlogs` VALUES ('2529172', 'admin', 'LOGIN', '2018-12-23 08:56:53', '45.32.9.147');
INSERT INTO `operationlogs` VALUES ('2529173', 'admin', 'LOGIN', '2018-12-23 11:31:00', '45.32.9.147');
INSERT INTO `operationlogs` VALUES ('2529174', 'wind', 'LOGIN', '2018-12-23 12:13:43', '45.32.9.147');
INSERT INTO `operationlogs` VALUES ('2529175', 'wind', 'SETLEVEL', '2018-12-23 14:43:29', '将会员skytest的等级从[天兵]改为[天王]');
INSERT INTO `operationlogs` VALUES ('2529176', 'wind', 'LOGIN', '2018-12-24 10:55:12', '45.32.9.147');
INSERT INTO `operationlogs` VALUES ('2529177', 'admin', 'LOGIN', '2018-12-24 11:16:43', '112.206.187.194');
INSERT INTO `operationlogs` VALUES ('2529178', 'wind', 'LOGIN', '2018-12-24 11:23:42', '45.32.9.147');
INSERT INTO `operationlogs` VALUES ('2529179', 'wind', 'LOGIN', '2018-12-24 15:55:42', '45.32.9.147');
INSERT INTO `operationlogs` VALUES ('2529180', 'wind', 'LOGIN', '2018-12-26 14:20:34', '45.32.9.147');
INSERT INTO `operationlogs` VALUES ('2529181', 'wind', 'LOGIN', '2018-12-27 11:26:30', '45.32.9.147');
INSERT INTO `operationlogs` VALUES ('2529182', 'wind', 'LOGIN', '2018-12-27 22:47:11', '45.32.9.147');
INSERT INTO `operationlogs` VALUES ('2529183', 'don', 'LOGIN', '2018-12-28 01:05:47', '45.32.9.147');
INSERT INTO `operationlogs` VALUES ('2529184', 'wind', 'LOGIN', '2018-12-28 12:37:40', '45.32.9.147');
INSERT INTO `operationlogs` VALUES ('2529185', 'wind', 'MODIFY_OWN_PWD', '2018-12-29 15:07:20', null);
INSERT INTO `operationlogs` VALUES ('2529186', 'wind', 'LOGIN', '2018-12-29 15:07:31', '45.32.9.147');
INSERT INTO `operationlogs` VALUES ('2529187', 'wind', 'LOGIN', '2018-12-30 11:35:17', '45.32.9.147');
INSERT INTO `operationlogs` VALUES ('2529188', 'admin', 'LOGIN', '2020-09-04 20:59:59', '0:0:0:0:0:0:0:1');
INSERT INTO `operationlogs` VALUES ('2529189', 'admin', 'LOGIN', '2020-09-04 21:42:15', '0:0:0:0:0:0:0:1');
INSERT INTO `operationlogs` VALUES ('2529190', 'admin', 'LOGIN', '2020-09-05 21:39:02', '0:0:0:0:0:0:0:1');
INSERT INTO `operationlogs` VALUES ('2529191', 'kavin998', 'CHANGE_CREDIT_QUOTAL', '2020-09-05 22:02:47', 'admin为用户kavin998增加/扣除10000.00');
INSERT INTO `operationlogs` VALUES ('2529192', 'admin', 'REPAIR_PAYORDER', '2020-09-05 22:05:00', '0:0:0:0:0:0:0:1  (额度增减单号:2 金额：10000.0) 说明:手工存款kavin');
INSERT INTO `operationlogs` VALUES ('2529193', 'admin', 'LOGIN', '2020-09-06 13:20:13', '0:0:0:0:0:0:0:1');
INSERT INTO `operationlogs` VALUES ('2529194', 'admin', 'LOGIN', '2020-09-16 13:10:04', '0:0:0:0:0:0:0:1');
INSERT INTO `operationlogs` VALUES ('2529195', 'admin', 'LOGIN', '2020-09-20 14:55:20', '0:0:0:0:0:0:0:1');
INSERT INTO `operationlogs` VALUES ('2529196', 'austin998', 'CHANGE_CREDIT_QUOTAL', '2020-09-20 14:58:46', 'admin为用户austin998老虎机佣金增加/扣除10000.00');
INSERT INTO `operationlogs` VALUES ('2529197', 'admin', 'REPAIR_PAYORDER', '2020-09-20 14:59:36', '0:0:0:0:0:0:0:1  (额度增减单号:3 金额：10000.0) 说明:代加10000元，austin998');
INSERT INTO `operationlogs` VALUES ('2529198', 'admin', 'SETWARNLEVEL', '2020-09-20 15:00:50', '将会员austin998的警告等级从[危险]改为[安全]备注：');
INSERT INTO `operationlogs` VALUES ('2529199', 'austin998', 'CHANGE_CREDIT_QUOTAL', '2020-09-20 15:06:50', 'admin为用户austin998老虎机佣金增加/扣除9999.00');
INSERT INTO `operationlogs` VALUES ('2529200', 'admin', 'REPAIR_PAYORDER', '2020-09-20 15:07:21', '0:0:0:0:0:0:0:1  (额度增减单号:4 金额：9999.0) 说明:反对反对');
INSERT INTO `operationlogs` VALUES ('2529201', 'kavin998', 'CHANGE_CREDIT_QUOTAL', '2020-09-20 15:12:15', 'admin为用户kavin998增加/扣除1111.00');
INSERT INTO `operationlogs` VALUES ('2529202', 'admin', 'REPAIR_PAYORDER', '2020-09-20 15:12:25', '0:0:0:0:0:0:0:1  (额度增减单号:5 金额：1111.0) 说明:热热热');
INSERT INTO `operationlogs` VALUES ('2529203', 'austin998', 'CHANGE_CREDIT_QUOTAL', '2020-09-20 15:13:21', 'admin为用户austin998增加/扣除10000.00');
INSERT INTO `operationlogs` VALUES ('2529204', 'admin', 'REPAIR_PAYORDER', '2020-09-20 15:13:29', '0:0:0:0:0:0:0:1  (额度增减单号:6 金额：10000.0) 说明:同仁堂率 ');
INSERT INTO `operationlogs` VALUES ('2529205', 'admin', 'LOGIN', '2020-09-22 19:24:20', '0:0:0:0:0:0:0:1');
INSERT INTO `operationlogs` VALUES ('2529206', 'admin', 'LOGIN', '2020-09-22 19:35:41', '0:0:0:0:0:0:0:1');
INSERT INTO `operationlogs` VALUES ('2529207', 'admin', 'LOGIN', '2020-09-22 19:59:43', '0:0:0:0:0:0:0:1');
INSERT INTO `operationlogs` VALUES ('2529208', 'admin', 'LOGIN', '2020-09-22 21:08:01', '0:0:0:0:0:0:0:1');
INSERT INTO `operationlogs` VALUES ('2529209', 'admin', 'LOGIN', '2020-09-23 17:51:42', '0:0:0:0:0:0:0:1');
INSERT INTO `operationlogs` VALUES ('2529210', 'admin', 'LOGIN', '2020-09-23 23:01:43', '0:0:0:0:0:0:0:1');
INSERT INTO `operationlogs` VALUES ('2529211', 'admin', 'LOGIN', '2020-09-24 00:22:43', '0:0:0:0:0:0:0:1');

-- ----------------------------
-- Table structure for `operator`
-- ----------------------------
DROP TABLE IF EXISTS `operator`;
CREATE TABLE `operator` (
  `username` varchar(20) NOT NULL,
  `password` varchar(32) NOT NULL,
  `email` varchar(32) DEFAULT NULL,
  `enabled` int(1) NOT NULL DEFAULT '0',
  `authority` varchar(20) DEFAULT NULL,
  `loginTimes` int(11) NOT NULL DEFAULT '0',
  `lastLoginTime` datetime DEFAULT NULL,
  `lastLoginIp` varchar(100) DEFAULT NULL,
  `createTime` datetime NOT NULL,
  `firstDayWeek` date DEFAULT NULL,
  `phoneno` varchar(32) DEFAULT NULL,
  `phonenoGX` varchar(32) DEFAULT NULL COMMENT '国信的坐席号码',
  `phonenoBL` varchar(32) DEFAULT NULL COMMENT '比邻的坐席号码',
  `cs` varchar(10) DEFAULT NULL,
  `type` varchar(10) DEFAULT NULL,
  `partner` varchar(20) DEFAULT NULL,
  `agent` varchar(255) DEFAULT NULL COMMENT '代理账号.使用,作多账号CONCAT',
  `blserver_url` varchar(255) DEFAULT NULL,
  `passwordNumber` int(32) DEFAULT '0',
  `randomStr` varchar(255) DEFAULT NULL,
  `validType` int(11) DEFAULT NULL,
  `smsPwd` varchar(50) DEFAULT NULL,
  `smsUpdateTime` datetime DEFAULT NULL,
  `employeeNo` varchar(20) DEFAULT NULL,
  `subRole` varchar(32) DEFAULT NULL COMMENT '如果具备创建用户的权限,可创建的用户角色',
  `remark` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`username`),
  UNIQUE KEY `username` (`username`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of operator
-- ----------------------------
INSERT INTO `operator` VALUES ('admin', 'e10adc3949ba59abbe56e057f20f883e', '', '0', 'boss', '20', '2020-09-24 00:22:43', '0:0:0:0:0:0:0:1', '2018-12-04 23:55:36', '2020-12-04', null, null, null, null, null, null, null, null, '0', '2826461621', '3', null, null, null, '1', null);
INSERT INTO `operator` VALUES ('austin', '999ca6bf91929266e802d2ba93203f8e', 'austins9898@gmail.com', '0', 'boss', '0', null, null, '2020-09-24 00:24:02', '1970-01-01', '', '', '', null, null, null, null, null, '0', null, '3', null, null, '', null, null);
INSERT INTO `operator` VALUES ('don', 'e10adc3949ba59abbe56e057f20f883e', '', '0', 'boss', '3', '2018-12-28 01:05:47', '45.32.9.147', '2018-12-04 23:55:36', '2020-12-04', '', '', '', '', '', '', '', '', '0', '7097921977', '3', null, null, '', '1', '');
INSERT INTO `operator` VALUES ('wind', 'ef772d2aa5fee901c12ac6a364e10bed', null, '0', 'boss', '14', '2018-12-30 11:35:17', '45.32.9.147', '2020-12-04 23:55:36', '2018-12-29', null, null, null, null, null, null, null, null, '0', '6390848950', '3', null, null, null, '1', null);

-- ----------------------------
-- Table structure for `other_customer`
-- ----------------------------
DROP TABLE IF EXISTS `other_customer`;
CREATE TABLE `other_customer` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(32) DEFAULT '',
  `email` varchar(32) DEFAULT '',
  `phone` varchar(32) DEFAULT '',
  `isreg` int(1) DEFAULT '0' COMMENT '0未注册1已注册',
  `isdeposit` int(1) DEFAULT '0' COMMENT '0未存款1已存款',
  `phonestatus` int(1) DEFAULT '0' COMMENT '0未拨打1正常2无人接听3空号',
  `userstatus` int(1) DEFAULT '0' COMMENT '0未处理1良好2一般3拒绝',
  `cs` varchar(32) DEFAULT '',
  `remark` varchar(100) DEFAULT '',
  `createTime` datetime DEFAULT NULL,
  `batch` int(11) DEFAULT '1',
  `shippingcode` varchar(50) DEFAULT NULL,
  `type` char(4) DEFAULT NULL,
  `qq` varchar(255) DEFAULT NULL,
  `noticeTime` datetime DEFAULT NULL,
  `win` double(20,2) DEFAULT '0.00',
  `emailflag` int(2) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of other_customer
-- ----------------------------

-- ----------------------------
-- Table structure for `other_customer_new`
-- ----------------------------
DROP TABLE IF EXISTS `other_customer_new`;
CREATE TABLE `other_customer_new` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(32) DEFAULT '',
  `email` varchar(32) DEFAULT '',
  `phone` varchar(32) DEFAULT '',
  `isreg` int(1) DEFAULT '0' COMMENT '0未注册1已注册',
  `isdeposit` int(1) DEFAULT '0' COMMENT '0未存款1已存款',
  `phonestatus` int(1) DEFAULT '0' COMMENT '0未拨打1正常2无人接听3空号',
  `userstatus` int(1) DEFAULT '0' COMMENT '0未处理1良好2一般3拒绝',
  `cs` varchar(32) DEFAULT '',
  `remark` varchar(100) DEFAULT '',
  `createTime` datetime DEFAULT NULL,
  `batch` int(11) DEFAULT '1',
  `shippingcode` varchar(50) DEFAULT NULL,
  `type` char(4) DEFAULT NULL,
  `qq` varchar(255) DEFAULT NULL,
  `noticeTime` datetime DEFAULT NULL,
  `win` double(20,2) DEFAULT '0.00',
  `emailflag` int(2) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of other_customer_new
-- ----------------------------

-- ----------------------------
-- Table structure for `other_recordmail`
-- ----------------------------
DROP TABLE IF EXISTS `other_recordmail`;
CREATE TABLE `other_recordmail` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `email` varchar(60) NOT NULL,
  `status` varchar(10) DEFAULT NULL,
  `createtime` datetime DEFAULT NULL,
  `remark` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=156 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of other_recordmail
-- ----------------------------

-- ----------------------------
-- Table structure for `paymerchant`
-- ----------------------------
DROP TABLE IF EXISTS `paymerchant`;
CREATE TABLE `paymerchant` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `constid` varchar(32) DEFAULT NULL,
  `merchantcode` varchar(32) DEFAULT NULL,
  `createtime` datetime DEFAULT NULL,
  `updatetime` datetime DEFAULT NULL,
  `updateby` varchar(32) DEFAULT NULL,
  `note` varchar(255) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=31 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of paymerchant
-- ----------------------------

-- ----------------------------
-- Table structure for `payorder`
-- ----------------------------
DROP TABLE IF EXISTS `payorder`;
CREATE TABLE `payorder` (
  `billno` varchar(30) NOT NULL,
  `payPlatform` varchar(20) NOT NULL,
  `money` double(24,2) NOT NULL,
  `newaccount` int(1) NOT NULL,
  `password` varchar(20) DEFAULT NULL,
  `loginname` varchar(20) NOT NULL,
  `aliasName` varchar(50) DEFAULT NULL,
  `phone` varchar(30) DEFAULT NULL,
  `email` varchar(50) DEFAULT NULL,
  `partner` varchar(20) DEFAULT NULL,
  `attach` varchar(100) DEFAULT NULL,
  `msg` varchar(100) DEFAULT NULL,
  `md5info` varchar(256) DEFAULT NULL,
  `returnTime` datetime DEFAULT NULL,
  `ip` varchar(16) DEFAULT NULL,
  `createTime` datetime NOT NULL,
  `flag` int(1) NOT NULL,
  `type` int(1) DEFAULT '0',
  PRIMARY KEY (`billno`),
  UNIQUE KEY `billno` (`billno`) USING BTREE,
  KEY `payPlatform` (`payPlatform`) USING BTREE,
  KEY `loginname` (`loginname`) USING BTREE,
  KEY `createTime` (`createTime`) USING BTREE,
  KEY `flag` (`flag`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of payorder
-- ----------------------------

-- ----------------------------
-- Table structure for `payorderbillno`
-- ----------------------------
DROP TABLE IF EXISTS `payorderbillno`;
CREATE TABLE `payorderbillno` (
  `billno` varchar(100) NOT NULL,
  `loginname` varchar(32) DEFAULT NULL,
  `money` double(11,0) DEFAULT NULL,
  `payplatform` varchar(10) DEFAULT NULL,
  `remark` varchar(80) DEFAULT NULL,
  PRIMARY KEY (`billno`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of payorderbillno
-- ----------------------------

-- ----------------------------
-- Table structure for `payorder_validation`
-- ----------------------------
DROP TABLE IF EXISTS `payorder_validation`;
CREATE TABLE `payorder_validation` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `username` varchar(20) NOT NULL COMMENT '用户名',
  `amount` decimal(8,2) NOT NULL COMMENT '金额',
  `originalAmount` decimal(8,2) NOT NULL COMMENT '计划存入金额',
  `createtime` datetime NOT NULL COMMENT '订单创建时间',
  `status` char(1) NOT NULL DEFAULT '0' COMMENT '0: 未到账  1：到帐 2：过期作废',
  `arriveTime` datetime DEFAULT NULL COMMENT '到帐时间',
  `bankcard` varchar(30) DEFAULT NULL COMMENT '存入的银行卡账号',
  `code` varchar(10) DEFAULT NULL COMMENT '无实际意义，前后台交互做标识用',
  `transferID` int(11) DEFAULT NULL COMMENT '额度验证存款记录ID',
  `remark` varchar(50) DEFAULT NULL COMMENT '备注',
  `type` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_transferID` (`transferID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of payorder_validation
-- ----------------------------

-- ----------------------------
-- Table structure for `pay_credit`
-- ----------------------------
DROP TABLE IF EXISTS `pay_credit`;
CREATE TABLE `pay_credit` (
  `pay_order_no` varchar(30) NOT NULL COMMENT '支付订单号',
  `loginname` varchar(20) NOT NULL DEFAULT '' COMMENT '用户名',
  `agent_name` varchar(20) NOT NULL DEFAULT '' COMMENT '代理名称',
  `intro` varchar(20) NOT NULL DEFAULT '' COMMENT '用户推荐码',
  `partner` varchar(20) NOT NULL DEFAULT '' COMMENT '代理推荐码',
  `user_create_time` datetime DEFAULT NULL COMMENT '用户注册时间',
  `pay_platform` varchar(10) NOT NULL COMMENT '支付平台',
  `pay_way` int(2) DEFAULT NULL COMMENT '支付方式 1.支付宝 2.微信 3.在线支付 4.快捷支付 5.点卡支付 6.秒存',
  `deposit` double(16,2) DEFAULT '0.00' COMMENT '用户存款',
  `first_deposit` int(2) DEFAULT '0' COMMENT '是否首存  1.首存 0 不是收存',
  `user_fee` double(6,2) DEFAULT '0.00' COMMENT '支付手续费',
  `user_get` double(16,2) DEFAULT '0.00' COMMENT '扣除手续费后的用户存款',
  `bank_fee` double(6,2) DEFAULT '0.00' COMMENT '银行手续费',
  `bank_get` double(16,2) DEFAULT '0.00' COMMENT '扣除手续费后的存款银行存款',
  `remark` varchar(200) DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`pay_order_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of pay_credit
-- ----------------------------

-- ----------------------------
-- Table structure for `pg_data`
-- ----------------------------
DROP TABLE IF EXISTS `pg_data`;
CREATE TABLE `pg_data` (
  `billNo` varchar(32) NOT NULL COMMENT '流水号',
  `gameName` varchar(32) DEFAULT NULL COMMENT '游戏名称',
  `gameCode` varchar(32) DEFAULT NULL COMMENT '游戏编码(游戏ID后面转)',
  `playName` varchar(32) DEFAULT NULL COMMENT '玩家账号',
  `betAmount` decimal(24,2) DEFAULT NULL COMMENT '投注额',
  `validBetAmount` decimal(24,2) DEFAULT NULL COMMENT '有效投注额',
  `winAmount` decimal(24,2) DEFAULT NULL COMMENT '玩家所赢的金额',
  `balanceBefore` decimal(24,2) DEFAULT NULL COMMENT '玩家交易前余额',
  `balanceAfter` decimal(24,2) DEFAULT NULL COMMENT '玩家交易后余额',
  `gameType` int(2) DEFAULT NULL COMMENT '游戏类型',
  `deviceType` int(2) DEFAULT NULL COMMENT '设备类型(1 Windows 2 macOS 3 Android 4 iOS 5 其它)',
  `betTime` datetime DEFAULT NULL COMMENT '下注时间',
  `platform` varchar(32) DEFAULT NULL COMMENT '平台',
  `settleTime` datetime DEFAULT NULL COMMENT '派彩时间',
  `createTime` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`billNo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of pg_data
-- ----------------------------
INSERT INTO `pg_data` VALUES ('1310181651233964032', null, '1', 'kavin998', '5.40', '5.40', '3.60', '127.10', '125.30', '1', '4', '2020-09-27 19:37:02', 'PG', '2020-09-27 19:38:15', '2020-09-27 19:45:00');
INSERT INTO `pg_data` VALUES ('1310181662285946880', null, '1', 'kavin998', '5.40', '5.40', '0.00', '125.30', '119.90', '1', '4', '2020-09-27 19:37:04', 'PG', '2020-09-27 19:38:20', '2020-09-27 19:45:00');
INSERT INTO `pg_data` VALUES ('1310181674596229121', null, '1', 'kavin998', '5.40', '5.40', '1.08', '119.90', '115.58', '1', '4', '2020-09-27 19:37:07', 'PG', '2020-09-27 19:38:14', '2020-09-27 19:45:00');
INSERT INTO `pg_data` VALUES ('1310181686902317056', null, '1', 'kavin998', '5.40', '5.40', '0.00', '115.58', '110.18', '1', '4', '2020-09-27 19:37:10', 'PG', '2020-09-27 19:38:13', '2020-09-27 19:45:00');
INSERT INTO `pg_data` VALUES ('1310181706833657856', null, '1', 'kavin998', '5.40', '5.40', '39.60', '110.18', '144.38', '1', '4', '2020-09-27 19:37:15', 'PG', '2020-09-27 19:38:17', '2020-09-27 19:45:00');
INSERT INTO `pg_data` VALUES ('1310181752434122752', null, '1', 'kavin998', '5.40', '5.40', '0.00', '144.38', '138.98', '1', '4', '2020-09-27 19:37:26', 'PG', '2020-09-27 19:38:21', '2020-09-27 19:45:00');
INSERT INTO `pg_data` VALUES ('1310181763473530880', null, '1', 'kavin998', '5.40', '5.40', '1.44', '138.98', '135.02', '1', '4', '2020-09-27 19:37:29', 'PG', '2020-09-27 19:38:16', '2020-09-27 19:45:00');
INSERT INTO `pg_data` VALUES ('1310181775792209920', null, '1', 'kavin998', '5.40', '5.40', '0.90', '135.02', '130.52', '1', '4', '2020-09-27 19:37:31', 'PG', '2020-09-27 19:38:11', '2020-09-27 19:45:00');
INSERT INTO `pg_data` VALUES ('1310181785573318656', null, '1', 'kavin998', '5.40', '5.40', '5.40', '130.52', '130.52', '1', '4', '2020-09-27 19:37:34', 'PG', '2020-09-27 19:38:21', '2020-09-27 19:45:00');
INSERT INTO `pg_data` VALUES ('1310181796625309696', null, '1', 'kavin998', '5.40', '5.40', '18.72', '130.52', '143.84', '1', '4', '2020-09-27 19:37:36', 'PG', '2020-09-27 19:38:11', '2020-09-27 19:45:00');
INSERT INTO `pg_data` VALUES ('1310181851943985152', null, '1', 'kavin998', '0.00', '0.00', '1.62', '143.84', '145.46', '1', '4', '2020-09-27 19:37:50', 'PG', '2020-09-27 19:38:15', '2020-09-27 19:45:00');
INSERT INTO `pg_data` VALUES ('1310181867462909952', null, '1', 'kavin998', '5.40', '5.40', '0.00', '145.46', '140.06', '1', '4', '2020-09-27 19:37:53', 'PG', '2020-09-27 19:38:10', '2020-09-27 19:45:00');
INSERT INTO `pg_data` VALUES ('1310181878514909184', null, '1', 'kavin998', '5.40', '5.40', '0.00', '140.06', '134.66', '1', '4', '2020-09-27 19:37:56', 'PG', '2020-09-27 19:38:19', '2020-09-27 19:45:00');
INSERT INTO `pg_data` VALUES ('1310181894625230848', null, '1', 'kavin998', '5.40', '5.40', '0.00', '134.66', '129.26', '1', '4', '2020-09-27 19:38:00', 'PG', '2020-09-27 19:39:14', '2020-09-27 19:45:00');
INSERT INTO `pg_data` VALUES ('1310181913545728000', null, '1', 'kavin998', '5.40', '5.40', '0.00', '129.26', '123.86', '1', '4', '2020-09-27 19:38:04', 'PG', '2020-09-27 19:39:13', '2020-09-27 19:45:00');
INSERT INTO `pg_data` VALUES ('1310181922362163200', null, '1', 'kavin998', '5.40', '5.40', '0.00', '123.86', '118.46', '1', '4', '2020-09-27 19:38:06', 'PG', '2020-09-27 19:39:10', '2020-09-27 19:45:00');
INSERT INTO `pg_data` VALUES ('1310181933481263104', null, '1', 'kavin998', '5.40', '5.40', '14.76', '118.46', '127.82', '1', '4', '2020-09-27 19:38:09', 'PG', '2020-09-27 19:39:21', '2020-09-27 19:45:00');
INSERT INTO `pg_data` VALUES ('1310181960266080256', null, '1', 'kavin998', '5.40', '5.40', '1.44', '127.82', '123.86', '1', '4', '2020-09-27 19:38:15', 'PG', '2020-09-27 19:39:16', '2020-09-27 19:45:00');
INSERT INTO `pg_data` VALUES ('1310181971309682688', null, '1', 'kavin998', '5.40', '5.40', '0.00', '123.86', '118.46', '1', '4', '2020-09-27 19:38:18', 'PG', '2020-09-27 19:39:12', '2020-09-27 19:45:00');
INSERT INTO `pg_data` VALUES ('1310181982370062336', null, '1', 'kavin998', '5.40', '5.40', '28.08', '118.46', '141.14', '1', '4', '2020-09-27 19:38:21', 'PG', '2020-09-27 19:39:10', '2020-09-27 19:45:00');
INSERT INTO `pg_data` VALUES ('1310182025651085312', null, '1', 'kavin998', '5.40', '5.40', '0.00', '141.14', '135.74', '1', '4', '2020-09-27 19:38:31', 'PG', '2020-09-27 19:39:14', '2020-09-27 19:45:00');
INSERT INTO `pg_data` VALUES ('1310182036765990912', null, '1', 'kavin998', '5.40', '5.40', '0.00', '135.74', '130.34', '1', '4', '2020-09-27 19:38:34', 'PG', '2020-09-27 19:39:15', '2020-09-27 19:45:00');
INSERT INTO `pg_data` VALUES ('1310182047817990144', null, '1', 'kavin998', '5.40', '5.40', '0.00', '130.34', '124.94', '1', '4', '2020-09-27 19:38:36', 'PG', '2020-09-27 19:39:11', '2020-09-27 19:45:00');
INSERT INTO `pg_data` VALUES ('1310182084044193792', null, '1', 'kavin998', '0.00', '0.00', '11.88', '124.94', '136.82', '1', '4', '2020-09-27 19:38:45', 'PG', '2020-09-27 19:39:15', '2020-09-27 19:45:00');
INSERT INTO `pg_data` VALUES ('1310182106282385408', null, '1', 'kavin998', '5.40', '5.40', '0.00', '136.82', '131.42', '1', '4', '2020-09-27 19:38:50', 'PG', '2020-09-27 19:39:11', '2020-09-27 19:45:00');
INSERT INTO `pg_data` VALUES ('1310182117539905536', null, '1', 'kavin998', '5.40', '5.40', '0.00', '131.42', '126.02', '1', '4', '2020-09-27 19:38:53', 'PG', '2020-09-27 19:39:13', '2020-09-27 19:45:00');
INSERT INTO `pg_data` VALUES ('1310183380004438016', null, '1', 'kavin998', '5.40', '5.40', '1.62', '115.22', '111.44', '1', '4', '2020-09-27 19:43:54', 'PG', '2020-09-27 19:44:19', '2020-09-27 19:51:00');
INSERT INTO `pg_data` VALUES ('1310183390582472704', null, '1', 'kavin998', '5.40', '5.40', '2.16', '111.44', '108.20', '1', '4', '2020-09-27 19:43:56', 'PG', '2020-09-27 19:44:14', '2020-09-27 19:51:00');
INSERT INTO `pg_data` VALUES ('1310183401626075136', null, '1', 'kavin998', '5.40', '5.40', '0.00', '108.20', '102.80', '1', '4', '2020-09-27 19:43:59', 'PG', '2020-09-27 19:44:11', '2020-09-27 19:51:00');
INSERT INTO `pg_data` VALUES ('1310183412715806720', null, '1', 'kavin998', '5.40', '5.40', '0.00', '102.80', '97.40', '1', '4', '2020-09-27 19:44:02', 'PG', '2020-09-27 19:45:21', '2020-09-27 19:51:00');
INSERT INTO `pg_data` VALUES ('1310183423721668608', null, '1', 'kavin998', '5.40', '5.40', '0.00', '97.40', '92.00', '1', '4', '2020-09-27 19:44:04', 'PG', '2020-09-27 19:45:17', '2020-09-27 19:51:00');
INSERT INTO `pg_data` VALUES ('1310183434786234368', null, '1', 'kavin998', '5.40', '5.40', '12.06', '92.00', '98.66', '1', '4', '2020-09-27 19:44:07', 'PG', '2020-09-27 19:45:10', '2020-09-27 19:51:00');
INSERT INTO `pg_data` VALUES ('1310183445825650688', null, '1', 'kavin998', '5.40', '5.40', '1.80', '98.66', '95.06', '1', '4', '2020-09-27 19:44:10', 'PG', '2020-09-27 19:45:20', '2020-09-27 19:51:00');
INSERT INTO `pg_data` VALUES ('1310183456877641728', null, '1', 'kavin998', '5.40', '5.40', '7.20', '95.06', '96.86', '1', '4', '2020-09-27 19:44:12', 'PG', '2020-09-27 19:45:13', '2020-09-27 19:51:00');
INSERT INTO `pg_data` VALUES ('1310183467933827072', null, '1', 'kavin998', '5.40', '5.40', '0.00', '96.86', '91.46', '1', '4', '2020-09-27 19:44:15', 'PG', '2020-09-27 19:45:17', '2020-09-27 19:51:00');
INSERT INTO `pg_data` VALUES ('1310183575261863936', null, '1', 'kavin998', '0.00', '0.00', '2.16', '91.46', '93.62', '1', '4', '2020-09-27 19:44:41', 'PG', '2020-09-27 19:45:21', '2020-09-27 19:51:00');

-- ----------------------------
-- Table structure for `phone_code`
-- ----------------------------
DROP TABLE IF EXISTS `phone_code`;
CREATE TABLE `phone_code` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `loginname` varchar(255) NOT NULL,
  `code` varchar(255) NOT NULL,
  `createtime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of phone_code
-- ----------------------------

-- ----------------------------
-- Table structure for `phone_wrong`
-- ----------------------------
DROP TABLE IF EXISTS `phone_wrong`;
CREATE TABLE `phone_wrong` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `phone` varchar(255) NOT NULL,
  `remark` varchar(255) DEFAULT NULL,
  `createtime` datetime DEFAULT NULL,
  `aes_phone` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=36578 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of phone_wrong
-- ----------------------------

-- ----------------------------
-- Table structure for `platform_account_status`
-- ----------------------------
DROP TABLE IF EXISTS `platform_account_status`;
CREATE TABLE `platform_account_status` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `loginname` varchar(32) DEFAULT NULL COMMENT '玩家账号',
  `platform` varchar(32) DEFAULT NULL COMMENT '游戏平台',
  `status` char(1) DEFAULT NULL COMMENT '状态（0冻结，1解冻）',
  `operator` varchar(32) DEFAULT NULL COMMENT '操作人',
  `operate_time` datetime DEFAULT NULL COMMENT '操作时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `loginname_platform` (`loginname`,`platform`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of platform_account_status
-- ----------------------------

-- ----------------------------
-- Table structure for `platform_data`
-- ----------------------------
DROP TABLE IF EXISTS `platform_data`;
CREATE TABLE `platform_data` (
  `uuid` varchar(255) NOT NULL,
  `platform` varchar(255) NOT NULL,
  `loginname` varchar(255) NOT NULL,
  `bet` double NOT NULL,
  `profit` double NOT NULL,
  `starttime` datetime NOT NULL,
  `endtime` datetime NOT NULL,
  `updatetime` datetime NOT NULL,
  PRIMARY KEY (`uuid`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of platform_data
-- ----------------------------

-- ----------------------------
-- Table structure for `points_present`
-- ----------------------------
DROP TABLE IF EXISTS `points_present`;
CREATE TABLE `points_present` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '编号，主键，自动增长',
  `name` varchar(50) DEFAULT NULL COMMENT '商品名称',
  `image_url` varchar(100) DEFAULT NULL COMMENT '商品图片',
  `type` varchar(50) DEFAULT NULL COMMENT '商品类型',
  `property` text COMMENT '商品属性',
  `order` int(5) DEFAULT NULL COMMENT '显示顺序',
  `range` varchar(100) DEFAULT NULL COMMENT '商品区间值',
  `summary` varchar(500) DEFAULT NULL COMMENT '商品简介',
  `start_time` datetime DEFAULT NULL COMMENT '活动开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '活动结束时间',
  `is_delete` char(1) NOT NULL COMMENT '删除标志，Y(已删除)/N(未删除)',
  `is_show` char(1) NOT NULL COMMENT '显示标示，Y(需要显示)/N(不需要显示)',
  `create_user` varchar(50) NOT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_user` varchar(50) NOT NULL COMMENT '修改人',
  `update_time` datetime NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of points_present
-- ----------------------------

-- ----------------------------
-- Table structure for `points_present_record`
-- ----------------------------
DROP TABLE IF EXISTS `points_present_record`;
CREATE TABLE `points_present_record` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '编号，主键，自动增长',
  `points_present_id` int(10) NOT NULL COMMENT '商品编号，对应points_present表的id',
  `property` text NOT NULL COMMENT '商品属性',
  `receiver_name` varchar(50) DEFAULT NULL COMMENT '实物奖品收货人姓名',
  `phone` varchar(50) DEFAULT NULL COMMENT '实物奖品收货人电话',
  `address` varchar(255) DEFAULT NULL COMMENT '实物奖品收货人地址',
  `order_no` varchar(100) DEFAULT NULL COMMENT '实物奖品收货人订单号',
  `status` varchar(20) NOT NULL COMMENT '状态，已兑换/已抽奖/已寄送/已完成',
  `loginname` varchar(50) NOT NULL COMMENT '玩家账号',
  `level` int(2) DEFAULT NULL COMMENT '玩家等级',
  `points` double(24,2) NOT NULL COMMENT '领取商品所耗积分',
  `name` varchar(100) NOT NULL COMMENT '领取商品名称',
  `type` varchar(50) NOT NULL COMMENT '领取商品类型',
  `lucky_draw_present_id` int(10) DEFAULT NULL COMMENT '抽奖编号，对应lucky_draw_present表的id',
  `lucky_draw_present_type` varchar(50) DEFAULT NULL COMMENT '商品类型，对应lucky_draw_present表的points_present_type',
  `remark` varchar(1000) DEFAULT NULL COMMENT '备注',
  `create_user` varchar(50) NOT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_user` varchar(50) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of points_present_record
-- ----------------------------

-- ----------------------------
-- Table structure for `preferential_comment`
-- ----------------------------
DROP TABLE IF EXISTS `preferential_comment`;
CREATE TABLE `preferential_comment` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '编号，主键，自动增长',
  `p_id` int(11) NOT NULL COMMENT '最新优惠ID',
  `login_name` varchar(20) NOT NULL COMMENT '会员账号',
  `nick_name` varchar(20) DEFAULT NULL COMMENT '会员昵称',
  `content` varchar(255) NOT NULL COMMENT '回复内容',
  `reply_time` datetime NOT NULL COMMENT '回复时间',
  PRIMARY KEY (`id`),
  KEY `p_id` (`p_id`),
  KEY `reply_time` (`reply_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of preferential_comment
-- ----------------------------

-- ----------------------------
-- Table structure for `preferential_config`
-- ----------------------------
DROP TABLE IF EXISTS `preferential_config`;
CREATE TABLE `preferential_config` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '编号，主键，自动增长',
  `platform_id` varchar(20) NOT NULL COMMENT '游戏平台编号',
  `platform_name` varchar(100) NOT NULL COMMENT '游戏平台名称',
  `title_id` varchar(10) NOT NULL COMMENT '自助优惠类型编号',
  `title_name` varchar(50) NOT NULL COMMENT '自助优惠类型名称',
  `alias_title` varchar(255) NOT NULL COMMENT '自助优惠名称',
  `percent` double(10,2) DEFAULT NULL COMMENT '优惠百分比',
  `bet_multiples` int(20) DEFAULT NULL COMMENT '流水倍数要求',
  `limit_money` double(20,0) DEFAULT NULL COMMENT '限额',
  `amount` double(10,0) DEFAULT NULL COMMENT '体验金额度',
  `is_used` int(10) NOT NULL COMMENT '是否开启使用，0:关闭 1:开启，默认为1',
  `start_time` datetime NOT NULL COMMENT '启用开始时间',
  `end_time` datetime NOT NULL COMMENT '启用结束时间',
  `times` int(10) DEFAULT NULL COMMENT '优惠使用次数',
  `times_flag` int(10) DEFAULT NULL COMMENT '优惠次数类别，1:天 2:周 3:月 4:年，默认为1',
  `vip` varchar(100) DEFAULT NULL COMMENT '等级',
  `deposit_amount` double(10,2) DEFAULT NULL COMMENT '存款额',
  `deposit_start_time` datetime DEFAULT NULL COMMENT '存款开始时间',
  `deposit_end_time` datetime DEFAULT NULL COMMENT '存款结束时间',
  `bet_amount` double(10,2) DEFAULT NULL COMMENT '输赢值',
  `bet_start_time` datetime DEFAULT NULL COMMENT '输赢开始时间',
  `bet_end_time` datetime DEFAULT NULL COMMENT '输赢结束时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `delete_flag` int(10) DEFAULT NULL COMMENT '删除标志，0:已删除 1:未删除，默认为1',
  `is_phone` varchar(50) DEFAULT NULL COMMENT '申请通道，1:官网 2:WEB 3:安卓APP 4:苹果APP',
  `is_pass_sms` varchar(2) DEFAULT NULL,
  `machine_code_enabled` int(10) DEFAULT NULL COMMENT '是否启用机器码验证，0:否/1:是',
  `machine_code_times` int(10) DEFAULT NULL COMMENT '机器码使用次数',
  `lowest_amount` double(10,2) DEFAULT NULL COMMENT '最低转账金额',
  `group_id` varchar(20) DEFAULT NULL COMMENT '优惠互斥组别',
  `mutex_times` int(5) DEFAULT NULL COMMENT '互斥组别申请次数',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=695 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of preferential_config
-- ----------------------------
INSERT INTO `preferential_config` VALUES ('691', '6001', 'PT存送优惠', '590', '自助PT首存优惠', 'PT首存128%', '1.28', '25', '1888', null, '1', '2018-12-26 00:00:00', '2022-12-24 00:00:00', '1', '4', '0,1,2,3,4,5,6,7,8', '20.00', '2018-12-26 00:00:00', '2022-12-24 00:00:00', null, null, null, '2018-12-24 18:43:12', '2018-12-24 18:43:12', '1', '1,2,3,4', '0', '0', null, '20.00', 'SC', '1');
INSERT INTO `preferential_config` VALUES ('692', '6001', 'PT存送优惠', '590', '自助PT首存优惠', '自助PT首存优惠', '1.28', '25', '1888', null, '1', '2018-12-26 00:00:00', '2022-12-24 00:00:00', '1', '4', '0,1,2,3,4,5,6,7,8', '20.00', '2018-12-26 00:00:00', '2022-12-24 00:00:00', null, null, null, '2018-12-24 18:44:53', '2018-12-24 18:44:53', '1', '1,2,3,4', '0', '0', null, '20.00', 'SC', '1');
INSERT INTO `preferential_config` VALUES ('693', '6001', 'PT存送优惠', '590', '自助PT首存优惠', 'PT首存128%', '1.28', '25', '1888', null, '1', '2018-12-26 00:00:00', '2018-12-26 14:22:32', '1', '4', '0,1,2,3,4,5,6,7,8', null, null, null, null, null, null, '2018-12-26 14:22:46', '2018-12-26 14:22:46', '1', '1,2,3,4', '0', '0', null, null, '', null);
INSERT INTO `preferential_config` VALUES ('694', '6002', 'MG存送优惠', '731', '自助MG次存优惠', '的味道', '0.78', '1', '1', null, '1', '2018-12-29 16:04:16', '2018-12-31 16:04:18', '1', '1', '0,1,2,3,4,5,6,7,8', null, null, null, null, null, null, '2018-12-29 16:04:26', '2018-12-29 16:04:26', '1', '1,2,3,4', '0', '0', null, null, '', null);

-- ----------------------------
-- Table structure for `preferential_record`
-- ----------------------------
DROP TABLE IF EXISTS `preferential_record`;
CREATE TABLE `preferential_record` (
  `pno` varchar(100) NOT NULL,
  `loginname` varchar(255) DEFAULT NULL,
  `platform` varchar(255) DEFAULT NULL COMMENT '游戏平台',
  `validbet` double(20,2) DEFAULT NULL COMMENT '截止目前的投注额',
  `createtime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `type` int(255) DEFAULT '0' COMMENT '1为手动审核通过',
  PRIMARY KEY (`pno`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of preferential_record
-- ----------------------------
INSERT INTO `preferential_record` VALUES ('101200915gscjaroeq3', 'kavin998', 'mg', null, '2020-09-15 01:49:24', '0');
INSERT INTO `preferential_record` VALUES ('101200915xoljnbhsne', 'kavin998', 'mg', null, '2020-09-15 10:42:25', '0');
INSERT INTO `preferential_record` VALUES ('101200915dcvtqokqmg', 'kavin998', 'mg', null, '2020-09-15 11:00:25', '0');
INSERT INTO `preferential_record` VALUES ('10120091524kav4pkhf', 'kavin998', 'dt', '0.00', '2020-09-15 11:45:35', '0');
INSERT INTO `preferential_record` VALUES ('101200920f9evlwhixk', 'austin998', 'cq9', null, '2020-09-20 19:41:13', '0');
INSERT INTO `preferential_record` VALUES ('101200920i0kljulcwy', 'austin998', 'cq9', null, '2020-09-20 19:41:51', '0');
INSERT INTO `preferential_record` VALUES ('1012009207saelfpyf7', 'austin998', 'cq9', null, '2020-09-20 20:04:01', '0');
INSERT INTO `preferential_record` VALUES ('101200920xg7fu452v9', 'austin998', 'cq9', null, '2020-09-20 20:05:18', '0');
INSERT INTO `preferential_record` VALUES ('101200920c4sfxeclru', 'austin998', 'cq9', null, '2020-09-20 20:06:20', '0');
INSERT INTO `preferential_record` VALUES ('101200920fr94t5ffh9', 'austin998', 'cq9', null, '2020-09-20 20:07:52', '0');
INSERT INTO `preferential_record` VALUES ('101200920ull1gbzfrq', 'austin998', 'cq9', null, '2020-09-20 20:08:43', '0');
INSERT INTO `preferential_record` VALUES ('101200920ciqjgw5705', 'austin998', 'cq9', null, '2020-09-20 20:10:12', '0');
INSERT INTO `preferential_record` VALUES ('101200920dh1rxbzswe', 'austin998', 'cq9', null, '2020-09-20 20:10:58', '0');
INSERT INTO `preferential_record` VALUES ('101200927fqno4qg542', 'kavin998', 'cq9', null, '2020-09-27 13:30:38', '0');
INSERT INTO `preferential_record` VALUES ('1012009273q4fpfztpt', 'kavin998', 'cq9', null, '2020-09-27 13:34:43', '0');
INSERT INTO `preferential_record` VALUES ('1012009277rm9gg0223', 'kavin998', 'cq9', null, '2020-09-27 13:37:34', '0');
INSERT INTO `preferential_record` VALUES ('101200927lfmgh6l3qp', 'kavin998', 'pg', null, '2020-09-27 14:00:24', '0');
INSERT INTO `preferential_record` VALUES ('101200927xhqnmqauad', 'kavin998', 'pg', null, '2020-09-27 14:02:34', '0');
INSERT INTO `preferential_record` VALUES ('101200930m10bxunakg', 'kavin998', 'bg', null, '2020-09-30 00:43:37', '0');
INSERT INTO `preferential_record` VALUES ('101200930gf2zbdgh5f', 'kavin998', 'bg', null, '2020-09-30 00:44:04', '0');
INSERT INTO `preferential_record` VALUES ('101200930pmqlltukju', 'kavin998', 'bg', null, '2020-09-30 00:45:15', '0');
INSERT INTO `preferential_record` VALUES ('101200930hixnjrxgj7', 'kavin998', 'bg', null, '2020-09-30 00:49:07', '0');
INSERT INTO `preferential_record` VALUES ('101201005ycwc1xumsz', 'kavin998', 'mg', null, '2020-10-05 00:46:19', '0');
INSERT INTO `preferential_record` VALUES ('101201005q9s57kdsjw', 'austin998', '', null, '2020-10-05 15:12:37', '0');
INSERT INTO `preferential_record` VALUES ('101201005etg63bsddi', 'austin998', 'mg', null, '2020-10-05 15:13:37', '0');
INSERT INTO `preferential_record` VALUES ('101201005r8rslgdarh', 'austin998', 'sba', null, '2020-10-05 15:20:59', '0');
INSERT INTO `preferential_record` VALUES ('101201005b8mgb6ifl3', 'kavin998', 'sba', null, '2020-10-05 15:34:10', '0');
INSERT INTO `preferential_record` VALUES ('1012010050e2sbl3o8k', 'kavin998', 'sba', null, '2020-10-05 15:34:35', '0');
INSERT INTO `preferential_record` VALUES ('101201005bnao064ocp', 'kavin998', 'sba', null, '2020-10-05 15:38:22', '0');
INSERT INTO `preferential_record` VALUES ('1012010058bzzchcjwe', 'kavin998', 'sba', null, '2020-10-05 15:41:41', '0');
INSERT INTO `preferential_record` VALUES ('101201008ukdlqp4vyy', 'austin998', 'dt', '0.00', '2020-10-08 18:41:26', '0');

-- ----------------------------
-- Table structure for `prefer_transfer_record`
-- ----------------------------
DROP TABLE IF EXISTS `prefer_transfer_record`;
CREATE TABLE `prefer_transfer_record` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `loginname` varchar(255) DEFAULT NULL,
  `nowbet` double(20,2) DEFAULT NULL,
  `needbet` double(20,2) DEFAULT NULL,
  `remark` varchar(500) DEFAULT NULL,
  `createtime` datetime DEFAULT NULL,
  `platform` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=11328 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of prefer_transfer_record
-- ----------------------------
INSERT INTO `prefer_transfer_record` VALUES ('11320', 'kavin998', '0.00', '200.00', '红包雨', '2020-09-27 15:04:56', 'PG', 'IN');
INSERT INTO `prefer_transfer_record` VALUES ('11321', 'kavin998', '0.00', '200.00', '红包雨', '2020-09-27 15:05:18', 'PG', 'IN');
INSERT INTO `prefer_transfer_record` VALUES ('11322', 'kavin998', '0.00', '200.00', '红包雨', '2020-09-27 15:09:53', 'PG', 'IN');
INSERT INTO `prefer_transfer_record` VALUES ('11323', 'kavin998', '0.00', '110.00', '红包雨', '2020-09-27 15:10:09', 'CQ9', 'IN');
INSERT INTO `prefer_transfer_record` VALUES ('11324', 'kavin998', '0.00', '200.00', '红包雨', '2020-09-27 15:16:43', 'PG', 'IN');
INSERT INTO `prefer_transfer_record` VALUES ('11325', 'kavin998', '0.00', '110.00', '红包雨', '2020-09-27 15:19:57', 'CQ9', 'IN');
INSERT INTO `prefer_transfer_record` VALUES ('11326', 'kavin998', '0.00', '200.00', '红包雨', '2020-09-27 21:02:23', 'PG', 'IN');
INSERT INTO `prefer_transfer_record` VALUES ('11327', 'kavin998', '0.00', '110.00', '红包雨', '2020-09-27 21:02:47', 'CQ9', 'IN');

-- ----------------------------
-- Table structure for `privilege`
-- ----------------------------
DROP TABLE IF EXISTS `privilege`;
CREATE TABLE `privilege` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `loginname` varchar(16) NOT NULL COMMENT '用户名',
  `createtime` datetime NOT NULL COMMENT '派发时间',
  `amount` decimal(6,2) NOT NULL COMMENT '金额',
  `status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '0: 待派发  1：已派发  2：已取消',
  `distribute_month` varchar(6) NOT NULL COMMENT '派发月份',
  `mindeposit` decimal(10,2) DEFAULT NULL COMMENT '存款要求',
  `minbet` decimal(10,2) DEFAULT NULL COMMENT '流水要求',
  `starttime` datetime DEFAULT NULL COMMENT '开始时间',
  `endtime` datetime DEFAULT NULL COMMENT '结束时间',
  `depositAmount` decimal(10,2) DEFAULT NULL COMMENT '实际存款额',
  `betAmount` decimal(10,2) DEFAULT NULL COMMENT '实际流水额',
  `remark` varchar(32) NOT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uc_privilege` (`loginname`,`distribute_month`,`remark`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of privilege
-- ----------------------------

-- ----------------------------
-- Table structure for `prize`
-- ----------------------------
DROP TABLE IF EXISTS `prize`;
CREATE TABLE `prize` (
  `pno` varchar(20) NOT NULL,
  `title` varchar(40) NOT NULL,
  `loginname` varchar(20) NOT NULL,
  `tryCredit` double(24,2) NOT NULL,
  `remark` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`pno`),
  UNIQUE KEY `pno` (`pno`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of prize
-- ----------------------------

-- ----------------------------
-- Table structure for `proposal`
-- ----------------------------
DROP TABLE IF EXISTS `proposal`;
CREATE TABLE `proposal` (
  `pno` varchar(20) NOT NULL,
  `proposer` varchar(20) NOT NULL,
  `createTime` datetime NOT NULL,
  `type` int(3) NOT NULL,
  `quickly` int(1) NOT NULL DEFAULT '0',
  `loginname` varchar(255) DEFAULT NULL,
  `amount` double(24,2) DEFAULT NULL,
  `agent` varchar(20) DEFAULT '',
  `flag` int(1) NOT NULL DEFAULT '0',
  `whereisfrom` varchar(10) NOT NULL DEFAULT '前台',
  `remark` varchar(500) DEFAULT NULL,
  `generateType` varchar(20) DEFAULT NULL,
  `afterLocalAmount` double(24,2) DEFAULT NULL,
  `afterRemoteAmount` double(24,2) DEFAULT NULL,
  `afterAgRemoteAmount` double(24,2) DEFAULT NULL,
  `afterAgInRemoteAmount` double(24,2) DEFAULT NULL,
  `afterBbinRemoteAmount` double(24,2) DEFAULT NULL,
  `afterKenoRemoteAmount` double(24,2) DEFAULT NULL,
  `afterSbRemoteAmount` double(24,2) DEFAULT NULL,
  `afterSkyRemoteAmount` double(24,2) DEFAULT NULL,
  `bankaccount` varchar(50) DEFAULT NULL,
  `saveway` varchar(50) DEFAULT NULL,
  `bankname` varchar(30) DEFAULT NULL,
  `executeTime` datetime DEFAULT NULL COMMENT '执行时间',
  `timecha` varchar(50) DEFAULT NULL COMMENT '时间差',
  `overtime` int(1) DEFAULT '0' COMMENT '1超时0未超时',
  `msflag` int(3) DEFAULT '0' COMMENT '判断是否用民生银行秒付,默认0不秒付,1表示使用秒付',
  `mstype` int(3) DEFAULT '0' COMMENT '0还没开始秒付,1秒付失败,2秒付成功',
  `passflag` int(2) DEFAULT '0' COMMENT '秒付提案的审核状态,0是待审核1是审核通过-1是未通过',
  `unknowflag` int(1) DEFAULT '1' COMMENT '默认为1还未处理，有处理数据是2，客户验证失败3,未知情况为4',
  `giftamount` double(24,2) DEFAULT NULL,
  `betmultiples` varchar(50) DEFAULT NULL,
  `shippingcode` varchar(50) DEFAULT NULL,
  `shippinginfo` varchar(255) DEFAULT NULL,
  `mssgflag` int(11) DEFAULT '0',
  PRIMARY KEY (`pno`),
  KEY `proposer` (`proposer`) USING BTREE,
  KEY `type` (`type`) USING BTREE,
  KEY `loginname` (`loginname`) USING BTREE,
  KEY `flag` (`flag`) USING BTREE,
  KEY `createTime` (`createTime`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of proposal
-- ----------------------------
INSERT INTO `proposal` VALUES ('10120091524kav4pkhf', 'system', '2020-09-15 11:45:35', '101', '0', 'kavin998', '11.00', '', '2', '前台', null, null, null, null, null, null, null, null, null, null, null, null, null, '2020-09-15 11:45:35', null, '0', '0', '0', '0', '1', '0.00', '0', null, null, '0');
INSERT INTO `proposal` VALUES ('101200915dcvtqokqmg', 'system', '2020-09-15 11:00:25', '101', '0', 'kavin998', '0.00', '', '2', '前台', '红包雨余额转入MG平台,10倍流水，转入11.0', null, null, null, null, null, null, null, null, null, null, null, null, '2020-09-15 11:00:25', null, '0', '0', '0', '0', '1', '11.00', '10', null, null, '0');
INSERT INTO `proposal` VALUES ('101200915gscjaroeq3', 'system', '2020-09-15 01:49:24', '101', '0', 'kavin998', '0.00', '', '2', '前台', '红包雨余额转入MG平台,10倍流水，转入11.0', null, null, null, null, null, null, null, null, null, null, null, null, '2020-09-15 01:49:24', null, '0', '0', '0', '0', '1', '11.00', '10', null, null, '0');
INSERT INTO `proposal` VALUES ('101200915xoljnbhsne', 'system', '2020-09-15 10:42:25', '101', '0', 'kavin998', '0.00', '', '2', '前台', '红包雨余额转入MG平台,10倍流水，转入11.0', null, null, null, null, null, null, null, null, null, null, null, null, '2020-09-15 10:42:25', null, '0', '0', '0', '0', '1', '11.00', '10', null, null, '0');
INSERT INTO `proposal` VALUES ('1012009207saelfpyf7', 'system', '2020-09-20 20:04:01', '101', '0', 'austin998', '0.00', '', '2', '前台', '红包雨余额转入CQ9平台,10倍流水，转入13.0', null, null, null, null, null, null, null, null, null, null, null, null, '2020-09-20 20:04:01', null, '0', '0', '0', '0', '1', '13.00', '10', null, null, '0');
INSERT INTO `proposal` VALUES ('101200920c4sfxeclru', 'system', '2020-09-20 20:06:20', '101', '0', 'austin998', '0.00', '', '2', '前台', '红包雨余额转入CQ9平台,10倍流水，转入13.0', null, null, null, null, null, null, null, null, null, null, null, null, '2020-09-20 20:06:20', null, '0', '0', '0', '0', '1', '13.00', '10', null, null, '0');
INSERT INTO `proposal` VALUES ('101200920ciqjgw5705', 'system', '2020-09-20 20:10:12', '101', '0', 'austin998', '0.00', '', '2', '前台', '红包雨余额转入CQ9平台,10倍流水，转入11.0', null, null, null, null, null, null, null, null, null, null, null, null, '2020-09-20 20:10:12', null, '0', '0', '0', '0', '1', '11.00', '10', null, null, '0');
INSERT INTO `proposal` VALUES ('101200920dh1rxbzswe', 'system', '2020-09-20 20:10:58', '101', '0', 'austin998', '0.00', '', '2', '前台', '红包雨余额转入CQ9平台,10倍流水，转入22.0', null, null, null, null, null, null, null, null, null, null, null, null, '2020-09-20 20:10:58', null, '0', '0', '0', '0', '1', '22.00', '10', null, null, '0');
INSERT INTO `proposal` VALUES ('101200920f9evlwhixk', 'system', '2020-09-20 19:41:13', '101', '0', 'austin998', '0.00', '', '2', '前台', '红包雨余额转入CQ9平台,10倍流水，转入11.0', null, null, null, null, null, null, null, null, null, null, null, null, '2020-09-20 19:41:13', null, '0', '0', '0', '0', '1', '11.00', '10', null, null, '0');
INSERT INTO `proposal` VALUES ('101200920fr94t5ffh9', 'system', '2020-09-20 20:07:52', '101', '0', 'austin998', '0.00', '', '2', '前台', '红包雨余额转入CQ9平台,10倍流水，转入11.0', null, null, null, null, null, null, null, null, null, null, null, null, '2020-09-20 20:07:52', null, '0', '0', '0', '0', '1', '11.00', '10', null, null, '0');
INSERT INTO `proposal` VALUES ('101200920i0kljulcwy', 'system', '2020-09-20 19:41:51', '101', '0', 'austin998', '0.00', '', '2', '前台', '红包雨余额转入CQ9平台,10倍流水，转入11.0', null, null, null, null, null, null, null, null, null, null, null, null, '2020-09-20 19:41:51', null, '0', '0', '0', '0', '1', '11.00', '10', null, null, '0');
INSERT INTO `proposal` VALUES ('101200920ull1gbzfrq', 'system', '2020-09-20 20:08:43', '101', '0', 'austin998', '0.00', '', '2', '前台', '红包雨余额转入CQ9平台,10倍流水，转入11.0', null, null, null, null, null, null, null, null, null, null, null, null, '2020-09-20 20:08:43', null, '0', '0', '0', '0', '1', '11.00', '10', null, null, '0');
INSERT INTO `proposal` VALUES ('101200920xg7fu452v9', 'system', '2020-09-20 20:05:18', '101', '0', 'austin998', '0.00', '', '2', '前台', '红包雨余额转入CQ9平台,10倍流水，转入12.0', null, null, null, null, null, null, null, null, null, null, null, null, '2020-09-20 20:05:18', null, '0', '0', '0', '0', '1', '12.00', '10', null, null, '0');
INSERT INTO `proposal` VALUES ('1012009273q4fpfztpt', 'system', '2020-09-27 13:34:43', '101', '0', 'kavin998', '0.00', '', '2', '前台', '红包雨余额转入CQ9平台,10倍流水，转入12.0', null, null, null, null, null, null, null, null, null, null, null, null, '2020-09-27 13:34:43', null, '0', '0', '0', '0', '1', '12.00', '10', null, null, '0');
INSERT INTO `proposal` VALUES ('1012009277rm9gg0223', 'system', '2020-09-27 13:37:34', '101', '0', 'kavin998', '0.00', '', '2', '前台', '红包雨余额转入CQ9平台,10倍流水，转入11.0', null, null, null, null, null, null, null, null, null, null, null, null, '2020-09-27 13:37:34', null, '0', '0', '0', '0', '1', '11.00', '10', null, null, '0');
INSERT INTO `proposal` VALUES ('101200927fqno4qg542', 'system', '2020-09-27 13:30:38', '101', '0', 'kavin998', '0.00', '', '2', '前台', '红包雨余额转入CQ9平台,10倍流水，转入12.0', null, null, null, null, null, null, null, null, null, null, null, null, '2020-09-27 13:30:38', null, '0', '0', '0', '0', '1', '12.00', '10', null, null, '0');
INSERT INTO `proposal` VALUES ('101200927lfmgh6l3qp', 'system', '2020-09-27 14:00:24', '101', '0', 'kavin998', '0.00', '', '2', '前台', '红包雨余额转入PG平台,10倍流水，转入11.0', null, null, null, null, null, null, null, null, null, null, null, null, '2020-09-27 14:00:24', null, '0', '0', '0', '0', '1', '11.00', '10', null, null, '0');
INSERT INTO `proposal` VALUES ('101200927xhqnmqauad', 'system', '2020-09-27 14:02:34', '101', '0', 'kavin998', '0.00', '', '2', '前台', '红包雨余额转入PG平台,10倍流水，转入20.0', null, null, null, null, null, null, null, null, null, null, null, null, '2020-09-27 14:02:34', null, '0', '0', '0', '0', '1', '20.00', '10', null, null, '0');
INSERT INTO `proposal` VALUES ('101200930gf2zbdgh5f', 'system', '2020-09-30 00:44:04', '101', '0', 'kavin998', '0.00', '', '2', '前台', '红包雨余额转入BG平台,10倍流水，转入12.0', null, null, null, null, null, null, null, null, null, null, null, null, '2020-09-30 00:44:04', null, '0', '0', '0', '0', '1', '12.00', '10', null, null, '0');
INSERT INTO `proposal` VALUES ('101200930hixnjrxgj7', 'system', '2020-09-30 00:49:07', '101', '0', 'kavin998', '0.00', '', '2', '前台', '红包雨余额转入BG平台,10倍流水，转入12.0', null, null, null, null, null, null, null, null, null, null, null, null, '2020-09-30 00:49:07', null, '0', '0', '0', '0', '1', '12.00', '10', null, null, '0');
INSERT INTO `proposal` VALUES ('101200930m10bxunakg', 'system', '2020-09-30 00:43:37', '101', '0', 'kavin998', '0.00', '', '2', '前台', '红包雨余额转入BG平台,10倍流水，转入12.0', null, null, null, null, null, null, null, null, null, null, null, null, '2020-09-30 00:43:37', null, '0', '0', '0', '0', '1', '12.00', '10', null, null, '0');
INSERT INTO `proposal` VALUES ('101200930pmqlltukju', 'system', '2020-09-30 00:45:15', '101', '0', 'kavin998', '0.00', '', '2', '前台', '红包雨余额转入BG平台,10倍流水，转入12.0', null, null, null, null, null, null, null, null, null, null, null, null, '2020-09-30 00:45:15', null, '0', '0', '0', '0', '1', '12.00', '10', null, null, '0');
INSERT INTO `proposal` VALUES ('1012010050e2sbl3o8k', 'system', '2020-10-05 15:34:35', '101', '0', 'kavin998', '0.00', '', '2', '前台', '红包雨余额转入SBA平台,10倍流水，转入11.0', null, null, null, null, null, null, null, null, null, null, null, null, '2020-10-05 15:34:35', null, '0', '0', '0', '0', '1', '11.00', '10', null, null, '0');
INSERT INTO `proposal` VALUES ('1012010058bzzchcjwe', 'system', '2020-10-05 15:41:41', '101', '0', 'kavin998', '0.00', '', '2', '前台', '红包雨余额转入SBA平台,10倍流水，转入150.0', null, null, null, null, null, null, null, null, null, null, null, null, '2020-10-05 15:41:41', null, '0', '0', '0', '0', '1', '150.00', '10', null, null, '0');
INSERT INTO `proposal` VALUES ('101201005b8mgb6ifl3', 'system', '2020-10-05 15:34:10', '101', '0', 'kavin998', '0.00', '', '2', '前台', '红包雨余额转入SBA平台,10倍流水，转入11.0', null, null, null, null, null, null, null, null, null, null, null, null, '2020-10-05 15:34:10', null, '0', '0', '0', '0', '1', '11.00', '10', null, null, '0');
INSERT INTO `proposal` VALUES ('101201005bnao064ocp', 'system', '2020-10-05 15:38:22', '101', '0', 'kavin998', '0.00', '', '2', '前台', '红包雨余额转入SBA平台,10倍流水，转入11.0', null, null, null, null, null, null, null, null, null, null, null, null, '2020-10-05 15:38:22', null, '0', '0', '0', '0', '1', '11.00', '10', null, null, '0');
INSERT INTO `proposal` VALUES ('101201005etg63bsddi', 'system', '2020-10-05 15:13:37', '101', '0', 'austin998', '0.00', '', '2', '前台', '红包雨余额转入MG平台,10倍流水，转入10.0', null, null, null, null, null, null, null, null, null, null, null, null, '2020-10-05 15:13:37', null, '0', '0', '0', '0', '1', '10.00', '10', null, null, '0');
INSERT INTO `proposal` VALUES ('101201005q9s57kdsjw', 'system', '2020-10-05 15:12:37', '101', '0', 'austin998', '0.00', '', '2', '前台', '红包雨余额转入SBA平台,10倍流水，转入10.0', null, null, null, null, null, null, null, null, null, null, null, null, '2020-10-05 15:12:37', null, '0', '0', '0', '0', '1', '10.00', '10', null, null, '0');
INSERT INTO `proposal` VALUES ('101201005r8rslgdarh', 'system', '2020-10-05 15:20:59', '101', '0', 'austin998', '0.00', '', '2', '前台', '红包雨余额转入SBA平台,10倍流水，转入11.0', null, null, null, null, null, null, null, null, null, null, null, null, '2020-10-05 15:20:59', null, '0', '0', '0', '0', '1', '11.00', '10', null, null, '0');
INSERT INTO `proposal` VALUES ('101201005ycwc1xumsz', 'system', '2020-10-05 00:46:19', '101', '0', 'kavin998', '0.00', '', '2', '前台', '红包雨余额转入MG平台,10倍流水，转入11.0', null, null, null, null, null, null, null, null, null, null, null, null, '2020-10-05 00:46:19', null, '0', '0', '0', '0', '1', '11.00', '10', null, null, '0');
INSERT INTO `proposal` VALUES ('101201008ukdlqp4vyy', 'system', '2020-10-08 18:41:26', '101', '0', 'austin998', '11.00', '', '2', '前台', null, null, null, null, null, null, null, null, null, null, null, null, null, '2020-10-08 18:41:26', null, '0', '0', '0', '0', '1', '0.00', '0', null, null, '0');
INSERT INTO `proposal` VALUES ('629200917rrkqhuep4o', 'kavin998', '2020-09-17 20:39:12', '629', '0', 'kavin998', '4.10', '', '2', '前台', 'mg自助洗码;秒反水;executed;自动审核', 'customer', null, null, null, null, null, null, null, null, null, null, null, '2020-09-17 20:39:12', null, '0', '0', '0', '1', '1', null, null, null, null, '0');
INSERT INTO `proposal` VALUES ('633200921lufizym2gq', 'austin998', '2020-09-21 21:06:53', '633', '0', 'austin998', '6.12', '', '2', '前台', 'cq9自助洗码;秒反水;executed;自动审核', 'customer', null, null, null, null, null, null, null, null, null, null, null, '2020-09-21 21:06:53', null, '0', '0', '0', '1', '1', null, null, null, null, '0');

-- ----------------------------
-- Table structure for `proposal_extend`
-- ----------------------------
DROP TABLE IF EXISTS `proposal_extend`;
CREATE TABLE `proposal_extend` (
  `pno` varchar(20) NOT NULL COMMENT '提案号',
  `platform` varchar(20) NOT NULL COMMENT '存送平台编号',
  `preferential_id` int(20) NOT NULL COMMENT '自助优惠编号',
  `sid` varchar(255) DEFAULT NULL COMMENT '手机设备号',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`pno`),
  KEY `INDEX_1` (`platform`,`preferential_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of proposal_extend
-- ----------------------------

-- ----------------------------
-- Table structure for `ptbigbang`
-- ----------------------------
DROP TABLE IF EXISTS `ptbigbang`;
CREATE TABLE `ptbigbang` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(20) NOT NULL COMMENT '用户名',
  `netwin_lose` decimal(12,2) NOT NULL COMMENT '纯赢/纯输',
  `startTime` datetime NOT NULL COMMENT '开始时间',
  `endTime` datetime NOT NULL COMMENT '结束时间',
  `createTime` datetime NOT NULL COMMENT '创建时间',
  `bonus` decimal(10,2) DEFAULT NULL COMMENT '红利',
  `status` char(1) NOT NULL DEFAULT '0' COMMENT '状态（0：未派发  1：已派发 2: 已领取 3：已处理）',
  `giftmoney` decimal(8,2) DEFAULT NULL COMMENT '礼金',
  `times` tinyint(4) DEFAULT NULL COMMENT '流水倍数要求',
  `distributeTime` datetime DEFAULT NULL COMMENT '派发时间',
  `getTime` datetime DEFAULT NULL COMMENT '领取时间',
  `betAmount` decimal(12,2) DEFAULT NULL COMMENT '领取时的投注额',
  `distributeDay` varchar(10) NOT NULL COMMENT ' 派发日期（和用户名作为一约束用）',
  `remark` varchar(60) DEFAULT NULL COMMENT '备注',
  `platform` varchar(32) DEFAULT NULL COMMENT '游戏平台',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uc_ptbigbang` (`username`,`distributeDay`) USING BTREE
) ENGINE=MyISAM AUTO_INCREMENT=75929 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ptbigbang
-- ----------------------------

-- ----------------------------
-- Table structure for `ptcommissions`
-- ----------------------------
DROP TABLE IF EXISTS `ptcommissions`;
CREATE TABLE `ptcommissions` (
  `agent` varchar(30) NOT NULL,
  `createdate` datetime NOT NULL COMMENT '佣金数据的时间',
  `platform` varchar(20) NOT NULL,
  `amount` double(10,2) DEFAULT NULL COMMENT '金额',
  `percent` double(20,2) DEFAULT NULL COMMENT '佣金比例',
  `subCount` int(10) DEFAULT NULL COMMENT '下线会员数',
  `activeuser` int(5) DEFAULT NULL COMMENT '活跃会员数',
  `flag` int(2) DEFAULT NULL,
  `createTime` datetime DEFAULT NULL COMMENT '创建时间',
  `excuteTime` datetime DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  `betall` double(255,2) NOT NULL COMMENT '投注总额',
  `profitall` double(255,2) NOT NULL COMMENT '输赢总额',
  `platformfee` double(255,2) NOT NULL COMMENT '平台费',
  `couponfee` double(255,2) NOT NULL COMMENT '优惠金额',
  `ximafee` double(255,2) NOT NULL COMMENT '反水金额',
  `historyfee` double(255,2) NOT NULL COMMENT '历史总佣金（不包括本次佣金）',
  `progressive_bets` double(255,2) NOT NULL COMMENT 'pt奖池',
  PRIMARY KEY (`agent`,`createdate`,`platform`),
  KEY `ind_agent` (`agent`) USING BTREE,
  KEY `ind_createdate` (`createdate`) USING BTREE,
  KEY `ind_platform` (`platform`) USING BTREE
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ptcommissions
-- ----------------------------

-- ----------------------------
-- Table structure for `ptcommissions_record`
-- ----------------------------
DROP TABLE IF EXISTS `ptcommissions_record`;
CREATE TABLE `ptcommissions_record` (
  `agent` varchar(30) NOT NULL,
  `createdate` datetime NOT NULL COMMENT '佣金数据的时间',
  `platform` varchar(20) NOT NULL,
  `remark` varchar(255) DEFAULT NULL,
  `couponfee` double(255,2) NOT NULL COMMENT '优惠金额',
  `ximafee` double(255,2) NOT NULL COMMENT '反水金额',
  `createTime` datetime DEFAULT NULL COMMENT '创建时间',
  `betall` double(255,2) NOT NULL COMMENT '投注总额',
  `profitall` double(255,2) NOT NULL COMMENT '输赢总额',
  PRIMARY KEY (`agent`,`createdate`,`platform`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ptcommissions_record
-- ----------------------------

-- ----------------------------
-- Table structure for `ptprofit`
-- ----------------------------
DROP TABLE IF EXISTS `ptprofit`;
CREATE TABLE `ptprofit` (
  `uuid` varchar(36) NOT NULL,
  `userId` int(11) DEFAULT NULL,
  `amount` double(11,2) DEFAULT NULL COMMENT '总输赢=总投注额-赔付',
  `betCredit` double(11,2) DEFAULT NULL COMMENT '总投注额',
  `starttime` datetime NOT NULL,
  `endtime` datetime NOT NULL,
  `loginname` varchar(50) DEFAULT NULL,
  `payOut` double(11,2) DEFAULT NULL COMMENT '赔付',
  PRIMARY KEY (`uuid`),
  UNIQUE KEY `lse` (`starttime`,`endtime`,`loginname`,`userId`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ptprofit
-- ----------------------------

-- ----------------------------
-- Table structure for `pt_coupon`
-- ----------------------------
DROP TABLE IF EXISTS `pt_coupon`;
CREATE TABLE `pt_coupon` (
  `id` int(32) NOT NULL AUTO_INCREMENT,
  `code` varchar(20) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL COMMENT '是否使用 1.已经使用 2.未使用',
  `type` char(2) DEFAULT NULL,
  `html` text,
  `createtime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `updatetime` datetime DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `operator` varchar(255) DEFAULT NULL COMMENT '操作人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of pt_coupon
-- ----------------------------

-- ----------------------------
-- Table structure for `pt_data`
-- ----------------------------
DROP TABLE IF EXISTS `pt_data`;
CREATE TABLE `pt_data` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `PLAYERNAME` varchar(255) DEFAULT NULL COMMENT '用户名',
  `CODE` varchar(255) DEFAULT NULL COMMENT '代码',
  `CURRENCYCODE` varchar(255) DEFAULT NULL COMMENT '货币',
  `ACTIVEPLAYERS` varchar(255) DEFAULT NULL COMMENT '玩家数量',
  `BALANCECHANGE` varchar(255) DEFAULT NULL COMMENT '余额',
  `DEPOSITS` varchar(255) DEFAULT NULL COMMENT '存款',
  `WITHDRAWS` varchar(255) DEFAULT NULL COMMENT '提款',
  `BONUSES` varchar(255) DEFAULT NULL COMMENT '奖金',
  `COMPS` varchar(255) DEFAULT NULL COMMENT '积分',
  `PROGRESSIVEBETS` varchar(255) DEFAULT NULL COMMENT '奖池投注',
  `PROGRESSIVEWINS` varchar(255) DEFAULT NULL COMMENT '奖池赢得',
  `BETS` double(24,3) DEFAULT NULL COMMENT '投注额',
  `WINS` varchar(255) DEFAULT NULL COMMENT '赢得',
  `NETLOSS` double(24,3) DEFAULT NULL COMMENT '净赚',
  `NETPURCHASE` varchar(255) DEFAULT NULL COMMENT '存入金额',
  `NETGAMING` varchar(255) DEFAULT NULL COMMENT '使用金额',
  `HOUSEEARNINGS` varchar(255) DEFAULT NULL COMMENT '净赚',
  `RNUM` varchar(255) DEFAULT NULL COMMENT '无意义',
  `CREATTIME` datetime DEFAULT NULL,
  `STARTTIME` datetime DEFAULT NULL COMMENT '开始时间',
  `ENDTIME` datetime DEFAULT NULL COMMENT '结束时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of pt_data
-- ----------------------------

-- ----------------------------
-- Table structure for `pt_data_new`
-- ----------------------------
DROP TABLE IF EXISTS `pt_data_new`;
CREATE TABLE `pt_data_new` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `PLAYERNAME` varchar(50) DEFAULT NULL,
  `FULLNAME` varchar(50) DEFAULT NULL,
  `VIPLEVEL` varchar(2) DEFAULT NULL,
  `COUNTRY` varchar(10) DEFAULT NULL,
  `GAMES` varchar(10) DEFAULT NULL,
  `CURRENCYCODE` varchar(10) DEFAULT NULL,
  `BETS` double(24,2) DEFAULT NULL,
  `WINS` double(50,2) DEFAULT NULL,
  `INCOME` varchar(50) DEFAULT NULL,
  `RNUM` int(20) DEFAULT NULL,
  `CREATETIME` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `STARTTIME` datetime DEFAULT NULL,
  `ENDTIME` datetime DEFAULT NULL,
  `BETS_TIGER` double(20,2) DEFAULT '0.00',
  `WINS_TIGER` double(20,2) DEFAULT '0.00',
  `PROGRESSIVE_BETS` double(20,2) DEFAULT '0.00' COMMENT '累计奖池老虎机投注额',
  `PROGRESSIVE_WINS` double(20,2) DEFAULT '0.00' COMMENT '累计奖池老虎机输赢值',
  `PROGRESSIVE_FEE` double(20,2) DEFAULT NULL COMMENT '奖池额度',
  PRIMARY KEY (`id`),
  KEY `PLAYERNAME` (`PLAYERNAME`),
  KEY `STARTTIME` (`STARTTIME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of pt_data_new
-- ----------------------------

-- ----------------------------
-- Table structure for `pt_data_new_85`
-- ----------------------------
DROP TABLE IF EXISTS `pt_data_new_85`;
CREATE TABLE `pt_data_new_85` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `PLAYERNAME` varchar(50) DEFAULT NULL,
  `FULLNAME` varchar(50) DEFAULT NULL,
  `VIPLEVEL` varchar(2) DEFAULT NULL,
  `COUNTRY` varchar(10) DEFAULT NULL,
  `GAMES` varchar(10) DEFAULT NULL,
  `CURRENCYCODE` varchar(10) DEFAULT NULL,
  `BETS` double(24,2) DEFAULT NULL,
  `WINS` double(50,2) DEFAULT NULL,
  `INCOME` varchar(50) DEFAULT NULL,
  `RNUM` int(20) DEFAULT NULL,
  `CREATETIME` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `STARTTIME` datetime DEFAULT NULL,
  `ENDTIME` datetime DEFAULT NULL,
  `BETS_TIGER` double(20,2) DEFAULT '0.00',
  `WINS_TIGER` double(20,2) DEFAULT '0.00',
  `PROGRESSIVE_BETS` double(20,2) DEFAULT '0.00' COMMENT '累计奖池老虎机投注额',
  `PROGRESSIVE_WINS` double(20,2) DEFAULT '0.00' COMMENT '累计奖池老虎机输赢值',
  PRIMARY KEY (`id`),
  KEY `PLAYERNAME` (`PLAYERNAME`) USING BTREE,
  KEY `CREATETIME` (`CREATETIME`) USING BTREE,
  KEY `STARTTIME` (`STARTTIME`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of pt_data_new_85
-- ----------------------------

-- ----------------------------
-- Table structure for `pt_data_new_commit`
-- ----------------------------
DROP TABLE IF EXISTS `pt_data_new_commit`;
CREATE TABLE `pt_data_new_commit` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `PLAYERNAME` varchar(50) DEFAULT NULL,
  `FULLNAME` varchar(50) DEFAULT NULL,
  `VIPLEVEL` varchar(2) DEFAULT NULL,
  `COUNTRY` varchar(10) DEFAULT NULL,
  `GAMES` varchar(10) DEFAULT NULL,
  `CURRENCYCODE` varchar(10) DEFAULT NULL,
  `BETS` double(24,2) DEFAULT NULL,
  `WINS` double(50,2) DEFAULT NULL,
  `INCOME` varchar(50) DEFAULT NULL,
  `RNUM` int(20) DEFAULT NULL,
  `CREATETIME` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `STARTTIME` datetime DEFAULT NULL,
  `ENDTIME` datetime DEFAULT NULL,
  `BETS_TIGER` double(20,2) DEFAULT '0.00',
  `WINS_TIGER` double(20,2) DEFAULT '0.00',
  `PROGRESSIVE_WINS` double(20,2) DEFAULT NULL,
  `PROGRESSIVE_BETS` double(20,2) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=4777625 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of pt_data_new_commit
-- ----------------------------

-- ----------------------------
-- Table structure for `pt_export_e68`
-- ----------------------------
DROP TABLE IF EXISTS `pt_export_e68`;
CREATE TABLE `pt_export_e68` (
  `username` varchar(255) NOT NULL,
  `balance` double(20,2) DEFAULT NULL,
  `realbalance` double(20,2) DEFAULT NULL,
  `createtime` datetime DEFAULT NULL,
  `updatetime` datetime DEFAULT NULL,
  `isdelete` int(5) DEFAULT '0',
  `filename` varchar(255) DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  UNIQUE KEY `username` (`username`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of pt_export_e68
-- ----------------------------

-- ----------------------------
-- Table structure for `pt_transfer2new_history`
-- ----------------------------
DROP TABLE IF EXISTS `pt_transfer2new_history`;
CREATE TABLE `pt_transfer2new_history` (
  `username` varchar(32) NOT NULL,
  `money` decimal(8,2) DEFAULT NULL,
  `createTime` datetime NOT NULL,
  `remark` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of pt_transfer2new_history
-- ----------------------------

-- ----------------------------
-- Table structure for `pt_userse68`
-- ----------------------------
DROP TABLE IF EXISTS `pt_userse68`;
CREATE TABLE `pt_userse68` (
  `id` int(11) NOT NULL DEFAULT '0',
  `loginname` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `remark` varchar(2888) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of pt_userse68
-- ----------------------------

-- ----------------------------
-- Table structure for `qrcode`
-- ----------------------------
DROP TABLE IF EXISTS `qrcode`;
CREATE TABLE `qrcode` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `agent` varchar(100) DEFAULT NULL COMMENT '代理账号',
  `recommendCode` varchar(100) DEFAULT NULL COMMENT '推荐码',
  `address` varchar(300) DEFAULT NULL COMMENT '访问地址',
  `type` int(3) DEFAULT NULL COMMENT '类型(保留字段暂时没用)',
  `qrcode` varchar(300) DEFAULT NULL COMMENT '微信号',
  `remark` varchar(300) DEFAULT NULL COMMENT '备注',
  `updateoperator` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of qrcode
-- ----------------------------

-- ----------------------------
-- Table structure for `question`
-- ----------------------------
DROP TABLE IF EXISTS `question`;
CREATE TABLE `question` (
  `id` int(15) NOT NULL AUTO_INCREMENT,
  `loginname` varchar(50) NOT NULL,
  `questionid` int(10) NOT NULL,
  `content` varchar(255) NOT NULL,
  `delflag` int(5) NOT NULL DEFAULT '0' COMMENT '0.不删除1.删除',
  `remark` varchar(255) DEFAULT NULL,
  `createtime` datetime NOT NULL,
  `updatetime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `loginname` (`loginname`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of question
-- ----------------------------
INSERT INTO `question` VALUES ('20', 'twtest01', '7', 'e10adc3949ba59abbe56e057f20f883e', '0', null, '2018-12-23 07:52:45', null);

-- ----------------------------
-- Table structure for `question_status`
-- ----------------------------
DROP TABLE IF EXISTS `question_status`;
CREATE TABLE `question_status` (
  `loginname` varchar(50) NOT NULL,
  `errortimes` int(5) NOT NULL DEFAULT '0',
  `createtime` datetime NOT NULL,
  `updatetime` datetime DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`loginname`),
  KEY `loginname` (`loginname`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of question_status
-- ----------------------------

-- ----------------------------
-- Table structure for `quotalrevision`
-- ----------------------------
DROP TABLE IF EXISTS `quotalrevision`;
CREATE TABLE `quotalrevision` (
  `billno` int(11) NOT NULL AUTO_INCREMENT,
  `loginname` varchar(255) DEFAULT NULL,
  `createtime` datetime DEFAULT NULL,
  `type` varchar(30) DEFAULT NULL,
  `credit` double(24,2) DEFAULT '0.00',
  `newCredit` double(24,2) DEFAULT '0.00',
  `remit` double(24,2) DEFAULT '0.00',
  `remark` varchar(255) DEFAULT NULL,
  `examine` int(11) DEFAULT NULL,
  `examineRemark` varchar(200) DEFAULT NULL,
  `examinetime` datetime DEFAULT NULL,
  `ip` varchar(30) DEFAULT NULL,
  `operator` varchar(30) DEFAULT NULL,
  `examineIp` varchar(30) DEFAULT NULL,
  `examineOperator` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`billno`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of quotalrevision
-- ----------------------------
INSERT INTO `quotalrevision` VALUES ('1', 'twtest01', '2018-12-16 15:03:54', 'CHANGE_QUOTAL', '0.00', '500.00', '500.00', '', '1', '', '2018-12-16 15:04:15', '45.32.9.147', 'wind', '45.32.9.147', 'wind');
INSERT INTO `quotalrevision` VALUES ('2', 'kavin998', '2020-09-05 22:02:47', 'CHANGE_QUOTAL', '0.00', '10000.00', '10000.00', '手工存款', '1', '手工存款kavin', '2020-09-05 22:05:00', '0:0:0:0:0:0:0:1', 'admin', '0:0:0:0:0:0:0:1', 'admin');
INSERT INTO `quotalrevision` VALUES ('3', 'austin998', '2020-09-20 14:58:46', 'CHANGE_QUOTALSLOT', '0.00', '10000.00', '10000.00', 'austin998老虎机佣金增加/扣除10000.00', '1', '代加10000元，austin998', '2020-09-20 14:59:36', '0:0:0:0:0:0:0:1', 'admin', '0:0:0:0:0:0:0:1', 'admin');
INSERT INTO `quotalrevision` VALUES ('4', 'austin998', '2020-09-20 15:06:50', 'CHANGE_QUOTALSLOT', '0.00', '9999.00', '9999.00', 'austin998老虎机佣金增加/扣除9999.00', '1', '反对反对', '2020-09-20 15:07:21', '0:0:0:0:0:0:0:1', 'admin', '0:0:0:0:0:0:0:1', 'admin');
INSERT INTO `quotalrevision` VALUES ('5', 'kavin998', '2020-09-20 15:12:15', 'CHANGE_QUOTAL', '8333.10', '9444.10', '1111.00', '', '1', '热热热', '2020-09-20 15:12:25', '0:0:0:0:0:0:0:1', 'admin', '0:0:0:0:0:0:0:1', 'admin');
INSERT INTO `quotalrevision` VALUES ('6', 'austin998', '2020-09-20 15:13:21', 'CHANGE_QUOTAL', '0.00', '10000.00', '10000.00', '', '1', '同仁堂率 ', '2020-09-20 15:13:29', '0:0:0:0:0:0:0:1', 'admin', '0:0:0:0:0:0:0:1', 'admin');

-- ----------------------------
-- Table structure for `raincoupon_record`
-- ----------------------------
DROP TABLE IF EXISTS `raincoupon_record`;
CREATE TABLE `raincoupon_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `loginname` varchar(50) NOT NULL,
  `typeid` int(11) NOT NULL COMMENT '红包雨配置表id',
  `amount` double(10,2) DEFAULT NULL COMMENT '金额',
  `times` int(10) DEFAULT NULL COMMENT '流水倍数',
  `coupon` varchar(255) DEFAULT NULL COMMENT '红包优惠卷',
  `remark` varchar(255) DEFAULT NULL,
  `createtime` datetime DEFAULT NULL,
  `isused` int(10) DEFAULT '0' COMMENT '0.未使用1.已使用',
  `pno` varchar(255) DEFAULT NULL COMMENT '提案码',
  `updatetime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `loginname` (`loginname`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of raincoupon_record
-- ----------------------------
INSERT INTO `raincoupon_record` VALUES ('1', 'kavin998', '0', '11.00', null, null, '主账户转入红包雨账户 0.0->11.0', '2020-09-15 01:03:56', '10', null, null);
INSERT INTO `raincoupon_record` VALUES ('2', 'kavin998', '0', '11.00', null, null, '主账户转入红包雨账户 11.0->22.0', '2020-09-15 01:04:25', '10', null, null);
INSERT INTO `raincoupon_record` VALUES ('3', 'kavin998', '0', '100.00', '0', null, '主账户转入红包雨账户 11.0->111.0', '2020-09-15 10:42:11', '10', null, null);
INSERT INTO `raincoupon_record` VALUES ('4', 'austin998', '0', '150.00', null, null, '主账户转入红包雨账户 10.0->160.0', '2020-09-20 18:43:18', '10', null, null);
INSERT INTO `raincoupon_record` VALUES ('5', 'austin998', '0', '112.00', null, null, '主账户转入红包雨账户 67.0->179.0', '2020-09-20 20:10:44', '10', null, null);
INSERT INTO `raincoupon_record` VALUES ('6', 'kavin998', '0', '900.00', null, null, '主账户转入红包雨账户 12.0->912.0', '2020-09-30 00:43:20', '10', null, null);
INSERT INTO `raincoupon_record` VALUES ('7', 'austin998', '0', '11.00', '0', null, '主账户转入红包雨账户 126.0->137.0', '2020-10-08 18:41:19', '10', null, null);

-- ----------------------------
-- Table structure for `rebankinfo`
-- ----------------------------
DROP TABLE IF EXISTS `rebankinfo`;
CREATE TABLE `rebankinfo` (
  `pno` varchar(20) NOT NULL,
  `title` varchar(20) NOT NULL,
  `loginname` varchar(20) NOT NULL,
  `accountName` varchar(50) NOT NULL,
  `accountNo` varchar(32) NOT NULL,
  `bank` varchar(50) NOT NULL,
  `accountType` varchar(20) NOT NULL,
  `accountCity` varchar(50) NOT NULL,
  `bankAddress` varchar(100) NOT NULL,
  `ip` varchar(16) NOT NULL,
  `remark` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`pno`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of rebankinfo
-- ----------------------------

-- ----------------------------
-- Table structure for `recaptcha_config`
-- ----------------------------
DROP TABLE IF EXISTS `recaptcha_config`;
CREATE TABLE `recaptcha_config` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `domain` varchar(128) DEFAULT NULL COMMENT '域名',
  `site_key` varchar(128) DEFAULT NULL COMMENT '公钥',
  `secret_key` varchar(128) DEFAULT NULL COMMENT '私钥',
  `status` varchar(1) DEFAULT NULL COMMENT '状态（0禁用1启用）',
  `createtime` datetime DEFAULT NULL COMMENT '创建时间',
  `remark` varchar(256) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of recaptcha_config
-- ----------------------------

-- ----------------------------
-- Table structure for `red_rain_wallet`
-- ----------------------------
DROP TABLE IF EXISTS `red_rain_wallet`;
CREATE TABLE `red_rain_wallet` (
  `loginname` varchar(20) NOT NULL,
  `amout` double DEFAULT '0',
  `remark` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`loginname`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of red_rain_wallet
-- ----------------------------
INSERT INTO `red_rain_wallet` VALUES ('austin998', '126', '红包雨余额转入QT 137.0->126.0');
INSERT INTO `red_rain_wallet` VALUES ('kavin998', '670', '红包雨余额转入SBA820.0->670.0');

-- ----------------------------
-- Table structure for `remoteurls`
-- ----------------------------
DROP TABLE IF EXISTS `remoteurls`;
CREATE TABLE `remoteurls` (
  `url` varchar(100) NOT NULL,
  `flag` int(1) NOT NULL,
  `updatetime` datetime NOT NULL,
  PRIMARY KEY (`url`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of remoteurls
-- ----------------------------

-- ----------------------------
-- Table structure for `replyinfo`
-- ----------------------------
DROP TABLE IF EXISTS `replyinfo`;
CREATE TABLE `replyinfo` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `topic_id` int(11) NOT NULL,
  `content` varchar(2000) NOT NULL,
  `create_time` datetime NOT NULL,
  `create_uname` varchar(50) NOT NULL,
  `ip_address` varchar(30) NOT NULL,
  `reply_type` int(11) NOT NULL COMMENT '0回复玩家 1回复管理员',
  PRIMARY KEY (`id`),
  KEY `idx_topicid_time` (`topic_id`,`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of replyinfo
-- ----------------------------

-- ----------------------------
-- Table structure for `sba_data_tw`
-- ----------------------------
DROP TABLE IF EXISTS `sba_data_tw`;
CREATE TABLE `sba_data_tw` (
  `transId` varchar(255) NOT NULL COMMENT '沙巴体育上的下注ID',
  `vendorMemberId` varchar(32) DEFAULT NULL COMMENT '供应商的成员编号',
  `operatorId` varchar(32) DEFAULT NULL COMMENT '操作符号',
  `matchDatetime` varchar(19) DEFAULT NULL COMMENT '比赛开球时间',
  `stake` varchar(20) DEFAULT NULL COMMENT '投注额',
  `transactionTime` varchar(19) DEFAULT NULL COMMENT '下注的交易时间',
  `ticketStatus` varchar(10) DEFAULT NULL COMMENT '票据状态 Half WON/Half LOSE/WON/LOSE/VOID/running/DRAW/Reject/Refund/Waiting）Half WON/Half LOSE/WON/LOSE/DRAW/Refund状态为已结算',
  `winLoseAmount` varchar(20) DEFAULT NULL COMMENT '最终输赢金额',
  `afterAmount` varchar(20) DEFAULT NULL COMMENT '结算后的余额',
  `winLostDateTime` varchar(19) DEFAULT NULL COMMENT '最终下注输赢时间',
  `currency` varchar(20) DEFAULT NULL COMMENT '货币种类id',
  `oddsType` varchar(32) DEFAULT NULL COMMENT '赔率类型',
  `versionKey` varchar(32) DEFAULT NULL COMMENT '版本号',
  PRIMARY KEY (`transId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sba_data_tw
-- ----------------------------

-- ----------------------------
-- Table structure for `sb_bets`
-- ----------------------------
DROP TABLE IF EXISTS `sb_bets`;
CREATE TABLE `sb_bets` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `loginname` varchar(50) DEFAULT NULL COMMENT '用户',
  `createtime` datetime DEFAULT NULL COMMENT '下注时间',
  `sbbets` double DEFAULT NULL COMMENT '下注金额',
  `sbinfo` varchar(255) DEFAULT NULL COMMENT '信息',
  `sbremark` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sb_bets
-- ----------------------------

-- ----------------------------
-- Table structure for `sb_coupon`
-- ----------------------------
DROP TABLE IF EXISTS `sb_coupon`;
CREATE TABLE `sb_coupon` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `loginname` varchar(50) DEFAULT NULL COMMENT '玩家账号',
  `createtime` datetime DEFAULT NULL COMMENT '创建时间',
  `status` int(2) DEFAULT '0' COMMENT '0表示待处理 1表示已经完成',
  `shippingcode` varchar(50) DEFAULT NULL COMMENT '优惠码',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sb_coupon
-- ----------------------------

-- ----------------------------
-- Table structure for `self_record`
-- ----------------------------
DROP TABLE IF EXISTS `self_record`;
CREATE TABLE `self_record` (
  `pno` varchar(50) NOT NULL,
  `loginname` varchar(255) DEFAULT NULL,
  `platform` varchar(255) NOT NULL,
  `selfname` varchar(255) DEFAULT NULL COMMENT '自助优惠名称',
  `type` int(10) NOT NULL DEFAULT '0' COMMENT '0.流水没有达到 1.达到',
  `createtime` datetime DEFAULT NULL,
  `updatetime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间，由数据库自动更新',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`pno`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of self_record
-- ----------------------------
INSERT INTO `self_record` VALUES ('101200915gscjaroeq3', 'kavin998', 'mg', '红包雨', '1', '2020-09-15 01:49:24', '2020-09-15 10:42:25', null);
INSERT INTO `self_record` VALUES ('101200915xoljnbhsne', 'kavin998', 'mg', '红包雨', '1', '2020-09-15 10:42:25', '2020-09-15 11:00:25', null);
INSERT INTO `self_record` VALUES ('101200915dcvtqokqmg', 'kavin998', 'mg', '红包雨', '1', '2020-09-15 11:00:25', '2020-09-17 21:59:02', '目前有效投注总额为826.0元，需要达到投注总额为110.0元，MG游戏账户余额为332.85元');
INSERT INTO `self_record` VALUES ('10120091524kav4pkhf', 'kavin998', 'dt', '红包雨', '1', '2020-09-15 11:45:35', '2020-09-17 21:58:46', '目前有效投注总额为17.5元，需要达到投注总额为0.0元，DT游戏账户余额为1.5元');
INSERT INTO `self_record` VALUES ('101200920f9evlwhixk', 'austin998', 'cq9', '红包雨', '1', '2020-09-20 19:41:13', '2020-09-20 19:41:51', null);
INSERT INTO `self_record` VALUES ('101200920i0kljulcwy', 'austin998', 'cq9', '红包雨', '1', '2020-09-20 19:41:51', '2020-09-20 20:04:01', null);
INSERT INTO `self_record` VALUES ('1012009207saelfpyf7', 'austin998', 'cq9', '红包雨', '1', '2020-09-20 20:04:01', '2020-09-20 20:05:18', null);
INSERT INTO `self_record` VALUES ('101200920xg7fu452v9', 'austin998', 'cq9', '红包雨', '1', '2020-09-20 20:05:18', '2020-09-20 20:06:20', null);
INSERT INTO `self_record` VALUES ('101200920c4sfxeclru', 'austin998', 'cq9', '红包雨', '1', '2020-09-20 20:06:20', '2020-09-20 20:07:52', null);
INSERT INTO `self_record` VALUES ('101200920fr94t5ffh9', 'austin998', 'cq9', '红包雨', '1', '2020-09-20 20:07:52', '2020-09-20 20:08:43', null);
INSERT INTO `self_record` VALUES ('101200920ull1gbzfrq', 'austin998', 'cq9', '红包雨', '1', '2020-09-20 20:08:43', '2020-09-20 20:10:12', null);
INSERT INTO `self_record` VALUES ('101200920ciqjgw5705', 'austin998', 'cq9', '红包雨', '1', '2020-09-20 20:10:12', '2020-09-20 20:10:58', null);
INSERT INTO `self_record` VALUES ('101200920dh1rxbzswe', 'austin998', 'cq9', '红包雨', '0', '2020-09-20 20:10:58', null, null);
INSERT INTO `self_record` VALUES ('101200927fqno4qg542', 'kavin998', 'cq9', '红包雨', '1', '2020-09-27 13:30:38', '2020-09-27 13:34:43', null);
INSERT INTO `self_record` VALUES ('1012009273q4fpfztpt', 'kavin998', 'cq9', '红包雨', '1', '2020-09-27 13:34:43', '2020-09-27 13:37:34', null);
INSERT INTO `self_record` VALUES ('1012009277rm9gg0223', 'kavin998', 'cq9', '红包雨', '0', '2020-09-27 13:37:34', null, null);
INSERT INTO `self_record` VALUES ('101200927lfmgh6l3qp', 'kavin998', 'pg', '红包雨', '1', '2020-09-27 14:00:24', '2020-09-27 14:02:34', null);
INSERT INTO `self_record` VALUES ('101200927xhqnmqauad', 'kavin998', 'pg', '红包雨', '0', '2020-09-27 14:02:34', null, null);
INSERT INTO `self_record` VALUES ('101200930m10bxunakg', 'kavin998', 'bg', '红包雨', '1', '2020-09-30 00:43:37', '2020-09-30 00:44:04', null);
INSERT INTO `self_record` VALUES ('101200930gf2zbdgh5f', 'kavin998', 'bg', '红包雨', '1', '2020-09-30 00:44:04', '2020-09-30 00:45:15', null);
INSERT INTO `self_record` VALUES ('101200930pmqlltukju', 'kavin998', 'bg', '红包雨', '1', '2020-09-30 00:45:15', '2020-09-30 00:49:07', null);
INSERT INTO `self_record` VALUES ('101200930hixnjrxgj7', 'kavin998', 'bg', '红包雨', '0', '2020-09-30 00:49:07', null, null);
INSERT INTO `self_record` VALUES ('101201005ycwc1xumsz', 'kavin998', 'mg', '红包雨', '0', '2020-10-05 00:46:19', null, null);
INSERT INTO `self_record` VALUES ('101201005q9s57kdsjw', 'austin998', '', '红包雨', '0', '2020-10-05 15:12:37', null, null);
INSERT INTO `self_record` VALUES ('101201005etg63bsddi', 'austin998', 'mg', '红包雨', '0', '2020-10-05 15:13:37', null, null);
INSERT INTO `self_record` VALUES ('101201005r8rslgdarh', 'austin998', 'sba', '红包雨', '0', '2020-10-05 15:20:59', null, null);
INSERT INTO `self_record` VALUES ('101201005b8mgb6ifl3', 'kavin998', 'sba', '红包雨', '1', '2020-10-05 15:34:10', '2020-10-05 15:34:35', null);
INSERT INTO `self_record` VALUES ('1012010050e2sbl3o8k', 'kavin998', 'sba', '红包雨', '1', '2020-10-05 15:34:35', '2020-10-05 15:38:22', null);
INSERT INTO `self_record` VALUES ('101201005bnao064ocp', 'kavin998', 'sba', '红包雨', '1', '2020-10-05 15:38:22', '2020-10-05 15:41:41', null);
INSERT INTO `self_record` VALUES ('1012010058bzzchcjwe', 'kavin998', 'sba', '红包雨', '0', '2020-10-05 15:41:41', null, null);
INSERT INTO `self_record` VALUES ('101201008ukdlqp4vyy', 'austin998', 'dt', '红包雨', '0', '2020-10-08 18:41:26', null, null);

-- ----------------------------
-- Table structure for `sendemail`
-- ----------------------------
DROP TABLE IF EXISTS `sendemail`;
CREATE TABLE `sendemail` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `email` varchar(255) DEFAULT NULL COMMENT '邮件地址',
  `username` varchar(32) DEFAULT NULL,
  `emailflag` varchar(2) DEFAULT NULL COMMENT '邮件地址是否有效 0无效 1有效 2未检测 3未知',
  `sendflag` varchar(2) DEFAULT NULL COMMENT '邮件是否发送 0未发送 1待发送 2已发送 3发送失败',
  `isclick` varchar(2) DEFAULT '' COMMENT '玩家是否点击邮件中的链接',
  `isopen` varchar(2) DEFAULT NULL COMMENT '玩家是否打开邮件0未打开1已打开',
  `ip` varchar(64) DEFAULT NULL COMMENT '玩家打开邮件的ip',
  `times` int(11) DEFAULT NULL COMMENT '邮件打开次数',
  `device` varchar(255) DEFAULT NULL COMMENT '玩家打开邮件的设备',
  `remark` varchar(255) DEFAULT NULL,
  `sendtime` datetime DEFAULT NULL COMMENT '发送时间',
  `opentime` datetime DEFAULT NULL COMMENT '邮件打开的时间',
  `imptime` datetime DEFAULT NULL COMMENT '数据插入时间',
  `bt` varchar(255) DEFAULT NULL,
  `nr` mediumtext,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=15416 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sendemail
-- ----------------------------

-- ----------------------------
-- Table structure for `seq`
-- ----------------------------
DROP TABLE IF EXISTS `seq`;
CREATE TABLE `seq` (
  `seqName` varchar(30) NOT NULL,
  `seqValue` varchar(20) NOT NULL,
  PRIMARY KEY (`seqName`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of seq
-- ----------------------------
INSERT INTO `seq` VALUES ('SEQ_CSNO', '10');
INSERT INTO `seq` VALUES ('SEQ_USERID', '30010');

-- ----------------------------
-- Table structure for `signamount`
-- ----------------------------
DROP TABLE IF EXISTS `signamount`;
CREATE TABLE `signamount` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(150) DEFAULT NULL,
  `amountbalane` double DEFAULT NULL COMMENT '奖金余额',
  `updatetime` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(255) DEFAULT NULL,
  `continuesigncount` int(11) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of signamount
-- ----------------------------

-- ----------------------------
-- Table structure for `signrecord`
-- ----------------------------
DROP TABLE IF EXISTS `signrecord`;
CREATE TABLE `signrecord` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(150) DEFAULT NULL,
  `createtime` datetime DEFAULT NULL,
  `isused` varchar(2) DEFAULT NULL COMMENT '是否被使用，0未使用  1已使用(该字段暂时不用)',
  `isdelete` varchar(2) DEFAULT NULL COMMENT '是否被删除 0未删除  1已删除',
  `remark` varchar(255) DEFAULT NULL,
  `updatetime` datetime DEFAULT NULL,
  `type` varchar(2) DEFAULT NULL COMMENT '0代表签到1代表存款奖金2代表流水奖金',
  `amount` double DEFAULT NULL,
  `monthCount` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of signrecord
-- ----------------------------

-- ----------------------------
-- Table structure for `singleparty`
-- ----------------------------
DROP TABLE IF EXISTS `singleparty`;
CREATE TABLE `singleparty` (
  `loginname` varchar(20) NOT NULL,
  `bettotal` double(24,2) DEFAULT NULL,
  `ranking` int(11) DEFAULT NULL,
  `rankdate` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`loginname`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of singleparty
-- ----------------------------

-- ----------------------------
-- Table structure for `slots_match_weekly`
-- ----------------------------
DROP TABLE IF EXISTS `slots_match_weekly`;
CREATE TABLE `slots_match_weekly` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `loginname` varchar(50) NOT NULL,
  `startTime` datetime NOT NULL,
  `endTime` datetime NOT NULL,
  `getTime` datetime NOT NULL,
  `bet` double DEFAULT NULL,
  `win` double NOT NULL,
  `platform` varchar(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`) USING BTREE
) ENGINE=MyISAM AUTO_INCREMENT=605 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of slots_match_weekly
-- ----------------------------

-- ----------------------------
-- Table structure for `smsswitch`
-- ----------------------------
DROP TABLE IF EXISTS `smsswitch`;
CREATE TABLE `smsswitch` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(64) NOT NULL,
  `type` char(255) NOT NULL,
  `content` varchar(64) DEFAULT NULL COMMENT '短信内容',
  `minvalue` int(11) DEFAULT NULL COMMENT '触发发送短信的最小值',
  `disable` char(1) DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of smsswitch
-- ----------------------------

-- ----------------------------
-- Table structure for `suggest`
-- ----------------------------
DROP TABLE IF EXISTS `suggest`;
CREATE TABLE `suggest` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `loginname` varchar(20) NOT NULL COMMENT '用户名',
  `flow_id` varchar(10) NOT NULL COMMENT '工作流ID',
  `run_id` varchar(10) NOT NULL COMMENT '工单流水号',
  `type` varchar(32) NOT NULL COMMENT '问题类型',
  `createtime` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of suggest
-- ----------------------------

-- ----------------------------
-- Table structure for `sw_data`
-- ----------------------------
DROP TABLE IF EXISTS `sw_data`;
CREATE TABLE `sw_data` (
  `roundId` varchar(32) NOT NULL,
  `brandId` varchar(32) DEFAULT NULL,
  `playerCode` varchar(32) DEFAULT NULL COMMENT '玩家账号',
  `gameCode` varchar(32) DEFAULT NULL COMMENT '游戏编号',
  `currency` varchar(32) DEFAULT NULL COMMENT '币种',
  `win` double(14,2) DEFAULT NULL COMMENT '派彩',
  `bet` double(14,2) DEFAULT NULL COMMENT '投注',
  `revenue` double(14,2) DEFAULT NULL COMMENT '收入（输赢）',
  `ts` varchar(32) DEFAULT NULL COMMENT '下注时间',
  `balanceBefore` varchar(32) DEFAULT NULL COMMENT '结算前',
  `balanceAfter` varchar(32) DEFAULT NULL COMMENT '结算后',
  `device` varchar(32) DEFAULT NULL COMMENT '终端类型',
  `insertedAt` varchar(32) DEFAULT NULL COMMENT '入库时间',
  `totalEvents` varchar(32) DEFAULT NULL COMMENT '事件记录数',
  PRIMARY KEY (`roundId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sw_data
-- ----------------------------

-- ----------------------------
-- Table structure for `systemconfig`
-- ----------------------------
DROP TABLE IF EXISTS `systemconfig`;
CREATE TABLE `systemconfig` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `typeNo` varchar(12) DEFAULT NULL,
  `typeName` varchar(80) DEFAULT NULL,
  `itemNo` varchar(10) DEFAULT NULL,
  `value` varchar(1000) DEFAULT NULL,
  `note` varchar(255) DEFAULT NULL,
  `flag` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=175 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of systemconfig
-- ----------------------------
INSERT INTO `systemconfig` VALUES ('1', 'type001', '发件人', '001', '天威娱乐', '天威娱乐', '是');
INSERT INTO `systemconfig` VALUES ('78', 'type010', '推荐好友次存奖励', '001', '0.01', '推荐好友次存奖励', '是');
INSERT INTO `systemconfig` VALUES ('62', 'type100', '手机号', '001', '17080396022或17080396007', '体验金短信手机号', '否');
INSERT INTO `systemconfig` VALUES ('70', 'type101', '内部代理', '001', 'a_ldcs1;a_ldcs2:a_ldcs3;a_ldcs4;a_ldcs5;a_ldcs6', '内部代理账号，不要关闭', '否');
INSERT INTO `systemconfig` VALUES ('71', 'type102', '代理专员代码', '001', 'A;B;', '代理专员代码', '否');
INSERT INTO `systemconfig` VALUES ('79', 'type011', '推荐好友奖金流水倍数限制', '001', '20', '推荐好友奖金转入游戏的流水倍数限制', '否');
INSERT INTO `systemconfig` VALUES ('80', 'type012', '推荐好友首存最高奖励金额', '001', '288', '推荐好友首存最高奖励金额', '是');
INSERT INTO `systemconfig` VALUES ('81', 'type013', '推荐好友次存最高奖励金额', '001', '388', '推荐好友次存最高奖励金额', '是');
INSERT INTO `systemconfig` VALUES ('82', 'type009', '推荐好友首存奖励', '001', '0.5', '推荐好友首存奖励', '是');
INSERT INTO `systemconfig` VALUES ('103', 'type103', '提款限制', '001', '2000', '', '否');
INSERT INTO `systemconfig` VALUES ('104', 'type021', '是否禁用自建邮件服务器', '001', '001', '是否禁用自建邮件服务器', '是');
INSERT INTO `systemconfig` VALUES ('109', 'type999', '微信支付限制', '001', '50000', '', '否');
INSERT INTO `systemconfig` VALUES ('92', 'type015', '积分派发翻倍奖励', '001', '001', '实际派发积分=原始派发积分*配置值', '否');
INSERT INTO `systemconfig` VALUES ('121', 'type901', '流水排行活动', '2', '2', '显示期间', '否');
INSERT INTO `systemconfig` VALUES ('119', 'type200', '不执行日结佣金代理账号', '001', 'a_kk02', '这些账号不执行日结佣金，多个账号请用,隔开', '否');
INSERT INTO `systemconfig` VALUES ('165', 'type002', '上天威，满载而归', '001', '【次优惠】笔存笔送，最高30%无上限，提升您娱乐享受<br/>【天生赢家】AG百家乐钜惠，逢八就发、逢九就赢！最高1688彩金！<br/>【周周送金】完成每周指定任务，每日领取最高688彩金，连续领七天！<br/>【海量红包】下载龙都app，每日可抢红包雨，领取最高188彩金！<br/>【6月19日】老虎机钱包存50送30、存200送100（2选1）<br/>【6月19日】PT存50送30、存200送100（2选1）<br/>【6月19日】TTG存50送30、存200送100（2选1）<br/>【论坛龙币】每日登入论坛！领取龙币红包，摇摇乐随机最高8888龙币！<br/>', '官网弹窗', '否');
INSERT INTO `systemconfig` VALUES ('143', 'type201', '限时存送最低存入配置', '001', '10', '限时存送最低配置额度，禁用时不限制', '否');
INSERT INTO `systemconfig` VALUES ('149', 'type023', '存款自助红包是否禁用', '001', '1', '存款自助红包是否禁用', '否');
INSERT INTO `systemconfig` VALUES ('166', 'type987', '红包雨主账号转入限额', 'type987', '1000', '', '否');
INSERT INTO `systemconfig` VALUES ('167', 'type513', '生日礼金', '001', null, '自助生日礼金是否禁用', '否');

-- ----------------------------
-- Table structure for `systemlogs`
-- ----------------------------
DROP TABLE IF EXISTS `systemlogs`;
CREATE TABLE `systemlogs` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `loginname` varchar(20) NOT NULL,
  `action` varchar(20) DEFAULT NULL,
  `createtime` datetime DEFAULT NULL,
  `applyDate` varchar(20) DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `action` (`action`,`applyDate`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of systemlogs
-- ----------------------------

-- ----------------------------
-- Table structure for `s_permission`
-- ----------------------------
DROP TABLE IF EXISTS `s_permission`;
CREATE TABLE `s_permission` (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '编号，主键，自动增长',
  `parent_id` int(10) NOT NULL COMMENT '父编号',
  `name` varchar(50) NOT NULL COMMENT '名称',
  `type` varchar(10) NOT NULL COMMENT '类型(1:菜单/2:按钮)',
  `permission_code` varchar(50) DEFAULT NULL COMMENT '权限码',
  `path` varchar(100) DEFAULT NULL COMMENT '页面路由路径',
  `icon_cls` varchar(50) DEFAULT NULL COMMENT '样式名称',
  `show` int(1) NOT NULL COMMENT '是否显示(1:显示/0:不显示)',
  `component` varchar(50) DEFAULT NULL COMMENT '组件名称',
  `order` int(10) DEFAULT NULL COMMENT '显示顺序',
  `create_user` varchar(50) NOT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_user` varchar(50) NOT NULL COMMENT '修改人',
  `update_time` datetime NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of s_permission
-- ----------------------------

-- ----------------------------
-- Table structure for `task`
-- ----------------------------
DROP TABLE IF EXISTS `task`;
CREATE TABLE `task` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `pno` varchar(20) NOT NULL,
  `level` varchar(20) NOT NULL,
  `flag` int(1) NOT NULL DEFAULT '0',
  `agreeTime` datetime DEFAULT NULL,
  `agreeIp` varchar(16) DEFAULT NULL,
  `manager` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `pno` (`pno`) USING BTREE,
  KEY `flag` (`level`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of task
-- ----------------------------
INSERT INTO `task` VALUES ('1', '629200917rrkqhuep4o', 'AUDIT', '0', '2020-09-17 20:39:12', null, 'kavin998');
INSERT INTO `task` VALUES ('2', '629200917rrkqhuep4o', 'EXCUTE', '0', '2020-09-17 20:39:12', null, 'kavin998');
INSERT INTO `task` VALUES ('3', '633200921lufizym2gq', 'AUDIT', '0', '2020-09-21 21:06:53', null, 'austin998');
INSERT INTO `task` VALUES ('4', '633200921lufizym2gq', 'EXCUTE', '0', '2020-09-21 21:06:53', null, 'austin998');

-- ----------------------------
-- Table structure for `task_amount`
-- ----------------------------
DROP TABLE IF EXISTS `task_amount`;
CREATE TABLE `task_amount` (
  `loginname` varchar(255) NOT NULL,
  `amount` double(10,2) DEFAULT NULL COMMENT '累计金额',
  `remark` varchar(255) DEFAULT NULL,
  `createtime` datetime DEFAULT NULL,
  `updatetime` datetime DEFAULT NULL,
  PRIMARY KEY (`loginname`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of task_amount
-- ----------------------------

-- ----------------------------
-- Table structure for `task_list`
-- ----------------------------
DROP TABLE IF EXISTS `task_list`;
CREATE TABLE `task_list` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` int(10) DEFAULT NULL COMMENT '1.完成一次PT次存2.PT流水1万',
  `title` varchar(255) DEFAULT NULL,
  `giftmoney` double(10,2) NOT NULL COMMENT '彩金',
  `requireData` double(10,2) DEFAULT NULL,
  `disable` int(10) NOT NULL DEFAULT '0' COMMENT '1.置为不可用',
  `createtime` datetime NOT NULL,
  `updatetime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of task_list
-- ----------------------------

-- ----------------------------
-- Table structure for `task_user_record`
-- ----------------------------
DROP TABLE IF EXISTS `task_user_record`;
CREATE TABLE `task_user_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) NOT NULL,
  `title` varchar(255) DEFAULT NULL,
  `taskId` int(11) DEFAULT NULL,
  `type` int(10) DEFAULT NULL COMMENT '1.完成一次PT次存2.PT流水1万',
  `historyBet` double(11,2) DEFAULT NULL,
  `isAdd` int(10) NOT NULL DEFAULT '0' COMMENT '是否添加到任务额度里0.否1.是2.过期3.任务已关闭',
  `createtime` datetime DEFAULT NULL COMMENT '领取任务时间',
  `updatetime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of task_user_record
-- ----------------------------

-- ----------------------------
-- Table structure for `tb_inform`
-- ----------------------------
DROP TABLE IF EXISTS `tb_inform`;
CREATE TABLE `tb_inform` (
  `informId` int(11) NOT NULL AUTO_INCREMENT,
  `informTitle` varchar(30) DEFAULT NULL,
  `informContent` varchar(30) DEFAULT NULL,
  `informTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`informId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_inform
-- ----------------------------

-- ----------------------------
-- Table structure for `telvisit`
-- ----------------------------
DROP TABLE IF EXISTS `telvisit`;
CREATE TABLE `telvisit` (
  `id` int(9) NOT NULL AUTO_INCREMENT,
  `loginname` varchar(30) NOT NULL,
  `addtime` datetime NOT NULL,
  `taskid` int(9) DEFAULT NULL COMMENT '务任ID',
  `islock` int(1) NOT NULL DEFAULT '0' COMMENT '锁定与否；0 未锁定，1锁定',
  `execstatus` int(1) NOT NULL DEFAULT '-1' COMMENT '执行状态；0 失败，1成功，2 未访问，3已访问',
  `locker` varchar(30) DEFAULT NULL COMMENT '锁定记录的人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of telvisit
-- ----------------------------

-- ----------------------------
-- Table structure for `telvisitremark`
-- ----------------------------
DROP TABLE IF EXISTS `telvisitremark`;
CREATE TABLE `telvisitremark` (
  `id` int(9) NOT NULL AUTO_INCREMENT,
  `telvisitid` int(9) NOT NULL,
  `addtime` datetime NOT NULL,
  `remark` varchar(2000) DEFAULT '',
  `operator` varchar(30) DEFAULT '' COMMENT '作员操',
  `execstatus` int(1) DEFAULT '2' COMMENT '执行状态；0 失败，1成功，2 未访问，3已访问',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of telvisitremark
-- ----------------------------

-- ----------------------------
-- Table structure for `telvisittask`
-- ----------------------------
DROP TABLE IF EXISTS `telvisittask`;
CREATE TABLE `telvisittask` (
  `id` int(9) NOT NULL AUTO_INCREMENT,
  `taskname` varchar(200) NOT NULL,
  `taskcondition` varchar(1000) NOT NULL,
  `isdelay` int(1) DEFAULT '0' COMMENT '是否延迟；0 否，1 是',
  `delaytime` int(11) DEFAULT '0' COMMENT '迟延时间',
  `starttime` datetime NOT NULL,
  `endtime` datetime NOT NULL,
  `addtime` datetime NOT NULL,
  `taskstatus` int(1) NOT NULL COMMENT '0未完成，1完成，2废弃',
  `finishtime` datetime DEFAULT NULL COMMENT '任务完成时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of telvisittask
-- ----------------------------

-- ----------------------------
-- Table structure for `tlybank`
-- ----------------------------
DROP TABLE IF EXISTS `tlybank`;
CREATE TABLE `tlybank` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `type` varchar(20) DEFAULT NULL COMMENT '卡类型',
  `loginname` varchar(20) DEFAULT NULL COMMENT '用户名',
  `accountName` varchar(50) DEFAULT NULL COMMENT '持卡人',
  `accountType` varchar(20) DEFAULT NULL COMMENT '卡类型',
  `accountCity` varchar(50) DEFAULT NULL COMMENT '开户城市',
  `bankAddress` varchar(100) DEFAULT NULL COMMENT '开户地点',
  `accountNo` varchar(32) DEFAULT NULL COMMENT '卡号',
  `bank` varchar(50) DEFAULT NULL COMMENT '银行名称',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `createTime` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`) USING BTREE
) ENGINE=MyISAM AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tlybank
-- ----------------------------

-- ----------------------------
-- Table structure for `tlyorder`
-- ----------------------------
DROP TABLE IF EXISTS `tlyorder`;
CREATE TABLE `tlyorder` (
  `pno` varchar(30) NOT NULL COMMENT '提案号',
  `amout` double(24,2) DEFAULT NULL COMMENT '金额',
  `card_number` varchar(30) DEFAULT NULL COMMENT '卡号',
  `billno` varchar(30) DEFAULT NULL COMMENT '订单号',
  `loginname` varchar(30) DEFAULT NULL,
  `createTime` datetime DEFAULT NULL COMMENT '创建时间',
  `flag` int(8) DEFAULT NULL COMMENT '状态-1 未处理，0 失败 1 成功 2 已处理',
  `bankname` varchar(32) DEFAULT NULL COMMENT '银行名称',
  `updateTime` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `accountName` varchar(32) DEFAULT NULL COMMENT '持卡人姓名',
  `phone` varchar(32) DEFAULT NULL COMMENT '手机号',
  `bankAddress` varchar(100) DEFAULT NULL COMMENT '开户行地址',
  PRIMARY KEY (`pno`),
  UNIQUE KEY `pno` (`pno`) USING BTREE
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tlyorder
-- ----------------------------

-- ----------------------------
-- Table structure for `tokeninfo`
-- ----------------------------
DROP TABLE IF EXISTS `tokeninfo`;
CREATE TABLE `tokeninfo` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `token` char(36) NOT NULL,
  `platform` varchar(50) DEFAULT NULL,
  `loginname` varchar(50) DEFAULT NULL,
  `timestamp` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `token_UNIQUE` (`token`) USING BTREE
) ENGINE=MyISAM AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tokeninfo
-- ----------------------------

-- ----------------------------
-- Table structure for `topicinfo`
-- ----------------------------
DROP TABLE IF EXISTS `topicinfo`;
CREATE TABLE `topicinfo` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(50) NOT NULL,
  `content` varchar(2000) NOT NULL,
  `create_time` datetime NOT NULL,
  `create_uname` varchar(50) NOT NULL,
  `ip_address` varchar(30) NOT NULL,
  `flag` int(11) NOT NULL DEFAULT '0' COMMENT '0 已审批 1 未审批',
  `re_count` int(11) NOT NULL DEFAULT '0',
  `topic_type` int(11) NOT NULL COMMENT '0发帖给玩家 1发帖给管理员',
  `user_name_type` int(11) NOT NULL COMMENT '100 全部会员 101全部代理    102个人群发 103代表是按照客服推荐码群发104代表是按照代理账号群发 105代表管理员 大于0对应玩家等级 ',
  `topic_status` int(11) NOT NULL COMMENT '0 单发  1 群发',
  `is_admin_read` int(11) NOT NULL DEFAULT '0' COMMENT '0管理员未读 1管理员已读',
  PRIMARY KEY (`id`),
  KEY `idx_time` (`create_time`),
  KEY `idx_uname` (`create_uname`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of topicinfo
-- ----------------------------
INSERT INTO `topicinfo` VALUES ('1', 'dd', 'dds', '2020-09-15 20:14:40', 'kavin998', '127.0.0.1', '0', '0', '1', '105', '0', '0');

-- ----------------------------
-- Table structure for `topicstatus`
-- ----------------------------
DROP TABLE IF EXISTS `topicstatus`;
CREATE TABLE `topicstatus` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `topic_id` int(11) NOT NULL,
  `receive_uname` varchar(50) NOT NULL,
  `ip_address` varchar(30) NOT NULL,
  `is_user_read` int(11) NOT NULL DEFAULT '0' COMMENT '0玩家未读 1玩家已读',
  `create_time` datetime NOT NULL,
  `is_valid` int(11) NOT NULL DEFAULT '0' COMMENT '0有效 1已失效',
  `title` varchar(50) DEFAULT NULL COMMENT '提案专用主题',
  `content` varchar(2000) DEFAULT NULL COMMENT '提案专用内容',
  PRIMARY KEY (`id`),
  KEY `idx_topicid` (`topic_id`),
  KEY `idx_time` (`create_time`),
  KEY `idx_uname` (`receive_uname`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of topicstatus
-- ----------------------------
INSERT INTO `topicstatus` VALUES ('1', '1', '客服管理员', '127.0.0.1', '1', '2020-09-15 20:14:40', '0', null, null);

-- ----------------------------
-- Table structure for `totalpoint`
-- ----------------------------
DROP TABLE IF EXISTS `totalpoint`;
CREATE TABLE `totalpoint` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `totalpoints` double DEFAULT NULL COMMENT '总积分',
  `username` varchar(200) DEFAULT NULL,
  `updatetime` datetime DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  `oldtotalpoints` double DEFAULT NULL COMMENT '历史积分总额',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uc_totalpoint` (`username`) USING BTREE
) ENGINE=MyISAM AUTO_INCREMENT=68500 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of totalpoint
-- ----------------------------

-- ----------------------------
-- Table structure for `total_deposits`
-- ----------------------------
DROP TABLE IF EXISTS `total_deposits`;
CREATE TABLE `total_deposits` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `loginname` varchar(32) DEFAULT NULL COMMENT '玩家账号',
  `alldeposit` double(30,2) DEFAULT NULL COMMENT '所有存款',
  `createtime` datetime DEFAULT NULL COMMENT '创建时间',
  `updatetime` datetime DEFAULT NULL COMMENT '创建时间',
  `oneDeposit` double(30,2) NOT NULL DEFAULT '0.00' COMMENT '前一个月存款',
  `twoDeposit` double(30,2) NOT NULL DEFAULT '0.00' COMMENT '前两个月存款',
  `extend1` double(30,2) NOT NULL DEFAULT '0.00' COMMENT '扩展字段',
  `extend2` int(30) NOT NULL DEFAULT '0' COMMENT '扩展字段',
  `extend3` varchar(50) NOT NULL DEFAULT '' COMMENT '扩展字段',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of total_deposits
-- ----------------------------
INSERT INTO `total_deposits` VALUES ('23', 'twtest01', '0.00', '2018-12-23 08:03:44', '2018-12-23 08:03:44', '0.00', '0.00', '0.00', '0', '');
INSERT INTO `total_deposits` VALUES ('24', 'windtest', '0.00', '2018-12-25 02:58:40', '2018-12-29 16:05:05', '0.00', '0.00', '0.00', '0', '');
INSERT INTO `total_deposits` VALUES ('25', 'twnick', '0.00', '2018-12-25 03:07:37', '2018-12-25 03:07:37', '0.00', '0.00', '0.00', '0', '');
INSERT INTO `total_deposits` VALUES ('26', 'wadetest', '0.00', '2018-12-26 14:39:47', '2018-12-26 14:39:47', '0.00', '0.00', '0.00', '0', '');

-- ----------------------------
-- Table structure for `transfer`
-- ----------------------------
DROP TABLE IF EXISTS `transfer`;
CREATE TABLE `transfer` (
  `id` bigint(11) NOT NULL,
  `source` varchar(12) NOT NULL,
  `target` varchar(12) NOT NULL,
  `remit` double(24,2) NOT NULL,
  `loginname` varchar(20) NOT NULL,
  `createtime` datetime NOT NULL,
  `credit` double NOT NULL,
  `newCredit` double NOT NULL,
  `flag` int(1) NOT NULL,
  `paymentid` varchar(40) DEFAULT NULL,
  `remark` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`) USING BTREE,
  KEY `source` (`source`) USING BTREE,
  KEY `target` (`target`) USING BTREE,
  KEY `createtime` (`createtime`) USING BTREE,
  KEY `flag` (`flag`) USING BTREE,
  KEY `remit` (`remit`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of transfer
-- ----------------------------
INSERT INTO `transfer` VALUES ('2509131103042458783', '', 'mg', '1.00', 'kavin998', '2020-09-13 11:02:31', '10000', '9999', '0', null, '转入成功');
INSERT INTO `transfer` VALUES ('2509132253293454457', 'tianwei', 'dt', '12.00', 'kavin998', '2020-09-13 22:53:25', '9999', '9987', '0', null, '转入成功');
INSERT INTO `transfer` VALUES ('2509132253511176557', 'tianwei', 'dt', '12.00', 'kavin998', '2020-09-13 22:53:51', '9987', '9975', '0', null, '转入成功');
INSERT INTO `transfer` VALUES ('2509132259085888991', 'dt', 'tianwei', '22.00', 'kavin998', '2020-09-13 22:59:08', '9975', '9997', '0', null, '转出成功');
INSERT INTO `transfer` VALUES ('2509132259331288659', 'tianwei', 'dt', '7.00', 'kavin998', '2020-09-13 22:59:33', '9997', '9990', '0', null, '转入成功');
INSERT INTO `transfer` VALUES ('2509142010343089281', 'tianwei', 'mg', '16.00', 'kavin998', '2020-09-14 20:10:33', '9990', '9974', '0', null, '转入成功');
INSERT INTO `transfer` VALUES ('2509142021055542204', 'tianwei', 'mg', '14.00', 'kavin998', '2020-09-14 20:21:05', '9974', '9960', '0', null, '转入成功');
INSERT INTO `transfer` VALUES ('2509142027516914514', 'tianwei', 'mg', '11.00', 'kavin998', '2020-09-14 20:27:51', '9960', '9949', '0', null, '转入成功');
INSERT INTO `transfer` VALUES ('2509142102366849901', 'tianwei', 'mg', '11.00', 'kavin998', '2020-09-14 21:02:30', '9949', '9938', '0', null, '转入成功');
INSERT INTO `transfer` VALUES ('2509142130196288151', 'mg', 'tianwei', '12.00', 'kavin998', '2020-09-14 21:30:18', '9938', '9950', '0', null, '转出成功');
INSERT INTO `transfer` VALUES ('2509142130449855928', 'dt', 'tianwei', '1.00', 'kavin998', '2020-09-14 21:30:45', '9950', '9951', '0', null, '转出成功');
INSERT INTO `transfer` VALUES ('2509150149247035766', 'tianwei', 'mg', '11.00', 'kavin998', '2020-09-15 01:49:24', '9929', '9918', '0', null, '转入成功');
INSERT INTO `transfer` VALUES ('2509151042257256825', 'tianwei', 'mg', '11.00', 'kavin998', '2020-09-15 10:42:25', '9829', '9818', '0', null, '转入成功');
INSERT INTO `transfer` VALUES ('2509151100259367533', 'tianwei', 'mg', '11.00', 'kavin998', '2020-09-15 11:00:26', '9829', '9818', '0', null, '转入成功');
INSERT INTO `transfer` VALUES ('2509151145350363481', 'tianwei', 'dt', '11.00', 'kavin998', '2020-09-15 11:45:35', '9829', '9818', '0', null, '转入成功');
INSERT INTO `transfer` VALUES ('2509172158465575983', 'tianwei', 'dt', '1000.00', 'kavin998', '2020-09-17 21:58:46', '9833.1', '8833.1', '0', null, '转入成功');
INSERT INTO `transfer` VALUES ('2509172159026061841', 'tianwei', 'mg', '500.00', 'kavin998', '2020-09-17 21:59:02', '8833.1', '8333.1', '0', null, '转入成功');
INSERT INTO `transfer` VALUES ('2509201710507809157', 'tianwei', 'dt', '12.00', 'austin998', '2020-09-20 17:10:50', '10000', '9988', '0', null, '转入成功');
INSERT INTO `transfer` VALUES ('2509201722460522002', 'tianwei', '', '13.00', 'austin998', '2020-09-20 17:22:46', '9988', '9975', '0', null, '转入成功');
INSERT INTO `transfer` VALUES ('2509201724168310989', 'tianwei', '', '11.00', 'austin998', '2020-09-20 17:24:17', '9975', '9964', '0', null, '转入成功');
INSERT INTO `transfer` VALUES ('2509201724424482156', 'tianwei', '', '11.00', 'austin998', '2020-09-20 17:24:42', '9964', '9953', '0', null, '转入成功');
INSERT INTO `transfer` VALUES ('2509201728119148344', 'tianwei', '', '11.00', 'austin998', '2020-09-20 17:28:12', '9953', '9942', '0', null, '转入成功');
INSERT INTO `transfer` VALUES ('2509201728440109108', '', 'tianwei', '6.00', 'austin998', '2020-09-20 17:28:44', '9942', '9948', '0', null, '转出成功');
INSERT INTO `transfer` VALUES ('2509201730417880063', '', 'tianwei', '6.00', 'austin998', '2020-09-20 17:30:42', '9948', '9954', '0', null, '转出成功');
INSERT INTO `transfer` VALUES ('2509201743145090490', 'tianwei', 'mg', '66.00', 'austin998', '2020-09-20 17:43:14', '9954', '9888', '0', null, '转入成功');
INSERT INTO `transfer` VALUES ('2509201743286844307', 'mg', 'tianwei', '6.00', 'austin998', '2020-09-20 17:43:28', '9888', '9894', '0', null, '转出成功');
INSERT INTO `transfer` VALUES ('2509201743527328675', 'mg', 'tianwei', '4.00', 'austin998', '2020-09-20 17:43:52', '9894', '9898', '0', null, '转出成功');
INSERT INTO `transfer` VALUES ('2509201828390672122', 'mg', 'tianwei', '2.00', 'austin998', '2020-09-20 18:28:38', '9898', '9900', '0', null, '转出成功');
INSERT INTO `transfer` VALUES ('2509201829218932452', 'mg', 'tianwei', '2.00', 'austin998', '2020-09-20 18:29:21', '9900', '9902', '0', null, '转出成功');
INSERT INTO `transfer` VALUES ('2509201829481920098', 'tianwei', 'mg', '1.00', 'austin998', '2020-09-20 18:29:48', '9902', '9901', '0', null, '转入成功');
INSERT INTO `transfer` VALUES ('2509201830450286539', 'tianwei', 'mg', '1.00', 'austin998', '2020-09-20 18:30:45', '9901', '9900', '0', null, '转入成功');
INSERT INTO `transfer` VALUES ('2509201831235926501', 'tianwei', 'mg', '1.00', 'austin998', '2020-09-20 18:31:22', '9900', '9899', '0', null, '转入成功');
INSERT INTO `transfer` VALUES ('2509201839429127651', '', 'tianwei', '4.00', 'austin998', '2020-09-20 18:39:42', '9899', '9903', '0', null, '转出成功');
INSERT INTO `transfer` VALUES ('2509201941137638007', 'tianwei', 'mg', '11.00', 'austin998', '2020-09-20 19:41:13', '9753', '9742', '0', null, '转入成功');
INSERT INTO `transfer` VALUES ('2509201941518869232', 'tianwei', 'mg', '11.00', 'austin998', '2020-09-20 19:41:51', '9753', '9742', '0', null, '转入成功');
INSERT INTO `transfer` VALUES ('2509202004016085633', 'tianwei', 'mg', '13.00', 'austin998', '2020-09-20 20:04:01', '9753', '9740', '0', null, '转入成功');
INSERT INTO `transfer` VALUES ('2509202005184390387', 'tianwei', 'mg', '12.00', 'austin998', '2020-09-20 20:05:18', '9753', '9741', '0', null, '转入成功');
INSERT INTO `transfer` VALUES ('2509202006203638718', 'tianwei', 'mg', '13.00', 'austin998', '2020-09-20 20:06:20', '9753', '9740', '0', null, '转入成功');
INSERT INTO `transfer` VALUES ('2509202007524053089', 'tianwei', 'mg', '11.00', 'austin998', '2020-09-20 20:07:52', '9753', '9742', '0', null, '转入成功');
INSERT INTO `transfer` VALUES ('2509202008439291102', 'tianwei', 'mg', '11.00', 'austin998', '2020-09-20 20:08:44', '9753', '9742', '0', null, '转入成功');
INSERT INTO `transfer` VALUES ('2509202010128874065', 'tianwei', 'mg', '11.00', 'austin998', '2020-09-20 20:10:12', '9753', '9742', '0', null, '转入成功');
INSERT INTO `transfer` VALUES ('2509202010588314715', 'tianwei', 'mg', '22.00', 'austin998', '2020-09-20 20:10:58', '9641', '9619', '0', null, '转入成功');
INSERT INTO `transfer` VALUES ('2509242330576197013', 'tianwei', 'mg', '11.00', 'kavin998', '2020-09-24 23:30:57', '9444.1', '9433.1', '0', null, '转入成功');
INSERT INTO `transfer` VALUES ('2509270004019952065', 'tianwei', '', '12.00', 'kavin998', '2020-09-27 00:03:57', '9433.1', '9421.1', '0', null, '转入成功');
INSERT INTO `transfer` VALUES ('2509270004518207499', 'tianwei', '', '13.00', 'kavin998', '2020-09-27 00:04:51', '9421.1', '9408.1', '0', null, '转入成功');
INSERT INTO `transfer` VALUES ('2509270006017070363', 'tianwei', '', '11.00', 'kavin998', '2020-09-27 00:06:01', '9408.1', '9397.1', '0', null, '转入成功');
INSERT INTO `transfer` VALUES ('2509270008536809127', 'tianwei', '', '11.00', 'kavin998', '2020-09-27 00:08:53', '9397.1', '9386.1', '0', null, '转入成功');
INSERT INTO `transfer` VALUES ('2509270009142666135', 'tianwei', '', '11.00', 'kavin998', '2020-09-27 00:09:14', '9386.1', '9375.1', '0', null, '转入成功');
INSERT INTO `transfer` VALUES ('2509270009319053008', 'tianwei', '', '12.00', 'kavin998', '2020-09-27 00:09:32', '9375.1', '9363.1', '0', null, '转入成功');
INSERT INTO `transfer` VALUES ('2509270010381657748', 'tianwei', '', '33.00', 'kavin998', '2020-09-27 00:10:38', '9363.1', '9330.1', '0', null, '转入成功');
INSERT INTO `transfer` VALUES ('2509270012356016828', 'tianwei', '', '11.00', 'kavin998', '2020-09-27 00:12:35', '9330.1', '9319.1', '0', null, '转入成功');
INSERT INTO `transfer` VALUES ('2509270013119088722', 'tianwei', '', '12.00', 'kavin998', '2020-09-27 00:13:11', '9319.1', '9307.1', '0', null, '转入成功');
INSERT INTO `transfer` VALUES ('2509270013566858581', 'tianwei', '', '23.00', 'kavin998', '2020-09-27 00:13:56', '9307.1', '9284.1', '0', null, '转入成功');
INSERT INTO `transfer` VALUES ('2509270016108072882', 'tianwei', '', '12.00', 'kavin998', '2020-09-27 00:16:10', '9284.1', '9272.1', '0', null, '转入成功');
INSERT INTO `transfer` VALUES ('2509270022079774546', '', 'tianwei', '12.00', 'kavin998', '2020-09-27 00:22:08', '9272.1', '9284.1', '0', null, '转出成功');
INSERT INTO `transfer` VALUES ('2509271330380312589', 'tianwei', 'mg', '12.00', 'kavin998', '2020-09-27 13:30:38', '9284.1', '9272.1', '0', null, '转入成功');
INSERT INTO `transfer` VALUES ('2509271334434830989', 'tianwei', 'mg', '12.00', 'kavin998', '2020-09-27 13:34:43', '9284.1', '9272.1', '0', null, '转入成功');
INSERT INTO `transfer` VALUES ('2509271337341478395', 'tianwei', 'mg', '11.00', 'kavin998', '2020-09-27 13:37:34', '9284.1', '9273.1', '0', null, '转入成功');
INSERT INTO `transfer` VALUES ('2509271400246584724', 'tianwei', 'mg', '11.00', 'kavin998', '2020-09-27 14:00:24', '9284.1', '9273.1', '0', null, '转入成功');
INSERT INTO `transfer` VALUES ('2509271402343348454', 'tianwei', 'mg', '20.00', 'kavin998', '2020-09-27 14:02:34', '9284.1', '9264.1', '0', null, '转入成功');
INSERT INTO `transfer` VALUES ('2509271519458051119', 'tianwei', 'mg', '12.00', 'kavin998', '2020-09-27 15:19:45', '9284.1', '9272.1', '0', null, '转入成功');
INSERT INTO `transfer` VALUES ('2509271520217850746', 'tianwei', 'dt', '12.00', 'kavin998', '2020-09-27 15:20:22', '9272.1', '9260.1', '0', null, '转入成功');
INSERT INTO `transfer` VALUES ('2509272102535479558', 'tianwei', 'dt', '111.00', 'kavin998', '2020-09-27 21:02:53', '9260.1', '9149.1', '0', null, '转入成功');
INSERT INTO `transfer` VALUES ('2509272114412283971', 'tianwei', 'mg', '12.00', 'kavin998', '2020-09-27 21:14:40', '9149.1', '9137.1', '0', null, '转入成功');
INSERT INTO `transfer` VALUES ('2509272115547640341', 'tianwei', 'mg', '12.00', 'kavin998', '2020-09-27 21:15:16', '9137.1', '9125.1', '0', null, '转入成功');
INSERT INTO `transfer` VALUES ('2509292241500691244', 'tianwei', '', '111.00', 'kavin998', '2020-09-29 22:41:49', '9125.1', '9014.1', '0', null, '转入成功');
INSERT INTO `transfer` VALUES ('2509292258459878076', 'tianwei', '', '112.00', 'kavin998', '2020-09-29 22:58:44', '9014.1', '8902.1', '0', null, '转入成功');
INSERT INTO `transfer` VALUES ('2509292312382544342', 'tianwei', '', '123.00', 'kavin998', '2020-09-29 23:12:38', '8902.1', '8779.1', '0', null, '转入成功');
INSERT INTO `transfer` VALUES ('2509292315315291093', 'tianwei', '', '11.00', 'kavin998', '2020-09-29 23:15:31', '8779.1', '8768.1', '0', null, '转入成功');
INSERT INTO `transfer` VALUES ('2509292327523501228', 'tianwei', '', '111.00', 'kavin998', '2020-09-29 23:26:37', '8768.1', '8657.1', '0', null, '转入成功');
INSERT INTO `transfer` VALUES ('2509292328044872024', 'tianwei', '', '11.00', 'kavin998', '2020-09-29 23:28:04', '8657.1', '8646.1', '0', null, '转入成功');
INSERT INTO `transfer` VALUES ('2509292330566470705', 'tianwei', '', '11.00', 'kavin998', '2020-09-29 23:30:55', '8646.1', '8635.1', '0', null, '转入成功');
INSERT INTO `transfer` VALUES ('2509292333060310819', 'tianwei', '', '2.00', 'kavin998', '2020-09-29 23:33:05', '8635.1', '8633.1', '0', null, '转入成功');
INSERT INTO `transfer` VALUES ('2509292335358151445', 'tianwei', '', '2.00', 'kavin998', '2020-09-29 23:35:34', '8633.1', '8631.1', '0', null, '转入成功');
INSERT INTO `transfer` VALUES ('2509292337365965150', 'tianwei', '', '2.00', 'kavin998', '2020-09-29 23:36:12', '8631.1', '8629.1', '0', null, '转入成功');
INSERT INTO `transfer` VALUES ('2509292343354258566', 'tianwei', '', '2.00', 'kavin998', '2020-09-29 23:43:34', '8629.1', '8627.1', '0', null, '转入成功');
INSERT INTO `transfer` VALUES ('2509292344134712107', 'tianwei', '', '2.00', 'kavin998', '2020-09-29 23:44:13', '8627.1', '8625.1', '0', null, '转入成功');
INSERT INTO `transfer` VALUES ('2509292354497067541', 'tianwei', '', '122.00', 'kavin998', '2020-09-29 23:54:48', '8625.1', '8503.1', '0', null, '转入成功');
INSERT INTO `transfer` VALUES ('2509292355329169314', '', 'tianwei', '2.00', 'kavin998', '2020-09-29 23:55:32', '8503.1', '8505.1', '0', null, '转出成功');
INSERT INTO `transfer` VALUES ('2509300043375888421', 'tianwei', 'bg', '12.00', 'kavin998', '2020-09-30 00:43:37', '7605.1', '7593.1', '0', null, '转入成功');
INSERT INTO `transfer` VALUES ('2509300044042767139', 'tianwei', 'bg', '12.00', 'kavin998', '2020-09-30 00:44:04', '7605.1', '7593.1', '0', null, '转入成功');
INSERT INTO `transfer` VALUES ('2509300045156775794', 'tianwei', 'bg', '12.00', 'kavin998', '2020-09-30 00:45:15', '7605.1', '7593.1', '0', null, '转入成功');
INSERT INTO `transfer` VALUES ('2509300049070071472', 'tianwei', 'bg', '12.00', 'kavin998', '2020-09-30 00:49:07', '7605.1', '7593.1', '0', null, '转入成功');
INSERT INTO `transfer` VALUES ('2510011405162754712', 'tianwei', '', '3334.00', 'kavin998', '2020-10-01 14:05:16', '7605.1', '4271.1', '0', null, '转入成功');
INSERT INTO `transfer` VALUES ('2510011406181995260', 'tianwei', '', '2.00', 'kavin998', '2020-10-01 14:06:18', '4271.1', '4269.1', '0', null, '转入成功');
INSERT INTO `transfer` VALUES ('2510011406289288915', 'tianwei', '', '234.00', 'kavin998', '2020-10-01 14:06:29', '4269.1', '4035.1', '0', null, '转入成功');
INSERT INTO `transfer` VALUES ('2510011406394948935', 'tianwei', '', '35.00', 'kavin998', '2020-10-01 14:06:39', '4035.1', '4000.1', '0', null, '转入成功');
INSERT INTO `transfer` VALUES ('2510011632260282327', 'tianwei', '', '222.00', 'kavin998', '2020-10-01 16:32:24', '4000.1', '3778.1', '0', null, '转入成功');
INSERT INTO `transfer` VALUES ('2510011633020690621', 'tianwei', '', '2.00', 'kavin998', '2020-10-01 16:33:01', '3778.1', '3776.1', '0', null, '转入成功');
INSERT INTO `transfer` VALUES ('2510011633101712364', 'tianwei', '', '222.00', 'kavin998', '2020-10-01 16:33:08', '3776.1', '3554.1', '0', null, '转入成功');
INSERT INTO `transfer` VALUES ('2510011633190424926', 'tianwei', '', '90.00', 'kavin998', '2020-10-01 16:33:19', '3554.1', '3464.1', '0', null, '转入成功');
INSERT INTO `transfer` VALUES ('2510032115248633230', 'tianwei', 'bbin', '222.00', 'austin998', '2020-10-03 21:15:22', '9647.12', '9425.12', '0', null, '转入成功');
INSERT INTO `transfer` VALUES ('2510032142553464306', 'bbin', 'tianwei', '12.00', 'austin998', '2020-10-03 21:42:53', '9425.12', '9437.12', '0', null, '转出成功');
INSERT INTO `transfer` VALUES ('2510041925268025625', 'tianwei', 'bbin', '432.00', 'austin998', '2020-10-04 19:25:26', '9437.12', '9005.12', '0', null, '转入成功');
INSERT INTO `transfer` VALUES ('2510050046195463692', 'tianwei', 'mg', '11.00', 'kavin998', '2020-10-05 00:46:19', '3464.1', '3453.1', '0', null, '转入成功');
INSERT INTO `transfer` VALUES ('2510051405172420496', 'tianwei', 'sba', '22.00', 'austin998', '2020-10-05 14:05:09', '9005.12', '8983.12', '0', null, '转入成功');
INSERT INTO `transfer` VALUES ('2510051405272854133', 'tianwei', 'sba', '22.00', 'austin998', '2020-10-05 14:05:27', '8983.12', '8961.12', '0', null, '转入成功');
INSERT INTO `transfer` VALUES ('2510051406498136706', 'tianwei', 'sba', '22.00', 'austin998', '2020-10-05 14:06:50', '8961.12', '8939.12', '0', null, '转入成功');
INSERT INTO `transfer` VALUES ('2510051407333084950', 'tianwei', 'sba', '22.00', 'austin998', '2020-10-05 14:07:33', '8939.12', '8917.12', '0', null, '转入成功');
INSERT INTO `transfer` VALUES ('2510051411311373061', 'tianwei', 'sba', '22.00', 'austin998', '2020-10-05 14:11:31', '8917.12', '8895.12', '0', null, '转入成功');
INSERT INTO `transfer` VALUES ('2510051412047734551', 'tianwei', 'sba', '22.00', 'austin998', '2020-10-05 14:12:05', '8895.12', '8873.12', '0', null, '转入成功');
INSERT INTO `transfer` VALUES ('2510051412280713891', 'tianwei', 'sba', '22.00', 'austin998', '2020-10-05 14:12:28', '8873.12', '8851.12', '0', null, '转入成功');
INSERT INTO `transfer` VALUES ('2510051415091828189', 'tianwei', 'sba', '22.00', 'austin998', '2020-10-05 14:15:09', '8851.12', '8829.12', '0', null, '转入成功');
INSERT INTO `transfer` VALUES ('2510051415220546793', 'tianwei', 'sba', '22.00', 'austin998', '2020-10-05 14:15:22', '8829.12', '8807.12', '0', null, '转入成功');
INSERT INTO `transfer` VALUES ('2510051415524310408', 'tianwei', 'sba', '22.00', 'austin998', '2020-10-05 14:15:52', '8807.12', '8785.12', '0', null, '转入成功');
INSERT INTO `transfer` VALUES ('2510051418385347512', 'tianwei', 'sba', '22.00', 'austin998', '2020-10-05 14:18:38', '8785.12', '8763.12', '0', null, '转入成功');
INSERT INTO `transfer` VALUES ('2510051419154124382', 'tianwei', 'sba', '22.00', 'austin998', '2020-10-05 14:19:15', '8763.12', '8741.12', '0', null, '转入成功');
INSERT INTO `transfer` VALUES ('2510051420340744864', 'tianwei', 'sba', '22.00', 'austin998', '2020-10-05 14:19:40', '8741.12', '8719.12', '0', null, '转入成功');
INSERT INTO `transfer` VALUES ('2510051422448633680', 'tianwei', 'sba', '22.00', 'austin998', '2020-10-05 14:22:45', '8719.12', '8697.12', '0', null, '转入成功');
INSERT INTO `transfer` VALUES ('2510051424027231452', 'tianwei', 'sba', '22.00', 'austin998', '2020-10-05 14:24:03', '8697.12', '8675.12', '0', null, '转入成功');
INSERT INTO `transfer` VALUES ('2510051431168752139', 'tianwei', 'sba', '22.00', 'austin998', '2020-10-05 14:31:17', '8675.12', '8653.12', '0', null, '转入成功');
INSERT INTO `transfer` VALUES ('2510051433049834247', 'tianwei', 'sba', '22.00', 'austin998', '2020-10-05 14:31:31', '8653.12', '8631.12', '0', null, '转入成功');
INSERT INTO `transfer` VALUES ('2510051508085363313', 'tianwei', 'sba', '22.00', 'austin998', '2020-10-05 15:08:08', '8631.12', '8609.12', '0', null, '转入成功');
INSERT INTO `transfer` VALUES ('2510051508223879700', 'tianwei', 'sba', '200.00', 'austin998', '2020-10-05 15:08:22', '8609.12', '8409.12', '0', null, '转入成功');
INSERT INTO `transfer` VALUES ('2510051512370429742', 'tianwei', '', '10.00', 'austin998', '2020-10-05 15:12:37', '8409.12', '8399.12', '0', null, '转入成功');
INSERT INTO `transfer` VALUES ('2510051513372869889', 'tianwei', 'mg', '10.00', 'austin998', '2020-10-05 15:13:37', '8409.12', '8399.12', '0', null, '转入成功');
INSERT INTO `transfer` VALUES ('2510051520598370979', 'tianwei', 'sba', '11.00', 'austin998', '2020-10-05 15:20:59', '8409.12', '8398.12', '0', null, '转入成功');
INSERT INTO `transfer` VALUES ('2510051534109666732', 'tianwei', 'sba', '11.00', 'kavin998', '2020-10-05 15:34:10', '3464.1', '3453.1', '0', null, '转入成功');
INSERT INTO `transfer` VALUES ('2510051534354061885', 'tianwei', 'sba', '11.00', 'kavin998', '2020-10-05 15:34:35', '3464.1', '3453.1', '0', null, '转入成功');
INSERT INTO `transfer` VALUES ('2510051538227345512', 'tianwei', 'sba', '11.00', 'kavin998', '2020-10-05 15:38:22', '3464.1', '3453.1', '0', null, '转入成功');
INSERT INTO `transfer` VALUES ('2510051541417749202', 'tianwei', 'sba', '150.00', 'kavin998', '2020-10-05 15:41:41', '3464.1', '3314.1', '0', null, '转入成功');
INSERT INTO `transfer` VALUES ('2510081833041560962', 'tianwei', 'dt', '10.00', 'austin998', '2020-10-08 18:33:04', '8409.12', '8399.12', '0', null, '转入成功');
INSERT INTO `transfer` VALUES ('2510081833139792441', 'tianwei', 'dt', '10.00', 'austin998', '2020-10-08 18:33:14', '8399.12', '8389.12', '0', null, '转入成功');
INSERT INTO `transfer` VALUES ('2510081841265400997', 'tianwei', 'dt', '11.00', 'austin998', '2020-10-08 18:41:26', '8378.12', '8367.12', '0', null, '转入成功');

-- ----------------------------
-- Table structure for `triosentered`
-- ----------------------------
DROP TABLE IF EXISTS `triosentered`;
CREATE TABLE `triosentered` (
  `id` int(11) NOT NULL,
  `username` varchar(255) NOT NULL COMMENT '用户名',
  `platform` varchar(255) NOT NULL COMMENT '游戏平台',
  `type` char(255) NOT NULL COMMENT '三重奏类型，123重奏表示对应123',
  `remark` varchar(255) DEFAULT NULL,
  `createTime` datetime NOT NULL COMMENT '三重奏报名表',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of triosentered
-- ----------------------------

-- ----------------------------
-- Table structure for `ttgtransfer`
-- ----------------------------
DROP TABLE IF EXISTS `ttgtransfer`;
CREATE TABLE `ttgtransfer` (
  `id` varchar(50) NOT NULL,
  `amount` double DEFAULT NULL,
  `createtime` datetime DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  `loginname` varchar(60) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ttgtransfer
-- ----------------------------

-- ----------------------------
-- Table structure for `t_bank_status`
-- ----------------------------
DROP TABLE IF EXISTS `t_bank_status`;
CREATE TABLE `t_bank_status` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `bankname` varchar(32) DEFAULT NULL COMMENT '银行名称',
  `status` varchar(16) DEFAULT NULL COMMENT '状态,"MAINTENANCE" 维护中 "OK" 正常',
  `create_time` datetime DEFAULT NULL COMMENT '数据的创建时间',
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '数据的更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_bank_status
-- ----------------------------
INSERT INTO `t_bank_status` VALUES ('1', '工商银行', 'OK', '2017-01-31 10:56:41', '2017-04-05 17:09:16');
INSERT INTO `t_bank_status` VALUES ('2', '招商银行', 'OK', '2017-01-31 10:56:41', '2017-04-05 17:09:17');
INSERT INTO `t_bank_status` VALUES ('3', '上海农村商业银行', 'OK', '2017-01-31 10:56:41', '2017-04-05 17:09:18');
INSERT INTO `t_bank_status` VALUES ('4', '农业银行', 'OK', '2017-01-31 10:56:41', '2017-04-05 17:09:19');
INSERT INTO `t_bank_status` VALUES ('5', '建设银行', 'OK', '2017-01-31 10:56:41', '2017-04-05 17:09:20');
INSERT INTO `t_bank_status` VALUES ('6', '交通银行', 'OK', '2017-01-31 10:56:41', '2017-04-05 17:09:20');
INSERT INTO `t_bank_status` VALUES ('7', '民生银行', 'OK', '2017-01-31 10:56:41', '2017-04-05 17:09:21');
INSERT INTO `t_bank_status` VALUES ('8', '光大银行', 'OK', '2017-01-31 10:56:41', '2017-04-05 17:09:21');
INSERT INTO `t_bank_status` VALUES ('9', '兴业银行', 'OK', '2017-01-31 10:56:41', '2017-04-05 17:09:22');
INSERT INTO `t_bank_status` VALUES ('10', '上海浦东银行', 'OK', '2017-01-31 10:56:41', '2017-04-05 17:09:23');
INSERT INTO `t_bank_status` VALUES ('11', '广东发展银行', 'OK', '2017-01-31 10:56:41', '2017-04-05 17:09:24');
INSERT INTO `t_bank_status` VALUES ('12', '深圳发展银行', 'OK', '2017-01-31 10:56:41', '2017-04-05 17:09:24');
INSERT INTO `t_bank_status` VALUES ('13', '中国银行', 'OK', '2017-01-31 10:56:41', '2017-04-05 17:09:25');
INSERT INTO `t_bank_status` VALUES ('14', '中信银行', 'OK', '2017-01-31 10:56:41', '2017-04-05 17:09:26');
INSERT INTO `t_bank_status` VALUES ('15', '邮政银行', 'OK', '2017-01-31 10:56:41', '2017-04-05 17:09:26');

-- ----------------------------
-- Table structure for `t_i18n`
-- ----------------------------
DROP TABLE IF EXISTS `t_i18n`;
CREATE TABLE `t_i18n` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `key` varchar(64) DEFAULT NULL,
  `cn_val` varchar(64) DEFAULT NULL,
  `en_val` varchar(64) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=255 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_i18n
-- ----------------------------
INSERT INTO `t_i18n` VALUES ('9', 'operation', '操作', 'operation', '2017-02-20 11:18:59');
INSERT INTO `t_i18n` VALUES ('8', 'account', '账户', 'account', '2017-02-20 11:18:35');
INSERT INTO `t_i18n` VALUES ('10', 'record', '记录', 'record', '2017-02-20 14:05:27');
INSERT INTO `t_i18n` VALUES ('11', 'report', '统计', 'report', '2017-02-20 14:05:47');
INSERT INTO `t_i18n` VALUES ('12', 'check', '结算', 'check', '2017-02-20 14:06:03');
INSERT INTO `t_i18n` VALUES ('13', 'riskControl', '风险控制', 'risk control', '2017-02-20 14:06:24');
INSERT INTO `t_i18n` VALUES ('14', 'agent', '代理', 'agent', '2017-02-20 14:06:34');
INSERT INTO `t_i18n` VALUES ('15', 'others', '其他', 'others', '2017-02-20 14:06:45');
INSERT INTO `t_i18n` VALUES ('16', 'auth', '权限管理', 'auth', '2017-02-20 14:06:58');
INSERT INTO `t_i18n` VALUES ('17', 'i18n', '国际化', 'internationalization', '2017-02-20 14:07:57');
INSERT INTO `t_i18n` VALUES ('18', 'exit', '退出', 'exit', '2017-02-20 14:09:14');
INSERT INTO `t_i18n` VALUES ('19', 'longduGlobalMgr', '龙都国际后台', 'longdu global', '2017-02-20 14:10:13');
INSERT INTO `t_i18n` VALUES ('20', 'username', '用户名', 'username', '2017-02-20 14:53:54');
INSERT INTO `t_i18n` VALUES ('21', 'password', '密码', 'password', '2017-02-20 14:54:05');
INSERT INTO `t_i18n` VALUES ('22', 'login', '登陆', 'login', '2017-02-20 14:54:14');
INSERT INTO `t_i18n` VALUES ('23', 'verifyCode', '验证码', 'verify code', '2017-02-20 14:54:42');
INSERT INTO `t_i18n` VALUES ('24', 'sendEmail', '发送邮件', 'send email', '2017-02-20 14:55:06');
INSERT INTO `t_i18n` VALUES ('25', 'emailVerifyCode', '邮箱验证码', 'email verify code', '2017-02-21 10:01:29');
INSERT INTO `t_i18n` VALUES ('26', 'coupon', '优惠券', 'coupon', '2017-02-24 11:34:54');
INSERT INTO `t_i18n` VALUES ('27', 'user', '用户', 'user', '2017-02-24 11:46:39');
INSERT INTO `t_i18n` VALUES ('28', 'role', '角色', 'role', '2017-02-24 11:46:48');
INSERT INTO `t_i18n` VALUES ('29', 'permission', '权限', 'permission', '2017-02-24 11:47:01');
INSERT INTO `t_i18n` VALUES ('30', 'i18nConfig', '国际化翻译', 'i18n config', '2017-02-24 13:04:21');
INSERT INTO `t_i18n` VALUES ('65', 'paySwitch', '支付开关', 'payment switch', '2017-03-02 10:29:15');
INSERT INTO `t_i18n` VALUES ('64', 'remoteBankinfo', '远程银行账户', 'remote bank account', '2017-03-02 10:28:36');
INSERT INTO `t_i18n` VALUES ('63', 'manageBankinfo', '管理银行账户', 'manage bank account', '2017-03-02 10:28:00');
INSERT INTO `t_i18n` VALUES ('62', 'addBankinfo', '增加银行账户', 'add bank account', '2017-03-02 10:27:26');
INSERT INTO `t_i18n` VALUES ('61', 'alipayAccount', '玩家支付宝存款账号', 'alipay account', '2017-03-02 10:26:40');
INSERT INTO `t_i18n` VALUES ('60', 'unbindCard', '卡折号解绑', 'bank card unbinding', '2017-03-02 10:25:52');
INSERT INTO `t_i18n` VALUES ('59', 'syncMember', '同步会员信息', 'sync member info', '2017-03-02 10:25:12');
INSERT INTO `t_i18n` VALUES ('58', 'flashWithdrawRestriction', '秒提限制', 'flash withdraw restriction', '2017-03-02 10:24:36');
INSERT INTO `t_i18n` VALUES ('57', 'modifyUser', '修改会员基本资料', 'modify user', '2017-03-02 10:23:55');
INSERT INTO `t_i18n` VALUES ('56', 'resetPassword', '重设密码', 'reset password', '2017-03-02 10:23:03');
INSERT INTO `t_i18n` VALUES ('55', 'userBackup', '备份用户列表', 'user backup', '2017-03-02 10:20:23');
INSERT INTO `t_i18n` VALUES ('66', 'payMerchantManagement', '支付商户管理', 'manage payment merchant', '2017-03-02 10:30:17');
INSERT INTO `t_i18n` VALUES ('67', 'sysParamConfig', '系统参数配置', 'config system param', '2017-03-02 10:30:57');
INSERT INTO `t_i18n` VALUES ('68', 'smsParamConfig', '短信参数配置', 'config sms param', '2017-03-02 10:31:23');
INSERT INTO `t_i18n` VALUES ('69', 'bankStatusConfig', '提款银行状态配置', 'config withdraw bank status', '2017-03-02 10:31:52');
INSERT INTO `t_i18n` VALUES ('70', 'cashin', '存款', 'deposit', '2017-03-02 10:33:05');
INSERT INTO `t_i18n` VALUES ('72', 'rmbProposal', '人民币事务提案', 'RMB transaction proposal', '2017-03-02 10:35:05');
INSERT INTO `t_i18n` VALUES ('73', 'pesoProposal', 'PESO事务提案', 'PESO transaction proposal', '2017-03-02 10:36:31');
INSERT INTO `t_i18n` VALUES ('74', 'batchXima', '批量洗码', 'Batch washing yards', '2017-03-02 10:37:33');
INSERT INTO `t_i18n` VALUES ('75', 'losePayback', '负盈利反赠', 'lose gift', '2017-03-02 10:48:23');
INSERT INTO `t_i18n` VALUES ('76', 'weekPayback', '周周回馈', 'weekly gift', '2017-03-02 10:49:05');
INSERT INTO `t_i18n` VALUES ('77', 'luckLottery', '幸运抽奖', 'lucky lottery', '2017-03-02 10:50:12');
INSERT INTO `t_i18n` VALUES ('78', 'batchPromotion', '优惠批量派发', 'back promotion', '2017-03-02 10:50:51');
INSERT INTO `t_i18n` VALUES ('79', 'birthdayGift', '生日礼金', 'birthday gift', '2017-03-02 10:51:33');
INSERT INTO `t_i18n` VALUES ('80', 'redPacket', '红包优惠', 'red packet', '2017-03-02 10:52:08');
INSERT INTO `t_i18n` VALUES ('81', 'ptCoupon', 'PT优惠券', 'pt coupon', '2017-03-02 10:52:37');
INSERT INTO `t_i18n` VALUES ('82', 'otherCoupon', 'TTG/NT等其它优惠劵', 'other coupon', '2017-03-02 10:53:06');
INSERT INTO `t_i18n` VALUES ('83', 'levelGift', '晋级礼金', 'level gift', '2017-03-02 10:53:33');
INSERT INTO `t_i18n` VALUES ('241', 'quickPayOrderRecord', '秒存订单记录', 'quick pay order record', '2017-03-28 20:27:32');
INSERT INTO `t_i18n` VALUES ('239', 'payPlatformConfigManager', '支付平台配置专员', 'pay platform config manager', '2017-03-28 10:35:29');
INSERT INTO `t_i18n` VALUES ('237', 'payPlatformManage', '支付平台配置管理', 'pay platform manage', '2017-03-28 10:29:11');
INSERT INTO `t_i18n` VALUES ('235', 'bankTransfer(new)', '银行转账(new)', 'bank transfer', '2017-03-28 09:15:56');
INSERT INTO `t_i18n` VALUES ('91', 'repairHlOrder', '额度补单', 'repair pay order of HL', '2017-03-02 11:41:39');
INSERT INTO `t_i18n` VALUES ('92', 'manualModifyAmount', '额度增减', 'manual modify amount', '2017-03-02 11:46:34');
INSERT INTO `t_i18n` VALUES ('93', 'manualModifyCredit', '手工增减银行额度', 'manual modify credit', '2017-03-02 11:50:13');
INSERT INTO `t_i18n` VALUES ('94', 'bankTransfer', '银行转账', 'bank transfer', '2017-03-02 11:51:26');
INSERT INTO `t_i18n` VALUES ('95', 'selfGiftConfig', '自助优惠配置', 'self service gift config', '2017-03-02 13:14:45');
INSERT INTO `t_i18n` VALUES ('96', 'userLevelConfig', '会员升降级', 'user level config', '2017-03-02 13:15:28');
INSERT INTO `t_i18n` VALUES ('97', 'ptBigbang', 'PT幸运转', 'pt bigbang', '2017-03-02 13:35:19');
INSERT INTO `t_i18n` VALUES ('98', 'clearUpData', '自动清理数据', 'self-service cleanup data', '2017-03-02 13:36:06');
INSERT INTO `t_i18n` VALUES ('99', 'urgeOrder', '催账记录', 'urge order record', '2017-03-02 13:36:28');
INSERT INTO `t_i18n` VALUES ('100', 'friendRecommendationGift', '好友推荐礼金', 'user recomendation gift', '2017-03-02 13:37:17');
INSERT INTO `t_i18n` VALUES ('101', 'goddessRecord', '守护女神', 'guardian goddess record', '2017-03-02 13:38:46');
INSERT INTO `t_i18n` VALUES ('102', 'singleParty', '单身派对', 'single party', '2017-03-02 13:39:16');
INSERT INTO `t_i18n` VALUES ('103', 'modifyPlatformRecord', '修改返水数据', 'modify platform record', '2017-03-02 13:41:20');
INSERT INTO `t_i18n` VALUES ('104', 'dataImportBL', '比邻导入数据', 'data import of BL', '2017-03-02 13:41:57');
INSERT INTO `t_i18n` VALUES ('105', 'gameCodeManage', '游戏管理', 'game code manage', '2017-03-02 13:42:47');
INSERT INTO `t_i18n` VALUES ('106', 'distributeMobileSignPrize', '手机连续签到抽奖活动配发确认', 'distribute mobile sign prize', '2017-03-02 13:43:16');
INSERT INTO `t_i18n` VALUES ('107', 'mobileSignOddsConfig', '手机连续签到奖品几率管理', 'config mobile sign odds', '2017-03-02 13:43:42');
INSERT INTO `t_i18n` VALUES ('108', 'onlinePay', '在线支付', 'online payment', '2017-03-02 13:47:44');
INSERT INTO `t_i18n` VALUES ('109', 'onlinePay', '在线支付', 'online payment', '2017-03-02 14:20:49');
INSERT INTO `t_i18n` VALUES ('110', 'quotaInteruptionRecord', '额度掉单记录', 'quota interuption record', '2017-03-02 14:21:18');
INSERT INTO `t_i18n` VALUES ('111', 'quotaModifyRecord', '额度增减记录', 'quota modify record', '2017-03-02 14:21:42');
INSERT INTO `t_i18n` VALUES ('112', 'proposalRecord', '提案记录', 'proposal record', '2017-03-02 14:22:04');
INSERT INTO `t_i18n` VALUES ('113', 'RMBProposalRecord', '人民币事务提案记录', 'RMB proposal record', '2017-03-02 14:22:32');
INSERT INTO `t_i18n` VALUES ('114', 'PESOProposalRecord', 'PESO事务提案记录', 'PESO proposal record', '2017-03-02 14:22:58');
INSERT INTO `t_i18n` VALUES ('115', 'bankTransferProposal', '银行转账提案', 'bank transfer proposal', '2017-03-02 14:23:34');
INSERT INTO `t_i18n` VALUES ('116', 'ICBCCashinRecord', '工行网银存款记录', 'ICBC cashin record', '2017-03-02 14:24:01');
INSERT INTO `t_i18n` VALUES ('117', 'ABCCashinRecord', '农行网银存款记录', 'ABC cashin record', '2017-03-02 14:24:31');
INSERT INTO `t_i18n` VALUES ('118', 'CCBAndCMBCashinRecord', '建行(加急)/招行网银存款记录', 'CCB(urgent)/CMB cashin record', '2017-03-02 14:25:11');
INSERT INTO `t_i18n` VALUES ('119', 'AlipayCashinRecord', '支付宝存款记录', 'alipay cashin record', '2017-03-02 14:25:34');
INSERT INTO `t_i18n` VALUES ('120', 'wechatCashinRecord', '微信验证存款记录', 'wechat cashin record', '2017-03-02 14:25:56');
INSERT INTO `t_i18n` VALUES ('121', 'quotaValidateRecord', '额度验证订单记录', 'quota validate order record', '2017-03-02 14:26:50');
INSERT INTO `t_i18n` VALUES ('122', 'quotaRecord', '额度记录', 'quota record', '2017-03-02 14:27:16');
INSERT INTO `t_i18n` VALUES ('123', 'bankQuotaRecord', '银行额度记录', 'bank quota record', '2017-03-02 14:27:45');
INSERT INTO `t_i18n` VALUES ('124', 'localTransferRecord', '本地转账记录', 'local transfer record', '2017-03-02 14:28:21');
INSERT INTO `t_i18n` VALUES ('125', 'memberActionRecord', '会员事件记录', 'member action record', '2017-03-02 14:28:55');
INSERT INTO `t_i18n` VALUES ('126', 'officeActionRecord', '后台事件记录', 'office action record', '2017-03-02 14:29:35');
INSERT INTO `t_i18n` VALUES ('127', 'commissionRecord', '佣金记录查询', 'commission record', '2017-03-02 14:30:17');
INSERT INTO `t_i18n` VALUES ('128', 'dailyCommissionRecord', '日结佣金记录查询', 'daily commission record', '2017-03-02 14:30:38');
INSERT INTO `t_i18n` VALUES ('129', 'firstCashinRecord', '首存会员记录', 'first cashin record', '2017-03-02 14:31:18');
INSERT INTO `t_i18n` VALUES ('130', 'AGProfitRecord', '游戏平台输赢记录', 'AG profit record', '2017-03-02 14:32:02');
INSERT INTO `t_i18n` VALUES ('131', 'NTProfitRecord', 'NT平台游戏记录', 'NT profit record', '2017-03-02 14:32:25');
INSERT INTO `t_i18n` VALUES ('132', 'PTProfitRecord', 'NEWPT平台数据', 'PT profit record', '2017-03-02 14:32:50');
INSERT INTO `t_i18n` VALUES ('133', 'newPTProfitRecord', '最新PT平台数据', 'new PT profit record', '2017-03-02 14:33:12');
INSERT INTO `t_i18n` VALUES ('134', 'JCProfitRecord', 'JC平台输赢数据', 'JC profit record', '2017-03-02 14:33:33');
INSERT INTO `t_i18n` VALUES ('135', 'salePhoneRecord', '电销专员电话记录', 'sale phone record', '2017-03-02 14:34:14');
INSERT INTO `t_i18n` VALUES ('136', 'batchTextRecord', '电销专员电话记录(群发邮件短信)', 'batch sms/email record', '2017-03-02 14:34:41');
INSERT INTO `t_i18n` VALUES ('137', 'depositOrderRecord', '存款订单记录', 'deposit order record', '2017-03-02 14:35:14');
INSERT INTO `t_i18n` VALUES ('138', 'userSecretSecurityRecord', '玩家密保记录', 'user secret security record', '2017-03-02 14:35:43');
INSERT INTO `t_i18n` VALUES ('139', 'prizeDeliveryRecord', '活动礼品寄送', 'prize delivery record', '2017-03-02 14:36:08');
INSERT INTO `t_i18n` VALUES ('140', 'slotMachineActivityRecord', '老虎机流水活动查询', 'slot machine activity record', '2017-03-02 14:37:37');
INSERT INTO `t_i18n` VALUES ('141', 'shakeTaskRecord', '玩家摇摇乐任务记录', 'shake task record', '2017-03-02 14:37:59');
INSERT INTO `t_i18n` VALUES ('142', 'userRecommendationRecord', '玩家推荐好友查询', 'user recommendation record', '2017-03-02 14:38:20');
INSERT INTO `t_i18n` VALUES ('143', 'pointRecord', '玩家积分记录', 'point record', '2017-03-02 14:38:35');
INSERT INTO `t_i18n` VALUES ('144', 'breakoutRecord', '玩家闯关活动', 'breakout activity record', '2017-03-02 14:38:57');
INSERT INTO `t_i18n` VALUES ('145', 'signGiftRecord', '玩家签到礼金', 'sign gift record', '2017-03-02 14:39:22');
INSERT INTO `t_i18n` VALUES ('146', 'MGProfitRecord', 'MG输赢统计', 'MG profit record', '2017-03-02 14:39:44');
INSERT INTO `t_i18n` VALUES ('147', 'MGTransferRecord', 'MG转账记录', 'MG transfer record', '2017-03-02 14:40:03');
INSERT INTO `t_i18n` VALUES ('148', 'MGExceptionRecord', 'MG异常数据', 'MG exception record', '2017-03-02 14:40:27');
INSERT INTO `t_i18n` VALUES ('149', 'MGProfitToKPI', 'MG输赢数据导入KPI后台', 'MG profit to KPI office', '2017-03-02 14:40:54');
INSERT INTO `t_i18n` VALUES ('150', 'activityOrderRecord', '活动订单记录', 'activity order record', '2017-03-02 14:41:18');
INSERT INTO `t_i18n` VALUES ('151', 'profitReport', '盈利报表', 'profit report', '2017-03-02 14:46:49');
INSERT INTO `t_i18n` VALUES ('152', 'howToKnowStatistics', '来源网址统计', 'source URL statistics', '2017-03-02 14:47:09');
INSERT INTO `t_i18n` VALUES ('153', 'activityStatistics', '活动流水统计', 'activity statistics', '2017-03-02 14:47:59');
INSERT INTO `t_i18n` VALUES ('154', 'bankAccountCashInOutStatistics', '银行进出帐统计', 'bank account transfer record', '2017-03-02 14:51:19');
INSERT INTO `t_i18n` VALUES ('155', 'systemReturnProfit', '系统洗码', 'system return profit', '2017-03-02 15:06:48');
INSERT INTO `t_i18n` VALUES ('156', 'winLoseAnalysis', '输赢信息分析', 'win&lose analysis', '2017-03-02 15:11:59');
INSERT INTO `t_i18n` VALUES ('157', 'winPointRank', '赢点排名分析', 'win point rank', '2017-03-02 15:12:33');
INSERT INTO `t_i18n` VALUES ('158', 'platformData', '平台信息分析', 'platform data analysis', '2017-03-02 15:13:12');
INSERT INTO `t_i18n` VALUES ('159', 'preferentialBetRecord', '自助存送投注记录', 'preferential bet record', '2017-03-02 15:13:36');
INSERT INTO `t_i18n` VALUES ('160', 'preferentialTransferRecord', '自助存送转账记录', 'preferential transfer record', '2017-03-02 15:14:00');
INSERT INTO `t_i18n` VALUES ('161', 'modifyAgentInfo', '修改代理基本资料', 'modify agent info', '2017-03-02 15:22:33');
INSERT INTO `t_i18n` VALUES ('162', 'new agent', '新增代理', 'new agent', '2017-03-02 15:22:49');
INSERT INTO `t_i18n` VALUES ('163', 'agentCreditAdvance', '代理信用资金预支', 'agent credit advance', '2017-03-02 15:23:10');
INSERT INTO `t_i18n` VALUES ('164', 'agentUnbindUserAccount', '代理账号解绑游戏账号', 'agent unbind user account', '2017-03-02 15:23:30');
INSERT INTO `t_i18n` VALUES ('165', 'vipAgent', '代理VIP', 'vip agent', '2017-03-02 15:23:55');
INSERT INTO `t_i18n` VALUES ('166', 'innerAgentManage', '内部代理管理', 'intenal agent manage', '2017-03-02 15:25:03');
INSERT INTO `t_i18n` VALUES ('167', 'agentPhoneRecord', '代理专员电话记录', 'agent phone record', '2017-03-02 15:25:23');
INSERT INTO `t_i18n` VALUES ('168', 'agentWebHostsRecord', '代理域名记录', 'agent web hosts record', '2017-03-02 15:25:43');
INSERT INTO `t_i18n` VALUES ('169', 'agentReferralsStatistic', '代理下线统计', 'agent referral statistic', '2017-03-02 15:26:07');
INSERT INTO `t_i18n` VALUES ('170', 'agentCommission', '代理佣金结算', 'agent commission', '2017-03-02 15:26:38');
INSERT INTO `t_i18n` VALUES ('171', 'agentDataAnalysis', '代理数据分析', 'agent data analysis', '2017-03-02 15:26:55');
INSERT INTO `t_i18n` VALUES ('172', 'newAgentDataAnalysis', '代理数据分析（新）', 'agent data analysis(new)', '2017-03-02 15:27:22');
INSERT INTO `t_i18n` VALUES ('173', 'newAgent', '新增代理', 'new agent', '2017-03-02 15:28:11');
INSERT INTO `t_i18n` VALUES ('174', 'activityCalendar', '活动日历', 'activity calendar', '2017-03-02 15:37:03');
INSERT INTO `t_i18n` VALUES ('175', 'announcementManage', '公告管理', 'announcement manage', '2017-03-02 15:37:26');
INSERT INTO `t_i18n` VALUES ('176', 'publishAnnouncement', '发布公告', 'publish announcement', '2017-03-02 15:37:57');
INSERT INTO `t_i18n` VALUES ('177', 'gustbook', '站内信', 'gustbook', '2017-03-02 15:38:16');
INSERT INTO `t_i18n` VALUES ('178', 'smsPlatform', '短信平台', 'sms platform', '2017-03-02 15:38:38');
INSERT INTO `t_i18n` VALUES ('179', 'emailPlatform', '最新邮件平台', 'email platform', '2017-03-02 15:38:57');
INSERT INTO `t_i18n` VALUES ('180', 'batchSentEmailToUser', '已注册客户群发邮件平台', 'batch sent email to user', '2017-03-02 15:39:23');
INSERT INTO `t_i18n` VALUES ('181', 'batchSentEmailToOthers', '外部名单群发邮件平台', 'batch sent email to pthers', '2017-03-02 15:39:50');
INSERT INTO `t_i18n` VALUES ('182', 'checkMailForm', '查询信箱格式', 'check mail form', '2017-03-02 15:40:14');
INSERT INTO `t_i18n` VALUES ('183', 'emailSubscriptionList', '订阅邮件客户', 'email subscription list', '2017-03-02 15:40:34');
INSERT INTO `t_i18n` VALUES ('184', 'modifyMyPassword', '修改密码', 'modify my password', '2017-03-02 15:41:06');
INSERT INTO `t_i18n` VALUES ('185', 'unlockOperator', '解除禁用', 'unlock operator', '2017-03-02 15:41:26');
INSERT INTO `t_i18n` VALUES ('186', 'createSubOperator', '创建下级管理员', 'create sub operator', '2017-03-02 15:41:44');
INSERT INTO `t_i18n` VALUES ('187', 'employeeManagement', '员工管理(新)', 'employee management', '2017-03-02 15:42:03');
INSERT INTO `t_i18n` VALUES ('188', 'bindEmployeeNo', '绑定员工编号', 'bind employee no', '2017-03-02 15:42:27');
INSERT INTO `t_i18n` VALUES ('189', 'resetOperatorPassword', '重置后台密码', 'reset operator password', '2017-03-02 15:42:54');
INSERT INTO `t_i18n` VALUES ('190', 'userList', '用户列表', 'user list', '2017-03-03 17:50:19');
INSERT INTO `t_i18n` VALUES ('191', 'previousStep', '上一步', 'previous step', '2017-03-03 17:55:09');
INSERT INTO `t_i18n` VALUES ('192', 'userType', '用户类型', 'user type', '2017-03-03 17:55:34');
INSERT INTO `t_i18n` VALUES ('193', 'userLevel', '会员等级', 'user level', '2017-03-03 17:56:38');
INSERT INTO `t_i18n` VALUES ('194', 'startTime', '开始时间', 'start time', '2017-03-03 17:57:37');
INSERT INTO `t_i18n` VALUES ('195', 'endTime', '结束时间', 'end time', '2017-03-03 17:58:21');
INSERT INTO `t_i18n` VALUES ('196', 'superiorAgent', '上级代理', 'superior agent', '2017-03-03 18:01:24');
INSERT INTO `t_i18n` VALUES ('197', 'agentWebsite', '代理网址', 'agent website', '2017-03-03 18:09:57');
INSERT INTO `t_i18n` VALUES ('198', 'realName', '真实姓名', 'real name', '2017-03-03 18:10:15');
INSERT INTO `t_i18n` VALUES ('199', 'email', '邮箱', 'email', '2017-03-03 18:10:31');
INSERT INTO `t_i18n` VALUES ('200', 'phone', '电话', 'phone', '2017-03-03 18:10:46');
INSERT INTO `t_i18n` VALUES ('201', 'memberStatus', '会员状态', 'member status', '2017-03-03 18:11:02');
INSERT INTO `t_i18n` VALUES ('202', 'birthday', '生日', 'birthday', '2017-03-03 18:11:17');
INSERT INTO `t_i18n` VALUES ('203', 'userAccountNotice', '[当你输入了会员帐号，时间不再起效]', '[when you enter the user ID，time is no longer onset]', '2017-03-06 16:41:17');
INSERT INTO `t_i18n` VALUES ('204', 'whetherDeposit', '是否存款', 'whether deposit', '2017-03-06 16:42:21');
INSERT INTO `t_i18n` VALUES ('205', 'opening', '开户', 'opening', '2017-03-06 16:43:48');
INSERT INTO `t_i18n` VALUES ('206', 'sourceUrl', '来源网址', 'source url', '2017-03-06 16:45:42');
INSERT INTO `t_i18n` VALUES ('207', 'eachPage', '每页', 'each page', '2017-03-06 16:47:08');
INSERT INTO `t_i18n` VALUES ('208', 'warnLevel', '警告等级', 'warn level', '2017-03-06 16:47:56');
INSERT INTO `t_i18n` VALUES ('209', 'phoneStatus', '电话状态', 'phone status', '2017-03-06 16:48:44');
INSERT INTO `t_i18n` VALUES ('212', 'recommendationNumber', '推荐码', 'recommendation number', '2017-03-06 16:55:11');
INSERT INTO `t_i18n` VALUES ('211', 'invitationNumber', '邀请码', 'invitation number', '2017-03-06 16:53:39');
INSERT INTO `t_i18n` VALUES ('213', 'ABCRandomNumber', '农行随机码', 'ABC random number', '2017-03-06 16:58:23');
INSERT INTO `t_i18n` VALUES ('214', 'agentRecommendationNumber', '代理推荐码', 'agent recommendation number', '2017-03-06 17:00:13');
INSERT INTO `t_i18n` VALUES ('215', 'lastLoginIp', '上次登录ip', 'last login ip', '2017-03-06 17:01:09');
INSERT INTO `t_i18n` VALUES ('216', 'breakPeriod', '未游戏周期', 'break period', '2017-03-06 17:07:17');
INSERT INTO `t_i18n` VALUES ('217', 'clientOS', '客户操作系统', 'client OS', '2017-03-06 17:09:34');
INSERT INTO `t_i18n` VALUES ('218', 'longduAmount', '龙都额度', 'longdu amount', '2017-03-06 17:20:44');
INSERT INTO `t_i18n` VALUES ('219', 'platformAmount', '平台额度', 'platform amount', '2017-03-06 17:21:41');
INSERT INTO `t_i18n` VALUES ('220', 'type', '类型', 'type', '2017-03-06 17:22:16');
INSERT INTO `t_i18n` VALUES ('221', 'openingTime', '开户时间', 'opening time', '2017-03-06 17:24:18');
INSERT INTO `t_i18n` VALUES ('222', 'openingIp', '开户IP', 'opening ip', '2017-03-06 17:25:04');
INSERT INTO `t_i18n` VALUES ('223', 'openingArea', '开户地区', 'opening area', '2017-03-06 17:26:26');
INSERT INTO `t_i18n` VALUES ('224', 'lastLoginTime', '上次登陆时间', 'last login time', '2017-03-06 17:27:04');
INSERT INTO `t_i18n` VALUES ('225', 'lastLoginArea', '上次登陆地区', 'last login area', '2017-03-06 17:29:35');
INSERT INTO `t_i18n` VALUES ('226', 'loginTimes', '登陆次数', 'login times', '2017-03-06 17:30:23');
INSERT INTO `t_i18n` VALUES ('227', 'status', '状态', 'status', '2017-03-06 17:31:08');
INSERT INTO `t_i18n` VALUES ('228', 'dailyAmount', '日限额', 'daily amount', '2017-03-06 17:32:34');
INSERT INTO `t_i18n` VALUES ('229', 'comment', '备注', 'comment', '2017-03-06 17:34:16');
INSERT INTO `t_i18n` VALUES ('230', 'updateCSIDNumber', '更新客服标识码', 'update CS ID number', '2017-03-06 17:50:40');
INSERT INTO `t_i18n` VALUES ('231', 'batchCall', '群呼', 'batch call', '2017-03-06 17:52:20');
INSERT INTO `t_i18n` VALUES ('232', 'query', '查询', 'query', '2017-03-06 17:56:01');
INSERT INTO `t_i18n` VALUES ('233', 'repairPayOrder', '在线支付补单', 'repaire pay order', '2017-03-27 18:17:25');
INSERT INTO `t_i18n` VALUES ('234', 'latestPreferential', '最新优惠配置', 'latest preferential config', '2017-05-18 09:35:21');
INSERT INTO `t_i18n` VALUES ('236', 'userStatus', '用户优惠评论权限', 'user preferential comment permission', '2017-05-18 09:36:44');
INSERT INTO `t_i18n` VALUES ('242', 'userDeviceInfo', '用户终端设备信息', 'user device info', '2017-05-18 09:40:02');
INSERT INTO `t_i18n` VALUES ('243', 'specialTopic', '活动专题配置', 'special topic', '2017-05-18 09:41:13');
INSERT INTO `t_i18n` VALUES ('244', 'preferentialComment', '最细优惠评论记录', 'preferential comment', '2017-05-18 09:41:54');
INSERT INTO `t_i18n` VALUES ('245', 'latestWinInfo', '中奖信息配置', 'latest win info', '2017-05-18 09:42:34');
INSERT INTO `t_i18n` VALUES ('246', 'getMobileCode', '获取手机验证码', 'get phone code', '2017-05-23 17:35:57');
INSERT INTO `t_i18n` VALUES ('247', 'mobilelVerifyCode', '手机验证码', 'phone verification code', '2017-05-23 17:36:14');
INSERT INTO `t_i18n` VALUES ('248', 'dataCollection', '数据采集', 'dataCollection', '2018-12-10 01:09:23');
INSERT INTO `t_i18n` VALUES ('249', 'ptDataCollection', 'PT洗码数据采集', 'ptDataCollection', '2018-12-10 01:20:09');
INSERT INTO `t_i18n` VALUES ('250', 'sbaDataCollection', '沙巴体育洗码数据采集', 'sbaDataCollection', '2018-12-10 01:20:09');
INSERT INTO `t_i18n` VALUES ('251', '761DataCollection', '棋乐游洗码数据采集', '761DataCollection', '2018-12-10 01:20:09');
INSERT INTO `t_i18n` VALUES ('252', 'MGDataCollection', 'MG洗码数据采集', 'MGDataCollection', '2018-12-10 01:20:09');
INSERT INTO `t_i18n` VALUES ('253', 'SWDataCollection', 'SW洗码数据采集', 'SWDataCollection', '2018-12-10 01:20:09');
INSERT INTO `t_i18n` VALUES ('254', 'AGDataCollection', 'AG洗码数据采集', 'AGDataCollection', '2018-12-10 01:20:09');

-- ----------------------------
-- Table structure for `t_permission`
-- ----------------------------
DROP TABLE IF EXISTS `t_permission`;
CREATE TABLE `t_permission` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `pid` int(11) DEFAULT NULL COMMENT '父ID',
  `name` varchar(32) DEFAULT NULL COMMENT '名称',
  `type` char(1) DEFAULT NULL COMMENT '类似,"M",主菜单,"S":二级菜单,"O",操作',
  `url` varchar(255) DEFAULT NULL COMMENT '菜单跳转的url地址',
  `icon` varchar(32) DEFAULT NULL COMMENT '图标',
  `description` varchar(128) DEFAULT NULL COMMENT '注释',
  `i18n_key` varchar(32) DEFAULT NULL,
  `code` varchar(32) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=356 DEFAULT CHARSET=utf8 COMMENT='权限表,可以是某一个菜单,也可以是页面中的某一个区域或者按钮';

-- ----------------------------
-- Records of t_permission
-- ----------------------------
INSERT INTO `t_permission` VALUES ('1', '0', '账户', 'M', null, 'fa fa-user fa-lg', null, 'account', null, '2017-02-10 13:49:33');
INSERT INTO `t_permission` VALUES ('2', '1', '用户列表', 'S', '/office/functions/user.jsp', null, null, 'user', null, '2017-02-10 13:49:33');
INSERT INTO `t_permission` VALUES ('3', '1', '备份用户列表', 'S', '/office/functions/user_backup.jsp', null, null, 'userBackup', null, '2017-02-10 13:49:33');
INSERT INTO `t_permission` VALUES ('4', '1', '重设密码', 'S', '/office/functions/reset_password.jsp', null, null, 'resetPassword', null, '2017-02-10 13:49:33');
INSERT INTO `t_permission` VALUES ('5', '1', '修改会员基本资料', 'S', '/office/functions/modifyCustomerInfo.jsp', null, null, 'modifyUser', null, '2017-02-10 13:49:33');
INSERT INTO `t_permission` VALUES ('6', '1', '秒提限制', 'S', '/office/functions/modifyCustomerMailFlag.jsp', null, null, 'flashWithdrawRestriction', null, '2017-02-10 13:49:33');
INSERT INTO `t_permission` VALUES ('7', '1', '同步会员信息', 'S', '/office/functions/synmemberinfotobbs.jsp', null, null, 'syncMember', null, '2017-02-10 13:49:33');
INSERT INTO `t_permission` VALUES ('8', '1', '卡折号解绑', 'S', '/office/functions/userbankinfounbanding.jsp', null, null, 'unbindCard', null, '2017-02-10 13:49:33');
INSERT INTO `t_permission` VALUES ('9', '1', '玩家支付宝存款账号', 'S', '/office/functions/userAlipayAccount.jsp', null, null, 'alipayAccount', null, '2017-02-10 13:49:33');
INSERT INTO `t_permission` VALUES ('10', '1', '增加银行账户', 'S', '/office/functions/bankinfo/addbankinfo.jsp', null, null, 'addBankinfo', null, '2017-02-10 13:49:33');
INSERT INTO `t_permission` VALUES ('11', '1', '管理银行账户', 'S', '/office/functions/bankinfo/managebankinfo.jsp', null, null, 'manageBankinfo', null, '2017-02-10 13:49:33');
INSERT INTO `t_permission` VALUES ('12', '1', '远程银行账户', 'S', '/office/functions/bankinfo/bankinfostatus.jsp', null, null, 'remoteBankinfo', null, '2017-02-10 13:49:33');
INSERT INTO `t_permission` VALUES ('13', '1', '支付开关', 'S', '/bankinfo/managemsbankswitch.do', null, null, 'paySwitch', null, '2017-02-10 13:49:33');
INSERT INTO `t_permission` VALUES ('14', '1', '支付商户管理', 'S', '/bankinfo/paymerchant.do', null, null, 'payMerchantManagement', null, '2017-02-10 13:49:33');
INSERT INTO `t_permission` VALUES ('15', '1', '系统参数配置', 'S', '/bankinfo/querySystemConfig.do', null, null, 'sysParamConfig', null, '2017-02-10 13:49:33');
INSERT INTO `t_permission` VALUES ('16', '1', '短信参数配置', 'S', '/office/querySMSConfig.do', null, null, 'smsParamConfig', null, '2017-02-10 13:49:33');
INSERT INTO `t_permission` VALUES ('17', '1', '提款银行状态配置', 'S', '/bankinfo/getBankStatusList.do', null, null, 'bankStatusConfig', null, '2017-02-10 13:49:33');
INSERT INTO `t_permission` VALUES ('18', '0', '操作', 'M', null, 'fa fa-wrench fa-lg', null, 'operation', null, '2017-02-10 13:49:33');
INSERT INTO `t_permission` VALUES ('19', '18', '存款', 'S', '/office/functions/cashin.jsp', null, null, 'cashin', null, '2017-02-10 13:49:33');
INSERT INTO `t_permission` VALUES ('20', '18', '人民币事务提案', 'S', '/office/functions/shiwu.jsp', null, null, 'rmbProposal', null, '2017-02-10 13:49:33');
INSERT INTO `t_permission` VALUES ('21', '18', 'PESO事务提案', 'S', '/office/functions/shiwupeso.jsp', null, null, 'pesoProposal', null, '2017-02-10 13:49:33');
INSERT INTO `t_permission` VALUES ('22', '18', '批量洗码', 'S', '/office/functions/xima/xima.jsp', null, null, 'batchXima', null, '2017-02-10 13:49:33');
INSERT INTO `t_permission` VALUES ('23', '18', '负盈利反赠', 'S', '/office/functions/xima/profit.jsp', null, null, 'losePayback', null, '2017-02-10 13:49:33');
INSERT INTO `t_permission` VALUES ('24', '18', '周周回馈', 'S', '/office/functions/xima/weekSent.jsp', null, null, 'weekPayback', null, '2017-02-10 13:49:33');
INSERT INTO `t_permission` VALUES ('25', '18', '幸运抽奖', 'S', '/office/functions/prize.jsp', null, null, 'luckLottery', null, '2017-02-10 13:49:33');
INSERT INTO `t_permission` VALUES ('26', '18', '优惠批量派发', 'S', '/office/functions/privilege.jsp', null, null, 'batchPromotion', null, '2017-02-10 13:49:33');
INSERT INTO `t_permission` VALUES ('27', '18', '生日礼金', 'S', '/office/functions/birthdayGifts.jsp', null, null, 'birthdayGift', null, '2017-02-10 13:49:33');
INSERT INTO `t_permission` VALUES ('28', '18', '红包优惠', 'S', '/office/functions/redCoupon.jsp', null, null, 'redPacket', null, '2017-02-10 13:49:33');
INSERT INTO `t_permission` VALUES ('29', '18', '优惠券', 'S', '/office/functions/coupon.jsp', null, null, 'coupon', null, '2017-02-10 13:49:33');
INSERT INTO `t_permission` VALUES ('30', '18', 'PT优惠券', 'S', '/office/functions/ptCoupon.jsp', null, null, 'ptCoupon', null, '2017-02-10 13:49:33');
INSERT INTO `t_permission` VALUES ('31', '18', 'TTG/NT等其它优惠劵', 'S', '/office/functions/ttgCoupon.jsp', null, null, 'otherCoupon', null, '2017-02-10 13:49:33');
INSERT INTO `t_permission` VALUES ('32', '18', '晋级礼金', 'S', '/office/functions/levelprize.jsp', null, null, 'levelGift', null, '2017-02-10 13:49:33');
INSERT INTO `t_permission` VALUES ('317', '57', '秒存订单记录', 'S', '/office/functions/depositAmountOrder.jsp', '', '', 'quickPayOrderRecord', '', '2017-03-28 20:25:48');
INSERT INTO `t_permission` VALUES ('315', '1', '支付平台配置专员', 'S', '/bankinfo/merchantpay_finance.do', '', '', 'payPlatformConfigManager', '', '2017-03-28 10:31:23');
INSERT INTO `t_permission` VALUES ('313', '1', '支付平台配置管理', 'S', '/bankinfo/merchantpay_main.do', '', '', 'payPlatformManage', '', '2017-03-28 10:28:33');
INSERT INTO `t_permission` VALUES ('311', '18', '银行转账(new)', 'S', '/intransfer/pay_intransfer.do', '', '', 'bankTransfer(new)', '', '2017-03-28 09:14:28');
INSERT INTO `t_permission` VALUES ('309', '18', '在线支付补单', 'S', '/bankinfo/supplement_main.do', '', '', 'repairPayOrder', '', '2017-03-27 18:16:27');
INSERT INTO `t_permission` VALUES ('40', '18', '额度补单', 'S', '/office/functions/repairHlOrder.jsp', null, null, 'repairHlOrder', null, '2017-02-10 13:49:33');
INSERT INTO `t_permission` VALUES ('41', '18', '额度增减', 'S', '/office/functions/quotalAddModity.jsp', null, null, 'manualModifyAmount', null, '2017-02-10 13:49:33');
INSERT INTO `t_permission` VALUES ('42', '18', '手工增减银行额度', 'S', '/office/functions/changebankCredit.jsp', null, null, 'manualModifyCredit', null, '2017-02-10 13:49:33');
INSERT INTO `t_permission` VALUES ('43', '18', '银行转账', 'S', '/office/functions/intransfer/intransfer.jsp', null, null, 'bankTransfer', null, '2017-02-10 13:49:33');
INSERT INTO `t_permission` VALUES ('44', '18', '自助优惠配置', 'S', '/office/functions/youHuiConfig.jsp', null, null, 'selfGiftConfig', null, '2017-02-10 13:49:33');
INSERT INTO `t_permission` VALUES ('45', '18', '会员升降级', 'S', '/office/functions/upgradelevel.jsp', null, null, 'userLevelConfig', null, '2017-02-10 13:49:33');
INSERT INTO `t_permission` VALUES ('46', '18', 'PT幸运转', 'S', '/office/functions/ptbigbang.jsp', null, null, 'ptBigbang', null, '2017-02-10 13:49:33');
INSERT INTO `t_permission` VALUES ('47', '18', '自动清理数据', 'S', '/office/clearUpDate.do', null, null, 'clearUpData', null, '2017-02-10 13:49:33');
INSERT INTO `t_permission` VALUES ('48', '18', '催账记录', 'S', '/office/functions/urgeOrder.jsp', null, null, 'urgeOrder', null, '2017-02-10 13:49:33');
INSERT INTO `t_permission` VALUES ('49', '18', '好友推荐礼金', 'S', '/office/functions/friendbonusrecord.jsp', null, null, 'friendRecommendationGift', null, '2017-02-10 13:49:33');
INSERT INTO `t_permission` VALUES ('50', '18', '守护女神', 'S', '/office/functions/goddessrecord.jsp', null, null, 'goddessRecord', null, '2017-02-10 13:49:33');
INSERT INTO `t_permission` VALUES ('51', '18', '单身派对', 'S', '/office/functions/singleparty.jsp', null, null, 'singleParty', null, '2017-02-10 13:49:33');
INSERT INTO `t_permission` VALUES ('52', '18', '修改返水数据', 'S', '/office/functions/modifyPlatformRecord.jsp', null, null, 'modifyPlatformRecord', null, '2017-02-10 13:49:33');
INSERT INTO `t_permission` VALUES ('53', '18', '比邻导入数据', 'S', '/office/functions/dataImportBL.jsp', null, null, 'dataImportBL', null, '2017-02-10 13:49:33');
INSERT INTO `t_permission` VALUES ('54', '18', '游戏管理', 'S', '/office/functions/gamecodemanage.jsp', null, null, 'gameCodeManage', null, '2017-02-10 13:49:33');
INSERT INTO `t_permission` VALUES ('55', '18', '手机连续签到抽奖活动配发确认', 'S', '/office/functions/mobileSignLottery.jsp', null, null, 'distributeMobileSignPrize', null, '2017-02-10 13:49:33');
INSERT INTO `t_permission` VALUES ('56', '18', '手机连续签到奖品几率管理', 'S', '/office/functions/lotteryPrizeManagement.jsp', null, null, 'mobileSignOddsConfig', null, '2017-02-10 13:49:33');
INSERT INTO `t_permission` VALUES ('57', '0', '记录', 'M', null, 'fa fa-pencil fa-lg', null, 'record', null, '2017-02-10 13:49:33');
INSERT INTO `t_permission` VALUES ('58', '57', '在线支付', 'S', '/office/payOrder2.do', null, null, 'onlinePay', null, '2017-02-10 13:49:33');
INSERT INTO `t_permission` VALUES ('59', '57', '额度掉单记录', 'S', '/office/functions/gameOrder.jsp', null, null, 'quotaInteruptionRecord', null, '2017-02-10 13:49:33');
INSERT INTO `t_permission` VALUES ('60', '57', '额度增减记录', 'S', '/office/functions/quotalList.jsp', null, null, 'quotaModifyRecord', null, '2017-02-10 13:49:33');
INSERT INTO `t_permission` VALUES ('61', '57', '提案记录', 'S', '/office/functions/proposal.jsp', null, null, 'proposalRecord', null, '2017-02-10 13:49:33');
INSERT INTO `t_permission` VALUES ('62', '57', '人民币事务提案记录', 'S', '/office/functions/shiwuproposal.jsp', null, null, 'RMBProposalRecord', null, '2017-02-10 13:49:33');
INSERT INTO `t_permission` VALUES ('63', '57', 'PESO事务提案记录', 'S', '/office/functions/shiwupesoproposal.jsp', null, null, 'PESOProposalRecord', null, '2017-02-10 13:49:33');
INSERT INTO `t_permission` VALUES ('64', '57', '银行转账提案', 'S', '/office/functions/proposaltransfer.jsp', null, null, 'bankTransferProposal', null, '2017-02-10 13:49:33');
INSERT INTO `t_permission` VALUES ('65', '57', '工行网银存款记录', 'S', '/office/functions/bankonline/bankonline.jsp', null, null, 'ICBCCashinRecord', null, '2017-02-10 13:49:33');
INSERT INTO `t_permission` VALUES ('66', '57', '农行网银存款记录', 'S', '/office/functions/bankonline/abcbankonline.jsp', null, null, 'ABCCashinRecord', null, '2017-02-10 13:49:33');
INSERT INTO `t_permission` VALUES ('67', '57', '建行(加急)/招行网银存款记录', 'S', '/office/functions/bankonline/cmbbankonline.jsp', null, null, 'CCBAndCMBCashinRecord', null, '2017-02-10 13:49:33');
INSERT INTO `t_permission` VALUES ('68', '57', '支付宝存款记录', 'S', '/office/functions/bankonline/alipayline.jsp', null, null, 'AlipayCashinRecord', null, '2017-02-10 13:49:33');
INSERT INTO `t_permission` VALUES ('69', '57', '微信验证存款记录', 'S', '/office/functions/bankonline/wechatline.jsp', null, null, 'wechatCashinRecord', null, '2017-02-10 13:49:33');
INSERT INTO `t_permission` VALUES ('70', '57', '额度验证订单记录', 'S', '/office/functions/validateAmountOrder.jsp', null, null, 'quotaValidateRecord', null, '2017-02-10 13:49:33');
INSERT INTO `t_permission` VALUES ('71', '57', '额度记录', 'S', '/office/functions/creditlog.jsp', null, null, 'quotaRecord', null, '2017-02-10 13:49:33');
INSERT INTO `t_permission` VALUES ('72', '57', '银行额度记录', 'S', '/office/functions/bankcreditlog.jsp', null, null, 'bankQuotaRecord', null, '2017-02-10 13:49:33');
INSERT INTO `t_permission` VALUES ('73', '57', '本地转账记录', 'S', '/office/functions/transfer.jsp', null, null, 'localTransferRecord', null, '2017-02-10 13:49:33');
INSERT INTO `t_permission` VALUES ('74', '57', '会员事件记录', 'S', '/office/functions/actionlog.jsp', null, null, 'memberActionRecord', null, '2017-02-10 13:49:33');
INSERT INTO `t_permission` VALUES ('75', '57', '后台事件记录', 'S', '/office/functions/operationlog.jsp', null, null, 'officeActionRecord', null, '2017-02-10 13:49:33');
INSERT INTO `t_permission` VALUES ('76', '57', '佣金记录查询', 'S', '/office/functions/commissions.jsp', null, null, 'commissionRecord', null, '2017-02-10 13:49:33');
INSERT INTO `t_permission` VALUES ('77', '57', '日结佣金记录查询', 'S', '/office/functions/commissions/ptcommission.jsp', null, null, 'dailyCommissionRecord', null, '2017-02-10 13:49:33');
INSERT INTO `t_permission` VALUES ('78', '57', '首存会员记录', 'S', '/office/functions/first_cashin_usres.jsp', null, null, 'firstCashinRecord', null, '2017-02-10 13:49:33');
INSERT INTO `t_permission` VALUES ('79', '57', '游戏平台输赢记录', 'S', '/office/functions/agprofit.jsp', null, null, 'AGProfitRecord', null, '2017-02-10 13:49:33');
INSERT INTO `t_permission` VALUES ('80', '57', 'NT平台游戏记录', 'S', '/office/functions/ntProfit.jsp', null, null, 'NTProfitRecord', null, '2017-02-10 13:49:33');
INSERT INTO `t_permission` VALUES ('81', '57', 'NEWPT平台数据', 'S', '/office/functions/ptDataProfit.jsp', null, null, 'PTProfitRecord', null, '2017-02-10 13:49:33');
INSERT INTO `t_permission` VALUES ('82', '57', '最新PT平台数据', 'S', '/office/functions/ptDataProfitNew.jsp', null, null, 'newPTProfitRecord', null, '2017-02-10 13:49:33');
INSERT INTO `t_permission` VALUES ('83', '57', 'JC平台输赢数据', 'S', '/office/functions/jcDataProfit.jsp', null, null, 'JCProfitRecord', null, '2017-02-10 13:49:33');
INSERT INTO `t_permission` VALUES ('84', '57', '电销专员电话记录', 'S', '/office/functions/phoneLists.jsp', null, null, 'salePhoneRecord', null, '2017-02-10 13:49:33');
INSERT INTO `t_permission` VALUES ('85', '57', '电销专员电话记录(群发邮件短信)', 'S', '/office/functions/yiyeGroupSend.jsp', null, null, 'batchTextRecord', null, '2017-02-10 13:49:33');
INSERT INTO `t_permission` VALUES ('86', '57', '存款订单记录', 'S', '/office/functions/depositOrderRecord.jsp', null, null, 'depositOrderRecord', null, '2017-02-10 13:49:33');
INSERT INTO `t_permission` VALUES ('87', '57', '玩家密保记录', 'S', '/office/functions/userSafeRecord.jsp', null, null, 'userSecretSecurityRecord', null, '2017-02-10 13:49:33');
INSERT INTO `t_permission` VALUES ('88', '57', '活动礼品寄送', 'S', '/office/functions/gift.jsp', null, null, 'prizeDeliveryRecord', null, '2017-02-10 13:49:33');
INSERT INTO `t_permission` VALUES ('89', '57', '老虎机流水活动查询', 'S', '/office/functions/betrank.jsp', null, null, 'slotMachineActivityRecord', null, '2017-02-10 13:49:33');
INSERT INTO `t_permission` VALUES ('90', '57', '玩家摇摇乐任务记录', 'S', '/office/functions/userTaskRecord.jsp', null, null, 'shakeTaskRecord', null, '2017-02-10 13:49:33');
INSERT INTO `t_permission` VALUES ('91', '57', '玩家推荐好友查询', 'S', '/office/functions/friendintroduce.jsp', null, null, 'userRecommendationRecord', null, '2017-02-10 13:49:33');
INSERT INTO `t_permission` VALUES ('92', '57', '玩家积分记录', 'S', '/office/functions/pointRecord.jsp', null, null, 'pointRecord', null, '2017-02-10 13:49:33');
INSERT INTO `t_permission` VALUES ('93', '57', '玩家闯关活动', 'S', '/office/functions/emigratedrecord.jsp', null, null, 'breakoutRecord', null, '2017-02-10 13:49:33');
INSERT INTO `t_permission` VALUES ('94', '57', '玩家签到礼金', 'S', '/office/functions/signRecord.jsp', null, null, 'signGiftRecord', null, '2017-02-10 13:49:33');
INSERT INTO `t_permission` VALUES ('95', '57', 'MG输赢统计', 'S', '/office/functions/mgplaycheck.jsp', null, null, 'MGProfitRecord', null, '2017-02-10 13:49:33');
INSERT INTO `t_permission` VALUES ('96', '57', 'MG转账记录', 'S', '/office/functions/mgTransferRecord.jsp', null, null, 'MGTransferRecord', null, '2017-02-10 13:49:33');
INSERT INTO `t_permission` VALUES ('97', '57', 'MG异常数据', 'S', 'http://lehu.mgsjiekou.com/failedEndgameQuene.jsp', null, null, 'MGExceptionRecord', null, '2017-02-10 13:49:33');
INSERT INTO `t_permission` VALUES ('98', '57', 'MG输赢数据导入KPI后台', 'S', '/office/functions/mg2kpi.jsp', null, null, 'MGProfitToKPI', null, '2017-02-10 13:49:33');
INSERT INTO `t_permission` VALUES ('99', '57', '活动订单记录', 'S', '/office/functions/listGiftOrder.jsp', null, null, 'activityOrderRecord', null, '2017-02-10 13:49:33');
INSERT INTO `t_permission` VALUES ('100', '0', '统计', 'M', null, 'fa fa-bar-chart fa-lg', null, 'report', null, '2017-02-10 13:49:33');
INSERT INTO `t_permission` VALUES ('101', '100', '盈利报表', 'S', '/office/functions/baobiao.jsp', null, null, 'profitReport', null, '2017-02-10 13:49:33');
INSERT INTO `t_permission` VALUES ('102', '100', '来源网址统计', 'S', '/office/functions/howToKnowStatistics.jsp', null, null, 'howToKnowStatistics', null, '2017-02-10 13:49:33');
INSERT INTO `t_permission` VALUES ('103', '100', '银行进出帐统计', 'S', '/office/functions/fundStatistics.jsp', null, null, 'bankAccountCashInOutStatistics', null, '2017-02-10 13:49:33');
INSERT INTO `t_permission` VALUES ('104', '100', '活动流水统计', 'S', '/office/functions/concertEvent.jsp', null, null, 'activityStatistics', null, '2017-02-10 13:49:33');
INSERT INTO `t_permission` VALUES ('105', '0', '结算', 'M', null, 'fa fa-calculator fa-lg', null, 'check', null, '2017-02-10 13:49:33');
INSERT INTO `t_permission` VALUES ('106', '105', '系统洗码', 'S', '/office/functions/systemxima.jsp', null, null, 'systemReturnProfit', null, '2017-02-10 13:49:33');
INSERT INTO `t_permission` VALUES ('107', '0', '风险控制', 'M', null, 'fa fa-fire fa-lg', null, 'riskControl', null, '2017-02-10 13:49:33');
INSERT INTO `t_permission` VALUES ('108', '107', '输赢信息分析', 'S', '/office/functions/customerDataAnalysis.jsp', null, null, 'winLoseAnalysis', null, '2017-02-10 13:49:33');
INSERT INTO `t_permission` VALUES ('109', '107', '赢点排名分析', 'S', '/office/functions/winPoint.jsp', null, null, 'winPointRank', null, '2017-02-10 13:49:33');
INSERT INTO `t_permission` VALUES ('110', '107', '平台信息分析', 'S', '/office/functions/platformData.jsp', null, null, 'platformData', null, '2017-02-10 13:49:33');
INSERT INTO `t_permission` VALUES ('111', '107', '自助存送投注记录', 'S', '/office/functions/preferentialRecord.jsp', null, null, 'preferentialBetRecord', null, '2017-02-10 13:49:33');
INSERT INTO `t_permission` VALUES ('112', '107', '自助存送转账记录', 'S', '/office/functions/preferTransferlRecord.jsp', null, null, 'preferentialTransferRecord', null, '2017-02-10 13:49:33');
INSERT INTO `t_permission` VALUES ('113', '0', '代理', 'M', null, 'fa fa-group fa-lg', null, 'agent', null, '2017-02-10 13:49:33');
INSERT INTO `t_permission` VALUES ('114', '113', '修改代理基本资料', 'S', '/office/functions/modifyagentInfo.jsp', null, null, 'modifyAgentInfo', null, '2017-02-10 13:49:33');
INSERT INTO `t_permission` VALUES ('115', '113', '新增代理', 'S', '/office/functions/newAgent.jsp', null, null, 'newAgent', null, '2017-02-10 13:49:33');
INSERT INTO `t_permission` VALUES ('116', '113', '代理信用资金预支', 'S', '/office/functions/proxyAdvance.jsp', null, null, 'agentCreditAdvance', null, '2017-02-10 13:49:33');
INSERT INTO `t_permission` VALUES ('117', '113', '代理账号解绑游戏账号', 'S', '/office/functions/agentuserunBindGameuser.jsp', null, null, 'agentUnbindUserAccount', null, '2017-02-10 13:49:33');
INSERT INTO `t_permission` VALUES ('118', '113', '代理VIP', 'S', '/office/functions/commissions/agentvip.jsp', null, null, 'vipAgent', null, '2017-02-10 13:49:33');
INSERT INTO `t_permission` VALUES ('119', '113', '内部代理管理', 'S', '/office/functions/agentlist.jsp', null, null, 'innerAgentManage', null, '2017-02-10 13:49:33');
INSERT INTO `t_permission` VALUES ('120', '113', '代理专员电话记录', 'S', '/office/functions/agentPhoneLists.jsp', null, null, 'agentPhoneRecord', null, '2017-02-10 13:49:33');
INSERT INTO `t_permission` VALUES ('121', '113', '代理域名记录', 'S', '/office/functions/agentWebsiteRecord.jsp', null, null, 'agentWebHostsRecord', null, '2017-02-10 13:49:33');
INSERT INTO `t_permission` VALUES ('122', '113', '代理下线统计', 'S', '/office/functions/AgentReferralsStatistic.jsp', null, null, 'agentReferralsStatistic', null, '2017-02-10 13:49:33');
INSERT INTO `t_permission` VALUES ('123', '113', '代理佣金结算', 'S', '/office/functions/commissions/commissions.jsp', null, null, 'agentCommission', null, '2017-02-10 13:49:33');
INSERT INTO `t_permission` VALUES ('124', '113', '代理数据分析', 'S', '/office/functions/proxyLists.jsp', null, null, 'agentDataAnalysis', null, '2017-02-10 13:49:33');
INSERT INTO `t_permission` VALUES ('125', '113', '代理数据分析（新）', 'S', '/office/functions/agentDataAnalysis.jsp', null, null, 'newAgentDataAnalysis', null, '2017-02-10 13:49:33');
INSERT INTO `t_permission` VALUES ('126', '0', '其他', 'M', null, 'fa fa-plus fa-lg', null, 'others', null, '2017-02-10 13:49:33');
INSERT INTO `t_permission` VALUES ('127', '126', '活动日历', 'S', '/activityCalendar/query.do', null, null, 'activityCalendar', null, '2017-02-10 13:49:33');
INSERT INTO `t_permission` VALUES ('128', '126', '公告管理', 'S', '/announcement/query.do', null, null, 'announcementManage', null, '2017-02-10 13:49:33');
INSERT INTO `t_permission` VALUES ('129', '126', '发布公告', 'S', '/office/functions/announcement_add.jsp', null, null, 'publishAnnouncement', null, '2017-02-10 13:49:33');
INSERT INTO `t_permission` VALUES ('130', '126', '站内信（旧）', 'S', '/office/functions/guestbookManage.jsp', null, null, 'gustbook', null, '2017-02-10 13:49:33');
INSERT INTO `t_permission` VALUES ('131', '126', '短信平台', 'S', '/office/functions/sms.jsp', null, null, 'smsPlatform', null, '2017-02-10 13:49:33');
INSERT INTO `t_permission` VALUES ('132', '126', '最新邮件平台', 'S', '/office/functions/email4.jsp', null, null, 'emailPlatform', null, '2017-02-10 13:49:33');
INSERT INTO `t_permission` VALUES ('133', '126', '已注册客户群发邮件平台', 'S', '/office/functions/yiye_email.jsp', null, null, 'batchSentEmailToUser', null, '2017-02-10 13:49:33');
INSERT INTO `t_permission` VALUES ('134', '126', '外部名单群发邮件平台', 'S', '/office/functions/yiye_externalEmail.jsp', null, null, 'batchSentEmailToOthers', null, '2017-02-10 13:49:33');
INSERT INTO `t_permission` VALUES ('135', '126', '查询信箱格式', 'S', '/office/functions/checkMailLists.jsp', null, null, 'checkMailForm', null, '2017-02-10 13:49:33');
INSERT INTO `t_permission` VALUES ('136', '126', '订阅邮件客户', 'S', '/office/functions/emailSubscriptionLists.jsp', null, null, 'emailSubscriptionList', null, '2017-02-10 13:49:33');
INSERT INTO `t_permission` VALUES ('137', '126', '修改密码', 'S', '/office/functions/modifyOwnPwd.jsp', null, null, 'modifyMyPassword', null, '2017-02-10 13:49:33');
INSERT INTO `t_permission` VALUES ('138', '126', '解除禁用', 'S', '/office/functions/relieveOperator.jsp', null, null, 'unlockOperator', null, '2017-02-10 13:49:33');
INSERT INTO `t_permission` VALUES ('140', '126', '员工管理(新)', 'S', '/office/queryOperators.do', null, null, 'employeeManagement', null, '2017-02-10 13:49:33');
INSERT INTO `t_permission` VALUES ('141', '126', '绑定员工编号', 'S', '/office/functions/bindEmployeeNo.jsp', null, null, 'bindEmployeeNo', null, '2017-02-10 13:49:33');
INSERT INTO `t_permission` VALUES ('142', '126', '重置后台密码', 'S', '/office/functions/resetOperatorPassword.jsp', null, null, 'resetOperatorPassword', null, '2017-02-10 13:49:33');
INSERT INTO `t_permission` VALUES ('144', '0', '权限管理', 'M', '', 'fa fa-eye fa-lg', null, 'auth', null, '2017-02-10 13:49:33');
INSERT INTO `t_permission` VALUES ('145', '144', '用户', 'S', '/html/auth/operator.html', null, null, 'user', null, '2017-02-10 13:49:33');
INSERT INTO `t_permission` VALUES ('150', '0', '国际化', 'M', '', 'fa fa-globe', '', 'i18n', null, '2017-02-17 17:29:33');
INSERT INTO `t_permission` VALUES ('151', '150', '国际化翻译', 'S', '/html/i18n/i18n.html', '', '', 'i18nConfig', null, '2017-02-17 17:32:20');
INSERT INTO `t_permission` VALUES ('146', '144', '角色', 'S', '/html/auth/role.html', null, null, 'role', null, '2017-05-03 14:43:20');
INSERT INTO `t_permission` VALUES ('147', '144', '权限', 'S', '/html/auth/permission.html', null, null, 'permission', null, '2017-05-03 14:42:52');
INSERT INTO `t_permission` VALUES ('323', '321', '自助优惠配置', 'S', '/app/listPreferentialConfig.jsp', '', '', 'preferentialConfig', '', '2017-05-05 13:48:20');
INSERT INTO `t_permission` VALUES ('321', '0', '手机APP', 'M', '', 'fa fa-mobile fa-lg', '', 'mobileApp', '', '2017-05-05 13:45:51');
INSERT INTO `t_permission` VALUES ('327', '321', '约束地址配置', 'S', '/app/listConstraintAddressConfig.jsp', '', '', 'constraintAddress', '', '2017-05-11 09:12:30');
INSERT INTO `t_permission` VALUES ('325', '321', '更新缓存数据', 'S', '/app/refreshCache.jsp', '', '', 'refreshCache', '', '2017-05-05 13:49:48');
INSERT INTO `t_permission` VALUES ('319', '145', '用户授权', 'O', '/role/updateOperatorRole.do', '', '', '', 'updateOperatorRole', '2017-05-03 16:33:11');
INSERT INTO `t_permission` VALUES ('322', '321', '最新优惠配置', 'S', '/app/listLatestPreferential.jsp', '', '', 'latestPreferential', '', '2017-05-17 18:08:54');
INSERT INTO `t_permission` VALUES ('324', '321', '用户优惠评论权限设置', 'S', '/app/listUserStatus.jsp', '', '', 'userStatus', '', '2017-05-17 18:20:26');
INSERT INTO `t_permission` VALUES ('326', '321', '玩家终端设备信息', 'S', '/app/listUserDeviceInfo.jsp', '', '', 'userDeviceInfo', '', '2017-05-17 18:22:28');
INSERT INTO `t_permission` VALUES ('328', '321', '活动专题配置', 'S', '/app/listSpecialTopic.jsp', '', '', 'specialTopic', '', '2017-05-17 18:27:26');
INSERT INTO `t_permission` VALUES ('329', '321', '最新优惠评论记录', 'S', '/app/listPreferentialComment.jsp', '', '', 'preferentialComment', '', '2017-05-18 09:19:47');
INSERT INTO `t_permission` VALUES ('330', '321', '中奖信息配置', 'S', '/app/listLatestWinInfo.jsp', '', '', 'latestWinInfo', '', '2017-05-18 09:21:30');
INSERT INTO `t_permission` VALUES ('331', '126', '站内信（新）', 'S', '/office/functions/topicInfoManage.jsp', '', '', '', '', '2017-05-30 14:53:05');
INSERT INTO `t_permission` VALUES ('332', '126', '站内信优惠记录', 'S', '/office/functions/topicInfoYouHuiManage.jsp', '', '', '', '', '2017-05-30 14:54:11');
INSERT INTO `t_permission` VALUES ('333', '57', '银行卡被绑定记录', 'S', '/office/functions/userbankinfoByBankno.jsp', null, null, 'bankCardisbanded', null, '2017-06-16 10:47:21');
INSERT INTO `t_permission` VALUES ('334', '18', '自助红包配置', 'S', '/office/functions/HBConfig.jsp', '', '', 'redConfig', '', '2017-06-29 17:11:28');
INSERT INTO `t_permission` VALUES ('335', '57', '自助存提款红包', 'S', '/office/functions/HBrecord.jsp', '', '', 'redHistory', '', '2017-06-29 17:15:58');
INSERT INTO `t_permission` VALUES ('336', '1', '游戏平台账号冻结/解冻', 'S', '/office/queryPlatformAccountStatus.do', '', '', 'queryPlatformAccountStatus', '', '2017-07-28 13:34:26');
INSERT INTO `t_permission` VALUES ('337', '1', '套利用户数据', 'S', '/office/functions/arbitrageData.jsp', '', '', 'arbitrageData', '', '2017-08-11 14:03:26');
INSERT INTO `t_permission` VALUES ('338', '18', '活动配置', 'S', '/office/functions/activityConfig.jsp', '', '', 'activityConfig', '', '2017-08-15 08:57:38');
INSERT INTO `t_permission` VALUES ('339', '107', '输赢信息分析(新)', 'S', '/office/functions/customerDataAnalysisNew.jsp', '', '', '', '', '2017-08-24 16:13:55');
INSERT INTO `t_permission` VALUES ('340', '18', '红包雨活动配置', 'S', '/office/functions/listRedEnvelopeActivity.jsp', '', '', '', '', '2018-01-15 11:24:27');
INSERT INTO `t_permission` VALUES ('341', '57', '红包记录', 'S', '/office/functions/ActivityHistoryRedRain.jsp', '', '', '', '', '2018-01-15 11:24:50');
INSERT INTO `t_permission` VALUES ('342', '18', '工会管理', 'S', '/office/functions/gongHuiConfig.jsp', '', '', '', '', '2018-03-28 14:28:07');
INSERT INTO `t_permission` VALUES ('343', '100', '工会会员统计', 'S', '/office/functions/match/gongHuiWeekTongJi.jsp', '', '', '', '', '2018-03-28 14:34:30');
INSERT INTO `t_permission` VALUES ('344', '1', '换线用户', 'S', '/office/functions/changeLineUserRecord.jsp', '', '', '', '', '2018-04-05 15:44:56');
INSERT INTO `t_permission` VALUES ('345', '18', '批量洗码（新）', 'S', '/office/functions/xima/xima2.jsp', '', '', '', '', '2018-05-30 10:17:53');
INSERT INTO `t_permission` VALUES ('346', '57', '二维码配置管理', 'S', '/office/queryQrcode.do', '', '', '', '', '2018-06-28 17:13:55');
INSERT INTO `t_permission` VALUES ('347', '57', '别撸了记录', 'S', '/office/functions/ActivityHistoryLu.jsp', '', '', '', '', '2018-06-29 11:20:40');
INSERT INTO `t_permission` VALUES ('348', '0', '数据采集', 'M', '', 'fa fa-wrench fa-lg', '洗码数据采集功能', 'dataCollection', '', '2018-12-10 01:14:00');
INSERT INTO `t_permission` VALUES ('349', '348', 'PT洗码数据采集', 'S', '/office/functions/ptDataProfitNew.jsp', '', '', 'ptDataCollection', '', '2018-12-10 01:23:33');
INSERT INTO `t_permission` VALUES ('350', '348', '沙巴体育洗码数据采集', 'S', '/office/functions/sbaData.jsp', '', '', 'sbaDataCollection', '', '2018-12-10 01:24:38');
INSERT INTO `t_permission` VALUES ('351', '348', '棋乐游洗码数据采集', 'S', '/office/functions/chessData.jsp', '', '', '761DataCollection', '', '2018-12-10 01:25:01');
INSERT INTO `t_permission` VALUES ('352', '348', 'MG数据采集', 'S', '/office/functions/mgData.jsp', '', '', 'MGDataCollection', '', '2018-12-10 01:25:24');
INSERT INTO `t_permission` VALUES ('353', '348', 'SW数据采集', 'S', '/office/functions/swData.jsp', '', '', 'SWDataCollection', '', '2018-12-10 01:25:44');
INSERT INTO `t_permission` VALUES ('354', '348', 'AG数据采集', 'S', '/office/functions/agDataProfit.jsp', '', '', 'AGDataCollection', '', '2018-12-10 01:26:01');
INSERT INTO `t_permission` VALUES ('355', '18', '自助体验金配置', 'S', '/office/functions/experienceGold.jsp', '', '', '', '', '2018-12-24 11:19:00');

-- ----------------------------
-- Table structure for `t_role`
-- ----------------------------
DROP TABLE IF EXISTS `t_role`;
CREATE TABLE `t_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(32) DEFAULT NULL COMMENT '角色名称',
  `code` varchar(32) DEFAULT NULL COMMENT '代号',
  `description` varchar(128) DEFAULT NULL COMMENT '注释',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8 COMMENT='权限控制的角色表,每一个角色都代表一组特定权限的集合,可以将角色赋予给用户';

-- ----------------------------
-- Records of t_role
-- ----------------------------
INSERT INTO `t_role` VALUES ('1', 'boss', 'boss', 'boss', '2017-02-15 14:32:12');
INSERT INTO `t_role` VALUES ('2', '财务', 'finance', '财务', '2017-02-15 16:48:27');
INSERT INTO `t_role` VALUES ('3', '财务经理', 'finance_manager', 'finance_manager', '2017-02-24 11:56:10');
INSERT INTO `t_role` VALUES ('4', '客服', 'sale', '客服', '2017-02-24 13:06:54');
INSERT INTO `t_role` VALUES ('5', '客服经理', 'sale_manager', '客服经理', '2017-02-24 13:07:16');
INSERT INTO `t_role` VALUES ('6', '市场', 'market', '市场', '2017-02-24 13:07:39');
INSERT INTO `t_role` VALUES ('7', '市场经理', 'market_manager', '市场经理', '2017-02-24 13:07:58');
INSERT INTO `t_role` VALUES ('8', '管理员', 'admin', '管理员', '2017-02-24 13:09:10');
INSERT INTO `t_role` VALUES ('10', '股东', 'vc', '股东', '2017-02-24 13:09:42');
INSERT INTO `t_role` VALUES ('11', '质检', 'qc', '质检', '2017-02-24 13:09:56');
INSERT INTO `t_role` VALUES ('12', '卡管', 'card', '管卡的', '2017-03-07 15:04:13');
INSERT INTO `t_role` VALUES ('13', '行政', 'ad', '行政', '2017-03-07 15:06:17');

-- ----------------------------
-- Table structure for `t_role_permission`
-- ----------------------------
DROP TABLE IF EXISTS `t_role_permission`;
CREATE TABLE `t_role_permission` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `role_id` int(11) DEFAULT NULL COMMENT '角色ID',
  `permission_id` int(11) DEFAULT NULL COMMENT '权限ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2774 DEFAULT CHARSET=utf8 COMMENT='权限管理的 角色-权限表,代表角色和权限之间的对应关系';

-- ----------------------------
-- Records of t_role_permission
-- ----------------------------
INSERT INTO `t_role_permission` VALUES ('1', '1', '1', '2017-02-13 17:16:56');
INSERT INTO `t_role_permission` VALUES ('2', '1', '2', '2017-02-13 17:16:56');
INSERT INTO `t_role_permission` VALUES ('3', '1', '3', '2017-02-13 17:16:56');
INSERT INTO `t_role_permission` VALUES ('4', '1', '4', '2017-02-13 17:16:56');
INSERT INTO `t_role_permission` VALUES ('5', '1', '5', '2017-02-13 17:16:56');
INSERT INTO `t_role_permission` VALUES ('6', '1', '6', '2017-02-13 17:16:56');
INSERT INTO `t_role_permission` VALUES ('7', '1', '7', '2017-02-13 17:16:56');
INSERT INTO `t_role_permission` VALUES ('8', '1', '8', '2017-02-13 17:16:56');
INSERT INTO `t_role_permission` VALUES ('9', '1', '9', '2017-02-13 17:16:56');
INSERT INTO `t_role_permission` VALUES ('10', '1', '10', '2017-02-13 17:16:56');
INSERT INTO `t_role_permission` VALUES ('11', '1', '11', '2017-02-13 17:16:56');
INSERT INTO `t_role_permission` VALUES ('12', '1', '12', '2017-02-13 17:16:56');
INSERT INTO `t_role_permission` VALUES ('13', '1', '13', '2017-02-13 17:16:57');
INSERT INTO `t_role_permission` VALUES ('14', '1', '14', '2017-02-13 17:16:57');
INSERT INTO `t_role_permission` VALUES ('15', '1', '15', '2017-02-13 17:16:57');
INSERT INTO `t_role_permission` VALUES ('16', '1', '16', '2017-02-13 17:16:57');
INSERT INTO `t_role_permission` VALUES ('17', '1', '17', '2017-02-13 17:16:57');
INSERT INTO `t_role_permission` VALUES ('18', '1', '18', '2017-02-13 17:16:57');
INSERT INTO `t_role_permission` VALUES ('19', '1', '19', '2017-02-13 17:16:57');
INSERT INTO `t_role_permission` VALUES ('20', '1', '20', '2017-02-13 17:16:57');
INSERT INTO `t_role_permission` VALUES ('21', '1', '21', '2017-02-13 17:16:57');
INSERT INTO `t_role_permission` VALUES ('22', '1', '22', '2017-02-13 17:16:57');
INSERT INTO `t_role_permission` VALUES ('23', '1', '23', '2017-02-13 17:16:57');
INSERT INTO `t_role_permission` VALUES ('24', '1', '24', '2017-02-13 17:16:57');
INSERT INTO `t_role_permission` VALUES ('25', '1', '25', '2017-02-13 17:16:57');
INSERT INTO `t_role_permission` VALUES ('26', '1', '26', '2017-02-13 17:16:57');
INSERT INTO `t_role_permission` VALUES ('27', '1', '27', '2017-02-13 17:16:57');
INSERT INTO `t_role_permission` VALUES ('28', '1', '28', '2017-02-13 17:16:57');
INSERT INTO `t_role_permission` VALUES ('29', '1', '29', '2017-02-13 17:16:57');
INSERT INTO `t_role_permission` VALUES ('30', '1', '30', '2017-02-13 17:16:57');
INSERT INTO `t_role_permission` VALUES ('31', '1', '31', '2017-02-13 17:16:57');
INSERT INTO `t_role_permission` VALUES ('32', '1', '32', '2017-02-13 17:16:57');
INSERT INTO `t_role_permission` VALUES ('40', '1', '40', '2017-02-13 17:16:57');
INSERT INTO `t_role_permission` VALUES ('41', '1', '41', '2017-02-13 17:16:57');
INSERT INTO `t_role_permission` VALUES ('42', '1', '42', '2017-02-13 17:16:57');
INSERT INTO `t_role_permission` VALUES ('43', '1', '43', '2017-02-13 17:16:57');
INSERT INTO `t_role_permission` VALUES ('44', '1', '44', '2017-02-13 17:16:57');
INSERT INTO `t_role_permission` VALUES ('45', '1', '45', '2017-02-13 17:16:57');
INSERT INTO `t_role_permission` VALUES ('46', '1', '46', '2017-02-13 17:16:57');
INSERT INTO `t_role_permission` VALUES ('47', '1', '47', '2017-02-13 17:16:57');
INSERT INTO `t_role_permission` VALUES ('48', '1', '48', '2017-02-13 17:16:57');
INSERT INTO `t_role_permission` VALUES ('49', '1', '49', '2017-02-13 17:16:57');
INSERT INTO `t_role_permission` VALUES ('50', '1', '50', '2017-02-13 17:16:57');
INSERT INTO `t_role_permission` VALUES ('51', '1', '51', '2017-02-13 17:16:57');
INSERT INTO `t_role_permission` VALUES ('52', '1', '52', '2017-02-13 17:16:57');
INSERT INTO `t_role_permission` VALUES ('53', '1', '53', '2017-02-13 17:16:57');
INSERT INTO `t_role_permission` VALUES ('54', '1', '54', '2017-02-13 17:16:57');
INSERT INTO `t_role_permission` VALUES ('55', '1', '55', '2017-02-13 17:16:57');
INSERT INTO `t_role_permission` VALUES ('56', '1', '56', '2017-02-13 17:16:57');
INSERT INTO `t_role_permission` VALUES ('57', '1', '57', '2017-02-13 17:16:57');
INSERT INTO `t_role_permission` VALUES ('58', '1', '58', '2017-02-13 17:16:57');
INSERT INTO `t_role_permission` VALUES ('59', '1', '59', '2017-02-13 17:16:57');
INSERT INTO `t_role_permission` VALUES ('60', '1', '60', '2017-02-13 17:16:57');
INSERT INTO `t_role_permission` VALUES ('61', '1', '61', '2017-02-13 17:16:57');
INSERT INTO `t_role_permission` VALUES ('62', '1', '62', '2017-02-13 17:16:57');
INSERT INTO `t_role_permission` VALUES ('63', '1', '63', '2017-02-13 17:16:57');
INSERT INTO `t_role_permission` VALUES ('64', '1', '64', '2017-02-13 17:16:57');
INSERT INTO `t_role_permission` VALUES ('65', '1', '65', '2017-02-13 17:16:57');
INSERT INTO `t_role_permission` VALUES ('66', '1', '66', '2017-02-13 17:16:57');
INSERT INTO `t_role_permission` VALUES ('67', '1', '67', '2017-02-13 17:16:57');
INSERT INTO `t_role_permission` VALUES ('68', '1', '68', '2017-02-13 17:16:57');
INSERT INTO `t_role_permission` VALUES ('69', '1', '69', '2017-02-13 17:16:57');
INSERT INTO `t_role_permission` VALUES ('70', '1', '70', '2017-02-13 17:16:57');
INSERT INTO `t_role_permission` VALUES ('71', '1', '71', '2017-02-13 17:16:57');
INSERT INTO `t_role_permission` VALUES ('72', '1', '72', '2017-02-13 17:16:57');
INSERT INTO `t_role_permission` VALUES ('73', '1', '73', '2017-02-13 17:16:57');
INSERT INTO `t_role_permission` VALUES ('74', '1', '74', '2017-02-13 17:16:57');
INSERT INTO `t_role_permission` VALUES ('75', '1', '75', '2017-02-13 17:16:57');
INSERT INTO `t_role_permission` VALUES ('76', '1', '76', '2017-02-13 17:16:57');
INSERT INTO `t_role_permission` VALUES ('77', '1', '77', '2017-02-13 17:16:57');
INSERT INTO `t_role_permission` VALUES ('78', '1', '78', '2017-02-13 17:16:57');
INSERT INTO `t_role_permission` VALUES ('79', '1', '79', '2017-02-13 17:16:57');
INSERT INTO `t_role_permission` VALUES ('80', '1', '80', '2017-02-13 17:16:57');
INSERT INTO `t_role_permission` VALUES ('81', '1', '81', '2017-02-13 17:16:57');
INSERT INTO `t_role_permission` VALUES ('82', '1', '82', '2017-02-13 17:16:57');
INSERT INTO `t_role_permission` VALUES ('83', '1', '83', '2017-02-13 17:16:57');
INSERT INTO `t_role_permission` VALUES ('84', '1', '84', '2017-02-13 17:16:57');
INSERT INTO `t_role_permission` VALUES ('85', '1', '85', '2017-02-13 17:16:57');
INSERT INTO `t_role_permission` VALUES ('86', '1', '86', '2017-02-13 17:16:57');
INSERT INTO `t_role_permission` VALUES ('87', '1', '87', '2017-02-13 17:16:57');
INSERT INTO `t_role_permission` VALUES ('88', '1', '88', '2017-02-13 17:16:57');
INSERT INTO `t_role_permission` VALUES ('89', '1', '89', '2017-02-13 17:16:57');
INSERT INTO `t_role_permission` VALUES ('90', '1', '90', '2017-02-13 17:16:57');
INSERT INTO `t_role_permission` VALUES ('91', '1', '91', '2017-02-13 17:16:57');
INSERT INTO `t_role_permission` VALUES ('92', '1', '92', '2017-02-13 17:16:57');
INSERT INTO `t_role_permission` VALUES ('93', '1', '93', '2017-02-13 17:16:57');
INSERT INTO `t_role_permission` VALUES ('94', '1', '94', '2017-02-13 17:16:57');
INSERT INTO `t_role_permission` VALUES ('95', '1', '95', '2017-02-13 17:16:57');
INSERT INTO `t_role_permission` VALUES ('96', '1', '96', '2017-02-13 17:16:57');
INSERT INTO `t_role_permission` VALUES ('97', '1', '97', '2017-02-13 17:16:57');
INSERT INTO `t_role_permission` VALUES ('98', '1', '98', '2017-02-13 17:16:57');
INSERT INTO `t_role_permission` VALUES ('99', '1', '99', '2017-02-13 17:16:57');
INSERT INTO `t_role_permission` VALUES ('100', '1', '100', '2017-02-13 17:16:57');
INSERT INTO `t_role_permission` VALUES ('101', '1', '101', '2017-02-13 17:16:57');
INSERT INTO `t_role_permission` VALUES ('102', '1', '102', '2017-02-13 17:16:57');
INSERT INTO `t_role_permission` VALUES ('103', '1', '103', '2017-02-13 17:16:57');
INSERT INTO `t_role_permission` VALUES ('104', '1', '104', '2017-02-13 17:16:57');
INSERT INTO `t_role_permission` VALUES ('105', '1', '105', '2017-02-13 17:16:57');
INSERT INTO `t_role_permission` VALUES ('106', '1', '106', '2017-02-13 17:16:57');
INSERT INTO `t_role_permission` VALUES ('107', '1', '107', '2017-02-13 17:16:57');
INSERT INTO `t_role_permission` VALUES ('108', '1', '108', '2017-02-13 17:16:57');
INSERT INTO `t_role_permission` VALUES ('109', '1', '109', '2017-02-13 17:16:57');
INSERT INTO `t_role_permission` VALUES ('110', '1', '110', '2017-02-13 17:16:57');
INSERT INTO `t_role_permission` VALUES ('111', '1', '111', '2017-02-13 17:16:57');
INSERT INTO `t_role_permission` VALUES ('112', '1', '112', '2017-02-13 17:16:57');
INSERT INTO `t_role_permission` VALUES ('113', '1', '113', '2017-02-13 17:16:57');
INSERT INTO `t_role_permission` VALUES ('114', '1', '114', '2017-02-13 17:16:57');
INSERT INTO `t_role_permission` VALUES ('115', '1', '115', '2017-02-13 17:16:57');
INSERT INTO `t_role_permission` VALUES ('116', '1', '116', '2017-02-13 17:16:57');
INSERT INTO `t_role_permission` VALUES ('117', '1', '117', '2017-02-13 17:16:57');
INSERT INTO `t_role_permission` VALUES ('118', '1', '118', '2017-02-13 17:16:57');
INSERT INTO `t_role_permission` VALUES ('119', '1', '119', '2017-02-13 17:16:57');
INSERT INTO `t_role_permission` VALUES ('120', '1', '120', '2017-02-13 17:16:57');
INSERT INTO `t_role_permission` VALUES ('121', '1', '121', '2017-02-13 17:16:57');
INSERT INTO `t_role_permission` VALUES ('122', '1', '122', '2017-02-13 17:16:57');
INSERT INTO `t_role_permission` VALUES ('123', '1', '123', '2017-02-13 17:16:57');
INSERT INTO `t_role_permission` VALUES ('124', '1', '124', '2017-02-13 17:16:57');
INSERT INTO `t_role_permission` VALUES ('125', '1', '125', '2017-02-13 17:16:57');
INSERT INTO `t_role_permission` VALUES ('126', '1', '126', '2017-02-13 17:16:57');
INSERT INTO `t_role_permission` VALUES ('127', '1', '127', '2017-02-13 17:16:57');
INSERT INTO `t_role_permission` VALUES ('128', '1', '128', '2017-02-13 17:16:57');
INSERT INTO `t_role_permission` VALUES ('129', '1', '129', '2017-02-13 17:16:57');
INSERT INTO `t_role_permission` VALUES ('130', '1', '130', '2017-02-13 17:16:57');
INSERT INTO `t_role_permission` VALUES ('131', '1', '131', '2017-02-13 17:16:57');
INSERT INTO `t_role_permission` VALUES ('132', '1', '132', '2017-02-13 17:16:57');
INSERT INTO `t_role_permission` VALUES ('133', '1', '133', '2017-02-13 17:16:57');
INSERT INTO `t_role_permission` VALUES ('134', '1', '134', '2017-02-13 17:16:57');
INSERT INTO `t_role_permission` VALUES ('135', '1', '135', '2017-02-13 17:16:57');
INSERT INTO `t_role_permission` VALUES ('136', '1', '136', '2017-02-13 17:16:57');
INSERT INTO `t_role_permission` VALUES ('137', '1', '137', '2017-02-13 17:16:57');
INSERT INTO `t_role_permission` VALUES ('138', '1', '138', '2017-02-13 17:16:57');
INSERT INTO `t_role_permission` VALUES ('140', '1', '140', '2017-02-13 17:16:57');
INSERT INTO `t_role_permission` VALUES ('141', '1', '141', '2017-02-13 17:16:57');
INSERT INTO `t_role_permission` VALUES ('142', '1', '142', '2017-02-13 17:16:57');
INSERT INTO `t_role_permission` VALUES ('144', '1', '144', '2017-02-13 17:16:57');
INSERT INTO `t_role_permission` VALUES ('150', '2', '2', '2017-02-16 09:22:34');
INSERT INTO `t_role_permission` VALUES ('154', '1', '145', '2017-02-16 09:28:10');
INSERT INTO `t_role_permission` VALUES ('155', '1', '150', '2017-02-17 17:31:21');
INSERT INTO `t_role_permission` VALUES ('156', '1', '151', '2017-02-17 17:38:15');
INSERT INTO `t_role_permission` VALUES ('162', '5', '1', '2017-03-07 14:59:31');
INSERT INTO `t_role_permission` VALUES ('163', '2', '1', '2017-03-07 15:05:20');
INSERT INTO `t_role_permission` VALUES ('164', '3', '1', '2017-03-07 15:05:20');
INSERT INTO `t_role_permission` VALUES ('165', '4', '1', '2017-03-07 15:05:20');
INSERT INTO `t_role_permission` VALUES ('166', '6', '1', '2017-03-07 15:05:20');
INSERT INTO `t_role_permission` VALUES ('167', '7', '1', '2017-03-07 15:05:20');
INSERT INTO `t_role_permission` VALUES ('168', '11', '1', '2017-03-07 15:05:20');
INSERT INTO `t_role_permission` VALUES ('169', '12', '1', '2017-03-07 15:05:20');
INSERT INTO `t_role_permission` VALUES ('170', '13', '1', '2017-03-07 15:06:44');
INSERT INTO `t_role_permission` VALUES ('171', '3', '2', '2017-03-07 15:07:56');
INSERT INTO `t_role_permission` VALUES ('172', '4', '2', '2017-03-07 15:07:56');
INSERT INTO `t_role_permission` VALUES ('173', '5', '2', '2017-03-07 15:07:56');
INSERT INTO `t_role_permission` VALUES ('174', '6', '2', '2017-03-07 15:07:56');
INSERT INTO `t_role_permission` VALUES ('175', '7', '2', '2017-03-07 15:07:56');
INSERT INTO `t_role_permission` VALUES ('176', '11', '2', '2017-03-07 15:07:56');
INSERT INTO `t_role_permission` VALUES ('177', '13', '2', '2017-03-07 15:07:56');
INSERT INTO `t_role_permission` VALUES ('178', '12', '2', '2017-03-07 15:08:41');
INSERT INTO `t_role_permission` VALUES ('179', '4', '4', '2017-03-07 15:09:37');
INSERT INTO `t_role_permission` VALUES ('180', '5', '4', '2017-03-07 15:09:37');
INSERT INTO `t_role_permission` VALUES ('181', '6', '4', '2017-03-07 15:09:37');
INSERT INTO `t_role_permission` VALUES ('182', '7', '4', '2017-03-07 15:09:37');
INSERT INTO `t_role_permission` VALUES ('183', '2', '6', '2017-03-07 15:10:58');
INSERT INTO `t_role_permission` VALUES ('184', '3', '6', '2017-03-07 15:10:58');
INSERT INTO `t_role_permission` VALUES ('185', '4', '6', '2017-03-07 15:10:58');
INSERT INTO `t_role_permission` VALUES ('186', '5', '6', '2017-03-07 15:10:58');
INSERT INTO `t_role_permission` VALUES ('187', '12', '6', '2017-03-07 15:10:58');
INSERT INTO `t_role_permission` VALUES ('188', '4', '7', '2017-03-07 15:11:42');
INSERT INTO `t_role_permission` VALUES ('189', '5', '7', '2017-03-07 15:11:42');
INSERT INTO `t_role_permission` VALUES ('190', '6', '7', '2017-03-07 15:11:42');
INSERT INTO `t_role_permission` VALUES ('191', '7', '7', '2017-03-07 15:11:42');
INSERT INTO `t_role_permission` VALUES ('192', '8', '7', '2017-03-07 15:11:42');
INSERT INTO `t_role_permission` VALUES ('193', '4', '8', '2017-03-07 15:12:20');
INSERT INTO `t_role_permission` VALUES ('194', '5', '8', '2017-03-07 15:12:20');
INSERT INTO `t_role_permission` VALUES ('195', '6', '8', '2017-03-07 15:12:20');
INSERT INTO `t_role_permission` VALUES ('196', '7', '8', '2017-03-07 15:12:20');
INSERT INTO `t_role_permission` VALUES ('197', '8', '8', '2017-03-07 15:12:20');
INSERT INTO `t_role_permission` VALUES ('198', '11', '8', '2017-03-07 15:12:20');
INSERT INTO `t_role_permission` VALUES ('201', '4', '9', '2017-03-07 15:12:51');
INSERT INTO `t_role_permission` VALUES ('202', '5', '9', '2017-03-07 15:12:51');
INSERT INTO `t_role_permission` VALUES ('203', '6', '9', '2017-03-07 15:12:51');
INSERT INTO `t_role_permission` VALUES ('204', '7', '9', '2017-03-07 15:12:51');
INSERT INTO `t_role_permission` VALUES ('205', '8', '9', '2017-03-07 15:12:51');
INSERT INTO `t_role_permission` VALUES ('206', '11', '9', '2017-03-07 15:12:51');
INSERT INTO `t_role_permission` VALUES ('207', '12', '9', '2017-03-07 15:12:51');
INSERT INTO `t_role_permission` VALUES ('209', '2', '11', '2017-03-07 15:37:28');
INSERT INTO `t_role_permission` VALUES ('210', '2', '12', '2017-03-07 15:37:28');
INSERT INTO `t_role_permission` VALUES ('211', '2', '13', '2017-03-07 15:37:28');
INSERT INTO `t_role_permission` VALUES ('212', '2', '17', '2017-03-07 15:37:28');
INSERT INTO `t_role_permission` VALUES ('213', '2', '18', '2017-03-07 15:37:28');
INSERT INTO `t_role_permission` VALUES ('215', '2', '20', '2017-03-07 15:37:28');
INSERT INTO `t_role_permission` VALUES ('216', '2', '21', '2017-03-07 15:37:28');
INSERT INTO `t_role_permission` VALUES ('217', '2', '22', '2017-03-07 15:37:28');
INSERT INTO `t_role_permission` VALUES ('218', '2', '23', '2017-03-07 15:37:28');
INSERT INTO `t_role_permission` VALUES ('220', '2', '26', '2017-03-07 15:37:28');
INSERT INTO `t_role_permission` VALUES ('221', '2', '43', '2017-03-07 15:37:28');
INSERT INTO `t_role_permission` VALUES ('222', '2', '45', '2017-03-07 15:37:28');
INSERT INTO `t_role_permission` VALUES ('224', '2', '48', '2017-03-07 15:37:28');
INSERT INTO `t_role_permission` VALUES ('225', '2', '49', '2017-03-07 15:37:28');
INSERT INTO `t_role_permission` VALUES ('228', '2', '52', '2017-03-07 15:37:28');
INSERT INTO `t_role_permission` VALUES ('229', '2', '54', '2017-03-07 15:37:28');
INSERT INTO `t_role_permission` VALUES ('230', '2', '57', '2017-03-07 15:37:28');
INSERT INTO `t_role_permission` VALUES ('231', '2', '58', '2017-03-07 15:37:28');
INSERT INTO `t_role_permission` VALUES ('232', '2', '59', '2017-03-07 15:37:28');
INSERT INTO `t_role_permission` VALUES ('233', '2', '60', '2017-03-07 15:37:28');
INSERT INTO `t_role_permission` VALUES ('234', '2', '61', '2017-03-07 15:37:28');
INSERT INTO `t_role_permission` VALUES ('235', '2', '62', '2017-03-07 15:37:28');
INSERT INTO `t_role_permission` VALUES ('236', '2', '63', '2017-03-07 15:37:28');
INSERT INTO `t_role_permission` VALUES ('237', '2', '64', '2017-03-07 15:37:28');
INSERT INTO `t_role_permission` VALUES ('238', '2', '65', '2017-03-07 15:37:28');
INSERT INTO `t_role_permission` VALUES ('239', '2', '66', '2017-03-07 15:37:28');
INSERT INTO `t_role_permission` VALUES ('240', '2', '67', '2017-03-07 15:37:28');
INSERT INTO `t_role_permission` VALUES ('241', '2', '68', '2017-03-07 15:37:28');
INSERT INTO `t_role_permission` VALUES ('242', '2', '69', '2017-03-07 15:37:28');
INSERT INTO `t_role_permission` VALUES ('243', '2', '70', '2017-03-07 15:37:28');
INSERT INTO `t_role_permission` VALUES ('244', '2', '71', '2017-03-07 15:37:28');
INSERT INTO `t_role_permission` VALUES ('245', '2', '72', '2017-03-07 15:37:28');
INSERT INTO `t_role_permission` VALUES ('246', '2', '73', '2017-03-07 15:37:28');
INSERT INTO `t_role_permission` VALUES ('247', '2', '74', '2017-03-07 15:37:28');
INSERT INTO `t_role_permission` VALUES ('248', '2', '75', '2017-03-07 15:37:28');
INSERT INTO `t_role_permission` VALUES ('249', '2', '76', '2017-03-07 15:37:28');
INSERT INTO `t_role_permission` VALUES ('250', '2', '77', '2017-03-07 15:37:28');
INSERT INTO `t_role_permission` VALUES ('251', '2', '78', '2017-03-07 15:37:28');
INSERT INTO `t_role_permission` VALUES ('252', '2', '79', '2017-03-07 15:37:28');
INSERT INTO `t_role_permission` VALUES ('253', '2', '80', '2017-03-07 15:37:28');
INSERT INTO `t_role_permission` VALUES ('254', '2', '81', '2017-03-07 15:37:28');
INSERT INTO `t_role_permission` VALUES ('255', '2', '82', '2017-03-07 15:37:28');
INSERT INTO `t_role_permission` VALUES ('259', '2', '86', '2017-03-07 15:37:28');
INSERT INTO `t_role_permission` VALUES ('262', '2', '91', '2017-03-07 15:37:28');
INSERT INTO `t_role_permission` VALUES ('263', '2', '92', '2017-03-07 15:37:28');
INSERT INTO `t_role_permission` VALUES ('266', '2', '95', '2017-03-07 15:37:28');
INSERT INTO `t_role_permission` VALUES ('267', '2', '96', '2017-03-07 15:37:28');
INSERT INTO `t_role_permission` VALUES ('268', '2', '97', '2017-03-07 15:37:28');
INSERT INTO `t_role_permission` VALUES ('270', '2', '107', '2017-03-07 15:37:28');
INSERT INTO `t_role_permission` VALUES ('271', '2', '108', '2017-03-07 15:37:28');
INSERT INTO `t_role_permission` VALUES ('272', '2', '109', '2017-03-07 15:37:28');
INSERT INTO `t_role_permission` VALUES ('273', '2', '110', '2017-03-07 15:37:28');
INSERT INTO `t_role_permission` VALUES ('274', '2', '111', '2017-03-07 15:37:28');
INSERT INTO `t_role_permission` VALUES ('275', '2', '112', '2017-03-07 15:37:28');
INSERT INTO `t_role_permission` VALUES ('276', '2', '113', '2017-03-07 15:37:28');
INSERT INTO `t_role_permission` VALUES ('277', '2', '117', '2017-03-07 15:37:28');
INSERT INTO `t_role_permission` VALUES ('278', '2', '118', '2017-03-07 15:37:28');
INSERT INTO `t_role_permission` VALUES ('279', '2', '119', '2017-03-07 15:37:28');
INSERT INTO `t_role_permission` VALUES ('280', '2', '121', '2017-03-07 15:37:28');
INSERT INTO `t_role_permission` VALUES ('281', '2', '124', '2017-03-07 15:37:28');
INSERT INTO `t_role_permission` VALUES ('282', '2', '125', '2017-03-07 15:37:28');
INSERT INTO `t_role_permission` VALUES ('283', '2', '126', '2017-03-07 15:37:28');
INSERT INTO `t_role_permission` VALUES ('284', '2', '130', '2017-03-07 15:37:28');
INSERT INTO `t_role_permission` VALUES ('285', '2', '141', '2017-03-07 15:37:28');
INSERT INTO `t_role_permission` VALUES ('287', '3', '7', '2017-03-07 15:42:56');
INSERT INTO `t_role_permission` VALUES ('288', '3', '8', '2017-03-07 15:42:56');
INSERT INTO `t_role_permission` VALUES ('289', '3', '10', '2017-03-07 15:42:56');
INSERT INTO `t_role_permission` VALUES ('290', '3', '11', '2017-03-07 15:42:56');
INSERT INTO `t_role_permission` VALUES ('291', '3', '12', '2017-03-07 15:42:56');
INSERT INTO `t_role_permission` VALUES ('292', '3', '13', '2017-03-07 15:42:56');
INSERT INTO `t_role_permission` VALUES ('293', '3', '15', '2017-03-07 15:42:56');
INSERT INTO `t_role_permission` VALUES ('294', '3', '16', '2017-03-07 15:42:56');
INSERT INTO `t_role_permission` VALUES ('295', '3', '17', '2017-03-07 15:42:56');
INSERT INTO `t_role_permission` VALUES ('296', '3', '18', '2017-03-07 15:42:56');
INSERT INTO `t_role_permission` VALUES ('297', '3', '20', '2017-03-07 15:42:56');
INSERT INTO `t_role_permission` VALUES ('298', '3', '21', '2017-03-07 15:42:56');
INSERT INTO `t_role_permission` VALUES ('299', '3', '22', '2017-03-07 15:42:56');
INSERT INTO `t_role_permission` VALUES ('300', '3', '23', '2017-03-07 15:42:56');
INSERT INTO `t_role_permission` VALUES ('302', '3', '26', '2017-03-07 15:42:56');
INSERT INTO `t_role_permission` VALUES ('303', '3', '42', '2017-03-07 15:42:56');
INSERT INTO `t_role_permission` VALUES ('304', '3', '43', '2017-03-07 15:42:56');
INSERT INTO `t_role_permission` VALUES ('305', '3', '45', '2017-03-07 15:42:56');
INSERT INTO `t_role_permission` VALUES ('307', '3', '48', '2017-03-07 15:42:56');
INSERT INTO `t_role_permission` VALUES ('308', '3', '49', '2017-03-07 15:42:56');
INSERT INTO `t_role_permission` VALUES ('311', '3', '52', '2017-03-07 15:42:56');
INSERT INTO `t_role_permission` VALUES ('312', '3', '54', '2017-03-07 15:42:56');
INSERT INTO `t_role_permission` VALUES ('313', '3', '55', '2017-03-07 15:42:56');
INSERT INTO `t_role_permission` VALUES ('314', '3', '56', '2017-03-07 15:42:56');
INSERT INTO `t_role_permission` VALUES ('315', '3', '57', '2017-03-07 15:42:56');
INSERT INTO `t_role_permission` VALUES ('316', '3', '58', '2017-03-07 15:42:56');
INSERT INTO `t_role_permission` VALUES ('317', '3', '59', '2017-03-07 15:42:56');
INSERT INTO `t_role_permission` VALUES ('318', '3', '60', '2017-03-07 15:42:56');
INSERT INTO `t_role_permission` VALUES ('319', '3', '61', '2017-03-07 15:42:56');
INSERT INTO `t_role_permission` VALUES ('320', '3', '62', '2017-03-07 15:42:56');
INSERT INTO `t_role_permission` VALUES ('321', '3', '63', '2017-03-07 15:42:56');
INSERT INTO `t_role_permission` VALUES ('322', '3', '64', '2017-03-07 15:42:56');
INSERT INTO `t_role_permission` VALUES ('323', '3', '65', '2017-03-07 15:42:56');
INSERT INTO `t_role_permission` VALUES ('324', '3', '66', '2017-03-07 15:42:56');
INSERT INTO `t_role_permission` VALUES ('325', '3', '67', '2017-03-07 15:42:56');
INSERT INTO `t_role_permission` VALUES ('326', '3', '68', '2017-03-07 15:42:56');
INSERT INTO `t_role_permission` VALUES ('327', '3', '69', '2017-03-07 15:42:56');
INSERT INTO `t_role_permission` VALUES ('328', '3', '70', '2017-03-07 15:42:56');
INSERT INTO `t_role_permission` VALUES ('329', '3', '71', '2017-03-07 15:42:56');
INSERT INTO `t_role_permission` VALUES ('330', '3', '72', '2017-03-07 15:42:56');
INSERT INTO `t_role_permission` VALUES ('331', '3', '73', '2017-03-07 15:42:56');
INSERT INTO `t_role_permission` VALUES ('332', '3', '74', '2017-03-07 15:42:56');
INSERT INTO `t_role_permission` VALUES ('333', '3', '75', '2017-03-07 15:42:56');
INSERT INTO `t_role_permission` VALUES ('334', '3', '76', '2017-03-07 15:42:56');
INSERT INTO `t_role_permission` VALUES ('335', '3', '77', '2017-03-07 15:42:56');
INSERT INTO `t_role_permission` VALUES ('336', '3', '78', '2017-03-07 15:42:56');
INSERT INTO `t_role_permission` VALUES ('337', '3', '79', '2017-03-07 15:42:56');
INSERT INTO `t_role_permission` VALUES ('338', '3', '80', '2017-03-07 15:42:56');
INSERT INTO `t_role_permission` VALUES ('339', '3', '81', '2017-03-07 15:42:56');
INSERT INTO `t_role_permission` VALUES ('340', '3', '82', '2017-03-07 15:42:56');
INSERT INTO `t_role_permission` VALUES ('344', '3', '86', '2017-03-07 15:42:56');
INSERT INTO `t_role_permission` VALUES ('346', '3', '90', '2017-03-07 15:42:56');
INSERT INTO `t_role_permission` VALUES ('347', '3', '91', '2017-03-07 15:42:56');
INSERT INTO `t_role_permission` VALUES ('348', '3', '92', '2017-03-07 15:42:56');
INSERT INTO `t_role_permission` VALUES ('351', '3', '95', '2017-03-07 15:42:56');
INSERT INTO `t_role_permission` VALUES ('352', '3', '96', '2017-03-07 15:42:56');
INSERT INTO `t_role_permission` VALUES ('353', '3', '97', '2017-03-07 15:42:56');
INSERT INTO `t_role_permission` VALUES ('355', '3', '107', '2017-03-07 15:42:56');
INSERT INTO `t_role_permission` VALUES ('356', '3', '108', '2017-03-07 15:42:56');
INSERT INTO `t_role_permission` VALUES ('357', '3', '109', '2017-03-07 15:42:56');
INSERT INTO `t_role_permission` VALUES ('358', '3', '110', '2017-03-07 15:42:56');
INSERT INTO `t_role_permission` VALUES ('359', '3', '111', '2017-03-07 15:42:56');
INSERT INTO `t_role_permission` VALUES ('360', '3', '112', '2017-03-07 15:42:56');
INSERT INTO `t_role_permission` VALUES ('361', '3', '113', '2017-03-07 15:42:56');
INSERT INTO `t_role_permission` VALUES ('362', '3', '117', '2017-03-07 15:42:56');
INSERT INTO `t_role_permission` VALUES ('363', '3', '118', '2017-03-07 15:42:56');
INSERT INTO `t_role_permission` VALUES ('364', '3', '119', '2017-03-07 15:42:56');
INSERT INTO `t_role_permission` VALUES ('365', '3', '121', '2017-03-07 15:42:56');
INSERT INTO `t_role_permission` VALUES ('366', '3', '124', '2017-03-07 15:42:56');
INSERT INTO `t_role_permission` VALUES ('367', '3', '125', '2017-03-07 15:42:56');
INSERT INTO `t_role_permission` VALUES ('368', '3', '126', '2017-03-07 15:42:56');
INSERT INTO `t_role_permission` VALUES ('369', '3', '130', '2017-03-07 15:42:56');
INSERT INTO `t_role_permission` VALUES ('370', '3', '138', '2017-03-07 15:42:56');
INSERT INTO `t_role_permission` VALUES ('372', '3', '140', '2017-03-07 15:42:56');
INSERT INTO `t_role_permission` VALUES ('373', '3', '141', '2017-03-07 15:42:56');
INSERT INTO `t_role_permission` VALUES ('374', '5', '3', '2017-03-07 15:49:09');
INSERT INTO `t_role_permission` VALUES ('375', '5', '15', '2017-03-07 15:49:09');
INSERT INTO `t_role_permission` VALUES ('376', '5', '16', '2017-03-07 15:49:09');
INSERT INTO `t_role_permission` VALUES ('377', '5', '18', '2017-03-07 15:49:09');
INSERT INTO `t_role_permission` VALUES ('378', '5', '19', '2017-03-07 15:49:09');
INSERT INTO `t_role_permission` VALUES ('379', '5', '20', '2017-03-07 15:49:09');
INSERT INTO `t_role_permission` VALUES ('380', '5', '21', '2017-03-07 15:49:09');
INSERT INTO `t_role_permission` VALUES ('381', '5', '23', '2017-03-07 15:49:09');
INSERT INTO `t_role_permission` VALUES ('382', '5', '24', '2017-03-07 15:49:09');
INSERT INTO `t_role_permission` VALUES ('383', '5', '25', '2017-03-07 15:49:09');
INSERT INTO `t_role_permission` VALUES ('384', '5', '26', '2017-03-07 15:49:09');
INSERT INTO `t_role_permission` VALUES ('385', '5', '27', '2017-03-07 15:49:09');
INSERT INTO `t_role_permission` VALUES ('386', '5', '28', '2017-03-07 15:49:09');
INSERT INTO `t_role_permission` VALUES ('387', '5', '29', '2017-03-07 15:49:09');
INSERT INTO `t_role_permission` VALUES ('388', '5', '30', '2017-03-07 15:49:09');
INSERT INTO `t_role_permission` VALUES ('389', '5', '31', '2017-03-07 15:49:09');
INSERT INTO `t_role_permission` VALUES ('390', '5', '32', '2017-03-07 15:49:09');
INSERT INTO `t_role_permission` VALUES ('398', '5', '40', '2017-03-07 15:49:09');
INSERT INTO `t_role_permission` VALUES ('399', '5', '41', '2017-03-07 15:49:09');
INSERT INTO `t_role_permission` VALUES ('400', '5', '44', '2017-03-07 15:49:09');
INSERT INTO `t_role_permission` VALUES ('401', '5', '45', '2017-03-07 15:49:09');
INSERT INTO `t_role_permission` VALUES ('402', '5', '46', '2017-03-07 15:49:09');
INSERT INTO `t_role_permission` VALUES ('403', '5', '49', '2017-03-07 15:49:09');
INSERT INTO `t_role_permission` VALUES ('406', '5', '52', '2017-03-07 15:49:09');
INSERT INTO `t_role_permission` VALUES ('407', '5', '53', '2017-03-07 15:49:09');
INSERT INTO `t_role_permission` VALUES ('408', '5', '54', '2017-03-07 15:49:09');
INSERT INTO `t_role_permission` VALUES ('409', '5', '55', '2017-03-07 15:49:09');
INSERT INTO `t_role_permission` VALUES ('410', '5', '56', '2017-03-07 15:49:09');
INSERT INTO `t_role_permission` VALUES ('411', '5', '57', '2017-03-07 15:49:09');
INSERT INTO `t_role_permission` VALUES ('412', '5', '58', '2017-03-07 15:49:09');
INSERT INTO `t_role_permission` VALUES ('413', '5', '59', '2017-03-07 15:49:09');
INSERT INTO `t_role_permission` VALUES ('414', '5', '60', '2017-03-07 15:49:09');
INSERT INTO `t_role_permission` VALUES ('415', '5', '61', '2017-03-07 15:49:09');
INSERT INTO `t_role_permission` VALUES ('416', '5', '62', '2017-03-07 15:49:09');
INSERT INTO `t_role_permission` VALUES ('417', '5', '63', '2017-03-07 15:49:09');
INSERT INTO `t_role_permission` VALUES ('418', '5', '64', '2017-03-07 15:49:09');
INSERT INTO `t_role_permission` VALUES ('419', '5', '65', '2017-03-07 15:49:09');
INSERT INTO `t_role_permission` VALUES ('420', '5', '66', '2017-03-07 15:49:09');
INSERT INTO `t_role_permission` VALUES ('421', '5', '67', '2017-03-07 15:49:09');
INSERT INTO `t_role_permission` VALUES ('422', '5', '68', '2017-03-07 15:49:09');
INSERT INTO `t_role_permission` VALUES ('423', '5', '69', '2017-03-07 15:49:09');
INSERT INTO `t_role_permission` VALUES ('424', '5', '70', '2017-03-07 15:49:09');
INSERT INTO `t_role_permission` VALUES ('425', '5', '71', '2017-03-07 15:49:09');
INSERT INTO `t_role_permission` VALUES ('426', '5', '74', '2017-03-07 15:49:09');
INSERT INTO `t_role_permission` VALUES ('427', '5', '75', '2017-03-07 15:49:09');
INSERT INTO `t_role_permission` VALUES ('428', '5', '76', '2017-03-07 15:49:09');
INSERT INTO `t_role_permission` VALUES ('429', '5', '77', '2017-03-07 15:49:09');
INSERT INTO `t_role_permission` VALUES ('430', '5', '78', '2017-03-07 15:49:09');
INSERT INTO `t_role_permission` VALUES ('431', '5', '79', '2017-03-07 15:49:09');
INSERT INTO `t_role_permission` VALUES ('432', '5', '80', '2017-03-07 15:49:09');
INSERT INTO `t_role_permission` VALUES ('433', '5', '81', '2017-03-07 15:49:09');
INSERT INTO `t_role_permission` VALUES ('434', '5', '82', '2017-03-07 15:49:09');
INSERT INTO `t_role_permission` VALUES ('435', '5', '83', '2017-03-07 15:49:09');
INSERT INTO `t_role_permission` VALUES ('436', '5', '84', '2017-03-07 15:49:09');
INSERT INTO `t_role_permission` VALUES ('437', '5', '85', '2017-03-07 15:49:09');
INSERT INTO `t_role_permission` VALUES ('438', '5', '86', '2017-03-07 15:49:09');
INSERT INTO `t_role_permission` VALUES ('439', '5', '87', '2017-03-07 15:49:09');
INSERT INTO `t_role_permission` VALUES ('440', '5', '88', '2017-03-07 15:49:09');
INSERT INTO `t_role_permission` VALUES ('441', '5', '89', '2017-03-07 15:49:09');
INSERT INTO `t_role_permission` VALUES ('442', '5', '90', '2017-03-07 15:49:09');
INSERT INTO `t_role_permission` VALUES ('443', '5', '91', '2017-03-07 15:49:09');
INSERT INTO `t_role_permission` VALUES ('444', '5', '92', '2017-03-07 15:49:09');
INSERT INTO `t_role_permission` VALUES ('445', '5', '93', '2017-03-07 15:49:09');
INSERT INTO `t_role_permission` VALUES ('446', '5', '94', '2017-03-07 15:49:09');
INSERT INTO `t_role_permission` VALUES ('447', '5', '95', '2017-03-07 15:49:09');
INSERT INTO `t_role_permission` VALUES ('448', '5', '96', '2017-03-07 15:49:09');
INSERT INTO `t_role_permission` VALUES ('449', '5', '97', '2017-03-07 15:49:09');
INSERT INTO `t_role_permission` VALUES ('450', '5', '98', '2017-03-07 15:49:09');
INSERT INTO `t_role_permission` VALUES ('451', '5', '99', '2017-03-07 15:49:09');
INSERT INTO `t_role_permission` VALUES ('452', '5', '100', '2017-03-07 15:49:09');
INSERT INTO `t_role_permission` VALUES ('453', '5', '102', '2017-03-07 15:49:09');
INSERT INTO `t_role_permission` VALUES ('454', '5', '104', '2017-03-07 15:49:09');
INSERT INTO `t_role_permission` VALUES ('455', '5', '107', '2017-03-07 15:49:09');
INSERT INTO `t_role_permission` VALUES ('456', '5', '108', '2017-03-07 15:49:09');
INSERT INTO `t_role_permission` VALUES ('457', '5', '109', '2017-03-07 15:49:09');
INSERT INTO `t_role_permission` VALUES ('458', '5', '110', '2017-03-07 15:49:09');
INSERT INTO `t_role_permission` VALUES ('459', '5', '111', '2017-03-07 15:49:09');
INSERT INTO `t_role_permission` VALUES ('460', '5', '112', '2017-03-07 15:49:09');
INSERT INTO `t_role_permission` VALUES ('461', '5', '113', '2017-03-07 15:49:09');
INSERT INTO `t_role_permission` VALUES ('462', '5', '114', '2017-03-07 15:49:09');
INSERT INTO `t_role_permission` VALUES ('463', '5', '117', '2017-03-07 15:49:09');
INSERT INTO `t_role_permission` VALUES ('464', '5', '118', '2017-03-07 15:49:09');
INSERT INTO `t_role_permission` VALUES ('465', '5', '119', '2017-03-07 15:49:09');
INSERT INTO `t_role_permission` VALUES ('466', '5', '121', '2017-03-07 15:49:09');
INSERT INTO `t_role_permission` VALUES ('467', '5', '124', '2017-03-07 15:49:09');
INSERT INTO `t_role_permission` VALUES ('468', '5', '125', '2017-03-07 15:49:09');
INSERT INTO `t_role_permission` VALUES ('469', '5', '126', '2017-03-07 15:49:09');
INSERT INTO `t_role_permission` VALUES ('470', '5', '127', '2017-03-07 15:49:09');
INSERT INTO `t_role_permission` VALUES ('471', '5', '128', '2017-03-07 15:49:09');
INSERT INTO `t_role_permission` VALUES ('472', '5', '129', '2017-03-07 15:49:09');
INSERT INTO `t_role_permission` VALUES ('473', '5', '130', '2017-03-07 15:49:09');
INSERT INTO `t_role_permission` VALUES ('474', '5', '131', '2017-03-07 15:49:09');
INSERT INTO `t_role_permission` VALUES ('475', '5', '132', '2017-03-07 15:49:09');
INSERT INTO `t_role_permission` VALUES ('476', '5', '133', '2017-03-07 15:49:09');
INSERT INTO `t_role_permission` VALUES ('477', '5', '134', '2017-03-07 15:49:09');
INSERT INTO `t_role_permission` VALUES ('478', '5', '135', '2017-03-07 15:49:09');
INSERT INTO `t_role_permission` VALUES ('479', '5', '136', '2017-03-07 15:49:09');
INSERT INTO `t_role_permission` VALUES ('480', '5', '137', '2017-03-07 15:49:09');
INSERT INTO `t_role_permission` VALUES ('481', '5', '138', '2017-03-07 15:49:09');
INSERT INTO `t_role_permission` VALUES ('483', '5', '140', '2017-03-07 15:49:09');
INSERT INTO `t_role_permission` VALUES ('484', '5', '141', '2017-03-07 15:49:09');
INSERT INTO `t_role_permission` VALUES ('485', '5', '142', '2017-03-07 15:49:09');
INSERT INTO `t_role_permission` VALUES ('486', '2', '137', '2017-03-07 15:49:33');
INSERT INTO `t_role_permission` VALUES ('487', '3', '137', '2017-03-07 15:49:33');
INSERT INTO `t_role_permission` VALUES ('488', '4', '137', '2017-03-07 15:49:33');
INSERT INTO `t_role_permission` VALUES ('489', '6', '137', '2017-03-07 15:49:33');
INSERT INTO `t_role_permission` VALUES ('490', '7', '137', '2017-03-07 15:49:33');
INSERT INTO `t_role_permission` VALUES ('491', '8', '137', '2017-03-07 15:49:33');
INSERT INTO `t_role_permission` VALUES ('492', '9', '137', '2017-03-07 15:49:33');
INSERT INTO `t_role_permission` VALUES ('493', '10', '137', '2017-03-07 15:49:33');
INSERT INTO `t_role_permission` VALUES ('494', '11', '137', '2017-03-07 15:49:33');
INSERT INTO `t_role_permission` VALUES ('495', '12', '137', '2017-03-07 15:49:33');
INSERT INTO `t_role_permission` VALUES ('496', '13', '137', '2017-03-07 15:49:33');
INSERT INTO `t_role_permission` VALUES ('497', '4', '3', '2017-03-07 15:57:02');
INSERT INTO `t_role_permission` VALUES ('498', '4', '18', '2017-03-07 15:57:02');
INSERT INTO `t_role_permission` VALUES ('499', '4', '19', '2017-03-07 15:57:02');
INSERT INTO `t_role_permission` VALUES ('500', '4', '23', '2017-03-07 15:57:02');
INSERT INTO `t_role_permission` VALUES ('501', '4', '24', '2017-03-07 15:57:02');
INSERT INTO `t_role_permission` VALUES ('502', '4', '25', '2017-03-07 15:57:02');
INSERT INTO `t_role_permission` VALUES ('503', '4', '26', '2017-03-07 15:57:02');
INSERT INTO `t_role_permission` VALUES ('504', '4', '27', '2017-03-07 15:57:02');
INSERT INTO `t_role_permission` VALUES ('505', '4', '28', '2017-03-07 15:57:02');
INSERT INTO `t_role_permission` VALUES ('506', '4', '32', '2017-03-07 15:57:02');
INSERT INTO `t_role_permission` VALUES ('514', '4', '40', '2017-03-07 15:57:02');
INSERT INTO `t_role_permission` VALUES ('515', '4', '41', '2017-03-07 15:57:02');
INSERT INTO `t_role_permission` VALUES ('516', '4', '45', '2017-03-07 15:57:02');
INSERT INTO `t_role_permission` VALUES ('517', '4', '46', '2017-03-07 15:57:02');
INSERT INTO `t_role_permission` VALUES ('518', '4', '49', '2017-03-07 15:57:02');
INSERT INTO `t_role_permission` VALUES ('521', '4', '54', '2017-03-07 15:57:02');
INSERT INTO `t_role_permission` VALUES ('522', '4', '55', '2017-03-07 15:57:02');
INSERT INTO `t_role_permission` VALUES ('523', '4', '57', '2017-03-07 15:57:02');
INSERT INTO `t_role_permission` VALUES ('524', '4', '58', '2017-03-07 15:57:02');
INSERT INTO `t_role_permission` VALUES ('525', '4', '59', '2017-03-07 15:57:02');
INSERT INTO `t_role_permission` VALUES ('526', '4', '60', '2017-03-07 15:57:02');
INSERT INTO `t_role_permission` VALUES ('527', '4', '61', '2017-03-07 15:57:02');
INSERT INTO `t_role_permission` VALUES ('528', '4', '65', '2017-03-07 15:57:02');
INSERT INTO `t_role_permission` VALUES ('529', '4', '66', '2017-03-07 15:57:02');
INSERT INTO `t_role_permission` VALUES ('530', '4', '67', '2017-03-07 15:57:02');
INSERT INTO `t_role_permission` VALUES ('531', '4', '68', '2017-03-07 15:57:02');
INSERT INTO `t_role_permission` VALUES ('532', '4', '69', '2017-03-07 15:57:02');
INSERT INTO `t_role_permission` VALUES ('533', '4', '70', '2017-03-07 15:57:02');
INSERT INTO `t_role_permission` VALUES ('534', '4', '71', '2017-03-07 15:57:02');
INSERT INTO `t_role_permission` VALUES ('535', '4', '78', '2017-03-07 15:57:02');
INSERT INTO `t_role_permission` VALUES ('536', '4', '79', '2017-03-07 15:57:02');
INSERT INTO `t_role_permission` VALUES ('537', '4', '80', '2017-03-07 15:57:02');
INSERT INTO `t_role_permission` VALUES ('538', '4', '81', '2017-03-07 15:57:02');
INSERT INTO `t_role_permission` VALUES ('539', '4', '82', '2017-03-07 15:57:02');
INSERT INTO `t_role_permission` VALUES ('540', '4', '83', '2017-03-07 15:57:02');
INSERT INTO `t_role_permission` VALUES ('541', '4', '84', '2017-03-07 15:57:02');
INSERT INTO `t_role_permission` VALUES ('542', '4', '85', '2017-03-07 15:57:02');
INSERT INTO `t_role_permission` VALUES ('543', '4', '86', '2017-03-07 15:57:02');
INSERT INTO `t_role_permission` VALUES ('544', '4', '87', '2017-03-07 15:57:02');
INSERT INTO `t_role_permission` VALUES ('545', '4', '89', '2017-03-07 15:57:02');
INSERT INTO `t_role_permission` VALUES ('546', '4', '90', '2017-03-07 15:57:02');
INSERT INTO `t_role_permission` VALUES ('547', '4', '91', '2017-03-07 15:57:02');
INSERT INTO `t_role_permission` VALUES ('548', '4', '92', '2017-03-07 15:57:02');
INSERT INTO `t_role_permission` VALUES ('549', '4', '93', '2017-03-07 15:57:02');
INSERT INTO `t_role_permission` VALUES ('550', '4', '94', '2017-03-07 15:57:02');
INSERT INTO `t_role_permission` VALUES ('551', '4', '95', '2017-03-07 15:57:02');
INSERT INTO `t_role_permission` VALUES ('552', '4', '96', '2017-03-07 15:57:02');
INSERT INTO `t_role_permission` VALUES ('553', '4', '97', '2017-03-07 15:57:02');
INSERT INTO `t_role_permission` VALUES ('554', '4', '99', '2017-03-07 15:57:02');
INSERT INTO `t_role_permission` VALUES ('555', '4', '100', '2017-03-07 15:57:02');
INSERT INTO `t_role_permission` VALUES ('556', '4', '104', '2017-03-07 15:57:02');
INSERT INTO `t_role_permission` VALUES ('557', '4', '107', '2017-03-07 15:57:02');
INSERT INTO `t_role_permission` VALUES ('558', '4', '108', '2017-03-07 15:57:02');
INSERT INTO `t_role_permission` VALUES ('559', '4', '109', '2017-03-07 15:57:02');
INSERT INTO `t_role_permission` VALUES ('560', '4', '110', '2017-03-07 15:57:02');
INSERT INTO `t_role_permission` VALUES ('561', '4', '111', '2017-03-07 15:57:02');
INSERT INTO `t_role_permission` VALUES ('562', '4', '112', '2017-03-07 15:57:02');
INSERT INTO `t_role_permission` VALUES ('563', '4', '113', '2017-03-07 15:57:02');
INSERT INTO `t_role_permission` VALUES ('564', '4', '121', '2017-03-07 15:57:02');
INSERT INTO `t_role_permission` VALUES ('565', '4', '124', '2017-03-07 15:57:02');
INSERT INTO `t_role_permission` VALUES ('566', '4', '125', '2017-03-07 15:57:02');
INSERT INTO `t_role_permission` VALUES ('567', '4', '126', '2017-03-07 15:57:02');
INSERT INTO `t_role_permission` VALUES ('568', '4', '127', '2017-03-07 15:57:02');
INSERT INTO `t_role_permission` VALUES ('569', '4', '128', '2017-03-07 15:57:02');
INSERT INTO `t_role_permission` VALUES ('570', '4', '129', '2017-03-07 15:57:02');
INSERT INTO `t_role_permission` VALUES ('571', '4', '130', '2017-03-07 15:57:02');
INSERT INTO `t_role_permission` VALUES ('572', '4', '131', '2017-03-07 15:57:02');
INSERT INTO `t_role_permission` VALUES ('573', '4', '133', '2017-03-07 15:57:02');
INSERT INTO `t_role_permission` VALUES ('574', '4', '134', '2017-03-07 15:57:02');
INSERT INTO `t_role_permission` VALUES ('575', '4', '135', '2017-03-07 15:57:02');
INSERT INTO `t_role_permission` VALUES ('576', '4', '136', '2017-03-07 15:57:02');
INSERT INTO `t_role_permission` VALUES ('577', '4', '141', '2017-03-07 15:57:02');
INSERT INTO `t_role_permission` VALUES ('578', '6', '3', '2017-03-07 16:03:04');
INSERT INTO `t_role_permission` VALUES ('579', '6', '18', '2017-03-07 16:03:04');
INSERT INTO `t_role_permission` VALUES ('580', '6', '19', '2017-03-07 16:03:04');
INSERT INTO `t_role_permission` VALUES ('581', '6', '20', '2017-03-07 16:03:04');
INSERT INTO `t_role_permission` VALUES ('582', '6', '25', '2017-03-07 16:03:04');
INSERT INTO `t_role_permission` VALUES ('583', '6', '28', '2017-03-07 16:03:04');
INSERT INTO `t_role_permission` VALUES ('584', '6', '29', '2017-03-07 16:03:04');
INSERT INTO `t_role_permission` VALUES ('585', '6', '30', '2017-03-07 16:03:04');
INSERT INTO `t_role_permission` VALUES ('586', '6', '31', '2017-03-07 16:03:04');
INSERT INTO `t_role_permission` VALUES ('587', '6', '41', '2017-03-07 16:03:04');
INSERT INTO `t_role_permission` VALUES ('588', '6', '46', '2017-03-07 16:03:04');
INSERT INTO `t_role_permission` VALUES ('589', '6', '49', '2017-03-07 16:03:04');
INSERT INTO `t_role_permission` VALUES ('590', '6', '50', '2017-03-07 16:03:04');
INSERT INTO `t_role_permission` VALUES ('591', '6', '51', '2017-03-07 16:03:04');
INSERT INTO `t_role_permission` VALUES ('592', '6', '54', '2017-03-07 16:03:04');
INSERT INTO `t_role_permission` VALUES ('593', '6', '57', '2017-03-07 16:03:04');
INSERT INTO `t_role_permission` VALUES ('594', '6', '58', '2017-03-07 16:03:04');
INSERT INTO `t_role_permission` VALUES ('595', '6', '59', '2017-03-07 16:03:04');
INSERT INTO `t_role_permission` VALUES ('596', '6', '60', '2017-03-07 16:03:04');
INSERT INTO `t_role_permission` VALUES ('597', '6', '61', '2017-03-07 16:03:04');
INSERT INTO `t_role_permission` VALUES ('598', '6', '65', '2017-03-07 16:03:04');
INSERT INTO `t_role_permission` VALUES ('599', '6', '66', '2017-03-07 16:03:04');
INSERT INTO `t_role_permission` VALUES ('600', '6', '67', '2017-03-07 16:03:04');
INSERT INTO `t_role_permission` VALUES ('601', '6', '68', '2017-03-07 16:03:04');
INSERT INTO `t_role_permission` VALUES ('602', '6', '69', '2017-03-07 16:03:04');
INSERT INTO `t_role_permission` VALUES ('603', '6', '71', '2017-03-07 16:03:04');
INSERT INTO `t_role_permission` VALUES ('604', '6', '76', '2017-03-07 16:03:04');
INSERT INTO `t_role_permission` VALUES ('605', '6', '77', '2017-03-07 16:03:04');
INSERT INTO `t_role_permission` VALUES ('606', '6', '78', '2017-03-07 16:03:04');
INSERT INTO `t_role_permission` VALUES ('607', '6', '79', '2017-03-07 16:03:04');
INSERT INTO `t_role_permission` VALUES ('608', '6', '80', '2017-03-07 16:03:04');
INSERT INTO `t_role_permission` VALUES ('609', '6', '81', '2017-03-07 16:03:04');
INSERT INTO `t_role_permission` VALUES ('610', '6', '82', '2017-03-07 16:03:04');
INSERT INTO `t_role_permission` VALUES ('611', '6', '83', '2017-03-07 16:03:04');
INSERT INTO `t_role_permission` VALUES ('612', '6', '84', '2017-03-07 16:03:04');
INSERT INTO `t_role_permission` VALUES ('613', '6', '85', '2017-03-07 16:03:04');
INSERT INTO `t_role_permission` VALUES ('614', '6', '86', '2017-03-07 16:03:04');
INSERT INTO `t_role_permission` VALUES ('615', '6', '87', '2017-03-07 16:03:04');
INSERT INTO `t_role_permission` VALUES ('616', '6', '90', '2017-03-07 16:03:04');
INSERT INTO `t_role_permission` VALUES ('617', '6', '91', '2017-03-07 16:03:04');
INSERT INTO `t_role_permission` VALUES ('618', '6', '92', '2017-03-07 16:03:04');
INSERT INTO `t_role_permission` VALUES ('619', '6', '93', '2017-03-07 16:03:04');
INSERT INTO `t_role_permission` VALUES ('620', '6', '94', '2017-03-07 16:03:04');
INSERT INTO `t_role_permission` VALUES ('621', '6', '95', '2017-03-07 16:03:04');
INSERT INTO `t_role_permission` VALUES ('622', '6', '96', '2017-03-07 16:03:04');
INSERT INTO `t_role_permission` VALUES ('623', '6', '100', '2017-03-07 16:03:04');
INSERT INTO `t_role_permission` VALUES ('624', '6', '102', '2017-03-07 16:03:04');
INSERT INTO `t_role_permission` VALUES ('625', '6', '107', '2017-03-07 16:03:04');
INSERT INTO `t_role_permission` VALUES ('626', '6', '108', '2017-03-07 16:03:04');
INSERT INTO `t_role_permission` VALUES ('627', '6', '109', '2017-03-07 16:03:04');
INSERT INTO `t_role_permission` VALUES ('628', '6', '110', '2017-03-07 16:03:04');
INSERT INTO `t_role_permission` VALUES ('629', '6', '111', '2017-03-07 16:03:04');
INSERT INTO `t_role_permission` VALUES ('630', '6', '112', '2017-03-07 16:03:04');
INSERT INTO `t_role_permission` VALUES ('631', '6', '113', '2017-03-07 16:03:04');
INSERT INTO `t_role_permission` VALUES ('632', '6', '115', '2017-03-07 16:03:04');
INSERT INTO `t_role_permission` VALUES ('633', '6', '116', '2017-03-07 16:03:04');
INSERT INTO `t_role_permission` VALUES ('634', '6', '117', '2017-03-07 16:03:04');
INSERT INTO `t_role_permission` VALUES ('635', '6', '118', '2017-03-07 16:03:04');
INSERT INTO `t_role_permission` VALUES ('636', '6', '119', '2017-03-07 16:03:04');
INSERT INTO `t_role_permission` VALUES ('637', '6', '120', '2017-03-07 16:03:04');
INSERT INTO `t_role_permission` VALUES ('638', '6', '121', '2017-03-07 16:03:04');
INSERT INTO `t_role_permission` VALUES ('639', '6', '122', '2017-03-07 16:03:04');
INSERT INTO `t_role_permission` VALUES ('640', '6', '123', '2017-03-07 16:03:04');
INSERT INTO `t_role_permission` VALUES ('641', '6', '124', '2017-03-07 16:03:04');
INSERT INTO `t_role_permission` VALUES ('642', '6', '125', '2017-03-07 16:03:04');
INSERT INTO `t_role_permission` VALUES ('643', '6', '126', '2017-03-07 16:03:04');
INSERT INTO `t_role_permission` VALUES ('644', '6', '130', '2017-03-07 16:03:04');
INSERT INTO `t_role_permission` VALUES ('645', '6', '131', '2017-03-07 16:03:04');
INSERT INTO `t_role_permission` VALUES ('646', '6', '132', '2017-03-07 16:03:04');
INSERT INTO `t_role_permission` VALUES ('647', '6', '133', '2017-03-07 16:03:04');
INSERT INTO `t_role_permission` VALUES ('648', '6', '134', '2017-03-07 16:03:04');
INSERT INTO `t_role_permission` VALUES ('649', '6', '135', '2017-03-07 16:03:04');
INSERT INTO `t_role_permission` VALUES ('650', '6', '136', '2017-03-07 16:03:04');
INSERT INTO `t_role_permission` VALUES ('651', '6', '141', '2017-03-07 16:03:04');
INSERT INTO `t_role_permission` VALUES ('652', '7', '3', '2017-03-07 16:08:28');
INSERT INTO `t_role_permission` VALUES ('653', '7', '15', '2017-03-07 16:08:28');
INSERT INTO `t_role_permission` VALUES ('654', '7', '16', '2017-03-07 16:08:28');
INSERT INTO `t_role_permission` VALUES ('655', '7', '18', '2017-03-07 16:08:28');
INSERT INTO `t_role_permission` VALUES ('656', '7', '20', '2017-03-07 16:08:28');
INSERT INTO `t_role_permission` VALUES ('657', '7', '21', '2017-03-07 16:08:28');
INSERT INTO `t_role_permission` VALUES ('658', '7', '28', '2017-03-07 16:08:28');
INSERT INTO `t_role_permission` VALUES ('659', '7', '29', '2017-03-07 16:08:28');
INSERT INTO `t_role_permission` VALUES ('660', '7', '30', '2017-03-07 16:08:28');
INSERT INTO `t_role_permission` VALUES ('661', '7', '31', '2017-03-07 16:08:28');
INSERT INTO `t_role_permission` VALUES ('662', '7', '41', '2017-03-07 16:08:28');
INSERT INTO `t_role_permission` VALUES ('663', '7', '54', '2017-03-07 16:08:28');
INSERT INTO `t_role_permission` VALUES ('664', '7', '55', '2017-03-07 16:08:28');
INSERT INTO `t_role_permission` VALUES ('665', '7', '56', '2017-03-07 16:08:28');
INSERT INTO `t_role_permission` VALUES ('666', '7', '57', '2017-03-07 16:08:28');
INSERT INTO `t_role_permission` VALUES ('667', '7', '58', '2017-03-07 16:08:28');
INSERT INTO `t_role_permission` VALUES ('668', '7', '59', '2017-03-07 16:08:28');
INSERT INTO `t_role_permission` VALUES ('669', '7', '60', '2017-03-07 16:08:28');
INSERT INTO `t_role_permission` VALUES ('670', '7', '61', '2017-03-07 16:08:28');
INSERT INTO `t_role_permission` VALUES ('671', '7', '65', '2017-03-07 16:08:28');
INSERT INTO `t_role_permission` VALUES ('672', '7', '66', '2017-03-07 16:08:28');
INSERT INTO `t_role_permission` VALUES ('673', '7', '67', '2017-03-07 16:08:28');
INSERT INTO `t_role_permission` VALUES ('674', '7', '68', '2017-03-07 16:08:28');
INSERT INTO `t_role_permission` VALUES ('675', '7', '69', '2017-03-07 16:08:28');
INSERT INTO `t_role_permission` VALUES ('676', '7', '71', '2017-03-07 16:08:28');
INSERT INTO `t_role_permission` VALUES ('677', '7', '76', '2017-03-07 16:08:28');
INSERT INTO `t_role_permission` VALUES ('678', '7', '77', '2017-03-07 16:08:28');
INSERT INTO `t_role_permission` VALUES ('679', '7', '78', '2017-03-07 16:08:28');
INSERT INTO `t_role_permission` VALUES ('680', '7', '79', '2017-03-07 16:08:28');
INSERT INTO `t_role_permission` VALUES ('681', '7', '80', '2017-03-07 16:08:28');
INSERT INTO `t_role_permission` VALUES ('682', '7', '81', '2017-03-07 16:08:28');
INSERT INTO `t_role_permission` VALUES ('683', '7', '82', '2017-03-07 16:08:28');
INSERT INTO `t_role_permission` VALUES ('684', '7', '83', '2017-03-07 16:08:28');
INSERT INTO `t_role_permission` VALUES ('685', '7', '84', '2017-03-07 16:08:28');
INSERT INTO `t_role_permission` VALUES ('686', '7', '85', '2017-03-07 16:08:28');
INSERT INTO `t_role_permission` VALUES ('687', '7', '86', '2017-03-07 16:08:28');
INSERT INTO `t_role_permission` VALUES ('688', '7', '87', '2017-03-07 16:08:28');
INSERT INTO `t_role_permission` VALUES ('689', '7', '90', '2017-03-07 16:08:28');
INSERT INTO `t_role_permission` VALUES ('690', '7', '91', '2017-03-07 16:08:28');
INSERT INTO `t_role_permission` VALUES ('691', '7', '92', '2017-03-07 16:08:28');
INSERT INTO `t_role_permission` VALUES ('692', '7', '93', '2017-03-07 16:08:28');
INSERT INTO `t_role_permission` VALUES ('693', '7', '94', '2017-03-07 16:08:28');
INSERT INTO `t_role_permission` VALUES ('694', '7', '95', '2017-03-07 16:08:28');
INSERT INTO `t_role_permission` VALUES ('695', '7', '96', '2017-03-07 16:08:28');
INSERT INTO `t_role_permission` VALUES ('696', '7', '99', '2017-03-07 16:08:28');
INSERT INTO `t_role_permission` VALUES ('697', '7', '100', '2017-03-07 16:08:28');
INSERT INTO `t_role_permission` VALUES ('698', '7', '102', '2017-03-07 16:08:28');
INSERT INTO `t_role_permission` VALUES ('699', '7', '107', '2017-03-07 16:08:28');
INSERT INTO `t_role_permission` VALUES ('700', '7', '108', '2017-03-07 16:08:28');
INSERT INTO `t_role_permission` VALUES ('701', '7', '109', '2017-03-07 16:08:28');
INSERT INTO `t_role_permission` VALUES ('702', '7', '110', '2017-03-07 16:08:28');
INSERT INTO `t_role_permission` VALUES ('703', '7', '111', '2017-03-07 16:08:28');
INSERT INTO `t_role_permission` VALUES ('704', '7', '112', '2017-03-07 16:08:28');
INSERT INTO `t_role_permission` VALUES ('705', '7', '113', '2017-03-07 16:08:28');
INSERT INTO `t_role_permission` VALUES ('706', '7', '114', '2017-03-07 16:08:28');
INSERT INTO `t_role_permission` VALUES ('707', '7', '115', '2017-03-07 16:08:28');
INSERT INTO `t_role_permission` VALUES ('708', '7', '116', '2017-03-07 16:08:28');
INSERT INTO `t_role_permission` VALUES ('709', '7', '117', '2017-03-07 16:08:28');
INSERT INTO `t_role_permission` VALUES ('710', '7', '118', '2017-03-07 16:08:28');
INSERT INTO `t_role_permission` VALUES ('711', '7', '119', '2017-03-07 16:08:28');
INSERT INTO `t_role_permission` VALUES ('712', '7', '120', '2017-03-07 16:08:28');
INSERT INTO `t_role_permission` VALUES ('713', '7', '121', '2017-03-07 16:08:28');
INSERT INTO `t_role_permission` VALUES ('714', '7', '122', '2017-03-07 16:08:28');
INSERT INTO `t_role_permission` VALUES ('715', '7', '123', '2017-03-07 16:08:28');
INSERT INTO `t_role_permission` VALUES ('716', '7', '124', '2017-03-07 16:08:28');
INSERT INTO `t_role_permission` VALUES ('717', '7', '125', '2017-03-07 16:08:28');
INSERT INTO `t_role_permission` VALUES ('718', '7', '126', '2017-03-07 16:08:28');
INSERT INTO `t_role_permission` VALUES ('719', '7', '130', '2017-03-07 16:08:28');
INSERT INTO `t_role_permission` VALUES ('720', '7', '132', '2017-03-07 16:08:28');
INSERT INTO `t_role_permission` VALUES ('721', '7', '133', '2017-03-07 16:08:28');
INSERT INTO `t_role_permission` VALUES ('722', '7', '134', '2017-03-07 16:08:28');
INSERT INTO `t_role_permission` VALUES ('723', '7', '135', '2017-03-07 16:08:28');
INSERT INTO `t_role_permission` VALUES ('724', '7', '136', '2017-03-07 16:08:28');
INSERT INTO `t_role_permission` VALUES ('725', '7', '138', '2017-03-07 16:08:28');
INSERT INTO `t_role_permission` VALUES ('727', '7', '140', '2017-03-07 16:08:28');
INSERT INTO `t_role_permission` VALUES ('728', '7', '141', '2017-03-07 16:08:28');
INSERT INTO `t_role_permission` VALUES ('729', '8', '10', '2017-03-07 16:13:38');
INSERT INTO `t_role_permission` VALUES ('730', '8', '11', '2017-03-07 16:13:38');
INSERT INTO `t_role_permission` VALUES ('731', '8', '12', '2017-03-07 16:13:38');
INSERT INTO `t_role_permission` VALUES ('732', '8', '13', '2017-03-07 16:13:38');
INSERT INTO `t_role_permission` VALUES ('733', '8', '15', '2017-03-07 16:13:38');
INSERT INTO `t_role_permission` VALUES ('734', '8', '16', '2017-03-07 16:13:38');
INSERT INTO `t_role_permission` VALUES ('735', '8', '17', '2017-03-07 16:13:38');
INSERT INTO `t_role_permission` VALUES ('736', '8', '18', '2017-03-07 16:13:38');
INSERT INTO `t_role_permission` VALUES ('737', '8', '19', '2017-03-07 16:13:38');
INSERT INTO `t_role_permission` VALUES ('738', '8', '20', '2017-03-07 16:13:38');
INSERT INTO `t_role_permission` VALUES ('739', '8', '21', '2017-03-07 16:13:38');
INSERT INTO `t_role_permission` VALUES ('740', '8', '22', '2017-03-07 16:13:38');
INSERT INTO `t_role_permission` VALUES ('741', '8', '23', '2017-03-07 16:13:38');
INSERT INTO `t_role_permission` VALUES ('742', '8', '24', '2017-03-07 16:13:38');
INSERT INTO `t_role_permission` VALUES ('743', '8', '25', '2017-03-07 16:13:38');
INSERT INTO `t_role_permission` VALUES ('744', '8', '26', '2017-03-07 16:13:38');
INSERT INTO `t_role_permission` VALUES ('745', '8', '27', '2017-03-07 16:13:38');
INSERT INTO `t_role_permission` VALUES ('746', '8', '28', '2017-03-07 16:13:38');
INSERT INTO `t_role_permission` VALUES ('747', '8', '32', '2017-03-07 16:13:38');
INSERT INTO `t_role_permission` VALUES ('755', '8', '40', '2017-03-07 16:13:38');
INSERT INTO `t_role_permission` VALUES ('756', '8', '41', '2017-03-07 16:13:38');
INSERT INTO `t_role_permission` VALUES ('757', '8', '42', '2017-03-07 16:13:38');
INSERT INTO `t_role_permission` VALUES ('758', '8', '43', '2017-03-07 16:13:38');
INSERT INTO `t_role_permission` VALUES ('759', '8', '55', '2017-03-07 16:13:38');
INSERT INTO `t_role_permission` VALUES ('760', '8', '56', '2017-03-07 16:13:38');
INSERT INTO `t_role_permission` VALUES ('761', '8', '57', '2017-03-07 16:13:38');
INSERT INTO `t_role_permission` VALUES ('762', '8', '61', '2017-03-07 16:13:38');
INSERT INTO `t_role_permission` VALUES ('763', '8', '62', '2017-03-07 16:13:38');
INSERT INTO `t_role_permission` VALUES ('764', '8', '63', '2017-03-07 16:13:38');
INSERT INTO `t_role_permission` VALUES ('765', '8', '64', '2017-03-07 16:13:38');
INSERT INTO `t_role_permission` VALUES ('766', '8', '65', '2017-03-07 16:13:38');
INSERT INTO `t_role_permission` VALUES ('767', '8', '66', '2017-03-07 16:13:38');
INSERT INTO `t_role_permission` VALUES ('768', '8', '67', '2017-03-07 16:13:38');
INSERT INTO `t_role_permission` VALUES ('769', '8', '68', '2017-03-07 16:13:38');
INSERT INTO `t_role_permission` VALUES ('770', '8', '69', '2017-03-07 16:13:38');
INSERT INTO `t_role_permission` VALUES ('771', '8', '70', '2017-03-07 16:13:38');
INSERT INTO `t_role_permission` VALUES ('772', '8', '73', '2017-03-07 16:13:38');
INSERT INTO `t_role_permission` VALUES ('773', '8', '74', '2017-03-07 16:13:38');
INSERT INTO `t_role_permission` VALUES ('774', '8', '75', '2017-03-07 16:13:38');
INSERT INTO `t_role_permission` VALUES ('775', '8', '76', '2017-03-07 16:13:38');
INSERT INTO `t_role_permission` VALUES ('776', '8', '77', '2017-03-07 16:13:38');
INSERT INTO `t_role_permission` VALUES ('777', '8', '79', '2017-03-07 16:13:38');
INSERT INTO `t_role_permission` VALUES ('778', '8', '80', '2017-03-07 16:13:38');
INSERT INTO `t_role_permission` VALUES ('779', '8', '81', '2017-03-07 16:13:38');
INSERT INTO `t_role_permission` VALUES ('780', '8', '82', '2017-03-07 16:13:38');
INSERT INTO `t_role_permission` VALUES ('781', '8', '83', '2017-03-07 16:13:38');
INSERT INTO `t_role_permission` VALUES ('782', '8', '84', '2017-03-07 16:13:38');
INSERT INTO `t_role_permission` VALUES ('783', '8', '85', '2017-03-07 16:13:38');
INSERT INTO `t_role_permission` VALUES ('784', '8', '88', '2017-03-07 16:13:38');
INSERT INTO `t_role_permission` VALUES ('785', '8', '89', '2017-03-07 16:13:38');
INSERT INTO `t_role_permission` VALUES ('786', '8', '100', '2017-03-07 16:13:38');
INSERT INTO `t_role_permission` VALUES ('787', '8', '104', '2017-03-07 16:13:38');
INSERT INTO `t_role_permission` VALUES ('788', '8', '105', '2017-03-07 16:13:38');
INSERT INTO `t_role_permission` VALUES ('789', '8', '106', '2017-03-07 16:13:38');
INSERT INTO `t_role_permission` VALUES ('790', '8', '113', '2017-03-07 16:13:38');
INSERT INTO `t_role_permission` VALUES ('791', '8', '115', '2017-03-07 16:13:38');
INSERT INTO `t_role_permission` VALUES ('792', '8', '118', '2017-03-07 16:13:38');
INSERT INTO `t_role_permission` VALUES ('793', '8', '119', '2017-03-07 16:13:38');
INSERT INTO `t_role_permission` VALUES ('794', '8', '120', '2017-03-07 16:13:38');
INSERT INTO `t_role_permission` VALUES ('795', '8', '122', '2017-03-07 16:13:38');
INSERT INTO `t_role_permission` VALUES ('796', '8', '123', '2017-03-07 16:13:38');
INSERT INTO `t_role_permission` VALUES ('797', '8', '124', '2017-03-07 16:13:38');
INSERT INTO `t_role_permission` VALUES ('798', '8', '126', '2017-03-07 16:13:38');
INSERT INTO `t_role_permission` VALUES ('799', '8', '127', '2017-03-07 16:13:38');
INSERT INTO `t_role_permission` VALUES ('800', '8', '128', '2017-03-07 16:13:38');
INSERT INTO `t_role_permission` VALUES ('801', '8', '129', '2017-03-07 16:13:38');
INSERT INTO `t_role_permission` VALUES ('802', '8', '130', '2017-03-07 16:13:38');
INSERT INTO `t_role_permission` VALUES ('803', '8', '138', '2017-03-07 16:13:38');
INSERT INTO `t_role_permission` VALUES ('804', '8', '140', '2017-03-07 16:13:38');
INSERT INTO `t_role_permission` VALUES ('805', '8', '141', '2017-03-07 16:13:38');
INSERT INTO `t_role_permission` VALUES ('806', '10', '57', '2017-03-07 16:17:10');
INSERT INTO `t_role_permission` VALUES ('807', '10', '61', '2017-03-07 16:17:10');
INSERT INTO `t_role_permission` VALUES ('808', '10', '64', '2017-03-07 16:17:10');
INSERT INTO `t_role_permission` VALUES ('809', '10', '65', '2017-03-07 16:17:10');
INSERT INTO `t_role_permission` VALUES ('810', '10', '66', '2017-03-07 16:17:10');
INSERT INTO `t_role_permission` VALUES ('811', '10', '68', '2017-03-07 16:17:10');
INSERT INTO `t_role_permission` VALUES ('812', '10', '69', '2017-03-07 16:17:10');
INSERT INTO `t_role_permission` VALUES ('813', '10', '70', '2017-03-07 16:17:10');
INSERT INTO `t_role_permission` VALUES ('814', '10', '73', '2017-03-07 16:17:10');
INSERT INTO `t_role_permission` VALUES ('815', '10', '74', '2017-03-07 16:17:10');
INSERT INTO `t_role_permission` VALUES ('816', '10', '75', '2017-03-07 16:17:10');
INSERT INTO `t_role_permission` VALUES ('817', '10', '76', '2017-03-07 16:17:10');
INSERT INTO `t_role_permission` VALUES ('818', '10', '77', '2017-03-07 16:17:10');
INSERT INTO `t_role_permission` VALUES ('819', '10', '79', '2017-03-07 16:17:10');
INSERT INTO `t_role_permission` VALUES ('820', '10', '80', '2017-03-07 16:17:10');
INSERT INTO `t_role_permission` VALUES ('821', '10', '81', '2017-03-07 16:17:10');
INSERT INTO `t_role_permission` VALUES ('822', '10', '82', '2017-03-07 16:17:10');
INSERT INTO `t_role_permission` VALUES ('823', '10', '83', '2017-03-07 16:17:10');
INSERT INTO `t_role_permission` VALUES ('824', '10', '84', '2017-03-07 16:17:10');
INSERT INTO `t_role_permission` VALUES ('825', '10', '85', '2017-03-07 16:17:10');
INSERT INTO `t_role_permission` VALUES ('826', '10', '113', '2017-03-07 16:17:10');
INSERT INTO `t_role_permission` VALUES ('827', '10', '118', '2017-03-07 16:17:10');
INSERT INTO `t_role_permission` VALUES ('828', '10', '119', '2017-03-07 16:17:10');
INSERT INTO `t_role_permission` VALUES ('829', '10', '126', '2017-03-07 16:17:10');
INSERT INTO `t_role_permission` VALUES ('830', '10', '130', '2017-03-07 16:17:10');
INSERT INTO `t_role_permission` VALUES ('831', '11', '3', '2017-03-07 16:21:23');
INSERT INTO `t_role_permission` VALUES ('832', '11', '18', '2017-03-07 16:21:23');
INSERT INTO `t_role_permission` VALUES ('833', '11', '23', '2017-03-07 16:21:23');
INSERT INTO `t_role_permission` VALUES ('834', '11', '24', '2017-03-07 16:21:23');
INSERT INTO `t_role_permission` VALUES ('835', '11', '26', '2017-03-07 16:21:23');
INSERT INTO `t_role_permission` VALUES ('836', '11', '46', '2017-03-07 16:21:23');
INSERT INTO `t_role_permission` VALUES ('837', '11', '49', '2017-03-07 16:21:23');
INSERT INTO `t_role_permission` VALUES ('838', '11', '50', '2017-03-07 16:21:23');
INSERT INTO `t_role_permission` VALUES ('839', '11', '51', '2017-03-07 16:21:23');
INSERT INTO `t_role_permission` VALUES ('840', '11', '57', '2017-03-07 16:21:23');
INSERT INTO `t_role_permission` VALUES ('841', '11', '58', '2017-03-07 16:21:23');
INSERT INTO `t_role_permission` VALUES ('842', '11', '59', '2017-03-07 16:21:23');
INSERT INTO `t_role_permission` VALUES ('843', '11', '60', '2017-03-07 16:21:23');
INSERT INTO `t_role_permission` VALUES ('844', '11', '61', '2017-03-07 16:21:23');
INSERT INTO `t_role_permission` VALUES ('845', '11', '65', '2017-03-07 16:21:23');
INSERT INTO `t_role_permission` VALUES ('846', '11', '66', '2017-03-07 16:21:23');
INSERT INTO `t_role_permission` VALUES ('847', '11', '67', '2017-03-07 16:21:23');
INSERT INTO `t_role_permission` VALUES ('848', '11', '68', '2017-03-07 16:21:23');
INSERT INTO `t_role_permission` VALUES ('849', '11', '69', '2017-03-07 16:21:23');
INSERT INTO `t_role_permission` VALUES ('850', '11', '70', '2017-03-07 16:21:23');
INSERT INTO `t_role_permission` VALUES ('851', '11', '71', '2017-03-07 16:21:23');
INSERT INTO `t_role_permission` VALUES ('852', '11', '74', '2017-03-07 16:21:23');
INSERT INTO `t_role_permission` VALUES ('853', '11', '75', '2017-03-07 16:21:23');
INSERT INTO `t_role_permission` VALUES ('854', '11', '79', '2017-03-07 16:21:23');
INSERT INTO `t_role_permission` VALUES ('855', '11', '80', '2017-03-07 16:21:23');
INSERT INTO `t_role_permission` VALUES ('856', '11', '81', '2017-03-07 16:21:23');
INSERT INTO `t_role_permission` VALUES ('857', '11', '82', '2017-03-07 16:21:23');
INSERT INTO `t_role_permission` VALUES ('858', '11', '83', '2017-03-07 16:21:23');
INSERT INTO `t_role_permission` VALUES ('859', '11', '86', '2017-03-07 16:21:23');
INSERT INTO `t_role_permission` VALUES ('860', '11', '87', '2017-03-07 16:21:23');
INSERT INTO `t_role_permission` VALUES ('861', '11', '89', '2017-03-07 16:21:23');
INSERT INTO `t_role_permission` VALUES ('862', '11', '90', '2017-03-07 16:21:23');
INSERT INTO `t_role_permission` VALUES ('863', '11', '91', '2017-03-07 16:21:23');
INSERT INTO `t_role_permission` VALUES ('864', '11', '92', '2017-03-07 16:21:23');
INSERT INTO `t_role_permission` VALUES ('865', '11', '93', '2017-03-07 16:21:23');
INSERT INTO `t_role_permission` VALUES ('866', '11', '94', '2017-03-07 16:21:23');
INSERT INTO `t_role_permission` VALUES ('867', '11', '95', '2017-03-07 16:21:23');
INSERT INTO `t_role_permission` VALUES ('868', '11', '96', '2017-03-07 16:21:23');
INSERT INTO `t_role_permission` VALUES ('869', '11', '99', '2017-03-07 16:21:23');
INSERT INTO `t_role_permission` VALUES ('870', '11', '100', '2017-03-07 16:21:23');
INSERT INTO `t_role_permission` VALUES ('871', '11', '104', '2017-03-07 16:21:23');
INSERT INTO `t_role_permission` VALUES ('872', '11', '107', '2017-03-07 16:21:23');
INSERT INTO `t_role_permission` VALUES ('873', '11', '108', '2017-03-07 16:21:23');
INSERT INTO `t_role_permission` VALUES ('874', '11', '109', '2017-03-07 16:21:23');
INSERT INTO `t_role_permission` VALUES ('875', '11', '110', '2017-03-07 16:21:23');
INSERT INTO `t_role_permission` VALUES ('876', '11', '111', '2017-03-07 16:21:23');
INSERT INTO `t_role_permission` VALUES ('877', '11', '112', '2017-03-07 16:21:23');
INSERT INTO `t_role_permission` VALUES ('878', '11', '113', '2017-03-07 16:21:23');
INSERT INTO `t_role_permission` VALUES ('879', '11', '121', '2017-03-07 16:21:23');
INSERT INTO `t_role_permission` VALUES ('880', '11', '126', '2017-03-07 16:21:23');
INSERT INTO `t_role_permission` VALUES ('881', '11', '127', '2017-03-07 16:21:23');
INSERT INTO `t_role_permission` VALUES ('882', '11', '128', '2017-03-07 16:21:23');
INSERT INTO `t_role_permission` VALUES ('883', '11', '130', '2017-03-07 16:21:23');
INSERT INTO `t_role_permission` VALUES ('884', '11', '141', '2017-03-07 16:21:23');
INSERT INTO `t_role_permission` VALUES ('885', '12', '10', '2017-03-07 16:27:34');
INSERT INTO `t_role_permission` VALUES ('886', '12', '11', '2017-03-07 16:27:34');
INSERT INTO `t_role_permission` VALUES ('887', '12', '12', '2017-03-07 16:27:34');
INSERT INTO `t_role_permission` VALUES ('888', '12', '13', '2017-03-07 16:27:34');
INSERT INTO `t_role_permission` VALUES ('889', '12', '17', '2017-03-07 16:27:34');
INSERT INTO `t_role_permission` VALUES ('890', '12', '18', '2017-03-07 16:27:34');
INSERT INTO `t_role_permission` VALUES ('891', '12', '20', '2017-03-07 16:27:34');
INSERT INTO `t_role_permission` VALUES ('892', '12', '21', '2017-03-07 16:27:34');
INSERT INTO `t_role_permission` VALUES ('893', '12', '22', '2017-03-07 16:27:34');
INSERT INTO `t_role_permission` VALUES ('894', '12', '26', '2017-03-07 16:27:34');
INSERT INTO `t_role_permission` VALUES ('895', '12', '43', '2017-03-07 16:27:34');
INSERT INTO `t_role_permission` VALUES ('896', '12', '45', '2017-03-07 16:27:34');
INSERT INTO `t_role_permission` VALUES ('897', '12', '48', '2017-03-07 16:27:34');
INSERT INTO `t_role_permission` VALUES ('898', '12', '49', '2017-03-07 16:27:34');
INSERT INTO `t_role_permission` VALUES ('899', '12', '52', '2017-03-07 16:27:34');
INSERT INTO `t_role_permission` VALUES ('900', '12', '57', '2017-03-07 16:27:34');
INSERT INTO `t_role_permission` VALUES ('901', '12', '58', '2017-03-07 16:27:34');
INSERT INTO `t_role_permission` VALUES ('902', '12', '59', '2017-03-07 16:27:34');
INSERT INTO `t_role_permission` VALUES ('903', '12', '60', '2017-03-07 16:27:34');
INSERT INTO `t_role_permission` VALUES ('904', '12', '61', '2017-03-07 16:27:34');
INSERT INTO `t_role_permission` VALUES ('905', '12', '62', '2017-03-07 16:27:34');
INSERT INTO `t_role_permission` VALUES ('906', '12', '63', '2017-03-07 16:27:34');
INSERT INTO `t_role_permission` VALUES ('907', '12', '64', '2017-03-07 16:27:34');
INSERT INTO `t_role_permission` VALUES ('908', '12', '65', '2017-03-07 16:27:34');
INSERT INTO `t_role_permission` VALUES ('909', '12', '66', '2017-03-07 16:27:34');
INSERT INTO `t_role_permission` VALUES ('910', '12', '67', '2017-03-07 16:27:34');
INSERT INTO `t_role_permission` VALUES ('911', '12', '68', '2017-03-07 16:27:34');
INSERT INTO `t_role_permission` VALUES ('912', '12', '69', '2017-03-07 16:27:34');
INSERT INTO `t_role_permission` VALUES ('913', '12', '70', '2017-03-07 16:27:34');
INSERT INTO `t_role_permission` VALUES ('914', '12', '71', '2017-03-07 16:27:34');
INSERT INTO `t_role_permission` VALUES ('915', '12', '72', '2017-03-07 16:27:34');
INSERT INTO `t_role_permission` VALUES ('916', '12', '73', '2017-03-07 16:27:34');
INSERT INTO `t_role_permission` VALUES ('917', '12', '74', '2017-03-07 16:27:34');
INSERT INTO `t_role_permission` VALUES ('918', '12', '75', '2017-03-07 16:27:34');
INSERT INTO `t_role_permission` VALUES ('919', '12', '76', '2017-03-07 16:27:34');
INSERT INTO `t_role_permission` VALUES ('920', '12', '77', '2017-03-07 16:27:34');
INSERT INTO `t_role_permission` VALUES ('921', '12', '79', '2017-03-07 16:27:34');
INSERT INTO `t_role_permission` VALUES ('922', '12', '80', '2017-03-07 16:27:34');
INSERT INTO `t_role_permission` VALUES ('923', '12', '81', '2017-03-07 16:27:34');
INSERT INTO `t_role_permission` VALUES ('924', '12', '82', '2017-03-07 16:27:34');
INSERT INTO `t_role_permission` VALUES ('925', '12', '83', '2017-03-07 16:27:34');
INSERT INTO `t_role_permission` VALUES ('926', '12', '85', '2017-03-07 16:27:34');
INSERT INTO `t_role_permission` VALUES ('927', '12', '86', '2017-03-07 16:27:34');
INSERT INTO `t_role_permission` VALUES ('928', '12', '87', '2017-03-07 16:27:34');
INSERT INTO `t_role_permission` VALUES ('929', '12', '91', '2017-03-07 16:27:34');
INSERT INTO `t_role_permission` VALUES ('930', '12', '92', '2017-03-07 16:27:34');
INSERT INTO `t_role_permission` VALUES ('931', '12', '93', '2017-03-07 16:27:34');
INSERT INTO `t_role_permission` VALUES ('932', '12', '94', '2017-03-07 16:27:34');
INSERT INTO `t_role_permission` VALUES ('933', '12', '95', '2017-03-07 16:27:34');
INSERT INTO `t_role_permission` VALUES ('934', '12', '96', '2017-03-07 16:27:34');
INSERT INTO `t_role_permission` VALUES ('935', '12', '97', '2017-03-07 16:27:34');
INSERT INTO `t_role_permission` VALUES ('936', '12', '107', '2017-03-07 16:27:34');
INSERT INTO `t_role_permission` VALUES ('937', '12', '108', '2017-03-07 16:27:34');
INSERT INTO `t_role_permission` VALUES ('938', '12', '109', '2017-03-07 16:27:34');
INSERT INTO `t_role_permission` VALUES ('939', '12', '110', '2017-03-07 16:27:34');
INSERT INTO `t_role_permission` VALUES ('940', '12', '111', '2017-03-07 16:27:34');
INSERT INTO `t_role_permission` VALUES ('941', '12', '112', '2017-03-07 16:27:34');
INSERT INTO `t_role_permission` VALUES ('942', '12', '113', '2017-03-07 16:27:34');
INSERT INTO `t_role_permission` VALUES ('943', '12', '117', '2017-03-07 16:27:34');
INSERT INTO `t_role_permission` VALUES ('944', '12', '118', '2017-03-07 16:27:34');
INSERT INTO `t_role_permission` VALUES ('945', '12', '119', '2017-03-07 16:27:34');
INSERT INTO `t_role_permission` VALUES ('946', '12', '121', '2017-03-07 16:27:34');
INSERT INTO `t_role_permission` VALUES ('947', '12', '124', '2017-03-07 16:27:34');
INSERT INTO `t_role_permission` VALUES ('948', '12', '125', '2017-03-07 16:27:34');
INSERT INTO `t_role_permission` VALUES ('949', '12', '126', '2017-03-07 16:27:34');
INSERT INTO `t_role_permission` VALUES ('950', '12', '130', '2017-03-07 16:27:34');
INSERT INTO `t_role_permission` VALUES ('951', '12', '141', '2017-03-07 16:27:34');
INSERT INTO `t_role_permission` VALUES ('955', '15', '2', '2017-03-08 10:20:52');
INSERT INTO `t_role_permission` VALUES ('957', '15', '3', '2017-03-08 10:20:52');
INSERT INTO `t_role_permission` VALUES ('959', '15', '4', '2017-03-08 10:20:52');
INSERT INTO `t_role_permission` VALUES ('961', '15', '6', '2017-03-08 10:20:52');
INSERT INTO `t_role_permission` VALUES ('963', '15', '7', '2017-03-08 10:20:52');
INSERT INTO `t_role_permission` VALUES ('965', '15', '8', '2017-03-08 10:20:52');
INSERT INTO `t_role_permission` VALUES ('967', '15', '9', '2017-03-08 10:20:52');
INSERT INTO `t_role_permission` VALUES ('969', '15', '15', '2017-03-08 10:20:52');
INSERT INTO `t_role_permission` VALUES ('971', '15', '16', '2017-03-08 10:20:52');
INSERT INTO `t_role_permission` VALUES ('973', '15', '19', '2017-03-08 10:20:52');
INSERT INTO `t_role_permission` VALUES ('975', '15', '20', '2017-03-08 10:20:52');
INSERT INTO `t_role_permission` VALUES ('977', '15', '21', '2017-03-08 10:20:52');
INSERT INTO `t_role_permission` VALUES ('979', '15', '23', '2017-03-08 10:20:52');
INSERT INTO `t_role_permission` VALUES ('981', '15', '24', '2017-03-08 10:20:52');
INSERT INTO `t_role_permission` VALUES ('983', '15', '25', '2017-03-08 10:20:52');
INSERT INTO `t_role_permission` VALUES ('985', '15', '26', '2017-03-08 10:20:52');
INSERT INTO `t_role_permission` VALUES ('987', '15', '27', '2017-03-08 10:20:52');
INSERT INTO `t_role_permission` VALUES ('989', '15', '28', '2017-03-08 10:20:52');
INSERT INTO `t_role_permission` VALUES ('991', '15', '29', '2017-03-08 10:20:52');
INSERT INTO `t_role_permission` VALUES ('993', '15', '30', '2017-03-08 10:20:52');
INSERT INTO `t_role_permission` VALUES ('995', '15', '31', '2017-03-08 10:20:52');
INSERT INTO `t_role_permission` VALUES ('997', '15', '32', '2017-03-08 10:20:52');
INSERT INTO `t_role_permission` VALUES ('1013', '15', '40', '2017-03-08 10:20:52');
INSERT INTO `t_role_permission` VALUES ('1015', '15', '41', '2017-03-08 10:20:52');
INSERT INTO `t_role_permission` VALUES ('1017', '15', '44', '2017-03-08 10:20:52');
INSERT INTO `t_role_permission` VALUES ('1019', '15', '45', '2017-03-08 10:20:52');
INSERT INTO `t_role_permission` VALUES ('1021', '15', '46', '2017-03-08 10:20:52');
INSERT INTO `t_role_permission` VALUES ('1023', '15', '49', '2017-03-08 10:20:52');
INSERT INTO `t_role_permission` VALUES ('1025', '15', '50', '2017-03-08 10:20:52');
INSERT INTO `t_role_permission` VALUES ('1027', '15', '51', '2017-03-08 10:20:52');
INSERT INTO `t_role_permission` VALUES ('1029', '15', '52', '2017-03-08 10:20:52');
INSERT INTO `t_role_permission` VALUES ('1031', '15', '53', '2017-03-08 10:20:52');
INSERT INTO `t_role_permission` VALUES ('1033', '15', '54', '2017-03-08 10:20:52');
INSERT INTO `t_role_permission` VALUES ('1035', '15', '55', '2017-03-08 10:20:52');
INSERT INTO `t_role_permission` VALUES ('1037', '15', '56', '2017-03-08 10:20:52');
INSERT INTO `t_role_permission` VALUES ('1041', '15', '59', '2017-03-08 10:20:52');
INSERT INTO `t_role_permission` VALUES ('1043', '15', '60', '2017-03-08 10:20:52');
INSERT INTO `t_role_permission` VALUES ('1045', '15', '61', '2017-03-08 10:20:52');
INSERT INTO `t_role_permission` VALUES ('1047', '15', '62', '2017-03-08 10:20:52');
INSERT INTO `t_role_permission` VALUES ('1049', '15', '63', '2017-03-08 10:20:52');
INSERT INTO `t_role_permission` VALUES ('1051', '15', '65', '2017-03-08 10:20:52');
INSERT INTO `t_role_permission` VALUES ('1053', '15', '66', '2017-03-08 10:20:52');
INSERT INTO `t_role_permission` VALUES ('1055', '15', '67', '2017-03-08 10:20:52');
INSERT INTO `t_role_permission` VALUES ('1057', '15', '68', '2017-03-08 10:20:52');
INSERT INTO `t_role_permission` VALUES ('1059', '15', '69', '2017-03-08 10:20:52');
INSERT INTO `t_role_permission` VALUES ('1061', '15', '70', '2017-03-08 10:20:52');
INSERT INTO `t_role_permission` VALUES ('1063', '15', '71', '2017-03-08 10:20:52');
INSERT INTO `t_role_permission` VALUES ('1065', '15', '74', '2017-03-08 10:20:52');
INSERT INTO `t_role_permission` VALUES ('1067', '15', '75', '2017-03-08 10:20:52');
INSERT INTO `t_role_permission` VALUES ('1069', '15', '76', '2017-03-08 10:20:52');
INSERT INTO `t_role_permission` VALUES ('1071', '15', '77', '2017-03-08 10:20:52');
INSERT INTO `t_role_permission` VALUES ('1073', '15', '78', '2017-03-08 10:20:52');
INSERT INTO `t_role_permission` VALUES ('1075', '15', '79', '2017-03-08 10:20:52');
INSERT INTO `t_role_permission` VALUES ('1077', '15', '80', '2017-03-08 10:20:52');
INSERT INTO `t_role_permission` VALUES ('1079', '15', '81', '2017-03-08 10:20:52');
INSERT INTO `t_role_permission` VALUES ('1081', '15', '82', '2017-03-08 10:20:52');
INSERT INTO `t_role_permission` VALUES ('1083', '15', '83', '2017-03-08 10:20:52');
INSERT INTO `t_role_permission` VALUES ('1085', '15', '84', '2017-03-08 10:20:52');
INSERT INTO `t_role_permission` VALUES ('1087', '15', '85', '2017-03-08 10:20:52');
INSERT INTO `t_role_permission` VALUES ('1089', '15', '86', '2017-03-08 10:20:52');
INSERT INTO `t_role_permission` VALUES ('1091', '15', '87', '2017-03-08 10:20:52');
INSERT INTO `t_role_permission` VALUES ('1093', '15', '88', '2017-03-08 10:20:52');
INSERT INTO `t_role_permission` VALUES ('1095', '15', '89', '2017-03-08 10:20:52');
INSERT INTO `t_role_permission` VALUES ('1097', '15', '90', '2017-03-08 10:20:52');
INSERT INTO `t_role_permission` VALUES ('1099', '15', '91', '2017-03-08 10:20:52');
INSERT INTO `t_role_permission` VALUES ('1101', '15', '92', '2017-03-08 10:20:52');
INSERT INTO `t_role_permission` VALUES ('1103', '15', '93', '2017-03-08 10:20:52');
INSERT INTO `t_role_permission` VALUES ('1105', '15', '94', '2017-03-08 10:20:52');
INSERT INTO `t_role_permission` VALUES ('1107', '15', '95', '2017-03-08 10:20:52');
INSERT INTO `t_role_permission` VALUES ('1109', '15', '96', '2017-03-08 10:20:52');
INSERT INTO `t_role_permission` VALUES ('1111', '15', '97', '2017-03-08 10:20:52');
INSERT INTO `t_role_permission` VALUES ('1113', '15', '98', '2017-03-08 10:20:52');
INSERT INTO `t_role_permission` VALUES ('1115', '15', '99', '2017-03-08 10:20:52');
INSERT INTO `t_role_permission` VALUES ('1117', '15', '102', '2017-03-08 10:20:52');
INSERT INTO `t_role_permission` VALUES ('1119', '15', '104', '2017-03-08 10:20:52');
INSERT INTO `t_role_permission` VALUES ('1121', '15', '108', '2017-03-08 10:20:52');
INSERT INTO `t_role_permission` VALUES ('1123', '15', '109', '2017-03-08 10:20:52');
INSERT INTO `t_role_permission` VALUES ('1125', '15', '110', '2017-03-08 10:20:52');
INSERT INTO `t_role_permission` VALUES ('1127', '15', '111', '2017-03-08 10:20:52');
INSERT INTO `t_role_permission` VALUES ('1129', '15', '112', '2017-03-08 10:20:52');
INSERT INTO `t_role_permission` VALUES ('1131', '15', '114', '2017-03-08 10:20:52');
INSERT INTO `t_role_permission` VALUES ('1133', '15', '117', '2017-03-08 10:20:52');
INSERT INTO `t_role_permission` VALUES ('1135', '15', '118', '2017-03-08 10:20:52');
INSERT INTO `t_role_permission` VALUES ('1137', '15', '119', '2017-03-08 10:20:52');
INSERT INTO `t_role_permission` VALUES ('1139', '15', '121', '2017-03-08 10:20:52');
INSERT INTO `t_role_permission` VALUES ('1141', '15', '124', '2017-03-08 10:20:52');
INSERT INTO `t_role_permission` VALUES ('1143', '15', '125', '2017-03-08 10:20:52');
INSERT INTO `t_role_permission` VALUES ('1145', '15', '127', '2017-03-08 10:20:52');
INSERT INTO `t_role_permission` VALUES ('1147', '15', '128', '2017-03-08 10:20:52');
INSERT INTO `t_role_permission` VALUES ('1149', '15', '129', '2017-03-08 10:20:52');
INSERT INTO `t_role_permission` VALUES ('1151', '15', '130', '2017-03-08 10:20:52');
INSERT INTO `t_role_permission` VALUES ('1153', '15', '131', '2017-03-08 10:20:52');
INSERT INTO `t_role_permission` VALUES ('1155', '15', '132', '2017-03-08 10:20:52');
INSERT INTO `t_role_permission` VALUES ('1157', '15', '133', '2017-03-08 10:20:52');
INSERT INTO `t_role_permission` VALUES ('1159', '15', '134', '2017-03-08 10:20:52');
INSERT INTO `t_role_permission` VALUES ('1161', '15', '135', '2017-03-08 10:20:52');
INSERT INTO `t_role_permission` VALUES ('1163', '15', '136', '2017-03-08 10:20:52');
INSERT INTO `t_role_permission` VALUES ('1165', '15', '137', '2017-03-08 10:20:52');
INSERT INTO `t_role_permission` VALUES ('1167', '15', '138', '2017-03-08 10:20:52');
INSERT INTO `t_role_permission` VALUES ('1171', '15', '140', '2017-03-08 10:20:52');
INSERT INTO `t_role_permission` VALUES ('1173', '15', '141', '2017-03-08 10:20:52');
INSERT INTO `t_role_permission` VALUES ('1175', '15', '142', '2017-03-08 10:20:52');
INSERT INTO `t_role_permission` VALUES ('1177', '15', '115', '2017-03-08 10:29:48');
INSERT INTO `t_role_permission` VALUES ('1179', '15', '116', '2017-03-08 10:29:48');
INSERT INTO `t_role_permission` VALUES ('1181', '15', '120', '2017-03-08 10:29:48');
INSERT INTO `t_role_permission` VALUES ('1183', '15', '122', '2017-03-08 10:29:48');
INSERT INTO `t_role_permission` VALUES ('1185', '15', '123', '2017-03-08 10:29:48');
INSERT INTO `t_role_permission` VALUES ('1187', '15', '145', '2017-03-08 10:29:48');
INSERT INTO `t_role_permission` VALUES ('1193', '15', '5', '2017-03-08 10:39:46');
INSERT INTO `t_role_permission` VALUES ('1195', '15', '101', '2017-03-08 10:39:46');
INSERT INTO `t_role_permission` VALUES ('1197', '17', '2', '2017-03-08 10:54:46');
INSERT INTO `t_role_permission` VALUES ('1199', '17', '3', '2017-03-08 10:54:46');
INSERT INTO `t_role_permission` VALUES ('1201', '17', '4', '2017-03-08 10:54:46');
INSERT INTO `t_role_permission` VALUES ('1203', '17', '6', '2017-03-08 10:54:46');
INSERT INTO `t_role_permission` VALUES ('1205', '17', '7', '2017-03-08 10:54:46');
INSERT INTO `t_role_permission` VALUES ('1207', '17', '8', '2017-03-08 10:54:46');
INSERT INTO `t_role_permission` VALUES ('1209', '17', '9', '2017-03-08 10:54:46');
INSERT INTO `t_role_permission` VALUES ('1211', '17', '19', '2017-03-08 10:54:46');
INSERT INTO `t_role_permission` VALUES ('1213', '17', '23', '2017-03-08 10:54:46');
INSERT INTO `t_role_permission` VALUES ('1215', '17', '24', '2017-03-08 10:54:46');
INSERT INTO `t_role_permission` VALUES ('1217', '17', '25', '2017-03-08 10:54:46');
INSERT INTO `t_role_permission` VALUES ('1219', '17', '26', '2017-03-08 10:54:46');
INSERT INTO `t_role_permission` VALUES ('1221', '17', '27', '2017-03-08 10:54:46');
INSERT INTO `t_role_permission` VALUES ('1223', '17', '28', '2017-03-08 10:54:46');
INSERT INTO `t_role_permission` VALUES ('1225', '17', '32', '2017-03-08 10:54:46');
INSERT INTO `t_role_permission` VALUES ('1241', '17', '40', '2017-03-08 10:54:46');
INSERT INTO `t_role_permission` VALUES ('1243', '17', '41', '2017-03-08 10:54:46');
INSERT INTO `t_role_permission` VALUES ('1245', '17', '45', '2017-03-08 10:54:46');
INSERT INTO `t_role_permission` VALUES ('1247', '17', '46', '2017-03-08 10:54:46');
INSERT INTO `t_role_permission` VALUES ('1249', '17', '49', '2017-03-08 10:54:46');
INSERT INTO `t_role_permission` VALUES ('1251', '17', '50', '2017-03-08 10:54:46');
INSERT INTO `t_role_permission` VALUES ('1253', '17', '51', '2017-03-08 10:54:46');
INSERT INTO `t_role_permission` VALUES ('1255', '17', '54', '2017-03-08 10:54:46');
INSERT INTO `t_role_permission` VALUES ('1257', '17', '55', '2017-03-08 10:54:46');
INSERT INTO `t_role_permission` VALUES ('1261', '17', '59', '2017-03-08 10:54:46');
INSERT INTO `t_role_permission` VALUES ('1263', '17', '60', '2017-03-08 10:54:46');
INSERT INTO `t_role_permission` VALUES ('1265', '17', '61', '2017-03-08 10:54:46');
INSERT INTO `t_role_permission` VALUES ('1267', '17', '65', '2017-03-08 10:54:46');
INSERT INTO `t_role_permission` VALUES ('1269', '17', '66', '2017-03-08 10:54:46');
INSERT INTO `t_role_permission` VALUES ('1271', '17', '67', '2017-03-08 10:54:46');
INSERT INTO `t_role_permission` VALUES ('1273', '17', '68', '2017-03-08 10:54:46');
INSERT INTO `t_role_permission` VALUES ('1275', '17', '69', '2017-03-08 10:54:46');
INSERT INTO `t_role_permission` VALUES ('1277', '17', '70', '2017-03-08 10:54:46');
INSERT INTO `t_role_permission` VALUES ('1279', '17', '71', '2017-03-08 10:54:46');
INSERT INTO `t_role_permission` VALUES ('1281', '17', '78', '2017-03-08 10:54:46');
INSERT INTO `t_role_permission` VALUES ('1283', '17', '79', '2017-03-08 10:54:46');
INSERT INTO `t_role_permission` VALUES ('1285', '17', '80', '2017-03-08 10:54:46');
INSERT INTO `t_role_permission` VALUES ('1287', '17', '81', '2017-03-08 10:54:46');
INSERT INTO `t_role_permission` VALUES ('1289', '17', '82', '2017-03-08 10:54:46');
INSERT INTO `t_role_permission` VALUES ('1291', '17', '83', '2017-03-08 10:54:46');
INSERT INTO `t_role_permission` VALUES ('1293', '17', '86', '2017-03-08 10:54:46');
INSERT INTO `t_role_permission` VALUES ('1295', '17', '87', '2017-03-08 10:54:46');
INSERT INTO `t_role_permission` VALUES ('1297', '17', '89', '2017-03-08 10:54:46');
INSERT INTO `t_role_permission` VALUES ('1299', '17', '90', '2017-03-08 10:54:46');
INSERT INTO `t_role_permission` VALUES ('1301', '17', '91', '2017-03-08 10:54:46');
INSERT INTO `t_role_permission` VALUES ('1303', '17', '92', '2017-03-08 10:54:46');
INSERT INTO `t_role_permission` VALUES ('1305', '17', '93', '2017-03-08 10:54:46');
INSERT INTO `t_role_permission` VALUES ('1307', '17', '94', '2017-03-08 10:54:46');
INSERT INTO `t_role_permission` VALUES ('1309', '17', '95', '2017-03-08 10:54:46');
INSERT INTO `t_role_permission` VALUES ('1311', '17', '96', '2017-03-08 10:54:46');
INSERT INTO `t_role_permission` VALUES ('1313', '17', '97', '2017-03-08 10:54:46');
INSERT INTO `t_role_permission` VALUES ('1315', '17', '99', '2017-03-08 10:54:46');
INSERT INTO `t_role_permission` VALUES ('1317', '17', '104', '2017-03-08 10:54:46');
INSERT INTO `t_role_permission` VALUES ('1319', '17', '108', '2017-03-08 10:54:46');
INSERT INTO `t_role_permission` VALUES ('1321', '17', '109', '2017-03-08 10:54:46');
INSERT INTO `t_role_permission` VALUES ('1323', '17', '110', '2017-03-08 10:54:46');
INSERT INTO `t_role_permission` VALUES ('1325', '17', '111', '2017-03-08 10:54:46');
INSERT INTO `t_role_permission` VALUES ('1327', '17', '112', '2017-03-08 10:54:46');
INSERT INTO `t_role_permission` VALUES ('1329', '17', '121', '2017-03-08 10:54:46');
INSERT INTO `t_role_permission` VALUES ('1331', '17', '124', '2017-03-08 10:54:46');
INSERT INTO `t_role_permission` VALUES ('1333', '17', '125', '2017-03-08 10:54:46');
INSERT INTO `t_role_permission` VALUES ('1335', '17', '127', '2017-03-08 10:54:46');
INSERT INTO `t_role_permission` VALUES ('1337', '17', '128', '2017-03-08 10:54:46');
INSERT INTO `t_role_permission` VALUES ('1339', '17', '129', '2017-03-08 10:54:46');
INSERT INTO `t_role_permission` VALUES ('1341', '17', '130', '2017-03-08 10:54:46');
INSERT INTO `t_role_permission` VALUES ('1343', '17', '131', '2017-03-08 10:54:46');
INSERT INTO `t_role_permission` VALUES ('1345', '17', '132', '2017-03-08 10:54:46');
INSERT INTO `t_role_permission` VALUES ('1347', '17', '133', '2017-03-08 10:54:46');
INSERT INTO `t_role_permission` VALUES ('1349', '17', '135', '2017-03-08 10:54:46');
INSERT INTO `t_role_permission` VALUES ('1351', '17', '136', '2017-03-08 10:54:46');
INSERT INTO `t_role_permission` VALUES ('1353', '17', '137', '2017-03-08 10:54:46');
INSERT INTO `t_role_permission` VALUES ('1355', '17', '141', '2017-03-08 10:54:46');
INSERT INTO `t_role_permission` VALUES ('1359', '21', '2', '2017-03-08 11:05:49');
INSERT INTO `t_role_permission` VALUES ('1361', '21', '3', '2017-03-08 11:05:49');
INSERT INTO `t_role_permission` VALUES ('1363', '21', '4', '2017-03-08 11:05:49');
INSERT INTO `t_role_permission` VALUES ('1365', '21', '5', '2017-03-08 11:05:49');
INSERT INTO `t_role_permission` VALUES ('1367', '21', '6', '2017-03-08 11:05:49');
INSERT INTO `t_role_permission` VALUES ('1369', '21', '7', '2017-03-08 11:05:49');
INSERT INTO `t_role_permission` VALUES ('1371', '21', '8', '2017-03-08 11:05:49');
INSERT INTO `t_role_permission` VALUES ('1373', '21', '9', '2017-03-08 11:05:49');
INSERT INTO `t_role_permission` VALUES ('1375', '21', '10', '2017-03-08 11:05:49');
INSERT INTO `t_role_permission` VALUES ('1377', '21', '11', '2017-03-08 11:05:49');
INSERT INTO `t_role_permission` VALUES ('1379', '21', '12', '2017-03-08 11:05:49');
INSERT INTO `t_role_permission` VALUES ('1381', '21', '13', '2017-03-08 11:05:49');
INSERT INTO `t_role_permission` VALUES ('1383', '21', '14', '2017-03-08 11:05:49');
INSERT INTO `t_role_permission` VALUES ('1385', '21', '15', '2017-03-08 11:05:49');
INSERT INTO `t_role_permission` VALUES ('1387', '21', '16', '2017-03-08 11:05:49');
INSERT INTO `t_role_permission` VALUES ('1389', '21', '17', '2017-03-08 11:05:49');
INSERT INTO `t_role_permission` VALUES ('1391', '21', '18', '2017-03-08 11:05:49');
INSERT INTO `t_role_permission` VALUES ('1393', '21', '57', '2017-03-08 11:05:49');
INSERT INTO `t_role_permission` VALUES ('1395', '21', '100', '2017-03-08 11:05:49');
INSERT INTO `t_role_permission` VALUES ('1397', '21', '105', '2017-03-08 11:05:49');
INSERT INTO `t_role_permission` VALUES ('1399', '21', '107', '2017-03-08 11:05:49');
INSERT INTO `t_role_permission` VALUES ('1401', '21', '113', '2017-03-08 11:05:49');
INSERT INTO `t_role_permission` VALUES ('1403', '21', '126', '2017-03-08 11:05:49');
INSERT INTO `t_role_permission` VALUES ('1405', '21', '144', '2017-03-08 11:05:49');
INSERT INTO `t_role_permission` VALUES ('1407', '21', '150', '2017-03-08 11:05:49');
INSERT INTO `t_role_permission` VALUES ('1409', '21', '19', '2017-03-08 11:07:28');
INSERT INTO `t_role_permission` VALUES ('1411', '21', '20', '2017-03-08 11:07:28');
INSERT INTO `t_role_permission` VALUES ('1413', '21', '21', '2017-03-08 11:07:28');
INSERT INTO `t_role_permission` VALUES ('1415', '21', '22', '2017-03-08 11:07:28');
INSERT INTO `t_role_permission` VALUES ('1417', '21', '23', '2017-03-08 11:07:28');
INSERT INTO `t_role_permission` VALUES ('1419', '21', '24', '2017-03-08 11:07:28');
INSERT INTO `t_role_permission` VALUES ('1421', '21', '25', '2017-03-08 11:07:28');
INSERT INTO `t_role_permission` VALUES ('1423', '21', '26', '2017-03-08 11:07:28');
INSERT INTO `t_role_permission` VALUES ('1425', '21', '27', '2017-03-08 11:07:28');
INSERT INTO `t_role_permission` VALUES ('1427', '21', '28', '2017-03-08 11:07:28');
INSERT INTO `t_role_permission` VALUES ('1429', '21', '29', '2017-03-08 11:07:28');
INSERT INTO `t_role_permission` VALUES ('1431', '21', '30', '2017-03-08 11:07:28');
INSERT INTO `t_role_permission` VALUES ('1433', '21', '31', '2017-03-08 11:07:28');
INSERT INTO `t_role_permission` VALUES ('1435', '21', '32', '2017-03-08 11:07:28');
INSERT INTO `t_role_permission` VALUES ('1451', '21', '40', '2017-03-08 11:07:28');
INSERT INTO `t_role_permission` VALUES ('1453', '21', '41', '2017-03-08 11:07:28');
INSERT INTO `t_role_permission` VALUES ('1455', '21', '42', '2017-03-08 11:07:28');
INSERT INTO `t_role_permission` VALUES ('1457', '21', '43', '2017-03-08 11:07:28');
INSERT INTO `t_role_permission` VALUES ('1459', '21', '44', '2017-03-08 11:07:28');
INSERT INTO `t_role_permission` VALUES ('1461', '21', '45', '2017-03-08 11:07:28');
INSERT INTO `t_role_permission` VALUES ('1463', '21', '46', '2017-03-08 11:07:28');
INSERT INTO `t_role_permission` VALUES ('1465', '21', '47', '2017-03-08 11:07:28');
INSERT INTO `t_role_permission` VALUES ('1467', '21', '48', '2017-03-08 11:07:28');
INSERT INTO `t_role_permission` VALUES ('1469', '21', '49', '2017-03-08 11:07:28');
INSERT INTO `t_role_permission` VALUES ('1471', '21', '50', '2017-03-08 11:07:28');
INSERT INTO `t_role_permission` VALUES ('1473', '21', '51', '2017-03-08 11:07:28');
INSERT INTO `t_role_permission` VALUES ('1475', '21', '52', '2017-03-08 11:07:28');
INSERT INTO `t_role_permission` VALUES ('1477', '21', '53', '2017-03-08 11:07:28');
INSERT INTO `t_role_permission` VALUES ('1479', '21', '54', '2017-03-08 11:07:28');
INSERT INTO `t_role_permission` VALUES ('1481', '21', '55', '2017-03-08 11:07:28');
INSERT INTO `t_role_permission` VALUES ('1483', '21', '56', '2017-03-08 11:07:28');
INSERT INTO `t_role_permission` VALUES ('1487', '21', '59', '2017-03-08 11:07:28');
INSERT INTO `t_role_permission` VALUES ('1489', '21', '60', '2017-03-08 11:07:28');
INSERT INTO `t_role_permission` VALUES ('1491', '21', '61', '2017-03-08 11:07:28');
INSERT INTO `t_role_permission` VALUES ('1493', '21', '62', '2017-03-08 11:07:28');
INSERT INTO `t_role_permission` VALUES ('1495', '21', '63', '2017-03-08 11:07:28');
INSERT INTO `t_role_permission` VALUES ('1497', '21', '64', '2017-03-08 11:07:28');
INSERT INTO `t_role_permission` VALUES ('1499', '21', '65', '2017-03-08 11:07:28');
INSERT INTO `t_role_permission` VALUES ('1501', '21', '66', '2017-03-08 11:07:28');
INSERT INTO `t_role_permission` VALUES ('1503', '21', '67', '2017-03-08 11:07:28');
INSERT INTO `t_role_permission` VALUES ('1505', '21', '68', '2017-03-08 11:07:28');
INSERT INTO `t_role_permission` VALUES ('1507', '21', '69', '2017-03-08 11:07:28');
INSERT INTO `t_role_permission` VALUES ('1509', '21', '70', '2017-03-08 11:07:28');
INSERT INTO `t_role_permission` VALUES ('1511', '21', '71', '2017-03-08 11:07:28');
INSERT INTO `t_role_permission` VALUES ('1513', '21', '72', '2017-03-08 11:07:28');
INSERT INTO `t_role_permission` VALUES ('1515', '21', '73', '2017-03-08 11:07:28');
INSERT INTO `t_role_permission` VALUES ('1517', '21', '74', '2017-03-08 11:07:28');
INSERT INTO `t_role_permission` VALUES ('1519', '21', '75', '2017-03-08 11:07:28');
INSERT INTO `t_role_permission` VALUES ('1521', '21', '76', '2017-03-08 11:07:28');
INSERT INTO `t_role_permission` VALUES ('1523', '21', '77', '2017-03-08 11:07:28');
INSERT INTO `t_role_permission` VALUES ('1525', '21', '78', '2017-03-08 11:07:28');
INSERT INTO `t_role_permission` VALUES ('1527', '21', '79', '2017-03-08 11:07:28');
INSERT INTO `t_role_permission` VALUES ('1529', '21', '80', '2017-03-08 11:07:28');
INSERT INTO `t_role_permission` VALUES ('1531', '21', '81', '2017-03-08 11:07:28');
INSERT INTO `t_role_permission` VALUES ('1533', '21', '82', '2017-03-08 11:07:28');
INSERT INTO `t_role_permission` VALUES ('1535', '21', '83', '2017-03-08 11:07:28');
INSERT INTO `t_role_permission` VALUES ('1537', '21', '84', '2017-03-08 11:07:28');
INSERT INTO `t_role_permission` VALUES ('1539', '21', '85', '2017-03-08 11:07:28');
INSERT INTO `t_role_permission` VALUES ('1541', '21', '86', '2017-03-08 11:07:28');
INSERT INTO `t_role_permission` VALUES ('1543', '21', '87', '2017-03-08 11:07:28');
INSERT INTO `t_role_permission` VALUES ('1545', '21', '88', '2017-03-08 11:07:28');
INSERT INTO `t_role_permission` VALUES ('1547', '21', '89', '2017-03-08 11:07:28');
INSERT INTO `t_role_permission` VALUES ('1549', '21', '90', '2017-03-08 11:07:28');
INSERT INTO `t_role_permission` VALUES ('1551', '21', '91', '2017-03-08 11:07:28');
INSERT INTO `t_role_permission` VALUES ('1553', '21', '92', '2017-03-08 11:07:28');
INSERT INTO `t_role_permission` VALUES ('1555', '21', '93', '2017-03-08 11:07:28');
INSERT INTO `t_role_permission` VALUES ('1557', '21', '94', '2017-03-08 11:07:28');
INSERT INTO `t_role_permission` VALUES ('1559', '21', '95', '2017-03-08 11:07:28');
INSERT INTO `t_role_permission` VALUES ('1561', '21', '96', '2017-03-08 11:07:28');
INSERT INTO `t_role_permission` VALUES ('1563', '21', '97', '2017-03-08 11:07:28');
INSERT INTO `t_role_permission` VALUES ('1565', '21', '98', '2017-03-08 11:07:28');
INSERT INTO `t_role_permission` VALUES ('1567', '21', '99', '2017-03-08 11:07:28');
INSERT INTO `t_role_permission` VALUES ('1569', '21', '101', '2017-03-08 11:07:28');
INSERT INTO `t_role_permission` VALUES ('1571', '21', '102', '2017-03-08 11:07:28');
INSERT INTO `t_role_permission` VALUES ('1573', '21', '103', '2017-03-08 11:07:28');
INSERT INTO `t_role_permission` VALUES ('1575', '21', '104', '2017-03-08 11:07:28');
INSERT INTO `t_role_permission` VALUES ('1577', '21', '106', '2017-03-08 11:07:28');
INSERT INTO `t_role_permission` VALUES ('1579', '21', '108', '2017-03-08 11:07:28');
INSERT INTO `t_role_permission` VALUES ('1581', '21', '109', '2017-03-08 11:07:28');
INSERT INTO `t_role_permission` VALUES ('1583', '21', '110', '2017-03-08 11:07:28');
INSERT INTO `t_role_permission` VALUES ('1585', '21', '111', '2017-03-08 11:07:28');
INSERT INTO `t_role_permission` VALUES ('1587', '21', '112', '2017-03-08 11:07:28');
INSERT INTO `t_role_permission` VALUES ('1589', '21', '114', '2017-03-08 11:07:28');
INSERT INTO `t_role_permission` VALUES ('1591', '21', '115', '2017-03-08 11:07:28');
INSERT INTO `t_role_permission` VALUES ('1593', '21', '116', '2017-03-08 11:07:28');
INSERT INTO `t_role_permission` VALUES ('1595', '21', '117', '2017-03-08 11:07:28');
INSERT INTO `t_role_permission` VALUES ('1597', '21', '118', '2017-03-08 11:07:28');
INSERT INTO `t_role_permission` VALUES ('1599', '21', '119', '2017-03-08 11:07:28');
INSERT INTO `t_role_permission` VALUES ('1601', '21', '120', '2017-03-08 11:07:28');
INSERT INTO `t_role_permission` VALUES ('1603', '21', '121', '2017-03-08 11:07:28');
INSERT INTO `t_role_permission` VALUES ('1605', '21', '122', '2017-03-08 11:07:28');
INSERT INTO `t_role_permission` VALUES ('1607', '21', '123', '2017-03-08 11:07:28');
INSERT INTO `t_role_permission` VALUES ('1609', '21', '124', '2017-03-08 11:07:28');
INSERT INTO `t_role_permission` VALUES ('1611', '21', '125', '2017-03-08 11:07:28');
INSERT INTO `t_role_permission` VALUES ('1613', '21', '127', '2017-03-08 11:07:28');
INSERT INTO `t_role_permission` VALUES ('1615', '21', '128', '2017-03-08 11:07:28');
INSERT INTO `t_role_permission` VALUES ('1617', '21', '129', '2017-03-08 11:07:28');
INSERT INTO `t_role_permission` VALUES ('1619', '21', '130', '2017-03-08 11:07:28');
INSERT INTO `t_role_permission` VALUES ('1621', '21', '131', '2017-03-08 11:07:28');
INSERT INTO `t_role_permission` VALUES ('1623', '21', '132', '2017-03-08 11:07:28');
INSERT INTO `t_role_permission` VALUES ('1625', '21', '133', '2017-03-08 11:07:28');
INSERT INTO `t_role_permission` VALUES ('1627', '21', '134', '2017-03-08 11:07:28');
INSERT INTO `t_role_permission` VALUES ('1629', '21', '135', '2017-03-08 11:07:28');
INSERT INTO `t_role_permission` VALUES ('1631', '21', '136', '2017-03-08 11:07:28');
INSERT INTO `t_role_permission` VALUES ('1633', '21', '137', '2017-03-08 11:07:28');
INSERT INTO `t_role_permission` VALUES ('1635', '21', '138', '2017-03-08 11:07:28');
INSERT INTO `t_role_permission` VALUES ('1639', '21', '140', '2017-03-08 11:07:28');
INSERT INTO `t_role_permission` VALUES ('1641', '21', '141', '2017-03-08 11:07:28');
INSERT INTO `t_role_permission` VALUES ('1643', '21', '142', '2017-03-08 11:07:28');
INSERT INTO `t_role_permission` VALUES ('1645', '21', '145', '2017-03-08 11:07:28');
INSERT INTO `t_role_permission` VALUES ('1651', '21', '151', '2017-03-08 11:07:28');
INSERT INTO `t_role_permission` VALUES ('1657', '19', '2', '2017-03-08 11:51:22');
INSERT INTO `t_role_permission` VALUES ('1659', '19', '3', '2017-03-08 11:51:22');
INSERT INTO `t_role_permission` VALUES ('1661', '19', '4', '2017-03-08 11:51:22');
INSERT INTO `t_role_permission` VALUES ('1663', '19', '6', '2017-03-08 11:51:22');
INSERT INTO `t_role_permission` VALUES ('1665', '19', '7', '2017-03-08 11:51:22');
INSERT INTO `t_role_permission` VALUES ('1667', '19', '8', '2017-03-08 11:51:22');
INSERT INTO `t_role_permission` VALUES ('1669', '19', '9', '2017-03-08 11:51:22');
INSERT INTO `t_role_permission` VALUES ('1671', '19', '19', '2017-03-08 11:51:22');
INSERT INTO `t_role_permission` VALUES ('1673', '19', '23', '2017-03-08 11:51:22');
INSERT INTO `t_role_permission` VALUES ('1675', '19', '24', '2017-03-08 11:51:22');
INSERT INTO `t_role_permission` VALUES ('1677', '19', '25', '2017-03-08 11:51:22');
INSERT INTO `t_role_permission` VALUES ('1679', '19', '26', '2017-03-08 11:51:22');
INSERT INTO `t_role_permission` VALUES ('1681', '19', '27', '2017-03-08 11:51:22');
INSERT INTO `t_role_permission` VALUES ('1683', '19', '28', '2017-03-08 11:51:22');
INSERT INTO `t_role_permission` VALUES ('1685', '19', '32', '2017-03-08 11:51:22');
INSERT INTO `t_role_permission` VALUES ('1701', '19', '40', '2017-03-08 11:51:22');
INSERT INTO `t_role_permission` VALUES ('1703', '19', '41', '2017-03-08 11:51:22');
INSERT INTO `t_role_permission` VALUES ('1705', '19', '45', '2017-03-08 11:51:22');
INSERT INTO `t_role_permission` VALUES ('1707', '19', '46', '2017-03-08 11:51:22');
INSERT INTO `t_role_permission` VALUES ('1709', '19', '49', '2017-03-08 11:51:22');
INSERT INTO `t_role_permission` VALUES ('1711', '19', '50', '2017-03-08 11:51:22');
INSERT INTO `t_role_permission` VALUES ('1713', '19', '51', '2017-03-08 11:51:22');
INSERT INTO `t_role_permission` VALUES ('1715', '19', '54', '2017-03-08 11:51:22');
INSERT INTO `t_role_permission` VALUES ('1717', '19', '55', '2017-03-08 11:51:22');
INSERT INTO `t_role_permission` VALUES ('1721', '19', '59', '2017-03-08 11:51:22');
INSERT INTO `t_role_permission` VALUES ('1723', '19', '60', '2017-03-08 11:51:22');
INSERT INTO `t_role_permission` VALUES ('1725', '19', '61', '2017-03-08 11:51:22');
INSERT INTO `t_role_permission` VALUES ('1727', '19', '65', '2017-03-08 11:51:22');
INSERT INTO `t_role_permission` VALUES ('1729', '19', '66', '2017-03-08 11:51:22');
INSERT INTO `t_role_permission` VALUES ('1731', '19', '67', '2017-03-08 11:51:22');
INSERT INTO `t_role_permission` VALUES ('1733', '19', '68', '2017-03-08 11:51:22');
INSERT INTO `t_role_permission` VALUES ('1735', '19', '69', '2017-03-08 11:51:22');
INSERT INTO `t_role_permission` VALUES ('1737', '19', '70', '2017-03-08 11:51:22');
INSERT INTO `t_role_permission` VALUES ('1739', '19', '71', '2017-03-08 11:51:22');
INSERT INTO `t_role_permission` VALUES ('1741', '19', '78', '2017-03-08 11:51:22');
INSERT INTO `t_role_permission` VALUES ('1743', '19', '79', '2017-03-08 11:51:22');
INSERT INTO `t_role_permission` VALUES ('1745', '19', '80', '2017-03-08 11:51:22');
INSERT INTO `t_role_permission` VALUES ('1747', '19', '81', '2017-03-08 11:51:22');
INSERT INTO `t_role_permission` VALUES ('1749', '19', '82', '2017-03-08 11:51:22');
INSERT INTO `t_role_permission` VALUES ('1751', '19', '83', '2017-03-08 11:51:22');
INSERT INTO `t_role_permission` VALUES ('1753', '19', '84', '2017-03-08 11:51:22');
INSERT INTO `t_role_permission` VALUES ('1755', '19', '85', '2017-03-08 11:51:22');
INSERT INTO `t_role_permission` VALUES ('1757', '19', '86', '2017-03-08 11:51:22');
INSERT INTO `t_role_permission` VALUES ('1759', '19', '87', '2017-03-08 11:51:22');
INSERT INTO `t_role_permission` VALUES ('1761', '19', '88', '2017-03-08 11:51:22');
INSERT INTO `t_role_permission` VALUES ('1763', '19', '89', '2017-03-08 11:51:22');
INSERT INTO `t_role_permission` VALUES ('1765', '19', '90', '2017-03-08 11:51:22');
INSERT INTO `t_role_permission` VALUES ('1767', '19', '91', '2017-03-08 11:51:22');
INSERT INTO `t_role_permission` VALUES ('1769', '19', '92', '2017-03-08 11:51:22');
INSERT INTO `t_role_permission` VALUES ('1771', '19', '93', '2017-03-08 11:51:22');
INSERT INTO `t_role_permission` VALUES ('1773', '19', '94', '2017-03-08 11:51:22');
INSERT INTO `t_role_permission` VALUES ('1775', '19', '95', '2017-03-08 11:51:22');
INSERT INTO `t_role_permission` VALUES ('1777', '19', '96', '2017-03-08 11:51:22');
INSERT INTO `t_role_permission` VALUES ('1779', '19', '97', '2017-03-08 11:51:22');
INSERT INTO `t_role_permission` VALUES ('1781', '19', '99', '2017-03-08 11:51:22');
INSERT INTO `t_role_permission` VALUES ('1783', '19', '104', '2017-03-08 11:51:22');
INSERT INTO `t_role_permission` VALUES ('1785', '19', '108', '2017-03-08 11:51:22');
INSERT INTO `t_role_permission` VALUES ('1787', '19', '109', '2017-03-08 11:51:22');
INSERT INTO `t_role_permission` VALUES ('1789', '19', '110', '2017-03-08 11:51:22');
INSERT INTO `t_role_permission` VALUES ('1791', '19', '111', '2017-03-08 11:51:22');
INSERT INTO `t_role_permission` VALUES ('1793', '19', '112', '2017-03-08 11:51:22');
INSERT INTO `t_role_permission` VALUES ('1795', '19', '121', '2017-03-08 11:51:22');
INSERT INTO `t_role_permission` VALUES ('1797', '19', '124', '2017-03-08 11:51:22');
INSERT INTO `t_role_permission` VALUES ('1799', '19', '125', '2017-03-08 11:51:22');
INSERT INTO `t_role_permission` VALUES ('1801', '19', '127', '2017-03-08 11:51:22');
INSERT INTO `t_role_permission` VALUES ('1803', '19', '128', '2017-03-08 11:51:22');
INSERT INTO `t_role_permission` VALUES ('1805', '19', '129', '2017-03-08 11:51:22');
INSERT INTO `t_role_permission` VALUES ('1807', '19', '130', '2017-03-08 11:51:22');
INSERT INTO `t_role_permission` VALUES ('1809', '19', '131', '2017-03-08 11:51:22');
INSERT INTO `t_role_permission` VALUES ('1811', '19', '132', '2017-03-08 11:51:22');
INSERT INTO `t_role_permission` VALUES ('1813', '19', '133', '2017-03-08 11:51:22');
INSERT INTO `t_role_permission` VALUES ('1815', '19', '135', '2017-03-08 11:51:22');
INSERT INTO `t_role_permission` VALUES ('1817', '19', '136', '2017-03-08 11:51:22');
INSERT INTO `t_role_permission` VALUES ('1819', '19', '137', '2017-03-08 11:51:22');
INSERT INTO `t_role_permission` VALUES ('1821', '19', '141', '2017-03-08 11:51:22');
INSERT INTO `t_role_permission` VALUES ('1823', '23', '2', '2017-03-08 12:50:13');
INSERT INTO `t_role_permission` VALUES ('1825', '23', '3', '2017-03-08 12:50:13');
INSERT INTO `t_role_permission` VALUES ('1827', '23', '4', '2017-03-08 12:50:13');
INSERT INTO `t_role_permission` VALUES ('1829', '23', '6', '2017-03-08 12:50:13');
INSERT INTO `t_role_permission` VALUES ('1831', '23', '7', '2017-03-08 12:50:13');
INSERT INTO `t_role_permission` VALUES ('1833', '23', '8', '2017-03-08 12:50:13');
INSERT INTO `t_role_permission` VALUES ('1835', '23', '9', '2017-03-08 12:50:13');
INSERT INTO `t_role_permission` VALUES ('1837', '23', '15', '2017-03-08 12:50:13');
INSERT INTO `t_role_permission` VALUES ('1839', '23', '16', '2017-03-08 12:50:13');
INSERT INTO `t_role_permission` VALUES ('1841', '23', '20', '2017-03-08 12:50:13');
INSERT INTO `t_role_permission` VALUES ('1843', '23', '21', '2017-03-08 12:50:13');
INSERT INTO `t_role_permission` VALUES ('1845', '23', '25', '2017-03-08 12:50:13');
INSERT INTO `t_role_permission` VALUES ('1847', '23', '28', '2017-03-08 12:50:13');
INSERT INTO `t_role_permission` VALUES ('1849', '23', '41', '2017-03-08 12:50:13');
INSERT INTO `t_role_permission` VALUES ('1851', '23', '46', '2017-03-08 12:50:13');
INSERT INTO `t_role_permission` VALUES ('1853', '23', '49', '2017-03-08 12:50:13');
INSERT INTO `t_role_permission` VALUES ('1855', '23', '50', '2017-03-08 12:50:13');
INSERT INTO `t_role_permission` VALUES ('1857', '23', '51', '2017-03-08 12:50:13');
INSERT INTO `t_role_permission` VALUES ('1859', '23', '54', '2017-03-08 12:50:13');
INSERT INTO `t_role_permission` VALUES ('1863', '23', '59', '2017-03-08 12:50:13');
INSERT INTO `t_role_permission` VALUES ('1865', '23', '60', '2017-03-08 12:50:13');
INSERT INTO `t_role_permission` VALUES ('1867', '23', '61', '2017-03-08 12:50:13');
INSERT INTO `t_role_permission` VALUES ('1869', '23', '65', '2017-03-08 12:50:13');
INSERT INTO `t_role_permission` VALUES ('1871', '23', '66', '2017-03-08 12:50:13');
INSERT INTO `t_role_permission` VALUES ('1873', '23', '67', '2017-03-08 12:50:13');
INSERT INTO `t_role_permission` VALUES ('1875', '23', '68', '2017-03-08 12:50:13');
INSERT INTO `t_role_permission` VALUES ('1877', '23', '69', '2017-03-08 12:50:13');
INSERT INTO `t_role_permission` VALUES ('1879', '23', '71', '2017-03-08 12:50:13');
INSERT INTO `t_role_permission` VALUES ('1881', '23', '76', '2017-03-08 12:50:13');
INSERT INTO `t_role_permission` VALUES ('1883', '23', '77', '2017-03-08 12:50:13');
INSERT INTO `t_role_permission` VALUES ('1885', '23', '78', '2017-03-08 12:50:13');
INSERT INTO `t_role_permission` VALUES ('1887', '23', '79', '2017-03-08 12:50:13');
INSERT INTO `t_role_permission` VALUES ('1889', '23', '80', '2017-03-08 12:50:13');
INSERT INTO `t_role_permission` VALUES ('1891', '23', '81', '2017-03-08 12:50:13');
INSERT INTO `t_role_permission` VALUES ('1893', '23', '82', '2017-03-08 12:50:13');
INSERT INTO `t_role_permission` VALUES ('1895', '23', '83', '2017-03-08 12:50:13');
INSERT INTO `t_role_permission` VALUES ('1897', '23', '86', '2017-03-08 12:50:13');
INSERT INTO `t_role_permission` VALUES ('1899', '23', '87', '2017-03-08 12:50:13');
INSERT INTO `t_role_permission` VALUES ('1901', '23', '90', '2017-03-08 12:50:13');
INSERT INTO `t_role_permission` VALUES ('1903', '23', '91', '2017-03-08 12:50:13');
INSERT INTO `t_role_permission` VALUES ('1905', '23', '92', '2017-03-08 12:50:13');
INSERT INTO `t_role_permission` VALUES ('1907', '23', '102', '2017-03-08 12:50:13');
INSERT INTO `t_role_permission` VALUES ('1909', '23', '108', '2017-03-08 12:50:13');
INSERT INTO `t_role_permission` VALUES ('1911', '23', '109', '2017-03-08 12:50:13');
INSERT INTO `t_role_permission` VALUES ('1913', '23', '110', '2017-03-08 12:50:13');
INSERT INTO `t_role_permission` VALUES ('1915', '23', '111', '2017-03-08 12:50:13');
INSERT INTO `t_role_permission` VALUES ('1917', '23', '112', '2017-03-08 12:50:13');
INSERT INTO `t_role_permission` VALUES ('1919', '23', '115', '2017-03-08 12:50:13');
INSERT INTO `t_role_permission` VALUES ('1921', '23', '116', '2017-03-08 12:50:13');
INSERT INTO `t_role_permission` VALUES ('1923', '23', '117', '2017-03-08 12:50:13');
INSERT INTO `t_role_permission` VALUES ('1925', '23', '118', '2017-03-08 12:50:13');
INSERT INTO `t_role_permission` VALUES ('1927', '23', '119', '2017-03-08 12:50:13');
INSERT INTO `t_role_permission` VALUES ('1929', '23', '120', '2017-03-08 12:50:13');
INSERT INTO `t_role_permission` VALUES ('1931', '23', '121', '2017-03-08 12:50:13');
INSERT INTO `t_role_permission` VALUES ('1933', '23', '122', '2017-03-08 12:50:13');
INSERT INTO `t_role_permission` VALUES ('1935', '23', '123', '2017-03-08 12:50:13');
INSERT INTO `t_role_permission` VALUES ('1937', '23', '124', '2017-03-08 12:50:13');
INSERT INTO `t_role_permission` VALUES ('1939', '23', '125', '2017-03-08 12:50:13');
INSERT INTO `t_role_permission` VALUES ('1941', '23', '130', '2017-03-08 12:50:13');
INSERT INTO `t_role_permission` VALUES ('1943', '23', '131', '2017-03-08 12:50:13');
INSERT INTO `t_role_permission` VALUES ('1945', '23', '132', '2017-03-08 12:50:13');
INSERT INTO `t_role_permission` VALUES ('1947', '23', '133', '2017-03-08 12:50:13');
INSERT INTO `t_role_permission` VALUES ('1949', '23', '134', '2017-03-08 12:50:13');
INSERT INTO `t_role_permission` VALUES ('1951', '23', '135', '2017-03-08 12:50:13');
INSERT INTO `t_role_permission` VALUES ('1953', '23', '136', '2017-03-08 12:50:13');
INSERT INTO `t_role_permission` VALUES ('1955', '23', '137', '2017-03-08 12:50:13');
INSERT INTO `t_role_permission` VALUES ('1957', '23', '141', '2017-03-08 12:50:13');
INSERT INTO `t_role_permission` VALUES ('1959', '25', '2', '2017-03-08 13:43:27');
INSERT INTO `t_role_permission` VALUES ('1961', '25', '3', '2017-03-08 13:43:27');
INSERT INTO `t_role_permission` VALUES ('1963', '25', '4', '2017-03-08 13:43:27');
INSERT INTO `t_role_permission` VALUES ('1965', '25', '7', '2017-03-08 13:43:27');
INSERT INTO `t_role_permission` VALUES ('1967', '25', '8', '2017-03-08 13:43:27');
INSERT INTO `t_role_permission` VALUES ('1969', '25', '9', '2017-03-08 13:43:27');
INSERT INTO `t_role_permission` VALUES ('1971', '25', '20', '2017-03-08 13:43:27');
INSERT INTO `t_role_permission` VALUES ('1973', '25', '21', '2017-03-08 13:43:27');
INSERT INTO `t_role_permission` VALUES ('1975', '25', '25', '2017-03-08 13:43:27');
INSERT INTO `t_role_permission` VALUES ('1977', '25', '28', '2017-03-08 13:43:27');
INSERT INTO `t_role_permission` VALUES ('1979', '25', '41', '2017-03-08 13:43:27');
INSERT INTO `t_role_permission` VALUES ('1981', '25', '46', '2017-03-08 13:43:27');
INSERT INTO `t_role_permission` VALUES ('1983', '25', '49', '2017-03-08 13:43:27');
INSERT INTO `t_role_permission` VALUES ('1985', '25', '50', '2017-03-08 13:43:27');
INSERT INTO `t_role_permission` VALUES ('1987', '25', '51', '2017-03-08 13:43:27');
INSERT INTO `t_role_permission` VALUES ('1989', '25', '54', '2017-03-08 13:43:27');
INSERT INTO `t_role_permission` VALUES ('1993', '25', '59', '2017-03-08 13:43:27');
INSERT INTO `t_role_permission` VALUES ('1995', '25', '60', '2017-03-08 13:43:27');
INSERT INTO `t_role_permission` VALUES ('1997', '25', '61', '2017-03-08 13:43:27');
INSERT INTO `t_role_permission` VALUES ('1999', '25', '65', '2017-03-08 13:43:27');
INSERT INTO `t_role_permission` VALUES ('2001', '25', '66', '2017-03-08 13:43:27');
INSERT INTO `t_role_permission` VALUES ('2003', '25', '67', '2017-03-08 13:43:27');
INSERT INTO `t_role_permission` VALUES ('2005', '25', '68', '2017-03-08 13:43:27');
INSERT INTO `t_role_permission` VALUES ('2007', '25', '69', '2017-03-08 13:43:27');
INSERT INTO `t_role_permission` VALUES ('2009', '25', '70', '2017-03-08 13:43:27');
INSERT INTO `t_role_permission` VALUES ('2011', '25', '71', '2017-03-08 13:43:27');
INSERT INTO `t_role_permission` VALUES ('2013', '25', '76', '2017-03-08 13:43:27');
INSERT INTO `t_role_permission` VALUES ('2015', '25', '77', '2017-03-08 13:43:27');
INSERT INTO `t_role_permission` VALUES ('2017', '25', '78', '2017-03-08 13:43:27');
INSERT INTO `t_role_permission` VALUES ('2019', '25', '79', '2017-03-08 13:43:27');
INSERT INTO `t_role_permission` VALUES ('2021', '25', '80', '2017-03-08 13:43:27');
INSERT INTO `t_role_permission` VALUES ('2023', '25', '81', '2017-03-08 13:43:27');
INSERT INTO `t_role_permission` VALUES ('2025', '25', '82', '2017-03-08 13:43:27');
INSERT INTO `t_role_permission` VALUES ('2027', '25', '83', '2017-03-08 13:43:27');
INSERT INTO `t_role_permission` VALUES ('2029', '25', '84', '2017-03-08 13:43:27');
INSERT INTO `t_role_permission` VALUES ('2031', '25', '85', '2017-03-08 13:43:27');
INSERT INTO `t_role_permission` VALUES ('2033', '25', '86', '2017-03-08 13:43:27');
INSERT INTO `t_role_permission` VALUES ('2035', '25', '87', '2017-03-08 13:43:27');
INSERT INTO `t_role_permission` VALUES ('2037', '25', '90', '2017-03-08 13:43:27');
INSERT INTO `t_role_permission` VALUES ('2039', '25', '91', '2017-03-08 13:43:27');
INSERT INTO `t_role_permission` VALUES ('2041', '25', '92', '2017-03-08 13:43:27');
INSERT INTO `t_role_permission` VALUES ('2043', '25', '93', '2017-03-08 13:43:27');
INSERT INTO `t_role_permission` VALUES ('2045', '25', '94', '2017-03-08 13:43:27');
INSERT INTO `t_role_permission` VALUES ('2047', '25', '95', '2017-03-08 13:43:27');
INSERT INTO `t_role_permission` VALUES ('2049', '25', '96', '2017-03-08 13:43:27');
INSERT INTO `t_role_permission` VALUES ('2051', '25', '99', '2017-03-08 13:43:27');
INSERT INTO `t_role_permission` VALUES ('2053', '25', '102', '2017-03-08 13:43:27');
INSERT INTO `t_role_permission` VALUES ('2055', '25', '108', '2017-03-08 13:43:27');
INSERT INTO `t_role_permission` VALUES ('2057', '25', '109', '2017-03-08 13:43:27');
INSERT INTO `t_role_permission` VALUES ('2059', '25', '110', '2017-03-08 13:43:27');
INSERT INTO `t_role_permission` VALUES ('2061', '25', '111', '2017-03-08 13:43:27');
INSERT INTO `t_role_permission` VALUES ('2063', '25', '112', '2017-03-08 13:43:27');
INSERT INTO `t_role_permission` VALUES ('2065', '25', '115', '2017-03-08 13:43:27');
INSERT INTO `t_role_permission` VALUES ('2067', '25', '116', '2017-03-08 13:43:27');
INSERT INTO `t_role_permission` VALUES ('2069', '25', '117', '2017-03-08 13:43:27');
INSERT INTO `t_role_permission` VALUES ('2071', '25', '118', '2017-03-08 13:43:27');
INSERT INTO `t_role_permission` VALUES ('2073', '25', '119', '2017-03-08 13:43:27');
INSERT INTO `t_role_permission` VALUES ('2075', '25', '120', '2017-03-08 13:43:27');
INSERT INTO `t_role_permission` VALUES ('2077', '25', '121', '2017-03-08 13:43:27');
INSERT INTO `t_role_permission` VALUES ('2079', '25', '122', '2017-03-08 13:43:27');
INSERT INTO `t_role_permission` VALUES ('2081', '25', '123', '2017-03-08 13:43:27');
INSERT INTO `t_role_permission` VALUES ('2083', '25', '124', '2017-03-08 13:43:27');
INSERT INTO `t_role_permission` VALUES ('2085', '25', '125', '2017-03-08 13:43:27');
INSERT INTO `t_role_permission` VALUES ('2087', '25', '130', '2017-03-08 13:43:27');
INSERT INTO `t_role_permission` VALUES ('2089', '25', '131', '2017-03-08 13:43:27');
INSERT INTO `t_role_permission` VALUES ('2091', '25', '132', '2017-03-08 13:43:27');
INSERT INTO `t_role_permission` VALUES ('2093', '25', '133', '2017-03-08 13:43:27');
INSERT INTO `t_role_permission` VALUES ('2095', '25', '134', '2017-03-08 13:43:27');
INSERT INTO `t_role_permission` VALUES ('2097', '25', '135', '2017-03-08 13:43:27');
INSERT INTO `t_role_permission` VALUES ('2099', '25', '136', '2017-03-08 13:43:27');
INSERT INTO `t_role_permission` VALUES ('2101', '25', '137', '2017-03-08 13:43:27');
INSERT INTO `t_role_permission` VALUES ('2103', '25', '141', '2017-03-08 13:43:27');
INSERT INTO `t_role_permission` VALUES ('2105', '27', '2', '2017-03-08 13:53:39');
INSERT INTO `t_role_permission` VALUES ('2107', '27', '3', '2017-03-08 13:53:39');
INSERT INTO `t_role_permission` VALUES ('2109', '27', '4', '2017-03-08 13:53:39');
INSERT INTO `t_role_permission` VALUES ('2111', '27', '6', '2017-03-08 13:53:39');
INSERT INTO `t_role_permission` VALUES ('2113', '27', '7', '2017-03-08 13:53:39');
INSERT INTO `t_role_permission` VALUES ('2115', '27', '8', '2017-03-08 13:53:39');
INSERT INTO `t_role_permission` VALUES ('2117', '27', '9', '2017-03-08 13:53:39');
INSERT INTO `t_role_permission` VALUES ('2119', '27', '15', '2017-03-08 13:53:39');
INSERT INTO `t_role_permission` VALUES ('2121', '27', '16', '2017-03-08 13:53:39');
INSERT INTO `t_role_permission` VALUES ('2123', '27', '19', '2017-03-08 13:53:39');
INSERT INTO `t_role_permission` VALUES ('2125', '27', '20', '2017-03-08 13:53:39');
INSERT INTO `t_role_permission` VALUES ('2127', '27', '21', '2017-03-08 13:53:39');
INSERT INTO `t_role_permission` VALUES ('2129', '27', '23', '2017-03-08 13:53:39');
INSERT INTO `t_role_permission` VALUES ('2131', '27', '24', '2017-03-08 13:53:39');
INSERT INTO `t_role_permission` VALUES ('2133', '27', '25', '2017-03-08 13:53:39');
INSERT INTO `t_role_permission` VALUES ('2135', '27', '26', '2017-03-08 13:53:39');
INSERT INTO `t_role_permission` VALUES ('2137', '27', '27', '2017-03-08 13:53:39');
INSERT INTO `t_role_permission` VALUES ('2139', '27', '28', '2017-03-08 13:53:39');
INSERT INTO `t_role_permission` VALUES ('2141', '27', '29', '2017-03-08 13:53:39');
INSERT INTO `t_role_permission` VALUES ('2143', '27', '30', '2017-03-08 13:53:39');
INSERT INTO `t_role_permission` VALUES ('2145', '27', '31', '2017-03-08 13:53:39');
INSERT INTO `t_role_permission` VALUES ('2147', '27', '32', '2017-03-08 13:53:39');
INSERT INTO `t_role_permission` VALUES ('2163', '27', '40', '2017-03-08 13:53:39');
INSERT INTO `t_role_permission` VALUES ('2165', '27', '41', '2017-03-08 13:53:39');
INSERT INTO `t_role_permission` VALUES ('2167', '27', '44', '2017-03-08 13:53:39');
INSERT INTO `t_role_permission` VALUES ('2169', '27', '45', '2017-03-08 13:53:39');
INSERT INTO `t_role_permission` VALUES ('2171', '27', '46', '2017-03-08 13:53:39');
INSERT INTO `t_role_permission` VALUES ('2173', '27', '49', '2017-03-08 13:53:39');
INSERT INTO `t_role_permission` VALUES ('2175', '27', '50', '2017-03-08 13:53:39');
INSERT INTO `t_role_permission` VALUES ('2177', '27', '51', '2017-03-08 13:53:39');
INSERT INTO `t_role_permission` VALUES ('2179', '27', '52', '2017-03-08 13:53:39');
INSERT INTO `t_role_permission` VALUES ('2181', '27', '53', '2017-03-08 13:53:39');
INSERT INTO `t_role_permission` VALUES ('2183', '27', '54', '2017-03-08 13:53:39');
INSERT INTO `t_role_permission` VALUES ('2185', '27', '55', '2017-03-08 13:53:39');
INSERT INTO `t_role_permission` VALUES ('2187', '27', '56', '2017-03-08 13:53:39');
INSERT INTO `t_role_permission` VALUES ('2191', '27', '59', '2017-03-08 13:53:39');
INSERT INTO `t_role_permission` VALUES ('2193', '27', '60', '2017-03-08 13:53:39');
INSERT INTO `t_role_permission` VALUES ('2195', '27', '61', '2017-03-08 13:53:39');
INSERT INTO `t_role_permission` VALUES ('2197', '27', '62', '2017-03-08 13:53:39');
INSERT INTO `t_role_permission` VALUES ('2199', '27', '63', '2017-03-08 13:53:39');
INSERT INTO `t_role_permission` VALUES ('2201', '27', '65', '2017-03-08 13:53:39');
INSERT INTO `t_role_permission` VALUES ('2203', '27', '66', '2017-03-08 13:53:39');
INSERT INTO `t_role_permission` VALUES ('2205', '27', '67', '2017-03-08 13:53:39');
INSERT INTO `t_role_permission` VALUES ('2207', '27', '68', '2017-03-08 13:53:39');
INSERT INTO `t_role_permission` VALUES ('2209', '27', '69', '2017-03-08 13:53:39');
INSERT INTO `t_role_permission` VALUES ('2211', '27', '70', '2017-03-08 13:53:39');
INSERT INTO `t_role_permission` VALUES ('2213', '27', '71', '2017-03-08 13:53:39');
INSERT INTO `t_role_permission` VALUES ('2215', '27', '74', '2017-03-08 13:53:39');
INSERT INTO `t_role_permission` VALUES ('2217', '27', '75', '2017-03-08 13:53:39');
INSERT INTO `t_role_permission` VALUES ('2219', '27', '76', '2017-03-08 13:53:39');
INSERT INTO `t_role_permission` VALUES ('2221', '27', '77', '2017-03-08 13:53:39');
INSERT INTO `t_role_permission` VALUES ('2223', '27', '78', '2017-03-08 13:53:39');
INSERT INTO `t_role_permission` VALUES ('2225', '27', '79', '2017-03-08 13:53:39');
INSERT INTO `t_role_permission` VALUES ('2227', '27', '80', '2017-03-08 13:53:39');
INSERT INTO `t_role_permission` VALUES ('2229', '27', '81', '2017-03-08 13:53:39');
INSERT INTO `t_role_permission` VALUES ('2231', '27', '82', '2017-03-08 13:53:39');
INSERT INTO `t_role_permission` VALUES ('2233', '27', '83', '2017-03-08 13:53:39');
INSERT INTO `t_role_permission` VALUES ('2235', '27', '84', '2017-03-08 13:53:39');
INSERT INTO `t_role_permission` VALUES ('2237', '27', '85', '2017-03-08 13:53:39');
INSERT INTO `t_role_permission` VALUES ('2239', '27', '86', '2017-03-08 13:53:39');
INSERT INTO `t_role_permission` VALUES ('2241', '27', '87', '2017-03-08 13:53:39');
INSERT INTO `t_role_permission` VALUES ('2243', '27', '88', '2017-03-08 13:53:39');
INSERT INTO `t_role_permission` VALUES ('2245', '27', '89', '2017-03-08 13:53:39');
INSERT INTO `t_role_permission` VALUES ('2247', '27', '90', '2017-03-08 13:53:39');
INSERT INTO `t_role_permission` VALUES ('2249', '27', '91', '2017-03-08 13:53:39');
INSERT INTO `t_role_permission` VALUES ('2251', '27', '92', '2017-03-08 13:53:39');
INSERT INTO `t_role_permission` VALUES ('2253', '27', '93', '2017-03-08 13:53:39');
INSERT INTO `t_role_permission` VALUES ('2255', '27', '94', '2017-03-08 13:53:39');
INSERT INTO `t_role_permission` VALUES ('2257', '27', '95', '2017-03-08 13:53:39');
INSERT INTO `t_role_permission` VALUES ('2259', '27', '96', '2017-03-08 13:53:39');
INSERT INTO `t_role_permission` VALUES ('2261', '27', '97', '2017-03-08 13:53:39');
INSERT INTO `t_role_permission` VALUES ('2263', '27', '98', '2017-03-08 13:53:39');
INSERT INTO `t_role_permission` VALUES ('2265', '27', '99', '2017-03-08 13:53:39');
INSERT INTO `t_role_permission` VALUES ('2267', '27', '102', '2017-03-08 13:53:39');
INSERT INTO `t_role_permission` VALUES ('2269', '27', '104', '2017-03-08 13:53:39');
INSERT INTO `t_role_permission` VALUES ('2271', '27', '108', '2017-03-08 13:53:39');
INSERT INTO `t_role_permission` VALUES ('2273', '27', '109', '2017-03-08 13:53:39');
INSERT INTO `t_role_permission` VALUES ('2275', '27', '110', '2017-03-08 13:53:39');
INSERT INTO `t_role_permission` VALUES ('2277', '27', '111', '2017-03-08 13:53:39');
INSERT INTO `t_role_permission` VALUES ('2279', '27', '112', '2017-03-08 13:53:39');
INSERT INTO `t_role_permission` VALUES ('2281', '27', '114', '2017-03-08 13:53:39');
INSERT INTO `t_role_permission` VALUES ('2283', '27', '117', '2017-03-08 13:53:39');
INSERT INTO `t_role_permission` VALUES ('2285', '27', '118', '2017-03-08 13:53:39');
INSERT INTO `t_role_permission` VALUES ('2287', '27', '119', '2017-03-08 13:53:39');
INSERT INTO `t_role_permission` VALUES ('2289', '27', '121', '2017-03-08 13:53:39');
INSERT INTO `t_role_permission` VALUES ('2291', '27', '124', '2017-03-08 13:53:39');
INSERT INTO `t_role_permission` VALUES ('2293', '27', '125', '2017-03-08 13:53:39');
INSERT INTO `t_role_permission` VALUES ('2295', '27', '127', '2017-03-08 13:53:39');
INSERT INTO `t_role_permission` VALUES ('2297', '27', '128', '2017-03-08 13:53:39');
INSERT INTO `t_role_permission` VALUES ('2299', '27', '129', '2017-03-08 13:53:39');
INSERT INTO `t_role_permission` VALUES ('2301', '27', '130', '2017-03-08 13:53:39');
INSERT INTO `t_role_permission` VALUES ('2303', '27', '131', '2017-03-08 13:53:39');
INSERT INTO `t_role_permission` VALUES ('2305', '27', '132', '2017-03-08 13:53:39');
INSERT INTO `t_role_permission` VALUES ('2307', '27', '133', '2017-03-08 13:53:39');
INSERT INTO `t_role_permission` VALUES ('2309', '27', '134', '2017-03-08 13:53:39');
INSERT INTO `t_role_permission` VALUES ('2311', '27', '135', '2017-03-08 13:53:39');
INSERT INTO `t_role_permission` VALUES ('2313', '27', '136', '2017-03-08 13:53:39');
INSERT INTO `t_role_permission` VALUES ('2315', '27', '137', '2017-03-08 13:53:39');
INSERT INTO `t_role_permission` VALUES ('2317', '27', '138', '2017-03-08 13:53:39');
INSERT INTO `t_role_permission` VALUES ('2321', '27', '140', '2017-03-08 13:53:39');
INSERT INTO `t_role_permission` VALUES ('2323', '27', '141', '2017-03-08 13:53:39');
INSERT INTO `t_role_permission` VALUES ('2325', '27', '145', '2017-03-08 13:53:39');
INSERT INTO `t_role_permission` VALUES ('2329', '15', '18', '2017-03-08 14:18:15');
INSERT INTO `t_role_permission` VALUES ('2331', '15', '57', '2017-03-08 14:18:15');
INSERT INTO `t_role_permission` VALUES ('2333', '15', '100', '2017-03-08 14:18:15');
INSERT INTO `t_role_permission` VALUES ('2335', '15', '107', '2017-03-08 14:18:15');
INSERT INTO `t_role_permission` VALUES ('2337', '15', '113', '2017-03-08 14:18:15');
INSERT INTO `t_role_permission` VALUES ('2339', '15', '126', '2017-03-08 14:18:15');
INSERT INTO `t_role_permission` VALUES ('2341', '15', '144', '2017-03-08 14:18:15');
INSERT INTO `t_role_permission` VALUES ('2345', '27', '18', '2017-03-08 14:19:39');
INSERT INTO `t_role_permission` VALUES ('2347', '27', '57', '2017-03-08 14:19:39');
INSERT INTO `t_role_permission` VALUES ('2349', '27', '100', '2017-03-08 14:19:39');
INSERT INTO `t_role_permission` VALUES ('2351', '27', '107', '2017-03-08 14:19:39');
INSERT INTO `t_role_permission` VALUES ('2353', '27', '113', '2017-03-08 14:19:39');
INSERT INTO `t_role_permission` VALUES ('2355', '27', '126', '2017-03-08 14:19:39');
INSERT INTO `t_role_permission` VALUES ('2357', '27', '144', '2017-03-08 14:19:39');
INSERT INTO `t_role_permission` VALUES ('2361', '25', '18', '2017-03-08 14:20:50');
INSERT INTO `t_role_permission` VALUES ('2363', '25', '57', '2017-03-08 14:20:50');
INSERT INTO `t_role_permission` VALUES ('2365', '25', '100', '2017-03-08 14:20:50');
INSERT INTO `t_role_permission` VALUES ('2367', '25', '107', '2017-03-08 14:20:50');
INSERT INTO `t_role_permission` VALUES ('2369', '25', '113', '2017-03-08 14:20:50');
INSERT INTO `t_role_permission` VALUES ('2371', '25', '126', '2017-03-08 14:20:50');
INSERT INTO `t_role_permission` VALUES ('2375', '23', '18', '2017-03-08 14:21:25');
INSERT INTO `t_role_permission` VALUES ('2377', '23', '57', '2017-03-08 14:21:25');
INSERT INTO `t_role_permission` VALUES ('2379', '23', '100', '2017-03-08 14:21:25');
INSERT INTO `t_role_permission` VALUES ('2381', '23', '107', '2017-03-08 14:21:25');
INSERT INTO `t_role_permission` VALUES ('2383', '23', '113', '2017-03-08 14:21:25');
INSERT INTO `t_role_permission` VALUES ('2385', '23', '126', '2017-03-08 14:21:25');
INSERT INTO `t_role_permission` VALUES ('2389', '19', '18', '2017-03-08 14:21:59');
INSERT INTO `t_role_permission` VALUES ('2391', '19', '57', '2017-03-08 14:21:59');
INSERT INTO `t_role_permission` VALUES ('2393', '19', '100', '2017-03-08 14:21:59');
INSERT INTO `t_role_permission` VALUES ('2395', '19', '107', '2017-03-08 14:21:59');
INSERT INTO `t_role_permission` VALUES ('2397', '19', '113', '2017-03-08 14:21:59');
INSERT INTO `t_role_permission` VALUES ('2399', '19', '126', '2017-03-08 14:21:59');
INSERT INTO `t_role_permission` VALUES ('2403', '17', '18', '2017-03-08 14:24:55');
INSERT INTO `t_role_permission` VALUES ('2405', '17', '57', '2017-03-08 14:24:55');
INSERT INTO `t_role_permission` VALUES ('2407', '17', '100', '2017-03-08 14:24:55');
INSERT INTO `t_role_permission` VALUES ('2409', '17', '107', '2017-03-08 14:24:55');
INSERT INTO `t_role_permission` VALUES ('2411', '17', '113', '2017-03-08 14:24:55');
INSERT INTO `t_role_permission` VALUES ('2413', '17', '126', '2017-03-08 14:24:55');
INSERT INTO `t_role_permission` VALUES ('2417', '3', '145', '2017-03-10 14:14:06');
INSERT INTO `t_role_permission` VALUES ('2423', '3', '144', '2017-03-10 14:15:32');
INSERT INTO `t_role_permission` VALUES ('2425', '5', '145', '2017-03-10 14:16:19');
INSERT INTO `t_role_permission` VALUES ('2427', '5', '144', '2017-03-10 14:17:39');
INSERT INTO `t_role_permission` VALUES ('2431', '7', '144', '2017-03-10 14:17:50');
INSERT INTO `t_role_permission` VALUES ('2433', '7', '145', '2017-03-10 14:17:50');
INSERT INTO `t_role_permission` VALUES ('2437', '11', '144', '2017-03-10 14:18:25');
INSERT INTO `t_role_permission` VALUES ('2439', '11', '145', '2017-03-10 14:18:25');
INSERT INTO `t_role_permission` VALUES ('2441', '12', '144', '2017-03-10 14:18:46');
INSERT INTO `t_role_permission` VALUES ('2443', '12', '145', '2017-03-10 14:18:46');
INSERT INTO `t_role_permission` VALUES ('2445', '3', '25', '2017-03-11 16:09:22');
INSERT INTO `t_role_permission` VALUES ('2447', '3', '40', '2017-03-11 16:09:22');
INSERT INTO `t_role_permission` VALUES ('2449', '3', '41', '2017-03-11 16:09:22');
INSERT INTO `t_role_permission` VALUES ('2451', '3', '14', '2017-03-11 16:12:36');
INSERT INTO `t_role_permission` VALUES ('2455', '3', '27', '2017-03-11 16:12:36');
INSERT INTO `t_role_permission` VALUES ('2457', '3', '28', '2017-03-11 16:12:36');
INSERT INTO `t_role_permission` VALUES ('2459', '3', '29', '2017-03-11 16:12:36');
INSERT INTO `t_role_permission` VALUES ('2461', '3', '30', '2017-03-11 16:12:36');
INSERT INTO `t_role_permission` VALUES ('2463', '3', '31', '2017-03-11 16:12:36');
INSERT INTO `t_role_permission` VALUES ('2465', '3', '32', '2017-03-11 16:12:36');
INSERT INTO `t_role_permission` VALUES ('2481', '3', '44', '2017-03-11 16:12:36');
INSERT INTO `t_role_permission` VALUES ('2483', '3', '47', '2017-03-11 16:12:36');
INSERT INTO `t_role_permission` VALUES ('2485', '3', '98', '2017-03-11 16:12:36');
INSERT INTO `t_role_permission` VALUES ('2487', '3', '100', '2017-03-11 16:12:36');
INSERT INTO `t_role_permission` VALUES ('2489', '3', '103', '2017-03-11 16:12:36');
INSERT INTO `t_role_permission` VALUES ('2555', '5', '115', '2017-03-13 15:35:28');
INSERT INTO `t_role_permission` VALUES ('2557', '5', '116', '2017-03-13 15:35:28');
INSERT INTO `t_role_permission` VALUES ('2559', '5', '120', '2017-03-13 15:35:28');
INSERT INTO `t_role_permission` VALUES ('2561', '5', '122', '2017-03-13 15:35:28');
INSERT INTO `t_role_permission` VALUES ('2563', '5', '123', '2017-03-13 15:35:28');
INSERT INTO `t_role_permission` VALUES ('2567', '1', '309', '2017-03-27 18:19:34');
INSERT INTO `t_role_permission` VALUES ('2569', '3', '309', '2017-03-27 18:19:34');
INSERT INTO `t_role_permission` VALUES ('2571', '4', '309', '2017-03-27 18:19:34');
INSERT INTO `t_role_permission` VALUES ('2573', '5', '309', '2017-03-27 18:19:34');
INSERT INTO `t_role_permission` VALUES ('2575', '7', '309', '2017-03-27 18:19:34');
INSERT INTO `t_role_permission` VALUES ('2577', '8', '309', '2017-03-27 18:19:34');
INSERT INTO `t_role_permission` VALUES ('2579', '1', '311', '2017-03-28 09:17:09');
INSERT INTO `t_role_permission` VALUES ('2581', '2', '311', '2017-03-28 09:17:09');
INSERT INTO `t_role_permission` VALUES ('2583', '3', '311', '2017-03-28 09:17:09');
INSERT INTO `t_role_permission` VALUES ('2585', '8', '311', '2017-03-28 09:17:09');
INSERT INTO `t_role_permission` VALUES ('2587', '12', '311', '2017-03-28 09:17:09');
INSERT INTO `t_role_permission` VALUES ('2589', '1', '315', '2017-03-28 10:31:56');
INSERT INTO `t_role_permission` VALUES ('2591', '3', '315', '2017-03-28 10:31:56');
INSERT INTO `t_role_permission` VALUES ('2593', '8', '315', '2017-03-28 10:31:56');
INSERT INTO `t_role_permission` VALUES ('2595', '1', '313', '2017-03-28 10:32:18');
INSERT INTO `t_role_permission` VALUES ('2599', '3', '313', '2017-03-28 10:32:18');
INSERT INTO `t_role_permission` VALUES ('2601', '8', '313', '2017-03-28 10:32:18');
INSERT INTO `t_role_permission` VALUES ('2603', '1', '317', '2017-03-28 20:26:48');
INSERT INTO `t_role_permission` VALUES ('2605', '2', '317', '2017-03-28 20:26:48');
INSERT INTO `t_role_permission` VALUES ('2607', '3', '317', '2017-03-28 20:26:48');
INSERT INTO `t_role_permission` VALUES ('2609', '4', '317', '2017-03-28 20:26:48');
INSERT INTO `t_role_permission` VALUES ('2611', '5', '317', '2017-03-28 20:26:48');
INSERT INTO `t_role_permission` VALUES ('2613', '8', '317', '2017-03-28 20:26:48');
INSERT INTO `t_role_permission` VALUES ('2615', '10', '317', '2017-03-28 20:26:48');
INSERT INTO `t_role_permission` VALUES ('2617', '11', '317', '2017-03-28 20:26:48');
INSERT INTO `t_role_permission` VALUES ('2619', '12', '317', '2017-03-28 20:26:48');
INSERT INTO `t_role_permission` VALUES ('2621', '6', '317', '2017-04-05 15:32:24');
INSERT INTO `t_role_permission` VALUES ('2623', '2', '315', '2017-03-28 09:17:09');
INSERT INTO `t_role_permission` VALUES ('2624', '1', '146', '2017-05-12 15:49:54');
INSERT INTO `t_role_permission` VALUES ('2625', '1', '147', '2017-05-12 15:49:57');
INSERT INTO `t_role_permission` VALUES ('2626', '1', '323', '2017-05-12 15:52:02');
INSERT INTO `t_role_permission` VALUES ('2627', '1', '327', '2017-05-12 15:52:04');
INSERT INTO `t_role_permission` VALUES ('2628', '1', '325', '2017-05-12 15:52:07');
INSERT INTO `t_role_permission` VALUES ('2629', '1', '321', '2017-05-12 15:52:16');
INSERT INTO `t_role_permission` VALUES ('2631', '1', '322', '2017-05-18 09:47:14');
INSERT INTO `t_role_permission` VALUES ('2632', '1', '324', '2017-05-18 09:47:37');
INSERT INTO `t_role_permission` VALUES ('2633', '1', '326', '2017-05-18 09:48:06');
INSERT INTO `t_role_permission` VALUES ('2634', '1', '328', '2017-05-18 09:48:19');
INSERT INTO `t_role_permission` VALUES ('2635', '1', '329', '2017-05-18 09:48:32');
INSERT INTO `t_role_permission` VALUES ('2636', '1', '330', '2017-05-18 09:48:49');
INSERT INTO `t_role_permission` VALUES ('2637', '1', '331', '2017-05-30 14:54:57');
INSERT INTO `t_role_permission` VALUES ('2638', '1', '332', '2017-05-30 14:54:57');
INSERT INTO `t_role_permission` VALUES ('2639', '1', '333', '2017-06-16 11:03:26');
INSERT INTO `t_role_permission` VALUES ('2640', '1', '334', '2017-06-29 17:11:57');
INSERT INTO `t_role_permission` VALUES ('2641', '5', '334', '2017-06-29 17:11:57');
INSERT INTO `t_role_permission` VALUES ('2642', '1', '335', '2017-06-29 17:17:16');
INSERT INTO `t_role_permission` VALUES ('2643', '2', '335', '2017-06-29 17:17:16');
INSERT INTO `t_role_permission` VALUES ('2644', '3', '335', '2017-06-29 17:17:16');
INSERT INTO `t_role_permission` VALUES ('2645', '4', '335', '2017-06-29 17:17:16');
INSERT INTO `t_role_permission` VALUES ('2646', '5', '335', '2017-06-29 17:17:16');
INSERT INTO `t_role_permission` VALUES ('2647', '6', '335', '2017-06-29 17:17:16');
INSERT INTO `t_role_permission` VALUES ('2648', '7', '335', '2017-06-29 17:17:16');
INSERT INTO `t_role_permission` VALUES ('2649', '8', '335', '2017-06-29 17:17:16');
INSERT INTO `t_role_permission` VALUES ('2650', '10', '335', '2017-06-29 17:17:16');
INSERT INTO `t_role_permission` VALUES ('2651', '11', '335', '2017-06-29 17:17:16');
INSERT INTO `t_role_permission` VALUES ('2652', '12', '335', '2017-06-29 17:17:16');
INSERT INTO `t_role_permission` VALUES ('2653', '13', '335', '2017-06-29 17:17:16');
INSERT INTO `t_role_permission` VALUES ('2654', '1', '336', '2017-07-28 13:36:21');
INSERT INTO `t_role_permission` VALUES ('2655', '2', '336', '2017-07-28 13:36:21');
INSERT INTO `t_role_permission` VALUES ('2656', '3', '336', '2017-07-28 13:36:21');
INSERT INTO `t_role_permission` VALUES ('2657', '5', '336', '2017-07-28 13:36:21');
INSERT INTO `t_role_permission` VALUES ('2658', '7', '336', '2017-07-28 13:36:21');
INSERT INTO `t_role_permission` VALUES ('2659', '8', '336', '2017-07-28 13:36:21');
INSERT INTO `t_role_permission` VALUES ('2660', '1', '337', '2017-08-11 14:04:15');
INSERT INTO `t_role_permission` VALUES ('2661', '5', '337', '2017-08-11 14:04:15');
INSERT INTO `t_role_permission` VALUES ('2662', '1', '338', '2017-08-15 08:57:56');
INSERT INTO `t_role_permission` VALUES ('2663', '5', '338', '2017-08-15 08:57:56');
INSERT INTO `t_role_permission` VALUES ('2664', '1', '339', '2017-08-24 16:14:24');
INSERT INTO `t_role_permission` VALUES ('2665', '2', '339', '2017-08-24 16:14:24');
INSERT INTO `t_role_permission` VALUES ('2666', '3', '339', '2017-08-24 16:14:24');
INSERT INTO `t_role_permission` VALUES ('2667', '4', '339', '2017-08-24 16:14:24');
INSERT INTO `t_role_permission` VALUES ('2668', '5', '339', '2017-08-24 16:14:24');
INSERT INTO `t_role_permission` VALUES ('2669', '6', '339', '2017-08-24 16:14:24');
INSERT INTO `t_role_permission` VALUES ('2670', '7', '339', '2017-08-24 16:14:24');
INSERT INTO `t_role_permission` VALUES ('2671', '11', '339', '2017-08-24 16:14:24');
INSERT INTO `t_role_permission` VALUES ('2672', '12', '339', '2017-08-24 16:14:24');
INSERT INTO `t_role_permission` VALUES ('2673', '8', '1', '2017-08-29 11:12:36');
INSERT INTO `t_role_permission` VALUES ('2676', '8', '319', '2017-10-04 16:26:17');
INSERT INTO `t_role_permission` VALUES ('2677', '1', '340', '2018-01-15 11:25:14');
INSERT INTO `t_role_permission` VALUES ('2678', '5', '340', '2018-01-15 11:25:14');
INSERT INTO `t_role_permission` VALUES ('2679', '7', '340', '2018-01-15 11:25:14');
INSERT INTO `t_role_permission` VALUES ('2680', '1', '341', '2018-01-15 11:25:25');
INSERT INTO `t_role_permission` VALUES ('2681', '4', '341', '2018-01-15 11:25:25');
INSERT INTO `t_role_permission` VALUES ('2682', '5', '341', '2018-01-15 11:25:25');
INSERT INTO `t_role_permission` VALUES ('2683', '6', '341', '2018-01-15 11:25:25');
INSERT INTO `t_role_permission` VALUES ('2684', '7', '341', '2018-01-15 11:25:25');
INSERT INTO `t_role_permission` VALUES ('2685', '1', '342', '2018-03-28 14:28:27');
INSERT INTO `t_role_permission` VALUES ('2686', '5', '342', '2018-03-28 14:28:27');
INSERT INTO `t_role_permission` VALUES ('2687', '7', '342', '2018-03-28 14:28:27');
INSERT INTO `t_role_permission` VALUES ('2688', '8', '342', '2018-03-28 14:28:27');
INSERT INTO `t_role_permission` VALUES ('2689', '1', '343', '2018-03-28 14:35:19');
INSERT INTO `t_role_permission` VALUES ('2690', '5', '343', '2018-03-28 14:35:19');
INSERT INTO `t_role_permission` VALUES ('2691', '7', '343', '2018-03-28 14:35:19');
INSERT INTO `t_role_permission` VALUES ('2692', '1', '344', '2018-04-05 15:45:19');
INSERT INTO `t_role_permission` VALUES ('2693', '2', '344', '2018-04-05 15:45:19');
INSERT INTO `t_role_permission` VALUES ('2694', '3', '344', '2018-04-05 15:45:19');
INSERT INTO `t_role_permission` VALUES ('2695', '4', '344', '2018-04-05 15:45:19');
INSERT INTO `t_role_permission` VALUES ('2696', '5', '344', '2018-04-05 15:45:19');
INSERT INTO `t_role_permission` VALUES ('2697', '6', '344', '2018-04-05 15:45:19');
INSERT INTO `t_role_permission` VALUES ('2698', '7', '344', '2018-04-05 15:45:19');
INSERT INTO `t_role_permission` VALUES ('2699', '8', '344', '2018-04-05 15:45:19');
INSERT INTO `t_role_permission` VALUES ('2700', '1', '345', '2018-05-30 10:18:06');
INSERT INTO `t_role_permission` VALUES ('2701', '2', '345', '2018-05-30 10:18:06');
INSERT INTO `t_role_permission` VALUES ('2702', '3', '345', '2018-05-30 10:18:06');
INSERT INTO `t_role_permission` VALUES ('2703', '4', '345', '2018-05-30 10:18:06');
INSERT INTO `t_role_permission` VALUES ('2704', '5', '345', '2018-05-30 10:18:06');
INSERT INTO `t_role_permission` VALUES ('2705', '6', '345', '2018-05-30 10:18:06');
INSERT INTO `t_role_permission` VALUES ('2706', '7', '345', '2018-05-30 10:18:06');
INSERT INTO `t_role_permission` VALUES ('2707', '8', '345', '2018-05-30 10:18:06');
INSERT INTO `t_role_permission` VALUES ('2708', '10', '345', '2018-05-30 10:18:06');
INSERT INTO `t_role_permission` VALUES ('2709', '1', '346', '2018-06-28 17:14:20');
INSERT INTO `t_role_permission` VALUES ('2710', '5', '346', '2018-06-28 17:14:20');
INSERT INTO `t_role_permission` VALUES ('2711', '8', '346', '2018-06-28 17:14:20');
INSERT INTO `t_role_permission` VALUES ('2712', '1', '347', '2018-06-29 11:20:55');
INSERT INTO `t_role_permission` VALUES ('2713', '4', '347', '2018-06-29 11:20:55');
INSERT INTO `t_role_permission` VALUES ('2714', '5', '347', '2018-06-29 11:20:55');
INSERT INTO `t_role_permission` VALUES ('2715', '1', '348', '2018-12-10 01:14:51');
INSERT INTO `t_role_permission` VALUES ('2716', '2', '348', '2018-12-10 01:14:51');
INSERT INTO `t_role_permission` VALUES ('2717', '3', '348', '2018-12-10 01:14:51');
INSERT INTO `t_role_permission` VALUES ('2718', '4', '348', '2018-12-10 01:14:51');
INSERT INTO `t_role_permission` VALUES ('2719', '5', '348', '2018-12-10 01:14:51');
INSERT INTO `t_role_permission` VALUES ('2720', '8', '348', '2018-12-10 01:14:51');
INSERT INTO `t_role_permission` VALUES ('2721', '10', '348', '2018-12-10 01:14:51');
INSERT INTO `t_role_permission` VALUES ('2722', '11', '348', '2018-12-10 01:14:51');
INSERT INTO `t_role_permission` VALUES ('2723', '1', '349', '2018-12-10 01:26:33');
INSERT INTO `t_role_permission` VALUES ('2724', '2', '349', '2018-12-10 01:26:33');
INSERT INTO `t_role_permission` VALUES ('2725', '3', '349', '2018-12-10 01:26:33');
INSERT INTO `t_role_permission` VALUES ('2726', '4', '349', '2018-12-10 01:26:33');
INSERT INTO `t_role_permission` VALUES ('2727', '5', '349', '2018-12-10 01:26:33');
INSERT INTO `t_role_permission` VALUES ('2728', '8', '349', '2018-12-10 01:26:33');
INSERT INTO `t_role_permission` VALUES ('2729', '10', '349', '2018-12-10 01:26:33');
INSERT INTO `t_role_permission` VALUES ('2730', '11', '349', '2018-12-10 01:26:33');
INSERT INTO `t_role_permission` VALUES ('2731', '1', '350', '2018-12-10 01:30:09');
INSERT INTO `t_role_permission` VALUES ('2732', '2', '350', '2018-12-10 01:30:09');
INSERT INTO `t_role_permission` VALUES ('2733', '3', '350', '2018-12-10 01:30:09');
INSERT INTO `t_role_permission` VALUES ('2734', '4', '350', '2018-12-10 01:30:09');
INSERT INTO `t_role_permission` VALUES ('2735', '5', '350', '2018-12-10 01:30:09');
INSERT INTO `t_role_permission` VALUES ('2736', '8', '350', '2018-12-10 01:30:09');
INSERT INTO `t_role_permission` VALUES ('2737', '10', '350', '2018-12-10 01:30:09');
INSERT INTO `t_role_permission` VALUES ('2738', '11', '350', '2018-12-10 01:30:09');
INSERT INTO `t_role_permission` VALUES ('2739', '1', '351', '2018-12-10 01:30:09');
INSERT INTO `t_role_permission` VALUES ('2740', '2', '351', '2018-12-10 01:30:09');
INSERT INTO `t_role_permission` VALUES ('2741', '3', '351', '2018-12-10 01:30:09');
INSERT INTO `t_role_permission` VALUES ('2742', '4', '351', '2018-12-10 01:30:09');
INSERT INTO `t_role_permission` VALUES ('2743', '5', '351', '2018-12-10 01:30:09');
INSERT INTO `t_role_permission` VALUES ('2744', '8', '351', '2018-12-10 01:30:09');
INSERT INTO `t_role_permission` VALUES ('2745', '10', '351', '2018-12-10 01:30:09');
INSERT INTO `t_role_permission` VALUES ('2746', '11', '351', '2018-12-10 01:30:09');
INSERT INTO `t_role_permission` VALUES ('2747', '1', '352', '2018-12-10 01:30:09');
INSERT INTO `t_role_permission` VALUES ('2748', '2', '352', '2018-12-10 01:30:09');
INSERT INTO `t_role_permission` VALUES ('2749', '3', '352', '2018-12-10 01:30:09');
INSERT INTO `t_role_permission` VALUES ('2750', '4', '352', '2018-12-10 01:30:09');
INSERT INTO `t_role_permission` VALUES ('2751', '5', '352', '2018-12-10 01:30:09');
INSERT INTO `t_role_permission` VALUES ('2752', '8', '352', '2018-12-10 01:30:09');
INSERT INTO `t_role_permission` VALUES ('2753', '10', '352', '2018-12-10 01:30:09');
INSERT INTO `t_role_permission` VALUES ('2754', '11', '352', '2018-12-10 01:30:09');
INSERT INTO `t_role_permission` VALUES ('2755', '1', '353', '2018-12-10 01:30:09');
INSERT INTO `t_role_permission` VALUES ('2756', '2', '353', '2018-12-10 01:30:09');
INSERT INTO `t_role_permission` VALUES ('2757', '3', '353', '2018-12-10 01:30:09');
INSERT INTO `t_role_permission` VALUES ('2758', '4', '353', '2018-12-10 01:30:09');
INSERT INTO `t_role_permission` VALUES ('2759', '5', '353', '2018-12-10 01:30:09');
INSERT INTO `t_role_permission` VALUES ('2760', '8', '353', '2018-12-10 01:30:09');
INSERT INTO `t_role_permission` VALUES ('2761', '10', '353', '2018-12-10 01:30:09');
INSERT INTO `t_role_permission` VALUES ('2762', '11', '353', '2018-12-10 01:30:09');
INSERT INTO `t_role_permission` VALUES ('2763', '1', '354', '2018-12-10 01:30:09');
INSERT INTO `t_role_permission` VALUES ('2764', '2', '354', '2018-12-10 01:30:09');
INSERT INTO `t_role_permission` VALUES ('2765', '3', '354', '2018-12-10 01:30:09');
INSERT INTO `t_role_permission` VALUES ('2766', '4', '354', '2018-12-10 01:30:09');
INSERT INTO `t_role_permission` VALUES ('2767', '5', '354', '2018-12-10 01:30:09');
INSERT INTO `t_role_permission` VALUES ('2768', '8', '354', '2018-12-10 01:30:09');
INSERT INTO `t_role_permission` VALUES ('2769', '10', '354', '2018-12-10 01:30:09');
INSERT INTO `t_role_permission` VALUES ('2770', '11', '354', '2018-12-10 01:30:09');
INSERT INTO `t_role_permission` VALUES ('2771', '1', '355', '2018-12-24 11:22:00');
INSERT INTO `t_role_permission` VALUES ('2772', '4', '355', '2018-12-24 11:22:00');
INSERT INTO `t_role_permission` VALUES ('2773', '5', '355', '2018-12-24 11:22:00');

-- ----------------------------
-- Table structure for `t_user_role`
-- ----------------------------
DROP TABLE IF EXISTS `t_user_role`;
CREATE TABLE `t_user_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `username` varchar(32) DEFAULT NULL COMMENT '用户名',
  `role_id` int(11) DEFAULT NULL COMMENT '角色ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COMMENT='权限控制的 用户角色表,表示用户和角色之间的对应关系';

-- ----------------------------
-- Records of t_user_role
-- ----------------------------
INSERT INTO `t_user_role` VALUES ('1', 'admin', '1', '2018-12-04 03:03:32');
INSERT INTO `t_user_role` VALUES ('2', 'don', '1', '2018-12-04 23:56:44');
INSERT INTO `t_user_role` VALUES ('3', 'wind', '1', '2018-12-04 23:55:14');
INSERT INTO `t_user_role` VALUES ('4', 'austin', '1', '2020-09-24 00:24:02');

-- ----------------------------
-- Table structure for `union`
-- ----------------------------
DROP TABLE IF EXISTS `union`;
CREATE TABLE `union` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '工会ID',
  `president` varchar(50) DEFAULT NULL COMMENT '工会会长',
  `level` int(11) DEFAULT '0' COMMENT '工会等级',
  `createTime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `max` int(11) DEFAULT '0' COMMENT '工会最大人数',
  `remark` varchar(100) DEFAULT NULL COMMENT '备注',
  `rank` int(11) DEFAULT '0',
  `nums` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of union
-- ----------------------------

-- ----------------------------
-- Table structure for `union_staff`
-- ----------------------------
DROP TABLE IF EXISTS `union_staff`;
CREATE TABLE `union_staff` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) DEFAULT NULL COMMENT '用户名',
  `updateTime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '人员加入或离开时间',
  `remark` varchar(100) DEFAULT NULL COMMENT '备注',
  `level` int(11) DEFAULT NULL,
  `union_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of union_staff
-- ----------------------------

-- ----------------------------
-- Table structure for `urge_order`
-- ----------------------------
DROP TABLE IF EXISTS `urge_order`;
CREATE TABLE `urge_order` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `loginname` varchar(255) NOT NULL,
  `accountName` varchar(255) DEFAULT NULL COMMENT '真实姓名',
  `thirdOrder` varchar(800) DEFAULT NULL COMMENT '第三方订单号',
  `nickname` varchar(255) DEFAULT NULL COMMENT '昵称',
  `depositTime` datetime DEFAULT NULL COMMENT '存款时间',
  `amount` double(10,2) DEFAULT NULL COMMENT '金额',
  `createtime` datetime DEFAULT NULL,
  `remark` varchar(2555) DEFAULT NULL COMMENT '备注',
  `status` int(1) DEFAULT NULL COMMENT '状态',
  `type` varchar(1) DEFAULT NULL COMMENT '存款类型（"1","在线支付宝扫描","2","支付宝扫描","3","支付宝附言","4","微信扫描","5","微信额度验证","6","在线支付","7","工行附言","8","招行附言","9","点卡支付"）',
  `picture` varchar(255) DEFAULT NULL COMMENT '截图地址',
  `updatetime` datetime DEFAULT NULL,
  `operator` varchar(255) DEFAULT NULL COMMENT '处理人员',
  `cardtype` smallint(6) DEFAULT NULL COMMENT '点卡类型',
  `cardno` varchar(32) DEFAULT NULL COMMENT '点卡卡号',
  PRIMARY KEY (`id`),
  KEY `idx_loginname` (`loginname`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of urge_order
-- ----------------------------

-- ----------------------------
-- Table structure for `userbankinfo`
-- ----------------------------
DROP TABLE IF EXISTS `userbankinfo`;
CREATE TABLE `userbankinfo` (
  `id` int(8) NOT NULL AUTO_INCREMENT,
  `loginname` varchar(30) NOT NULL,
  `bankno` varchar(50) NOT NULL,
  `bankname` varchar(50) NOT NULL,
  `bankaddress` varchar(200) NOT NULL DEFAULT '',
  `addtime` datetime NOT NULL,
  `flag` int(1) NOT NULL DEFAULT '0' COMMENT '0：启用，1：禁用',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=139606 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of userbankinfo
-- ----------------------------
INSERT INTO `userbankinfo` VALUES ('139604', 'twtest01', '6221501111111111111', '邮政银行', 'none', '2018-12-23 08:02:54', '0');
INSERT INTO `userbankinfo` VALUES ('139605', 'windtest', '6214855492248888', '招商银行', 'none', '2018-12-24 11:08:01', '0');

-- ----------------------------
-- Table structure for `userbankinfo_backup`
-- ----------------------------
DROP TABLE IF EXISTS `userbankinfo_backup`;
CREATE TABLE `userbankinfo_backup` (
  `id` int(8) NOT NULL AUTO_INCREMENT,
  `loginname` varchar(30) NOT NULL,
  `bankno` varchar(50) NOT NULL,
  `bankname` varchar(50) NOT NULL,
  `bankaddress` varchar(200) NOT NULL DEFAULT '',
  `addtime` datetime NOT NULL,
  `flag` int(1) NOT NULL DEFAULT '0' COMMENT '0：启用，1：禁用',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of userbankinfo_backup
-- ----------------------------

-- ----------------------------
-- Table structure for `users`
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `loginname` varchar(20) NOT NULL,
  `password` varchar(32) NOT NULL DEFAULT '21218cca77804d2ba1922c33e0151105',
  `credit` double(24,2) NOT NULL DEFAULT '0.00',
  `lastLoginTime` datetime DEFAULT NULL,
  `level` int(2) DEFAULT '0',
  `md5Str` varchar(100) NOT NULL,
  `agent` varchar(30) DEFAULT NULL,
  `partner` varchar(30) DEFAULT NULL,
  `accountName` varchar(40) DEFAULT NULL,
  `phone` varchar(100) DEFAULT NULL,
  `flag` int(1) NOT NULL DEFAULT '0',
  `aliasName` varchar(100) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `address` varchar(50) DEFAULT NULL,
  `postcode` varchar(10) DEFAULT NULL,
  `createtime` datetime DEFAULT NULL,
  `role` varchar(20) NOT NULL DEFAULT '201',
  `howToKnow` varchar(100) DEFAULT NULL,
  `loginTimes` int(11) NOT NULL DEFAULT '0',
  `lastLoginIp` varchar(100) DEFAULT NULL,
  `lastarea` varchar(255) DEFAULT '',
  `agcode` varchar(40) DEFAULT '',
  `qq` varchar(40) DEFAULT NULL,
  `remark` varchar(600) DEFAULT NULL,
  `referWebsite` varchar(100) DEFAULT NULL COMMENT '来源网站',
  `pwdinfo` varchar(100) DEFAULT NULL,
  `registerIp` varchar(16) DEFAULT NULL,
  `area` varchar(255) DEFAULT '',
  `sms` int(1) DEFAULT '0',
  `isCashin` int(1) NOT NULL DEFAULT '1',
  `birthday` datetime DEFAULT NULL,
  `id` int(6) DEFAULT '0',
  `warnflag` int(2) DEFAULT '0' COMMENT '0：未知会员1：可疑会员2：危险会员',
  `warnremark` varchar(600) DEFAULT NULL,
  `rate` double(24,3) DEFAULT '0.006' COMMENT 'EA自定义反水率',
  `bbinrate` double(24,3) DEFAULT '0.006' COMMENT 'bbin自定义发水率',
  `agrate` double(24,3) DEFAULT '0.008' COMMENT 'ag自定义反水率',
  `aginrate` double(24,3) DEFAULT '0.008' COMMENT 'agin自定义反水率',
  `kenorate` double(24,3) DEFAULT '0.015' COMMENT 'keno自定义反水率',
  `sbrate` double(24,3) DEFAULT '0.004' COMMENT 'sb体育自定义反水',
  `skyrate` double(24,3) DEFAULT NULL,
  `intro` varchar(40) DEFAULT NULL COMMENT '推荐码',
  `mailaddress` varchar(50) DEFAULT NULL,
  `shippingcode` varchar(50) DEFAULT NULL,
  `giftamount` double(24,0) DEFAULT NULL,
  `creditlimit` double(24,0) DEFAULT '-1' COMMENT '-1转账没有限制 0 表示不能转账 1000表示一天最高转账1000',
  `creditday` double(24,0) DEFAULT '0',
  `creditdaydate` varchar(255) DEFAULT NULL,
  `randnum` varchar(4) DEFAULT NULL COMMENT '农行随机数',
  `ptflag` int(1) DEFAULT '0' COMMENT '0未登录1已登录',
  `shippingcodePt` varchar(50) DEFAULT NULL,
  `earebate` double(24,0) DEFAULT '28888' COMMENT 'EA反水上限',
  `bbinrebate` double(24,0) DEFAULT '28888',
  `agrebate` double(24,0) DEFAULT '28888',
  `kenorebate` double(24,0) DEFAULT '28888',
  `sbrebate` double(24,0) DEFAULT '28888',
  `ptrebate` double(24,0) DEFAULT '28888',
  `aginrebate` double(24,0) DEFAULT '28888',
  `clientos` varchar(50) DEFAULT NULL,
  `ptlogin` int(1) DEFAULT '0' COMMENT '0表示pt未注册 1表示已经注册',
  `invitecode` varchar(50) DEFAULT NULL,
  `microchannel` varchar(50) DEFAULT NULL,
  `passwdflag` int(11) DEFAULT NULL,
  `user_remark` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`loginname`),
  UNIQUE KEY `loginname` (`loginname`) USING BTREE,
  UNIQUE KEY `randnum` (`randnum`) USING BTREE,
  KEY `agent` (`agent`) USING BTREE,
  KEY `accountName` (`accountName`) USING BTREE,
  KEY `flag` (`flag`) USING BTREE,
  KEY `level` (`level`) USING BTREE,
  KEY `aliasName` (`aliasName`) USING BTREE,
  KEY `createtime` (`createtime`) USING BTREE,
  KEY `role` (`role`) USING BTREE,
  KEY `isCashin` (`isCashin`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='InnoDB free: 11264 kB';

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES ('austin998', '41f6536b91ba0e1874a9b427bc0f7b0f', '8378.12', '2020-10-09 04:21:43', '0', 'whjfgf5y', null, null, '李海', 'o3jPTCyBXJaYn+hiEC3HLQ==', '0', '', 'G/DybRoMgqUqqkRYQxYd6x+x2HlmLfFWDcSk9S9KGkI=', null, 'grepk9azpq', '2020-09-20 14:53:57', 'MONEY_CUSTOMER', null, '43', '0:0:0:0:0:0:0:1', '', '', '', '有同IP或者同姓名注册', 'http://localhost', 'VYoU2fXXgBHdJ3acL7sq5yoVbzLIWnn+', '0:0:0:0:0:0:0:1', '', '0', '1', '1992-09-20 12:00:00', '30009', '3', '', '0.006', '0.006', '0.008', '0.008', '0.015', '0.004', null, 'cs9', null, null, null, '-1', '31', '2020-10-08', 'LkYA', '0', null, '28888', '28888', '28888', '28888', '28888', '28888', '28888', '未知', '0', '', null, null, null);
INSERT INTO `users` VALUES ('kavin123', '41f6536b91ba0e1874a9b427bc0f7b0f', '0.00', null, '0', '9v336hlg', null, null, '李海论', 'UddJ8KqJis/XWac8CU8uxw==', '0', '', 'oW9dX3cigPbaKtO76cHL2Ovto086NmvusEWOkhilV64=', null, '45e524hv78', '2020-09-27 15:38:44', 'MONEY_CUSTOMER', null, '0', null, '', '', '', '有同IP或者同姓名注册', 'http://localhost', 'X33JZisqDg2xUntwezCGX0c3g2p1piQs', '0:0:0:0:0:0:0:1', '', '0', '1', '1998-09-27 12:00:00', '30010', '2', null, '0.006', '0.006', '0.008', '0.008', '0.015', '0.004', null, 'cs10', null, null, null, '-1', '0', null, null, '0', null, '28888', '28888', '28888', '28888', '28888', '28888', '28888', null, '0', '', null, null, null);
INSERT INTO `users` VALUES ('kavin998', '41f6536b91ba0e1874a9b427bc0f7b0f', '3464.10', '2020-10-09 05:04:58', '0', '60d7e05j', null, null, '李四', '2s9u1ZUKlhVCLlABbsMmWw==', '0', '', '2tFgHRgqXt3qL9ePSyOu5uvto086NmvusEWOkhilV64=', null, 'srzgub5joh', '2020-09-02 21:06:26', 'MONEY_CUSTOMER', null, '140', '0:0:0:0:0:0:0:1', '', '', '', '', 'http://localhost', '45Q/zGn4E1+qny9oMSy7CU8d1kaEFNvQ', '0:0:0:0:0:0:0:1', '', '0', '1', '1992-08-02 12:00:00', '30008', '0', null, '0.006', '0.006', '0.008', '0.008', '0.015', '0.004', null, 'cs8', null, null, null, '-1', '0', '2020-10-05', 'gJuG', '0', null, '28888', '28888', '28888', '28888', '28888', '28888', '28888', '未知', '0', '', '', null, null);
INSERT INTO `users` VALUES ('skytest', '9d797696abf9b713338e5fece6130952', '0.00', '2018-12-28 12:06:38', '2', 'fdtqjvo6', null, null, '姚博', 'iX5YyqAyJrKjfSBmVOCWUA==', '0', null, 'ko7fLQzyboYTWO5Ga9ZV49CpKtGHtS6EbxPLiE5ZNrc=', null, 'wvulu8ko79', '2018-12-19 16:19:56', 'MONEY_CUSTOMER', 'http://167.179.80.135', '9', '45.32.9.147', '美国', '', null, '有同IP或者同姓名注册', 'http://167.179.80.135', 'XYJBu2s7Pvf5J6fx2uguLrknJfhbMrZ3', '45.32.9.147', '日本 东京Choopa数据中心', '0', '1', '2018-12-11 12:00:00', '30006', '2', null, '0.006', '0.006', '0.008', '0.008', '0.015', '0.004', null, 'cs6', null, null, null, '-1', '0', null, 'fk2V', '0', null, '28888', '28888', '28888', '28888', '28888', '28888', '28888', 'Windows 7', '0', null, null, null, null);
INSERT INTO `users` VALUES ('test01', 'dc483e80a7a0bd9ef71d8cf973673924', '0.00', '2018-11-28 18:05:16', '0', 'vbu7v0pk', null, null, null, 'rtAMIhPQVcKxu2FwLGGeLA==', '0', null, null, null, '1ihyoqh284', '2018-11-27 17:17:01', 'MONEY_CUSTOMER', 'http://167.179.80.135', '2', '45.32.9.147', '美国', '', null, '', 'http://167.179.80.135', 'CK288uslOhlUju13Fm2PqQ==', '45.32.9.147', '美国', '0', '1', null, '30002', '0', null, '0.006', '0.006', '0.008', '0.008', '0.015', '0.004', null, 'cs2', null, null, null, '-1', '0', null, 'zXI0', '0', null, '28888', '28888', '28888', '28888', '28888', '28888', '28888', 'Windows 7', '0', null, null, null, null);
INSERT INTO `users` VALUES ('twnick', '505e6e7f902970dd843312173b1e9e5b', '0.00', '2018-12-25 03:05:15', '0', 'occ7no7k', null, null, '张三', '2t6ZP2UUaReI/fWqkHVWVw==', '0', null, 'vnAio41CQmMil0fbBO+nzw==', null, 's30b5lxzsj', '2018-12-21 01:31:22', 'MONEY_CUSTOMER', 'http://tianweiyule.com', '1', '45.32.9.147', '美国', '', '55123132', '有同IP或者同姓名注册', 'http://tianweiyule.com', 'phV2qQqfB45xt0/K49CrajTwFCdBPH3y', '45.32.9.147', '日本 东京Choopa数据中心', '0', '1', '2017-12-25 12:00:00', '30007', '2', null, '0.006', '0.006', '0.008', '0.008', '0.015', '0.004', null, 'cs7', null, null, null, '-1', '0', null, 'nK3q', '0', null, '28888', '28888', '28888', '28888', '28888', '28888', '28888', '未知', '0', null, '51532', null, null);
INSERT INTO `users` VALUES ('twtest01', 'cf802424ab1302f188bd1ffa77cf6c54', '500.00', '2018-12-26 23:29:45', '6', 'uyf4fb9l', null, null, '测试一', 'o6IqH86vWekTM1MSxPFGbQ==', '0', null, 'TnLEKG0MXuyQKjOZczGa1OMgXGZdhigOI4FhMqKoQjU=', null, 'teka40oipc', '2018-11-22 23:04:48', 'MONEY_CUSTOMER', 'http://167.179.80.135', '32', '112.208.155.161', '菲律宾', '', null, '', 'http://167.179.80.135', 'qRUutqZ+SFyUdGlNI8M8TODMvJ4qlnm4', '112.208.146.236', '欧洲', '0', '1', '2018-12-23 12:00:00', '30001', '0', null, '0.006', '0.006', '0.008', '0.008', '0.015', '0.004', null, 'cs1', null, null, null, '-1', '0', null, 'D4RY', '0', null, '28888', '28888', '28888', '28888', '28888', '28888', '28888', '未知', '0', null, null, null, null);
INSERT INTO `users` VALUES ('twtest02', '9d797696abf9b713338e5fece6130952', '0.00', '2018-12-24 14:01:11', '0', 'tyavbs4f', null, null, null, 'f7LLXyjYzMqvPUnM07lg4g==', '0', null, null, null, 'g9ouuebsvj', '2018-12-13 00:50:15', 'MONEY_CUSTOMER', 'http://tianweiyule.com', '6', '130.105.170.172', '美国', '', null, '有同IP或者同姓名注册', 'http://tianweiyule.com', 'AvgqyjD+AvMW7hVaFCvzfi88KoYaBvRQ', '45.32.9.147', '美国', '0', '1', null, '30004', '2', null, '0.006', '0.006', '0.008', '0.008', '0.015', '0.004', null, 'cs4', null, null, null, '-1', '0', null, 'mRSc', '0', null, '28888', '28888', '28888', '28888', '28888', '28888', '28888', 'Linux', '0', null, null, null, null);
INSERT INTO `users` VALUES ('wadetest', '9d797696abf9b713338e5fece6130952', '0.00', '2018-12-26 14:39:43', '4', 't55xq8rf', null, null, null, 'jsQlOhtVuO+3ttB3F68uJg==', '0', null, null, null, 'lq36fu29qz', '2018-12-19 16:18:05', 'MONEY_CUSTOMER', 'http://www.tianweiyule.com', '4', '45.32.9.147', '美国', '', null, '有同IP或者同姓名注册', 'http://www.tianweiyule.com', 'KxbiQOf/IcaomyyCMvpA2kqEbz1JKPpk', '45.32.9.147', '日本 东京Choopa数据中心', '0', '1', null, '30005', '2', null, '0.006', '0.006', '0.008', '0.008', '0.015', '0.004', null, 'cs5', null, null, null, '-1', '0', null, 'JcpC', '0', null, '28888', '28888', '28888', '28888', '28888', '28888', '28888', '未知', '0', null, null, null, null);
INSERT INTO `users` VALUES ('windtest', '2569d419bfea999ff13fd1f7f4498b89', '0.00', '2018-12-29 13:28:49', '4', 'y5lhljoq', null, null, '测试', 's4bu/riE19ITUa/8N+/aKg==', '0', null, 'GvgJ0R3hySBtMrqGPVxj9AvGzqNrwFq3VRpXVtyytn0=', null, 'nmvtnt42sq', '2018-12-01 11:33:51', 'MONEY_CUSTOMER', 'http://tianweiyule.com', '57', '45.32.9.147', '美国', '', '123111122222', '', 'http://tianweiyule.com', 'o6K+EoG8WwDbNzrvi2UBWQ0nemAgEllV', '130.105.170.172', '美国', '0', '1', '2018-12-01 12:00:00', '30003', '0', null, '0.006', '0.006', '0.008', '0.008', '0.015', '0.004', null, 'cs3', null, null, null, '-1', '0', null, '1zbI', '0', null, '28888', '28888', '28888', '28888', '28888', '28888', '28888', 'Macintosh', '0', null, null, null, null);

-- ----------------------------
-- Table structure for `userstatus`
-- ----------------------------
DROP TABLE IF EXISTS `userstatus`;
CREATE TABLE `userstatus` (
  `loginname` varchar(30) NOT NULL DEFAULT '',
  `loginerrornum` int(1) DEFAULT '0' COMMENT '连续登录错误次数',
  `mailflag` int(1) DEFAULT '1' COMMENT '存取款是否发送邮件的标识',
  `cashinwrong` int(11) NOT NULL DEFAULT '0',
  `touzhuflag` int(1) DEFAULT '0',
  `times` int(4) DEFAULT '0',
  `starttime` datetime DEFAULT NULL,
  `firstCash` double(24,2) DEFAULT '0.00',
  `amount` double(24,2) DEFAULT '0.00',
  `remark` varchar(500) DEFAULT NULL,
  `slotaccount` double(24,2) DEFAULT '0.00' COMMENT '老虎机佣金总额',
  `validateCode` varchar(50) DEFAULT NULL,
  `payorderValue` varchar(100) DEFAULT NULL,
  `discussflag` int(1) DEFAULT NULL,
  `commission` varchar(10) DEFAULT NULL COMMENT '老虎机佣金比率',
  `smsflag` varchar(2) DEFAULT NULL,
  `liverate` double(10,2) DEFAULT NULL COMMENT '真人佣金比率',
  `sportsrate` double(10,2) DEFAULT NULL COMMENT '体育佣金比率',
  `lotteryrate` double(10,2) DEFAULT NULL COMMENT '彩票佣金比率',
  PRIMARY KEY (`loginname`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of userstatus
-- ----------------------------
INSERT INTO `userstatus` VALUES ('austin998', '0', null, '0', '0', '0', null, '0.00', '0.00', null, '0.00', null, '0', null, null, null, null, null, null);
INSERT INTO `userstatus` VALUES ('kavin123', null, null, '0', '0', '0', null, '0.00', '0.00', null, '0.00', null, '0', null, null, null, null, null, null);
INSERT INTO `userstatus` VALUES ('kavin998', '0', null, '0', '0', '0', null, '0.00', '0.00', null, '0.00', null, '0', null, null, null, null, null, null);
INSERT INTO `userstatus` VALUES ('skytest', '0', '0', '0', '0', '0', null, '0.00', '0.00', null, '0.00', null, '0', null, null, null, null, null, null);
INSERT INTO `userstatus` VALUES ('test01', '0', '0', '0', '0', '0', null, '0.00', '0.00', null, '0.00', null, '0', null, null, null, null, null, null);
INSERT INTO `userstatus` VALUES ('twnick', '0', '0', '0', '0', '0', null, '0.00', '0.00', null, '0.00', null, '0', null, null, null, null, null, null);
INSERT INTO `userstatus` VALUES ('twtest01', '0', '0', '0', '0', '0', null, '0.00', '0.00', null, '0.00', null, '0', null, null, null, null, null, null);
INSERT INTO `userstatus` VALUES ('twtest02', '0', '0', '0', '0', '0', null, '0.00', '0.00', null, '0.00', null, '0', null, null, null, null, null, null);
INSERT INTO `userstatus` VALUES ('wadetest', '0', '0', '0', '0', '0', null, '0.00', '0.00', null, '0.00', null, '0', null, null, null, null, null, null);
INSERT INTO `userstatus` VALUES ('windtest', '0', '0', '0', '0', '0', null, '0.00', '0.00', null, '0.00', null, '0', null, null, null, null, null, null);

-- ----------------------------
-- Table structure for `userstatus_backup`
-- ----------------------------
DROP TABLE IF EXISTS `userstatus_backup`;
CREATE TABLE `userstatus_backup` (
  `loginname` varchar(30) NOT NULL DEFAULT '',
  `loginerrornum` int(1) DEFAULT '0' COMMENT '连续登录错误次数',
  `mailflag` int(1) DEFAULT '1' COMMENT '存取款是否发送邮件的标识',
  `cashinwrong` int(11) NOT NULL DEFAULT '0',
  `touzhuflag` int(1) DEFAULT '0',
  `times` int(4) DEFAULT '0',
  `starttime` datetime DEFAULT NULL,
  `firstCash` double(24,2) DEFAULT '0.00',
  `amount` double(24,2) DEFAULT '0.00',
  PRIMARY KEY (`loginname`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of userstatus_backup
-- ----------------------------

-- ----------------------------
-- Table structure for `users_agent_game`
-- ----------------------------
DROP TABLE IF EXISTS `users_agent_game`;
CREATE TABLE `users_agent_game` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `loginnameAgent` varchar(20) NOT NULL,
  `loginnameGame` varchar(20) NOT NULL,
  `deleteFlag` int(11) DEFAULT '0',
  `createTime` datetime DEFAULT NULL,
  `remark` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of users_agent_game
-- ----------------------------

-- ----------------------------
-- Table structure for `users_backup`
-- ----------------------------
DROP TABLE IF EXISTS `users_backup`;
CREATE TABLE `users_backup` (
  `loginname` varchar(20) NOT NULL,
  `password` varchar(32) NOT NULL DEFAULT '21218cca77804d2ba1922c33e0151105',
  `credit` double(24,2) NOT NULL DEFAULT '0.00',
  `lastLoginTime` datetime DEFAULT NULL,
  `level` int(2) DEFAULT '0',
  `md5Str` varchar(100) NOT NULL,
  `agent` varchar(30) DEFAULT NULL,
  `partner` varchar(30) DEFAULT NULL,
  `accountName` varchar(40) DEFAULT NULL,
  `phone` varchar(100) DEFAULT NULL,
  `flag` int(1) NOT NULL DEFAULT '0',
  `aliasName` varchar(100) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `address` varchar(50) DEFAULT NULL,
  `postcode` varchar(10) DEFAULT NULL,
  `createtime` datetime DEFAULT NULL,
  `role` varchar(20) NOT NULL DEFAULT '201',
  `howToKnow` varchar(100) DEFAULT NULL,
  `loginTimes` int(11) NOT NULL DEFAULT '0',
  `lastLoginIp` varchar(100) DEFAULT NULL,
  `lastarea` varchar(255) DEFAULT '',
  `agcode` varchar(40) DEFAULT '',
  `qq` varchar(40) DEFAULT NULL,
  `remark` varchar(600) DEFAULT NULL,
  `referWebsite` varchar(100) DEFAULT NULL COMMENT '来源网站',
  `pwdinfo` varchar(100) DEFAULT NULL,
  `registerIp` varchar(16) DEFAULT NULL,
  `area` varchar(255) DEFAULT '',
  `sms` int(1) DEFAULT '0',
  `isCashin` int(1) NOT NULL DEFAULT '1',
  `birthday` datetime DEFAULT NULL,
  `id` int(6) DEFAULT '0',
  `warnflag` int(2) DEFAULT '0' COMMENT '0：未知会员1：可疑会员2：危险会员',
  `warnremark` varchar(600) DEFAULT NULL,
  `rate` double(24,3) DEFAULT '0.006' COMMENT 'EA自定义反水率',
  `bbinrate` double(24,3) DEFAULT '0.006' COMMENT 'bbin自定义发水率',
  `agrate` double(24,3) DEFAULT '0.008' COMMENT 'ag自定义反水率',
  `aginrate` double(24,3) DEFAULT '0.008' COMMENT 'agin自定义反水率',
  `kenorate` double(24,3) DEFAULT '0.015' COMMENT 'keno自定义反水率',
  `sbrate` double(24,3) DEFAULT '0.004' COMMENT 'sb体育自定义反水',
  `skyrate` double(24,3) DEFAULT NULL,
  `intro` varchar(40) DEFAULT NULL COMMENT '推荐码',
  `mailaddress` varchar(50) DEFAULT NULL,
  `shippingcode` varchar(50) DEFAULT NULL,
  `giftamount` double(24,0) DEFAULT NULL,
  `creditlimit` double(24,0) DEFAULT '-1' COMMENT '-1转账没有限制 0 表示不能转账 1000表示一天最高转账1000',
  `creditday` double(24,0) DEFAULT '0',
  `creditdaydate` varchar(255) DEFAULT NULL,
  `randnum` varchar(4) DEFAULT NULL COMMENT '农行随机数',
  `ptflag` int(1) DEFAULT '0' COMMENT '0未登录1已登录',
  `shippingcodePt` varchar(50) DEFAULT NULL,
  `earebate` double(24,0) DEFAULT '28888' COMMENT 'EA反水上限',
  `bbinrebate` double(24,0) DEFAULT '28888',
  `agrebate` double(24,0) DEFAULT '28888',
  `kenorebate` double(24,0) DEFAULT '28888',
  `sbrebate` double(24,0) DEFAULT '28888',
  `ptrebate` double(24,0) DEFAULT '28888',
  `aginrebate` double(24,0) DEFAULT '28888',
  `clientos` varchar(50) DEFAULT NULL,
  `ptlogin` int(1) DEFAULT '0' COMMENT '0表示pt未注册 1表示已经注册',
  `invitecode` varchar(50) DEFAULT NULL,
  `microchannel` varchar(50) DEFAULT NULL,
  `passwdflag` int(11) DEFAULT NULL,
  PRIMARY KEY (`loginname`),
  UNIQUE KEY `loginname` (`loginname`) USING BTREE,
  UNIQUE KEY `randnum` (`randnum`) USING BTREE,
  KEY `agent` (`agent`) USING BTREE,
  KEY `accountName` (`accountName`) USING BTREE,
  KEY `flag` (`flag`) USING BTREE,
  KEY `level` (`level`) USING BTREE,
  KEY `aliasName` (`aliasName`) USING BTREE,
  KEY `createtime` (`createtime`) USING BTREE,
  KEY `role` (`role`) USING BTREE,
  KEY `isCashin` (`isCashin`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='InnoDB free: 11264 kB';

-- ----------------------------
-- Records of users_backup
-- ----------------------------

-- ----------------------------
-- Table structure for `users_copy`
-- ----------------------------
DROP TABLE IF EXISTS `users_copy`;
CREATE TABLE `users_copy` (
  `loginname` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  PRIMARY KEY (`loginname`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of users_copy
-- ----------------------------

-- ----------------------------
-- Table structure for `users_qq`
-- ----------------------------
DROP TABLE IF EXISTS `users_qq`;
CREATE TABLE `users_qq` (
  `loginname` varchar(20) NOT NULL,
  `password` varchar(32) NOT NULL DEFAULT '21218cca77804d2ba1922c33e0151105',
  `credit` double(24,2) NOT NULL DEFAULT '0.00',
  `lastLoginTime` datetime DEFAULT NULL,
  `level` int(2) DEFAULT '0',
  `md5Str` varchar(100) NOT NULL,
  `agent` varchar(30) DEFAULT NULL,
  `partner` varchar(30) DEFAULT NULL,
  `accountName` varchar(40) DEFAULT NULL,
  `phone` varchar(100) DEFAULT NULL,
  `flag` int(1) NOT NULL DEFAULT '0',
  `aliasName` varchar(100) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `address` varchar(50) DEFAULT NULL,
  `postcode` varchar(10) DEFAULT NULL,
  `createtime` datetime DEFAULT NULL,
  `role` varchar(20) NOT NULL DEFAULT '201',
  `howToKnow` varchar(100) DEFAULT NULL,
  `loginTimes` int(11) NOT NULL DEFAULT '0',
  `lastLoginIp` varchar(100) DEFAULT NULL,
  `lastarea` varchar(255) DEFAULT '',
  `agcode` varchar(40) DEFAULT '',
  `qq` varchar(40) DEFAULT NULL,
  `remark` varchar(600) DEFAULT NULL,
  `referWebsite` varchar(100) DEFAULT NULL COMMENT '来源网站',
  `pwdinfo` varchar(100) DEFAULT NULL,
  `registerIp` varchar(16) DEFAULT NULL,
  `area` varchar(255) DEFAULT '',
  `sms` int(1) DEFAULT '0',
  `isCashin` int(1) NOT NULL DEFAULT '1',
  `birthday` datetime DEFAULT NULL,
  `id` int(6) DEFAULT '0',
  `warnflag` int(2) DEFAULT '0' COMMENT '0：未知会员1：可疑会员2：危险会员',
  `warnremark` varchar(600) DEFAULT NULL,
  `rate` double(24,3) DEFAULT '0.006' COMMENT 'EA自定义反水率',
  `bbinrate` double(24,3) DEFAULT '0.006' COMMENT 'bbin自定义发水率',
  `agrate` double(24,3) DEFAULT '0.008' COMMENT 'ag自定义反水率',
  `aginrate` double(24,3) DEFAULT '0.008' COMMENT 'agin自定义反水率',
  `kenorate` double(24,3) DEFAULT '0.015' COMMENT 'keno自定义反水率',
  `sbrate` double(24,3) DEFAULT '0.004' COMMENT 'sb体育自定义反水',
  `skyrate` double(24,3) DEFAULT NULL,
  `intro` varchar(40) DEFAULT NULL COMMENT '推荐码',
  `mailaddress` varchar(50) DEFAULT NULL,
  `shippingcode` varchar(50) DEFAULT NULL,
  `giftamount` double(24,0) DEFAULT NULL,
  `creditlimit` double(24,0) DEFAULT '-1' COMMENT '-1转账没有限制 0 表示不能转账 1000表示一天最高转账1000',
  `creditday` double(24,0) DEFAULT '0',
  `creditdaydate` varchar(255) DEFAULT NULL,
  `randnum` varchar(4) DEFAULT NULL COMMENT '农行随机数',
  `ptflag` int(1) DEFAULT '0' COMMENT '0未登录1已登录',
  `shippingcodePt` varchar(50) DEFAULT NULL,
  `earebate` double(24,0) DEFAULT '28888' COMMENT 'EA反水上限',
  `bbinrebate` double(24,0) DEFAULT '28888',
  `agrebate` double(24,0) DEFAULT '28888',
  `kenorebate` double(24,0) DEFAULT '28888',
  `sbrebate` double(24,0) DEFAULT '28888',
  `ptrebate` double(24,0) DEFAULT '28888',
  `aginrebate` double(24,0) DEFAULT '28888',
  `clientos` varchar(50) DEFAULT NULL,
  `ptlogin` int(1) DEFAULT '0' COMMENT '0表示pt未注册 1表示已经注册',
  `invitecode` varchar(50) DEFAULT NULL,
  `microchannel` varchar(50) DEFAULT NULL,
  `passwdflag` int(11) DEFAULT NULL,
  PRIMARY KEY (`loginname`),
  UNIQUE KEY `loginname` (`loginname`) USING BTREE,
  UNIQUE KEY `randnum` (`randnum`) USING BTREE,
  KEY `agent` (`agent`) USING BTREE,
  KEY `accountName` (`accountName`) USING BTREE,
  KEY `flag` (`flag`) USING BTREE,
  KEY `level` (`level`) USING BTREE,
  KEY `aliasName` (`aliasName`) USING BTREE,
  KEY `createtime` (`createtime`) USING BTREE,
  KEY `role` (`role`) USING BTREE,
  KEY `isCashin` (`isCashin`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='InnoDB free: 11264 kB';

-- ----------------------------
-- Records of users_qq
-- ----------------------------

-- ----------------------------
-- Table structure for `userupgradelog`
-- ----------------------------
DROP TABLE IF EXISTS `userupgradelog`;
CREATE TABLE `userupgradelog` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户名',
  `username` varchar(20) NOT NULL,
  `bet` double(14,2) DEFAULT '0.00' COMMENT '上月投注额',
  `ptbet` double(14,2) DEFAULT '0.00' COMMENT '上月PT投注额',
  `bet_week` double(14,2) DEFAULT NULL COMMENT '周投注额',
  `ptbet_week` double(14,2) DEFAULT NULL COMMENT '周PT投注额',
  `createTime` datetime NOT NULL COMMENT '创建时间',
  `oldlevel` tinyint(255) NOT NULL COMMENT '原级别',
  `newlevel` tinyint(255) NOT NULL COMMENT '新级别',
  `status` char(1) NOT NULL COMMENT '状态：0 已取消  1：已处理 2：待确认',
  `optmonth` varchar(10) NOT NULL DEFAULT '' COMMENT '处理的月份',
  `remark` varchar(255) DEFAULT '' COMMENT '备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uc_upgrademonth` (`username`,`optmonth`) USING BTREE
) ENGINE=MyISAM AUTO_INCREMENT=105623 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of userupgradelog
-- ----------------------------

-- ----------------------------
-- Table structure for `user_address`
-- ----------------------------
DROP TABLE IF EXISTS `user_address`;
CREATE TABLE `user_address` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `login_name` varchar(50) DEFAULT NULL,
  `area` varchar(30) DEFAULT NULL,
  `address` varchar(200) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `name` varchar(20) DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `flag` int(11) DEFAULT '0' COMMENT '0不是默认,1默认地址',
  `remark` varchar(100) DEFAULT NULL,
  `province` varchar(20) DEFAULT NULL COMMENT '省份',
  `city` varchar(20) DEFAULT NULL COMMENT '市',
  `district` varchar(20) DEFAULT NULL COMMENT '区',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of user_address
-- ----------------------------

-- ----------------------------
-- Table structure for `user_alipay_account`
-- ----------------------------
DROP TABLE IF EXISTS `user_alipay_account`;
CREATE TABLE `user_alipay_account` (
  `id` int(32) NOT NULL AUTO_INCREMENT,
  `alipay_account` varchar(255) NOT NULL,
  `loginname` varchar(100) DEFAULT NULL,
  `realname` varchar(50) DEFAULT NULL,
  `disable` int(10) NOT NULL DEFAULT '0' COMMENT '0.可用1.不可用',
  `createtime` datetime DEFAULT NULL,
  `updatetime` datetime DEFAULT NULL,
  `remark` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `alipay_account` (`alipay_account`) USING BTREE,
  UNIQUE KEY `loginname` (`loginname`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user_alipay_account
-- ----------------------------

-- ----------------------------
-- Table structure for `user_lottery_record`
-- ----------------------------
DROP TABLE IF EXISTS `user_lottery_record`;
CREATE TABLE `user_lottery_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `loginname` varchar(50) NOT NULL,
  `item_name` varchar(50) NOT NULL,
  `is_receive` int(11) NOT NULL,
  `winning_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `receive_date` timestamp NULL DEFAULT NULL,
  `period` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=146 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user_lottery_record
-- ----------------------------

-- ----------------------------
-- Table structure for `user_maintain_log`
-- ----------------------------
DROP TABLE IF EXISTS `user_maintain_log`;
CREATE TABLE `user_maintain_log` (
  `id` int(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `loginname` varchar(50) NOT NULL COMMENT '玩家账号',
  `content` varchar(1000) NOT NULL COMMENT '内容',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_user` varchar(50) NOT NULL COMMENT '创建人',
  `update_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '修改时间',
  `update_user` varchar(50) NOT NULL COMMENT '修改人',
  `type` tinyint(4) DEFAULT '1' COMMENT '类型：0-系统，1-人工',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of user_maintain_log
-- ----------------------------

-- ----------------------------
-- Table structure for `xima`
-- ----------------------------
DROP TABLE IF EXISTS `xima`;
CREATE TABLE `xima` (
  `pno` varchar(20) NOT NULL,
  `title` varchar(20) NOT NULL,
  `loginname` varchar(20) NOT NULL,
  `payType` varchar(20) DEFAULT NULL,
  `firstCash` double(24,2) NOT NULL,
  `tryCredit` double(24,2) NOT NULL,
  `startTime` datetime DEFAULT NULL,
  `endTime` datetime DEFAULT NULL,
  `rate` double(24,4) NOT NULL,
  `remark` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`pno`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of xima
-- ----------------------------
INSERT INTO `xima` VALUES ('629200917rrkqhuep4o', 'MONEY_CUSTOMER', 'kavin998', '网银支付', '820.00', '4.10', '2020-09-17 00:00:00', '2020-09-17 20:39:12', '0.0050', 'mg自助洗码;秒反水;executed;自动审核');
INSERT INTO `xima` VALUES ('633200921lufizym2gq', 'MONEY_CUSTOMER', 'austin998', '网银支付', '1223.20', '6.12', '2020-09-21 12:00:00', '2020-09-21 21:06:53', '0.0050', 'cq9自助洗码;秒反水;executed;自动审核');
