/**
 * 
 */
package org.digijava.module.dataExchange.pojo;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;

import org.digijava.module.dataExchange.engine.SourceBuilder;
import org.digijava.module.dataExchange.jaxb.Activities;

/**
 * @author dan
 * this class implements the execution of a source builder (url, ws, file upload)
 * DEImportBuilder.java class is building the import: check, xml validation, content validation, save
 */
public class DEImportItem {
	private SourceBuilder sourceBuilder;
	private Activities activities;
	private HashMap<String,Boolean> hashFields;
	
	
	public HashMap<String, Boolean> getHashFields() {
		return hashFields;
	}


	public void setHashFields(HashMap<String, Boolean> hashFields) {
		this.hashFields = hashFields;
	}


	public Activities getActivities() {
		return activities;
	}


	public void setActivities(Activities activities) {
		this.activities = activities;
	}


	public DEImportItem(SourceBuilder sourceBuilder) {
		this.sourceBuilder = sourceBuilder;
	}


	public DEImportItem() {
		// TODO Auto-generated constructor stub
	}


	public SourceBuilder getSourceBuilder() {
		return sourceBuilder;
	}


	public void setSourceBuilder(SourceBuilder sourceBuilder) {
		this.sourceBuilder = sourceBuilder;
	}
	
	public InputStream getInputStream(){
		return new ByteArrayInputStream(this.sourceBuilder.getInputString().getBytes());
	}
	

}
