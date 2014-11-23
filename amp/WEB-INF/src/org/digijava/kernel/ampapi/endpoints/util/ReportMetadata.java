package org.digijava.kernel.ampapi.endpoints.util;

import java.util.List;

import org.dgfoundation.amp.newreports.ReportSpecificationImpl;
import org.digijava.kernel.ampapi.endpoints.settings.SettingField;

public class ReportMetadata {
	private String name = "";
	private String connection = "";
	private String cube = "";
	private String uniqueName = "";
	private String catalog = "";
	private String schema = "";
	private String queryName = "";
	private ReportSpecificationImpl reportSpec;
	private List<SettingField> settings;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getConnection() {
		return connection;
	}
	public void setConnection(String connection) {
		this.connection = connection;
	}
	public String getCube() {
		return cube;
	}
	public void setCube(String cube) {
		this.cube = cube;
	}
	public String getCatalog() {
		return catalog;
	}
	public void setCatalog(String catalog) {
		this.catalog = catalog;
	}
	public String getSchema() {
		return schema;
	}
	public void setSchema(String schema) {
		this.schema = schema;
	}
	public String getQueryName() {
		return queryName;
	}
	public void setQueryName(String queryName) {
		this.queryName = queryName;
	}
	public ReportSpecificationImpl getReportSpec() {
		return reportSpec;
	}
	public void setReportSpec(ReportSpecificationImpl report) {
		this.reportSpec = report;
	}
	public String getUniqueName() {
		return uniqueName;
	}
	public void setUniqueName(String uniqueName) {
		this.uniqueName = uniqueName;
	}
	/**
	 * @return the settings
	 */
	public List<SettingField> getSettings() {
		return settings;
	}
	/**
	 * @param settings the settings to set
	 */
	public void setSettings(List<SettingField> settings) {
		this.settings = settings;
	}
	
}