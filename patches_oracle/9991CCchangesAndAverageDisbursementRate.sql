update amp_columns set totalExpression=tokenExpression where cellType='org.dgfoundation.amp.ar.cell.ComputedCountingAmountCell';
update amp_columns set tokenExpression=null where cellType='org.dgfoundation.amp.ar.cell.ComputedCountingAmountCell';
update amp_columns set cellType='org.dgfoundation.amp.ar.cell.ComputedAmountCell' where cellType='org.dgfoundation.amp.ar.cell.ComputedCountingAmountCell';
INSERT INTO amp_columns (columnName, aliasName, cellType, extractorView, relatedContentPersisterClass, tokenExpression, filterRetrievable, description, totalExpression) VALUES ('Average Disbursement Rate', NULL, 'org.dgfoundation.amp.ar.cell.ComputedAmountCell', NULL, NULL, 'buildExecutionRate', NULL, 'Sum of Execution Rate / Number of Activities', 'averageDisbursementRate');
INSERT INTO amp_columns (columnName, aliasName, cellType, extractorView, relatedContentPersisterClass, tokenExpression, filterRetrievable, description, totalExpression) VALUES ('Project Age Ratio', NULL, 'org.dgfoundation.amp.ar.cell.ComputedDateCell', 'v_computed_dates', NULL, 'projectAgeRatio', NULL, 'Project Age Ratio = Age of project / Project Period', NULL);

