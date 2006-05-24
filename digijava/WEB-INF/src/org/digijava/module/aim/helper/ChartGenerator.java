/*
 * ChartGenerator.java
 * Created : 18-APr-2006
 */

package org.digijava.module.aim.helper;

import java.awt.Color;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.digijava.module.aim.util.MEIndicatorsUtil;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.entity.StandardEntityCollection;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.servlet.ServletUtilities;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

public class ChartGenerator {
	
	private static Logger logger = Logger.getLogger(ChartGenerator.class);
	
	public static String getPortfolioRiskChartFileName(HttpSession session,PrintWriter pw,
			int chartWidth,int chartHeight) {
		
		Collection activityIds = new ArrayList();
		if (session.getAttribute("ampProjects") != null) {
			ArrayList projects = (ArrayList) session.getAttribute("ampProjects");
			for (int i = 0;i < projects.size();i ++) {
				AmpProject proj = (AmpProject) projects.get(i);
				activityIds.add(proj.getAmpActivityId());
			}
		}
		
		Collection col = MEIndicatorsUtil.getPortfolioMEIndicatorRisks(activityIds);
		return generateRiskChart(col,Constants.PORTFOLIO_RISK_CHART_TITLE,
				chartWidth,chartHeight,session,pw);
	}
	
	public static String getActivityRiskChartFileName(Long actId,
			HttpSession session,PrintWriter pw,
			int chartWidth,int chartHeight) {
		
		Collection meRisks = MEIndicatorsUtil.getMEIndicatorRisks(actId);
		return generateRiskChart(meRisks,Constants.ACTIVITY_RISK_CHART_TITLE,chartWidth,
				chartHeight,session,pw);
	}
	
	public static String getPortfolioPerformanceChartFileName(Long actId,Long indId,
			Integer page,HttpSession session,PrintWriter pw,
			int chartWidth,int chartHeight) {
	
		Collection activityIds = new ArrayList();
		if (actId.longValue() < 0) {
			if (session.getAttribute("ampProjects") != null) {
				ArrayList projects = (ArrayList) session.getAttribute("ampProjects");
				for (int i = 0;i < projects.size();i ++) {
					AmpProject proj = (AmpProject) projects.get(i);
					activityIds.add(proj.getAmpActivityId());
				}
			}
		} else {
			activityIds.add(actId);
		}
		
		Collection col = new ArrayList();
		ArrayList temp = (ArrayList) MEIndicatorsUtil.getPortfolioMEIndicatorValues(activityIds,indId);
		
		if ((actId.longValue() > 0 && indId.longValue() <= 0) ||
				(actId.longValue() <= 0 && indId.longValue() > 0)) {
			int st = (page.intValue() - 1) * 30; 
			int ed = st + 30;
			ed = (ed > temp.size()) ? temp.size() : ed;
			for (int i = st; i < ed;i ++) {
				col.add(temp.get(i));
			}
		} else {
			col = temp;
		}
		return generatePerformanceChart(col,Constants.PORTFOLIO_PERFORMANCE_CHART_TITLE,
				chartWidth,chartHeight,session,pw);
	}		
	
	public static String getActivityPerformanceChartFileName(Long actId,
			HttpSession session,PrintWriter pw,
			int chartWidth,int chartHeight) {
		
		Collection meIndValues = MEIndicatorsUtil.getMEIndicatorValues(actId);
		return generatePerformanceChart(meIndValues,Constants.ACTIVITY_PERFORMANCE_CHART_TITLE,
				chartWidth,chartHeight,session,pw);
	}
	
	public static String generateRiskChart(Collection col,String title,
			int chartWidth,int chartHeight,HttpSession session,PrintWriter pw) {
		String fileName = null;
		try {
			if (col != null && col.size() > 0) {
				Iterator itr = col.iterator();
				
				DefaultPieDataset ds = new DefaultPieDataset();
				while (itr.hasNext()) {
					MEIndicatorRisk risk = (MEIndicatorRisk) itr.next();
					ds.setValue(risk.getRisk(),risk.getRiskCount());
				}
				
				JFreeChart chart = ChartFactory.createPieChart(
						title, // title
						ds,		// dataset
						true,	// show legend
						false,	// show tooltips
						false);	// show urls
				chart.setBackgroundPaint(Color.WHITE);
				ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
				fileName = ServletUtilities.saveChartAsPNG(chart,chartWidth,
						chartHeight,info,session);
				ChartUtilities.writeImageMap(pw,fileName,info,false);
				pw.flush();
			}
		} catch (Exception e) {
			logger.error("Exception from generateRisk() :" + e.getMessage());
			e.printStackTrace(System.out);
		}		
		return fileName;		
	}
	
	public static String generatePerformanceChart(Collection col,String title,
			int chartWidth,int chartHeight,HttpSession session,PrintWriter pw) {
		
		String fileName = null;
		try {
			if (col != null && col.size() > 0) {
				Iterator itr = col.iterator();
				DefaultCategoryDataset ds = new DefaultCategoryDataset();
				while (itr.hasNext()) {
					MEIndicatorValue meIndVal = (MEIndicatorValue) itr.next();
					ds.addValue(meIndVal.getValue(),meIndVal.getType(),meIndVal.getIndicatorName());
				}
				
				JFreeChart chart = ChartFactory.createStackedBarChart(
						title, // title
						null,	// X-axis label
						null,	// Y-axis label
						ds,		// dataset
						PlotOrientation.VERTICAL,	// Orientation
						true,	// show legend
						false,	// show tooltips
						false);	// show urls
				chart.setBackgroundPaint(Color.WHITE);
				ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
				fileName = ServletUtilities.saveChartAsPNG(chart,chartWidth,
						chartHeight,info,session);
				ChartUtilities.writeImageMap(pw,fileName,info,false);
				pw.flush();
			}

		} catch (Exception e) {
			logger.error("Exception from generatePerformanceChart() :" + e.getMessage());
			e.printStackTrace(System.out);
		}
 		return fileName;		
	}
}