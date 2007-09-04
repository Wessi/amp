package org.digijava.module.aim.helper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.digijava.module.aim.dbentity.AmpTheme;
/**
 * 
 * @author Alex Gartner
 * Contains the filtered information of an AmpTheme. Newlines are filtered.
 * Needed for the view. 
 */
public class FilteredAmpTheme extends AmpTheme {
	private String filterString (String str){
		if(str==null) return "";
		try{
			String ret	= str.replaceAll("\n"," ");
			ret			= ret.replaceAll("\r","");
			ret			= ret.replaceAll("\t","&nbsp;&nbsp;&nbsp;");
			return ret;
		}
		catch(Exception E) {
			E.printStackTrace();
			return null;
		}
	} 
	public FilteredAmpTheme( AmpTheme ampTheme) {
		this.setAmpThemeId( 	ampTheme.getAmpThemeId() );
		this.setName( 			ampTheme.getName() );
		this.setThemeCode( 		ampTheme.getThemeCode() );
		this.setTypeCategoryValue(	ampTheme.getTypeCategoryValue() );
		this.setDescription( 	filterString(ampTheme.getDescription()) );
		this.setLeadAgency( 	filterString(ampTheme.getLeadAgency()) );
		this.setTargetGroups( 	filterString(ampTheme.getTargetGroups()) );
		this.setBackground( 	filterString(ampTheme.getBackground()) );
		this.setObjectives( 	filterString(ampTheme.getObjectives()) );
		this.setOutputs( 		filterString(ampTheme.getOutputs()) );
		this.setBeneficiaries( 	filterString(ampTheme.getBeneficiaries()) );
		this.setEnvironmentConsiderations( 	filterString(ampTheme.getEnvironmentConsiderations()) );
	}
	public static List transformAmpThemeList(List list) {
		List newList		= new ArrayList();
		Iterator iterator	= list.iterator();
		while ( iterator.hasNext() ) {
			AmpTheme ampTheme				= (AmpTheme) iterator.next();
			FilteredAmpTheme	fAmpTheme	= new FilteredAmpTheme(ampTheme);
			newList.add(fAmpTheme);
		}
		return newList;
	}
}
