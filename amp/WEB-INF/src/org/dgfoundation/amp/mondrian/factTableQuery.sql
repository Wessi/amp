INSERT INTO mondrian_fact_table (entity_type, entity_id, entity_internal_id, transaction_type, adjustment_type, transaction_date, date_code, transaction_amount, 
  currency_id, donor_id, financing_instrument_id, terms_of_assistance_id, primary_sector_id, secondary_sector_id, tertiary_sector_id, location_id,
  primary_program_id, secondary_program_id, tertiary_program_id, national_objectives_program_id,
  ea_org_id, ba_org_id, ia_org_id, ro_org_id, src_role_id, dest_role_id, dest_org_id)
  SELECT 
	'A' as entity_type,
	rawdonation.amp_activity_id AS entity_id,
	rawdonation.amp_fund_detail_id AS entity_internal_id,
    rawdonation.transaction_type AS transaction_type,
    rawdonation.adjustment_type AS adjustment_type,
    rawdonation.transaction_date AS transaction_date,
    rawdonation.date_code AS date_code,

	rawdonation.transaction_amount * (
         COALESCE(location.percentage, 1) *
         COALESCE(prim_prog.percentage, 1) *
         COALESCE(sec_prog.percentage, 1) *
		 COALESCE(tert_prog.percentage, 1) *
         COALESCE(npo_prog.percentage, 1) *
         COALESCE(prim_sect.percentage, 1) *
		 COALESCE(sec_sect.percentage, 1) *
         COALESCE(tert_sect.percentage, 1) *
         COALESCE(ra.percentage, 1) *
         COALESCE(ba.percentage, 1) *
         COALESCE(ia.percentage, 1) *
         COALESCE(ea.percentage, 1)
         ) AS transaction_amount,

     rawdonation.currency_id AS currency_id,
	 rawdonation.donor_id AS donor_id,
     rawdonation.financing_instrument_id AS financing_instrument_id,
     rawdonation.terms_of_assistance_id AS terms_of_assistance_id,

     COALESCE(prim_sect.ent_id, 999999999) AS primary_sector_id,
     COALESCE(sec_sect.ent_id, 999999999) AS secondary_sector_id,
     COALESCE(tert_sect.ent_id, 999999999) AS tertiary_sector_id,

     COALESCE(location.ent_id, 999999999) AS location_id,

     COALESCE(prim_prog.ent_id, 999999999) AS primary_program_id,
     COALESCE(sec_prog.ent_id, 999999999) AS secondary_program_id,
     COALESCE(tert_prog.ent_id, 999999999) AS tertiary_program_id,
     COALESCE(npo_prog.ent_id, 999999999) AS national_objectives_program_id,

     COALESCE(ea.ent_id, 999999999) AS ea_org_id,
     COALESCE(ba.ent_id, 999999999) AS ba_org_id,
     COALESCE(ia.ent_id, 999999999) AS ia_org_id,
     COALESCE(ra.ent_id, 999999999) AS ro_org_id,
     
     rawdonation.src_role_id AS src_role_id,
     rawdonation.dest_role_id AS dest_role_id,
     rawdonation.dest_org_id AS dest_org_id
          
	FROM mondrian_raw_donor_transactions rawdonation
    LEFT JOIN etl_activity_sector_primary prim_sect ON prim_sect.act_id = rawdonation.amp_activity_id
    LEFT JOIN etl_activity_sector_secondary sec_sect ON sec_sect.act_id = rawdonation.amp_activity_id
    LEFT JOIN etl_activity_sector_tertiary tert_sect ON tert_sect.act_id = rawdonation.amp_activity_id
    
    LEFT JOIN etl_activity_program_national_plan_objective npo_prog ON npo_prog.act_id = rawdonation.amp_activity_id
    LEFT JOIN etl_activity_program_primary_program prim_prog ON prim_prog.act_id = rawdonation.amp_activity_id
    LEFT JOIN etl_activity_program_secondary_program sec_prog ON sec_prog.act_id = rawdonation.amp_activity_id
    LEFT JOIN etl_activity_program_tertiary_program tert_prog ON tert_prog.act_id = rawdonation.amp_activity_id

    LEFT JOIN etl_locations location ON location.act_id = rawdonation.amp_activity_id

    LEFT JOIN etl_executing_agencies ea ON ea.act_id = rawdonation.amp_activity_id
    LEFT JOIN etl_beneficiary_agencies ba ON ba.act_id = rawdonation.amp_activity_id
    LEFT JOIN etl_implementing_agencies ia ON ia.act_id = rawdonation.amp_activity_id
    LEFT JOIN etl_responsible_agencies ra ON ra.act_id = rawdonation.amp_activity_id

order by rawdonation.amp_activity_id;
