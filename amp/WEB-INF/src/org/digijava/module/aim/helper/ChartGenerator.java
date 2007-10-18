/*
 * ChartGenerator.java
 * Created : 18-APr-2006
 */

package org.digijava.module.aim.helper;

import java.awt.Color;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.digijava.kernel.entity.Message;
import org.digijava.kernel.persistence.WorkerException;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.util.MEIndicatorsUtil;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.entity.StandardEntityCollection;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.StackedBarRenderer;
import org.jfree.chart.servlet.ServletUtilities;
import org.jfree.chart.urls.CategoryURLGenerator;
import org.jfree.chart.urls.StandardCategoryURLGenerator;
import org.jfree.chart.urls.StandardPieURLGenerator;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import java.util.*;

public class ChartGenerator {

	private static Logger logger = Logger.getLogger(ChartGenerator.class);

	public static String getPortfolioRiskChartFileName(HttpSession session,PrintWriter pw,
			int chartWidth,int chartHeight,String url, HttpServletRequest request) throws WorkerException {

		Collection activityIds = new ArrayList();
		if (session.getAttribute(Constants.AMP_PROJECTS) != null) {
			ArrayList projects = (ArrayList) session.getAttribute(Constants.AMP_PROJECTS);
			for (int i = 0;i < projects.size();i ++) {
				AmpProject proj = (AmpProject) projects.get(i);
				activityIds.add(proj.getAmpActivityId());
			}
		}

		ArrayList col = (ArrayList) MEIndicatorsUtil.getPortfolioMEIndicatorRisks(activityIds);

		String lang = RequestUtils.getNavigationLanguage(request).getCode();
		Long siteId = RequestUtils.getSite(request).getId();
		String siteName = RequestUtils.getSite(request).getName();
        for (Iterator valIter = col.iterator(); valIter.hasNext(); ) {
            MEIndicatorRisk item = (MEIndicatorRisk) valIter.next();
            String value = item.getRisk();
            String key = "aim:portfolioRiskChart:" + value.toLowerCase();
            key = key.replaceAll(" ", "");
            String msg = CategoryManagerUtil.translate(key, request, value);
            item.setRisk(msg);
        }

		Collections.sort(col);
		ChartParams cp = new ChartParams();
		cp.setChartHeight(chartHeight);
		cp.setChartWidth(chartWidth);
		cp.setData(col);
		cp.setTitle("");
		cp.setSession(session);
		cp.setWriter(pw);
		cp.setUrl(url);


		return generateRiskChart(cp);
	}

	public static String getActivityRiskChartFileName(Long actId,
			HttpSession session,PrintWriter pw,
			int chartWidth,int chartHeight,String url, HttpServletRequest request) {

		ArrayList meRisks = (ArrayList) MEIndicatorsUtil.getMEIndicatorRisks(actId);
        for (Iterator valIter = meRisks.iterator(); valIter.hasNext(); ) {
            MEIndicatorRisk item = (MEIndicatorRisk) valIter.next();
            String value = item.getRisk();
            String key = "aim:activityRiskChart:" + value.toLowerCase();
            key = key.replaceAll(" ", "");
            String msg = CategoryManagerUtil.translate(key, request, value);
            item.setRisk(msg);
        }

        Collections.sort(meRisks);


		ChartParams cp = new ChartParams();
		cp.setChartHeight(chartHeight);
		cp.setChartWidth(chartWidth);
		cp.setData(meRisks);
		cp.setTitle("");
		cp.setSession(session);
		cp.setWriter(pw);
		cp.setUrl(url);
		return generateRiskChart(cp);
	}

	public static String getPortfolioPerformanceChartFileName(Long actId,Long indId,
			Integer page,HttpSession session,PrintWriter pw,
			int chartWidth,int chartHeight,String url,boolean includeBaseline, HttpServletRequest request) {

		Collection activityIds = new ArrayList();
		if (actId.longValue() < 0) {
			if (session.getAttribute(Constants.AMP_PROJECTS) != null) {
				ArrayList projects = (ArrayList) session.getAttribute(Constants.AMP_PROJECTS);
				for (int i = 0;i < projects.size();i ++) {
					AmpProject proj = (AmpProject) projects.get(i);
					activityIds.add(proj.getAmpActivityId());
				}
			}
		} else {
			activityIds.add(actId);
		}

		Collection col = new ArrayList();
		ArrayList temp = (ArrayList) MEIndicatorsUtil.getPortfolioMEIndicatorValues(
				activityIds,indId,includeBaseline, request);

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
        for (Iterator valIter = col.iterator(); valIter.hasNext(); ) {
            MEIndicatorValue item = (MEIndicatorValue) valIter.next();
            String value = item.getType();
            String key = "aim:portfolioPerformanceChart:" + value.toLowerCase();
            key = key.replaceAll(" ", "");
            String msg = CategoryManagerUtil.translate(key, request, value);
            item.setType(msg);
        }

		ChartParams cp = new ChartParams();
		cp.setChartHeight(chartHeight);
		cp.setChartWidth(chartWidth);
		cp.setData(col);
		cp.setTitle("");
		cp.setSession(session);
		cp.setWriter(pw);
		cp.setUrl(url);

		return generatePerformanceChart(cp);
	}

