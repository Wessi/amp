CREATE or replace  VIEW  `v_global_settings_feature_templates` AS select `amp_feature_templates`.`id` AS `id`,`amp_feature_templates`.`featureTemplateName` AS `value` from `amp_feature_templates` ;
INSERT into amp_global_settings(settingsName, settingsValue, possibleValues) values('Feature Template','1','v_global_settings_feature_templates'); 