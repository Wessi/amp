package org.digijava.module.dataExchange.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import org.digijava.kernel.entity.Message;
import org.digijava.kernel.exception.DgException;

import org.digijava.module.aim.dbentity.*;
import org.digijava.module.aim.helper.Components;
import org.digijava.module.aim.helper.Constants;
import org.digijava.module.aim.helper.FundingDetail;
import org.digijava.module.aim.helper.PhysicalProgress;
import org.digijava.module.aim.util.*;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.digijava.module.dataExchange.Exception.AmpExportException;
import org.digijava.module.dataExchange.jaxb.ActivityType;
import org.digijava.module.dataExchange.jaxb.CodeValueType;
import org.digijava.module.dataExchange.jaxb.ComponentFundingType;
import org.digijava.module.dataExchange.jaxb.ContactType;
import org.digijava.module.dataExchange.jaxb.DateType;
import org.digijava.module.dataExchange.jaxb.FreeTextType;
import org.digijava.module.dataExchange.jaxb.FundingDetailType;
import org.digijava.module.dataExchange.jaxb.FundingType;
import org.digijava.module.dataExchange.jaxb.LocationFundingType;
import org.digijava.module.dataExchange.jaxb.ObjectFactory;
import org.digijava.module.dataExchange.jaxb.PercentageCodeValueType;
import org.digijava.module.dataExchange.type.AmpColumnEntry;



public class ExportBuilder {

	private AmpActivity ampActivity = null;

	private ObjectFactory objectFactory = new ObjectFactory();

	private String siteId =  null; //RequestUtils.getSite(request).getSiteId()

	public ExportBuilder(AmpActivity ampActivity, String siteId){
		this.ampActivity = ampActivity;
		this.siteId = siteId;
	}

	public ExportBuilder(Long ampActivityId, String siteId) throws AmpExportException{
		try{
			this.ampActivity = ActivityUtil.loadActivity(ampActivityId);
			this.siteId = siteId;
		} catch (DgException ex){
			throw new AmpExportException(ex, AmpExportException.ACTIVITY_LOAD);
		}	
	}

	public ExportBuilder(long ampActivityId, String siteId) throws AmpExportException{
		this(new Long(ampActivityId), siteId);
	}

	public ActivityType getActivityType(AmpColumnEntry ampColumnEntry) throws AmpExportException{
		ActivityType retValue = objectFactory.createActivityType();
		retValue.setDbKey(ampActivity.getAmpActivityId().toString());
		retValue.setDate(ExportHelper.getGregorianCalendar(ampActivity.getCreatedDate()));
		
		for (AmpColumnEntry elem : ampColumnEntry.getElements()) {
			if (elem.canExport()){
				buildActivityType(retValue, elem);
			}
		}

		return retValue;
	}


