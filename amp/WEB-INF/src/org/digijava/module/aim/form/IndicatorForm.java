package org.digijava.module.aim.form;

import java.io.Serializable;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.LabelValueBean;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpTheme;



public class IndicatorForm extends ActionForm implements Serializable
{
	private Long indId = null;
	private Long activityId;
	private String indicatorName = null;
	private String indicatorDesc = null;
	private String indicatorCode = null;
	private String searchKey = null;
	private boolean defaultFlag;
	private Long selectedIndicator = null;
	private Collection searchReturn = null;
	private Collection indicators = null;
	private Collection nondefaultindicators = null;	
	private Collection indicatorValues = null;
	private Collection meIndActList = null;
	private String sameIndicatorName = null;
	private String sameIndicatorCode = null;
	private boolean errorFlag;
	private String event;
	private Long selectedIndicators[];
	private Long selIndicators[];
	private String searchkey = null;
	private String addswitch = null;
	private boolean noSearchResult = false;
	
	private Collection  allSectors;
	private int tempNumResults;
	private Collection pagedCol;
	private Integer currentPage;
	private String currentAlpha;
	private String step = null;
	private int item;
	private Integer selectedindicatorFromPages;
	private String sectorName;
    private String action;
   	private String[] alphaPages;
    private int numResults;
    private boolean startAlphaFlag;
    private Collection cols = null;
    private Collection pages;
    private Collection colsAlpha = null;
    private Long indid[]; // list of ind selected from
    private String name;
    
    //// class NewIndicatorForm
    private Long id;
    private String description;
    private String code;
    private String date;
    private Integer category;
    private String type;
    private int indType;
    private int indicatorType;
    private String keyword;

    private Collection<AmpTheme> programsCol;
    private Collection<AmpActivity> ActivitiesCol;
    private Long selectedProgramId;
    private Collection<LabelValueBean> selectedPrograms;
    private Long selectedActivityId;
    private Collection<LabelValueBean> selectedActivities;
    private Long selActivitySector[];
    private Long themeId;
    private boolean sectorReset;
    private Long sectorScheme;
    private Collection sectorSchemes;
    private Collection parentSectors;
	private Collection childSectorsLevel1;
	private Collection childSectorsLevel2;
	private Long sector;
	private Long subsectorLevel1;
	private Long subsectorLevel2;
	private Collection activitySectors;
    
    
    
    
   	
	private char ascendingInd;
	
	public String getSameIndicatorCode() {
		return sameIndicatorCode;
	}

	public void setSameIndicatorCode(String sameIndicatorCode) {
		this.sameIndicatorCode = sameIndicatorCode;
	}

	public String getSameIndicatorName() {
		return sameIndicatorName;
	}

	public void setSameIndicatorName(String sameIndicatorName) {
		this.sameIndicatorName = sameIndicatorName;
	}

	public Collection getIndicators() {
		return indicators;
	}

	public void setIndicators(Collection indicators) {
		this.indicators = indicators;
	}

	public boolean getDefaultFlag() {
		return defaultFlag;
	}

	public String getIndicatorCode() {
		return indicatorCode;
	}

	public void setIndicatorCode(String indicatorCode) {
		this.indicatorCode = indicatorCode;
	}

	public String getIndicatorDesc() {
		return indicatorDesc;
	}

	public void setIndicatorDesc(String indicatorDesc) {
		this.indicatorDesc = indicatorDesc;
	}

	public String getIndicatorName() {
		return indicatorName;
	}

	public void setIndicatorName(String indicatorName) {
		this.indicatorName = indicatorName;
	}

	public Collection getIndicatorValues() {
		return indicatorValues;
	}

	public void setIndicatorValues(Collection indicatorValues) {
		this.indicatorValues = indicatorValues;
	}

	public Collection getMeIndActList() {
		return meIndActList;
	}

	public void setMeIndActList(Collection meIndActList) {
		this.meIndActList = meIndActList;
	}

	public Long getSelectedIndicator() {
		return selectedIndicator;
	}

	public void setSelectedIndicator(Long selectedIndicator) {
		this.selectedIndicator = selectedIndicator;
	}

	public String getSearchKey() {
		return searchKey;
	}

	public void setSearchKey(String searchKey) {
		this.searchKey = searchKey;
	}

	public Collection getSearchReturn() {
		return searchReturn;
	}

	public void setSearchReturn(Collection searchReturn) {
		this.searchReturn = searchReturn;
	}

	public Long getIndId() {
		return indId;
	}

	public void setIndId(Long indId) {
		this.indId = indId;
	}

	public boolean isErrorFlag() {
		return errorFlag;
	}

