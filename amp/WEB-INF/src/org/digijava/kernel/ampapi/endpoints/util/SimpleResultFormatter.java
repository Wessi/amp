package org.digijava.kernel.ampapi.endpoints.util;

import java.util.Iterator;
import java.util.LinkedHashMap;

import org.dgfoundation.amp.newreports.GeneratedReport;
import org.dgfoundation.amp.newreports.ReportAreaImpl;
import org.dgfoundation.amp.newreports.ReportCell;
import org.dgfoundation.amp.newreports.ReportOutputColumn;

/**
 * 
 * @author Diego Dimunzio
 * 
 */

public class SimpleResultFormatter {
	/**
	 * 
	 * @param report
	 * @return
	 * Return a simple list of two values Name and amount
	 * { 
	 * 	"R07 - Norte de olancho": 28821911.895,
	 * 	"R09 - Biosfera Río Plátano": 20643011.721,
       }
	 */
	public static JsonBean ResultFormatter(GeneratedReport report){
		JsonBean retlist = new JsonBean();
		//ArrayList<ReportArea> reportchildrens =  (ArrayList<ReportArea>) report.reportContents.getChildren();
		for (Iterator iterator = report.reportContents.getChildren().iterator(); iterator.hasNext();) {
			ReportAreaImpl reportArea =  (ReportAreaImpl) iterator.next();
			LinkedHashMap<ReportOutputColumn, ReportCell> content = (LinkedHashMap<ReportOutputColumn, ReportCell>) reportArea.getContents();
			org.dgfoundation.amp.newreports.TextCell reportcolumn = (org.dgfoundation.amp.newreports.TextCell) content.values().toArray()[0];
			ReportCell reportcell = (ReportCell) content.values().toArray()[1];
			if(retlist.getSize()<5){
				retlist.set(reportcolumn.displayedValue,reportcell.value);
			}else{
				break;
			}
		}
		
		return retlist;
	}
}
