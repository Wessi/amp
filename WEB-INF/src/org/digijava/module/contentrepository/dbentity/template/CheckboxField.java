package org.digijava.module.contentrepository.dbentity.template;

public class CheckboxField extends TemplateField {
	
	public String getType(){
		return "chf"; //checkbox field
	}
	
	public boolean getCanHaveMultipleValues() {
		return false;
	}
	
	@Override
	public String getRendered() {
		String retVal=null;
		if(getPossibleValuesList()!=null){
			//submits in request parameter "doc_checkbox_5" if checkbox  for this template is 5th field.
			retVal="<input type=\"checkbox\" name=\"doc_checkbox_"+getOrdinalNumber().intValue()+"\">";
			for (PossibleValue posVal : getPossibleValuesList()) {
				retVal+=posVal.getValue();
			}
			retVal+="</input>";
		}		
		return retVal;
	}

}
