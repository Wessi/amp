use amp_testing;

/* set up variables */
SET @import_time:=unix_timestamp();
SET @approved='approved';
SET @team_id=24; /* this is VIPFE Team */
SET @activity_creator=92;  /* this is ATL member */
SET @activity_code='AMP-BOLIVIA';
SET @commitment=0;
SET @disbursment=1;
SET @funding_adjusment_planned=0;
SET @funding_adjusment_actual=1;
SET @org_role_code='MA';
SET @funding_perspective=2;
SET @funding_currency_id=21;
SET @max_order_no=0;
SET @max_level_order_no=0;
SET @funding_modality=3;
SET @financing_instrument=116; /*  This should be removed becase now we have correct query */
SET @status_class_id=6;
select @status_class_id:=c.id from amp_category_class c where c.keyName='activity_status'; 
SET @level_class_id=7;
select @level_class_id:=c.id from amp_category_class c where c.keyName='implementation_level'; 
select @max_order_no:=max(a.index_column) FROM AMP_CATEGORY_VALUE a WHERE a.amp_category_class_id=@status_class_id;
select @max_level_order_no:=max(a.index_column) FROM AMP_CATEGORY_VALUE a WHERE a.amp_category_class_id=@level_class_id;
SET @timestmp:=unix_timestamp();
SET @avtivity_desc='aim-desc-import-';
SET @avtivity_obj='aim-obj-import-';
SET @editor_order=0; /*  This is needed for KALOSHA's import tool, just should not be empty cos it is primitive int */
SET @amp_site_id = 'amp';
SET @subprog_prefix='EBRP';
select @import_time;

ALTER TABLE AMP_ORGANISATION
ADD COLUMN old_id varchar(255),
ADD INDEX (old_id);

ALTER TABLE AMP_CATEGORY_VALUE
ADD COLUMN old_id varchar(30),
ADD INDEX (old_id);

ALTER TABLE AMP_LEVEL
ADD COLUMN old_id varchar(30),
ADD INDEX (old_id);

ALTER TABLE AMP_ACTIVITY
ADD COLUMN old_id bigint(20),
ADD COLUMN old_status_id varchar(20),
ADD INDEX (old_id),
ADD INDEX (old_status_id),
ADD INDEX (amp_id);


ALTER TABLE AMP_SECTOR
ADD COLUMN old_id varchar(20),
ADD INDEX (old_id);

ALTER TABLE amp_terms_assist
ADD COLUMN old_id varchar(20),
ADD INDEX (old_id);

alter table amp_region
ADD INDEX (region_code);

SET AUTOCOMMIT = 0;
START TRANSACTION;

/*importing sectors */

select 'importing sector shceme'; 

insert into AMP_SECTOR_SCHEME (sec_scheme_code, sec_scheme_name)
Values ('BOL_IMP', 'Bolivia Import');

update AMP_GLOBAL_SETTINGS set settingsValue =  LAST_INSERT_ID()
where settingsName = 'Default Sector Scheme';

select 'importing sectors'; 

insert into AMP_SECTOR (amp_sec_scheme_id,  sector_code,   name,   old_id)
select sch.amp_sec_scheme_id,   c.codsec,   c.descsec, c.codsec    
from bolivian_db.sec c, AMP_SECTOR_SCHEME sch 
where sch.sec_scheme_code='BOL_IMP' ;
 

/*  importing sectors, which are colled components in bolivia */
select 'importing schema fro components (sectors)';

INSERT INTO AMP_SECTOR_SCHEME (sec_scheme_code, sec_scheme_name)
VALUES ('BOL_COMPO_IMP', 'Components');

select 'importing components (sectors)';

INSERT INTO AMP_SECTOR (amp_sec_scheme_id,  sector_code,   name)
SELECT  sch.amp_sec_scheme_id,  c.valdato, c.interp   
FROM bolivian_db.claves AS c,  AMP_SECTOR_SCHEME AS sch 
WHERE sch.sec_scheme_code='BOL_COMPO_IMP'  AND c.nomdato='cvetipcomp';


/* import organizations */

select 'importing organisations'; 

INSERT INTO AMP_ORGANISATION
(
old_id,
name,
org_code,
acronym
)
SELECT
codage,
nomage,
codage,
codage
FROM bolivian_db.`age` as o;

/* setting up organization types */
select 'update AMP_ORGANISATION MUL'; 

