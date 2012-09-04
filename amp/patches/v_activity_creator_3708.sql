CREATE or replace VIEW `v_activity_creator` AS select `a`.`amp_activity_id` AS `amp_activity_id`, CONCAT(`u`.`FIRST_NAMES`," ",`u`.`LAST_NAME`) AS `name`,`atm`.`user` AS `user_id` from ((`amp_activity` `a` join `amp_team_member` `atm`) join `dg_user` `u`) where ((`atm`.`amp_team_mem_id` = `a`.`activity_creator`) and (`atm`.`user` = `u`.`ID`)) order by `a`.`amp_activity_id` ;