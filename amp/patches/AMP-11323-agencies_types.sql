 CREATE OR REPLACE VIEW v_executing_agency_type AS
 SELECT f.activity AS amp_activity_id, ot.org_type AS name, b.org_type
 FROM amp_org_role f, amp_organisation o, amp_role r, amp_org_group b, amp_org_type ot
 WHERE f.organisation = o.amp_org_id AND f.role = r.amp_role_id AND r.role_code='EA' AND o.org_grp_id = b.amp_org_grp_id AND b.org_type = ot.amp_org_type_id
 ORDER BY f.activity, o.name;
  
 CREATE OR REPLACE VIEW v_implementing_agency_type AS
 SELECT f.activity AS amp_activity_id, ot.org_type AS name, b.org_type
 FROM amp_org_role f, amp_organisation o, amp_role r, amp_org_group b, amp_org_type ot
 WHERE f.organisation = o.amp_org_id AND f.role = r.amp_role_id AND r.role_code='IA' AND o.org_grp_id = b.amp_org_grp_id AND b.org_type = ot.amp_org_type_id
 ORDER BY f.activity, o.name;
 
 INSERT INTO amp_columns (columnName,aliasName,cellType,extractorView) VALUES 
 ('Executing Agency Type','executing_agency_type','org.dgfoundation.amp.ar.cell.TextCell','v_executing_agency_type'),
 ('Implementing Agency Type','implementing_agency_type','org.dgfoundation.amp.ar.cell.TextCell','v_implementing_agency_type');