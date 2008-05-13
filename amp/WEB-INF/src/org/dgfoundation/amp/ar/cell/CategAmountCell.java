/**
 * CategAmountCell.java
 * (c) 2005 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 */
package org.dgfoundation.amp.ar.cell;

import java.util.HashSet;
import java.util.Set;

import org.dgfoundation.amp.ar.ArConstants;
import org.dgfoundation.amp.ar.Categorizable;
import org.dgfoundation.amp.ar.CellColumn;
import org.dgfoundation.amp.ar.MetaInfo;
import org.dgfoundation.amp.ar.workers.CategAmountColWorker;

/**
 * 
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since May 30, 2006 AmountCell that also holds metadata
 */
public class CategAmountCell extends AmountCell implements Categorizable {

	protected Set metaData;

	@Override
	public Cell merge(Cell c) {
		AmountCell ret=(AmountCell) super.merge(c);
		CategAmountCell realRet=new CategAmountCell(ret.getOwnerId());
		realRet.getMergedCells().addAll(ret.getMergedCells());
		CategAmountCell categ=(CategAmountCell) c;
		realRet.getMetaData().addAll(categ.getMetaData());
		return realRet;
	}	
	@Override
    public void merge(Cell c1, Cell c2) {
		super.merge(c1, c2);
		CategAmountCell categ1=(CategAmountCell) c1;
		CategAmountCell categ2=(CategAmountCell) c2;
		categ1.getMetaData().addAll(categ2.getMetaData());
	}
	
	/**
	 * this item is a customized show only for cummulative amounts
	 */
	protected boolean cummulativeShow;
	
	/**
	 * @return Returns the cummulativeShow.
	 */
	public boolean isCummulativeShow() {
		return cummulativeShow;
	}

	public Cell newInstance() {
		return new CategAmountCell();
	}
	
	/**
	 * @param cummulativeShow The cummulativeShow to set.
	 */
	public void setCummulativeShow(boolean cummulativeShow) {
		this.cummulativeShow = cummulativeShow;
	}

	/**
	 * @return Returns the metaData.
	 */
	public Set getMetaData() {
		return metaData;
	}

	/**
	 * @param metaData
	 *            The metaData to set.
	 */
	public void setMetaData(Set metaData) {
		this.metaData = metaData;
	}

	public String getMetaValueString(String category) {
		MetaInfo mi = MetaInfo.getMetaInfo(metaData,category);
		if (mi == null)
			return null;
		return  mi.getValue().toString();
	}

	
	public boolean existsMetaString(String category) {
		MetaInfo mi = MetaInfo.getMetaInfo(metaData,category);
		if(mi!=null) return true;
		return false;
		
	}


	public CategAmountCell() {
		super();
		metaData = new HashSet();
	}

	public Class getWorker() {
		return CategAmountColWorker.class;
	}

	/**
	 * @param ownerId
	 * @param name
	 * @param value
	 */
	public CategAmountCell(Long id) {
		super(id);
		metaData = new HashSet();
	}

	public void setValue(Object o) {
		this.amount = ((Double) o).doubleValue();
	}

