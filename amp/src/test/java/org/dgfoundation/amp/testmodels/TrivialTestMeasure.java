package org.dgfoundation.amp.testmodels;

import org.dgfoundation.amp.nireports.behaviours.TrivialMeasureBehaviour;
import org.dgfoundation.amp.nireports.schema.Behaviour;
import org.dgfoundation.amp.nireports.schema.NiTransactionMeasure;
import org.digijava.module.aim.helper.Constants;

/**
 * copied from trivial AmpTrivialMeasure
 * @author acartaleanu
 *
 */
public class TrivialTestMeasure extends NiTransactionMeasure {

	public TrivialTestMeasure(String measureName, long transactionType, String adjustmentTypeName, boolean directed, boolean ignoreFilters, Behaviour<?> beh) {
		super(measureName, 
			cac -> cac.metaInfo.containsMeta(TestMetaCategory.TRANSACTION_TYPE.category, Long.valueOf(transactionType)) &&
			cac.metaInfo.containsMeta(TestMetaCategory.ADJUSTMENT_TYPE.category, adjustmentTypeName) &&
			(directed ? (false) : (cac.metaInfo.containsMeta(TestMetaCategory.SOURCE_ROLE.category, Constants.FUNDING_AGENCY))),
			beh,
			null,
			ignoreFilters
		);
	}

	public TrivialTestMeasure(String measureName, long transactionType, String adjustmentTypeName, boolean directed, boolean ignoreFilters) {
		this(measureName, transactionType, adjustmentTypeName, directed, ignoreFilters, TrivialMeasureBehaviour.getInstance());
	}

	public TrivialTestMeasure(String measureName, long transactionType, String adjustmentTypeName, boolean directed) {
		this(measureName, transactionType, adjustmentTypeName, directed, false);
	}
}