	private void buildActivityType(ActivityType parent, AmpColumnEntry ampColumnEntry) throws AmpExportException{
		String path = ampColumnEntry.getPath();

		if (path.equalsIgnoreCase("activity.id")){
			if (ampActivity.getInternalIds() != null && ampActivity.getInternalIds().size() > 0){
				for (Iterator iterator = ampActivity.getInternalIds().iterator(); iterator.hasNext();) {
					AmpActivityInternalId ids = (AmpActivityInternalId) iterator.next();
					parent.getId().add(buildActivityTypeId(ids));
				}
			} else {
				throw new AmpExportException("Id is empty", AmpExportException.ACTIVITY_DATA_INEFFICIENT);
			}
		} else if (path.equalsIgnoreCase("activity.title")){
			if (ampActivity.getName() != null){
				parent.getTitle().add(buildFreeText(null, ampActivity.getName()));
			} else {
				throw new AmpExportException("Name is empty", AmpExportException.ACTIVITY_DATA_INEFFICIENT);
			}
				
		} else if (path.equalsIgnoreCase("activity.objective")){
			if (ampActivity.getObjective() != null){
				for (Message msg : ExportHelper.getTranslations(ampActivity.getObjective(),ampActivity.getObjective(), siteId)) {
					parent.getObjective().add(buildFreeText(msg));
				}
			}
		} else if (path.equalsIgnoreCase("activity.description")){
			if (ampActivity.getDescription() != null){
				for (Message msg : ExportHelper.getTranslations(ampActivity.getDescription(),ampActivity.getDescription(), siteId)) {
					parent.getDescription().add(buildFreeText(msg));
				}
			}
		} else if (path.equalsIgnoreCase("activity.implementationLevels")){
			parent.setImplementationLevels(buildImplementationLevel());
		} else if (path.equalsIgnoreCase("activity.location")){
			if (ampActivity.getLocations() != null){
				for (Iterator iterator = ampActivity.getLocations().iterator(); iterator.hasNext();) {
					AmpActivityLocation ampLocation = (AmpActivityLocation) iterator.next();
					parent.getLocation().add(buildLocation(ampLocation.getLocation(), ampColumnEntry.getElementByName("funding")));
				}
			}
		} else if (path.equalsIgnoreCase("activity.proposedApprovalDate")){
			parent.setProposedApprovalDate(buildDate(ampActivity.getProposedApprovalDate(), ampColumnEntry.isMandatory() ));
		} else if (path.equalsIgnoreCase("activity.actualApprovalDate")){
			parent.setActualApprovalDate(buildDate(ampActivity.getActivityApprovalDate(), ampColumnEntry.isMandatory()));
		} else if (path.equalsIgnoreCase("activity.proposedStartDate")){
			parent.setProposedStartDate(buildDate(ampActivity.getProposedStartDate(), ampColumnEntry.isMandatory()));
		} else if (path.equalsIgnoreCase("activity.actualStartDate")){
			parent.setActualStartDate(buildDate(ampActivity.getActualStartDate(), ampColumnEntry.isMandatory()));
		} else if (path.equalsIgnoreCase("activity.modifiedClosingDate")){
			parent.setModifiedClosingDate(buildDate(ampActivity.getActualCompletionDate(), ampColumnEntry.isMandatory()));
		} else if (path.equalsIgnoreCase("activity.closingDate")){
			parent.setModifiedClosingDate(buildDate(ampActivity.getActivityCloseDate(), ampColumnEntry.isMandatory()));
		} else if (path.equalsIgnoreCase("activity.status")){
			if (ampActivity.getApprovalStatus() != null){
				parent.setStatus(buildCodeValue(ampActivity.getApprovalStatus()));
			} else {
				throw new AmpExportException("Status is empty", AmpExportException.ACTIVITY_DATA_INEFFICIENT);
			}
		} else if (path.equalsIgnoreCase("activity.statusReason")){
			parent.setStatusReason(buildFreeText(ampActivity.getStatusReason()));
		} else if (path.equalsIgnoreCase("activity.sectors")){
			if (ampActivity.getSectors() != null && ampActivity.getSectors().size() > 0){
				for (Iterator iterator = ampActivity.getSectors().iterator(); iterator.hasNext();) {
					AmpActivitySector ampSector = (AmpActivitySector) iterator.next();
					if (ampSector.getSectorPercentage() != null){
						parent.getSectors().add(buildPercentageCodeValue(ampSector.getSectorId().getSectorCode(),
								ampSector.getSectorId().getName(),
								ampSector.getSectorPercentage().floatValue()));
					} else {
						throw new AmpExportException("Sector Precent is empty", AmpExportException.ACTIVITY_DATA_INEFFICIENT);
					}
				}
			} else {
				throw new AmpExportException("Sector is empty", AmpExportException.ACTIVITY_DATA_INEFFICIENT);
			}
		} else if (path.equalsIgnoreCase("activity.programs")){
			if (ampActivity.getActivityPrograms() != null && ampActivity.getActivityPrograms().size() > 0){
				for (Iterator iterator = ampActivity.getActivityPrograms().iterator(); iterator.hasNext();) {
					AmpActivityProgram ampProgram = (AmpActivityProgram) iterator.next();
					if (ampProgram.getProgramPercentage() != null){
					parent.getPrograms().add(buildPercentageCodeValue(ampProgram.getProgram().getThemeCode(),
							ampProgram.getProgram().getName(),
							ampProgram.getProgramPercentage().floatValue()));
					} else {
						throw new AmpExportException("Programs.Precent is empty", AmpExportException.ACTIVITY_DATA_INEFFICIENT);
					}
				}
			}
		} else if (path.equalsIgnoreCase("activity.notes")){
			if (ampActivity.getNotes()  != null){
				for (Iterator iterator = ampActivity.getNotes().iterator(); iterator.hasNext();) {
					AmpNotes ampNotes = (AmpNotes) iterator.next();
					parent.getNotes().add(buildFreeText(ampNotes.getLanguage(),ampNotes.getDescription()));
				}
			}
		} else if (path.equalsIgnoreCase("activity.funding")){
			for (Iterator iterator = ampActivity.getFunding().iterator(); iterator.hasNext();) {
				AmpFunding ampFunding = (AmpFunding) iterator.next();
				parent.getFunding().add(buildFunding(ampFunding, ampColumnEntry));			
			}
		} else if (path.equalsIgnoreCase("activity.keywords")){
			// at this moment do not have in amp
		} else if (path.equalsIgnoreCase("activity.components")){
			for (Components<FundingDetail> component : ActivityUtil.getAllComponents(ampActivity.getAmpActivityId().longValue())) {
				parent.getComponent().add(buildComponent(component, ampColumnEntry));
			}
		} else if (path.equalsIgnoreCase("activity.issues")){
			if (ampActivity.getIssues()  != null){
				for (Iterator iterator = ampActivity.getIssues().iterator(); iterator.hasNext();) {
					AmpIssues issue = (AmpIssues) iterator.next();
					parent.getIssues().add(buildIssue(issue, ampColumnEntry));
				}
			}	
		} else if (path.equalsIgnoreCase("activity.documents")){
			if (ampActivity.getDocuments()  != null){
				for (Iterator iterator = ampActivity.getDocuments().iterator(); iterator.hasNext();) {
					CMSContentItem item = (CMSContentItem) iterator.next();
					if (item.getIsFile()){
						ActivityType.Documents doc = objectFactory.createActivityTypeDocuments();
						doc.setDescription(item.getDescription());
						doc.setTitle(item.getTitle());
						if (item.getLanguage() != null){
							doc.setLang(item.getLanguage().getCode());
						}
						parent.getDocuments().add(doc);
					}
				}
			}	
		} else if (path.equalsIgnoreCase("activity.relatedLinks")){
			if (ampActivity.getDocuments()  != null){
				for (Iterator iterator = ampActivity.getDocuments().iterator(); iterator.hasNext();) {
					CMSContentItem item = (CMSContentItem) iterator.next();
					if (!item.getIsFile()){
						ActivityType.RelatedLinks link = objectFactory.createActivityTypeRelatedLinks();
						link.setDescription(item.getDescription());
						link.setLabel(item.getTitle());
						if (item.getLanguage() != null){
							link.setLang(item.getLanguage().getCode());
						}
						parent.getRelatedLinks().add(link);
					}
				}
			}	
		} else if (path.equalsIgnoreCase("activity.relatedOrgs")){
			if (ampActivity.getOrgrole() != null){
				for (AmpOrgRole ampOrgRole : ampActivity.getOrgrole()) {
					ActivityType.RelatedOrgs org = objectFactory.createActivityTypeRelatedOrgs();
					
					org.setValue(ampOrgRole.getOrganisation().getName());
					org.setCode(ampOrgRole.getOrganisation().getOrgCode());
//TODO					org.setLang("");

					if (ampOrgRole.getRole().getRoleCode().equalsIgnoreCase(Constants.REPORTING_AGENCY)){
						org.setType(DataExchangeConstants.ORG_ROLE_REPORTING_AGENCY);
					} else if (ampOrgRole.getRole().getRoleCode().equalsIgnoreCase(Constants.FUNDING_AGENCY)){
						org.setType(DataExchangeConstants.ORG_ROLE_FUNDING_AGENCY);
					} else if (ampOrgRole.getRole().getRoleCode().equalsIgnoreCase(Constants.IMPLEMENTING_AGENCY)){
						org.setType(DataExchangeConstants.ORG_ROLE_IMPLEMENTING_AGENCY);
					} else if (ampOrgRole.getRole().getRoleCode().equalsIgnoreCase(Constants.BENEFICIARY_AGENCY)){
						org.setType(DataExchangeConstants.ORG_ROLE_BENEFICIARY_AGENCY);
					} else if (ampOrgRole.getRole().getRoleCode().equalsIgnoreCase(Constants.CONTRACTING_AGENCY)){
						org.setType(DataExchangeConstants.ORG_ROLE_CONTRACTING_AGENCY);
					} else if (ampOrgRole.getRole().getRoleCode().equalsIgnoreCase(Constants.REGIONAL_GROUP)){
						org.setType(DataExchangeConstants.ORG_ROLE_REGIONAL_GROUP);
					} else if (ampOrgRole.getRole().getRoleCode().equalsIgnoreCase(Constants.SECTOR_GROUP)){
						org.setType(DataExchangeConstants.ORG_ROLE_SECTOR_GROUP);
					} else if (ampOrgRole.getRole().getRoleCode().equalsIgnoreCase(Constants.EXECUTING_AGENCY)){
						org.setType(DataExchangeConstants.ORG_ROLE_EXECUTING_AGENCY);
					} else if (ampOrgRole.getRole().getRoleCode().equalsIgnoreCase(Constants.RESPONSIBLE_ORGANISATION)){
						org.setType(DataExchangeConstants.ORG_ROLE_RESPONSIBLE_ORGANIZATION);
					} else if (ampOrgRole.getRole().getRoleCode().equalsIgnoreCase(Constants.CONTRACTOR)){
						org.setType(DataExchangeConstants.ORG_ROLE_CONTRACTOR);
					} else if (ampOrgRole.getRole().getRoleCode().equalsIgnoreCase(Constants.RELATED_INSTITUTIONS)){
						org.setType(DataExchangeConstants.ORG_ROLE_RELEATED_INSTITUTIONS);
					} else {
						throw new AmpExportException("Releated Organization type is unknown", AmpExportException.ACTIVITY_DATA_INEFFICIENT);
					}
					

					parent.getRelatedOrgs().add(org);
				}
			}
			
		} else if (path.equalsIgnoreCase("activity.donorContacts")){
			ContactType cont = buildContactType(ampActivity.getContFirstName(),
					ampActivity.getContLastName(), ampActivity.getEmail());
			if (cont != null){
				parent.getDonorContacts().add(cont);
			}
		} else if (path.equalsIgnoreCase("activity.govContacts")){
			ContactType cont = buildContactType(ampActivity.getMofedCntFirstName(),
					ampActivity.getMofedCntLastName(),ampActivity.getMofedCntEmail());
			if (cont != null){
				parent.getGovContacts().add(cont);
			}
		} else if (path.equalsIgnoreCase("activity.additional")){
			// TODO not implemented need more details
		}
	}

