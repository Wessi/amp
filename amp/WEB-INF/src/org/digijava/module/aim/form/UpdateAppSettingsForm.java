package org.digijava.module.aim.form;

import org.apache.struts.action.*;
import java.util.*;

public class UpdateAppSettingsForm extends ActionForm {

	private Long appSettingsId = null;

	private int defRecsPerPage;

	private String language = null;

	private String defPerspective = null;

	private Long currencyId = null;

	private Long fisCalendarId = null;

	private String type = null; /*
								 * specifies whether updating team level
								 * settings or user specific settings
								 */

	private Collection currencies = null;

	private Collection fisCalendars = null;

	private String teamName = null;

	private String memberName = null;

	private String restore = null;

	private String save = null;

	private boolean updateFlag = false;

	private boolean updated = false;
	
	private Long defaultReportForTeamId	= new Long(0);

	private List languages;
	
	private Collection reports; 

	public Collection getReports() {
		return reports;
	}

	public void setReports(Collection reports) {
		this.reports = reports;
	}

	public boolean getUpdateFlag() {
		return updateFlag;
	}

	public void setUpdateFlag(boolean flag) {
		updateFlag = flag;
	}

	public boolean getUpdated() {
		return updated;
	}

	public void setUpdated(boolean flag) {
		updated = flag;
	}

	public String getRestore() {
		return this.restore;
	}

	public void setRestore(String restore) {
		this.restore = restore;
	}

	public String getSave() {
		return this.save;
	}

	public void setSave(String save) {
		this.save = save;
	}

	public Collection getCurrencies() {
		return this.currencies;
	}

	public void setCurrencies(Collection currencies) {
		this.currencies = currencies;
	}

	public Collection getFisCalendars() {
		return fisCalendars;
	}

	public void setFisCalendars(Collection fisCalendars) {
		this.fisCalendars = fisCalendars;
	}

	public Long getAppSettingsId() {
		return this.appSettingsId;
	}

	public void setAppSettingsId(Long appSettingsId) {
		this.appSettingsId = appSettingsId;
	}

	public int getDefRecsPerPage() {
		return this.defRecsPerPage;
	}

	public void setDefRecsPerPage(int defRecsPerPage) {
		this.defRecsPerPage = defRecsPerPage;
	}

	public String getLanguage() {
		return this.language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getDefPerspective() {
		return this.defPerspective;
	}

	public void setDefPerspective(String defPerspective) {
		this.defPerspective = defPerspective;
	}

	public Long getCurrencyId() {
		return this.currencyId;
	}

	public void setCurrencyId(Long currencyId) {
		this.currencyId = currencyId;
	}

	public Long getFisCalendarId() {
		return this.fisCalendarId;
	}

	public void setFisCalendarId(Long fisCalendarId) {
		this.fisCalendarId = fisCalendarId;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTeamName() {
		return (this.teamName);
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public String getMemberName() {
		return (this.memberName);
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}
	/**
	 * @return Returns the languages.
	 */
	public List getLanguages() {
		return languages;
	}
	/**
	 * @param languages The languages to set.
	 */
	public void setLanguages(List languages) {
		this.languages = languages;
	}

	public Long getDefaultReportForTeamId() {
		return defaultReportForTeamId;
	}

	public void setDefaultReportForTeamId(Long defaultReportForTeamId) {
		this.defaultReportForTeamId = defaultReportForTeamId;
	}
}
