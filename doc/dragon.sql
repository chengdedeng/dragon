SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `annotation`
-- ----------------------------
DROP TABLE IF EXISTS `annotation0`;
CREATE TABLE `annotation0` (
  `type` varchar(20) NOT NULL,
  `ip` varchar(30) NOT NULL,
  `port` int(4) DEFAULT NULL,
  `time` bigint(8) DEFAULT NULL,
  `spanId` varchar(32) NOT NULL,
  `value` varchar(2500) DEFAULT NULL,
  `app` varchar(50) NOT NULL,
  `traceId` varchar(32) NOT NULL,
  KEY `spanIdIndex` (`spanId`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `annotation1`;
CREATE TABLE `annotation1` (
  `type` varchar(20) NOT NULL,
  `ip` varchar(30) NOT NULL,
  `port` int(4) DEFAULT NULL,
  `time` bigint(8) DEFAULT NULL,
  `spanId` varchar(32) NOT NULL,
  `value` varchar(2500) DEFAULT NULL,
  `app` varchar(50) NOT NULL,
  `traceId` varchar(32) NOT NULL,
  KEY `spanIdIndex` (`spanId`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `annotation2`;
CREATE TABLE `annotation2` (
  `type` varchar(20) NOT NULL,
  `ip` varchar(30) NOT NULL,
  `port` int(4) DEFAULT NULL,
  `time` bigint(8) DEFAULT NULL,
  `spanId` varchar(32) NOT NULL,
  `value` varchar(2500) DEFAULT NULL,
  `app` varchar(50) NOT NULL,
  `traceId` varchar(32) NOT NULL,
  KEY `spanIdIndex` (`spanId`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `annotation3`;
CREATE TABLE `annotation3` (
  `type` varchar(20) NOT NULL,
  `ip` varchar(30) NOT NULL,
  `port` int(4) DEFAULT NULL,
  `time` bigint(8) DEFAULT NULL,
  `spanId` varchar(32) NOT NULL,
  `value` varchar(2500) DEFAULT NULL,
  `app` varchar(50) NOT NULL,
  `traceId` varchar(32) NOT NULL,
  KEY `spanIdIndex` (`spanId`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `annotation4`;
CREATE TABLE `annotation4` (
  `type` varchar(20) NOT NULL,
  `ip` varchar(30) NOT NULL,
  `port` int(4) DEFAULT NULL,
  `time` bigint(8) DEFAULT NULL,
  `spanId` varchar(32) NOT NULL,
  `value` varchar(2500) DEFAULT NULL,
  `app` varchar(50) NOT NULL,
  `traceId` varchar(32) NOT NULL,
  KEY `spanIdIndex` (`spanId`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `annotation5`;
CREATE TABLE `annotation5` (
  `type` varchar(20) NOT NULL,
  `ip` varchar(30) NOT NULL,
  `port` int(4) DEFAULT NULL,
  `time` bigint(8) DEFAULT NULL,
  `spanId` varchar(32) NOT NULL,
  `value` varchar(2500) DEFAULT NULL,
  `app` varchar(50) NOT NULL,
  `traceId` varchar(32) NOT NULL,
  KEY `spanIdIndex` (`spanId`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `annotation6`;
CREATE TABLE `annotation6` (
  `type` varchar(20) NOT NULL,
  `ip` varchar(30) NOT NULL,
  `port` int(4) DEFAULT NULL,
  `time` bigint(8) DEFAULT NULL,
  `spanId` varchar(32) NOT NULL,
  `value` varchar(2500) DEFAULT NULL,
  `app` varchar(50) NOT NULL,
  `traceId` varchar(32) NOT NULL,
  KEY `spanIdIndex` (`spanId`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `annotation7`;
CREATE TABLE `annotation7` (
  `type` varchar(20) NOT NULL,
  `ip` varchar(30) NOT NULL,
  `port` int(4) DEFAULT NULL,
  `time` bigint(8) DEFAULT NULL,
  `spanId` varchar(32) NOT NULL,
  `value` varchar(2500) DEFAULT NULL,
  `app` varchar(50) NOT NULL,
  `traceId` varchar(32) NOT NULL,
  KEY `spanIdIndex` (`spanId`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `annotation8`;
CREATE TABLE `annotation8` (
  `type` varchar(20) NOT NULL,
  `ip` varchar(30) NOT NULL,
  `port` int(4) DEFAULT NULL,
  `time` bigint(8) DEFAULT NULL,
  `spanId` varchar(32) NOT NULL,
  `value` varchar(2500) DEFAULT NULL,
  `app` varchar(50) NOT NULL,
  `traceId` varchar(32) NOT NULL,
  KEY `spanIdIndex` (`spanId`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `annotation9`;
CREATE TABLE `annotation9` (
  `type` varchar(20) NOT NULL,
  `ip` varchar(30) NOT NULL,
  `port` int(4) DEFAULT NULL,
  `time` bigint(8) DEFAULT NULL,
  `spanId` varchar(32) NOT NULL,
  `value` varchar(2500) DEFAULT NULL,
  `app` varchar(50) NOT NULL,
  `traceId` varchar(32) NOT NULL,
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
DROP TABLE IF EXISTS `span0`;
CREATE TABLE `span0` (
  `spanId` varchar(32) NOT NULL,
  `traceId` varchar(32) NOT NULL,
  `parentId` varchar(32) DEFAULT NULL,
  `serviceId` bigint(8) NOT NULL,
  PRIMARY KEY (`spanId`),
  KEY `traceIdIndex` (`traceId`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `span1`;
CREATE TABLE `span1` (
  `spanId` varchar(32) NOT NULL,
  `traceId` varchar(32) NOT NULL,
  `parentId` varchar(32) DEFAULT NULL,
  `serviceId` bigint(8) NOT NULL,
  PRIMARY KEY (`spanId`),
  KEY `traceIdIndex` (`traceId`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `span2`;
CREATE TABLE `span2` (
  `spanId` varchar(32) NOT NULL,
  `traceId` varchar(32) NOT NULL,
  `parentId` varchar(32) DEFAULT NULL,
  `serviceId` bigint(8) NOT NULL,
  PRIMARY KEY (`spanId`),
  KEY `traceIdIndex` (`traceId`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `span3`;
CREATE TABLE `span3` (
  `spanId` varchar(32) NOT NULL,
  `traceId` varchar(32) NOT NULL,
  `parentId` varchar(32) DEFAULT NULL,
  `serviceId` bigint(8) NOT NULL,
  PRIMARY KEY (`spanId`),
  KEY `traceIdIndex` (`traceId`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `span4`;
CREATE TABLE `span4` (
  `spanId` varchar(32) NOT NULL,
  `traceId` varchar(32) NOT NULL,
  `parentId` varchar(32) DEFAULT NULL,
  `serviceId` bigint(8) NOT NULL,
  PRIMARY KEY (`spanId`),
  KEY `traceIdIndex` (`traceId`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `span5`;
CREATE TABLE `span5` (
  `spanId` varchar(32) NOT NULL,
  `traceId` varchar(32) NOT NULL,
  `parentId` varchar(32) DEFAULT NULL,
  `serviceId` bigint(8) NOT NULL,
  PRIMARY KEY (`spanId`),
  KEY `traceIdIndex` (`traceId`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `span6`;
CREATE TABLE `span6` (
  `spanId` varchar(32) NOT NULL,
  `traceId` varchar(32) NOT NULL,
  `parentId` varchar(32) DEFAULT NULL,
  `serviceId` bigint(8) NOT NULL,
  PRIMARY KEY (`spanId`),
  KEY `traceIdIndex` (`traceId`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `span7`;
CREATE TABLE `span7` (
  `spanId` varchar(32) NOT NULL,
  `traceId` varchar(32) NOT NULL,
  `parentId` varchar(32) DEFAULT NULL,
  `serviceId` bigint(8) NOT NULL,
  PRIMARY KEY (`spanId`),
  KEY `traceIdIndex` (`traceId`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `span8`;
CREATE TABLE `span8` (
  `spanId` varchar(32) NOT NULL,
  `traceId` varchar(32) NOT NULL,
  `parentId` varchar(32) DEFAULT NULL,
  `serviceId` bigint(8) NOT NULL,
  PRIMARY KEY (`spanId`),
  KEY `traceIdIndex` (`traceId`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `span9`;
CREATE TABLE `span9` (
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
DROP TABLE IF EXISTS `trace0`;
CREATE TABLE `trace0` (
  `traceId` varchar(32) NOT NULL,
  `serviceId` int(4) NOT NULL,
  `time` bigint(8) NOT NULL,
  PRIMARY KEY (`traceId`,`serviceId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `trace1`;
CREATE TABLE `trace1` (
  `traceId` varchar(32) NOT NULL,
  `serviceId` int(4) NOT NULL,
  `time` bigint(8) NOT NULL,
  PRIMARY KEY (`traceId`,`serviceId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `trace2`;
CREATE TABLE `trace2` (
  `traceId` varchar(32) NOT NULL,
  `serviceId` int(4) NOT NULL,
  `time` bigint(8) NOT NULL,
  PRIMARY KEY (`traceId`,`serviceId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `trace3`;
CREATE TABLE `trace3` (
  `traceId` varchar(32) NOT NULL,
  `serviceId` int(4) NOT NULL,
  `time` bigint(8) NOT NULL,
  PRIMARY KEY (`traceId`,`serviceId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `trace4`;
CREATE TABLE `trace4` (
  `traceId` varchar(32) NOT NULL,
  `serviceId` int(4) NOT NULL,
  `time` bigint(8) NOT NULL,
  PRIMARY KEY (`traceId`,`serviceId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `trace5`;
CREATE TABLE `trace5` (
  `traceId` varchar(32) NOT NULL,
  `serviceId` int(4) NOT NULL,
  `time` bigint(8) NOT NULL,
  PRIMARY KEY (`traceId`,`serviceId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `trace6`;
CREATE TABLE `trace6` (
  `traceId` varchar(32) NOT NULL,
  `serviceId` int(4) NOT NULL,
  `time` bigint(8) NOT NULL,
  PRIMARY KEY (`traceId`,`serviceId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `trace7`;
CREATE TABLE `trace7` (
  `traceId` varchar(32) NOT NULL,
  `serviceId` int(4) NOT NULL,
  `time` bigint(8) NOT NULL,
  PRIMARY KEY (`traceId`,`serviceId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `trace8`;
CREATE TABLE `trace8` (
  `traceId` varchar(32) NOT NULL,
  `serviceId` int(4) NOT NULL,
  `time` bigint(8) NOT NULL,
  PRIMARY KEY (`traceId`,`serviceId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `trace9`;
CREATE TABLE `trace9` (
  `traceId` varchar(32) NOT NULL,
  `serviceId` int(4) NOT NULL,
  `time` bigint(8) NOT NULL,
  PRIMARY KEY (`traceId`,`serviceId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
