/**
 * GroupReportData.java
 * (c) 2005 Development Gateway Foundation
 * @author Mihai Postelnicu - mpostelnicu@dgfoundation.org
 * 
 */
package org.dgfoundation.amp.ar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
public class GroupReportData extends ReportData {

    
    	
	@Override
	public int getVisibleRows() {
    	    Iterator i=items.iterator();
    	    int ret=0;
    	if(this.getReportMetadata().getHideActivities()!=null && this.getReportMetadata().getHideActivities())
			return ret;
		
    	    while (i.hasNext()) {
				ReportData element = (ReportData) i.next();
				ret+=element.getVisibleRows();
    	    }
    	    return ret;
	}
    


		 	
	/**
	 * GroupReportData comparator class. This class implements reportData comparison. 
	 * Comparison is based on the level of this ReportData and the chosen sorter for that particular level
	 * @author mihai
	 * @see GroupReportData.getThisLevelSorter()
	 */
	public class GroupReportDataComparator implements Comparator {
		public final int compare (Object o1, Object o2) {
			Comparator c=new Cell.CellComparator();
			String sorterName=getThisLevelSorter().getCategory();
			String sorterType=(String) getThisLevelSorter().getValue();
			boolean ascending="ascending".equals(sorterType)?true:false;
			ReportData c1=(ReportData) o1;
			ReportData c2=(ReportData) o2;
			if(ArConstants.HIERARCHY_SORTER_TITLE.equals(sorterName))
					return ascending?c1.getName().compareTo(c2.getName()):c2.getName().compareTo(c1.getName());
			else return ascending?c.compare(c1.findTrailCell(sorterName),c2.findTrailCell(sorterName)):c.compare(c2.findTrailCell(sorterName),c1.findTrailCell(sorterName));
		}
	}
	
	protected List levelSorters;
		
	protected String currentView;
	
	public void applyLevelSorter() {
		if(getThisLevelSorter()!=null)
		Collections.sort(items,new GroupReportDataComparator());
		Iterator i=items.iterator();
		while (i.hasNext()) {
			ReportData element = (ReportData) i.next();
			element.applyLevelSorter();
		}
	}
	
	protected Integer sourceColsCount;

	public MetaInfo getThisLevelSorter() {
		int myDepth=getLevelDepth()-1;
		if(myDepth<0 || getLevelSorters().size()<=myDepth) return null;
		return (MetaInfo) getLevelSorters().get(myDepth);
	}
	
	public GroupReportData(GroupReportData d) {
		super(d.getName());
		this.parent = d.getParent();
		this.reportMetadata=d.getReportMetadata();
		this.sourceColsCount = d.getSourceColsCount();
		this.globalHeadingsDisplayed=new Boolean(false);
		this.columnsToBeRemoved=d.getColumnsToBeRemoved();
	}

	/**
	 * @param sourceColsCount
	 *            The sourceColsCount to set.
	 */
	public void setSourceColsCount(Integer sourceColsNumber) {
		this.sourceColsCount = sourceColsNumber;
	}

	public void addReport(ReportData rd) {
		items.add(rd);
		rd.setParent(this);
	}

	/**
	 * @return Returns the currentView.
	 */
	public String getCurrentView() {
		if (parent == null)
			return currentView;
		else
			return parent.getCurrentView();
	}

	/**
	 * @param currentView
	 *            The currentView to set.
	 */
	public void setCurrentView(String currentView) {
		this.currentView = currentView;
	}

	public GroupReportData(String name) {
		super(name);
		this.globalHeadingsDisplayed=new Boolean(false);
		// TODO Auto-generated constructor stub
	}

	public ReportData getReport(String name) {
		Iterator i = items.iterator();
		while (i.hasNext()) {
			ReportData element = (ReportData) i.next();
			if (element.getName().equals(name))
				return element;
		}
		return null;
	}


	public GroupReportData horizSplitByCateg(String columnName)
			throws UnidentifiedItemException, IncompatibleColumnException {
		GroupReportData dest = new GroupReportData(this);
		Iterator i = items.iterator();
		while (i.hasNext()) {
			ReportData element = (ReportData) i.next();
			
			ReportData result= element.horizSplitByCateg(columnName);
			if(result.getItems().size()!=0)
			    dest.addReport(result);
			else dest.addReport(element);
		}
		return dest;
	}

