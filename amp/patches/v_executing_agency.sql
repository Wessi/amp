CREATE VIEW  `v_executing_agency` 
AS select 
	`f`.`activity` AS `amp_activity_id`,
	`o`.`name` AS `name`,
	`f`.`organisation` AS `amp_org_id` 
    from (`amp_org_role` `f` join `amp_organisation` `o` join `amp_role` `r`
		on((`f`.`organisation` = `o`.`amp_org_id`) and (`f`.`role` = `r`.`amp_role_id`) and (`r`.`role_code`="EA"))) 
	order by `f`.`activity`,`o`.`name`