	public static String getActivityPerformanceChartFileName(Long actId,
			HttpSession session,PrintWriter pw,
			int chartWidth,int chartHeight,String url,boolean includeBaseline, HttpServletRequest request) {

		Collection meIndValues = MEIndicatorsUtil.getMEIndicatorValues(actId,includeBaseline);
        for (Iterator valIter = meIndValues.iterator(); valIter.hasNext(); ) {
            MEIndicatorValue item = (MEIndicatorValue) valIter.next();
            String value = item.getType();
            String key = "aim:activityPerformanceChart:" + value.toLowerCase();
            key = key.replaceAll(" ", "");
            String msg = CategoryManagerUtil.translate(key, request, value);
            item.setType(msg);
        }


		ChartParams cp = new ChartParams();
		cp.setChartHeight(chartHeight);
		cp.setChartWidth(chartWidth);
		cp.setData(meIndValues);
		cp.setTitle("");
		cp.setSession(session);
		cp.setWriter(pw);
		cp.setUrl(url);

		return generatePerformanceChart(cp);
	}

	public static String generateRiskChart(ChartParams cp) {
		String fileName = null;
		Collection col = cp.getData();
		try {
			if (col != null && col.size() > 0) {
				Iterator itr = col.iterator();

				DefaultPieDataset ds = new DefaultPieDataset();

				Color seriesColors[] = new Color[col.size()];
				int index = 0;
				while (itr.hasNext()) {
					MEIndicatorRisk risk = (MEIndicatorRisk) itr.next();

					switch (risk.getRiskRating()) {
					case Constants.HIGHLY_SATISFACTORY:
						seriesColors[index++] = Constants.HIGHLY_SATISFACTORY_CLR;
						break;
					case Constants.VERY_SATISFACTORY:
						seriesColors[index++] = Constants.VERY_SATISFACTORY_CLR;
						break;
					case Constants.SATISFACTORY:
						seriesColors[index++] = Constants.SATISFACTORY_CLR;
						break;
					case Constants.UNSATISFACTORY:
						seriesColors[index++] = Constants.UNSATISFACTORY_CLR;
						break;
					case Constants.VERY_UNSATISFACTORY:
						seriesColors[index++] = Constants.VERY_UNSATISFACTORY_CLR;
						break;
					case Constants.HIGHLY_UNSATISFACTORY:
						seriesColors[index++] = Constants.HIGHLY_UNSATISFACTORY_CLR;
					}

					ds.setValue(risk.getRisk(),risk.getRiskCount());
				}

				JFreeChart chart = ChartFactory.createPieChart(
						cp.getTitle(), // title
						ds,		// dataset
						true,	// show legend
						false,	// show tooltips
						false);	// show urls
				chart.setBackgroundPaint(Color.WHITE);

				PiePlot plot = (PiePlot) chart.getPlot();
				for (int i = 0;i < index;i ++) {
					plot.setSectionPaint(i,seriesColors[i]);
				}

				String url = cp.getUrl();
				if (url != null && url.trim().length() > 0) {
					plot.setURLGenerator(new StandardPieURLGenerator(url,"risk"));
				}
				ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());

				fileName = ServletUtilities.saveChartAsPNG(chart,cp.getChartWidth(),
						cp.getChartHeight(),info,cp.getSession());
				ChartUtilities.writeImageMap(cp.getWriter(),fileName,info,false);

				cp.getWriter().flush();
			}
		} catch (Exception e) {
			logger.error("Exception from generateRisk() :" + e.getMessage());
			e.printStackTrace(System.out);
		}
		return fileName;
	}

	public static String generatePerformanceChart(ChartParams cp) {

		String fileName = null;
		Collection col = cp.getData();
		try {
			if (col != null && col.size() > 0) {
				Iterator itr = col.iterator();
				DefaultCategoryDataset ds = new DefaultCategoryDataset();
				while (itr.hasNext()) {
					MEIndicatorValue meIndVal = (MEIndicatorValue) itr.next();
					ds.addValue(meIndVal.getValue(),meIndVal.getType(),meIndVal.getIndicatorName());
				}

				JFreeChart chart = ChartFactory.createStackedBarChart(
						cp.getTitle(), // title
						null,	// X-axis label
						null,	// Y-axis label
						ds,		// dataset
						PlotOrientation.VERTICAL,	// Orientation
						true,	// show legend
						false,	// show tooltips
						false);	// show urls
				chart.setBackgroundPaint(Color.WHITE);

				CategoryPlot plot = (CategoryPlot) chart.getPlot();

				StackedBarRenderer r1 = new StackedBarRenderer();
				r1.setSeriesPaint(0,Constants.ACTUAL_VAL_CLR);
				r1.setSeriesPaint(1,Constants.TARGET_VAL_CLR);

				/*
				if (includeBaseLine) {
					r1.setSeriesPaint(0,Constants.BASE_VAL_CLR);
					r1.setSeriesPaint(1,Constants.ACTUAL_VAL_CLR);
					r1.setSeriesPaint(2,Constants.TARGET_VAL_CLR);
				} else {
					r1.setSeriesPaint(0,Constants.ACTUAL_VAL_CLR);
					r1.setSeriesPaint(1,Constants.TARGET_VAL_CLR);
				}*/

				String url = cp.getUrl();
				if (url != null && url.trim().length() > 0) {
					CategoryURLGenerator cUrl1 = new StandardCategoryURLGenerator(url,"series","ind");
					r1.setItemURLGenerator(cUrl1);
				}

				plot.setRenderer(r1);
				CategoryAxis axis = plot.getDomainAxis();
				axis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);
				NumberAxis numAxis = (NumberAxis) plot.getRangeAxis();
				numAxis.setRange(0D,1D);
				numAxis.setNumberFormatOverride(new DecimalFormat("###%"));

				ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
				fileName = ServletUtilities.saveChartAsPNG(chart,cp.getChartWidth(),
						cp.getChartHeight(),info,cp.getSession());
				ChartUtilities.writeImageMap(cp.getWriter(),fileName,info,false);
				cp.getWriter().flush();
			}

		} catch (Exception e) {
			logger.error("Exception from generatePerformanceChart() :" + e.getMessage());
			e.printStackTrace(System.out);
		}
 		return fileName;
	}
}
