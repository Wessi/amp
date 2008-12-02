package org.digijava.module.widget.util;

import java.awt.Color;
import java.awt.Font;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.Util;
import org.dgfoundation.amp.utils.AmpCollectionUtils.KeyWorker;
import org.digijava.kernel.entity.Message;
import org.digijava.kernel.exception.DgException;
import org.digijava.kernel.persistence.PersistenceManager;
import org.digijava.kernel.persistence.WorkerException;
import org.digijava.module.aim.dbentity.AmpCurrency;
import org.digijava.module.aim.dbentity.AmpFundingDetail;
import org.digijava.module.aim.dbentity.AmpIndicatorValue;
import org.digijava.module.aim.dbentity.AmpOrganisation;
import org.digijava.module.aim.dbentity.AmpPledge;
import org.digijava.module.aim.dbentity.AmpRegion;
import org.digijava.module.aim.dbentity.AmpSector;
import org.digijava.module.aim.dbentity.IndicatorSector;
import org.digijava.module.aim.exception.AimException;
import org.digijava.module.aim.helper.CurrencyWorker;
import org.digijava.module.aim.helper.FormatHelper;
import org.digijava.module.aim.logic.FundingCalculationsHelper;
import org.digijava.module.aim.util.CurrencyUtil;
import org.digijava.module.aim.util.DecimalWraper;
import org.digijava.module.aim.util.FeaturesUtil;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;
import org.digijava.module.categorymanager.util.CategoryConstants;
import org.digijava.module.categorymanager.util.CategoryManagerUtil;
import org.digijava.module.orgProfile.helper.FilterHelper;
import org.digijava.module.translation.util.DbUtil;
import org.digijava.module.widget.dbentity.AmpDaWidgetPlace;
import org.digijava.module.widget.dbentity.AmpWidget;
import org.digijava.module.widget.dbentity.AmpWidgetIndicatorChart;
import org.digijava.module.widget.helper.ChartOption;
import org.digijava.module.widget.helper.DonorSectorFundingHelper;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.labels.StandardXYItemLabelGenerator;
import org.jfree.chart.labels.XYItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.Range;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.ui.RectangleInsets;



/**
 * Chart widgets util.
 * @author Irakli Kobiashvili
 *
 */
public class ChartWidgetUtil {

    private static Logger logger = Logger.getLogger(ChartWidgetUtil.class);
    public final static int MILLION=100000;

    /**
     * Generates chart object from specified indicator and options.
     * This chart then can be rendered as image or pdf or file.
     * @param indicatorCon
     * @param opt
     * @return
     * @throws DgException
     */
    public static JFreeChart getIndicatorChart(IndicatorSector indicatorCon,ChartOption opt) throws DgException{
		JFreeChart chart = null;
		Font font8 = new Font(null,0,8);
		TimeSeriesCollection ds = getIndicatorChartDataset(indicatorCon);
		boolean tooltips = false;
		boolean urls = false;
		//create time series line chart
		chart = ChartFactory.createTimeSeriesChart(opt.getTitle(), null, null, ds, opt.isShowLegend(), tooltips, urls);
		chart.getTitle().setFont(font8);
		if (opt.isShowLegend()){
			chart.getLegend().setItemFont(font8);		
		}
		RectangleInsets padding = new RectangleInsets(0,0,0,0);
		chart.setPadding(padding);
		
		XYPlot plot =(XYPlot) chart.getPlot();
		plot.setRangeZeroBaselineVisible(true);
		plot.getDomainAxis().setLabelFont(font8);
		plot.getDomainAxis().setTickLabelFont(font8);
		plot.getRangeAxis().setLabelFont(font8);
		plot.getRangeAxis().setTickLabelFont(font8);
		
		XYItemRenderer renderer = plot.getRenderer();
//		renderer.setItemLabelFont(font8);
		renderer.setItemLabelsVisible(opt.isShowLabels());
		XYItemLabelGenerator labelGenerator = new StandardXYItemLabelGenerator("{2}",new DecimalFormat("0.00"),new DecimalFormat("#.####"));
		renderer.setBaseItemLabelGenerator(labelGenerator);

		//For next two lines see order of TimeSeries added to TimeSeriesCollection in getIndicatorChartDataset() below
		renderer.setSeriesPaint(0, Color.blue);//0 = Actual line, because this was added first.
		renderer.setSeriesPaint(1, Color.red);//1 = Target line, because this was added second.
		if (renderer instanceof XYLineAndShapeRenderer){
			XYLineAndShapeRenderer r = (XYLineAndShapeRenderer)renderer;
			r.setBaseShapesVisible(true);
			r.setBaseShapesFilled(true);
		}
		
		//This will expand ranges slightly so that points at the edge will move inside 
		//and number will not be cut by borders. 
		Range oldRange = plot.getRangeAxis().getRange();
		Range newRange = Range.expand(oldRange, 0.1, 0.1);
		plot.getRangeAxis().setRange(newRange);

		oldRange = plot.getDomainAxis().getRange();
		newRange = Range.expand(oldRange, 0.1, 0.1);
		plot.getDomainAxis().setRange(newRange);

		return chart;
	}
    
