package org.digijava.module.aim.dbentity ;

import java.io.Serializable;
import java.util.Set;
//import java.io.Serializable;

public class AmpColumns  implements Serializable
{

	private Long columnId ;
	private String columnName ;
	private String aliasName;
	private Set reports;
	

	public String getAliasName() {
		return aliasName;
	}
	public void setAliasName(String aliasName) {
		this.aliasName = aliasName;
	}
	public Long getColumnId() {
		return columnId;
	}
	public void setColumnId(Long columnId) {
		this.columnId = columnId;
	}
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	public Set getReports() {
		return reports;
	}
	public void setReports(Set reports) {
		this.reports = reports;
	}
}