UPDATE AMP_ORGANISATION AS org, bolivian_db.`age` AS o, AMP_ORG_TYPE AS t 
SET org.org_type_id=t.amp_org_type_id
WHERE org.old_id=o.codage AND o.cvebimulti='M' AND t.org_type_code='MUL';

select 'update AMP_ORGANISATION BIL'; 

UPDATE AMP_ORGANISATION AS org, bolivian_db.`age` AS o, AMP_ORG_TYPE AS t 
SET org.org_type_id=t.amp_org_type_id
WHERE org.old_id=o.codage AND o.cvebimulti='B' AND t.org_type_code='BIL';

select 'update AMP_ORGANISATION GROUP'; 

UPDATE AMP_ORGANISATION AS org, bolivian_db.`age` AS o, AMP_ORG_GROUP AS aog
SET org.org_grp_id=aog.amp_org_grp_id
WHERE org.old_id=o.codage and aog.org_grp_code = o.cveorg;

select 'importing executing Agencies';

INSERT INTO AMP_ORGANISATION
(
old_id,
name,
org_code,
acronym,
org_type_id
)
SELECT
codent,
noment,
codent,
codent,
tt.amp_org_type_id
FROM bolivian_db.`ent` as e,  amp_org_type as tt
where  tt.org_type_code='OTHER' and not exists (select a.codage from bolivian_db.`age` as a where a.codage=e.codent);


/* importing activity statuses */

/*INSERT INTO AMP_CATEGORY_VALUE (old_id, amp_category_class_id,category_value, index_column)
SELECT c.valdato, @status_class_id, c.interp , @max_order_no:=@max_order_no+1 
FROM bolivian_db.`claves` as c 
WHERE c.nomdato='statconv' ;*/ 


/* importing implementation levels */
/*INSERT INTO AMP_CATEGORY_VALUE (old_id, amp_category_class_id,category_value, index_column)
SELECT lvl.valdato, @level_class_id, lvl.interp , @max_level_order_no:=@max_level_order_no+1 
FROM bolivian_db.`claves` as lvl 
WHERE lvl.nomdato='cvealc' ;  */ 

/*INSERT INTO AMP_LEVEL (level_code,name,old_id)
SELECT lvl.valdato, lvl.interp, lvl.valdato
FROM bolivian_db.`claves` lvl
WHERE lvlnomdato='cvealc'; */

/* terms and assist */
select 'amp_terms_assist'; 

INSERT INTO amp_terms_assist(terms_assist_code, terms_assist_name,old_id)
SELECT lvl.valdato, lvl.interp, lvl.valdato
FROM bolivian_db.`claves` lvl
WHERE lvl.nomdato='cvecoop';

/* import activities */
select 'inserting into AMP_ACTIVITY fom conv';

INSERT INTO AMP_ACTIVITY 
(
old_id, 
amp_id,
name, 
description, 
objectives,
contractors, 
program_description,
`condition`, 
status_reason, 
proposed_approval_date,
proposed_start_date,
actual_completion_date,
actual_start_date,
amp_team_id,
approval_status,
proj_cost_amount,
activity_creator,
totalCost,
old_status_id

)
SELECT 
c.numconv,
c.numconv, 
c.nomconv,
concat(@avtivity_desc, @timestmp:=@timestmp+1),
concat(@avtivity_obj, @timestmp:=@timestmp+1),
' ',
' ',
' ',
' ',
c.fechprogefec,
c.fechprogprdes,
c.fechproguldes,
c.fechcont,
@team_id,
@approved,
montorig,
@activity_creator,
montous,
cvealc
  
FROM  bolivian_db.`conv` as c ;
/* where c.STATCONV!='D' and c.STATCONV!='C' and c.STATCONV!='A' and c.STATCONV!='5'; */  


/* mapping contacts */
select 'mapping contacts';

update amp_activity as a, bolivian_db.`usu` as u, bolivian_db.`conv` c 
set a.mofed_cnt_last_name=u.nombreusuario
where a.old_id=c.numconv and c.codusu=u.codusu;


/* mapping executing agencies (organizations) to activities */
select 'mapping executing agencies to activities';


insert into amp_org_role
(
activity,
organisation,
role
)
select a.amp_activity_id, o.amp_org_id, ar.amp_role_id 
from  bolivian_db.`conv_entejec` as m, amp_activity as a, amp_organisation as o , amp_role as ar
where ar.role_code='EA' 
and a.old_id=m.numconv 
and o.old_id = m.codentejec 
and not exists (select ag.codage from bolivian_db.age as ag where ag.codage=o.old_id);

