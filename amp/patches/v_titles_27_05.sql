CREATE or replace VIEW `v_titles` AS select `amp_activity`.`amp_activity_id` AS `amp_activity_id`,`amp_activity`.`name` AS `name`,`amp_activity`.`amp_activity_id` AS `title_id`,`amp_activity`.`draft` AS `draft`, `amp_activity`.`approval_status` AS `status` from `amp_activity` order by `amp_activity`.`amp_activity_id`;