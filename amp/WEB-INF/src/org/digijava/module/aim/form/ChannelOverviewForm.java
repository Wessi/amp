package org.digijava.module.aim.form ;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.digijava.module.aim.helper.Activity;
import org.digijava.module.aim.helper.OrgProjectId;
import org.digijava.module.aim.util.ActivityUtil;

public class ChannelOverviewForm extends MainProjectDetailsForm
{
	private Long id ;
	//private String ampId ;
	private String objective ;
	private String status ;
	private String language ;
	private String version ;
	private Collection sectors ;
	private Collection subSectors ;
	private String country;
	private Collection locations;
	private String fundingagency ;
	private String reportingagency;
	private String implagency;
	private String relatedins;
	private String level ;
	private Collection internalIds ;
	private String modality ;
	private String theme;
	private String modalityCode;
	private int flag;
	private int flago;
	private String activityStartDate;
	private String activityCloseDate;
	private String condition;
	private String grandTotal;
	private boolean write ;
	private boolean delete ;
	private boolean validLogin;
	private Collection assistance ;
	private Integer pageNo;
	private String currCode;
	private Collection modal;
	private boolean canView;
	private HashMap allComments;
	private Activity activity;
	private String implemLocationLevel;
	private int numImplLocationLevels	= 0;
	private String buttonText;  // added by Akash for activity approval
    private List primaryPrograms;
    private List secondaryPrograms;
    private List nationalPlanObjectivePrograms;
        
    private OrgProjectId selectedOrganizations[]; //To Show organitations name in channel overview
	private    HashMap<String,String> errors = new HashMap<String, String>();
	private    HashMap<String,String> messages = new HashMap<String, String>();

	private List classificationConfigs;
	
	public void addMessage(String key, String value) {
	    this.messages.put(key, value) ;
	}

	public void addError(String key, String value) {
	    this.errors.put(key, value) ;
	}

	public void clearMessages(){
        this.errors.clear();
	    this.messages.clear();
	}
    /**
     * 
     * @return
     */
    public String getImplemLocationLevel() {
		return implemLocationLevel;
	}
    
    /**
     * 
     * @param implemLocationLevel
     */
    
	public void setImplemLocationLevel(String implemLocationLevel) {
		this.implemLocationLevel = implemLocationLevel;
	}
    
    /**
	 * @return Returns the selectedOrganizations.
	 */
	public OrgProjectId[] getSelectedOrganizations() {
		return selectedOrganizations;
	}

	/**
	 * @param selectedOrganizations
	 *            The selectedOrganizations to set.
	 */
	public void setSelectedOrganizations(OrgProjectId[] selectedOrganizations) {
		this.selectedOrganizations = selectedOrganizations;
	}

	public Integer getPageNo() {
			  return pageNo;
	}

	public void setPageNo(Integer page) {
			  pageNo = page;
	}

	private boolean add;  // added by Priyajith

	public boolean getAdd() {
			  return add;
	}

	public void setAdd(boolean flag) {
			  add = flag;
	}

	public Long getId()
	{
		return id ;
	}

	/*
	public String getAmpId()
	{
		return ampId;
	}*/

	public String getLanguage()
	{
		return language;
	}

	public String getObjective()
	{
		return objective;
	}

	public String getStatus()
	{
		return status;
	}

	public String getVersion()
	{
		return version;
	}

	public String getLevel()
	{
		return level;
	}

	public boolean getWrite()
	{
		return write;
	}

	public boolean getDelete()
	{
		return delete;
	}

	public boolean getValidLogin()
	{
		return validLogin;
	}

	public int getFlag()
	{
		return flag;
	}

	public int getFlago()
	{
		return flago;
	}

	public String getActivityStartDate()
	{
		return activityStartDate;
	}

	public String getActivityCloseDate()
	{
		return activityCloseDate;
	}

	public String getCondition()
	{
		return condition;
	}

	public String getGrandTotal()
	{
		return grandTotal;
	}

	public Collection getAssistance()
	{
		return assistance;
	}

	public void setId(Long id)
	{
		this.id = id ;
	}

	public void setLanguage(String language)
	{
		this.language = language;
	}

	public void setObjective(String objective)
	{
		this.objective = objective ;
	}

	public void setStatus(String status)
	{
		this.status = status ;
	}

	public void setVersion(String version)
	{
		this.version = version ;
	}

	public void setActivityStartDate(String string)
	{
		this.activityStartDate = string ;
	}

	public void setActivityCloseDate(String string)
	{
		this.activityCloseDate = string ;
	}

	public void setCondition(String string)
	{
		this.condition = string ;
	}

	public void setGrandTotal(String string)
	{
		this.grandTotal = string ;
	}

	public void setWrite(boolean bool)
	{
		this.write = bool ;
	}

	public void setDelete(boolean bool)
	{
		this.delete = bool ;
	}

	public void setValidLogin(boolean bool)
	{
		this.validLogin = bool ;
	}

	public Collection getSectors()
	{
		return sectors;
	}


