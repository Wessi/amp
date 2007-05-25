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

public Cell filter(Cell metaCell,Set ids) {
		CategAmountCell ret=(CategAmountCell) super.filter(metaCell,ids);
		if(ret==null) return null;
		if(metaCell.getColumn().getName().equals(ArConstants.DONOR)) {
				if(!metaCell.getValue().toString().equals(ret.getMetaValueString(ArConstants.DONOR)))
			return null;
		}
		if(metaCell.getColumn().getName().equals(ArConstants.REGION)) {
				if(!metaCell.getValue().toString().equals(ret.getMetaValueString(ArConstants.REGION)))
			return null;
		}

		if(metaCell.getColumn().getName().equals("Type Of Assistance")) {
				if(!metaCell.getValue().toString().equals(ret.getMetaValueString(ArConstants.TERMS_OF_ASSISTANCE)))
			return null;
		}

		if(metaCell.getColumn().getName().equals("Component Name")) {
			if(!metaCell.getValue().toString().equals(ret.getMetaValueString("Component")))
		return null;
		}
		
		//apply metatext filters
		if(metaCell instanceof MetaTextCell) {
			//apply metatext filters for column Sector
			if(metaCell.getColumn().getName().equals("Sector")) {
				//we need to get the sector percentage, it is stored in the MetaText of related to the owner of the current cell
				CellColumn c=(CellColumn) metaCell.getColumn();
				MetaTextCell relatedSector=(MetaTextCell) c.getByOwnerAndValue(this.getOwnerId(),metaCell.getValue());
				if(relatedSector!=null) { 
				MetaInfo percentMeta=MetaInfo.getMetaInfo(relatedSector.getMetaData(),ArConstants.SECTOR_PERCENTAGE);
				Integer percentage=(Integer) percentMeta.getValue();
				ret.setPercentage(percentage.intValue());			
				}
			}
		}
		
		if(ret.getMergedCells().size()>0) 
			logger.info(ret.getMergedCells());
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
		if (internal.compareTo(m) == 0)
			return true;
		else
			return false;
	}
}
