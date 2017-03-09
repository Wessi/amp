package org.digijava.kernel.ampapi.endpoints.gpi;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.math.NumberUtils;
import org.digijava.kernel.ampapi.endpoints.errors.ApiError;
import org.digijava.kernel.ampapi.endpoints.errors.ApiErrorResponse;
import org.digijava.kernel.ampapi.endpoints.util.JsonBean;
import org.digijava.module.aim.dbentity.AmpGPINiAidOnBudget;
import org.digijava.module.aim.dbentity.AmpGPINiDonorNotes;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.TeamMemberUtil;
import org.digijava.module.aim.util.TeamUtil;
import org.digijava.module.common.util.DateTimeUtil;

/**
 * 
 * @author gerald
 *
 */
public class GPIDataService {
	public static JsonBean getAidOnBudgetById(Long id) {
		if (hasGPIDataRights() == false) {
			ApiErrorResponse.reportForbiddenAccess(GPIErrors.UNAUTHORIZED_OPERATION);
		}

		AmpGPINiAidOnBudget aidOnBudget = GPIUtils.getAidOnBudgetById(id);
		if (aidOnBudget != null)
			return modelToJsonBean(aidOnBudget);
		else
			return null;
	}

	public static JsonBean getAidOnBudgetList(Integer offset, Integer count, String orderBy, String sort) {
		if (hasGPIDataRights() == false) {
			ApiErrorResponse.reportForbiddenAccess(GPIErrors.UNAUTHORIZED_OPERATION);
		}

		Integer total = GPIUtils.getAidOnBudgetCount();
		List<AmpGPINiAidOnBudget> aidOnBudgetList = GPIUtils.getAidOnBudgetList(offset, count, orderBy, sort, total);
		JsonBean data = new JsonBean();
		List<JsonBean> lst = new ArrayList<>();
		for (AmpGPINiAidOnBudget aidOnBudget : aidOnBudgetList) {
			lst.add(modelToJsonBean(aidOnBudget));
		}

		data.set("data", lst);
		data.set(GPIEPConstants.TOTAL_RECORDS, total);
		return data;
	}

	private static JsonBean modelToJsonBean(AmpGPINiAidOnBudget aidOnBudget) {
		JsonBean data = new JsonBean();
		data.set(GPIEPConstants.FIELD_ID, aidOnBudget.getAmpGPINiAidOnBudgetId());
		data.set(GPIEPConstants.FIELD_DONOR_ID, aidOnBudget.getDonor().getAmpOrgId());
		data.set(GPIEPConstants.FIELD_CURRENCY_CODE, aidOnBudget.getCurrency().getCurrencyCode());
		data.set(GPIEPConstants.FIELD_AMOUNT, aidOnBudget.getAmount());
		data.set(GPIEPConstants.FIELD_DATE,
				DateTimeUtil.formatDate(aidOnBudget.getIndicatorDate(), GPIEPConstants.DATE_FORMAT));
		return data;
	}

	private static AmpGPINiAidOnBudget getAidOnBudget(JsonBean data) {
		Long id;
		AmpGPINiAidOnBudget aidOnBudget = null;
		if (data.getString(GPIEPConstants.FIELD_ID) != null
				&& NumberUtils.isNumber(data.getString(GPIEPConstants.FIELD_ID))) {
			id = Long.parseLong(String.valueOf(data.get(GPIEPConstants.FIELD_ID)));
			aidOnBudget = GPIUtils.getAidOnBudgetById(id);
		} else {
			aidOnBudget = new AmpGPINiAidOnBudget();
		}

		
		return aidOnBudget;
	}
	
