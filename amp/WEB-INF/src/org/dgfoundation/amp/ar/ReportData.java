/**
 * ReportData.java
 * (c) 2005 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.dgfoundation.amp.ar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.dgfoundation.amp.ar.cell.Cell;
import org.dgfoundation.amp.ar.exception.IncompatibleColumnException;
import org.dgfoundation.amp.ar.exception.UnidentifiedItemException;
import org.digijava.module.aim.dbentity.AmpReports;

/**
 * 
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Jun 23, 2006
 *
 */
public abstract class ReportData extends Viewable {
	
	
	protected static Logger logger = Logger.getLogger(ReportData.class);
	
	protected Boolean globalHeadingsDisplayed;
	
	protected String name;
	
	protected List trailCells;

	protected List items;
	
	protected String sortByColumn;
	protected boolean sortAscending;
	
	protected GroupReportData parent;
	
	protected AmpReports reportMetadata;
	
	public abstract Collection getOwnerIds();

	public abstract Integer getSourceColsCount();
	
	public abstract String getSorterColumn();
	
	public abstract boolean getSortAscending();
	
	public abstract void removeColumnsByName(String name);

	public abstract String getAbsoluteReportName();
	
	public abstract void applyLevelSorter();
	
	public abstract void removeEmptyChildren();
	
	public String getNameTrn(){
		return this.name.toLowerCase().replaceAll(" ", "");
	}
	
	public String getColumnId(){
		if (this.name.indexOf(':') < 0)
			return this.name;
		
		String id = this.name.substring(0, this.name.indexOf(':'));
		return id;
	}
	
	public String getRepName(){
		if (this.name.indexOf(':') < 0)
			return "";
		
		String id = this.name.substring(this.name.indexOf(':') + 1, name.length());
		return id;
	}
	
	public String getColumnIdTrn(){
		if (this.name.indexOf(':') < 0)
			return this.name;
		String id = this.name.substring(0, this.name.indexOf(':'));
		return id.toLowerCase().replaceAll(" ",	"");
	}
	
	public String getRepNameTrn(){
		if (this.name.indexOf(':') < 0)
			return "";
		String id = this.name.substring(this.name.indexOf(':') + 1, name.length());
		return id.toLowerCase().replaceAll(" ",	"");
	}
	
	public int getTotalUniqueRows() {
		return getOwnerIds().size();
	}
	
	/**
	 * @return Returns the parent.
	 */
	public GroupReportData getParent() {
		return parent;
	}

	/**
	 * @param parent The parent to set.
	 */
	public void setParent(GroupReportData parent) {
		this.parent = parent;
	}

	/**
	 * @return Returns the items.
	 */
	public List getItems() {
		return items;
	}

	public Iterator iterator() {
		return items.iterator();
	}
		
	public Object getItem(int idx) {
		return items.get(idx);
	}
	
	/**
	 * Hierarchy generator. This method splits horizontally a report into subReports,
	 * based on categories (hierarchies). Descendants will support thgetSortByColumnis for any type
	 * of ReportData object (nested or plain). 
	 * @param columnName
	 * @return
	 * @throws UnidentifiedItemException
	 * @throws IncompatibleColumnException
	 */
	public abstract GroupReportData horizSplitByCateg(String columnName)  throws UnidentifiedItemException,IncompatibleColumnException;
	
	/**
	 * Performs report data post processing. These are several customized processing tasks performed after the
	 * main structure is already defined and populated.
	 *
	 */
	public abstract void postProcess();
		
	public ReportData(String name) {
		this.name=name;
		items=new ArrayList();
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

		
	public List getTrailCells() {
		return trailCells;
	}
	
	public List getTrailRow() {
		ArrayList ret=new ArrayList();
		Iterator i=items.iterator();
		while (i.hasNext()) {
			Column element = (Column) i.next();
			ret.addAll(element.getTrailCells());
		}
		return ret;
	}
	
	public abstract int getTotalDepth();
	
	public abstract int getLevelDepth();

	/**
	 * @return Returns the reportMetadata.
	 */
	public AmpReports getReportMetadata() {
		if(reportMetadata==null) return parent.getReportMetadata();else
			return reportMetadata;
	}

	/**
	 * @param reportMetadata The reportMetadata to set.
	 */
	public void setReportMetadata(AmpReports reportMetadata) {
		this.reportMetadata = reportMetadata;
	}

	/**
	 * @return Returns the globalHeadingsDisplayed.
	 */
	public Boolean getGlobalHeadingsDisplayed() {
		if(this.getParent()!=null) return this.getParent().getGlobalHeadingsDisplayed();
		return this.globalHeadingsDisplayed;
	}

	/**
	 * @param globalHeadingsDisplayed The globalHeadingsDisplayed to set.
	 */
	public void setGlobalHeadingsDisplayed(Boolean globalHeadingsDisplayed) {
		if(this.getParent()!=null) this.getParent().setGlobalHeadingsDisplayed(globalHeadingsDisplayed);
		else
		this.globalHeadingsDisplayed=globalHeadingsDisplayed;
	}


	/**
	 * Searches the trailCells list for a Cell belonging to the column name specified as parameter
	 * @param columnName the column name to which the cell belongs
	 * @return the cell or null if not found
	 */
	public Cell findTrailCell(String columnName) {
		Iterator i=this.getTrailCells().iterator();
		while (i.hasNext()) {
			Cell element = (Cell) i.next();
			if(columnName.equals(element.getColumn().getAbsoluteColumnName())) return element; 
		}
		logger.error(this.getName()+":Could not find appropriate trail cell for column name "+columnName);
		return null;
	}
	
	public String getLevelBkgColor()
	{
		int v=this.getLevelDepth();
		//if (v==1) return "#BBEEFF";
		//if (v==2) return "#FFAAAA";
		//if (v==3) return "#BBFFBB";
		if (v==3) return "#8FBCFF";
		return "ffffff";
	}
	
	
	
}