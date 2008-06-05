/**
 * 
 */
package org.digijava.module.aim.dbentity;

/**
 * @author mihai
 *
 */
public class AmpColumnsFilters {
	private Long id;
	private AmpColumns column;
	private String beanFieldName;
	private String viewFieldName;
	
	public AmpColumnsFilters(AmpColumns column, String beanFieldName, String viewFieldName) {
		this.column=column;
		this.beanFieldName=beanFieldName;
		this.viewFieldName=viewFieldName;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public AmpColumns getColumn() {
		return column;
	}
	public void setColumn(AmpColumns column) {
		this.column = column;
	}
	public String getBeanFieldName() {
		return beanFieldName;
	}
	public void setBeanFieldName(String beanFieldName) {
		this.beanFieldName = beanFieldName;
	}
	public String getViewFieldName() {
		return viewFieldName;
	}
	public void setViewFieldName(String viewFieldName) {
		this.viewFieldName = viewFieldName;
	}
	}
