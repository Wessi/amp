package org.digijava.module.aim.helper;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;

import org.apache.log4j.Logger;
import org.digijava.module.aim.util.FeaturesUtil;

/**
 * @author Sebastian Dimunzio 
 * This a helper class for number formating,
 */
public class FormatHelper {

    private static Logger logger = Logger.getLogger(FormatHelper.class);


           /**                                                                                                                                                  
            * Parse a String tring based on Global Setting Format to Double                                                                                     
            *                                                                                                                                                   
            * @param number                                                                                                                                     
            * @return                                                                                                                                           
            * @throws ParseException                                                                                                                            
            */                                                                                                                                                  
           public static Double parseDouble(String number) {                                                                              
                                             
                   if(number==null) return null;
                   String format = "###,###,###,###.##";                                                                                                        
                   String decimalSeparator = ".";                                                                                                               
                   String groupSeparator = ",";                                                                                                                 
                                                                                                                                                                
                   // get setting from global setting                                                                                                           
                   format = FeaturesUtil                                                                                                                        
                                  .getGlobalSettingValue(GlobalSettingsConstants.NUMBER_FORMAT);                                                               
                   decimalSeparator = FeaturesUtil                                                                                                              
                                   .getGlobalSettingValue(GlobalSettingsConstants.DECIMAL_SEPARATOR);                                                           
                   groupSeparator = FeaturesUtil                                                                                                                
                                  .getGlobalSettingValue(GlobalSettingsConstants.GROUP_SEPARATOR);                                                             
                                                                                                                                                                
                   Double result;                                                                                                                               
                   DecimalFormatSymbols decSymbols = new DecimalFormatSymbols();                                                                                
                   decSymbols.setDecimalSeparator(decimalSeparator.charAt(0));                                                                                  
                   decSymbols.setGroupingSeparator(groupSeparator.charAt(0));                                                                                   
                   DecimalFormat formater = new DecimalFormat(format, decSymbols);                                                                              
                   try {                                                                                                                                        
                           result = formater.parse(number).doubleValue();                                                                                       
                   } catch (ParseException e) {                                                                                                                 
                           logger.error("Error parsing String to double", e);     
                           return null;
                   }                                                                                                                                            
                   return result;                                                                                                                               
           }                                                                                                                                                    
                                                                                                                                                                
           public static String getDifference(String one,String two){                                                                                           
                   return "";                                                                                                                                   
            }                      
    
    public static String formatNumber(double nr) {
    	
    	String format = "###,###,###,###.##";
    	String decimalSeparator = ".";
    	String groupSeparator = ",";

    	// get setting from global setting
    	format = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.NUMBER_FORMAT);
    	decimalSeparator = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.DECIMAL_SEPARATOR);
    	groupSeparator = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.GROUP_SEPARATOR);
    	Double number;
    	String result;
    	if (nr == 0) {
    	    number = new Double(0d);
    	}
    	else number = new Double(nr);

    	DecimalFormatSymbols decSymbols = new DecimalFormatSymbols();
    	decSymbols.setDecimalSeparator(decimalSeparator.charAt(0));
    	decSymbols.setGroupingSeparator(groupSeparator.charAt(0));
    	DecimalFormat formater = new DecimalFormat(format, decSymbols);
    	result = formater.format(number);
    	return result;
        }
    
   /**                                                                                                                                                  
    * Return an string based on Global Setting Number Format                                                                                            
    * @param number                                                                                                                                     
    * @return                                                                                                                                           
    */                                                                                                                                                  
   public static String formatNumber(Double number) {                                                                                                   
       String result;                                                                                                                               
       if (number == null) {                                                                                                                        
               number = new Double(0d);                                                                                                             
       }                                                                                                                                                             
	   DecimalFormat formater = getDecimalFormat();                                                     
	   result = formater.format(number);                                                                                                            
       return result;                                                                                                                               
    }       
   
   public static DecimalFormat getDecimalFormat(){
       String format = "###,###,###,###.##";                                                                                                        
       String decimalSeparator = ".";                                                                                                               
       String groupSeparator = ",";                                                                                                                 
                                                                                                                                                    
       // get setting from global setting                                                                                                           
       format = FeaturesUtil                                                                                                                        
                       .getGlobalSettingValue(GlobalSettingsConstants.NUMBER_FORMAT);                                                               
       decimalSeparator = FeaturesUtil                                                                                                              
                      .getGlobalSettingValue(GlobalSettingsConstants.DECIMAL_SEPARATOR);                                                           
       groupSeparator = FeaturesUtil                                                                                                                
                       .getGlobalSettingValue(GlobalSettingsConstants.GROUP_SEPARATOR);                                                             
                                                                                                                                                                                                                                                                                               
                                                                                                                                                   
       DecimalFormatSymbols decSymbols = new DecimalFormatSymbols();                                                                                
       decSymbols.setDecimalSeparator(decimalSeparator.charAt(0));                                                                                  
       decSymbols.setGroupingSeparator(groupSeparator.charAt(0));                                                                                   
       DecimalFormat formater = new DecimalFormat(format, decSymbols);    	
       return formater;
   }
                       
    
    
   
    public static DecimalFormat getDefaultFormat() {

	String format = "###,###,###,###.##";
	String decimalSeparator = ".";
	String groupSeparator = ",";

	// get setting from global setting
	format = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.NUMBER_FORMAT);
	decimalSeparator = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.DECIMAL_SEPARATOR);
	groupSeparator = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.GROUP_SEPARATOR);

	DecimalFormatSymbols decSymbols = new DecimalFormatSymbols();
	decSymbols.setDecimalSeparator(decimalSeparator.charAt(0));
	decSymbols.setGroupingSeparator(groupSeparator.charAt(0));
	DecimalFormat formater = new DecimalFormat(format, decSymbols);
	
	return formater;
    }
    
    public static String getDecimalSymbol() {
	return String.valueOf(getDefaultFormat().getDecimalFormatSymbols().getDecimalSeparator());
    }

    public static String getGroupSymbol() {
	return String.valueOf(getDefaultFormat().getDecimalFormatSymbols().getGroupingSeparator());
    }

    
   
}
