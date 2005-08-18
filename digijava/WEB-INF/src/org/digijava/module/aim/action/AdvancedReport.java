
/*
 * AdvancedReport.java @Author Ronald B Created: 27-July-2005
 */

package org.digijava.module.aim.action;


import java.util.Iterator;
import java.util.Collection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.module.aim.dbentity.AmpColumns;
import org.digijava.module.aim.dbentity.AmpMeasures;
import org.digijava.module.aim.dbentity.AmpReportColumn;
import org.digijava.module.aim.dbentity.AmpReportHierarchy;
import org.digijava.module.aim.dbentity.AmpReports;
import org.digijava.kernel.persistence.PersistenceManager;

import org.jfree.data.*;
import org.jfree.chart.*;
import org.jfree.chart.axis.*;
import org.jfree.chart.plot.*;
import org.jfree.ui.RectangleInsets;
import org.jfree.chart.entity.*;
import org.jfree.chart.labels.*;
import org.jfree.chart.urls.*;
import org.jfree.chart.servlet.*;
import org.jfree.data.general.DefaultPieDataset;

import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;

import org.digijava.module.aim.form.AdvancedReportForm;
import org.digijava.module.aim.helper.TeamMember;
import org.digijava.module.aim.helper.Report;
import org.digijava.module.aim.util.ReportUtil;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;


public class AdvancedReport extends Action {

	private static Logger logger = Logger.getLogger(Login.class);
	private String str="";
	public ActionForward execute(ActionMapping mapping, ActionForm form, 
			HttpServletRequest request, HttpServletResponse response) throws java.lang.Exception
	{
		AdvancedReportForm formBean = (AdvancedReportForm) form;
		
		HttpSession httpSession = request.getSession();
		Query query;
		Session session = null;
		Transaction tx = null;
		String sqlQuery;
		Iterator iter;
		Collection coll = new ArrayList();
		AmpColumns ampColumns;
		TeamMember teamMember=(TeamMember)httpSession.getAttribute("currentMember");

		
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
			// Fills the column that can be selected from AMP_COLUMNS
			if(formBean.getAmpColumns() == null)
			{
				formBean.setAmpColumns(ReportUtil.getColumnList());
				formBean.setAmpMeasures(ReportUtil.getMeasureList());				
				formBean.setReportTitle("");
			}
			else
				logger.info(" AmpColumns is not NULL........");
			
			// add columns that are available
			
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
			// Goto Step 2.
			if(request.getParameter("check") != null && request.getParameter("check").equals("5"))
			{
				logger.info("In here  generating data..........");
				if(formBean.getColumnHierarchie() != null)
				{
					//coll = formBean.getAddedColumns();
					coll = formBean.getColumnHierarchie();
					if(coll != null)
					{
						formBean.setFinalData( ReportUtil.generateQuery(coll, ampTeamId) );
					}
				}
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
			if(request.getParameter("check") != null && request.getParameter("check").equals("3"))
			{
				logger.info("In here  chart process####..........");

				//logger.info("###########################Inside GeneratePIEChart..:):)");
				//logger.info("CHART FORMBEAN SIZE::::::::::::::::"+formBean.getFinalData().size());
		        Iterator iter2 = formBean.getFinalData().iterator();
		        
		        Collection chart_coll=new ArrayList();
		    	chart_coll.add("60");
		    	chart_coll.add("Donor 1");

				while(iter2.hasNext()){
					Report r= (Report) iter2.next();
					chart_coll.add(r.getAcCommitment().replaceAll("," , ""));
					logger.info("filling COMM into the COLLLLL."+r.getAcCommitment());
					chart_coll.add(r.getDonor());
					//logger.info("filling DONOR NAME into the COLLLLL."+r.getDonor());
				}
				
				
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
				JFreeChart chart = new JFreeChart("", JFreeChart.DEFAULT_TITLE_FONT, plot, true);
		        chart.setBackgroundPaint(java.awt.Color.white);

		        //  Write the chart image to the temporary directory
		        ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
		        String filename = "";
		        try{
				//logger.info("@@@@@@@@@@ IMAGE CREATION PNG:");
				filename = ServletUtilities.saveChartAsJPEG(chart, 600, 550, info, httpSession);

				//logger.info("@@@@@@@@@@IMAGE FILE NAME:"+filename);

				formBean.setImageUrl(filename);
//				ServletUtilities.sendTempFile(filename, response);
				}
				catch(Exception e)
				{
					//logger.info("EXCEPTION thrown at image:"+e);				
				}
				

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
					double actCommit = 0, actDisb = 0, unDisb = 0;

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
						ampReports.setName(formBean.getReportTitle());
						ampReports.setAmpReportId(new Long("0"));
						
						// saving the selected columns for the report
						Set columns = new HashSet();
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
						
						session.save(ampReports);
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
						if(str.equals("AddMeasure") == true || str.equals("DeleteMeasure") == true)
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
				if(option.equals("MoveUpMeasure")== true || option.equals("MoveDownMeasure")== true)
				{
					ampMeasures = (AmpMeasures) iter.next();
					if(option.equals("MoveUpMeasure")== true)
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
								formBean.setAddedMeasures(temp);
								break;
							}
						}
					}
					if(option.equals("MoveDownMeasure")== true)
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
