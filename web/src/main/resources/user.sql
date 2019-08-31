-- ----------------------------
-- Table structure for gm_member_level
-- ----------------------------
DROP TABLE IF EXISTS `gm_member_level`;
CREATE TABLE `gm_member_level` (
  `id` char(32) PRIMARY KEY,
  `name` char(32) CHARACTER SET utf8 NOT NULL COMMENT 'LEVEL名称',
  `level_up_deposit` double(12,2) NOT NULL DEFAULT '0' COMMENT '会员等级升级所需存款',
  `level_up_integration` double(12,2) NOT NULL DEFAULT '0' COMMENT '会员等级升级所需积分',
  `level_hold_deposit` double(12,2) NOT NULL DEFAULT '0' COMMENT '会员等级保持所需存款',
  `level_hold_integration` double(12,2) NOT NULL DEFAULT '0' COMMENT '会员等级保持所需积分',
  `free_jetton_of_manth` double(12,2) NOT NULL DEFAULT '0' COMMENT '每月免费筹码',
  `birth_cash_gift` double(12,2) NOT NULL DEFAULT '0' COMMENT '生日礼金',
  `rescue_cash_ratio` double(8,4) NOT NULL DEFAULT '0' COMMENT '救援金比例',
  `return_cash_of_day` double(8,4) NOT NULL DEFAULT '0' COMMENT '日返水比例',
  `return_cash_BBIN` double(8,4) NOT NULL DEFAULT '0' COMMENT 'BBIN返水比例',
  `integration_discount` double(8,4) NOT NULL DEFAULT '0' COMMENT '积分兑换折扣',
  `drawings_limit` double(14,4) NOT NULL DEFAULT '0' COMMENT '单日提款额度',
  `week_disc_ratio` double(8,4) NOT NULL DEFAULT '0' COMMENT '每周首存优惠,存送比例',
  `week_disc_multi` double(12,2) NOT NULL DEFAULT '0' COMMENT '每周首存优惠,流水倍数',
  `week_disc_max` double(12,2) NOT NULL DEFAULT '0' COMMENT '每周首存优惠,最高赠送',
  `festival_cash_gift` int NOT NULL DEFAULT '1' COMMENT '法定节日礼金,2:无，1:有',
  `invite_discounts` int NOT NULL DEFAULT '2' COMMENT '内部邀请专属优惠,2:无，1:有',
  `VIP_deposit_path` int NOT NULL DEFAULT '2' COMMENT 'VIP独立存款通道,2:无，1:有',
  `VIP_return_time` int NOT NULL DEFAULT '2' COMMENT 'VIP平均出款时间',
  `VIP_service` int NOT NULL DEFAULT '2' COMMENT 'VIP专属客服,2:无，1:有',
  `callback` int NOT NULL DEFAULT '2' COMMENT '电话回拨,2:不允许，1:允许',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录插入时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户LEVEL等级';

-- ----------------------------
-- Table structure for gm_member_tag
-- ----------------------------
DROP TABLE IF EXISTS `gm_member_tag`;
CREATE TABLE `gm_member_tag` (
  `id` char(32) PRIMARY KEY,
  `name` char(32) NOT NULL COMMENT '标签名称',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录插入时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户标签';

-- ----------------------------
-- Table structure for gm_game_type
-- ----------------------------
DROP TABLE IF EXISTS `gm_game_type`;
CREATE TABLE `gm_game_type` (
  `id` char(32) PRIMARY KEY,
  `name` char(32) NOT NULL COMMENT '游戏名称',
  `description` varchar(1000) NOT NULL DEFAULT '' COMMENT '游戏类型排序',
  `sort` char(8) NOT NULL DEFAULT '50' COMMENT '类型排序',
  `image` mediumtext COMMENT '平台图标',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录插入时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='游戏类型';

-- ----------------------------
-- Table structure for gm_article_category
-- ----------------------------
DROP TABLE IF EXISTS `gm_article_category`;
CREATE TABLE `gm_article_category` (
  `id` char(32) PRIMARY KEY,
  `pid` char(32) NOT NULL DEFAULT '0' COMMENT '父分类ID',
  `type` int NOT NULL DEFAULT '0' COMMENT '2列表1单页',
  `name` char(64) NOT NULL COMMENT '文章分类名称',
  `is_sys` int COMMENT '1系统分类，不可删除',
  `sort` int NOT NULL DEFAULT '50' COMMENT '分类排序',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录插入时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  KEY `sort` (`sort`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='文章分类';

-- ----------------------------
-- Table structure for gm_withdraw_bank
-- ----------------------------
DROP TABLE IF EXISTS `gm_withdraw_bank`;
CREATE TABLE `gm_withdraw_bank` (
  `id` char(32) PRIMARY KEY,
  `name` varchar(32) NOT NULL COMMENT '银行名称',
  `code` varchar(32) NOT NULL DEFAULT '' COMMENT '银行代码',
  `auto_bank_id` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '易捷自动出款银行卡ID',
  `image` mediumtext COMMENT '银行图标',
  `sort` int(3) unsigned NOT NULL DEFAULT '50' COMMENT '排序',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录插入时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  UNIQUE KEY `name` (`name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='提款银行列表';

-- ----------------------------
-- Table structure for gm_article
-- ----------------------------
DROP TABLE IF EXISTS `gm_article`;
CREATE TABLE `gm_article` (
  `id` char(32) PRIMARY KEY,
  `cate_id` char(32) NOT NULL COMMENT '分类ID',
  `title` varchar(64) NOT NULL COMMENT '文章标题',
  `image` mediumtext COMMENT '文章默认图片',
  `content` text NOT NULL COMMENT '文章内容',
  `state` smallint(3) DEFAULT '1' COMMENT '1启用,2禁用',
  `sort` smallint(3) NOT NULL DEFAULT '50' COMMENT '排序',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录插入时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  KEY `cate_id` (`cate_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='文章表';

-- ----------------------------
-- Table structure for gm_game_platform
-- ----------------------------
DROP TABLE IF EXISTS `gm_game_platform`;
CREATE TABLE `gm_game_platform` (
  `id` char(32) PRIMARY KEY,
  `name` char(32) COMMENT '游戏平台名称',
  `code` char(32) COMMENT '游戏平台代码',
  `prefix` char(32) DEFAULT '' COMMENT '账户前缀',
  `balance_unlock` decimal(9,2) DEFAULT '0.00' COMMENT '余额低于多少解绑',
  `transfer_in` smallint(3) unsigned DEFAULT '1' COMMENT '1允许转入2禁止转入,默认1',
  `transfer_out` smallint(3) unsigned DEFAULT '1' COMMENT '1允许转出2禁止转出,默认1',
  `sort` smallint(3) unsigned DEFAULT '50' COMMENT '排序',
  `maintain` smallint(3) unsigned DEFAULT '1' COMMENT '1正常2维护状态,默认1',
  `state` smallint(3) unsigned DEFAULT '1' COMMENT '1正常2暂停,默认1',
  `image` mediumtext COMMENT '平台图标',
  `is_sync_password` smallint COMMENT '是否同步密码,1是，2否',
  `test_model` smallint COMMENT '试玩:1有2无,默认2',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录插入时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  UNIQUE KEY `code` (`code`) USING BTREE,
  KEY `sort` (`sort`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='游戏平台';

-- ----------------------------
-- Table structure for gm_ad_pos
-- ----------------------------
DROP TABLE IF EXISTS `gm_ad_pos`;
CREATE TABLE `gm_ad_pos` (
  `id` char(32) PRIMARY KEY,
  `name` varchar(64) NOT NULL COMMENT '广告位名称',
  `width` smallint(5) unsigned NOT NULL COMMENT '宽度',
  `height` smallint(5) unsigned NOT NULL COMMENT '高度',
  `sort` smallint(5) unsigned NOT NULL DEFAULT '50' COMMENT '排序',
  `is_sys` smallint(3) unsigned COMMENT '1系统广告位，不可删除',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录插入时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  KEY `sort` (`sort`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='广告位置';

-- ----------------------------
-- Table structure for gm_game_category
-- ----------------------------
DROP TABLE IF EXISTS `gm_game_category`;
CREATE TABLE `gm_game_category` (
  `id` char(32) PRIMARY KEY,
  `name` varchar(64) NOT NULL COMMENT '游戏分类名',
  `platform_id` char(32) NOT NULL COMMENT '平台id',
  `description` varchar(1024) COMMENT '描述',
  `sort` smallint(5) unsigned NOT NULL DEFAULT '50' COMMENT '排序',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录插入时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  KEY `sort` (`sort`) USING BTREE,
  KEY `platform_id` (`platform_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='游戏分类';

-- ----------------------------
-- Table structure for gm_ad
-- ----------------------------
DROP TABLE IF EXISTS `gm_ad`;
CREATE TABLE `gm_ad` (
  `id` char(32) PRIMARY KEY,
  `pos_id` char(32) NOT NULL COMMENT '广告位ID',
  `name` char(64) NOT NULL COMMENT '广告名称',
  `image` mediumtext COMMENT '广告图片地址',
  `image_wap` mediumtext COMMENT '手机版广告',
  `url` varchar(512) NOT NULL DEFAULT '' COMMENT '链接地址',
  `sort` smallint(5) unsigned NOT NULL DEFAULT '50' COMMENT '排序',
  `state` smallint(3) unsigned NOT NULL DEFAULT '1' COMMENT '1启用，2禁用，默认1',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录插入时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  KEY `pos_id` (`pos_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='广告';

-- ----------------------------
-- Table structure for gm_activity_category
-- ----------------------------
DROP TABLE IF EXISTS `gm_activity_category`;
CREATE TABLE `gm_activity_category` (
  `id` char(32) PRIMARY KEY,
  `name` varchar(64) NOT NULL COMMENT '活动分类名称',
  `sort` smallint(5) unsigned NOT NULL DEFAULT '50' COMMENT '排序',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录插入时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  KEY `sort` (`sort`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='活动分类';

-- ----------------------------
-- Table structure for gm_game
-- ----------------------------
DROP TABLE IF EXISTS `gm_game`;
CREATE TABLE `gm_game` (
  `id` char(32) PRIMARY KEY,
  `name` varchar(64) NOT NULL COMMENT '游戏名称',
  `name_english` varchar(128) COMMENT '英文名',
  `platform_id` char(32) COMMENT '游戏平台ID',
  `category_id` char(32) COMMENT '游戏分类ID',
  `type_id` char(32) COMMENT '游戏类型ID',
  `code` varchar(64) COMMENT '游戏代码',
  `code2` varchar(64) COMMENT '游戏代码2',
  `code_wap` varchar(64) COMMENT 'H5游戏code',
  `code_wap2` varchar(64) COMMENT 'H5游戏code2',
  `image` mediumtext COMMENT '游戏首图',
  `icon` mediumtext COMMENT '游戏图标',
  `banner` mediumtext COMMENT '游戏banner图片',
  `sort` smallint(3) unsigned NOT NULL DEFAULT '50' COMMENT '排序',
  `app_recommend` smallint(3) unsigned DEFAULT '2' COMMENT 'app推荐,1推荐，2非推荐',
  `pc_recommend` smallint(3) unsigned DEFAULT '2' COMMENT 'pc推荐,1推荐，2非推荐',
  `wap_recommend` smallint(3) unsigned DEFAULT '2' COMMENT 'H5推荐,1推荐，2非推荐',
  `name_wap` varchar(64) COMMENT '手机版名称',
  `description` varchar(255) COMMENT '游戏描述',
  `state` tinyint(3) COMMENT '状态:1上线2下线',
  `newest` tinyint(3) unsigned DEFAULT '1' COMMENT '最新标签:1最新',
  `hot` tinyint(3) unsigned DEFAULT '1' COMMENT '热门标签：1热门',
  `app_ad_link` varchar(1024) COMMENT 'app广告链接',
  `pc_ad_link` varchar(1024) COMMENT 'pa广告链接',
  `wap_ad_link` varchar(1024) COMMENT 'H5广告链接',
  'prize_line' int unsigned not null DEFAULT '0' COMMENT '返奖线',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录插入时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  KEY `sort` (`sort`) USING BTREE,
  KEY `platform_id` (`platform_id`,`category_id`,`state`) USING BTREE,
  KEY `type_id` (`type_id`,`state`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='游戏列表';

-- ----------------------------
-- Table structure for gm_activity_record
-- ----------------------------
DROP TABLE IF EXISTS `gm_activity_record`;
CREATE TABLE `gm_activity_record` (
  `id` char(32) PRIMARY KEY,
  `cate_id` char(32),
  `title` varchar(64) COMMENT '标题',
  `active_time` datetime COMMENT '活动开始时间',
  `active_user` varchar(64) DEFAULT '' COMMENT '活动对象',
  `active_platform` varchar(64) DEFAULT '' COMMENT '活动平台IDS',
  `active_type` tinyint(3) unsigned DEFAULT '1' COMMENT '1常规活动2限时活动3彩金活动',
  `apply_way` varchar(64) DEFAULT '' COMMENT '申请方式',
  `need_deposit` tinyint(3) unsigned DEFAULT '2' COMMENT '1需要存款,2不需要存款',
  `introduce` text COMMENT '活动简介',
  `detail` text COMMENT '活动详情',
  `detail_wap` text COMMENT '手机版详情',
  `rule` text COMMENT '活动规则',
  `small_image` mediumtext COMMENT '活动小图',
  `big_image` mediumtext COMMENT '活动大图',
  `gift_max` int(10) unsigned DEFAULT '0' COMMENT '最高送金额',
  `bet_multiple` smallint(5) unsigned DEFAULT '1' COMMENT '流水倍数',
  `attend_num` int(10) unsigned DEFAULT '0' COMMENT '关注数',
  `sort` tinyint(3) unsigned DEFAULT '50' COMMENT '排序',
  `state_active` tinyint(3) DEFAULT '1' COMMENT '活动状态，-1已结束0待开始1进行中',
  `state` tinyint(3) COMMENT '活动记录状态，1启用,2禁用',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录插入时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  KEY `cate_id` (`cate_id`) USING BTREE,
  KEY `state` (`state`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='活动记录';

-- ----------------------------
-- Table structure for gm_game_platform_api
-- ----------------------------
DROP TABLE IF EXISTS `gm_game_platform_api`;
CREATE TABLE `gm_game_platform_api` (
  `id` char(32) PRIMARY KEY,
  `api_name` varchar(128) COMMENT 'api名称',
  `api_address` varchar(1024) COMMENT 'api地址',
  `state` smallint(3) unsigned COMMENT '1启用，2禁用，默认1',
  `platform_id` char(32) COMMENT '所属平台id',
  `prefix` char(32) COMMENT '账号前缀',
  `api_domain` varchar(1024) COMMENT 'apiDomain',
  `operator_code` varchar(1024) COMMENT 'OperatorCode',
  `secret_key` varchar(1024) COMMENT 'SecretKey',
  `game_provider` varchar(1024) COMMENT 'GameProvider',
  `agent_id` bigint unsigned COMMENT '代理id',
  `pub_key` text COMMENT 'rsa公钥',
  `md5_key` text COMMENT 'md5密钥',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录插入时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='游戏平台API配置';

-- ----------------------------
-- Table structure for gm_game_client_address
-- ----------------------------
DROP TABLE IF EXISTS `gm_game_client_address`;
CREATE TABLE `gm_game_client_address` (
  `id` char(32) PRIMARY KEY,
  `platform_id` char(32) COMMENT '所属平台id',
  `pc_elec` varchar(1024) COMMENT '电脑地址(电子)',
  `pc_real` varchar(1024) COMMENT '电脑地址(真人)',
  `android_elec` varchar(1024) COMMENT '安卓地址(电子)',
  `android_real` varchar(1024) COMMENT '安卓地址(真人)',
  `apple_elec` varchar(1024) COMMENT '苹果地址(电子)',
  `apple_real` varchar(1024) COMMENT '苹果地址(真人)',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录插入时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='游戏平台客户端地址配置';

-- ----------------------------
-- Table structure for gm_member
-- ----------------------------
DROP TABLE IF EXISTS `gm_member`;
CREATE TABLE `gm_member`  (
  `id` char(32) PRIMARY KEY,
  `recom_id` char(32) COMMENT '推荐人ID',
  `agent_id` char(32) COMMENT '所属代理ID1默认代理',
  `level_id` char(32) COMMENT '会员等级ID,默认1-LV0',
  `tag_id` char(32) COMMENT '用户标签ID，暂时只支持1个标签',
  `vip_id` char(32) NOT NULL DEFAULT '1' COMMENT '1不算VIP，VIP等级ID',
  `vip_endtime` datetime(0) COMMENT 'VIP到期时间',
  `username` char(20) NOT NULL COMMENT '用户名',
  `password` text NOT NULL COMMENT '登录密码',
  `salt` char(32) COMMENT '密码加密串',
  `recom_code` char(15) COMMENT '用户独有的推荐码',
  `points` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '消费积分 每打200流水送1个积分',
  `points_credit` smallint(3) UNSIGNED NOT NULL DEFAULT 100 COMMENT '信用分,默认100',
  `rewards` int(10) UNSIGNED NOT NULL DEFAULT 0 COMMENT '奖章数',
  `realname` varchar(30) NOT NULL DEFAULT '' COMMENT '真实姓名',
  `nickname` varchar(30) NOT NULL DEFAULT '' COMMENT '昵称',
  `cookie` varchar(32) NOT NULL DEFAULT '' COMMENT '客户端cookie唯一标识',
  `device_mac` varchar(64) NOT NULL DEFAULT '' COMMENT '设备标识',
  `email` varchar(64) NOT NULL DEFAULT '' COMMENT '邮箱',
  `subscribe` varchar(64) NOT NULL DEFAULT '' COMMENT '订阅邮箱',
  `mobile` char(15) NOT NULL DEFAULT '' COMMENT '手机号',
  `sex` tinyint(3) UNSIGNED NOT NULL DEFAULT 0 COMMENT '性别0不限1男2女',
  `age` tinyint(3) UNSIGNED NOT NULL DEFAULT 0 COMMENT '年龄',
  `qq` char(12) NOT NULL DEFAULT '' COMMENT 'QQ',
  `qq_auth` tinyint(3) UNSIGNED NOT NULL DEFAULT 0 COMMENT '1确认认证',
  `birthday` date COMMENT '生日，生日礼金',
  `wechat` varchar(32) NOT NULL DEFAULT '' COMMENT '微信号',
  `wechat_auth` tinyint(3) UNSIGNED NOT NULL DEFAULT 0 COMMENT '1已确认认证',
  `avatar` varchar(1000) COMMENT '用户头像',
  `money` bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT '个人账户',
  `important` tinyint(3) UNSIGNED NOT NULL DEFAULT 0 COMMENT '重点用户上线提醒 0不是1是',
  `is_deposit` tinyint(3) UNSIGNED NOT NULL DEFAULT 0 COMMENT '1已存款',
  `like_platform` smallint(5) UNSIGNED NOT NULL DEFAULT 0 COMMENT '游戏偏好',
  `bbin_app_account` tinyint(3) NOT NULL DEFAULT 0 COMMENT '1已创建手机端账号',
  `state` tinyint(3) NOT NULL DEFAULT 1 COMMENT '状态 0冻结与1启用',
  `withdraw_pwd` char(32) NOT NULL DEFAULT '' COMMENT '提款密码',
  `withdraw_state` tinyint(3) UNSIGNED NOT NULL DEFAULT 1 COMMENT '提款权限 1可提2不可提',
  `withdraw_type` tinyint(1) UNSIGNED NOT NULL DEFAULT 0 COMMENT '当用户提款时 1管理员手动出款 2自动出款',
  `reg_url` varchar(255) NOT NULL DEFAULT '' COMMENT '注册网址',
  `reg_referer` varchar(1000) NOT NULL DEFAULT '' COMMENT '注册来源',
  `reg_ip` varchar(32) NOT NULL DEFAULT '' COMMENT '注册ip',
  `reg_ip_address` varchar(60) NOT NULL DEFAULT '' COMMENT 'ip归属地',
  `last_login_ip` varchar(32) NOT NULL DEFAULT '' COMMENT '最后登录IP',
  `last_login_time` varchar(32) NOT NULL DEFAULT '' COMMENT '最后登录时间',
  `login_times` int UNSIGNED NOT NULL DEFAULT 0 COMMENT '登录次数',
  `reg_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `recom_address` varchar(255) COMMENT '推广域名',
  UNIQUE INDEX `username`(`username`) USING BTREE,
  UNIQUE INDEX `recom_code`(`recom_code`) USING BTREE,
  INDEX `vip_id`(`vip_id`, `vip_endtime`) USING BTREE,
  INDEX `agent_id`(`agent_id`) USING BTREE,
  INDEX `state`(`state`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户表';

-- ----------------------------
-- Table structure for gm_member_platform_access
-- ----------------------------
DROP TABLE IF EXISTS `gm_member_platform_access`;
CREATE TABLE `gm_member_platform_access` (
  `id` char(32) PRIMARY KEY,
  `member_id` char(32) COMMENT '用户ID',
  `platform_id` char(32) COMMENT '游戏平台ID',
  `access_login` tinyint(3) unsigned NOT NULL DEFAULT '1' COMMENT '1允许登录0不允许',
  `access_trans_in` tinyint(3) unsigned NOT NULL DEFAULT '1' COMMENT '1允许转入0不允许',
  `access_trans_out` tinyint(3) unsigned NOT NULL DEFAULT '1' COMMENT '1允许转出0不允许',
  `is_online` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '1在线0离线，此字段可不用，动态检测',
  UNIQUE KEY `member_id_platform_id` (`member_id`,`platform_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户游戏平台权限';

-- ----------------------------
-- Table structure for gm_work_shift
-- ----------------------------
DROP TABLE IF EXISTS `gm_work_shift`;
CREATE TABLE `gm_work_shift` (
  `id` char(32) PRIMARY KEY,
  `shift_name` char(32) COMMENT '班次名',
  `start_time` varchar(32) not null COMMENT '开始时间',
  `end_time` varchar(32) not null COMMENT '结束时间',
  `state` tinyint(3) NOT NULL DEFAULT 1 COMMENT '状态：2冻结与1启用',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录插入时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='工作班次';

-- ----------------------------
-- Table structure for gm_work_event
-- ----------------------------
DROP TABLE IF EXISTS `gm_work_event`;
CREATE TABLE `gm_work_event` (
  `id` char(32) PRIMARY KEY,
  `event_name` char(32) COMMENT '事件名',
  `state` tinyint(3) NOT NULL DEFAULT 1 COMMENT '状态：2停用与1启用',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录插入时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='工作班次';

-- ----------------------------
-- Table structure for gm_work_log
-- ----------------------------
DROP TABLE IF EXISTS `gm_work_log`;
CREATE TABLE `gm_work_log` (
  `id` char(32) PRIMARY KEY,
  `user_id` char(32) not null COMMENT '员工id',
  `shift_id` char(32) not null COMMENT '班次id',
  `event_id` char(32) not null COMMENT '事件id',
  `description` text not null COMMENT '事件描述',
  `top_time` datetime COMMENT '置顶时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录插入时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='工作日志';

-- ----------------------------
-- Table structure for gm_game_favorite
-- ----------------------------
DROP TABLE IF EXISTS `gm_test`;
CREATE TABLE `gm_test` (
  `id` char(32) PRIMARY KEY,
  `name` char(32)  COMMENT '名字',
  `log_time` datetime COMMENT '记录时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录插入时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='test表';