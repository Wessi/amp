package org.digijava.module.um.form;

import java.util.*;

import org.apache.struts.action.*;

public class ViewEditUserForm
    extends ActionForm {

  private Long id;
  private String email;
  private String contacts;
  private String firstNames;
  private String lastName;
  private String name;
  private String url;
  private String mailingAddress;

  private Long selectedOrgGroupId;
  private Collection orgGroups;

  private String selectedOrgTypeId;
  private Collection orgTypes;

  private String selectedOrgName;
  private Collection orgs;

  private String selectedCountryIso;
  private Collection countries;

  private String selectedLanguageCode;
  private Collection languages;

  private String event;
  private Boolean ban;

  public ViewEditUserForm() {

  }

  public String getContacts() {
    return contacts;
  }

  public Collection getCountries() {
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

  public Collection getOrgGroups() {
    return orgGroups;
  }

  public Collection getOrgs() {
    return orgs;
  }

  public Collection getOrgTypes() {
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

  public void setOrgTypes(Collection orgTypes) {
    this.orgTypes = orgTypes;
  }

  public void setOrgs(Collection orgs) {
    this.orgs = orgs;
  }

  public void setOrgGroups(Collection orgGroups) {
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

  public void setCountries(Collection countries) {
    this.countries = countries;
  }

  public void setContacts(String contacts) {
    this.contacts = contacts;
  }

  public void setBan(Boolean ban) {
    this.ban = ban;
  }

}
