/*
 * ChartGenerator.java
 * Created : 18-APr-2006
 */

package org.digijava.module.aim.helper;

import java.awt.Color;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.WorkerException;
import org.digijava.kernel.translator.TranslatorWorker;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.aim.dbentity.AmpActivity;
import org.digijava.module.aim.dbentity.AmpIndicatorValue;
import org.digijava.module.aim.dbentity.IndicatorActivity;
import org.digijava.module.aim.util.ActivityUtil;
import org.digijava.module.aim.util.IndicatorUtil;
import org.digijava.module.aim.util.MEIndicatorsUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
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
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.servlet.ServletUtilities;
import org.jfree.chart.urls.StandardCategoryURLGenerator;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

public class ChartGenerator {

	private static Logger logger = Logger.getLogger(ChartGenerator.class);

	public static final String KEY_RISK_PREFIX="aim:risk:";
	public static final String KEY_PERFORMANCE_PREFIX="aim:performance:";

	public static String getPortfolioRiskChartFileName(HttpSession session,PrintWriter pw,
			int chartWidth,int chartHeight,String url, HttpServletRequest request) throws WorkerException {
        Long siteId=RequestUtils.getSiteDomain(request).getSite().getId();
        String langCode= RequestUtils.getNavigationLanguage(request).getCode();
		Collection activityIds = new ArrayList();
		if (session.getAttribute(Constants.AMP_PROJECTS) != null) {
			ArrayList projects = (ArrayList) session.getAttribute(Constants.AMP_PROJECTS);
			for (int i = 0;i < projects.size();i ++) {
				AmpProject proj = (AmpProject) projects.get(i);
				activityIds.add(proj.getAmpActivityId());
			}
		}

		ArrayList col = (ArrayList) MEIndicatorsUtil.getPortfolioMEIndicatorRisks(activityIds);


        for (Iterator valIter = col.iterator(); valIter.hasNext(); ) {
            MEIndicatorRisk item = (MEIndicatorRisk) valIter.next();
            String value = item.getRisk();
            String key =  value.toLowerCase();
            key = key.replaceAll(" ", "");
            String msg = TranslatorWorker.translateText(key, langCode, siteId);
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


		return generateRiskChart(cp,request);
	}

	public static String getActivityRiskChartFileName(Long actId,HttpSession session,PrintWriter pw,int chartWidth,int chartHeight,String url, HttpServletRequest request) throws Exception{
		Collection<AmpCategoryValue> risks = IndicatorUtil.getRisks(actId);

		/*ArrayList meRisks = (ArrayList) MEIndicatorsUtil.getMEIndicatorRisks(actId);
        for (Iterator<AmpIndicatorRiskRatings> riskIter = risks.iterator(); riskIter.hasNext(); ) {
        	AmpIndicatorRiskRatings item = (AmpIndicatorRiskRatings) riskIter.next();
            String value = item.getRatingName();
            String key = value.toLowerCase();
            key = key.replaceAll(" ", "");
            String msg = TranslatorWorker.translateText(key, langCode, siteId);
            item.setTranslatedRatingName(msg);
        }*/

        //Collections.sort((List)risks);

		ChartParams cp = new ChartParams();
		cp.setChartHeight(chartHeight);
		cp.setChartWidth(chartWidth);
		cp.setData(risks);
		cp.setTitle("");
		cp.setSession(session);
		cp.setWriter(pw);
		cp.setUrl(url);
		return generateRiskChart(cp,request);
	}

    /*
     *
     * TODO remove this method we have same method in IndicatorUtil getRisks()...
     */
    @Deprecated
	public static ArrayList<AmpCategoryValue> getActivityRisks(Long actId)	throws DgException {
		ArrayList<AmpCategoryValue> risks=new ArrayList<AmpCategoryValue>();
		List<IndicatorActivity> valuesActivity=IndicatorUtil.getIndicatorActivities(actId);
		if(valuesActivity!=null && valuesActivity.size()>0){
			Iterator<IndicatorActivity> it=valuesActivity.iterator();
			while(it.hasNext()){
				 IndicatorActivity indActivity=it.next();
				 Set<AmpIndicatorValue> values=indActivity.getValues();
				 for(Iterator<AmpIndicatorValue> valuesIter=values.iterator();valuesIter.hasNext();){
					 AmpIndicatorValue val=valuesIter.next();
					 if(val.getRiskValue()!=null){
						 risks.add(val.getRiskValue());
						 break;//TODO INDIC because this is stupid! all values have same risk and this risk should go to connection.
					 }
				}
			}
		}
		return risks;
	}

	public static String getPortfolioPerformanceChartFileName(Long actId,Long indId,
			Integer page,HttpSession session,PrintWriter pw,
			int chartWidth,int chartHeight,String url,boolean includeBaseline, HttpServletRequest request) throws WorkerException {

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
        Long siteId=RequestUtils.getSiteDomain(request).getSite().getId();
        String langCode= RequestUtils.getNavigationLanguage(request).getCode();

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
            String key = value.toLowerCase();
            key = key.replaceAll(" ", "");
            String msg = TranslatorWorker.translateText(key, langCode, siteId);
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

		return generatePerformanceChart(cp,request);
	}

	public static String getActivityPerformanceChartFileName(Long actId,HttpSession session,PrintWriter pw,
			int chartWidth,int chartHeight,String url,boolean includeBaseline, HttpServletRequest request) throws Exception{

		
		List<IndicatorActivity> values=IndicatorUtil.getIndicatorActivities(actId);;

//		Set<IndicatorActivity> valuesActivity=ActivityUtil.loadActivity(actId).getIndicators();
//			if(valuesActivity!=null && valuesActivity.size()>0){
//				Iterator<IndicatorActivity> it=valuesActivity.iterator();
//				while(it.hasNext()){
//					 IndicatorActivity indActivity=it.next();
//					 values=indActivity.getValues();
//					 for(Iterator valuesIter=values.iterator();valuesIter.hasNext();){
//						AmpIndicatorValue value=(AmpIndicatorValue)valuesIter.next();
//						String val=new Integer(value.getValueType()).toString();
//						String key = KEY_PERFORMANCE_PREFIX+ val.toLowerCase();
//						key = key.replaceAll(" ", "");
//						String msg = CategoryManagerUtil.translate(key, request, val);
//						//item.setType(msg);
//
//					}
//				}
//			}



//		Collection meIndValues = MEIndicatorsUtil.getMEIndicatorValues(actId,includeBaseline);
//        for (Iterator valIter = meIndValues.iterator(); valIter.hasNext(); ) {
//            MEIndicatorValue item = (MEIndicatorValue) valIter.next();
//            String value = item.getType();
//            String key = KEY_PERFORMANCE_PREFIX+ value.toLowerCase();
//            key = key.replaceAll(" ", "");
//            String msg = CategoryManagerUtil.translate(key, request, value);
//            item.setType(msg);
//        }

		ChartParams cp = new ChartParams();
		cp.setChartHeight(chartHeight);
		cp.setChartWidth(chartWidth);
		cp.setData(values);
		cp.setTitle("");
		cp.setSession(session);
		cp.setWriter(pw);
		cp.setUrl(url);

		return generatePerformanceChart(cp,request);
	}

	public static String generateRiskChart(ChartParams cp,HttpServletRequest request) {
		String fileName = null;
        Long siteId=RequestUtils.getSiteDomain(request).getSite().getId();
        String langCode= RequestUtils.getNavigationLanguage(request).getCode();
		Collection<AmpCategoryValue> col = cp.getData();
		try {
			JFreeChart chart = generateRiskChart(cp,siteId,langCode);
			if(chart!=null){
				String url = cp.getUrl(); 
				if (url != null && url.trim().length() > 0) {
					((PiePlot)chart.getPlot()).setURLGenerator(new PieChartURLGenerator(url,"risk",langCode,siteId));
				}
				ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());

				fileName = ServletUtilities.saveChartAsPNG(chart,cp.getChartWidth(),cp.getChartHeight(),info,cp.getSession());
				ChartUtilities.writeImageMap(cp.getWriter(),fileName,info,false);

				cp.getWriter().flush();
			}
		} catch (Exception e) {
			logger.error("Exception from generateRisk() :" + e.getMessage());
			e.printStackTrace(System.out);
		}
		return fileName;
	}
	
