/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.util;

import java.util.List;

import org.digijava.kernel.translator.TranslatorWorker;

/**
 * Stores a generic setting configuration  
 * @author Nadejda Mandrescu
 */
public class GisSettingOptions {
	/**
	 * A setting option
	 */
	public static class Option {
		public final String id;
		public final String name;
		/**
		 * Configures an option by 'id' and 'name'
		 * @param id - option id
		 * @param name - option name
		 */
		public Option(String id, String name) {
			this.id = id;
			this.name = name;
		}
	}
	
	/** Setting id */
	public final Integer id;
	/** Setting name */
	public final String name;
	/** Default setting option id */
	public final String defaultId;
	/** List of available setting options */
	public final List<Option> options;
	
	/**
	 * Configures a GIS Setting
	 * @param id - setting id
	 * @param name - setting name
	 * @param defaultId - default setting option id
	 * @param options - list of available setting options
	 */
	public GisSettingOptions(Integer id, String name, String defaultId, List<Option> options) {
		this.id = id;
		this.name = TranslatorWorker.translateText(name);
		this.defaultId = defaultId;
		this.options = options;
	}	
}
