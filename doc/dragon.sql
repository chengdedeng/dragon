SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `annotation`
-- ----------------------------
DROP TABLE IF EXISTS `annotation`;
CREATE TABLE `annotation` (
  `type` varchar(20) NOT NULL,
  `ip` varchar(30) NOT NULL,
  `port` int(4) DEFAULT NULL,
  `time` bigint(8) DEFAULT NULL,
  `spanId` varchar(32) NOT NULL,
  `value` varchar(2500) DEFAULT NULL,
  `app` varchar(50) NOT NULL,
  KEY `spanIdIndex` (`spanId`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `service`
-- ----------------------------
DROP TABLE IF EXISTS `service`;
CREATE TABLE `service` (
  `serviceId` int(4) NOT NULL AUTO_INCREMENT,
  `serviceName` varchar(100) NOT NULL,
  PRIMARY KEY (`serviceId`),
  UNIQUE KEY `index_service` (`serviceName`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `span`
-- ----------------------------
DROP TABLE IF EXISTS `span`;
CREATE TABLE `span` (
  `spanId` varchar(32) NOT NULL,
  `traceId` varchar(32) NOT NULL,
  `parentId` varchar(32) DEFAULT NULL,
  `serviceId` bigint(8) NOT NULL,
  PRIMARY KEY (`spanId`),
  KEY `traceIdIndex` (`traceId`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `trace`
-- ----------------------------
DROP TABLE IF EXISTS `trace`;
CREATE TABLE `trace` (
  `traceId` varchar(32) NOT NULL,
  `serviceId` int(4) NOT NULL,
  `time` bigint(8) NOT NULL,
  PRIMARY KEY (`traceId`,`serviceId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

SET FOREIGN_KEY_CHECKS = 1;
