package org.digijava.module.aim.dbentity;

import java.util.Set;

import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;

/**
 * holds contact user's information
 * @author Dare
 *
 */
public class AmpContact {
	private Long id;
	private String name;
	private String lastname;
	private String email;
	private AmpCategoryValue title;
	private String organisationName;
	private String phone;
	private String fax;
	private String function;
	private String mobilephone;
	private String officeaddress;
	private String temporaryId;
	
	/**
	 * currently these fields are not usable, but will become when we decide 
	 * to link contact list to calendar and messaging 
	 */
	private Boolean shared; //is contact shared between amp users
	private AmpTeamMember creator; //who created the contact
	
	private Set<AmpActivityContact> activityContacts;
	
	public Long getId() {
		return id;
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public AmpCategoryValue getTitle() {
		return title;
	}
	public void setTitle(AmpCategoryValue title) {
		this.title = title;
	}
	public String getOrganisationName() {
		return organisationName;
	}
	public void setOrganisationName(String organisationName) {
		this.organisationName = organisationName;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getFax() {
		return fax;
	}
	public void setFax(String fax) {
		this.fax = fax;
	}
	public Boolean getShared() {
		return shared;
	}
	public void setShared(Boolean shared) {
		this.shared = shared;
	}
	public AmpTeamMember getCreator() {
		return creator;
	}
	public void setCreator(AmpTeamMember creator) {
		this.creator = creator;
	}

	public Set<AmpActivityContact> getActivityContacts() {
		return activityContacts;
	}
	public void setActivityContacts(Set<AmpActivityContact> activityContacts) {
		this.activityContacts = activityContacts;
	}
	public String getTemporaryId() {
		return temporaryId;
	}
	public void setTemporaryId(String temporaryId) {
		this.temporaryId = temporaryId;
	}
	
	public String getFunction() {
		return function;
	}
	public void setFunction(String function) {
		this.function = function;
	}
	public String getMobilephone() {
		return mobilephone;
	}
	public void setMobilephone(String mobilephone) {
		this.mobilephone = mobilephone;
	}
	public String getOfficeaddress() {
		return officeaddress;
	}
	public void setOfficeaddress(String officeaddress) {
		this.officeaddress = officeaddress;
	}	
}