	private ActivityType.Id buildActivityTypeId(AmpActivityInternalId ids) throws AmpExportException{
		ActivityType.Id retValue = objectFactory.createActivityTypeId();
		retValue.setUniqID(ids.getInternalId());
		if (ids.getOrganisation() != null){
			retValue.setAssigningOrg(buildCodeValue(ids.getOrganisation().getOrgCode(), ids.getOrganisation().getName()));
		} else {
			throw new AmpExportException("Id.Org is empty", AmpExportException.ACTIVITY_DATA_INEFFICIENT);
		}

		return retValue;
	}

	private CodeValueType buildImplementationLevel() throws AmpExportException{

		AmpCategoryValue ampCategoryValue = 
			CategoryManagerUtil.getAmpCategoryValueFromListByKey(
					CategoryConstants.IMPLEMENTATION_LEVEL_KEY, 
					ampActivity.getCategories());		

		if (ampCategoryValue == null){
			throw new AmpExportException("IMPLEMENTATION_LEVEL is empty", AmpExportException.ACTIVITY_DATA_INEFFICIENT);
		}
		return buildCodeValue(ampCategoryValue);
	}


	private ActivityType.Location buildLocation(AmpLocation location, AmpColumnEntry ampColumnEntry) throws AmpExportException{
		ActivityType.Location retValue = objectFactory.createActivityTypeLocation();
		retValue.setLang(location.getLanguage());
		
		AmpCategoryValueLocations acvLoction = location.getLocation();
		if (acvLoction != null){
			retValue.setIso3(""+acvLoction.getIso3());
			retValue.setCountryName(acvLoction.getName());
			retValue.setGis(location.getGisCoordinates());
		}
		AmpCategoryValueLocations acvLoctionRegion = location.getRegionLocation();
		if (acvLoctionRegion != null){
			retValue.setLocationName(buildCodeValue(acvLoctionRegion.getCode(),acvLoctionRegion.getName()));
			AmpCategoryValue categoryValue = acvLoctionRegion.getParentCategoryValue();
			if (CategoryConstants.IMPLEMENTATION_LOCATION_DISTRICT.getValueKey().equalsIgnoreCase(categoryValue.getValue()) ){
				retValue.setLocationType(DataExchangeConstants.LOCATION_TYPE_DISTRICT); 
			}
			if (CategoryConstants.IMPLEMENTATION_LOCATION_ZONE.getValueKey().equalsIgnoreCase(categoryValue.getValue()) ){
				retValue.setLocationType(DataExchangeConstants.LOCATION_TYPE_ZONE); 
			}
			if (CategoryConstants.IMPLEMENTATION_LOCATION_REGION.getValueKey().equalsIgnoreCase(categoryValue.getValue()) ){
				retValue.setLocationType(DataExchangeConstants.LOCATION_TYPE_REGION); 

				if (ampActivity.getRegionalFundings() != null){
					Collection regFund = ampActivity.getRegionalFundings();
					for (Iterator iterator = regFund.iterator(); iterator.hasNext();) {
						AmpRegionalFunding regFunding = (AmpRegionalFunding) iterator.next();
						if (acvLoctionRegion.getCode().equalsIgnoreCase(regFunding.getRegionLocation().getCode())){
							retValue.getLocationFunding().add(buildLocationFunding(regFunding, ampColumnEntry));
						}
					}
				}
			
			}
		} else {
			throw new AmpExportException("Location.LocationName is null", AmpExportException.ACTIVITY_DATA_INEFFICIENT);
		}
		
/*		
//TODO		if (location.getDepartment())  can not found department
		if (location.getAmpWoreda() != null){
			retValue.setLocationType(DataExchangeConstants.LOCATION_TYPE_WOREDA); 
			retValue.setLocationName(buildCodeValue(location.getAmpWoreda().getGeoCode(),location.getAmpWoreda().getName()));
		} else if (location.getAmpZone() != null){
			retValue.setLocationType(DataExchangeConstants.LOCATION_TYPE_ZONE); 
			retValue.setLocationName(buildCodeValue(location.getAmpZone().getGeoCode(),location.getAmpZone().getName()));
		} else if (location.getAmpRegion() != null){
			retValue.setLocationType(DataExchangeConstants.LOCATION_TYPE_REGION); 
			retValue.setLocationName(buildCodeValue(location.getAmpRegion().getGeoCode(),location.getAmpRegion().getName()));
			
			if (ampActivity.getRegionalFundings() != null){
				Collection regFund = ampActivity.getRegionalFundings();
				for (Iterator iterator = regFund.iterator(); iterator.hasNext();) {
					AmpRegionalFunding regFunding = (AmpRegionalFunding) iterator.next();
					if (location.getAmpRegion().equals(regFunding.getRegion())){
						retValue.getLocationFunding().add(buildLocationFunding(regFunding, ampColumnEntry));
					}
				}
			}
			
		} else if (location.getDgCountry() != null){
			retValue.setLocationType(DataExchangeConstants.LOCATION_TYPE_ZONE); 
			retValue.setLocationName(buildCodeValue(location.getDgCountry().getIso3(),location.getDgCountry().getCountryName()));
		}
		retValue.setGis(location.getGisCoordinates());
		retValue.setIso3(location.getDgCountry().getIso3());
		retValue.setCountryName(location.getDgCountry().getCountryName());
*/		
		return retValue;
	}

