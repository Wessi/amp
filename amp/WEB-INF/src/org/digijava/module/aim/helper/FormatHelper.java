package org.digijava.module.aim.helper;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.log4j.Logger;
import org.digijava.module.aim.util.FeaturesUtil;

/**
 * @author Sebastian Dimunzio 
 * This a helper class for number formating,
 */
public class FormatHelper {

    private static Logger logger = Logger.getLogger(FormatHelper.class);

    
    public static ThreadLocal<DecimalFormat> tlocal = new ThreadLocal<DecimalFormat>();

           /**                                                                                                                                                  
            * Parse a String tring based on Global Setting Format to Double                                                                                     
            *                                                                                                                                                   
            * @param number                                                                                                                                     
            * @return                                                                                                                                           
            * @throws ParseException                                                                                                                            
            */                                                                                                                                                  
           public static Double parseDouble(String number) {                                                                              
                                             
                   if(number==null) return null;
                   if("".equalsIgnoreCase(number)){
                	   return new Double(0);
                   }
                                                                                    
                   Double result;                                                                                                                               
                   DecimalFormat formater = getDecimalFormat();                                                                              
                   try {                                                                                                                                        
                           result = formater.parse(number).doubleValue();                                                                                       
                   } catch (ParseException e) {                                                                                                                 
                           logger.error("Error parsing String to double", e);     
                           return null;
                   }                                                                                                                                            
                   return result;                                                                                                                               
           }                                                                                                                                                    
                                                                                                                                                                
        public static String getDifference(String one, String two) {
		Double d1 = parseDouble(one);
		Double d2 = parseDouble(two);
		return formatNumber(d1 - d2);
	}                      
    
    public static String formatNumber(double nr) {
    	
    	
    		Double number;
    	String result;
    	if (nr == 0) {
    	    number = new Double(0d);
    	}
    	else number = new Double(nr);

    	
    	DecimalFormat formater = getDecimalFormat();
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
   
   public static String formatNumberUsingCustomFormat(double number){
	   DecimalFormat formater = null;     
	   String result;  
	   if (tlocal.get()!=null){
		   formater=tlocal.get();
		   result = formater.format(number);  
		   return result;
	   }else{
		   return formatNumber(number);
	   }
  }
   
   /**
    * Return a string based on Global Setting Number Format, not rounded. 
    * The number has the symbols decimal and group separator, but shows all decimal digits
    * @param number
    * @return 
    */
   public static String formatNumberNotRounded(Double number) {                                                                                                   
       String result;                                                                                                                               
       if (number == null) {                                                                                                                        
               number = new Double(0d);                                                                                                             
       }                                                                                                                                                             
	   DecimalFormat formater = getDecimalFormatNotRounded();                                                     
	   result = formater.format(number);                                                                                                            
       return result;                                                                                                                               
    }   
   
   public static DecimalFormat getDecimalFormatNotRounded(){
       String decimalSeparator = ".";                                                                                                               
       String groupSeparator = ",";                                                                                                                 
                                                                                                                                                    
       decimalSeparator = FeaturesUtil                                                                                                              
                      .getGlobalSettingValue(GlobalSettingsConstants.DECIMAL_SEPARATOR);                                                           
       groupSeparator = FeaturesUtil                                                                                                                
                       .getGlobalSettingValue(GlobalSettingsConstants.GROUP_SEPARATOR);                                                             
                                                                                                                                                                                                                                                                                               
                                                                                                                                                   
       DecimalFormatSymbols decSymbols = new DecimalFormatSymbols();                                                                                
       decSymbols.setDecimalSeparator(decimalSeparator.charAt(0));                                                                                  
       decSymbols.setGroupingSeparator(groupSeparator.charAt(0));                                                                                   
       DecimalFormat formater = new DecimalFormat("", decSymbols);    	
       return formater;
	   
   }
   
   public static DecimalFormat getDecimalFormat(){
       return getDefaultFormat();
   }
                       
    
    
   
    public static DecimalFormat getDefaultFormat() {

	String format = "###,###,###,###.##";
	String decimalSeparator = ".";
	String groupSeparator = ",";

	// get setting from global setting
	format = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.NUMBER_FORMAT);
	decimalSeparator = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.DECIMAL_SEPARATOR);
	groupSeparator = FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.GROUP_SEPARATOR);

	if("true".equals(FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.AMOUNTS_IN_THOUSANDS))) {		
		//use the decimal separator to learn how many decimals we have:
		int groupPlace = format.indexOf(groupSeparator);
		if(groupPlace==-1) {
			//no decimal places, we don't allow that when thousands=on, we add three
			format+=".000";
		} else {
		    	int flength=format.length();
			for(int i=1;i<=(flength-groupPlace-2);i++)
			    format+=format.charAt(flength - 1);
		}
	}
	
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

   public static GregorianCalendar parseDate(String sDate) throws Exception{
	if (sDate==null) return null;
       String defaultFormat= FeaturesUtil.getGlobalSettingValue(GlobalSettingsConstants.DEFAULT_DATE_FORMAT);
	  SimpleDateFormat formater=new SimpleDateFormat(defaultFormat);
	  GregorianCalendar result=new GregorianCalendar();
	try {
	    result.setTime(formater.parse(sDate));
	} catch (ParseException e) {
	    // TODO Auto-generated catch block
	   throw new Exception("The source string has not a format according to globbal setting "+defaultFormat,e );
	}
	  return result;
   }
   
   public static String formatDate(Date date) {
	if(date==null) return null;
	String defaultFormat = FeaturesUtil
		.getGlobalSettingValue(GlobalSettingsConstants.DEFAULT_DATE_FORMAT);
	SimpleDateFormat formater = new SimpleDateFormat(defaultFormat);	
	return formater.format(date);
    }
   
}
