package org.digijava.module.mondrian.form;

import org.apache.struts.action.ActionForm;

public class ShowReportForm extends ActionForm{

	
	private static final long serialVersionUID = 1L;
	
	private String reportname;

	public String getReportname() {
		return reportname;
	}

	public void setReportname(String reportname) {
		this.reportname = reportname;
	}
}