	private LocationFundingType buildLocationFunding(AmpRegionalFunding ampfunding, AmpColumnEntry ampColumnEntry) throws AmpExportException{
		LocationFundingType retValue = objectFactory.createLocationFundingType();
		
		String fDetailType = (ampfunding.getAdjustmentType() == 1) ? 
				DataExchangeConstants.ADJUSTMENT_TYPE_ACTUAL : 
					DataExchangeConstants.ADJUSTMENT_TYPE_PLANNED;
		
		FundingDetailType fDetail = buildFundingDetail(fDetailType, ampfunding.getTransactionDate(), 
				ampfunding.getTransactionAmount().longValue(), ampfunding.getCurrency().getCurrencyCode());


		switch (ampfunding.getTransactionType().intValue()) {
		case Constants.COMMITMENT:
			if (ampColumnEntry.getElementByName("commitments").canExport()){
				retValue.getCommitments().add(fDetail);
			}
			break;
		case Constants.DISBURSEMENT:
			if (ampColumnEntry.getElementByName("disbursements").canExport()){
				retValue.getDisbursements().add(fDetail);
			}
			break;
		case Constants.EXPENDITURE:
			if (ampColumnEntry.getElementByName("expenditures").canExport()){
				retValue.getExpenditures().add(fDetail);
			}
			break;

		}
		
		return retValue;
	}	


