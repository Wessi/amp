/**
 * 
 */
package org.dgfoundation.amp.ar.workers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.dgfoundation.amp.ar.ARUtil;
import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.MetaInfo;
import org.dgfoundation.amp.ar.ReportGenerator;
import org.dgfoundation.amp.ar.cell.Cell;
import org.dgfoundation.amp.ar.cell.MetaTextCell;
import org.dgfoundation.amp.ar.cell.TextCell;

/**
 * @author mihai
 *
 */
public class MetaTextColWorker extends TextColWorker {

	/**
	 * @param condition
	 * @param viewName
	 * @param columnName
	 * @param generator
	 */
	public MetaTextColWorker(String condition, String viewName,
			String columnName, ReportGenerator generator) {
		super(condition, viewName, columnName, generator);
		// TODO Auto-generated constructor stub
	}
	
	protected Cell getCellFromRow(ResultSet rs) throws SQLException {
		TextCell tc=(TextCell) super.getCellFromRow(rs);
		MetaTextCell mtc=new MetaTextCell(tc);
		
		if(columnName.equals("Project Title")) 
			{
				mtc.getMetaData().add(new MetaInfo(ArConstants.DRAFT,rs.getBoolean(4)));
				mtc.getMetaData().add(new MetaInfo(ArConstants.STATUS,rs.getString(5)));
			}else
		if(columnName.equals("National Planning Objectives")) 
			mtc.getMetaData().add(new MetaInfo(ArConstants.PERCENTAGE,rs.getDouble(4))); else
		if(columnName.contains(ArConstants.COLUMN_ANY_SECTOR)) 
			mtc.getMetaData().add(new MetaInfo(ArConstants.PERCENTAGE,rs.getDouble(4))); else
			
		if(columnName.equals("Executing Agency")){ 
			double percentage = rs.getDouble(4);
			if(!rs.wasNull()){
				mtc.getMetaData().add(new MetaInfo(ArConstants.PERCENTAGE,percentage));	
			}
		} else
				
		if(columnName.equals("Region") && (generator.getReportMetadata().getType()==ArConstants.DONOR_TYPE || generator.getReportMetadata().getType()==ArConstants.COMPONENT_TYPE))
			mtc.getMetaData().add(new MetaInfo(ArConstants.PERCENTAGE,rs.getDouble(4))); else
//		if(columnName.equals("Componente") && generator.getReportMetadata().getType()==ArConstants.DONOR_TYPE)
		if(columnName.equals("Componente"))
			mtc.getMetaData().add(new MetaInfo(ArConstants.PERCENTAGE,rs.getDouble(4))); 
		//
		if((columnName.equals("Primary Program") || columnName.equals("Secondary Program"))) 
			mtc.getMetaData().add(new MetaInfo(ArConstants.PERCENTAGE,rs.getDouble(4))); 
		//if(mtc.getMetaData())
		MetaInfo percentageMeta = MetaInfo.getMetaInfo(mtc.getMetaData(), ArConstants.PERCENTAGE);
		if(		percentageMeta!=null && 
				((Double)percentageMeta.getValue()).doubleValue()==0 && 
				columnName.equals(ArConstants.COLUMN_REGION) && 
				ARUtil.hasHierarchy(generator.getReportMetadata().getHierarchies(),ArConstants.COLUMN_REGION )) 
					mtc.setValue(ArConstants.UNALLOCATED);
		return mtc;
	}

}