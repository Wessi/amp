CREATE or replace VIEW `v_executing_agency_groups` AS select `f`.`activity` AS `amp_activity_id`,`b`.`org_grp_name` AS `name`,`b`.`amp_org_grp_id` AS `amp_org_grp_id` from (((`amp_org_role` `f` join `amp_organisation` `o`) join `amp_role` `r`) join `amp_org_group` `b` on(((`f`.`organisation` = `o`.`amp_org_id`) and (`f`.`role` = `r`.`amp_role_id`) and (`r`.`role_code` = 'EA') and (`o`.`org_grp_id` = `b`.`amp_org_grp_id`)))) order by `f`.`activity`,`o`.`name`;
CREATE or replace VIEW `v_implementing_agency_groups` AS select `f`.`activity` AS `amp_activity_id`,`b`.`org_grp_name` AS `name`,`b`.`amp_org_grp_id` AS `amp_org_grp_id` from (((`amp_org_role` `f` join `amp_organisation` `o`) join `amp_role` `r`) join `amp_org_group` `b` on(((`f`.`organisation` = `o`.`amp_org_id`) and (`f`.`role` = `r`.`amp_role_id`) and (`r`.`role_code` = 'IA') and (`o`.`org_grp_id` = `b`.`amp_org_grp_id`)))) order by `f`.`activity`,`o`.`name`;
CREATE or replace VIEW `v_beneficiary_agency_groups` AS select `f`.`activity` AS `amp_activity_id`,`b`.`org_grp_name` AS `name`,`b`.`amp_org_grp_id` AS `amp_org_grp_id` from (((`amp_org_role` `f` join `amp_organisation` `o`) join `amp_role` `r`) join `amp_org_group` `b` on(((`f`.`organisation` = `o`.`amp_org_id`) and (`f`.`role` = `r`.`amp_role_id`) and (`r`.`role_code` = 'BA') and (`o`.`org_grp_id` = `b`.`amp_org_grp_id`)))) order by `f`.`activity`,`o`.`name`;
CREATE or replace VIEW `v_responsible_organisation_groups` AS select `f`.`activity` AS `amp_activity_id`,`b`.`org_grp_name` AS `name`,`b`.`amp_org_grp_id` AS `amp_org_grp_id` from (((`amp_org_role` `f` join `amp_organisation` `o`) join `amp_role` `r`) join `amp_org_group` `b` on(((`f`.`organisation` = `o`.`amp_org_id`) and (`f`.`role` = `r`.`amp_role_id`) and (`r`.`role_code` = 'RO') and (`o`.`org_grp_id` = `b`.`amp_org_grp_id`)))) order by `f`.`activity`,`o`.`name`;