	public Object getValue() {
		return new Double(amount);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dgfoundation.amp.ar.Viewable#getViewArray()
	 */
	protected MetaInfo[] getViewArray() {
		// TODO Auto-generated method stub
		return null;
	}

	
public void applyMetaFilter(String columnName,String metaName,Cell metaCell,CategAmountCell ret) {
	if(metaCell.getColumn().getName().equals(columnName)) {
		//we need to get the location percentage, it is stored in the MetaText of related to the owner of the current cell
		CellColumn c=(CellColumn) metaCell.getColumn();
		MetaTextCell relatedLocation=(MetaTextCell) c.getByOwnerAndValue(this.getOwnerId(),metaCell.getValue());
		if(relatedLocation!=null) { 
		MetaInfo percentMeta=MetaInfo.getMetaInfo(relatedLocation.getMetaData(),metaName);
		if(percentMeta!=null) {
			Double percentage=(Double) percentMeta.getValue() ;
			ret.setPercentage(percentage.doubleValue());			
		}
		}
	}
	
}
	
	
public Cell filter(Cell metaCell,Set ids) {
    	CategAmountCell ret = (CategAmountCell) super.filter(metaCell,ids);    
		if(ret==null) return null;
		if(metaCell.getColumn().getName().equals(ArConstants.DONOR)) 
				if(!metaCell.getValue().toString().equals(ret.getMetaValueString(ArConstants.DONOR)))
		return null;
		
		if(metaCell.getColumn().getName().equals(ArConstants.DONOR_GROUP)) 
			if(!metaCell.getValue().toString().equals(ret.getMetaValueString(ArConstants.DONOR_GROUP)))
		return null;
		
			
		if(metaCell.getColumn().getName().equals(ArConstants.DONOR_TYPE_COL)) 
			if(!metaCell.getValue().toString().equals(ret.getMetaValueString(ArConstants.DONOR_TYPE_COL)))
		return null;
	
		if(metaCell.getColumn().getName().equals(ArConstants.REGION) &&
				this.getNearestReportData().getReportMetadata().getType().equals(ArConstants.REGIONAL_TYPE)) 
				if(!metaCell.getValue().toString().equals(ret.getMetaValueString(ArConstants.REGION)))
		return null;
		

		if(metaCell.getColumn().getName().equals(ArConstants.TERMS_OF_ASSISTANCE)) 
				if(!metaCell.getValue().toString().equals(ret.getMetaValueString(ArConstants.TERMS_OF_ASSISTANCE)))
		return null;
		
		if(metaCell.getColumn().getName().equals(ArConstants.FINANCING_INSTRUMENT)) 
			if(!metaCell.getValue().toString().equals(ret.getMetaValueString(ArConstants.FINANCING_INSTRUMENT)))
		return null;
	
		

		if(metaCell.getColumn().getName().equals(ArConstants.COMPONENT)) 
			if(!metaCell.getValue().toString().equals(ret.getMetaValueString(ArConstants.COMPONENT)))
		return null;
		
		
		//apply metatext filters
		if(metaCell instanceof MetaTextCell) {
			//apply metatext filters for column Sector
		 applyMetaFilter("Sector", ArConstants.SECTOR_PERCENTAGE, metaCell, ret);
		 applyMetaFilter("Executing Agency", ArConstants.EXECUTING_AGENCY_PERCENTAGE, metaCell, ret);
		 applyMetaFilter("Sub-Sector", ArConstants.SECTOR_PERCENTAGE, metaCell, ret);
		 applyMetaFilter("Region", ArConstants.LOCATION_PERCENTAGE, metaCell, ret);
		 applyMetaFilter("Componente", ArConstants.COMPONENTE_PERCENTAGE, metaCell, ret);
		 applyMetaFilter("National Planning Objectives", ArConstants.NPO_PERCENTAGE, metaCell, ret);
		 applyMetaFilter("Primary Program", ArConstants.PROGRAM_PERCENTAGE, metaCell, ret);
		 applyMetaFilter("Secondary Program", ArConstants.PROGRAM_PERCENTAGE, metaCell, ret);
		}
		
		//if(ret.getMergedCells().size()>0) 
			//logger.info(ret.getMergedCells());
		return ret;
	}	/*
		 * (non-Javadoc)
		 * 
		 * @see org.dgfoundation.amp.ar.Categorizable#hasMeta(org.dgfoundation.amp.ar.MetaInfo)
		 */
	public boolean hasMetaInfo(MetaInfo m) {
		MetaInfo internal = MetaInfo.getMetaInfo(metaData,m.getCategory());
		if (internal == null)
			return false;
		if (internal.equals(m))
			return true;
		else
			return false;
	}
}
