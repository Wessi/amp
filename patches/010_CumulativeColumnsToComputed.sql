update amp_columns set cellType = 'org.dgfoundation.amp.ar.cell.ComputedAmountCell',extractorView = null,tokenExpression = 'cumulativeCommitment' where columnName = 'Cumulative Commitment';
update amp_columns set cellType='org.dgfoundation.amp.ar.cell.ComputedAmountCell',extractorView=null,tokenExpression='cumulativeDisbursement' where columnName='Cumulative Disbursement';