	public static AmpGPINiAidOnBudget updateModel(AmpGPINiAidOnBudget aidOnBudget, JsonBean data){
		if (data.get(GPIEPConstants.FIELD_CURRENCY_CODE) != null) {
			String currencyCode = data.getString((GPIEPConstants.FIELD_CURRENCY_CODE));
			aidOnBudget.setCurrency(CurrencyUtil.getAmpcurrency(currencyCode));
		}

		if (data.getString(GPIEPConstants.FIELD_DONOR_ID) != null) {
			Long donorId = Long.parseLong(String.valueOf(data.get(GPIEPConstants.FIELD_DONOR_ID)));
			aidOnBudget.setDonor(GPIUtils.getOrganisation(donorId));
		}

		if (data.get(GPIEPConstants.FIELD_AMOUNT) != null) {
			aidOnBudget.setAmount((Double.parseDouble(String.valueOf(data.get(GPIEPConstants.FIELD_AMOUNT)))));
		}

		if (data.getString(GPIEPConstants.FIELD_DATE) != null) {
			Date date = DateTimeUtil.parseDate(data.getString(GPIEPConstants.FIELD_DATE), GPIEPConstants.DATE_FORMAT);
			aidOnBudget.setIndicatorDate(date);
		}
		return aidOnBudget;

	}

	public static JsonBean saveAidOnBudget(JsonBean data) {
		if (hasGPIDataRights() == false) {
			ApiErrorResponse.reportForbiddenAccess(GPIErrors.UNAUTHORIZED_OPERATION);
		}

		JsonBean result = new JsonBean();
		List<JsonBean> validationErrors = validateAidOnBudget(data);
		if (validationErrors.size() == 0) {
			AmpGPINiAidOnBudget aidOnBudget = getAidOnBudget(data);
			updateModel(aidOnBudget, data);
			GPIUtils.saveAidOnBudget(aidOnBudget);
			JsonBean saved = modelToJsonBean(aidOnBudget);
			result.set(GPIEPConstants.DATA, saved);
			result.set(GPIEPConstants.RESULT, GPIEPConstants.SAVED);
			if (data.get(GPIEPConstants.CID) != null) {
				saved.set(GPIEPConstants.CID, data.get(GPIEPConstants.CID));
			}
		} else {
			result.set(GPIEPConstants.DATA, data);
			result.set(GPIEPConstants.RESULT, GPIEPConstants.SAVE_FAILED);
			result.set(GPIEPConstants.ERRORS, validationErrors);
		}

		return result;
	}

	public static List<JsonBean> validateAidOnBudget(JsonBean data) {
		List<JsonBean> validationErrors = new ArrayList<>();
		Long donorId = Long.parseLong(String.valueOf(data.get(GPIEPConstants.FIELD_DONOR_ID)));
		Date date = DateTimeUtil.parseDate(data.getString(GPIEPConstants.FIELD_DATE), GPIEPConstants.DATE_FORMAT);
		Long id = null;
		if (data.get(GPIEPConstants.FIELD_ID) != null) {
			id = Long.parseLong(String.valueOf(data.get(GPIEPConstants.FIELD_ID)));
		}

		if (GPIUtils.similarRecordExists(id, donorId, date)) {
			JsonBean error = new JsonBean();
			error.set(ApiError.getErrorCode(GPIErrors.DATE_DONOR_COMBINATION_EXISTS),
					GPIErrors.DATE_DONOR_COMBINATION_EXISTS.description);
			validationErrors.add(error);
		}

		return validationErrors;
	}

	public static List<JsonBean> saveAllEdits(List<JsonBean> aidOnBudgetList) {
		if (hasGPIDataRights() == false) {
			ApiErrorResponse.reportForbiddenAccess(GPIErrors.UNAUTHORIZED_OPERATION);
		}

		List<JsonBean> results = new ArrayList<>();
		for (JsonBean aidOnBudget : aidOnBudgetList) {
			results.add(saveAidOnBudget(aidOnBudget));
		}

		return results;
	}

	public static JsonBean deleteAidOnBudgetById(Long id) {
		if (hasGPIDataRights() == false) {
			ApiErrorResponse.reportForbiddenAccess(GPIErrors.UNAUTHORIZED_OPERATION);
		}

		JsonBean result = new JsonBean();
		GPIUtils.deleteAidOnBudget(id);
		result.set(GPIEPConstants.RESULT, GPIEPConstants.DELETED);
		return result;
	}

	public static boolean hasGPIDataRights() {
		// TODO: Fix - TeamUtil.getCurrentMember() returns null
		/*
		 * TeamMember tm = TeamUtil.getCurrentMember(); AmpTeamMember atm =
		 * TeamMemberUtil.getAmpTeamMember(tm.getMemberId()); return
		 * atm.getUser().hasNationalCoordinatorGroup() ||
		 * atm.getUser().hasVerifiedDonor();
		 */
		return true;
	}

