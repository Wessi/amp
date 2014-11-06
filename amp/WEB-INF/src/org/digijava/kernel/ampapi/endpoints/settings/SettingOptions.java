/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.settings;

import java.util.List;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.digijava.kernel.translator.TranslatorWorker;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
/**
 * Stores a generic setting configuration  
 * @author Nadejda Mandrescu
 */
public class SettingOptions {
	/**
	 * A setting option
	 */
	public static class Option {
		/** option id */
		public final String id;
		/** option name to display */
		public final String name;
		/** option value, to change */
		public final String value;
		
		/**
		 * Configures an option by 'id' and 'name'
		 * 
		 * @param id - option id
		 * @param name - option name
		 */
		public Option(String id, String name) {
			this(id, name, id, false);
		}
		
		public Option(String id, String name, String value) {
			this(id, name, value, false);
		}
		
		public Option(String id, String name, boolean translate) {
			this(id, name, id, translate);
		}
		
		public Option(String id, String name, String value, boolean translate) {
			this.id = id;
			this.name = translate ? TranslatorWorker.translateText(name) : name;
			this.value = value;
		}
	}
	
	
	
	/** Setting id */
	@Deprecated
	public final String id;
	/** Specifies if multiple options can be selected */
	public final Boolean multi;
	/** Setting name */
	@Deprecated
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
	public SettingOptions(String id, boolean multi, String name, String defaultId, List<Option> options) {
		this.id = id;
		this.multi = multi;
		this.name = name == null ? null : TranslatorWorker.translateText(name);
		this.defaultId = defaultId;
		this.options = options;
	}
}
