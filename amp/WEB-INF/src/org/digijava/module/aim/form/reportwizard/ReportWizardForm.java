/**
 * 
 */
package org.digijava.module.aim.form.reportwizard;

import java.util.*;

import org.apache.struts.action.ActionForm;
import org.digijava.module.aim.dbentity.AmpColumns;
import org.digijava.module.aim.dbentity.AmpTeamMember;

/**
 * @author alex
 *
 */
public class ReportWizardForm extends ActionForm {
	
	private Long reportId				= null;
	private AmpTeamMember ampTeamMember	= null;
	
	private Map<String, List<AmpColumns>> ampTreeColumns;
	private Collection ampMeasures		= null;
	private String reportType 			= "donor";
	private Boolean desktopTab   		= false;
	
	private Boolean hideActivities 		= false;
	private String 	reportPeriod		= "A";
	private String reportTitle 			= "";
	private String reportDescription 	= "";
	private String originalTitle		= "";
	
	private Long[] selectedColumns		= null;
	private Long[] selectedHierarchies	= null;
	private Long[] selectedMeasures		= null;

	private Boolean duplicateName		= false;
	private Boolean overwritingForeignReport = false;
	
	private Boolean publicReport 		= false;
	private Boolean useFilters			= false;
	
	private Boolean allowEmptyFundingColumns	= false;
	private Boolean onePager			= false;
	
	private String projecttitle= "Project Title";
	
	private Boolean budgetExporter = false;
	
	private Long reportCategory			= new Long(0);
	
	public String getProjecttitle() {
		return projecttitle;
	}

	public void setProjecttitle(String projecttitle) {
		this.projecttitle = projecttitle;
	}

	public Long getReportId() {
		return reportId;
	}

	public void setReportId(Long reportId) {
		this.reportId = reportId;
	}
	
	public AmpTeamMember getAmpTeamMember() {
		return ampTeamMember;
	}

	public void setAmpTeamMember(AmpTeamMember ampTeamMember) {
		this.ampTeamMember = ampTeamMember;
	}

	public Map<String, List<AmpColumns>> getAmpTreeColumns() {
		return ampTreeColumns;
	}

	public void setAmpTreeColumns(Map<String, List<AmpColumns>> ampTreeColumns) {
		this.ampTreeColumns = ampTreeColumns;
	}

	
	public Collection getAmpMeasures() {
		return ampMeasures;
	}

	public void setAmpMeasures(Collection ampMeasures) {
		this.ampMeasures = ampMeasures;
	}


	public Boolean getDesktopTab() {
		return desktopTab;
	}

	public void setDesktopTab(Boolean desktopTab) {
		this.desktopTab = desktopTab;
	}

	public String getReportType() {
		return reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	public Boolean getHideActivities() {
		return hideActivities;
	}

	public void setHideActivities(Boolean hideActivities) {
		this.hideActivities = hideActivities;
	}

	public String getReportDescription() {
		return reportDescription;
	}

	public void setReportDescription(String reportDescription) {
		this.reportDescription = reportDescription;
	}

	public String getReportPeriod() {
		return reportPeriod;
	}

	public void setReportPeriod(String reportPeriod) {
		this.reportPeriod = reportPeriod;
	}

	public String getReportTitle() {
		return reportTitle;
	}

	public void setReportTitle(String reportTitle) {
		this.reportTitle = reportTitle;
	}

	public Long[] getSelectedColumns() {
		return selectedColumns;
	}

	public void setSelectedColumns(Long[] selectedColumns) {
		this.selectedColumns = selectedColumns;
	}

	public Long[] getSelectedHierarchies() {
		return selectedHierarchies;
	}

	public void setSelectedHierarchies(Long[] selectedHierarchies) {
		this.selectedHierarchies = selectedHierarchies;
	}

	public Long[] getSelectedMeasures() {
		return selectedMeasures;
	}

	public void setSelectedMeasures(Long[] selectedMeasures) {
		this.selectedMeasures = selectedMeasures;
	}

	public String getOriginalTitle() {
		return originalTitle;
	}

	public void setOriginalTitle(String originalTitle) {
		this.originalTitle = originalTitle;
	}

	public Boolean getDuplicateName() {
		return duplicateName;
	}

	public void setDuplicateName(Boolean duplicateName) {
		this.duplicateName = duplicateName;
	}

	public void setPublicReport(Boolean publicReport) {
		this.publicReport = publicReport;
	}

	public Boolean getPublicReport() {
		return publicReport;
	}

	public Boolean getUseFilters() {
		return useFilters;
	}

	public void setUseFilters(Boolean useFilters) {
		this.useFilters = useFilters;
	}

	public Boolean getAllowEmptyFundingColumns() {
		return allowEmptyFundingColumns;
	}

	public void setAllowEmptyFundingColumns(Boolean allowEmptyFundingColumns) {
		this.allowEmptyFundingColumns = allowEmptyFundingColumns;
	}

	public Boolean getOnePager() {
		return onePager;
	}

	public void setOnePager(Boolean onePager) {
		this.onePager = onePager;
	}
	
	/**
	 * @return the budgetExporter
	 */
	public Boolean getBudgetExporter() {
		return budgetExporter;
	}

	/**
	 * @param budgetExporter the budgetExporter to set
	 */
	public void setBudgetExporter(Boolean budgetExporter) {
		this.budgetExporter = budgetExporter;
	}

	public Long getReportCategory() {
		return reportCategory;
	}

	public void setReportCategory(Long reportCategory) {
		this.reportCategory = reportCategory;
	}
	
	public Boolean getOverwritingForeignReport()
	{
		return overwritingForeignReport;
	}
	
	public void setOverwritingForeignReport(Boolean bla)
	{
		this.overwritingForeignReport = bla;
	}
}