      /**
     * Generates chart object from specified indicator and options.
     * This chart then can be rendered as image or pdf or file.
     * @param indicatorCon
     * @param opt
     * @return
     * @throws DgException
     */
    public static JFreeChart getTypeOfAidChart(ChartOption opt, FilterHelper filter) throws DgException{
		JFreeChart chart = null;
		Font font8 = new Font(null,0,12);
		CategoryDataset dataset=getTypeOfAidDataset(filter);
		chart=ChartFactory.createStackedBarChart("Type of Aid","","Amount in millions",dataset,PlotOrientation.VERTICAL, true, true,false);
		chart.getTitle().setFont(font8);
		if (opt.isShowLegend()){
			chart.getLegend().setItemFont(font8);		
		}
		
		
		return chart;
	}
    
    
      /**
     * Generates chart object from specified indicator and options.
     * This chart then can be rendered as image or pdf or file.
     * @param indicatorCon
     * @param opt
     * @return
     * @throws DgException
     */
    public static JFreeChart getPledgesCommDisbChart(ChartOption opt, FilterHelper filter) throws DgException{
		JFreeChart chart = null;
		Font font8 = new Font(null,0,12);
		CategoryDataset dataset=getPledgesCommDisbDataset(filter);
		chart=ChartFactory.createBarChart("Pledges/Comm/Disb","","Amount in millions",dataset,PlotOrientation.VERTICAL, true, true,false);
		chart.getTitle().setFont(font8);
		if (opt.isShowLegend()){
			chart.getLegend().setItemFont(font8);		
		}
                  // get a reference to the plot for further customisation...
              CategoryPlot plot = chart.getCategoryPlot();
              BarRenderer renderer = (BarRenderer) plot.getRenderer();
              renderer.setDrawBarOutline(false);
              renderer.setItemMargin(0);
           
	      return chart;
	}
    
      /**
     * Generates chart object from specified indicator and options.
     * This chart then can be rendered as image or pdf or file.
     * @param indicatorCon
     * @param opt
     * @return
     * @throws DgException
     */
    public static JFreeChart getODAProfileChart(ChartOption opt, FilterHelper filter) throws DgException{
		JFreeChart chart = null;
		Font font8 = new Font(null,0,12);
		CategoryDataset dataset=getODAProfileDataset(filter);
		chart=ChartFactory.createStackedAreaChart("ODA Profile","","Amount in millions",dataset,PlotOrientation.VERTICAL, true, true,false);
		chart.getTitle().setFont(font8);
		if (opt.isShowLegend()){
			chart.getLegend().setItemFont(font8);		
		}

         
		return chart;
	}
    
    
    private static CategoryDataset getTypeOfAidDataset(FilterHelper filter) throws DgException {
        DefaultCategoryDataset result = new DefaultCategoryDataset();
        Long year = filter.getYear();
        if (year == null || year == -1) {
            year = Long.parseLong(FeaturesUtil.getGlobalSettingValue("Current Fiscal Year"));
        }

        Long currId = filter.getCurrId();
        String currCode;
        if (currId == null) {
            currCode = "USD";
        } else {
            currCode = CurrencyUtil.getCurrency(currId).getCurrencyCode();
        }
        if (filter.getOrgId() != null) {
            for (long i = year - 4; i <= year; i++) {
                Collection<AmpCategoryValue> typeOfAids = CategoryManagerUtil.getAmpCategoryValueCollectionByKey(CategoryConstants.TYPE_OF_ASSISTENCE_KEY);
                for (AmpCategoryValue aid : typeOfAids) {
                    DecimalWraper funding = getFunding(filter.getOrgId(), i, aid.getId(), currCode,filter.getTransactionType());
                    result.addValue(funding.doubleValue()/MILLION, aid.getValue(), new Long(i));
                }

            }

        }
        
        return result;
    }
    
