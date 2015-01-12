CREATE OR REPLACE VIEW `v_regional_funding` AS select 
`f`.`activity_id` AS `amp_activity_id`,
`f`.`amp_regional_funding_id` AS `amp_regional_funding_id`,
`f`.`amp_regional_funding_id` AS `amp_fund_detail_id`,
`r`.`name` AS `region_name`,
`f`.`transaction_type` AS `transaction_type`,
`f`.`adjustment_type` AS `adjustment_type`,
date_format(`f`.`transaction_date`,_latin1'%Y-%m-%d') AS `transaction_date`,
`f`.`transaction_amount` AS `transaction_amount`,`c`.`currency_code` AS `currency_code`,
`getExchange`(`c`.`currency_code`,`f`.`transaction_date`) AS `exchange_rate`,
p.code as perspective_code
from amp_perspective p,((`amp_regional_funding` `f` join `amp_region` `r`) join `amp_currency` `c`) where 
p.amp_perspective_id=f.perspective_id and
((`c`.`amp_currency_id` = `f`.`currency_id`) and (`f`.`region_id` = `r`.`amp_region_id`)) order by `f`.`activity_id`