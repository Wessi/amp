package org.dgfoundation.amp.nireports.schema;

import java.util.Map;

import org.dgfoundation.amp.newreports.ReportFilters;
import org.dgfoundation.amp.nireports.CategAmountCell;
import org.dgfoundation.amp.nireports.NiFilters;

import com.google.common.base.Function;

/**
 * an interface describind the Schema of a reports' implementation
 * @author Constantin Dolghier
 *
 */
public interface NiReportsSchema {
	/**
	 * returns the list of columns which exist in the schema
	 * @return
	 */
	public Map<String, NiReportColumn<?>> getColumns();
	
	/**
	 * returns the list of measures which exist in the schema
	 * @return
	 */
	public Map<String, NiReportMeasure> getMeasures();
	
	/**
	 * returns the fetcher of funding
	 * @return
	 */
	public NiReportColumn<CategAmountCell> getFundingFetcher();

	/**
	 * returns a function which exposes the spec's {@link ReportFilters} instance to Ni's NiFilters
	 * @return
	 */
	public Function<ReportFilters, NiFilters> getFiltersConverter();
}