	private FundingType buildFunding(AmpFunding funding, AmpColumnEntry ampColumnEntry) throws AmpExportException{
		FundingType retValue = objectFactory.createFundingType();
		retValue.setCode(""+funding.getFinancingId());

		for (AmpColumnEntry elem : ampColumnEntry.getElements()) {
			if (elem.canExport()){
				buildFundingSubElements(funding, retValue, elem);
			}
		}				
		return retValue;
	}

	private void buildFundingSubElements(AmpFunding ampfunding, FundingType funding, AmpColumnEntry ampColumnEntry) throws AmpExportException{
		String path = ampColumnEntry.getPath();

		if (path.equalsIgnoreCase("activity.funding.fundingOrg")){
			funding.setFundingOrg(buildCodeValue(ampfunding.getAmpDonorOrgId().getOrgCode(), ampfunding.getAmpDonorOrgId().getName()));
		} else if (path.equalsIgnoreCase("activity.funding.assistanceType")){
			CodeValueType cValue =  buildCodeValue(ampfunding.getTypeOfAssistance());
			if (cValue == null){
				throw new AmpExportException("Funding.assistanceType is null", AmpExportException.ACTIVITY_DATA_INEFFICIENT);
			}
			funding.setAssistanceType(cValue);
		} else if (path.equalsIgnoreCase("activity.funding.financingInstrument")){
			CodeValueType cValue =  buildCodeValue(ampfunding.getFinancingInstrument());
			if (cValue == null){
				throw new AmpExportException("Funding.financingInstrument is null", AmpExportException.ACTIVITY_DATA_INEFFICIENT);
			}
			funding.setFinancingInstrument(cValue);
		} else if (path.equalsIgnoreCase("activity.funding.conditions")){
			funding.setConditions(buildFreeText(ampfunding.getLanguage(), ampfunding.getConditions()));
		} else if (path.equalsIgnoreCase("activity.funding.signatureDate")){
			funding.setSignatureDate(buildDate(ampfunding.getSignatureDate(), ampColumnEntry.isMandatory()));
		} else if (path.equalsIgnoreCase("activity.funding.projections")){
			for (AmpFundingMTEFProjection ampProj : ampfunding.getMtefProjections()) {
				FundingType.Projections proj = objectFactory.createFundingTypeProjections();
				proj.setType(ampProj.getProjected().getValue());
				proj.setAmount(ampProj.getAmount().longValue());
				proj.setCurrency(ampProj.getAmpCurrency().getCurrencyCode());

				Calendar cal = Calendar.getInstance();
				cal.setTime(ampProj.getProjectionDate());
				proj.setStartYear(cal.get(Calendar.YEAR));
//				proj.setEndYear(null); // TODO 
				funding.getProjections().add(proj);
			}

		} else if (path.equalsIgnoreCase("activity.funding.commitments")){
			if (ampfunding.getFundingDetails() != null) {
				for (Iterator iterator = ampfunding.getFundingDetails().iterator(); iterator.hasNext();) {
					AmpFundingDetail  ampFDetail = (AmpFundingDetail) iterator.next();
					if (ampFDetail.getTransactionType() == Constants.COMMITMENT){
						funding.getCommitments().add(buildFundingDetail(ampFDetail));
					}
				}
			}
		} else if (path.equalsIgnoreCase("activity.funding.disbursements")){
			if (ampfunding.getFundingDetails() != null) {
				for (Iterator iterator = ampfunding.getFundingDetails().iterator(); iterator.hasNext();) {
					AmpFundingDetail  ampFDetail = (AmpFundingDetail) iterator.next();
					if (ampFDetail.getTransactionType() == Constants.DISBURSEMENT){
						funding.getCommitments().add(buildFundingDetail(ampFDetail));
					}
				}
			}
		} else if (path.equalsIgnoreCase("activity.funding.expenditures")){
			if (ampfunding.getFundingDetails() != null) {
				for (Iterator iterator = ampfunding.getFundingDetails().iterator(); iterator.hasNext();) {
					AmpFundingDetail  ampFDetail = (AmpFundingDetail) iterator.next();
					if (ampFDetail.getTransactionType() == Constants.EXPENDITURE){
						funding.getCommitments().add(buildFundingDetail(ampFDetail));
					}
				}
			}
		}
	}