	public void setErrorFlag(boolean errorFlag) {
		this.errorFlag = errorFlag;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	/**
	 * @return Returns the nondefaultindicators.
	 */
	public Collection getNondefaultindicators() {
		return nondefaultindicators;
	}

	/**
	 * @param nondefaultindicators The nondefaultindicators to set.
	 */
	public void setNondefaultindicators(Collection nondefaultindicators) {
		this.nondefaultindicators = nondefaultindicators;
	}

	/**
	 * @return Returns the searchkey.
	 */
	public String getSearchkey() {
		return searchkey;
	}

	/**
	 * @param searchkey The searchkey to set.
	 */
	public void setSearchkey(String searchkey) {
		this.searchkey = searchkey;
	}

	/**
	 * @return Returns the selectedIndicators.
	 */
	public Long[] getSelectedIndicators() {
		return selectedIndicators;
	}

	/**
	 * @param selectedIndicators The selectedIndicators to set.
	 */
	public void setSelectedIndicators(Long[] selectedIndicators) {
		this.selectedIndicators = selectedIndicators;
	}

	/**
	 * @return Returns the activityId.
	 */
	public Long getActivityId() {
		return activityId;
	}

	/**
	 * @param activityId The activityId to set.
	 */
	public void setActivityId(Long activityId) {
		this.activityId = activityId;
	}

	/**
	 * @return Returns the addswitch.
	 */
	public String getAddswitch() {
		return addswitch;
	}

	/**
	 * @param addswitch The addswitch to set.
	 */
	public void setAddswitch(String addswitch) {
		this.addswitch = addswitch;
	}

	/**
	 * @return Returns the selIndicators.
	 */
	public Long[] getSelIndicators() {
		return selIndicators;
	}

	/**
	 * @param selIndicators The selIndicators to set.
	 */
	public void setSelIndicators(Long[] selIndicators) {
		this.selIndicators = selIndicators;
	}

	/**
	 * @return Returns the noSearchResult.
	 */
	public boolean getNoSearchResult() {
		return noSearchResult;
	}

	/**
	 * @param noSearchResult The noSearchResult to set.
	 */
	public void setNoSearchResult(boolean noSearchResult) {
		this.noSearchResult = noSearchResult;
	}

	/**
	 * @return Returns the ascendingInd.
	 */
	public char getAscendingInd() {
		return ascendingInd;
	}

	/**
	 * @param ascendingInd The ascendingInd to set.
	 */
	public void setAscendingInd(char ascendingInd) {
		this.ascendingInd = ascendingInd;
	}

	/**
	 * @return Returns the defaultFlag.
	 */
	public boolean isDefaultFlag() {
		return defaultFlag;
}
	/**
	 * @param defaultFlag The defaultFlag to set.
	 */
	public void setDefaultFlag(boolean defaultFlag) {
		this.defaultFlag = defaultFlag;
	}

	/* (non-Javadoc)
	 * @see org.apache.struts.action.ActionForm#reset(org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
	 */
	public void reset(ActionMapping arg0, HttpServletRequest arg1) {
		defaultFlag = false;
	}

	public Collection getAllSectors() {
		return allSectors;
	}

	public void setAllSectors(Collection allSectors) {
		this.allSectors = allSectors;
	}

	public int getTempNumResults() {
		return tempNumResults;
	}

	public void setTempNumResults(int tempNumResults) {
		this.tempNumResults = tempNumResults;
	}

	public Collection getPagedCol() {
		return pagedCol;
	}

	public void setPagedCol(Collection pagedCol) {
		this.pagedCol = pagedCol;
	}

	public Integer getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}

	public String getCurrentAlpha() {
		return currentAlpha;
	}

	public void setCurrentAlpha(String currentAlpha) {
		this.currentAlpha = currentAlpha;
	}

	public String getStep() {
		return step;
	}

	public void setStep(String step) {
		this.step = step;
	}

	public int getItem() {
		return item;
	}

	public void setItem(int item) {
		this.item = item;
	}

	public Integer getSelectedindicatorFromPages() {
		return selectedindicatorFromPages;
	}

	public void setSelectedindicatorFromPages(Integer selectedindicatorFromPages) {
		this.selectedindicatorFromPages = selectedindicatorFromPages;
	}

	public String getSectorName() {
		return sectorName;
	}