/* importing issues */
select 'importing issues 1';

INSERT INTO AMP_ISSUES(name, amp_activity_id)
SELECT c.sit_actual, a.amp_activity_id  
FROM bolivian_db.`conv` AS c, amp_activity AS a 
WHERE (c.sit_actual is not null) AND c.numconv=a.old_id; 

select 'importing issues 2';

insert into amp_issues(name,amp_activity_id)
SELECT c.tramite_actual, a.amp_activity_id 
FROM bolivian_db.`conv` c, amp_activity a 
where (c.tramite_actual is not null) and c.numconv=a.old_id; 

select 'importing issues 3';

insert into amp_issues(name,amp_activity_id)
SELECT c.Tip_ejecucion, a.amp_activity_id 
FROM bolivian_db.`conv` c, amp_activity a 
where (c.Tip_ejecucion is not null) and c.numconv=a.old_id ;

select 'importing issues 4';

insert into amp_issues(name,amp_activity_id)
SELECT c.marca, a.amp_activity_id 
FROM bolivian_db.`conv` c, amp_activity a 
where (c.marca is not null) and c.numconv=a.old_id; 


/* mapping activity and sectors */
select 'mapping activity and sectors';

insert into amp_activity_sector
(amp_activity_id,amp_sector_id,sector_percentage)
select  act.amp_activity_id, sec.amp_sector_id,  ac.porcsec 
from AMP_ACTIVITY as act, AMP_SECTOR as sec, bolivian_db.conv_sec as ac   
where act.old_id=ac.numconv and sec.old_id=ac.codsec ;

/* mapping activity and statuses */ 
select 'mapping activity and statuses';

INSERT INTO amp_activities_categoryvalues (amp_activity_id, amp_categoryvalue_id) 
SELECT act.amp_activity_id, cat.id 
FROM AMP_ACTIVITY as act,    AMP_CATEGORY_VALUE as cat,    bolivian_db.`conv` acto, bolivian_db.`claves` as cla   
WHERE cat.amp_category_class_id=@status_class_id and cat.category_value=cla.interp and acto.numconv=act.old_id 
and acto.statconv=cla.valdato and cla.nomdato='statconv'; 

/*
FROM AMP_ACTIVITY as act,    AMP_CATEGORY_VALUE as cat,    bolivian_db.`conv` co, bolivian_db.`claves` as cla   
WHERE co.numconv=act.old_id and co.statconv=cat.old_id and cla; 
*/

/* mapping implementation levels */
select 'mapping implementation levels';

INSERT INTO amp_activities_categoryvalues (amp_activity_id, amp_categoryvalue_id) 
SELECT act.amp_activity_id, cat.id 
FROM AMP_ACTIVITY as act,    AMP_CATEGORY_VALUE as cat,    bolivian_db.`conv` acto, bolivian_db.`claves` as cla   
WHERE cat.amp_category_class_id=@level_class_id and cat.category_value=cla.interp and acto.numconv=act.old_id 
and acto.cvealc=cla.valdato and cla.nomdato='cvealc'; 

/*INSERT INTO amp_activities_categoryvalues (amp_activity_id, amp_categoryvalue_id) 
SELECT act.amp_activity_id, cat.id FROM AMP_ACTIVITY as act, AMP_CATEGORY_VALUE as cat, bolivian_db.`conv` co  
WHERE co.numconv=act.old_id and co.cvealc=cat.old_id;*/ 


/* Finncial Instrument */
select 'mapping implementation levels';
/*INSERT INTO amp_activities_categoryvalues (amp_activity_id, amp_categoryvalue_id) 
SELECT act.amp_activity_id, @financing_instrument 
FROM AMP_ACTIVITY as act,   bolivian_db.`conv` acto    
WHERE acto.numconv=act.old_id  ;*/



/*UPDATE AMP_ACTIVITY AS a, AMP_LEVEL AS il, bolivian_db.`conv` c
SET  a.amp_level_id = il.amp_level_id
WHERE a.old_id = c.numconv AND c.cvealc=il.old_id;*/
 
/* mapping descriptions for english*/
select 'mapping descriptions for english';

