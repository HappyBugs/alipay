/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50717
Source Host           : localhost:3306
Source Database       : alipay_order

Target Server Type    : MYSQL
Target Server Version : 50717
File Encoding         : 65001

Date: 2019-05-05 11:55:32
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `order`
-- ----------------------------
DROP TABLE IF EXISTS `order`;
CREATE TABLE `order` (
  `id` varchar(125) NOT NULL,
  `state` int(1) NOT NULL DEFAULT '0',
  `createTime` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of order
-- ----------------------------
INSERT INTO `order` VALUES ('1557021936665', '1', '2019-05-05 10:05:36');
INSERT INTO `order` VALUES ('1557022442611', '1', '2019-05-05 10:14:02');
INSERT INTO `order` VALUES ('1557023645655', '1', '2019-05-05 10:34:05');
INSERT INTO `order` VALUES ('1557023899961', '1', '2019-05-05 10:38:19');
