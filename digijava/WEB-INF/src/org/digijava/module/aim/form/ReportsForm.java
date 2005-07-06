package org.digijava.module.aim.form;

import org.apache.struts.action.*;
import java.util.Collection;

public class ReportsForm extends ActionForm {

		  private Collection reports = null;
		  private Long reportId = null;
		  private String name = null;
		  private String description = null;
		  private String flag = null;
		  private Long selReports[] = null;
		  private String addReport = null;
		  private String removeReports = null;
		  private String assignReports = null;
		  private Long teamId = null;
		  private String teamName = null;
		  private Long memberId = null;
		  private String memberName = null;

		  public Long getMemberId() {
					 return this.memberId;
		  }

		  public void setMemberId(Long memberId) {
					 this.memberId = memberId;
		  }

		  public String getMemberName() {
					 return this.memberName;
		  }

		  public void setMemberName(String memberName) {
					 this.memberName = memberName;
		  }
		  
		  public Long getTeamId() {
					 return this.teamId;
		  }

		  public void setTeamId(Long teamId) {
					 this.teamId = teamId;
		  }

		  public String getTeamName() {
					 return this.teamName;
		  }

		  public void setTeamName(String teamName) {
					 this.teamName = teamName;
		  }

		  public Long[] getSelReports() {
					 return this.selReports;
		  }

		  public void setSelReports(Long selReports[]) {
					 this.selReports = selReports;
		  }

		  public String getAddReport() {
					 return this.addReport;
		  }

		  public void setAddReport(String addReport) {
					 this.addReport = addReport;
		  }

		  public String getRemoveReports() {
					 return this.removeReports;
		  }

		  public void setRemoveReports(String removeReports) {
					 this.removeReports = removeReports;
		  }

		  public String getAssignReports() {
					 return this.assignReports;
		  }

		  public void setAssignReports(String assignReports) {
					 this.assignReports = assignReports;
		  }

		  public Long getReportId() {
					 return reportId;
		  }

		  public void setReportId(Long reportId) {
					 this.reportId = reportId;
		  }
		  
		  public Collection getReports() {
					 return (this.reports);
		  }

		  public void setReports(Collection reports) {
					 this.reports = reports;
		  }

		  public String getName() {
					 return name;
		  }

		  public String getFlag() {
					 return flag;
		  }

		  public String getDescription() {
					 return description;
		  }

		  public void setName(String name) {
					 this.name = name;
		  }

		  public void setFlag(String flag) {
					 this.flag = flag;
		  }

		  public void setDescription(String description) {
					this.description = description;
		  }
}