	public void setSectorName(String sectorName) {
		this.sectorName = sectorName;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String[] getAlphaPages() {
		return alphaPages;
	}

	public void setAlphaPages(String[] alphaPages) {
		this.alphaPages = alphaPages;
	}

	public int getNumResults() {
		return numResults;
	}

	public void setNumResults(int numResults) {
		this.numResults = numResults;
	}

	public boolean isStartAlphaFlag() {
		return startAlphaFlag;
	}

	public void setStartAlphaFlag(boolean startAlphaFlag) {
		this.startAlphaFlag = startAlphaFlag;
	}

	public Collection getCols() {
		return cols;
	}

	public void setCols(Collection cols) {
		this.cols = cols;
	}

	public Collection getPages() {
		return pages;
	}

	public void setPages(Collection pages) {
		this.pages = pages;
	}

	public Collection getColsAlpha() {
		return colsAlpha;
	}

	public void setColsAlpha(Collection colsAlpha) {
		this.colsAlpha = colsAlpha;
	}

	public Long[] getIndid() {
		return indid;
	}

	public void setIndid(Long[] indid) {
		this.indid = indid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Integer getCategory() {
		return category;
	}

	public void setCategory(Integer category) {
		this.category = category;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getIndType() {
		return indType;
	}

	public void setIndType(int indType) {
		this.indType = indType;
	}

	public int getIndicatorType() {
		return indicatorType;
	}

	public void setIndicatorType(int indicatorType) {
		this.indicatorType = indicatorType;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public Collection<AmpTheme> getProgramsCol() {
		return programsCol;
	}

	public void setProgramsCol(Collection<AmpTheme> programsCol) {
		this.programsCol = programsCol;
	}

	public Collection<AmpActivity> getActivitiesCol() {
		return ActivitiesCol;
	}

	public void setActivitiesCol(Collection<AmpActivity> activitiesCol) {
		ActivitiesCol = activitiesCol;
	}

	public Long getSelectedProgramId() {
		return selectedProgramId;
	}

	public void setSelectedProgramId(Long selectedProgramId) {
		this.selectedProgramId = selectedProgramId;
	}

	public Collection<LabelValueBean> getSelectedPrograms() {
		return selectedPrograms;
	}

	public void setSelectedPrograms(Collection<LabelValueBean> selectedPrograms) {
		this.selectedPrograms = selectedPrograms;
	}

	public Long getSelectedActivityId() {
		return selectedActivityId;
	}

	public void setSelectedActivityId(Long selectedActivityId) {
		this.selectedActivityId = selectedActivityId;
	}

	public Collection<LabelValueBean> getSelectedActivities() {
		return selectedActivities;
	}

	public void setSelectedActivities(Collection<LabelValueBean> selectedActivities) {
		this.selectedActivities = selectedActivities;
	}

	public Long[] getSelActivitySector() {
		return selActivitySector;
	}

	public void setSelActivitySector(Long[] selActivitySector) {
		this.selActivitySector = selActivitySector;
	}

	public Long getThemeId() {
		return themeId;
	}

	public void setThemeId(Long themeId) {
		this.themeId = themeId;
	}

	public boolean isSectorReset() {
		return sectorReset;
	}

	public void setSectorReset(boolean sectorReset) {
		this.sectorReset = sectorReset;
	}

	public Long getSectorScheme() {
		return sectorScheme;
	}

	public void setSectorScheme(Long sectorScheme) {
		this.sectorScheme = sectorScheme;
	}

	public Collection getSectorSchemes() {
		return sectorSchemes;
	}

	public void setSectorSchemes(Collection sectorSchemes) {
		this.sectorSchemes = sectorSchemes;
	}

	public Collection getParentSectors() {
		return parentSectors;
	}

	public void setParentSectors(Collection parentSectors) {
		this.parentSectors = parentSectors;
	}

	public Collection getChildSectorsLevel1() {
		return childSectorsLevel1;
	}

	public void setChildSectorsLevel1(Collection childSectorsLevel1) {
		this.childSectorsLevel1 = childSectorsLevel1;
	}

	public Collection getChildSectorsLevel2() {
		return childSectorsLevel2;
	}

	public void setChildSectorsLevel2(Collection childSectorsLevel2) {
		this.childSectorsLevel2 = childSectorsLevel2;
	}

	public Long getSector() {
		return sector;
	}

	public void setSector(Long sector) {
		this.sector = sector;
	}

	public Long getSubsectorLevel1() {
		return subsectorLevel1;
	}

	public void setSubsectorLevel1(Long subsectorLevel1) {
		this.subsectorLevel1 = subsectorLevel1;
	}

	public Long getSubsectorLevel2() {
		return subsectorLevel2;
	}

	public void setSubsectorLevel2(Long subsectorLevel2) {
		this.subsectorLevel2 = subsectorLevel2;
	}

	public Collection getActivitySectors() {
		return activitySectors;
	}

	public void setActivitySectors(Collection activitySectors) {
		this.activitySectors = activitySectors;
	}
}
