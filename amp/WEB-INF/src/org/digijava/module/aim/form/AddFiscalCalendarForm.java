package org.digijava.module.aim.form;

import java.util.Collection;
import java.util.Vector;

import org.apache.struts.action.*;
import org.digijava.module.aim.helper.BaseCalendar;

import javax.servlet.http.HttpServletRequest;

public class AddFiscalCalendarForm extends ActionForm {

	private Long fiscalCalId = null;
	private String fiscalCalName = null;
	private String description = null;
	private int startMonthNum = 1;
	private int startDayNum = 1;
	private int yearOffset = 0;
	private String flag = null;
	private String action = null;
	private String baseCalendar = null;
	
	
	
	public Collection<BaseCalendar> getBaseCalendarList() {
		return BaseCalendar.calendarList;
	}
	

	public Long getFiscalCalId() {
			  return (this.fiscalCalId);
	}

	public void setFiscalCalId(Long fiscalCalId) {
			  this.fiscalCalId = fiscalCalId;
	}

	public String getFiscalCalName() {
			  return (this.fiscalCalName);
	}

	public void setFiscalCalName(String fiscalCalName) {
			  this.fiscalCalName = fiscalCalName;
	}

	public String getDescription() {
			  return (this.description);
	}

	public void setDescription(String description) {
			  this.description = description;
	}

	public int getStartMonthNum() {
			  return (this.startMonthNum);
	}

	public void setStartMonthNum(int num) {
			  this.startMonthNum = num;
	}

	public int getStartDayNum() {
			  return (this.startDayNum);
	}

	public void setStartDayNum(int num) {
			  this.startDayNum = num;
	}

	public int getYearOffset() {
			  return (this.yearOffset);
	}

	public void setYearOffset(int year)  {
			  this.yearOffset = year;
	}

	public String getFlag() {
			  return (this.flag);
	}

	public void setFlag(String flag) {
			  this.flag = flag;
	}

	public String getAction() {
		  return (this.action);
	}

	public void setAction(String action) {
		  this.action = action;
	}

	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request) {

		ActionErrors errors = new ActionErrors();

		if (startMonthNum < 1 || startMonthNum > 12) {
			errors.add("startMonthNum", new ActionError(
					"error:aim:addFiscalCal:invalidStartMonth"));
		}
		
		if (startDayNum > 31) {
			errors.add("startDayNum", new ActionError(
					"error:aim:addFiscalCal:invalidStartDay"));
		} else if (startMonthNum % 2 == 0 && startMonthNum < 8) {
			if (startDayNum > 30) {
				errors.add("startDayNum", new ActionError(
						"error:aim:addFiscalCal:invalidStartDay"));
			}
			if (startMonthNum == 2 && startDayNum > 28) {
				errors.add("startDayNum", new ActionError(
						"error:aim:addFiscalCal:invalidStartDay"));
			}
		} else if (startMonthNum % 2 != 0 && startMonthNum > 7) {
			if (startDayNum > 30) {
				errors.add("startDayNum", new ActionError(
						"error:aim:addFiscalCal:invalidStartDay"));
			}
		}

		if (yearOffset > 0) {
			errors.add("yearOffset", new ActionError(
					"error:aim:addFiscalCal:invalidYearOffset"));
		}

		return (errors);
	}

	public String getBaseCalendar() {
		return baseCalendar;
	}

	public void setBaseCalendar(String baseCalendar) {
		this.baseCalendar = baseCalendar;
	}




}
