CREATE OR REPLACE VIEW `v_primaryprogram` AS select `a`.`amp_activity_id` AS `amp_activity_id`,`t`.`name` AS `name`,`ap`.`amp_program_id` AS `amp_program_id`,`ap`.`program_percentage` AS `program_percentage` from ((`amp_activity` `a` join `amp_activity_program` `ap` on (((`a`.`amp_activity_id` = `ap`.`amp_activity_id`) and (`ap`.`program_setting` = 2)))) join `amp_theme` `t` on ((`t`.`amp_theme_id` = `ap`.`amp_program_id`))) order by `a`.`amp_activity_id`, `t`.`name`;
CREATE OR REPLACE VIEW `v_secondaryprogram` AS select `a`.`amp_activity_id` AS `amp_activity_id`,`t`.`name` AS `name`,`ap`.`amp_program_id` AS `amp_program_id`,`ap`.`program_percentage` AS `program_percentage` from ((`amp_activity` `a` join `amp_activity_program` `ap` on (((`a`.`amp_activity_id` = `ap`.`amp_activity_id`) and (`ap`.`program_setting` = 3)))) join `amp_theme` `t` on ((`t`.`amp_theme_id` = `ap`.`amp_program_id`))) order by `a`.`amp_activity_id`,`t`.`name`;
CREATE OR REPLACE VIEW `v_sub_sectors` AS select `sa`.`amp_activity_id` AS `amp_activity_id`,`getSectorName`(`getParentSubSectorId`(`s`.`amp_sector_id`)) AS `sectorname`,`getParentSubSectorId`(`s`.`amp_sector_id`) AS `amp_sector_id`,sum(`sa`.`sector_percentage`) AS `sector_percentage`,`s`.`amp_sec_scheme_id` AS `amp_sec_scheme_id`,`ss`.`sec_scheme_name` AS `sec_scheme_name` from (((`amp_sector_scheme` `ss` join `amp_classification_config` `cc` on (((`cc`.`name` = _utf8'Primary') and (`cc`.`classification_id` = `ss`.`amp_sec_scheme_id`)))) join `amp_sector` `s` on (((`s`.`amp_sec_scheme_id` = `ss`.`amp_sec_scheme_id`) and (`getSectorDepth`(`s`.`amp_sector_id`) >= 1))))join `amp_activity_sector` `sa` on (((`sa`.`amp_sector_id` = `s`.`amp_sector_id`) and (`sa`.`classification_config_id` = `cc`.`id`)))) group by `sa`.`amp_activity_id`,`getParentSubSectorId`(`s`.`amp_sector_id`) order by `sa`.`amp_activity_id`,`getSectorName`(`getParentSubSectorId`(`s`.`amp_sector_id`));

DROP TABLE IF EXISTS cached_amp_activity;
CREATE TABLE cached_amp_activity LIKE amp_activity;
INSERT INTO cached_amp_activity SELECT * FROM amp_activity;

DROP TABLE IF EXISTS cached_v_donor_date_hierarchy;
CREATE TABLE cached_v_donor_date_hierarchy AS SELECT * FROM v_donor_date_hierarchy limit 0,0;
alter table cached_v_donor_date_hierarchy modify quarter_name varchar(2);
insert into cached_v_donor_date_hierarchy SELECT * FROM v_donor_date_hierarchy;

DROP TABLE IF EXISTS cached_v_status;
CREATE TABLE cached_v_status AS SELECT * FROM v_status;

DROP TABLE IF EXISTS `cached_v_regions`;
CREATE TABLE cached_v_regions AS SELECT * FROM `v_regions_cached`;

DROP TABLE IF EXISTS `cached_v_primary_program`;
CREATE TABLE cached_v_primary_program AS SELECT * FROM `v_primaryprogram`;

DROP TABLE IF EXISTS `cached_v_secondary_program`;
CREATE TABLE cached_v_secondary_program AS SELECT * FROM `v_secondaryprogram`;

DROP TABLE IF EXISTS `cached_v_national_program`;
CREATE TABLE cached_v_national_program AS SELECT * FROM `v_nationalobjectives_level_1`;

DROP TABLE IF EXISTS `cached_v_sub_sector`;
CREATE TABLE cached_v_sub_sector AS SELECT * FROM `v_sub_sectors`;

DROP TABLE IF EXISTS `cached_v_primary_sector`;
CREATE TABLE `cached_v_primary_sector` AS SELECT * FROM `v_sectors`;

DROP TABLE IF EXISTS `cached_v_secondary_sector`;
CREATE TABLE `cached_v_secondary_sector` AS SELECT * FROM `v_secondary_sectors`;

DROP TABLE IF EXISTS `cached_v_sec_sub_sector`;
CREATE TABLE cached_v_sec_sub_sector AS SELECT * FROM `v_secondary_sub_sectors`;

