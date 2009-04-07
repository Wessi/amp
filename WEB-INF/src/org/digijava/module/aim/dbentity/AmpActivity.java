package org.digijava.module.aim.dbentity ;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.Set;

import org.digijava.kernel.user.User;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.aim.util.LoggerIdentifiable;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.gateperm.core.GatePermConst;
import org.digijava.module.gateperm.core.Permissible;

public class AmpActivity extends Permissible implements Comparable<AmpActivity>, Serializable,
		LoggerIdentifiable {

	private static String [] IMPLEMENTED_ACTIONS=new String[]{GatePermConst.Actions.EDIT};

	private AmpTeamMember createdBy;

	private String projectImpact;
	private String activitySummary;
	private String contractingArrangements;
	private String condSeq;
	private String linkedActivities;
	private String conditionality;
	private String projectManagement;
	private String contractDetails;
	
	
    private Boolean budget;
    private String govAgreementNumber;
    private String budgetCodeProjectID;

    @PermissibleProperty(type={Permissible.PermissibleProperty.PROPERTY_TYPE_ID})
    private Long ampActivityId ;

	private String ampId ;

	@PermissibleProperty(type={Permissible.PermissibleProperty.PROPERTY_TYPE_LABEL})
	private String name ;
	private String description ;
	private String projectComments ;
	private String lessonsLearned;
	private String objective ;
	private String purpose;
	private String results;
    private String documentSpace;

    private Boolean draft;
    
    private String equalOpportunity;
    private String environment;
    private String minorities;

    	private Long activityLevel;
	private String language ;
	private String version ;
	private String calType; 	// values GREGORIAN, ETH_CAL, ETH_FISCAL_CAL
	private String condition ;
	private Date activityApprovalDate;  // defunct
	private Date activityStartDate ;    // defunct
	private Date activityCloseDate ;    // defunct
	private Date originalCompDate;      // defunct
	private Date contractingDate;
	private Date disbursmentsDate;
	private Set sectors ;
	private Set contracts;
	private Set componentes; //for bolivia;
	private Set locations ;
	private Set<AmpOrgRole> orgrole;
//	private AmpLevel level ; //TO BE DELETED
	private Set internalIds ;
	private Set funding ;
	private Set progress;
	private Set documents ;
	private Set notes;
	private Set issues;
	private Set costs;
	//private AmpModality modality ;
	private AmpCategoryValue modality;
	private AmpTheme themeId;
	private String programDescription;
	private AmpTeam team;
	private Set member;
	
	private String contactName;
	private AmpTeamMember updatedBy;

    private BigDecimal funAmount;
    private String currencyCode;
    private Date funDate;

    private Set referenceDocs;

    private Set activityPrograms;
    // use contFirstName and contLastName instead.
								 // The field is defunct

	// Donor contact information
	private String contFirstName;
	private String contLastName;
	private String email;
	private String dnrCntTitle;
	private String dnrCntOrganization;
	private String dnrCntPhoneNumber;
	private String dnrCntFaxNumber;

	// MOFED contact information
	private String mofedCntFirstName;
	private String mofedCntLastName;
	private String mofedCntEmail;
	private String mfdCntTitle;
	private String mfdCntOrganization;
	private String mfdCntPhoneNumber;
	private String mfdCntFaxNumber;
	
	// Project Coordinator contact information
	private String prjCoFirstName;
	private String prjCoLastName;
	private String prjCoEmail;
	private String prjCoTitle;
	private String prjCoOrganization;
	private String prjCoPhoneNumber;
	private String prjCoFaxNumber;
	
	// Sector Ministry contact information
	private String secMiCntFirstName;
	private String secMiCntLastName;
	private String secMiCntEmail;
	private String secMiCntTitle;
	private String secMiCntOrganization;
	private String secMiCntPhoneNumber;
	private String secMiCntFaxNumber;

	private String comments;

	private String statusReason;
	private Set<AmpComponent> components;
	private Set<AmpComponentFunding> componentFundings;

	private Date proposedStartDate;
	private Date actualStartDate;
	private Date proposedApprovalDate;
	private Date actualApprovalDate;
	private Date actualCompletionDate;
    private Date proposedCompletionDate;
	private Set closingDates;

	private User author; // use activityCreator instead

    										  // This field is defunct

	private AmpTeamMember activityCreator;
	private Date createdDate;
	private Date updatedDate;
	
	private AmpTeamMember approvedBy;
	private Date approvalDate;
	
	//private Set teamList;
	private String contractors;

	private Set regionalFundings;

	private String approvalStatus;

	// Aid Harmonization Survey Set
	private Set survey;

	private Integer lineMinRank;
	private Integer planMinRank;
	private Collection actRankColl;
	
	
	
	/**
	 * Indicator connections.
	 * This field contains {@link IndicatorActivity} beans which represent activity-indicator connections 
	 * and contain set of values for this connection.
	 * Please refer to AmpActivity.hbm.xml and IndicatorConnection.hbm.xml for details.
	 */
    private Set<IndicatorActivity> indicators;
    
    // Start Bolivia Adds
    private Date convenioDateFilter;
    private String convenioNumcont;
    private String clasiNPD;
    // End Bolivia Adds


	private Set<AmpActivityDocument> activityDocuments	= null;
	/* Categories */
	private Set categories;

	/*
	 * Tanzania adds
	 */

	private String FY;

	private String vote;

	private String subVote;

	private String subProgram;

	private String projectCode;
	private String crisNumber;

	private Long gbsSbs;

	private Boolean governmentApprovalProcedures;

	private Boolean jointCriteria;
	
	private Boolean humanitarianAid;
	
        private Set actPrograms;
        
        private boolean createdAsDraft;
        
       	private String donors;

   private String customField1;
   private String customField2;   
   private Long customField3;
   private Date customField4;
   private String customField5;
   private Boolean customField6;
        
        public AmpActivity() {
			// TODO Auto-generated constructor stub
		}
       
        public AmpActivity(Long ampActivityId, String name,Boolean budget, Date updatedDate, AmpTeamMember updateBy, String ampid) {
        	this.ampActivityId=ampActivityId;
			this.name=name;
			this.budget=budget;
			this.updatedDate=updatedDate;
			this.updatedBy=updateBy;
			this.ampId=ampid;
		}
        
        public boolean isCreatedAsDraft() {
            return createdAsDraft;
        }

        public void setCreatedAsDraft(boolean createdAsDraft) {
            this.createdAsDraft = createdAsDraft;
        }

	public Boolean isGovernmentApprovalProcedures() {
		return governmentApprovalProcedures;
	}

	public void setGovernmentApprovalProcedures(
			Boolean governmentApprovalProcedures) {
		this.governmentApprovalProcedures = governmentApprovalProcedures;
	}

	public Boolean isJointCriteria() {
		return jointCriteria;
	}

	public void setJointCriteria(Boolean jointCriteria) {
		this.jointCriteria = jointCriteria;
	}

	public Set getCategories() {
		return categories;
	}

	public void setCategories(Set categories) {
		this.categories = categories;
	}


	public Set<AmpActivityDocument> getActivityDocuments() {
		return activityDocuments;
	}

	public void setActivityDocuments(Set<AmpActivityDocument> activityDocuments) {
		this.activityDocuments = activityDocuments;
	}


	public AmpCategoryValue getModality() {
		return modality;
	}

	public void setModality(AmpCategoryValue modality) {
		this.modality = modality;
	}

	public Set getCosts() {
		return costs;
	}

	public void setCosts(Set costs) {
		this.costs = costs;
	}

	/**
	 * @return
	 */
	public String getAmpId() {
		return ampId;
	}

	/**
	 * @return
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return
	 */
	public Set getFunding() {
		return funding;
	}



	/**
	 * @return
	 */
	public Set getInternalIds() {
		return internalIds;
	}

	/**
	 * @return
	 */
	public String getLanguage() {
		return language;
	}

	/**
	 * @return
	 */
//	public AmpLevel getLevel() { //TO BE DELETED
//		return level;
//	}

	/**
	 * @return
	 */
	public Set getLocations() {
		return locations;
	}

	/**
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return
	 */
	public String getObjective() {
		return objective;
	}

	/**
	 * @return
	 */
	public Set<AmpOrgRole> getOrgrole() {
		return orgrole;
	}

	/**
	 * @return
	 */
	public Set getSectors() {
		return sectors;
	}

	public Set getIssues() {
		return issues;
	}

	/**
	 * @return
	 */
//	public AmpStatus getStatus() { // TO BE DELETED
//		return status;
//	}

	/**
	 * @return
	 */
	public String getVersion() {
		return version;
	}

	public Date getActivityStartDate() {
		return activityStartDate;
	}

	public String getCondition() {
		return condition;
	}

	public Date getActivityCloseDate() {
		return activityCloseDate;
	}

	/**
	 * @param string
	 */
	public void setAmpId(String string) {
		ampId = string;
	}

	/**
	 * @param string
	 */
	public void setDescription(String string) {
		description = string;
	}

	/**
	 * @param set
	 */
	public void setFunding(Set set) {
		funding = set;
	}


	/**
	 * @param set
	 */
	public void setInternalIds(Set set) {
		internalIds = set;
	}

	public void setIssues(Set set) {
		issues = set;
	}

	/**
	 * @param string
	 */
	public void setLanguage(String string) {
		language = string;
	}

	/**
	 * @param level
	 */
//	public void setLevel(AmpLevel level) { // TO BE DELETED
//		this.level = level;
//	}

	/**
	 * @param set
	 */
	public void setLocations(Set set) {
		locations = set;
	}

	/**
	 * @param string
	 */
	public void setName(String string) {
		name = string;
	}

	/**
	 * @param string
	 */
	public void setObjective(String string) {
		objective = string;
	}

	/**
	 * @param set
	 */
	public void setOrgrole(Set<AmpOrgRole> set) {
		orgrole = set;
	}


	/**
	 * @param set
	 */
	public void setSectors(Set set) {
		sectors = set;
	}

	/**
	 * @param status
	 */
//	public void setStatus(AmpStatus status) { // TO BE DELETED
//		this.status = status;
//	}

	/**
	 * @param string
	 */
	public void setVersion(String string) {
		version = string;
	}

	public void setActivityStartDate(Date date) {
		activityStartDate = date;
	}

	public void setActivityCloseDate(Date date) {
		activityCloseDate = date;
	}

	public void setCondition(String string) {
		condition = string;
	}

	/**
	 * @return
	 */
	public Long getAmpActivityId() {
		return ampActivityId;
	}

	/**
	 * @param long1
	 */
	public void setAmpActivityId(Long long1) {
		ampActivityId = long1;
	}

	/**
	 * @return
	 */
	public Set getProgress() {
		return progress;
	}

	/**
	 * @return
	 */
	public Set getDocuments() {
		return documents;
	}

	/**
	 * @return
	 */
	public Set getNotes() {
		return notes;
	}


	/**
	 * @param string
	 */
	public void setProgress(Set progress) {
		this.progress = progress;
	}

	/**
	 * @param string
	 */
	public void setDocuments(Set documents) {
		this.documents = documents;
	}

	/**
	 * @param string
	 */
	public void setNotes(Set notes) {
		this.notes = notes;
	}

	public AmpTheme getThemeId() {
		return themeId;
	}

	public void setThemeId(AmpTheme themeId) {
		this.themeId = themeId;
	}

	public AmpTeam getTeam() {
		return team;
	}

	public void setTeam(AmpTeam team) {
		this.team = team;
	}

	public Set getMember() {
		return member;
	}

	public void setMember(Set member) {
		this.member = member;
	}
	/**
	 * @return
	 */
	public Date getOriginalCompDate() {
		return originalCompDate;
	}

	/**
	 * @param date
	 */
	public void setOriginalCompDate(Date date) {
		originalCompDate = date;
	}

	/**
	 * @return
	 */
	public String getCalType() {
		return calType;
	}

	/**
	 * @param string
	 */
	public void setCalType(String string) {
		calType = string;
	}

	

	public int compareTo(AmpActivity act) {
		// if (!(o instanceof AmpActivity)) throw new ClassCastException();
		//
		// AmpActivity act = (AmpActivity) o;
		
		//Added cos this method is comparing by Names, 
		//but many activities have NULL names and running script to correct records only once is temporary solution.
		String myName=(this.name==null)?"":this.name;
		String hisName=(act.getName()==null)?"":act.getName();
		return (myName.trim().toLowerCase().compareTo(hisName.trim().toLowerCase()));

	}

	/**
	 * @return
	 */
	public String getComments() {
		return comments;
	}

	/**
	 * @return
	 */
	public String getContactName() {
		return contactName;
	}

	/**
	 * @param string
	 */
	public void setComments(String string) {
		comments = string;
	}

	/**
	 * @param string
	 */
	public void setContactName(String string) {
		contactName = string;
	}

	/**
	 * @return Returns the email.
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @param email
	 *            The email to set.
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * @return Returns the statusReason.
	 */
	public String getStatusReason() {
		return statusReason;
	}
	/**
	 * @param statusReason
	 *            The statusReason to set.
	 */
	public void setStatusReason(String statusReason) {
		this.statusReason = statusReason;
	}
	/**
	 * @return Returns the components.
	 */
	public Set<AmpComponent> getComponents() {
		return components;
	}
	/**
	 * @param components
	 *            The components to set.
	 */
	public void setComponents(Set<AmpComponent> components) {
		this.components = components;
	}
	/**
	 * @return Returns the activityApprovalDate.
	 */
	public Date getActivityApprovalDate() {
		return activityApprovalDate;
	}
	/**
	 * @param activityApprovalDate
	 *            The activityApprovalDate to set.
	 */
	public void setActivityApprovalDate(Date activityApprovalDate) {
		this.activityApprovalDate = activityApprovalDate;
	}
	/**
	 * @return Returns the actualApprovalDate.
	 */
	public Date getActualApprovalDate() {
		return actualApprovalDate;
	}
	/**
	 * @param actualApprovalDate
	 *            The actualApprovalDate to set.
	 */
	public void setActualApprovalDate(Date actualApprovalDate) {
		this.actualApprovalDate = actualApprovalDate;
	}
	/**
	 * @return Returns the actualCompletionDate.
	 */
	public Date getActualCompletionDate() {
		return actualCompletionDate;
	}
	/**
	 * @param actualCompletionDate
	 *            The actualCompletionDate to set.
	 */
	public void setActualCompletionDate(Date actualCompletionDate) {
		this.actualCompletionDate = actualCompletionDate;
	}
	/**
	 * @return Returns the actualStartDate.
	 */
	public Date getActualStartDate() {
		return actualStartDate;
	}
	/**
	 * @param actualStartDate
	 *            The actualStartDate to set.
	 */
	public void setActualStartDate(Date actualStartDate) {
		this.actualStartDate = actualStartDate;
	}
	/**
	 * @return Returns the proposedApprovalDate.
	 */
	public Date getProposedApprovalDate() {
		return proposedApprovalDate;
	}
	/**
	 * @param proposedApprovalDate
	 *            The proposedApprovalDate to set.
	 */
	public void setProposedApprovalDate(Date proposedApprovalDate) {
		this.proposedApprovalDate = proposedApprovalDate;
	}
	/**
	 * @return Returns the proposedStartDate.
	 */
	public Date getProposedStartDate() {
		return proposedStartDate;
	}
	/**
	 * @param proposedStartDate
	 *            The proposedStartDate to set.
	 */
	public void setProposedStartDate(Date proposedStartDate) {
		this.proposedStartDate = proposedStartDate;
	}
	/**
	 * @return Returns the closingDates.
	 */
	public Set getClosingDates() {
		return closingDates;
	}
	/**
	 * @param closingDates
	 *            The closingDates to set.
	 */
	public void setClosingDates(Set closingDates) {
		this.closingDates = closingDates;
	}
	/**
	 * @return Returns the author.
	 */
	public User getAuthor() {
		return author;
	}
	/**
	 * @param author
	 *            The author to set.
	 */
	public void setAuthor(User author) {
		this.author = author;
	}
	/**
	 * @return Returns the createdDate.
	 */
	public Date getCreatedDate() {
		return createdDate;
	}
	/**
	 * @param createdDate
	 *            The createdDate to set.
	 */
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	/**
	 * @return Returns the updatedDate.
	 */
	public Date getUpdatedDate() {
		return updatedDate;
	}
	/**
	 * @param updatedDate
	 *            The updatedDate to set.
	 */
	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	/**
	 * @return Returns the contFirstName.
	 */
	public String getContFirstName() {
		return contFirstName;
	}

	/**
	 * @param contFirstName
	 *            The contFirstName to set.
	 */
	public void setContFirstName(String contFirstName) {
		this.contFirstName = contFirstName;
	}

	/**
	 * @return Returns the contLastName.
	 */
	public String getContLastName() {
		return contLastName;
	}

	/**
	 * @param contLastName
	 *            The contLastName to set.
	 */
	public void setContLastName(String contLastName) {
		this.contLastName = contLastName;
	}
	/**
	 * @return Returns the programDescription.
	 */
	public String getProgramDescription() {
		return programDescription;
	}
	/**
	 * @param programDescription
	 *            The programDescription to set.
	 */
	public void setProgramDescription(String programDescription) {
		this.programDescription = programDescription;
	}
	/**
	 * @return Returns the contractors.
	 */
	public String getContractors() {
		return contractors;
	}
	/**
	 * @param contractors
	 *            The contractors to set.
	 */
	public void setContractors(String contractors) {
		this.contractors = contractors;
	}

	/**
	 * @return Returns the activityCreator.
	 */
	public AmpTeamMember getActivityCreator() {
		return activityCreator;
	}

	/**
	 * @param activityCreator
	 *            The activityCreator to set.
	 */
	public void setActivityCreator(AmpTeamMember activityCreator) {
		this.activityCreator = activityCreator;
	}
	/**
	 * @return Returns the mofedCntEmail.
	 */
	public String getMofedCntEmail() {
		return mofedCntEmail;
	}
	/**
	 * @param mofedCntEmail
	 *            The mofedCntEmail to set.
	 */
	public void setMofedCntEmail(String mofedCntEmail) {
		this.mofedCntEmail = mofedCntEmail;
	}
	/**
	 * @return Returns the mofedCntFirstName.
	 */
	public String getMofedCntFirstName() {
		return mofedCntFirstName;
	}
	/**
	 * @param mofedCntFirstName
	 *            The mofedCntFirstName to set.
	 */
	public void setMofedCntFirstName(String mofedCntFirstName) {
		this.mofedCntFirstName = mofedCntFirstName;
	}
	/**
	 * @return Returns the mofedCntLastName.
	 */
	public String getMofedCntLastName() {
		return mofedCntLastName;
	}
	/**
	 * @param mofedCntLastName
	 *            The mofedCntLastName to set.
	 */
	public void setMofedCntLastName(String mofedCntLastName) {
		this.mofedCntLastName = mofedCntLastName;
	}

	// Commented by Mikheil - in general, Hibernate classes do not need to
	// overrride
//this method, because it may lead to incorrect functinoality
/*
	 * public boolean equals(Object obj) { if (obj == null) throw new
	 * NullPointerException();
	 *
	 * if (!(obj instanceof AmpActivity)) throw new ClassCastException();
	 *
	 * AmpActivity act = (AmpActivity) obj; return
	 * this.ampActivityId.equals(act.getAmpActivityId()); }
    */

	/**
	 * @return Returns the regionalFundings.
	 */
	public Set getRegionalFundings() {
		return regionalFundings;
	}

	/**
	 * @param regionalFundings
	 *            The regionalFundings to set.
	 */
	public void setRegionalFundings(Set regionalFundings) {
		this.regionalFundings = regionalFundings;
	}



	/**
	 * @return Returns the approvalStatus.
	 */
	public String getApprovalStatus() {
		return approvalStatus;
	}
	/**
	 * @param approval_status
	 *            The approval_status to set.
	 */
	public void setApprovalStatus(String approvalStatus) {
		this.approvalStatus = approvalStatus;
	}

	/**
	 * @return Returns the survey.
	 */
	public Set getSurvey() {
		return survey;
	}

    public String getDocumentSpace() {
        return documentSpace;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public BigDecimal getFunAmount() {
        return FeaturesUtil.applyThousandsForVisibility(funAmount);
    }

    public Date getFunDate() {
        return funDate;
    }

    /**
	 * @param survey
	 *            The survey to set.
	 */
	public void setSurvey(Set survey) {
		this.survey = survey;
	}

    public void setDocumentSpace(String documentSpace) {
        this.documentSpace = documentSpace;
    }

    public void setCurrencyCode(String currenyCode) {
        this.currencyCode = currenyCode;
    }

    public void setFunAmount(BigDecimal funAmount) {
        this.funAmount = FeaturesUtil.applyThousandsForEntry(funAmount);
    }

    public void setFunDate(Date funDate) {
        this.funDate = funDate;
    }

	public Integer getLineMinRank() {
		return lineMinRank;
	}

	public void setLineMinRank(Integer lineMinRank) {
		this.lineMinRank = lineMinRank;
	}

	public Integer getPlanMinRank() {
		return planMinRank;
	}

	public void setPlanMinRank(Integer planMinRank) {
		this.planMinRank = planMinRank;
	}

	public Collection getActRankColl() {
		return actRankColl;
	}

    public Set getActivityPrograms() {
        return activityPrograms;
    }

    public Date getProposedCompletionDate() {
        return proposedCompletionDate;
    }

    public void setActRankColl(Collection actRankColl) {
		this.actRankColl = actRankColl;
	}

    public void setActivityPrograms(Set activityPrograms) {
        this.activityPrograms = activityPrograms;
    }

    public void setProposedCompletionDate(Date proposedCompletionDate) {
        this.proposedCompletionDate = proposedCompletionDate;
	}
	public Boolean getBudget() {
		return budget;
	}
	public void setBudget(Boolean budget) {
		this.budget = budget;
	}

	public AmpTeamMember getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(AmpTeamMember updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getContractingDate() {
		return contractingDate;
}
	public void setContractingDate(Date contractingDate) {
		this.contractingDate = contractingDate;
	}

	public Date getDisbursmentsDate() {
		return disbursmentsDate;
	}

	public void setDisbursmentsDate(Date disbursmentsDate) {
		this.disbursmentsDate = disbursmentsDate;
	}

	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

	public String getResults() {
		return results;
	}

	public void setResults(String results) {
		this.results = results;
	}

	public String getDnrCntFaxNumber() {
		return dnrCntFaxNumber;
	}

	public void setDnrCntFaxNumber(String dnrCntFaxNumber) {
		this.dnrCntFaxNumber = dnrCntFaxNumber;
	}

	public String getDnrCntOrganization() {
		return dnrCntOrganization;
	}

	public void setDnrCntOrganization(String dnrCntOrganization) {
		this.dnrCntOrganization = dnrCntOrganization;
	}

	public String getDnrCntPhoneNumber() {
		return dnrCntPhoneNumber;
	}

	public void setDnrCntPhoneNumber(String dnrCntPhoneNumber) {
		this.dnrCntPhoneNumber = dnrCntPhoneNumber;
	}

	public String getDnrCntTitle() {
		return dnrCntTitle;
	}

	public void setDnrCntTitle(String dnrCntTitle) {
		this.dnrCntTitle = dnrCntTitle;
	}

	public String getMfdCntFaxNumber() {
		return mfdCntFaxNumber;
	}

	public void setMfdCntFaxNumber(String mfdCntFaxNumber) {
		this.mfdCntFaxNumber = mfdCntFaxNumber;
	}

	public String getMfdCntOrganization() {
		return mfdCntOrganization;
	}

	public void setMfdCntOrganization(String mfdCntOrganization) {
		this.mfdCntOrganization = mfdCntOrganization;
	}

	public String getMfdCntPhoneNumber() {
		return mfdCntPhoneNumber;
	}

	public void setMfdCntPhoneNumber(String mfdCntPhoneNumber) {
		this.mfdCntPhoneNumber = mfdCntPhoneNumber;
	}

	public String getMfdCntTitle() {
		return mfdCntTitle;
	}

	public void setMfdCntTitle(String mfdCntTitle) {
		this.mfdCntTitle = mfdCntTitle;
	}

	public Object getObjectType() {
		return this.getClass().getName();
	}

	public Object getIdentifier() {
		return this.getAmpActivityId();
	}

	public String getObjectName() {
		return this.getAmpId()+" "+this.getName();
	}

	public String getFY() {
		return FY;
	}

	public void setFY(String fy) {
		FY = fy;
	}

	public Long getGbsSbs() {
		return gbsSbs;
	}

	public void setGbsSbs(Long gbsSbs) {
		this.gbsSbs = gbsSbs;
	}

	public String getProjectCode() {
		return projectCode;
	}

	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}

	public String getSubProgram() {
		return subProgram;
	}

	public void setSubProgram(String subProgram) {
		this.subProgram = subProgram;
	}

	public String getSubVote() {
		return subVote;
	}

	public void setSubVote(String subVote) {
		this.subVote = subVote;
	}

	public String getVote() {
		return vote;
	}

	public void setVote(String vote) {
		this.vote = vote;
	}

	public AmpTeamMember getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(AmpTeamMember createdBy) {
		this.createdBy = createdBy;
	}

	public String getLessonsLearned() {
		return lessonsLearned;
	}

	public void setLessonsLearned(String lessonsLearned) {
		this.lessonsLearned = lessonsLearned;
	}

	public String getEnvironment() {
		return environment;
	}

	public void setEnvironment(String environment) {
		this.environment = environment;
	}

	public String getEqualOpportunity() {
		return equalOpportunity;
	}

	public void setEqualOpportunity(String equalOpportunity) {
		this.equalOpportunity = equalOpportunity;
	}

	public String getMinorities() {
		return minorities;
	}

	public void setMinorities(String minorities) {
		this.minorities = minorities;
	}

	@Override
	public String[] getImplementedActions() {
		return IMPLEMENTED_ACTIONS;
	}

	@Override
	public Class getPermissibleCategory() {
		return AmpActivity.class;
	}

	public Set getReferenceDocs() {
		return referenceDocs;
	}

    public Boolean getDraft() {
        return draft;
    }

        public Set getActPrograms() {
                return actPrograms;
        }

        public void setReferenceDocs(Set referenceDocs) {
		this.referenceDocs = referenceDocs;
	}

    public void setDraft(Boolean draft) {
        this.draft = draft;
    }

        public void setActPrograms(Set actPrograms) {
                this.actPrograms = actPrograms;
        }

	public Long getActivityLevel() {
	    return activityLevel;
	}

	public void setActivityLevel(Long activityLevel) {
	    this.activityLevel = activityLevel;
	}

	public Set getComponentes() {
	    return componentes;
	}

	public void setComponentes(Set componentesSet) {
	    this.componentes = componentesSet;
	}

	public Boolean getGovernmentApprovalProcedures() {
	    return governmentApprovalProcedures;
	}

	public Boolean getJointCriteria() {
	    return jointCriteria;
	}

	public Set<IndicatorActivity> getIndicators() {
		return indicators;
	}

	public void setIndicators(Set<IndicatorActivity> indicators) {
		this.indicators = indicators;
	}

	public String getGovAgreementNumber() {
		return govAgreementNumber;
	}

	public void setGovAgreementNumber(String govAgreementNumber) {
		this.govAgreementNumber = govAgreementNumber;
	}

	public void setConvenioDateFilter(Date convenioDateFilter) {
		this.convenioDateFilter = convenioDateFilter;
	}

	public Date getConvenioDateFilter() {
		return convenioDateFilter;
	}

	public String getProjectImpact() {
		return projectImpact;
	}

	public void setProjectImpact(String projectImpact) {
		this.projectImpact = projectImpact;
	}

	public String getActivitySummary() {
		return activitySummary;
	}

	public void setActivitySummary(String activitySummary) {
		this.activitySummary = activitySummary;
	}

	public String getContractingArrangements() {
		return contractingArrangements;
	}

	public void setContractingArrangements(String contractingArrangements) {
		this.contractingArrangements = contractingArrangements;
	}

	public String getCondSeq() {
		return condSeq;
	}

	public void setCondSeq(String condSeq) {
		this.condSeq = condSeq;
	}

	public String getLinkedActivities() {
		return linkedActivities;
	}

	public void setLinkedActivities(String linkedActivities) {
		this.linkedActivities = linkedActivities;
	}

	public String getConditionality() {
		return conditionality;
	}

	public void setConditionality(String conditionality) {
		this.conditionality = conditionality;
	}

	public String getProjectManagement() {
		return projectManagement;
	}

	public void setProjectManagement(String projectManagement) {
		this.projectManagement = projectManagement;
	}

	public String getContractDetails() {
		return contractDetails;
	}

	public void setContractDetails(String contractDetails) {
		this.contractDetails = contractDetails;
	}

	public String getPrjCoFirstName() {
		return prjCoFirstName;
	}

	public void setPrjCoFirstName(String prjCoFirstName) {
		this.prjCoFirstName = prjCoFirstName;
	}

	public String getPrjCoLastName() {
		return prjCoLastName;
	}

	public void setPrjCoLastName(String prjCoLastName) {
		this.prjCoLastName = prjCoLastName;
	}

	public String getPrjCoEmail() {
		return prjCoEmail;
	}

	public void setPrjCoEmail(String prjCoEmail) {
		this.prjCoEmail = prjCoEmail;
	}

	public String getPrjCoTitle() {
		return prjCoTitle;
	}

	public void setPrjCoTitle(String prjCoTitle) {
		this.prjCoTitle = prjCoTitle;
	}

	public String getPrjCoOrganization() {
		return prjCoOrganization;
	}

	public void setPrjCoOrganization(String prjCoOrganization) {
		this.prjCoOrganization = prjCoOrganization;
	}

	public String getPrjCoPhoneNumber() {
		return prjCoPhoneNumber;
	}

	public void setPrjCoPhoneNumber(String prjCoPhoneNumber) {
		this.prjCoPhoneNumber = prjCoPhoneNumber;
	}

	public String getPrjCoFaxNumber() {
		return prjCoFaxNumber;
	}

	public void setPrjCoFaxNumber(String prjCoFaxNumber) {
		this.prjCoFaxNumber = prjCoFaxNumber;
	}

	public String getSecMiCntFirstName() {
		return secMiCntFirstName;
	}

	public void setSecMiCntFirstName(String secMiCntFirstName) {
		this.secMiCntFirstName = secMiCntFirstName;
	}

	public String getSecMiCntLastName() {
		return secMiCntLastName;
	}

	public void setSecMiCntLastName(String secMiCntLastName) {
		this.secMiCntLastName = secMiCntLastName;
	}

	public String getSecMiCntEmail() {
		return secMiCntEmail;
	}

	public void setSecMiCntEmail(String secMiCntEmail) {
		this.secMiCntEmail = secMiCntEmail;
	}

	public String getSecMiCntTitle() {
		return secMiCntTitle;
	}

	public void setSecMiCntTitle(String secMiCntTitle) {
		this.secMiCntTitle = secMiCntTitle;
	}

	public String getSecMiCntOrganization() {
		return secMiCntOrganization;
	}

	public void setSecMiCntOrganization(String secMiCntOrganization) {
		this.secMiCntOrganization = secMiCntOrganization;
	}

	public String getSecMiCntPhoneNumber() {
		return secMiCntPhoneNumber;
	}

	public void setSecMiCntPhoneNumber(String secMiCntPhoneNumber) {
		this.secMiCntPhoneNumber = secMiCntPhoneNumber;
	}

	public String getSecMiCntFaxNumber() {
		return secMiCntFaxNumber;
	}

	public void setSecMiCntFaxNumber(String secMiCntFaxNumber) {
		this.secMiCntFaxNumber = secMiCntFaxNumber;
	}

	public void setConvenioNumcont(String convenioNumcont) {
		this.convenioNumcont = convenioNumcont;
	}

	public String getConvenioNumcont() {
		return convenioNumcont;
	}

	public String getClasiNPD() {
	    return clasiNPD;
	}

	public void setClasiNPD(String clasiNPD) {
	    this.clasiNPD = clasiNPD;
	}
	
	public String toString(){
		if(name!=null) return name;
		return super.toString();
	}

	
	public Boolean isHumanitarianAid( ) {
		return this.humanitarianAid ;
	}

	public void setHumanitarianAid(Boolean humanitarianAid) {
		this.humanitarianAid = humanitarianAid;
	}

	public Boolean getHumanitarianAid() {
		return humanitarianAid;
	}

	public AmpTeamMember getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(AmpTeamMember approvedBy) {
		this.approvedBy = approvedBy;
	}

	public Date getApprovalDate() {
		return approvalDate;
	}

	public void setApprovalDate(Date approvalDate) {
		this.approvalDate = approvalDate;
	}

	public String getCrisNumber() {
		return crisNumber;
	}

	public void setCrisNumber(String crisNumber) {
		this.crisNumber = crisNumber;
	}

	public Set getContracts() {
		return contracts;
	}

	public void setContracts(Set contracts) {
		this.contracts = contracts;
	}

	
	public String getDonors() {
		return donors;
	}

	public void setDonors(String donors) {
		this.donors = donors;
	}

	public void setCustomField1(String customField1) {
		this.customField1 = customField1;
	}

	public String getCustomField1() {
		return customField1;
	}

	public String getBudgetCodeProjectID() {
		return budgetCodeProjectID;
	}

	public void setBudgetCodeProjectID(String budgetCodeProjectID) {
		this.budgetCodeProjectID = budgetCodeProjectID;
	}

	public void setCustomField2(String customField2) {
		this.customField2 = customField2;
	}

	public String getCustomField2() {
		return customField2;
	}

	public void setCustomField3(Long customField3) {
		this.customField3 = customField3;
	}

	public Long getCustomField3() {
		return customField3;
	}

	public void setCustomField4(Date customField4) {
		this.customField4 = customField4;
	}

	public Date getCustomField4() {
		return customField4;
	}

	public void setCustomField5(String customField5) {
		this.customField5 = customField5;
	}

	public String getCustomField5() {
		return customField5;
	}

	public void setCustomField6(Boolean customField6) {
		this.customField6 = customField6;
	}

	public Boolean getCustomField6() {
		return customField6;
	}

	public String getProjectComments() {
		return this.projectComments;
	}

	public void setProjectComments(String projectComments) {
		this.projectComments = projectComments;
	}

	public void setComponentFundings(Set<AmpComponentFunding> componentFundings) {
		this.componentFundings = componentFundings;
	}

	public Set<AmpComponentFunding> getComponentFundings() {
		return componentFundings;
	}

}
