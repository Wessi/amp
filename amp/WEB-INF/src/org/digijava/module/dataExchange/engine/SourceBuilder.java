package org.digijava.module.dataExchange.engine;

import java.io.InputStream;

import org.digijava.module.dataExchange.dbentity.DESourceSetting;

public abstract class SourceBuilder {
	
	protected DESourceSetting DESourceSetting;
	protected String inputString;

	public SourceBuilder() {
		// TODO Auto-generated constructor stub
	}
	
	
	public SourceBuilder(DESourceSetting DESourceSetting,
			String inputStream) {
		super();
		this.DESourceSetting = DESourceSetting;
		this.inputString = inputStream;
	}
	
	public DESourceSetting getDESourceSetting() {
		return DESourceSetting;
	}

	public void setDESourceSetting(DESourceSetting dESourceSetting) {
		DESourceSetting = dESourceSetting;
	}

	public String getInputString() {
		return inputString;
	}

	public void setInputString(String inputString) {
		this.inputString = inputString;
	}

	public SourceBuilder(DESourceSetting DESourceSetting) {
		super();
		this.DESourceSetting = DESourceSetting;
	}

	/**
	 * This will initialize the private property inputString.
	 * This must be implemented by all implementers (URLSourceBuilder and WSSourceBuilder).
	 * For FileSourceBuilder the InputStream will be given as a parameter in the constructor.
	 */
	public abstract void process();

	
	
}
