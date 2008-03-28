 CREATE OR REPLACE VIEW `v_credit_donation` AS
	SELECT amp_activity_id AS `amp_activity_id`,
	(case when name = 'Donaci�n' then 'Donaci�n' else 'Credito' end) AS `name`,
	(case when name = 'Donaci�n' then 0 else 1 end) AS `id`
	FROM v_financing_instrument v;


