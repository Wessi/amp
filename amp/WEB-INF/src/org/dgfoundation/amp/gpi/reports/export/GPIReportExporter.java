package org.dgfoundation.amp.gpi.reports.export;

import java.util.Map;

import org.dgfoundation.amp.gpi.reports.GPIReport;
import org.digijava.kernel.translator.TranslatorWorker;

/**
 * @author Viorel Chihai
 *
 */
public interface GPIReportExporter {
	
	public static final String XLSX = "xlsx";
	public static final String PDF = "pdf";
	
	public static final String INDICATOR_5B_SUMMARY_LABEL = "Indicator 5b % at country level";
	public static final String COLUMN_QUESTION = "Question";
	public static final String COLUMN_VALUE = "Value";
	
	/**
	 * @param report
	 * @return
	 * @throws Exception
	 */
	public abstract byte[] exportReport(GPIReport report) throws Exception;
	
	default String getColumnHeaderLabel(Map<String, String> indicatorLabels, String columnName) {
        return indicatorLabels.containsKey(columnName) 
                ? TranslatorWorker.translateText(indicatorLabels.get(columnName)) : columnName;
    }
	
}
