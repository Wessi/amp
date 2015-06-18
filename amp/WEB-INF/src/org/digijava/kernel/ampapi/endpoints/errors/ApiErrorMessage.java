/**
 * 
 */
package org.digijava.kernel.ampapi.endpoints.errors;

/**
 * Defines API Error Message template and stores custom value if needed
 * @author Nadejda Mandrescu
 */
public class ApiErrorMessage {
	/** Message custom Error Code [0..99] within it's component/method*/
	public final Integer id;
	/** General error description (laconic) */
	public final String description;
	/** (Optional) Error message pattern, e.g. "Missing fields: %s". <br> 
	 * Set to "" if no pattern and direct value must be displayed. <br>
	 * Set to null if no pattern and description + " %s" pattern must be used 
	 */
	public final String pattern;
	/** Error details (e.g. "project_title"), without prefix (use pattern for prefix) */
	public final String value;
	
	/**
	 * Defines an ApiErrorMessage 
	 * @param id see {@link #id}
	 * @param description see {@link #description}
	 * @param pattern see {@link #pattern}
	 */
	public ApiErrorMessage(Integer id, String description, String pattern) {
		this(id, description, pattern, null);
	}
	
	/**
	 * Configures an {@link #ApiErrorMessage(Integer, String, String)} with more details
	 * @param aem general error definition, see {@link #ApiErrorMessage(Integer, String, String)}
	 * @param value details, see {@link #value}
	 */
	public ApiErrorMessage(ApiErrorMessage aem, String value) {
		this(aem.id, aem.description, aem.pattern, aem.value == null ? value : aem.value + ", " + value);
	}
	
	private ApiErrorMessage(int id, String description, String pattern, String value) {
		if (id <0 || id > 99) {
			throw new RuntimeException(String.format("Invalid id = %n, must be within [0..99] range.", id));
		}
		if (description == null) {
			throw new RuntimeException(String.format("Description is mandatory"));
		}
		this.id = id;
		this.description = description;
		this.pattern = pattern;
		this.value = value;
	}
	
	public int hashCode() {
		return (19 + id) * 23 + description.hashCode();
	}

}
