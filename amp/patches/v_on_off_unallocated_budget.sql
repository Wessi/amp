ALTER TABLE `amp_activity` CHANGE `budget` `budget` TINYINT NULL DEFAULT NULL;
CREATE or replace VIEW `v_on_off_budget` AS select `a`.`amp_activity_id` AS `amp_activity_id`,IF(`a`.`budget`=1,"yes",IF(`a`.`budget`=0,"no","unallocated")) AS `budget` from `amp_activity` `a` order by `a`.`amp_activity_id`;
insert ignore into amp_columns(columnName, cellType, extractorview) values("On/Off Budget", "org.dgfoundation.amp.ar.cell.TrnTextCell","v_on_off_budget");
INSERT ignore INTO `amp_columns_order`(`columnName`,`indexOrder`) SELECT 'Budget', max(`indexOrder`) + 1 from `amp_columns_order`;