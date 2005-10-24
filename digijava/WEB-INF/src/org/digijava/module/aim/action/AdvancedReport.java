
/*
 * AdvancedReport.java @Author Ronald B Created: 27-July-2005
 */

package org.digijava.module.aim.action;


import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Collection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpColumns;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpFilters;
import org.digijava.module.aim.dbentity.AmpMeasures;
import org.digijava.module.aim.dbentity.AmpPages;
import org.digijava.module.aim.dbentity.AmpReportColumn;
import org.digijava.module.aim.dbentity.AmpReportHierarchy;
//import org.digijava.module.aim.dbentity.AmpReportMeasures;
import org.digijava.module.aim.dbentity.AmpReports;
import org.digijava.module.aim.dbentity.AmpTeam;
import org.digijava.module.aim.dbentity.AmpTeamMember;
import org.digijava.module.aim.dbentity.AmpTeamPageFilters;
import org.digijava.module.aim.dbentity.AmpTeamReports;
import org.digijava.kernel.persistence.PersistenceManager;

import org.digijava.module.aim.helper.AmpFund;
import org.digijava.module.aim.helper.DecimalToText;

import org.jfree.chart.*;
import org.jfree.chart.plot.*;
import org.jfree.chart.entity.*;
import org.jfree.chart.labels.*;
import org.jfree.chart.urls.*;
import org.jfree.chart.servlet.*;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.category.*;

import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;

import org.digijava.module.aim.form.AdvancedReportForm;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.helper.Report;
import org.digijava.module.aim.util.DbUtil;
import org.digijava.module.aim.util.ReportUtil;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;


public class AdvancedReport extends Action {

	private static Logger logger = Logger.getLogger(Login.class);
	private String str="";
	static HttpSession httpSession= null;
	static AdvancedReportForm formBean;

