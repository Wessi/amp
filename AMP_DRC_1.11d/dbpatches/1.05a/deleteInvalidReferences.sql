DELETE S FROM AMP_ACTIVITY_SECTOR S LEFT OUTER JOIN AMP_ACTIVITY A
ON (S.AMP_ACTIVITY_ID=A.AMP_ACTIVITY_ID)
WHERE A.AMP_ACTIVITY_ID IS NULL;

DELETE L FROM AMP_ACTIVITY_LOCATION L LEFT OUTER JOIN AMP_ACTIVITY A
ON (L.AMP_ACTIVITY_ID=A.AMP_ACTIVITY_ID)
WHERE A.AMP_ACTIVITY_ID IS NULL;

DELETE L FROM AMP_ACTIVITY_INTERNAL_ID L LEFT OUTER JOIN AMP_ACTIVITY A
ON (L.AMP_ACTIVITY_ID=A.AMP_ACTIVITY_ID)
WHERE A.AMP_ACTIVITY_ID IS NULL;

DELETE L FROM AMP_MEMBER_ACTIVITIES L LEFT OUTER JOIN AMP_ACTIVITY A
ON (L.AMP_ACTIVITY_ID=A.AMP_ACTIVITY_ID)
WHERE A.AMP_ACTIVITY_ID IS NULL;

DELETE L FROM AMP_ACTIVITY_KM_DOCUMENTS L LEFT OUTER JOIN AMP_ACTIVITY A
ON (L.AMP_ACTIVITY_ID=A.AMP_ACTIVITY_ID)
WHERE A.AMP_ACTIVITY_ID IS NULL;

DELETE L FROM AMP_ORG_ROLE L LEFT OUTER JOIN AMP_ACTIVITY A
ON (L.ACTIVITY=A.AMP_ACTIVITY_ID)
WHERE A.AMP_ACTIVITY_ID IS NULL;

DELETE L FROM AMP_FUNDING L LEFT OUTER JOIN AMP_ACTIVITY A
ON (L.AMP_ACTIVITY_ID=A.AMP_ACTIVITY_ID)
WHERE A.AMP_ACTIVITY_ID IS NULL;


DELETE FD FROM AMP_FUNDING_DETAIL FD LEFT OUTER JOIN AMP_FUNDING F
ON (FD.AMP_FUNDING_ID=F.AMP_FUNDING_ID)
WHERE F.AMP_FUNDING_ID IS NULL;

DELETE FD FROM AMP_CLOSING_DATE_HISTORY FD LEFT OUTER JOIN AMP_FUNDING F
ON (FD.AMP_FUNDING_ID=F.AMP_FUNDING_ID)
WHERE F.AMP_FUNDING_ID IS NULL;

DELETE L FROM AMP_COMPONENTS L LEFT OUTER JOIN AMP_ACTIVITY A
ON (L.AMP_ACTIVITY_ID=A.AMP_ACTIVITY_ID)
WHERE A.AMP_ACTIVITY_ID IS NULL;

DELETE FD FROM AMP_PHYSICAL_PERFORMANCE FD LEFT OUTER JOIN AMP_COMPONENTS F
ON (FD.AMP_COMPONENT_ID=F.AMP_COMPONENT_ID)
WHERE F.AMP_COMPONENT_ID IS NULL;

DELETE FD FROM AMP_COMPONENT_FUNDING FD LEFT OUTER JOIN AMP_COMPONENTS F
ON (FD.AMP_COMPONENT_ID=F.AMP_COMPONENT_ID)
WHERE F.AMP_COMPONENT_ID IS NULL;

DELETE L FROM AMP_NOTES L LEFT OUTER JOIN AMP_ACTIVITY A
ON (L.AMP_ACTIVITY_ID=A.AMP_ACTIVITY_ID)
WHERE A.AMP_ACTIVITY_ID IS NULL;

DELETE L FROM AMP_ACTIVITY_CLOSING_DATES L LEFT OUTER JOIN AMP_ACTIVITY A
ON (L.AMP_ACTIVITY_ID=A.AMP_ACTIVITY_ID)
WHERE A.AMP_ACTIVITY_ID IS NULL;

DELETE L FROM AMP_ISSUES L LEFT OUTER JOIN AMP_ACTIVITY A
ON (L.AMP_ACTIVITY_ID=A.AMP_ACTIVITY_ID)
WHERE A.AMP_ACTIVITY_ID IS NULL;

DELETE L FROM AMP_MEASURE L LEFT OUTER JOIN AMP_ISSUES A
ON (L.AMP_ISSUE_ID=A.AMP_ISSUE_ID)
WHERE A.AMP_ISSUE_ID IS NULL;

DELETE L FROM AMP_ACTOR L LEFT OUTER JOIN AMP_MEASURE A
ON (L.AMP_MEASURE_ID=A.AMP_MEASURE_ID)
WHERE A.AMP_MEASURE_ID IS NULL;

DELETE L FROM AMP_REGIONAL_FUNDING L LEFT OUTER JOIN AMP_ACTIVITY A
ON (L.ACTIVITY_ID=A.AMP_ACTIVITY_ID)
WHERE A.AMP_ACTIVITY_ID IS NULL;

DELETE P FROM AMP_PAGES P LEFT OUTER JOIN AMP_REPORTS R
ON (P.PAGE_NAME=R.NAME) 
WHERE R.NAME IS NULL AND P.PAGE_CODE NOT IN('DTP','FP');

DELETE PF FROM AMP_PAGE_FILTERS PF LEFT OUTER JOIN AMP_PAGES P
ON (PF.AMP_PAGE_ID=P.AMP_PAGE_ID) 
WHERE P.AMP_PAGE_ID IS NULL;

DELETE MR FROM AMP_MEMBER_REPORTS MR LEFT OUTER JOIN AMP_REPORTS R
ON (MR.AMP_REPORT_ID=R.AMP_REPORT_ID) 
WHERE R.AMP_REPORT_ID IS NULL;

DELETE MR FROM AMP_REPORT_COLUMN MR LEFT OUTER JOIN AMP_REPORTS R
ON (MR.AMP_REPORT_ID=R.AMP_REPORT_ID) 
WHERE R.AMP_REPORT_ID IS NULL;

DELETE MR FROM AMP_REPORT_HIERARCHY MR LEFT OUTER JOIN AMP_REPORTS R
ON (MR.AMP_REPORT_ID=R.AMP_REPORT_ID) 
WHERE R.AMP_REPORT_ID IS NULL;

DELETE MR FROM AMP_REPORT_MEASURES MR LEFT OUTER JOIN AMP_REPORTS R
ON (MR.AMP_REPORT_ID=R.AMP_REPORT_ID) 
WHERE R.AMP_REPORT_ID IS NULL;

DELETE MR FROM AMP_TEAM_REPORTS MR LEFT OUTER JOIN AMP_REPORTS R
ON (MR.REPORT=R.AMP_REPORT_ID) 
WHERE R.AMP_REPORT_ID IS NULL;

DELETE TPF FROM AMP_TEAM_PAGE_FILTERS TPF LEFT OUTER JOIN AMP_PAGES P
ON (TPF.PAGE=P.AMP_PAGE_ID) WHERE P.AMP_PAGE_ID IS NULL;

DELETE TPF FROM AMP_TEAM_PAGE_FILTERS TPF LEFT OUTER JOIN AMP_TEAM T
ON (TPF.TEAM=T.AMP_TEAM_ID ) WHERE T.AMP_TEAM_ID  IS NULL;