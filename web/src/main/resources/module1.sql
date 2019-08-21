-- ----------------------------
-- Table structure for z_log
-- ----------------------------
DROP TABLE IF EXISTS `gm_test`;
CREATE TABLE `gm_test` (
  `id` char(32) PRIMARY KEY,
  `name` varchar(64) COMMENT '名字',
  `log_time` datetime COMMENT '置顶时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录插入时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='测试表';