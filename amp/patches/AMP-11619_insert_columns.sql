INSERT INTO amp_measures (measureName, aliasName, type) 
VALUES ('Planned Disbursements - Capital', 'Planned Disbursements - Capital', 'A');

INSERT INTO amp_measures (measureName, aliasName, type) 
VALUES ('Planned Disbursements - Expenditure', 'Planned Disbursements - Expenditure', 'A');

CREATE OR REPLACE VIEW `v_donor_funding` AS 
select `f`.`amp_activity_id` AS `amp_activity_id`,`f`.`amp_funding_id` AS `amp_funding_id`,
`fd`.`amp_fund_detail_id` AS `amp_fund_detail_id`,`d`.`name` AS `donor_name`, 
`fd`.`transaction_type` AS `transaction_type`,`fd`.`adjustment_type` AS `adjustment_type`, 
`fd`.`transaction_date` AS `transaction_date`,`fd`.`transaction_amount` AS `transaction_amount`, 
`fd`.`pledge_id` AS `pledge_id`,`c`.`currency_code` AS `currency_code`,`cval`.`id` AS `terms_assist_id`, 
`cval`.`category_value` AS `terms_assist_name`,`fd`.`fixed_exchange_rate` AS `fixed_exchange_rate`, 
`b`.`org_grp_name` AS `org_grp_name`,`ot`.`org_type` AS `donor_type_name`,`cval2`.`category_value` AS `financing_instrument_name`, 
`cval2`.`id` AS `financing_instrument_id`,`d`.`amp_org_id` AS `org_id`,`d`.`org_grp_id` AS `org_grp_id`,`ot`.`amp_org_type_id` AS `org_type_id`, 
`cval3`.`category_value` AS `mode_of_payment_name`,`cval3`.`id` AS `mode_of_payment_id`,`cval4`.`category_value` AS `funding_status_name`, 
`cval4`.`id` AS `funding_status_id`, f.capital_spend_percent 
from (((((((((`amp_funding` `f` join `amp_funding_detail` `fd`) 
join `amp_category_value` `cval`) join `amp_currency` `c`) 
join `amp_organisation` `d`) join `amp_org_group` `b`) 
join `amp_org_type` `ot`) join `amp_category_value` `cval2`) 
left join `amp_category_value` `cval3` on((`cval3`.`id` = `f`.`mode_of_payment_category_va`))) 
left join `amp_category_value` `cval4` on((`cval4`.`id` = `f`.`funding_status_category_va`))) 
where ((`cval2`.`id` = `f`.`financing_instr_category_value`) and (`c`.`amp_currency_id` = `fd`.`amp_currency_id`) 
and (`f`.`amp_funding_id` = `fd`.`AMP_FUNDING_ID`) and (`cval`.`id` = `f`.`type_of_assistance_category_va`) 
and (`d`.`amp_org_id` = `f`.`amp_donor_org_id`) and (`d`.`org_grp_id` = `b`.`amp_org_grp_id`) and 
(`ot`.`amp_org_type_id` = `b`.`org_type`)) order by `f`.`amp_activity_id`;
