/**
 * ColumnReportData.java
 * (c) 2005 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.dgfoundation.amp.ar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.dgfoundation.amp.ar.cell.Cell;
import org.dgfoundation.amp.ar.exception.IncompatibleColumnException;
import org.dgfoundation.amp.ar.exception.UnidentifiedItemException;

/**
 * 
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * @since Jun 28, 2006
 * 
 */
public class ColumnReportData extends ReportData {	
	/**
	 * @param name
	 */
	public ColumnReportData(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	public void addColumn(Column col) {
		items.add(col);
		col.setParent(this);
		
	}
	
	public void addColumns(Collection col) {
		Iterator i=col.iterator();
		while (i.hasNext()) {
			Column element = (Column) i.next();
			addColumn(element);
		}
		
	}
	

	public Column getColumn(Object columnId) {
		Iterator i = items.iterator();
		while (i.hasNext()) {
			Column element = (Column) i.next();
			if (element.getColumnId().equals(columnId))
				return element;
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dgfoundation.amp.ar.ReportData#categorizeBy(org.dgfoundation.amp.ar.cell.Cell)
	 */
	public GroupReportData horizSplitByCateg(String columnName)
			throws UnidentifiedItemException,IncompatibleColumnException {
		GroupReportData dest = new GroupReportData(this.getName());

		// create set with unique values for the filtered col:
		Column keyCol = getColumn(columnName);
		
		removeColumn(columnName);
		
		if(keyCol instanceof GroupColumn) 
			throw new IncompatibleColumnException("GroupColumnS cannot be used as filter keys!");
		if (keyCol == null)
			throw new UnidentifiedItemException(
					"Cannot found a Column with Id " + columnName
							+ " in this ReportData");
		TreeSet cats=new TreeSet();
		Iterator i=keyCol.iterator();
		while (i.hasNext()) {
			Cell element = (Cell) i.next();
			//TODO: i do not like this but i have no choice !
		//		if(element instanceof ListCell) cats.addAll((Collection)
//					element.getValue());
	//		else 
				cats.add(element);
		}
		
		
		//we iterate each category from the set and search for matching rows
		i = cats.iterator();
		while (i.hasNext()) {
			Cell cat = (Cell) i.next();
			ColumnReportData crd=new ColumnReportData((String) cat.getColumnId()+": "+cat.toString());
			dest.addReport(crd);
					
			// construct the Set of ids that match the filter:
			Set ids = new TreeSet();
			//TODO: we do not allow GroupColumnS for keyColumns
			Iterator ii=keyCol.iterator();
			while (ii.hasNext()) {
				Cell element = (Cell) ii.next();
				if(element.compareTo(cat)==0) ids.add(element.getOwnerId());
			}

			//now we get each column and get the dest column by applying the filter
			ii=this.getItems().iterator();
			while (ii.hasNext()) {
				Column col = (Column) ii.next();
				crd.addColumn(col.filterCopy(cat,ids));
			}
			
		}
		
		return dest;
	}
	
	public void replaceColumn(String name, Column column) {
		int idx = items.indexOf(getColumn(name));
		items.remove(idx);
		items.add(idx, column);
		column.setParent(this);
	}


	/* (non-Javadoc)
	 * @see org.dgfoundation.amp.ar.ReportData#postProcess()
	 */
	public void postProcess() {
		Iterator i=items.iterator();
		List destCols=new ArrayList();
		while (i.hasNext()) {
			Column element = (Column) i.next();
 			Column res=element.postProcess();
			destCols.add(res);
		}
		
		items=destCols;
		
		prepareAspect();
		
		//create trail cells...
		trailCells=new ArrayList();
		i=items.iterator();
		while (i.hasNext()) {
			Column element = (Column) i.next();
			List l=element.getTrailCells();
			if (l!=null) trailCells.addAll(l);
		}
	}

	public Set getOwnerIds() {
		Set ret=new TreeSet();
		Iterator i=items.iterator();
		while (i.hasNext()) {
			Column element = (Column) i.next();
			ret.addAll(element.getOwnerIds());
		}
		return ret;
	}
	

	/* (non-Javadoc)
	 * @see org.dgfoundation.amp.ar.Viewable#getCurrentView()
	 */
	public String getCurrentView() {
		return parent.getCurrentView();
	}

	
	public int getMaxColumnDepth() {
		Iterator i=items.iterator();
		int ret=0;
		while (i.hasNext()) {
			Column element = (Column) i.next();
			int c=element.getColumnSpan();
			if(c>ret) ret=c;
		}
		return ret;
	}
	
	/* (non-Javadoc)
	 * @see org.dgfoundation.amp.ar.ReportData#getTotalDepth()
	 */
	public int getTotalDepth() {
		Iterator i=items.iterator();
		int ret=0;
		while (i.hasNext()) {
			Column element = (Column) i.next();
			ret+=element.getColumnDepth();
		}
		return ret;
	}

	public List getColumnsByDepth() {
		List ret=new ArrayList();
		
		
		
		return ret;
	}


	public void prepareAspect() {
		int maxDepth=getMaxColumnDepth();
		Iterator i=items.iterator();
		while (i.hasNext()) {
			Column element = (Column) i.next();
			element.setRowSpan(maxDepth+1);
		}
	}

	public void removeColumn(String name) {
		Iterator i=items.iterator();
		while (i.hasNext()) {
			Column element = (Column) i.next();
			if(element.getName().equals(name)) {i.remove();return;}
		}
	}

	/* (non-Javadoc)
	 * @see org.dgfoundation.amp.ar.ReportData#getSourceColsCount()
	 */
	public Integer getSourceColsCount() {
		return parent.getSourceColsCount();
	}

}
