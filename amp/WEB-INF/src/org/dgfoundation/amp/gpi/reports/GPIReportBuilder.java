package org.dgfoundation.amp.gpi.reports;

import org.dgfoundation.amp.newreports.GeneratedReport;
import org.digijava.kernel.ampapi.endpoints.settings.Settings;
import org.digijava.kernel.ampapi.endpoints.settings.SettingsUtils;

/**
 * A utility class to build a GPI Report from {@link GeneratedReport} generated report
 * 
 * @author Viorel Chihai
 *
 */
public class GPIReportBuilder {

	/**
	 * the source generated report which lead to this report being converted
	 */
	protected GeneratedReport generatedReport;
	
	/**
	 * the object that will transform the report
	 */
	protected final GPIReportOutputBuilder gpiReportOutputBuilder;

	public GPIReportBuilder(GeneratedReport report, GPIReportOutputBuilder outputBuilder) {
		this.generatedReport = report;
		this.gpiReportOutputBuilder = outputBuilder;
	}

	public GPIReport build(int page, int recordsPerPage) {
		GPIReport gpiReport = new GPIReport();
		gpiReport.setSettings(getReportSettings());
		gpiReport.setPage(getReportPage(page, recordsPerPage));

		return gpiReport;
	}

	protected GPIReportPage getReportPage(int page, int recordsPerPage) {
		return gpiReportOutputBuilder.buildGPIReportPage(generatedReport, page, recordsPerPage);
	}

	private Settings getReportSettings() {
		Settings reportSettings = SettingsUtils.getReportSettings(generatedReport.spec);

		return reportSettings;
	}
}
