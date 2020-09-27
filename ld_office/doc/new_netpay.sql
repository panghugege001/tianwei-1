/*
MySQL Data Transfer
Source Host: localhost
Source Database: elf
Target Host: localhost
Target Database: elf
Date: 2010-8-2 AM 09:44:48
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for new_netpay
-- ----------------------------
CREATE TABLE `new_netpay` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `inner_serialNo` varchar(30) NOT NULL COMMENT '部内流水号(商铺单号)',
  `payPlatform_serialNo` varchar(30) NOT NULL COMMENT '第三方支付平台流水号',
  `payPlatform_Name` varchar(30) NOT NULL COMMENT '第三方支付平台名称',
  `bankOrderNo` varchar(30) NOT NULL COMMENT '银行单号',
  `bankName` varchar(30) NOT NULL COMMENT '银行名称',
  `userID` varchar(20) NOT NULL,
  `amount` decimal(24,2) NOT NULL,
  `payDate` datetime NOT NULL COMMENT '付支成功时间',
  `informDate` datetime NOT NULL COMMENT '第三方平台通知本平台的时间',
  `thirdpartyID` varchar(30) DEFAULT NULL COMMENT '第三方平台会员ID(备用)',
  `payType` int(1) NOT NULL COMMENT '支付类型（支付、退款等）',
  `payResult` int(1) NOT NULL COMMENT '支付结果',
  PRIMARY KEY (`id`),
  UNIQUE KEY `POrder_ID` (`id`),
  UNIQUE KEY `POrder_inner_seriaNo` (`inner_serialNo`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records 
-- ----------------------------
INSERT INTO `new_netpay` VALUES ('1', '20100801160101003', '09876543211234567890', 'yeepay', '123123', 'ICBC-NET', 'admin', '100.00', '2009-10-23 15:30:58', '2009-10-23 15:31:06', '', '1', '1');
