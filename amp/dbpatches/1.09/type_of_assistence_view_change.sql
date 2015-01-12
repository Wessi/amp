CREATE OR REPLACE VIEW `v_terms_assist` AS select `a`.`amp_activity_id` AS `amp_activity_id`,`val`.`category_value` AS `terms_assist_name`,`val`.`id` AS `terms_assist_code` from amp_activity a,amp_funding fund,amp_category_value val WHERE 
 fund.amp_activity_id = a.amp_activity_id AND val.id = fund.type_of_assistance_category_value_id
 group by `a`.`amp_activity_id`,`val`.`id` order by `a`.`amp_activity_id`,`val`.`category_value`