INSERT INTO DG_EDITOR 
(EDITOR_KEY, LANGUAGE, SITE_ID, BODY,LAST_MOD_DATE,CREATION_IP,ORDER_INDEX)
SELECT a.description, 'en', @amp_site_id ,c.descconv,now() ,'127.0.0.1',@editor_order 
FROM bolivian_db.`conv` AS c, AMP_ACTIVITY AS a
WHERE c.numconv=a.old_id ;

/* mapping descriptions for spanish*/
select 'mapping descriptions for spanish';

INSERT INTO DG_EDITOR 
(EDITOR_KEY, LANGUAGE, SITE_ID, BODY,LAST_MOD_DATE,CREATION_IP,ORDER_INDEX)
SELECT a.description, 'es', @amp_site_id ,c.descconv,now() ,'127.0.0.1',@editor_order 
FROM bolivian_db.`conv` AS c, AMP_ACTIVITY AS a
WHERE c.numconv=a.old_id ;



/* mapping fundings activities and organizations */
select 'mapping fundings activities and organizations';

INSERT into AMP_FUNDING (
financing_id,
amp_donor_org_id,
amp_activity_id, 
type_of_assistance_category_value_id,
amp_modality_id,
financing_instr_category_value_id)
SELECT 
o.codage,
org.amp_org_id ,  
a.amp_activity_id,  
catval.id, 
@funding_modality,
111 
FROM bolivian_db.`conv` AS c,  
AMP_ACTIVITY AS a,  
AMP_ORGANISATION AS org,  
bolivian_db.`age` AS o,  
bolivian_db.`claves` AS ta,  
amp_category_value AS catval   
WHERE c.numconv=a.old_id AND org.old_id=o.codage 
AND c.codage=org.old_id AND ta.nomdato='cvecoop' 
AND ta.valdato=c.cvecoop AND catval.category_value=ta.interp 
AND catval.amp_category_class_id=10;


/* importing currencies */
select 'importing currencies (initial) fundings';
INSERT INTO amp_currency(
currency_code,
country_name,
currency_name,
active_flag)
SELECT sigla_mda, moneda, pais, 1
FROM bolivian_db.clasif_moneda
where sigla_mda not in (select currency_code from amp_currency);


/* importing planned fundings */
select 'importing planned (initial) fundings';

INSERT INTO AMP_FUNDING_DETAIL 
(
adjustment_type, 
transaction_type,  
transaction_date, 
transaction_amount,
org_role_code,
perspective_id,
AMP_FUNDING_ID,
amp_currency_id,
fixed_exchange_rate
)
SELECT
@funding_adjusment_planned, 
@commitment, 
enm.fechvigenm,
enm.montorig,
@org_role_code,
@funding_perspective,
f.amp_funding_id,
cu.amp_currency_id,
enm.tipcam
FROM bolivian_db.`enm` as enm, AMP_ACTIVITY as a, AMP_FUNDING as f, amp_currency as cu
WHERE  a.old_id=enm.numconv 
and a.amp_activity_id=f.amp_activity_id 
and enm.cvemonorig=cu.currency_code 
and enm.numenm=0 
and enm.fechvigenm is not null;

/* mapping planned fundings but without dates, in this case we are using date from activity */
INSERT INTO AMP_FUNDING_DETAIL 
(
adjustment_type, 
transaction_type,  
transaction_date, 
transaction_amount,
org_role_code,
perspective_id,
AMP_FUNDING_ID,
amp_currency_id,
fixed_exchange_rate
)
SELECT
@funding_adjusment_planned, 
@commitment, 
a.actual_start_date,
enm.montorig,
@org_role_code,
@funding_perspective,
f.amp_funding_id,
cu.amp_currency_id,
enm.tipcam
FROM bolivian_db.`enm` as enm, AMP_ACTIVITY as a, AMP_FUNDING as f, amp_currency as cu 
WHERE  a.old_id=enm.numconv 
and a.actual_start_date not like '0000-00-00%'  
and a.actual_start_date is not null 
and a.amp_activity_id=f.amp_activity_id 
and enm.cvemonorig=cu.currency_code 
and enm.numenm=0 
and enm.fechvigenm is null;
/*  THIS IS TEMPORARY WORKAROUND becaue some activity-funding pairs have empty values */



/* mapping additional commintments from ENM table.*/ 
select 'mapping additional commintments from ENM table.';

