DROP VIEW IF EXISTS `v_g_settings_sh_comp_fund_year`;
CREATE OR REPLACE VIEW `v_g_settings_sh_comp_fund_year` AS select `util_global_settings_possible_`.`value_id` AS `id`,`util_global_settings_possible_`.`value_shown` AS `value` from `util_global_settings_possible_` where (`util_global_settings_possible_`.`setting_name` = _utf8'Show Component Funding by Year');
update amp_global_settings set possibleValues='v_g_settings_sh_comp_fund_year' where settingsName = 'Show Component Funding by Year';
