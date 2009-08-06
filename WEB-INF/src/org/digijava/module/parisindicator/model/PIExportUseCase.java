package org.digijava.module.parisindicator.model;

import java.util.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;

import org.apache.struts.action.ActionServlet;
import org.digijava.kernel.request.Site;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.parisindicator.helper.*;
import org.digijava.module.parisindicator.helper.export.*;
import org.digijava.module.parisindicator.util.*;

public class PIExportUseCase {

	public synchronized void createPDFReport(ActionServlet servlet, HttpServletResponse response,
			HttpServletRequest request, String reportCode, Collection<PIReportAbstractRow> mainTableRows,
			int[][] miniTable, int startYear, int endYear) throws Exception {

		String reportsFolderPath = servlet.getServletContext().getRealPath(
				"/WEB-INF/classes/org/digijava/module/parisindicator/jasperreports");
		java.io.File reportsFolder = new java.io.File(reportsFolderPath);
		if (!reportsFolder.exists() || !reportsFolder.isDirectory()) {
			reportsFolder.mkdirs();
		}
		reportsFolderPath = reportsFolder.getAbsolutePath().replace("\\", "/");
		String reportName = "ParisIndicator" + reportCode;
		String reportPath = reportsFolderPath + "/" + reportName;
		String realPathJrxml = reportPath + ".jrxml";
		String jasperFile = reportPath + ".jasper";

		PIAbstractExport export = null;
		PIExportExtraOperations auxiliaryExport = null;
		JRDataSource dataSource = null;
		JasperPrint jasperPrint = null;

		Site site = RequestUtils.getSite(request);
		String langCode = RequestUtils.getNavigationLanguage(request).getCode();
		if (PIConstants.PARIS_INDICATOR_REPORT_3.equalsIgnoreCase(reportCode)) {
			export = new PIReport3Export(site, langCode);
		} else if (PIConstants.PARIS_INDICATOR_REPORT_4.equalsIgnoreCase(reportCode)) {
			export = new PIReport4Export(site, langCode);
		} else if (PIConstants.PARIS_INDICATOR_REPORT_5a.equalsIgnoreCase(reportCode)) {
			export = new PIReport5aExport(site, langCode);
			// Dynamically generate the .jrxml file.
			auxiliaryExport = new PIReport5aExport(site, langCode);
			auxiliaryExport.createJrxmlFromClass(reportPath + "_sub.jrxml", startYear, endYear);
			JasperCompileManager.compileReportToFile(reportPath + "_sub.jrxml");
			((PIReport5aExport) export).setSubReportDirectory(reportPath + "_sub.jasper");
			((PIReport5aExport) export).setMiniReportData(auxiliaryExport.generateDataSource(miniTable, startYear, endYear));
			
		} else if (PIConstants.PARIS_INDICATOR_REPORT_5b.equalsIgnoreCase(reportCode)) {
			export = new PIReport5bExport(site, langCode);
		} else if (PIConstants.PARIS_INDICATOR_REPORT_6.equalsIgnoreCase(reportCode)) {
			export = new PIReport6Export(site, langCode);
			// Dynamically generate the .jrxml file.
			auxiliaryExport = new PIReport6Export(site, langCode);
			auxiliaryExport.createJrxmlFromClass(realPathJrxml, startYear, endYear);
		} else if (PIConstants.PARIS_INDICATOR_REPORT_7.equalsIgnoreCase(reportCode)) {
			export = new PIReport7Export(site, langCode);
		} else if (PIConstants.PARIS_INDICATOR_REPORT_9.equalsIgnoreCase(reportCode)) {
			export = new PIReport9Export(site, langCode);
		} else if (PIConstants.PARIS_INDICATOR_REPORT_10a.equalsIgnoreCase(reportCode)) {
			export = new PIReport10aExport(site, langCode);
		}

		try {
			JasperCompileManager.compileReportToFile(realPathJrxml);
			if (PIConstants.PARIS_INDICATOR_REPORT_6.equalsIgnoreCase(reportCode)) {
				dataSource = new PIJasperDataSource(auxiliaryExport.generateDataSource(mainTableRows, startYear,
						endYear));
			} else {
				dataSource = new JRBeanArrayDataSource(export.generateDataSource(mainTableRows).toArray());
			}		
			jasperPrint = JasperFillManager.fillReport(jasperFile, export.getParameters(endYear), dataSource);
			response.setHeader("Content-Disposition", "attachment; filename=ParisIndicator" + reportCode + ".pdf");
			response.setContentType("application/pdf");
			JRPdfExporter exporter = new JRPdfExporter();
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			ServletOutputStream servletOutputStream = response.getOutputStream();
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, servletOutputStream);
			exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
			exporter.exportReport();
			servletOutputStream.flush();
			servletOutputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}