INSERT INTO AMP_FUNDING_DETAIL 
(
adjustment_type, 
transaction_type,  
transaction_date, 
transaction_amount,
org_role_code,
perspective_id,
AMP_FUNDING_ID,
amp_currency_id,
fixed_exchange_rate
)
SELECT
@funding_adjusment_actual, 
@commitment, 
enm.fechvigenm,
enm.montorig,
@org_role_code,
@funding_perspective,
f.amp_funding_id,
cu.amp_currency_id,
enm.tipcam
FROM bolivian_db.`enm` as enm, AMP_ACTIVITY as a, AMP_FUNDING as f, amp_currency as cu
WHERE  a.old_id=enm.numconv 
and a.amp_activity_id=f.amp_activity_id 
and enm.cvemonorig=cu.currency_code 
and enm.numenm>0 
and enm.montorig!=0 
and enm.fechvigenm is not null;

select 'importing actual disbursments';
INSERT INTO AMP_FUNDING_DETAIL 
(
adjustment_type, 
transaction_type,  
transaction_date, 
transaction_amount,
org_role_code,
perspective_id,
AMP_FUNDING_ID,
amp_currency_id,
fixed_exchange_rate
)
SELECT
@funding_adjusment_actual, 
@disbursment, 
dsm.fechdesem,
dsm.montorig,
@org_role_code,
@funding_perspective,
f.amp_funding_id,
cu.amp_currency_id,
dsm.tipcam
FROM bolivian_db.`desem` as dsm, AMP_ACTIVITY as a, AMP_FUNDING as f, amp_currency as cu
WHERE  a.old_id=dsm.numconv 
and a.amp_activity_id=f.amp_activity_id 
and dsm.cvemonorig=cu.currency_code
and lower(dsm.tipdesem)='e'; 


select 'importing planned disbursments';
INSERT INTO AMP_FUNDING_DETAIL 
(
adjustment_type, 
transaction_type,  
transaction_date, 
transaction_amount,
org_role_code,
perspective_id,
AMP_FUNDING_ID,
amp_currency_id,
fixed_exchange_rate
)
SELECT
@funding_adjusment_planned, 
@disbursment, 
dsm.fechdesem,
dsm.montorig,
@org_role_code,
@funding_perspective,
f.amp_funding_id,
cu.amp_currency_id,
dsm.tipcam
FROM bolivian_db.`desem` as dsm, AMP_ACTIVITY as a, AMP_FUNDING as f, amp_currency as cu
WHERE  a.old_id=dsm.numconv 
and a.amp_activity_id=f.amp_activity_id 
and dsm.cvemonorig=cu.currency_code
and lower(dsm.tipdesem)='p'; 




select 'correcting invalid dates in fundings';

update amp_funding_detail 
set transaction_date='2011-01-01 01:01:01'
where transaction_date is null or transaction_date like '0000-00-00%';



/* correcting activity dates */
select 'correcting 0000-00-00 dates in activities';

update amp_activity
set actual_start_date=null
where actual_start_date='0000-00-00 00:00:00'; 

update amp_activity
set proposed_start_date=null
where proposed_start_date='0000-00-00 00:00:00';  

update amp_activity
set proposed_approval_date=null
where proposed_approval_date='0000-00-00 00:00:00';  

update amp_activity
set actual_approval_date=null
where actual_approval_date='0000-00-00 00:00:00'; 

update amp_activity
set actual_completion_date=null
where actual_completion_date='0000-00-00 00:00:00'; 


/*  ==Type of credit==  - maps to  Financing Instrument in funding.*/

select '==type of credit==';

/* removing old values, we are going to replace financing instrument meaning, it will be called Type of credit for Bolivia */
select 'remove previous category values for financing instrument';

DELETE catval FROM amp_category_value AS catval, amp_category_class AS catclass
where catval.amp_category_class_id=catclass.id  AND catclass.keyName ='financing_instrument'; 

/*  importing new values: there are just 3 records*/
select 'iporting new category values for Type of Credit';

SET @temp_cat_val=-1;

INSERT INTO amp_category_value
(category_value,amp_category_class_id,index_column)
SELECT cla.interp, catclass.id, @temp_cat_val:=@temp_cat_val+1 FROM amp_category_class AS catclass, bolivian_db.`claves` AS cla 
WHERE cla.nomdato='cvecred' AND catclass.keyName='financing_instrument';

/* mapping credit types to activities */
select 'mapping credit types to activity fundings';

