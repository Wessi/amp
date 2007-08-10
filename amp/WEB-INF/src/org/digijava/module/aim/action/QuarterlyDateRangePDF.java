package org.digijava.module.aim.action;	

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletOutputStream;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperRunManager;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionServlet;
import org.digijava.module.aim.form.CommitmentbyDonorForm;
import org.digijava.module.aim.helper.AmpFund;
import org.digijava.module.aim.helper.QuarterlyDateRangeDatasource;
import org.digijava.module.aim.helper.Report;
import org.digijava.module.aim.helper.fiscalYrs; 
import org.digijava.module.aim.helper.QdrJrxml;


public class QuarterlyDateRangePDF
		  extends Action {

		  private static Logger logger = Logger.getLogger(QuarterlyDateRangePDF.class) ;
		  
		  public ActionForward execute(ActionMapping mapping,
								ActionForm form,
								javax.servlet.http.HttpServletRequest request,
								javax.servlet.http.HttpServletResponse response) 
					 throws java.lang.Exception {

					 CommitmentbyDonorForm formBean = (CommitmentbyDonorForm) form;

					 Collection report = new ArrayList();
					logger.info("Name : " + formBean.getStatus());
					if (formBean != null) {
						logger.info("formBean is not null");
						report= formBean.getAllReports();

					} else {
						logger.info("formbean is null");
					}
					if(report == null)
						logger.info("coll is null");
					else
						logger.info("formBean is not null");

					 Iterator iter = null;
					 if (report.size() == 0) {
								logger.info("collection is empty");
					 } else {
								logger.info("collection is not empty");
								iter = report.iterator();
					 }
	 				 //System.out.println("col size "+ report.size());

					 int i=0, j=0, yy=0, yyTmp=0, flag=0, colCnt=0, yyCnt=0;
					 Report r;
					 ArrayList ampFunds, ampFisYears ,ampSector, ampDonor;
					 Integer year = null;
					 if(report.size() > 0)
					 {
					 	if(iter.hasNext() == true)
					 	{
					 		r  = (Report)iter.next();
					 		ampFisYears = new ArrayList(formBean.getFiscalYearRange());
					 		colCnt = ampFisYears.size();
					 		logger.info(" YY : "+ ampFisYears.size());
					 		yyCnt = colCnt;
					 		ampFunds = new ArrayList(r.getAmpFund());
					 		colCnt = ampFunds.size();
					 		
					 		
					 	}
					 	iter = report.iterator();
					 }
					 colCnt = 7 + yyCnt + colCnt ;
					 logger.info("CNT="+colCnt);
					 
					 Object[][] data = new Object[report.size()][colCnt + 2];
					 int k=0;
					 if(report.size() > 0 )
					 {	
					 	while (iter.hasNext()) 
					 	{
										
							r =(Report)iter.next();
							ampFisYears = new ArrayList(formBean.getFiscalYearRange());
							Iterator fiscIter = ampFisYears.iterator();
							fiscalYrs fisc=null;
							if(fiscIter.hasNext() == true)
							{
								year = (Integer)fiscIter.next();
								yy = year.intValue();
							}
							yyTmp = yy;		
							data[i][0] = formBean.getWorkspaceType() + " " + formBean.getWorkspaceName() ;
							//data[i][1]= formBean.getFiltersNames();
							String filterName[] = new String[2];
							filterName = formBean.getFilter();
							int col=0;
							for(int l=0; l<filterName.length; l++)
							{	
								col = col + 1;
								logger.info("Action : " + filterName[l]);
								data[i][col] = filterName[l];
							}

							ampDonor = new ArrayList(r.getDonors());
								data[i][3] = ampDonor.toString().replace('[',' ').replace(']',' ');	
							data[i][4]=r.getTitle();
							ampSector  = new ArrayList(r.getSectors());
								data[i][5] = ampSector.toString().replace('[',' ').replace(']',' ');
							data[i][6]=r.getStatus();
							data[i][7]=r.getStartDate();	
							data[i][8]=r.getCloseDate();	
							ampFunds = new ArrayList(r.getAmpFund());
							Iterator iter1 = ampFunds.iterator();
							k = 8;
							while ( iter1.hasNext() )	
							{
							  	k = k + 1;
							   	AmpFund ampFund = (AmpFund)iter1.next();
								data[i][k] = ampFund.getCommAmount() + " / " + ampFund.getDisbAmount();
							} 	
							for(int l=0; l<yyCnt; l++)
							{
								k = k + 1;
								data[i][k]= Integer.toString(yy);
								yy++;
							}
							i++;
							yy=yyTmp;
							flag = 1;
						} // end of obj iteration
					 }
					 else
					 {
					 	flag = 0;					
					 }
					if(flag == 1)
					{	
					
						QuarterlyDateRangeDatasource dataSource = new QuarterlyDateRangeDatasource(data);
						ActionServlet s = getServlet();
						String jarFile = s.getServletContext().getRealPath(
											  "/WEB-INF/lib/jasperreports-0.6.1.jar");
						System.setProperty("jasper.reports.compile.class.path",jarFile);
						String realPathJrxml = s.getServletContext().getRealPath(
											 "/WEB-INF/classes/org/digijava/module/aim/reports");
						logger.info("Path : " + realPathJrxml);
						realPathJrxml = realPathJrxml + "\\" + "QuarterlyDateRangePdf.jrxml";
						logger.info("Path : " + realPathJrxml);
						QdrJrxml jrxml =  new QdrJrxml();
						
						logger.info("YYCNT = " + yyCnt);
						jrxml.createJrxml(yyCnt, realPathJrxml);
						
						JasperCompileManager.compileReportToFile(realPathJrxml);
						byte[] bytes = null;
						try
						{
							String jasperFile = s.getServletContext().getRealPath(
									 "/WEB-INF/classes/org/digijava/module/aim/reports/QuarterlyDateRangePdf.jasper");
							Map parameters = new HashMap();
							bytes = JasperRunManager.runReportToPdf( jasperFile,  parameters, dataSource);
					 	}
					 	catch (JRException e)
					 	{	
							logger.info("Exception from QuarterlyDateRangePDF = " + e);
					 	}
						if (bytes != null && bytes.length > 0)
						{
							response.setContentType("application/pdf");
							response.setHeader("Content-Disposition","inline; filename=QuarterlyDateRange.pdf");
							response.setContentLength(bytes.length);
							ServletOutputStream ouputStream = response.getOutputStream();
							ouputStream.write(bytes, 0, bytes.length);
							ouputStream.flush();
							ouputStream.close();
						}
						else
					 	{
							logger.info("Nothing to display");
					 	}
					}
					 	return null;
		}
}
