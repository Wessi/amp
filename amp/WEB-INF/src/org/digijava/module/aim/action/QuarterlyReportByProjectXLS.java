package org.digijava.module.aim.action;	

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpSession;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionServlet;
import org.digijava.module.aim.form.MulitlateralbyDonorForm ;
import org.digijava.module.aim.helper.AmpFund;
import org.digijava.module.aim.helper.AmpTeamDonors;
import org.digijava.module.aim.helper.ProjectTermAssist;
import org.digijava.module.aim.helper.TermFund;
import org.digijava.module.aim.helper.TermFundTotal;
import org.digijava.module.aim.helper.multiReport ;
import org.digijava.module.aim.helper.Project ;
import org.digijava.module.aim.helper.fiscalYrs ;
import org.digijava.module.aim.helper.FundTotal ;
import org.digijava.module.aim.helper.ProjectbyDonorDatasource;
import org.digijava.module.aim.helper.QuarterlyReportByProjectXlsJrxml;


public class QuarterlyReportByProjectXLS extends Action 
{
	private static Logger logger = Logger.getLogger(QuarterlyReportByProjectXLS.class) ;
	
	public ActionForward execute(ActionMapping mapping,
								ActionForm form, javax.servlet.http.HttpServletRequest request,
								javax.servlet.http.HttpServletResponse response) 
	throws java.lang.Exception
	{
		
		HttpSession session = request.getSession();
		MulitlateralbyDonorForm formBean = (MulitlateralbyDonorForm) form;
		//MulitlateralbyDonorForm formBean = (MulitlateralbyDonorForm) session.getAttribute("ampReports");
		Collection coll = new ArrayList();
		int yearCount=0;
		if (formBean != null) {
			logger.info("formBean is not null");
			coll= formBean.getMultiReport();
			yearCount = formBean.getFiscalYearRange().size();
			
		} else {
			logger.info("formbean is null");
		}
		if(coll == null)
			logger.info("coll is null");
		else
			logger.info("formBean is not null");
		
		Iterator iter = null;
		if (coll.size() == 0) {
			logger.info("collection is empty");
		} else {
			logger.info("collection is not empty");
			iter = coll.iterator();
		}
		
		logger.info("col size "+ coll.size());
		multiReport multiReport = new multiReport();
		Project project = new Project();
		FundTotal fundTotal = new FundTotal();
		ProjectTermAssist projectTermAssist = new ProjectTermAssist();
		AmpTeamDonors ampTeamDonors  = new AmpTeamDonors();
		AmpFund ampFund = new AmpFund();
		TermFund  termFund = new TermFund();
		TermFundTotal termFundTotal = new TermFundTotal();
		
		Collection ampProjects,ampFunds,ampFundTotals,ampFisYears,totalDonorTermAssistFunds,totalDonorFunds;
		Collection totalTeamTermAssistFunds, totalTeamFunds,termAssistFunds, donors, termAssists, termFundTotals;
		Iterator projectIter, fundIter,fundTotalIter, totalDonorTermAssistFundIter, totalDonorFundIter,termFundTotalIter;
		Iterator totalTeamTermAssistFundIter, totalTeamFundIter, termAssistFundIter, donorIter,termAssistIter;
		Integer year;
		int row=0, col=0, yy=0, yyTmp=0, flag =0, rowCnt=0, colCnt=0;
		
		if(formBean != null	)
		{
			yearCount = formBean.getFiscalYearRange().size();
			//System.out.println("Year Coutn : " + yearCount );
		}
		if(coll != null)
		{
			while(iter.hasNext())
			{
				multiReport = (multiReport)iter.next();
				donors = new ArrayList(multiReport.getDonors());
				donorIter = donors.iterator();
				while(donorIter.hasNext())
				{
					ampTeamDonors = (AmpTeamDonors) donorIter.next();
					ampProjects = ampTeamDonors.getProject();
					projectIter = ampProjects.iterator();
					while(projectIter.hasNext())
					{
						project = (Project) projectIter.next();
						rowCnt = rowCnt + 1;
						logger.info(rowCnt + " :" + project.getName());
						
						if(project.getTermAssist() != null)
						{
							termAssists = new ArrayList(project.getTermAssist());
							termAssistIter = termAssists.iterator();
							while(termAssistIter.hasNext())
							{
								projectTermAssist = (ProjectTermAssist) termAssistIter.next();
								rowCnt = rowCnt + 1;
								logger.info(rowCnt + " :" + projectTermAssist.getTermAssistName());
							}
						}// IF Term Assist si NOT NULL
						
					} // End of Project Iter
					
					if(ampTeamDonors.getTotalDonorTermAssistFund() != null)
					{
						totalDonorTermAssistFunds = ampTeamDonors.getTotalDonorTermAssistFund();
						totalDonorTermAssistFundIter = totalDonorTermAssistFunds.iterator();
						while(totalDonorTermAssistFundIter.hasNext())
						{
							termFund = (TermFund) totalDonorTermAssistFundIter.next();
							rowCnt = rowCnt + 1;
							logger.info(rowCnt + " :" + "Total  " +termFund.getTermAssistName());
						}
					} // ENd of Total Donor Term Assist Fund 
					rowCnt = rowCnt + 1;
					logger.info(rowCnt + " : " + "Total for 11 : " + ampTeamDonors.getDonorAgency());
				}// End of Donor Iteration
			
				if(multiReport.getTotalTeamTermAssistFund() != null)
				{
					totalTeamTermAssistFunds = new ArrayList(multiReport.getTotalTeamTermAssistFund());
					totalTeamTermAssistFundIter = totalTeamTermAssistFunds.iterator();
					while(totalTeamTermAssistFundIter.hasNext())
					{
						termFund = (TermFund) totalTeamTermAssistFundIter.next();
						rowCnt = rowCnt + 1;
						logger.info(rowCnt + " :" + termFund.getTermAssistName());
					}
				}
				rowCnt = rowCnt + 1;
				logger.info(rowCnt + " : " + "Total for  22: " + multiReport.getTeamName());				
				
			} // End of Objects Collection
		}// ENd of IF 
		
		colCnt = 5 + formBean.getFiscalYearRange().size() +  (formBean.getFiscalYearRange().size() * 12) + 4;
		logger.info(rowCnt + " : Row : COl: " + colCnt );
		Object[][] data = new Object[rowCnt][colCnt];
		String teamName, donor;
		row = col = 0;
		if(coll != null)
		{
			iter = coll.iterator();
			data[row][col] =formBean.getWorkspaceType() + " " + formBean.getWorkspaceName() ;
			for(int i=0; i<formBean.getFilter().length; i++)
			{	
				col = col + 1;
				data[row][col] = formBean.getFilter()[i].toString();
			}
			
			while(iter.hasNext())
			{
				col = 2;
				multiReport = (multiReport)iter.next();
				//col+=1;
				//data[row][col] = multiReport.getTeamName();
				 teamName = multiReport.getTeamName();
				
				donors = new ArrayList(multiReport.getDonors());
				donorIter = donors.iterator();
				while(donorIter.hasNext())
				{
					col = 2;
					ampTeamDonors = (AmpTeamDonors) donorIter.next();
					//col+=1;
					//data[row][col]  = ampTeamDonors.getDonorAgency();
					donor = ampTeamDonors.getDonorAgency();
					ampProjects = ampTeamDonors.getProject();
					projectIter = ampProjects.iterator();
					while(projectIter.hasNext())
					{
						col = 2;
						project = (Project) projectIter.next();
						col+=1;
						data[row][col] = multiReport.getTeamName();
						col+=1;
						data[row][col]  = ampTeamDonors.getDonorAgency();
						col+=1;
						data[row][col] = project.getName();
						
						// Stores the years consecutivily
						ampFisYears = new ArrayList((formBean.getFiscalYearRange()));
						Iterator fiscIter = ampFisYears.iterator();
						fiscalYrs fisc=null;
						if(fiscIter.hasNext() == true)
						{
							year = (Integer)fiscIter.next();
							yy = year.intValue();
						}
						yyTmp = yy;
						for(int i=0; i< formBean.getFiscalYearRange().size(); i++)
						{
							col+=1;
							data[row][col] = Integer.toString(yy);
							yy = yy + 1;
						}

						ampFunds = project.getAmpFund();
						fundIter = ampFunds.iterator();
						while(fundIter.hasNext())
						{
							ampFund = (AmpFund) fundIter.next();
							col+=1;
							data[row][col] = ampFund.getPlannedDisbAmount();;
							col+=1;
							data[row][col] = ampFund.getDisbAmount();
							col+=1;
							data[row][col] = ampFund.getExpAmount();
						}
						col+=1;
						data[row][col] = project.getProjPlannedDisbAmount();
						col+=1;
						data[row][col] = project.getProjDisbAmount();
						col+=1;
						data[row][col] = project.getProjExpAmount();
						
						if(project.getTermAssist() != null)
						{
							termAssists = new ArrayList(project.getTermAssist());
							termAssistIter = termAssists.iterator();
							while(termAssistIter.hasNext())
							{
								col = 4;
								row = row + 1;
								data[row][3] = multiReport.getTeamName();;
								projectTermAssist = (ProjectTermAssist) termAssistIter.next();
								data[row][col] = ampTeamDonors.getDonorAgency();
								col+=1;
								data[row][col] = projectTermAssist.getTermAssistName();
								col = 5 + formBean.getFiscalYearRange().size();
								termAssistFunds = new ArrayList(projectTermAssist.getTermAssistFund());
								termAssistFundIter = termAssistFunds.iterator();
								while(termAssistFundIter.hasNext())
								{
									ampFund = (AmpFund) termAssistFundIter.next();
									col+=1;
									data[row][col] = ampFund.getPlannedDisbAmount();
									col+=1;
									data[row][col] = ampFund.getDisbAmount();
									col+=1;
									data[row][col] = ampFund.getExpAmount();
								}
								col+=1;
								data[row][col] = projectTermAssist.getTermPlannedDisbAmount();
								col+=1;
								data[row][col] = projectTermAssist.getTermDisbAmount();
								col+=1;
								data[row][col] = projectTermAssist.getTermExpAmount();
							}
						}// IF Term Assist si NOT NULL
						row = row + 1;
					} // End of Project Iter
					
					row = row - 1;
					
					if(ampTeamDonors.getTotalDonorTermAssistFund() != null)
					{
						totalDonorTermAssistFunds = ampTeamDonors.getTotalDonorTermAssistFund();
						totalDonorTermAssistFundIter = totalDonorTermAssistFunds.iterator();
						while(totalDonorTermAssistFundIter.hasNext())
						{
							col = 4;
							row = row + 1;
							data[row][3] = multiReport.getTeamName();
							data[row][4] = ampTeamDonors.getDonorAgency();
							termFund = (TermFund) totalDonorTermAssistFundIter.next();
							col+=1;
							//System.out.println(row + " : Total  "+termFund.getTermAssistName());
							data[row][col] = "Total " +termFund.getTermAssistName();
							
							col = 5 + formBean.getFiscalYearRange().size();
							
							termFundTotals = new ArrayList(termFund.getTermFundTotal());
							termFundTotalIter = termFundTotals.iterator();
							while(termFundTotalIter.hasNext())
							{
								termFundTotal = (TermFundTotal) termFundTotalIter.next();
								col+=1;
								data[row][col] = termFundTotal.getTotPlannedDisbAmount();
								col+=1;
								data[row][col] = termFundTotal.getTotDisbAmount();
								col+=1;
								data[row][col] = termFundTotal.getTotExpAmount();
							}
							col+=1;
							data[row][col] = termFund.getTotDonorPlannedDisbAmount();
							col+=1;
							data[row][col] = termFund.getTotDonorDisbAmount();
							col+=1;
							data[row][col] = termFund.getTotDonorExpAmount();
						}
					} // ENd of Total Donor Term Assist Fund 
					
					col= 4;
					row = row + 1;
					data[row][3] = multiReport.getTeamName();
					data[row][4] = ampTeamDonors.getDonorAgency();
					col+=1;
					data[row][col] = "Total for " + ampTeamDonors.getDonorAgency();
					col = 5 + formBean.getFiscalYearRange().size();
					totalDonorFunds = ampTeamDonors.getTotalDonorFund();
					totalDonorFundIter = totalDonorFunds.iterator();
					while(totalDonorFundIter.hasNext())
					{
						fundTotal = (FundTotal) totalDonorFundIter.next();
						col+=1;
						data[row][col] = fundTotal.getTotPlannedDisbAmount();
						col+=1;
						data[row][col] = fundTotal.getTotDisbAmount();
						col+=1;
						data[row][col] = fundTotal.getTotExpAmount();
						
					}
					col+=1;
					data[row][col] = ampTeamDonors.getDonorPlannedDisbAmount();
					col+=1;
					data[row][col] = ampTeamDonors.getDonorDisbAmount();
					col+=1;
					data[row][col] = ampTeamDonors.getDonorDisbAmount();
					
					row = row + 1;
				}// End of Donor Iteration
			
				row = row - 1;
				
				if(multiReport.getTotalTeamTermAssistFund() != null)
				{
					totalTeamTermAssistFunds = new ArrayList(multiReport.getTotalTeamTermAssistFund());
					totalTeamTermAssistFundIter = totalTeamTermAssistFunds.iterator();
					while(totalTeamTermAssistFundIter.hasNext())
					{
						col = 4;
						row = row + 1;
						data[row][3] = multiReport.getTeamName();
						data[row][4] = ampTeamDonors.getDonorAgency();
						termFund = (TermFund) totalTeamTermAssistFundIter.next();
						col+=1;
						data[row][col] = "Total " + termFund.getTermAssistName();
						
						col = 5 + formBean.getFiscalYearRange().size();
						
						termFundTotals = termFund.getTermFundTotal();
						termFundTotalIter = termFundTotals.iterator();
						while(termFundTotalIter.hasNext())
						{
							termFundTotal = (TermFundTotal) termFundTotalIter.next();
							col+=1;
							data[row][col] = termFundTotal.getTotCommAmount();
							col+=1;
							data[row][col] = termFundTotal.getTotDisbAmount();
							col+=1;
							data[row][col] = termFundTotal.getTotExpAmount();
						}
						col+=1;
						data[row][col] = termFund.getTotDonorCommAmount();
						col+=1;
						data[row][col] = termFund.getTotDonorDisbAmount();
						col+=1;
						data[row][col] = termFund.getTotDonorExpAmount();
					}
				}
				
				col= 4;
				row = row + 1;
				data[row][3] = multiReport.getTeamName();;
				data[row][4] = ampTeamDonors.getDonorAgency();
				col+=1;
				data[row][col] = "Total for " + multiReport.getTeamName();
				col = 5 + formBean.getFiscalYearRange().size();
				
				totalTeamFunds = new ArrayList(multiReport.getTotalTeamFund());
				totalTeamFundIter  = totalTeamFunds.iterator();
				while(totalTeamFundIter.hasNext())
				{
					fundTotal = (FundTotal) totalTeamFundIter.next();
					col+=1;
					data[row][col] = fundTotal.getTotPlannedDisbAmount();
					col+=1;
					data[row][col] = fundTotal.getTotDisbAmount();
					col+=1;
					data[row][col] = fundTotal.getTotExpAmount();
					
				}
				col+=1;
				data[row][col] = multiReport.getTeamPlannedDisbAmount();
				col+=1;
				data[row][col] = multiReport.getTeamDisbAmount();
				col+=1;
				data[row][col] = multiReport.getTeamExpAmount();
				
				row = row + 1;
				flag = 1;
				
			} // End of Objects Collection
		}// ENd of IF 


		if(flag == 1)
		{
			
			for(int i=0; i < rowCnt; i++)
			{
				for(int j=0; j < colCnt; j++)
				{
					if(data[i][j] != null)
					{
						if(data[i][j].equals("0"))
						{
							data[i][j] = "";
						}
					}
					else
					{
						data[i][j] = "";
					}	
				}
			}
			
			String fileName="QuaterlyREportByProjectXls.jrxml";
			ProjectbyDonorDatasource dataSource = new ProjectbyDonorDatasource(data);
			ActionServlet s = getServlet();
			String jarFile = s.getServletContext().getRealPath(
								"/WEB-INF/lib/jasperreports-0.6.1.jar");
			System.setProperty("jasper.reports.compile.class.path",jarFile);
			String realPathJrxml = s.getServletContext().getRealPath(
							 	"/WEB-INF/classes/org/digijava/module/aim/reports");
			realPathJrxml = realPathJrxml + "\\" +"quarterlyReportByProjectXls.jrxml";
			QuarterlyReportByProjectXlsJrxml jrxml = new QuarterlyReportByProjectXlsJrxml();
			jrxml.createJrxml(realPathJrxml,yearCount);
			JasperCompileManager.compileReportToFile(realPathJrxml);
			if(request.getParameter("docType") != null && request.getParameter("docType").equals("xls"))
			{
			byte[] bytes = null;
			String jasperFile = s.getServletContext().getRealPath(
							 "/WEB-INF/classes/org/digijava/module/aim/reports/quarterlyReportByProjectXls.jasper");
			Map parameters = new HashMap();
			JasperPrint jasperPrint = 
			JasperFillManager.fillReport(jasperFile, parameters, dataSource);
			String destFile = s.getServletContext().getRealPath("/WEB-INF/classes/org/digijava/module/aim/reports/quarterlyReportByProjectXls.xls");
			ServletOutputStream outputStream = null;
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition","inline; filename=quarterlyReportByProjectXls.xls");
			try
			{
				outputStream = response.getOutputStream();
				JRXlsExporter exporter = new JRXlsExporter();
				exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
				exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, outputStream);
				exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
				exporter.exportReport();
			}
			catch (Exception e) 
			{
				if (outputStream != null) 
					outputStream.close();
			}

		}
		else if(request.getParameter("docType") != null && request.getParameter("docType").equals("csv"))
		{
				logger.info("EXPORTING CSV for QuaterlyTeamreportbyProject");
				ServletOutputStream outputStream = null;
				try
				{
				
					Map parameters = new HashMap();
					byte[] bytes = null;
					String jasperFile = s.getServletContext().getRealPath("/WEB-INF/classes/org/digijava/module/aim/reports/quarterlyReportByProjectXls.jasper");
					JasperPrint jasperPrint = JasperFillManager.fillReport(jasperFile,parameters,dataSource);
					String destFile = s.getServletContext().getRealPath("/WEB-INF/src/org/digijava/module/aim/reports/quarterlyReportByProjectXls.csv");
					
					//JasperPrint jasperPrint = JasperFillManager.fillReport(jasperFile,parameters,dataSource);
						response.setContentType("application/vnd.ms-excel");
						String responseHeader = "inline; filename="+destFile;
						logger.info("--------------" + responseHeader);
						response.setHeader("Content-Disposition", responseHeader);
						//response.setHeader("Content-Disposition","inline; filename=commitmentByModalityXls.xls");
						logger.info("--------------");
					//JRXlsExporter exporter = new JRXlsExporter();
					JRCsvExporter exporter = new JRCsvExporter();
					outputStream = response.getOutputStream();
					exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
					exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, outputStream);
					//exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
					exporter.exportReport();
				}
				catch (Exception e) 
				{
					if (outputStream != null) 
					{
						outputStream.close();
					}	
				}
		}
		}
		return null;
	}// end of Execute Func
}// end of Class
	