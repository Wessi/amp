DROP VIEW IF EXISTS `amp_local`.`v_indicator_name`;
CREATE OR REPLACE  VIEW `v_indicator_name` AS select `a`.`activity_id` AS `amp_activity_id`,`b`.`name` AS `name`,`b`.`amp_me_indicator_id` AS `amp_me_indicator_id` from ((`amp_me_indicator_value` `a` join `amp_me_indicators` `b`) join `amp_activity` `c`) where ((`c`.`amp_activity_id` = `a`.`activity_id`) and (`a`.`me_indicator_id` = `b`.`amp_me_indicator_id`));

DROP VIEW IF EXISTS `amp_local`.`v_indicator_description`;
CREATE OR REPLACE  VIEW `v_indicator_description` AS select `a`.`activity_id` AS `amp_activity_id`,`b`.`description` AS `name`,`b`.`amp_me_indicator_id` AS `amp_me_indicator_id` from ((`amp_me_indicator_value` `a` join `amp_me_indicators` `b`) join `amp_activity` `c`) where ((`c`.`amp_activity_id` = `a`.`activity_id`) and (`a`.`me_indicator_id` = `b`.`amp_me_indicator_id`));

DROP VIEW IF EXISTS `amp_local`.`v_indicator_id`;
CREATE OR REPLACE  VIEW `v_indicator_id` AS select `a`.`activity_id` AS `amp_activity_id`,`b`.`code` AS `name`,`b`.`amp_me_indicator_id` AS `amp_me_indicator_id` from ((`amp_me_indicator_value` `a` join `amp_me_indicators` `b`) join `amp_activity` `c`) where ((`c`.`amp_activity_id` = `a`.`activity_id`) and (`a`.`me_indicator_id` = `b`.`amp_me_indicator_id`));

DROP VIEW IF EXISTS `amp_local`.`v_indicator_actualvalue`;
CREATE OR REPLACE  VIEW `v_indicator_actualvalue` AS select `a`.`activity_id` AS `amp_activity_id`,`a`.`actual_val` AS `name`,`b`.`amp_me_indicator_id` AS `amp_me_indicator_id` from ((`amp_me_indicator_value` `a` join `amp_me_indicators` `b`) join `amp_activity` `c`) where ((`c`.`amp_activity_id` = `a`.`activity_id`) and (`a`.`me_indicator_id` = `b`.`amp_me_indicator_id`));

DROP VIEW IF EXISTS `amp_local`.`v_indicator_basevalue`;
CREATE OR REPLACE  VIEW `v_indicator_basevalue` AS select `a`.`activity_id` AS `amp_activity_id`,`a`.`base_val` AS `name`,`b`.`amp_me_indicator_id` AS `amp_me_indicator_id` from ((`amp_me_indicator_value` `a` join `amp_me_indicators` `b`) join `amp_activity` `c`) where ((`c`.`amp_activity_id` = `a`.`activity_id`) and (`a`.`me_indicator_id` = `b`.`amp_me_indicator_id`));

DROP VIEW IF EXISTS `amp_local`.`v_indicator_targetvalue`;
CREATE OR REPLACE  VIEW `v_indicator_targetvalue` AS select `a`.`activity_id` AS `amp_activity_id`,`a`.`actual_val` AS `name`,`b`.`amp_me_indicator_id` AS `amp_me_indicator_id` from ((`amp_me_indicator_value` `a` join `amp_me_indicators` `b`) join `amp_activity` `c`) where ((`c`.`amp_activity_id` = `a`.`activity_id`) and (`a`.`me_indicator_id` = `b`.`amp_me_indicator_id`));

INSERT INTO `amp_columns` VALUES (30,'Indicator Name','indicatorName','org.dgfoundation.amp.ar.cell.TextCell','v_indicator_name'),(31,'Indicator Description','indicatorDescription','org.dgfoundation.amp.ar.cell.TextCell','v_indicator_description'),(32,'Indicator ID','indicatorID','org.dgfoundation.amp.ar.cell.TextCell','v_indicator_id'),(33,'Indicator Current Value','indicatorCurrentValue','org.dgfoundation.amp.ar.cell.TextCell','v_indicator_actualvalue'),(34,'Indicator Base Value','indicatorBaseValue','org.dgfoundation.amp.ar.cell.TextCell','v_indicator_basevalue'),(35,'Indicator Target Value','indicatorTargetValue','org.dgfoundation.amp.ar.cell.TextCell','v_indicator_targetvalue');