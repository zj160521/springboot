                                                                                                                           -- ----------------------------
-- Table structure for gm_stat_finance
-- ----------------------------
DROP TABLE IF EXISTS `gm_stat_finance`;
CREATE TABLE `gm_stat_finance` (
  `id` char(32) PRIMARY KEY,
  `type` tinyint(3) unsigned NOT NULL DEFAULT '1' COMMENT '1小时,2日,3月,4年',
  `stat_date` char(32) NOT NULL COMMENT '统计时间，格式2019-04-09',
  `stat_hour` smallint unsigned COMMENT '统计时刻',
  `deposite_total` decimal(14,2) unsigned NOT NULL DEFAULT '0.00' COMMENT '存款总额',
  `fee_total` decimal(14,2) unsigned NOT NULL DEFAULT '0.00' COMMENT '存款手续费',
  `withdraw_total` decimal(14,2) unsigned NOT NULL DEFAULT '0.00' COMMENT '提款总额',
  `bonus_total` decimal(14,2) unsigned NOT NULL DEFAULT '0.00' COMMENT '红利总额',
  `bet_total` decimal(14,2) unsigned NOT NULL DEFAULT '0.00' COMMENT '投注总额',
  `win_total` decimal(14,2) unsigned NOT NULL DEFAULT '0.00' COMMENT '中奖总额',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录插入时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  UNIQUE KEY `type_stat_date` (`type`,`stat_date`,`stat_hour`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='财务报表';

-- ----------------------------
-- Table structure for gm_day_payment
-- ----------------------------
DROP TABLE IF EXISTS `gm_payment_stat`;
CREATE TABLE `gm_payment_stat` (
  `id` char(32) PRIMARY KEY,
  `stat_date` char(32) NOT NULL COMMENT '统计时间，格式2019-04-09',
  `payment` char(32) NOT NULL COMMENT '支付线路',
  `deposit_total` decimal(14,2) unsigned NOT NULL DEFAULT '0.00' COMMENT '存款总额',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录插入时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='支付线路统计';

-- ----------------------------
-- Table structure for gm_member_stat
-- ----------------------------
DROP TABLE IF EXISTS `gm_member_stat`;
CREATE TABLE `gm_member_stat` (
  `id` char(32) PRIMARY KEY,
  `agent_id` int(10) unsigned COMMENT '所属代理ID',
  `member_id` char(32) COMMENT '用户ID',
  `deposit_count` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '总存款次数',
  `deposit_total` decimal(14,2) unsigned NOT NULL DEFAULT '0.00' COMMENT '存款总额',
  `withdraw_count` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '总提款次数',
  `withdraw_total` decimal(14,2) unsigned NOT NULL DEFAULT '0.00' COMMENT '提款总额',
  `promo_total` decimal(14,2) unsigned NOT NULL DEFAULT '0.00' COMMENT '优惠总额:专指存送活动送的钱',
  `promo_count` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '优惠次数',
  `bonus_total` decimal(14,2) unsigned NOT NULL DEFAULT '0.00' COMMENT '红利总额:反水等N种',
  `bonus_count` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '红利次数',
  `rebate_total` decimal(14,2) unsigned NOT NULL DEFAULT '0.00' COMMENT '反水总额',
  `bonus_lock_total` decimal(14,2) unsigned NOT NULL DEFAULT '0.00' COMMENT '锁定红利总额',
  `sign_days` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '签到次数',
  `login_days` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '登录天数',
  `login_times` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '登录次数',
  `last_login_time` char(32) COMMENT '最后登录时间',
  `last_login_ip` char(15) COMMENT '最后登录IP',
  `first_deposit_money` decimal(14,2) unsigned NOT NULL DEFAULT '0.00' COMMENT '首存金额',
  `first_deposit_time` char(32) COMMENT '首存时间',
  `last_deposit_time` char(32) COMMENT '末存时间',
  `first_withdraw_time` char(32) COMMENT '首提时间',
  `last_withdraw_time` char(32) COMMENT '末提时间',
  `reg_domain` varchar(64) COMMENT '注册域名',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录插入时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  UNIQUE KEY `member_id` (`member_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户数据统计';