     private static CategoryDataset getODAProfileDataset(FilterHelper filter) throws DgException {
        DefaultCategoryDataset result = new DefaultCategoryDataset();
        Long year = filter.getYear();
        if (year == null || year == -1) {
            year = Long.parseLong(FeaturesUtil.getGlobalSettingValue("Current Fiscal Year"));
        }

        Long currId = filter.getCurrId();
        String currCode;
        if (currId == null) {
            currCode = "USD";
        } else {
            currCode = CurrencyUtil.getCurrency(currId).getCurrencyCode();
        }
        if (filter.getOrgId() != null) {
            for (long i = year - 4; i <= year; i++) {
                Collection<AmpCategoryValue> financingInstruments = CategoryManagerUtil.getAmpCategoryValueCollectionByKey(CategoryConstants.FINANCING_INSTRUMENT_KEY);
                for (AmpCategoryValue financingInstrument : financingInstruments) {
                    DecimalWraper funding = getFundingByFinancingInstrument(filter.getOrgId(), i, financingInstrument.getId(), currCode,filter.getTransactionType());
                    result.addValue(funding.doubleValue()/MILLION, financingInstrument.getValue(), new Long(i));
                    }

            }

        }
        
        return result;
    }
    
    
    private static CategoryDataset getPledgesCommDisbDataset(FilterHelper filter) throws DgException {
       DefaultCategoryDataset result = new DefaultCategoryDataset();
        Long year = filter.getYear();
        if (year == null || year == -1) {
            year = Long.parseLong(FeaturesUtil.getGlobalSettingValue("Current Fiscal Year"));
        }

        Long currId = filter.getCurrId();
        String currCode;
        if (currId == null) {
            currCode = "USD";
        } else {
            currCode = CurrencyUtil.getCurrency(currId).getCurrencyCode();
        }
        if (filter.getOrgId() != null) {
            for (long i = year - 2; i <= year; i++) {
              Double fundingPledge = getPledgesFunding(filter.getOrgId(), i, currCode);
              result.addValue(fundingPledge .doubleValue()/MILLION, "Pledges", new Long(i));
              DecimalWraper funding = getFunding(filter.getOrgId(), i, currCode,true);  
              result.addValue(funding.doubleValue()/MILLION, "Actual commitments", new Long(i));   
              funding = getFunding(filter.getOrgId(), i, currCode,false);
              result.addValue(funding.doubleValue()/MILLION, "Actual disbursements", new Long(i)); 

            }

        }
        
        return result;
    }
    
    
    /**
     * Generates chart dataset from AMP data.
     * This dataset is required to generate chart object.
     * Because this chart show changes of some values in time we use time series objects.
     * It creates two lines (TimeSeries): one line is desired change of values, which connects base value to target value, 
     * and another line is actual change of indicator value, it connects all actual value points.
     * So we go through values of the indicator and put them in the correct TimeSeries objects. 
     * Correctness of the resulting chart depends on correctness of data.  
     * @param indicator
     * @return
     * @throws DgException
     */
	public static TimeSeriesCollection getIndicatorChartDataset(IndicatorSector indicator) throws DgException{
		TimeSeriesCollection ds = new TimeSeriesCollection();
		if (indicator.getValues()!=null && indicator.getValues().size()>0){
			TimeSeries tsActual = new TimeSeries("Actual");
			TimeSeries tsTarget = new TimeSeries("Target");
			for (AmpIndicatorValue value : indicator.getValues()) {
				if (value.getValueType()==AmpIndicatorValue.ACTUAL){
					tsActual.addOrUpdate(new Day(value.getValueDate()),value.getValue());
				}
				if (value.getValueType()==AmpIndicatorValue.BASE){
					tsTarget.addOrUpdate(new Day(value.getValueDate()),value.getValue());
				}
				if (value.getValueType()==AmpIndicatorValue.TARGET){
					tsTarget.addOrUpdate(new Day(value.getValueDate()),value.getValue());
				}
			}
			ds.addSeries(tsActual);
			ds.addSeries(tsTarget);
		}
		return ds;
	}
	
	/**
	 * Returns all chart widgets.
	 * @return
	 * @throws DgException
	 */
	@SuppressWarnings("unchecked")
	public static List<AmpWidgetIndicatorChart> getAllIndicatorChartWidgets() throws DgException{
		Session session = PersistenceManager.getRequestDBSession();
		String oql="from "+AmpWidgetIndicatorChart.class.getName();
		List<AmpWidgetIndicatorChart> result = null;
		try {
			Query query = session.createQuery(oql);
			result = (List<AmpWidgetIndicatorChart>)query.list();
		} catch (Exception e) {
			logger.error(e);
			throw new DgException("Cannot load all chart widgets",e);
		}
		return result;
	}

	/**
	 * Returns chart widget that is assigned to specified place.
	 * @param place
	 * @return
	 * @throws DgException
	 */
	public static AmpWidgetIndicatorChart getIndicatorChartWidget(AmpDaWidgetPlace place) throws DgException{
		AmpWidget widget = WidgetUtil.getWidgetOnPlace(place.getId());
		if (widget==null) return null;
		return (AmpWidgetIndicatorChart)widget;
	}
	
	/**
	 * Loads indicator chart widget by its ID.
	 * @param widgetId
	 * @return
	 * @throws DgException
	 */
	public static AmpWidgetIndicatorChart getIndicatorChartWidget(Long widgetId) throws DgException{
		Session session = PersistenceManager.getRequestDBSession();
		AmpWidgetIndicatorChart result;
		try {
			result = (AmpWidgetIndicatorChart)session.load(AmpWidgetIndicatorChart.class, widgetId);
		} catch (HibernateException e) {
			logger.error(e);
			throw new DgException("Cannot load indicator chart widget",e);
		}
		return result;
	}
	
