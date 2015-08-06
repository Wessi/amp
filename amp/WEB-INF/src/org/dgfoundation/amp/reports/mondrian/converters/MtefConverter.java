package org.dgfoundation.amp.reports.mondrian.converters;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.dgfoundation.amp.ar.MeasureConstants;
import org.dgfoundation.amp.ar.viewfetcher.RsInfo;
import org.dgfoundation.amp.ar.viewfetcher.SQLUtils;
import org.dgfoundation.amp.newreports.FilterRule;
import org.dgfoundation.amp.newreports.ReportElement;
import org.dgfoundation.amp.newreports.ReportElement.ElementType;
import org.dgfoundation.amp.newreports.ReportMeasure;
import org.dgfoundation.amp.newreports.ReportSpecificationImpl;
import org.dgfoundation.amp.reports.mondrian.MondrianReportFilters;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.module.aim.dbentity.AmpReportColumn;
import org.digijava.module.aim.dbentity.AmpReports;
import org.hibernate.jdbc.Work;

/**
 * singleton class used for injecting into a ReportSpecificationImpl the data needed for including the MTEF columns as measures in the Mondrian reports implementation
 * @author Dolghier Constantin
 *
 */
public class MtefConverter {
	public final static MtefConverter instance = new MtefConverter();
	
	/**
	 * Map<mtef-first-year-in-range, Pair<julian-code-of-first-day-in-year, julian-code-of-last-day-in-year>>
	 */
	public final Map<Integer, YearMtefInfo> mtefInfos;
	
	public class YearMtefInfo {
		public final int startDayJulianCode;
		public final int endDayJulianCode;
		
		public YearMtefInfo(int startDayJulianCode, int endDayJulianCode) {
			this.startDayJulianCode = startDayJulianCode;
			this.endDayJulianCode = endDayJulianCode;
		}
		
		@Override public String toString() {
			return String.format("(%d - %d)", startDayJulianCode, endDayJulianCode);
		}
	}
	
	private MtefConverter() {
		final SortedMap<Integer, YearMtefInfo> mtefInfos = new TreeMap<>();
		PersistenceManager.getSession().doWork(new Work() {
			@Override public void execute(Connection connection) throws SQLException {
				String query = "SELECT yr, to_char((yr || '-01-01')::date, 'J')::integer AS year_start_day_code, " + 
						"to_char((yr+1 || '-01-01')::date, 'J')::integer - 1 AS year_end_day_code " + 
						"FROM generate_series(1970, 2050) yr";

				try(RsInfo rsi = SQLUtils.rawRunQuery(connection, query, null)) {
					while (rsi.rs.next()) {
						int yearNr = rsi.rs.getInt(1);
						int firstDay = rsi.rs.getInt(2);
						int lastDay = rsi.rs.getInt(3);
						mtefInfos.put(yearNr, new YearMtefInfo(firstDay, lastDay));
					}
				}
			}});
		this.mtefInfos = Collections.unmodifiableMap(mtefInfos);
	}
	
	/**
	 * scans the report for MTEF columns and converts them to "mtef" measure reference + filter entries <br />
	 * this function is thread-safe, because it has no off-stack state <br />
	 * dies if the input is fishy - this is done on purpose
	 */
	public void convertMtefs(AmpReports report, ReportSpecificationImpl spec) {
		SortedSet<Integer> mtefYears = new TreeSet<>();
		for(AmpReportColumn arc:report.getColumns()) {
			Integer yearNr = arc.getColumn().getMtefYear();
			if (yearNr != null) {
				yearNr = Math.min(Math.max(yearNr, 1970), 2050); // clamp between 1970 and 2050 - this is what our Mondrian reports implementation supports (for now)
				mtefYears.add(yearNr); 
			}
		}
		if (mtefYears.isEmpty()) return; // nothing to do

		// build list of items to add
		List<FilterRule> filterRules = new ArrayList<>();
		for(int mtefYear:mtefYears) {
			YearMtefInfo yearInfo = this.mtefInfos.get(mtefYear);
			filterRules.add(new FilterRule(
					Integer.toString(yearInfo.startDayJulianCode),
					Integer.toString(yearInfo.endDayJulianCode),
				true, true));
		}
		
		// we have to add MTEF info to filters -> ensure that a filters instance exists
		if (spec.getFilters() == null)
			spec.setFilters(new MondrianReportFilters());
		spec.getFilters().getFilterRules().put(new ReportElement(ElementType.MTEF_DATE), filterRules);
		spec.getMeasures().add(0, new ReportMeasure(MeasureConstants.MTEF_PROJECTIONS)); // add MTEF proj as the first measure
	}
}
