package org.dgfoundation.amp.exprlogic;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.MetaInfo;
import org.dgfoundation.amp.ar.cell.CategAmountCell;

public class DateRangeLogicalToken extends LogicalToken {

	private Date d1 = null;
	private Date d2 = null;
	private String type;

	public DateRangeLogicalToken(Date d1, Date d2, String type) {
		this.d1 = d1;
		this.d2 = d2;
		this.type = type;
	}

	@Override
	public boolean evaluate(CategAmountCell c) {
		MetaInfo m = MetaInfo.getMetaInfo(c.getMetaData(), ArConstants.TRANSACTION_DATE);
		Date date = (Date) m.getValue();
		ret = (date.compareTo(d1) > -1 && date.compareTo(d2) < 0);
		return super.evaluate(c);
	}
}
