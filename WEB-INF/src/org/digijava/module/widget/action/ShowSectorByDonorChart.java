package org.digijava.module.widget.action;

import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.digijava.kernel.util.RequestUtils;
import org.digijava.module.widget.form.SectorByDonorTeaserForm;
import org.digijava.module.widget.helper.ChartOption;
import org.digijava.module.widget.util.ChartWidgetUtil;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;

public class ShowSectorByDonorChart extends Action {

	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		SectorByDonorTeaserForm cForm = (SectorByDonorTeaserForm)form;
        response.setContentType("image/png");
        Integer year = null;
        Long[] donorIDs = null;
        ChartOption opt=createChartOption(cForm,request);

        if (cForm.getSelectedYear()!=null && !cForm.getSelectedYear().equals("-1")){
        	year = new Integer(cForm.getSelectedYear());
        }else{
        	GregorianCalendar cal = new GregorianCalendar();
        	year = new Integer(cal.get(Calendar.YEAR));
        }
        if(cForm.getSelectedDonor()!=null && cForm.getSelectedDonor().longValue()!=-1){
        	donorIDs = new Long[1];
        	donorIDs[0] = cForm.getSelectedDonor();
        }        
        
        //generate chart
        JFreeChart chart = ChartWidgetUtil.getSectorByDonorChart(donorIDs, year, opt);
        ChartRenderingInfo info = new ChartRenderingInfo();
        
        //write image in response
		ChartUtilities.writeChartAsPNG(response.getOutputStream(),chart,opt.getWidth().intValue(),opt.getHeight().intValue(), info);
		
		//fill years' drop-down
		cForm.setYears(ChartWidgetUtil.getYears());
		return null;
	}
	
	private ChartOption createChartOption(SectorByDonorTeaserForm form,HttpServletRequest request){
		ChartOption opt= new ChartOption();
		
		//TITLE
		if (form.getShowTitle()==null){
			opt.setShowTitle(true);
		}else{
			opt.setShowTitle(form.getShowTitle());
		}
		//LEGEND
		if (form.getShowLegend()==null){
			opt.setShowLegend(true);
		}else{
			opt.setShowLegend(form.getShowLegend());
		}
		//LABEL
		if (form.getShowLabel()==null){
			opt.setShowLabels(true);
		}else{
			opt.setShowLabels(form.getShowLabel());
		}
		//HEIGHT		
		if (form.getImageHeight() == null){
        	opt.setHeight(new Integer(350));
        }else{
        	opt.setHeight(form.getImageHeight());
        }
		//WIDTH
		if (form.getImageWidth() == null){
        	opt.setWidth(new Integer(420));
        }else{
        	opt.setWidth(form.getImageWidth());
        }
                Long siteId=RequestUtils.getSiteDomain(request).getSite().getId();
                opt.setSiteId(siteId);
                String langCode= RequestUtils.getNavigationLanguage(request).getCode();
                opt.setLangCode(langCode);
		return opt;		
	}

}