	public static JFreeChart generateRiskChart(ChartParams cp,Long siteId,String langCode) {
		JFreeChart chart =null;
		Collection<AmpCategoryValue> col = cp.getData();
		try {
			if (col != null && col.size() > 0) {
				Iterator<AmpCategoryValue> itr = col.iterator();

				DefaultPieDataset ds = new DefaultPieDataset();
				//how many risks are for each risk name
				Map<String,Integer> riskCount=new HashMap<String, Integer> ();
				//what is value of each risk name
				Map<String,Integer> riskValues=new HashMap<String, Integer> ();

				for (AmpCategoryValue risk : col) {
					Integer count=riskCount.get(risk.getValue());
					if (count==null){
						//this is first one o the type
						count=new Integer(1);
					}else{
						//this is not first one so increment
						count=new Integer(count+1);
					}
					riskCount.put(risk.getValue(), count);
					riskValues.put(risk.getValue(), risk.getIndex());
				}

				Color seriesColors[] = new Color[col.size()];
				int index = 0;
				Set<String> types=riskCount.keySet();
				if (types!=null){
					//for each name
					for (String type : types) {
						String riskName=type;
						Integer count=riskCount.get(riskName);
						Integer value=riskValues.get(riskName);

						//for each name/value (low, high) set different color
						switch (value.intValue()) {
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

						//put in datasource
                        String msg = TranslatorWorker.translateText(riskName, langCode, siteId);
						ds.setValue(msg,count);

					}
				}

				chart = ChartFactory.createPieChart(
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
				
			}
		} catch (Exception e) {
			logger.error("Exception from generateRisk() :" + e.getMessage());
			e.printStackTrace(System.out);
		}
		return chart;
	}

	public static String generatePerformanceChart(ChartParams cp,HttpServletRequest request) {
		String fileName=null;
        Long siteId=RequestUtils.getSiteDomain(request).getSite().getId();
        String langCode= RequestUtils.getNavigationLanguage(request).getCode();
		
		try {			
			JFreeChart chart = generatePerformanceChart(cp,siteId,langCode);
			CategoryPlot plot = (CategoryPlot) chart.getPlot();
			CategoryItemRenderer r1 = plot.getRenderer();   //new StackedBarRenderer();
			//r1.setSeriesPaint(0,Constants.BASE_VAL_CLR);
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
				StandardCategoryURLGenerator cUrl1 = new StandardCategoryURLGenerator(url,"series","ind");
//				CategoryURLGenerator cUrl1 = new CategoryUrlGen(url);
				r1.setItemURLGenerator(cUrl1);
			}

			plot.setRenderer(r1);
			CategoryAxis axis = plot.getDomainAxis();
			axis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);
			NumberAxis numAxis = (NumberAxis) plot.getRangeAxis();
			numAxis.setRange(0D,100D);
		//	numAxis.setNumberFormatOverride(new DecimalFormat("%"));

			ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
			fileName = ServletUtilities.saveChartAsPNG(chart,cp.getChartWidth(),
					cp.getChartHeight(),info,cp.getSession());
			ChartUtilities.writeImageMap(cp.getWriter(),fileName,info,false);
			cp.getWriter().flush();

		} catch (Exception e) {
			logger.error("Exception from generatePerformanceChart() :" + e.getMessage());
			e.printStackTrace();
		}
 		return fileName;
	}
	