	public ActionForward execute(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception
	{
		logger.info("###---------------------------------------------------------------------->>>>>>>>");
		formBean = (AdvancedReportForm) form;
		
		httpSession = request.getSession();
		Query query;
		Collection reports = new ArrayList();
		Collection selectedTransc = new ArrayList();
		Session session = null;
		Transaction tx = null;
		String sqlQuery;		
		Iterator iter;
		Collection coll = new ArrayList();
		DecimalFormat mf = new DecimalFormat("###,###,###,###,###") ;
		TeamMember teamMember=(TeamMember)httpSession.getAttribute("currentMember");
		logger.info(teamMember.getMemberId());
		if(teamMember==null)
			return mapping.findForward("index");
		Long ampTeamId=teamMember.getTeamId();
		logger.debug("Team Id: " + ampTeamId);
		String perspective = "DN";
		
		if(formBean.getPerspective() == null)
		{
			perspective = teamMember.getAppSettings().getPerspective();
		}
		else
			perspective = formBean.getPerspectiveFilter();
		if(perspective.equals("Donor"))
			perspective="DN";
		if(perspective.equals("MOFED"))
			perspective="MA";
		formBean.setPerspectiveFilter(perspective);

		try
		{
			session = PersistenceManager.getSession();
			tx = session.beginTransaction();

			// clears all the values once portfolio is clicked
			if( request.getParameter("clear") != null && request.getParameter("clear").equals("true"))
			{
				logger.info("from ViewMyDesktop");
				formBean.setAmpColumns(null);
				formBean.setAddedColumns(null);
				formBean.setColumnHierarchie(null);
				formBean.setAddedMeasures(null);
				formBean.setReportTitle("");
				formBean.setAdjustType(null);
				formBean.setSelAdjustType(null);
				formBean.setSelectedAdjustmentType(null);
				
			}

			
			// Fills the column that can be selected from AMP_COLUMNS
			if(formBean.getAmpColumns() == null)
			{
				formBean.setAmpColumns(ReportUtil.getColumnList());
				formBean.setAmpMeasures(ReportUtil.getMeasureList());
				formBean.setReportTitle("");
				
				/*iter = formBean.getAmpMeasures().iterator();
				Collection tempColl = new ArrayList();
				while(iter.hasNext())
				{
					AmpMeasures ampMeasure = (AmpMeasures) iter.next();
					if(ampMeasure.getType().equals("A") == true)
						tempColl.add(ampMeasure);
				}
				formBean.setAdjustType(tempColl);
				*/
			}
			else
				logger.info(" AmpColumns is not NULL........");
			
			// add columns that are available
			if(request.getParameter("check") != null && request.getParameter("check").equals("add"))
			{
				str = request.getParameter("check");
				logger.info( "Operation is : " + str);
				updateData(formBean.getAmpColumns(), formBean.getAddedColumns(), formBean.getSelectedColumns(), formBean);
				formBean.setSelectedColumns(null);
			}
			// Remove the columns selected
			if(request.getParameter("check") != null && request.getParameter("check").equals("delete"))
			{
				str = request.getParameter("check");
				logger.info( "Operation is : " + str);
				updateData(formBean.getAddedColumns(), formBean.getAmpColumns() , formBean.getRemoveColumns(), formBean);
				formBean.setRemoveColumns(null);
			}
			
			// Add the columns selected : Step 2
			if(request.getParameter("check") != null && request.getParameter("check").equals("Step2AddRows"))
			{
				str = request.getParameter("check");
				updateData(formBean.getAddedColumns(), formBean.getColumnHierarchie(), formBean.getSelectedColumns(), formBean);
				formBean.setSelectedColumns(null);
				return mapping.findForward("SelectRows");
			}
			// Remove the columns selected : Step 2
			if(request.getParameter("check") != null && request.getParameter("check").equals("Step2DeleteRows"))
			{
				str = request.getParameter("check");
				logger.info( "Operation is : " + str);
				updateData(formBean.getColumnHierarchie(), formBean.getAddedColumns(), formBean.getRemoveColumns(), formBean);
				formBean.setRemoveColumns(null);
				return mapping.findForward("SelectRows");
			}
			// Step 3 : Select Measures
			if(request.getParameter("check") != null && request.getParameter("check").equals("AddMeasure"))
			{
				str = request.getParameter("check");
				logger.info( "Operation is : " + str);
				updateData(formBean.getAmpMeasures(), formBean.getAddedMeasures(), formBean.getSelectedColumns(), formBean);
				formBean.setSelectedColumns(null);
				return mapping.findForward("SelectMeasures");
			}
			
			// Remove the columns selected
			if(request.getParameter("check") != null && request.getParameter("check").equals("DeleteMeasure"))
			{
				str = request.getParameter("check");
				logger.info( "Operation is : " + str);
				updateData(formBean.getAddedMeasures(), formBean.getAmpMeasures() , formBean.getRemoveColumns(), formBean);
				formBean.setRemoveColumns(null);
				return mapping.findForward("SelectMeasures");
			}

			/*
			// Step 3 : Select Adjustment Type
			if(request.getParameter("check") != null && request.getParameter("check").equals("AddAdjustType"))
			{
				str = request.getParameter("check");
				logger.info( "Operation is : " + str + "" + formBean.getAdjustType().size());
				updateData(formBean.getAdjustType(), formBean.getSelAdjustType(), formBean.getSelectedAdjustmentType(), formBean);
				formBean.setSelectedAdjustmentType(null);
				return mapping.findForward("SelectMeasures");
			}
			
			// Remove the columns selected
			if(request.getParameter("check") != null && request.getParameter("check").equals("DeleteAdjustType"))
			{
				str = request.getParameter("check");
				logger.info( "Operation is : " + str);
				updateData(formBean.getSelAdjustType(), formBean.getAdjustType() , formBean.getRemoveAdjustType(), formBean);
				formBean.setRemoveAdjustType(null);
				return mapping.findForward("SelectMeasures");
			}
			*/
			// Goto Step 2.
			if(request.getParameter("check") != null && request.getParameter("check").equals("5"))
			{
				logger.info("In here  generating data..........");
				int fromYr = 0, toYr = 0, year=0, fiscalCalId;
				String ampCurrencyCode=null;
				AmpCurrency ampCurrency=null;
				GregorianCalendar c=new GregorianCalendar();
				year=c.get(Calendar.YEAR);
				//for storing the value of year filter 
				if(formBean.getAmpToYear()==null)
				{
					toYr=year;
					formBean.setAmpToYear(new Long(toYr));
				}
				else
			  		toYr = formBean.getAmpToYear().intValue();

				if(formBean.getAmpFromYear()==null)
				{
					fromYr=toYr-2;
					formBean.setAmpFromYear(new Long(fromYr));
				}
				else
			  		fromYr = formBean.getAmpFromYear().intValue();
				if(formBean.getFiscalCalId()==0)
				{
					fiscalCalId=teamMember.getAppSettings().getFisCalId().intValue();
					formBean.setFiscalCalId(fiscalCalId);
				}
				else
					fiscalCalId =  formBean.getFiscalCalId();		

				if(request.getParameter("view")!=null)
				{
					if(request.getParameter("view").equals("reset"))
					{
						perspective =teamMember.getAppSettings().getPerspective();
						if(perspective.equals("Donor"))
							perspective="DN";
						if(perspective.equals("MOFED"))
							perspective="MA";
						fiscalCalId=teamMember.getAppSettings().getFisCalId().intValue();
					}
				}
				if(formBean.getAmpCurrencyCode()==null || formBean.getAmpCurrencyCode().equals("0"))
				{
					ampCurrency=DbUtil.getAmpcurrency(teamMember.getAppSettings().getCurrencyId());
					ampCurrencyCode=ampCurrency.getCurrencyCode();
					formBean.setAmpCurrencyCode(ampCurrencyCode);
				}
				else
					ampCurrencyCode=formBean.getAmpCurrencyCode();

				
				logger.info(fromYr + ":  From year - TO Year : " + toYr);
				// Gets the Transaction and Adjustement Type from the user
				ArrayList adjType = new ArrayList(), transc = new ArrayList();
				int count = 0;
				/*
				if(formBean.getSelAdjustType() != null)
				{
					
					iter = formBean.getSelAdjustType().iterator();
					while(iter.hasNext())
					{
						AmpMeasures ampMeasures = (AmpMeasures) iter.next();
						logger.info("Adjustment : " + ampMeasures.getMeasureId() + "" + ampMeasures.getMeasureName());
						adjType.add(count, ampMeasures.getMeasureId());
						count = count + 1;
					}
				}
				*/
				if(formBean.getAddedMeasures() != null)
				{
					count = 0;
					iter = formBean.getAddedMeasures().iterator();
					while(iter.hasNext())
					{
						AmpMeasures ampMeasure = (AmpMeasures) iter.next();
						logger.info("Transaction : " + ampMeasure.getMeasureId() + "" + ampMeasure.getMeasureName());
						transc.add(count, ampMeasure.getMeasureId());
						selectedTransc.add(ampMeasure.getMeasureName().toString());
						count = count + 1;
					}
				}
				logger.info(transc.size() + "________________ " + adjType.size());
				logger.info("Adjustment: " + adjType.size());
				logger.info("Transaction: " + transc.size());

				ArrayList measures = transc;
				if(measures.indexOf(new Long(1))>=0)
					formBean.setAcCommFlag("true");
				else
					formBean.setAcCommFlag("false");

				if(measures.indexOf(new Long(2))>=0)
					formBean.setAcDisbFlag("true");
				else
					formBean.setAcDisbFlag("false");

				if(measures.indexOf(new Long(3))>=0)
					formBean.setAcExpFlag("true");
				else
					formBean.setAcExpFlag("false");
		
				if(measures.indexOf(new Long(4))>=0)
					formBean.setPlCommFlag("true");
				else
					formBean.setPlCommFlag("false");

				if(measures.indexOf(new Long(5))>=0)
					formBean.setPlDisbFlag("true");
				else
					formBean.setPlDisbFlag("false");

				if(measures.indexOf(new Long(6))>=0)
					formBean.setPlExpFlag("true");
				else
					formBean.setPlExpFlag("false");

				if(measures.indexOf(new Long(7))>=0)
					formBean.setAcBalFlag("true");
				else
					formBean.setAcBalFlag("false");

				formBean.setFundColumns((measures.size()));
	
				
				boolean allPages = false;
				if(formBean.getAddedColumns() != null)
				{
					//formBean.setFinalData(ReportUtil.generateQuery(formBean.getAddedColumns(),ampTeamId));
					
					Collection pageRecords = new ArrayList();
					int page=0;
					if (request.getParameter("page") == null) 
					{
						page = 1;
						reports=ReportUtil.generateAdvancedReport(ampTeamId,fromYr,toYr,fiscalCalId,ampCurrencyCode,perspective, transc, formBean.getAddedColumns() );
						logger.info("Page is NULL............................" + reports.size());
						formBean.setFinalData(reports);
						httpSession.setAttribute("ampReports",reports);
						
					}
					else 
					{
						logger.info("  ------>>>>>>>>    " + formBean.getFinalData().size());
						if(request.getParameter("page").equals("all") == true)
							allPages = true;
						else
						{
							page = Integer.parseInt(request.getParameter("page"));
							reports=(ArrayList)httpSession.getAttribute("ampReports");
							logger.info("Page is NOT NULL.................");
						}
					}
					
					if(allPages == true)
					{
						logger.info("IN ALl Records.........>>>" + formBean.getFinalData().size());
						formBean.setReport(formBean.getFinalData());
						formBean.setFiscalYearRange(new ArrayList());
						for(int yr=fromYr;yr<=toYr;yr++)
							formBean.getFiscalYearRange().add(new Integer(yr));
						reports=(ArrayList)httpSession.getAttribute("ampReports");
					}
					else
					{
						int stIndex = ((page - 1) * 10) + 1;
						int edIndex = page * 10;
						if (edIndex > reports.size()) 
						{
							edIndex = reports.size();
						}
						Vector vect = new Vector();
						vect.addAll(reports);
									 
						for (int i = (stIndex-1);i < edIndex;i ++) 
						{
							pageRecords.add(vect.get(i));
						}
						logger.info("< For each Page Record size is ..... >" + pageRecords.size());
						Collection pages = null;
						int numPages=0;
						if (reports.size() > 10) 
						{
							pages = new ArrayList();
							numPages = reports.size()/10;
							if(reports.size()%10>0)
								numPages++;
							for (int i = 0;i < numPages;i++) 
							{
								Integer pageNum=new Integer(i+1);
								pages.add(pageNum);
							}
						 }
						
						int yearRange=(toYr-fromYr)+1;
						formBean.setFiscalYearRange(new ArrayList());
						for(int yr=fromYr;yr<=toYr;yr++)
							formBean.getFiscalYearRange().add(new Integer(yr));
						formBean.setTotalColumns(14);
						formBean.setPages(pages);
						formBean.setReport(pageRecords);
						formBean.setPage(new Integer(page));
						formBean.setAllReports(reports);
						logger.info(" page REC " + pageRecords.size());
				 		logger.info(" REPORTS  " + reports.size());
					}

//					---------------------------------------------------		
					/*BEGIN CODE FOR GRAND TOTAL*/
					logger.info("BEGIN CODE FOR GRAND TOTAL..............");
					int yearRange=(toYr-fromYr)+1;
					double totUnDisb = 0, actTotalCommit = 0, actTotalDisb = 0, actTotalExp = 0, planTotalCommit = 0, planTotalDisb = 0, planTotalExp = 0;
					double[][] totFunds=new double[yearRange][7];
					iter = reports.iterator();			
				//	logger.debug("Grand Total :" + grandTotal);
					while ( iter.hasNext() )
					{
						Report report=(Report) iter.next();
						Iterator advIter=report.getRecords().iterator();
						while(advIter.hasNext())
						{
							org.digijava.module.aim.helper.AdvancedReport advancedReport=(org.digijava.module.aim.helper.AdvancedReport) advIter.next();
							if(advancedReport.getAmpFund()!=null)
							{
								Iterator iterFund = advancedReport.getAmpFund().iterator();
								for(int i=0;i<yearRange ;i++ )
								{
									AmpFund ampFund=(AmpFund) iterFund.next();
									if(measures.indexOf(new Long(1))!=-1)
										totFunds[i][0]=totFunds[i][0] + Double.parseDouble(DecimalToText.removeCommas(ampFund.getCommAmount()));
									if(measures.indexOf(new Long(2))!=-1)
										totFunds[i][1]=totFunds[i][1] + Double.parseDouble(DecimalToText.removeCommas(ampFund.getDisbAmount()));
									if(measures.indexOf(new Long(3))!=-1)
										totFunds[i][2]=totFunds[i][2] + Double.parseDouble(DecimalToText.removeCommas(ampFund.getExpAmount()));
									if(measures.indexOf(new Long(4))!=-1)
										totFunds[i][3]=totFunds[i][3] + Double.parseDouble(DecimalToText.removeCommas(ampFund.getPlCommAmount()));
									if(measures.indexOf(new Long(5))!=-1)
										totFunds[i][4]=totFunds[i][4] + Double.parseDouble(DecimalToText.removeCommas(ampFund.getPlDisbAmount()));
									if(measures.indexOf(new Long(6))!=-1)
										totFunds[i][5]=totFunds[i][5] + Double.parseDouble(DecimalToText.removeCommas(ampFund.getPlExpAmount()));
									if(measures.indexOf(new Long(7))!=-1)
										totFunds[i][6]=totFunds[i][6] + Double.parseDouble(DecimalToText.removeCommas(ampFund.getUnDisbAmount()));
					
									actTotalCommit = actTotalCommit + Double.parseDouble(DecimalToText.removeCommas(ampFund.getCommAmount()));
									actTotalDisb = actTotalDisb + totFunds[i][1] + Double.parseDouble(DecimalToText.removeCommas(ampFund.getDisbAmount()));
									actTotalExp = actTotalExp + totFunds[i][2] + Double.parseDouble(DecimalToText.removeCommas(ampFund.getExpAmount()));
									planTotalCommit = planTotalCommit + totFunds[i][3] + Double.parseDouble(DecimalToText.removeCommas(ampFund.getPlCommAmount()));
									planTotalDisb = planTotalDisb + totFunds[i][4] + Double.parseDouble(DecimalToText.removeCommas(ampFund.getPlDisbAmount()));
									planTotalExp = planTotalExp + totFunds[i][5] + Double.parseDouble(DecimalToText.removeCommas(ampFund.getPlExpAmount()));
									totUnDisb = totUnDisb + Double.parseDouble(DecimalToText.removeCommas(ampFund.getUnDisbAmount()));

								}
							}
						}	
					}
					formBean.setTotFund(new ArrayList());
					for(int i=0;i<yearRange ;i++ )
					{
						AmpFund ampFund=new AmpFund();
						if(measures.indexOf(new Long(1))!=-1){
							ampFund.setCommAmount(mf.format(totFunds[i][0]));
							System.out.println("GRRRRRRRRR"+totFunds[i][0]+"--------"+mf.format(totFunds[i][0]));
						}	
						if(measures.indexOf(new Long(2))!=-1)
							ampFund.setDisbAmount(mf.format(totFunds[i][1])); 
						if(measures.indexOf(new Long(3))!=-1)
							ampFund.setExpAmount(mf.format(totFunds[i][2]));	
						if(measures.indexOf(new Long(4))!=-1)
							ampFund.setPlCommAmount(mf.format(totFunds[i][3])); 
						if(measures.indexOf(new Long(5))!=-1)
							ampFund.setPlDisbAmount(mf.format(totFunds[i][4])); 
						if(measures.indexOf(new Long(6))!=-1)
							ampFund.setPlExpAmount(mf.format(totFunds[i][5]));	
						if(measures.indexOf(new Long(7))!=-1)
							ampFund.setUnDisbAmount(mf.format(totFunds[i][6]));	
						formBean.getTotFund().add(ampFund);
					}

					AmpFund fund = new AmpFund();
					logger.info("setting val of : actTotalCommit====="+actTotalCommit);
					fund.setCommAmount(mf.format(actTotalCommit));
					fund.setDisbAmount(mf.format(actTotalDisb));
					fund.setExpAmount(mf.format(actTotalExp));
					fund.setPlCommAmount(mf.format(planTotalCommit));
					fund.setPlDisbAmount(mf.format(planTotalDisb));
					fund.setPlExpAmount(mf.format(planTotalExp));
					fund.setUnDisbAmount(mf.format(totUnDisb));
					formBean.getTotFund().add(fund);
					
					
					/*END CODE FOR GRAND TOTAL*/
					logger.info("END CODE FOR GRAND TOTAL..............");

					formBean.setForecastYear(new ArrayList());
					for(int i=toYr;i<=(toYr+3);i++)
						formBean.getForecastYear().add(new Integer(i));

				}
				logger.info("###----------------------------------------------------------------------#####");

				return mapping.findForward("GenerateReport");
			}

			// Step 4 : Report Details
			if(request.getParameter("check") != null && request.getParameter("check").equals("4"))
			{
				logger.info("In here  Getting Report Details..........");
				return mapping.findForward("ReportDetails");
			}
			if(request.getParameter("check") != null && request.getParameter("check").equals("SelectColumn"))
				return mapping.findForward("forward");
			
			if(request.getParameter("check") != null && request.getParameter("check").equals("SelectRows"))
				return mapping.findForward("SelectRows");


			if(request.getParameter("check") != null && request.getParameter("check").equals("SelectMeasures"))
				return mapping.findForward("SelectMeasures");


			// step 3 : 
			if(request.getParameter("check") != null && request.getParameter("check").equals("charts"))
			{
				logger.info("In here  chart process####..........");

				//logger.info("###########################Inside GeneratePIEChart..:):)");
				//logger.info("CHART FORMBEAN SIZE::::::::::::::::"+formBean.getFinalData().size());

		        //Iterator iter2 = formBean.getFinalData().iterator();
		        Collection chart_coll=new ArrayList();
		        String title = "", commit = "", comm="", disb="", exp="";
				iter = formBean.getFinalData().iterator();
				Collection colls = null;
				Collection ampFund= null;

				Iterator it, fundItr=null;

				while(iter.hasNext())
				{
					Report report = (Report) iter.next();
					colls = report.getRecords();
					
					it = colls.iterator();

					while(it.hasNext())
					{
						org.digijava.module.aim.helper.AdvancedReport advReport = (org.digijava.module.aim.helper.AdvancedReport)it.next();
						if(advReport.getTitle() != null)
							title = advReport.getTitle();
						
						if(advReport.getAmpFund() != null){
								logger.info("ampFund is NOT NULL....");
							fundItr = advReport.getAmpFund().iterator();

							AmpFund ampFund1 = new AmpFund();
							while(fundItr.hasNext()){
								ampFund1 = (AmpFund) fundItr.next();
								comm = ampFund1.getCommAmount();
								disb = ampFund1.getDisbAmount();
								exp = ampFund1.getExpAmount();
							}
						}

						if(advReport.getActualCommitment() != null)
							commit = advReport.getActualCommitment();
							//chart_coll.add(advReport.getActualCommitment().replaceAll("," , ""));
						//chart_coll.add(advReport.getTitle());
					}//end of while
					logger.info("ZZZZZZZZZZz"+title+"<------***********------->"  + comm +"<------***********------->" +disb + "<------***********------->"  + exp );
					//chart_coll.add(new Double(commit.replaceAll(",", "")) );

					chart_coll.add(new Double(comm.replaceAll(",", "")) );
					chart_coll.add(title);
					chart_coll.add(new Double(disb.replaceAll(",", "")) );
					chart_coll.add(title);
					chart_coll.add(new Double(exp.replaceAll(",", "")) );
					chart_coll.add(title);

				}
				
				logger.info("  Chart Size : " +chart_coll.size());
		        
//		    	chart_coll.add("60");
//		    	chart_coll.add("Donor 1");

/*				while(iter2.hasNext()){
					Report r= (Report) iter2.next();
					chart_coll.add(r.getAcCommitment().replaceAll("," , ""));
//					logger.info("filling COMM into the COLLLLL."+r.getAcCommitment());
					chart_coll.add(r.getDonor());
					//logger.info("filling DONOR NAME into the COLLLLL."+r.getDonor());
				}
*/				
				// calling Piechart
				String piechartname=createPieChart(chart_coll);
				//logger.info("@@@@@@@@@@IMAGE FILE NAME:"+piechartname);
				formBean.setPieImageUrl(piechartname);

				// calling Barchart
				String barchartname=createBarChart(chart_coll);
				//logger.info("@@@@@@@@@@IMAGE FILE NAME:"+piechartname);
				formBean.setBarImageUrl(barchartname);

				return mapping.findForward("GenerateChart");
			}


			// Step 4 : Report Details
			if(request.getParameter("check") != null && request.getParameter("check").equals("4"))
			{
				logger.info("In here  Getting Report Details..........");
				return mapping.findForward("ReportDetails");
			}
			
			// Move the selected column Up : Step 1 ie Select Columns
			if(request.getParameter("check") != null && request.getParameter("check").equals("MoveUp"))
				moveColumns(formBean, "MoveUp");
			if(request.getParameter("check") != null && request.getParameter("check").equals("Step2MoveUp"))
			{
				moveColumns(formBean, "Step2MoveUp");
				return mapping.findForward("SelectRows");
			}
			if(request.getParameter("check") != null && request.getParameter("check").equals("MoveUpMeasure"))
			{
				moveColumns(formBean, "MoveUpMeasure");
				return mapping.findForward("SelectMeasures");
			}

			// Move the selected column Down : Step 1 ie Select Columns
			if(request.getParameter("check") != null && request.getParameter("check").equals("MoveDown"))
				moveColumns(formBean, "MoveDown");
			if(request.getParameter("check") != null && request.getParameter("check").equals("Step2MoveDown"))
			{
				moveColumns(formBean, "Step2MoveDown");
				return mapping.findForward("SelectRows");
			}
			if(request.getParameter("check") != null && request.getParameter("check").equals("MoveDownMeasure"))
			{
				moveColumns(formBean, "MoveDownMeasure");
				return mapping.findForward("SelectMeasures");
			}
			
			
			if(request.getParameter("check") != null && request.getParameter("check").equals("MoveUpAdjustType"))
			{
				moveColumns(formBean, "MoveUpAdjustType");
				return mapping.findForward("SelectMeasures");
			}
			if(request.getParameter("check") != null && request.getParameter("check").equals("MoveDownAdjustType"))
			{
				moveColumns(formBean, "MoveDownAdjustType");
				return mapping.findForward("SelectMeasures");
			}
			
			// save Report
			if(request.getParameter("check") != null && request.getParameter("check").equals("SaveReport"))
			{
				boolean flag = false;
				logger.info("---------Start--Report --- Save -------------");
				ActionErrors errors = new ActionErrors();	
				if(formBean.getReportTitle() != null)
				{
					if(formBean.getReportTitle().trim().length() == 0)
					{
							errors.add("title", new ActionError("error.aim.reportManager.ReportNameAbsent"));
							saveErrors(request, errors);
							flag = true;
							return mapping.findForward("MissingReportDetails");
					}
				}
				
				if(flag == false)
				{
					boolean found = false;
					String queryString = "select report.name from " + AmpReports.class.getName() + " report ";
					logger.info( " Query 2 :" + queryString);
					query = session.createQuery(queryString);
					iter = query.list().iterator();
					int i = 0;
					if(query!=null)
					{
						iter = query.list().iterator();
						while(iter.hasNext())
						{
							String str = (String) iter.next();
							if( formBean.getReportTitle().trim().equals(str) )
							{
		                		errors.add("DuplicateReportName", new ActionError("error.aim.reportManager.DuplicateReportName"));
								saveErrors(request, errors);
								found = true;
								return mapping.findForward("MissingReportDetails");
							}
							else
								found = false;
						}
					}

	                if(found == false)
	                {
						AmpReports ampReports = new AmpReports();
						String descr = "/"+formBean.getReportTitle().replaceAll(" " , "");
						descr = descr + ".do";
						ampReports.setDescription(descr);
						ampReports.setName(formBean.getReportTitle().trim());
						ampReports.setAmpReportId(new Long("0"));
						
						// saving the selected columns for the report
						Set columns = new HashSet();
						if(formBean.getAddedColumns() != null)
						{
							iter = formBean.getAddedColumns().iterator();
							i = 1;
							while(iter.hasNext())
							{
								AmpColumns cols = (AmpColumns)iter.next();
	
								AmpReportColumn	temp = new AmpReportColumn();
								temp.setColumn(cols);
								temp.setOrderId(""+i);
								columns.add(temp);
								i = i + 1;
							}
							ampReports.setColumns(columns);
						}
						
						if(formBean.getColumnHierarchie() != null)
						{
							// saving the column hierarchies in step2 
							Set hierarchies = new HashSet();
							iter  = formBean.getColumnHierarchie().iterator();
							i = 1;
							while(iter.hasNext())
							{
								AmpColumns cols = (AmpColumns)iter.next();
								
								AmpReportHierarchy reportHierarchy = new AmpReportHierarchy();
								reportHierarchy.setColumn(cols);
								reportHierarchy.setLevelId(""+i);
								hierarchies.add(reportHierarchy);
								i = i + 1;
							}
							ampReports.setHierarchies(hierarchies);
						}
						
						// saving the AMp Report Measures
						Set measures = new HashSet();
						if(formBean.getAddedMeasures() != null)
						{
							iter = formBean.getAddedMeasures().iterator();
							i = 1;
							while(iter.hasNext())
							{
								AmpMeasures ampMeasures = (AmpMeasures) iter.next();
								measures.add(ampMeasures);
								i = i + 1;
							}
						}
						
						if(formBean.getSelAdjustType() != null)
						{
							iter = formBean.getSelAdjustType().iterator();
							
							while(iter.hasNext())
							{
								AmpMeasures ampMeasures = (AmpMeasures) iter.next();
								measures.add(ampMeasures);
							}
						}
						ampReports.setMeasures(measures);
						
						session.save(ampReports);
						
						ampReports.setDescription("/viewAdvancedReport.do?view=reset&ampReportId="+ampReports.getAmpReportId());
						session.update(ampReports);
						
						logger.info("***************  START *******");

						logger.info("is Team Head : " + teamMember.getTeamHead());
						if(teamMember.getTeamHead() == true)
						{
							logger.info(teamMember.getMemberName() + " is Team Leader ");
							AmpTeamReports ampTeamReports = new AmpTeamReports();
							ampTeamReports.setTeamView(true);
							AmpTeam ampTeam = (AmpTeam) session.get(AmpTeam.class, teamMember.getTeamId());
							ampTeamReports.setTeam(ampTeam);
							ampTeamReports.setReport(ampReports);
							
							session.save(ampTeamReports);
						}
						else
						{
							logger.info(teamMember.getMemberName() + " is Team Memeber ");
							Long lg = teamMember.getMemberId();
							AmpTeamMember ampTeamMember = (AmpTeamMember) session.get(AmpTeamMember.class, lg);
							Set reportSet = ampTeamMember.getReports();
							reportSet.add(ampReports);
							ampTeamMember.setReports(reportSet);
							
							session.save(ampTeamMember);
						}
			
						// Setting the filters for the New Report that has been created.
						Set pageFilters = new HashSet();
						queryString = "select filters from " + AmpFilters.class.getName() + " filters ";
						logger.info( " Filter Query...:: " + queryString);
						query = session.createQuery(queryString);
						if(query!=null)
						{
							iter = query.list().iterator();
							while(iter.hasNext())
							{
								AmpFilters filt = (AmpFilters) iter.next();
								if(filt.getFilterName().compareTo("Region") != 0 && 
									filt.getFilterName().compareTo("Start Date/Close Date") !=0	&& 
									filt.getFilterName().compareTo("Planned/Actual") != 0 )  
								{
									logger.info("Insertd : " + filt.getFilterName());
									pageFilters.add(filt);
								}
							}
						}
						logger.info("Filter size : " + pageFilters.size());
						
						AmpPages ampPages = new AmpPages();
						ampPages.setFilters(pageFilters);
						ampPages.setPageName(ampReports.getName());
						logger.info(" Page Name  : " + ampPages.getPageName());
						
						String pageCode = "" + ampReports.getName().trim().charAt(0);
						for(int j=0; j <ampReports.getName().length(); j++)
						{
							if(ampReports.getName().charAt(j) == ' ')
									pageCode = pageCode + ampReports.getName().charAt(j+1);
						}
						ampPages.setPageCode(pageCode);
						session.save(ampPages);
						
						
						queryString = "select filters from " + AmpFilters.class.getName() + " filters ";
						logger.info( " Filter Query...:: " + queryString);
						query = session.createQuery(queryString);
						if(query!=null)
						{
							iter = query.list().iterator();
							while(iter.hasNext())
							{
								AmpFilters filt = (AmpFilters) iter.next();
								if(filt.getFilterName().compareTo("Region") != 0 && 
									filt.getFilterName().compareTo("Start Date/Close Date") !=0	&& 
									filt.getFilterName().compareTo("Planned/Actual") != 0 )  
								{
									AmpTeamPageFilters ampTeamPageFilters = new AmpTeamPageFilters();
									ampTeamPageFilters.setFilter(filt);
							//		logger.info("Filter:" + filt.getFilterName());
							
									ampTeamPageFilters.setPage(ampPages);
							//		logger.info("Page" + ampPages.getPageName());
									AmpTeam ampTeam = (AmpTeam) session.get(AmpTeam.class, teamMember.getTeamId());
									ampTeamPageFilters.setTeam(ampTeam);
								//	logger.info("Team:" + ampTeam.getName())
									session.save(ampTeamPageFilters);
								}						
							}
						}

						logger.info("***************  END   *******");
						
						
						tx.commit(); // commit the transcation
						// Clears the values of the Previous report 
						formBean.setAmpColumns(null);
						formBean.setAddedColumns(null);
						formBean.setColumnHierarchie(null);
						formBean.setAddedMeasures(null);
						
	                }
					logger.info("-------------Stop-----------");
				}
				
				return mapping.findForward("viewMyDesktop");
			}

		}
		catch(Exception e)
		{
			e.printStackTrace(System.out);
		}
		finally {
			if (session != null) {
				try {
					PersistenceManager.releaseSession(session);
				} catch (Exception e) {
					logger.error("Release session faliled :" + e);
				}
			}
		}

		return mapping.findForward("forward");
	}// end of function execute
	
// Function for Pie Chart
		private static String createPieChart(Collection chart_coll){

				logger.info("@@@@@@@@@@ INSIDE createPieChart...");
				Iterator iter3 = chart_coll.iterator();
				//logger.info("@@@@@@@@@@ flag:"+chart_coll.size());
				String temp="";
				Double demp;

				DefaultPieDataset data = new DefaultPieDataset();
//		        data.setValue("test12Aug", new Double(2.0));
		        
				while (iter3.hasNext()) {
					demp=new Double(iter3.next().toString());
					temp= (String)iter3.next();
					//logger.info(temp+":::::"+demp);
					data.setValue(temp, demp);
//					iter.next();
		        }

		        //  Create the chart object
				//logger.info("@@@@@@@@@@ PLOTTTTTTT:");
		        PiePlot plot = new PiePlot(data);
		        plot.setURLGenerator(new StandardPieURLGenerator("xy_chart.jsp","section"));
		        plot.setToolTipGenerator(new StandardPieItemLabelGenerator());
				//logger.info("@@@@@@@@@@ Chart Object:");
				JFreeChart chart = new JFreeChart("", JFreeChart.DEFAULT_TITLE_FONT, plot, false);
		        chart.setBackgroundPaint(java.awt.Color.white);

		        //  Write the chart image to the temporary directory
		        ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
		        String filename = "";
		        try{
				//logger.info("@@@@@@@@@@ IMAGE CREATION PNG:");
				filename = ServletUtilities.saveChartAsPNG(chart, 600, 550, info, httpSession);
//				ServletUtilities.sendTempFile(filename, response);
				}
				catch(Exception e){
					logger.info("EXCEPTION thrown at PIECHARTimage: "+e);				
				}

			return filename;
		}
// end pie chart function.

//		 Function for BarChart
		private static String createBarChart(Collection chart_coll){

			logger.info("@@@@@@@@@@ INSIDE createBARRRRRR Chart...");

			String filename = null;
			DefaultCategoryDataset data = new DefaultCategoryDataset(); 
	        Iterator iter = chart_coll.iterator();
			////System.out.println("@@@@@@@@@@ flag:"+chart_coll.size());

//			data.addValue(new Double(30.0), "CDAC", "");
			
			String temp="";
			Double demp;

			while (iter.hasNext()) {
				demp=new Double(iter.next().toString());
				temp= (String)iter.next();
//				System.out.println(temp+":::::"+demp);
				data.addValue(demp,temp,"comm");
				demp=new Double(iter.next().toString());
				temp= (String)iter.next();
//				System.out.println(temp+":::::"+demp);
				data.addValue(demp,temp,"disb");
				demp=new Double(iter.next().toString());
				temp= (String)iter.next();
//				System.out.println(temp+":::::"+demp);
				data.addValue(demp,temp,"exp");
	        }

		// Create the chart object 

			CategoryDataset categorydataset = new DefaultCategoryDataset();
			categorydataset = data;

			String chartTitle=formBean.getReportTitle();
//			System.out.println("CHART TITLE----(ACTION)----------------"+chartTitle);
			
			JFreeChart jfreechart = ChartFactory.createBarChart(chartTitle+" - Bar Chart", "Title", "Amount(in US$)", categorydataset, PlotOrientation.VERTICAL,true, true, true);
			jfreechart.setBackgroundPaint(java.awt.Color.white);
			
			// Write the chart image to the temporary directory 
			ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
	
			logger.info("  Chart Size : " +chart_coll.size());
			int dim=chart_coll.size()/6;
			int x=dim*30;
			int y=dim*25;
				System.out.println("IMG DIMMMMMMMMMMMMM: "+"dim="+dim+"x="+x+"::"+y);
			try{
			filename = ServletUtilities.saveChartAsPNG(jfreechart, x, y, info, httpSession);
			}
			catch(Exception e){
					logger.info("EXCEPTION thrown at image:"+e);				
			}

//			//System.out.println("@@@@@@@@@@IMAGE FILE NAME:"+filename);

			return filename;
		}
// end barchart function.

	public void updateData(Collection src, Collection dest, Long []selCol, AdvancedReportForm formBean)
	{
		if(str.equals("delete"))
		{
			if(src == null)
				return;
		}
		Iterator iter;
		Collection coll = new ArrayList();
		Collection temp = new ArrayList();
		Collection  dup= new ArrayList();
		AmpColumns ampColumns;
		AmpMeasures ampMeasures = null, tempMeasures = null;
		boolean flag=false;
		AmpColumns colTemp = null;
		try
		{
			if(selCol != null)
			{
				if(dest == null)
				{
					dup = src;
					coll.clear();
					temp.clear();
				}
				else
				{
					temp.clear();
					dup = src;
					coll = dest;
				}
				
				for(int i=0; i < selCol.length; i++)
				{
					iter = src.iterator();// change needed
					temp.clear();
					while(iter.hasNext())
					{
						if(str.equals("AddMeasure") == true || str.equals("DeleteMeasure") == true 
								|| str.equals("AddAdjustType") == true || str.equals("DeleteAdjustType") == true )
						{
							ampMeasures = (AmpMeasures) iter.next();
							if(ampMeasures.getMeasureId().compareTo(selCol[i]) == 0 )
							{
								coll.add(ampMeasures);
								tempMeasures = ampMeasures;
								flag = true;
							}
							else
							{
								if(temp.contains(ampMeasures) == false)
									temp.add(ampMeasures);
							}
						}
						else
						{
							ampColumns = (AmpColumns) iter.next();
							if(ampColumns.getColumnId().compareTo(selCol[i]) == 0)
							{
								coll.add(ampColumns);
								colTemp = ampColumns;
								flag = true;
							}
							else
							{
								if(temp.contains(ampColumns) == false)
									temp.add(ampColumns);
							}
						}
					}
					if(flag == true && colTemp != null)
					{
						dup.remove(colTemp);
						flag = false;
						src = dup;
					}
					if(flag == true && tempMeasures != null)
					{
						dup.remove(tempMeasures);
						flag = false;
						src = dup;
					}

				}

				if(str.equals("add"))
				{
					formBean.setAddedColumns(coll);
					formBean.setAmpColumns(temp);
				}
				if(str.equals("delete"))
				{
					formBean.setAddedColumns(temp);
					formBean.setAmpColumns(coll);
				}
				if(str.equals("Step2AddRows") == true)
				{
					formBean.setColumnHierarchie(coll);
					formBean.setAddedColumns(temp);
				}
				if(str.equals("Step2DeleteRows"))
				{
					formBean.setColumnHierarchie(temp);
					formBean.setAddedColumns(coll);
				}
			
				if(str.equals("AddMeasure") == true)
				{
					formBean.setAddedMeasures(coll);
					formBean.setAmpMeasures(temp);
				}
				if(str.equals("DeleteMeasure"))
				{
					formBean.setAddedMeasures(temp);
					formBean.setAmpMeasures(coll);
				}
				if(str.equals("AddAdjustType") == true)
				{
					formBean.setSelAdjustType(coll);
					formBean.setAdjustType(temp);
				}
				if(str.equals("DeleteAdjustType") == true)
				{
					formBean.setSelAdjustType(temp);
					formBean.setAdjustType(coll);
				}
				
			}
		}
		catch(Exception e)
		{
			e.printStackTrace(System.out);
		}
	}// end of Function ...........
	
	// Function to move Columns Up and Down
	
	private static void moveColumns(AdvancedReportForm formBean, String option)
	{
		Iterator iter= null;
		AmpColumns ampColumns = null;
		AmpMeasures ampMeasures  = null;
		Collection tempColl = new ArrayList();

		if(option.equals("MoveUp") || option.equals("MoveDown"))
		{
			tempColl = formBean.getAddedColumns();
			logger.info("Step1 " + option);
		}
		if(option.equals("Step2MoveUp") || option.equals("Step2MoveDown"))
		{
			tempColl = formBean.getColumnHierarchie();
			logger.info("Step2 " + option);
		}
		if(option.equals("MoveUpMeasure") || option.equals("MoveDownMeasure"))
		{
			tempColl = formBean.getAddedMeasures();
			logger.info("Step3  : " + option);
		}
		if(option.equals("MoveUpAdjustType") || option.equals("MoveDownAdjustType"))
			tempColl = formBean.getSelAdjustType();
			
		if(tempColl.size() == 1)
			logger.info(" Cannot move field up.......");
		else
		{
			Long lg = new Long(formBean.getMoveColumn());
			ArrayList temp = new ArrayList();
			AmpColumns curr = null, prev = null , next = null;
			AmpMeasures currMeasure, prevMeasure, nextMeasure;
			int index = 0;
			
			temp.addAll(tempColl);
			iter = temp.iterator();		
			logger.info(lg.toString() + " <<< Field Selected >>>> " + formBean.getMoveColumn() + "??????" + tempColl.size());
			while(iter.hasNext())
			{
				if(option.equals("MoveUpMeasure")== true || option.equals("MoveDownMeasure")== true
					|| option.equals("MoveUpAdjustType")== true || option.equals("MoveDownAdjustType")== true)
				{
					ampMeasures = (AmpMeasures) iter.next();
					if(option.equals("MoveUpMeasure")== true || option.equals("MoveUpAdjustType")== true)
					{
						if(lg.compareTo(ampMeasures.getMeasureId()) == 0 )
						{
							if(temp.indexOf(ampMeasures) > 0)
							{
								currMeasure = (AmpMeasures)temp.get(temp.indexOf(ampMeasures));
								prevMeasure = (AmpMeasures)temp.get(temp.indexOf(ampMeasures)-1);
								index = temp.indexOf(ampMeasures);
								temp.set(index, prevMeasure);
								temp.set(index-1, currMeasure);
								if(option.equals("MoveUpAdjustType"))
									formBean.setSelAdjustType(temp);
								if(option.equals("MoveUpMeasure"))
									formBean.setAddedMeasures(temp);
								
								break;
							}
						}
					}
					if(option.equals("MoveDownMeasure")== true  || option.equals("MoveDownAdjustType")== true )
					{
						if(lg.compareTo(ampMeasures.getMeasureId()) == 0 )
						{
							if( (temp.indexOf(ampMeasures)+1) < tempColl.size() )
							{
								currMeasure = (AmpMeasures)temp.get(temp.indexOf(ampMeasures));
								nextMeasure = (AmpMeasures)temp.get(temp.indexOf(ampMeasures)+1);
								index = temp.indexOf(ampMeasures);
								temp.set(index, nextMeasure);
								temp.set(index+1, currMeasure);
								if(option.equals("MoveDownAdjustType"))
									formBean.setSelAdjustType(temp);
								if(option.equals("MoveDownMeasure"))
									formBean.setAddedMeasures(temp);
								
								break;
							}
						}
					}

				}
				else
				{
					ampColumns = (AmpColumns) iter.next();
					
					if(option.compareTo("MoveUp") == 0 || option.compareTo("Step2MoveUp") == 0)
					{
						if(lg.compareTo(ampColumns.getColumnId()) == 0 )
						{
							if(temp.indexOf(ampColumns) > 0)
							{
								curr = (AmpColumns)temp.get(temp.indexOf(ampColumns));
								prev = (AmpColumns)temp.get(temp.indexOf(ampColumns)-1);
								index = temp.indexOf(ampColumns);
		
								temp.set(index, prev);
								temp.set(index-1, curr);
								if(option.equals("MoveUp"))
									formBean.setAddedColumns(temp);
								if(option.equals("Step2MoveUp"))
									formBean.setColumnHierarchie(temp);
								break;
							}
							else
							{
								logger.info("Cannot  Swap.........");
								break;								
							}
						}
					} // This code moves the selected Column Up
					
					if(option.compareTo("MoveDown") == 0 || option.compareTo("Step2MoveDown") == 0)
					{
						if(lg.compareTo(ampColumns.getColumnId()) == 0 )
						{
							logger.info(" Found : " + temp.indexOf(ampColumns));
							if( (temp.indexOf(ampColumns)+1) < tempColl.size())
							{
								curr = (AmpColumns)temp.get(temp.indexOf(ampColumns));
								next = (AmpColumns)temp.get(temp.indexOf(ampColumns)+1);
								index = temp.indexOf(ampColumns);
								temp.set(index, next);
								temp.set(index+1, curr);
								if(option.equals("MoveDown"))
									formBean.setAddedColumns(temp);
								if(option.equals("Step2MoveDown"))
									formBean.setColumnHierarchie(temp);
								
								break;
							}
							else
							{
								logger.info("Cannot  Swap.........");
								break;								
							}
						}
					} // This code moves the selected Column Down
				}				
			}
		}
	}
	
}
