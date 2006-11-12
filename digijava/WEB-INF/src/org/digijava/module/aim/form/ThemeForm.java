package org.digijava.module.aim.form;

import org.apache.struts.action.*;
import java.util.Collection;

public class ThemeForm extends ActionForm {

		  private Collection themes;
		  private Collection subPrograms;
		  private Collection prgIndicators;
		  private Long themeId;
		  private String programName;
		  private String programCode;
		  private String programDescription;
		  private String programType;
		  private Long prgParentThemeId;
		  private String prgLanguage;
		  private String parentProgram;
		  private String version; 
		  private String event;
		  private int valueType;
		  private String code;
		  private String name;
		  private String type;
		  private String indicatorDescription;
		  private String creationDate;
		  private int category;
		  private boolean npIndicator;
		  

		/**
		 * @return Returns the themes.
		 */
		public Collection getThemes() {
			return themes;
		}

		/**
		 * @param themes The themes to set.
		 */
		public void setThemes(Collection themes) {
			this.themes = themes;
		}

		/**
		 * @return Returns the subPrograms.
		 */
		public Collection getSubPrograms() {
			return subPrograms;
		}

		/**
		 * @param subPrograms The subPrograms to set.
		 */
		public void setSubPrograms(Collection subPrograms) {
			this.subPrograms = subPrograms;
		}

		/**
		 * @return Returns the prgIndicators.
		 */
		public Collection getPrgIndicators() {
			return prgIndicators;
		}

		/**
		 * @param prgIndicators The prgIndicators to set.
		 */
		public void setPrgIndicators(Collection prgIndicators) {
			this.prgIndicators = prgIndicators;
		}

		/**
		 * @return Returns the themeId.
		 */
		public Long getThemeId() {
			return themeId;
		}

		/**
		 * @param themeId The themeId to set.
		 */
		public void setThemeId(Long themeId) {
			this.themeId = themeId;
		}

		/**
		 * @return Returns the programCode.
		 */
		public String getProgramCode() {
			return programCode;
		}

		/**
		 * @param programCode The programCode to set.
		 */
		public void setProgramCode(String programCode) {
			this.programCode = programCode;
		}

		/**
		 * @return Returns the programDescription.
		 */
		public String getProgramDescription() {
			return programDescription;
		}

		/**
		 * @param programDescription The programDescription to set.
		 */
		public void setProgramDescription(String programDescription) {
			this.programDescription = programDescription;
		}

		/**
		 * @return Returns the programName.
		 */
		public String getProgramName() {
			return programName;
		}

		/**
		 * @param programName The programName to set.
		 */
		public void setProgramName(String programName) {
			this.programName = programName;
		}

		/**
		 * @return Returns the programType.
		 */
		public String getProgramType() {
			return programType;
		}

		/**
		 * @param programType The programType to set.
		 */
		public void setProgramType(String programType) {
			this.programType = programType;
		}

		/**
		 * @return Returns the indicatorDescription.
		 */
		public String getIndicatorDescription() {
			return indicatorDescription;
		}

		/**
		 * @param indicatorDescription The indicatorDescription to set.
		 */
		public void setIndicatorDescription(String indicatorDescription) {
			this.indicatorDescription = indicatorDescription;
		}

		/**
		 * @return Returns the prgLanguage.
		 */
		public String getPrgLanguage() {
			return prgLanguage;
		}

		/**
		 * @param prgLanguage The prgLanguage to set.
		 */
		public void setPrgLanguage(String prgLanguage) {
			this.prgLanguage = prgLanguage;
		}

		/**
		 * @return Returns the parentProgram.
		 */
		public String getParentProgram() {
			return parentProgram;
		}

		/**
		 * @param parentProgram The parentProgram to set.
		 */
		public void setParentProgram(String parentProgram) {
			this.parentProgram = parentProgram;
		}

		/**
		 * @return Returns the prgParentThemeId.
		 */
		public Long getPrgParentThemeId() {
			return prgParentThemeId;
		}

		/**
		 * @param prgParentThemeId The prgParentThemeId to set.
		 */
		public void setPrgParentThemeId(Long prgParentThemeId) {
			this.prgParentThemeId = prgParentThemeId;
		}

		/**
		 * @return Returns the version.
		 */
		public String getVersion() {
			return version;
		}

		/**
		 * @param version The version to set.
		 */
		public void setVersion(String version) {
			this.version = version;
		}

		/**
		 * @return Returns the event.
		 */
		public String getEvent() {
			return event;
		}

		/**
		 * @param event The event to set.
		 */
		public void setEvent(String event) {
			this.event = event;
		}

		/**
		 * @return Returns the category.
		 */
		public int getCategory() {
			return category;
		}

		/**
		 * @param category The category to set.
		 */
		public void setCategory(int category) {
			this.category = category;
		}

		/**
		 * @return Returns the code.
		 */
		public String getCode() {
			return code;
		}

		/**
		 * @param code The code to set.
		 */
		public void setCode(String code) {
			this.code = code;
		}

		/**
		 * @return Returns the creationDate.
		 */
		public String getCreationDate() {
			return creationDate;
		}

		/**
		 * @param creationDate The creationDate to set.
		 */
		public void setCreationDate(String creationDate) {
			this.creationDate = creationDate;
		}

		/**
		 * @return Returns the name.
		 */
		public String getName() {
			return name;
		}

		/**
		 * @param name The name to set.
		 */
		public void setName(String name) {
			this.name = name;
		}

		/**
		 * @return Returns the npIndicator.
		 */
		public boolean isNpIndicator() {
			return npIndicator;
		}

		/**
		 * @param npIndicator The npIndicator to set.
		 */
		public void setNpIndicator(boolean npIndicator) {
			this.npIndicator = npIndicator;
		}

		/**
		 * @return Returns the type.
		 */
		public String getType() {
			return type;
		}

		/**
		 * @param type The type to set.
		 */
		public void setType(String type) {
			this.type = type;
		}

		/**
		 * @return Returns the valueType.
		 */
		public int getValueType() {
			return valueType;
		}

		/**
		 * @param valueType The valueType to set.
		 */
		public void setValueType(int valueType) {
			this.valueType = valueType;
		}
}

