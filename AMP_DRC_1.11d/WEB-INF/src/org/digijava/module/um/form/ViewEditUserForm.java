package org.digijava.module.um.form;

import java.util.Collection;

import org.apache.struts.action.ActionForm;
import org.digijava.module.aim.dbentity.AmpOrgGroup;
import org.digijava.module.aim.dbentity.AmpOrgType;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.helper.CountryBean;

public class ViewEditUserForm extends ActionForm {

	private static final long serialVersionUID = 1L;
	private Long id;
	private String email;
	private String contacts;
	private String firstNames;
	private String lastName;
	private String name;
	private String url;
	private String mailingAddress;

	private Long selectedOrgGroupId;
	private Collection<AmpOrgGroup> orgGroups;

	private String selectedOrgTypeId;
	private Collection<AmpOrgType> orgTypes;

	private String selectedOrgName;
	private Long selectedOrgId;
	private Collection<AmpOrganisation> orgs;

	/* this is the attached org related with gateperm OrgRoleGate */
	private Long assignedOrgId;

	private String selectedCountryIso;
	private Collection<CountryBean> countries;

	private String selectedLanguageCode;
	private Collection languages;

	private String event;
	private Boolean ban;
	private String confirmNewPassword;
	private String newPassword;
	private Boolean displaySuccessMessage;

	public ViewEditUserForm() {

	}

	public String getContacts() {
		return contacts;
	}

	public Collection<CountryBean> getCountries() {
		return countries;
	}

	public String getEmail() {
		return email;
	}

	public String getEvent() {
		return event;
	}

	public String getFirstNames() {
		return firstNames;
	}

	public Long getId() {
		return id;
	}

	public Collection getLanguages() {
		return languages;
	}

	public String getLastName() {
		return lastName;
	}

	public String getMailingAddress() {
		return mailingAddress;
	}

	public String getName() {
		return name;
	}

	public Collection<AmpOrgGroup> getOrgGroups() {
		return orgGroups;
	}

	public Collection<AmpOrganisation> getOrgs() {
		return orgs;
	}

	public Collection<AmpOrgType> getOrgTypes() {
		return orgTypes;
	}

	public String getSelectedCountryIso() {
		return selectedCountryIso;
	}

	public String getSelectedLanguageCode() {
		return selectedLanguageCode;
	}

	public Long getSelectedOrgGroupId() {
		return selectedOrgGroupId;
	}

	public String getSelectedOrgName() {
		return selectedOrgName;
	}

	public String getSelectedOrgTypeId() {
		return selectedOrgTypeId;
	}

	public String getUrl() {
		return url;
	}

	public Boolean getBan() {
		return ban;
	}

	public String getConfirmNewPassword() {

		return confirmNewPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public Boolean getDisplaySuccessMessage() {
		return displaySuccessMessage;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setSelectedOrgTypeId(String selectedOrgTypeId) {
		this.selectedOrgTypeId = selectedOrgTypeId;
	}

	public void setSelectedOrgName(String selectedOrgName) {
		this.selectedOrgName = selectedOrgName;
	}

	public void setSelectedOrgGroupId(Long selectedOrgGroupId) {
		this.selectedOrgGroupId = selectedOrgGroupId;
	}

	public void setSelectedLanguageCode(String selectedLanguageCode) {
		this.selectedLanguageCode = selectedLanguageCode;
	}

	public void setSelectedCountryIso(String selectedCountryIso) {
		this.selectedCountryIso = selectedCountryIso;
	}

	public void setOrgTypes(Collection<AmpOrgType> orgTypes) {
		this.orgTypes = orgTypes;
	}

	public void setOrgs(Collection<AmpOrganisation> orgs) {
		this.orgs = orgs;
	}

	public void setOrgGroups(Collection<AmpOrgGroup> orgGroups) {
		this.orgGroups = orgGroups;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setMailingAddress(String mailingAddress) {
		this.mailingAddress = mailingAddress;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setLanguages(Collection languages) {
		this.languages = languages;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setFirstNames(String firstNames) {
		this.firstNames = firstNames;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setCountries(Collection<CountryBean> countries) {
		this.countries = countries;
	}

	public void setContacts(String contacts) {
		this.contacts = contacts;
	}

	public void setBan(Boolean ban) {
		this.ban = ban;
	}

	public void setConfirmNewPassword(String confirmNewPassword) {

		this.confirmNewPassword = confirmNewPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public void setDisplaySuccessMessage(Boolean displaySuccessMessage) {
		this.displaySuccessMessage = displaySuccessMessage;
	}

	public Long getAssignedOrgId() {
		return assignedOrgId;
	}

	public void setAssignedOrgId(Long assignedOrgId) {
		this.assignedOrgId = assignedOrgId;
	}

	public Long getSelectedOrgId() {
		return selectedOrgId;
	}

	public void setSelectedOrgId(Long selectedOrgId) {
		this.selectedOrgId = selectedOrgId;
	}

}