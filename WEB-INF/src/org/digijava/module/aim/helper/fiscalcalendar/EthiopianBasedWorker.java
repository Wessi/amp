package org.digijava.module.aim.helper.fiscalcalendar;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import org.digijava.module.aim.dbentity.AmpFiscalCalendar;

public class EthiopianBasedWorker implements ICalendarWorker {

	protected Map<Integer, ComparableMonth> monthCache = new HashMap<Integer, ComparableMonth>();

	private GregorianCalendar internalCalendar = null;

	private EthiopianCalendar internalEthiopianCalendar = null;

	private Date internalTime = null;

	private AmpFiscalCalendar fiscalCalendar = null;

	public EthiopianBasedWorker(AmpFiscalCalendar calendar) {
		this.fiscalCalendar = calendar;
	}

	public Date getDate() throws Exception {
		return internalCalendar.getTime();
	}

	public Comparable getMonth() throws Exception {
		checkSetTimeCalled();
		int monthId = internalEthiopianCalendar.ethMonth;
		ComparableMonth cm = monthCache.get(monthId);
		if (cm == null) {
			String monthStr = internalEthiopianCalendar.ethMonthName;
			cm = new ComparableMonth(monthId, monthStr);
			monthCache.put(monthId, cm);
		}
		return cm;
	}

	public Integer getQuarter() throws Exception {
		checkSetTimeCalled();
		return internalEthiopianCalendar.ethFiscalQrt;
	}

	public Integer getYear() throws Exception {
		checkSetTimeCalled();
		return internalEthiopianCalendar.ethYear;
	}

	public void setTime(Date time) {
		internalTime = time;
		internalCalendar = new GregorianCalendar();
		internalCalendar.setTime(time);
		// set offset from fiscal calendar
		internalCalendar.add(GregorianCalendar.YEAR, fiscalCalendar.getYearOffset());
		int toAdd = -(fiscalCalendar.getStartMonthNum() - 1);
		internalCalendar.add(GregorianCalendar.MONTH, toAdd);
		toAdd = -(fiscalCalendar.getStartDayNum() - 1);
		internalCalendar.add(GregorianCalendar.DAY_OF_MONTH, fiscalCalendar.getYearOffset());
		internalEthiopianCalendar = EthiopianCalendar.getEthiopianDate(internalCalendar);

	}

	public Date toDefatultDate() {
		// TODO Auto-generated method stub
		return null;
	}

	private void checkSetTimeCalled() throws Exception {
		if (internalTime == null)
			throw new Exception("Should call to setime first");
	}
}