	/**
	 * Saves or creates indicator chart widget in db.
	 * @param widget
	 * @return
	 * @throws DgException
	 */
	public static AmpWidgetIndicatorChart saveOrUpdate(AmpWidgetIndicatorChart widget) throws DgException{
		Session session = PersistenceManager.getRequestDBSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			session.saveOrUpdate(widget);
			tx.commit();
		} catch (Exception e) {
			//System.out.println(e);
			if (tx !=null){
				try {
					tx.rollback();
				} catch (Exception e1) {
					//System.out.println(e1);
					throw new DgException("Cannot rallback chart widget save");
				}
			}
			throw new DgException("Cannot save chart widget");
		}
		return widget;
	}
	
	/**
	 * Removes indicator chart widget from db.
	 * @param widget
	 * @throws DgException
	 */
	public static void delete(AmpWidgetIndicatorChart widget) throws DgException{
		Session session = PersistenceManager.getRequestDBSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			session.delete(widget);
			tx.commit();
		} catch (Exception e) {
			logger.error(e);
			if (tx !=null){
				try {
					tx.rollback();
				} catch (Exception e1) {
					logger.error(e1);
					throw new DgException("Cannot rallback chart widget delete");
				}
			}
			throw new DgException("Cannot delete chart widget");
		}
	}
	
	/**
	 * Checks if there is an widget for specified indicator.
	 * This helps to prevent deletion of indicator while it is assigned to widgets. 
	 * @param sectorIndicator
	 * @return true if widgets for the indicator is found in db. othervise false is returned.
	 * @throws DgException
	 */
	@SuppressWarnings("unchecked")
	public static boolean isWidgetForIndicator(IndicatorSector sectorIndicator) throws DgException{
		boolean result=false;
		Session session = PersistenceManager.getRequestDBSession();
		String oql = "from " + AmpWidgetIndicatorChart.class.getName();
		oql += " as w where w.indicator.id = :indId";
		try {
			Query query = session.createQuery(oql);
			query.setLong("indId", sectorIndicator.getId());
			List widgets = query.list();
			return (widgets!=null && widgets.size()>0);
		} catch (HibernateException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * Generates chart of sector-donor funding.
	 * @param donors ID's of donors to use in calculation
	 * @param year
	 * @param opt chart options
	 * @return
	 * @throws DgException
	 * @throws WorkerException
	 */
	public static JFreeChart getSectorByDonorChart(Long[] donors,Integer year,ChartOption opt)throws DgException,WorkerException{
		JFreeChart result = null;
		PieDataset ds = getSectorByDonorDataset(donors,year);
		String selYear = (year==null)?"All years":year.toString();
                //String titleMsg= TranslatorWorker.translate("widget:piechart:breakdownbysector", opt.getLangCode(), opt.getSiteId());
                Message msg= DbUtil.getMessage("widget:piechart:breakdownbysector", opt.getLangCode(), opt.getSiteId());
                String titleMsg="";
                if(msg!=null){
                   titleMsg=msg.getMessage(); 
                }
		String title = (opt.isShowTitle())? titleMsg:null;
		boolean tooltips = false;
		boolean urls = false;
		result = ChartFactory.createPieChart(title, ds, opt.isShowLegend(), tooltips, urls);
		PiePlot plot = (PiePlot)result.getPlot();

		if (opt.isShowTitle()){
			Font font = new Font(null,0,12);
			result.getTitle().setFont(font);
		}
		
		if (opt.isShowLabels()){
			String pattern = "{0} = {1} ({2})";
			if (opt.getLabelPattern()!=null){
				pattern=opt.getLabelPattern();
			}
                        DecimalFormat format=FormatHelper.getDecimalFormat();
                        format.setMaximumFractionDigits(0);
			PieSectionLabelGenerator gen = new StandardPieSectionLabelGenerator(pattern,format,new DecimalFormat("0.0%"));
			plot.setLabelGenerator(gen);
		}else{
			plot.setLabelGenerator(null);
		}
		
		//plot.setSectionOutlinesVisible(false);
		plot.setIgnoreNullValues(true);
		plot.setIgnoreZeroValues(true);
		return result;
	}
        
        
    public static JFreeChart getSectorByDonorChart(ChartOption opt, FilterHelper filter) throws DgException, WorkerException {
        JFreeChart chart = null;
        Font font8 = new Font(null, 0, 12);
        DefaultPieDataset dataset = getDonorSectorDataSet(filter);
        chart = ChartFactory.createPieChart("Sector Breakdown ("+(filter.getYear()-1)+")", dataset, true, true, false);
        chart.getTitle().setFont(font8);
        if (opt.isShowLegend()) {
            chart.getLegend().setItemFont(font8);
        }
        PiePlot plot = (PiePlot) chart.getPlot();
        String pattern = "{0} = {1} ({2})";
        if (opt.getLabelPattern() != null) {
            pattern = opt.getLabelPattern();
        }
        DecimalFormat format = FormatHelper.getDecimalFormat();
        format.setMaximumFractionDigits(0);
        PieSectionLabelGenerator gen = new StandardPieSectionLabelGenerator(pattern, format, new DecimalFormat("0.0%"));
        plot.setLabelGenerator(gen);



        return chart;
    }
       
       public static JFreeChart getRegionByDonorChart(ChartOption opt,FilterHelper filter) throws DgException, WorkerException {
     	JFreeChart chart = null;
		Font font8 = new Font(null,0,12);
		DefaultPieDataset dataset=getDonorRegionalDataSet(filter);
		chart=ChartFactory.createPieChart("Regional Breakdown ("+(filter.getYear()-1)+")",dataset, true, true,false);
		chart.getTitle().setFont(font8);
		if (opt.isShowLegend()){
			chart.getLegend().setItemFont(font8);		
		}
		
		
		return chart;
    }

	/**
	 * Generates dataset for sector-donor pie chart.
	 * @param donors
	 * @param year fundings only for this year are used.
	 * @return
	 * @throws DgException
	 */
	public static PieDataset getSectorByDonorDataset(Long[] donors, Integer year) throws DgException{
		DefaultPieDataset ds = new DefaultPieDataset();
		Date fromDate = null;
		Date toDate = null;
		if (year!=null){
			fromDate=getStartOfYear(year.intValue());
			toDate = getStartOfYear(year.intValue()+1);
		}
                Double[] allFundingWrapper={new Double(0)};// to hold whole funding value
		Collection<DonorSectorFundingHelper> fundings=getDonorSectorFunding(donors, fromDate, toDate,allFundingWrapper);
		if (fundings!=null){
                         double otherFunfing=0;
			for (DonorSectorFundingHelper funding : fundings) {
                             Double percent = funding.getFounding() / allFundingWrapper[0];
                            // the sectors which percent is less then 5% should be group in "Other"
                            if (percent > 0.05) {
                                ds.setValue(funding.getSector().getName(), Math.round(funding.getFounding()));
                            } else {
                                otherFunfing += funding.getFounding();
                            }
			}
                        if(otherFunfing!=0){
                            ds.setValue("Other Sectors",Math.round(otherFunfing));
                        }		
		}
		return ds;
	}
        
	public static Date getStartOfYear(int year){
		GregorianCalendar cal = new GregorianCalendar();
		cal.set(year, 0, 1, 0, 0, 0);
		return cal.getTime();
	}
	
	/**
	 * Returns collection of DonorSectorFundingHelper beans. 
	 * Each of them represents funding of one particular sector. 
	 * @param donorIDs
	 * @param fromDate
	 * @param toDate
	 * @param wholeFunding
	 * @return
	 * @throws DgException
	 */
	public static Collection<DonorSectorFundingHelper> getDonorSectorFunding(Long donorIDs[],Date fromDate, Date toDate,Double[] wholeFunding) throws DgException {
    	Collection<DonorSectorFundingHelper> fundings=null;  
		String oql ="select f.ampDonorOrgId, actSec.sectorId, "+
                        " actSec.sectorPercentage, act.ampActivityId,  sum(fd.transactionAmountInUSD)";
		oql += " from ";
		oql += AmpFundingDetail.class.getName() +
                        " as fd inner join fd.ampFundingId f ";
                oql +=  "   inner join f.ampActivityId act "+
                        " inner join act.sectors actSec "+
                        " inner join actSec.sectorId sec "+
                        " inner join actSec.activityId act "+
                        " inner join actSec.classificationConfig config ";
		
		
		oql += " where sec.parentSectorId is null  and fd.transactionType = 0 and fd.adjustmentType = 1 ";
		if (donorIDs != null && donorIDs.length > 0) {
			oql += " and (fd.ampFundingId.ampDonorOrgId in ("+ getInStatment(donorIDs) + ") ) ";
		}
		if (fromDate != null && toDate != null) {
			oql += " and (fd.transactionDate between :fDate and  :eDate ) ";
		}
                oql +=" and config.name='Primary' and act.team is not null ";
		oql += " group by f.ampDonorOrgId, actSec.sectorId,  fd.ampCurrencyId";
		oql += " order by f.ampDonorOrgId, actSec.sectorId";

		Session session = PersistenceManager.getRequestDBSession();

		//search for grouped data
	    @SuppressWarnings("unchecked")
		List result = null;
		try {
			Query query = session.createQuery(oql);
			if (fromDate!=null && toDate!=null){
				query.setDate("fDate", fromDate);
				query.setDate("eDate", toDate);
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MMMM-dd hh:mm:ss");
				logger.debug("Filtering from "+df.format(fromDate)+" to "+df.format(toDate));
			}
			result = query.list();
		} catch (Exception e) {
			throw new DgException(
					"Cannot load sector fundings by donors from db", e);
		}
		
		//Process grouped data
		if (result != null) {
			Map<Long, DonorSectorFundingHelper> donors = new HashMap<Long, DonorSectorFundingHelper>();
			for (Object row : result) {
				Object[] rowData = (Object[]) row;
				//AmpOrganisation donor = (AmpOrganisation) rowData[0];
				AmpSector sector = (AmpSector) rowData[1];
				Float sectorPrcentage = (Float) rowData[2];
				//AmpActivity activity = (AmpActivity) rowData[3];
				//AmpCurrency currency = (AmpCurrency) rowData[4];
				Double amt = (Double) rowData[4];
                                Double amount =FeaturesUtil.applyThousandsForVisibility(amt);
				//calculate percentage
				Double calculated = (sectorPrcentage.floatValue() == 100)?amount:calculatePercentage(amount,sectorPrcentage);
				//convert to
				//Double converted = convert(calculated, currency);
				//search if we already have such sector data
				DonorSectorFundingHelper sectorFundngObj = donors.get(sector.getAmpSectorId());
				//if not create and add to map
				if (sectorFundngObj == null){
					sectorFundngObj = new DonorSectorFundingHelper(sector);
					donors.put(sector.getAmpSectorId(), sectorFundngObj);
				}
				//add amount to sector
				sectorFundngObj.addFunding(calculated.doubleValue());
                                //calculate whole funding information
                                wholeFunding[0]+=calculated.doubleValue();
			}
			fundings = donors.values(); 
		}
		return fundings;
	}
        
       
	public static DefaultPieDataset getDonorRegionalDataSet(FilterHelper filter) throws DgException {
            Long year = filter.getYear();
            if (year == null || year == -1) {
                year = Long.parseLong(FeaturesUtil.getGlobalSettingValue("Current Fiscal Year"));
            }
            year-=1; // previous fiscal year
            Long currId = filter.getCurrId();
            String currCode;
            if (currId == null) {
                currCode = "USD";
            } else {
                currCode = CurrencyUtil.getCurrency(currId).getCurrencyCode();
            }
            Long orgID=filter.getOrgId();
            int transactionType=filter.getTransactionType();
            DefaultPieDataset ds = new DefaultPieDataset();
        String oql = "select distinct reg  from ";
        oql += AmpFundingDetail.class.getName() +
                " as fd inner join fd.ampFundingId f ";
        oql += "   inner join f.ampActivityId act ";
        oql += " inner join act.locations loc inner join loc.location.ampRegion reg where " +
                " reg is not null and f.ampDonorOrgId=:orgID and fd.transactionType =:transactionType and  fd.adjustmentType = 1";
        oql += " and year(fd.transactionDate)=:year   and act.team is not null ";
        Session session = PersistenceManager.getRequestDBSession();
        @SuppressWarnings("unchecked")
        List<AmpRegion> regions = null;
        try {
            Query query = session.createQuery(oql);
            query.setLong("year", year);
            query.setLong("orgID", orgID);
            query.setLong("transactionType", transactionType);
            regions = query.list();
            Iterator<AmpRegion> regionIter = regions.iterator();
            while (regionIter.hasNext()) {
                AmpRegion region = regionIter.next();
                oql = "select new AmpFundingDetail(fd.transactionType,fd.adjustmentType,fd.transactionAmount,fd.transactionDate,fd.ampCurrencyId,loc.locationPercentage,fd.fixedExchangeRate) ";
                oql += " from ";
                oql += AmpFundingDetail.class.getName() +
                        " as fd inner join fd.ampFundingId f ";
                oql += "   inner join f.ampActivityId act inner join act.locations loc inner join " +
                        " loc.location.ampRegion reg ";


                oql += " where  fd.transactionType =:transactionType and  fd.adjustmentType = 1 and f.ampDonorOrgId=:orgID ";

                oql += " and year(fd.transactionDate)=:year and reg is not null and reg.ampRegionId=  " + region.getAmpRegionId();
                query = session.createQuery(oql);
                query.setLong("year", year);
                query.setLong("orgID", orgID);
                query.setLong("transactionType", transactionType);
                List<AmpFundingDetail> fundingDets = query.list();
                FundingCalculationsHelper cal = new FundingCalculationsHelper();
                cal.doCalculations(fundingDets, currCode);
                DecimalWraper total =null;
                if(transactionType==0){
                    total=cal.getTotActualComm();
                }
                else{
                    total=cal.getTotActualDisb();
                }
                ds.setValue(region.getName(), total.doubleValue()/MILLION);
                
            }


        } catch (Exception e) {
            logger.error(e);
            throw new DgException(
                    "Cannot load sector fundings by donors from db", e);
        }
        return ds;

    }
     
    @SuppressWarnings("empty-statement")
	public static DefaultPieDataset getDonorSectorDataSet(FilterHelper filter) throws DgException {
        Long year = filter.getYear();
        if (year == null || year == -1) {
            year = Long.parseLong(FeaturesUtil.getGlobalSettingValue("Current Fiscal Year"));
        }
        year -= 1; // previous fiscal year
        Long currId = filter.getCurrId();
        String currCode;
        if (currId == null) {
            currCode = "USD";
        } else {
            currCode = CurrencyUtil.getCurrency(currId).getCurrencyCode();
        }
        Long orgID = filter.getOrgId();
        int transactionType=filter.getTransactionType();
        DefaultPieDataset ds = new DefaultPieDataset();
        String oql = "select distinct sec  from ";
        oql += AmpFundingDetail.class.getName() +
                " as fd inner join fd.ampFundingId f ";

        oql += "   inner join f.ampActivityId act " +
                " inner join act.sectors actSec " +
                " inner join actSec.sectorId sec " +
                " inner join actSec.activityId act " +
                " inner join actSec.classificationConfig config ";

        oql += "  where  " +
                "  f.ampDonorOrgId=:orgID and fd.transactionType =:transactionType  and  fd.adjustmentType = 1 ";
        oql += " and year(fd.transactionDate)=:year   and act.team is not null and config.name='Primary' ";
        Session session = PersistenceManager.getRequestDBSession();
        @SuppressWarnings("unchecked")
        List<AmpSector> sectors = null;
        try {
            Query query = session.createQuery(oql);
            query.setLong("year", year);
            query.setLong("orgID", orgID);
            query.setLong("transactionType", transactionType);
            sectors = query.list();
            DecimalWraper totAllSeqtors= getSectorFunding(year,orgID,transactionType,null,currCode);;
            Iterator<AmpSector> sectorIter = sectors.iterator();
            double others=0;
            while (sectorIter.hasNext()) {
                AmpSector sector = sectorIter.next();
                DecimalWraper total=getSectorFunding(year,orgID,transactionType,sector.getAmpSectorId(),currCode);
                double percent=total.doubleValue()/totAllSeqtors.doubleValue();
                // the sectors which percent is less then 5% should be group in "Others"
                if (percent > 0.05) {
                    ds.setValue(sector.getName(), total.doubleValue() / MILLION);
                } else {
                    others+=total.doubleValue();
                }
                
            }
            if(others>0){
                  ds.setValue("Others", others / MILLION);
            }


        } catch (Exception e) {
            logger.error(e);
            throw new DgException(
                    "Cannot load sector fundings by donors from db", e);
        }
        return ds;

    }

    public static DecimalWraper getSectorFunding(Long year, Long orgId, int transactionType, Long sectorId,String currCode) throws DgException {
        Session session = PersistenceManager.getRequestDBSession();
        String oql = "select new AmpFundingDetail(fd.transactionType,fd.adjustmentType,fd.transactionAmount,fd.transactionDate,fd.ampCurrencyId,actSec.sectorPercentage,fd.fixedExchangeRate) ";
        oql += " from ";
        oql += AmpFundingDetail.class.getName() +
                " as fd inner join fd.ampFundingId f ";
        oql += "   inner join f.ampActivityId act " +
                " inner join act.sectors actSec " +
                " inner join actSec.sectorId sec " +
                " inner join actSec.activityId act " +
                " inner join actSec.classificationConfig config ";

        oql += "  where  " +
                "   f.ampDonorOrgId=:orgID and fd.transactionType =:transactionType and  fd.adjustmentType = 1 ";
        oql += " and year(fd.transactionDate)=:year   and act.team is not null and config.name='Primary'  ";
        if( sectorId!=null){
            oql += " and  sec.ampSectorId=:sectorId  ";
        }
        Query query = session.createQuery(oql);
        query.setLong("year", year);
        query.setLong("orgID", orgId);
        query.setLong("transactionType", transactionType);
        if( sectorId!=null){
           query.setLong("sectorId", sectorId);
        }
        List<AmpFundingDetail> fundingDets = query.list();
        FundingCalculationsHelper cal = new FundingCalculationsHelper();
        cal.doCalculations(fundingDets, currCode);
        DecimalWraper total = null;
        if (transactionType == 0) {
            total = cal.getTotActualComm();
        } else {
            total = cal.getTotActualDisb();
        }
        return total;

    }
        
        
      
	public static DecimalWraper getFundingByFinancingInstrument(Long orgID, Long year, Long financingInstrumentId, String currCode, int transactionType) throws DgException {
        DecimalWraper total =null;
        String oql = "select fd ";
        oql += " from ";
        oql += AmpFundingDetail.class.getName() +
                " as fd inner join fd.ampFundingId f ";
        oql += "   inner join f.ampActivityId act ";


        oql += " where  fd.transactionType =:transactionType and  fd.adjustmentType = 1 and f.ampDonorOrgId=:orgID ";
       
        oql += " and year(fd.transactionDate)=:year ";
  
        oql += "  and act.team is not null and f.financingInstrument=:financingInstrumentId ";


        Session session = PersistenceManager.getRequestDBSession();       
        @SuppressWarnings("unchecked")
        List<AmpFundingDetail> fundingDets = null;
        try {
            Query query = session.createQuery(oql);
            query.setLong("year", year);
            query.setLong("orgID", orgID);
            query.setLong("transactionType", transactionType);
            query.setLong("financingInstrumentId", financingInstrumentId);
            fundingDets = query.list();
            FundingCalculationsHelper cal = new FundingCalculationsHelper();
            cal.doCalculations(fundingDets, currCode);
            
            if (transactionType == 0) {
                total = cal.getTotActualComm();
            } else {
                total = cal.getTotActualDisb();
            }
           

        } catch (Exception e) {
            logger.error(e);
            throw new DgException(
                    "Cannot load sector fundings by donors from db", e);
        }


        return total;
    }
        
        
       
	public static DecimalWraper getFunding(Long orgID, Long year, Long assistanceTypeId, String currCode,int transactionType) throws DgException {
        DecimalWraper total = null;
        String oql = "select fd ";
        oql += " from ";
        oql += AmpFundingDetail.class.getName() +
                " as fd inner join fd.ampFundingId f ";
        oql += "   inner join f.ampActivityId act ";


        oql += " where  fd.transactionType =:transactionType and  fd.adjustmentType = 1 and f.ampDonorOrgId=:orgID ";;
       
        oql += " and year(fd.transactionDate)=:year ";
  
        oql += "  and act.team is not null and f.typeOfAssistance=:assistanceTypeId ";


        Session session = PersistenceManager.getRequestDBSession();       
        @SuppressWarnings("unchecked")
        List<AmpFundingDetail> fundingDets = null;
        try {
            Query query = session.createQuery(oql);
            query.setLong("year", year);
            query.setLong("orgID", orgID);
            query.setLong("assistanceTypeId", assistanceTypeId);
            query.setLong("transactionType", transactionType);
            fundingDets = query.list();
            FundingCalculationsHelper cal = new FundingCalculationsHelper();
            cal.doCalculations(fundingDets, currCode);
            if (transactionType == 0) {
                total = cal.getTotActualComm();
            } else {
                total = cal.getTotActualDisb();
            }

        } catch (Exception e) {
            logger.error(e);
            throw new DgException(
                    "Cannot load sector fundings by donors from db", e);
        }


        return total;
    }
        
        
       
    public static double getPledgesFunding(Long orgID, Long year, String currCode) throws DgException {
       double totalPlannedPldges = 0;
        String oql = "select fd ";
        oql += " from ";
        oql += AmpOrganisation.class.getName() +
                " org inner join org.fundingDetails fd ";
    
        oql += " where    fd.adjustmentType = 0 and org.ampOrgId=:orgID ";
       
        oql += " and year(fd.date)=:year ";
  
        Session session = PersistenceManager.getRequestDBSession();       
        @SuppressWarnings("unchecked")
        List<AmpPledge> fundingDets = null;
        try {
            Query query = session.createQuery(oql);
            query.setLong("year", year);
            query.setLong("orgID", orgID);
            fundingDets = query.list();
           Iterator<AmpPledge> fundDetIter=fundingDets.iterator();
           while(fundDetIter.hasNext()){
               AmpPledge pledge=fundDetIter.next();
               java.sql.Date dt = new java.sql.Date(pledge.getDate().getTime());
               double frmExRt = Util.getExchange(pledge.getCurrency().getCurrencyCode(), dt);
               double toExRt = Util.getExchange(currCode, dt);
               DecimalWraper amt = CurrencyWorker.convertWrapper(pledge.getAmount(), frmExRt, toExRt, dt);
               totalPlannedPldges+=amt.doubleValue();
           }
          
           

        } catch (Exception e) {
            logger.error(e);
            throw new DgException(
                    "Cannot load sector fundings by donors from db", e);
        }


        return totalPlannedPldges;
    }
        
          
       
    public static DecimalWraper getFunding(Long orgID, Long year, String currCode, boolean isComm) throws DgException {
        String oql = "select fd ";
        oql += " from ";
        oql += AmpFundingDetail.class.getName() +
                " as fd inner join fd.ampFundingId f ";
        oql += "   inner join f.ampActivityId act ";


        oql += " where  fd.adjustmentType = 1 and f.ampDonorOrgId=:orgID ";
       
        oql += " and year(fd.transactionDate)=:year ";
  
        oql += "  and act.team is not null";


        Session session = PersistenceManager.getRequestDBSession();       
        @SuppressWarnings("unchecked")
        List<AmpFundingDetail> fundingDets = null;
        try {
            Query query = session.createQuery(oql);
            query.setLong("year", year);
            query.setLong("orgID", orgID);
            fundingDets = query.list();
            FundingCalculationsHelper cal = new FundingCalculationsHelper();
            cal.doCalculations(fundingDets, currCode);
            if(isComm){
            return  cal.getTotActualComm();
            }
            else{
                return cal.getTotActualDisb();
            }

        } catch (Exception e) {
            logger.error(e);
            throw new DgException(
                    "Cannot load sector fundings by donors from db", e);
        }
    }
    
    public static Double convert(Double amount,AmpCurrency originalCurrency){
    	try {
			CurrencyWorker.convertToUSD(amount, originalCurrency.getCurrencyCode());
		} catch (AimException e) {
			//e.printStackTrace();
			return amount;
		}
    	return amount;
    }
    
    public static Double calculatePercentage(Double amount,Float percentage){
    	double result=amount.doubleValue()*percentage.floatValue()/100;
    	return new Double(result);
    }
    
    public static String getInStatment(Long ids[]){
    	String oql="";
		for (int i = 0; i < ids.length; i++) {
			oql+=""+ids[i];
			if (i<ids.length-1){
				oql+=",";
			}
		}
    	return oql;
    }
    
    /**
     * Key worker for donors
     * @author Irakli Kobiashvili
     *
     */
    public static class DonorIdWorker implements KeyWorker<Long, AmpOrganisation>{

		public void markKeyForRemoval(AmpOrganisation element) {
			//empty, no need yet
		}

		@SuppressWarnings("deprecation")
		public void updateKey(AmpOrganisation element, Long newKey) {
			element.setAmpOrgId(newKey);
		}

		public Long resolveKey(AmpOrganisation element) {
			return element.getAmpOrgId();
		}
    	
    }

	
}
