package org.digijava.module.gis.helper;

/**
 * Option bean for charts.
 * @author Irakli Kobiashvili
 *
 */
public class ChartOption {
	private boolean showLegend;
	private boolean showLabels;
	private boolean showTitle;
	private String labelPattern;
	private Integer width;
	private Integer height;
	private boolean createMap;
	
	public boolean isShowLegend() {
		return showLegend;
	}
	public void setShowLegend(boolean showLegend) {
		this.showLegend = showLegend;
	}
	public boolean isShowLabels() {
		return showLabels;
	}
	public void setShowLabels(boolean showLabels) {
		this.showLabels = showLabels;
	}
	public boolean isShowTitle() {
		return showTitle;
	}
	public void setShowTitle(boolean showTitle) {
		this.showTitle = showTitle;
	}
	public String getLabelPattern() {
		return labelPattern;
	}
	public void setLabelPattern(String labelPattern) {
		this.labelPattern = labelPattern;
	}
	public Integer getWidth() {
		return width;
	}
	public void setWidth(Integer width) {
		this.width = width;
	}
	public Integer getHeight() {
		return height;
	}
	public void setHeight(Integer height) {
		this.height = height;
	}
	public boolean isCreateMap() {
		return createMap;
	}
	public void setCreateMap(boolean createMap) {
		this.createMap = createMap;
	}
}