	public static JFreeChart generatePerformanceChart(ChartParams cp,Long siteId,String langCode) {
			JFreeChart chart =null;
			Double baseValue=null,actualValue=null,targetValue=null;
			String baseValueType=null,targetValueType=null,actualValueType=null;
			DefaultCategoryDataset ds = new DefaultCategoryDataset();
			try {
				Collection data=cp.getData();
				for (Iterator iterator = data.iterator(); iterator.hasNext();) {
					IndicatorActivity connection = (IndicatorActivity) iterator.next();

					Collection<AmpIndicatorValue> col = connection.getValues();
					if (col != null && col.size() > 0) {
						String indicatorName=connection.getIndicator().getName();

						Iterator<AmpIndicatorValue> itr = col.iterator();
						boolean revisedAlreadyParsed=false;
						while (itr.hasNext()) {
							AmpIndicatorValue ampIndValue = itr.next();
							if (ampIndValue.getValueType() == AmpIndicatorValue.BASE) {
								baseValue = ampIndValue.getValue();
								baseValueType =  "base".toLowerCase();
								baseValueType = baseValueType.replaceAll(" ", "");
								String msg = TranslatorWorker.translateText(baseValueType, langCode, siteId);
								baseValueType=msg;
							} else if (ampIndValue.getValueType() == AmpIndicatorValue.ACTUAL) {
								actualValue = ampIndValue.getValue();
								actualValueType = "actual".toLowerCase();
								actualValueType = actualValueType.replaceAll(" ", "");
								  String msg = TranslatorWorker.translateText(actualValueType, langCode, siteId);
								actualValueType=msg;
							} else if (ampIndValue.getValueType() == AmpIndicatorValue.TARGET && !revisedAlreadyParsed) {
								targetValue = ampIndValue.getValue();
								targetValueType = "target".toLowerCase();
								targetValueType = targetValueType.replaceAll(" ", "");
								  String msg = TranslatorWorker.translateText(targetValueType, langCode, siteId);
								targetValueType=msg;

							} else if (ampIndValue.getValueType() == AmpIndicatorValue.REVISED) {
								targetValue = ampIndValue.getValue();
								targetValueType =  "target".toLowerCase();
								targetValueType = targetValueType.replaceAll(" ", "");
								 String msg = TranslatorWorker.translateText(targetValueType, langCode, siteId);
								targetValueType=msg;
								revisedAlreadyParsed=true;//this is used to not overwrite revised with target.
							}
						}
						if (baseValue<=actualValue&& actualValue<=targetValue){
							actualValue=actualValue-baseValue;
							targetValue=targetValue-baseValue;
							actualValue=(100f*actualValue)/targetValue;
							targetValue=100-actualValue;
						} else {
							double result = 0;
							baseValue -= targetValue;
				            actualValue -= targetValue;
				            targetValue = baseValue;
				            if (baseValue != 0 && actualValue != 0) {
				                result = actualValue / (baseValue / 100);
				                actualValue=100-result;
				                targetValue=100 - actualValue;
				            }
							//ds.addValue(baseValue,baseValueType,indicatorName);
						}
						ds.addValue(actualValue,actualValueType,indicatorName);
						ds.addValue(targetValue,targetValueType,indicatorName);

					}
				}


				 chart = ChartFactory.createStackedBarChart(
						cp.getTitle(), // title
						null,	// X-axis label
						null,	// Y-axis label
						ds,		// dataset
						PlotOrientation.VERTICAL,	// Orientation
						true,	// show legend
						false,	// show tooltips
						false);	// show urls
				chart.setBackgroundPaint(Color.WHITE);				

			} catch (Exception e) {
				logger.error("Exception from generatePerformanceChart() :" + e.getMessage());
				e.printStackTrace();
			}
	 		return chart;
	}
}
