/*
Navicat MySQL Data Transfer

Source Server         : 192.168.0.210
Source Server Version : 80031
Source Host           : 192.168.0.210:8888
Source Database       : stock_spider

Target Server Type    : MYSQL
Target Server Version : 80031
File Encoding         : 65001

Date: 2022-11-20 20:08:12
*/

SET FOREIGN_KEY_CHECKS=0;

CREATE DATABASE stock_spider CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
use stock_spider;
-- ----------------------------
-- Table structure for stockstar
-- ----------------------------
DROP TABLE IF EXISTS `stockstar`;
CREATE TABLE `stockstar` (
  `id` varchar(255) NOT NULL,
  `type` varchar(100) NOT NULL,
  `fulllink` varchar(800) NOT NULL,
  `url` varchar(800) NOT NULL,
  `source` varchar(255) DEFAULT NULL,
  `title` varchar(800) NOT NULL,
  `orgnization` varchar(800) DEFAULT NULL,
  `reportdate` datetime NOT NULL,
  `authors` varchar(255) DEFAULT NULL,
  `updatetime` datetime NOT NULL,
  `downloadedfilepath` varchar(800) NOT NULL,
  `es_indexed_version2` char(1) DEFAULT NULL COMMENT '是否建入ES索引标志位版本2',
  `es_indexed_version1` char(1) DEFAULT NULL COMMENT '是否建入ES索引标志位版本1',
  `parse_updatetime` datetime DEFAULT NULL COMMENT '解析信息更新日期',
  `select_code` varchar(255) DEFAULT NULL COMMENT '记录选择码',
  `parse_type` varchar(100) DEFAULT NULL COMMENT '解析研报类别',
  `parse_orgnization` varchar(100) DEFAULT NULL COMMENT '解析研报机构',
  `parse_title` varchar(800) DEFAULT NULL COMMENT '解析研报标题',
  `parse_reportdate` datetime DEFAULT NULL COMMENT '解析研报日期',
  `parse_authors` varchar(200) DEFAULT NULL COMMENT '解析研报分析师',
  `parse_pagecount` int DEFAULT NULL COMMENT '解析研报页数',
  `parse_keypoint` longtext COMMENT '解析研报要点',
  `parse_content` longtext COMMENT '解析研报正文',
  `parse_chart` longtext COMMENT '解析研报图表信息',
  `parse_type_note` varchar(1024) DEFAULT NULL COMMENT '解析研报类别注释',
  `parse_orgnization_note` varchar(1024) DEFAULT NULL COMMENT '解析研报机构注释',
  `parse_title_note` varchar(1024) DEFAULT NULL COMMENT '解析研报标题注释',
  `parse_reportdate_note` varchar(1024) DEFAULT NULL COMMENT '解析研报日期注释',
  `parse_authors_note` varchar(1024) DEFAULT NULL COMMENT '解析研报分析师注释',
  `parse_pagecount_note` varchar(1024) DEFAULT NULL COMMENT '解析研报页数注释',
  `parse_keypoint_note` varchar(1024) DEFAULT NULL COMMENT '解析研报要点注释',
  `parse_content_note` varchar(1024) DEFAULT NULL COMMENT '解析研报正文注释',
  `parse_chart_note` varchar(1024) DEFAULT NULL COMMENT '解析研报图表信息注释',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='证券之星数据';

-- ----------------------------
-- Records of stockstar
-- ----------------------------
INSERT INTO `stockstar` VALUES ('趋势策略_20221117_浙商证券_流动性估值跟踪：个人养老金落地，影响几何？', '趋势策略', 'http://pg.jrj.com.cn/acc/Res/CN_RES/INVEST/2022/11/16/540b1f04-38f3-4afe-8fa3-675cdc615cf4.pdf', 'https://stock.stockstar.com/JC2022111700003731.shtml', 'http://stock.stockstar.com/report', '流动性估值跟踪：个人养老金落地，影响几何？', '浙商证券', '2022-11-17 00:00:00', '王杨，陈昊', '2022-11-20 12:20:40', '趋势策略_20221117_浙商证券_流动性估值跟踪：个人养老金落地，影响几何？_540b1f04-38f3-4afe-8fa3-675cdc615cf4.pdf', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
INSERT INTO `stockstar` VALUES ('趋势策略_20221118_华安证券_银行间机构现券交易点评：复盘债市近期的买与卖', '趋势策略', 'http://pg.jrj.com.cn/acc/Res/CN_RES/BOND/2022/11/18/84574cab-bd36-4eb0-a045-aefa8020e8f1.pdf', 'https://stock.stockstar.com/JC2022111800003551.shtml', 'http://stock.stockstar.com/report', '银行间机构现券交易点评：复盘债市近期的买与卖', '华安证券', '2022-11-18 00:00:00', '颜子琦', '2022-11-20 12:20:37', '趋势策略_20221118_华安证券_银行间机构现券交易点评：复盘债市近期的买与卖_84574cab-bd36-4eb0-a045-aefa8020e8f1.pdf', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
INSERT INTO `stockstar` VALUES ('趋势策略_20221118_国海证券_“固收+”策略研究系列（三）：探索“双低”策略在“固收+”中的运用', '趋势策略', 'http://pg.jrj.com.cn/acc/Res/CN_RES/INVEST/2022/11/17/97cb393d-5a96-4e04-9511-6c6cc3faf64f.pdf', 'https://stock.stockstar.com/JC2022111800002439.shtml', 'http://stock.stockstar.com/report', '“固收+”策略研究系列（三）：探索“双低”策略在“固收+”中的运用', '国海证券', '2022-11-18 00:00:00', '靳毅', '2022-11-20 12:20:38', '趋势策略_20221118_国海证券_“固收+”策略研究系列（三）：探索“双低”策略在“固收+”中的运用_97cb393d-5a96-4e04-9511-6c6cc3faf64f.pdf', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
INSERT INTO `stockstar` VALUES ('趋势策略_20221118_国海证券_三季度货政报告解读：六方面理解三季度货政报告', '趋势策略', 'http://pg.jrj.com.cn/acc/Res/CN_RES/INVEST/2022/11/17/2b3760aa-dedf-4a30-b309-ceb122d78827.pdf', 'https://stock.stockstar.com/JC2022111800003549.shtml', 'http://stock.stockstar.com/report', '三季度货政报告解读：六方面理解三季度货政报告', '国海证券', '2022-11-18 00:00:00', '胡国鹏,袁稻雨', '2022-11-20 12:20:37', '趋势策略_20221118_国海证券_三季度货政报告解读：六方面理解三季度货政报告_2b3760aa-dedf-4a30-b309-ceb122d78827.pdf', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
INSERT INTO `stockstar` VALUES ('趋势策略_20221118_建设银行_中国建设银行商品市场每日快讯', '趋势策略', 'http://pg.jrj.com.cn/acc/Res/CN_RES/INVEST/2022/11/17/bbc0e87f-4a9d-409e-b170-5ced897b824f.pdf', 'https://stock.stockstar.com/JC2022111800003909.shtml', 'http://stock.stockstar.com/report', '中国建设银行商品市场每日快讯', '建设银行', '2022-11-18 00:00:00', null, '2022-11-20 12:20:36', '趋势策略_20221118_建设银行_中国建设银行商品市场每日快讯_bbc0e87f-4a9d-409e-b170-5ced897b824f.pdf', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
INSERT INTO `stockstar` VALUES ('趋势策略_20221118_浙商证券_“新能源+”指数产品日报', '趋势策略', 'http://pg.jrj.com.cn/acc/Res/CN_RES/INVEST/2022/11/17/0eb7290b-5710-410d-a1ae-de0242b40dc3.pdf', 'https://stock.stockstar.com/JC2022111800001137.shtml', 'http://stock.stockstar.com/report', '“新能源+”指数产品日报', '浙商证券', '2022-11-18 00:00:00', '陈冀', '2022-11-20 12:20:39', '趋势策略_20221118_浙商证券_“新能源+”指数产品日报_0eb7290b-5710-410d-a1ae-de0242b40dc3.pdf', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
