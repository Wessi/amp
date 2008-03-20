package org.digijava.module.aim.logic.defaultimpl;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Iterator;

import org.digijava.module.aim.helper.FilterParams;
import org.digijava.module.aim.helper.YearlyInfo;
import org.digijava.module.aim.logic.DonorFundingCalculator;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.DecimalWraper;

/**
 * 
 * @author Mauricio Coria - coriamauricio@gmail.com
 */
public class DefaultDonorFundingCalculator implements DonorFundingCalculator {

	public DecimalWraper getTotalDonorFunding(Collection<YearlyInfo> c,
			FilterParams fp) {
		DecimalWraper total = new DecimalWraper();
		Iterator<YearlyInfo> iter = c.iterator();
		while (iter.hasNext()) {
			YearlyInfo yf = iter.next();
			if (total.getValue() == null) {
				total.setValue(new BigDecimal(yf.getActualAmount()));
				total.setCalculations(yf.getWrapedActual());
			} else {
				total.setValue(new BigDecimal(yf.getActualAmount()).add(total.getValue()));
				total.setCalculations(total.getCalculations() + " + " + yf.getWrapedActual());
			}
		}
		return total;
	}

	/**
	 * return defatul TotalCommtiments calculation, it is the sum of all actual
	 */
	public DecimalWraper getTotalCommtiments(Long activityId, String currCode, String perspective) {

	DecimalWraper actual = DbUtil.getAmpFundingAmount(activityId, org.digijava.module.aim.helper.Constants.COMMITMENT,
	        org.digijava.module.aim.helper.Constants.ACTUAL, perspective,currCode);
	actual.setCalculations("Total = sum of Planned (" + actual.getCalculations() + ") " + actual.toString());

	return actual;
    }

	public DecimalWraper getTotalCommtiments(DecimalWraper planned,
		DecimalWraper actual) {
	    // just return the actual
	    return actual;
	}


}