	private ActivityType.Component buildComponent(Components<FundingDetail> component, AmpColumnEntry ampColumnEntry) throws AmpExportException{	
		ActivityType.Component retValue = objectFactory.createActivityTypeComponent();
		
		retValue.setComponentName(component.getTitle());
		
		AmpComponentType type = ComponentsUtil.getComponentTypeById(component.getType_Id());
		retValue.setComponentType(buildCodeValue(type.getCode(), type.getName()));
		
		ComponentFundingType componentFunding = objectFactory.createComponentFundingType();
		retValue.getComponentFunding().add(componentFunding);
		
		AmpColumnEntry componentFundingEntry = ampColumnEntry.getElementByName("componentFunding");
		if (componentFundingEntry.getElementByName("commitments").canExport() &&
				component.getCommitments() != null){
			for (FundingDetail fDetail : component.getCommitments()) {
				componentFunding.getCommitments().add(buildFundingDetail(fDetail));
			}
		}
		if (componentFundingEntry.getElementByName("disbursements").canExport() &&
				component.getDisbursements() != null){
			for (FundingDetail fDetail : component.getDisbursements()) {
				componentFunding.getDisbursements().add(buildFundingDetail(fDetail));
			}
		}
		if (componentFundingEntry.getElementByName("expenditures").canExport() &&
				component.getExpenditures() != null){
			for (FundingDetail fDetail : component.getExpenditures()) {
				componentFunding.getExpenditures().add(buildFundingDetail(fDetail));
			}
		}
		
		
		if (ampColumnEntry.getElementByName("physicalProgress").canExport()){
			for (PhysicalProgress pProgress : component.getPhyProgress()) {
				ActivityType.Component.PhysicalProgress physicalProgress = objectFactory.createActivityTypeComponentPhysicalProgress();
				physicalProgress.setTitle(buildFreeText(pProgress.getTitle()));
				physicalProgress.setDescription(buildFreeText(pProgress.getDescription()));
				physicalProgress.setReportingDate(buildDate(getDate(pProgress.getReportingDate()), ampColumnEntry.isMandatory()));
				retValue.getPhysicalProgress().add(physicalProgress);
			}
		}
		
		return retValue;
	}

	
	private ActivityType.Issues buildIssue(AmpIssues ampIssue, AmpColumnEntry ampColumnEntry) throws AmpExportException{
		ActivityType.Issues retValue = objectFactory.createActivityTypeIssues();
		
		retValue.setTitle(buildFreeText(ampIssue.getName()));
		
		if (ampIssue.getMeasures() != null){
			for (Iterator iterator = ampIssue.getMeasures().iterator(); iterator.hasNext();) {
				AmpMeasure ampMeasure = (AmpMeasure) iterator.next();
				ActivityType.Issues.Measure mesure = new ActivityType.Issues.Measure();
				mesure.setTitle(buildFreeText(ampMeasure.getName()));
				
				if (ampMeasure.getActors() != null){
					for (Iterator iterator2 = ampMeasure.getActors().iterator(); iterator2.hasNext();) {
						AmpActor ampActor = (AmpActor) iterator2.next();
						mesure.getActor().add(buildFreeText(ampActor.getName()));
					}
				}
				retValue.getMeasure().add(mesure);
			}
		}
		
		return retValue;
	}
	
