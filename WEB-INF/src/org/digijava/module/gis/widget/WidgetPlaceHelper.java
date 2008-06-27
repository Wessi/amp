package org.digijava.module.gis.widget;

import java.io.Serializable;
import java.util.Date;

import org.digijava.module.gis.dbentity.AmpDaTable;
import org.digijava.module.gis.dbentity.AmpDaWidgetPlace;
import org.digijava.module.gis.dbentity.AmpWidget;
import org.digijava.module.gis.dbentity.AmpWidgetIndicatorChart;
import org.digijava.module.gis.util.WidgetUtil;

/**
 * Widget place and Widget helper class.
 * @author Irakli Kobiashvili
 *
 */
public class WidgetPlaceHelper implements Serializable{

	private static final long serialVersionUID = 1L;
	private Long placeId;
	private String placeName;
	private String placeCode;
	private Date placeLastRenderTime;
	private Long widgetId;
	private String widgetName;
	private String widgetCode;
	private String widgetTypeName;
	private Integer widgetTypeCode;
	private String widgetClassName;
	private Long objectId;
	private String objectName;
	private String widgetCombinedName;
	
	/**
	 * Default constructor
	 */
	public WidgetPlaceHelper(){
		
	}
	
	/**
	 * Constructor from widget place entity bean.
	 * @param place
	 */
	public WidgetPlaceHelper(AmpDaWidgetPlace place){
		this.placeId = place.getId();
		this.placeName = place.getName();
		this.placeCode = place.getCode();
		this.placeLastRenderTime = place.getLastRendered();
		if (place.getAssignedWidget()!=null){
			fromWidget(place.getAssignedWidget());
		}
	}
	
	/**
	 * Constructor from widget entity bean.
	 * @param widget
	 */
	public WidgetPlaceHelper(AmpWidget widget){
		fromWidget(widget);
	}
	
	private void fromWidget(AmpWidget widget){
		this.widgetClassName = widget.getClass().getName();
		this.widgetId = widget.getId();
		this.widgetName = widget.getName();
		this.widgetCode = widget.getCode();
		if (widget instanceof AmpDaTable){
			this.widgetTypeName = "Table";
			this.widgetTypeCode = new Integer(WidgetUtil.TABLE);
		}else if (widget instanceof AmpWidgetIndicatorChart){
			this.widgetTypeName = "Indicator Chart";
			this.widgetTypeCode = new Integer(WidgetUtil.CHART_INDICATOR);
		}else {
			this.widgetTypeName = "Unknown";
			this.widgetTypeCode = new Integer(0);
		}
		this.widgetCombinedName = this.widgetName + "  ("+this.widgetTypeName+")";
	}
	
	public Long getPlaceId() {
		return placeId;
	}
	public void setPlaceId(Long placeId) {
		this.placeId = placeId;
	}
	public String getPlaceName() {
		return placeName;
	}
	public void setPlaceName(String placeName) {
		this.placeName = placeName;
	}
	public String getPlaceCode() {
		return placeCode;
	}
	public void setPlaceCode(String placeCode) {
		this.placeCode = placeCode;
	}
	public Date getPlaceLastRenderTime() {
		return placeLastRenderTime;
	}
	public void setPlaceLastRenderTime(Date placeLastRenderTime) {
		this.placeLastRenderTime = placeLastRenderTime;
	}
	public Long getWidgetId() {
		return widgetId;
	}
	public void setWidgetId(Long widgetId) {
		this.widgetId = widgetId;
	}
	public String getWidgetName() {
		return widgetName;
	}
	public void setWidgetName(String widgetName) {
		this.widgetName = widgetName;
	}
	public String getWidgetCode() {
		return widgetCode;
	}
	public void setWidgetCode(String widgetCode) {
		this.widgetCode = widgetCode;
	}
	public String getWidgetTypeName() {
		return widgetTypeName;
	}
	public void setWidgetTypeName(String widgetTypeName) {
		this.widgetTypeName = widgetTypeName;
	}
	public String getWidgetClassName() {
		return widgetClassName;
	}
	public void setWidgetClassName(String widgetClassName) {
		this.widgetClassName = widgetClassName;
	}
	public Long getObjectId() {
		return objectId;
	}
	public void setObjectId(Long objectId) {
		this.objectId = objectId;
	}
	public String getObjectName() {
		return objectName;
	}
	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}

	public Integer getWidgetTypeCode() {
		return widgetTypeCode;
	}

	public void setWidgetTypeCode(Integer widgetTypeCode) {
		this.widgetTypeCode = widgetTypeCode;
	}

	public String getWidgetCombinedName() {
		return widgetCombinedName;
	}

	public void setWidgetCombinedName(String widgetCombinedName) {
		this.widgetCombinedName = widgetCombinedName;
	}

}
