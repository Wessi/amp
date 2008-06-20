package org.digijava.module.gis.form;

import org.apache.struts.action.ActionForm;
import org.digijava.module.gis.action.ShowWidgetChart;

/**
 * Form for {@link ShowWidgetChart} action.
 * @author Irakli Kobiashvili
 *
 */
public class ShowWidgetChartForm extends ActionForm {
	private static final long serialVersionUID = 1L;

	private Integer imageWidth;
	private Integer imageHeight;
	private Long widgetId;
	private Long objectId; 
	
	public Long getObjectId() {
		return objectId;
	}
	public void setObjectId(Long objectId) {
		this.objectId = objectId;
	}
	public Integer getImageWidth() {
		return imageWidth;
	}
	public void setImageWidth(Integer imageWidth) {
		this.imageWidth = imageWidth;
	}
	public Integer getImageHeight() {
		return imageHeight;
	}
	public void setImageHeight(Integer imageHeight) {
		this.imageHeight = imageHeight;
	}
	public Long getWidgetId() {
		return widgetId;
	}
	public void setWidgetId(Long widgetId) {
		this.widgetId = widgetId;
	}
	
}