	private ContactType buildContactType (String firstName, String lastName, String mail) throws AmpExportException{
		ContactType retValue = null;
		if (firstName != null && firstName.trim().length() > 0 &&
				lastName != null && lastName.trim().length() > 0 ){
			retValue = objectFactory.createContactType();
			retValue.setFirstName(firstName);
			retValue.setLastName(lastName);
			retValue.setEmail(mail);
		}
		
		return retValue;
	}
	
	
	private CodeValueType buildCodeValue(AmpCategoryValue ampCategoryValue) throws AmpExportException{
		/* TODO not sure that code is same */
		if (ampCategoryValue == null){
			return null;
		}
		return buildCodeValue(null, ampCategoryValue.getValue());
	}

	private CodeValueType buildCodeValue(String value) throws AmpExportException{
		return buildCodeValue(null, value);
	}
	
	private CodeValueType buildCodeValue(String code, String value) throws AmpExportException{
		CodeValueType retValue = null;
		if (value != null && value.trim().length() > 0){
			retValue = objectFactory.createCodeValueType();
			if (code != null && !code.isEmpty()){
				retValue.setCode(code);
			}
			retValue.setValue(value);
		}
		return retValue;
	}		

	private PercentageCodeValueType buildPercentageCodeValue(String code, String value, float precent) throws AmpExportException{
		PercentageCodeValueType retValue = objectFactory.createPercentageCodeValueType();
		if (code != null && !code.isEmpty()){
			retValue.setCode(code);
		}
		retValue.setValue(value);
		retValue.setPercentage(precent);
		return retValue;
	}		
	
	
	private FreeTextType buildFreeText(String name) throws AmpExportException{
		return buildFreeText(null, name);
	}

