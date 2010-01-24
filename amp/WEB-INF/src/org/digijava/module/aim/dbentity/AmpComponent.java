/*
 * AmpComponent.java
 * Created : 9th March, 2005
 */

package org.digijava.module.aim.dbentity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Set;

import org.apache.log4j.Logger;
import org.digijava.module.aim.util.Output;

import org.digijava.module.categorymanager.dbentity.AmpCategoryClass;
import org.digijava.module.categorymanager.dbentity.AmpCategoryValue;

/**
 * Persister class for Components
 * @author Priyajith
 */
public class AmpComponent implements Serializable,Comparable<AmpComponent>, Versionable {
	private static Logger logger = Logger.getLogger(AmpComponent.class);
	private Long ampComponentId;
	private String title;
	private String description;
	private java.sql.Timestamp creationdate;
	private String code;
	
	//private String type;
	private AmpCategoryValue type;
	
	private AmpActivity activity;
	private String Url;
	private Set<AmpComponentFunding> funding;
	
	public void setActivity(AmpActivity activity) {
		this.activity = activity;
	}
	public AmpActivity getActivity() {
		return activity;
	}
	public Long getAmpComponentId() {
		return ampComponentId;
	}
	public void setAmpComponentId(Long ampComponentId) {
		this.ampComponentId = ampComponentId;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	public AmpCategoryValue getType() {
		return type;
	}
	public void setType(AmpCategoryValue type) {
		this.type = type;
	}
	
	public void setUrl(String url) {
		Url = url;
	}
	public String getUrl() {
		return Url;
	}
	public void setFunding(Set<AmpComponentFunding> funding) {
		this.funding = funding;
	}
	public Set<AmpComponentFunding> getFunding() {
		return funding;
	}
	
	/**
	 * A simple string comparison to sort components by title
	 */
	
	public int compareTo(AmpComponent o) {
		try {
			if (this.title.compareToIgnoreCase(o.title) > 0) {
				return 1;
			} else if (this.title.compareToIgnoreCase(o.title) == 0) {
				return -0;
			}
		} catch (Exception e) {
			logger.error(e);
			return -1;
		}
		return -1;
	}	
	
	@Override
	public boolean equals(Object obj) {
		AmpComponent target=(AmpComponent) obj;
		if (target!=null && this.ampComponentId!=null){
			return (target.getAmpComponentId().doubleValue()==this.getAmpComponentId().doubleValue());
		}
		return false;
		
	}
	
	@Override
	public int hashCode() {
		if( this.ampComponentId ==null) return 0;
		return this.ampComponentId.hashCode();
	}
	public java.sql.Timestamp getCreationdate() {
		return creationdate;
	}
	public void setCreationdate(java.sql.Timestamp creationdate) {
		this.creationdate = creationdate;
	}
	
	@Override
	public boolean equalsForVersioning(Object obj) {
		AmpComponent aux = (AmpComponent) obj;
		String original = this.title != null ? this.title : "";
		String copy = aux.title != null ? aux.title : "";
		if (original.equals(copy)) {
			return true;
		}
		return false;
	}

	@Override
	public Output getOutput() {
		Output out = new Output();
		out.setOutputs(new ArrayList<Output>());
		out.getOutputs().add(
				new Output(null, new String[] { " Title: " }, new Object[] { this.title != null ? this.title
						: "Empty Title" }));
		if (this.description != null && !this.description.trim().equals("")) {
			out.getOutputs()
					.add(new Output(null, new String[] { " Description: " }, new Object[] { this.description }));
		}
		if (this.code != null && !this.code.trim().equals("")) {
			out.getOutputs().add(new Output(null, new String[] { " Code: " }, new Object[] { this.code }));
		}
		if (this.creationdate != null) {
			out.getOutputs().add(
					new Output(null, new String[] { " Creation Date: " }, new Object[] { this.creationdate }));
		}
		if (this.Url != null && !this.Url.trim().equals("")) {
			out.getOutputs().add(new Output(null, new String[] { " URL: " }, new Object[] { this.Url }));
		}
		if (this.activity != null ) {
			out.getOutputs().add(new Output(null, new String[] { " Activity: " }, new Object[] { this.activity }));
		}
		return out;
	}
	@Override
	public Object getValue() {
		String value = " " + this.creationdate + this.description + this.Url + this.code + this.activity;
		return value;
	}
	
}