CREATE OR REPLACE VIEW `v_donor_funding_cached` AS select `f`.`amp_activity_id` AS `amp_activity_id`,`f`.`amp_funding_id` AS `amp_funding_id`,`fd`.`amp_fund_detail_id` AS `amp_fund_detail_id`,`d`.`name` AS `donor_name`,`fd`.`transaction_type` AS `transaction_type`,`fd`.`adjustment_type` AS `adjustment_type`,`fd`.`transaction_date` AS `transaction_date`,`fd`.`transaction_amount` AS `raw_amount`,((((((((((`fd`.`transaction_amount` * coalesce(`rc`.`location_percentage`, 100)) / 100) * coalesce(`pp`.`program_percentage`, 100)) / 100) * coalesce(`sp`.`program_percentage`, 100)) / 100) * coalesce(`np`.`program_percentage`, 100)) / 100) * `sa`.`sector_percentage`) /100) AS `transaction_amount`,`c`.`currency_code` AS `currency_code`,`cval`.`id` AS `terms_assist_id`,`cval`.`category_value` AS `terms_assist_name`,`fd`.`fixed_exchange_rate` AS `fixed_exchange_rate`,`b`.`org_grp_name` AS `org_grp_name`,`ot`.`org_type` AS `donor_type_name`,`cval2`.`category_value` AS `financing_instrument_name`,`cval2`.`id` AS `financing_instrument_id`,`d`.`amp_org_id` AS `org_grp_id`,`ot`.`amp_org_type_id` AS `org_type_id`,`fd`.`disbursement_order_rejected` AS `disb_ord_rej`,`getSectorName`(`getParentSectorId`(`s`.`amp_sector_id`)) AS `p_sectorname`,`getParentSectorId`(`s`.`amp_sector_id`) AS `amp_sector_id`,`sa`.`sector_percentage` AS `sector_percentage`,`ss`.`sec_scheme_name` AS `sec_scheme_name`,`cc`.`name` AS `classification_name`,`cc`.`classification_id` AS `classification_id`,`ss`.`amp_sec_scheme_id` AS `amp_sec_scheme_id`,`sa`.`classification_config_id` AS `sec_act_id`,`cc`.`id` AS `cc_id`,`s`.`sector_code` AS `sector_code`,`rc`.`Region` AS `region`,`pp`.`name` AS `primary_program_name`,`sp`.`name` AS `secondary_program_name`,`np`.`name` AS `national_program_name`,`psub`.`sectorname` AS `p_sub_sector_name` from ((((((((((((((((`amp_funding` `f` join `amp_funding_detail` `fd` on ((`f`.`amp_funding_id` = `fd`.`AMP_FUNDING_ID`)))join `amp_category_value` `cval`)join `amp_currency` `c`)left join `amp_organisation` `d` on ((`f`.`amp_donor_org_id` = `d`.`amp_org_id`)))join `amp_org_group` `b`)join `amp_org_type` `ot`)join `amp_category_value` `cval2`)join `amp_activity_sector` `sa` on ((`f`.`amp_activity_id` = `sa`.`amp_activity_id`)))join `amp_sector` `s` on ((`sa`.`amp_sector_id` = `s`.`amp_sector_id`)))join `amp_sector_scheme` `ss`)join `amp_classification_config` `cc`)join `cached_v_regions` `rc` on ((`f`.`amp_activity_id` = `rc`.`amp_activity_id`)))left join `cached_v_primary_program` `pp` on ((`f`.`amp_activity_id` =`pp`.`amp_activity_id`)))left join `cached_v_secondary_program` `sp` on ((`f`.`amp_activity_id` =`sp`.`amp_activity_id`)))left join `cached_v_national_program` `np` on ((`f`.`amp_activity_id` =`np`.`amp_activity_id`)))left join `cached_v_sub_sector` `psub` on ((`s`.`amp_sector_id` = `psub`.`amp_sector_id`)))where ((`cval2`.`id` = `f`.`financing_instr_category_value`) and(`c`.`amp_currency_id` = `fd`.`amp_currency_id`) and (`f`.`amp_funding_id` = `fd`.`AMP_FUNDING_ID`) and (`cval`.`id` = `f`.`type_of_assistance_category_va`) and (`d`.`amp_org_id` = `f`.`amp_donor_org_id`) and (`d`.`org_grp_id` = `b`.`amp_org_grp_id`) and (`ot`.`amp_org_type_id` = `d`.`org_type_id`) and (`cc`.`name` = _latin1'Primary') and (`sa`.`classification_config_id` = `cc`.`id`) and (`cc`.`classification_id` = `ss`.`amp_sec_scheme_id`)) group by `fd`.`transaction_type`,`fd`.`amp_fund_detail_id`,`f`.`amp_funding_id`,`fd`.`adjustment_type`,`getParentSectorId`(`s`.`amp_sector_id`),`f`.`amp_activity_id`,`cc`.`id`,`sa`.`classification_config_id`,`ss`.`amp_sec_scheme_id`,`cc`.`classification_id`,`cc`.`name`,`ss`.`sec_scheme_name`,`fd`.`disbursement_order_rejected`,`ot`.`amp_org_type_id`,`d`.`amp_org_id`,`cval2`.`id`,`cval2`.`category_value`,`ot`.`org_type`,`b`.`org_grp_name`,`fd`.`fixed_exchange_rate`,`cval`.`category_value`,`cval`.`id`,`c`.`currency_code`,`sa`.`sector_percentage`,`fd`.`transaction_amount`,`fd`.`transaction_date`,`d`.`name`,`s`.`sector_code`,`rc`.`region_id`,`pp`.`name`,`pp`.`amp_program_id`,`sp`.`amp_program_id`,`np`.`amp_program_id`,`psub`.`amp_sector_id`,`psub`.`sectorname` order by `f`.`amp_activity_id`,`getSectorName`(`getParentSectorId`(`sa`.`amp_sector_id`)),`fd`.`transaction_type`,`f`.`amp_funding_id`;