	private FreeTextType buildFreeText(String lang, String name) throws AmpExportException{
		FreeTextType retValue = null;
		if (name != null && name.trim().length() > 0 ){
			retValue = objectFactory.createFreeTextType();
			if (lang != null && !lang.isEmpty()){
				retValue.setLang(lang);
			}
			retValue.setValue(name);
		}
		return retValue;
	}	

	private FreeTextType buildFreeText(Message message) throws AmpExportException{
		if (message == null){
			return null;
		}
		return buildFreeText(message.getLocale(), message.getMessage());
	}

	private DateType buildDate(Date date, boolean require) throws AmpExportException{
		DateType retValue = null;
		if (date == null){
			if (require){
				throw new AmpExportException("Date is null", AmpExportException.ACTIVITY_DATA_INEFFICIENT);
			} 
		} else {
			retValue = objectFactory.createDateType();
			retValue.setDate(ExportHelper.getGregorianCalendar(date));
		}
		return retValue;
	}	

	private FundingDetailType buildFundingDetail(AmpFundingDetail detail) throws AmpExportException{
		String fDetailType = (detail.getAdjustmentType() == 1) ? 
				DataExchangeConstants.ADJUSTMENT_TYPE_ACTUAL : 
					DataExchangeConstants.ADJUSTMENT_TYPE_PLANNED;
		long amount = 0;
		
		if (detail.getTransactionAmount() != null){
			amount = detail.getTransactionAmount().longValue();
		} else {
			throw new AmpExportException("AmpFundingDetail.getTransactionAmount is null", AmpExportException.ACTIVITY_FORMAT);
		}
		
		return buildFundingDetail(fDetailType, detail.getTransactionDate(), amount, detail.getAmpCurrencyId().getCurrencyCode());
	}

	private FundingDetailType buildFundingDetail(FundingDetail fDetail) throws AmpExportException{
		long amount = 0;
		
		try {
			amount = Long.parseLong(fDetail.getTransactionAmount());
		} catch (Exception e) {
			throw new AmpExportException(e, AmpExportException.ACTIVITY_FORMAT);
		}

		return buildFundingDetail(fDetail.getAdjustmentTypeName(), getDate(fDetail.getTransactionDate()), amount, fDetail.getCurrencyCode());
	}

	private Date getDate(String stringDate) throws AmpExportException{
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date retValue = null;
		try{		
			retValue = format.parse(stringDate);
		} catch (Exception e) {
			throw new AmpExportException(e, AmpExportException.ACTIVITY_FORMAT);
		}

		return retValue;
	}
	
	private FundingDetailType buildFundingDetail(String type, Date date, long amount, String currency) throws AmpExportException{
		FundingDetailType retValue = objectFactory.createFundingDetailType();
		retValue.setType(type);
		retValue.setDate(ExportHelper.getGregorianCalendar(date));
		retValue.setAmount(amount);
		retValue.setCurrency(currency);
		
		return retValue;
	}

	

}