	public static JsonBean saveDonorNotes(JsonBean data) {
		JsonBean result = new JsonBean();
		List<JsonBean> validationErrors = validateDonorNotes(data);

		if (validationErrors.isEmpty()) {			
			AmpGPINiDonorNotes donorNotes = getOrCreateDonorNotes(data);			
			GPIUtils.saveDonorNotes(donorNotes);
			JsonBean saved = modelToJsonBean(donorNotes);
			result.set(GPIEPConstants.DATA, saved);
			result.set(GPIEPConstants.RESULT, GPIEPConstants.SAVED);
			if (data.get(GPIEPConstants.CID) != null) {
				saved.set(GPIEPConstants.CID, data.get(GPIEPConstants.CID));
			}

		} else {
			result.set(GPIEPConstants.DATA, data);
			result.set(GPIEPConstants.RESULT, GPIEPConstants.SAVE_FAILED);
			result.set(GPIEPConstants.ERRORS, validationErrors);

		}
		return result;
	}
	
	private static AmpGPINiDonorNotes getOrCreateDonorNotes(JsonBean data){
		Long id;
		AmpGPINiDonorNotes donorNotes; 
		if (data.getString(GPIEPConstants.FIELD_ID) != null
				&& NumberUtils.isNumber(data.getString(GPIEPConstants.FIELD_ID))) {
			id = Long.parseLong(String.valueOf(data.get(GPIEPConstants.FIELD_ID)));
			donorNotes = GPIUtils.getDonorNotesById(id);
		} else {
			donorNotes = new AmpGPINiDonorNotes();
		}

		donorNotes.setNotes(data.getString(GPIEPConstants.FIELD_NOTES));
		if (data.getString(GPIEPConstants.FIELD_NOTES_DATE) != null) {
			Date notesDate = DateTimeUtil.parseDate(data.getString(GPIEPConstants.FIELD_NOTES_DATE),
					GPIEPConstants.DATE_FORMAT);
			donorNotes.setNotesDate(notesDate);
		}

		if (data.getString(GPIEPConstants.FIELD_DONOR_ID) != null) {
			Long donorId = Long.parseLong(String.valueOf(data.get(GPIEPConstants.FIELD_DONOR_ID)));
			donorNotes.setDonor(GPIUtils.getOrganisation(donorId));
		}
		
		return donorNotes;
	}
	private static JsonBean modelToJsonBean(AmpGPINiDonorNotes donorNotes) {
		JsonBean data = new JsonBean();		
		data.set(GPIEPConstants.FIELD_ID, donorNotes.getAmpGPINiDonorNotesId());
		data.set(GPIEPConstants.FIELD_DONOR_ID, donorNotes.getDonor().getAmpOrgId());
		data.set(GPIEPConstants.FIELD_NOTES, donorNotes.getNotes());
		data.set(GPIEPConstants.FIELD_NOTES_DATE, DateTimeUtil.formatDate(donorNotes.getNotesDate(), GPIEPConstants.DATE_FORMAT));
		return data;
	}

	private static List<JsonBean> validateDonorNotes(JsonBean data) {
		List<JsonBean> validationErrors = new ArrayList<>();
		return validationErrors;

	}

	public static JsonBean getDonorNotesList(Integer offset, Integer count, String orderBy, String sort) {
		Integer total = GPIUtils.getDonorNotesCount();
		List<AmpGPINiDonorNotes>  notesList = GPIUtils.getDonorNotesList(offset, count, orderBy, sort, total);
		JsonBean data = new JsonBean();
		List<JsonBean> lst = new ArrayList<>();
		
		for (AmpGPINiDonorNotes notes : notesList) {
			lst.add(modelToJsonBean(notes));
		}
		
		data.set("data", lst);
		data.set(GPIEPConstants.TOTAL_RECORDS, total);
		return data;
	}
	
	public static JsonBean deleteDonorNotesById(Long id) {
		
		JsonBean result = new JsonBean();
		GPIUtils.deleteDonorNotes(id);
		result.set(GPIEPConstants.RESULT, GPIEPConstants.DELETED);
		return result;
	}
	
	

}