	public void setSectors(Collection sectors)
	{
		this.sectors = sectors ;
	}


	public Collection getSubSectors()
	{
		return subSectors;
	}


	public void setSubSectors(Collection collection)
	{
		subSectors = collection;
	}

	public String getCountry()
	{
		return country;
	}

	public void setCountry(String country)
	{

		this.country = country;
	}
	public Collection getLocations()
	{
		return locations;
	}

	public void setLocations(Collection locations)
	{

		this.locations = locations;
	}
	public String getFundingagency()
	{
		return fundingagency ;
	}
	public void setFundingagency(String fundingagency)
	{
		this.fundingagency = fundingagency ;
	}

	public String getReportingagency()
	{
		return reportingagency;
	}
	public void setReportingagency(String reportingagency)
	{
		this.reportingagency= reportingagency;
	}


	public String getImplagency()
	{
		return implagency;
	}
	public void setImplagency(String implagency)
	{
		this.implagency= implagency;
	}


	public String getRelatedins()
	{
		return relatedins;
	}

	/*
	public void setAmpId(String string) {
		ampId = string;
	}*/

	public void setRelatedins(String relatedins)
	{
		this.relatedins= relatedins;
	}

	public void setLevel(String level)
	{
		this.level = level;
	}

	public void setFlago(int flago)
	{
		this.flago = flago;
	}

	public void setFlag(int flag)
	{
		this.flag = flag;
	}

	/**
	 * @return
	 */
	public Collection getInternalIds() {
		return internalIds;
	}

	/**
	 * @param collection
	 */
	public void setInternalIds(Collection collection) {
		internalIds = collection;
	}

	public String getModality() {
		return modality;
	}

	public void setModality(String string) {
		modality = string;
	}

	public String getModalityCode() {
		return modalityCode;
	}

	public void setModalityCode(String string) {
		modalityCode = string;
	}

	public String getTheme() {
		return theme;
	}

	public void setTheme(String string) {
		theme = string;
	}

	public void setAssistance(Collection c)
	{
		assistance = c ;
	}

	/**
	 * @return Returns the activity.
	 */
	public Activity getActivity() {
		return activity;
	}
	/**
	 * @param activity The activity to set.
	 */
	public void setActivity(Activity activity) {
		this.activity = activity;
	}
	/**
	 * @return Returns the currCode.
	 */
	public String getCurrCode() {
		return currCode;
	}
	/**
	 * @param currCode The currCode to set.
	 */
	public void setCurrCode(String currCode) {
		this.currCode = currCode;
	}

	/**
	 * @return Returns the modal.
	 */
	public Collection getModal() {
		return modal;
	}
	/**
	 * @param modal The modal to set.
	 */
	public void setModal(Collection modal) {
		this.modal = modal;
	}
	/**
	 * @return Returns the buttonText.
	 */
	public String getButtonText() {
		return buttonText;
	}
	/**
	 * @param buttonText The buttonText to set.
	 */
	public void setButtonText(String buttonText) {
		this.buttonText = buttonText;
	}

	/**
	 * @return Returns the canView.
	 */
	public boolean isCanView() {
		return canView;
	}

	/**
	 * @param canView The canView to set.
	 */
	public void setCanView(boolean canView) {
		this.canView = canView;
	}

	public HashMap getAllComments() {
		return allComments;
	}

        public List getSecondaryPrograms() {
                return secondaryPrograms;
        }

        public List getPrimaryPrograms() {
                return primaryPrograms;
        }

        public List getNationalPlanObjectivePrograms() {
                return nationalPlanObjectivePrograms;
        }

        public void setAllComments(HashMap allComments) {
		this.allComments = allComments;
	}

        public void setSecondaryPrograms(List secondaryPrograms) {
                this.secondaryPrograms = secondaryPrograms;
        }

        public void setPrimaryPrograms(List primaryPrograms) {
                this.primaryPrograms = primaryPrograms;
        }

        public void setNationalPlanObjectivePrograms(List
            nationalPlanObjectivePrograms) {
                this.nationalPlanObjectivePrograms =
                    nationalPlanObjectivePrograms;
        }

		public int getNumImplLocationLevels() {
			return numImplLocationLevels;
		}

		public void setNumImplLocationLevels(int numImplLocationLevels) {
			this.numImplLocationLevels = numImplLocationLevels;
		}
                
                public boolean getImplLocationCountry(){
                    boolean flag=ActivityUtil.isImplLocationCountry(activity.getActivityId());
                    return flag;
                }

				public HashMap<String, String> getErrors() {
					return errors;
				}

				public void setErrors(HashMap<String, String> errors) {
					this.errors = errors;
				}

				public HashMap<String, String> getMessages() {
					return messages;
				}

				public void setMessages(HashMap<String, String> messages) {
					this.messages = messages;
				}


			    public List getClassificationConfigs() {
			        return classificationConfigs;
			    }

			    public void setClassificationConfigs(List classificationConfigs) {
			        this.classificationConfigs = classificationConfigs;
			    }


				
				
}
