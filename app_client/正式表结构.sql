/*
Navicat MySQL Data Transfer

Source Server         : dazecarrdspublic.mysql.rds.aliyuncs.com
Source Server Version : 50616
Source Host           : dazecarrdspublic.mysql.rds.aliyuncs.com:3456
Source Database       : car

Target Server Type    : MYSQL
Target Server Version : 50616
File Encoding         : 65001

Date: 2015-12-21 10:19:54
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for baoxian_base_info
-- ----------------------------
DROP TABLE IF EXISTS `baoxian_base_info`;
CREATE TABLE `baoxian_base_info` (
  `id` varchar(36) NOT NULL,
  `vehicle_model_price` varchar(20) NOT NULL COMMENT '车型价格',
  `vehicle_model_code` varchar(36) DEFAULT NULL COMMENT '车型code',
  `vehicle_model_name` varchar(200) DEFAULT NULL COMMENT '车型名',
  `driving_id` bigint(20) DEFAULT NULL COMMENT '行驶证认证id(t_user_vehicle_auth)',
  `driving_url` varchar(200) DEFAULT NULL COMMENT '行驶证地址',
  `driving_checked` int(1) DEFAULT '0' COMMENT '行驶证是否认证-1认证失败(图片问题)0未认证，1认证通过',
  `id_card_num` varchar(20) DEFAULT NULL COMMENT '车主身份证编码',
  `id_card_name` varchar(20) DEFAULT NULL COMMENT '车主身份证名',
  `city_code` varchar(36) NOT NULL COMMENT '城市code',
  `id_card_url` varchar(200) DEFAULT NULL COMMENT '车主身份证地址',
  `id_card_checked` int(1) DEFAULT '0' COMMENT '车主身份证是否已经认证-1认证失败(图片问题)0未认证，1认证通过',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `city_name` varchar(20) DEFAULT NULL COMMENT '城市名称',
  `mobile` varchar(12) DEFAULT NULL COMMENT '手机',
  `vehicle_num` varchar(20) DEFAULT NULL COMMENT '车牌，为空表示新车未上牌',
  `guohu` tinyint(1) DEFAULT NULL COMMENT '是否过户车',
  `user_id` varchar(36) NOT NULL COMMENT '用户id',
  `user_type` int(1) NOT NULL DEFAULT '0' COMMENT '用户类型0c端用户，1服务商',
  `toubaoren_card_num` varchar(20) DEFAULT NULL COMMENT '投保人身份证id',
  `toubaoren_card_name` varchar(20) DEFAULT NULL COMMENT '投保人身份证名',
  `beibaoren_card_num` varchar(20) DEFAULT '' COMMENT '被保人身份证id',
  `beibaoren_card_name` varchar(20) DEFAULT NULL COMMENT '被保人身份证名',
  `frame_num` varchar(200) DEFAULT NULL COMMENT '车辆识别码',
  `motor_num` varchar(200) DEFAULT NULL COMMENT '发动机码',
  `register_date` date DEFAULT NULL COMMENT '车辆登记日期',
  `toubaoren` tinyint(1) DEFAULT '1' COMMENT '投保人信息是否一致',
  `beibaoren` tinyint(1) DEFAULT '1' COMMENT '被保人信息是否一致',
  `deleted` tinyint(1) DEFAULT '0',
  `operator_id` varchar(20) DEFAULT NULL,
  `operator_name` varchar(36) DEFAULT NULL,
  `guohu_date` varchar(40) DEFAULT NULL COMMENT '过户时间',
  `id_card_type` varchar(20) DEFAULT NULL COMMENT '车主证件类型1身份证21企业',
  `toubaoren_card_type` varchar(20) DEFAULT NULL COMMENT '投保人证件类型1身份证21企业',
  `beibaoren_card_type` varchar(20) DEFAULT NULL COMMENT '被保人证件类型1身份证21企业',
  `owner` varchar(32) DEFAULT NULL,
  `use_property` varchar(32) DEFAULT NULL,
  `issue_date` varchar(32) DEFAULT NULL,
  `brand_model` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户填写信息';

-- ----------------------------
-- Table structure for baoxian_base_info_media
-- ----------------------------
DROP TABLE IF EXISTS `baoxian_base_info_media`;
CREATE TABLE `baoxian_base_info_media` (
  `id` varchar(36) NOT NULL,
  `name` varchar(255) NOT NULL,
  `code` varchar(64) NOT NULL COMMENT '泛化的影像资料代码',
  `url` varchar(255) DEFAULT NULL,
  `status` int(11) DEFAULT NULL COMMENT '0: 要求补全 1: 待审核 2: 已审核（冻结）',
  `weight` int(11) DEFAULT '0' COMMENT '排序权重',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`,`code`),
  UNIQUE KEY `idx_media_unique` (`id`,`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for baoxian_base_info_operation_record
-- ----------------------------
DROP TABLE IF EXISTS `baoxian_base_info_operation_record`;
CREATE TABLE `baoxian_base_info_operation_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `base_info_id` varchar(100) DEFAULT NULL COMMENT 'underwritingId',
  `content` varchar(255) DEFAULT NULL COMMENT '操作的内容',
  `admin_id` varchar(11) DEFAULT NULL COMMENT '操作人ID',
  `admin_name` varchar(255) DEFAULT NULL COMMENT '操作人姓名',
  `create_time` datetime DEFAULT NULL COMMENT '操作时间',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4035 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for baoxian_company
-- ----------------------------
DROP TABLE IF EXISTS `baoxian_company`;
CREATE TABLE `baoxian_company` (
  `id` varchar(36) NOT NULL,
  `code` varchar(200) DEFAULT NULL COMMENT '泛华编码',
  `city_code` varchar(36) DEFAULT NULL COMMENT '城市(泛华编码)',
  `name` varchar(40) DEFAULT NULL COMMENT '名称',
  `company_name` varchar(200) DEFAULT NULL COMMENT '保险公司全称',
  `pic` varchar(200) DEFAULT NULL COMMENT '图片',
  `baidu_name` varchar(40) DEFAULT NULL COMMENT '百度保险名称',
  `remark` varchar(400) DEFAULT NULL COMMENT '描述',
  `province` varchar(36) DEFAULT '' COMMENT '省份泛华code',
  `max_rebate` float DEFAULT '0' COMMENT '官方反润最大值',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` bigint(20) DEFAULT NULL COMMENT '添加人',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人',
  `open_info` int(1) DEFAULT '0' COMMENT '是否开通-1关闭 0开通c 1开通商户2全部开通',
  `support_automatic` tinyint(1) DEFAULT '1' COMMENT '是否支持自动核保',
  `channel` varchar(20) NOT NULL DEFAULT 'fanhua' COMMENT '渠道yangguang fanhua',
  PRIMARY KEY (`id`),
  UNIQUE KEY `code` (`code`,`city_code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='保险公司名称';

-- ----------------------------
-- Table structure for baoxian_company_dict
-- ----------------------------
DROP TABLE IF EXISTS `baoxian_company_dict`;
CREATE TABLE `baoxian_company_dict` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `fullname` varchar(100) DEFAULT NULL COMMENT '全名',
  `shortname` varchar(100) DEFAULT NULL COMMENT '简称',
  `logo` varchar(255) DEFAULT NULL COMMENT 'logo',
  `remark` varchar(200) DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '添加时间',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人',
  `rebate` float DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for baoxian_insure_info
-- ----------------------------
DROP TABLE IF EXISTS `baoxian_insure_info`;
CREATE TABLE `baoxian_insure_info` (
  `id` varchar(36) NOT NULL,
  `baoxian_underwriting_report_id` varchar(36) DEFAULT NULL COMMENT '核保报价id',
  `vehicle_model_code` varchar(36) DEFAULT NULL COMMENT '车型code',
  `vehicle_model_name` varchar(200) DEFAULT NULL COMMENT '车型名',
  `driving_id` bigint(11) DEFAULT NULL COMMENT '行驶证认证id(t_user_vehicle_auth)',
  `driving_name` varchar(20) DEFAULT NULL COMMENT '行驶证地址',
  `driving_num` varchar(100) DEFAULT '0' COMMENT '行驶证是否认证',
  `id_card_num` varchar(20) DEFAULT NULL COMMENT '身份证编码',
  `id_card_name` varchar(20) DEFAULT NULL COMMENT '身份证名',
  `city_code` varchar(36) NOT NULL COMMENT '城市code',
  `id_card_address` varchar(200) DEFAULT NULL COMMENT '身份证地址',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `city_name` varchar(20) DEFAULT NULL COMMENT '城市名称',
  `mobile` varchar(12) DEFAULT NULL COMMENT '手机',
  `guohu` tinyint(1) DEFAULT '0' COMMENT '是否过户车',
  `driving_date` datetime DEFAULT NULL COMMENT '领证日期',
  `syx_begin_date` varchar(40) DEFAULT NULL COMMENT '商业险保险日期',
  `jqx_begin_date` varchar(20) DEFAULT NULL COMMENT '交强险保险日期',
  `peisong_province` varchar(36) DEFAULT NULL COMMENT '配送省',
  `peisong_city` varchar(36) DEFAULT NULL COMMENT '配送城市',
  `peisong_mobile` varchar(36) DEFAULT NULL COMMENT '配送联系电话',
  `peisong_name` varchar(36) DEFAULT NULL COMMENT '收件人',
  `peisong_address` varchar(200) DEFAULT NULL COMMENT '配送地址',
  `peisong_town` varchar(36) DEFAULT NULL COMMENT '配送区',
  `user_id` varchar(36) NOT NULL COMMENT '用户id',
  `user_type` int(1) NOT NULL DEFAULT '0' COMMENT '用户类型0c端用户，1商户',
  `baoxian_peisong_id` varchar(36) DEFAULT NULL COMMENT '配送表的id',
  `baoxian_underwriting_id` varchar(36) NOT NULL COMMENT '核保信息id',
  PRIMARY KEY (`id`),
  UNIQUE KEY `baoxian_underwriting_report_id` (`baoxian_underwriting_report_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT=' 真实投保信息';

-- ----------------------------
-- Table structure for baoxian_peisong
-- ----------------------------
DROP TABLE IF EXISTS `baoxian_peisong`;
CREATE TABLE `baoxian_peisong` (
  `id` varchar(36) NOT NULL,
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `mobile` varchar(12) NOT NULL COMMENT '配送联系电话',
  `province_code` varchar(36) DEFAULT NULL COMMENT '配送省编码',
  `city_code` varchar(36) DEFAULT NULL COMMENT '配送城市编码',
  `town_code` varchar(36) DEFAULT NULL COMMENT '配送区编码',
  `name` varchar(36) DEFAULT NULL COMMENT '收件人',
  `address` varchar(200) DEFAULT NULL COMMENT '配送地址',
  `user_id` varchar(36) NOT NULL COMMENT '用户id',
  `user_type` int(1) NOT NULL DEFAULT '0' COMMENT '用户类型0c端用户，1商户',
  `province_name` varchar(20) DEFAULT NULL COMMENT '配送省',
  `city_name` varchar(20) DEFAULT NULL COMMENT '配送城市',
  `town_name` varchar(20) DEFAULT NULL COMMENT '配送区',
  `deleted` tinyint(1) DEFAULT NULL COMMENT '是否删除0否1删除',
  `id_card_address` varchar(200) DEFAULT NULL COMMENT '身份证地址',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT=' 真实投保信息';

-- ----------------------------
-- Table structure for baoxian_policy_payinfo
-- ----------------------------
DROP TABLE IF EXISTS `baoxian_policy_payinfo`;
CREATE TABLE `baoxian_policy_payinfo` (
  `id` varchar(36) NOT NULL,
  `order_num` varchar(40) NOT NULL COMMENT '渠道订单id',
  `price` decimal(8,2) NOT NULL COMMENT '支付金额',
  `pay_way` varchar(200) DEFAULT NULL COMMENT '支付方式',
  `trade_num` varchar(200) DEFAULT NULL COMMENT '交易流水',
  `pay_time` datetime DEFAULT NULL COMMENT '支付时间',
  `pay_account` varchar(200) DEFAULT NULL COMMENT '支付帐号',
  `operator_id` varchar(40) DEFAULT NULL COMMENT '操作人id',
  `operator_name` varchar(40) DEFAULT NULL COMMENT '操作人名',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `baoxian_underwriting_report_id` varchar(36) NOT NULL COMMENT '核保报价订单id',
  `policy_no` varchar(40) NOT NULL COMMENT '保单',
  PRIMARY KEY (`id`),
  UNIQUE KEY `baoxian_underwriting_report_id` (`baoxian_underwriting_report_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='保单的支付信息';

-- ----------------------------
-- Table structure for baoxian_provider_rebate
-- ----------------------------
DROP TABLE IF EXISTS `baoxian_provider_rebate`;
CREATE TABLE `baoxian_provider_rebate` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `city_code` varchar(36) NOT NULL COMMENT '泛华城市表中的城市编码',
  `city_id` bigint(20) NOT NULL COMMENT '服务商对应的地区城市id',
  `city_name` varchar(255) DEFAULT NULL COMMENT '城市名称',
  `provider_id` varchar(36) NOT NULL COMMENT '橙牛服务商id',
  `baoxian_provider_id` varchar(64) NOT NULL COMMENT '保险公司的保险供应商id',
  `baoxian_provider_name` varchar(255) DEFAULT NULL COMMENT '保险公司的保险供应商名称',
  `baoxian_rebate_max` float DEFAULT NULL COMMENT '泛华给我们的返点比例',
  `baoxian_rebate_supply` float DEFAULT NULL COMMENT '给我们的服务商的返点比例',
  `baoxian_code` varchar(255) DEFAULT NULL COMMENT '保险公司代码',
  `comment` varchar(255) DEFAULT NULL COMMENT '备注',
  `channel` varchar(255) DEFAULT NULL COMMENT '渠道',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=38474 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for baoxian_quote_info
-- ----------------------------
DROP TABLE IF EXISTS `baoxian_quote_info`;
CREATE TABLE `baoxian_quote_info` (
  `id` varchar(36) NOT NULL COMMENT 'id',
  `baoxian_underwriting_id` varchar(36) NOT NULL COMMENT '核保信息id',
  `request_time` datetime DEFAULT NULL COMMENT '提交时间',
  `request_url` varchar(2000) DEFAULT NULL COMMENT '请求的地址',
  `response_time` datetime DEFAULT NULL COMMENT '返回时间',
  `response_status` varchar(20) DEFAULT NULL COMMENT '返回的数据状态在保险公司保单状态0初审中 Quoting 初审通过待核保 Verifying核保中，VerifySuccess 核保通过待支付VerifyError 核保退回   Payed已经支付，Cantinsure 无法承保，ErrorClose关闭，TimeOut报价过期 Cancelled取消核保  Finished 已完成',
  `status` tinyint(1) DEFAULT NULL COMMENT '操作状态0失败1完成',
  `total_charge` varchar(20) DEFAULT NULL COMMENT '返回的支付金额',
  `quote_id` varchar(40) DEFAULT NULL COMMENT '返回的核保id',
  `response` text COMMENT '返回数据',
  `step` int(1) NOT NULL COMMENT '操作步骤，0提交基础数据，1补齐数据，2提交核保，3查询报价，8支付结果',
  `request` text COMMENT '请求的数据',
  PRIMARY KEY (`id`),
  KEY `baoxian_underwriting_id` (`baoxian_underwriting_id`),
  KEY `quote_id` (`quote_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='泛华接口对接报价信息';

-- ----------------------------
-- Table structure for baoxian_report_operation_record
-- ----------------------------
DROP TABLE IF EXISTS `baoxian_report_operation_record`;
CREATE TABLE `baoxian_report_operation_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `underwriting_id` varchar(100) DEFAULT NULL COMMENT 'underwritingId',
  `content` varchar(255) DEFAULT NULL COMMENT '操作的内容',
  `admin_id` varchar(11) DEFAULT NULL COMMENT '操作人ID',
  `admin_name` varchar(255) DEFAULT NULL COMMENT '操作人姓名',
  `create_time` datetime DEFAULT NULL COMMENT '操作时间',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16160 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for baoxian_report_order_operation_record
-- ----------------------------
DROP TABLE IF EXISTS `baoxian_report_order_operation_record`;
CREATE TABLE `baoxian_report_order_operation_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `report_id` varchar(100) DEFAULT NULL COMMENT 'underwritingId',
  `content` varchar(255) DEFAULT NULL COMMENT '操作的内容',
  `admin_id` varchar(11) DEFAULT NULL COMMENT '操作人ID',
  `admin_name` varchar(255) DEFAULT NULL COMMENT '操作人姓名',
  `create_time` datetime DEFAULT NULL COMMENT '操作时间',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=917 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for baoxian_report_price
-- ----------------------------
DROP TABLE IF EXISTS `baoxian_report_price`;
CREATE TABLE `baoxian_report_price` (
  `id` varchar(36) NOT NULL COMMENT 'id',
  `city_code` varchar(36) NOT NULL COMMENT '城市code（泛华）',
  `city_name` varchar(20) DEFAULT NULL COMMENT '城市名',
  `vehicle_model_price` int(11) NOT NULL COMMENT '车型价格',
  `baoxian_company_name` varchar(200) DEFAULT NULL COMMENT '保险公司',
  `baoxian_company_code` varchar(36) NOT NULL COMMENT '保险图标',
  `type` varchar(20) DEFAULT NULL COMMENT '报价方式0基本,1高性价比 ,2全面保障 ',
  `szx` varchar(12) DEFAULT NULL COMMENT '三者险',
  `csx` varchar(12) DEFAULT NULL COMMENT '车损险',
  `ckx` varchar(12) DEFAULT NULL COMMENT '司乘险',
  `dqx` varchar(12) DEFAULT NULL COMMENT '盗抢险',
  `blx` varchar(12) DEFAULT NULL COMMENT '玻璃险',
  `hfx` varchar(12) DEFAULT NULL COMMENT '划痕险',
  `bjmp` varchar(12) DEFAULT NULL COMMENT '不计免赔',
  `zrx` varchar(12) DEFAULT NULL COMMENT '自燃险',
  `total_price` varchar(12) NOT NULL COMMENT '参考报价',
  `update_time` datetime DEFAULT NULL COMMENT '数据修改时间',
  `task_code` varchar(255) DEFAULT NULL COMMENT '任务编码',
  PRIMARY KEY (`id`),
  UNIQUE KEY `city_code` (`city_code`,`vehicle_model_price`,`baoxian_company_code`,`type`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='闪电报价信息';

-- ----------------------------
-- Table structure for baoxian_task
-- ----------------------------
DROP TABLE IF EXISTS `baoxian_task`;
CREATE TABLE `baoxian_task` (
  `id` varchar(36) NOT NULL,
  `city_code` varchar(36) NOT NULL COMMENT '城市code',
  `city_name` varchar(255) DEFAULT NULL COMMENT '对应到百度抓取的城市',
  `task_code` varchar(36) NOT NULL COMMENT '任务编码',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `start_time` datetime DEFAULT NULL COMMENT '任务开始时间',
  `finished_time` datetime DEFAULT NULL COMMENT '完成时间',
  `vehicle_model_price` int(11) NOT NULL COMMENT '车价',
  `status` int(1) NOT NULL COMMENT '-1执行失败，0未执行，1执行中，2执行成功',
  `message` varchar(4000) DEFAULT NULL COMMENT '失败描述',
  `try_times` int(11) DEFAULT NULL COMMENT '重试次数<200',
  PRIMARY KEY (`id`),
  UNIQUE KEY `city_code` (`task_code`,`city_code`,`vehicle_model_price`) USING BTREE,
  KEY `task_code` (`task_code`),
  KEY `city_code_2` (`city_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='保险报价任务';

-- ----------------------------
-- Table structure for baoxian_underwriting
-- ----------------------------
DROP TABLE IF EXISTS `baoxian_underwriting`;
CREATE TABLE `baoxian_underwriting` (
  `id` varchar(36) NOT NULL COMMENT 'id',
  `baoxian_base_info_id` varchar(36) NOT NULL COMMENT '用户信息id',
  `status` int(20) DEFAULT NULL COMMENT '核保状态-1核保失败，0核保中,1核保完成',
  `baoxian_company_name` varchar(200) DEFAULT NULL COMMENT '保险公司',
  `mobile` varchar(12) NOT NULL COMMENT '手机',
  `baoxian_company_code` varchar(36) NOT NULL COMMENT '保险编码',
  `jqx` tinyint(1) DEFAULT NULL COMMENT '交强险',
  `syx` tinyint(1) DEFAULT NULL COMMENT '商业险',
  `ccs` tinyint(1) DEFAULT NULL COMMENT '车船税',
  `szx` varchar(12) DEFAULT NULL COMMENT '三者险',
  `csx` varchar(12) DEFAULT NULL COMMENT '车损险',
  `ckx` varchar(12) DEFAULT NULL COMMENT '乘客险',
  `dqx` varchar(12) DEFAULT NULL COMMENT '盗抢险',
  `blx` varchar(12) DEFAULT NULL COMMENT '玻璃险0不投保1国产玻璃，1进口玻璃',
  `hfx` varchar(12) DEFAULT NULL COMMENT '划痕险',
  `zrx` varchar(12) DEFAULT NULL COMMENT '自燃险',
  `szx_bjmp` tinyint(1) DEFAULT NULL COMMENT '三者险不计免赔',
  `csx_bjmp` tinyint(1) DEFAULT NULL COMMENT '车损险不计免赔',
  `ckx_bjmp` tinyint(1) DEFAULT NULL COMMENT '乘客险不计免赔',
  `ssx_bjmp` tinyint(1) DEFAULT NULL COMMENT '涉水险不计免赔',
  `dqx_bjmp` tinyint(1) DEFAULT NULL COMMENT '盗抢险不计免赔',
  `blx_bjmp` tinyint(1) DEFAULT NULL COMMENT '玻璃险不计免赔',
  `hfx_bjmp` tinyint(1) DEFAULT NULL COMMENT '划痕险不计免赔',
  `zrx_bjmp` tinyint(1) DEFAULT NULL COMMENT '自燃险不计免赔',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `operator_id` varchar(36) DEFAULT NULL COMMENT '报价人',
  `operator_name` varchar(20) DEFAULT NULL COMMENT '报价人',
  `update_time` datetime DEFAULT NULL COMMENT '数据修改时间',
  `report_time` datetime DEFAULT NULL COMMENT '报价时间',
  `message` varchar(4000) DEFAULT NULL COMMENT '描述',
  `fail_type` int(1) DEFAULT NULL COMMENT '失败类型0没有失败，1身份证信息不正确，2行驶证信息不正确，3其他',
  `operation_status` int(1) DEFAULT NULL COMMENT '操作状态 0未操作，1操作中',
  `city_code` varchar(36) NOT NULL COMMENT '城市code',
  `city_name` varchar(20) DEFAULT NULL COMMENT '投保城市',
  `vehicle_model_price` int(11) NOT NULL COMMENT '车型价格',
  `vehicle_model_code` varchar(36) DEFAULT NULL COMMENT '车型code',
  `jqx_start_date` date DEFAULT NULL COMMENT '交强险生效日期（估算,具体以核保报价日期为准）',
  `vehicle_model_name` varchar(200) DEFAULT NULL COMMENT '车型名',
  `ssx` varchar(12) DEFAULT NULL COMMENT '涉水险',
  `syx_start_date` date DEFAULT NULL COMMENT '商业险失效日期（估算,具体以核保报价日期为准）',
  `user_id` varchar(36) NOT NULL COMMENT '用户id',
  `user_type` int(1) NOT NULL DEFAULT '0' COMMENT '用户类型0c端用户，1商户',
  `sjzrx_bjmp` tinyint(1) DEFAULT NULL COMMENT '司机责任险不计免赔',
  `sjzrx` varchar(12) DEFAULT NULL COMMENT '司机责任险',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `tyx` tinyint(1) DEFAULT '0' COMMENT '机动车损失保险无法找到第三方特约险',
  `zdzxc` tinyint(1) DEFAULT '0' COMMENT '指定专修厂',
  `deleted` tinyint(1) DEFAULT '0' COMMENT '是否删除',
  `xubao` tinyint(1) NOT NULL DEFAULT '0' COMMENT '续保0否1是',
  `automatic_status` int(1) DEFAULT NULL COMMENT '自动核保状态为空的话不是自动核保，-1核保失败，0核保中,1核保完成',
  `automatic_message` varchar(4000) DEFAULT NULL,
  `baoxian_insure_info_id` varchar(36) DEFAULT NULL COMMENT '核保基础信息',
  `track_status` int(1) DEFAULT NULL COMMENT '跟进状态 0 初次跟进，1 意向一般，2 高意向，3 无意向，4 成功签单，5 自定义',
  `last_automatic_time` datetime DEFAULT NULL COMMENT '最近自动核保时间',
  `automatic_count` int(11) DEFAULT '0' COMMENT '自动核保次数',
  `model_price` varchar(11) DEFAULT NULL,
  `xzsbssx` tinyint(1) DEFAULT '0' COMMENT '新增设备损失险',
  `cpcssx` tinyint(1) DEFAULT '0' COMMENT '车碰车损失险',
  `zrmcty` tinyint(1) DEFAULT '0' COMMENT '自燃免除特约',
  `ssmcty` tinyint(1) DEFAULT '0' COMMENT '涉水免除特约',
  `kxmpety` varchar(12) DEFAULT '0' COMMENT '可选免赔额特约',
  `dcsgmpty` tinyint(1) DEFAULT '0' COMMENT '多次事故免赔特约',
  `cshwzrx` tinyint(1) DEFAULT '0' COMMENT '车上货物责任险',
  `scxlwpssx` tinyint(1) DEFAULT '0' COMMENT '随车行李物品损失保险',
  `jlcty` tinyint(1) DEFAULT '0' COMMENT '教练车特约',
  `jdctsssx` tinyint(1) DEFAULT '0' COMMENT '机动车停驶损失险',
  `tzckzx` tinyint(1) DEFAULT '0' COMMENT '特种车扩展险',
  `jsshfwjzrx` tinyint(1) DEFAULT '0' COMMENT '精神损害抚慰金责任险',
  `xlqjfybcx` tinyint(1) DEFAULT '0' COMMENT '修理期间费用补偿险',
  `jdcssbxwfzddsftyx` tinyint(1) DEFAULT '0' COMMENT '机动车损失保险无法找到第三方特约险',
  `sgzrmpltytk` varchar(12) DEFAULT '0' COMMENT '事故责任免赔率特约条款',
  `dcjcdssx` varchar(12) DEFAULT '0' COMMENT '倒车镜车灯损坏险',
  `bjmpx` tinyint(1) DEFAULT '0' COMMENT '不计免赔险',
  `xzsbss_bjmp` tinyint(1) DEFAULT '0' COMMENT '新增设备损失险不计免赔',
  `fjx_bjmp` tinyint(1) DEFAULT '0' COMMENT '附加险不计免赔',
  `jbx_bjmp` tinyint(1) DEFAULT '0' COMMENT '基本险不计免赔',
  `cshwzrx_bjmp` tinyint(1) DEFAULT '0' COMMENT '车上货物责任险不计免赔',
  `fjcsryzrx_bjmp` tinyint(1) DEFAULT '0' COMMENT '附加车上人员责任险不计免赔',
  `xzsbssx_bjmp` tinyint(1) DEFAULT '0' COMMENT '新增设备损失险不计免赔',
  `media_status` int(11) DEFAULT '2' COMMENT '0: 等待补全资料 1: 已提交待审核 2: 审核通过 3: 资料同步中 4: 已同步到泛化',
  `support_automatic` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否支持自动核保',
  `channel` varchar(20) NOT NULL DEFAULT 'fanhua' COMMENT '渠道yangguang fanhua',
  PRIMARY KEY (`id`),
  KEY `city_code` (`baoxian_base_info_id`,`mobile`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='核保信息';

-- ----------------------------
-- Table structure for baoxian_underwriting_date_exception
-- ----------------------------
DROP TABLE IF EXISTS `baoxian_underwriting_date_exception`;
CREATE TABLE `baoxian_underwriting_date_exception` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `date` date DEFAULT NULL,
  `kind` int(11) DEFAULT NULL COMMENT '日期类型：0-工作日；1-节假日；',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=855 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for baoxian_underwriting_express
-- ----------------------------
DROP TABLE IF EXISTS `baoxian_underwriting_express`;
CREATE TABLE `baoxian_underwriting_express` (
  `id` varchar(36) NOT NULL,
  `company` varchar(200) NOT NULL COMMENT '物流快递公司',
  `order_num` varchar(40) NOT NULL COMMENT '快递公司单号',
  `express_time` datetime DEFAULT NULL COMMENT '快递时间',
  `operator_id` varchar(40) DEFAULT NULL COMMENT '操作人id',
  `operator_name` varchar(40) DEFAULT NULL COMMENT '操作人名',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `baoxian_underwriting_report_id` varchar(36) NOT NULL COMMENT '核保报价订单id',
  PRIMARY KEY (`id`),
  UNIQUE KEY `baoxian_underwriting_report_id` (`baoxian_underwriting_report_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='保单的快递信息';

-- ----------------------------
-- Table structure for baoxian_underwriting_log
-- ----------------------------
DROP TABLE IF EXISTS `baoxian_underwriting_log`;
CREATE TABLE `baoxian_underwriting_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `baoxian_underwriting_id` varchar(36) NOT NULL COMMENT '核保请求id',
  `field_name` varchar(64) NOT NULL COMMENT '字段名称',
  `old_value` varchar(255) DEFAULT NULL COMMENT '旧值',
  `new_value` varchar(255) DEFAULT NULL COMMENT '新值',
  `operator_id` varchar(36) DEFAULT NULL COMMENT '客服ID',
  `operator_name` varchar(20) DEFAULT NULL COMMENT '客服名称',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `operation_no` int(11) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL COMMENT '操作时间',
  PRIMARY KEY (`id`),
  KEY `idx_baoxian_underwriting_log_belongs_to_underwriting_request` (`baoxian_underwriting_id`),
  CONSTRAINT `fk_baoxian_underwriting_log_belongs_to_underwriting_request` FOREIGN KEY (`baoxian_underwriting_id`) REFERENCES `baoxian_underwriting_request` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=2212 DEFAULT CHARSET=utf8 COMMENT='核保请求变更记录';

-- ----------------------------
-- Table structure for baoxian_underwriting_report
-- ----------------------------
DROP TABLE IF EXISTS `baoxian_underwriting_report`;
CREATE TABLE `baoxian_underwriting_report` (
  `id` varchar(36) NOT NULL COMMENT 'id',
  `baoxian_underwriting_id` varchar(36) NOT NULL COMMENT '核保信息id',
  `baoxian_company_code` varchar(36) NOT NULL COMMENT '保险编码',
  `baoxian_company_name` varchar(200) DEFAULT NULL COMMENT '保险公司',
  `status` int(20) DEFAULT NULL COMMENT '报价订单状态-1关闭订单，0未提交订单,1提交订单',
  `mobile` varchar(12) NOT NULL COMMENT '手机',
  `jqx_price` decimal(12,2) DEFAULT NULL COMMENT '交强险',
  `syx_price` decimal(12,2) DEFAULT NULL COMMENT '商业险',
  `ccs_price` decimal(12,2) DEFAULT NULL COMMENT '车船税',
  `szx_price` decimal(12,2) DEFAULT NULL COMMENT '三者险',
  `csx_price` decimal(12,2) DEFAULT NULL COMMENT '车损险',
  `ckx_price` decimal(12,2) DEFAULT NULL COMMENT '司乘险',
  `dqx_price` decimal(12,2) DEFAULT NULL COMMENT '盗抢险',
  `sjzrx_price` decimal(12,2) DEFAULT NULL COMMENT '司机责任',
  `ssx_price` decimal(12,2) DEFAULT NULL COMMENT '涉水险',
  `blx_price` decimal(12,2) DEFAULT NULL COMMENT '玻璃险',
  `hfx_price` decimal(12,2) DEFAULT NULL COMMENT '划痕险',
  `zrx_price` decimal(12,2) DEFAULT NULL COMMENT '自燃险',
  `szx_bjmp_price` decimal(12,2) DEFAULT NULL COMMENT '三者险不计免赔',
  `csx_bjmp_price` decimal(12,2) DEFAULT NULL COMMENT '车损险不计免赔',
  `ckx_bjmp_price` decimal(12,2) DEFAULT NULL COMMENT '乘客险不计免赔',
  `dqx_bjmp_price` decimal(12,2) DEFAULT NULL COMMENT '盗抢险不计免赔',
  `sjzrx_bjmp_price` decimal(12,2) DEFAULT NULL COMMENT '司机责任险不计免赔',
  `blx_bjmp_price` decimal(12,2) DEFAULT NULL COMMENT '玻璃险不计免赔',
  `hfx_bjmp_price` decimal(12,2) DEFAULT NULL COMMENT '划痕险不计免赔',
  `ssx_bjmp_price` decimal(12,2) DEFAULT NULL COMMENT '涉水险不计免赔',
  `zrx_bjmp_price` decimal(12,2) DEFAULT NULL COMMENT '自燃险不计免赔',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `orders_time` datetime DEFAULT NULL COMMENT '提交订单时间',
  `orders_num` varchar(36) DEFAULT NULL COMMENT '订单num',
  `total_price` decimal(12,2) DEFAULT NULL COMMENT '投保价格',
  `update_time` datetime DEFAULT NULL COMMENT '数据修改时间',
  `message` varchar(4000) DEFAULT NULL COMMENT '描述',
  `city_code` varchar(36) NOT NULL COMMENT '城市code',
  `city_name` varchar(20) DEFAULT NULL COMMENT '投保城市',
  `vehicle_model_price` int(11) NOT NULL COMMENT '车型价格',
  `vehicle_model_code` varchar(36) DEFAULT NULL COMMENT '车型code',
  `vehicle_model_name` varchar(200) DEFAULT NULL COMMENT '车型名',
  `policy_no` varchar(100) DEFAULT NULL COMMENT '保险公司投保单号',
  `pay_status` int(11) DEFAULT NULL COMMENT '支付状态-1支付失败,0未支付,1已经支付',
  `pay_time` varchar(40) DEFAULT NULL COMMENT '支付时间(支付系统返回的时间)',
  `operator_id` varchar(36) DEFAULT NULL COMMENT '报价人',
  `operator_name` varchar(20) DEFAULT NULL COMMENT '报价人',
  `operator_time` datetime DEFAULT NULL COMMENT '操作时间',
  `user_id` varchar(36) NOT NULL COMMENT '用户id',
  `user_type` int(1) NOT NULL DEFAULT '0' COMMENT '用户类型0c端用户，1商户',
  `baoxian_insure_info_id` varchar(36) DEFAULT NULL COMMENT '核保基础信息',
  `policy_status` varchar(40) DEFAULT NULL COMMENT '在保险公司保单状态0初审中 Quoting 初审通过待核保 VerifySuccess 核保通过待支付 Finished 已完成  1 已经支付承保中，2已经承保，3已经配送fail承保失败',
  `jqx_start_date` date DEFAULT NULL COMMENT '交强险生效日期',
  `syx_start_date` date DEFAULT NULL COMMENT '商业险失效日期',
  `enable_order` tinyint(1) DEFAULT NULL COMMENT '后台是否可以下单0不能1可以(关键信息没有补全或已经下单，无法人工下单）',
  `system_pay_status` int(1) DEFAULT NULL COMMENT '系统支付状态（是否已经人工支付）-1支付失败，0未支付1已经支付',
  `system_pay_time` datetime DEFAULT NULL COMMENT '系统支付时间（人工操作支付时间）',
  `tyx_price` decimal(8,2) DEFAULT '0.00' COMMENT '机动车损失保险无法找到第三方特约险',
  `zdzxc_price` decimal(8,2) DEFAULT '0.00' COMMENT '指定专修厂',
  `baoxian_policy_payinfo_id` varchar(36) DEFAULT NULL COMMENT '支付信息id',
  `express_status` int(1) DEFAULT '0' COMMENT '配送状态-1配送失败，0未配送，1已经配送',
  `baoxian_underwriting_express_id` varchar(36) DEFAULT NULL COMMENT '保单物流信息',
  `market_price` decimal(8,2) DEFAULT NULL COMMENT '市场价',
  `syx_policy_no` varchar(40) DEFAULT NULL COMMENT '商业险保单',
  `jqx_policy_no` varchar(40) DEFAULT NULL COMMENT '交强险保单',
  `underwriting_status` int(1) NOT NULL DEFAULT '0' COMMENT '承保状态0未承保，1已经承保-1承保失败',
  `expire_time` datetime DEFAULT NULL COMMENT '支付过期时间',
  `syx_prop_num` varchar(36) DEFAULT NULL COMMENT '商业险投保单号',
  `jqx_prop_num` varchar(36) DEFAULT NULL COMMENT '交强险投保单号',
  `quote_id` varchar(40) DEFAULT NULL COMMENT '返回的核保id',
  `syx_bjmp_price` decimal(8,2) DEFAULT NULL COMMENT '商业险合计不计免赔',
  `cpcssx_price` decimal(8,2) DEFAULT NULL COMMENT '车碰车损失险',
  `zrmcty_price` decimal(8,2) DEFAULT NULL COMMENT '自燃免除特约',
  `ssmcty_price` decimal(8,2) DEFAULT NULL COMMENT '涉水免除特约',
  `kxmpty_price` decimal(8,2) DEFAULT NULL COMMENT '可选免赔额特约',
  `dcsgmpty_price` decimal(8,2) DEFAULT NULL COMMENT '多次事故免赔特约',
  `xzsbssx_price` decimal(8,2) DEFAULT NULL COMMENT '新增设备损失险',
  `xzsbssx_bjmp_price` decimal(8,2) DEFAULT NULL COMMENT '新增设备损失险不计免赔',
  `cshwzrx_price` decimal(8,2) DEFAULT NULL COMMENT '车上货物责任险',
  `cshwzrx_bjmp_price` decimal(8,2) DEFAULT NULL COMMENT '车上货物责任险不计免赔',
  `ssxlwpssx_price` decimal(8,2) DEFAULT NULL COMMENT '随车行李物品损失保',
  `fjcsryzrx_bjmp_price` decimal(8,2) DEFAULT NULL COMMENT '附加车上人员责任险不计免赔',
  `jdctsssx_price` decimal(8,2) DEFAULT NULL COMMENT '机动车停驶损失险',
  `jbx_bjmp_price` decimal(8,2) DEFAULT NULL COMMENT '基本险不计免赔',
  `fjx_bjmp_price` decimal(8,2) DEFAULT NULL COMMENT '附加险不计免赔',
  `dcjcdshx_price` decimal(8,2) DEFAULT NULL COMMENT '倒车镜车灯损坏险',
  `jlcty_price` decimal(8,2) DEFAULT NULL COMMENT '教练车特约',
  `sgzrmplty_price` decimal(8,2) DEFAULT NULL COMMENT '事故责任免赔率特约条款',
  `tzckzx_price` decimal(8,2) DEFAULT NULL COMMENT '特种车扩展险',
  `jdcssbxwfzddsftyx_price` decimal(8,2) DEFAULT NULL COMMENT '机动车损失保险无法找到第三方特约险',
  `xlqjfybcx_price` decimal(8,2) DEFAULT NULL COMMENT '修理期间费用补偿险',
  `jsshfwjzrx_pirce` decimal(8,2) DEFAULT NULL COMMENT '精神损害抚慰金责任险',
  `other_price` decimal(8,2) DEFAULT NULL COMMENT '其他险',
  `bjmpx_price` decimal(8,2) DEFAULT NULL COMMENT '不计免赔险',
  `channel` varchar(20) DEFAULT 'fanhua' COMMENT '取到阳光泛华',
  `un` double DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `baoxian_underwriting_id` (`baoxian_underwriting_id`) USING BTREE,
  UNIQUE KEY `city_code` (`quote_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='核保报价信息';

-- ----------------------------
-- Table structure for baoxian_underwriting_report_payinfo
-- ----------------------------
DROP TABLE IF EXISTS `baoxian_underwriting_report_payinfo`;
CREATE TABLE `baoxian_underwriting_report_payinfo` (
  `id` varchar(24) NOT NULL,
  `trade_num` varchar(200) DEFAULT NULL COMMENT '交易流水',
  `pay_way` varchar(20) DEFAULT NULL COMMENT '支付方式',
  `price` decimal(8,2) DEFAULT NULL COMMENT '支付价格',
  `create_time` datetime DEFAULT NULL,
  `operator_id` varchar(36) DEFAULT NULL COMMENT '操作人',
  `operator_name` varchar(40) DEFAULT NULL COMMENT '操作人名',
  `baoxian_underwriting_report_id` varchar(36) NOT NULL COMMENT '核保报价订单id',
  `response_info` varchar(4000) DEFAULT NULL COMMENT '返回的支付信息',
  `response_time` datetime DEFAULT NULL COMMENT '返回时间',
  `request_info` varchar(4000) DEFAULT NULL COMMENT '请求',
  `status` int(1) DEFAULT NULL COMMENT '支付结果-1支付失败0确认中1已经支付',
  `message` varchar(400) DEFAULT NULL COMMENT '结果描述',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='客户的支付信息';

-- ----------------------------
-- Table structure for baoxian_underwriting_request
-- ----------------------------
DROP TABLE IF EXISTS `baoxian_underwriting_request`;
CREATE TABLE `baoxian_underwriting_request` (
  `id` varchar(36) NOT NULL COMMENT 'id',
  `jqx` tinyint(1) DEFAULT '0' COMMENT '交强险',
  `jqx_status` tinyint(2) DEFAULT '0' COMMENT '交强险修改状态(0 原始状态，1 修改状态)',
  `syx` tinyint(1) DEFAULT '0' COMMENT '商业险',
  `syx_status` tinyint(2) DEFAULT '0' COMMENT '商业险修改状态',
  `ccs` tinyint(1) DEFAULT '0' COMMENT '车船税',
  `ccs_status` tinyint(2) DEFAULT '0' COMMENT '车船税修改状态',
  `szx` varchar(12) DEFAULT '0' COMMENT '三者险',
  `szx_status` tinyint(2) DEFAULT '0' COMMENT '三者险修改状态',
  `szx_bjmp` tinyint(1) DEFAULT '0' COMMENT '三者险不计免赔',
  `szx_bjmp_status` tinyint(2) DEFAULT '0' COMMENT '三者险不计免赔修改状态',
  `csx` varchar(12) DEFAULT '0' COMMENT '车损险',
  `csx_status` tinyint(2) DEFAULT '0' COMMENT '车损险修改状态',
  `ckx` varchar(12) DEFAULT '0' COMMENT '乘客险',
  `ckx_status` tinyint(2) DEFAULT '0' COMMENT '乘客险修改状态',
  `dqx` varchar(12) DEFAULT '0' COMMENT '盗抢险',
  `dqx_status` tinyint(2) DEFAULT '0' COMMENT '盗抢险修改状态',
  `blx` varchar(12) DEFAULT '0' COMMENT '玻璃险0不投保1国产玻璃，2进口玻璃',
  `blx_status` tinyint(2) DEFAULT '0' COMMENT '玻璃险修改状态',
  `hfx` varchar(12) DEFAULT '0' COMMENT '划痕险',
  `hfx_status` tinyint(2) DEFAULT '0' COMMENT '划痕险修改状态',
  `zrx` varchar(12) DEFAULT '0' COMMENT '自燃险',
  `zrx_status` tinyint(2) DEFAULT '0' COMMENT '自燃险修改状态',
  `csx_bjmp` tinyint(1) DEFAULT '0' COMMENT '车损险不计免赔',
  `csx_bjmp_status` tinyint(2) DEFAULT '0' COMMENT '车损险不计免赔修改状态',
  `ckx_bjmp` tinyint(1) DEFAULT '0' COMMENT '乘客险不计免赔',
  `ckx_bjmp_status` tinyint(2) DEFAULT '0' COMMENT '乘客险不计免赔修改状态',
  `ssx_bjmp` tinyint(1) DEFAULT '0' COMMENT '涉水险不计免赔',
  `ssx_bjmp_status` tinyint(2) DEFAULT '0' COMMENT '涉水险不计免赔修改状态',
  `dqx_bjmp` tinyint(1) DEFAULT '0' COMMENT '盗抢险不计免赔',
  `dqx_bjmp_status` tinyint(2) DEFAULT '0' COMMENT '盗抢险不计免赔修改状态',
  `blx_bjmp` tinyint(1) DEFAULT '0' COMMENT '玻璃险不计免赔',
  `blx_bjmp_status` tinyint(2) DEFAULT '0' COMMENT '玻璃险不计免赔修改状态',
  `hfx_bjmp` tinyint(1) DEFAULT '0' COMMENT '划痕险不计免赔',
  `hfx_bjmp_status` tinyint(2) DEFAULT '0' COMMENT '划痕险不计免赔修改状态',
  `zrx_bjmp` tinyint(1) DEFAULT '0' COMMENT '自燃险不计免赔',
  `zrx_bjmp_status` tinyint(2) DEFAULT '0' COMMENT '自燃险不计免赔修改状态',
  `ssx` varchar(12) DEFAULT '0' COMMENT '涉水险',
  `ssx_status` tinyint(2) DEFAULT '0' COMMENT '涉水险修改状态',
  `sjzrx` varchar(12) DEFAULT '0' COMMENT '司机责任险',
  `sjzrx_status` tinyint(2) DEFAULT '0' COMMENT '司机责任险修改状态',
  `sjzrx_bjmp` tinyint(1) DEFAULT '0' COMMENT '司机责任险不计免赔',
  `sjzrx_bjmp_status` tinyint(2) DEFAULT '0' COMMENT '司机责任险不计免赔修改状态',
  `tyx` tinyint(1) DEFAULT '0' COMMENT '机动车损失保险无法找到第三方特约险',
  `tyx_status` tinyint(2) DEFAULT '0' COMMENT '机动车损失保险无法找到第三方特约险修改状态',
  `zdzxc` tinyint(1) DEFAULT '0' COMMENT '指定专修厂',
  `zdzxc_status` tinyint(2) DEFAULT '0' COMMENT '指定专修厂修改状态',
  `jbx_bjmp` tinyint(1) DEFAULT '0' COMMENT '基本险不计免赔险',
  `jbx_bjmp_status` tinyint(2) DEFAULT '0' COMMENT '基本险不计免赔修改状态',
  `xzsbssx` tinyint(1) DEFAULT '0' COMMENT '新增设备损失险',
  `xzsbssx_status` tinyint(2) DEFAULT '0' COMMENT '新增设备损失险修改状态',
  `xzsbss_bjmp` tinyint(1) DEFAULT '0' COMMENT '新增设备损失险不计免赔',
  `xzsbss_bjmp_status` tinyint(2) DEFAULT '0' COMMENT '新增设备损失险不计免赔修改状态',
  `cpcssx` tinyint(1) DEFAULT '0' COMMENT '车碰车损失险',
  `cpcssx_status` tinyint(2) DEFAULT '0' COMMENT '车碰车损失险修改状态',
  `zrmcty` tinyint(1) DEFAULT '0' COMMENT '自燃免除特约',
  `zrmcty_status` tinyint(2) DEFAULT '0' COMMENT '自燃免除特约修改状态',
  `ssmcty` tinyint(1) DEFAULT '0' COMMENT '涉水免除特约',
  `ssmcty_status` tinyint(2) DEFAULT '0' COMMENT '涉水免除特约修改状态',
  `kxmpety` varchar(12) DEFAULT '0' COMMENT '可选免赔额特约',
  `kxmpety_status` tinyint(2) DEFAULT '0' COMMENT '可选免赔额特约修改状态',
  `dcsgmpty` tinyint(1) DEFAULT '0' COMMENT '多次事故免赔特约',
  `dcsgmpty_status` tinyint(2) DEFAULT '0' COMMENT '多次事故免赔特约修改状态',
  `cshwzrx` tinyint(1) DEFAULT '0' COMMENT '车上货物责任险',
  `cshwzrx_status` tinyint(2) DEFAULT '0' COMMENT '车上货物责任险修改状态',
  `cshwzrx_bjmp` tinyint(1) DEFAULT '0' COMMENT '车上货物责任险不计免赔',
  `cshwzrx_bjmp_status` tinyint(2) DEFAULT '0' COMMENT '车上货物责任险不计免赔修改状态',
  `scxlwpssx` tinyint(1) DEFAULT '0' COMMENT '随车行李物品损失保险',
  `scxlwpssx_status` tinyint(2) DEFAULT '0' COMMENT '随车行李物品损失保险修改状态',
  `jlcty` tinyint(1) DEFAULT '0' COMMENT '教练车特约',
  `jlcty_status` tinyint(2) DEFAULT '0' COMMENT '教练车特约修改状态',
  `jdctsssx` tinyint(1) DEFAULT '0' COMMENT '机动车停驶损失险',
  `jdctsssx_status` tinyint(2) DEFAULT '0' COMMENT '机动车停驶损失险修改状态',
  `tzckzx` tinyint(1) DEFAULT '0' COMMENT '特种车扩展险',
  `tzckzx_status` tinyint(2) DEFAULT '0' COMMENT '特种车扩展险修改状态',
  `jsshfwjzrx` tinyint(1) DEFAULT '0' COMMENT '精神损害抚慰金责任险',
  `jsshfwjzrx_status` tinyint(2) DEFAULT '0' COMMENT '精神损害抚慰金责任险修改状态',
  `xlqjfybcx` tinyint(1) DEFAULT '0' COMMENT '修理期间费用补偿险',
  `xlqjfybcx_status` tinyint(2) DEFAULT '0' COMMENT '修理期间费用补偿险修改状态',
  `jdcssbxwfzddsftyx` tinyint(1) DEFAULT '0' COMMENT '机动车损失保险无法找到第三方特约险',
  `jdcssbxwfzddsftyx_status` tinyint(2) DEFAULT '0' COMMENT '机动车损失保险无法找到第三方特约险修改状态',
  `sgzrmpltytk` varchar(12) DEFAULT '0' COMMENT '事故责任免赔率特约条款',
  `sgzrmpltytk_status` tinyint(2) DEFAULT '0' COMMENT '事故责任免赔率特约条款修改状态',
  `dcjcdssx` varchar(12) DEFAULT '0' COMMENT '倒车镜车灯损坏险',
  `dcjcdssx_status` tinyint(2) DEFAULT '0' COMMENT '倒车镜车灯损坏险修改状态',
  `bjmpx` tinyint(1) DEFAULT '0' COMMENT '不计免赔险',
  `bjmpx_status` tinyint(2) DEFAULT '0' COMMENT '不计免赔险修改状态',
  `fjx_bjmp` tinyint(1) DEFAULT '0' COMMENT '附加险不计免赔',
  `fjx_bjmp_status` tinyint(2) DEFAULT '0' COMMENT '附加险不计免赔修改状态',
  `fjcsryzrx_bjmp` tinyint(1) DEFAULT '0' COMMENT '附加车上人员责任险不计免赔',
  `fjcsryzrx_bjmp_status` tinyint(2) DEFAULT '0' COMMENT '附加车上人员责任险不计免赔修改状态',
  `remark` varchar(255) DEFAULT NULL COMMENT '核保方案修改备注',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `last_operation_no` int(11) DEFAULT '0' COMMENT '最后的操作批次',
  `xzsbssx_bjmp` tinyint(1) DEFAULT '0' COMMENT '新增设备损失险不计免赔',
  `xzsbssx_bjmp_status` tinyint(2) DEFAULT '0' COMMENT '新增设备损失险不计免赔修改状态',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='核保原始请求信息';

-- ----------------------------
-- Table structure for baoxian_underwriting_submit_log
-- ----------------------------
DROP TABLE IF EXISTS `baoxian_underwriting_submit_log`;
CREATE TABLE `baoxian_underwriting_submit_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `baoxian_underwriting_id` varchar(36) NOT NULL,
  `status` int(11) DEFAULT NULL,
  `message` varchar(255) DEFAULT NULL,
  `operator_id` varchar(36) DEFAULT NULL,
  `operator_name` varchar(20) DEFAULT NULL COMMENT '报价人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `fk_baoxian_underwriting_submit_log_belongs_to_baoxian_under_idx` (`baoxian_underwriting_id`),
  CONSTRAINT `fk_baoxian_underwriting_submit_log` FOREIGN KEY (`baoxian_underwriting_id`) REFERENCES `baoxian_underwriting` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=4496 DEFAULT CHARSET=utf8;