UPDATE amp_activity AS act, bolivian_db.`conv` AS con, amp_category_value AS catval, amp_category_class AS catclass, bolivian_db.`claves` AS cla, amp_funding AS fnd
SET fnd.financing_instr_category_value_id=catval.id    
WHERE cla.nomdato='cvecred' 
AND catclass.keyName='financing_instrument' 
AND cla.interp=catval.category_value 
AND act.old_id=con.numconv 
AND con.cvecred=cla.valdato
AND fnd.amp_activity_id=act.amp_activity_id;

/* ==Regions==*/
select 'importin regions';

insert into amp_region
(name, country_id, region_code)
select interp, 'bo', valdato from bolivian_db.`claves` as c 
where c.nomdato='cvedep';

select 'inserting locations';

insert into amp_location
(name,country,region,country_id,region_id)
select r.name, 'Bolivia', r.name, 'bo', r.amp_region_id from amp_region r, bolivian_db.`claves` as c
where c.nomdato='cvedep' and r.region_code=c.valdato;

select 'mapping locations to activities';

insert into amp_activity_location
(amp_activity_id, amp_location_id,location_percentage)
select act.amp_activity_id, loc.amp_location_id, condep.porcdep 
from amp_activity as act, 
amp_location as loc, 
amp_region as reg, 
bolivian_db.`claves` as c, 
bolivian_db.`conv_dep` as condep
where 
act.amp_id=condep.numconv 
and loc.region_id=reg.amp_region_id 
and reg.region_code=c.valdato
and c.nomdato='cvedep'
and condep.cvedep=c.valdato;


/*  mapping components (sectors) */

select ' mapping components (sectors)';
INSERT INTO amp_activity_componente (amp_activity_id, amp_sector_id, percentage)
SELECT act.amp_activity_id, sec.amp_sector_id, bcomp.porccomp 
FROM amp_sector AS sec, amp_sector_scheme AS sch, bolivian_db.comp AS bcomp, bolivian_db.`conv` AS con, amp_activity AS act
WHERE sec.amp_sec_scheme_id=sch.amp_sec_scheme_id  
AND sch.sec_scheme_code='BOL_COMPO_IMP'  
AND act.amp_id=con.numconv 
AND bcomp.numconv=con.numconv 
AND bcomp.cvetipcomp=sec.sector_code;

/* mapping activities and themes(programs) */ 

select 'mapping activities and themes(programs)';

INSERT INTO amp_activity_theme
(amp_activity_id,amp_theme_id)
select act.amp_activity_id, prog.amp_theme_id 
from  amp_activity as act, amp_theme as prog, bolivian_db.`conv` as acto 
where act.old_id=acto.numconv and prog.theme_code = concat('EBRP', substring(acto.Cod_EBRP,2));

insert INTO amp_activity_program
(amp_activity_id,amp_program_id,program_percentage,program_setting)
select  act.amp_activity_id, prog.amp_theme_id, 100, progset.amp_program_settings_id 
from amp_activity as act, amp_theme as prog, amp_program_settings as progset, bolivian_db.`conv` as con  
where act.amp_id=con.numconv
and prog.theme_code = concat('EBRP', substring(con.Cod_EBRP,2))
and progset.name like 'National Plan Objective';


COMMIT;

/* REMOVE TEMPORARY INDEXES */
DROP INDEX old_id ON amp_terms_assist;

DROP INDEX old_id ON  AMP_SECTOR;

DROP INDEX old_id ON AMP_ORGANISATION;

DROP INDEX old_id ON AMP_CATEGORY_VALUE;

DROP INDEX old_status_id ON AMP_ACTIVITY;
DROP INDEX old_id ON AMP_ACTIVITY;

DROP INDEX old_id ON AMP_LEVEL;

DROP INDEX region_code on amp_region;

/*removing temporary columnes  */
ALTER TABLE amp_terms_assist
DROP COLUMN old_id;

ALTER TABLE AMP_SECTOR
DROP COLUMN old_id;

ALTER TABLE AMP_ORGANISATION
DROP COLUMN old_id;

ALTER TABLE AMP_CATEGORY_VALUE
DROP COLUMN old_id;

ALTER TABLE AMP_ACTIVITY
DROP COLUMN old_status_id,
DROP COLUMN old_id;


ALTER TABLE AMP_LEVEL
DROP COLUMN old_id;
/* end */
