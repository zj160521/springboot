-- ----------------------------
-- Table structure for z_log
-- ----------------------------
DROP TABLE IF EXISTS `z_log`;
CREATE TABLE `z_log` (
  `id` char(32) PRIMARY KEY,
  `num` integer COMMENT '事件id',
  `description` text not null COMMENT '事件描述',
  `log_time` datetime COMMENT '置顶时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录插入时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='工作日志';