	public void postProcess() {
		Iterator i = items.iterator();
		while (i.hasNext()) {
			ReportData element = (ReportData) i.next();
			element.postProcess();
		}
		
	
		// create trail cells
		try {

			trailCells = new ArrayList();
			if (items.size() > 0) {
				ReportData firstRd = (ReportData) items.iterator().next();
				for (int k = 0; k < firstRd.getTrailCells().size(); k++) {
					Cell c=(Cell) firstRd.getTrailCells().get(k);
					if (c!=null){
					Cell newc=c.newInstance();
					newc.setColumn(c.getColumn());
					trailCells.add(newc);
					}else{
						trailCells.add(null);
					}
				}
					
				logger.debug("GroupTrail.size=" + trailCells.size());

				i = items.iterator();
				while (i.hasNext()) {
					ReportData element = (ReportData) i.next();
					if (element.getTrailCells().size() < trailCells.size()) {
						logger
								.error("INVALID Report TrailCells size for report: "
										+ element.getParent().getName()
										+ "->"
										+ element.getName());
						logger.error("ReportTrail.getTrailCells().size()="
								+ element.getTrailCells().size());
					} else
						for (int j = 0; j < trailCells.size(); j++) {
							Cell newc =null;
							
							Cell c = (Cell) trailCells.get(j);
							Cell c2 = (Cell) element.getTrailCells().get(j);
							if (c!=null){
								newc = c.merge(c2);
								newc.setColumn(c2.getColumn());
							}
							trailCells.remove(j);
							trailCells.add(j, newc);
						}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dgfoundation.amp.ar.ReportData#getTotalDepth()
	 */
	public int getTotalDepth() {
		if (items.size() == 0)
			return -1;
		ReportData rd = (ReportData) items.get(0);
		return rd.getTotalDepth();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dgfoundation.amp.ar.ReportData#getSourceColsCount()
	 */
	public Integer getSourceColsCount() {
		if (parent == null)
			return sourceColsCount;
		else
			return parent.getSourceColsCount();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dgfoundation.amp.ar.ReportData#getOwnerIds()
	 */
	public Collection getOwnerIds() {
		Set ret = new TreeSet();
		Iterator i = items.iterator();
		while (i.hasNext()) {
			ReportData element = (ReportData) i.next();
			ret.addAll(element.getOwnerIds());
		}
		return ret;
	}

	public String getSorterColumn() {
		if (parent == null)
			return sortByColumn;
		else
			return parent.getSorterColumn();
	}

	public boolean getSortAscending() {
		if (parent == null)
			return sortAscending;
		else
			return parent.getSortAscending();
	}

	
	public void setSorterColumn(String sortByColumn) {
		if(sortByColumn.equals(this.sortByColumn)) sortAscending= !sortAscending; else sortAscending=true; 
		this.sortByColumn=sortByColumn;
	}

	
	/**
	 * @return the first column report found in this tree
	 */
	public ColumnReportData getFirstColumnReport() {
		if(items.size()==0) return null;
		ReportData rd=(ReportData) items.get(0);
		if (rd instanceof GroupReportData) return ((GroupReportData)rd).getFirstColumnReport();
		return (ColumnReportData) rd;
	}

	public void removeColumnsByName(String name) {
		Iterator i=items.iterator();
		while (i.hasNext()) {
			ReportData element = (ReportData) i.next();
			element.removeColumnsByName(name);
		}
	}

	public String getAbsoluteReportName() {
		if (parent!=null) return parent.getAbsoluteReportName()+"--"+ this.name;
		else return this.name;
	}

	public int getLevelDepth() {
		if(parent==null) return 0; else return 1+parent.getLevelDepth();
	}

	public List getLevelSorters() {
		if(parent==null) return levelSorters; else return parent.getLevelSorters();
	}

	public void importLevelSorters(Map sorterMap, int levels) {
		levelSorters=new ArrayList(levels);
		for(int k=0;k<levels;k++) levelSorters.add(null);
		Iterator i=sorterMap.keySet().iterator();
		while (i.hasNext()) {
			Long element;
			Object obj=i.next();
			if (obj instanceof String) {
				String src = (String) i.next();
				element=Long.parseLong(src);
			}else{
				element=(Long) obj;
			}
			
			if(element==null || element -1>=levels) i.remove(); else
			levelSorters.set(element.intValue() -1,sorterMap.get(element));
			
		}
	}

	public void removeEmptyChildren() {
		Iterator i=items.iterator();
		while (i.hasNext()) {
			ReportData element = (ReportData) i.next();
			if(element.getItems().size()==0) i.remove(); else element.removeEmptyChildren();
		}
	}


	
}
