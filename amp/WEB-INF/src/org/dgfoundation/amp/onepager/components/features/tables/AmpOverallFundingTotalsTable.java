package org.dgfoundation.amp.onepager.components.features.tables;

import java.util.Set;

import org.digijava.module.aim.helper.Constants;
import org.apache.ecs.xhtml.label;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.dgfoundation.amp.onepager.components.AmpComponentPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpLabelFieldPanel;
import org.dgfoundation.amp.onepager.components.fields.AmpLabelInformationFieldPanel;
import org.dgfoundation.amp.onepager.models.AmpOverallFundingModel;
import org.dgfoundation.amp.onepager.translation.TranslatorUtil;
import org.digijava.module.aim.dbentity.AmpActivityVersion;
import org.digijava.module.aim.dbentity.AmpFunding;
import org.digijava.module.categorymanager.util.CategoryConstants;

public class AmpOverallFundingTotalsTable extends AmpComponentPanel<Void> {

	public AmpOverallFundingTotalsTable(String id, String fmName, IModel<Set<AmpFunding>> funding) throws Exception {
		super(id, fmName);
		// TODO Auto-generated constructor stub
		//int COMMITMENT = 0 ;
		//int DISBURSEMENT = 1 ;
		//int EXPENDITURE = 2 ;
	 //   AmpLabelFieldPanel<T>
		
        @SuppressWarnings({ "rawtypes", "unchecked" })
        AmpLabelInformationFieldPanel totalActualCommitments=new AmpLabelInformationFieldPanel("totalActualCommitments",new AmpOverallFundingModel(funding,Constants.COMMITMENT,CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getValueKey()),"Total Actual Commitments");
		add(totalActualCommitments);
		
		AmpLabelInformationFieldPanel totalActualDisbursements=new AmpLabelInformationFieldPanel("totalActualDisbursements",new AmpOverallFundingModel(funding,Constants.DISBURSEMENT,CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getValueKey()),"Total Actual Disbursement");
		add(totalActualDisbursements);


		AmpLabelInformationFieldPanel totalActualExpenditures=new AmpLabelInformationFieldPanel("totalActualExpenditures",new AmpOverallFundingModel(funding,Constants.EXPENDITURE,CategoryConstants.ADJUSTMENT_TYPE_ACTUAL.getValueKey()),"Total Actual Expenditure");
		add(totalActualExpenditures);		

        AmpLabelInformationFieldPanel totalPlannedCommitments=new AmpLabelInformationFieldPanel("totalPlannedCommitments",new AmpOverallFundingModel(funding,Constants.COMMITMENT,CategoryConstants.ADJUSTMENT_TYPE_PLANNED.getValueKey()),"Total Planned Commitments");
		add(totalPlannedCommitments);
		
		AmpLabelInformationFieldPanel totalPlannedDisbursements=new AmpLabelInformationFieldPanel("totalPlannedDisbursements",new AmpOverallFundingModel(funding,Constants.DISBURSEMENT,CategoryConstants.ADJUSTMENT_TYPE_PLANNED.getValueKey()),"Total Planned Disbursement");
		add(totalPlannedDisbursements);


		AmpLabelInformationFieldPanel totalPlannedExpenditures=new AmpLabelInformationFieldPanel("totalPlannedExpenditures",new AmpOverallFundingModel(funding,Constants.EXPENDITURE,CategoryConstants.ADJUSTMENT_TYPE_PLANNED.getValueKey()),"Total Planned Expenditure");
		add(totalPlannedExpenditures);		
		
	}

}
