delete from amp_fields_templates where field in (select id from amp_fields_visibility where parent in (select id from amp_features_visibility b where name = 'Messages'));
delete from amp_fields_visibility where parent in (select id from amp_features_visibility where name = 'Messages');
delete from amp_features_templates where feature in (select id from amp_features_visibility where name = 'Messages');
delete from amp_features_visibility  where name = 'Messages';
delete from amp_modules_templates where module in (select id from amp_modules_visibility where name = 'My Messages');
delete from amp_features_visibility where parent in ( select id from amp_modules_visibility where name = 'My Messages');
delete from amp_modules_visibility where name = 'My Messages';
