CREATE OR REPLACE VIEW v_mtef_funding AS
select f.amp_activity_id, m.amp_fund_mtef_projection_id, m.amount, c.currency_code, m.projection_date as currency_date,
getExchange(c.currency_code,m.projection_date) AS exchange_rate, org.name as donor_name, grp.org_grp_name as org_grp_name, otype.org_type as donor_type_name,
  tos.category_value  as terms_assist_name, 
  fin.category_value as financing_instrument_name 
from amp_funding f, amp_funding_mtef_projection m, amp_currency c, amp_category_class acc, amp_category_value acv, amp_organisation org,
    amp_category_value tos, amp_category_value fin, amp_org_group grp, amp_org_type otype
where m.amp_funding_id=f.amp_funding_id and m.amp_currency_id=c.amp_currency_id and acv.amp_category_class_id=acc.id  
 AND f.amp_donor_org_id=org.amp_org_id AND tos.id=f.type_of_assistance_category_va AND fin.id=f.financing_instr_category_value
 AND grp.amp_org_grp_id=org.org_grp_id AND otype.amp_org_type_id=grp.org_type 
 and acc.keyName="mtef_projection" and acv.category_value="projection" and m.amp_projected_categoryvalue_id=acv.id;